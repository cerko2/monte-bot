package javabot.strategy.planner.action;

import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.UnitUtils;

public class BuildCyberneticsCore extends Action{

	public BuildCyberneticsCore(JNIBWAPI game){
		producedType = game.getUnitType(UnitTypes.Protoss_Cybernetics_Core.ordinal());
		duration = 	producedType.getBuildTime();
		HashSet<UnitType> requiredTypes = new HashSet<UnitType>();
		for (Integer ii : UnitUtils.requiredUnits(UnitTypes.Protoss_Cybernetics_Core)){
			requiredTypes.add(game.getUnitType(ii));
		}
		requirements = new ActionRequirements(200, 0, 0, game.getUnitType(UnitTypes.Protoss_Probe.ordinal()), requiredTypes);
	}
	
}