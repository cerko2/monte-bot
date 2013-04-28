package javabot.strategy.planner;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import javabot.strategy.planner.action.Action;
import javabot.strategy.planner.action.ActionRequirements;

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
			Action action = sequentialPlan.get(0);
			
			if (timeline.isEmpty()){
				if (initialState.canExecute(action)){
					addInitialStates(initialState, action);
				}
				else {
					State startState = delayUntillReady(initialState, action);
					addInitialStates(startState, action);
				}
			}
			else {
				scheduleAction(action);
			}
			
		}
		
	}
	
	// ideme odzadu az kym sa nenajde state kde sa akcia neda vykonat
	// potom najdeme moment medzi tymito dvoma state-mi kde sa da vykonat
	private void scheduleAction(Action action){
		
		NavigableMap<Integer, State> reverseTimeline = timeline.descendingMap();
		
		for (Entry<Integer, State> entry : reverseTimeline.entrySet()){
			if (!entry.getValue().canExecute(action)){
				Entry<Integer, State> laterState = reverseTimeline.lowerEntry(entry.getKey());
				
				if (laterState == null){
					
				}
				else {
					placeBetweenStates(entry.getValue(), laterState.getValue(), action);
				}
			}
			else {
				if (reverseTimeline.lastKey() == entry.getKey()){
					//move to the beginning
				}
			}
		}
		
	}
	
	private void placeBetweenStates(State earlier, State later, Action action){
		ActionRequirements requirements = action.getRequirements();
		
		State startState = null;
		
		//if we have enough supply and tech then we dont have resources
		if (supplyBuffer(earlier) >= requirements.getSupply()
				&& earlier.haveTech(requirements))
		{
			
			startState = delayUntillReady(earlier, action);
			if (later.getFrame() == startState.getFrame()){
				startState = later;
			}
			
		}
		//supply/tech added only at later state so we add the action there
		else {
			startState = later;
		}
		
		TimelineActionStart startAction = new TimelineActionStart(action);
		startState.addAction(startAction, true);
		
		TimelineActionEnd endAction = new TimelineActionEnd(action);
		State endState = null;
		
		if (timeline.containsKey(startState.getFrame() + action.getDuration())){
			endState = timeline.get(startState.getFrame() + action.getDuration());
			endState.addAction(endAction, false);
		}
		else {
			//copy earlier states data and go from there
			endState = new State(getEarlierState(startState.getFrame() + action.getDuration()));
			
			//reset states frame so that updateState sets it correctly
			endState.setFrame(startState.getFrame());
			endState.addAction(endAction, true);
			timeline.put(endState.getFrame(), endState);
		}
		//get state before endState, and update end income
		State earlierState = getEarlierState(endState.getFrame());
		endState.updateIncome(earlierState);
		
		//update following states with actions effects
		for (Entry<Integer, State> entry : timeline.tailMap(endState.getFrame(), false).entrySet()){
			endAction.updateState(entry.getValue(), false);
		}
		
		
	}
	
	private State getEarlierState(int frame){
		return timeline.lowerEntry(frame).getValue();
	}
	
	private int supplyBuffer(State state){
		return state.getSupplyMax() - state.getSupplyUsed();
	}
	
	private void addInitialStates(State state, Action action){
		TimelineActionStart startAction = new TimelineActionStart(action);
		State startState = new State(state);
		startState.addAction(startAction, true);
		timeline.put(startState.getFrame(), startState);
		
		TimelineActionEnd endAction = new TimelineActionEnd(action);
		State endState = new State(startState);
		endState.addAction(endAction, true);
		endState.setMinerals(endState.getMinerals() + (int) (action.getDuration() * endState.getMineralWorkers() * Planner.minIncomePerWorker));
		endState.setGas(endState.getGas() + (int) (action.getDuration() * endState.getGasWorkers() * Planner.gasIncomePerWorker)); 
		timeline.put(endState.getFrame(), endState);
	}
	
	private State delayUntillReady(State state, Action action){
		State delayed = new State(state);
		
		ActionRequirements requirements = action.getRequirements();
		
		int minBuffer = requirements.getMinerals() - delayed.getMinerals();
		int gasBuffer = requirements.getGas() - delayed.getGas();
		
		int framesToMove = (int) Math.ceil(minBuffer / delayed.getMineralRate());
		int gasMove = (int) Math.ceil(gasBuffer / delayed.getGasRate());
		framesToMove = (framesToMove > gasMove) ? framesToMove : gasMove;
		
		delayed.delay(framesToMove);
		
		return delayed;
	}
	
	public void setInitialState(State initialState){
		this.initialState = initialState;
	}
	
}
