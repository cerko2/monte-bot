package javabot.strategy.planner.action;

import java.util.HashSet;

import javabot.types.UnitType;

public class ActionRequirements {
	
	private int minerals;
	private int gas;
	private int supply;
	private UnitType renewable;
	private HashSet<UnitType> prerequisites;
	
	public ActionRequirements(int minerals, int gas, int supply, UnitType renewable, HashSet<UnitType> prerequisites) {
		this.minerals = minerals;
		this.gas = gas;
		this.supply = supply;
		this.renewable = renewable;
		this.prerequisites = prerequisites;
	}
	
	public int getMinerals() {
		return minerals;
	}
	
	public int getGas() {
		return gas;
	}
	
	public int getSupply() {
		return supply;
	}
	
	public UnitType getRenewable() {
		return renewable;
	}
	
	public HashSet<UnitType> getPrerequisites(){
		return prerequisites;
	}
	
}
