package javabot.strategy.planner.action;

import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.UnitUtils;

public class TrainCorsair extends Action{
	
	public TrainCorsair(JNIBWAPI game){
		producedType = game.getUnitType(UnitTypes.Protoss_Corsair.ordinal());
		duration = producedType.getBuildTime();
		HashSet<UnitType> requiredTypes = new HashSet<UnitType>();
		for (Integer ii : UnitUtils.requiredUnits(UnitTypes.Protoss_Corsair)){
			requiredTypes.add(game.getUnitType(ii));
		}
		requirements = new ActionRequirements(150, 100, 2, game.getUnitType(UnitTypes.Protoss_Stargate.ordinal()), requiredTypes);
	}
}