package javabot.util;

import javabot.model.Unit;

public class UnitUtils {
	
	public static int getDistance(Unit unit1, Unit unit2){
		return getDistance(unit1.getX(), unit1.getY(), unit2.getX(), unit2.getY());
	}
	
	public static int getDistance(int x1, int y1, int x2, int y2){
		return (int) Math.sqrt(Math.pow(x1 - x2,2) + Math.pow(y1 - y2,2));
	}
	
}
