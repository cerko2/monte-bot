package javabot.util;

import java.util.ArrayList;

import javabot.JNIBWAPI;
import javabot.types.TechType.TechTypes;
import javabot.types.UnitType.UnitTypes;
import javabot.types.UpgradeType.UpgradeTypes;

public class ArmyComposition {
	public ArrayList<Integer> unitTypes;
	public ArrayList<Integer> ratio;
	public ArrayList<Integer> technologies;
	public ArrayList<Integer> upgrades;
	
	public ArrayList<Integer> getUnitTypes() {
		return this.unitTypes;
	}
	public ArrayList<Integer> getUpgrades() {
		return this.upgrades;
	}
	public ArrayList<Integer> getTechnologies() {
		return this.technologies;
	}

	
	public ArmyComposition(String s) {
		this.unitTypes = new ArrayList<Integer>();
		this.ratio = new ArrayList<Integer>();
		this.technologies = new ArrayList<Integer>();
		this.upgrades = new ArrayList<Integer>();
		
		String[] temp;
		temp = s.split("@");
		
		String[] unitStrings = temp[0].split(",");
		for (String us : unitStrings) {
			String[] u = us.split(";");
			this.ratio.add(Integer.valueOf(u[0]));
		    this.unitTypes.add(translateStringToUnitType(u[1]));
		}
		
		if (temp.length > 1) {
			String[] upgTechStrings = temp[1].split(",");
			for (String us : upgTechStrings) {
				if (translateStringToTechType(us) != -1) {
					this.technologies.add(translateStringToTechType(us));
				} else {
					this.upgrades.add(translateStringToUpgradeType(us));
				}
			}
			
		}
			
	}
	
	
	public String getString(JNIBWAPI game){
		String tmp1 = "";
		for (int i : unitTypes) tmp1 += game.getUnitType(i).getName().substring(game.getUnitType(i).getName().indexOf(" "))+" ";
		String tmp3 = "";
		for (int i : upgrades) tmp3 += game.getUpgradeType(i).getName()+" ";
		String tmp4 = "";
		for (int i : technologies) tmp4 += game.getTechType(i).getName()+" ";
		return 
				"["+tmp1+"]\n" +
				"RAT: "+String.valueOf(ratio)+" "+
				"UPG: "+tmp3+"\n"+
				"TECH: "+tmp4;
	}
	
	
	public int getRatio(Integer id){
		for (Integer i = 0; i < this.unitTypes.size(); i++){
			if (this.unitTypes.get(i) == id) return this.ratio.get(i);
			
		}
		return 0;
	}
	
	
	
	//Preklad zo stringoveho mena na UnitType
	private int translateStringToUnitType(String meno) {
		meno = meno.trim();
		if(meno.contains("Zealot")) return UnitTypes.Protoss_Zealot.ordinal();
		if(meno.contains("DarkTemplar")) return UnitTypes.Protoss_Dark_Templar.ordinal();
		if(meno.contains("HighTemplar")) return UnitTypes.Protoss_High_Templar.ordinal();
		if(meno.contains("Dragoon")) return UnitTypes.Protoss_Dragoon.ordinal();
		if(meno.contains("Reaver")) return UnitTypes.Protoss_Reaver.ordinal();
		if(meno.contains("Scout")) return UnitTypes.Protoss_Scout.ordinal();
		if(meno.contains("Carrier")) return UnitTypes.Protoss_Carrier.ordinal();
		if(meno.contains("Corsair")) return UnitTypes.Protoss_Corsair.ordinal();
		if(meno.contains("Arbiter")) return UnitTypes.Protoss_Arbiter.ordinal();
		if(meno.contains("Archon")) return UnitTypes.Protoss_Archon.ordinal();
		if(meno.contains("DarkArchon")) return UnitTypes.Protoss_Dark_Archon.ordinal();
		if(meno.contains("Zergling")) return UnitTypes.Zerg_Zergling.ordinal();
		if(meno.contains("Hydralisk")) return UnitTypes.Zerg_Hydralisk.ordinal();
		if(meno.contains("Lurker")) return UnitTypes.Zerg_Lurker.ordinal();
		if(meno.contains("Devourer")) return UnitTypes.Zerg_Devourer.ordinal();
		if(meno.contains("Mutalisk")) return UnitTypes.Zerg_Mutalisk.ordinal();
		if(meno.contains("Guardian")) return UnitTypes.Zerg_Guardian.ordinal();
		if(meno.contains("Ultralisk")) return UnitTypes.Zerg_Ultralisk.ordinal();
		if(meno.contains("Defiler")) return UnitTypes.Zerg_Defiler.ordinal();
		if(meno.contains("Scourge")) return UnitTypes.Zerg_Scourge.ordinal();
		if(meno.contains("Queen")) return UnitTypes.Zerg_Queen.ordinal();
		if(meno.contains("Marine")) return UnitTypes.Terran_Marine.ordinal();
		if(meno.contains("Firebat")) return UnitTypes.Terran_Firebat.ordinal();
		if(meno.contains("Medic")) return UnitTypes.Terran_Medic.ordinal();
		if(meno.contains("Ghost")) return UnitTypes.Terran_Ghost.ordinal();
		if(meno.contains("Vulture")) return UnitTypes.Terran_Vulture.ordinal();
		if(meno.contains("SiegeTank")) return UnitTypes.Terran_Siege_Tank_Tank_Mode.ordinal();
		if(meno.contains("Goliath")) return UnitTypes.Terran_Goliath.ordinal();
		if(meno.contains("Wraith")) return UnitTypes.Terran_Wraith.ordinal();
		if(meno.contains("ScienceVessel")) return UnitTypes.Terran_Science_Vessel.ordinal();
		if(meno.contains("Battlecruiser")) return UnitTypes.Terran_Battlecruiser.ordinal();
		if(meno.contains("Valkyrie")) return UnitTypes.Terran_Valkyrie.ordinal();
		else return -1;
		
	}
	
	
	
	

	// Preklad zo stringoveho mena na TechType
	private int translateStringToTechType(String meno) {
		if(meno.contains("Recall")) return TechTypes.Recall.ordinal();
		if(meno.contains("StasisField")) return TechTypes.Stasis_Field.ordinal();
		if(meno.contains("DisruptionWeb")) return TechTypes.Disruption_Web.ordinal();
		if(meno.contains("Feedback")) return TechTypes.Feedback.ordinal();
		if(meno.contains("MindControl")) return TechTypes.Mind_Control.ordinal();
		if(meno.contains("Maelstrom")) return TechTypes.Maelstrom.ordinal();
		if(meno.contains("PsionicStorm")) return TechTypes.Psionic_Storm.ordinal();
		if(meno.contains("Hallucination")) return TechTypes.Hallucination.ordinal();
				
		return -1;
	}
	
	
	
	private int translateStringToUpgradeType(String meno) {
		
		if(meno.contains("LegEnhancements")) return UpgradeTypes.Leg_Enhancements.ordinal();
		if(meno.contains("SingularityCharge")) return UpgradeTypes.Singularity_Charge.ordinal();
		if(meno.contains("CarrierCapacity")) return UpgradeTypes.Carrier_Capacity.ordinal();
		if(meno.contains("ArgusTalisman")) return UpgradeTypes.Argus_Talisman.ordinal();
		if(meno.contains("KhaydarinAmulet")) return UpgradeTypes.Khaydarin_Amulet.ordinal();
		if(meno.contains("KhaydarinCore")) return UpgradeTypes.Khaydarin_Core.ordinal();
		if(meno.contains("ApialSensors")) return UpgradeTypes.Apial_Sensors.ordinal();
		if(meno.contains("GraviticThursters")) return UpgradeTypes.Gravitic_Thrusters.ordinal();
		if(meno.contains("ArgusJewel")) return UpgradeTypes.Argus_Jewel.ordinal();
		if(meno.contains("GraviticBoosters")) return UpgradeTypes.Gravitic_Boosters.ordinal();
		if(meno.contains("SensorArray")) return UpgradeTypes.Sensor_Array.ordinal();
		if(meno.contains("ReaverCapacity")) return UpgradeTypes.Reaver_Capacity.ordinal();
		if(meno.contains("ScarabDamage")) return UpgradeTypes.Scarab_Damage.ordinal();
		if(meno.contains("GraviticDrive")) return UpgradeTypes.Gravitic_Drive.ordinal();
		
    
		return -1;
	}

}
