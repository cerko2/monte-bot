package javabot.strategy.planner.action;

import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.UnitUtils;

public class TrainCarrier extends Action{
	
	public TrainCarrier(JNIBWAPI game){
		producedType = game.getUnitType(UnitTypes.Protoss_Carrier.ordinal());
		duration = producedType.getBuildTime();
		HashSet<UnitType> requiredTypes = new HashSet<UnitType>();
		for (Integer ii : UnitUtils.requiredUnits(UnitTypes.Protoss_Carrier)){
			requiredTypes.add(game.getUnitType(ii));
		}
		requirements = new ActionRequirements(350, 200, 6, game.getUnitType(UnitTypes.Protoss_Stargate.ordinal()), requiredTypes);
	}
}