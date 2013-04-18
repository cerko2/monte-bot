package javabot.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import javabot.util.BWColor;

import javabot.JNIBWAPI;
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
	
	/**
	 * Air Units Can Be On a spot that is not in any REGION
	 * @param map
	 * @param point
	 * @return
	 */
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
//		System.out.println("region not found");
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
	
	/*** POZICNE METODY PRE MC PLANNER **/
	
	public static Region findNearestRegion( Map map, Unit u )
	{
		double distance = 100000000.00;
		
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
	
	/**
	 * Only for air units ( may need to adjust the distance )
	 * @return
	 */
	public static ArrayList<Region> surroundingRegions( Map map, Unit u )
	{
		ArrayList<Region> result = new ArrayList<Region>();
		for ( Region r : map.getRegions() )
		{
			if ( UnitUtils.getDistance( r.getCenterX(), r.getCenterY(), u.getX(), u.getY() ) < 5000 )
			{
				result.add(r);
			}
		}
		return result;
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
	
	public static double airPathToRegion( Region from, Region to )
	{
		return UnitUtils.getDistance(from.getCenterX(), from.getCenterY(), to.getCenterX(), to.getCenterY());
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
		
		if ( connection == null )
		{	
			System.out.println( "Region Utils: neexistuje spolocny chokepoint" );
			return -1;
		}
		
		return UnitUtils.getDistance( connection.getCenterX(), connection.getCenterY(), from.getCenterX(), from.getCenterY()) +
			   UnitUtils.getDistance( connection.getCenterX(), connection.getCenterY(), to.getCenterX(), to.getCenterY());
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
	
}
