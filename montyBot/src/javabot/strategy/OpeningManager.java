package javabot.strategy;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.macro.Boss;
import javabot.model.ChokePoint;
import javabot.model.Map;
import javabot.model.Race;
import javabot.model.Region;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;
import javabot.util.NaturalBase;
import javabot.util.RegionUtils;

public class OpeningManager extends AbstractManager{
	private boolean debug = true;
	private JNIBWAPI game;
	private Boss boss;
	private boolean isActive;
	private ArrayList<OpeningList> allOpeningLists;
	private OpeningList openingList;
	private boolean computedWallIn = false;
	ArrayList<double[]> openingsChances = new ArrayList<double[]>();
	ArrayList<String> debugOpeningsPercentageChance = new ArrayList<String>();
	String nextTask = "";


	// main base region
	private Region home; 

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
			drawText(10, 192, "Opening Manager: active");
			//drawText(10, 208, "Next task: " + nextTask);
			drawText(10, 208, "Selected opening: " + openingList.getName());
			if (!openingList.isCompleted()){
				perform(openingList.getNextTask());
				
				int height = 224;
				if (debugOpeningsPercentageChance.size() == 0){
					drawText(10, height, "Opening was selected randomly.");
				}
				else{
					drawText(10, height, "Opening lists chances:");
					height += 16;
					for (int i = 0; i < debugOpeningsPercentageChance.size(); i++){
						drawText(20, height, debugOpeningsPercentageChance.get(i));
						height += 16;
					}
				}
			}
			else{
				setInactive();
				//game.printText("Opening Manager has ended.");
			}
		}
		else{
			boss.getUnitProductionManager().setFreeMode(true); // ukoncenie diktatury Azder.
			boss.getBuildManager().setFreeMode(true); // vyhlasenie nezavislosti Azder.
			drawText(10, 176, "Opening Manager: disabled");
		}
		
		// set the location of main and natural base 
		if (game.getFrameCount() == 1) {
			Unit homeNexus = game.getMyUnits().get(0);
			for (Unit u : game.getMyUnits()) {
				if (u.getTypeID() == UnitTypes.Protoss_Nexus.ordinal()) {
					homeNexus = u;
					break;
				}
			}
			this.home = RegionUtils.getRegion(game.getMap(), new Point(homeNexus.getX(),homeNexus.getY()));

			// ... and compute wall-in if necessary
			if (openingList.containWallIn() && !computedWallIn){
				NaturalBase nat = RegionUtils.getNaturalChoke(game, home);
				if (nat != null) boss.getWallInModule().smartComputeWall(nat.chokepoint, nat.region, openingList.retrieveWallInBuildings());
				computedWallIn = true;
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
	
	public int getOpeningType(){
		return openingList.getType();
	}

	public boolean isWallinOpening(){
		return openingList.containWallIn();
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
					boss.getBuildManager().createBuilding(openingList.getNextTask().unitTypeID,openingList.getNextTask().wallIn);
					openingList.completeTask();
					//sendText("Build building");
					nextTask = "Build building";
				}
				else{
					if (task.unitTypeID == UnitTypes.Protoss_Probe.ordinal()){
						boss.getWorkerManager().buildWorker();
						openingList.completeTask();
						//sendText("Train worker");
						nextTask = "Train worker";
					}
					else{
						boss.getUnitProductionManager().createUnit(openingList.getNextTask().unitTypeID);
						openingList.completeTask();
						//sendText("Train unit");
						nextTask = "Train unit";
					}
				}
			break;
			case OpeningTask.SCOUTING_ACTION:
				boss.startScouting();
				openingList.completeTask();
				//sendText("Start scouting");
				nextTask = "Start scouting";
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
				openingID = getRandomOpeningListID(enemyRace);
			}
			else{
				openingID = findSuitableOpeningID(racesKB);
			}
		}
		else{
			openingID = findSuitableOpeningID(playersKB);
		}
		//DEBUG: If WALLIN debug in ON, always choose opening number 5
		if (boss.WALLIN_DEBUG) openingID = 5;
		openingList = getOpeningListByID(openingID);
		//game.printText("Bol zvoleny opening: " + openingList.getName());
	}	
	
	private int findSuitableOpeningID(HashMap<Integer, double[]> KB){
		int result = -1;
		double maxScore = Double.MIN_VALUE;
		Random r;
		ArrayList<Integer> unusedOpenings = getUnusedOpeningLists(KB);
		for (Entry<Integer, double[]> entry : KB.entrySet()) {
			double[] stats = entry.getValue();
			double[] os = {entry.getKey(), stats[1], 0};
			openingsChances.add(os);
		}
			
		double sum = 0;
		for (int i = 0; i < openingsChances.size(); i++){
			double[] temp = openingsChances.get(i);
			temp[1] += 10;
			if (temp[1] > maxScore){
				maxScore = temp[1];
			}
			sum += temp[1];
			openingsChances.set(i, temp);
		}
		
		if (unusedOpenings.size() > 0){
			sum += maxScore;
			String s = "Unused opening lists (" + (maxScore * 100 / sum) + " percent)";
			debugOpeningsPercentageChance.add(s);
		}
		
		for (int i = 0; i < openingsChances.size(); i++){
			double[] temp = openingsChances.get(i);
			temp[2] = temp[1] * 100 / sum;
			openingsChances.set(i, temp);
			String s = getOpeningListByID((int)temp[0]).getName() + " (" + temp[2] + " percent)";
			debugOpeningsPercentageChance.add(s);
		}

		r = new Random();
		double percentage = r.nextDouble() * 100;
		
		double currentPercentage = 0;
		for (int i = 0; i < openingsChances.size(); i++){
			currentPercentage += openingsChances.get(i)[2];
			if (percentage <= currentPercentage){
				int openingID = (int)openingsChances.get(i)[0];
				result = openingID;
				return result;
			}
		}
		
		if (unusedOpenings.size() > 0){
			
			if (unusedOpenings.size() == 1){
				result = unusedOpenings.get(0);
			}
			else{
				r = new Random();
				int index = r.nextInt(unusedOpenings.size() - 1);
				result = unusedOpenings.get(index);
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
	
	private ArrayList<Integer> getOpeningListsIDsByRace(int raceID){
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < allOpeningLists.size(); i++) {
			OpeningList ol = allOpeningLists.get(i);
			if (ol.getAgainstRace() == raceID){
				result.add(ol.getID());
			}
		}
		return result;
	}
	
	private int getRandomOpeningListID(int raceID){
		int result = -1;
		ArrayList<Integer> allOpeningListsByRace = getOpeningListsIDsByRace(raceID);
		if (allOpeningListsByRace.size() - 1 == 0){
			result = allOpeningListsByRace.get(0);
		}
		else{
			Random r = new Random();
			int index = r.nextInt(allOpeningListsByRace.size() - 1);
			result = allOpeningListsByRace.get(index);
		}
		return result;
	}
	
	private ArrayList<Integer> getUnusedOpeningLists(HashMap<Integer, double[]> KB){
		ArrayList<Integer> result = new ArrayList<Integer>();
		ArrayList<Integer> possibleOpeningLists = getOpeningListsIDsByRace(game.getEnemies().get(0).getRaceID());
		for (int i = 0; i < possibleOpeningLists.size(); i++) {
			int openingListID = possibleOpeningLists.get(i);
			if (!KB.containsKey(openingListID)){
				result.add(openingListID);
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
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 7, OpeningTask.SCOUTING_ACTION));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 7, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 8, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Pylon.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 8, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Gateway.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Gateway.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.SCOUTING_ACTION));
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
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 10, OpeningTask.SCOUTING_ACTION));
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
		
		//TODO: for @Miso Certicky - you can modify, clone or inspire from this opening to create other
		//wallIn opening
		ol = new OpeningList(5, "Fast Expand Forge Walling", Race.ZERG.ordinal(), OpeningList.FAST_EXPAND_TYPE);
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 4, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 5, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 6, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 7, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 8, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Pylon.ordinal(), OpeningTask.WALL_IN));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 8, OpeningTask.SCOUTING_ACTION));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 8, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 10, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Forge.ordinal(), OpeningTask.WALL_IN));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 10, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Photon_Cannon.ordinal(), OpeningTask.WALL_IN));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 10, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Photon_Cannon.ordinal(), OpeningTask.WALL_IN));
		ol.add(new OpeningTask(OpeningTask.MINERALS_CONSTRAINT, 400, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Nexus.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 10, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Gateway.ordinal(), OpeningTask.WALL_IN));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 10, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Gateway.ordinal()));
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
		ol = new OpeningList(3, "14 Nexus", Race.TERRAN.ordinal(), OpeningList.FAST_EXPAND_TYPE);
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
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 13, OpeningTask.SCOUTING_ACTION));
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
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.SCOUTING_ACTION));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 9, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 10, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Probe.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 11, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Zealot.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 13, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Pylon.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 13, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Zealot.ordinal()));
		ol.add(new OpeningTask(OpeningTask.SUPPLY_CONSTRAINT, 15, OpeningTask.PRODUCING_ACTION, UnitTypes.Protoss_Zealot.ordinal()));
		aol.add(ol);
		
		allOpeningLists = aol;
	}
	
	private void drawText(int x, int y, String msg){
		if(debug) game.drawText(x, y, msg, true);
	}

}
	
class OpeningTask {
	public static final int SUPPLY_CONSTRAINT = 0;
	public static final int MINERALS_CONSTRAINT = 1;
	public static final int GASS_CONSTRAINT = 2;
	
	public static final int PRODUCING_ACTION = 0;
	public static final int SCOUTING_ACTION = 1;
	public static final boolean WALL_IN = true;
	
	public int constraintType;
	public int constraint;
	public int action;
	public int unitTypeID;
	public boolean wallIn;
	
	public OpeningTask(int constraintType, int constraint, int action, int unitTypeID, boolean wallIn){
		this.constraintType = constraintType;
		this.constraint = constraint;
		this.action = action;
		this.unitTypeID = unitTypeID;
		this.wallIn = wallIn;
	}
	
	public OpeningTask(int constraintType, int constraint, int action, int unitTypeID){
		this(constraintType, constraint, action, unitTypeID, false);
	}
	
	public OpeningTask(int constraintType, int constraint, int action){
		this(constraintType, constraint, action, -1, false);
	}
}

class OpeningList{
	public static final int NORMAL_TYPE = 0;
	public static final int FAST_EXPAND_TYPE = 1;
	public static final int PROXY_TYPE = 2;
	private ArrayList<OpeningTask> self;
	private int iterator;
	private int againstRace;
	private int ID;
	private String name;
	private int type;
	
	public OpeningList(int ID, String name, int againstRace, int type){
		this.name = name;
		this.againstRace = againstRace;
		this.type = type;
		this.ID = ID;
		self = new ArrayList<OpeningTask>();
		iterator = 0;
	}
	
	public OpeningList(int ID, String name, int againstRace){
		this(ID, name, againstRace, NORMAL_TYPE);
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
	
	public int getType(){
		return type;
	}
	
	public boolean containWallIn(){
		for (OpeningTask ot : self) {
			if (ot.wallIn) return true;
		}
		return false;
	}
	
	public ArrayList<Integer> retrieveWallInBuildings(){
		ArrayList<Integer> wallIn = new ArrayList<Integer>();
		for (OpeningTask ot : self) {
			if (ot.wallIn) wallIn.add(ot.unitTypeID);
		}
		return wallIn;
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

