package javabot;

import java.awt.Point;

import javabot.macro.Boss;
import javabot.macro.UnitProductionManager;
import javabot.model.ChokePoint;
import javabot.model.Region;
import javabot.model.Unit;
import javabot.strategy.WallInModule;
import javabot.types.UnitType.UnitTypes;
import javabot.util.BWColor;
import javabot.util.Wall;

public class JavaBot extends AbstractManager {

	// Managers & Modules:
	private Boss boss;
	private WallInModule wallInModule;
	private UnitProductionManager unitProductionManager;
	
	private Region home; // Needed only for DEBUGGING (miso certikcy)

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
		bwapi.setGameSpeed(30);
		
		// analyze the map
		bwapi.loadMapData(true);
		initialize();
		
		super.gameStarted();
	}
	
	public void initialize(){
		
		// Create the managers
		boss = new Boss(bwapi);
		wallInModule = new WallInModule(bwapi);
		unitProductionManager = new UnitProductionManager(bwapi,boss); 
		
		// Add the managers
		addManager(boss);
		addManager(wallInModule);			// miso certicky
		addManager(unitProductionManager);	// azder
	}
	
	
	// Method called on every frame (approximately 30x every second).
	public void gameUpdate() {
		
		super.gameUpdate();
		
		// Draw debug information on screen
		drawDebugInfo();


		// START DEBUG: Compute the wall on frame 1 (miso certicky)
		if (bwapi.getFrameCount() == 1) {
			home = bwapi.getMap().getRegions().get(0);

			Unit homeNexus = bwapi.getMyUnits().get(0);
			Integer dist = 999999;
			Integer dist2;
			for (Unit u : bwapi.getMyUnits()) {
				if (u.getTypeID() == UnitTypes.Protoss_Nexus.ordinal()) {
					homeNexus = u;
					break;
				}
			}
			for (Region r : bwapi.getMap().getRegions()) {
				dist2 = Math.abs(r.getCenterX()-homeNexus.getX()) + Math.abs(r.getCenterY()-homeNexus.getY()); 
				if (dist2 < dist) {
					dist = dist2;
					home = r;
				}
			}

			for (ChokePoint c : home.getChokePoints()) {
				wallInModule.computeWall( c, home, UnitTypes.Zerg_Zergling.ordinal());
			}
		}
		// END DEBUG

	}
	
	// Draws debug information on the screen. 
	// Reimplement this function however you want. 
	public void drawDebugInfo() {

		// Draw our home position and chokes
		if (home != null) {
			bwapi.drawText(new Point(5,0), "Our home position: "+String.valueOf(home.getCenterX())+","+String.valueOf(home.getCenterY()), true);
			bwapi.drawText(new Point(5,14), "Map: "+bwapi.getMap().getName(), true);

			// chokepoints and center of the home Region
			bwapi.drawCircle(home.getCenterX(), home.getCenterY(), 15, BWColor.TEAL, true, false);
			for (ChokePoint c : home.getChokePoints()) {
				bwapi.drawLine(new Point(home.getCenterX() , home.getCenterY() ), new Point(c.getCenterX(),c.getCenterY()),BWColor.TEAL,false);
				bwapi.drawCircle(c.getCenterX(),c.getCenterY(), 10, BWColor.TEAL, true, false);
			}
		}

		for (int i=1; i < bwapi.getMap().getWidth(); i++) {
			for (int j=1; j < bwapi.getMap().getHeight(); j++) {
				if (wallInModule.obstructedByNeutrals(i, j))
					bwapi.drawCircle(i*32,j*32, 5, BWColor.GREEN, false, false);
			}
		}
		
		// Draw all previously computed walls
		for (Wall w : wallInModule.getAllWalls()) {
			for (Point bt : w.getBuildTiles()) {
				Integer tileWidth = bwapi.getUnitType(w.getBuildingTypeIds().get(w.getBuildTiles().indexOf(bt))).getTileWidth();
				Integer tileHeight = bwapi.getUnitType(w.getBuildingTypeIds().get(w.getBuildTiles().indexOf(bt))).getTileHeight();
				bwapi.drawBox(bt.x*32, bt.y*32, (bt.x + tileWidth)*32, (bt.y + tileHeight)*32, BWColor.YELLOW, false, false);
				bwapi.drawText(new Point(bt.x*32+4, bt.y*32+2), bwapi.getUnitType(w.getBuildingTypeIds().get(w.getBuildTiles().indexOf(bt))).getName()+" "+String.valueOf(bt.x)+","+String.valueOf(bt.y), false);
			}
		}
		
	}
	
}
