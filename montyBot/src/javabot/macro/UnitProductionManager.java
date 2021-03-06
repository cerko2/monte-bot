package javabot.macro;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;
import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;

//Azder UPM
/*TODO*/
/* 
 * lepsie spravit navysovanie pomeru. 
 * otestovat funciu createUnit
 */
public class UnitProductionManager extends AbstractManager{
	private boolean testing = true; //testovacie vypisy.
	private boolean freeMode = false; // dokym neskonci opening som obmedzeny.
	
	private JNIBWAPI game = null;
	private Boss boss  = null;
	private int minerals = 0;
	private int gas = 0;
	
	private ArrayList<Double> rateArmy = new ArrayList<Double>(); // sucet = 100;???
	private ArrayList<Double> rateArmyActual = new ArrayList<Double>();
	private ArrayList<MyArmyGap> rateArmyGap = new ArrayList<MyArmyGap>();
	private ArrayList<Unit> useBuilding = new ArrayList<Unit>();
	private Vector<Integer> createStack = new Vector<Integer>();
	private Vector<Integer> createStackExternal = new Vector<Integer>();
	private double rate = 1;
	
	private static int numArmy = 13; // velkos rozmanitosti armady.
	private static int maxRate = 100; 
	private static int actFrequency = 30; //frekvecia myAct
	
	private class MyArmyGap implements Comparable<MyArmyGap>{
		private double gap = 0;
		private int ID = -1;
		public MyArmyGap(double gap,int ID){
			this.gap = gap;
			this.ID = ID;
		}
		@Override
		public int compareTo(MyArmyGap o) {
			return (int)( o.gap- gap);
		}
	}	
//-----------------------------------------------------------------------------------------	
	public UnitProductionManager(Boss boss){
		this.boss = boss;
		this.game = boss.game;
		sendText("Start: Unit Production");	
		for(int i = 0 ; i < numArmy ; i++){
			rateArmy.add(0.0);
		}  
		// testing  /*TODO*/
		rateArmy.set(0, 50.0);
		rateArmy.set(1, 50.0);
	}
	public void setAddResources(int minerals,int gas){
		this.minerals += minerals;
		this.gas += gas;
	}
	public void gameUpdate(){
		if((game.getFrameCount() % actFrequency == 0 ) ){
			setSettings();
			if(minerals > 0) 
				myAct();
		}
		drawDebugInfo();		
	}
	public Vector<Integer> getConstructionPlans(){
		Vector<Integer> vect = new Vector<Integer>();
		setCrateStack();
		
		vect.addAll(createStackExternal);
		vect.addAll(createStack);
		return vect;
	}
	
	/**
	 * Void function to switch Unit Production Manager into "Free Mode", it must be done 
	 * after Opening Manager has ended.
	 * @param Boolean freeMode - set private variable to false(Unit Production Manager
	 * produce units requested by Opening Manager) or true(Unit Production Manager 
	 * manage unit production by self)
	 * 
	 */
	public void setFreeMode(Boolean freeMode){
		this.freeMode = freeMode;
	}
	/**
	 * 
	 * @param typeID - ID of unit, that you want to train
	 */
	public void createUnit(int typeID){	
		createStackExternal.add(typeID);
		buildExternalStack();
	}
	/**
	 * 
	 * @param rateArmy 
	 * @return
	 */
	public boolean setRateArmy( ArrayList<Double> rateArmy){ //true ak to akceptujem
		if(this.rateArmy.size() == rateArmy.size()){ 
			double sum = sumRate(rateArmy);
			if(sum != 100){ // normalizujem to na 100
				double correctD = 100.0 / sum  ; 	
				rateArmy =  correctRate(rateArmy,correctD);
			}
			this.rateArmy = rateArmy;  
			return true;
		}
		return false;
	}
	public ArrayList<Double> getRateArmy( ){ 
		return rateArmy;
	}
//-----------------------------------------------------------------------------------------
	private double sumRate(ArrayList<Double> rateArmy){
		double sum = 0.0;
		for(Double d : rateArmy)
			sum += d;
		return sum;
	}
	private ArrayList<Double> correctRate(ArrayList<Double> rateArmy,double correctD){
		ArrayList<Double> rate = new ArrayList<Double>();
		for(Double d : rateArmy)
			rate.add(d * correctD);
		return rate;
	}
//-----------------------------------------------------------------------------------------
	private void setSettings(){
		if(boss != null){
			this.minerals = boss.getUnitProductionMinerals();  
			this.gas = boss.getUnitProductionGas();
			if(testing){ /*TODO*/
				this.minerals = game.getSelf().getMinerals();
				this.gas = game.getSelf().getGas();
			}
		}else sendText("err: boss = null");
	}
	private void myAct(){
		useBuilding = new ArrayList<Unit>();
		if(!boss.getOpeningManager().isActive())
			freeMode = true;
		if(!freeMode){
			boolean unitID = boss.getOpeningManager().nextWorker();
			if(unitID){
				createStackExternal.add(UnitTypes.Protoss_Probe.ordinal());
			}
		}
		if(!freeMode){
			int unitID = boss.getOpeningManager().nextUnit();
			if(unitID >= 0)
				createStackExternal.add(unitID);
		}
		
		buildExternalStack();
		if(freeMode){ // AK je modul aktivny
			setRateArmyActual();
			setRateArmyGap();	
			setCrateStack();
			//---------->>
			while (!createStack.isEmpty() && minerals > 0) {
				int typeID = createStack.get(0);
				createStack.remove(0);
				productionUnit(typeID);
			}
			for(int i =0 ; i < numArmy;i++){
				if(rateArmy.get(i) > 0){
					boss.getBuildManager().needBuilding(InternalID_To_UnitTypeID(i));
				}
			}
		}
		useBuilding = new ArrayList<Unit>();
	}	
//-----------------------------------------------------------------------------------------
	private void buildExternalStack(){
		boolean createExternal = true;
		while (!createStackExternal.isEmpty() && minerals > 0 && createExternal) {
			int typeID = createStackExternal.get(0);
			createExternal = productionUnit(typeID);
		
			if(createExternal) // ak ju dalo stavat tak vyraz
				createStackExternal.remove(0);
		}	
	}
	private void setRateArmyActual(){	
		rateArmyActual = new ArrayList<Double>();
		for(int i = 0 ; i < numArmy ; i++){
			rateArmyActual.add(0.0);
		} 
		int sumID = -1;
		for(Unit u : game.getMyUnits()){
			sumID = UnitTypeID_To_InternalID(u.getTypeID());
			if(sumID != -1) //jednotka je v zozname
				if(rateArmy.get(sumID) > 0) // mam take jednotky vyrabat
					rateArmyActual.set(sumID, rateArmyActual.get(sumID)+ 1);
		}
		Double countArmy = (double) countArmy();
		if(countArmy <= 0) countArmy = 1.0;
		for(int i = 0 ; i < numArmy ; i++){
			rateArmyActual.set(i,( rateArmyActual.get(i) / countArmy)*100);
		} 
		rate =   maxRate / countArmy ;
	}
	private int countArmy(){
		int sum = 0;
		for(Unit u : game.getMyUnits()){
			if(UnitTypeID_To_InternalID(u.getTypeID()) != - 1)
				sum++;
		}
		if(sum == 0) return 1;
		return sum;
	}
	private void setRateArmyGap(){
		rateArmyGap = new ArrayList<MyArmyGap>();; 
		for(int i = 0 ; i < numArmy ; i++){
			rateArmyGap.add(new MyArmyGap(0.0, i));
		} 
		for(int i = 0 ; i < numArmy ; i++){
			rateArmyGap.get(i).gap =   rateArmy.get(i) - rateArmyActual.get(i);
		} 
		Collections.sort(rateArmyGap);
	}
	private void setCrateStack(){
		createStack = new Vector<Integer>();
		double pomRate = 0.0;
		int count = 0;
		for(int i =0 ; i < numArmy;i++){
			pomRate = rateArmyGap.get(i).gap;
			if(pomRate > 0){
				count = (int)(pomRate /  rate);
				for(int j=0 ; j < count; j++){
					createStack.add(InternalID_To_UnitTypeID(rateArmyGap.get(i).ID));
				}
			}	
		}
		
		for(int i =0 ; i < numArmy;i++){ // TODO dako rozumne
			Random r = new Random();
			int s = r.nextInt(numArmy);
			pomRate = rateArmy.get(s);
			if(pomRate > 0){
				createStack.add(InternalID_To_UnitTypeID(s));
			}	
		}
		
		Vector<Integer> productBuild = new Vector<Integer>();
		for(int i = 0; i < createStack.size();i++) {
			int typeID = createStack.get(i);
			int buildingID = game.getUnitType(typeID).getWhatBuildID();
			Unit building = findBuilding(buildingID,productBuild);
			if(building  != null){
				productBuild.add(building.getID());
			}else{
				createStack.remove(i);
			}
		}
		
	}
//---------->>""
	private boolean productionUnit(int typeID){
		if(typeID == UnitTypes.Protoss_Archon.ordinal()||typeID == UnitTypes.Protoss_Dark_Archon.ordinal()){	
			return productionArchon(typeID);
		}else{
			int buildingID = game.getUnitType(typeID).getWhatBuildID();
			Unit building = findBuilding(buildingID);
			if(building != null)
				return createUnit(building, typeID);
			return false;
		}
	}
	private boolean productionArchon(int typeID){
		 //TODO zavolat MC nech vytvory unit.
		return true;
	}
	private Unit findBuilding(int building){
		for(Unit u : game.getMyUnits()){
			if(u.getTypeID() == building ){	
				if(!u.isTraining() && u.isCompleted())
					return u;
			}
		}
		return null;	
	}
	private Unit findBuilding(int building,Vector<Integer> array){
		for(Unit u : game.getMyUnits()){
			if(u.getTypeID() == building ){	
				if(!u.isTraining() && !inArray( u.getID(),array) && u.isCompleted())
					return u;
			}
		}
		return null;	
	}

	private boolean createUnit(Unit building, int typeID){
		Boolean testInGroup = false;
		int freeSuply = game.getSelf().getSupplyTotal() - game.getSelf().getSupplyUsed();
		if(freeSuply  >= game.getUnitType(typeID).getSupplyRequired() && minerals >= game.getUnitType(typeID).getMineralPrice() && gas >= game.getUnitType(typeID).getGasPrice()){
			testInGroup =  isInGroup(building, useBuilding);
			if(!building.isTraining() && !testInGroup){
				game.train(building.getID(), typeID);	
				useBuilding.add(building);
				return true;
			}
		}
		return false;
	}
//------------------------------------------------------------------------------------------
	private boolean isInGroup(Unit u, ArrayList<Unit> list){
		for(Unit l:list){
			if(l.getID() == u.getID())
				return true;
		}
		return false;
	}
	private boolean inArray(Integer ID,Vector<Integer> zoz){
		for(Integer i: zoz){
			if (i == ID)
				return true;
		}
		return false;
	}
//------------------------------------ FROM-TO ---------------------------------------------
	private int UnitTypeID_To_InternalID(int typeID){
		if(typeID == UnitTypes.Protoss_Zealot.ordinal()) return 0; 			// 0
		if(typeID == UnitTypes.Protoss_Dragoon.ordinal()) return 1; 		// 1
		if(typeID == UnitTypes.Protoss_High_Templar.ordinal()) return 2; 	// 2
		if(typeID == UnitTypes.Protoss_Dark_Templar.ordinal()) return 3; 	// 3
		if(typeID == UnitTypes.Protoss_Shuttle.ordinal()) return 4; 		// 4
		if(typeID == UnitTypes.Protoss_Reaver.ordinal()) return 5; 			// 5
		if(typeID == UnitTypes.Protoss_Observer.ordinal()) return 6; 		// 6
		if(typeID == UnitTypes.Protoss_Scout.ordinal()) return 7; 			// 7
		if(typeID == UnitTypes.Protoss_Corsair.ordinal()) return 8; 		// 8
		if(typeID == UnitTypes.Protoss_Carrier.ordinal()) return 9; 		// 9
		if(typeID == UnitTypes.Protoss_Arbiter.ordinal()) return 10; 		// 10
		if(typeID == UnitTypes.Protoss_Archon.ordinal()) return 11; 		// 11
		if(typeID == UnitTypes.Protoss_Dark_Archon.ordinal()) return 12; 	// 12
		return -1;
	}
	private int InternalID_To_UnitTypeID(int InternalID){
		if(InternalID == 0) return UnitTypes.Protoss_Zealot.ordinal(); 			// 0
		if(InternalID == 1) return UnitTypes.Protoss_Dragoon.ordinal(); 		// 1
		if(InternalID == 2) return UnitTypes.Protoss_High_Templar.ordinal(); 	// 2
		if(InternalID == 3) return  UnitTypes.Protoss_Dark_Templar.ordinal(); 	// 3
		if(InternalID == 4) return UnitTypes.Protoss_Shuttle.ordinal(); 		// 4
		if(InternalID == 5) return UnitTypes.Protoss_Reaver.ordinal(); 			// 5
		if(InternalID == 6) return UnitTypes.Protoss_Observer.ordinal(); 		// 6
		if(InternalID == 7) return UnitTypes.Protoss_Scout.ordinal(); 			// 7
		if(InternalID == 8) return UnitTypes.Protoss_Corsair.ordinal(); 		// 8
		if(InternalID == 9) return  UnitTypes.Protoss_Carrier.ordinal(); 		// 9
		if(InternalID == 10) return UnitTypes.Protoss_Arbiter.ordinal(); 		// 10
		if(InternalID == 11) return UnitTypes.Protoss_Archon.ordinal(); 		// 11
		if(InternalID == 12) return UnitTypes.Protoss_Dark_Archon.ordinal(); 	// 12
		return -1;
	}
//------------------------------------ only testing ----------------------------------------
	private void sendText(String msg){
		if(testing) game.sendText("UPM-" + msg);
	}
	private void drawDebugInfo() {
		if(testing){
			int hh = 180 ;
			int ww = 500;
			for(int i = 0; i < rateArmyGap.size();i++){
				if(i == 0) 
					game.drawText(new Point(ww,hh - 15), "Rate: "+rate, true);
				int s = (int) Math.round(rateArmyGap.get(i).gap);
				game.drawText(new Point(ww,hh+(i*10)), rateArmyGap.get(i).ID + ": "+ s, true);
				int ss = (int) Math.round( rateArmyActual.get(i));
				game.drawText(new Point(ww+40,hh+(i*10)), i + ": "+  ss, true);
				int sss = (int) Math.round( rateArmy.get(i));
				game.drawText(new Point(ww+80,hh+(i*10)), i + ": "+ sss , true);
			}
			for(int i = 0; i < createStackExternal.size(); i++ ){
				int s = (int) Math.round(createStackExternal.get(i));
				game.drawText(new Point(ww,20 + (i*10)), i + ": "+ s, true);
			}
			if(createStackExternal.isEmpty())
				game.drawText(new Point(ww,20),"-1 : empty", true);
		}
	}	
}
