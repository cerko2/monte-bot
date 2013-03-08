package javabot.strategy;

import java.util.ArrayList;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.types.UnitType.UnitTypes;

public class OpeningManager extends AbstractManager{
	private boolean testing = false; //testovacie vypisy.
	private JNIBWAPI game;
	private boolean isActive;
	private ArrayList<OpeningTask> openingList;
	private int taskIndex;
	//private ArrayList<Integer> openingBuildList;
	//private ArrayList<Integer> openingUnitProductionList;
	private int nextBuilding;
	private int nextUnit;
	private boolean nextWorker;
	
	public OpeningManager(JNIBWAPI game){
		this.game = game;
		isActive = true;
		openingList = new ArrayList<OpeningTask>();
		taskIndex = 0;
		nextBuilding = nextUnit = -1;
		nextWorker = false;
		setOpening();
	}
	
	public void gameUpdate(){
		if (isActive()){
			if (taskIndex < openingList.size()){
				OpeningTask task = openingList.get(taskIndex); 
				//if (!task.isDone()){ //TODO tato podmienka je asi zbytocna
					perform(task);
					//return;
				//}
			}
			else{
				setInactive();
				game.printText("Open Manager has ended.");
			}
		}
	}
	
	public void unitCreate(int unitID){
		//TODO maybe not needed;
	}
	
	public boolean isActive(){
		return isActive;
	}
	
	public boolean nextWorker(){
		boolean result = nextWorker;
		if (!result){
			return result;
		}
		openingList.get(taskIndex).done();
		taskIndex++;
		nextWorker = false;
		return result;
	}
	
	public int nextUnit(){
		int result = nextUnit;
		if (result == -1){
			return result;
		}
		openingList.get(taskIndex).done();
		taskIndex++;
		nextUnit = -1;
		return result;
	}
	
	public int nextBuilding(){
		int result = nextBuilding;
		if (result == -1){
			return result;
		}		
		openingList.get(taskIndex).done();
		taskIndex++;
		nextBuilding = -1;
		return result;
	}
	
	private void setInactive(){
		isActive = false;
	}
	
	private void setOpening(){
		//TODO depending on opponent race
		//TODO next lines are only for testing
	///	openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 4, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		// 9/9 Gateway
/*
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 4, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Gateway.ordinal()));
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 4, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Zealot.ordinal()));
		*/
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 4, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 5, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 6, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 7, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 8, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Pylon.ordinal()));
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 8, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Gateway.ordinal()));
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Gateway.ordinal()));
		//openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.SCOUTING_ACTION, -1));
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 10, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 11, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Zealot.ordinal()));
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 13, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Pylon.ordinal()));
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 13, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Zealot.ordinal()));
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 15, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Zealot.ordinal()));
	
		/*
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 7, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 8, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Pylon.ordinal()));
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Zealot.ordinal()));
	*/
	}
	
	public void perform(OpeningTask task){
		switch (task.constraintType){
			case OpeningTask.SUPPLY_CONSTRAINT:
				if (game.getSelf().getSupplyUsed() / 2 < task.constraint){
					return;
				}
			break;
			case OpeningTask.MINERALS_CONSTRAINT:
				if (game.getSelf().getMinerals() < task.constraint){
					return;
				}
			break;
			case OpeningTask.GASS_CONSTRAINT:
				if (game.getSelf().getGas() < task.constraint){
					return;
				}
			break;
		}
		
		switch (task.action){
			case OpeningTask.PRODUCING_ACTION:
				if (game.getUnitType(task.unitTypeID).isBuilding()){
					nextBuilding = task.unitTypeID;
					sendText("postav budovu");
				}
				else{
					if (task.unitTypeID == UnitTypes.Protoss_Probe.ordinal()){
						nextWorker = true;
						sendText("trenuj workera");
					}
					else{
						nextUnit = task.unitTypeID;
						sendText("trenuj unit");
					}
				}
			break;
			case OpeningTask.SCOUTING_ACTION:
				//TODO send scout;
			break;
		}
	}
//------------------------------------ only testing ----------------------------------------
private void sendText(String msg){
	if(testing) game.sendText("OM: " + msg);
}
//------------------------------------ only testing ----------------------------------------

}
	
class OpeningTask {
	public static final int SUPPLY_CONSTRAINT = 0;
	public static final int MINERALS_CONSTRAINT = 1;
	public static final int GASS_CONSTRAINT = 2;
	
	public static final int PRODUCING_ACTION = 0;
	public static final int SCOUTING_ACTION = 1;
	
	public int constraintType;
	public int constraint;
	public int action;
	public int unitTypeID;
	private boolean isDone;
	
	public OpeningTask(int constraintType, int constraint, int action, int unitTypeID){
		this.constraintType = constraintType;
		this.constraint = constraint;
		this.action = action;
		this.unitTypeID = unitTypeID;
		isDone = false;
	}
	
	public boolean isDone(){
		return isDone;
	}
	
	public void done(){
		isDone = true;
	}
	
}
