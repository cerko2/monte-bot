package javabot.macro;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Vector;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;
import javabot.util.BWColor;

//Azder BM
/*TODO*/
/* 
* getBuildTile
* fix add building
* 
*/
public class BuildManager extends AbstractManager{
	private boolean testing = true; //testovacie vypisy.
	private boolean grid = false; //testovacie vypisy.
	private boolean freeMode = false; // dokym neskonci opening som obmedzeny.
	private int time = 0;
	
	private JNIBWAPI game = null;
	private Boss boss  = null;
	private int minerals = 0;
	private int gas = 0;
	ArrayList<MyCount> myCount = new ArrayList<MyCount>();
	private Vector<MyStack> createStack = new Vector<MyStack>();
	private ArrayList<ArrayList<MyPlan>> myPlan = new ArrayList<ArrayList<MyPlan>>();
	private ArrayList<MyUnit> workers = new ArrayList<MyUnit>();
	
	private static int TIME_EXPIRES = 100;
	private static int ACT_FREQUENCY = 60; //frekvecia myAct
	private static int INIT_MY_PLAN_FREQUENCY = 100000;
	private static int JOBS = 0;
	private int homeX = 0;
	private int homeY = 0;
	
	private class MyCount{
		private int count = 0;
		public void add(){
			count++;
		}
		public void reset(){
			count = 0;
		}
		public int getCount(){
			return count;
		}
	}
	private class MyPlan{
		Boolean build = false;
		Boolean lock = false;
	}
	private class MyUnit{
		Unit worker = null;
		int jobID = -1;
		int t = 0;
		public MyUnit(Unit worker,int jobID){
			this.worker = worker;
			this.jobID = jobID;
			this.t = time;
		}
	}
	private class MyStack{
		int typeID = -1;
		int jobID = -1;
		public MyStack(int typeID){ /*TODO*/ 
			this.typeID = typeID;
			this.jobID = ++JOBS;
		}
	}
	private void setBuild(int x,int y,boolean hod){
		if(x >= 0 && y >= 0  && y < game.getMap().getHeight() && x < game.getMap().getWidth()){
			myPlan.get(x).get(y).build = hod; 
		}
	}
	private boolean getBuild(int x,int y){
		if(x >= 0 && y >= 0  && y < game.getMap().getHeight() && x < game.getMap().getWidth()){
			return myPlan.get(x).get(y).build; 
		}
		return false;
	}
	private void setLock(int x,int y,boolean hod){
		if(x >= 0 && y >= 0  && y < game.getMap().getHeight() && x < game.getMap().getWidth()){
			myPlan.get(x).get(y).lock = hod; 
		}
	}
	private boolean getLock(int x,int y){
		if(x >= 0 && y >= 0  && y < game.getMap().getHeight() && x < game.getMap().getWidth()){
			return myPlan.get(x).get(y).lock; 
		}
		return true;
	}
	private void restetMyCount(){
		for(int i=0; i <myCount.size(); i++)
			myCount.get(i).reset();
		for (Unit unit : game.getMyUnits()) {	
			int id = unit.getTypeID();
			myCount.get(id).add();
		}
	}
//-----------------------------------------------------------------------------------------	
	public BuildManager(Boss boss){
		this.boss = boss;
		this.game = boss.game;
		sendText("Start: Build Manager");
		if(testing) freeMode = true;
		for(Unit u: game.getMyUnits()){
			if(u.getTypeID() == UnitTypes.Protoss_Nexus.ordinal()){
				homeX = u.getX();
				homeY = u.getY();
			}
		}
		for(int i = 0; i < 235;i++){
			myCount.add(new MyCount());
		}
		initializationMyPlan();		
	}
	public void setAddResources(int minerals,int gas){
		this.minerals += minerals;
		this.gas += gas;
	}
	public void gameUpdate(){
		if((game.getFrameCount() % ACT_FREQUENCY == 0 )){
			setSettings();
			if(minerals > 0) 
				myAct();
		}
		drawDebugInfo();		
	}
	public void unitCreate(int unitID){
		unit(unitID);
	}
	public void unitMorph(int unitID){
		unit(unitID);
	}
	private void unit(int unitID){
		if(unitID != -1){
			Unit u = getUnit(unitID);
			if(u != null){
				int typeID = u.getTypeID();
				if(!createStack.isEmpty() )
					if(typeID == createStack.get(0).typeID)
						createStack.remove(0);
			}
		}	
	}
	public Vector<Integer> getConstructionPlans(){
		Vector<Integer> rad = new Vector<Integer>();
		for(MyStack m: createStack)
			rad.add(m.typeID);
		return rad; 
	}
	public boolean isWorkerFree(Unit u){
		return isInGroup(u, workers);
	}
	public boolean isWorkerFree(int unitID){
		return isInGroup(unitID, workers);
	}
	public boolean createBuilding(int typeID){
		if(game.getUnitType(typeID).isBuilding()){
			createStack.add(new MyStack(typeID));
			buildStack();
			return true;
		}
		return false;
	}
	public boolean needBuilding(int typeID){ /*TODO*/
		if(!game.getUnitType(typeID).isBuilding()){
			restetMyCount();
			reControlBuilding(UnitTypes.Protoss_Nexus.ordinal());
			if(typeID == UnitTypes.Protoss_Zealot.ordinal()) {
				reControlBuilding(UnitTypes.Protoss_Gateway.ordinal());
			} else if(typeID == UnitTypes.Protoss_Dragoon.ordinal()) {
				reControlBuilding(UnitTypes.Protoss_Assimilator.ordinal());
				reControlBuilding(UnitTypes.Protoss_Cybernetics_Core.ordinal());
				reControlBuilding(UnitTypes.Protoss_Gateway.ordinal());
			} else if(typeID == UnitTypes.Protoss_High_Templar.ordinal() || typeID == UnitTypes.Protoss_Dark_Templar.ordinal()) {
				reControlBuilding(UnitTypes.Protoss_Cybernetics_Core.ordinal());
				reControlBuilding(UnitTypes.Protoss_Gateway.ordinal());
				reControlBuilding(UnitTypes.Protoss_Citadel_of_Adun.ordinal());
				reControlBuilding(UnitTypes.Protoss_Templar_Archives.ordinal());
			} 
			else if(typeID == UnitTypes.Protoss_Shuttle.ordinal()) {
				reControlBuilding(UnitTypes.Protoss_Gateway.ordinal());
				reControlBuilding(UnitTypes.Protoss_Cybernetics_Core.ordinal());
				reControlBuilding(UnitTypes.Protoss_Robotics_Facility.ordinal());
			} 
			else if(typeID == UnitTypes.Protoss_Reaver.ordinal()) {
				reControlBuilding(UnitTypes.Protoss_Gateway.ordinal());
				reControlBuilding(UnitTypes.Protoss_Cybernetics_Core.ordinal());
				reControlBuilding(UnitTypes.Protoss_Robotics_Facility.ordinal());
				reControlBuilding(UnitTypes.Protoss_Robotics_Support_Bay.ordinal());
			} 
			else if(typeID == UnitTypes.Protoss_Observer.ordinal()) {
				
			} 
			else if(typeID == UnitTypes.Protoss_Scout.ordinal()) {
				
			} 
			else if(typeID == UnitTypes.Protoss_Corsair.ordinal()) {
				
			} 
			else if(typeID == UnitTypes.Protoss_Carrier.ordinal()) {
				
			} 
			else if(typeID == UnitTypes.Protoss_Arbiter.ordinal()){
				
			} 
			else if(typeID == UnitTypes.Protoss_Archon.ordinal()) {
				
			} 
			else if(typeID == UnitTypes.Protoss_Dark_Archon.ordinal()) {
				
			} 
			else {return false;}
			return true;
		}
		return false;
	}
//-----------------------------------------------------------------------------------------
	private void reControlBuilding(int typeID){
		if(myCount.get(typeID).count <= 0 && !isInStack(typeID)){
			createBuilding(typeID);
		}
	}
	private boolean isInStack(int typeID){
		for(MyStack my: createStack){
			if(my.typeID == typeID)
				return true;
		}
		return false;
	}
	private void initializationMyPlan(){
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
	private void createMyLimited(){
		for(Unit u : game.getNeutralUnits()){
			if(game.getUnitType(u.getTypeID()).isBuilding()){
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
//-----------------------------------------------------------------------------------------	
	private void setSettings(){
		if(boss != null){
			this.minerals = boss.getBuildManagerMinerals();  
			this.gas = boss.getBuildManagerGas();
			if(testing){ /*TODO*/
				this.minerals = game.getSelf().getMinerals();
				this.gas = game.getSelf().getGas();
			}
		}else sendText("err: boss = null");
	}
	private void myAct(){
		freeMode = false;
		int SUPPLY = 6 ; // udrzuj tolko miesta 
		time++;
	//	createStackAct = new Vector<MyStack>();
		ArrayList<MyUnit> workerss = new ArrayList<>(workers);
		for(MyUnit u : workerss){
			if(u.worker.isIdle() || u.t + TIME_EXPIRES >= time || !isExist(u.worker.getID())){
				workers.remove(u);
				boss.getWorkerManager().addWorker(u.worker);
			}
		}
		
		restetMyCount();
		if(!boss.getOpeningManager().isActive())
			freeMode = true;
		if(freeMode && game.getSelf().getSupplyTotal() < 400){
			int freeSupply = getTotalSupply() - game.getSelf().getSupplyUsed();
			if((freeSupply < SUPPLY) && !isInStack(UnitTypes.Protoss_Pylon.ordinal())){
				createStack.add(0,new MyStack(UnitTypes.Protoss_Pylon.ordinal()));
			}
		}
		int building = boss.getOpeningManager().nextBuilding();
		if(building >= 0)
			createStack.add(new MyStack(building));
		
		
		myPlan();
		buildStack();
	}
	private boolean isExist(int id) {
		for(Unit u : game.getMyUnits())
			if(u.getID() == id)
				return true;
		return false;
	}
	private void myPlan(){
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
			if(game.getUnitType(u.getTypeID()).isBuilding() && !u.isBeingConstructed()){
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
		for(Unit u : game.getMyUnits()){
			if(game.getUnitType(u.getTypeID()).isBuilding()){
				int tileWidth = game.getUnitType(u.getTypeID()).getTileWidth();
				int tileHeight = game.getUnitType(u.getTypeID()).getTileHeight();
				for(int i = u.getTileX(); i < u.getTileX() + tileWidth;i++){
					for(int j = u.getTileY(); j < u.getTileY() + tileHeight;j++){
						setBuild(i, j, false);
					}	
				}
			}
		}
	}
//-----------------------------------------------------------------------------------------	
	private void buildStack(){
		Boolean goStack =  true;
		if(!createStack.isEmpty()){
			int typeID = createStack.get(0).typeID;
			int jobID = createStack.get(0).jobID;
			if(minerals >= game.getUnitType(typeID).getMineralPrice() && gas >= game.getUnitType(typeID).getGasPrice()){
				goStack = build(typeID,jobID);
				if(goStack){
					//createStack.remove(0); /*TODO*/
					minerals -= game.getUnitType(typeID).getMineralPrice();
					gas -= game.getUnitType(typeID).getGasPrice();
					myCount.get(typeID).add();
				}
			}
		}
	}
	private boolean build(int typeID,int jobID){
		Point targer = getBuildTile(typeID, homeX, homeY);
		targer.x *= 32;
		targer.y *= 32;
		int workerID = getWorker(targer.x,targer.y);
		Unit workerUnit = getUnit(workerID);
		if(workerID != -1 && workerUnit != null){
			workers.add(new MyUnit(workerUnit, jobID));
			if(targer.x >= 0){
				if(getDistance(workerUnit, targer) < 256){
					game.build(workerID, (int) targer.x / 32, (int) targer.y / 32, typeID);
					return true;
				}else{
					game.rightClick(workerID, targer.x,targer.y);
					return false;
				}	
			}
		}
		return false;
	}
	private Point getBuildTile(int buildingTypeID, int x, int y) {
		Point ret = new Point(-1, -1);
		int maxDist = 3;
		int stopDist = 40;
		int tileX = x/32; int tileY = y/32;
		
		// Refinery, Assimilator, Extractor
		if (game.getUnitType(buildingTypeID).isRefinery()) {
			for (Unit n : game.getNeutralUnits()) {
				if ((n.getTypeID() == UnitTypes.Resource_Vespene_Geyser.ordinal()) && 
						( Math.abs(n.getTileX()-tileX) < stopDist ) &&
						( Math.abs(n.getTileY()-tileY) < stopDist )
						) return new Point(n.getTileX(),n.getTileY());
			}
		}
		
		while ((maxDist < stopDist) && (ret.x == -1)) {
			for (int i=tileX-maxDist; i<=tileX+maxDist; i++) {
				for (int j=tileY-maxDist; j<=tileY+maxDist; j++) {
					if (game.canBuildHere(-1, i, j, buildingTypeID, false)) {
						// units that are blocking the tile
						boolean unitsInWay = false;
						for (Unit u : game.getAllUnits()) {
							if ((Math.abs(u.getTileX()-i) < 4) && (Math.abs(u.getTileY()-j) < 4)) unitsInWay = true;
						}
						if(getBuild(i, j) || buildingTypeID == UnitTypes.Protoss_Pylon.ordinal())
							if (!unitsInWay) {
								ret.x = i; ret.y = j;
								if(buildingTypeID != UnitTypes.Protoss_Pylon.ordinal()){
									int tileWidth = game.getUnitType(buildingTypeID).getTileWidth();
									int tileHeight = game.getUnitType(buildingTypeID).getTileHeight();
									for(int k = i ; k <= i + tileWidth; k++)
										for(int l = j ; l <= j + tileHeight;l++)
											setBuild(k, l, false);
								}
								return ret;
							}
						if (game.getUnitType(buildingTypeID).isRequiresPsi()) {}
					}
				}
			}
			maxDist += 2;
		}
		
		if (ret.x == -1) game.printText("Unable to find suitable build position for "+game.getUnitType(buildingTypeID).getName());
		return ret;
	}
	private int getWorker(int x,int y){
		return boss.getWorkerManager().getWorker(x, y);
	}
//------------------------------------------------------------------------------------------
	private double getDistance (Unit a, Point b){
		double distance, pomx, pomy;
		pomx = Math.abs(a.getX()-b.x);
		pomy = Math.abs(a.getY()-b.y);
		distance = Math.sqrt((pomx*pomx)+(pomy*pomy))   ;
		return distance;
	}
	private Unit getUnit(int ID){
		for (Unit unit : game.getMyUnits()) {	
			if(unit.getID() == ID )
				return unit;
		}
		return null;
	}
	private boolean isInElipse(int x,int y,int sx,int sy){
		double a = 9;
		double b = 6;
		double xx = x+0.5;
		double yy = y+0.5;
		double sxx = sx;
		double syy = sy;
		double sum = (((xx - sxx)/ a)*((xx - sxx)/ a)) + (((yy-syy) / b)*((yy-syy) / b)); 
		//game.drawText(x*32, y*32,sum + "" , false);
		return(sum < 1.0);	
	}
	private boolean isInGroup(Unit u, ArrayList<MyUnit> list){
		for(MyUnit l:list){
			if(l.worker.getID() == u.getID())
				return true;
		}
		return false;
	}
	private boolean isInGroup(int ID, ArrayList<MyUnit> list){
		for(MyUnit l:list){
			if(l.worker.getID() == ID)
				return true;
		}
		return false;
	}
	private int getTotalSupply(){
		int sum = (myCount.get(UnitTypes.Protoss_Pylon.ordinal()).count * 8) + (myCount.get(UnitTypes.Protoss_Nexus.ordinal()).count * 9);
		return sum * 2 ; 
	}
//------------------------------------ only testing ----------------------------------------
	private void sendText(String msg){
		if(testing) game.sendText("BM: " + msg);
	}
	private void drawDebugInfo() {
		if(testing){
			int ww = 440;
			for(int i = 0; i < createStack.size();i++){
				int s = (int) Math.round(createStack.get(i).typeID);
				game.drawText(new Point(ww,20+(i*10)), createStack.get(i).jobID + ": "+ s, true);
			}
			if(createStack.isEmpty())
				game.drawText(new Point(ww,20),"-1 : empty", true);
			if(grid){
				for(Unit u : game.getMyUnits()){
					if(game.getUnitType(u.getTypeID()).isBuilding()){
						int tileWidth = game.getUnitType(u.getTypeID()).getTileWidth();
						int tileHeight = game.getUnitType(u.getTypeID()).getTileHeight();
						game.drawBox(u.getTileX()*32, u.getTileY()*32, (u.getTileX() + tileWidth)*32, (u.getTileY() + tileHeight)*32, BWColor.GREEN, false, false);
					
					}
				}
				for(int i = 0 ; i < game.getMap().getWidth();i++){
					for(int j = 0 ; j < game.getMap().getHeight();j++){
						if(getBuild(i, j))
							game.drawBox(i*32,j*32, (i+1)*32, (j+1)*32, BWColor.GREY, false, false);
						else if(getLock(i, j))
							game.drawBox(i*32,j*32, (i+1)*32, (j+1)*32, BWColor.RED, false, false);
					}
				}
			}
			for(MyUnit u : workers){
				game.drawCircle(u.worker.getX(),u.worker.getY(), 16, BWColor.RED, false, false);
			}
			
			
		}
	}	

	
}

