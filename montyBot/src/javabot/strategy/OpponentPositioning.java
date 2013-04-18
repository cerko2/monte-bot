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
	
	public OpponentPositioning(JNIBWAPI game){
		this.game = game;
	}
	
	public void gameStarted(){
		enemyUnits = new HashMap<Integer, Unit>();
		enemyMines = new HashMap<Integer, Unit>();
	}
	
	public void gameUpdate(){
		for (Unit unit : game.getEnemyUnits()){
			if (unit.getTypeID() == UnitTypes.Terran_Vulture_Spider_Mine.ordinal()){
				enemyMines.put(unit.getID(), unit);
			}
			else {
				enemyUnits.put(unit.getID(), unit);
			}
		}
	}
	
	public void unitDestroy(int unitID){
		if (enemyMines.containsKey(unitID)){
			enemyMines.remove(unitID);
		}
		else if (enemyUnits.containsKey(unitID)){
			enemyUnits.remove(unitID);
		}
	}
	
	public Collection<Unit> getEnemyUnits(){
		if ( enemyUnits == null ) return null;
		return enemyUnits.values();
	}
	
	public Collection<Unit> getEnemyMines(){
		return enemyMines.values();
	}
}
