package javabot.strategy.planner.action;

import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.UnitUtils;

public class BuildCitadelOfAdun extends Action{

	public BuildCitadelOfAdun(JNIBWAPI game){
		producedType = game.getUnitType(UnitTypes.Protoss_Citadel_of_Adun.ordinal());
		duration = 	producedType.getBuildTime();
		HashSet<UnitType> requiredTypes = new HashSet<UnitType>();
		for (Integer ii : UnitUtils.requiredUnits(UnitTypes.Protoss_Citadel_of_Adun)){
			requiredTypes.add(game.getUnitType(ii));
		}
		requirements = new ActionRequirements(150, 100, 0, game.getUnitType(UnitTypes.Protoss_Probe.ordinal()), requiredTypes);
	}
	
}