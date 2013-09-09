package javabot.combat;

import javabot.JNIBWAPI;
import javabot.model.Region;

public class Base 
{
	
	Region baseRegion;
	JNIBWAPI bwapi;
	
	public Base( JNIBWAPI bwapi, Region region )
	{
		this.bwapi 		= bwapi;
		this.baseRegion = region;
	}
	
	public Region getRegion()
	{
		return baseRegion;
	}
	
}
