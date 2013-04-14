package javabot.combat;

import javabot.model.Region;

public class Action {

	private Region currentRegion;
	private String actionName;
	
	private boolean enemy_action;
	private boolean baseAction;
	private int squadID;
	
	private int started_at;
	private int ended_at;
	
	public Action( Region currentRegion, String actionName )
	{
		this.setCurrentRegion( currentRegion );
		this.setActionName( actionName );
	}

	public Region getCurrentRegion() 
	{
		return currentRegion;
	}

	public void setCurrentRegion( Region currentRegion ) 
	{
		this.currentRegion = currentRegion;
	}

	public String getActionName() 
	{
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

}
