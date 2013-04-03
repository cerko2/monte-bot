package javabot.strategy;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.macro.Boss;

public class OpponentKnowledgeBase extends AbstractManager{
	
	private JNIBWAPI game;
	private Boss boss;
	
	private String enemyName = "";
	private int openingID = -1;
	
	private HashMap<String, HashMap<Integer, double[]>> knowledgeBase;
	
	public OpponentKnowledgeBase(Boss boss){
		this.game = boss.game;
		this.boss = boss;
		enemyName = game.getEnemies().get(0).getName();
		knowledgeBase = new HashMap<String, HashMap<Integer, double[]>>();
		try {
			readFile();
		} catch (IOException e) {
			System.err.println("Error while reading opponentKnowledgeBase.txt");
			e.printStackTrace();
		}
	}
	
	public void gameEnded(){
		openingID = boss.getOpeningManager().getOpeningID();
		if (openingID != -1){
			
			//TODO zistit cas!!! v C++ elapsedTime()... if (game.get)
			//game.getFrameCount() / 24;
			double time = new Random().nextDouble() * 20;
			
			//double points = 10 / time;
			double points = 10;
			
			HashMap<Integer, double[]> enemysKB = knowledgeBase.get(enemyName);
			if (enemysKB == null){
				enemysKB = new HashMap<Integer, double[]>();
				enemysKB.put(openingID, new double[]{1, points});
				knowledgeBase.put(enemyName, enemysKB);
			}
			else{
				double[] openingStats = enemysKB.get(openingID);
				if (openingStats == null){
					openingStats = new double[]{1, points};
					enemysKB.put(openingID, openingStats);
					knowledgeBase.put(enemyName, enemysKB);
				}
				else{
					openingStats[0] += 1;
					openingStats[1] = (openingStats[1] * (openingStats[0] - 1) + points) / openingStats[0];
					enemysKB.put(openingID, openingStats);
					knowledgeBase.put(enemyName, enemysKB);
				}
			}
			
			try {
				writeToFile();
			} catch (IOException e) {
				System.err.println("Error while writing to opponentKnowledgeBase.txt");
				e.printStackTrace();
			}
		}
	}
	
	public HashMap<Integer, double[]>> getEnemysKB(String name){
		return knowledgeBase.get(name);
	}
	
	private void readFile() throws IOException {
		Path path = Paths.get("bwapi-data/AI/montyBot/opponentKnowledgeBase.txt");
	    try (Scanner scanner =  new Scanner(path, StandardCharsets.UTF_8.name())){
	    	if (scanner.hasNextLine()){
	    		int numberOfRecords = scanner.nextInt();
	    		scanner.nextLine();
	    		scanner.nextLine();
	    		for (int i = 1; i <= numberOfRecords; i++){
	    			String name = scanner.nextLine();
	    			String statistics = "";
	    			HashMap<Integer, double[]> openings = new HashMap<Integer, double[]>();
	    			while (scanner.hasNextLine() && !statistics.equals("#PLAYER")){
	    				statistics = scanner.nextLine();
	    				if (!statistics.equals("#PLAYER")){
		    				String[] split = statistics.split(" ");
		    				int openingID = Integer.parseInt(split[0]);
		    				double[] openingStats = {Double.parseDouble(split[1]), Double.parseDouble(split[2])};
		    				openings.put(openingID, openingStats);
	    				}
	    			}
	    			knowledgeBase.put(name, openings);
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
			writer.write(knowledgeBase.size() + "");
			writer.newLine();
			for (Entry<String, HashMap<Integer, double[]>> entry : knowledgeBase.entrySet()) {
				writer.write("#PLAYER");
				writer.newLine();
				String name = entry.getKey();
				writer.write(name);
				writer.newLine();
				for (Entry<Integer, double[]> opening : entry.getValue().entrySet()){
					writer.write(opening.getKey() + " " + opening.getValue()[0] + " " + opening.getValue()[1]);
					writer.newLine();
				}
			}
			writer.close();
		}
	}
}
