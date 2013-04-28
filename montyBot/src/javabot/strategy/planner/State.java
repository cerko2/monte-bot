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
	
	protected int mineralWorkers;
	protected int gasWorkers;
	
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
			double gasRate,
			int mineralWorkers,
			int gasWorkers
		)
	{
		
		actions = new ArrayList<TimelineAction>();

		this.frame = frame;
		this.renewableResources = new HashMap<Integer, Integer>(renewableResources);
		this.availableTech = new HashSet<Integer>(availableTech);
		this.minerals = minerals;
		this.gas = gas;
		this.supplyUsed = supplyUsed;
		this.supplyMax = supplyMax;
		this.mineralRate = mineralRate;
		this.gasRate = gasRate;
		this.mineralWorkers = mineralWorkers;
		this.gasWorkers = gasWorkers;
		
	}
	
	public State(State state){
		this(state.getFrame(),
				new HashMap<Integer, Integer>(state.getRenewableResources()),
				new HashSet<Integer>(state.getAvailableTech()),
				state.getMinerals(),
				state.getGas(),
				state.getSupplyUsed(),
				state.getSupplyMax(),
				state.getMineralRate(),
				state.getGasRate(),
				state.getMineralWorkers(),
				state.getGasWorkers()
			);
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
	
	public void addAction(TimelineAction action, boolean updateFrame){
		actions.add(action);
		action.updateState(this, updateFrame);
		action.linkState(this);
	}
	
	public void addTech(UnitType type){
		if (type.isBuilding()){
			availableTech.add(type.getID());
		}
	}
	
	public int delay(int frames){
		frame += frames;
		
		minerals += mineralRate * frames;
		gas += gasRate * frames;
		
		return frame;
	}
	
	public void updateIncome(State startState){
		int frames = frame - startState.getFrame();
		minerals += mineralRate * frames;
		gas += gasRate * frames;
	}

	public int getMinerals() {
		return minerals;
	}

	public void setMinerals(int minerals) {
		this.minerals = minerals;
	}

	public int getGas() {
		return gas;
	}

	public void setGas(int gas) {
		this.gas = gas;
	}

	public double getMineralRate() {
		return mineralRate;
	}

	public void setMineralRate(double mineralRate) {
		this.mineralRate = mineralRate;
	}

	public double getGasRate() {
		return gasRate;
	}

	public void setGasRate(double gasRate) {
		this.gasRate = gasRate;
	}

	public int getMineralWorkers() {
		return mineralWorkers;
	}

	public void setMineralWorkers(int mineralWorkers) {
		this.mineralWorkers = mineralWorkers;
	}

	public int getGasWorkers() {
		return gasWorkers;
	}

	public void setGasWorkers(int gasWorkers) {
		this.gasWorkers = gasWorkers;
	}

	public ArrayList<TimelineAction> getActions() {
		return actions;
	}

	public int getSupplyUsed() {
		return supplyUsed;
	}

	public int getSupplyMax() {
		return supplyMax;
	}

	public HashMap<Integer, Integer> getRenewableResources() {
		return renewableResources;
	}

	public HashSet<Integer> getAvailableTech() {
		return availableTech;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}
	
	public void setSupplyUsed(int supplyUsed){
		this.supplyUsed = supplyUsed;
	}
	
	public void setSupplyMax(int supplyMax){
		this.supplyMax = supplyMax;
	}
	
}
