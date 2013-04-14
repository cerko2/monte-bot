package javabot.combat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javabot.JNIBWAPI;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;

public class SquadManager {
	
	//our squad ID => squad
	private TreeMap<Integer, Squad> ourSquads = new TreeMap<Integer, Squad>();
	
	// enemy squad ID => squad
	private TreeMap<Integer, Squad> enemySquads = new TreeMap<Integer, Squad>();
	
	// unit_id => enemy squad ID
	private TreeMap<Integer, Integer> enemyLeaders = new TreeMap<Integer, Integer>();
	
	private int enemy_squad_id = 0;
	private int our_squad_id   = 0;
	
	private JNIBWAPI bwapi;
	private MonteCarloPlanner monteCarlo;

	private ArrayList<Unit> enemyUnits;
	private ArrayList<Unit> myUnits;
	
	boolean noMoreMyUnits = false;
	
	public SquadManager( JNIBWAPI bwapi, MonteCarloPlanner monteCarlo ) 
	{
		this.bwapi 		= bwapi;
		this.monteCarlo = monteCarlo;
	}
	
	public void setSquads( ArrayList<Unit> enemyUnits, ArrayList<Unit> myUnits ) 
	{
		setEnemyUnits(enemyUnits);
		setMyUnits(myUnits);
		setEnemySquads();
		setOurSquads();
	}
	
	private void setOurSquads() 
	{
		ourSquads = new TreeMap<Integer, Squad>();
		//sorted list of enemy squads ( the weakest is first, then the order is ASCENDING )
		ArrayList<Squad> enemySquads = this.getEnemySquads();
		
		for ( Squad s : enemySquads ) 
		{

			if (noMoreMyUnits) 
			{
				return;
			}
			
			Squad tmpSquad = new Squad( bwapi, our_squad_id, s.getEnemySquadID() );
			ourSquads.put( our_squad_id, tmpSquad );
			
			int i = 0;
			
			if (tmpSquad.isWeakerInAir(s)) {
				System.out.println("is weaker in air");
			} else {
				System.out.println("is not weaker in AIR");
				System.out.println(tmpSquad.getAirPower());
				System.out.println(s.getAntiAirPower());
			}
			
			while ( tmpSquad.isWeakerInAir(s) ) 
			{
				i++;
				addAntiAirUnitToSquad(tmpSquad);
				if (noMoreMyUnits) {
					return;
				}
				if (i > 200) {
					break;
				}
				tmpSquad.updateSquad();
			}
			
			if (tmpSquad.isWeakerInAir(s)) {
				System.out.println("is weaker in ground");
			} else {
				System.out.println("is not weaker in ground");
				System.out.println(tmpSquad.getGroundPower());
				System.out.println(s.getAntiGroundPower());
			}
			
			while ( tmpSquad.isWeakerOnGround(s) )
			{
				addAntiGroundUnitToSquad(tmpSquad);
				if (noMoreMyUnits) {
					return;
				}
				tmpSquad.updateSquad();
			}
			
			our_squad_id++;
			
		}
		
	}
	
	private void addAntiAirUnitToSquad( Squad s ) 
	{
		int i = 0;
		for ( Unit u : myUnits ) 
		{
			if ( bwapi.getUnitType( u.getTypeID() ).isCanAttackAir() )
			{
				s.putUnit(u);
				myUnits.remove( i );
				return;
			}
			i++;
		}
	}
	
	private void addAntiGroundUnitToSquad( Squad s ) 
	{
		int i = -1;
		for ( Unit u : myUnits ) 
		{
			i++;
			if ( ( !bwapi.getUnitType( u.getTypeID() ).isCanMove() ) || ( u.getTypeID() == UnitTypes.Protoss_Probe.ordinal() ) ) 
			{
				continue;
			}
			
			if ( !bwapi.getUnitType( u.getTypeID() ).isCanAttackAir() && bwapi.getUnitType( u.getTypeID() ).isCanAttackGround() )
			{
				s.putUnit(u);
				myUnits.remove( i );
				return;
			}
			
		}
		
		i = -1;
		
		for ( Unit u : myUnits ) 
		{
			i++;
			
			if ( ( !bwapi.getUnitType( u.getTypeID() ).isCanMove() ) || ( u.getTypeID() == UnitTypes.Protoss_Probe.ordinal() ) || ( !bwapi.getUnitType( u.getTypeID() ).isCanAttackGround() ) ) 
			{
				continue;
			}
			
			s.putUnit(u);
			myUnits.remove( i );
			return;
			
		}
		
		noMoreMyUnits = true;
		
	}
	
	private void setEnemySquads() 
	{
		enemySquads  = new TreeMap<Integer, Squad>();
		enemyLeaders = new TreeMap<Integer, Integer>();
		for (Unit u : enemyUnits) {
			
			if ( u.getTypeID() == UnitTypes.Protoss_Probe.ordinal() ) 
			{
				continue;
			}
			
			if ( !
					   ( bwapi.getUnitType(u.getTypeID()).isCanAttackAir() 
					|| ( bwapi.getUnitType(u.getTypeID()).isCanAttackGround() ) 
					|| ( u.getTypeID() == UnitTypes.Protoss_Reaver.ordinal() ) 
					|| ( u.getTypeID() == UnitTypes.Protoss_Carrier.ordinal() )
					|| ( u.getTypeID() == UnitTypes.Protoss_Dark_Archon.ordinal() )
					|| ( u.getTypeID() == UnitTypes.Protoss_High_Templar.ordinal() )) ) 
			{
				continue;
			}
			
			if ( !hasLeader(u) ) 
			{
				enemy_squad_id++;
				enemySquads.put(new Integer(enemy_squad_id), new Squad( bwapi, enemy_squad_id ));
				enemySquads.get(new Integer(enemy_squad_id)).putUnit(u);
			} 
		}
		
	}
	
	/**
	 * Some modul should send known enemy units to this modul
	 * @param enemyUnits (bwapi.getEnemyUnits)
	 */
	public void setEnemyUnits(ArrayList<Unit> enemyUnits) 
	{
		this.enemyUnits = enemyUnits;
	}

	/**
	 * If the unit has a leader, it means it belongs to a squad. 
	 * To become a leader you must be the first unit I iterate through 
	 * in a certain are that is capable of attacking.
	 * @param u (Unit)
	 * @return
	 */
	private boolean hasLeader( Unit u ) 
	{
		
		if ( enemyLeaders.size() < 1 ) 
		{
			enemyLeaders.put( u.getID(), new Integer( enemy_squad_id+1 ) );
			return false;
		} 
		
		for ( Map.Entry<Integer, Integer> entry : enemyLeaders.entrySet() ) 
		{
			if ( MonteCarloPlanner.getDistance( bwapi.getUnit( entry.getKey() ), u ) < 500 ) 
			{
				enemySquads.get( new Integer( entry.getValue() ) ).putUnit( u );
				return true;
			}
		}
		
		enemyLeaders.put( u.getID(), new Integer( enemy_squad_id+1 ) );
		return false;
		
	} //hasLeader
	
	public int getEnemySquadsSize() 
	{
		return enemySquads.size();
	}
	
	public ArrayList<Squad> getEnemySquads() 
	{
		ArrayList<Squad> result = new ArrayList<Squad>();
		
		for ( Map.Entry<Integer, Squad> entry : enemySquads.entrySet() ) 
		{
			entry.getValue().updateSquad();
			result.add(entry.getValue());
		}
		
		Collections.sort(result);
		
		return result;
	}
	
	public TreeMap<Integer, Squad> getEnemySquadsTree()
	{
		return this.enemySquads;
	}
	
	public TreeMap<Integer, Squad> getOurSquadsTree() 
	{
		return this.ourSquads;
	}

	public ArrayList<Unit> getMyUnits() 
	{
		return myUnits;
	}

	public void setMyUnits( ArrayList<Unit> myUnits ) 
	{
		this.myUnits = myUnits;
	}
	
	public Squad getOurSquad( int enemyID ) 
	{

		for ( Map.Entry<Integer, Squad> entry : ourSquads.entrySet() )
		{
			if ( entry.getValue().getEnemySquadID() == enemyID )
			{
				return entry.getValue();
			}
		}
		
		return null;
	}
	
	public void updateSquadManager( int unitID ) 
	{
	
		enemyLeaders.remove( unitID );
		
		for ( Map.Entry<Integer, Squad> entry : ourSquads.entrySet() ) 
		{
			if ( entry.getValue().getUnits().size() == 1 ) 
			{
				setSquads( bwapi.getEnemyUnits(), bwapi.getMyUnits() );
				return;
			}
			
			entry.getValue().updateSquad( unitID );
		}
		
		for ( Map.Entry<Integer, Squad> entry : enemySquads.entrySet() ) 
		{
			if ( entry.getValue().getUnits().size() == 1 ) 
			{
				setSquads( bwapi.getEnemyUnits(), bwapi.getMyUnits() );
				return;
			}
			entry.getValue().updateSquad( unitID );
		}
		
	}

	public void updateSquadManager( ArrayList<Unit> enemyUnits, ArrayList<Unit> myUnits ) 
	{
		for (Map.Entry<Integer, Squad> entry : ourSquads.entrySet()) 
		{
			if ( entry.getValue().getUnits().size() == 1 ) 
			{
				setSquads( enemyUnits, myUnits );
				return;
			}
			entry.getValue().updateSquad();
		}
		
		for (Map.Entry<Integer, Squad> entry : enemySquads.entrySet()) 
		{
			if ( entry.getValue().getUnits().size() == 1 ) 
			{
				setSquads( enemyUnits, myUnits );
				return;
			}
			entry.getValue().updateSquad();
		}
	}
	
}
