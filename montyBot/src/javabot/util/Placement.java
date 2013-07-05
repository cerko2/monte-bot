package javabot.util;

import java.awt.Point;
import java.util.ArrayList;

import javabot.JNIBWAPI;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;

//Azder Placement
public class Placement {
	private static final boolean PLACEMENT_DEBUG = true;
	
	private JNIBWAPI game = null;
	private ArrayList<ArrayList<MyPlan>> myPlan = new ArrayList<ArrayList<MyPlan>>();
	private ArrayList<Point> ConstructionSites4 = new ArrayList<Point>();
	private ArrayList<Point> ConstructionSites6 = new ArrayList<Point>();
	private ArrayList<Point> ConstructionSites12 = new ArrayList<Point>();
	private int homeX, homeY;
	private int reset = 0;
	
	private class MyPlan{
		Boolean build = false;
		Boolean lock = false;
	}
//-------------------------------------------------------------------------------------
	public Placement(JNIBWAPI game){
		this.game = game;
		initializationMyLimitedPlan();	
		initializationConstructionSites();	
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
	public Point getBuildTile(int buildingTypeID, int tileX, int tileY) {
		myPlan();
		Point ret = new Point(-1, -1);
		// Refinery, Assimilator, Extractor
		if (game.getUnitType(buildingTypeID).isRefinery()) {
			Point home = new Point(homeX,homeY);
			Point near = new Point(-1,-1);
			Double dist = 9999999999999.9;
			for (Unit n : game.getNeutralUnits()) {
				if (n.getTypeID() == UnitTypes.Resource_Vespene_Geyser.ordinal()) {
					Double newDist = getDistance(n, home);
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
	private Point getPoint(int buildingTypeID,int tileX, int tileY){
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
				return getNearestPoint(ConstructionSites4,tileX,tileY);
			}else if(size == 6 && !ConstructionSites6.isEmpty()){
				return getNearestPoint(ConstructionSites6,tileX,tileY);
			}else if(size == 12 && !ConstructionSites12.isEmpty()){
				return getNearestPoint(ConstructionSites12,tileX,tileY);
			}	
		}
		return null;
	}
	private void getNewSize(int size){ // if need new size to build. 
		if(size == 4 && ConstructionSites4.isEmpty() && !ConstructionSites6.isEmpty()){ 
			ConstructionSites4.add(new Point(ConstructionSites6.get(0).x, ConstructionSites6.get(0).y));
			ConstructionSites6.remove(0);
		}else if(size == 4 && ConstructionSites4.isEmpty() && !ConstructionSites12.isEmpty()){ 
			ConstructionSites4.add(new Point(ConstructionSites12.get(0).x, ConstructionSites12.get(0).y));
			ConstructionSites4.add(new Point(ConstructionSites12.get(0).x+2, ConstructionSites12.get(0).y));
			ConstructionSites12.remove(0);
		}else if(size == 6 && ConstructionSites6.isEmpty() && !ConstructionSites12.isEmpty()){ 
			ConstructionSites6.add(new Point(ConstructionSites12.get(0).x, ConstructionSites12.get(0).y));
			ConstructionSites12.remove(0);
		}
	}
	private Point getNearestPoint(ArrayList<Point> list,int tileX, int tileY){
		Point near = list.get(0);
		Point tile = new Point(tileX, tileY);
		double dis = getDistance(tile, near);
		for(Point l : list){
			double disPom = getDistance(tile, l);
			if(dis > disPom){
				near = l;
				dis = disPom;
			}	
		}
		return near;
	}
	private double getDistance (Point a, Point b){
		double distance, pomx, pomy;
		pomx = Math.abs(a.x-b.x);
		pomy = Math.abs(a.y-b.y);
		distance = Math.sqrt((pomx*pomx)+(pomy*pomy))   ;
		return distance;
	}
	private double getDistance (Unit a, Point b){
		double distance, pomx, pomy;
		pomx = Math.abs(a.getX()-b.x);
		pomy = Math.abs(a.getY()-b.y);
		distance = Math.sqrt((pomx*pomx)+(pomy*pomy))   ;
		return distance;
	}
	private boolean canBuildHere(int odi,int odj , int doi,int doj){
		for(int i = odi; i <= doi ; i++){
			for(int j = odi; j <= doi ; j++){
				if(!getBuild(i, j))
					return false;
			}
		}
		return true;
	}
//----------------------------------------------------------------------------------------	
	private void initializationConstructionSites() {
		Unit nex = null;
		for(Unit u : game.getMyUnits()){
			if(u.getTypeID() == UnitTypes.Protoss_Nexus.ordinal()){
				nex = u;
				break;
			}
			
		}
		int tileX = nex.getTileX();
		int tileY = nex.getTileY();
		Point nexus = new Point(tileX,tileY);
		/*
		Point mineral = new Point(-1, -1);
		
		Double dist = 9999999999999.9;
		for (Unit n : game.getNeutralUnits()) {
			if (n.getTypeID() == UnitTypes.Resource_Mineral_Field.ordinal()) {
				Double newDist = getDistance(n, nexus);
				if(newDist < dist){
					dist = newDist;
					mineral = new Point(n.getTileX(), n.getTileY());
				}		
			}
		}
		sendText(nexus.x + "<> " +mineral.x);
		if(nexus.x < mineral.x )
			homeX = nexus.x - 4;
		else*/
			homeX = nexus.x - 4;
		homeY = nexus.y;
		setMainSablone(homeX, homeY);	
	}
	private void setMainSablone(int x,int y){
		ConstructionSites4 = new ArrayList<Point>();
		ConstructionSites6 = new ArrayList<Point>();
		ConstructionSites12 = new ArrayList<Point>();
		int a = 11;
		int b = 12;
		setSablone(x,y);
		setSablone(x+a,y);
		setSablone(x-a,y);
		setSablone(x,y+b);
		setSablone(x+a,y+b);
		setSablone(x-a,y+b);
		setSablone(x,y-b);
		setSablone(x+a,y-b);
		setSablone(x-a,y-b);
		checkSablone();	
	}
	private void setSablone(int x , int y ){
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
				if(canLockHere(ConstructionSites.get(i).x, ConstructionSites.get(i).y,ConstructionSites.get(i).x+XX, ConstructionSites.get(i).y+YY))
					pom.add(ConstructionSites.get(i));	
			}
		}	
		return pom;
	}
	private boolean canLockHere(int odi,int odj , int doi,int doj){
		if(odi < 0 || odj < 0 || doj > game.getMap().getHeight() || doi > game.getMap().getWidth() )
			return false;
		for(int i = odi; i < doi ; i++){
			for(int j = odj; j < doj ; j++){
				if(getLock(i, j))
					return false;
			}
		}
		return true;
	}
	private void recontrolConstructionSites(){
		for(Unit u :game.getMyUnits()){
			int buildingTypeID = u.getTypeID();
			if(game.getUnitType(buildingTypeID).isBuilding()){
				int tileWidth = game.getUnitType(buildingTypeID).getTileWidth();
				int tileHeight = game.getUnitType(buildingTypeID).getTileHeight();
				int size =  tileWidth * tileHeight;
				if(size == 4 && !ConstructionSites4.isEmpty())
					for(int i = 0 ; i < ConstructionSites4.size();i++)
						if( ConstructionSites4.get(i).x == u.getTileX() && ConstructionSites4.get(i).y == u.getTileY() )
							ConstructionSites4.remove(i);
				if(size == 6 && !ConstructionSites6.isEmpty()) 
					for(int i = 0 ; i < ConstructionSites6.size();i++)
					if( ConstructionSites6.get(i).x == u.getTileX() && ConstructionSites6.get(i).y == u.getTileY() )
						ConstructionSites6.remove(i);
				if(size == 12 && !ConstructionSites12.isEmpty()) 
					for(int i = 0 ; i < ConstructionSites12.size();i++)
						if( ConstructionSites12.get(i).x == u.getTileX() && ConstructionSites12.get(i).y == u.getTileY() )
							ConstructionSites12.remove(i);
			}
		}
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
	private void createMyLimited(){  // mineral and gas //TODO 
		for(Unit u : game.getNeutralUnits()){
			if(game.getUnitType(u.getTypeID()).isBuilding() ){
				int tileWidth = game.getUnitType(u.getTypeID()).getTileWidth();
				int tileHeight = game.getUnitType(u.getTypeID()).getTileHeight();
				for(int i = u.getTileX(); i < u.getTileX() + tileWidth;i++){
					for(int j = u.getTileY(); j < u.getTileY() + tileHeight;j++){
						setLock(i, j, true);
					}
				}
			}
		}
	}
	private void myPlan(){
		if(reset++ % 10 == 0){
			setMainSablone(homeX, homeY);
		}
		resetMyPlan();
		createMyPlan();
		controlMyPlan();
	}
//-----------------------------------------------------------------------------------------	
	private void resetMyPlan(){ 
		for(int i = 0 ; i < game.getMap().getWidth();i++){
			for(int j = 0 ; j < game.getMap().getHeight();j++){
				setBuild(i, j, false);
			}
		}
	}
	private void createMyPlan(){
		int a = 9;int b = 6;
		for(Unit u : game.getMyUnits()){
			if(game.getUnitType(u.getTypeID()).isBuilding()){ // && !u.isBeingConstructed()){
				if(u.getTypeID() == UnitTypes.Protoss_Pylon.ordinal()){
					for(int i = u.getTileX() - a  ; i < u.getTileX() + a+1;i++){
						for(int j = u.getTileY() - b ; j < (u.getTileY() + b+1);j++){
							if(!getLock(i, j)){
								if(isInElipse(i,j,(u.getTileX()+1),(u.getTileY()+1))){
									setBuild(i, j, true);
								}	
							}
						}
					} 
				}
			}
		}
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
					for(int i = u.getTileX(); i < u.getTileX() + tileWidth;i++){
						for(int j = u.getTileY(); j < u.getTileY() + tileHeight;j++){
							setLock(i, j, true);	
						}
					}
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
		private void sendText(String msg){
			if(PLACEMENT_DEBUG) game.sendText("Placement: " + msg);
		}
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
				for(int i = 0 ; i < game.getMap().getWidth();i++){
					for(int j = 0 ; j < game.getMap().getHeight();j++){
					//	if(getBuild(i, j))
					//		game.drawBox(i*32,j*32, (i+1)*32, (j+1)*32, BWColor.GREY, false, false);
						if(getLock(i, j))
							game.drawBox(i*32,j*32, (i+1)*32, (j+1)*32, BWColor.RED, false, false);
					}
				}
				*/
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
			}
		}	
}
