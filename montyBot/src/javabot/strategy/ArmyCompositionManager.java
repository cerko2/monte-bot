package javabot.strategy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javabot.*;
import javabot.macro.UnitProductionManager;
import javabot.model.*;
import javabot.util.*;
import javabot.types.*;
import javabot.types.TechType.TechTypes;
import javabot.types.UnitType.UnitTypes;
import javabot.types.UpgradeType.UpgradeTypes;

// Trieda ArmyCompositionManager
public class ArmyCompositionManager extends AbstractManager {
	
	// Deklarovanie nejakych privatnych premennych 
	private JNIBWAPI bwapi;
	private UnitProductionManager unitProductionManager;
	private OpponentPositioning opponentPositioningManager;
	private OpponentModeling opponentModellingManager;
	private ArrayList<String> casesFromFile;	// tu je pole casov nacitanych zo suboru vo funkcii gameStarted()
	
	private ArmyComposition currentComposition = new ArmyComposition("100;Zealot");

	public ArmyComposition getDesiredArmyComposition() {
		return this.currentComposition;
	}
	
	private boolean isRelevantType(int typeID) {
		if (
			typeID == UnitTypes.Protoss_Zealot.ordinal() ||
			typeID == UnitTypes.Protoss_Dark_Templar.ordinal() ||
			typeID == UnitTypes.Protoss_High_Templar.ordinal() ||
			typeID == UnitTypes.Protoss_Dragoon.ordinal() ||
			typeID == UnitTypes.Protoss_Reaver.ordinal() ||
			typeID == UnitTypes.Protoss_Scout.ordinal() ||
			typeID == UnitTypes.Protoss_Carrier.ordinal() ||
			typeID == UnitTypes.Protoss_Corsair.ordinal() ||
			typeID == UnitTypes.Protoss_Arbiter.ordinal() ||
			typeID == UnitTypes.Protoss_Archon.ordinal() ||
			typeID == UnitTypes.Protoss_Dark_Archon.ordinal() ||
			typeID == UnitTypes.Zerg_Zergling.ordinal() ||
			typeID == UnitTypes.Zerg_Hydralisk.ordinal() ||
			typeID == UnitTypes.Zerg_Lurker.ordinal() ||
			typeID == UnitTypes.Zerg_Devourer.ordinal() ||
			typeID == UnitTypes.Zerg_Mutalisk.ordinal() ||
			typeID == UnitTypes.Zerg_Guardian.ordinal() ||
			typeID == UnitTypes.Zerg_Ultralisk.ordinal() ||
			typeID == UnitTypes.Zerg_Defiler.ordinal() ||
			typeID == UnitTypes.Zerg_Scourge.ordinal() ||
			typeID == UnitTypes.Zerg_Queen.ordinal() ||
			typeID == UnitTypes.Terran_Marine.ordinal() ||
			typeID == UnitTypes.Terran_Firebat.ordinal() ||
			typeID == UnitTypes.Terran_Medic.ordinal() ||
			typeID == UnitTypes.Terran_Ghost.ordinal() ||
			typeID == UnitTypes.Terran_Vulture.ordinal() ||
			typeID == UnitTypes.Terran_Siege_Tank_Tank_Mode.ordinal() ||
			typeID == UnitTypes.Terran_Goliath.ordinal() ||
			typeID == UnitTypes.Terran_Wraith.ordinal() ||
			typeID == UnitTypes.Terran_Science_Vessel.ordinal() ||
			typeID == UnitTypes.Terran_Battlecruiser.ordinal() ||
			typeID == UnitTypes.Terran_Valkyrie.ordinal()
		){
			return true;
		} else {
			return false;
		}

	}
	
	// Toto vrati pole typeIDciek vsetkych relevantnych nepriatelovych jednotiek.
	// Ak je tam povedzme marinak 5x, tak v tom poli bude jeho typeID 5x.
	private ArrayList<Integer> getRelevantUnitsTypeIDs() {
		ArrayList<Integer> ret = new ArrayList<>();
		for (Unit u : opponentPositioningManager.getEnemyUnits()) {
			if (isRelevantType(u.getTypeID())) ret.add(u.getTypeID());
		}
		return ret;
	}
	
	private int getRatio(ArrayList<Integer> units, int typeID) {
		int count = 0;
		for (int i : units) {
			if (i == typeID) count++;
		}
		return Math.round(Math.round(((double)count / (double)units.size()) * (double)100));
	}
	
	// GameUpdate event
	public void gameUpdate() {
		if (bwapi.getFrameCount() % 97 == 0) {
			
			// Create an object for enemy army composition 
			
			// first, get all the relevant units that enemy has now
			ArrayList<Integer> rel = getRelevantUnitsTypeIDs();
			
			// now, add units that he probably will have in a few minutes
			for (int i : opponentModellingManager.getPredictedUnits()){
				bwapi.printText(String.valueOf(i));
				rel.add(i);
			}
			
			String enemyStr = "";
			for (int i : rel) {
				if (!enemyStr.contains(getRatio(rel, i)+";"+translateUnitTypeIDtoString(i))) {
					enemyStr += getRatio(rel, i)+";"+translateUnitTypeIDtoString(i)+",";
				}
			}
			if ((enemyStr.length() > 2) && (enemyStr.lastIndexOf(",") == enemyStr.length()-1)) 
				enemyStr = enemyStr.substring(0, enemyStr.length()-1);
			
			// if there is some enemy army
			if (enemyStr.length() > 2) {
			
				ArmyComposition enemyArmy = new ArmyComposition(enemyStr);
				ArmyComposition ourResponse = getArmyComposition(enemyArmy);
				
				// update our remembered composition
				this.currentComposition = ourResponse;
				
				// order the composition from Unit Production Manager
				ArrayList<Double> order = new ArrayList<>();
				order.add(0,Double.valueOf(ourResponse.getRatio(UnitTypes.Protoss_Zealot.ordinal())));
				order.add(1,Double.valueOf(ourResponse.getRatio(UnitTypes.Protoss_Dragoon.ordinal())));
				order.add(2,Double.valueOf(ourResponse.getRatio(UnitTypes.Protoss_High_Templar.ordinal())));
				order.add(3,Double.valueOf(ourResponse.getRatio(UnitTypes.Protoss_Dark_Templar.ordinal())));
				order.add(4,Double.valueOf(ourResponse.getRatio(UnitTypes.Protoss_Shuttle.ordinal())));
				order.add(5,Double.valueOf(ourResponse.getRatio(UnitTypes.Protoss_Reaver.ordinal())));
				order.add(6,Double.valueOf(ourResponse.getRatio(UnitTypes.Protoss_Observer.ordinal())));
				order.add(7,Double.valueOf(ourResponse.getRatio(UnitTypes.Protoss_Scout.ordinal())));
				order.add(8,Double.valueOf(ourResponse.getRatio(UnitTypes.Protoss_Corsair.ordinal())));
				order.add(9,Double.valueOf(ourResponse.getRatio(UnitTypes.Protoss_Carrier.ordinal())));
				order.add(10,Double.valueOf(ourResponse.getRatio(UnitTypes.Protoss_Arbiter.ordinal())));
				order.add(11,Double.valueOf(ourResponse.getRatio(UnitTypes.Protoss_Archon.ordinal())));
				order.add(12,Double.valueOf(ourResponse.getRatio(UnitTypes.Protoss_Dark_Archon.ordinal())));
				
				this.unitProductionManager.setRateArmy(order);
				/*
				ID = 0 is Zealot
				ID = 1 is Dragoon
				ID = 2 is High Templar
				ID = 3 is Dark Templar
				ID = 4 is Shuttle
				ID = 5 is Reaver
				ID = 6 is Observer
				ID = 7 is Scout
				ID = 8 is Corsair
				ID = 9 is Carrier
				ID = 10 is Arbiter
				ID = 11 is Archon
				ID = 12 is Dark Archon
				 */
			} else {
				// order default composition (dragoons only) if we know nothing about the opponent
				ArrayList<Double> order = new ArrayList<>();
				order.add(0,0.0); order.add(1,100.0); order.add(2,0.0); order.add(3,0.0);
				order.add(4,0.0); order.add(5,0.0); order.add(6,0.0); order.add(7,0.0);
				order.add(8,0.0); order.add(9,0.0); order.add(10,0.0); order.add(11,0.0);
				order.add(12,0.0);
				this.unitProductionManager.setRateArmy(order);
			}
		} 
	}
	
	// Konstruktor (vola sa ked sa vytvori armyCompositionManager objekt v JavaBot.java)
	public ArmyCompositionManager(JNIBWAPI game, UnitProductionManager upm, OpponentPositioning op, OpponentModeling om) {
		this.bwapi = game;
		this.unitProductionManager = upm;
		this.opponentPositioningManager = op;
		this.opponentModellingManager = om;
	}
	
	// Eventy (volane napriklad na zaciatku hry alebo na kazdom frame):
	public void gameStarted() {
		
		// Tu sa nacitaju zo suboru vsetky casy do premennej casesFromFile
		this.casesFromFile = new ArrayList<String>();

		String[] riadky = null;
		try {
			riadky = citajZoSuboru("bwapi-data/AI/montyBot/cbr-cases.txt");
		} catch (IOException e) {
			e.printStackTrace();
			bwapi.printText("ERROR: Failed to load a file with CBR cases!");
		}
		
		for (String kejs : riadky) {
			if((kejs.contains("%")) && (kejs.substring(0,kejs.indexOf("%")).trim().length() > 0) ) {
				casesFromFile.add(kejs.substring(0,kejs.indexOf("%")).trim() );
			} else if (!kejs.contains("%") && (kejs.trim().length() > 0) && (kejs.contains("---"))) {
				casesFromFile.add(kejs);
			}
		}
		
	}
	
	public ArmyComposition getArmyComposition (ArmyComposition a){
		
		ArmyComposition defaultArmy = new ArmyComposition("100;Zealot@LegEnhancements");
		int sim = 0;
		String rightSide = "";
		
		if (casesFromFile != null){
			for (String s : casesFromFile){
				String[] temp = s.split("---");
				String left = temp[0];
				String right = temp[1];
				
				if (sim < getSimilarity(new ArmyComposition(left), a)) {
						sim = getSimilarity(new ArmyComposition(left), a);
						rightSide = right;
				}
			}
			return new ArmyComposition(rightSide);	
		}
		return defaultArmy;
	}
	
	
	public int getSimilarity(ArmyComposition a, ArmyComposition b){
		int sim = 0;
		
		for (int u : a.unitTypes){
			if (b.unitTypes.contains(u)){
				if (b.getRatio(u) > (a.getRatio(u))) sim = sim + a.getRatio(u);
				if (b.getRatio(u) <= (a.getRatio(u))) sim = sim + b.getRatio(u);
			}
			
		}
		
		return sim;
	}
	
	
	
    private static String[] citajZoSuboru(String menoSuboru) throws IOException {
        FileReader r = new FileReader(menoSuboru);
        BufferedReader br = new BufferedReader(r); 

        // spocitame pocet neprazdnych riadkov v subore 
        // aby sme vedeli rozmer pola ktore mame vratit 
        int pocetRiadkov = 0;
        String riadok = "";
        // ked sa riadok == null tak sme narazili na koniec suboru 
        while (riadok != null) {
            riadok = br.readLine();
            if (riadok != null) {
                pocetRiadkov++;
            }
        }

        int i = 0;

        br.close();
        
        r = new FileReader(menoSuboru);
        br = new BufferedReader(r);

        // alokujeme pole velkosti poctu neprazdnych riadkov v subore 
        String[] vysledok = new String[pocetRiadkov];
        riadok = "";
        
        // nacitame riadky 
        while (riadok != null) {
            riadok = br.readLine();
            if (riadok != null) {
                vysledok[i] = riadok;
                i++;
            }
        }

        // zavrieme subor 
        br.close();

        // vratime dane pole 
        return vysledok;
    }
	
	private String translateUnitTypeIDtoString(int typeID) {
		String nam = bwapi.getUnitType(typeID).getName();
		return nam.substring(nam.indexOf(" ")).replace(" ", "").replace("TankMode", "").replace("SiegeMode", "");
	}
	
	
}
