package javabot.strategy.planner.action;

import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.UnitUtils;

public class BuildRoboticsSupportBay extends Action{

	public BuildRoboticsSupportBay(JNIBWAPI game){
		producedType = game.getUnitType(UnitTypes.Protoss_Robotics_Support_Bay.ordinal());
		duration = 	producedType.getBuildTime();
		HashSet<UnitType> requiredTypes = new HashSet<UnitType>();
		for (Integer ii : UnitUtils.requiredUnits(UnitTypes.Protoss_Robotics_Support_Bay)){
			requiredTypes.add(game.getUnitType(ii));
		}
		requirements = new ActionRequirements(150, 100, 0, game.getUnitType(UnitTypes.Protoss_Probe.ordinal()), requiredTypes);
	}
	
}
