package javabot.util;

import java.util.ArrayList;
import java.util.Collections;

import javabot.JNIBWAPI;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;

public class UnitUtils {
	
	public static int getNearestUnit(JNIBWAPI game, int unitTypeID, int x, int y) {
		int nearestID = -1;
		double nearestDist = 9999999;
		
		for (Unit unit : game.getMyUnits()) {
			if ((unit.getTypeID() != unitTypeID) || (!unit.isCompleted()) || unit.isGatheringGas() || !unit.isExists()) continue;
			if (nearestID == -1) {
				nearestID = unit.getID();
				continue;
			}
			double dist = Math.sqrt(Math.pow(unit.getX()-game.getUnit(nearestID).getX(),2) + Math.pow(unit.getY()-game.getUnit(nearestID).getY(),2));
			if (dist < nearestDist) {
				nearestID = unit.getID();
				nearestDist = dist;
			}
		}
		
		return nearestID;		
	}
	
	public static int getDistance(Unit unit1, Unit unit2){
		return getDistance(unit1.getX(), unit1.getY(), unit2.getX(), unit2.getY());
	}
	
	public static int getDistance(int x1, int y1, int x2, int y2){
		return (int) Math.sqrt(Math.pow(x1 - x2,2) + Math.pow(y1 - y2,2));
	}
	
	//uplna ojebabravacka este z 2. sc cvika kedze tu neni requiredUnits()
	public static ArrayList<Integer> requiredUnits(UnitTypes _unit){
		UnitTypes unit = _unit;
		boolean finished = false;
		ArrayList<Integer> reqUnits = new ArrayList<Integer>();
		//reqUnits.add(_unit.ordinal());
		while (!finished){
			switch (unit){
			case Protoss_Cybernetics_Core: 
				unit = UnitTypes.Protoss_Gateway;
				reqUnits.add(UnitTypes.Protoss_Gateway.ordinal());
				break;
			case Protoss_Citadel_of_Adun:
				unit = UnitTypes.Protoss_Cybernetics_Core;
				reqUnits.add(UnitTypes.Protoss_Cybernetics_Core.ordinal());
				break;
			case Protoss_Photon_Cannon:
				unit = UnitTypes.Protoss_Forge;
				reqUnits.add(UnitTypes.Protoss_Forge.ordinal());
				break;
			case Protoss_Shield_Battery:
				unit = UnitTypes.Protoss_Gateway;
				reqUnits.add(UnitTypes.Protoss_Gateway.ordinal());
				break;
			case Protoss_Stargate:
				unit = UnitTypes.Protoss_Cybernetics_Core;
				reqUnits.add(UnitTypes.Protoss_Cybernetics_Core.ordinal());
				break;
			case Protoss_Robotics_Facility:
				unit = UnitTypes.Protoss_Cybernetics_Core;
				reqUnits.add(UnitTypes.Protoss_Cybernetics_Core.ordinal());
				break;
			case Protoss_Robotics_Support_Bay:
				unit = UnitTypes.Protoss_Robotics_Facility;
				reqUnits.add(UnitTypes.Protoss_Robotics_Facility.ordinal());
				break;
			case Protoss_Observatory:
				unit = UnitTypes.Protoss_Robotics_Facility;
				reqUnits.add(UnitTypes.Protoss_Robotics_Facility.ordinal());
				break;
			case Protoss_Fleet_Beacon:
				unit = UnitTypes.Protoss_Stargate;
				reqUnits.add(UnitTypes.Protoss_Stargate.ordinal());
				break;
			case Protoss_Templar_Archives:
				unit = UnitTypes.Protoss_Citadel_of_Adun;
				reqUnits.add(UnitTypes.Protoss_Citadel_of_Adun.ordinal());
				break;
			case Protoss_Arbiter_Tribunal:
				unit = UnitTypes.Protoss_Templar_Archives;
				reqUnits.add(UnitTypes.Protoss_Templar_Archives.ordinal());
				reqUnits.add(UnitTypes.Protoss_Fleet_Beacon.ordinal());
				reqUnits.add(UnitTypes.Protoss_Stargate.ordinal());
				break;
			case Protoss_Zealot:
				unit = UnitTypes.Protoss_Gateway;
				reqUnits.add(UnitTypes.Protoss_Gateway.ordinal());
				break;
			case Protoss_Dragoon:
				unit = UnitTypes.Protoss_Cybernetics_Core;
				reqUnits.add(UnitTypes.Protoss_Cybernetics_Core.ordinal());
				break;
			case Protoss_High_Templar:
				unit = UnitTypes.Protoss_Templar_Archives;
				reqUnits.add(UnitTypes.Protoss_Templar_Archives.ordinal());
				break;
			case Protoss_Dark_Templar:
				unit = UnitTypes.Protoss_Templar_Archives;
				reqUnits.add(UnitTypes.Protoss_Templar_Archives.ordinal());
				break;
			case Protoss_Archon:
				unit = UnitTypes.Protoss_Templar_Archives;
				reqUnits.add(UnitTypes.Protoss_Templar_Archives.ordinal());
				break;
			case Protoss_Dark_Archon:
				unit = UnitTypes.Protoss_Templar_Archives;
				reqUnits.add(UnitTypes.Protoss_Templar_Archives.ordinal());
				break;
			case Protoss_Shuttle:
				unit = UnitTypes.Protoss_Robotics_Facility;
				reqUnits.add(UnitTypes.Protoss_Robotics_Facility.ordinal());
				break;
			case Protoss_Reaver:
				unit = UnitTypes.Protoss_Robotics_Support_Bay;
				reqUnits.add(UnitTypes.Protoss_Robotics_Support_Bay.ordinal());
				break;
			case Protoss_Observer:
				unit = UnitTypes.Protoss_Observatory;
				reqUnits.add(UnitTypes.Protoss_Observatory.ordinal());
				break;
			case Protoss_Scout:
				unit = UnitTypes.Protoss_Stargate;
				reqUnits.add(UnitTypes.Protoss_Stargate.ordinal());
				break;
			case Protoss_Corsair:
				unit = UnitTypes.Protoss_Stargate;
				reqUnits.add(UnitTypes.Protoss_Stargate.ordinal());
				break;
			case Protoss_Carrier:
				unit = UnitTypes.Protoss_Fleet_Beacon;
				reqUnits.add(UnitTypes.Protoss_Fleet_Beacon.ordinal());
				break;
			case Protoss_Arbiter:
				unit = UnitTypes.Protoss_Arbiter_Tribunal;
				reqUnits.add(UnitTypes.Protoss_Arbiter_Tribunal.ordinal());
				break;
			case Protoss_Probe:
				unit = UnitTypes.Protoss_Nexus;
				reqUnits.add(UnitTypes.Protoss_Nexus.ordinal());
				break;
			default : 
				finished = true;
			}
			//unit = game.getUnitType(reqUnits.get(reqUnits.size() - 1));
		}
		//reqUnits.add(UnitTypes.Protoss_Assimilator.ordinal());
		Collections.reverse(reqUnits);
		return reqUnits;
	}
}
