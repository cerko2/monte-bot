package javabot.strategy.planner.action;

import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.UnitUtils;

public class TrainDragoon extends Action{
	
	public TrainDragoon(JNIBWAPI game){
		producedType = game.getUnitType(UnitTypes.Protoss_Dragoon.ordinal());
		duration = producedType.getBuildTime();
		HashSet<UnitType> requiredTypes = new HashSet<UnitType>();
		for (Integer ii : UnitUtils.requiredUnits(UnitTypes.Protoss_Dragoon)){
			requiredTypes.add(game.getUnitType(ii));
		}
		requirements = new ActionRequirements(125, 50, 2, game.getUnitType(UnitTypes.Protoss_Gateway.ordinal()), requiredTypes);
	}
}