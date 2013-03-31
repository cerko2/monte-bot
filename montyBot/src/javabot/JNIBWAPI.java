package javabot;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import javabot.model.BaseLocation;
import javabot.model.ChokePoint;
import javabot.model.Map;
import javabot.model.Player;
import javabot.model.Region;
import javabot.model.Unit;
import javabot.types.BulletType;
import javabot.types.DamageType;
import javabot.types.ExplosionType;
import javabot.types.OrderType;
import javabot.types.TechType;
import javabot.types.UnitCommandType;
import javabot.types.UnitSizeType;
import javabot.types.UnitType;
import javabot.types.UpgradeType;
import javabot.types.WeaponType;
import javabot.util.Position;
import javabot.util.UnitUtils;

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
    public native void attack(int unitID, int x, int y);

    public native void attack(int unitID, int targetID);

    public native void build(int unitID, int tx, int ty, int typeID);

    public native void buildAddon(int unitID, int typeID);

    public native void train(int unitID, int typeID);

    public native void morph(int unitID, int typeID);

    public native void research(int unitID, int techID);

    public native void upgrade(int unitID, int updateID);

    public native void setRallyPoint(int unitID, int x, int y);

    public native void setRallyPoint(int unitID, int targetID);

    public native void move(int unitID, int x, int y);

    public native void patrol(int unitID, int x, int y);

    public native void holdPosition(int unitID);

    public native void stop(int unitID);

    public native void follow(int unitID, int targetID);

    public native void gather(int unitID, int trargetID);

    public native void returnCargo(int unitID);

    public native void repair(int unitID, int targetID);

    public native void burrow(int unitID);

    public native void unburrow(int unitID);

    public native void cloak(int unitID);

    public native void decloak(int unitID);

    public native void siege(int unitID);

    public native void unsiege(int unitID);

    public native void lift(int unitID);

    public native void land(int unitID, int tx, int ty);

    public native void load(int unitID, int targetID);

    public native void unload(int unitID, int targetID);

    public native void unloadAll(int unitID);

    public native void unloadAll(int unitID, int x, int y);

    public native void rightClick(int unitID, int x, int y);

    public native void rightClick(int unitID, int targetID);

    public native void haltConstruction(int unitID);

    public native void cancelConstruction(int unitID);

    public native void cancelAddon(int unitID);

    public native void cancelTrain(int unitID, int slot);

    public native void cancelMorph(int unitID);

    public native void cancelResearch(int unitID);

    public native void cancelUpgrade(int unitID);

    public native void useTech(int unitID, int typeID);

    public native void useTech(int unitID, int typeID, int x, int y);

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
    
    private native int[] getShortestPath(int tx1, int ty1, int tx2, int ty2);
    
    public native double getGroundDistance(int tx1, int ty1, int tx2, int ty2);
    
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
        //boolean analyzed = bwtaFile.exists();
        boolean analyzed = false;
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
            /*
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
            */
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
    
    public ArrayList<Position> getGroundPath(int x1, int y1, int x2, int y2){
    	
    	int[] pathData = getShortestPath(x1, y1, x2, y2);
    	
    	ArrayList<Position> resultPath = new ArrayList<Position>();
    	for (int i = 0; i < pathData.length; i += 2){
    		resultPath.add(new Position(pathData[i], pathData[i+1]));
    	}
    	
    	return resultPath;
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
