package javabot.macro;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.combat.MonteCarloPlanner;
import javabot.combat.micro.ScoutingManager;
import javabot.model.BaseLocation;
import javabot.model.ChokePoint;
import javabot.model.Player;
import javabot.model.Region;
import javabot.model.Unit;
import javabot.strategy.ArmyCompositionManager;
import javabot.strategy.OpeningManager;
import javabot.strategy.OpponentKnowledgeBase;
import javabot.strategy.OpponentModeling;
import javabot.strategy.OpponentPositioning;
import javabot.strategy.WallInModule;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.BWColor;
import javabot.util.Position;
import javabot.util.Wall;

public class Boss extends AbstractManager{
	
	public static final boolean BOSS_DEBUG = true;
	public static final boolean WALLIN_DEBUG = false;
	public static final boolean OPPONENT_POSITIONING_DEBUG = false;
	public static final boolean RESOURCE_DEBUG = false;
	public static final boolean MONTE_CARLO_DEBUG = true;
	
	//REALLY SLOW
	public static final boolean PATH_DEBUG = false;
	
	private Region home; // Needed only for DEBUGGING (miso certikcy)
	
	public JNIBWAPI game;
	private Player player;
	
	private final int workerDefenseTreshold = 3;
	
	// Managers & Modules:
	private ArmyCompositionManager armyCompositionManager;
	private BuildManager buildManager;
	private MonteCarloPlanner montePlanner;
	private OpeningManager openingManager;
	private OpponentKnowledgeBase opponentKnowledgeBase;
	private OpponentModeling opponentModeling;
	private OpponentPositioning opponentPositioning;
	private ScoutingManager scoutManager;
	private UnitProductionManager unitProductionManager;
	private WallInModule wallInModule;
	private WorkerManager workerManager;
	
	private ArrayList<Unit> validUnits;
	private ArrayList<Unit> combatUnits;
	private ArrayList<Unit> workerUnits;
	private HashSet<Unit> scoutUnits;
	private ArrayList<Unit> assignedUnits;
	
	private ArrayList<Unit> productionBuildings;
	private ArrayList<Unit> nexuses;
	
	private HashMap<Integer, Integer> buildingTypeCounts;
	
	private boolean scouting;
	
	private int minerals;
	private int gas;
	
	private int workerMinerals;
	private int monteCarloMinerals;
	private int buildManagerMinerals;
	private int buildManagerGas;
	private int unitProductionMinerals;
	private int unitProductionGas;
	
	public Boss (JNIBWAPI game){
		this.game = game;
		this.player = game.getSelf();
		
	}
	
	public void initialize(){
		validUnits = new ArrayList<Unit>();
		combatUnits = new ArrayList<Unit>();
		scoutUnits = new HashSet<Unit>();
		workerUnits = new ArrayList<Unit>();
		assignedUnits = new ArrayList<Unit>();
		
		productionBuildings = new ArrayList<Unit>();
		nexuses = new ArrayList<Unit>();
		
		buildingTypeCounts = new HashMap<Integer, Integer>();
		
		//subordinate initialization
		buildManager = new  BuildManager(this);
		opponentKnowledgeBase = new OpponentKnowledgeBase(this);
		openingManager = new OpeningManager(this);
		opponentPositioning = new OpponentPositioning(game);
		scoutManager = new ScoutingManager(game);
		wallInModule = new WallInModule(game);
		workerManager = new WorkerManager(this);
		unitProductionManager = new UnitProductionManager(this); 
		
		opponentModeling = new OpponentModeling(game, opponentPositioning);
		armyCompositionManager = new ArmyCompositionManager(game, unitProductionManager, opponentPositioning, opponentModeling);
		montePlanner = new MonteCarloPlanner( game );
		addManager(opponentKnowledgeBase);
		addManager(openingManager);
		addManager(opponentPositioning);
		addManager(wallInModule);			// miso certicky
		addManager(buildManager);			// azder
		addManager(unitProductionManager);	// azder
		addManager(armyCompositionManager);
		addManager(scoutManager);
		addManager( montePlanner );
	}
	
	public void gameStarted(){
		initialize();
		
		super.gameStarted();
	}
	
	public void gameUpdate(){
		setUnits();
		
		montePlanner.update( new ArrayList<Unit>( opponentPositioning.getEnemyUnits() ) , game.getMyUnits() );
		
		workerManager.update(workerUnits);
		
		minerals = player.getMinerals();
		gas = player.getGas();
		
		if (!BOSS_DEBUG){
			debug();
		}
		
		super.gameUpdate();
	}
	
	private void setUnits(){
		setValidUnits();
		setScoutUnits();
		setCombatUnits();
		setWorkerUnits();
		setBuildings();
	}
	
	private void setValidUnits(){
		validUnits.clear();
		assignedUnits.clear();
		
		for (Unit unit : game.getMyUnits()){
			if (isValidUnit(unit)){
				validUnits.add(unit);
			}
		}
		
		divideResources();
	}
	
	private boolean isValidUnit(Unit unit){
		if (unit == null){
			return false;
		}
		
		if (unit.isExists()
			&& unit.isCompleted()
			&& unit.getHitPoints() > 0
			&& unit.getTypeID() != UnitTypes.None.ordinal())
		{
			return true;
		}
		else {
			return false;
		}
	}

	private void setWorkerUnits(){
		workerUnits.clear();

		for (Unit unit : validUnits){
			if (!assignedUnits.contains(unit) && game.getUnitType(unit.getTypeID()).isWorker()){
				workerUnits.add(unit);
				assignedUnits.add(unit);
			}
		}
	}
	
	//zatial len provizorne neskor zmenim nech viac scoutov berie, a nie len worker/obs
	private void setScoutUnits(){
		scoutUnits.clear();
		
		for (Unit unit : scoutManager.getUnits()){
			if (unit.isExists()){
				scoutUnits.add(unit);
				assignedUnits.add(unit);
			}
		}
		
		if (scoutUnits.isEmpty()){
			if (scouting){
				for (Unit unit : validUnits){
					if (!assignedUnits.contains(unit) 
							&& (game.getUnitType(unit.getTypeID()).isWorker()
									|| unit.getTypeID() == UnitTypes.Protoss_Observer.ordinal()))
					{
						scoutUnits.add(unit);
						assignedUnits.add(unit);
						break;
					}
				}
			}
		}
		
		scoutManager.setUnits(scoutUnits);
	}
	
	private void setCombatUnits(){
		
		combatUnits.clear();
		for (Unit unit : validUnits){
			if (!assignedUnits.contains(unit) && isCombatUnit(unit)){
				combatUnits.add(unit);
				assignedUnits.add(unit);
			}
		}
		
		//lazy 4/5/6-pool check
		boolean lings = false;
		for (Unit unit : game.getEnemyUnits()){
			if (unit.getTypeID() == UnitTypes.Zerg_Zergling.ordinal()){
				lings = true;
			}
		}
		
		//placeholder na workerdefense treba trosku pomenit
		if (combatUnits.size() < workerDefenseTreshold && shouldDefendWithWorkers()){
			for (Unit unit : validUnits){
				if (!assignedUnits.contains(unit) 
					&& game.getUnitType(unit.getTypeID()).isWorker())
				{
					//workerManager.removeFromRes(worker, WorkerState.FIGHTING);
					combatUnits.add(unit);
					assignedUnits.add(unit);
					
				
					if (combatUnits.size() >= 3 && !lings){
						break;
					}
				}
			}
			
		}
	}
	
	private boolean isCombatUnit(Unit unit){
		if (unit == null){
			return false;
		}
		if (game.getUnitType(unit.getTypeID()).isWorker() || game.getUnitType(unit.getTypeID()).isBuilding()){
			return false;
		}
		
		if (game.getUnitType(unit.getTypeID()).isAttackCapable() 
			|| unit.getTypeID() == UnitTypes.Protoss_Reaver.ordinal()
			|| unit.getTypeID() == UnitTypes.Protoss_High_Templar.ordinal()
			|| unit.getTypeID() == UnitTypes.Protoss_Observer.ordinal()
			|| unit.getTypeID() == UnitTypes.Protoss_Shuttle.ordinal())
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	private void setBuildings() {
		productionBuildings.clear();
		buildingTypeCounts.clear();
		nexuses.clear();
		
		UnitType type = null;
		for (Unit unit : validUnits){
			type = game.getUnitType(unit.getTypeID());
			
			if (type.isBuilding()){
				int buildingTypeCount = 0;
				if (buildingTypeCounts.containsKey(type.getID())){
					buildingTypeCount = buildingTypeCounts.get(type.getID());
				}
				buildingTypeCount++;
				buildingTypeCounts.put(type.getID(), buildingTypeCount);
				
				if (isProductionBuilding(type)){
					productionBuildings.add(unit);
				}
				else if (type.getID() == UnitTypes.Protoss_Nexus.ordinal()){
					nexuses.add(unit);
				}
			}
		}
	}
	
	private boolean isProductionBuilding(UnitType type){
		if (type.getID() == UnitTypes.Protoss_Gateway.ordinal()
				|| type.getID() == UnitTypes.Protoss_Stargate.ordinal()
				|| type.getID() == UnitTypes.Protoss_Robotics_Facility.ordinal())
		{
			return true;
		}
		return false;
	}
	
	private boolean shouldDefendWithWorkers(){
		return false;
	}
	
	private void divideResources(){
		buildManagerMinerals = 0;
		buildManagerGas = 0;
		unitProductionMinerals = 0;
		unitProductionGas = 0;
		
		if (openingManager.isActive()){
			buildManagerMinerals = minerals;
			buildManagerGas = gas;
			return;
		}
		
		int idleNexusCount = 0;
		for (Unit unit : nexuses){
			if (unit.isIdle()){
				idleNexusCount++;
			}
		}
		
		workerMinerals = idleNexusCount * 50;
		
		Vector<Integer> buildQueue = buildManager.getConstructionPlans();
		
		if (!buildQueue.isEmpty()){
			UnitType buildingType = game.getUnitType(buildQueue.get(0));
			//if its tech building we dont have yet its high prio
			if (isTechBuilding(buildingType) 
					&& buildingTypeCounts.get(buildingType.getID()) == null
				)
			{
				buildManagerMinerals = buildingType.getMineralPrice();
				buildManagerGas = buildingType.getGasPrice();
				
				minerals -= buildManagerMinerals;
				gas -= buildManagerGas;
			}
		}
		
		boolean prodBuildingIdle = false;
		
		for (Unit unit : productionBuildings){
			if (unit.isIdle()){
				prodBuildingIdle = true;
				break;
			}
		}
		
		if (prodBuildingIdle){
			unitProductionMinerals = minerals;
			unitProductionGas = gas;
		}
		else {
			buildManagerMinerals += minerals;;
			buildManagerGas += gas;
		}
		
	}
	
	private boolean isTechBuilding(UnitType type){
		if (type.getID() == UnitTypes.Protoss_Cybernetics_Core.ordinal()
				|| type.getID() == UnitTypes.Protoss_Forge.ordinal()
				|| type.getID() == UnitTypes.Protoss_Robotics_Facility.ordinal()
				|| type.getID() == UnitTypes.Protoss_Robotics_Support_Bay.ordinal()
				|| type.getID() == UnitTypes.Protoss_Citadel_of_Adun.ordinal()
				|| type.getID() == UnitTypes.Protoss_Templar_Archives.ordinal()
				|| type.getID() == UnitTypes.Protoss_Stargate.ordinal()
				|| type.getID() == UnitTypes.Protoss_Fleet_Beacon.ordinal()
			)
		{
			return true;
		}
		return false;
	}
	
	public int getWorkerMinerals() {
		return workerMinerals;
	}

	public int getMonteCarloMinerals() {
		return monteCarloMinerals;
	}

	public int getBuildManagerMinerals() {
		return buildManagerMinerals;
	}

	public int getBuildManagerGas() {
		return buildManagerGas;
	}

	public int getUnitProductionMinerals() {
		return unitProductionMinerals;
	}

	public int getUnitProductionGas() {
		return unitProductionGas;
	}
	
	public ArmyCompositionManager getArmyCompositionManager() {
		return armyCompositionManager;
	}

	public BuildManager getBuildManager() {
		return buildManager;
	}

	public MonteCarloPlanner getMontePlanner() {
		return montePlanner;
	}

	public OpeningManager getOpeningManager() {
		return openingManager;
	}
	
	public OpponentPositioning getOpponentPositioning() {
		return opponentPositioning;
	}
	
	public OpponentKnowledgeBase getOpponentKnowledgeBase() {
		return opponentKnowledgeBase;
	}

	public ScoutingManager getScoutManager() {
		return scoutManager;
	}

	public UnitProductionManager getUnitProductionManager() {
		return unitProductionManager;
	}

	public WallInModule getWallInModule() {
		return wallInModule;
	}

	public WorkerManager getWorkerManager() {
		return workerManager;
	}
	
	public void startScouting(){
		scouting = true;
	}
	
	private void debug(){
		
		if (WALLIN_DEBUG){
			// START DEBUG: Compute the wall on frame 1 (miso certicky)
			if (game.getFrameCount() == 1) {
				home = game.getMap().getRegions().get(0);

				Unit homeNexus = game.getMyUnits().get(0);
				int dist = 999999;
				int dist2;
				for (Unit u : game.getMyUnits()) {
					if (u.getTypeID() == UnitTypes.Protoss_Nexus.ordinal()) {
						homeNexus = u;
						break;
					}
				}
				for (Region r : game.getMap().getRegions()) {
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

			// Draw our home position and chokes
			if (home != null) {
				game.drawText(new Point(5,0), "Army Composition: "+String.valueOf(armyCompositionManager.getDesiredArmyComposition().getString(game)), true);
				game.drawText(new Point(5,287), game.getMap().getName(), true);

				// chokepoints and center of the home Region
				game.drawCircle(home.getCenterX(), home.getCenterY(), 15, BWColor.TEAL, true, false);
				for (ChokePoint c : home.getChokePoints()) {
					game.drawLine(new Point(home.getCenterX() , home.getCenterY() ), new Point(c.getCenterX(),c.getCenterY()),BWColor.TEAL,false);
					game.drawCircle(c.getCenterX(),c.getCenterY(), 10, BWColor.TEAL, true, false);
				}
			}
				
			// Draw all previously computed walls
			for (Wall w : wallInModule.getAllWalls()) {
				for (Point bt : w.getBuildTiles()) {
					int tileWidth = game.getUnitType(w.getBuildingTypeIds().get(w.getBuildTiles().indexOf(bt))).getTileWidth();
					int tileHeight = game.getUnitType(w.getBuildingTypeIds().get(w.getBuildTiles().indexOf(bt))).getTileHeight();
					game.drawBox(bt.x*32, bt.y*32, (bt.x + tileWidth)*32, (bt.y + tileHeight)*32, BWColor.YELLOW, false, false);
					game.drawText(new Point(bt.x*32+4, bt.y*32+2), game.getUnitType(w.getBuildingTypeIds().get(w.getBuildTiles().indexOf(bt))).getName()+" "+String.valueOf(bt.x)+","+String.valueOf(bt.y), false);
				}
			}
		}
		
		if (OPPONENT_POSITIONING_DEBUG){
			UnitType type = null;
			for (Unit unit : opponentPositioning.getEnemyUnits()){
				type = game.getUnitType(unit.getTypeID());
				if (type.isBuilding()){
					int tileWidth = type.getTileWidth();
					int tileHeight = type.getTileHeight();
					game.drawBox(unit.getTileX()*32, unit.getTileY()*32, (unit.getTileX() + tileWidth)*32, (unit.getTileY() + tileHeight)*32, BWColor.RED, false, false);
					game.drawText(new Point(unit.getTileX()*32+4, unit.getTileY()*32+2), type.getName(), false);
				}
				else {
					game.drawBox(unit.getX() - type.getDimensionLeft(), unit.getY() - type.getDimensionUp(), (unit.getX() + type.getDimensionRight()), (unit.getY() + type.getDimensionDown()), BWColor.RED, false, false);
					game.drawText(new Point(unit.getX()+4, unit.getY()+2), type.getName(), false);
				}
			}
		}
		
		if (RESOURCE_DEBUG){
			game.drawText(10, 100,"buildManagerMinerals: " + buildManagerMinerals, true);
			game.drawText(10, 115,"buildManagerGas: " + buildManagerGas, true);
			game.drawText(10, 130,"unitProductionMinerals: " + unitProductionMinerals, true);
			game.drawText(10, 145,"unitProductionGas: " + unitProductionGas, true);
			game.drawText(10, 160,"workerMinerals: " + workerMinerals, true);
		}
		
		if (PATH_DEBUG){
			
			BaseLocation base1 = game.getMap().getBaseLocations().get(0);
			BaseLocation base2 = game.getMap().getBaseLocations().get(2);
			
			
			ArrayList<Position> path = game.getGroundPath(base1.getTx(), base1.getTy(), base2.getTx(), base2.getTy());
			game.drawLine(base1.getX(), base1.getY(), base2.getX(), base2.getY(), BWColor.YELLOW, false);
			
			for (int i = 0; i < path.size() - 1; i++){
				game.drawLine((int) path.get(i).x * 32, (int) path.get(i).y * 32, (int) path.get(i+1).x * 32, (int) path.get(i+1).y * 32, BWColor.WHITE, false);
			}
			
			System.out.println(game.getGroundDistance(base1.getTx(), base1.getTy(), base2.getTx(), base2.getTy()));
		}
	}
	
	public ArrayList<Unit> getNexuses() {
		return nexuses;
	}
}
