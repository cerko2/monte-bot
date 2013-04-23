package javabot.strategy.action;

import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.UnitUtils;

public class BuildRoboticsFacility extends Action{

	public BuildRoboticsFacility(JNIBWAPI game){
		producedType = game.getUnitType(UnitTypes.Protoss_Robotics_Facility.ordinal());
		duration = 	producedType.getBuildTime();
		HashSet<UnitType> requiredTypes = new HashSet<UnitType>();
		for (Integer ii : UnitUtils.requiredUnits(UnitTypes.Protoss_Robotics_Facility)){
			requiredTypes.add(game.getUnitType(ii));
		}
		requirements = new ActionRequirements(200, 200, 0, game.getUnitType(UnitTypes.Protoss_Probe.ordinal()), requiredTypes);
	}
	
}
