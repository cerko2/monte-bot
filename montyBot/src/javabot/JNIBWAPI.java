package javabot;

import javabot.model.*;
import javabot.types.*;
import javabot.types.UnitType.UnitTypes;
import javabot.util.UnitUtils;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * JNI interface for the Brood War API.
 * <p/>
 * This focus of this interface is to provide the callback and game state query
 * functionality in BWAPI. Utility functions such as can buildHere have not
 * yet been implemented.
 * <p/>
 * Note: for thread safety and game state sanity, all native calls should be invoked from the callback methods.
 * <p/>
 * For BWAPI documentation see: http://code.google.com/p/bwapi/
 * <p/>
 * API Pages
 * Game: http://code.google.com/p/bwapi/wiki/Game
 * Unit: http://code.google.com/p/bwapi/wiki/Unit
 */
public class JNIBWAPI {

    // load the BWAPI client library
    static {
    	try {
            System.loadLibrary("client-bridge-x86"); // HACK. Original was: System.loadLibrary("client-bridge-" + System.getProperty("os.arch"));
        } catch (UnsatisfiedLinkError e) {
          System.err.println("Native code library failed to load.\n" + e);
        }
    }

    /**
     * callback listener for BWAPI events
     */
    private BWAPIEventListener listener;

    // game state
    private int gameFrame = 0;
    private Map map;
    private HashMap<Integer, Unit> units = new HashMap<Integer, Unit>();
    private ArrayList<Unit> playerUnits = new ArrayList<Unit>();
    private ArrayList<Unit> alliedUnits = new ArrayList<Unit>();
    private ArrayList<Unit> enemyUnits = new ArrayList<Unit>();
    private ArrayList<Unit> neutralUnits = new ArrayList<Unit>();
    
    private HashMap<Integer, Unit> staticNeutralUnits = new HashMap<Integer, Unit>();
    private HashMap<Integer, Unit> staticMinerals = new HashMap<Integer, Unit>();
    private HashMap<Integer, Unit> staticGeysers = new HashMap<Integer, Unit>();

    // player lists
    private Player self;
    private HashSet<Integer> allyIDs = new HashSet<Integer>();
    private HashSet<Integer> enemyIDs = new HashSet<Integer>();
    private HashMap<Integer, Player> players = new HashMap<Integer, Player>();
    private ArrayList<Player> allies = new ArrayList<Player>();
    private ArrayList<Player> enemies = new ArrayList<Player>();

    // invokes the main native method
    private native void startClient(JNIBWAPI jniBWAPI);

    // query methods
    private native int getGameFrame();

    private native int[] getPlayerInfo();

    private native int[] getPlayerUpdate(int playerID);
    
    private native int[] getResearchStatus(int playerID);

    private native int[] getUpgradeStatus(int playerID);

    private native int[] getUnits();
    
    private native int[] getUnitTypes();

    private native String getUnitTypeName(int typeID);

    private native int[] getTechTypes();

    private native String getTechTypeName(int techID);

    private native int[] getUpgradeTypes();

    private native String getUpgradeTypeName(int upgradeID);

    private native int[] getWeaponTypes();

    private native String getWeaponTypeName(int weaponID);

    private native int[] getUnitSizeTypes();

    private native String getUnitSizeTypeName(int sizeID);

    private native int[] getBulletTypes();

    private native String getBulletTypeName(int bulletID);

    private native int[] getDamageTypes();

    private native String getDamageTypeName(int damageID);

    private native int[] getExplosionTypes();

    private native String getExplosionTypeName(int explosionID);

    private native int[] getUnitCommandTypes();

    private native String getUnitCommandTypeName(int unitCommandID);

    private native int[] getOrderTypes();

    private native String getOrderTypeName(int unitCommandID);

    // map data
    private native void analyzeTerrain();

    private native int getMapWidth();

    private native int getMapHeight();

    private native String getMapName();

    private native String getMapHash();

    private native int[] getHeightData();

    private native int[] getWalkableData();

    private native int[] getBuildableData();

    private native int[] getChokePoints();

    private native int[] getRegions();

    private native int[] getPolygon(int regionID);

    private native int[] getBaseLocations();

    // unit commands: http://code.google.com/p/bwapi/wiki/Unit
    
	/**
	 * <h2>attack</h2>
	 * 
	 * <p>
	 * Orders the unit to attack move to a specified location <code>[x, y]</code>
	 * </p>
     * 
     * @param unitID - ID of attacking unit
     * @param x - x-coordinate of the specified location
     * @param y - y-coordinate of the specified location
     */
    public native void attack(int unitID, int x, int y);

	/**
	 * <h2>attack</h2>
	 * 
	 * <p>
	 * Orders the unit to attack a single unit.
	 * </p>
     * 
     * @param unitID - ID of the attacking unit
     * @param targetID - ID of the target unit
     */
    public native void attack(int unitID, int targetID);

	/**
	 * <h2>build</h2>
	 * 
	 * <p>
	 * Orders the unit to build the given unit type at the given position. Note that if the player 
	 * does not have enough resources when the unit attempts to place the building down, the order 
	 * will fail. The tile position specifies where the top left corner of the building will be placed.
	 * </p>
     * 
     * @param unitID - ID of the unit used for this build
     * @param tx - x-coordinate of the building tile position
     * @param ty - y-coordinate of the building tile position
     * @param typeID - ID of the unit being built by building unit
     */
    public native void build(int unitID, int tx, int ty, int typeID);

	/**
	 * <h2>buildAddon</h2>
	 * 
	 * <p>
	 * Orders the unit to build the given add-on. The unit must be a Terran building that can have 
	 * an add-on and the specified unit type must be an add-on unit type. The build() command can also 
	 * be used to build add-ons.
	 * </p>
     * 
     * @param unitID - ID of the Terran building that can have the add-on
     * @param typeID - ID of the add-on
     */
    public native void buildAddon(int unitID, int typeID);

	/**
	 * <h2>train</h2>
	 * 
	 * <p>
	 * Orders the unit to add the specified unit type to the training queue. Note that the player must have 
	 * sufficient resources to train. If you wish to make units from a hatchery, use getLarva to get the larva 
	 * associated with the hatchery and then call morph on the larva you want to morph. This command can also 
	 * be used to make interceptors and scarabs.
	 * </p>
     * 
     * @param unitID - ID of the building that is possible to train unit of type <code>typeID</code>
     * @param typeID - ID of the unit that will be trained 
     */
    public native void train(int unitID, int typeID);

	/**
	 * <h2>morph</h2>
	 * 
	 * <p>
	 * Orders the unit to morph into the specified unit type.
	 * </p>
     * 
     * @param unitID - ID of the unit to be morphed
     * @param typeID - ID of the unit type in which will be unit morphed
     */
    public native void morph(int unitID, int typeID);

	/**
	 * <h2>research</h2>
	 * 
	 * <p>
	 * Orders the unit to research the given tech type.
	 * </p>
     * 
     * @param unitID - ID of the building that will be researching
     * @param techID - ID of the researched tech type
     */
    public native void research(int unitID, int techID);

	/**
	 * <h2>upgrade</h2>
	 * 
	 * <p>
	 * Orders the unit to upgrade the given upgrade type.
	 * </p>
     * 
     * @param unitID - ID of the building that will be upgrading
     * @param updateID - ID of the upgrade type
     */
    public native void upgrade(int unitID, int updateID);

	/**
	 * <h2>setRallyPoint</h2>
	 * 
	 * <p>
	 * Orders the unit to set its rally position to the specified position.
	 * </p>
     * 
     * @param unitID - ID of the unit to be set its rally position
     * @param x - x-coordinate of the specified position
     * @param y - y-coordinate of the specified position
     */
    public native void setRallyPoint(int unitID, int x, int y);

	/**
	 * <h2>setRallyPoint</h2>
	 * 
	 * <p>
	 * Orders the unit to set its rally position to the specified unit.
	 * </p>
     * 
     * @param unitID - ID of the unit to be set its rally position
     * @param targetID - ID of the specified unit
     */
    public native void setRallyPoint(int unitID, int targetID);

	/**
	 * <h2>move</h2>
	 * 
	 * <p>
	 * Orders the unit to move to the specified position.
	 * </p>
     * 
     * @param unitID - ID of the unit to be moved
     * @param x - x-coordinate of the specified position
     * @param y - y-coordinate of the specified position
     */
    public native void move(int unitID, int x, int y);

	/**
	 * <h2>patrol</h2>
	 * 
	 * <p>
	 * Orders the unit to patrol between its current position and the specified position.
	 * </p>
     * 
     * @param unitID - ID of the patrol unit
     * @param x - x-coordinate of the specified position
     * @param y - y-coordinate of the specified position
     */
    public native void patrol(int unitID, int x, int y);

	/**
	 * <h2>holdPosition</h2>
	 * 
	 * <p>
	 * Orders the unit to hold its position. <br />
	 * Note: Reavers and Carriers can only hold position if they have at least one Scarab or 
	 * Interceptor.
	 * </p>
     * 
     * @param unitID - ID of the unit that will hold its position
     */
    public native void holdPosition(int unitID);

	/**
	 * <h2>stop</h2>
	 * 
	 * <p>
	 * Orders the unit to stop.
	 * </p>
     * 
     * @param unitID - ID of the unit that will be stopped
     */
    public native void stop(int unitID);

	/**
	 * <h2>follow</h2>
	 * 
	 * <p>
	 * Orders the unit to follow the specified unit.
	 * </p>
     * 
     * @param unitID - ID of the unit that will follow
     * @param targetID - ID of the unit that will be followed
     */
    public native void follow(int unitID, int targetID);

	/**
	 * <h2>gather</h2>
	 * 
	 * <p>
	 * Orders the unit to gather the specified unit. Only workers can be ordered to gather, and the target 
	 * must be a mineral patch, Refinery, Assimilator, or Extractor.
	 * </p>
     * 
     * @param unitID - ID of the unit that will be gathering
     * @param targetID - ID of the specified unit
     */
    public native void gather(int unitID, int targetID);

	/**
	 * <h2>returnCargo</h2>
	 * 
	 * <p>
	 * Orders the unit to return its cargo to a nearby resource depot such as a Command Center. 
	 * Only workers that are carrying minerals or gas can be ordered to return cargo.
	 * </p>
     * 
     * @param unitID - ID of the unit that will return its cargo
     */
    public native void returnCargo(int unitID);

	/**
	 * <h2>repair</h2>
	 * 
	 * <p>
	 * Orders the unit to repair the specified unit. Only Terran SCVs can be ordered to repair, 
	 * and the target must be a mechanical Terran unit or building.
	 * </p>
     * 
     * @param unitID - ID of the unit that will repair the specified unit
     * @param targetID - ID of the specified unit need to be repaired
     */
    public native void repair(int unitID, int targetID);

	/**
	 * <h2>burrow</h2>
	 * 
	 * <p>
	 * Orders the unit to burrow. Either the unit must be a Zerg Lurker, or the unit must be a Zerg 
	 * ground unit and burrow tech must be researched.
	 * </p>
     * 
     * @param unitID - ID of the unit that will burrow
     */
    public native void burrow(int unitID);

	/**
	 * <h2>unburrow</h2>
	 * 
	 * <p>
	 * Orders the burrowed unit to unburrow.
	 * </p>
     * 
     * @param unitID - ID of the unit that will unburrow
     */
    public native void unburrow(int unitID);

	/**
	 * <h2>cloak</h2>
	 * 
	 * <p>
	 * Orders the unit to cloak.
	 * </p>
     * 
     * @param unitID - ID of the unit that will be cloaked
     */
    public native void cloak(int unitID);

	/**
	 * <h2>decloak</h2>
	 * 
	 * <p>
	 * Orders the unit to decloak.
	 * </p>
     * 
     * @param unitID - ID of the unit that will be decloaked
     */
    public native void decloak(int unitID);

	/**
	 * <h2>siege</h2>
	 * 
	 * <p>
	 * Orders the unit to siege. <br />
	 * Note: unit must be a Terran siege tank.
	 * </p>
     * 
     * @param unitID - ID of the siege tank that will siege
     */
    public native void siege(int unitID);

	/**
	 * <h2>unsiege</h2>
	 * 
	 * <p>
	 * Orders the unit to unsiege. <br />
	 * Note: unit must be a Terran siege tank.
	 * </p>
     * 
     * @param unitID - ID of the siege tank that will stop siege
     */
    public native void unsiege(int unitID);

	/**
	 * <h2>lift</h2>
	 * 
	 * <p>
	 * Orders the unit to lift. <br />
	 * Note: unit must be a Terran building that can be lifted.
	 * </p>
     * 
     * @param unitID - ID of the building that will be lifted
     */
    public native void lift(int unitID);

	/**
	 * <h2>land</h2>
	 * 
	 * <p>
	 * Orders the unit to land. <br />
	 * Note: unit must be a Terran building that is currently lifted.
	 * </p>
     * 
     * @param unitID - ID of the building that will be landed
     * @param tx - x-coordinate of the landed tile position
     * @param ty - y-coordinate of the landed tile position
     */
    public native void land(int unitID, int tx, int ty);

	/**
	 * <h2>load</h2>
	 * 
	 * <p>
	 * Orders the unit to load the target unit.
	 * </p>
     * 
     * @param unitID - ID of the unit that will load the target unit
     * @param targetID - ID of the target unit
     */
    public native void load(int unitID, int targetID);

	/**
	 * <h2>unload</h2>
	 * 
	 * <p>
	 * Orders the unit to unload the target unit.
	 * </p>
     * 
     * @param unitID - ID of the unit that will unload the target unit
     * @param targetID - ID of the target unit
     */
    public native void unload(int unitID, int targetID);

	/**
	 * <h2>unloadAll</h2>
	 * 
	 * <p>
	 * Orders the unit to unload all loaded units at the unit's current position.
	 * </p>
     * 
     * @param unitID - ID of the unit that will unload all loaded units
     */
    public native void unloadAll(int unitID);

	/**
	 * <h2>unloadAll</h2>
	 * 
	 * <p>
	 * Orders the unit to unload all loaded units at the specified location. Unit should be a 
	 * Terran Dropship, Protoss Shuttle, or Zerg Overlord. If the unit is a Terran Bunker, the units 
	 * will be unloaded right outside the bunker, like in the first version of unloadAll.
	 * </p>
     * 
     * @param unitID - ID of the unit that will unload all loaded units
     * @param x - x-coordinate of the specified location
     * @param y - y-coordinate of the specified location
     */
    public native void unloadAll(int unitID, int x, int y);

	/**
	 * <h2>rightClick</h2>
	 * 
	 * <p>
	 * Works like the right click in the GUI. Right click on a mineral patch to order a worker to mine, 
	 * right click on an enemy to attack it.
	 * </p>
     * 
     * @param unitID - ID of the selected unit
     * @param x - x-coordinate of the clicked position
     * @param y - y-coordinate of the clicked position
     */
    public native void rightClick(int unitID, int x, int y);

	/**
	 * <h2>rightClick</h2>
	 * 
	 * <p>
	 * Works like the right click in the GUI. Right click on a mineral patch to order a worker to mine, 
	 * right click on an enemy to attack it.
	 * </p>
     * 
     * @param unitID - ID of the selected unit
     * @param targetID - ID of the clicked unit
     */
    public native void rightClick(int unitID, int targetID);

	/**
	 * <h2>haltConstruction</h2>
	 * 
	 * <p>
	 * Orders the SCV to stop constructing the building, and the building is left in a partially 
	 * complete state until it is canceled, destroyed, or completed.
	 * </p>
     * 
     * @param unitID - ID of the SCV that will hold construction
     */
    public native void haltConstruction(int unitID);

	/**
	 * <h2>cancelConstruction</h2>
	 * 
	 * <p>
	 * Orders the building to stop being constructed.
	 * </p>
     * 
     * @param unitID - ID of the building
     */
    public native void cancelConstruction(int unitID);

	/**
	 * <h2>cancelAddon</h2>
	 * 
	 * <p>
	 * Orders the unit to stop making the addon.
	 * </p>
     * 
     * @param unitID - ID of the unit that will stop making the addon
     */
    public native void cancelAddon(int unitID);

	/**
	 * <h2>cancelTrain</h2>
	 * 
	 * <p>
	 * Orders the unit to remove the specified unit from its training queue. Leaving the default 
	 * value will remove the last unit from the training queue.
	 * </p>
     * 
     * @param unitID - ID of the unit that will remove the specified unit
     * @param slot - slot that will be removed from training queue
     */
    public native void cancelTrain(int unitID, int slot);

	/**
	 * <h2>cancelMorph</h2>
	 * 
	 * <p>
	 * Orders the unit to stop morphing.
	 * </p>
     * 
     * @param unitID - ID of the unit that will stop morphing
     */
    public native void cancelMorph(int unitID);

	/**
	 * <h2>cancelResearch</h2>
	 * 
	 * <p>
	 * Orders the unit to cancel a research in progress.
	 * </p>
     * 
     * @param unitID - ID of the unit that will cancel research
     */
    public native void cancelResearch(int unitID);

	/**
	 * <h2>cancelUpgrade</h2>
	 * 
	 * <p>
	 * Orders the unit to cancel an upgrade in progress.
	 * </p>
     * 
     * @param unitID - ID of the unit that will cancel the upgrade
     */
    public native void cancelUpgrade(int unitID);

	/**
	 * <h2>useTech</h2>
	 * 
	 * <p>
	 * Orders the unit to use a technology either not requiring a target (i.e. Stim Pack), a target 
	 * position (i.e. Spider Mines), or a target unit (i.e. Irradiate).
	 * </p>
     * 
     * @param unitID - ID of the unit that will use certain technology
     * @param typeID - ID of the useTech type
     */
    public native void useTech(int unitID, int typeID);

	/**
	 * <h2>useTech</h2>
	 * 
	 * <p>
	 * Orders the unit to use a technology either not requiring a target (i.e. Stim Pack), a target 
	 * position (i.e. Spider Mines), or a target unit (i.e. Irradiate).
	 * </p>
     * 
     * @param unitID - ID of the unit that will use certain technology
     * @param typeID - ID of the useTech type
     * @param x - x-coordinate of the target position
     * @param y - y-coordinate of the target position
     */
    public native void useTech(int unitID, int typeID, int x, int y);

	/**
	 * <h2>useTech</h2>
	 * 
	 * <p>
	 * Orders the unit to use a technology either not requiring a target (i.e. Stim Pack), a target 
	 * position (i.e. Spider Mines), or a target unit (i.e. Irradiate).
	 * </p>
     * 
     * @param unitID - ID of the unit that will use certain technology
     * @param typeID - ID of the useTech type
     * @param targetID - ID of the target unit
     */
    public native void useTech(int unitID, int typeID, int targetID);

    // utility commands
    public native void drawHealth(boolean enable);

    public native void drawTargets(boolean enable);

    public native void drawIDs(boolean enable);

    public native void enableUserInput();

    public native void enablePerfectInformation();

    public native void setGameSpeed(int speed);

    // draw commands
    public native void drawBox(int left, int top, int right, int bottom, int color, boolean fill, boolean screenCoords);

    public native void drawCircle(int x, int y, int radius, int color, boolean fill, boolean screenCoords);

    public native void drawLine(int x1, int y1, int x2, int y2, int color, boolean screenCoords);

    public void drawLine(Point a, Point b, int color, boolean screenCoords) {
        drawLine(a.x, a.y, b.x, b.y, color, screenCoords);
    }

    public native void drawDot(int x, int y, int color, boolean screenCoords);

    public native void drawText(int x, int y, String msg, boolean screenCoords);

    public void drawText(Point a, String msg, boolean screenCoords) {
        drawText(a.x, a.y, msg, screenCoords);
    }

    // Extended Commands (Fobbah)
    public native boolean hasCreep(int tx, int ty);

    public native boolean canBuildHere(int unitID, int tx, int ty, int utypeID, boolean checkExplored);

    public native void printText(String message);

    public native void sendText(String message);

    public native void setCommandOptimizationLevel(int level);

    private native boolean isReplay();
    
    // type data
    private HashMap<Integer, UnitType> unitTypes = new HashMap<Integer, UnitType>();
    private HashMap<Integer, TechType> techTypes = new HashMap<Integer, TechType>();
    private HashMap<Integer, UpgradeType> upgradeTypes = new HashMap<Integer, UpgradeType>();
    private HashMap<Integer, WeaponType> weaponTypes = new HashMap<Integer, WeaponType>();
    private HashMap<Integer, UnitSizeType> unitSizeTypes = new HashMap<Integer, UnitSizeType>();
    private HashMap<Integer, BulletType> bulletTypes = new HashMap<Integer, BulletType>();
    private HashMap<Integer, DamageType> damageTypes = new HashMap<Integer, DamageType>();
    private HashMap<Integer, ExplosionType> explosionTypes = new HashMap<Integer, ExplosionType>();
    private HashMap<Integer, UnitCommandType> unitCommandTypes = new HashMap<Integer, UnitCommandType>();
    private HashMap<Integer, OrderType> orderTypes = new HashMap<Integer, OrderType>();

    // type data accessors
    public UnitType getUnitType(int unitID) {
        return unitTypes.get(unitID);
    }

    public TechType getTechType(int techID) {
        return techTypes.get(techID);
    }

    public UpgradeType getUpgradeType(int upgradeID) {
        return upgradeTypes.get(upgradeID);
    }

    public WeaponType getWeaponType(int weaponID) {
        return weaponTypes.get(weaponID);
    }

    public UnitSizeType getUnitSizeType(int sizeID) {
        return unitSizeTypes.get(sizeID);
    }

    public BulletType getBulletType(int bulletID) {
        return bulletTypes.get(bulletID);
    }

    public DamageType getDamageType(int damageID) {
        return damageTypes.get(damageID);
    }

    public ExplosionType getExplosionType(int explosionID) {
        return explosionTypes.get(explosionID);
    }

    public UnitCommandType getUnitCommandType(int unitCommandID) {
        return unitCommandTypes.get(unitCommandID);
    }

    public OrderType getOrderType(int orderID) {
        return orderTypes.get(orderID);
    }

    public Collection<UnitType> unitTypes() {
        return unitTypes.values();
    }

    public Collection<TechType> techTypes() {
        return techTypes.values();
    }

    public Collection<UpgradeType> upgradeTypes() {
        return upgradeTypes.values();
    }

    public Collection<WeaponType> weaponTypes() {
        return weaponTypes.values();
    }

    public Collection<UnitSizeType> unitSizeTypes() {
        return unitSizeTypes.values();
    }

    public Collection<BulletType> bulletTypes() {
        return bulletTypes.values();
    }

    public Collection<DamageType> damageTypes() {
        return damageTypes.values();
    }

    public Collection<ExplosionType> explosionTypes() {
        return explosionTypes.values();
    }

    public Collection<UnitCommandType> unitCommandTypes() {
        return unitCommandTypes.values();
    }

    public Collection<OrderType> orderTypes() {
        return orderTypes.values();
    }

    //custom
    public native boolean isVisible(int tx, int ty);

    public native boolean isExplored(int tx, int ty);

    public native boolean isBuildable(int tx, int ty, boolean includeBuildings);

    public boolean isBuildable(int tx, int ty) {
        return isBuildable(tx, ty, false);
    }

    public native int getLastError();

    public native boolean hasLoadedUnit(int unitID1, int unitID2);

    public native int getRemainingLatencyFrames();
    
    // static unit commands (cerko)
    
    private native int[] getStaticNeutralUnits();
    
    private native int[] getStaticMinerals();
    
    private native int[] getStaticGeysers();

    private native String getPlayerName(int playerID);
    
    // game state accessors


    public int getFrameCount() {
        return gameFrame;
    }

    public Player getSelf() {
        return self;
    }

    public Player getPlayer(int playerID) {
        return players.get(playerID);
    }

    public ArrayList<Player> getAllies() {
        return allies;
    }

    public ArrayList<Player> getEnemies() {
        return enemies;
    }

    public Unit getUnit(int unitID) {
        return units.get(unitID);
    }

    public Collection<Unit> getAllUnits() {
        return units.values();
    }

    public ArrayList<Unit> getMyUnits() {
        return playerUnits;
    }

    public ArrayList<Unit> getAlliedUnits() {
        return alliedUnits;
    }

    public ArrayList<Unit> getEnemyUnits() {
        return enemyUnits;
    }

    public ArrayList<Unit> getNeutralUnits() {
        return neutralUnits;
    }
    
    public Collection<Unit> getAllStaticNeutralUnits(){
    	return staticNeutralUnits.values();
    }
    
    public Collection<Unit> getAllStaticMinerals(){
    	return staticMinerals.values();
    }
    
    public Collection<Unit> getAllStaticGeysers(){
    	return staticGeysers.values();
    }
    
    public Unit getStaticNeutralUnit(int unitID){
    	return staticNeutralUnits.get(unitID);
    }

    /**
     * Returns the map.
     * <p/>
     * Note: return null if loadMapData has not been called.
     */
    public Map getMap() {
        return map;
    }

    /**
     * Loads type data from BWAPI.
     */
    public void loadTypeData() {
        // unit types
        int[] unitTypeData = getUnitTypes();
        for (int index = 0; index < unitTypeData.length; index += UnitType.numAttributes) {
            UnitType type = new UnitType(unitTypeData, index);
            type.setName(getUnitTypeName(type.getID()));
            unitTypes.put(type.getID(), type);
        }

        // tech types
        int[] techTypeData = getTechTypes();
        for (int index = 0; index < techTypeData.length; index += TechType.numAttributes) {
            TechType type = new TechType(techTypeData, index);
            type.setName(getTechTypeName(type.getID()));
            techTypes.put(type.getID(), type);
        }

        // upgrade types
        int[] upgradeTypeData = getUpgradeTypes();
        for (int index = 0; index < upgradeTypeData.length; index += UpgradeType.numAttributes) {
            UpgradeType type = new UpgradeType(upgradeTypeData, index);
            type.setName(getUpgradeTypeName(type.getID()));
            upgradeTypes.put(type.getID(), type);
        }

        // weapon types
        int[] weaponTypeData = getWeaponTypes();
        for (int index = 0; index < weaponTypeData.length; index += WeaponType.numAttributes) {
            WeaponType type = new WeaponType(weaponTypeData, index);
            type.setName(getWeaponTypeName(type.getID()));
            weaponTypes.put(type.getID(), type);
        }

        // unit size types
        int[] unitSizeTypeData = getUnitSizeTypes();
        for (int index = 0; index < unitSizeTypeData.length; index += UnitSizeType.numAttributes) {
            UnitSizeType type = new UnitSizeType(unitSizeTypeData, index);
            type.setName(getUnitSizeTypeName(type.getID()));
            unitSizeTypes.put(type.getID(), type);
        }

        // bullet types
        int[] bulletTypeData = getBulletTypes();
        for (int index = 0; index < bulletTypeData.length; index += BulletType.numAttributes) {
            BulletType type = new BulletType(bulletTypeData, index);
            type.setName(getBulletTypeName(type.getID()));
            bulletTypes.put(type.getID(), type);
        }

        // damage types
        int[] damageTypeData = getDamageTypes();
        for (int index = 0; index < damageTypeData.length; index += DamageType.numAttributes) {
            DamageType type = new DamageType(damageTypeData, index);
            type.setName(getDamageTypeName(type.getID()));
            damageTypes.put(type.getID(), type);
        }

        // explosion types
        int[] explosionTypeData = getExplosionTypes();
        for (int index = 0; index < explosionTypeData.length; index += ExplosionType.numAttributes) {
            ExplosionType type = new ExplosionType(explosionTypeData, index);
            type.setName(getExplosionTypeName(type.getID()));
            explosionTypes.put(type.getID(), type);
        }

        // unitCommand types
        int[] unitCommandTypeData = getUnitCommandTypes();
        for (int index = 0; index < unitCommandTypeData.length; index += UnitCommandType.numAttributes) {
            UnitCommandType type = new UnitCommandType(unitCommandTypeData, index);
            type.setName(getUnitCommandTypeName(type.getID()));
            unitCommandTypes.put(type.getID(), type);
        }

        // order types
        int[] orderTypeData = getOrderTypes();
        for (int index = 0; index < orderTypeData.length; index += OrderType.numAttributes) {
            OrderType type = new OrderType(orderTypeData, index);
            type.setName(getOrderTypeName(type.getID()));
//			System.out.println("ID: "+ type.getID()+" Name: "+ type.getName());
            orderTypes.put(type.getID(), type);
        }
    }

    /**
     * Loads map data and BWTA data.
     * <p/>
     * TODO: figure out how to use BWTA's internal map storage
     */
    public void loadMapData(boolean enableBWTA) {
        map = new Map(getMapWidth(), getMapHeight(), getMapName(), getMapHash(), getHeightData(), getBuildableData(), getWalkableData());
        if (!enableBWTA) {
            return;
        }

        // get region and choke point data
        // need to save bwta files to different folders depending on race since static unit data seems to change depending on player race
        File bwtaFile = new File("BWTA-DATA/" + map.getHash() + ".bwta");
        boolean analyzed = bwtaFile.exists();
        int[] regionData = null;
        int[] chokePointData = null;
        int[] baseLocationData = null;
        HashMap<Integer, int[]> polygons = new HashMap<Integer, int[]>();

        // run BWTA
        if (!analyzed) {
            analyzeTerrain();
            regionData = getRegions();
            chokePointData = getChokePoints();
            baseLocationData = getBaseLocations();
            for (int index = 0; index < regionData.length; index += Region.numAttributes) {
                int id = regionData[index];
                polygons.put(id, getPolygon(id));
            }

            // store the results to a local file (bwta directory)
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(bwtaFile));

                // regions
                boolean first = true;
                for (int val : regionData) {
                    if (first) {
                        first = false;
                        writer.write("" + val);
                    } else {
                        writer.write("," + val);
                    }
                }
                writer.write("\n");

                // chokes
                first = true;
                for (int val : chokePointData) {
                    if (first) {
                        first = false;
                        writer.write("" + val);
                    } else {
                        writer.write("," + val);
                    }
                }
                writer.write("\n");

                // baseLocations
                first = true;
                for (int val : baseLocationData) {
                    if (first) {
                        first = false;
                        writer.write("" + val);
                    } else {
                        writer.write("," + val);
                    }
                }
                writer.write("\n");

                // polygons
                for (int id : polygons.keySet()) {
                    writer.write("" + id);
                    for (int val : polygons.get(id)) {
                        writer.write("," + val);
                    }
                    writer.write("\n");
                }

                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // load from file
        else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(bwtaFile));

                // regions
                String[] regions = reader.readLine().split(",");
                regionData = new int[regions.length];
                for (int i = 0; i < regions.length; i++) {
                    regionData[i] = Integer.parseInt(regions[i]);
                }

                // choke points
                String[] chokePoints = reader.readLine().split(",");
                if (chokePoints.length > 0 && !chokePoints[0].equals("")) {
                    chokePointData = new int[chokePoints.length];
                    for (int i = 0; i < chokePoints.length; i++) {
                        chokePointData[i] = Integer.parseInt(chokePoints[i]);
                    }
                } else {
                    chokePointData = new int[0];
                }

                // base locations
                String[] baseLocations = reader.readLine().split(",");
                if (baseLocations.length > 0 && !baseLocations[0].equals("")) {
                    baseLocationData = new int[baseLocations.length];
                    for (int i = 0; i < baseLocations.length; i++) {
                        baseLocationData[i] = Integer.parseInt(baseLocations[i]);
                    }
                }

                // polygons (first integer is ID)
                String line = reader.readLine();
                while (line != null) {
                    String[] coordinates = line.split(",");
                    int[] coordinateData = new int[coordinates.length - 1];

                    for (int i = 1; i < coordinates.length; i++) {
                        coordinateData[i - 1] = Integer.parseInt(coordinates[i]);
                    }

                    polygons.put(Integer.parseInt(coordinates[0]), coordinateData);
                    line = reader.readLine();
                }

                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // regions
        HashMap<Integer, Region> regionMap = new HashMap<Integer, Region>();
        for (int index = 0; index < regionData.length; index += Region.numAttributes) {
            Region region = new Region(regionData, index);
            region.setCoordinates(polygons.get(region.getID()));
            map.getRegions().add(region);
            regionMap.put(region.getID(), region);
        }

        // choke points
        if (chokePointData != null) {
            for (int index = 0; index < chokePointData.length; index += ChokePoint.numAttributes) {
                ChokePoint chokePoint = new ChokePoint(chokePointData, index);
                chokePoint.setFirstRegion(regionMap.get(chokePoint.getFirstRegionID()));
                chokePoint.setSecondRegion(regionMap.get(chokePoint.getSecondRegionID()));
                map.getChokePoints().add(chokePoint);
            }
        }
        // base locations
        if (baseLocationData != null) {
            for (int index=0; index<baseLocationData.length; index+=BaseLocation.numAttributes) {
                    BaseLocation baseLocation = new BaseLocation(baseLocationData, index);
                    map.getBaseLocations().add(baseLocation);
            }
        }

        //since ids were constantly changing between loads of saved files had to use euclidean distance for resource mapping onto baseLocs
        for (BaseLocation base : map.getBaseLocations()){
        	ArrayList<Unit> minerals = new ArrayList<Unit>();
        	ArrayList<Unit> geysers = new ArrayList<Unit>();
        	for (Unit unit : staticMinerals.values()){
        		if (UnitUtils.getDistance(unit.getX(), unit.getY(), base.getTx() * 32, base.getTy() * 32) < 12 * 32){
        			minerals.add(unit);
        		}
        	}
        	for (Unit unit : staticGeysers.values()){
        		if (UnitUtils.getDistance(unit.getX(), unit.getY(), base.getTx() * 32, base.getTy() * 32) < 12 * 32){
        			geysers.add(unit);
        		}
        	}
        	base.setStaticMinerals(minerals);
        	base.setGeysers(geysers);
        }

        // connect the region graph
        for (Region region : map.getRegions()) {
            for (ChokePoint chokePoint : map.getChokePoints()) {
                if (chokePoint.getFirstRegion().equals(region) || chokePoint.getSecondRegion().equals(region)) {
                    region.getChokePoints().add(chokePoint);
                    region.getConnectedRegions().add(chokePoint.getOtherRegion(region));
                }
            }
        }
    }

    /**
     * Instantiates a BWAPI instance, but does not connect to the bridge. To
     * connect, the start method must be invokeed.
     *
     * @param listener - listener for BWAPI callback events.
     */
    public JNIBWAPI(BWAPIEventListener listener) {
        this.listener = listener;
    }

    /**
     * Invokes the native library which will connect to the bridge and then invoke
     * callback functions.
     * <p/>
     * Note: this method never returns, it should be invoked from a separate
     * thread if concurrent java processing is needed.
     */
    public void start() {
        startClient(this);
    }

    /**
     * C++ callback function.
     * <p/>
     * Utility function for printing to the java console from C++.
     */
    public void javaPrint(String msg) {
        try {
            System.out.println("Bridge: " + msg);
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    /**
     * C++ callback function.
     * <p/>
     * Notifies the event listener that a connection has been formed to the bridge.
     */
    public void connected() {
        try {
            listener.connected();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    /**
     * C++ callback function.
     * <p/>
     * Notifies the event listener that a game has started.
     */
    public void gameStarted() {
        try {

            // get the players
            allies.clear();
            allyIDs.clear();
            enemies.clear();
            enemyIDs.clear();
            players.clear();

            int[] playerData = getPlayerInfo();
            for (int index = 0; index < playerData.length; index += Player.numAttributes) {
                Player player = new Player(playerData, index, getPlayerName(playerData[index]));
                players.put(player.getID(), player);

                if (player.isSelf()) {
                    self = player;
                } else if (player.isAlly()) {
                    allies.add(player);
                    allyIDs.add(player.getID());
                } else if (player.isEnemy()) {
                    enemies.add(player);
                    enemyIDs.add(player.getID());
                }
            }

            // get unit data
            units.clear();
            playerUnits.clear();
            alliedUnits.clear();
            enemyUnits.clear();
            neutralUnits.clear();
            
            int[] unitData = getUnits();

            for (int index = 0; index < unitData.length; index += Unit.numAttributes) {
                int id = unitData[index];
                Unit unit = new Unit(id);
                unit.update(unitData, index);

                units.put(id, unit);
                if (self != null) {
                    if (unit.getPlayerID() == self.getID()) {
                        playerUnits.add(unit);
                    } else if (allyIDs.contains(unit.getPlayerID())) {
                        alliedUnits.add(unit);
                    } else if (enemyIDs.contains(unit.getPlayerID())) {
                        enemyUnits.add(unit);
                    } else {
                        neutralUnits.add(unit);
                    }
                } else if (allyIDs.contains(unit.getPlayerID())) {
                    alliedUnits.add(unit);
                } else if (enemyIDs.contains(unit.getPlayerID())) {
                    enemyUnits.add(unit);
                } else {
                    neutralUnits.add(unit);
                }
                
            }
            
            loadStaticUnits();

            listener.gameStarted();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    /**
     * C++ callback function.
     * <p/>
     * Notifies the event listener that a game update occurred.
     */
    private void gameUpdate() {
        try {

            // update game state
            gameFrame = getGameFrame();
            if (!isReplay()) {
                self.update(getPlayerUpdate(self.getID()));
                self.updateResearch(getResearchStatus(self.getID()), getUpgradeStatus(self.getID()));
            } else {
                for (Integer playerID : players.keySet()) {
                    players.get(playerID).update(getPlayerUpdate(playerID));
                    players.get(playerID).updateResearch(getResearchStatus(playerID), getUpgradeStatus(playerID));
                }
            }
            // update units
            int[] unitData = getUnits();
            HashSet<Integer> deadUnits = new HashSet<Integer>(units.keySet());
            ArrayList<Unit> playerList = new ArrayList<Unit>();
            ArrayList<Unit> alliedList = new ArrayList<Unit>();
            ArrayList<Unit> enemyList = new ArrayList<Unit>();
            ArrayList<Unit> neutralList = new ArrayList<Unit>();

            for (int index = 0; index < unitData.length; index += Unit.numAttributes) {
                int id = unitData[index];

                // bugfix - unit list was emptying itself every second frame
                deadUnits.remove(id);

                Unit unit = units.get(id);
                if (unit == null) {
                    unit = new Unit(id);
                    units.put(id, unit);
                }

                unit.update(unitData, index);

                if (self != null) {
                    if (unit.getPlayerID() == self.getID()) {
                        playerList.add(unit);
                    } else if (allyIDs.contains(unit.getPlayerID())) {
                        alliedList.add(unit);
                    } else if (enemyIDs.contains(unit.getPlayerID())) {
                        enemyList.add(unit);
                    } else {
                        neutralList.add(unit);
                        if (staticNeutralUnits.containsKey(id)){
                        	staticNeutralUnits.get(id).update(unitData, index);
                        }
                    }
                } else if (allyIDs.contains(unit.getPlayerID())) {
                    alliedList.add(unit);
                } else if (enemyIDs.contains(unit.getPlayerID())) {
                    enemyList.add(unit);
                } else {
                    neutralList.add(unit);
                    if (staticNeutralUnits.containsKey(id)){
                    	staticNeutralUnits.get(id).update(unitData, index);
                    }
                }
                
                if (getUnitType(unit.getTypeID()).isResourceContainer() && staticNeutralUnits.containsKey(id)){
                    staticNeutralUnits.get(id).update(unitData, index);
                }
            }

            // update the unit lists
            playerUnits = playerList;
            alliedUnits = alliedList;
            enemyUnits = enemyList;
            neutralUnits = neutralList;
            for (Integer unitID : deadUnits) {
                units.get(unitID).setDestroyed();
                units.remove(unitID);
            }
            
            listener.gameUpdate();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    /**
     * C++ callback function.
     * <p/>
     * Notifies the event listener that the game has terminated.
     * <p/>
     * Note: this is always called after matchEnded(bool), and is meant as a
     * way of notifying the AI client to clear up state.
     */
    private void gameEnded() {
        try {
            listener.gameEnded();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    /**
     * C++ callback function.
     * <p/>
     * Sends BWAPI callback events to the event listener.
     *
     * @param type - event type (should probably be an enum)
     */
    private void eventOccured(int type, int param1, int param2) {
        try {
            switch (type) {
                case 0:
                    listener.matchEnded(param1 == 1);
                    break;
                case 1:
                    listener.playerLeft(param1);
                    break;
                case 2:
                    listener.nukeDetect(param2, param2);
                    break;
                case 3:
                    listener.nukeDetect();
                    break;
                case 4:
                    listener.unitDiscover(param1);
                    break;
                case 5:
                    listener.unitEvade(param1);
                    break;
                case 6:
                    listener.unitShow(param1);
                    break;
                case 7:
                    listener.unitHide(param1);
                    break;
                case 8:
                    listener.unitCreate(param1);
                    break;
                case 9:
                    listener.unitDestroy(param1);
                    break;
                case 10:
                    listener.unitMorph(param1);
                    break;
            }
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    /**
     * C++ callback function.
     * <p/>
     * Notifies the event listener that a key was pressed.
     */
    public void keyPressed(int keyCode) {
        try {
            listener.keyPressed(keyCode);
        } catch (Error e) {
            e.printStackTrace();
        }
    }
    
    private void loadStaticUnits(){
    	staticNeutralUnits.clear();
        staticGeysers.clear();
        staticMinerals.clear();
        
        int[] unitData = getStaticNeutralUnits();
        
        for (int index = 0; index < unitData.length; index += Unit.numAttributes) {
            int id = unitData[index];
            Unit unit = new Unit(id);
            unit.update(unitData, index);

            staticNeutralUnits.put(id, unit);
        }
        
        unitData = getStaticMinerals();
        for (int index = 0; index < unitData.length; index += Unit.numAttributes) {
            int id = unitData[index];
            Unit unit = staticNeutralUnits.get(id);
            if (unit == null){
            	unit = new Unit(id);
            }
            unit.update(unitData, index);

            staticMinerals.put(id, unit);
        }
        
        unitData = getStaticGeysers();
        for (int index = 0; index < unitData.length; index += Unit.numAttributes) {
            int id = unitData[index];
            Unit unit = staticNeutralUnits.get(id);
            if (unit == null){
            	unit = new Unit(id);
            }
            unit.update(unitData, index);

            staticGeysers.put(id, unit);
        }
        
    }
}
