package javabot.strategy.planner.action;

import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.UnitUtils;

public class TrainScout extends Action{
	
	public TrainScout(JNIBWAPI game){
		producedType = game.getUnitType(UnitTypes.Protoss_Scout.ordinal());
		duration = producedType.getBuildTime();
		HashSet<UnitType> requiredTypes = new HashSet<UnitType>();
		for (Integer ii : UnitUtils.requiredUnits(UnitTypes.Protoss_Scout)){
			requiredTypes.add(game.getUnitType(ii));
		}
		requirements = new ActionRequirements(275, 125, 3, game.getUnitType(UnitTypes.Protoss_Stargate.ordinal()), requiredTypes);
	}
}