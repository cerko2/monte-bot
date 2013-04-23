package javabot.strategy.action;

import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;

public class BuildNexus extends Action{
	
	public BuildNexus(JNIBWAPI game){
		supplyProvided = 9;
		producedType = game.getUnitType(UnitTypes.Protoss_Nexus.ordinal());
		duration = producedType.getBuildTime();
		HashSet<UnitType> requiredTypes = new HashSet<UnitType>();
		
		requirements = new ActionRequirements(400, 0, 0, game.getUnitType(UnitTypes.Protoss_Probe.ordinal()), requiredTypes);
	}
}