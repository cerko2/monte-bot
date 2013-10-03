package javabot.combat.squad_movement;

import javabot.JNIBWAPI;

public class FlyerSubSquad extends SubSquad
{

    SubSquad melee;
    SubSquad semiRanged;
    SubSquad ranged;
    SubSquad superRanged;

    public FlyerSubSquad( JNIBWAPI bwapi, int look_forward, SubSquad melee, SubSquad semiRanged, SubSquad ranged, SubSquad superRanged, int range )
    {
        super( bwapi, look_forward, range );
        this.melee = melee;
        this.semiRanged = semiRanged;
        this.ranged = ranged;
        this.superRanged = superRanged;
    }

}
