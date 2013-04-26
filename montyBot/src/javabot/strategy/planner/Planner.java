package javabot.strategy.planner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map.Entry;

import javabot.JNIBWAPI;
import javabot.model.Player;
import javabot.model.Unit;
import javabot.strategy.planner.action.Action;
import javabot.strategy.planner.action.ActionRequirements;
import javabot.types.UnitType;

public class Planner {
	
	private JNIBWAPI game;
	private Player player;
	
	//typeID - amount ready
	private HashMap<Integer, Integer> renewableResouces;
	private int minerals;
	private int gas;
	private int supplyUsed;
	
	//typeID
	private HashSet<Integer> availableTech;
	private HashSet<Integer> upcomingTech;
	
	private ArrayList<Action> actionsToExecute;
	
	public Planner(JNIBWAPI game){
		
		this.game = game;
		this.player = game.getSelf();
		this.renewableResouces = new HashMap<Integer, Integer>();
		this.availableTech = new HashSet<Integer>();
		this.actionsToExecute = new ArrayList<Action>();
		this.upcomingTech = new HashSet<Integer>();
		
	}
	
	public void generatePlan(HashMap<Integer, Integer> goals){
		
		//initial state
		setResources();
		setActionsToExecute(goals);
		ArrayList<Action> sequentialPlan = generateSequentialPlan(goals);
		
		for (Action action : sequentialPlan){
			System.out.println(action.getClass().getName());
		}
		
	}
	
	private void setResources(){
		renewableResouces.clear();
		availableTech.clear();
		
		minerals = player.getMinerals();
		gas = player.getGas();
		supplyUsed = player.getSupplyUsed() / 2;
		
		UnitType type = null;
		for (Unit unit : game.getMyUnits()){
			
			type = game.getUnitType(unit.getTypeID());
			addResource(type);
			
		}
	}
	
	private void setActionsToExecute(HashMap<Integer, Integer> goals){
		
		actionsToExecute.clear();
		
		for (Entry<Integer, Integer> goal : goals.entrySet()){
			
			for (int i = 0; i < goal.getValue(); i++){
				actionsToExecute.add(ActionFactory.createAction(goal.getKey(), game));
			}
			addActionRequirements(actionsToExecute.get(actionsToExecute.size() - 1));
			
		}
	}
	
	private void addActionRequirements(Action action){
		
		ActionRequirements requirements = action.getRequirements();
		
		for (UnitType type : requirements.getPrerequisites()){
			
			if (!availableTech.contains(type.getID()) && !upcomingTech.contains(type.getID())){
				actionsToExecute.add(ActionFactory.createAction(type.getID(), game));
				upcomingTech.add(type.getID());
			}
			
		}
	}
	
	private ArrayList<Action> generateSequentialPlan(HashMap<Integer, Integer> goals){
		ArrayList<Action> plan = new ArrayList<Action>();
		LinkedHashSet<Action> executableActions = new LinkedHashSet<Action>();
		
		while (!actionsToExecute.isEmpty() || !executableActions.isEmpty()){
			
			setExecutableActions(executableActions);
			actionsToExecute.removeAll(executableActions);
			
			for (Action action : executableActions){
				executeAction(action);
				plan.add(action);
				executableActions.remove(action);
				break;
			}
			
		}
		
		return plan;
	}
	
	private void setExecutableActions(LinkedHashSet<Action> executableActions){
		
		for (Action action : actionsToExecute){
			if (canExecute(action)){
				executableActions.add(action);
			}
		}
		
	}
	
	private void executeAction(Action action){
		
		ActionRequirements requirements = action.getRequirements();
		minerals -= requirements.getMinerals();
		gas -= requirements.getGas();
		supplyUsed += requirements.getSupply();
		addResource(action.getProducedType());
		
	}
	
	private boolean canExecute(Action action){
		
		ActionRequirements requirements = action.getRequirements();
		
		for (UnitType type : requirements.getPrerequisites()){
			if (!availableTech.contains(type.getID())){
				return false;
			}
		}
		
		return true;
	}
	
	private void addResource(UnitType type){
		if (type.isBuilding()){
			if (renewableResouces.containsKey(type.getID())){
				renewableResouces.put(type.getID(), renewableResouces.get(type.getID()) + 1);
			}
			else {
				renewableResouces.put(type.getID(), 1);
			}
			availableTech.add(type.getID());
		}
	}
	
}
