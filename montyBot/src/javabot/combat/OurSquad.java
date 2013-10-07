package javabot.combat;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import javabot.JNIBWAPI;
import javabot.model.Region;
import javabot.model.Unit;
import javabot.util.RegionUtils;
import javabot.util.UnitUtils;

public class OurSquad extends Squad
{

    public CopyOnWriteArrayList <Action> plan                      = new CopyOnWriteArrayList <Action>();

    ArrayList <Base>                     enemyBases;
    ArrayList <Base>                     ourBases;
    HashMap <Integer, EnemySquad>        enemySquads;
    TreeMap <Integer, OurSquad>          ourSquads;
    int                                  enemySquadId;
    int                                  ellapsedTime;
    int                                  score                     = 0;

    final double                         ENEMY_BASE_ATTRACTION     = 300.0;
    final double                         ENEMY_SQUAD_ATTRACTION    = 1.0;
    final double                         OPPOSITE_SQUAD_ATTRACTION = 1.0;                                // the
                                                                                                          // squad
                                                                                                          // that
                                                                                                          // I
                                                                                                          // will
                                                                                                          // surely
                                                                                                          // destroy
    final double                         OUR_BASE_ATTRACTION       = 1.0;
    final double                         OUR_SQUAD_ATTRACTION      = 1.0;

    private int                          doNotMoveTill;

    public OurSquad( JNIBWAPI bwapi, int enemySquadId )
    {
        super( bwapi );
        this.enemySquadId = enemySquadId;
    }

    public OurSquad( ArrayList <Unit> testFillSquadUnits, JNIBWAPI bwapi )
    {
        super( bwapi );
        this.squadUnits = testFillSquadUnits;
        setLeader();
    }

    public void update()
    {
        if ( squadUnits.isEmpty() )
            return;

        ellapsedTime = 0;
        antiAirPower = getAntiAirPower();
        antiGroundPower = getAntiGroundPower();
        airPower = getAirPower();
        groundPower = getGroundPower();

        setLeader();

        squadSpeed = bwapi.getUnitType( leader.getTypeID() ).getTopSpeed();

        hp = getHP();
        dps = getDps();
    }

    public void setLeader()
    {
        if ( this.leader != null && this.leader.isExists() )
            return;
        if ( squadUnits.isEmpty() )
            return;
        boolean onlyFliers = onlyFlyersInSquad();
        double slowest = 10000.0;
        Unit slowest_u = null;
        for ( Unit u: squadUnits )
        {
            if ( !onlyFliers && bwapi.getUnitType( u.getTypeID() ).isFlyer() )
            {
                continue;
            }
            if ( bwapi.getUnitType( u.getTypeID() ).getTopSpeed() < slowest )
            {
                slowest_u = u;
            }
        }

        this.leader = slowest_u;
    }

    /**************************** SIMULATOR METHODS ************************/

    public void setSimulatorCollections( ArrayList <Base> enemyBases, ArrayList <Base> ourBases, HashMap <Integer, EnemySquad> enemySquads2, TreeMap <Integer, OurSquad> ourSquads )
    {
        this.enemyBases = enemyBases;
        this.ourBases = ourBases;
        this.enemySquads = enemySquads2;
        this.ourSquads = ourSquads;
    }

    private synchronized Double evaluateRegion( Region r )
    {
        ArrayList <Double> values = new ArrayList <Double>();

        double result = 0.0;

        try
        {
            for ( Base b: enemyBases )
            {
                if ( RegionUtils.airPathToRegion( r, b.getRegion() ) * ENEMY_BASE_ATTRACTION == 0 )
                {
                    values.add( 0.0 );
                    continue;
                }
                values.add( 1 / RegionUtils.airPathToRegion( r, b.getRegion() ) * ENEMY_BASE_ATTRACTION );
            }

            for ( Base b: ourBases )
            {
                if ( RegionUtils.airPathToRegion( r, b.getRegion() ) * OUR_BASE_ATTRACTION == 0 )
                {
                    values.add( 0.0 );
                    continue;
                }
                values.add( 1 / RegionUtils.airPathToRegion( r, b.getRegion() ) * OUR_BASE_ATTRACTION );
            }

        }
        catch ( ConcurrentModificationException e )
        {
            System.err.append( "Concurrent modification Base b : ourBases exception" );
        }

        for ( Map.Entry <Integer, EnemySquad> e_squad: enemySquads.entrySet() )
        {

            double modifier = ENEMY_SQUAD_ATTRACTION;

            if ( e_squad.getKey() == enemySquadId )
            {
                modifier = OPPOSITE_SQUAD_ATTRACTION;
            }

            if ( RegionUtils.airPathToRegion( r, e_squad.getValue().getRegion( true ) ) * modifier == 0 )
            {
                values.add( 0.0 );
                continue;
            }

            values.add( 1 / RegionUtils.airPathToRegion( r, e_squad.getValue().getRegion( true ) ) * modifier );
        }

        for ( Map.Entry <Integer, OurSquad> entry: ourSquads.entrySet() )
        {
            double modifier = OUR_SQUAD_ATTRACTION;
            if ( entry.getKey() == enemySquadId )
            {
                modifier = 0.0;
            }

            if ( RegionUtils.airPathToRegion( r, entry.getValue().getRegion( entry.getValue().onlyFlyersInSquad() ) ) * modifier == 0 )
            {
                values.add( 0.0 );
                continue;
            }
            values.add( 1 / RegionUtils.airPathToRegion( r, entry.getValue().getRegion( entry.getValue().onlyFlyersInSquad() ) ) * modifier );
        }

        for ( Double d: values )
        {
            result += d;
        }
        
        return result;
    }

    public HashMap <Integer, Double> evaluateNearbyRegions()
    {
        ArrayList <Region> connectedRegions;
        if ( simulatorRegion == null )
        {
            return null;
        }

        if ( onlyFlyersInSquad() )
        {
            connectedRegions = RegionUtils.getConnectedRegions( bwapi.getMap(), simulatorRegion );
        }
        else
        {
            connectedRegions = RegionUtils.getGroundConnectedRegions( simulatorRegion, bwapi );
        }
        HashMap <Integer, Double> regions = new HashMap <Integer, Double>();
        
        
        for ( Region r: connectedRegions )
        {
            regions.put( r.getID(), evaluateRegion( r ) );
        }

        ArrayList <Double> wholeCake = new ArrayList <Double>();

        for ( Map.Entry <Integer, Double> entry: regions.entrySet() )
        {
            wholeCake.add( entry.getValue() );
        }

        double sumOfCake = 0.0;

        for ( Double d: wholeCake )
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

        for ( Map.Entry <Integer, Double> entry: regions.entrySet() )
        {
            wholeCake.add( entry.getValue() );
        }

        for ( Double d: wholeCake )
        {
            sumOfCake += d;
        }

        for ( Map.Entry <Integer, Double> entry: regions.entrySet() )
        {
            entry.setValue( ( entry.getValue() / sumOfCake ) * 100.0 );
        }

        return regions;

    }

    public boolean enemyIsNearSquad()
    {

        for ( Unit u: squadUnits )
        {
            if ( u.isUnderAttack() )
            {
                return true;
            }

            for ( Unit e: bwapi.getEnemyUnits() )
            {
                if ( UnitUtils.getDistance( u, e ) < 400 )
                {
                    return true;
                }
            }

        }

        return false;
    }

    public String planToString( CopyOnWriteArrayList <Action> plan )
    {
        String result = "Plan begins here: " + System.lineSeparator();
        for ( Action a: plan )
        {
            result += a.toString();
            result += System.lineSeparator();
        }
        result += "Plan Ends here" + System.lineSeparator();
        result += "##################################################";
        return result;
    }

    public void followPlan( CopyOnWriteArrayList <Action> plan2 )
    {
        if ( plan2 == null || plan2.isEmpty() )
        {
            return;
        }
        setLeader();
        update();

        if ( bwapi.getFrameCount() < doNotMoveTill )
        {
            return;
        }

        for ( Unit u: squadUnits )
        {
            if ( u != null && u.isExists() )
            {
                bwapi.attack( u.getID(), plan2.get( 0 ).getRegion().getCenterX(), plan2.get( 0 ).getRegion().getCenterY() );
            }
        }

        if ( plan2.isEmpty() )
            return;

        if ( plan2.get( 0 ) == null || plan2.get( 0 ).getRegion() == null || getRegion( true ) == null )
        {
            return;
        }
        if ( plan2.get( 0 ).getRegion().getID() == getRegion( true ).getID() )
        {
            plan2.remove( 0 );
        }

        if ( plan2.isEmpty() )
            return;

        if ( plan2.get( 0 ).getRegion().getID() == getRegion( true ).getID() )
        {
            doNotMoveTill = bwapi.getFrameCount() + 120;
        }

    }

    public CopyOnWriteArrayList <Action> generatePlan()
    {
        plan = new CopyOnWriteArrayList <Action>();

        simulatorRegion = RegionUtils.getRegion( bwapi.getMap(), leader );

        ellapsedTime = 0;
        while ( ellapsedTime < ( 24 * 120 ) )
        {
            Region to = chooseRegion();
            int actionTime = moveToNextRegion( to );

            plan.add( new Action( simulatorRegion, ellapsedTime, ellapsedTime + actionTime ) );
            ellapsedTime += actionTime;
        }

        return plan;
    }

    private Region chooseRegion()
    {
        HashMap <Integer, Double> connectedRegions = evaluateNearbyRegions();

        double chance = 0.0;

        if ( ( connectedRegions == null ) /* || ( connectedRegions.size() == 0 ) */)
        {
            return null;
        }

        for ( Map.Entry <Integer, Double> entry: connectedRegions.entrySet() )
        {
            chance += entry.getValue();
            entry.setValue( chance );
        }

        Random rand = new Random();

        int counter = 0;
        if ( Double.isNaN( chance ) || chance < 1 ) chance = 10.0;
        int percentage = rand.nextInt( ( int ) chance );

        while ( counter < 50 )
        {

            int minOK = 0;

            for ( Map.Entry <Integer, Double> entry: connectedRegions.entrySet() )
            {
                if ( ( percentage >= minOK ) && ( percentage <= entry.getValue() ) )
                {
                    return RegionUtils.getRegion( bwapi.getMap(), entry.getKey() );
                }
            }

            counter++;

        }

        return null;
    }

    public boolean containsUnit( Unit u )
    {
        for ( Unit e: squadUnits )
        {
            if ( e.getID() == u.getID() )
            {
                return true;
            }
        }
        return false;
    }

    /**************************** END SIMULATOR METHODS ********************/

}
