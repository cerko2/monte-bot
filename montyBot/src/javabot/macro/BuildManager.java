package javabot.macro;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Vector;

import javabot.AbstractManager;
import javabot.BWAPIEventListener;
import javabot.JNIBWAPI;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;
import javabot.util.BWColor;
import javabot.util.Placement;
import javabot.util.Support;
import javabot.util.Wall;

//Azder BM
/*TODO*/
/* 
* 
*/
public class BuildManager extends AbstractManager{
	private static final boolean BUILD_MANAGER_DEBUG = true; 
	

	private boolean freeMode = false; // dokym neskonci opening som obmedzeny.
	private int time = 0;
	private Placement placement = null;
	private JNIBWAPI game = null;
	private Support sc = null;
	private Boss boss  = null;
	private int minerals = 0;
	private int gas = 0;
	ArrayList<MyCount> myCount = new ArrayList<MyCount>();
	private Vector<MyStack> createStack = new Vector<MyStack>();

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
		boolean wall = false;
		public MyStack(int typeID){ /*TODO*/ 
			this.typeID = typeID;
			this.jobID = ++JOBS;
		}
		public MyStack(int typeID,boolean wall){
			this(typeID);
			this.wall = wall;
		}
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
		sc = new Support(game);
	}
	public void gameStarted(){
		placement = new Placement(game);
		if(boss.getOpeningManager().isWallinOpening())
			placement.setWall(boss.getWallInModule().getAllWalls());
		
		sendText("Start: Build Manager");
		for(Unit u: game.getMyUnits()){
			if(u.getTypeID() == UnitTypes.Protoss_Nexus.ordinal()){
				homeX = u.getX() / 32;
				homeY = u.getY() / 32;
			}
		}
		for(int i = 0; i < 235;i++){
			myCount.add(new MyCount());
		}
	}
	public void setFreeMode(Boolean freeMode){
		this.freeMode = freeMode;
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
			Unit u = sc.getUnit(unitID);
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
			createStack.add(new MyStack(typeID,false));
			buildStack();
			return true;
		}
		return false;
	}
	public boolean createBuilding(int typeID,boolean wall){
		if(game.getUnitType(typeID).isBuilding()){
			createStack.add(new MyStack(typeID,wall));
			buildStack();
			return true;
		}
		return false;
	}
	public boolean needBuilding(int typeID){
		boolean need = false;
		if(!game.getUnitType(typeID).isBuilding()){
			restetMyCount();
			//reControlBuilding(UnitTypes.Protoss_Nexus.ordinal());
			if(typeID == UnitTypes.Protoss_Zealot.ordinal()) {
				need |= reControlBuilding(UnitTypes.Protoss_Gateway.ordinal());
			} else if(typeID == UnitTypes.Protoss_Dragoon.ordinal()) {
				need |= reControlBuilding(UnitTypes.Protoss_Gateway.ordinal());
				need |= reControlBuilding(UnitTypes.Protoss_Cybernetics_Core.ordinal());	
				need |= reControlBuilding(UnitTypes.Protoss_Assimilator.ordinal());
			} else if(typeID == UnitTypes.Protoss_Dark_Archon.ordinal()|| typeID == UnitTypes.Protoss_Archon.ordinal() || typeID == UnitTypes.Protoss_High_Templar.ordinal() || typeID == UnitTypes.Protoss_Dark_Templar.ordinal()) {
				need |= reControlBuilding(UnitTypes.Protoss_Gateway.ordinal());
				need |= reControlBuilding(UnitTypes.Protoss_Cybernetics_Core.ordinal());
				need |= reControlBuilding(UnitTypes.Protoss_Citadel_of_Adun.ordinal());
				need |= reControlBuilding(UnitTypes.Protoss_Templar_Archives.ordinal());
			} else if(typeID == UnitTypes.Protoss_Shuttle.ordinal()) {
				need |= reControlBuilding(UnitTypes.Protoss_Gateway.ordinal());
				need |= reControlBuilding(UnitTypes.Protoss_Cybernetics_Core.ordinal());
				need |= reControlBuilding(UnitTypes.Protoss_Robotics_Facility.ordinal());
			} else if(typeID == UnitTypes.Protoss_Reaver.ordinal()) {
				need |= reControlBuilding(UnitTypes.Protoss_Gateway.ordinal());
				need |= reControlBuilding(UnitTypes.Protoss_Cybernetics_Core.ordinal());
				need |= reControlBuilding(UnitTypes.Protoss_Robotics_Facility.ordinal());
				need |= reControlBuilding(UnitTypes.Protoss_Robotics_Support_Bay.ordinal());
			} else if(typeID == UnitTypes.Protoss_Observer.ordinal()) {
				need |= reControlBuilding(UnitTypes.Protoss_Gateway.ordinal());
				need |= reControlBuilding(UnitTypes.Protoss_Cybernetics_Core.ordinal());
				need |= reControlBuilding(UnitTypes.Protoss_Robotics_Facility.ordinal());
				need |= reControlBuilding(UnitTypes.Protoss_Observatory.ordinal());
			} else if(typeID == UnitTypes.Protoss_Corsair.ordinal() || typeID == UnitTypes.Protoss_Scout.ordinal()) {
				need |= reControlBuilding(UnitTypes.Protoss_Gateway.ordinal());
				need |= reControlBuilding(UnitTypes.Protoss_Cybernetics_Core.ordinal());
				need |= reControlBuilding(UnitTypes.Protoss_Stargate.ordinal());
			} else if(typeID == UnitTypes.Protoss_Carrier.ordinal()) {
				need |= reControlBuilding(UnitTypes.Protoss_Gateway.ordinal());
				need |= reControlBuilding(UnitTypes.Protoss_Cybernetics_Core.ordinal());
				need |= reControlBuilding(UnitTypes.Protoss_Stargate.ordinal());
				need |= reControlBuilding(UnitTypes.Protoss_Fleet_Beacon.ordinal());
			} else if(typeID == UnitTypes.Protoss_Arbiter.ordinal()){
				need |= reControlBuilding(UnitTypes.Protoss_Gateway.ordinal());
				need |= reControlBuilding(UnitTypes.Protoss_Cybernetics_Core.ordinal());
				need |= reControlBuilding(UnitTypes.Protoss_Stargate.ordinal());
				need |= reControlBuilding(UnitTypes.Protoss_Arbiter_Tribunal.ordinal());
			}
		}
		return need;
	}
//-----------------------------------------------------------------------------------------
	private boolean reControlBuilding(int typeID){
		if(myCount.get(typeID).count <= 0){
			if(!isInStack(typeID))
				createBuilding(typeID);
			return true;
		}
		return false;
	}
	private boolean isInStack(int typeID){
		for(MyStack my: createStack){
			if(my.typeID == typeID)
				return true;
		}
		return false;
	}

//-----------------------------------------------------------------------------------------	
	private void setSettings(){
		if(boss != null){
			this.minerals = boss.getBuildManagerMinerals();  
			this.gas = boss.getBuildManagerGas();
			if(BUILD_MANAGER_DEBUG){ /*TODO*/
				this.minerals = game.getSelf().getMinerals();
				this.gas = game.getSelf().getGas();
			}
		}else sendText("err: boss = null");
	}
	private void myAct(){
		int SUPPLY = 12 ; // udrzuj tolko miesta 
		time++;
		ArrayList<MyUnit> workerss = new ArrayList<>(workers);
		for(MyUnit u : workerss){
			if(u.worker.isIdle() || u.t + TIME_EXPIRES >= time || !sc.isExist(u.worker.getID())){
				workers.remove(u);
				boss.getWorkerManager().addWorker(u.worker);
			}
		}
		
		restetMyCount();
		
		if(freeMode && game.getSelf().getSupplyTotal() < 400){
			int freeSupply = getTotalSupply() - game.getSelf().getSupplyUsed();
			if((freeSupply < SUPPLY) && !isInStack(UnitTypes.Protoss_Pylon.ordinal())){
				createStack.add(0,new MyStack(UnitTypes.Protoss_Pylon.ordinal()));
			}
		}
		checkPower();
		buildStack();
	}
	private void checkPower(){
		for(Unit u : game.getMyUnits()){
			int buildingTypeID = u.getTypeID();
			if(game.getUnitType(buildingTypeID).isBuilding()){
				if(!game.getUnitType(buildingTypeID).isRefinery() && buildingTypeID != UnitTypes.Protoss_Nexus.ordinal() && buildingTypeID != UnitTypes.Protoss_Pylon.ordinal()){	
					if(!placement.isBuildable(u.getTypeID(), new Point(u.getTileX(),u.getTileY()))){
						Point targer1 = placement.getBuildTile(UnitTypes.Protoss_Pylon.ordinal(), u.getTileX(), u.getTileY(),false); 
						build(UnitTypes.Protoss_Pylon.ordinal(),++JOBS, targer1);
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
			MyStack my = createStack.get(0);
			if(minerals >= game.getUnitType(typeID).getMineralPrice() && gas >= game.getUnitType(typeID).getGasPrice()){
				goStack = buildable(my);
				if(goStack){
					minerals -= game.getUnitType(typeID).getMineralPrice();
					gas -= game.getUnitType(typeID).getGasPrice();
					myCount.get(typeID).add();
				}
			}
		}
	}
	private boolean buildable(MyStack my){
		Point targer = new Point(-1,-1);
		if(game.getUnitType(my.typeID).isRefinery())
			targer = placement.getBuildTile(my.typeID, homeX, homeY,my.wall);
		else 
			targer = placement.getBuildTile(my.typeID, -1, -1,my.wall);
		if( targer.x == -1){
			if(my.wall)
				createStack.remove(0);
			return false;
		}
		if(!placement.isBuildable(my.typeID, targer)){
			Point targer1 = placement.getBuildTile(UnitTypes.Protoss_Pylon.ordinal(), targer.x, targer.y,false); 
			build(UnitTypes.Protoss_Pylon.ordinal(),++JOBS, targer1);
		}else
			return build(my.typeID,my.jobID,targer);
		return false;
		
	}
	private boolean build(int typeID,int jobID,Point targer){
		targer.x *= 32;
		targer.y *= 32;
		int workerID = getWorker(targer.x,targer.y);
		Unit workerUnit = sc.getUnit(workerID);
		if(workerID != -1 && workerUnit != null){
			workers.add(new MyUnit(workerUnit, jobID));
			if(targer.x >= 0){
				if(sc.getDistance(workerUnit, targer) < 256){
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


	private int getWorker(int x,int y){
		return boss.getWorkerManager().getWorker(x, y);
	}
//------------------------------------------------------------------------------------------
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
		if(BUILD_MANAGER_DEBUG) game.sendText("BM: " + msg);
	}
	private void drawDebugInfo() {
		if(BUILD_MANAGER_DEBUG){
			int ww = 440;
			for(int i = 0; i < createStack.size();i++){
				int s = (int) Math.round(createStack.get(i).typeID);
				game.drawText(new Point(ww,20+(i*10)), createStack.get(i).jobID + ": "+ s, true);
			}
			if(createStack.isEmpty())
				game.drawText(new Point(ww,20),"-1 : empty", true);
			for(MyUnit u : workers){
				game.drawCircle(u.worker.getX(),u.worker.getY(), 16, BWColor.RED, false, false);
			}		
			placement.drawDebugInfo();
			
		}
	}	

	
}

