package javabot.strategy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.macro.Boss;
import javabot.model.Race;
import javabot.model.Unit;

public class OpponentKnowledgeBase extends AbstractManager{
	
	private JNIBWAPI game;
	private Boss boss;
	private String enemyName = "";
	private int enemyRace = 6; //RANDOM RACE = 6
	private int openingID = -1;
	private static final int GAME_WON = 1;
	private static final int GAME_LOST = 2;
	private static final int GAME_INTERRUPTED = 3;
	private int gameEndedStatus = GAME_INTERRUPTED;
	
	private HashMap<String, HashMap<Integer, double[]>> playersKnowledgeBase;
	private HashMap<Integer, HashMap<Integer, double[]>> racesKnowledgeBase;
	
	public OpponentKnowledgeBase(Boss boss){
		this.game = boss.game;
		this.boss = boss;
		enemyName = game.getEnemies().get(0).getName();
		enemyRace = game.getEnemies().get(0).getRaceID();
		playersKnowledgeBase = new HashMap<String, HashMap<Integer, double[]>>();
		racesKnowledgeBase = new HashMap<Integer, HashMap<Integer, double[]>>();
		try {
			readFile();
		} catch (IOException e) {
			System.err.println("Error while reading opponentKnowledgeBase.txt");
			e.printStackTrace();
		}
	}
	
	public void matchEnded(boolean winner){
		if (winner){
			gameEndedStatus = GAME_WON;
		}
		else{
			for (Unit u : game.getMyUnits()){
				if (u != null && game.getUnitType(u.getTypeID()).isBuilding()){
					gameEndedStatus = GAME_INTERRUPTED;
					return;
				}
			}
			gameEndedStatus = GAME_LOST;
		}
	}
	
	public void gameEnded(){
		if (gameEndedStatus == GAME_LOST || gameEndedStatus == GAME_WON){
			openingID = boss.getOpeningManager().getOpeningID();
			if (openingID != -1){
				
				double time = game.getFrameCount() / 24 / 60;
				int c = (gameEndedStatus == GAME_WON) ? 1 : -1;
				double points = 10 * c / time;
				points = (points > 10) ? 10 : points;
				
				HashMap<Integer, double[]> enemysKB = playersKnowledgeBase.get(enemyName);
				if (enemysKB == null){
					enemysKB = new HashMap<Integer, double[]>();
					enemysKB.put(openingID, new double[]{1, points});
					playersKnowledgeBase.put(enemyName, enemysKB);
				}
				else{
					double[] openingStats = enemysKB.get(openingID);
					if (openingStats == null){
						openingStats = new double[]{1, points};
						enemysKB.put(openingID, openingStats);
						playersKnowledgeBase.put(enemyName, enemysKB);
					}
					else{
						openingStats[0] += 1;
						openingStats[1] = (openingStats[1] * (openingStats[0] - 1) + points) / openingStats[0];
						enemysKB.put(openingID, openingStats);
						playersKnowledgeBase.put(enemyName, enemysKB);
					}
				}
				
				HashMap<Integer, double[]> racesKB = racesKnowledgeBase.get(enemyRace);
				if (racesKB == null){
					racesKB = new HashMap<Integer, double[]>();
					racesKB.put(openingID, new double[]{1, points});
					racesKnowledgeBase.put(enemyRace, racesKB);
				}
				else{
					double[] openingStats = racesKB.get(openingID);
					if (openingStats == null){
						openingStats = new double[]{1, points};
						racesKB.put(openingID, openingStats);
						racesKnowledgeBase.put(enemyRace, racesKB);
					}
					else{
						openingStats[0] += 1;
						openingStats[1] = (openingStats[1] * (openingStats[0] - 1) + points) / openingStats[0];
						racesKB.put(openingID, openingStats);
						racesKnowledgeBase.put(enemyRace, racesKB);
					}
				}
				
				try {
					writeToFile();
				} catch (IOException e) {
					System.err.println("Error while writing to file 'opponentKnowledgeBase.txt'.");
					e.printStackTrace();
				}
			}
		}
	}
	
	public HashMap<Integer, double[]> getEnemysKB(String name){
		return playersKnowledgeBase.get(name);
	}
	
	public HashMap<Integer, double[]> getRacesKB(Integer raceID){
		return racesKnowledgeBase.get(raceID);
	}
	
	private void readFile() throws IOException {
		Path path = Paths.get("bwapi-data/AI/montyBot/opponentKnowledgeBase.txt");
		
		File f = new File(path.toString());
		if(!f.exists()){
			f.createNewFile();
			System.out.println("File 'opponentKnowledgeBase.txt' was automatically created.");
		}
		
	    try (Scanner scanner =  new Scanner(path, StandardCharsets.UTF_8.name())){
	    	if (scanner.hasNextLine()){
	    		scanner.nextLine();
	    		String statistics = "";
	    		while (scanner.hasNextLine()){
	    			String name = scanner.nextLine();
	    			statistics = "";
	    			HashMap<Integer, double[]> openings = new HashMap<Integer, double[]>();
	    			while (scanner.hasNextLine() && !statistics.equals("#PLAYER") && !statistics.equals("#RACE")){
	    				statistics = scanner.nextLine();
	    				if (!statistics.equals("#PLAYER") && !statistics.equals("#RACE")){
		    				String[] split = statistics.split(" ");
		    				int openingID = Integer.parseInt(split[0]);
		    				double[] openingStats = {Double.parseDouble(split[1]), Double.parseDouble(split[2])};
		    				openings.put(openingID, openingStats);
	    				}
	    				
	    				if (statistics.equals("#RACE")) break;
	    			}
	    			playersKnowledgeBase.put(name, openings);
	    			if (statistics.equals("#RACE")) break;
	    		}
	    		
	    		//TODO tato kontrola sa potom moze vymazat, pretoze nikdy nenastane (pokial
	    		//je subor vytvarany novym sposobom)
	    		if (scanner.hasNextLine()){
	    			while (scanner.hasNextLine()){
		    			String race = scanner.nextLine();
		    		    int raceID = (race == "RANDOM") ? 6 : Race.valueOf(race).getID();
		    		    statistics = "";
		    			HashMap<Integer, double[]> openings = new HashMap<Integer, double[]>();
		    			while (scanner.hasNextLine() && !statistics.equals("#RACE")){
		    				statistics = scanner.nextLine();
		    				if (!statistics.equals("#RACE")){
			    				String[] split = statistics.split(" ");
			    				int openingID = Integer.parseInt(split[0]);
			    				double[] openingStats = {Double.parseDouble(split[1]), Double.parseDouble(split[2])};
			    				openings.put(openingID, openingStats);
		    				}
		    			}
		    			racesKnowledgeBase.put(raceID, openings);
		    		}
	    		}
	    	}
	    	else{
	    		game.printText("Empty knowledge base.");
	    	}
	    }
	}
	
	private void writeToFile() throws IOException {
		Path path = Paths.get("bwapi-data/AI/montyBot/opponentKnowledgeBase.txt");
		
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)){
			for (Entry<String, HashMap<Integer, double[]>> entry : playersKnowledgeBase.entrySet()) {
				writer.write("#PLAYER");
				writer.newLine();
				String name = entry.getKey();
				writer.write(name);
				writer.newLine();
				for (Entry<Integer, double[]> opening : entry.getValue().entrySet()){
					writer.write(opening.getKey() + " " + (int)opening.getValue()[0] + " " + opening.getValue()[1]);
					writer.newLine();
				}
			}
			
			for (Entry<Integer, HashMap<Integer, double[]>> entry : racesKnowledgeBase.entrySet()) {
				writer.write("#RACE");
				writer.newLine();
				String race = (entry.getKey() == 6) ? "RANDOM" : Race.fromID(entry.getKey()).toString();
				writer.write(race);
				writer.newLine();
				for (Entry<Integer, double[]> opening : entry.getValue().entrySet()){
					writer.write(opening.getKey() + " " + (int)opening.getValue()[0] + " " + opening.getValue()[1]);
					writer.newLine();
				}
			}
			writer.close();
		}
	}
}
