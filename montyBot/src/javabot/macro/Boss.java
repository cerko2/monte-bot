package javabot.macro;

import java.util.ArrayList;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.combat.MonteCarloPlanner;
import javabot.combat.micro.ScoutingManager;
import javabot.model.Player;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;

public class Boss extends AbstractManager{
	
	public JNIBWAPI game;
	private Player player;
	
	private final int workerDefenseTreshold = 3;
	
	private MonteCarloPlanner montePlanner;
	private WorkerManager workerManager;
	private ScoutingManager scoutManager;
	
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
		this.player = game.getSelf(); // azder
	}
	
	public void initialize(){
		montePlanner = new MonteCarloPlanner();
		workerManager =new WorkerManager();
		
		validUnits = new ArrayList<Unit>();
		combatUnits = new ArrayList<Unit>();
		scoutUnits = new ArrayList<Unit>();
		workerUnits = new ArrayList<Unit>();
		assignedUnits = new ArrayList<Unit>();
	}
	
	public void gameStarted(){
		initialize();
	}
	
	public void gameUpdate(){
		setUnits();
		montePlanner.update(combatUnits);
		workerManager.update(workerUnits);
		
		if(player != null){ //azder
			minerals = player.getMinerals();
			gas = player.getGas();
		}
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
	
}
