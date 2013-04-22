package javabot.strategy.action;

import javabot.JNIBWAPI;
import javabot.types.UnitType.UnitTypes;


public class ActionFactory {

	public static Action createAction(int typeID, JNIBWAPI game){
		if (typeID == UnitTypes.Protoss_Arbiter.ordinal()){
			//return new TrainArbiter(game);
		}
		else if (typeID == UnitTypes.Protoss_Arbiter_Tribunal.ordinal()){
			return new BuildArbiterTribunal(game);
		}
		else if (typeID == UnitTypes.Protoss_Archon.ordinal()){
			
		}
		else if (typeID == UnitTypes.Protoss_Assimilator.ordinal()){
			return new BuildAssimilator(game);
		}
		else if (typeID == UnitTypes.Protoss_Carrier.ordinal()){

		}
		else if (typeID == UnitTypes.Protoss_Citadel_of_Adun.ordinal()){
			return new BuildCitadelOfAdun(game);
		}
		else if (typeID == UnitTypes.Protoss_Corsair.ordinal()){

		}
		else if (typeID == UnitTypes.Protoss_Cybernetics_Core.ordinal()){
			return new BuildCyberneticsCore(game);
		}
		else if (typeID == UnitTypes.Protoss_Dark_Archon.ordinal()){

		}
		else if (typeID == UnitTypes.Protoss_Dark_Templar.ordinal()){

		}
		else if (typeID == UnitTypes.Protoss_Dragoon.ordinal()){
			return new TrainDragoon(game);
		}
		else if (typeID == UnitTypes.Protoss_Fleet_Beacon.ordinal()){
			return new BuildFleetBeacon(game);
		}
		else if (typeID == UnitTypes.Protoss_Forge.ordinal()){
			return new BuildForge(game);
		}
		else if (typeID == UnitTypes.Protoss_Gateway.ordinal()){
			return new BuildGateway(game);
		}
		else if (typeID == UnitTypes.Protoss_High_Templar.ordinal()){

		}
		else if (typeID == UnitTypes.Protoss_Interceptor.ordinal()){

		}
		else if (typeID == UnitTypes.Protoss_Nexus.ordinal()){
			return new BuildNexus(game);
		}
		else if (typeID == UnitTypes.Protoss_Observatory.ordinal()){
			return new BuildObservatory(game);
		}
		else if (typeID == UnitTypes.Protoss_Observer.ordinal()){

		}
		else if (typeID == UnitTypes.Protoss_Photon_Cannon.ordinal()){
			return new BuildPhotonCannon(game);
		}
		else if (typeID == UnitTypes.Protoss_Probe.ordinal()){
			return new TrainProbe(game);
		}
		else if (typeID == UnitTypes.Protoss_Pylon.ordinal()){
			return new BuildPylon(game);
		}
		else if (typeID == UnitTypes.Protoss_Reaver.ordinal()){

		}
		else if (typeID == UnitTypes.Protoss_Robotics_Facility.ordinal()){
			return new BuildRoboticsFacility(game);
		}
		else if (typeID == UnitTypes.Protoss_Robotics_Support_Bay.ordinal()){
			return new BuildRoboticsSupportBay(game);
		}
		else if (typeID == UnitTypes.Protoss_Scarab.ordinal()){

		}
		else if (typeID == UnitTypes.Protoss_Scout.ordinal()){

		}
		else if (typeID == UnitTypes.Protoss_Shield_Battery.ordinal()){
			return new BuildShieldBattery(game);
		}
		else if (typeID == UnitTypes.Protoss_Shuttle.ordinal()){

		}
		else if (typeID == UnitTypes.Protoss_Stargate.ordinal()){
			return new BuildStargate(game);
		}
		else if (typeID == UnitTypes.Protoss_Templar_Archives.ordinal()){
			return new BuildTemplarArchives(game);
		}
		else if (typeID == UnitTypes.Protoss_Zealot.ordinal()){
			return new TrainZealot(game);
		}
		
		return new Wait(game);
		

	}
}
