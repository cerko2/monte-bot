package javabot.macro;

import java.awt.Point;
import java.util.ArrayList;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.combat.MonteCarloPlanner;
import javabot.combat.micro.ScoutingManager;
import javabot.model.ChokePoint;
import javabot.model.Player;
import javabot.model.Region;
import javabot.model.Unit;
import javabot.strategy.OpeningManager;
import javabot.strategy.WallInModule;
import javabot.types.UnitType.UnitTypes;
import javabot.util.BWColor;
import javabot.util.Wall;

public class Boss extends AbstractManager{
	
	public static final boolean BOSS_DEBUG = true;
	public static final boolean WALLIN_DEBUG = true;
	
	private Region home; // Needed only for DEBUGGING (miso certikcy)
	
	public JNIBWAPI game;
	private Player player;
	
	private final int workerDefenseTreshold = 3;
	
	// Managers & Modules:
	private BuildManager buildManager;
	private MonteCarloPlanner montePlanner;
	private OpeningManager openingManager;
	private ScoutingManager scoutManager;
	private UnitProductionManager unitProductionManager;
	private WallInModule wallInModule;
	private WorkerManager workerManager;
	
	private ArrayList<Unit> validUnits;
	private ArrayList<Unit> combatUnits;
	private ArrayList<Unit> workerUnits;
	private ArrayList<Unit> scoutUnits;
	private ArrayList<Unit> assignedUnits;
	
	public int minerals;
	public int gas;
	
	public int workerMinerals;
	public int monteCarloMinerals;
	public int BuildManagerMinerals;
	public int BuildManagerGas;
	public int UnitProductionMinerals;
	public int UnitProductionGas;
	
	public Boss (JNIBWAPI game){
		this.game = game;
		this.player = game.getSelf();
	}
	
	public void initialize(){
		montePlanner = new MonteCarloPlanner();
		workerManager =new WorkerManager();
		
		validUnits = new ArrayList<Unit>();
		combatUnits = new ArrayList<Unit>();
		scoutUnits = new ArrayList<Unit>();
		workerUnits = new ArrayList<Unit>();
		assignedUnits = new ArrayList<Unit>();
		
		//subordinate initialization
		openingManager = new OpeningManager(game);
		wallInModule = new WallInModule(game);
		unitProductionManager = new UnitProductionManager(this); 
		buildManager = new  BuildManager(this);
		
		addManager(openingManager);
		addManager(wallInModule);			// miso certicky
		addManager(buildManager);			// azder
		addManager(unitProductionManager);	// azder
	}
	
	public void gameStarted(){
		initialize();
		
		super.gameStarted();
	}
	
	public void gameUpdate(){
		setUnits();
		montePlanner.update(combatUnits);
		workerManager.update(workerUnits);
		
		minerals = player.getMinerals();
		gas = player.getGas();
		
		if (BOSS_DEBUG){
			debug();
		}
		
		super.gameUpdate();
	}
	
	private void setUnits(){
		setValidUnits();
		setScoutUnits();
		setCombatUnits();
		setWorkerUnits();
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
	
	private boolean shouldDefendWithWorkers(){
		return false;
	}
	
	private void divideResources(){
		
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
	
	private void debug(){
		
		if (WALLIN_DEBUG){
			// START DEBUG: Compute the wall on frame 1 (miso certicky)
			if (game.getFrameCount() == 1) {
				home = game.getMap().getRegions().get(0);

				Unit homeNexus = game.getMyUnits().get(0);
				Integer dist = 999999;
				Integer dist2;
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
				game.drawText(new Point(5,0), "Our home position: "+String.valueOf(home.getCenterX())+","+String.valueOf(home.getCenterY()), true);
				game.drawText(new Point(5,14), "Map: "+game.getMap().getName(), true);

				// chokepoints and center of the home Region
				game.drawCircle(home.getCenterX(), home.getCenterY(), 15, BWColor.TEAL, true, false);
				for (ChokePoint c : home.getChokePoints()) {
					game.drawLine(new Point(home.getCenterX() , home.getCenterY() ), new Point(c.getCenterX(),c.getCenterY()),BWColor.TEAL,false);
					game.drawCircle(c.getCenterX(),c.getCenterY(), 10, BWColor.TEAL, true, false);
				}
			}

			for (int i=1; i < game.getMap().getWidth(); i++) {
				for (int j=1; j < game.getMap().getHeight(); j++) {
					if (wallInModule.obstructedByNeutrals(i, j))
						game.drawCircle(i*32,j*32, 5, BWColor.GREEN, false, false);
				}
			}

			// Draw all previously computed walls
			for (Wall w : wallInModule.getAllWalls()) {
				for (Point bt : w.getBuildTiles()) {
					Integer tileWidth = game.getUnitType(w.getBuildingTypeIds().get(w.getBuildTiles().indexOf(bt))).getTileWidth();
					Integer tileHeight = game.getUnitType(w.getBuildingTypeIds().get(w.getBuildTiles().indexOf(bt))).getTileHeight();
					game.drawBox(bt.x*32, bt.y*32, (bt.x + tileWidth)*32, (bt.y + tileHeight)*32, BWColor.YELLOW, false, false);
					game.drawText(new Point(bt.x*32+4, bt.y*32+2), game.getUnitType(w.getBuildingTypeIds().get(w.getBuildTiles().indexOf(bt))).getName()+" "+String.valueOf(bt.x)+","+String.valueOf(bt.y), false);
				}
			}
		}
	}
}
