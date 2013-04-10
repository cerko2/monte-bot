package javabot.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.model.Player;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.Octave;

public class OpponentModeling extends AbstractManager {
	
	private JNIBWAPI bwapi;
	private OpponentPositioning opponentPositioningModule;
	private Octave octave;
	private Player opponent;
	private ArrayList<Integer> relevantUnitTypes;
	private HashMap<Integer, ArrayList<Float>> theta;		// key is the unitTypeID, and value is the Theta (arraylist of float parameters) for that unit type
	
	public OpponentModeling(JNIBWAPI bwapi, OpponentPositioning op) {
		this.bwapi = bwapi;
		this.opponentPositioningModule = op;
		this.octave = new Octave(bwapi);
		this.theta = new HashMap<>();
	}
	
	// On game start, this reads the right training set file for all relevant unit types
	// and computes Theta parameters using gradient descent. This Theta will be used
	// for prediction during this game.
	public void gameStarted() {
		// set the "this.opponent" variable
		this.opponent = bwapi.getEnemies().get(0);
		// get the list of relevant unit types
		this.relevantUnitTypes = getRelevantUnitTypes(opponent.getRaceID());
		
		// compute Theta for all the relevant unitTypes
		for (int ut : relevantUnitTypes) {
			bwapi.printText("computing Theta for "+bwapi.getUnitType(ut).getName());
			this.theta.put(ut, octave.computeTheta(this.opponent, ut, getOrderOfPolynomial(ut)));
			
			// DEBUG:
			String dbg = "";
			for (Float f : theta.get(ut).subList(0, 8)) {
				dbg += String.valueOf(f)+" ";
			}
			bwapi.printText(dbg+"...");
			
			
			
		}
	}
	
	// This should return an array of all the units we predict 
	// the enemy will build during following 120 seconds. 
	// If we predict 5 marines, the typeID of Marine will be in this array 5 times.
	public ArrayList<Integer> getPredictedUnits() {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		
		// TODO
		
		return ret;
	}

	// Returns the most suitable order of polynomial to be used for the 
	// prediction of a given unit type. This was determined empirically.
	private int getOrderOfPolynomial(int typeID) {
		
		int[] threes = {UnitTypes.Terran_Vulture.ordinal(), UnitTypes.Terran_Goliath.ordinal(), UnitTypes.Zerg_Lurker.ordinal(), UnitTypes.Zerg_Defiler.ordinal()};
		int[] fours = {UnitTypes.Terran_Marine.ordinal(), UnitTypes.Terran_Science_Vessel.ordinal(), UnitTypes.Protoss_Zealot.ordinal(), UnitTypes.Protoss_Dragoon.ordinal(), UnitTypes.Protoss_Dark_Templar.ordinal(), UnitTypes.Protoss_High_Templar.ordinal(), UnitTypes.Zerg_Mutalisk.ordinal()};
		
		if (Arrays.asList(threes).contains(typeID)) {
			return 3;
		} else if (Arrays.asList(fours).contains(typeID)) {
			return 4;
		}
		
		// default order is 2
		return 2;
	}
	
	private boolean isRelevantUnitType(int unitTypeID) {
		UnitType typ = bwapi.getUnitType(unitTypeID);
		return (typ != null) && ((!typ.getName().contains("Hero")) && !typ.isSpell() && !typ.isWorker() && !typ.isBuilding() && typ.getID() != UnitTypes.Zerg_Larva.ordinal() && typ.getID() != UnitTypes.Zerg_Egg.ordinal() && typ.getID() != UnitTypes.Zerg_Lurker_Egg.ordinal() && typ.getID() != UnitTypes.Zerg_Cocoon.ordinal()&& typ.getID() != UnitTypes.Zerg_Broodling.ordinal() && typ.getID() != UnitTypes.Zerg_Overlord.ordinal() && typ.getID() != UnitTypes.Protoss_Scarab.ordinal() && typ.getID() != UnitTypes.Protoss_Interceptor.ordinal() && typ.getID() != UnitTypes.Terran_Nuclear_Missile.ordinal() && typ.getID() != UnitTypes.Terran_Vulture_Spider_Mine.ordinal());
	}
	
	private ArrayList<Integer> getRelevantUnitTypes(int raceID) {
		ArrayList<Integer> ret = new ArrayList<>();
		for (int i=0; i<150; i++) {
			if (isRelevantUnitType(i)) {
				if (bwapi.getUnitType(i).getRaceID() == this.opponent.getRaceID()) {
					ret.add(i);
				}
			}
		}
		return ret;
	}
	
}
