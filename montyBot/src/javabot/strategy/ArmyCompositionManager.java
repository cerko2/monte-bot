package javabot.strategy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javabot.*;
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
	private ArrayList<String> casesFromFile;	// tu je pole casov nacitanych zo suboru vo funkcii gameStarted()
	
	// Konstruktor (vola sa ked sa vytvori armyCompositionManager objekt v JavaBot.java)
	public ArmyCompositionManager(JNIBWAPI game) {
		this.bwapi = game;
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
			bwapi.printText("ERROR: Failed to load CBR cases file!");
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
	
	
	
	
}
