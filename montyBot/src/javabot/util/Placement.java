package javabot.util;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

import javabot.JNIBWAPI;
import javabot.model.BaseLocation;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;

//Azder Placement
public class Placement {
	private static final boolean PLACEMENT_DEBUG = true;
	
	private JNIBWAPI game = null;
	private Support sc = null;
	private ArrayList<ArrayList<MyPlan>> myPlan = new ArrayList<ArrayList<MyPlan>>();
	private ArrayList<Point> ConstructionSites4 = new ArrayList<Point>();
	private ArrayList<Point> ConstructionSites6 = new ArrayList<Point>();
	private ArrayList<Point> ConstructionSites12 = new ArrayList<Point>();
	private ArrayList<Point> middleTemplate = new ArrayList<Point>();
	private ArrayList<Point> baseLocation = new ArrayList<Point>();
	private ArrayList<Wall> walls = new ArrayList<Wall>();
	private ArrayList<Point> usePointWalls =  new ArrayList<Point>();
	private int homeX, homeY;
	private Point home = null;
	private Polygon border = new Polygon();
	private int reset = 0;
	
	private class MyPlan{
		Boolean build = false;
		Boolean lock = false;
	}
//-------------------------------------------------------------------------------------
	public Placement(JNIBWAPI game){
		this.game = game;
		sc = new Support(game);
		initializationConstructionSites();	
		initializationMyLimitedPlan();	
		setMainSablone(homeX, homeY);
		initializationBaseLocations();	
	}
	public boolean isBuildable (int buildingTypeID,  Point pos){
		if(game.getUnitType(buildingTypeID).isRefinery() || buildingTypeID == UnitTypes.Protoss_Nexus.ordinal() || buildingTypeID == UnitTypes.Protoss_Pylon.ordinal())
			return true;
		int tileWidth = game.getUnitType(buildingTypeID).getTileWidth();
		int tileHeight = game.getUnitType(buildingTypeID).getTileHeight();
		for(int i = pos.x ; i < pos.x  + tileWidth; i++)
			for(int j = pos.y ; j < pos.y + tileHeight;j++)
				if(!getBuild(i, j))
					return false;	
		return true;
	}
	public void setWall(ArrayList<Wall> walls){
		this.walls = walls;
	}
	public Point getBuildTile(int buildingTypeID, int tileX, int tileY,boolean wall) {
		myPlan();
		Point ret = new Point(-1, -1);
		addUsePointWalls();
		ret = isInWall(buildingTypeID);
		if(wall)
			return ret;
		// Refinery, Assimilator, Extractor
		if (game.getUnitType(buildingTypeID).isRefinery()) {
			Point home = new Point(homeX,homeY);
			Point near = new Point(-1,-1);
			Double dist = 9999999999999.9;
			for (Unit n : game.getNeutralUnits()) {
				if (n.getTypeID() == UnitTypes.Resource_Vespene_Geyser.ordinal()) {
					Double newDist = sc.getDistance(n, home);
					if(newDist < dist){
						dist = newDist;
						near = new Point(n.getTileX(), n.getTileY());
					}		
				}
			}
			return near;
		}
		recontrolConstructionSites();
		ret = getPoint(buildingTypeID,tileX,tileY);
		if(ret == null || ret.x == 0 )
			ret = new Point(-1,-1);
		

		
		/*
		int maxDist = 3;
		int stopDist = 40;
		while ((maxDist < stopDist) && (ret.x == -1)) {
			for (int i=tileX-maxDist; i<=tileX+maxDist; i++) {
				for (int j=tileY-maxDist; j<=tileY+maxDist; j++) {
					if (game.canBuildHere(-1, i, j, buildingTypeID, false)) {
						int tileWidth = game.getUnitType(buildingTypeID).getTileWidth();
						int tileHeight = game.getUnitType(buildingTypeID).getTileHeight();
						tileWidth++;
						tileHeight++;

						// units that are blocking the tile
						boolean unitsInWay = false;
						for (Unit u : game.getAllUnits()) {
							int tileWidthUnit = game.getUnitType(buildingTypeID).getTileWidth();
							int tileHeightUnit = game.getUnitType(buildingTypeID).getTileHeight();
							if ((Math.abs(u.getTileX()-i) < tileWidthUnit) && (Math.abs(u.getTileY()-j) <  tileHeightUnit)) unitsInWay = true;
						}
						if(getBuild(i, j) || (buildingTypeID == UnitTypes.Protoss_Pylon.ordinal() && !getBuild(i, j)))
							if (!unitsInWay && canBuildHere(i, j, tileWidth, tileHeight)) {
								ret.x = i; ret.y = j;
								//if(buildingTypeID != UnitTypes.Protoss_Pylon.ordinal()){
									//int tileWidth = game.getUnitType(buildingTypeID).getTileWidth();
									//int tileHeight = game.getUnitType(buildingTypeID).getTileHeight();
									//if(buildingTypeID == UnitTypes.Protoss_Gateway.ordinal()||buildingTypeID == UnitTypes.Protoss_Robotics_Facility.ordinal()){
										//tileWidth++;
										//tileHeight++;
									//}
									for(int k = i ; k <= i + tileWidth; k++)
										for(int l = j ; l <= j + tileHeight;l++)
											setBuild(k, l, false);
								//}
								return ret;
							}
						if (game.getUnitType(buildingTypeID).isRequiresPsi()) {}
					}
				}
			}
			maxDist += 2;
		}
		*/	
	//	if (ret.x == -1) game.printText("Unable to find suitable build position "+game.getUnitType(buildingTypeID).getName());
		return ret;
	}
//-----------------------------------------------------------------------------------------------	
	private Point getPoint(int buildingTypeID,int tileX, int tileY){
		if(buildingTypeID == UnitTypes.Protoss_Nexus.ordinal())
			return getPointNexus();
		int tileWidth = game.getUnitType(buildingTypeID).getTileWidth();
		int tileHeight = game.getUnitType(buildingTypeID).getTileHeight();
		int size =  tileWidth * tileHeight;
		getNewSize(size);
		if(tileX == -1 && tileY == -1){
			if(size == 4 && !ConstructionSites4.isEmpty()){
				return new Point(ConstructionSites4.get(0).x,ConstructionSites4.get(0).y) ;
			}else if(size == 6 && !ConstructionSites6.isEmpty()){
				return  new Point(ConstructionSites6.get(0).x,ConstructionSites6.get(0).y) ;
			}else if(size == 12 && !ConstructionSites12.isEmpty()){
				return new Point(ConstructionSites12.get(0).x,ConstructionSites12.get(0).y) ;
			}
		}else {
			if(size == 4 && !ConstructionSites4.isEmpty()){	
				return sc.getNearestPoint(tileX,tileY,ConstructionSites4);
			}else if(size == 6 && !ConstructionSites6.isEmpty()){
				return sc.getNearestPoint(tileX,tileY,ConstructionSites6);
			}else if(size == 12 && !ConstructionSites12.isEmpty()){
				return sc.getNearestPoint(tileX,tileY,ConstructionSites12);
			}	
		}
		return null;
	}
	private Point getPointNexus() {
		return new Point(baseLocation.get(0).x,baseLocation.get(0).y) ;
	}
	private void getNewSize(int size){ // if need new size to build. 
		if(size == 4 && ConstructionSites4.isEmpty() && !ConstructionSites6.isEmpty()){ 
			Point pom = ConstructionSites6.get(0);
			Point middle = sc.getNearestPoint(pom,middleTemplate);
			if(middle.x > pom.x)
				pom.x +=1;
			ConstructionSites4.add(new Point(pom.x, pom.y));
			ConstructionSites6.remove(0);
		}else if(size == 4 && ConstructionSites4.isEmpty() && !ConstructionSites12.isEmpty()){ 
			Point pom = ConstructionSites12.get(0);
			Point middle = sc.getNearestPoint(pom,middleTemplate);
			if(middle.y > pom.y)
				pom.y -=1;
			ConstructionSites4.add(new Point(pom.x, pom.y));
			ConstructionSites4.add(new Point(pom.x+2, pom.y));
			ConstructionSites4.add(new Point(pom.x, pom.y+2));
			ConstructionSites4.add(new Point(pom.x+2, pom.y+2));
			ConstructionSites12.remove(0);
		}else if(size == 6 && ConstructionSites6.isEmpty() && !ConstructionSites12.isEmpty()){ 
			Point pom = ConstructionSites12.get(0);
			Point middle = sc.getNearestPoint(pom,middleTemplate);
			if(middle.y > pom.y)
				pom.y +=1;
			if(middle.x > pom.x)
				pom.x +=1;
			ConstructionSites6.add(new Point(pom.x,pom.y));
			ConstructionSites12.remove(0);
		}
	}

	private Point isInWall(int typeID){
		for (Wall w : walls) {
			for(int i = 0 ; i < w.getBuildingTypeIds().size(); i++){
				int id = w.getBuildingTypeIds().get(i);
				if(typeID == id){
					Point ret = new Point(w.getBuildTiles().get(i).x,w.getBuildTiles().get(i).y);
					if(!sc.isInArray(ret,usePointWalls))
					return ret;
				}
			}
		}
		return new Point(-1, -1);
	}
	private void addUsePointWalls(){
		usePointWalls = new ArrayList<Point>();
		for(Unit u : game.getMyUnits()){
			if(game.getUnitType(u.getTypeID()).isBuilding()){
				for (Wall w : walls) {
					for(int i = 0 ; i < w.getBuildTiles().size(); i++){
						if(w.getBuildTiles().get(i).x == u.getTileX() && w.getBuildTiles().get(i).y== u.getTileY())
							usePointWalls.add( new Point(w.getBuildTiles().get(i).x,w.getBuildTiles().get(i).y));
					}
				}
			}
		}
	}
//----------------------------------------------------------------------------------------	
	private void initializationConstructionSites() {
		Unit nex = null;
		for(Unit u : game.getMyUnits())
			if(u.getTypeID() == UnitTypes.Protoss_Nexus.ordinal()){
				nex = u;
				break;
			}
		int tileX = nex.getTileX();
		int tileY = nex.getTileY();
		Point nexus = new Point(tileX,tileY);
		homeX = nexus.x - 4;
		homeY = nexus.y;
		home = new Point(nexus.x, nexus.y);
		int[] coordinates = sc.getNearestRegion(new Point(homeX*32, homeY*32)).getCoordinates();
		for(int i =1 ; i < coordinates.length;i++){
			int x = coordinates[i-1]/32;
			int y = coordinates[i]/32; 
			if(coordinates[i-1]% 32  >= 16) x++;
			if(coordinates[i]% 32  >= 16) y++;
			border.addPoint(x, y);
			i++;
		}	
	}
	private void setMainSablone(int x,int y){
		ConstructionSites4 = new ArrayList<Point>();
		ConstructionSites6 = new ArrayList<Point>();
		ConstructionSites12 = new ArrayList<Point>();
		int a = 11; // or 12 need testing 
		int b = 12;
		setTemplate(x,y);
		setTemplate(x+a,y);
		setTemplate(x-a,y);
		setTemplate(x,y+b);
		setTemplate(x,y-b);
		setTemplate(x+a,y+b);
		setTemplate(x-a,y+b);
		setTemplate(x+a,y-b);
		setTemplate(x-a,y-b);
		
		setTemplate(x+a+a,y);
		setTemplate(x-a-a,y);
		setTemplate(x,y+b+b);
		setTemplate(x,y-b-b);
		
		setTemplate(x+a+a,y+b);
		setTemplate(x-a-a,y+b);
		setTemplate(x+a+a,y-b);
		setTemplate(x-a-a,y-b);	
		setTemplate(x+a,y+b+b);
		setTemplate(x-a,y+b+b);
		setTemplate(x+a,y-b-b);
		setTemplate(x-a,y-b-b);
		
		setTemplate(x+a+a,y+b+b);
		setTemplate(x-a-a,y-b-b);
		setTemplate(x-a-a,y+b+b);
		setTemplate(x+a+a,y-b-b);
		
		checkSablone();	
	}
	private void setTemplate(int x , int y ){
		middleTemplate.add(new Point(x+4,y+5));
		ConstructionSites12.add(new Point(x ,y));
		ConstructionSites12.add(new Point(x+4  ,y));
		ConstructionSites12.add(new Point(x ,y+7));
		ConstructionSites12.add(new Point(x+4  ,y+7));
		ConstructionSites4.add(new Point(x+4  ,y+5));
		ConstructionSites4.add(new Point(x+2 ,y+3));
		ConstructionSites4.add(new Point(x+4 ,y+3));
		ConstructionSites4.add(new Point(x+2  ,y+5));
		ConstructionSites6.add(new Point(x-1 ,y+3));
		ConstructionSites6.add(new Point(x -1 ,y+5));
		ConstructionSites6.add(new Point(x+6 ,y+3));
		ConstructionSites6.add(new Point(x+6  ,y+5));
	}
	private void checkSablone(){ 
		ConstructionSites4 = checkConstructionSites(ConstructionSites4,2,2);
		ConstructionSites6 =  checkConstructionSites(ConstructionSites6,3,2);
		ConstructionSites12 = checkConstructionSites(ConstructionSites12,4,3);
	}
	private ArrayList<Point> checkConstructionSites(ArrayList<Point> ConstructionSites,int XX, int  YY){
		ArrayList<Point> pom = new ArrayList<Point>();
		if(!ConstructionSites.isEmpty()){
			for(int i = 0 ; i < ConstructionSites.size(); i++){
				if(!isLockHere(ConstructionSites.get(i).x, ConstructionSites.get(i).y,ConstructionSites.get(i).x+XX, ConstructionSites.get(i).y+YY))
					pom.add(ConstructionSites.get(i));	
			}
		}	
		return pom;
	}
	private boolean isLockHere(int odi,int odj , int doi,int doj){
		if(odi < 0 || odj < 0 || doj > game.getMap().getHeight() || doi > game.getMap().getWidth() )
			return true;
		for(int i = odi; i < doi ; i++){
			for(int j = odj; j < doj ; j++){
				if(getLock(i, j))
					return true;
				if(!border.contains(i,j))
					return true;
			}
		}
		return false;
	}
	private void recontrolConstructionSites(){ 
		for(Unit u :game.getMyUnits()){
			int buildingTypeID = u.getTypeID();
			if(game.getUnitType(buildingTypeID).isBuilding()){
				int tileWidth = game.getUnitType(buildingTypeID).getTileWidth();
				int tileHeight = game.getUnitType(buildingTypeID).getTileHeight();
				if(!ConstructionSites4.isEmpty())
					for(int i = 0 ; i < ConstructionSites4.size();i++)
						if(isUseConstructionSites(ConstructionSites4.get(i), new Point(ConstructionSites4.get(i).x + 2 ,ConstructionSites4.get(i).y + 2)
													, new Point(u.getTileX(), u.getTileY()),new Point(u.getTileX()+tileWidth, u.getTileY()+tileHeight)))	
							ConstructionSites4.remove(i);
				if(!ConstructionSites6.isEmpty()) 
					for(int i = 0 ; i < ConstructionSites6.size();i++)
						if(isUseConstructionSites(ConstructionSites6.get(i), new Point(ConstructionSites6.get(i).x + 3 ,ConstructionSites6.get(i).y + 2)
													, new Point(u.getTileX(), u.getTileY()),new Point(u.getTileX()+tileWidth, u.getTileY()+tileHeight)))	
							ConstructionSites6.remove(i);
				if(!ConstructionSites12.isEmpty() && buildingTypeID != UnitTypes.Protoss_Nexus.ordinal()) 
					for(int i = 0 ; i < ConstructionSites12.size();i++)
						if(isUseConstructionSites(ConstructionSites12.get(i), new Point(ConstructionSites12.get(i).x + 4 ,ConstructionSites12.get(i).y + 3)
													, new Point(u.getTileX(), u.getTileY()),new Point(u.getTileX()+tileWidth, u.getTileY()+tileHeight)))	
							ConstructionSites12.remove(i);
				if(!baseLocation.isEmpty() && buildingTypeID == UnitTypes.Protoss_Nexus.ordinal()) 
					for(int i = 0 ; i < baseLocation.size();i++)
						if(isUseConstructionSites(baseLocation.get(i), new Point(baseLocation.get(i).x + 4 ,baseLocation.get(i).y + 3)
													, new Point(u.getTileX(), u.getTileY()),new Point(u.getTileX()+tileWidth, u.getTileY()+tileHeight)))	
							baseLocation.remove(i);
			}
		}
	}
	private boolean isUseConstructionSites(Point fromSite,Point toSite, Point fromUnit,Point toUnit){
		for(int i = fromSite.x ; i < toSite.x;i++)
			for(int j = fromSite.y ; j < toSite.y;j++)
				if(fromUnit.x <= i && i < toUnit.x)
					if(fromUnit.y <= j && j < toUnit.y)
						return true;
		return false;
	} 
	private void initializationMyLimitedPlan(){
		myPlan = new ArrayList<>();
		for(int i = 0 ; i < game.getMap().getWidth();i++){
			myPlan.add(new ArrayList<MyPlan>());
			for(int j = 0 ; j < game.getMap().getHeight();j++){
				myPlan.get(i).add(new MyPlan());	
				setBuild(i, j, false);
				if(!game.getMap().isBuildable(i, j))
					setLock(i, j, true);
			}
		}
		createMyLimited();
	}
	private void createMyLimited(){  // mineral and gas 
		for(Unit u : game.getNeutralUnits()){
			if(game.getUnitType(u.getTypeID()).isBuilding() ){
				int tileWidth = game.getUnitType(u.getTypeID()).getTileWidth();
				int tileHeight = game.getUnitType(u.getTypeID()).getTileHeight();
				for(int i = u.getTileX(); i < u.getTileX() + tileWidth;i++)
					for(int j = u.getTileY(); j < u.getTileY() + tileHeight;j++)
						setLock(i, j, true);
			}
		}
		extractiveZone();
	}
	private void extractiveZone(){
		Polygon mineral = new Polygon();
		Point top = new Point(home.x+2,home.y);
		Point bottom = new Point(home.x+2,home.y+3);
		for(Unit u : game.getNeutralUnits()){
			if(border.contains(u.getTileX(),u.getTileY()))
				if(u.getTypeID() == UnitTypes.Resource_Mineral_Field.ordinal()){
					if(u.getTileY() < top.y)
						top = new Point(u.getTileX(),u.getTileY());
					if(u.getTileY() > bottom.y)
						bottom = new Point(u.getTileX(),u.getTileY());
					}
		}
		mineral.addPoint(home.x+2,home.y);
		mineral.addPoint(top.x, top.y);
		mineral.addPoint(bottom.x, bottom.y);
		mineral.addPoint(home.x+2,home.y+3);
		for(int i = 0 ; i < game.getMap().getWidth();i++)
			for(int j = 0 ; j < game.getMap().getHeight();j++)
				if(mineral.contains(i,j))
					setLock(i, j, true);
	}
	private void myPlan(){
		if(reset++ % 10 == 0)
			setMainSablone(homeX, homeY);	
		resetMyPlan();
		createMyPlan();
		controlMyPlan();
	}
	private void initializationBaseLocations() {
		baseLocation =  new ArrayList<Point>();
		for(BaseLocation b :game.getMap().getBaseLocations())
			baseLocation.add(new Point( (b.getX()/ 32) -2,(b.getY()/ 32) -1));
		baseLocation = sc.sortGround(new Point(homeX,homeY),baseLocation);
	}
//-----------------------------------------------------------------------------------------	
	private void resetMyPlan(){ 
		for(int i = 0 ; i < game.getMap().getWidth();i++)
			for(int j = 0 ; j < game.getMap().getHeight();j++)
				setBuild(i, j, false);
	}
	private void createMyPlan(){
		int a = 9;int b = 6;
		for(Unit u : game.getMyUnits())
			if(game.getUnitType(u.getTypeID()).isBuilding() && u.getTypeID() == UnitTypes.Protoss_Pylon.ordinal()) 
				for(int i = u.getTileX() - a  ; i < u.getTileX() + a+1;i++)
					for(int j = u.getTileY() - b ; j < (u.getTileY() + b+1);j++)
						if(!getLock(i, j) && isInElipse(i,j,(u.getTileX()+1),(u.getTileY()+1)))
							setBuild(i, j, true);
	}
	private void controlMyPlan(){	
	/*	for(Unit u : game.getMyUnits()){
			if(game.getUnitType(u.getTypeID()).isBuilding()){
				int tileWidth = game.getUnitType(u.getTypeID()).getTileWidth();
				int tileHeight = game.getUnitType(u.getTypeID()).getTileHeight();
				for(int i = u.getTileX(); i < u.getTileX() + tileWidth;i++){
					for(int j = u.getTileY(); j < u.getTileY() + tileHeight;j++){
						setBuild(i, j, false);
					}	
				}
			}
		}*/
		for(Unit u : game.getNeutralUnits()){ // neutral build 
			if(game.getUnitType(u.getTypeID()).isBuilding() && (u.getTypeID() == UnitTypes.Resource_Mineral_Field.ordinal())){
				if(u.getResources() > 0){
					int tileWidth = game.getUnitType(u.getTypeID()).getTileWidth();
					int tileHeight = game.getUnitType(u.getTypeID()).getTileHeight();
					for(int i = u.getTileX(); i < u.getTileX() + tileWidth;i++)
						for(int j = u.getTileY(); j < u.getTileY() + tileHeight;j++)
							setLock(i, j, true);				
				}
			}
		}
	}
//------------------------------------------------------------------------------------
	private void setBuild(int x,int y,boolean hod){
		if(x >= 0 && y >= 0  && y < game.getMap().getHeight() && x < game.getMap().getWidth())
			myPlan.get(x).get(y).build = hod; 
	}
	private boolean getBuild(int x,int y){
		if(x >= 0 && y >= 0  && y < game.getMap().getHeight() && x < game.getMap().getWidth())
			return myPlan.get(x).get(y).build; 
		return false;
	}
	private void setLock(int x,int y,boolean hod){
		if(x >= 0 && y >= 0  && y < game.getMap().getHeight() && x < game.getMap().getWidth())
			myPlan.get(x).get(y).lock = hod; 
	}
	private boolean getLock(int x,int y){
		if(x >= 0 && y >= 0  && y < game.getMap().getHeight() && x < game.getMap().getWidth())
			return myPlan.get(x).get(y).lock; 
		return true;
	}
	private boolean isInElipse(int x,int y,int sx,int sy){ // TODO a,b 
		double a = 9;
		double b = 6;
		double xx = x+0.5;
		double yy = y+0.5;
		double sxx = sx;
		double syy = sy;
		double sum = (((xx - sxx)/ a)*((xx - sxx)/ a)) + (((yy-syy) / b)*((yy-syy) / b)); 
		return(sum < 1.0);	
	}
//------------------------------------ only testing ----------------------------------------
		public void drawDebugInfo() {
			if(PLACEMENT_DEBUG){
				recontrolConstructionSites();	
				
				/*
				for(Unit u : game.getMyUnits()){
					if(game.getUnitType(u.getTypeID()).isBuilding()){
						int tileWidth = game.getUnitType(u.getTypeID()).getTileWidth();
						int tileHeight = game.getUnitType(u.getTypeID()).getTileHeight();
						game.drawBox(u.getTileX()*32, u.getTileY()*32, (u.getTileX() + tileWidth)*32, (u.getTileY() + tileHeight)*32, BWColor.GREEN, false, false);
					}
				}
				*/
				/*
				for(int i = 0 ; i < game.getMap().getWidth();i++){
					for(int j = 0 ; j < game.getMap().getHeight();j++){
					//	if(getBuild(i, j))
					//		game.drawBox(i*32,j*32, (i+1)*32, (j+1)*32, BWColor.GREY, false, false);
						if(getLock(i, j))
							game.drawBox(i*32,j*32, (i+1)*32, (j+1)*32, BWColor.RED, false, false);
					}
				}
				*/
				/*
				for(int i =1; i < border.npoints; i++){
					game.drawLine(border.xpoints[i-1]*32, border.ypoints[i-1]*32,border.xpoints[i]*32,border.ypoints[i]*32, BWColor.RED,false);
				}
				game.drawLine(border.xpoints[0]*32, border.ypoints[0]*32,border.xpoints[border.npoints-1]*32,border.ypoints[border.npoints-1]*32, BWColor.RED,false);
			 	*/
				/*
				Point a = null;
				Point b = null;
				Point f = null;
				for(Point p : border){
					if(a != null && b!= null){
						game.drawLine(a, b, BWColor.RED,false);
						b = a;
						a = p;
					}else{
						if(a == null)f = p;
						b = a;a = p;
					}		
				}
				if(f != null && b!= null){
					game.drawLine(b, f, BWColor.RED,false);
				}
				*/
				/*
				for(Point p : middleTemplate){
					game.drawCircle(p.x*32, p.y*32, 8, BWColor.ORANGE, true,false);
					
				}*/
				for(Point p : ConstructionSites12){
					game.drawBox(p.x*32, p.y*32, (p.x + 4)*32, (p.y + 3)*32, BWColor.YELLOW, false, false);
					
				}
				for(Point p : ConstructionSites6){
					game.drawBox(p.x*32, p.y*32, (p.x + 3)*32, (p.y + 2)*32, BWColor.YELLOW, false, false);
					
				}
				for(Point p : ConstructionSites4){
					game.drawBox(p.x*32, p.y*32, (p.x + 2)*32, (p.y + 2)*32, BWColor.YELLOW, false, false);
					game.drawCircle((p.x*32+(p.x + 2)*32 )/2,(p.y*32+(p.y + 2)*32 )/2, 10, BWColor.YELLOW, false, false);
				}
				int s = 0;
				for(Point p : baseLocation){
					game.drawBox(p.x*32, p.y*32, (p.x + 4)*32, (p.y + 3)*32, BWColor.PURPLE, false, false);
					game.drawText(p.x *32 +12 , p.y *32 +12, ++s + " ", false);			
				}
			}
		}	
}
