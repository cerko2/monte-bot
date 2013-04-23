package javabot.strategy.planner.action;

import java.util.HashSet;

import javabot.types.UnitType;


public abstract class Action {
	
	protected int duration;
	protected ActionRequirements requirements;
	protected UnitType producedType;
	protected int supplyProvided;
	
	protected Action(){
		duration = 0;
		supplyProvided = 0;
		requirements = new ActionRequirements(0, 0, 0, null, new HashSet<UnitType>());
	}
	
	public void setDuration(int duration){
		this.duration = duration;
	}
	
	public ActionRequirements getRequirements(){
		return requirements;
	}
	
	public UnitType getProducedType(){
		return producedType;
	}
	
}
