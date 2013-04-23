package javabot.strategy.action;

import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.UnitUtils;

public class BuildArbiterTribunal extends Action{

	public BuildArbiterTribunal(JNIBWAPI game){
		producedType = game.getUnitType(UnitTypes.Protoss_Arbiter_Tribunal.ordinal());
		duration = 	producedType.getBuildTime();
		HashSet<UnitType> requiredTypes = new HashSet<UnitType>();
		for (Integer ii : UnitUtils.requiredUnits(UnitTypes.Protoss_Arbiter_Tribunal)){
			requiredTypes.add(game.getUnitType(ii));
		}
		requirements = new ActionRequirements(200, 150, 0, game.getUnitType(UnitTypes.Protoss_Probe.ordinal()), requiredTypes);
	}
	
}