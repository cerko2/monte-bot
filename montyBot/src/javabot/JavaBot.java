package javabot;

import java.awt.Point;

import javabot.macro.Boss;
import javabot.macro.UnitProductionManager;
import javabot.model.Unit;
import javabot.strategy.OpeningManager;
import javabot.util.BWColor;

public class JavaBot extends AbstractManager {
	
	private Boss boss;
	
	// Some miscelaneous variables. Feel free to add yours.
	int homePositionX;
	int homePositionY;

	private JNIBWAPI bwapi;
	public static void main(String[] args) {
		new JavaBot();
	}
	public JavaBot() {
		bwapi = new JNIBWAPI(this);
		bwapi.start();
	} 
	public void connected() {
		bwapi.loadTypeData();
	}
	
	// Method called at the beginning of the game.
	public void gameStarted() {		
		System.out.println("Game Started");

		// allow me to manually control units during the game
		bwapi.enableUserInput();
		
		// set game speed to 30 (0 is the fastest. Tournament speed is 20)
		// You can also change the game speed from within the game by "/speed X" command.
		bwapi.setGameSpeed(30);
		
		// analyze the map
		bwapi.loadMapData(true);
		initialize();
		
		super.gameStarted();
	}
	
	public void initialize(){
		boss = new Boss(bwapi);
		OpeningManager openingManager = new OpeningManager(bwapi);
		
		addManager(boss);
		addManager(openingManager);
		addManager(new UnitProductionManager(bwapi,boss)); // azder
	}
	
	
	// Method called on every frame (approximately 30x every second).
	public void gameUpdate() {
		
		super.gameUpdate();
		
		// Draw debug information on screen
		drawDebugInfo();

	}
	
	// Draws debug information on the screen. 
	// Reimplement this function however you want. 
	public void drawDebugInfo() {

		// Draw our home position.
		bwapi.drawText(new Point(5,0), "Our home position: "+String.valueOf(homePositionX)+","+String.valueOf(homePositionY), true);
		
		// Draw circles over workers (blue if they're gathering minerals, green if gas, yellow if they're constructing).
		for (Unit u : bwapi.getMyUnits())  {
			if (u.isGatheringMinerals()) bwapi.drawCircle(u.getX(), u.getY(), 12, BWColor.BLUE, false, false);
			else if (u.isGatheringGas()) bwapi.drawCircle(u.getX(), u.getY(), 12, BWColor.GREEN, false, false);
		}
		
	}
	
}
