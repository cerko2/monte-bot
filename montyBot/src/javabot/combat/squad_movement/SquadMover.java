package javabot.combat.squad_movement;

import java.awt.Point;
import java.util.ArrayList;

import javabot.JNIBWAPI;
import javabot.combat.*;
import javabot.model.Region;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;
import javabot.util.BWColor;
import javabot.util.RegionUtils;
import javabot.util.UnitUtils;

public class SquadMover
{

    Squad                squad;
    ArrayList <Integer>  regions = new ArrayList <Integer>();
    public final boolean DEBUB   = false;

    JNIBWAPI             bwapi;

    SubSquad             melee;
    SubSquad             semiRanged;
    SubSquad             ranged;
    SubSquad             superRanged;
    FlyerSubSquad        flyers;

    int                  waitTill;
    int                  range   = 0;

    public SquadMover( Squad squad, JNIBWAPI bwapi )
    {
        this.squad = squad;
        this.bwapi = bwapi;
        waitTill = bwapi.getFrameCount();
        setRegions();
        addUnitsToSubSquads();
    }

    private void resetSubSquads( JNIBWAPI bwapi )
    {
        melee = new SubSquad( bwapi, 0, range );
        semiRanged = new SubSquad( bwapi, melee, 1, range );
        ranged = new SubSquad( bwapi, semiRanged, 1, range );
        superRanged = new SubSquad( bwapi, ranged, 1, range );
        melee.setMatchingSquad( superRanged );
        flyers = new FlyerSubSquad( bwapi, 3, melee, semiRanged, ranged, superRanged, range );
    }

    private void addUnitsToSubSquads()
    {
        for ( Unit u: squad.getSquadUnits() )
        {
            addUnitToSubsquad( u );
        }
    }

    private void addUnitToSubsquad( Unit u )
    {
        if ( SubSquad.isMelee( u ) )
        {
            melee.put( u );
        }
        if ( SubSquad.isSemiRanged( u ) )
        {
            semiRanged.put( u );
        }
        if ( SubSquad.isRanged( u ) )
        {
            ranged.put( u );
        }
        if ( SubSquad.isSuperRanged( u ) )
        {
            superRanged.put( u );
        }
    }

    private void setRegions()
    {
        regions.add( 5 );
        regions.add( 10 );
        regions.add( 1 );
        regions.add( 10 );
        regions.add( 5 );
        regions.add( 3 );
        regions.add( 6 );
        regions.add( 6 );
        regions.add( 6 );
        regions.add( 6 );
        regions.add( 4 );
    }

    public void act()
    {
        if ( squad.getLeader() == null )
            return;

        // if the next target region would be the same region as the one I am on
        // now,
        // this means the desired action is to wait on this region. The waitTill
        // is
        // calculated later in method after encountering this situation.
        if ( waitTill > bwapi.getFrameCount() )
        {
            moveDistantUnits();
            return;
        }

        Unit tmpLeader = squad.getLeader();
        Region tmpRegion = null;
        if ( !regions.isEmpty() )
        {
            tmpRegion = RegionUtils.getRegionById( bwapi, regions.get( 0 ) );
        }

        if ( tmpRegion == null )
        {
            return;
        }

        if ( squadInTarget( tmpRegion, tmpLeader ) )
        {
            // as mentioned above, if the next region would be the same as the
            // region
            // our squad is on, wait n seconds.
            if ( RegionUtils.getRegion( bwapi.getMap(), regions.remove( 0 ) ).getID() == tmpRegion.getID() )
            {
                wait_n_seconds( 5 );
            }
        }

        move( tmpRegion );
        debug();
    }

    private void moveDistantUnits()
    {
        if ( regions.isEmpty() )
            return;
        Region tmpRegion = RegionUtils.getRegion( bwapi.getMap(), regions.get( 0 ) );
        for ( Unit u: getDistantUnits( tmpRegion ) )
        {
            if ( u.getTypeID() != UnitTypes.Protoss_Shuttle.ordinal() || !shuttleControl() )
            {
                bwapi.move( u.getID(), tmpRegion.getCenterX(), tmpRegion.getCenterY() );
            }
            shuttleControl();
        }

    }

    private boolean squadInTarget( Region tmpRegion, Unit tmpLeader )
    {
        return ( ( ( double ) getDistantUnits( tmpRegion ).size() / ( double ) squad.getSquadUnits().size() ) < 0.25 ) && leaderInTarget( tmpRegion, tmpLeader );
    }

    private boolean leaderInTarget( Region tmpRegion, Unit tmpLeader )
    {
        return UnitUtils.getDistance( tmpLeader.getX(), tmpLeader.getY(), tmpRegion.getCenterX(), tmpRegion.getCenterY() ) < 400;
    }

    private boolean shuttleControl()
    {
        Unit shuttle = getShuttle();
        if ( shuttle == null || squad.getLeader().isLoaded() )
        {
            return false;
        }
        bwapi.load( shuttle.getID(), squad.getLeader().getID() );
        return true;
    }

    private void move( Region tmpRegion )
    {
        for ( Unit u: squad.getSquadUnits() )
        {
            if ( u.getTypeID() != UnitTypes.Protoss_Shuttle.ordinal() || !shuttleControl() )
            {
                bwapi.move( u.getID(), tmpRegion.getCenterX(), tmpRegion.getCenterY() );
            }
            shuttleControl();
        }
    }

    private void wait_n_seconds( int n )
    {
        waitTill = bwapi.getFrameCount() + 24 * n;
    }

    /**
     * @param tmpRegion
     * @return ArrayList of units that are not together with the main part of
     *         the squad.
     */
    private ArrayList <Unit> getDistantUnits( Region tmpRegion )
    {
        if ( tmpRegion == null )
        {
            return null;
        }
        ArrayList <Unit> result = new ArrayList <Unit>();
        for ( Unit u: squad.getSquadUnits() )
        {
            if ( RegionUtils.getRegion( bwapi.getMap(), new Point( u.getX(), u.getY() ) ) == null )
            {
                continue;
            }
            if ( RegionUtils.getRegion( bwapi.getMap(), new Point( u.getX(), u.getY() ) ).getID() != tmpRegion.getID() )
            {
                result.add( u );
            }
        }
        return result;
    }

    private Unit getShuttle()
    {
        for ( Unit u: squad.getSquadUnits() )
        {
            if ( u.getTypeID() == UnitTypes.Protoss_Shuttle.ordinal() )
            {
                return u;
            }
        }
        return null;

    }

    private void debug()
    {
        if ( !DEBUB )
        {
            return;
        }

        melee.debug( BWColor.WHITE );
        semiRanged.debug( BWColor.BLUE );
        ranged.debug( BWColor.GREEN );
        superRanged.debug( BWColor.RED );
        flyers.debug( BWColor.PURPLE );

    }

}
