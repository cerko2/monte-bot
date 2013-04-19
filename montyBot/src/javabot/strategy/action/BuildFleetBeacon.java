package javabot.strategy.action;

import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.UnitUtils;

public class BuildFleetBeacon extends Action{

	public BuildFleetBeacon(JNIBWAPI game){
		producedType = game.getUnitType(UnitTypes.Protoss_Fleet_Beacon.ordinal());
		duration = 	producedType.getBuildTime();
		HashSet<UnitType> requiredTypes = new HashSet<UnitType>();
		for (Integer ii : UnitUtils.requiredUnits(UnitTypes.Protoss_Fleet_Beacon)){
			requiredTypes.add(game.getUnitType(ii));
		}
		requirements = new ActionRequirements(300, 200, 0, game.getUnitType(UnitTypes.Protoss_Probe.ordinal()), requiredTypes);
	}
	
}