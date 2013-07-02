package javabot.combat;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.HashMap;

import javabot.JNIBWAPI;
import javabot.model.Region;
import javabot.util.RegionUtils;

public class EnemySquad extends Squad {
	
	final double ENEMY_BASE_ATTRACTION     = 1.0;
	final double ENEMY_SQUAD_ATTRACTION    = 1.0;
	final double OUR_BASE_ATTRACTION       = 1.0;
	final double OUR_SQUAD_ATTRACTION      = 1.0;

	ArrayList<Action> plan = new ArrayList<Action>(); 
	int ellapsedTime;
	ArrayList<Base> enemyBases;
	ArrayList<Base> ourBases;
	HashMap <Integer, EnemySquad> enemySquads;
	TreeMap<Integer, OurSquad> ourSquads;
	
	public EnemySquad( JNIBWAPI bwapi ) 
	{
		super(bwapi);
	}
	
	public void setSimulatorCollections (
			ArrayList<Base> enemyBases, 
			ArrayList<Base> ourBases, 
			HashMap <Integer, EnemySquad> enemySquads, 
			TreeMap<Integer, OurSquad> ourSquads 
		)
	{
		this.enemyBases  = enemyBases;
		this.ourBases    = ourBases;
		this.enemySquads = enemySquads;
		this.ourSquads   = ourSquads;
	}
	
	private synchronized Double evaluateRegion( Region r ) 
	{
		ArrayList<Double> values = new ArrayList<Double>();
		
		double result = 0.0;
		
		try 
		{
			
			for ( Base b : enemyBases )
			{
				values.add( RegionUtils.airPathToRegion( simulatorRegion, b.getRegion() ) * ENEMY_BASE_ATTRACTION );
			}
			
			for ( Base b : ourBases )
			{
				values.add( RegionUtils.airPathToRegion( simulatorRegion, b.getRegion() ) * OUR_BASE_ATTRACTION );
			}
			
		for ( Map.Entry<Integer, EnemySquad> e_squad : enemySquads.entrySet() )
		{
			
			double modifier =  ENEMY_SQUAD_ATTRACTION;
			values.add( RegionUtils.airPathToRegion( simulatorRegion, e_squad.getValue().getRegion( true ) ) * modifier );
		}
		
		for ( Map.Entry<Integer, OurSquad> entry : ourSquads.entrySet() )
		{
			double modifier = OUR_SQUAD_ATTRACTION;
			values.add( RegionUtils.airPathToRegion( simulatorRegion, entry.getValue().getRegion( true ) ) * modifier );
		}
		
		for ( Double d : values )
		{
			result += d;
		}
		
		}
		
		catch ( ConcurrentModificationException e )
		{
			System.err.append( "Concurrent modification error enemy squad" );
		}
		
		return result;
	}
	
	public HashMap<Integer, Double> evaluateNearbyRegions() 
	{
		ArrayList<Region> connectedRegions;
		if ( onlyFlyersInSquad() )
		{
			connectedRegions = RegionUtils.getConnectedRegions( bwapi.getMap(), simulatorRegion ); 
		}
		else
		{
			connectedRegions = RegionUtils.getGroundConnectedRegions( simulatorRegion, bwapi );
		}
		
		HashMap<Integer, Double> regions = new HashMap<Integer, Double>();
		
		for ( Region r : connectedRegions )
		{
			regions.put( r.getID(), evaluateRegion( r ) );
		}
		
		ArrayList<Double> wholeCake = new ArrayList<Double>();
		
		for ( Map.Entry<Integer, Double> entry :  regions.entrySet() )
		{
			wholeCake.add( entry.getValue() );
		}
		
		double sumOfCake = 0.0;
		
		for ( Double d : wholeCake )
		{
			sumOfCake += d;
		}
		
		double doNothing = sumOfCake * 0.3;

		if ( !regions.containsKey( simulatorRegion.getID() ) )
		{
			regions.put( simulatorRegion.getID(), doNothing );
		}
		
		sumOfCake = 0.0;
		
		wholeCake.clear();
		
		for ( Map.Entry<Integer, Double> entry :  regions.entrySet() )
		{
			wholeCake.add( entry.getValue() );
		}
		
		for ( Double d : wholeCake )
		{
			sumOfCake += d;
		}
		
		for ( Map.Entry<Integer, Double> entry :  regions.entrySet() )
		{
			entry.setValue( ( entry.getValue() / sumOfCake ) * 100.0 );
		}
		
		return regions;
		
	}
	
	public ArrayList<Action> generatePlan()
	{
		plan = new ArrayList<Action>();
		
		simulatorRegion = RegionUtils.getRegion( bwapi.getMap(), leader );
		
		ellapsedTime = 0;
		while ( ellapsedTime < ( 24 * 120  ) )
		{
			Region to = chooseRegion();
			int actionTime = moveToNextRegion( to );
			
			plan.add( new Action( simulatorRegion, ellapsedTime, ellapsedTime + actionTime ) );
			ellapsedTime += actionTime;
		}
		
		return plan;
	}

	private Region chooseRegion( ) 
	{
		HashMap<Integer, Double> connectedRegions = evaluateNearbyRegions();

		double chance = 0.0;
		
		for ( Map.Entry<Integer, Double> entry : connectedRegions.entrySet() )
		{
			chance += entry.getValue();
			entry.setValue( chance );
		}
		
		Random rand    = new Random();
		
		Region result = null;
		
		int counter = 0;
		
		while ( ( result == null ) && ( counter < 50 ) )
		{
			if ( chance < 1 ) chance = 1;
			int percentage = rand.nextInt( (int) chance );
			
			int minOK = 0;
	
			for ( Map.Entry<Integer, Double> entry : connectedRegions.entrySet() )
			{
				if ( ( percentage >= minOK ) && ( percentage <= entry.getValue() ) )
				{
					return RegionUtils.getRegion( bwapi.getMap(), entry.getKey() );
				}
			}
			
			
			counter++;
			
		}
		return result;
	}
	
	
	
}
