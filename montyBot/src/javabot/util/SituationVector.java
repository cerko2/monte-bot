package javabot.util;

import java.util.ArrayList;

import javabot.JNIBWAPI;
import javabot.model.Player;
import javabot.model.Race;
import javabot.model.Unit;
import javabot.strategy.OpponentModeling;
import javabot.strategy.OpponentPositioning;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;

public class SituationVector {
	
	private JNIBWAPI bwapi;
	private ArrayList<Double> vect = new ArrayList<Double>();
	private OpponentModeling opponentModelling; 
	
	public SituationVector(JNIBWAPI game, OpponentPositioning op, OpponentModeling om) {
		this.bwapi = game;
		this.opponentModelling = om;
		
		// enemy race
		Player enemy = bwapi.getEnemies().get(0);
		
		// enemy bases
		int bases = 0;
		// enemy production facility counts
		int barracks = 0;
		int factories = 0;
		int starports = 0;
		int gateways = 0;
		int stargates = 0;
		int roboticsFacilities = 0;
		int hatcheries = 0;
		
		for (Unit u : op.getEnemyUnits()) {
			int i = u.getTypeID();
			if (i == UnitTypes.Terran_Barracks.ordinal()) barracks++;
			if (i == UnitTypes.Terran_Factory.ordinal()) factories++;
			if (i == UnitTypes.Terran_Starport.ordinal()) starports++;
			if (i == UnitTypes.Protoss_Gateway.ordinal()) gateways++;
			if (i == UnitTypes.Protoss_Stargate.ordinal()) stargates++;
			if (i == UnitTypes.Protoss_Robotics_Facility.ordinal()) roboticsFacilities++;
			if (i == UnitTypes.Zerg_Hatchery.ordinal()) hatcheries++;
			if (i == UnitTypes.Zerg_Lair.ordinal()) hatcheries++;
			if (i == UnitTypes.Zerg_Hive.ordinal()) hatcheries++;
			if ((i == UnitTypes.Terran_Command_Center.ordinal()) || (i == UnitTypes.Protoss_Nexus.ordinal()) || (i == UnitTypes.Zerg_Hatchery.ordinal()) || (i == UnitTypes.Zerg_Lair.ordinal()) || (i == UnitTypes.Zerg_Hive.ordinal())) {
				bases++;
			}
			
		}
		if (enemy.getRaceID() == Race.TERRAN.getID()) {
			vect.add((double)barracks*(double)bases*0.1);
			vect.add((float)factories*(float)bases*0.1);
			vect.add((float)starports*(float)bases*0.1);
		}
		
		if (enemy.getRaceID() == Race.PROTOSS.getID()) {
			vect.add((float)gateways*(float)bases*0.1);
			vect.add((float)roboticsFacilities*(float)bases*0.1);
			vect.add((float)stargates*(float)bases*0.1);
		}
		if (enemy.getRaceID() == Race.ZERG.getID()) {
			vect.add((float)hatcheries*(float)bases*0.1);
		}
		
		// tech buildings:
		// Terran
		if (enemy.getRaceID() == Race.TERRAN.getID()) {
			vect.add(hasBuilding(op,UnitTypes.Terran_Engineering_Bay.ordinal()));
			vect.add(hasBuilding(op,UnitTypes.Terran_Academy.ordinal()));
			vect.add(hasBuilding(op,UnitTypes.Terran_Machine_Shop.ordinal()));
			vect.add(hasBuilding(op,UnitTypes.Terran_Armory.ordinal()));
			vect.add(hasBuilding(op,UnitTypes.Terran_Control_Tower.ordinal()));
			vect.add(hasBuilding(op,UnitTypes.Terran_Physics_Lab.ordinal()));
			vect.add(hasBuilding(op,UnitTypes.Terran_Covert_Ops.ordinal()));
			vect.add(hasBuilding(op,UnitTypes.Terran_Nuclear_Silo.ordinal()));
		} 
		// Protoss
		if (enemy.getRaceID() == Race.PROTOSS.getID()) {
			vect.add(hasBuilding(op,UnitTypes.Protoss_Forge.ordinal()));
			vect.add(hasBuilding(op,UnitTypes.Protoss_Cybernetics_Core.ordinal()));
			vect.add(hasBuilding(op,UnitTypes.Protoss_Robotics_Support_Bay.ordinal()));
			vect.add(hasBuilding(op,UnitTypes.Protoss_Observatory.ordinal()));
			vect.add(hasBuilding(op,UnitTypes.Protoss_Citadel_of_Adun.ordinal()));
			vect.add(hasBuilding(op,UnitTypes.Protoss_Templar_Archives.ordinal()));
			vect.add(hasBuilding(op,UnitTypes.Protoss_Arbiter_Tribunal.ordinal()));
			vect.add(hasBuilding(op,UnitTypes.Protoss_Fleet_Beacon.ordinal()));
		}
		// Zerg
		if (enemy.getRaceID() == Race.ZERG.getID()) {
			vect.add(hasBuilding(op,UnitTypes.Zerg_Evolution_Chamber.ordinal()));
			vect.add(hasBuilding(op,UnitTypes.Zerg_Hydralisk_Den.ordinal()));
			vect.add(hasBuilding(op,UnitTypes.Zerg_Queens_Nest.ordinal()));
			vect.add(hasBuilding(op,UnitTypes.Zerg_Spire.ordinal()));
			vect.add(hasBuilding(op,UnitTypes.Zerg_Greater_Spire.ordinal()));
			vect.add(hasBuilding(op,UnitTypes.Zerg_Defiler_Mound.ordinal()));
			vect.add(hasBuilding(op,UnitTypes.Zerg_Ultralisk_Cavern.ordinal()));
		}
		
		// game situation
		vect.add(iHaveAir());
		vect.add(iHaveInvis());
		vect.add(enemyIsExpandingNow(op));
		
	}
	
	public ArrayList<Double> getSituationOrder1() {
		return this.vect;
	}

	public ArrayList<Double> getSituationHigherOrder(int order) {
		ArrayList<Double> ret = new ArrayList<Double>(this.vect);
		for (int i = 2; i<=order; i++) {
			for (Double d : this.vect) {
				ret.add( Math.pow(d, (double) i) );
			}
		}
		return ret;
	}

	private Double hasBuilding(OpponentPositioning op, int typeID) {
		for (Unit u : op.getEnemyUnits()) {
			UnitType typ = bwapi.getUnitType(u.getTypeID());
			if (typ.getID() == typeID) {
				if (
						(!u.isBeingConstructed() && u.isCompleted()) ||
						(bwapi.getFrameCount() - op.getLastSeen(u.getID()) >= typ.getBuildTime() ) )
					return 1.0;
			}
		}

		// abduce the presence of enemy building from seen units (if that building wasn't previously destroyed)
		if (!opponentModelling.destroyedEnemyBuildingTypes.contains(typeID)) {
			for (Unit u : op.getEnemyUnits()) {
				UnitType typ = bwapi.getUnitType(u.getTypeID());
				if (isPrecondition(typeID,typ.getID())) {
					return 1.0;
				}
			}
		}
		return 0.0;
	}

	private boolean isPrecondition(int buildingTypeID, int unitTypeID) {
		// on the right side, there should be any units/buildings/spells, while 
		// on the left, there should only be tech buildings (that are used in SituationVector() constructor)
		if ((buildingTypeID == UnitTypes.Zerg_Spawning_Pool.ordinal()) && 	(unitTypeID == UnitTypes.Zerg_Zergling.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Zerg_Hydralisk_Den.ordinal()) &&	(unitTypeID == UnitTypes.Zerg_Hydralisk.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Zerg_Hydralisk_Den.ordinal()) &&	(unitTypeID == UnitTypes.Zerg_Lurker.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Zerg_Hydralisk_Den.ordinal()) && 	(unitTypeID == UnitTypes.Zerg_Lurker_Egg.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Zerg_Spire.ordinal()) && 			(unitTypeID == UnitTypes.Zerg_Mutalisk.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Zerg_Spire.ordinal()) && 			(unitTypeID == UnitTypes.Zerg_Scourge.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Zerg_Greater_Spire.ordinal()) && 	(unitTypeID == UnitTypes.Zerg_Guardian.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Zerg_Greater_Spire.ordinal()) && 	(unitTypeID == UnitTypes.Zerg_Devourer.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Zerg_Queens_Nest.ordinal()) && 	(unitTypeID == UnitTypes.Zerg_Queen.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Zerg_Defiler_Mound.ordinal()) && 	(unitTypeID == UnitTypes.Zerg_Defiler.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Zerg_Ultralisk_Cavern.ordinal()) && 	(unitTypeID == UnitTypes.Zerg_Ultralisk.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Zerg_Evolution_Chamber.ordinal()) && 	(unitTypeID == UnitTypes.Zerg_Spore_Colony.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Zerg_Defiler_Mound.ordinal()) && 	(unitTypeID == UnitTypes.Spell_Dark_Swarm.ordinal())) return true;
		
		if ((buildingTypeID == UnitTypes.Terran_Academy.ordinal()) && 		(unitTypeID == UnitTypes.Terran_Firebat.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Terran_Academy.ordinal()) && 		(unitTypeID == UnitTypes.Terran_Medic.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Terran_Machine_Shop.ordinal()) && 	(unitTypeID == UnitTypes.Terran_Siege_Tank_Siege_Mode.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Terran_Machine_Shop.ordinal()) && 	(unitTypeID == UnitTypes.Terran_Siege_Tank_Tank_Mode.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Terran_Armory.ordinal()) && 		(unitTypeID == UnitTypes.Terran_Goliath.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Terran_Control_Tower.ordinal()) && (unitTypeID == UnitTypes.Terran_Dropship.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Terran_Armory.ordinal()) && 		(unitTypeID == UnitTypes.Terran_Valkyrie.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Terran_Physics_Lab.ordinal()) && 	(unitTypeID == UnitTypes.Terran_Battlecruiser.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Terran_Covert_Ops.ordinal()) && 	(unitTypeID == UnitTypes.Terran_Ghost.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Terran_Academy.ordinal()) && 		(unitTypeID == UnitTypes.Terran_Ghost.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Terran_Engineering_Bay.ordinal()) && (unitTypeID == UnitTypes.Terran_Missile_Turret.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Terran_Academy.ordinal()) && 		(unitTypeID == UnitTypes.Terran_Comsat_Station.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Terran_Covert_Ops.ordinal()) && 	(unitTypeID == UnitTypes.Terran_Nuclear_Silo.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Terran_Nuclear_Silo.ordinal()) && 	(unitTypeID == UnitTypes.Terran_Nuclear_Missile.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Terran_Covert_Ops.ordinal()) && 	(unitTypeID == UnitTypes.Terran_Nuclear_Missile.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Terran_Machine_Shop.ordinal()) && 	(unitTypeID == UnitTypes.Terran_Vulture_Spider_Mine.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Terran_Academy.ordinal()) && 		(unitTypeID == UnitTypes.Spell_Scanner_Sweep.ordinal())) return true;
		
		if ((buildingTypeID == UnitTypes.Protoss_Cybernetics_Core.ordinal()) && (unitTypeID == UnitTypes.Protoss_Dragoon.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Protoss_Cybernetics_Core.ordinal()) && (unitTypeID == UnitTypes.Protoss_Corsair.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Protoss_Observatory.ordinal()) && 		(unitTypeID == UnitTypes.Protoss_Observer.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Protoss_Templar_Archives.ordinal()) && (unitTypeID == UnitTypes.Protoss_High_Templar.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Protoss_Templar_Archives.ordinal()) && (unitTypeID == UnitTypes.Protoss_Dark_Templar.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Protoss_Templar_Archives.ordinal()) && (unitTypeID == UnitTypes.Protoss_Archon.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Protoss_Templar_Archives.ordinal()) && (unitTypeID == UnitTypes.Protoss_Dark_Archon.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Protoss_Arbiter_Tribunal.ordinal()) && (unitTypeID == UnitTypes.Protoss_Arbiter.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Protoss_Robotics_Support_Bay.ordinal()) && (unitTypeID == UnitTypes.Protoss_Reaver.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Protoss_Robotics_Support_Bay.ordinal()) && (unitTypeID == UnitTypes.Protoss_Scarab.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Protoss_Fleet_Beacon.ordinal()) && (unitTypeID == UnitTypes.Protoss_Carrier.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Protoss_Fleet_Beacon.ordinal()) && (unitTypeID == UnitTypes.Protoss_Interceptor.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Protoss_Cybernetics_Core.ordinal()) && (unitTypeID == UnitTypes.Protoss_Stargate.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Protoss_Cybernetics_Core.ordinal()) && (unitTypeID == UnitTypes.Protoss_Citadel_of_Adun.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Protoss_Cybernetics_Core.ordinal()) && (unitTypeID == UnitTypes.Protoss_Robotics_Facility.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Protoss_Templar_Archives.ordinal()) && (unitTypeID == UnitTypes.Protoss_Arbiter_Tribunal.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Protoss_Citadel_of_Adun.ordinal()) && 	(unitTypeID == UnitTypes.Protoss_Templar_Archives.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Protoss_Forge.ordinal()) && 			(unitTypeID == UnitTypes.Protoss_Photon_Cannon.ordinal())) return true;
		if ((buildingTypeID == UnitTypes.Protoss_Fleet_Beacon.ordinal()) && (unitTypeID == UnitTypes.Spell_Disruption_Web.ordinal())) return true;
			
		return false;
	}
	
	private Double enemyIsExpandingNow(OpponentPositioning op) {
		for (Unit u : op.getEnemyUnits()) {
			if (	(u.isBeingConstructed() || u.isMorphing() )
				&&	(bwapi.getFrameCount() - op.getLastSeen(u.getID()) < bwapi.getUnitType(u.getTypeID()).getBuildTime() )
				&& 	(u.getTypeID() == UnitTypes.Terran_Command_Center.ordinal() || u.getTypeID() == UnitTypes.Protoss_Nexus.ordinal() || u.getTypeID() == UnitTypes.Zerg_Hatchery.ordinal() ))
				return 1.0;
		}
		return 0.0;
	}
	
	private Double iHaveAir() {
		for (Unit u : bwapi.getMyUnits()) {
			if (!u.isBeingConstructed() && u.isCompleted()) {
				UnitType typ = bwapi.getUnitType(u.getTypeID());
				if (typ.isFlyer() && typ.isAttackCapable() ) 
					return 1.0;
			}
		}
		return 0.0;
	}
	
	private Double iHaveInvis() {
		for (Unit u : bwapi.getMyUnits()) {
			if (!u.isBeingConstructed() && u.isCompleted()) {
				int typID = u.getTypeID();
				if (
						typID == UnitTypes.Protoss_Dark_Templar.ordinal() || 
						typID == UnitTypes.Protoss_Arbiter.ordinal()) 
					return 1.0;
			}
		}
		return 0.0;
	}

}
