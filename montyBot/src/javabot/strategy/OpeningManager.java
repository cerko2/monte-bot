package javabot.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.macro.Boss;
import javabot.model.Race;
import javabot.types.UnitType.UnitTypes;

public class OpeningManager extends AbstractManager{
	private boolean debug = false;
	private JNIBWAPI game;
	private Boss boss;
	private boolean isActive;
	private ArrayList<OpeningList> allOpeningLists;
	private OpeningList openingList;
	
	public OpeningManager(Boss boss){
		this.boss = boss;
		this.game = boss.game;
		isActive = true;
		openingList = new OpeningList(-1, "empty", game.getEnemies().get(0).getRaceID());
		initializeOpenings();
		setOpening();
	}
	
	public void gameUpdate(){
		if (isActive()){
			if (!openingList.isCompleted()){
				perform(openingList.getNextTask());
			}
			else{
				setInactive();
				game.printText("Opening Manager has ended.");
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
	
	public int getOpeningID(){
		return openingList.getID();
	}

	private void setInactive(){
		isActive = false;
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
					boss.getBuildManager().createBuilding(openingList.getNextTask().unitTypeID);
					openingList.completeTask();
					sendText("Build building");
				}
				else{
					if (task.unitTypeID == UnitTypes.Protoss_Probe.ordinal()){
						boss.getWorkerManager().buildWorker();
						openingList.completeTask();
						sendText("Train worker");
					}
					else{
						boss.getUnitProductionManager().createUnit(openingList.getNextTask().unitTypeID);
						openingList.completeTask();
						sendText("Train unit");
					}
				}
			break;
			case OpeningTask.SCOUTING_ACTION:
				boss.startScouting();
				openingList.completeTask();
				sendText("Start scouting");
			break;
		}
	}
	
	private void setOpening(){
		int enemyRace = game.getEnemies().get(0).getRaceID();
		String enemyName = game.getEnemies().get(0).getName();
		int openingID = -1;

		HashMap<Integer, double[]> playersKB = boss.getOpponentKnowledgeBase().getEnemysKB(enemyName);
		if (playersKB == null){
			HashMap<Integer, double[]> racesKB = boss.getOpponentKnowledgeBase().getRacesKB(enemyRace);
			if (racesKB == null){
				//select random opening
				ArrayList<Integer> allOpeningListsByRace = getOpeningListsByRace(enemyRace);
				int index;
				if (allOpeningListsByRace.size() - 1 == 0){
					index = 0;
				}
				else{
					Random r = new Random();
					index = r.nextInt(allOpeningListsByRace.size() - 1);
				}
				openingID = allOpeningListsByRace.get(index);
			}
			else{
				openingID = findSuitableOpeningID(racesKB);
			}
		}
		else{
			openingID = findSuitableOpeningID(playersKB);
		}
		
		openingList = getOpeningListByID(openingID);
		game.printText("Bol zvoleny opening: " + openingList.getName());
	}	
	
	private int findSuitableOpeningID(HashMap<Integer, double[]> KB){
		int result = -1;
		double maxScore = Double.MIN_VALUE;
		int maxScoreID = -1;
		for (Entry<Integer, double[]> entry : KB.entrySet()) {
			double[] stats = entry.getValue();
			if (stats[1] > maxScore){
				maxScore = stats[1];
				maxScoreID = entry.getKey();
			}
		}
		if (maxScore >= 1.0){
			result = maxScoreID;
		}
		else if (maxScore < 1.0 && maxScore >= 0.75){
			ArrayList<Integer> possibleOpenings = getOpeningListsByRate(KB, 0.75);
			if (possibleOpenings.size() <= 1){
				result = maxScoreID;
			}
			else{
				Random r = new Random();
				int index = r.nextInt(possibleOpenings.size() - 1);
				result = possibleOpenings.get(index);
			}
		}
		else{
			ArrayList<Integer> unusedOpeningLists = getUnusedOpeningLists(KB);
			if (unusedOpeningLists.size() > 0){
				if (unusedOpeningLists.size() == 1){
					result = unusedOpeningLists.get(0);
				}
				else{
					Random r = new Random();
					int index = r.nextInt(unusedOpeningLists.size() - 1);
					result = unusedOpeningLists.get(index);
				}
			}
			else{
				ArrayList<Integer> allOpeningListsByRace = getOpeningListsByRace(game.getEnemies().get(0).getRaceID());
				if (allOpeningListsByRace.size() - 1 == 0){
					result = allOpeningListsByRace.get(0);
				}
				else{
					Random r = new Random();
					int index = r.nextInt(allOpeningListsByRace.size() - 1);
					result = allOpeningListsByRace.get(index);
				}
			}
		}
		return result;
	}
	
	private OpeningList getOpeningListByID(int ID){
		OpeningList result = new OpeningList(-1, "empty", game.getEnemies().get(0).getRaceID());
		for (int i = 0; i < allOpeningLists.size(); i++) {
			OpeningList ol = allOpeningLists.get(i);
			if (ol.getID() == ID){
				return ol;
			}
		}
		return result;
	}
	
	private ArrayList<Integer> getOpeningListsByRace(int raceID){
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < allOpeningLists.size(); i++) {
			OpeningList ol = allOpeningLists.get(i);
			if (ol.getAgainstRace() == raceID){
				result.add(ol.getID());
			}
		}
		return result;
	}
	
	private ArrayList<Integer> getOpeningListsByRate(HashMap<Integer, double[]> KB, double minRate){
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		for (Entry<Integer, double[]> entry: KB.entrySet()) {
			double[] stats = entry.getValue();
			double rate = stats[1];
			if (rate >= minRate){
				result.add(entry.getKey());
			}
		}
		
		return result;
	}
	
	private ArrayList<Integer> getUnusedOpeningLists(HashMap<Integer, double[]> KB){
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < allOpeningLists.size(); i++) {
			OpeningList ol = allOpeningLists.get(i);
			if (!KB.containsKey(ol.getID())){
				result.add(ol.getID());
			}
		}
		return result;
	}
	
	private void initializeOpenings(){
		ArrayList<OpeningList> aol = new ArrayList<OpeningList>();
		OpeningList ol;
		
		/////////////////////////////////////////////////////
		//              OPENINGS AGAINST ZERG              //
		/////////////////////////////////////////////////////
		ol = new OpeningList(1, "9/9 Proxy Gateway", Race.ZERG.ordinal());
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 4, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 5, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 6, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 7, OpeningTask.SCOUTING_ACTION, -1));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 7, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 8, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Pylon.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 8, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Gateway.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Gateway.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.SCOUTING_ACTION, -1));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 10, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 11, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Zealot.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 13, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Pylon.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 13, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Zealot.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 15, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Zealot.ordinal()));
		aol.add(ol);
		
		/*
		ol = new OpeningList("9/9 Gateway", Race.ZERG.ordinal());
		//ol.add(new OpeningTask(OpeningTask., , OpeningTask., UnitTypes.Protoss_.ordinal()));
		aol.add(ol);
		
		ol = new OpeningList("9/10 Gateway", Race.ZERG.ordinal());
		aol.add(ol);
		*/
		
		ol = new OpeningList(2, "10/10 Gateway", Race.ZERG.ordinal());
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 4, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 5, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 6, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 7, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 8, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Pylon.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 8, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		//TODO mal by mat dve obmedzenia 10 supply a 200 minerals...
		ol.add(new OpeningTask(OpeningTask.MINERALS_CONSTRAINT, 200, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Gateway.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 10, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Gateway.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 10, OpeningTask.SCOUTING_ACTION, -1));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 10, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 11, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Pylon.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 11, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 12, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Zealot.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 14, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Zealot.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 16, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 17, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 18, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Zealot.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 20, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Zealot.ordinal()));
		aol.add(ol);
		
		/*
		ol = new OpeningList("10/11 Gateway", Race.ZERG.ordinal());
		aol.add(ol);
		
		ol = new OpeningList("10/12 Gateway", Race.ZERG.ordinal());
		aol.add(ol);
		*/
		
		/////////////////////////////////////////////////////
		//             OPENINGS AGAINST TERRAN             //
		/////////////////////////////////////////////////////
		ol = new OpeningList(3, "14 Nexus", Race.TERRAN.ordinal());
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 4, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 5, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 6, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 7, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 8, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Pylon.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 8, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 10, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 11, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 12, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 13, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Nexus.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 13, OpeningTask.SCOUTING_ACTION, -1));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 13, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 14, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Gateway.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 14, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 15, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Assimilator.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 15, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 16, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 17, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Cybernetics_Core.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 17, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Gateway.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 17, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Zealot.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 19, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 20, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 21, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Pylon.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 21, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Dragoon.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 21, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Dragoon.ordinal()));
		//TODO!!! zakomponovat upgrady
		//ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 25, OpeningTask.PRODUCING_ACTION, UpgradeTypes.Protoss_Ground_Weapons.ordinal()));
		/*
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 27, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Pylon.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 27, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Dragoon.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 27, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Dragoon.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 33, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Pylon.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 35, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Dragoon.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 35, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Dragoon.ordinal()));
		*/
		aol.add(ol);
		
		/////////////////////////////////////////////////////
		//             OPENINGS AGAINST PROTOSS            //
		/////////////////////////////////////////////////////
		ol = new OpeningList(4, "9/9 Gateway", Race.PROTOSS.ordinal());
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 4, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 5, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 6, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 7, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 8, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Pylon.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 8, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Gateway.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Gateway.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.SCOUTING_ACTION, -1));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 10, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 11, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Zealot.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 13, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Pylon.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 13, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Zealot.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 15, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Zealot.ordinal()));
		aol.add(ol);
		
		allOpeningLists = aol;
	}
	
	private void sendText(String msg){
		if(debug) game.sendText("OM: " + msg);
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
	
	public OpeningTask(int constraintType, int constraint, int action, int unitTypeID){
		this.constraintType = constraintType;
		this.constraint = constraint;
		this.action = action;
		this.unitTypeID = unitTypeID;
	}
}

class OpeningList{
	private ArrayList<OpeningTask> self;
	private int iterator;
	private int againstRace;
	private int ID;
	private String name;
	
	public OpeningList(int ID, String name, int againstRace){
		this.name = name;
		this.againstRace = againstRace;
		this.ID = ID;
		self = new ArrayList<OpeningTask>();
		iterator = 0;
	}
	
	public void add(OpeningTask o){
		self.add(o);
	}
	
	public int getAgainstRace(){
		return againstRace;
	}
	
	public int getID(){
		return ID;
	}
	
	public String getName(){
		return name;
	}

	public OpeningTask getNextTask(){
		return self.get(iterator);
	}
	
	public void completeTask(){
		iterator++;
	}
	
	public boolean isCompleted(){
		return (iterator == self.size());
	}
}

