package javabot.strategy;

import java.util.ArrayList;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.types.UnitType.UnitTypes;

public class OpeningManager extends AbstractManager{
	private JNIBWAPI game;
	private boolean isActive;
	private ArrayList<OpeningTask> openingList;
	private int taskIndex;
	private int nextBuilding;
	private int nextUnit;
	private boolean nextWorker;
	private boolean sendScout;
	
	public OpeningManager(JNIBWAPI game){
		this.game = game;
		isActive = true;
		openingList = new ArrayList<OpeningTask>();
		taskIndex = 0;
		nextBuilding = nextUnit = -1;
		nextWorker = false;
		sendScout = false;
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
	
	/**
	 * <h2>isActive</h2>
	 * 
	 * <p>
	 * Returns <code>true</code> if the Opening Manager is still active or <code>false</code> if 
	 * he's done.
	 * </p>
	 * 
	 * @return <code>true</code> if the Opening Manager is active or <code>false</code> otherwise
	 */
	public boolean isActive(){
		return isActive;
	}
	
	/**
	 * <h2>nextWorker</h2>
	 * 
	 * <p>
	 * Returns <code>true</code> if the Worker Manager must train new worker or <code>false</code> if 
	 * there is no request to train new worker by the Opening Manager. This method is determined to be 
	 * used by the Worker Manager and should by used in Worker Manager's  
	 * <code>public void gameUpdate()</code> method.
	 * </p>
	 * 
	 * @return <code>true</code> if the Worker Manager must train new worker or <code>false</code> 
	 * otherwise
	 */	
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

	/**
	 * <h2>nextUnit</h2>
	 * 
	 * <p>
	 * Returns <code>unitID</code> of a unit that must be trained by the Unit Production Manager or 
	 * <code>-1</code> if there is no unit requested by the Opening Manager to be trained. This method 
	 * is determined to be used by the Unit Production Manager and should by used in 
	 * Unit Production Manager's <code>public void gameUpdate()</code> method.
	 * </p>
	 * 
	 * @return <code>unitID</code> of a unit that must be trained by the Unit Production Manager or 
	 * <code>-1</code> otherwise
	 */	
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

	/**
	 * <h2>nextBuilding</h2>
	 * 
	 * <p>
	 * Returns <code>unitID</code> of a building that must be built by the Building Manager or <code>-1</code>  
	 * if there is no building requested by the Opening Manager to be built. This method is determined 
	 * to be used by the Building Manager and should by used in Building Manager's  
	 * <code>public void gameUpdate()</code> method.
	 * </p>
	 * 
	 * @return <code>unitID</code> of a building that must be built by the Building Manager or 
	 * <code>-1</code> otherwise
	 */
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

	/**
	 * <h2>sendScout</h2>
	 * 
	 * <p>
	 * Returns <code>true</code> if a Scout Manager must send a scout to scouting or <code>false</code>  
	 * if there is no request to scouting by the Opening Manager. This method is determined 
	 * to be used by the Scout Manager and should by used in Scout Manager's  
	 * <code>public void gameUpdate()</code> method.
	 * </p>
	 * 
	 * @return <code>true</code> if the Scout Manager must send a scout to scouting or <code>false</code> 
	 * otherwise
	 */	
	public boolean sendScout(){
		boolean result = sendScout;
		if (!result){
			return result;
		}
		openingList.get(taskIndex).done();
		taskIndex++;
		sendScout = false;
		return result;		
	}
	
	private void setInactive(){
		isActive = false;
	}
	
	private void setOpening(){
		//TODO depending on opponent race
		//TODO next lines are only for testing
		// 9/9 Gateway
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 8, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Pylon.ordinal()));
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Gateway.ordinal()));
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Gateway.ordinal()));
		openingList.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.SCOUTING_ACTION, -1));
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
	
	private void perform(OpeningTask task){
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
					//game.printText("postav budovu");
				}
				else{
					if (task.unitTypeID == UnitTypes.Protoss_Probe.ordinal()){
						nextWorker = true;
						//game.printText("trenuj workera");
					}
					else{
						nextUnit = task.unitTypeID;
						//game.printText("trenuj unit");
					}
				}
			break;
			case OpeningTask.SCOUTING_ACTION:
				sendScout = true;
				//game.printText("posli scouta");
			break;
		}
	}
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
