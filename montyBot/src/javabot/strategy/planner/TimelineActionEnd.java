package javabot.strategy.planner;

import javabot.strategy.planner.action.Action;
import javabot.strategy.planner.action.ActionRequirements;

public class TimelineActionEnd extends TimelineAction{

	public TimelineActionEnd(Action action){
		this.action = action;
	}
	
	@Override
	protected void updateState(State state, boolean updateFrame) {
		
		ActionRequirements requirements = action.getRequirements();
		state.returnResource(requirements.getRenewable());
		state.setSupplyMax(state.getSupplyMax() + action.getSupplyProvided());
		state.addTech(action.getProducedType());
		
		if (updateFrame) 
			state.setFrame(state.getFrame() + action.getDuration());
		
	}

}
