package javabot.strategy.planner.action;

import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;

public class BuildPylon extends Action{
	
	public BuildPylon(JNIBWAPI game){
		supplyProvided = 8;
		producedType = game.getUnitType(UnitTypes.Protoss_Pylon.ordinal());
		duration = producedType.getBuildTime();
		HashSet<UnitType> requiredTypes = new HashSet<UnitType>();
	
		requirements = new ActionRequirements(100, 0, 0, game.getUnitType(UnitTypes.Protoss_Probe.ordinal()), requiredTypes);
	}
}