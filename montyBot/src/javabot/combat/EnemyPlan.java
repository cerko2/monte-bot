package javabot.combat;

import java.util.HashMap;

public class EnemyPlan 
{

	private HashMap<Integer, EnemySquad> enemySquads;

	public EnemyPlan( HashMap<Integer, EnemySquad> enemySquads )
	{
		this.enemySquads = enemySquads;
	}
	
	public HashMap<Integer, EnemySquad> getEnemySquads()
	{
		return this.enemySquads;
	}
	
	
}
