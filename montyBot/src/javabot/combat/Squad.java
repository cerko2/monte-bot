package javabot.combat;

import java.util.ArrayList;

import javabot.JNIBWAPI;
import javabot.model.Region;
import javabot.model.Unit;
import javabot.util.RegionUtils;

public class Squad {

	double antiAirPower;
	double antiGroundPower;
	double airPower;
	Unit leader = null;
	Region simulatorRegion;
	double squadSpeed;
	JNIBWAPI bwapi;
	ArrayList<Unit> squadUnits = new ArrayList<Unit>();
	double groundPower;
	
	int hp;
	double dps;
	
	public Squad( JNIBWAPI bwapi )
	{
		this.bwapi = bwapi;
	}
	
	public Region getRegion()
	{
		return RegionUtils.getRegion( bwapi.getMap() , leader );
	}
	
	public Region getRegion( boolean flyers_too )
	{
		if ( !flyers_too ) return getRegion();
		return RegionUtils.findNearestRegion( bwapi.getMap(), leader );
	}
	
	public void update() 
	{
		antiAirPower    = getAntiAirPower();
		antiGroundPower = getAntiGroundPower();
		airPower  		= getAirPower();
		groundPower     = getGroundPower();
		
		leader			= setLeader();
		if ( leader == null )
		{
			return;
		}
		simulatorRegion = RegionUtils.getRegion( bwapi.getMap() , leader );
		squadSpeed      = bwapi.getUnitType( leader.getTypeID() ).getTopSpeed();
		
		hp  = getHP();
		dps = getDps();
	}

	protected double getDps() 
	{
		
		double result = 0.0;
		for ( Unit u : squadUnits )
		{
			double weaponDamage = bwapi.getWeaponType( bwapi.getUnitType( u.getTypeID() ).getGroundWeaponID() ).getDamageAmount();
			double cooldown     = bwapi.getWeaponType( bwapi.getUnitType( u.getTypeID() ).getGroundWeaponID() ).getDamageCooldown();
			result += weaponDamage / cooldown;
		}
		return result;
	}

	protected int getHP() 
	{
		int result = 0;
		for ( Unit u : squadUnits )
		{
			result += u.getHitPoints() + u.getShield();
		}
		return result;
	}
	
	protected void setDps( double dps )
	{
		this.dps = dps;
	}
	
	protected void setHP( int hp )
	{
		this.hp = hp;
	}
	
	public void simulateSquadFight( OurSquad s1, EnemySquad s2 )
	{
		
		if ( ( s2.antiGroundPower < s1.antiGroundPower ) )
		{
			s1.score += s2.getHP();
		}
		
		if ( ( s2.antiGroundPower > s1.antiGroundPower ) )
		{
			s1.score -= s1.getHP();
		}
		
		if ( ( s2.antiGroundPower == s1.antiGroundPower ) )
		{
			s1.score += ( s2.getHP() - s1.getHP() );
		}
		
	}
	

	private Unit setLeader() 
	{
		if ( squadUnits.isEmpty() ) return null;
		return squadUnits.get( 0 );
	}

	/**
	 * 
	 * ((actualHP / total HP) * (DPS + 1))
	 * 
	 * @return the heuristic power of a single unit
	 */
	private double getUnitPower( Unit u ) 
	{
		
		double minerals  = bwapi.getUnitType( u.getTypeID() ).getMineralPrice();
		double gas       = bwapi.getUnitType( u.getTypeID() ).getGasPrice();
		double totalHP   = bwapi.getUnitType( u.getTypeID() ).getMaxShields() + bwapi.getUnitType( u.getTypeID() ).getMaxHitPoints();
		double currentHP = u.getHitPoints() + u.getShield();
		return ( minerals + ( 1.5 * gas ) ) * ( currentHP / totalHP );
		
	} // getUnitPower
	
	protected double getAntiGroundPower() 
	{
		double result = 0.0;
		
		for ( Unit u : squadUnits )
		{
			if ( bwapi.getUnitType( u.getTypeID() ).isCanAttackGround() )
			{
				result += getUnitPower( u );
			}
		}
		
		return result;
	}

	protected double getAntiAirPower() 
	{
		double result = 0.0;
		
		for ( Unit u : squadUnits )
		{
			if ( bwapi.getUnitType( u.getTypeID() ).isCanAttackAir() )
			{
				result += getUnitPower( u );
			}
		}
		
		return result;
	}
	
	protected double getAirPower()
	{
		double result = 0.0;
		
		for ( Unit u : squadUnits )
		{
			if ( bwapi.getUnitType( u.getTypeID() ).isFlyer() )
			{
				result += getUnitPower( u );
			}
		}
		
		return result;
	}
	
	protected double getGroundPower() 
	{
		double result = 0.0;
		
		for ( Unit u : squadUnits )
		{
			if ( !bwapi.getUnitType( u.getTypeID() ).isFlyer() )
			{
				result += getUnitPower( u );
			}
		}
		
		return result;
	}

	public Unit getLeader()
	{
		return leader;
	}
	
	@Override
	public String toString() 
	{
		String result = "";
		
		result += "Anti air power: " + antiAirPower + System.lineSeparator();
		result += "Anti ground power: " + antiGroundPower + System.lineSeparator();
		result += "Air power: " + airPower + System.lineSeparator();
		result += "Speed: " + squadSpeed + System.lineSeparator();
		
		if ( leader != null && simulatorRegion == null )
			simulatorRegion = RegionUtils.findNearestRegion( bwapi.getMap(), leader );
		result += "Region ID: " + simulatorRegion.getID() + System.lineSeparator();;
		result += "Squad HP: " + getHP() + System.lineSeparator();;
		result += "Squad DPS: " + getDps() + System.lineSeparator();; 
		
		return result;
	}
	
	public boolean isWeakerOnGroundThan( Squad enemySquad ) 
	{
		this.update();
		return this.antiGroundPower < ( enemySquad.groundPower * 1.3 );
	}

	public boolean isWeakerInAirThan( Squad enemySquad ) 
	{
		this.update();
		return this.antiAirPower < ( enemySquad.airPower * 1.3 );
	}
	
	public ArrayList<Unit> getSquadUnits()
	{
		return squadUnits;
	}
	
	public boolean onlyFlyersInSquad()
	{
		for ( Unit u : squadUnits )
		{
			if ( !bwapi.getUnitType( u.getTypeID() ).isFlyer() )
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * Moves the simulator region to the region in parameter
	 * Apply to nearby regions only
	 * return how much time it took time ( in frames )
	 */
	protected int moveToNextRegion( Region to )
	{
		
		if ( to == null ) 
		{
			return 0;
		}
		
		if ( simulatorRegion.getID() == to.getID() )
		{
			return 5*24;
		}
		
		if ( onlyFlyersInSquad() )
		{
			int time = (int) ( RegionUtils.airPathToRegion( simulatorRegion, to ) / squadSpeed );
			
			simulatorRegion = to;
			
			return time;
		}
		
		int time = (int) ( ( RegionUtils.groundPathToRegion( simulatorRegion, to, bwapi.getMap() ) ) / squadSpeed);
		
		simulatorRegion = to;
		
		return time;
	}
	
	
}
