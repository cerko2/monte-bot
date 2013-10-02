package javabot.strategy;

import java.util.Collection;
import java.util.HashMap;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;

public class OpponentPositioning extends AbstractManager{
	
	private JNIBWAPI game;
	
	private HashMap<Integer, Unit> enemyUnits;
	private HashMap<Integer, Unit> enemyMines;
	
	private HashMap<Integer, Integer> lastSeen;
	private HashMap<Integer, Integer> typeRegister;
	
	public OpponentPositioning(JNIBWAPI game){
		this.game = game;
	}
	
	public void gameStarted(){
		enemyUnits = new HashMap<Integer, Unit>();
		enemyMines = new HashMap<Integer, Unit>();
		lastSeen = new HashMap<Integer, Integer>();
		typeRegister = new HashMap<Integer, Integer>();
	}
	
	public void gameUpdate(){
		for (Unit unit : game.getEnemyUnits()){
			if (unit.getTypeID() == UnitTypes.Terran_Vulture_Spider_Mine.ordinal()){
				enemyMines.put(unit.getID(), unit);
			}
			else {
				enemyUnits.put(unit.getID(), unit);
			}
			lastSeen.put(unit.getID(), game.getFrameCount());
			typeRegister.put(unit.getID(), unit.getTypeID());
		}
	}
	
	public void unitDestroy(int unitID){
		if (enemyMines.containsKey(unitID)){
			enemyMines.remove(unitID);
			lastSeen.remove(unitID);
		}
		else if (enemyUnits.containsKey(unitID)){
			enemyUnits.remove(unitID);
			lastSeen.remove(unitID);
		}
	}
	
	public Collection<Unit> getEnemyUnits(){
		if ( enemyUnits == null ) return null;
		return enemyUnits.values();
	}
	
	public Collection<Unit> getEnemyMines(){
		return enemyMines.values();
	}
	
	public int getLastSeen(int unitID) {
		if (lastSeen.containsKey(unitID)) {
			return lastSeen.get(unitID);
		} else {
			return 0;
		}
	}
	
	// returns the typeID of (possibly dead) enemy unit
	public int getTypeOf(int unitID) {
		if (typeRegister.containsKey(unitID)) {
			return typeRegister.get(unitID);
		} else {
			return -1;
		}
	}
}
