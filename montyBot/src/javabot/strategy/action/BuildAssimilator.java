package javabot.strategy.action;

import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.UnitUtils;

public class BuildAssimilator extends Action{
	
	public BuildAssimilator(JNIBWAPI game){
		producedType = game.getUnitType(UnitTypes.Protoss_Assimilator.ordinal());
		duration = producedType.getBuildTime();
		HashSet<UnitType> requiredTypes = new HashSet<UnitType>();
		for (Integer ii : UnitUtils.requiredUnits(UnitTypes.Protoss_Gateway)){
			requiredTypes.add(game.getUnitType(ii));
		}
		requiredTypes.add(game.getUnitType(UnitTypes.Resource_Vespene_Geyser.ordinal()));
		requirements = new ActionRequirements(100, 0, 0, game.getUnitType(UnitTypes.Protoss_Probe.ordinal()), requiredTypes);
	}
}