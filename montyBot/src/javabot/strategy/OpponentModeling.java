package javabot.strategy;

import java.util.ArrayList;

import javabot.AbstractManager;
import javabot.JNIBWAPI;

public class OpponentModeling extends AbstractManager {
	
	private JNIBWAPI bwapi;
	private OpponentPositioning opponentPositioningModule;
	
	public OpponentModeling(JNIBWAPI bwapi, OpponentPositioning op) {
		this.bwapi = bwapi;
		this.opponentPositioningModule = op;
	}
	
	// This should return an array of all the units we predict 
	// the enemy will build during following 120 seconds. 
	// If we predict 5 marines, the typeID of Marine will be in this array 5 times.
	public ArrayList<Integer> getPredictedUnits() {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		
		// TODO
		
		return ret;
	}
	
}
