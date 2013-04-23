package javabot.strategy.planner.action;

import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.UnitUtils;

public class TrainReaver extends Action{
	
	public TrainReaver(JNIBWAPI game){
		producedType = game.getUnitType(UnitTypes.Protoss_Reaver.ordinal());
		duration = producedType.getBuildTime();
		HashSet<UnitType> requiredTypes = new HashSet<UnitType>();
		for (Integer ii : UnitUtils.requiredUnits(UnitTypes.Protoss_Reaver)){
			requiredTypes.add(game.getUnitType(ii));
		}
		requirements = new ActionRequirements(200, 100, 4, game.getUnitType(UnitTypes.Protoss_Robotics_Facility.ordinal()), requiredTypes);
	}
}