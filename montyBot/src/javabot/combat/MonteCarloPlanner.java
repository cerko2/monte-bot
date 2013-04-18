package javabot.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javabot.model.Region;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.model.Unit;
import javabot.util.RegionUtils;

public class MonteCarloPlanner extends AbstractManager 
{

	final boolean DEBUG = true;
	SquadManager squadManager;
	JNIBWAPI bwapi;
	
	ArrayList<Unit> enemyUnits;
	ArrayList<Unit> ourUnits;
	ArrayList<Base> ourBases;
	ArrayList<Base> enemyBases;
	
	public MonteCarloPlanner( JNIBWAPI bwapi ) 
	{
		this.bwapi = bwapi;
	}

	private void setEnemyBases() 
	{
		HashSet<Integer> regions = new HashSet<Integer>();
		enemyBases = new ArrayList<Base>();
		
		for ( Unit u : enemyUnits )
		{
			if ( bwapi.getUnitType( u.getTypeID() ).isBuilding() )
			{
				Region r = RegionUtils.getRegion( bwapi.getMap() , u );
				if ( !regions.contains( r ) )
				{
					regions.add( r.getID() );
					enemyBases.add( new Base( bwapi , r ) );
				}
			}
		}
		
	}

	private void setOurBases() 
	{
		HashSet<Integer> regions = new HashSet<Integer>();
		ourBases = new ArrayList<Base>();
		for ( Unit u : ourUnits )
		{
			if ( bwapi.getUnitType( u.getTypeID() ).isBuilding() )
			{
				Region r = RegionUtils.getRegion( bwapi.getMap() , u );
				if ( !regions.contains( r ) )
				{
					regions.add( r.getID() );
					ourBases.add( new Base( bwapi , r ) );
				}
			}
		}
	}
	
	public HarrassSquad getHarrassSquad()
	{
		return squadManager.getHarrassSquad();
	}
	
	public void update( ArrayList<Unit> enemyUnits, ArrayList<Unit> myUnits ) 
	{
		
		if ( bwapi.getFrameCount() % ( 24*30 ) == 0 )
		{
			this.enemyUnits = enemyUnits;
			this.ourUnits   = myUnits;
			if ( squadManager == null ) 
			{
				squadManager = new SquadManager( bwapi );
			}
			squadManager.updateSquadManager( enemyUnits, myUnits );
			
			setOurBases();
			setEnemyBases();
			generatePlans();
			
		}
		debug();
		
		if ( squadManager == null || squadManager.ourSquads == null )
		{
			return;
		}
		
		for ( Map.Entry<Integer, OurSquad> entry : squadManager.ourSquads.entrySet() )
		{
			entry.getValue().followPlan( entry.getValue().plan );
		}
		
	}
	
	/********************** GENERATE PLAN METHODS ********************/
	
	private void generatePlans()
	{
		for ( Map.Entry<Integer, OurSquad> entry : squadManager.ourSquads.entrySet() )
		{
			entry.getValue().setSimulatorCollections( enemyBases, ourBases, squadManager.enemySquads, squadManager.ourSquads );
			entry.getValue().generatePlan();
		}
	}
	
	private void debugConnectedRegions( HashMap<Integer, Double> connected, int squad_id, boolean enemySquad ) 
	{
		for ( Map.Entry<Integer, Double> entry : connected.entrySet() )
		{
			int regionId = entry.getKey();
			if ( regionId > 1000 ) regionId -= 1000000;
			Region r = RegionUtils.getRegion( bwapi.getMap(), regionId );
			
			if ( r == null )
			{
				System.out.println( "Region pri z connected bol nulovy" );
				continue;
			}
			
			if ( !enemySquad )
			{
				bwapi.drawText( r.getCenterX()-50, r.getCenterY() + ( 10 * ( squad_id + 1 ) ), "Our Squad ID: " + squad_id + " Chance: " + entry.getValue().intValue() + "%%", false );
			}
			else
			{
				bwapi.drawText( r.getCenterX()-50, r.getCenterY() - ( 10 * ( squad_id + 1 ) ), "Enemy Squad ID: " + squad_id + " Chance: " + entry.getValue().intValue() + "%%", false );
			}
		}
	}

	/********************** END GENERATE PLAN METHODS ****************/
	
	public void debug()
	{
		if ( DEBUG )
		{
			squadManager.debug();
			
			for ( Region r : bwapi.getMap().getRegions() )
			{
				bwapi.drawText( r.getCenterX(), r.getCenterY(), "Region s id: " + r.getID(), false );
			}
			
			for ( Base b : ourBases )
			{
				bwapi.drawText( b.getRegion().getCenterX(), b.getRegion().getCenterY()+10, "Our base on (region_id): " + b.getRegion().getID(), false);
			}
			
			for ( Base b : enemyBases )
			{
				bwapi.drawText( b.getRegion().getCenterX(), b.getRegion().getCenterY()+10, "Enemy base (region_id): " + b.getRegion().getID(), false);
			}
			
			for ( Map.Entry<Integer, OurSquad> ourSquad : squadManager.ourSquads.entrySet() )
			{
				ourSquad.getValue().setSimulatorCollections( enemyBases, ourBases, squadManager.enemySquads, squadManager.ourSquads );
				HashMap<Integer, Double> connected = ourSquad.getValue().evaluateNearbyRegions();
				debugConnectedRegions( connected, ourSquad.getKey(), false );
			}
			
			for ( Map.Entry<Integer, EnemySquad> enemySquad : squadManager.enemySquads.entrySet() )
			{
				enemySquad.getValue().setSimulatorCollections( enemyBases, ourBases, squadManager.enemySquads, squadManager.ourSquads );
				HashMap<Integer, Double> connected = enemySquad.getValue().evaluateNearbyRegions();
				debugConnectedRegions( connected, enemySquad.getKey(), true );
			}
		}
	}
	
	
}

