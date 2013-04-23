package javabot.strategy.planner.action;

import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.UnitUtils;

public class TrainObserver extends Action{
	
	public TrainObserver(JNIBWAPI game){
		producedType = game.getUnitType(UnitTypes.Protoss_Observer.ordinal());
		duration = producedType.getBuildTime();
		HashSet<UnitType> requiredTypes = new HashSet<UnitType>();
		for (Integer ii : UnitUtils.requiredUnits(UnitTypes.Protoss_Observer)){
			requiredTypes.add(game.getUnitType(ii));
		}
		requirements = new ActionRequirements(25, 75, 1, game.getUnitType(UnitTypes.Protoss_Robotics_Facility.ordinal()), requiredTypes);
	}
}