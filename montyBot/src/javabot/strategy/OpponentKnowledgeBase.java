package javabot.strategy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
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
	
	public void setOpeningID(int ID){
		openingID = ID;
	}
	
	public void gameEnded(){
		if (openingID != -1){
			
		}
	}
	
	private void readFile() throws IOException {
		Path path = Paths.get("bwapi-data/AI/montyBot/opponentKnowledgeBase.txt");
	    try (Scanner scanner =  new Scanner(path, StandardCharsets.UTF_8.name())){
	    	if (scanner.hasNextLine()){
	    		int numberOfRecords = scanner.nextInt();
	    		scanner.nextLine();
	    		for (int i = 1; i <= numberOfRecords; i++){
	    			String name = scanner.nextLine();
	    			while (scanner.hasNextLine() && !scanner.nextLine().equals("#PLAYER")){
	    				//TODO read openings
	    			}
	    		}
	    	}
	    	else{
	    		game.printText("Empty knowledge base.");
	    	}
	    }
	}
}
