package javabot.combat;

import java.util.ArrayList;
import java.util.TreeMap;

public class Plan implements Comparable<Plan> 
{

	TreeMap<Integer, ArrayList<Action>> actionsInTime = new TreeMap<Integer, ArrayList<Action>>();
	
	private int planGrade;
	private boolean myPlan;
	private int ellapsedTime = 0;
	
	//our squad ID => squad
	private TreeMap<Integer, Squad> ourSquads;
	
	// enemy squad ID => squad
	private TreeMap<Integer, Squad> enemySquads;
	
	private ArrayList<Base> ourBases;
	private ArrayList<Base> enemyBases;
	
	/**
	 * Constructor
	 * @param squadManager  - access to squads for this plan
	 * @param myBases    	- a collection of my regions ( only if this is my plan )
	 * @param enemyBases2 	- a collection of enemy regions 
	 * @param myPlan     	- determine whether or not the plan is for our squads, or for enemies
	 */
	public Plan( SquadManager 	 squadManager, 
				 ArrayList<Base> 	  myBases,
				 ArrayList<Base> enemyBases,
				 boolean 				myPlan 
				) 
	{
		this.myPlan = myPlan;
		if ( myPlan ) 
		{
			this.enemySquads = squadManager.getEnemySquadsTree();
			this.ourSquads   = squadManager.getOurSquadsTree();
			this.ourBases    = myBases;
			this.enemyBases  = enemyBases;
		} 
		else
		{   // for enemy, the assignments are reversed 
			// e.g. mySquads are his enemy squads etc.
			this.enemySquads = squadManager.getOurSquadsTree();
			this.ourSquads   = squadManager.getEnemySquadsTree();
			this.ourBases    = enemyBases;
			this.enemyBases  = myBases;
		}
	}

	public void evaluate( int value )
	{
		planGrade = value;
	}
	
	public int getPlanGrade() 
	{
		return planGrade;
	}
	
	@Override
	public int compareTo( Plan o ) 
	{
		return 0;
	}
	
	public void generatePlan()
	{
		while ( ellapsedTime < 120 )
		{
		}
	}

}
