package javabot.strategy.action;

import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.UnitUtils;

public class BuildObservatory extends Action{

	public BuildObservatory(JNIBWAPI game){
		producedType = game.getUnitType(UnitTypes.Protoss_Observatory.ordinal());
		duration = 	producedType.getBuildTime();
		HashSet<UnitType> requiredTypes = new HashSet<UnitType>();
		for (Integer ii : UnitUtils.requiredUnits(UnitTypes.Protoss_Observatory)){
			requiredTypes.add(game.getUnitType(ii));
		}
		requirements = new ActionRequirements(50, 100, 0, game.getUnitType(UnitTypes.Protoss_Probe.ordinal()), requiredTypes);
	}
	
}
