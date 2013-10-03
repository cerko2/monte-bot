package javabot.combat.squad_movement;

import java.awt.Point;
import java.util.ArrayList;

import javabot.JNIBWAPI;
import javabot.model.ChokePoint;
import javabot.model.Region;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;
import javabot.util.RegionUtils;
import javabot.util.UnitUtils;

public class SubSquad
{
    
    private ArrayList<Unit> units = new ArrayList<Unit>();
    
    JNIBWAPI bwapi;
    SubSquad match_squad;
    
    // the look_forward flag is used to identify whether 
    // the units should wait for a squad ( Melee for SuperRanged )
    // or, more commonly to wait, till all units from 
    // a shorter ranged squad are before units from a farther ranged squad
    //
    // Common values are:
    //   0 => look backward
    //   1 => look forward
    //   3 => misc
    int look_forward = 3;
    
    // range says whether the subsquad is for:
    // value | meaning
    //   0      melee units         //zealot, DT
    //   1      subranged units     // archon
    //   2      ranged units        // dragoon
    //   3      superranged units   // reaver, HT
    int range;
    
    public SubSquad( JNIBWAPI bwapi, int look_forward, int range )
    {
        this.bwapi = bwapi;
    }
    
    public SubSquad( JNIBWAPI bwapi, SubSquad match_squad, int look_forward, int range )
    {
        this.bwapi = bwapi;
        this.match_squad = match_squad;
    }
    
    public static boolean isMelee( Unit u )
    {
        return ( u.getTypeID() == UnitTypes.Protoss_Zealot.ordinal() )     || 
               ( u.getTypeID() == UnitTypes.Protoss_Dark_Templar.ordinal() );   
    }
    
    public static boolean isSemiRanged( Unit u )
    {
        return u.getTypeID() == UnitTypes.Protoss_Archon.ordinal();
    }
    
    public static boolean isRanged( Unit u )
    {
        return u.getTypeID() == UnitTypes.Protoss_Dragoon.ordinal();
    }
    
    public static boolean isSuperRanged( Unit u )
    {
        return ( u.getTypeID() == UnitTypes.Protoss_High_Templar.ordinal() ) ||
               ( u.getTypeID() == UnitTypes.Protoss_Reaver.ordinal() )       ||
               ( u.getTypeID() == UnitTypes.Protoss_Dark_Archon.ordinal() );
    }
    
    public static boolean isFlyer( Unit u )
    {
        return ( u.getTypeID() == UnitTypes.Protoss_Arbiter.ordinal()   ||
               ( u.getTypeID() == UnitTypes.Protoss_Carrier.ordinal() ) ||  
               ( u.getTypeID() == UnitTypes.Protoss_Corsair.ordinal() ) ||
               ( u.getTypeID() == UnitTypes.Protoss_Scout.ordinal() ) );
    }
    
    public void put( Unit u )
    {
        units.add( u );
    }
    
    public void debug( int color )
    {
        for ( Unit u : units )
        {
            bwapi.drawCircle( u.getX(), u.getY(), 10, color, false, false );
        }
    }
    
    public ArrayList<Unit> getSquadUnits()
    {
        return units;
    }
    
    /**
     * @param u
     * @param squad
     * @return true, if a unit from longer distance subsquad is in front of any unit of a 
     * shorter distance subsquad ( e.g. longer == dragoon is before shorter == zealot )
     */
    private boolean isStoppingUnit( Unit longer, SubSquad shorter_range_squad, Region dest_reg, Region from_reg )
    {
        ChokePoint connection = RegionUtils.getConnectionChoke( from_reg, dest_reg );
        for ( Unit shorter : shorter_range_squad.getSquadUnits() )
        {
            int shorter_dist;
            int longer_dist;
            if ( dest_reg.getID() != from_reg.getID() )
            {
                shorter_dist = UnitUtils.getDistance( shorter, new Point( connection.getCenterX(), connection.getCenterY() ) );
                longer_dist = UnitUtils.getDistance( longer, new Point( connection.getCenterX(), connection.getCenterY() ) );
            }
            else
            {
                shorter_dist = UnitUtils.getDistance( shorter, new Point( dest_reg.getCenterX(), dest_reg.getCenterY() ) );
                longer_dist  = UnitUtils.getDistance( longer , new Point( dest_reg.getCenterX(), dest_reg.getCenterY() ) );
            }
            
            if ( shorter_dist > longer_dist )
            {
                return true;
            }
            
        }
        return false;
    }

    public void move( Region dest_reg, Region from_reg )
    {
    }

    public void setMatchingSquad( SubSquad match_squad )
    {
        this.match_squad = match_squad;
    }
    
}
