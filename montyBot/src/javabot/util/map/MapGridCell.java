package javabot.util.map;

import java.util.HashSet;
import java.util.Set;

import javabot.model.Unit;
import javabot.util.Position;

public class MapGridCell {
	
	public int lastVisited;
	public int enemyTimeLastSeen;
	public Set<Unit> myUnits;
	public Set<Unit> enemyUnits;
	public Set<Unit> neutralUnits;
	public Position position;
	
	public MapGridCell(Position position){
		lastVisited = 0;
		enemyTimeLastSeen = 0;
		myUnits = new HashSet<Unit>();
		enemyUnits = new HashSet<Unit>();
		neutralUnits = new HashSet<Unit>();
		this.position = position;
	}
	
	public void clearUnitsOnCell(){
		myUnits.clear();
		enemyUnits.clear();
		neutralUnits.clear();
	}
}
