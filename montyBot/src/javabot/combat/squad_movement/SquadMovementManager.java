package javabot.combat.squad_movement;

import java.util.ArrayList;

import javabot.combat.Squad;
import javabot.model.Region;
import javabot.model.Unit;
import javabot.util.BWColor;
import javabot.util.RegionUtils;
import javabot.JNIBWAPI;

public class SquadMovementManager
{

    private static final boolean DEBUG = true;
    JNIBWAPI             bwapi;
    boolean              was   = false;

    ArrayList <Region> testActions = new ArrayList<Region>();
    private ArrayList <Squad> squads; 
    ArrayList<SquadMover> movers = new ArrayList<SquadMover>();

    public SquadMovementManager( ArrayList<Squad> squads, JNIBWAPI bwapi )
    {
        this.bwapi = bwapi;
        this.squads = squads;
        for ( Squad s : squads )
        {
            movers.add( new SquadMover( s, bwapi ) );
        }
    }

    
    public void act()
    {
        debug();        
        
        if ( bwapi.getFrameCount() % 7 == 0 )
        {
            for ( SquadMover mover : movers )
            {
                mover.act();
            }
        }
    }

    public void debug()
    {
        if ( !DEBUG )
            return;

        bwapi.drawTargets( true );
        
        for ( Unit u: squads.get( 0 ).getSquadUnits() )
        {
            if ( squads.get( 0 ).getLeader().getID() == u.getID() )
            {
                bwapi.drawCircle( u.getX(), u.getY(), 2, BWColor.WHITE, true, false );
                continue;
            }
            bwapi.drawCircle( u.getX(), u.getY(), 2, BWColor.RED, true, false );
        }
        
        for ( Region r : bwapi.getMap().getRegions() )
        {
            bwapi.drawText( r.getCenterX(), r.getCenterY(), "Region ID: " + r.getID(), false );
            RegionUtils.drawRegion( bwapi, r, BWColor.WHITE );
        }
        
    }

    
}
