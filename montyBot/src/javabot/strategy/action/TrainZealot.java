package javabot.strategy.action;

import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.UnitUtils;

public class TrainZealot extends Action{
	
	public TrainZealot(JNIBWAPI game){
		producedType = game.getUnitType(UnitTypes.Protoss_Zealot.ordinal());
		duration = producedType.getBuildTime();
		HashSet<UnitType> requiredTypes = new HashSet<UnitType>();
		for (Integer ii : UnitUtils.requiredUnits(UnitTypes.Protoss_Zealot)){
			requiredTypes.add(game.getUnitType(ii));
		}
		requirements = new ActionRequirements(100, 0, 2, game.getUnitType(UnitTypes.Protoss_Gateway.ordinal()), requiredTypes);
	}
}
