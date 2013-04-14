package javabot.combat;


import javabot.JNIBWAPI;
import javabot.model.ChokePoint;
import javabot.model.Region;
import javabot.model.Unit;

public class Base {
	
	int x,y;
	int nextActionAt = 0;
	boolean myBase;
	JNIBWAPI bwapi;
	Region baseRegion;
	
	MonteCarloPlanner monteCarlo;
	
	public Base( JNIBWAPI bwapi,  Region r, MonteCarloPlanner monteCarlo, boolean myBase )
	{
		this.bwapi      = bwapi;
		this.monteCarlo = monteCarlo;
		this.x 		    = r.getCenterX();
		this.y 			= r.getCenterY(); 
		this.myBase     = myBase;
		baseRegion      = r;
	}
	
	public Region getBaseRegion()
	{
		return baseRegion;
	}
	
	public void doNothing( int period )
	{
		nextActionAt += period;
	}
	
	/**
	 * @param choke
	 * @return returns the time that the blocking of chokepoint will take 
	 * ( 1 second for building, distance/speed for comeing to the choke )
	 */
	public void blockChoke( ChokePoint choke )
	{
		Unit builder = monteCarlo.getNearestWorker( choke.getCenterX(), choke.getCenterY() );
		int time = (int) ( MonteCarloPlanner.getDistance( builder, choke ) / bwapi.getUnitType( builder.getID() ).getTopSpeed() ) + 1;
		nextActionAt += time;
	}

}
