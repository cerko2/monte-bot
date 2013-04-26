package javabot.util;

import java.util.ArrayList;
import java.util.HashMap;

import javabot.JNIBWAPI;
import javabot.macro.Boss;
import javabot.strategy.OpponentModeling;

public class OctaveThread implements Runnable {

	private HashMap<Integer, ArrayList<Float>> theta;
	private JNIBWAPI bwapi;
	private Octave octave;
	private OpponentModeling opponentModelling;
	private Boss boss;
	
	public OctaveThread(JNIBWAPI game, OpponentModeling om, Boss b) {
		this.theta = new HashMap<>();
		this.bwapi = game;
		this.octave = new Octave(bwapi);
		this.opponentModelling = om;
		this.boss = b;
		
		Thread t = new Thread(this);
		t.start();

	}
	
	@Override
	public void run() {
				
		for (int ut : opponentModelling.relevantUnitTypes) {
			this.theta.put(ut, octave.computeTheta(opponentModelling.opponent, ut, opponentModelling.getOrderOfPolynomial(ut)));
			this.opponentModelling.setThetaUnit(ut, this.theta.get(ut));
			

			// START DEBUG
			if (boss.OPPONENT_MODELLING_DEBUG) {
				String dbg = "Theta of"+bwapi.getUnitType(ut).getName().substring(bwapi.getUnitType(ut).getName().indexOf(" ")) +": ";
				for (Float f : theta.get(ut).subList(0, 5)) {
					dbg += String.valueOf(f)+" ";
				}
				System.out.println(dbg+"...");
			}
			// END DEBUG
		}
		
		if (boss.OPPONENT_MODELLING_DEBUG) System.out.println("FINISHED computing Theta");
		
	}
	

}
