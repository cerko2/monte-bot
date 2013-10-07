package javabot.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;

import javabot.util.BWColor;

import javabot.JNIBWAPI;
import javabot.model.BaseLocation;
import javabot.model.ChokePoint;
import javabot.model.Map;
import javabot.model.Region;
import javabot.model.Unit;

public class RegionUtils {
	
	public static Region getRegion(Map map, Unit unit){
		if ( unit == null ) return null;
		return getRegion(map, new Point(unit.getX(), unit.getY()));
	}
	
	public static double airPathToRegion( Region from, Region to )
	{
		return UnitUtils.getDistance(from.getCenterX(), from.getCenterY(), to.getCenterX(), to.getCenterY());
	}
	
	public static ChokePoint getConnectionChoke( Region from, Region to )
	{
	    ChokePoint connection = null;
        
        for ( ChokePoint r : from.getChokePoints() )
        {
            if ( connection != null )
                break;
            for ( ChokePoint t : to.getChokePoints() )
            {
                if ( ( r.getCenterX() == t.getCenterX() ) && ( r.getCenterY() == t.getCenterY() ) )
                {
                    connection = t;
                    break;
                }
            }
        }
        
        return connection;
	}
	
	/**
	 * Between 2 neighbouring regions
	 * @param from
	 * @param to
	 * @param map
	 * @return
	 */
	public static double groundPathToRegion( Region from, Region to, Map map )
	{
		ChokePoint connection = getConnectionChoke( from, to );		
		
		if ( connection == null )
		{	
			return airPathToRegion( from, to );
		}
		
		return UnitUtils.getDistance( connection.getCenterX(), connection.getCenterY(), from.getCenterX(), from.getCenterY()) +
			   UnitUtils.getDistance( connection.getCenterX(), connection.getCenterY(), to.getCenterX(), to.getCenterY());
	}
	
	public static Region findNearestRegion( Map map, Unit u )
	{
		double distance = 100000000.00;
		
		if ( u == null )
		{
			return map.getRegions().get( 0 );
		}
		
		Region nearestRegion = null;
		
		for ( Region r : map.getRegions() )
		{
			if ( UnitUtils.getDistance( r.getCenterX(), r.getCenterY(), u.getX(), u.getY() ) < distance )
			{
				distance 	  = UnitUtils.getDistance( r.getCenterX(), r.getCenterY(), u.getX(), u.getY() );
				nearestRegion = r;
			}
		}
		return nearestRegion;
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
	
		
	public static boolean chokeObstructedByNeutral( JNIBWAPI bwapi, ChokePoint choke ) 
	{
		for ( Unit u : bwapi.getAllStaticNeutralUnits() ) 
		{
			if ( UnitUtils.getDistance(u.getX(), u.getY(), choke.getCenterX(), choke.getCenterY()) <= choke.getRadius() ) 
				return true;
		}
		return false;
	}
	
	public static ArrayList<Region> getConnectedRegions( Map map, Region region )
	{
		ArrayList<Region> result = new ArrayList<Region>();
		for ( Region r : map.getRegions() )
		{
			if ( UnitUtils.getDistance( r.getCenterX(), r.getCenterY(), region.getCenterX(), region.getCenterY() ) < 800 )
			{
				result.add(r);
			}
		}
		return result;
	}
	
	public static Region getRegion( Map map, int regionId ) 
	{
		for ( Region r : map.getRegions() )
		{
			if ( r.getID() == regionId )
			{
				return r;
			}
		}
		return null;
	}
	
	public static boolean chokeBlockedByNeutral( JNIBWAPI bwapi, ChokePoint chokePoint )
	{
		for ( Unit u : bwapi.getAllStaticNeutralUnits() )
		{
			if ( UnitUtils.getDistance( chokePoint.getCenterX(), chokePoint.getCenterY(), u.getX(), u.getY() ) < 100 )
			{
				return true;
			}
		}
		
		return false;
		
	}

	public static ArrayList<Region> getGroundConnectedRegions( Region region, JNIBWAPI bwapi ) 
	{
		ArrayList<Region> result = new ArrayList<Region>();
		
		for ( Region r : getConnectedRegions( bwapi.getMap(), region ) )
		{
			for ( ChokePoint c : r.getChokePoints() )
			{
				if ( ( c.getFirstRegionID()  == region.getID() ) || 
				     ( c.getSecondRegionID() == region.getID() ) 
				   )
				{
					if ( !chokeBlockedByNeutral( bwapi, c ) )
					{
						result.add( r );
					}
				}
			}
		}
		return result;
	}
	
}
