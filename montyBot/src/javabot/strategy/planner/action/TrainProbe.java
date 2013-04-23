package javabot.strategy.action;

import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.UnitUtils;

public class TrainProbe extends Action{
	
	public TrainProbe(JNIBWAPI game){
		producedType = game.getUnitType(UnitTypes.Protoss_Probe.ordinal());
		duration = producedType.getBuildTime();
		HashSet<UnitType> requiredTypes = new HashSet<UnitType>();
		for (Integer ii : UnitUtils.requiredUnits(UnitTypes.Protoss_Probe)){
			requiredTypes.add(game.getUnitType(ii));
		}
		requirements = new ActionRequirements(50, 0, 1, game.getUnitType(UnitTypes.Protoss_Nexus.ordinal()), requiredTypes);
	}
}
