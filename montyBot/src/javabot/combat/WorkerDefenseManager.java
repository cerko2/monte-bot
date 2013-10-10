package javabot.combat;

import java.util.ArrayList;

import javabot.JNIBWAPI;
import javabot.macro.Boss;
import javabot.model.Unit;


public class WorkerDefenseManager {

	private Boss boss;
	private JNIBWAPI game;
	
	
	/**
	 * WorkerDefenseManager constructor.
	 *
	 * @param Boss - boss class
	 */
	public WorkerDefenseManager(Boss boss) {
		this.boss = boss;
		game = boss.game;
	}
	
	/**
	 * Main method called approximately 30 times per second.
	 *
	 * @param units - units assigned to worker defense manager
	 */
	public void update(ArrayList<Unit> units) {
		
		
	}
	
}
