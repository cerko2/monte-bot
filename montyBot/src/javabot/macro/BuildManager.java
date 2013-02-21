package javabot.macro;

import java.util.ArrayList;

import javabot.JNIBWAPI;

public class BuildManager {
	JNIBWAPI game;
	Boss boss;
	ArrayList<Integer> buildList;
	
	public BuildManager(JNIBWAPI game, Boss boss){
		this.game = game;
		this.boss = boss;
		buildList = new ArrayList<Integer>();
	}
	
	public void gameUpdate(){
		
	}
}
