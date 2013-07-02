package javabot.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import sun.util.locale.BaseLocale;

import javabot.util.BWColor;

import javabot.JNIBWAPI;
import javabot.model.BaseLocation;
import javabot.model.ChokePoint;
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
		game.drawText(region.getCenterX(), region.getCenterY(),String.valueOf(region.getID()), false);
		for (ChokePoint c : region.getChokePoints()) {
			game.drawLine(c.getFirstSideX(),c.getFirstSideY(),c.getSecondSideX(),c.getSecondSideY(), BWColor.RED, false);
		}
	}
	
	public static Region getRegionById(JNIBWAPI bwapi, int regionId) {
		for (Region r : bwapi.getMap().getRegions()) {
			if (r.getID() == regionId) return r;
		}
		return null;
	}
	
	public static NaturalBase getNaturalChoke(JNIBWAPI bwapi, Region home) {
		HashSet<Integer> regs = new HashSet<>();
		for (ChokePoint choke : home.getChokePoints()) {
			if (choke.getFirstRegionID() != home.getID()) regs.add(choke.getFirstRegion().getID());
			if (choke.getSecondRegionID() != home.getID()) regs.add(choke.getSecondRegion().getID());
		}
		// set natural
		Region natural = null;
		for (BaseLocation b : bwapi.getMap().getBaseLocations()) {
			if (regs.contains(b.getRegionID())) {
				Region r = getRegionById(bwapi, b.getRegionID());
				boolean homeChoke = false;
				boolean awayChoke = false;
				for (ChokePoint c : r.getChokePoints()) {
					if (chokeObstructedByNeutral(bwapi, c)) continue;
					if ((c.getFirstRegionID() != home.getID()) && (c.getSecondRegionID() != home.getID())) {
						awayChoke = true;
					} else {
						homeChoke = true;
					}
				}
				if (homeChoke && awayChoke) {
					natural = r;
					break;
				}
			}
		}

		if (!regs.isEmpty() && natural != null) {
			// outer choke
			ChokePoint furthestChoke = null;
			for (ChokePoint choke : natural.getChokePoints()) {
				if (// (!regs.contains(choke.getFirstRegion().getID()) || !regs.contains(choke.getSecondRegion().getID())) &&
						(choke.getFirstRegion().getID() != home.getID() && choke.getSecondRegion().getID() != home.getID() ) && 
						(!chokeObstructedByNeutral(bwapi, choke))) {
					furthestChoke = choke;
					break;
				}
			}
			if (furthestChoke == null) return null;
			return new NaturalBase(natural, furthestChoke);
		}

		System.out.println("Natural couldn't be found.");
		return null;
	}
	
		
	private static boolean chokeObstructedByNeutral(JNIBWAPI bwapi, ChokePoint choke) {
		for (Unit u : bwapi.getAllStaticNeutralUnits()) {
			if (UnitUtils.getDistance(u.getX(), u.getY(), choke.getCenterX(), choke.getCenterY()) <= choke.getRadius() ) return true;
		}
		return false;
	}
	
}
