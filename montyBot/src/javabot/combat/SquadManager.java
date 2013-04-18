package javabot.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javabot.JNIBWAPI;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;
import javabot.util.BWColor;
import javabot.util.UnitUtils;

/**
 * DO NOT FORGET TO REMOVE UNITS FROM AVAILABLE LISTS
 * @author user
 *
 */

public class SquadManager {
	
	TreeMap< Integer, OurSquad > ourSquads     = new TreeMap< Integer, OurSquad >();
	HashMap< Integer, EnemySquad > enemySquads = new HashMap< Integer, EnemySquad >();
	HarrassSquad harassSquad;
	
	ArrayList<Unit> enemyCombatUnits;
	
	ArrayList<Unit> availableUnits;
	ArrayList<Unit> availableAntiAirUnits;
	ArrayList<Unit> availableAntiGroundUnits;
	
	private JNIBWAPI bwapi;
	
	public SquadManager( JNIBWAPI bwapi ) 
	{
		this.bwapi = bwapi;
	}
	
	public void updateSquadManager( ArrayList<Unit> enemyUnits, ArrayList<Unit> ourUnits )
	{
		ourSquads   = new TreeMap< Integer, OurSquad >();
		enemySquads = new HashMap< Integer, EnemySquad >();
		harassSquad = null;
		
		this.enemyCombatUnits         = getEnemyCombatUnits( enemyUnits );
		this.availableUnits           = getOurCombatUnits( ourUnits );
		this.availableAntiAirUnits    = getOurAntiAirUnits();
		this.availableAntiGroundUnits = getOurAntiGroundUnits();
		
		setEnemySquads();
		setOurSquads();
	}
	
	private ArrayList<Unit> getOurCombatUnits( ArrayList<Unit> ourUnits ) 
	{
		ArrayList<Unit> result = new ArrayList<Unit>();
		for ( Unit u : ourUnits )
		{
			if (  ( !bwapi.getUnitType( u.getTypeID() ).isWorker() ) && 
				( ( bwapi.getUnitType( u.getTypeID() ).isAttackCapable() ) || isSpecialUnit( u.getTypeID() ) )
			    )
			{
				result.add( u );
			}
		}
		return result;
	}

	private ArrayList<Unit> getOurAntiAirUnits() 
	{
		ArrayList<Unit> result = new ArrayList<Unit>();
		for ( Unit u : availableUnits )
		{
			if ( bwapi.getUnitType( u.getTypeID() ).isCanAttackAir() )
			{
				result.add( u );
			}
		}
		return result;
	}

	private ArrayList<Unit> getOurAntiGroundUnits() 
	{
		ArrayList<Unit> result = new ArrayList<Unit>();
		for ( Unit u : availableUnits )
		{
			if ( bwapi.getUnitType( u.getTypeID() ).isCanAttackGround() )
			{
				result.add( u );
			}
		}
		return result;
	}

	private boolean isSpecialUnit( int typeID ) 
	{
		if ( typeID == UnitTypes.Protoss_Carrier.ordinal() ) return true;
		if ( typeID == UnitTypes.Protoss_Reaver.ordinal() ) return true;
		if ( typeID == UnitTypes.Protoss_Dark_Archon.ordinal() ) return true;
		if ( typeID == UnitTypes.Protoss_High_Templar.ordinal() ) return true;
		if ( typeID == UnitTypes.Terran_Science_Vessel.ordinal() ) return true;
		if ( typeID == UnitTypes.Terran_Medic.ordinal() ) return true;
		if ( typeID == UnitTypes.Zerg_Defiler.ordinal() ) return true;
		if ( typeID == UnitTypes.Zerg_Queen.ordinal() ) return true;
		
		return false;
		
	}
	
	/*********************** OUR SQUADS ARE SET BELOW ************************************/ 
	
	private void setOurSquads() 
	{
		int ourSquadId = 0;
		
		while ( ( ourSquadId < enemySquads.size() ) && ( !availableUnits.isEmpty() ) )
		{
			matchEnemySquadsPower( ourSquadId );
			ourSquadId++;
		}
		
		while ( !availableUnits.isEmpty() )
		{
			addUnitToHarrassSquad();
		}
		
		for ( Map.Entry<Integer, OurSquad> s : ourSquads.entrySet() )
		{
			s.getValue().update();
		}
		
	}
	
	private void addUnitToHarrassSquad() 
	{
		if ( harassSquad == null )
			harassSquad = new HarrassSquad( bwapi );
		for ( Unit u : availableUnits )
		{
			harassSquad.squadUnits.add( u );
			harassSquad.update();
		}
		
		availableUnits.clear();
	}
	
	private void removeUnitFromAvailableUnits( Unit unit )
	{
		if ( unit == null ) return;
		int i = 0;
		for ( Unit u : availableUnits )
		{
			if ( u.getID() == unit.getID() )
				break;
			i++;
		}
		availableUnits.remove( i );
		
		i = 0;
		for ( Unit u : availableAntiAirUnits )
		{
			if ( u.getID() == unit.getID() )
				break;
			i++;
		}
		
		if ( i != availableAntiAirUnits.size() )
			availableAntiAirUnits.remove( i );
		
		i = 0;
		for ( Unit u : availableAntiGroundUnits )
		{
			if ( u.getID() == unit.getID() )
				break;
			i++;
		}
		if ( i != availableAntiGroundUnits.size() )
			availableAntiGroundUnits.remove( i );
		
	}

	/**
	 * After this function fires and we had enough units,
	 * our squad with the ID of an enemySquad is ready to kick this squads ass :)
	 * @param enemySquadId
	 */
	private void matchEnemySquadsPower( int enemySquadId )
	{
		EnemySquad enemySquad = enemySquads.get( enemySquadId );
		
		ourSquads.put( enemySquadId, new OurSquad( bwapi, enemySquadId ) );
		
		int counter = 0;
		while ( ourSquads.get( enemySquadId ).isWeakerOnGroundThan( enemySquad ) )
		{
			if ( counter > 100 ) break;
			addGroundFighterToSquad( enemySquadId );
			counter++;
		}
		
		counter = 0;
		while ( ourSquads.get( enemySquadId ).isWeakerInAirThan( enemySquad ) )
		{
			if ( counter > 100 ) break;
			addAirFighterToSquad( enemySquadId );
			counter++;
		}
		
		ArrayList<Integer> emptySquadIds = new ArrayList<Integer>();
		for ( Map.Entry<Integer, OurSquad> entry : ourSquads.entrySet() )
		{
			if ( entry.getValue().squadUnits.isEmpty() )
				emptySquadIds.add( entry.getKey() );
		}
		
		for ( Integer i : emptySquadIds )
		{
			ourSquads.remove( i );
		}
		
	}
	
	private Unit findClosestAntiAirUnit( int ourSquadId )
	{
		Unit closestUnit = null;
		double closestDist = 111111111111111.1;
		
		for ( Unit ourSquadUnit : ourSquads.get( ourSquadId ).squadUnits )
		{
			for ( Unit available : availableAntiAirUnits )
			{
				if ( UnitUtils.getDistance( ourSquadUnit , available ) < closestDist )
				{
					closestDist = UnitUtils.getDistance( ourSquadUnit , available );
					closestUnit = available;
				}
			}
		}
		
		removeUnitFromAvailableUnits( closestUnit );
		
		return closestUnit;
	}
	
	/**
	 * Do not forget to remove units from AVAILABLE LIST
	 * @param ourSquadId
	 * @return
	 */
	private Unit findClosestAntiGroundUnit( int ourSquadId )
	{
		Unit closestUnit = null;
		double closestDist = 111111111111111.1;
		
		for ( Unit ourSquadUnit : ourSquads.get( ourSquadId ).squadUnits )
		{
			for ( Unit available : availableAntiGroundUnits )
			{
				if ( UnitUtils.getDistance( ourSquadUnit , available ) < closestDist )
				{
					closestDist = UnitUtils.getDistance( ourSquadUnit , available );
					closestUnit = available;
				}
			}
		}
		
		removeUnitFromAvailableUnits( closestUnit );
		
		return closestUnit;
	}
	
	private void addAirFighterToSquad( int ourSquadId )
	{
		Unit newFighter = findClosestAntiAirUnit(ourSquadId);
		if ( newFighter == null )
		{
			if ( !availableAntiAirUnits.isEmpty() )
			{
				newFighter = availableAntiAirUnits.get( 0 );
				removeUnitFromAvailableUnits( newFighter );
			} else 
			{
				return;
			}
		}
		
		ourSquads.get( ourSquadId ).squadUnits.add( newFighter );
		
	}

	private void addGroundFighterToSquad( int ourSquadId ) 
	{
		Unit newFighter = findClosestAntiGroundUnit(ourSquadId);
		if ( newFighter == null )
		{
			if ( !availableAntiGroundUnits.isEmpty() )
			{
				newFighter = availableAntiGroundUnits.get( 0 );
				removeUnitFromAvailableUnits( newFighter );
			} else 
			{
				return;
			}
		}
		
		ourSquads.get( ourSquadId ).squadUnits.add( newFighter );
	}

	
	/*********************** END OF OUR SQUADS SETTING **********************************/
    

	/************************ ENEMY SQUADS ARE SET BELOW *****************************/
	
	private ArrayList<Unit> getEnemyCombatUnits( ArrayList<Unit> enemyUnits )
	{
		ArrayList<Unit> result = new ArrayList<Unit>();
		for ( Unit u : enemyUnits )
		{
			if (  ( !bwapi.getUnitType( u.getTypeID() ).isWorker() ) && 
				  ( ( bwapi.getUnitType( u.getTypeID() ).isAttackCapable() ) || isSpecialUnit( u.getTypeID() ) )
			    )
			{
				result.add( u );
			}
		}
		return result;
	}
	
	
	private void setEnemySquads()
	{
		int enemySquadId = 0;
		while ( !enemyCombatUnits.isEmpty() )
		{
			newEnemySquad( enemySquadId );
			enemySquadId++;
		}
	}
	
	private void newEnemySquad( int id )
	{
		enemySquads.put( id, new EnemySquad( this.bwapi ) );
		Unit tmpUnit = nextEnemySquadUnit( enemySquads.get( id ) );
		while ( tmpUnit != null )
		{
			enemySquads.get( id ).squadUnits.add( tmpUnit );
			tmpUnit = nextEnemySquadUnit( enemySquads.get( id ) );
		}
		
		enemySquads.get( id ).update();
		
	}
	
	/**
	 * Unit neimplementuje metody equals a hashmap
	 * @param unit
	 */
	private void removeUnitFromEnemyCombatUnits( Unit unit )
	{
		if ( unit == null ) return;
		int i = 0;
		for ( Unit u : enemyCombatUnits )
		{
			if ( u.getID() == unit.getID() )
				break;
			i++;
		}
		
		enemyCombatUnits.remove( i );
	}
	
	// jendotky patria do squadu ked su vo vzdialenosti distanceFromSquad od jednotky z tohoto squadu
	private Unit nearestEnemyUnit( Unit squadUnit, double distanceFromSquad )
	{
		double nearestDist = distanceFromSquad;
		Unit nearestUnit   = null;
		for ( Unit u : enemyCombatUnits )
		{
			if ( UnitUtils.getDistance( u, squadUnit ) < nearestDist )
			{
				nearestDist = UnitUtils.getDistance( u, squadUnit );
				nearestUnit = u;
			}
		}
		
		removeUnitFromEnemyCombatUnits( nearestUnit );
		
		return nearestUnit;
	}

	private Unit nextEnemySquadUnit( EnemySquad enemySquad ) 
	{
		// ak prva jednotka squadu
		if ( enemySquad.squadUnits.isEmpty() )
		{
			if ( enemyCombatUnits.isEmpty() )
			{
				return null;
			}
			else 
			{
				return enemyCombatUnits.get( 0 );
			}
		}
		
		for ( Unit squadUnit : enemySquad.squadUnits )
		{
			Unit tmp = nearestEnemyUnit( squadUnit, 200.0 );
			if ( tmp != null )
			{
				return tmp; 
			}
		}
		
		return null;
	}

	/*************************** END ENEMY SQUADS SETTING ***********************************/
	
	public void debug() 
	{
		ArrayList<Integer> colors = new ArrayList<Integer>();
		colors.add(BWColor.RED);
		colors.add(BWColor.GREEN);
		colors.add(BWColor.BLUE);
		colors.add(BWColor.YELLOW);
		colors.add(BWColor.PURPLE);
		colors.add(BWColor.WHITE);
		colors.add(BWColor.CYAN);
		colors.add(BWColor.ORANGE);
		colors.add(BWColor.BLACK);
		colors.add(BWColor.BROWN);
		colors.add(BWColor.TEAL);
		int i = 0;
		bwapi.drawText(10, 10, "Count our squads: " + ourSquads.size(), true);
		bwapi.drawText(10, 20, "Count enemy squads: " + enemySquads.size(), true);
		bwapi.drawText(10, 30, "Harass squad existuje: " + ( harassSquad != null ), true);
		for ( Map.Entry<Integer, EnemySquad> e_squad : enemySquads.entrySet() )
		{
			if ( i == colors.size() ) i = 0;
			for ( Unit u : e_squad.getValue().squadUnits )
			{
				bwapi.drawCircle( u.getX(), u.getY(), 20, colors.get(i), false, false);
			}
			Unit l = e_squad.getValue().getLeader();
			bwapi.drawText( l.getX(), l.getY(), e_squad.getValue().toString(), false );
			
			if ( ourSquads.get( e_squad.getKey() ) != null )
			{
				for ( Unit u : ourSquads.get( e_squad.getKey() ).squadUnits )
				{
					bwapi.drawCircle( u.getX(), u.getY(), 20, colors.get(i), false, false);
				}
				
				Unit a = ourSquads.get( e_squad.getKey() ).getLeader();
				bwapi.drawText( a.getX(), a.getY(), ourSquads.get( e_squad.getKey() ).toString(), false );
			}
			
			i++;
			
		}
		
		if ( harassSquad != null )
		{
			for ( Unit u : harassSquad.squadUnits )
			{
				bwapi.drawCircle( u.getX() , u.getY(), 5, BWColor.RED, true, false );
			}
		}
	}

	public HarrassSquad getHarrassSquad() 
	{
		return harassSquad;
	}
	
}
