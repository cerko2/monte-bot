package javabot.combat;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javabot.JNIBWAPI;
import javabot.model.*;

public class Squad implements Comparable<Squad> {
	
	public  TreeMap<Integer, Action> actionsInTime = new TreeMap<Integer, Action>();
	private Region simulatorRegion;
	private int time = 0;
	
	protected JNIBWAPI 		  bwapi;
	private int		          squadID = -1;
	
	
	//the enemy squad this squad was built against (or only the ID of an enemySquad)
	private int     enemySquadID;
	protected Unit  leader = null;
	private boolean isEnemySquad;
	private double  squadPower;
	private double  airPower;
	private double  groundPower;
	private double  antiAirPower;
	private double  antiGroundPower;
	
	protected double damage_per_second;
	protected int    hitPoints;
	
	private TreeMap<Integer, Unit> units = new TreeMap<Integer, Unit>();
	
	/**
	 * This constructor is meant for the creation of our squad
	 * 
	 * @param bwapi
	 * @param squadID
	 * @param enemySquadID
	 * @param units
	 */
	public Squad( JNIBWAPI bwapi, int squadID, int enemySquadID ) 
	{
		this.bwapi 		  = bwapi;
		this.squadID	  = squadID;
		this.enemySquadID = enemySquadID;
		isEnemySquad 	  = false;
		squadPower 		  = assignSquadPower();
		setSquadLeader();
	}
	
	/**
	 * This constructor is meant for the creation of an enemy squad
	 * 
	 * @param bwapi
	 * @param enemySquadID
	 */
	public Squad( JNIBWAPI bwapi, int enemySquadID ) 
	{
		this.bwapi 	      = bwapi;
		isEnemySquad 	  = true;
		this.enemySquadID = enemySquadID;
		squadPower 		  = assignSquadPower();
		setSquadLeader();
	}
	
	/**
	 * The leader of the squad is the slowest member of the squad. 
	 * The units in the squad should wait for this unit so they move in a formation.
	 */
	private void setSquadLeader() 
	{
		double currentMin = 999.9;
		int currentMinID = -1;
		
		for ( Map.Entry<Integer, Unit> entry: units.entrySet() ) 
		{
			if ( bwapi.getUnitType( entry.getValue().getTypeID() ).getTopSpeed() < currentMin ) 
			{
				currentMin = bwapi.getUnitType( entry.getValue().getTypeID() ).getTopSpeed();
				currentMinID = entry.getKey();
			}
		}
		leader = bwapi.getUnit( currentMinID );
		
	} // setSquadLeader
	
	public int getSquadID() 
	{
		return squadID;
	}
	
	public int getEnemySquadID() 
	{
		return enemySquadID;
	}
	
	public double getSquadSpeed() 
	{
		return bwapi.getUnitType( getSquadLeader().getTypeID() ).getTopSpeed();
	} 
	
	/**
	 * !!!called on unitDestroyed
	 * Updates the squad leader and the treeMap units after a member of the squad is destroyed
	 * @param unitID 
	 */
	public void updateSquad( int unitID ) 
	{
		units.remove( unitID );
		if ( !this.isEnemySquad && this.squadID == 1 ) {
			System.out.println( "My Remove ID: " + unitID );
			System.out.println( "My Leader ID: " + leader );
			System.out.println( "My Size R: " + units.size() );
		}
		
		if ( this.isEnemySquad && this.squadID == 1 ) {
			System.out.println( "Enemy Remove ID: " + unitID );
			System.out.println( "Enemy Leader ID: " + leader );
			System.out.println( "Enemy Size R: " + units.size() );
		}
		
		if ( units.size() == 0 )
		{
			System.out.println( "Squad uz nema jednotky" );
		}
		
		if ( getSquadLeader() == null )
		{
			return;
		}
		
		if ( unitID == getSquadLeader().getID() ) 
		{
			setSquadLeader();
		}
	}
	
	/**
	 * called whenever the squad needs to update
	 */
	public void updateSquad() 
	{
		setSquadLeader();
		squadPower = assignSquadPower();
		setAirPower();
		setAntiAirPower();
		setAntiGroundPower();
		setGroundPower();
	}
	
	/**
	 * 
	 * ((actualHP / total HP) * (DPS + 1))
	 * 
	 * @return the heuristic power of a single unit
	 */
	public double getUnitPower( Unit u ) 
	{
		
		double minerals  = bwapi.getUnitType( u.getTypeID() ).getMineralPrice();
		double gas       = bwapi.getUnitType( u.getTypeID() ).getGasPrice();
		double totalHP   = bwapi.getUnitType( u.getTypeID() ).getMaxShields() + bwapi.getUnitType( u.getTypeID() ).getMaxHitPoints();
		double currentHP = u.getHitPoints() + u.getShield();
		return ( minerals + ( 1.5 * gas ) ) * ( currentHP / totalHP );
		
	} // getUnitPower
	
	/**
	 * @return the heuristic power of the whole squad
	 */
	private double assignSquadPower() 
	{
		double result = 0.0;
		for ( Map.Entry<Integer, Unit> entry : units.entrySet() ) 
		{
			result += getUnitPower( entry.getValue() );
		}
		return result;
	}
	
	public double getSquadPower() 
	{
		return squadPower;
	}
	
	public boolean isEnemySquad() 
	{
		return isEnemySquad;
	}
	
	public void putUnit( Unit u ) 
	{
		if ( !this.isEnemySquad && this.squadID == 1 ) 
		{
			System.out.println( "My Units Size: " + units.size() );
			System.out.println( "My Unit: " + u.getID() );
		}
		
		if ( this.isEnemySquad && this.squadID == 1 ) 
		{
			System.out.println( "Enemy Units Size: " + units.size() );
			System.out.println( "Enemy Unit: " + u.getID() );
		}
		
		units.put( u.getID(), u );
	}
	
	public Unit getSquadLeader() 
	{
		if ( leader == null )
		{
			setSquadLeader();
		}
		return leader;
	}
	
	public ArrayList<Unit> getUnits() 
	{
		ArrayList<Unit> result = new ArrayList<Unit>();
		
		for ( Map.Entry<Integer, Unit> entry : units.entrySet() ) {
			result.add( entry.getValue() );
		}
		
		return result;
		
	}

	public double getAirPower() 
	{
		return airPower;
	}

	public void setAirPower() 
	{
		double result = 0.0;
		for ( Map.Entry<Integer, Unit> entry : units.entrySet() ) 
		{
			if ( bwapi.getUnitType( entry.getValue().getTypeID() ).isFlyer() ) 
			{
				result += getUnitPower( entry.getValue() );
			}
		}
		airPower = result;
	}

	public double getGroundPower() 
	{
		return groundPower;
	}

	public void setGroundPower() {
		double result = 0.0;
		for ( Map.Entry<Integer, Unit> entry : units.entrySet() ) 
		{
			if ( !bwapi.getUnitType( entry.getValue().getTypeID() ).isFlyer() ) 
			{
				result += getUnitPower( entry.getValue() );
			}
		}
		groundPower = result;
	} // setGroundPower

	public double getAntiAirPower() 
	{
		return antiAirPower;
	}

	public void setAntiAirPower() 
	{
		double result = 0.0;
		for ( Map.Entry<Integer, Unit> entry : units.entrySet() ) 
		{
			if ( bwapi.getUnitType( entry.getValue().getTypeID() ).isCanAttackAir() ) 
			{
				result += getUnitPower( entry.getValue() ) + bwapi.getWeaponType( bwapi.getUnitType( entry.getValue().getTypeID() ).getAirWeaponID() ).getDamageAmount();
			}
		}
		antiAirPower = result;
	} // setAntiAirPower

	public double getAntiGroundPower() 
	{
		return antiGroundPower;
	}

	public void setAntiGroundPower() 
	{
		double result = 0.0;
		for ( Map.Entry<Integer, Unit> entry : units.entrySet() ) 
		{
			if ( bwapi.getUnitType( entry.getValue().getTypeID() ).isCanAttackGround() ) 
			{
				result += getUnitPower( entry.getValue() ) + bwapi.getWeaponType( bwapi.getUnitType( entry.getValue().getTypeID() ).getGroundWeaponID() ).getDamageAmount();
			}
		}
		antiGroundPower = result;
	} // setAntiGroundPower

	@Override
	public int compareTo( Squad o ) 
	{
		if ( o.getSquadPower() == this.getSquadPower() ) 
		{
			return -1;
		}
		int compare = ( int ) ( this.getSquadPower() - o.getSquadPower() );
		return compare;
	}
	
	/**
	 * 
	 * @param o (Squad)
	 * @return true if this.squad is weaker than the squad in the paramater
	 */
	public boolean isWeakerInAir( Squad o ) 
	{

		if ( this.getAntiAirPower() < o.getAirPower() ) 
		{
			return true;
		}
		return false;
		
	} // isWeakerInAir
	
	/**
	 * 
	 * @param o (Squad)
	 * @return true if this.squad is weaker than the squad in the paramater
	 */
	public boolean isWeakerOnGround( Squad o ) 
	{
		
		if ( this.getAntiGroundPower() < (o.getGroundPower() * 1.5) ) 
		{
			return true;
		}
		return false;
		
	} // isWeakerOnGround

	
	
	/*************************************
	/** 		SIMULATOR METHODS 		**/
	/**								    **	
	 *									**
	/*								    **
	************************************ */
	
	public Region getRegion()
	{
		Point regionPoint = new Point( leader.getX(), leader.getY() );
		return MonteCarloPlanner.getPointRegion( regionPoint );
	}
	
	public double getDPS() 
	{
		return damage_per_second;
	}
	
	public int getHP() 
	{
		return hitPoints;
	}
	
	public void fightOneSecond( double enemyDPS )
	{
		int currentHP = getHP();
		hitPoints -= (int) enemyDPS;
		damage_per_second *= ( hitPoints / currentHP );  
	}
	
	public void generateSquadPlan ( 
			TreeMap<Integer, Squad> _ourSquads, 
			TreeMap<Integer, Squad> _enemySquads, 
			ArrayList<Base> _ourBases, 
			ArrayList<Base> _enemyBases )
	{
		while ( time < 120 )
		{
			time += moveToNextRegion( evaluateNearbyRegions( simulatorRegion, _ourSquads, _enemySquads, _ourBases, _enemyBases ) );
		}
	}
	
	private double evaluateRegion( 
			Region r1, 
		    TreeMap<Integer, Squad> _ourSquads, 
			TreeMap<Integer, Squad> _enemySquads, 
			ArrayList<Base> _ourBases, 
			ArrayList<Base> _enemyBases )
	{
		
		ArrayList<Double> values = new ArrayList<Double>();
		
		for ( Map.Entry<Integer, Squad> entry : _ourSquads.entrySet() )
		{
			double c = 1.5;
			values.add( c * ( 1 / ( MonteCarloPlanner.getDistance(r1 , entry.getValue().getRegion() ) ) ) );
		}
		
		for ( Map.Entry<Integer, Squad> entry : _enemySquads.entrySet() )
		{
			double c = 1.0;
			if ( entry.getValue().getSquadID() == this.getSquadID() ) 
			{
				c = 2.0;
			}  
				
			values.add( c * ( 1 / ( MonteCarloPlanner.getDistance( r1, entry.getValue().getRegion() ) ) ) );
		}
		
		for ( Base b : _ourBases )
		{
			double c = 1.2;
			values.add( c * ( 1 / ( MonteCarloPlanner.getDistance( r1 , b.getBaseRegion() ) ) ) );
		}
		
		for ( Base b : _enemyBases )
		{
			double c = 1.4;
			values.add( c * ( 1 / ( MonteCarloPlanner.getDistance( r1 , b.getBaseRegion() ) ) ) );
		}
		
		double result = 0;
		
		for ( Double d : values )
		{
			result += d; 
		}
		
		return result;
	}
	
//	Kazdy z nearby regionov evaluni a potom premen na kusy kolaca a napokon doraz 
//	tym, ze rozdelis percenta podla kolaca
	
	private HashMap< Region, Double > evaluateNearbyRegions( 
			Region currentRegion, 
			TreeMap<Integer, Squad> _ourSquads, 
			TreeMap<Integer, Squad> _enemySquads, 
			ArrayList<Base> _ourBases, 
			ArrayList<Base> _enemyBases )
	{
		try {
			if ( currentRegion == null ) throw new NullPointerException( "Current region dostal na vstup null pointer" );
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
		
		HashMap< Region, Double > result = new HashMap< Region, Double >();
		
		for ( Region r : currentRegion.getConnectedRegions() )
		{
			result.put( r , 0.0 );
		}
		
		for ( Map.Entry< Region, Double > entry : result.entrySet() )
		{
			entry.setValue( evaluateRegion( entry.getKey(), _ourSquads, _enemySquads, _ourBases, _enemyBases ) );
		}
		
		
		
		return null;
	}
	
	private int moveToNextRegion( HashMap<Region, Double> hashMap )
	{
		return -1;
	}
	
	/**
	 * simulates the movement of the squad to the region, that is closest
	 * to the region this squad eventually wants to get to.
	 * @param goalRegion
	 * @return how much time this acction takes
	 */
//	public int moveToNextRegion( Region goalRegion )
//	{
//		
//		if ( goalRegion == null ) throw new NullPointerException( "Goal Region dostal na vstup null pointer" );
//		
//		// determine which region is the correct one
//		double nearestDistance  = 1000000000; 
//		Region nearestRegion = null;
//		for ( Region r : this.getRegion().getConnectedRegions() )
//		{
//			Region myRegion = this.getRegion();
//			if ( MonteCarloPlanner.getDistance( r, myRegion ) < nearestDistance )
//			{
//				nearestDistance = MonteCarloPlanner.getDistance( r, myRegion );
//				nearestRegion   = r;
//			}
//		}
//		
//		if ( nearestRegion == null ) throw new NullPointerException( "Move to next region nenasiel region" );
//		
//		// determine the connecting chokePoint
//		
//		ChokePoint connectingChoke = null;
//		
//		for ( ChokePoint c : this.getRegion().getChokePoints() )
//			for ( ChokePoint ch : nearestRegion.getChokePoints() )
//			{
//				if ( ( c.getFirstRegionID() == ch.getFirstRegionID() ) && 
//					 ( c.getSecondRegionID() == ch.getSecondRegionID() ) )
//				{
//					connectingChoke = ch;
//				}
//			}
//		
//		if ( connectingChoke == null ) throw new NullPointerException( "Connecting choke je null" );
//		
//		double dist = MonteCarloPlanner.getDistance( this.getRegion(), connectingChoke ) + 
//					  MonteCarloPlanner.getDistance(nearestRegion, connectingChoke );
//		
//		double numberOfFrames = dist / bwapi.getUnitType( leader.getTypeID() ).getTopSpeed(); 
//		
//		simulatorRegion = nearestRegion;
//		
//		return (int) numberOfFrames / 24;
//		
//	} // moveToNextRegion
	
	public int doNothing( int time )
	{
		return time;
	}
	
	
	
	
}
