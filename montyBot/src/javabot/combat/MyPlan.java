package javabot.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javabot.model.Region;

public class MyPlan 
{
	TreeMap<Integer, OurSquad> ourSquads;
	
	int score = 0;
	
	public MyPlan( TreeMap<Integer, OurSquad> ourSquads ) 
	{
		this.ourSquads = ourSquads;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public void setScore( int value )
	{
		score = value;
	}
	
	public void compareToEnemyPlan( HashMap<Integer, EnemySquad> enemySquads  )
	{
		for ( int i = 0; i < 24*120; i+= 24 )
		{
			solveCollision( i, enemySquads );
		}
	}

	private void solveCollision( int time, HashMap<Integer, EnemySquad> enemySquads ) 
	{
		for ( Map.Entry<Integer, OurSquad> ourSquad : ourSquads.entrySet() )
		{
			ArrayList<Region> ourSquadInRegions = new ArrayList<Region>();
			for ( Action a : ourSquad.getValue().plan )
			{
				if ( a.happenedInTime( time ) )
				{
					ourSquadInRegions.add( a.getRegion() );
				}
			}
			
			ArrayList<Region> enemySquadsInRegions = new ArrayList<Region>();
			
			for ( Map.Entry<Integer, EnemySquad> enemySquad : enemySquads.entrySet() )
			{
				for ( Action e : enemySquad.getValue().plan )
				{
					if ( e.happenedInTime( time ) )
					{
						enemySquadsInRegions.add( e.getRegion() );
					}
					
					for ( Region r : ourSquadInRegions )
					{
						for ( Region reg : enemySquadsInRegions )
						{
							if ( r.getID() == reg.getID() )
							{
								ourSquad.getValue().simulateSquadFight( ourSquad.getValue(), enemySquad.getValue() );
								setScore( getScore() + ourSquad.getValue().score );
							}
						}
					}
					
				}
			}
			
			
		}
	}
}
