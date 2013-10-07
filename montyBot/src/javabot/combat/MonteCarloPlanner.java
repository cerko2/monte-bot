package javabot.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javabot.combat.squad_movement.SquadMovementManager;
import javabot.macro.Boss;
import javabot.model.ChokePoint;
import javabot.model.Region;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.model.Unit;
import javabot.util.BWColor;
import javabot.util.RegionUtils;
import javabot.util.UnitUtils;

public class MonteCarloPlanner extends AbstractManager implements Runnable
{

    public static final boolean DEBUG          = Boss.MONTE_MANAGER_DEBUG;
    final boolean               MONTE_ON       = true;
    private SquadManager        squadManager;
    JNIBWAPI                    bwapi;

    long                        time;

    ArrayList <Unit>            enemyUnits;
    ArrayList <Unit>            ourUnits;
    ArrayList <Base>            ourBases;
    ArrayList <Base>            enemyBases;
    ArrayList <OurSquad>        fightingSquads = new ArrayList <OurSquad>();

    SquadMovementManager        squadMover     = null;

    HashSet <MyPlan>            myPlans        = new HashSet <MyPlan>();
    HashSet <EnemyPlan>         enemyPlans     = new HashSet <EnemyPlan>();

    public MonteCarloPlanner( JNIBWAPI bwapi )
    {
        this.bwapi = bwapi;
        Thread t = new Thread( this );
        t.start();
        System.out.println( "Monte carlo started" );
    }

    private void setEnemyBases()
    {
        HashSet <Integer> regions = new HashSet <Integer>();
        enemyBases = new ArrayList <Base>();

        for ( Unit u: enemyUnits )
        {
            if ( bwapi.getUnitType( u.getTypeID() ).isBuilding() )
            {
                Region r = RegionUtils.getRegion( bwapi.getMap(), u );
                if ( !regions.contains( r.getID() ) )
                {
                    regions.add( r.getID() );
                    enemyBases.add( new Base( bwapi, r ) );
                }
            }
        }

    }

    private void setOurBases()
    {
        HashSet <Integer> regions = new HashSet <Integer>();
        ourBases = new ArrayList <Base>();
        for ( Unit u: ourUnits )
        {
            if ( bwapi.getUnitType( u.getTypeID() ).isBuilding() )
            {
                Region r = RegionUtils.getRegion( bwapi.getMap(), u );
                if ( !regions.contains( r.getID() ) )
                {
                    regions.add( r.getID() );
                    ourBases.add( new Base( bwapi, r ) );
                }
            }
        }
    }

    public HarrassSquad getHarrassSquad()
    {
        return squadManager.getHarrassSquad();
    }

    public void update( ArrayList <Unit> enemyUnits, ArrayList <Unit> myUnits )
    {

        if ( !MONTE_ON )
        {
            if ( squadMover == null )
            {
                ArrayList <Squad> squads = new ArrayList <Squad>();
                OurSquad s = new OurSquad( testFillSquadUnits(), bwapi );
                squads.add( s );
                squadMover = new SquadMovementManager( squads, bwapi );
            }
            squadMover.act();
            return;
        }

        if ( bwapi.getFrameCount() % ( 24 * 20 ) == 0 )
        {
            fightingSquads.clear();
            this.enemyUnits = enemyUnits;
            this.ourUnits = myUnits;
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

        for ( Map.Entry <Integer, OurSquad> entry: squadManager.ourSquads.entrySet() )
        {
            if ( entry.getValue().enemyIsNearSquad() )
            {
                if ( !fightingSquads.contains( entry.getValue() ) )
                {
                    fightingSquads.add( entry.getValue() );
                }
                if ( bwapi.getFrameCount() % ( 24 ) == 0 )
                {
                    battle();
                }
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
        myPlans = new HashSet <MyPlan>();
        enemyPlans = new HashSet <EnemyPlan>();
        for ( int i = 0; i < 100; i++ )
        {

            TreeMap <Integer, OurSquad> tmpOurSquads = new TreeMap <Integer, OurSquad>( squadManager.ourSquads );

            for ( Map.Entry <Integer, OurSquad> entry: tmpOurSquads.entrySet() )
            {
                entry.getValue().setSimulatorCollections( enemyBases, ourBases, squadManager.enemySquads, squadManager.ourSquads );
                entry.getValue().generatePlan();
            }

            myPlans.add( new MyPlan( tmpOurSquads ) );

        }

        HashMap <Integer, EnemySquad> tmpEnemySquads = new HashMap <Integer, EnemySquad>( squadManager.enemySquads );

        for ( int i = 0; i < 100; i++ )
        {

            for ( Map.Entry <Integer, EnemySquad> entry: tmpEnemySquads.entrySet() )
            {
                entry.getValue().setSimulatorCollections( enemyBases, ourBases, squadManager.enemySquads, squadManager.ourSquads );
                entry.getValue().generatePlan();
            }

            enemyPlans.add( new EnemyPlan( tmpEnemySquads ) );

        }

        time = System.currentTimeMillis() - time;

    }

    private synchronized TreeMap <Integer, OurSquad> getBestPlan()
    {

        generatePlans();

        time = System.currentTimeMillis();

        if ( myPlans.isEmpty() )
        {
            return null;
        }

        Iterator <MyPlan> iter = myPlans.iterator();

        MyPlan bestPlan = null;

        int i = 0;
        int best_id = 0;

        for ( MyPlan myPlan: myPlans )
        {
            if ( bestPlan == null )
            {
                bestPlan = ( MyPlan ) iter.next();
            }
            for ( EnemyPlan enemyPlan: enemyPlans )
            {
                if ( ( System.currentTimeMillis() - time ) > 5000 )
                {
                    best_id = i;
                    bwapi.printText( "Finished early and picked the best plan from " + best_id + " plans " );
                    return bestPlan.ourSquads;
                }
                myPlan.compareToEnemyPlan( enemyPlan.getEnemySquads() );
            }

            if ( bestPlan.getScore() < myPlan.getScore() )
            {
                bestPlan = myPlan;
                best_id = i;
            }
            i++;
        }

        bwapi.printText( "Best plan with the ID of " + best_id + " was picked " );

        time = System.currentTimeMillis() - time;

        if ( !myPlans.isEmpty() && ( bestPlan == null ) )
        {
            bestPlan = ( MyPlan ) iter.next();
            return bestPlan.ourSquads;
        }

        return bestPlan.ourSquads;

    }

    private void debugConnectedRegions( HashMap <Integer, Double> connected, int squad_id, boolean enemySquad )
    {

        OurSquad squad = squadManager.getOurSquad( squad_id );

        if ( squad == null )
        {
            return;
        }

        if ( connected == null )
        {
            return;
        }
        
        for ( Map.Entry <Integer, Double> entry: connected.entrySet() )
        {
            int regionId = entry.getKey();
            if ( regionId > 1000 )
                regionId -= 1000000;
            Region r = RegionUtils.getRegion( bwapi.getMap(), regionId );

            if ( r == null )
            {
                continue;
            }

            if ( !enemySquad )
            {
                if ( r == null || squad == null || squad.getRegion() == null )
                {
                    continue;
                }
                bwapi.drawText( r.getCenterX() - 50, r.getCenterY() + ( 10 * ( squad_id + 1 ) ), "Our Squad ID: " + squad_id + " Chance: " + entry.getValue().intValue() + "%%" + " Region ID: "
                        + squad.getRegion().getID(), false );
            }
            else
            {
                bwapi.drawText( r.getCenterX() - 50, r.getCenterY() - ( 10 * ( squad_id + 1 ) ), "Enemy Squad ID: " + squad_id + " Chance: " + entry.getValue().intValue() + "%%", false );
            }
        }
    }

    /********************** END GENERATE PLAN METHODS ****************/

    public void debug()
    {
        if ( MonteCarloPlanner.DEBUG )
        {
            
            squadManager.debug();

            for ( Map.Entry <Integer, OurSquad> ourSquad: squadManager.ourSquads.entrySet() )
            {
                ourSquad.getValue().setSimulatorCollections( enemyBases, ourBases, squadManager.enemySquads, squadManager.ourSquads );
                HashMap <Integer, Double> connected = ourSquad.getValue().evaluateNearbyRegions();
                debugConnectedRegions( connected, ourSquad.getKey(), false );
            }

            for ( Region r: bwapi.getMap().getRegions() )
            {
                bwapi.drawText( r.getCenterX(), r.getCenterY(), "Region s id: " + r.getID(), false );
            }

            for ( ChokePoint c: bwapi.getMap().getChokePoints() )
            {
                if ( RegionUtils.chokeBlockedByNeutral( bwapi, c ) )
                {
                    bwapi.drawCircle( c.getCenterX(), c.getCenterY(), 100, BWColor.YELLOW, false, false );
                }
            }

            for ( Base b: enemyBases )
            {
                bwapi.drawCircle( b.baseRegion.getCenterX(), b.baseRegion.getCenterY(), 100, BWColor.RED, false, false );
            }

        }
    }

    public ArrayList <OurSquad> getFightingSquads()
    {
        return fightingSquads;
    }

    @Override
    public void run()
    {
        while ( true )
        {
            if ( ( bwapi.getFrameCount() % ( 24 * 10 ) == 0 ) && ( bwapi.getFrameCount() > 24 * 10 ) )
            {
                if ( MONTE_ON )
                {
                    squadManager.ourSquads = getBestPlan();
                }
            }

            try
            {
                Thread.sleep( 10 );
            }
            catch ( InterruptedException e )
            {
                e.printStackTrace();
            }
        }
    }

    private void battle()
    {
        for ( OurSquad ourSquad: fightingSquads )
        {
            for ( Unit u: ourSquad.getSquadUnits() )
            {
                if ( !u.isExists() )
                    continue;
                else
                {
                    Unit enemy = FindNearestEnemy( u );
                    if ( ( enemy != null ) && ( enemy.isExists() ) )
                        bwapi.attack( u.getID(), enemy.getID() );
                }
            }
        }
    }

    private Unit FindNearestEnemy( Unit u )
    {
        Unit nearestEnemy = null;

        int nearestDist = 1111111;
        for ( Unit e: bwapi.getEnemyUnits() )
        {
            if ( UnitUtils.getDistance( u, e ) < nearestDist )
            {
                nearestDist = UnitUtils.getDistance( u, e );
                nearestEnemy = e;
            }
        }

        return nearestEnemy;
    }

    @Override
    public void unitDestroy( int unitID )
    {
        super.unitDestroy( unitID );
        System.out.println( "unit destroyed" );
    }

    public SquadManager getSquadManager()
    {
        return this.squadManager;
    }

    // //////////////////// TEST FUNCTIONS ONLY ///////////////////////
    private ArrayList <Unit> testFillSquadUnits()
    {
        ArrayList <Unit> squadUnits = new ArrayList <Unit>();
        for ( Unit u: bwapi.getMyUnits() )
        {
            if ( SquadManager.isSpecialUnit( u.getTypeID() )
                    || ( !bwapi.getUnitType( u.getTypeID() ).isWorker() && bwapi.getUnitType( u.getTypeID() ).isAttackCapable() && !bwapi.getUnitType( u.getTypeID() ).isBuilding() ) )
            {
                squadUnits.add( u );
            }
        }
        return squadUnits;
    }
}
