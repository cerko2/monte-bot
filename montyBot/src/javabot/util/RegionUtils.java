package javabot.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import javabot.util.BWColor;

import javabot.JNIBWAPI;
import javabot.model.Map;
import javabot.model.Region;
import javabot.model.Unit;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.types.WeaponType;

public class RegionUtils {
	
	public static Region getRegion(Map map, Unit unit){
		return getRegion(map, new Point(unit.getX(), unit.getY()));
	}
	
	public static Region getRegion(Map map, Point point){
		ArrayList<Region> regions = map.getRegions();
		for (Region region : regions){
			int[] coordinates = region.getCoordinates();
			double[] xCoords = new double[coordinates.length / 2];
			double[] yCoords = new double[coordinates.length / 2];
			for (int i = 0; i < xCoords.length; i++){
				xCoords[i] = coordinates[i * 2];
				yCoords[i] = coordinates[(i * 2) + 1];
			}
			if (pnpoly(xCoords.length, xCoords, yCoords, point.getX(), point.getY())){
				return region;
			}
		}
		System.out.println("region not found");
		return null;
	}
	
	public static boolean isInRegion(Region region, Unit unit){
		return isInRegion(region, new Point(unit.getX(), unit.getY()));
	}
	
	public static boolean isInRegion(Region region, Point point){
		int[] coordinates = region.getCoordinates();
		double[] xCoords = new double[coordinates.length / 2];
		double[] yCoords = new double[coordinates.length / 2];
		for (int i = 0; i < xCoords.length; i++){
			xCoords[i] = coordinates[i * 2];
			yCoords[i] = coordinates[(i * 2) + 1];
		}
		return pnpoly(xCoords.length, xCoords, yCoords, point.getX(), point.getY());
	}
	
	private static boolean pnpoly(int nvert, double[] vertx, double[] verty, double testx, double testy){
		int i, j = 0;
		boolean c = false;
		for (i = 0, j = nvert-1; i < nvert; j = i++) {
			if ( ((verty[i]>testy) != (verty[j]>testy)) &&
					(testx < (vertx[j]-vertx[i]) * (testy-verty[i]) / (verty[j]-verty[i]) + vertx[i]) )
				c = !c;
		}
		return c;
	}
	
	public static void drawRegion(JNIBWAPI game, Region region, int color){
		
		if (region == null)
			return;
		
		int[] coords = region.getCoordinates();
		for (int i = 0; i < coords.length / 2; i++){
			int nn = i + 1;
			if (nn == coords.length / 2){
				nn = 0;
			}
			game.drawLine(coords[i * 2], coords[(i * 2) + 1],coords[nn * 2], coords[(nn * 2) + 1], color, false);
		}
	}
	
}
