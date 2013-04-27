package javabot.strategy.planner;

import java.util.ArrayList;
import java.util.TreeMap;

import javabot.strategy.planner.action.Action;

public class Scheduler {
	
	private State initialState;
	
	private TreeMap<Integer, State> timeline;
	private ArrayList<Action> sequentialPlan;
	
	public Scheduler(){
		
		sequentialPlan = new ArrayList<Action>();
		timeline = new TreeMap<Integer, State>();
		
	}
	
	public void schedulePlan(ArrayList<Action> plan){
		
		timeline.clear();
		sequentialPlan.clear();
		
		sequentialPlan.addAll(plan);
		
		while(!sequentialPlan.isEmpty()){
			if (timeline.isEmpty()){
				if (initialState.canExecute(sequentialPlan.get(0))){
					
				}
			}
		}
		
	}
	
	public void setInitialState(State initialState){
		this.initialState = initialState;
	}
	
}
