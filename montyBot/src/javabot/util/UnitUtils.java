package javabot.util;

import javabot.JNIBWAPI;
import javabot.model.Unit;

public class UnitUtils {
	
	public static int getNearestUnit(JNIBWAPI game, int unitTypeID, int x, int y) {
		int nearestID = -1;
		double nearestDist = 9999999;
		
		for (Unit unit : game.getMyUnits()) {
			if ((unit.getTypeID() != unitTypeID) || (!unit.isCompleted()) || unit.isGatheringGas() || !unit.isExists()) continue;
			if (nearestID == -1) {
				nearestID = unit.getID();
				continue;
			}
			double dist = Math.sqrt(Math.pow(unit.getX()-game.getUnit(nearestID).getX(),2) + Math.pow(unit.getY()-game.getUnit(nearestID).getY(),2));
			if (dist < nearestDist) {
				nearestID = unit.getID();
				nearestDist = dist;
			}
		}
		
		return nearestID;		
	}
	
	public static int getDistance(Unit unit1, Unit unit2){
		return getDistance(unit1.getX(), unit1.getY(), unit2.getX(), unit2.getY());
	}
	
	public static int getDistance(int x1, int y1, int x2, int y2){
		return (int) Math.sqrt(Math.pow(x1 - x2,2) + Math.pow(y1 - y2,2));
	}
	
}
