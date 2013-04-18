package javabot.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.macro.Boss;
import javabot.model.Player;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.Octave;
import javabot.util.OctaveThread;
import javabot.util.SituationVector;

public class OpponentModeling extends AbstractManager {
	
	private JNIBWAPI bwapi;
	private OpponentPositioning opponentPositioningModule;
	private Boss boss;
	private Octave octave;
	public Player opponent;
	public ArrayList<Integer> relevantUnitTypes;
	public ArrayList<Integer> destroyedEnemyBuildingTypes; 
	private HashMap<Integer, ArrayList<Float>> theta;		// key is the unitTypeID, and value is the Theta (arraylist of float parameters) for that unit type
	
	public OpponentModeling(JNIBWAPI bwapi, OpponentPositioning op, Boss b) {
		this.bwapi = bwapi;
		this.opponentPositioningModule = op;
		this.boss = b;
		this.octave = new Octave(bwapi);
		this.theta = new HashMap<>();
		this.destroyedEnemyBuildingTypes = new ArrayList<Integer>();
	}
	
	// On game start, this reads the right training set file for all relevant unit types
	// and computes Theta parameters using gradient descent. This Theta will be used
	// for prediction during this game.
	public void gameStarted() {
		// set the "this.opponent" variable
		this.opponent = bwapi.getEnemies().get(0);
		// get the list of relevant unit types
		this.relevantUnitTypes = getRelevantUnitTypes(opponent.getRaceID());
		
		// compute Theta for all the relevant unitTypes (in a separate thread)
		OctaveThread oc = new OctaveThread(bwapi, this, boss);
	}
	
	// This should return an array of all the units we predict 
	// the enemy will build during following 120 seconds. 
	// If we predict 5 marines, the typeID of Marine will be in this array 5 times.
	public ArrayList<Integer> getPredictedUnits() {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		
		// get the current situation vector
		SituationVector sitVector = new SituationVector(bwapi, opponentPositioningModule, this);
		
		// loop over relevant unit types 
		String debug = "Predicted: ";
		for (int ut : this.relevantUnitTypes) {
			// if theta for this unit type is already computed
			if (theta.containsKey(ut)) {
				// predict how many new units of this type will opponent produce
				int pred = roundFloat(computePolynomial(sitVector, theta.get(ut), getOrderOfPolynomial(ut)));
				if (pred > 0) {
					debug += String.valueOf(pred)+"x"+bwapi.getUnitType(ut).getName().substring(bwapi.getUnitType(ut).getName().indexOf(" "))+", ";
					// and add these "units" to ret
					for (int i = 1; i <= pred; i++) {
						ret.add(ut);
					}
				}
			}
		}
		if (this.boss.OPPONENT_MODELLING_DEBUG) {
			System.out.println("Situation: "+sitVector.getSituationOrder1());
			System.out.println(debug);	
		}
		
		return ret;
	}

	// Returns the most suitable order of polynomial to be used for the 
	// prediction of a given unit type. This was determined empirically.
	public int getOrderOfPolynomial(int typeID) {
		
		ArrayList<Integer> threes = new ArrayList<>();
		threes.add(UnitTypes.Terran_Vulture.ordinal()); threes.add(UnitTypes.Terran_Goliath.ordinal());
		threes.add(UnitTypes.Zerg_Lurker.ordinal()); threes.add(UnitTypes.Zerg_Defiler.ordinal());
		ArrayList<Integer> fours = new ArrayList<>();
		fours.add(UnitTypes.Terran_Marine.ordinal()); fours.add(UnitTypes.Terran_Science_Vessel.ordinal());
		fours.add(UnitTypes.Protoss_Zealot.ordinal()); fours.add(UnitTypes.Protoss_Dragoon.ordinal());
		fours.add(UnitTypes.Protoss_Dark_Templar.ordinal()); fours.add(UnitTypes.Protoss_High_Templar.ordinal());
		fours.add(UnitTypes.Zerg_Mutalisk.ordinal());
		
		if (threes.contains(typeID)) {
			return 3;
		} else if (fours.contains(typeID)) {
			return 4;
		}
		
		// default order is 2
		return 2;
	}
	
	public void unitDestroy(int unitID){
		int typeID = opponentPositioningModule.getTypeOf(unitID);
		if (typeID == -1) return;
		UnitType typ = bwapi.getUnitType(typeID);
		if (
				(typ.isBuilding()) &&
				(bwapi.getUnit(unitID).getPlayerID() == opponent.getID()) &&
				(!destroyedEnemyBuildingTypes.contains(typ.getID()))) 
			destroyedEnemyBuildingTypes.add(typ.getID());
	}
	
	private Float computePolynomial(SituationVector sitVector, ArrayList<Float> th, int order) {
		Float ret = (float) th.get(0);
		
		ArrayList<Double> x = sitVector.getSituationHigherOrder(order);
		for (int i = 1; i < th.size(); i++ ) {
			ret += (float)(x.get(i-1)*th.get(i));
		}
		
		return ret;
	}
	
	private int roundFloat(Float f) {
		return Math.max(0, Math.round(f));
	}
	
	public void setThetaUnit(int ut, ArrayList<Float> th) {
		this.theta.put(ut, new ArrayList<Float>(th));
	}

	private boolean isRelevantUnitType(int unitTypeID) {
		UnitType typ = bwapi.getUnitType(unitTypeID);
		return (typ != null) && ((!typ.getName().contains("Hero")) && !typ.isSpell() && !typ.isWorker() && !typ.isBuilding() && typ.getID() != UnitTypes.Zerg_Larva.ordinal() && typ.getID() != UnitTypes.Zerg_Egg.ordinal() && typ.getID() != UnitTypes.Zerg_Lurker_Egg.ordinal() && typ.getID() != UnitTypes.Zerg_Cocoon.ordinal()&& typ.getID() != UnitTypes.Zerg_Broodling.ordinal() && typ.getID() != UnitTypes.Zerg_Overlord.ordinal() && typ.getID() != UnitTypes.Protoss_Scarab.ordinal() && typ.getID() != UnitTypes.Protoss_Interceptor.ordinal() && typ.getID() != UnitTypes.Terran_Nuclear_Missile.ordinal() && typ.getID() != UnitTypes.Terran_Vulture_Spider_Mine.ordinal() && typ.getID() != UnitTypes.Terran_Siege_Tank_Siege_Mode.ordinal() && typ.getID() != UnitTypes.Terran_Civilian.ordinal());
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
