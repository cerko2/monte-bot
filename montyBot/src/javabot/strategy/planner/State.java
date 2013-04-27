package javabot.strategy.planner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javabot.strategy.planner.action.Action;
import javabot.strategy.planner.action.ActionRequirements;
import javabot.types.UnitType;

public class State {
	
	protected int frame;
	
	protected ArrayList<TimelineAction> actions;
	protected int minerals;
	protected int gas;
	protected int supplyUsed;
	protected int supplyMax;
	
	protected double mineralRate;
	protected double gasRate;
	
	protected HashMap<Integer, Integer> renewableResources;
	protected HashSet<Integer> availableTech;
	
	public State(int frame,
			HashMap<Integer, Integer> renewableResources,
			HashSet<Integer> availableTech,
			int minerals,
			int gas,
			int supplyUsed,
			int supplyMax,
			double mineralRate,
			double gasRate
		)
	{
		
		actions = new ArrayList<TimelineAction>();

		this.frame = frame;
		this.renewableResources = new HashMap<Integer, Integer>(renewableResources);
		this.availableTech = new HashSet<Integer>(availableTech);
		
	}

	public int getFrame(){
		return frame;
	}
	
	public boolean canExecute(Action action){
		ActionRequirements requirements = action.getRequirements();
		if (meetsRequirements(requirements)){
			return true;
		}
		
		return false;
	}
	
	private boolean meetsRequirements(ActionRequirements requirements){
		if (requirements.getMinerals() <= minerals
				&& requirements.getGas() <= gas
				&& (supplyMax - supplyUsed) >= requirements.getSupply()
				&& haveTech(requirements)
				&& canBorrow(requirements.getRenewable()))
		{
			return true;
		}
		else
			return false;
	}
	
	public boolean borrow(UnitType type){
		if (renewableResources.containsKey(type.getID())){
			int amount = renewableResources.get(type.getID());
			if (amount > 0){
				renewableResources.put(type.getID(), amount - 1);
				return true;
			}
		}
		return false;
	}
	
	public boolean canBorrow(UnitType type){
		if (renewableResources.containsKey(type.getID())){
			int amount = renewableResources.get(type.getID());
			if (amount > 0){
				return true;
			}
		}
		return false;
	}

	public boolean returnResource(UnitType type){
		if (renewableResources.containsKey(type.getID())){
			int amount = renewableResources.get(type.getID());
			if (amount >= 0){
				renewableResources.put(type.getID(), amount + 1);
				return true;
			}
		}
		return false;
	}
	
	public boolean haveTech(ActionRequirements requirements){
		for (UnitType type : requirements.getPrerequisites()){
			if (!availableTech.contains(type.getID())){
				return false;
			}
		}
		return true;
	}
	
	public void addAction(TimelineAction action){
		actions.add(action);
		action.updateState(this);
	}
	
}
