package javabot.strategy.planner.action;

import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.UnitUtils;

public class TrainHighTemplar extends Action{
	
	public TrainHighTemplar(JNIBWAPI game){
		producedType = game.getUnitType(UnitTypes.Protoss_High_Templar.ordinal());
		duration = producedType.getBuildTime();
		HashSet<UnitType> requiredTypes = new HashSet<UnitType>();
		for (Integer ii : UnitUtils.requiredUnits(UnitTypes.Protoss_High_Templar)){
			requiredTypes.add(game.getUnitType(ii));
		}
		requirements = new ActionRequirements(50, 150, 2, game.getUnitType(UnitTypes.Protoss_Gateway.ordinal()), requiredTypes);
	}
}
