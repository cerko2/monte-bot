package javabot.combat;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javabot.model.ChokePoint;
import javabot.model.Region;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.model.Unit;
import javabot.util.BWColor;
import javabot.util.RegionUtils;

public class MonteCarloPlanner extends AbstractManager implements Runnable
{

	final boolean DEBUG = true;
	SquadManager squadManager;
	JNIBWAPI bwapi;
	
	long time;
	
	ArrayList<Unit> enemyUnits;
	ArrayList<Unit> ourUnits;
	ArrayList<Base> ourBases;
	ArrayList<Base> enemyBases;
	ArrayList<OurSquad> fightingSquads = new ArrayList<OurSquad>();
	
	HashSet<MyPlan> myPlans    	  = new HashSet<MyPlan>();
	HashSet<EnemyPlan> enemyPlans = new HashSet<EnemyPlan>();
	
	public MonteCarloPlanner( JNIBWAPI bwapi ) 
	{
		this.bwapi = bwapi;
		Thread t = new Thread(this);
		t.start();
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
		
		if ( bwapi.getFrameCount() % ( 24*10 ) == 0 )
		{
			fightingSquads.clear();
			this.enemyUnits = enemyUnits;
			this.ourUnits   = myUnits;
			if ( squadManager == null ) 
			{
				squadManager = new SquadManager( bwapi );
			}
			squadManager.updateSquadManager( enemyUnits, myUnits );
			
			setOurBases();
			setEnemyBases();
			
		}
		debug();
		
		if ( squadManager == null || squadManager.ourSquads == null )
		{
			return;
		}
		
		for ( Map.Entry<Integer, OurSquad> entry : squadManager.ourSquads.entrySet() )
		{
			if ( entry.getValue().enemyIsNearSquad() )
			{
				fightingSquads.add( entry.getValue() );
				
			}
			else
			{
				entry.getValue().followPlan( entry.getValue().plan );
			}
		}
		
	}
	
	/********************** GENERATE PLAN METHODS ********************/
	
	private synchronized void generatePlans()
	{
		time = System.currentTimeMillis();
//		System.out.println( "Started generating at " + 0 );
		myPlans    = new HashSet<MyPlan>();
		enemyPlans = new HashSet<EnemyPlan>();
		for ( int i = 0; i < 100; i++ )
		{
			
			TreeMap<Integer, OurSquad> tmpOurSquads = new TreeMap<Integer, OurSquad>( squadManager.ourSquads );
		
			for ( Map.Entry<Integer, OurSquad> entry : tmpOurSquads.entrySet() )
			{
				entry.getValue().setSimulatorCollections( enemyBases, ourBases, squadManager.enemySquads, squadManager.ourSquads );
				entry.getValue().generatePlan();
			}
			
			myPlans.add( new MyPlan ( tmpOurSquads ) );
			
		}
		
		HashMap<Integer, EnemySquad> tmpEnemySquads = new HashMap<Integer, EnemySquad>(squadManager.enemySquads);
		
		for ( int i = 0; i < 100; i++ )
		{
		
			for ( Map.Entry<Integer, EnemySquad> entry : tmpEnemySquads.entrySet() )
			{
				entry.getValue().setSimulatorCollections( enemyBases, ourBases, squadManager.enemySquads, squadManager.ourSquads );
				entry.getValue().generatePlan();
			}
			
			enemyPlans.add( new EnemyPlan( tmpEnemySquads ) );
		
		}
		
		time = System.currentTimeMillis() - time;
		
//		System.out.println( "Ended generating at: " + time );
//		System.out.println( "Our Plans Size " + myPlans.size() );
//		System.out.println( "Enemy Plans Size " + enemyPlans.size() );
		
	}
	
	private synchronized TreeMap<Integer, OurSquad> getBestPlan()
	{
		
		generatePlans();
		
		
		time = System.currentTimeMillis();
//		System.out.println( "Started computing at " + 0   );
		
		if ( myPlans.isEmpty() )
		{
//			System.out.println( "Monte Carlo my plans are empty" );
			return null;
		}
		
		Iterator<MyPlan> iter = myPlans.iterator();
		
		MyPlan bestPlan = null;
		
		int i = 0;
		int best_id = 0;
		
		for ( MyPlan myPlan : myPlans )
		{
			if ( bestPlan == null )
			{
				bestPlan = (MyPlan) iter.next();
			}
			for ( EnemyPlan enemyPlan : enemyPlans )
			{
				if ( ( System.currentTimeMillis() - time ) > 5000 ) 
				{
					best_id  = i;
//					System.out.println( "Ended computing at " + ( System.currentTimeMillis() - time ) );
					bwapi.printText( "Finished early and picked the best plan from " + best_id + " plans ");
					return bestPlan.ourSquads;
				}
				myPlan.compareToEnemyPlan( enemyPlan.getEnemySquads() );
			}
			
			if ( bestPlan.getScore() < myPlan.getScore() )
			{
				bestPlan = myPlan;
				best_id  = i;
			}
			i++;
		}
		
		bwapi.printText( "Best plan with the ID of " + best_id + " was picked ");
		
		time = System.currentTimeMillis() - time;
		
//		System.out.println( "Ended computing at " + time );
		
		if ( !myPlans.isEmpty() && ( bestPlan == null ) )
		{
//			System.out.println("Nenasiel best");
			bestPlan = (MyPlan) iter.next();
			return bestPlan.ourSquads;
		}
		
		
		return bestPlan.ourSquads;
		
		
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
//				System.out.println( "Region pri z connected bol nulovy" );
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
				HashMap <Integer, Double> connected = ourSquad.getValue().evaluateNearbyRegions();
				debugConnectedRegions( connected, ourSquad.getKey(), false );
				
				CopyOnWriteArrayList<Action> plan = ourSquad.getValue().plan;
				
				int i = 0;
				while ( i < plan.size()-2 )
				{
					int color = BWColor.BLUE;
					if ( i % 3 == 1 )
					{
						color = BWColor.RED;
					} 
					else if ( i % 3 == 2 )
					{
						color = BWColor.GREEN;
					} 
					else
					{
						color = BWColor.WHITE;
					}
					int delta = 0;
					Point a = new Point( plan.get( i ).region.getCenterX()+delta, plan.get( i ).region.getCenterY() );
					
					bwapi.drawText( a.x-(50*ourSquad.getKey()), a.y-( 10*i), "Action: " + i , false );
					++i;
					Point b = new Point( plan.get( i ).region.getCenterX()+delta, plan.get( i ).region.getCenterY() );
					bwapi.drawLine( a, b, color, false );
				}
				
			}
			
			for ( Map.Entry<Integer, EnemySquad> enemySquad : squadManager.enemySquads.entrySet() )
			{
				enemySquad.getValue().setSimulatorCollections( enemyBases, ourBases, squadManager.enemySquads, squadManager.ourSquads );
				HashMap<Integer, Double> connected = enemySquad.getValue().evaluateNearbyRegions();
				debugConnectedRegions( connected, enemySquad.getKey(), true );
			}
			
		}
	}
	
	public ArrayList<OurSquad> getFightingSquads()
	{
		return fightingSquads;
	}

	@Override
	public void run() 
	{
		while ( true )
		{
			if ( ( bwapi.getFrameCount() % ( 24*10 ) == 0 ) && ( bwapi.getFrameCount() > 24*60 ) )
			{
				squadManager.ourSquads = getBestPlan();
			}
			
			try {
				Thread.sleep( 10 );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}

