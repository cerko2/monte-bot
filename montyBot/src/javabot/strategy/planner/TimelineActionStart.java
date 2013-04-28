package javabot.strategy.planner;

import javabot.strategy.planner.action.Action;
import javabot.strategy.planner.action.ActionRequirements;

public class TimelineActionStart extends TimelineAction{

	public TimelineActionStart(Action action){
		this.action = action;
	}
	
	@Override
	protected void updateState(State state, boolean updateFrame) {
		
		ActionRequirements requirements = action.getRequirements();
		state.borrow(requirements.getRenewable());
		state.setMinerals(state.getMinerals() - requirements.getMinerals());
		state.setGas(state.getGas() - requirements.getGas());
		state.setSupplyUsed(state.getSupplyUsed() - requirements.getSupply());
	}
	
}
