package javabot.strategy.planner;

import javabot.JNIBWAPI;
import javabot.strategy.planner.action.Action;
import javabot.strategy.planner.action.BuildArbiterTribunal;
import javabot.strategy.planner.action.BuildAssimilator;
import javabot.strategy.planner.action.BuildCitadelOfAdun;
import javabot.strategy.planner.action.BuildCyberneticsCore;
import javabot.strategy.planner.action.BuildFleetBeacon;
import javabot.strategy.planner.action.BuildForge;
import javabot.strategy.planner.action.BuildGateway;
import javabot.strategy.planner.action.BuildNexus;
import javabot.strategy.planner.action.BuildObservatory;
import javabot.strategy.planner.action.BuildPhotonCannon;
import javabot.strategy.planner.action.BuildPylon;
import javabot.strategy.planner.action.BuildRoboticsFacility;
import javabot.strategy.planner.action.BuildRoboticsSupportBay;
import javabot.strategy.planner.action.BuildShieldBattery;
import javabot.strategy.planner.action.BuildStargate;
import javabot.strategy.planner.action.BuildTemplarArchives;
import javabot.strategy.planner.action.TrainArbiter;
import javabot.strategy.planner.action.TrainCarrier;
import javabot.strategy.planner.action.TrainCorsair;
import javabot.strategy.planner.action.TrainDarkTemplar;
import javabot.strategy.planner.action.TrainDragoon;
import javabot.strategy.planner.action.TrainHighTemplar;
import javabot.strategy.planner.action.TrainObserver;
import javabot.strategy.planner.action.TrainProbe;
import javabot.strategy.planner.action.TrainReaver;
import javabot.strategy.planner.action.TrainScout;
import javabot.strategy.planner.action.TrainShuttle;
import javabot.strategy.planner.action.TrainZealot;
import javabot.strategy.planner.action.Wait;
import javabot.types.UnitType.UnitTypes;


public class ActionFactory {

	public static Action createAction(int typeID, JNIBWAPI game){
		if (typeID == UnitTypes.Protoss_Arbiter.ordinal()){
			return new TrainArbiter(game);
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
			return new TrainCarrier(game);
		}
		else if (typeID == UnitTypes.Protoss_Citadel_of_Adun.ordinal()){
			return new BuildCitadelOfAdun(game);
		}
		else if (typeID == UnitTypes.Protoss_Corsair.ordinal()){
			return new TrainCorsair(game);
		}
		else if (typeID == UnitTypes.Protoss_Cybernetics_Core.ordinal()){
			return new BuildCyberneticsCore(game);
		}
		else if (typeID == UnitTypes.Protoss_Dark_Archon.ordinal()){

		}
		else if (typeID == UnitTypes.Protoss_Dark_Templar.ordinal()){
			return new TrainDarkTemplar(game);
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
			return new TrainHighTemplar(game);
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
			return new TrainObserver(game);
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
			return new TrainReaver(game);
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
			return new TrainScout(game);
		}
		else if (typeID == UnitTypes.Protoss_Shield_Battery.ordinal()){
			return new BuildShieldBattery(game);
		}
		else if (typeID == UnitTypes.Protoss_Shuttle.ordinal()){
			return new TrainShuttle(game);
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
