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
* 
*/
public class BuildManager extends AbstractManager{
	private boolean testing = true; //testovacie vypisy.
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
	
	private static int timeExpires = 100;
	private static int actFrequency = 30; //frekvecia myAct
	private static int initMyPlanFrequency = 100000;
	private static int jobs = 0;
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
		int typeJob = -1;
		int jobID = -1;
		public MyStack(int typeJob){
			this.typeJob = typeJob;
			this.jobID = ++jobs;
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
		if((game.getFrameCount() % actFrequency == 0 )){
			setSettings();
			if(minerals > 0) 
				myAct();
		}
		drawDebugInfo();		
	}
	public Vector<Integer> getConstructionPlans(){
		Vector<Integer> rad = new Vector<Integer>();
		for(MyStack m: createStack)
			rad.add(m.typeJob);
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
	public boolean needBuilding(int typeID){
		if(!game.getUnitType(typeID).isBuilding()){
			if(typeID == UnitTypes.Protoss_Zealot.ordinal()) {
				reControlBuilding(UnitTypes.Protoss_Gateway.ordinal());
			} else if(typeID == UnitTypes.Protoss_Dragoon.ordinal()) {
				reControlBuilding(UnitTypes.Protoss_Assimilator.ordinal());
				reControlBuilding(UnitTypes.Protoss_Cybernetics_Core.ordinal());
				reControlBuilding(UnitTypes.Protoss_Gateway.ordinal());
			} else if(typeID == UnitTypes.Protoss_High_Templar.ordinal()) {} 
			else if(typeID == UnitTypes.Protoss_Dark_Templar.ordinal()) {} 
			else if(typeID == UnitTypes.Protoss_Shuttle.ordinal()) {} 
			else if(typeID == UnitTypes.Protoss_Reaver.ordinal()) {} 
			else if(typeID == UnitTypes.Protoss_Observer.ordinal()) {} 
			else if(typeID == UnitTypes.Protoss_Scout.ordinal()) {} 
			else if(typeID == UnitTypes.Protoss_Corsair.ordinal()) {} 
			else if(typeID == UnitTypes.Protoss_Carrier.ordinal()) {} 
			else if(typeID == UnitTypes.Protoss_Arbiter.ordinal()){} 
			else if(typeID == UnitTypes.Protoss_Archon.ordinal()) {} 
			else if(typeID == UnitTypes.Protoss_Dark_Archon.ordinal()) {} 
			else {return false;}
			return true;
		}
		return false;
	}
//-----------------------------------------------------------------------------------------
	private void reControlBuilding(int typeID){
		if(myCount.get(typeID).count <= 0 && !isInStack(typeID)){
			sendText("treba budovu!!!");
			createBuilding(typeID);
		}
	}
	private boolean isInStack(int typeID){
		for(MyStack my: createStack){
			if(my.typeJob == typeID)
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
		int supply = 5 ; // udrzuj tolko miesta 
		time++;
		
		ArrayList<MyUnit> workerss = new ArrayList<>(workers);
		for(MyUnit u : workerss){
			if(u.worker.isIdle() || u.t + timeExpires >= time)
				workers.remove(u);
		}
		
		restetMyCount();
		if(!boss.getOpeningManager().isActive())
			freeMode = true;
		if(freeMode){
			int freeSupply = game.getSelf().getSupplyTotal() - game.getSelf().getSupplyUsed();
			if(freeSupply < supply && !isInStack(UnitTypes.Protoss_Pylon.ordinal()))
				createStack.add(0,new MyStack(UnitTypes.Protoss_Pylon.ordinal()));
		}
		int building = boss.getOpeningManager().nextBuilding();
		if(building >= 0)
			createStack.add(new MyStack(building));
		
		
		myPlan();
		buildStack();
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
			int typeID = createStack.get(0).typeJob;
			int jobID = createStack.get(0).jobID;
			if (!isExWorker(jobID))
			if(minerals >= game.getUnitType(typeID).getMineralPrice() && gas >= game.getUnitType(typeID).getGasPrice()){
				goStack = build(typeID,jobID);
				if(goStack){
					createStack.remove(0);
				}
			}
		}
	}
	private boolean build(int typeID,int jobID){
		int workerID = getWorker();
		Unit workerUnit = getUnit(workerID);

		if(workerID != -1 && workerUnit != null){
			workers.add(new MyUnit(workerUnit, jobID));
			Point targer = getBuildTile(workerID, typeID, homeX, homeY); 
			targer.x *= 32;
			targer.y *= 32;
			if(targer.x >= 0){
				if(getDistance(workerUnit, targer) < 256){
					game.build(workerID, (int) targer.x / 32, (int) targer.y / 32, typeID);
						/*TODO */ // ak tam stoji nepriatel 
					return true;
				}else{
					game.rightClick(workerID, targer.x,targer.y);
					return false;
				}	
			}
		}
		return false;
	}

	private Point getBuildTile(int builderID, int buildingTypeID, int x, int y) {
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
					if (game.canBuildHere(builderID, i, j, buildingTypeID, false)) {
						// units that are blocking the tile
						boolean unitsInWay = false;
						for (Unit u : game.getAllUnits()) {
							if (u.getID() == builderID) continue;
							if ((Math.abs(u.getTileX()-i) < 4) && (Math.abs(u.getTileY()-j) < 4)) unitsInWay = true;
						}
						if (!unitsInWay) {
							ret.x = i; ret.y = j;
							return ret;
						}
						// creep for Zerg (this may not be needed - not tested yet)
						if (game.getUnitType(buildingTypeID).isRequiresCreep()) {
							boolean creepMissing = false;
							for (int k=i; k<=i+game.getUnitType(buildingTypeID).getTileWidth(); k++) {
								for (int l=j; l<=j+game.getUnitType(buildingTypeID).getTileHeight(); l++) {
									if (!game.hasCreep(k, l)) creepMissing = true;
									break;
								}
							}
							if (creepMissing) continue; 
						}
						// psi power for Protoss (this seems to work out of the box)
						if (game.getUnitType(buildingTypeID).isRequiresPsi()) {}
					}
				}
			}
			maxDist += 2;
		}
		
		if (ret.x == -1) game.printText("Unable to find suitable build position for "+game.getUnitType(buildingTypeID).getName());
		return ret;
	}

	private int getWorker(){
		/*TODO*/ //dat doprec ked bude WM.
		for(Unit u : game.getMyUnits()){
			if(u.getTypeID() == UnitTypes.Protoss_Probe.ordinal()&& !isInGroup(u, workers) ){	
				game.move(u.getID(), u.getX(),u.getY());
				return u.getID();
			}
		}
		return -1;
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
	private boolean isExWorker(int jobID){
		for(MyUnit l: workers){
			if(l.jobID == jobID)
				return true;
		}
		return false;
	}
//------------------------------------ only testing ----------------------------------------
	private void sendText(String msg){
		if(testing) game.sendText("BM: " + msg);
	}
	private void drawDebugInfo() {
		if(testing){
			int ww = 440;
			for(int i = 0; i < createStack.size();i++){
				int s = (int) Math.round(createStack.get(i).typeJob);
				game.drawText(new Point(ww,20+(i*10)), createStack.get(i).jobID + ": "+ s, true);
			}
			if(createStack.isEmpty())
				game.drawText(new Point(ww,20),"-1 : empty", true);
			
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
			for(MyUnit u : workers){
				game.drawCircle(u.worker.getX(),u.worker.getY(), 16, BWColor.RED, false, false);
			}

			/*
			for(int i = 0 ; i < game.getMap().getWidth();i++){
				for(int j = 0 ; j < game.getMap().getHeight();j++){
					if((i-7) % 18 == 0 && (j-6) % 12 == 0)
						game.drawBox(i*32,j*32, (i+4)*32, (j+2)*32, BWColor.ORANGE, false, false);
	
				}
			}
			*/
			
		}
	}	
}
