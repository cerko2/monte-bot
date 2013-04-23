package javabot.strategy.planner.action;

import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.UnitUtils;

public class TrainArbiter extends Action{
	
	public TrainArbiter(JNIBWAPI game){
		producedType = game.getUnitType(UnitTypes.Protoss_Arbiter.ordinal());
		duration = producedType.getBuildTime();
		HashSet<UnitType> requiredTypes = new HashSet<UnitType>();
		for (Integer ii : UnitUtils.requiredUnits(UnitTypes.Protoss_Arbiter)){
			requiredTypes.add(game.getUnitType(ii));
		}
		requirements = new ActionRequirements(100, 350, 4, game.getUnitType(UnitTypes.Protoss_Stargate.ordinal()), requiredTypes);
	}
}