package javabot.strategy.planner;

import javabot.strategy.planner.action.Action;

public abstract class TimelineAction {

	protected Action action;
	protected State linkedState;
	
	abstract protected void updateState(State state);
	
}
