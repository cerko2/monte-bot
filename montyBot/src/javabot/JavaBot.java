package javabot;

import java.awt.Point;

import javabot.macro.Boss;
import javabot.macro.BuildManager;
import javabot.macro.UnitProductionManager;
import javabot.model.BaseLocation;
import javabot.model.ChokePoint;
import javabot.model.Region;
import javabot.model.Unit;
import javabot.strategy.OpeningManager;
import javabot.strategy.WallInModule;
import javabot.types.UnitType.UnitTypes;
import javabot.util.BWColor;
import javabot.util.Wall;

public class JavaBot extends AbstractManager {
	
	private static final boolean STATIC_UNIT_DEBUG = false;
	private static final boolean BASELOC_RESOURCES_DEBUG = false;

	// Managers & Modules:
	private Boss boss;

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
		
		// Add the managers
		addManager(boss);
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

		//Static unit debugging
		if (STATIC_UNIT_DEBUG){
			for (Unit unit : bwapi.getAllStaticNeutralUnits()){
				int tileWidth = bwapi.getUnitType(unit.getTypeID()).getTileWidth();
				int tileHeight = bwapi.getUnitType(unit.getTypeID()).getTileHeight();
				bwapi.drawBox(unit.getTileX()*32, unit.getTileY()*32, (unit.getTileX() + tileWidth)*32, (unit.getTileY() + tileHeight)*32, BWColor.GREY, false, false);
			}

			for (Unit unit : bwapi.getAllStaticMinerals()){
				int tileWidth = bwapi.getUnitType(unit.getTypeID()).getTileWidth();
				int tileHeight = bwapi.getUnitType(unit.getTypeID()).getTileHeight();
				bwapi.drawBox(unit.getTileX()*32, unit.getTileY()*32, (unit.getTileX() + tileWidth)*32, (unit.getTileY() + tileHeight)*32, BWColor.CYAN, false, false);
			}

			for (Unit unit : bwapi.getAllStaticGeysers()){
				int tileWidth = bwapi.getUnitType(unit.getTypeID()).getTileWidth();
				int tileHeight = bwapi.getUnitType(unit.getTypeID()).getTileHeight();
				bwapi.drawBox(unit.getTileX()*32, unit.getTileY()*32, (unit.getTileX() + tileWidth)*32, (unit.getTileY() + tileHeight)*32, BWColor.GREEN, false, false);
			}
		}
		
		if (BASELOC_RESOURCES_DEBUG){
			for (BaseLocation base : bwapi.getMap().getBaseLocations()){
				for (Unit unit : base.getStaticMinerals()){
					bwapi.drawText(unit.getX(), unit.getY(), unit.getResources() + "", false);
					bwapi.drawLine(base.getX(), base.getY(), unit.getX(), unit.getY(), BWColor.CYAN, false);
				}
				
				for (Unit unit : base.getGeysers()){
					bwapi.drawText(unit.getX(), unit.getY(), unit.getResources() + "", false);
					bwapi.drawLine(base.getX(), base.getY(), unit.getX(), unit.getY(), BWColor.GREEN, false);
				}
			}
		}
		
	}
	
}
