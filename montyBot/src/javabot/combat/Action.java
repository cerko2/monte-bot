package javabot.combat;

import javabot.model.Region;

public class Action {
	
	Region region;
	int start, end;
	
	public Action( Region r, int start, int end )
	{
		region 	   = r;
		this.start = start;
		this.end   = end;
	}
	
	public Region getRegion()
	{
		return region;
	}
	
	public boolean happenedInTime( int time )
	{
		return ( start < time ) && ( end >= time );
	}
	
	@Override
	public String toString() 
	{
		return "Region: " + region.getID() + " start: " + start + " end: " + end;
	}
	
}
