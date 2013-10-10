package javabot.macro;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.model.Unit;
import javabot.model.BaseLocation;
import javabot.types.TechType;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.types.UpgradeType;
import javabot.types.WeaponType;
import javabot.types.WeaponType.WeaponTypes;
import javabot.util.ArmyComposition;
import javabot.util.BWColor;
import javabot.util.Position;


public class WorkerManager extends AbstractManager {
	
	private final double maxWorkersPerMineralField = 2.3;
	private final double maxWorkersPerGeyser = 3.0;
	private final int maxWorkersCount = 70;
	private final double probeTopSpeed = 4.92;
	
	private Boss boss;
	private JNIBWAPI game;
	
	// Minerals assigned to this manager by Boss
	//private int minerals = 0;
	
	public NexusBase capitalNexusBase = null;
	
	// All Nexus bases which bot have
	public ArrayList<NexusBase> nexusBases = new ArrayList<NexusBase>();
	
	// All workers assigned to this manager
	public ArrayList<Unit> allWorkers = new ArrayList<Unit>();
	
	// All workers that isn't assigned to any NexusBase
	public ArrayList<Unit> unassignedWorkers = new ArrayList<Unit>();
	
	// Workers that is currenty transfered from one base to another
	public HashMap<Unit, NexusBase> transferedWorkers = new HashMap<Unit, NexusBase> ();
	
	// Queued number of workers to be built
	private int numWorkersToBuild = 0;
	
	// Num workers to be built until opening to now
	private int numWorkersUntilOpening = 4;
	
	// Ratio of resources that will be needed in near future
	public double mineralsRatioForBuild = 0;
	public double gasRatioForBuild = 0;
	
	// Mineral and gas workers ratio
	public double mineralWorkersRatio = 0;
	public double gasWorkersRatio = 0;
	
	// Cached paths from one base location to another
	private HashMap<Pair<BaseLocation, BaseLocation>, ArrayList<Position>> groundPaths = new HashMap<Pair<BaseLocation, BaseLocation>, ArrayList<Position>>();
	
	/*private int mineralIncome = 0;
	private int mineralIncome10 = 0;
	private int gasIncome = 0;
	private int gasIncome10 = 0;
	private int beforeMinerals = 0;
	private int beforeGas = 0;*/

	/**
	 * WorkerManager constructor.
	 *
	 * @param Boss - boss class
	 */
	public WorkerManager(Boss boss) {
		this.boss = boss;
		game = boss.game;
	}
	
	/**
	 * Main method called approximately 30 times per second.
	 *
	 * @param units - units assigned to worker manager
	 */
	public void update(ArrayList<Unit> units) {
		resetCountFakeWorkers();
		
		ArrayList<Unit> myUnits = boss.game.getMyUnits();
		ArrayList<Unit> myNexuses = new ArrayList<Unit>();
		
		// Create list of all my Nexuses
		for (Unit unit: myUnits) {
			if (unit.getTypeID() == UnitTypes.Protoss_Nexus.ordinal() && unit.isCompleted()) {
				myNexuses.add(unit);
			}
		}
		
		// Find out whether new Nexus is completed and is not added into nexusBases list
		for (Unit unit: myNexuses) {
			boolean newNexus = true;
				
			for (NexusBase nexusBase: nexusBases) {
				if (nexusBase.nexus == unit) {
					newNexus = false;
				}
			}
			
			if (newNexus) {
				NexusBase newNexusBase = new NexusBase(this, unit);
				
				if (capitalNexusBase == null) {
					capitalNexusBase = newNexusBase;
				}
				
				nexusBases.add(newNexusBase);
			}
		}	
		
		// Create list of actual Nexuses from nexusBases list
		ArrayList<Unit> nexusesInNexusBases = new ArrayList<Unit>();
		for (NexusBase nexusBase: nexusBases) {
			nexusesInNexusBases.add(nexusBase.nexus);
		}
		
		// If Nexus doesn't exist, delete item from nexusBases which have this nexus assigned to
		// assign all workers from this instance to unassignedWorkers list too
		for (Unit nexus: nexusesInNexusBases) {
			if (!myNexuses.contains(nexus)) {
				for (NexusBase nexusBase: nexusBases) {
					if (nexusBase.nexus == nexus) {
						unassignedWorkers.addAll(nexusBase.mineralWorkers);
						unassignedWorkers.addAll(nexusBase.gasWorkers);
						nexusBases.remove(nexusBase);
						break;
					}
				}
			}
		}
		
		// Finds out whether new worker was assigned to this manager and is not assigned 
		// to some NexusBase
		for (Unit worker: units) {
			if (!allWorkers.contains(worker)) {
				allWorkers.add(worker); 
				
				NexusBase nexusBaseWithSmallestWorkerRatio = getNexusBaseWithSmallestWorkerRatio();
				NexusBase nearestNexusBase = getNearestNexusBase(new Position(worker.getX(), worker.getY()));
				
				if (nexusBaseWithSmallestWorkerRatio != null && nearestNexusBase != null) {
					Position workerPosition = new Position(worker.getX(), worker.getY());
					
					if ((Math.abs(nexusBaseWithSmallestWorkerRatio.getWorkerRatio() - nearestNexusBase.getWorkerRatio()) <= 0.15 && nearestNexusBase.getWorkerRatio() < 1.0) || !isPathClear(getNearestNexusBase(workerPosition).base, nexusBaseWithSmallestWorkerRatio.base)) {
						nearestNexusBase.addWorker(worker);
						if (nearestNexusBase.getWorkerRatio() < 1.0) {
							transferedWorkers.put(worker, nearestNexusBase);
						}
					}
					else {
						nexusBaseWithSmallestWorkerRatio.addWorker(worker);
						if (nexusBaseWithSmallestWorkerRatio.getWorkerRatio() < 1.0) {
							transferedWorkers.put(worker, nexusBaseWithSmallestWorkerRatio);
						}
					}
				}
				else {
					unassignedWorkers.add(worker);
				}
			}
		}
		
		if (unassignedWorkers.size() > 0) {
			Unit worker = unassignedWorkers.get(0);
			
			NexusBase nexusBaseWithSmallestWorkerRatio = getNexusBaseWithSmallestWorkerRatio();
			NexusBase nearestNexusBase = getNearestNexusBase(new Position(worker.getX(), worker.getY()));
			
			if (nexusBaseWithSmallestWorkerRatio != null && nearestNexusBase != null) {
				Position workerPosition = new Position(worker.getX(), worker.getY());
				
				if ((Math.abs(nexusBaseWithSmallestWorkerRatio.getWorkerRatio() - nearestNexusBase.getWorkerRatio()) <= 0.15 && nearestNexusBase.getWorkerRatio() < 1.0) || !isPathClear(getNearestNexusBase(workerPosition).base, nexusBaseWithSmallestWorkerRatio.base)) {
					nearestNexusBase.addWorker(worker);
					if (nearestNexusBase.getWorkerRatio() < 1.0) {
						transferedWorkers.put(worker, nearestNexusBase);
					}
				}
				else {
					nexusBaseWithSmallestWorkerRatio.addWorker(worker);
					if (nexusBaseWithSmallestWorkerRatio.getWorkerRatio() < 1.0) {
						transferedWorkers.put(worker, nexusBaseWithSmallestWorkerRatio);
					}
				}

				unassignedWorkers.remove(worker);
			}
		}
		
		// Finds out whether some worker was unassigned by boss from this manager and deletes
		// it from NexusBase
		for (Unit worker: allWorkers) {
			if (!units.contains(worker) || !unitExists(worker)) {
				for (NexusBase nexusBase: nexusBases) {
					nexusBase.deleteWorker(worker);
				}
				
				allWorkers.remove(worker);
				unassignedWorkers.remove(worker);
				transferedWorkers.remove(worker);
				break;
			}
		}

		// Handle all NexusBase instances
		for (NexusBase nexusBase: nexusBases) {
			nexusBase.checkMinerals();
			nexusBase.checkAssimilators();
			if (!isSaturated() && getWorkersNumIsTraining() + allWorkers.size() < maxWorkersCount && !boss.getOpeningManager().isActive()) {
				nexusBase.buildWorkers();
			}
			nexusBase.handleWorkers();
		}
		
		// Build queued workers and build new workers that was destroyed until OpeningManager active
		if (boss.getOpeningManager().isActive() && nexusBases.size() > 0) {
			if (numWorkersToBuild > 0) {
				for (NexusBase nexusBase: nexusBases) {
					if (nexusBase.buildWorkers()) {
						numWorkersToBuild--;
						break;
					}
				}
			}
			
			if (numWorkersUntilOpening > getWorkersNumIsTraining() + allWorkers.size()) {
				for (NexusBase nexusBase: nexusBases) {
					if (nexusBase.buildWorkers()) {
						break;
					}
				}
			}
		}
		
		// Checks if some of transfered workers is near to base to which is transfered
		// and checks whether the base to which is worker transfered is still clear
		checkTranferedWorkers();
		
		// Transfer worker from base with largest worker ratio to base with smallest
		// worker ratio
		transferWorker();
		
		// Updates frames in which workers started to mine minerals
		for (NexusBase nexusBase: nexusBases) {
			nexusBase.updateStartMiningFrames();
		}
		
		// Update mineralsRatioForBuild and gasRatioForBuild
		if (game.getFrameCount() % 100 == 0) {
			balanceWorkersBetweenMineralsAndGas();
		}
		
		// Show manager debug info on the map
		if (boss.WORKER_MANAGER_DEBUG) {
			drawDebugInfo();
		}
		
		/*if (game.getFrameCount() % 1000 == 0) {
			game.printText("min: " + (mineralIncome - game.getSelf().getMinerals()) + "  gas: " + (gasIncome - game.getSelf().getGas()));
			mineralIncome = game.getSelf().getMinerals() + getMineralIncome(1000);
			gasIncome = game.getSelf().getGas() + getGasIncome(1000);
			
			game.printText("+10 min: " + mineralIncome10 + "  gas: " + gasIncome10);
			mineralIncome10 = getMineralIncome(1000, 10);
			gasIncome10 = getGasIncome(1000, 10);
			
			game.printText("real income min: " + (game.getSelf().getMinerals() - beforeMinerals) + "  gas: " + (game.getSelf().getGas() - beforeGas));
			
			beforeMinerals = game.getSelf().getMinerals();
			beforeGas = game.getSelf().getGas();
		}*/
	}
	
	public void balanceWorkersBetweenMineralsAndGas() {
		updateDesiredResources();
		
		for (NexusBase nexusBase: nexusBases) {
			computeMineralAndGasWorkersRatio();
			
			if (gasRatioForBuild - gasWorkersRatio >= 0.1) {
				nexusBase.moveOneWorkerToGas();
			}
			else if (gasRatioForBuild - gasWorkersRatio <= -0.1) {
				nexusBase.moveOneWorkerToMinerals();
			}
		}
	}
	
	/**
	 * Returns number of minerals that will be mined in desired time frame with current
	 * number of mineral workers and mineral fields - this means that distant predictions 
	 * to future may be very inaccurate.
	 * 
	 * @param frames - number of frames for which prediction will be computed
     *
	 * @return returns number of minerals that will be mined
	 */
	public int getMineralIncome(int frames) {
		int mineralIncome = 0;
		
		for (NexusBase nexusBase: nexusBases) {
			mineralIncome += nexusBase.getMineralIncome(frames);
		}
		
		return mineralIncome;
	}
	
	/**
	 * Returns number of minerals that will be mined in desired time frame with current
	 * number of mineral workers plus number of workers from parameter numNewWorkers and 
	 * mineral fields - this means that distant predictions to future may be very inaccurate. 
	 * Notice that to mineral workers number is not added number of numNewWorkers - some of
	 * new workers may be added to gas workers - this depends on current situation and demand
	 * for mineral workers. 
	 *  
	 * @param frames - number of frames for which prediction will be computed
     * @param numNewWorkers - number of workers that will be added to current mineral workers
     * or to gas workers (this depends on current situation). Notice that this number is not
     * added only to mineral or gas workers, but it is divided to this two.
     *
	 * @return returns number of minerals that will be mined
	 */
	public int getMineralIncome(int frames, int numNewWorkers) {
		for (int i = 0; i < numNewWorkers; i++) {
			NexusBase smallest = getNexusBaseWithSmallestWorkerRatio();
			
			if (smallest.getCountGasWorkers() < smallest.getMaxGasWorkers()) {
				smallest.fakeGasWorkers++;
			}
			else {
				smallest.fakeMineralWorkers++;
			}
		}
		
		int mineralIncome = getMineralIncome(frames);
		
		resetCountFakeWorkers();
		
		return mineralIncome;
	}
	
	/**
	 * Returns number of gas that will be harvested in desired time frame with current
	 * number of gas workers and geysers - this means that distant predictions 
	 * to future may be very inaccurate.
	 * 
	 * @param frames - number of frames for which prediction will be computed
     *
	 * @return returns number of gas that will be harvested
	 */
	public int getGasIncome(int frames) {
		int gasIncome = 0;
		
		for (NexusBase nexusBase: nexusBases) {
			gasIncome += nexusBase.getGasIncome(frames);
		}
		
		return gasIncome;
	}
	
	/**
	 * Returns number of gas that will be harvested in desired time frame with current
	 * number of gas workers plus number of workers from parameter numNewWorkers and 
	 * geysers - this means that distant predictions to future may be very inaccurate. 
	 * Notice that to gas workers number is not added number of numNewWorkers - some of
	 * new workers may be added to mineral workers - this depends on current situation 
	 * and demand for gas workers. 
	 *  
	 * @param frames - number of frames for which prediction will be computed
     * @param numNewWorkers - number of workers that will be added to current gas workers
     * or to mineral workers (this depends on current situation). Notice that this number is not
     * added only to gas or mineral workers, but it is divided to this two.
     *
	 * @return returns number of gas that will be harvested
	 */
	public int getGasIncome(int frames, int numNewWorkers) {
		for (int i = 0; i < numNewWorkers; i++) {
			NexusBase smallest = getNexusBaseWithSmallestWorkerRatio();
			
			if (smallest.getCountGasWorkers() < smallest.getMaxGasWorkers()) {
				smallest.fakeGasWorkers++;
			}
			else {
				smallest.fakeMineralWorkers++;
			}
		}
		
		int gasIncome = getGasIncome(frames);
		
		resetCountFakeWorkers();
		
		return gasIncome;
	}
	
	/**
	 * Orders WorkerManager to build worker in first Nexus. If worker cannot be built
	 * in first Nexus, it will wait until he can build worker. Building workers can be
	 * queued. 
	 */
	public void buildWorker() {
		if (nexusBases.size() > 0) {
			numWorkersUntilOpening++;
			
			for (NexusBase nexusBase: nexusBases) {
				if (nexusBase.buildWorkers()) {
					return;
				}
			}
			
			numWorkersToBuild++;
		}
	}
	
	/**
	 * Free one worker nearest to x,y position from WorkerManager and returns it UnitID.
	 * 
	 * @param x - x-coordinate
	 * @param y - y-coordinate
	 * @return Returns UnitID of freed worker nearest to x,y position. If none worker is present in 
	 * WorkerManager, it returns -1.
	 */
	public int getWorker(int x, int y) {
		Position target = new Position(x, y);
		
		Unit nearestWorker = null;
		Position nearestPos = new Position(-1, -1);
		
		for (int i = 0; i < 2; i++) {
			for (Unit worker: allWorkers) {
				boolean isGasWorker = false;
				
				if (i == 0) {
					for (NexusBase nexusBase: nexusBases) {
						for (Unit nexusWorker: nexusBase.gasWorkers) {
							if (nexusWorker == worker) {
								isGasWorker = true;
								break;
							}
						}
						
						if (isGasWorker) {
							break;
						}
					}
				}
				
				if (!isGasWorker || i == 1) {
					Position currentPos = new Position(worker.getX(), worker.getY());
					
					if (nearestWorker == null || target.distance(currentPos) < target.distance(nearestPos)) {
						nearestWorker = worker;
						nearestPos = currentPos;
					}
				}
			}
			
			if (nearestWorker != null) {
				break;
			}
		}
		
		if (nearestWorker != null) {
			for (NexusBase nexusBase: nexusBases) {
				nexusBase.deleteWorker(nearestWorker);
				unassignedWorkers.remove(nearestWorker);
			}

			return nearestWorker.getID();
		}
		else {
			return -1;
		}
	}
	
	/**
	 * Adds worker to WorkerManager.
	 * 
	 * @param worker - worker instance that will be added to WorkerManager.
	 */
	public void addWorker(Unit worker) {
		if (!allWorkers.contains(worker)) {
			allWorkers.add(worker);
		}
		
		for (NexusBase nexusBase: nexusBases) {
			nexusBase.deleteWorker(worker);
		}			
		
		NexusBase nexusBaseWithSmallestWorkerRatio = getNexusBaseWithSmallestWorkerRatio();
		NexusBase nearestNexusBase = getNearestNexusBase(new Position(worker.getX(), worker.getY()));
		Position workerPosition = new Position(worker.getX(), worker.getY());
		
		if (nexusBaseWithSmallestWorkerRatio != null && nearestNexusBase != null) {
			if (!isPathClear(getNearestNexusBase(workerPosition).base, nexusBaseWithSmallestWorkerRatio.base)) {
				nearestNexusBase.addWorker(worker);
			}
			else {			
				nexusBaseWithSmallestWorkerRatio.addWorker(worker);
			}
		}
		else {
			unassignedWorkers.add(worker);
		}
	}
	
	/**
	 * Returns nearest NexusBase class to position.
	 * 
	 * @param pos - position to which will be nearest NexusBase finded.
	 * @return nearest NexusBase.
	 */
	public NexusBase getNearestNexusBase(Position pos) {
		NexusBase nearest = null;
		Position posNearest = new Position(-1,-1);
		
		for (NexusBase nexusBase: nexusBases) {
			Position posCurrent = new Position(nexusBase.nexus.getX(), nexusBase.nexus.getY());
			
			if (nearest == null || posCurrent.distance(pos) < posNearest.distance(pos)) {
				nearest = nexusBase;
				posNearest = posCurrent;
			}
		}
		
		return nearest;
	}
	
	/**
	 * Retrieves number of workers that can be build.
     *
	 * @return number of workers.
	 */
	public int getWorkersNumToBuild() {
		return getMaxAllWorkers() - (getWorkersNumIsTraining() + allWorkers.size());
	}
	
	/**
	 * Returns whether WorkerManager has maximum number of workers and building
	 * more workers will be useless and WorkerManager will not employ them.  
     *
	 * @return whether WorkerManager is saturated.
	 */
	public boolean isSaturated() {
		return getWorkersNumToBuild() == 0;
	}
	
	/**
	 * Assigns resources to WorkerManager.
	 *
	 * @param minerals - minerals that will be added to actual manager minerals
	 * @param gas - gas that will be added to actual manager gas
	 */
	/*public void setAddResources(int minerals, int gas) {
		this.minerals += minerals;
		this.gas += gas;
	}*/
	
	/**
	 * Method sets all numbers of fake workers in all NexusBases to zero.
	 */
	private void resetCountFakeWorkers() {
		for (NexusBase nexusBase: nexusBases) {
			nexusBase.fakeMineralWorkers = 0;
			nexusBase.fakeGasWorkers = 0;
		}
	}
	
	/**
	 *  Store into variables mineralsRatioForBuild and gasRatioForBuild ratio of minerals and gas
	 *  of buildings, units, techs and upgrades that will be built in near future.
	 */
	private void updateDesiredResources() {
		ArmyComposition armyComposition = boss.getArmyCompositionManager().getDesiredArmyComposition();
		
		ArrayList<Integer> buildings = new ArrayList<Integer>(boss.getBuildManager().getConstructionPlans());
		ArrayList<Integer> units = armyComposition.getUnitTypes();
		ArrayList<Integer> upgrades = armyComposition.getUpgrades();
		ArrayList<Integer> technologies = armyComposition.getTechnologies();
		
		int tempMinerals = 0;
		int tempGas = 0;
		
		double mineralsRatio = 0;
		double gasRatio = 0;
		
		for (Integer buildingID: buildings) {
			UnitType unitType = game.getUnitType(buildingID);
			
			if (unitType != null) {
				tempMinerals += unitType.getMineralPrice();
				tempGas += unitType.getGasPrice();
			}
		}
		
		for (Integer upgradeID: upgrades) {
			UpgradeType upgradeType = game.getUpgradeType(upgradeID);
			
			if (upgradeType != null) {
				tempMinerals += upgradeType.getMineralPriceBase() + upgradeType.getMineralPriceFactor() * game.getSelf().upgradeLevel(upgradeID);
				tempGas += upgradeType.getGasPriceBase() + upgradeType.getGasPriceFactor() * game.getSelf().upgradeLevel(upgradeID);
			}
		}
		
		for (Integer techID: technologies) {
			TechType techType = game.getTechType(techID);
			
			if (techType != null) {
				tempMinerals += techType.getMineralPrice();
				tempGas += techType.getGasPrice();
			}
		}	
		
		double div = (tempMinerals + tempGas <= 0) ? 1 : tempMinerals + tempGas;
		mineralsRatio = (double)tempMinerals / div;
		gasRatio = (double)tempGas / div;
		
		tempMinerals = 0;
		tempGas = 0;
		
		for (Integer unitID: units) {
			tempMinerals += (double)game.getUnitType(unitID).getMineralPrice() / 100.0 * armyComposition.getRatio(unitID);
			tempGas += (double)game.getUnitType(unitID).getGasPrice() / 100.0 * armyComposition.getRatio(unitID);
		}
		
		div = (tempMinerals + tempGas <= 0) ? 1 : tempMinerals + tempGas;
		double unitsMineralRatio = (double)tempMinerals / div;
		double unitsGasRatio = (double)tempGas / div;
		
		mineralsRatioForBuild = (mineralsRatio + unitsMineralRatio) / 2;
		gasRatioForBuild = (gasRatio + unitsGasRatio) / 2;
		
		if (gasRatioForBuild == 0) {
			mineralsRatioForBuild = 1;
		}
	}
	
	private void computeMineralAndGasWorkersRatio() {
		double mineralWorkers = 0;
		double gasWorkers = 0;
		double allWorkers = 0;
		
		for (NexusBase nexusBase: nexusBases) {
			mineralWorkers += nexusBase.mineralWorkers.size();
			
			double currentGasWorkers = 0;
			
			for (Map.Entry<Unit, ArrayList<Unit>> assimilator: nexusBase.assimilators.entrySet()) {
				if (assimilator.getKey().getResources() == 0) {
					currentGasWorkers += assimilator.getValue().size();
				}
				else {
					currentGasWorkers += assimilator.getValue().size() * 2;
				}
			}
			
			gasWorkers += currentGasWorkers;
			
			allWorkers += nexusBase.mineralWorkers.size() + currentGasWorkers;  
		}
		
		if (allWorkers == 0) {
			allWorkers = 1;
		}
		
		mineralWorkersRatio = mineralWorkers / allWorkers;
		gasWorkersRatio = gasWorkers / allWorkers;
	}

	/**
	 * Retrieves base with Nexus which have smallest worker ratio (count of mineral and gas workers 
	 * to max mineral and gas workers that this base at most can have).
	 * 
	 * @return NexusBase instance with smallest worker ratio
	 */
	private NexusBase getNexusBaseWithSmallestWorkerRatio() {
		NexusBase base = null;
		double ratio = Double.MAX_VALUE;
		
		for (NexusBase nexusBase: nexusBases) {
			double currentRatio = nexusBase.getWorkerRatio();
			
			if (base == null || currentRatio < ratio) {
				base = nexusBase;
				ratio = currentRatio;
			}
		}
		
		return base;
	}
	
	/**
	 * Retrieves base with Nexus which have largest worker ratio (count of mineral and gas workers 
	 * to max mineral and gas workers that this base at most can have).
     *
	 * @return NexusBase instance with smallest worker ratio
	 */
	private NexusBase getNexusBaseWithLargestWorkerRatio() {
		NexusBase base = null;
		double ratio = Double.MIN_VALUE;
		
		for (NexusBase nexusBase: nexusBases) {
			double currentRatio = nexusBase.getWorkerRatio();
			
			if (base == null || currentRatio > ratio) {
				base = nexusBase;
				ratio = currentRatio;
			}
		}
		
		return base;
	}
	
	/**
	 * Retrieves max number of workers that can be build in all bases to 
	 * maximum effectiveness.
     *
	 * @return number of workers.
	 */
	private int getMaxAllWorkers() {
		int result = 0;
		
		for (NexusBase nexusBase: nexusBases) {
			result += nexusBase.getMaxAllWorkers();
		}
		
		return result;
	}
	
	/**
	 * Retrieves number of workers that is currently building in all Nexuses.
     *
	 * @return number of workers.
	 */
	private int getWorkersNumIsTraining() {
		int result = 0;
		
		for (NexusBase nexusBase: nexusBases) {
			result += nexusBase.nexus.isTraining() ? 1 : 0;
		}
		
		return result;
	}

	/**
	 * Checks wether some enemy units is in path between baseFrom and baseTo locations.
     *
	 * @return true if between baseFrom and baseTo is not any enemy unit. 
	 */
	private boolean isPathClear(BaseLocation baseFrom, BaseLocation baseTo) {
		if (baseFrom == baseTo) {
			return true;
		}
		
		ArrayList<Position> groundPath = null;

		Iterator it = groundPaths.entrySet().iterator();
			
	    while (it.hasNext()) {
	    	Map.Entry pair = (Map.Entry)it.next();
		        
		    if ((((Pair)pair.getKey()).left == baseFrom && ((Pair)pair.getKey()).right == baseTo) ||
		        (((Pair)pair.getKey()).left == baseTo && ((Pair)pair.getKey()).right == baseFrom)) {
		    	groundPath = (ArrayList<Position>)pair.getValue();
		    }
		}
	    
	    if (groundPath == null) {
	    	groundPath = game.getGroundPath(baseFrom.getX() / 32, baseFrom.getY() / 32, baseTo.getX() / 32, baseTo.getY() / 32);
			groundPaths.put(new Pair<BaseLocation, BaseLocation>(baseFrom, baseTo), groundPath);
	    }
		
	    Collection<Unit> enemyUnits = boss.getOpponentPositioning().getEnemyUnits();
	    it = enemyUnits.iterator();
		
	    while (it.hasNext()) {
	    	Unit enemyUnit = (Unit)it.next();
	    	WeaponType groundWeaponType = game.getWeaponType(game.getUnitType(enemyUnit.getTypeID()).getGroundWeaponID());
	    	int enemyUnitGroundRange = 0;

	    	if (groundWeaponType.getID() != WeaponTypes.None.ordinal()) {
	    		enemyUnitGroundRange = groundWeaponType.getMaxRange();
	    	}
	    	
	    	if (enemyUnitGroundRange > 0) {
		    	Position enemyUnitPosition = new Position(enemyUnit.getX(), enemyUnit.getY());
		    	
		    	for (Position position: groundPath) {
		    		Position currentPosition = new Position(position.x * 32 + 16, position.y * 32 + 16);
		    		
		    		if (currentPosition.distance(enemyUnitPosition) < enemyUnitGroundRange) {
		    			return false;
		    		}
		    	}
	    	}
	    }
	    
	    return true;
	}
	
	/**
	 * Transfers worker from base with smallest worker ratio to base with largest 
	 * worker ratio.
	 */
	private void transferWorker() {
		NexusBase smallest = getNexusBaseWithSmallestWorkerRatio();
		NexusBase largest = getNexusBaseWithLargestWorkerRatio();
		
		if (smallest != null && largest != null && smallest != largest && smallest.getWorkerRatio() < 1.0 && largest.getWorkerRatio() - smallest.getWorkerRatio() >= 0.15) {
			Unit worker = largest.getWorker();
			
			if (worker != null && isPathClear(smallest.base, largest.base)) {
				largest.deleteWorker(worker);
				if (smallest.addWorker(worker)) {
					transferedWorkers.put(worker, smallest);
				}
				else {
					unassignedWorkers.add(worker);
				}
			}
		}
	}
	
	/**
	 * Checks if some of transfered workers is near to base to which is transfered
	 * and checks whether the base to which is worker transfered is still clear
	 */
	private void checkTranferedWorkers() {
		for (Map.Entry<Unit, NexusBase> transferedWorker: transferedWorkers.entrySet()) {
			Position workerPosition = new Position(transferedWorker.getKey().getX(), transferedWorker.getKey().getY());
			Position nexusBasePosition = new Position(transferedWorker.getValue().nexus.getX(), transferedWorker.getValue().nexus.getY());
			
			if (workerPosition.distance(nexusBasePosition) < 320) {
				transferedWorkers.remove(transferedWorker.getKey());
				break;
			}
		}
		
		for (Map.Entry<Unit, NexusBase> transferedWorker: transferedWorkers.entrySet()) {
			Unit worker = transferedWorker.getKey();
			NexusBase transferedBase = transferedWorker.getValue();
			NexusBase nearestNexusBase = getNearestNexusBase(new Position(worker.getX(), worker.getY()));
			
			if (nearestNexusBase != null && !isPathClear(nearestNexusBase.base, transferedBase.base)) {
				transferedBase.deleteWorker(worker);
				unassignedWorkers.add(worker);
				transferedWorkers.remove(worker);
				
				if (capitalNexusBase != null && isPathClear(nearestNexusBase.base, capitalNexusBase.base)) {
					game.move(worker.getID(), capitalNexusBase.base.getX(), capitalNexusBase.base.getY());
				}
				
				break;
			}
		}
	}
	
	/** 
	 * Checks whether mineral field exists.
	 * 
	 *  @param mineral - mineral field to check for existence
	 *  @return true if mineral field exists, false otherwise
	 */
	private boolean mineralExists(Unit mineral) {
		ArrayList<Unit> neutralUnits = game.getNeutralUnits();
		
		for (Unit neutralUnit: neutralUnits) {
			if (neutralUnit.getX() == mineral.getX() && neutralUnit.getY() == mineral.getY()) {
				return true;
			}
		}
		
		return false;
	}
	
	/** 
	 * Checks whether unit exists.
	 * 
	 *  @param unit - unit to check for existence
	 *  @return true if unit exists, false otherwise
	 */
	private boolean unitExists(Unit unit) {
		ArrayList<Unit> myUnits = game.getMyUnits();
		
		for (Unit currentUnit: myUnits) {
			if (currentUnit == unit) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Draws on map manager debug information.
	 */
	private void drawDebugInfo() {
		DecimalFormat df = new DecimalFormat("#.##");
		
		String debug = "";
		debug += " awork: " + allWorkers.size();
		debug += "  uwork: " + unassignedWorkers.size();
		debug += "  saturation: ";
		
		int i = 1;
		for (NexusBase nexusBase: nexusBases) {
			nexusBase.drawDebugInfo();
			debug += (i > 1 ? ", " : "") + df.format(nexusBase.getWorkerRatio());
			i++;
		}
		
		debug += "  min. ratio: " + df.format(mineralWorkersRatio) +  "/" + df.format(mineralsRatioForBuild) + " gas ratio: " + df.format(gasWorkersRatio) +  "/" + df.format(gasRatioForBuild);
		
		game.drawText(6, 48, debug, true);
		
		for (Map.Entry<Unit, NexusBase> transferedWorker: transferedWorkers.entrySet()) {
			Position workerPosition = new Position(transferedWorker.getKey().getX(), transferedWorker.getKey().getY());
			Position nexusBasePosition = new Position(transferedWorker.getValue().nexus.getX(), transferedWorker.getValue().nexus.getY());
			
			game.drawLine((int)workerPosition.x, (int)workerPosition.y, (int)nexusBasePosition.x, (int)nexusBasePosition.y, BWColor.ORANGE, false);
		}
	}
	
	
	private class NexusBase {
		
		private WorkerManager manager;
		private JNIBWAPI game;
		
		public Unit nexus;
		
		public BaseLocation base = null;
		
		// Each mineral field of minerals belonging to this BaseLocation and it's workers, 
		// which will mine this mineral field
		public HashMap<Unit, ArrayList<Unit>> minerals = new HashMap<Unit, ArrayList<Unit>>();
		
		// Each Assimilator belonging to this Nexus and it's workers, which will harvest 
		// harvest gas from this Assimilator
		public HashMap<Unit, ArrayList<Unit>> assimilators = new HashMap<Unit, ArrayList<Unit>>();
		
		// All workers that will mine minerals assigned to this Nexus
		public ArrayList<Unit> mineralWorkers = new ArrayList<Unit>();
		
		// All workers that will harvest gas assigned to this Nexus
		public ArrayList<Unit> gasWorkers = new ArrayList<Unit>();
		
		// Store frame in which worker started to mine mineral
		private HashMap<Unit, Integer> startMiningFrame = new HashMap<Unit, Integer>();
		
		// Numbers of workers only added to compute prediction of mineral or gas income with new workers number
		public int fakeMineralWorkers = 0;
		public int fakeGasWorkers = 0;
		
		
		/**
		 * NexusBase constructor
		 *
		 * @param manager - instance of WorkerManager class
		 * @param nexus - new created nexus
		 */
		public NexusBase(WorkerManager manager, Unit nexus) {
			this.manager = manager;
			this.nexus = nexus;
			game = manager.boss.game;
			
			// Find nearest BaseLocation to this Nexus
			Position nexusPos = new Position(nexus.getX(), nexus.getY());
			
			BaseLocation nearestBase = null;
			Position posNearest = new Position(-1,-1);
			
			for (BaseLocation base: game.getMap().getBaseLocations()) {
				Position posCurrent = new Position(base.getX(), base.getY());
				
				if (nearestBase == null || nexusPos.distance(posCurrent) < nexusPos.distance(posNearest)) {
					nearestBase = base;
					posNearest = new Position(nearestBase.getX(), nearestBase.getY());
				}
			}
			
			if (nearestBase != null) {
				base = nearestBase;
				
				// Initialize list of minerals assigned to this BaseLocation
				for (Unit mineral: base.getStaticMinerals()) {
					if (mineral.isExists()) {
					    minerals.put(mineral, new ArrayList<Unit>());
					}
				}
			}
		}
		
		/**
		 * Updates frame in which workers started to mine mineral field
		 */
		public void updateStartMiningFrames()  {
			for (Map.Entry<Unit, Integer> worker: startMiningFrame.entrySet()) {
				if (worker.getValue() == -1 && (worker.getKey().getOrderID() == 87 || worker.getKey().getOrderID() == 83) /* order MiningMinerals, HarverstGas */) {
					worker.setValue(game.getFrameCount());
				}
				else if (worker.getValue() != -1 && worker.getKey().getOrderID() != 87 && worker.getKey().getOrderID() != 83 /* order MiningMinerals, HarverstGas */) {
					worker.setValue(-1);
				}
			}
		}
		
		/**
		 * Moves one mineral worker to gas workers.
		 */
		public void moveOneWorkerToGas() {
			int max = Integer.MIN_VALUE;
			Unit worker = null;
			
			for (Map.Entry<Unit, ArrayList<Unit>> mineral: minerals.entrySet()) {
		    	int size = mineral.getValue().size();

		    	if (size > max && size > 0) {
		    		max = size;
		    		worker = mineral.getValue().get(0);
		    	}
		    }
			
		    if (worker != null && assimilators.size() > 0) {		    	
				boolean allAssimilatorsDepleted = isAllAssimilatorsDepleted();
				
				for (Map.Entry<Unit, ArrayList<Unit>> assimilator: assimilators.entrySet()) {
					ArrayList<Unit> workers = assimilator.getValue();
					
					if (workers.size() < manager.maxWorkersPerGeyser && (assimilator.getKey().getResources() > 0 || allAssimilatorsDepleted)) {
						deleteWorker(worker);
						workers.add(worker);
						gasWorkers.add(worker);
						game.rightClick(worker.getID(), assimilator.getKey().getID());
						break;
					}
				}
		    }
		}
		
		/**
		 * Moves one gas worker to mineral workers.
		 */
		public void moveOneWorkerToMinerals() {
			int max = Integer.MIN_VALUE;
			Unit worker = null;
			
			for (Map.Entry<Unit, ArrayList<Unit>> assimilator: assimilators.entrySet()) {
		    	int size = assimilator.getValue().size();

		    	if (size > max && size > 0) {
		    		max = size;
		    		worker = assimilator.getValue().get(0);
		    	}
		    }
			
			if (worker != null && minerals.size() > 0) {		    	
				for (Map.Entry<Unit, ArrayList<Unit>> mineral: minerals.entrySet()) {
					ArrayList<Unit> workers = mineral.getValue();
					
					if (workers.size() < manager.maxWorkersPerMineralField) {
						deleteWorker(worker);
						workers.add(worker);
						mineralWorkers.add(worker);
						game.rightClick(worker.getID(), mineral.getKey().getID());
						break;
					}
				}
		    }
		}
		
		/**
		 * Adds worker to this NexusBase
		 *
		 * @param worker - worker which will be added to this NexusBase
		 * @return returns wheter worker was added to this NexusBase or not
		 */
		public boolean addWorker(Unit worker) {			
			// If we have 1 or more Assimilator assigned to this Nexus and count of gas workers
			// is smaller than maximum number of workers which can be assigned to this Nexus
			// add worker to gas workers and assign it to first Assimilator which have assigned 
			// workers number smaller to workersPerGeyser constant
			if (assimilators.size() > 0 && getCountGasWorkers() < getMaxGasWorkers()) {
				boolean allAssimilatorsDepleted = isAllAssimilatorsDepleted();
				
				for (Map.Entry<Unit, ArrayList<Unit>> assimilator: assimilators.entrySet()) {
					ArrayList<Unit> workers = assimilator.getValue();
					
					if (workers.size() < manager.maxWorkersPerGeyser && (assimilator.getKey().getResources() > 0 || allAssimilatorsDepleted)) {
						gasWorkers.add(worker);
						startMiningFrame.put(worker, -1);
						
						workers.add(worker);
						game.rightClick(worker.getID(), assimilator.getKey().getID());
						
						return true;
					}
				}
			}
			// Assign worker to nearest mineral field which have minimum workers assigned to
			// otherwise assign this worker to unassignedWorkers list in WorkerManager class 
			else if (minerals.size() > 0) {
				Unit nearestMineral = null;
				Position nexusPos = new Position(nexus.getX(), nexus.getY());
				Position posNearest = new Position(-1,-1);
				
				for (int maxWorkers = 0; maxWorkers < Math.ceil(maxWorkersPerMineralField); maxWorkers++) {
					for (Map.Entry<Unit, ArrayList<Unit>> mineral: minerals.entrySet()) {
						Position posCurrent = new Position(mineral.getKey().getX(), mineral.getKey().getY());
						
						if ((double)mineral.getValue().size() <= maxWorkers) {
							if (nearestMineral == null || posCurrent.distance(nexusPos) < posNearest.distance(nexusPos)) {
								nearestMineral = mineral.getKey();
							}
						}
					}
					
					if (nearestMineral != null) {
						break;
					}
				}
				
				if (nearestMineral != null) {
					for (Map.Entry<Unit, ArrayList<Unit>> mineral: minerals.entrySet()) {
						if (mineral.getKey() == nearestMineral) {
							mineral.getValue().add(worker);
							break;
						}
					}
					
					mineralWorkers.add(worker);
					startMiningFrame.put(worker, -1);
					
					return true;
				}
				else {
					manager.unassignedWorkers.add(worker);
				}
			}
			else {
				manager.unassignedWorkers.add(worker);
			}
			
			return false;
		}
		
		/**
		 * Adds worker to this NexusBase
		 *
		 * @param worker - worker which will be added to this NexusBase
		 */
		public void deleteWorker(Unit worker) {
			if (gasWorkers.contains(worker)) {
				gasWorkers.remove(worker);
				startMiningFrame.remove(worker);
			}
			
			for (Map.Entry<Unit, ArrayList<Unit>> assimilator: assimilators.entrySet()) {
				if (assimilator.getValue().contains(worker)) {
					assimilator.getValue().remove(worker);
					break;
				}
			}
			
			if (mineralWorkers.contains(worker)) {
				mineralWorkers.remove(worker);
				startMiningFrame.remove(worker);	
			}
			
			for (Map.Entry<Unit, ArrayList<Unit>> mineral: minerals.entrySet()) {
				if (mineral.getValue().contains(worker)) {
					mineral.getValue().remove(worker);
					break;
				}
			}
		}
		
		/**
		 * Get one worker unit from NexusBase.
		 *
		 * @return worker unit
		 */
		public Unit getWorker() {
			if (mineralWorkers.size() > 0) {
				return mineralWorkers.get(0);
			}
			else if (gasWorkers.size() > 0) {
				return gasWorkers.get(0);
			}
			else {
				return null;
			}
		}
		
		/**
		 * Returns if all Assimilators assigned to this base is depleted or not.
		 *
		 * @return boolean value if all Assimilators is depleted
		 */
		public boolean isAllAssimilatorsDepleted() {
			for (Map.Entry<Unit, ArrayList<Unit>> assimilator: assimilators.entrySet()) {
				if (assimilator.getKey().getResources() > 0) {
					return false;
				}
			}
			
			return true;
		}
		
		/**
		 * Checks all minerals in this BaseLocation and deletes it from list and assigns all
		 * mineral workers to unassignedWorkers list in WorkerManager
		 */
		public void checkMinerals() {
			Iterator<Entry<Unit, ArrayList<Unit>>> it = minerals.entrySet().iterator();

		    while (it.hasNext()) {
		    	Map.Entry<Unit, ArrayList<Unit>> pair = (Entry<Unit, ArrayList<Unit>>)it.next();
		        if (!mineralExists(pair.getKey())/* pair.getKey().getResources() < 8*/) {
		        	manager.unassignedWorkers.addAll(pair.getValue());
			        mineralWorkers.removeAll(pair.getValue());
			        it.remove();
			        break;
		        }
		    }
		}
		
		/**
		 * Checks wether new assimilators was created and create item for this assimilator
		 * in assimilators list
		 */
		public void checkAssimilators() {
			ArrayList<Unit> myUnits = boss.game.getMyUnits();
			
			for (Unit unit: myUnits) {
				if (unit.getTypeID() == UnitTypes.Protoss_Assimilator.ordinal() && unit.isCompleted()) {
					if (!assimilators.keySet().contains(unit)) {
						Position assimilatorPos = new Position(unit.getX(), unit.getY());
						boolean isNewAssimilator = false;
						
						for (Unit geyser: base.getGeysers()) {
							Position currentPos = new Position(geyser.getX(), geyser.getY());
							
							if (currentPos.distance(assimilatorPos) <= 3*32) {
								isNewAssimilator = true;
							}
						}
						
						if (isNewAssimilator) {
							assimilators.put(unit, new ArrayList<Unit>());
						}
					}
				}
			}
			
			Iterator<Entry<Unit, ArrayList<Unit>>> it = assimilators.entrySet().iterator();

		    while (it.hasNext()) {
		    	Map.Entry<Unit, ArrayList<Unit>> pair = (Entry<Unit, ArrayList<Unit>>)it.next();
		        if (!pair.getKey().isExists()) {
			        manager.unassignedWorkers.addAll(pair.getValue());
			        gasWorkers.removeAll(pair.getValue());
			        it.remove();
			        break;
		        	
		        }
		    }
		}
		
		/**
		 * Build workers if this NexusBase worker ratio is smaller than 1.0 or
		 * if other NexusBase has worker ratio smaller than 1.0
		 */
		public boolean buildWorkers() {
			if (/*(boss.getWorkerMinerals() >= 50 || boss.getOpeningManager().isActive()) &&*/ !nexus.isTraining() && game.getSelf().getMinerals() >= 50) {
				if (getWorkerRatio() < 1.0 && this.getWorkersNumToBuild() > 0) {
					game.train(nexus.getID(), UnitTypes.Protoss_Probe.ordinal());
					return true;
				}
				else if (unassignedWorkers.size() == 0) {
					for (NexusBase nexusBase: manager.nexusBases) {
						if (manager.isPathClear(base, nexusBase.base) && nexusBase.getWorkerRatio() < 1.0 && manager.getWorkersNumToBuild() > 0) {
							game.train(nexus.getID(), UnitTypes.Protoss_Probe.ordinal());
							return true;
						}
					}
				}
			}
			
			return false;
		}
		
		/**
		 * Mine minerals and harvest gas with assigned workers
		 */
		public void handleWorkers() {
			// Mine assigned minerals
			for (Map.Entry<Unit, ArrayList<Unit>> mineral: minerals.entrySet()) {
				for (Unit worker: mineral.getValue()) {
					if ((worker.isIdle() || worker.isGatheringGas() || worker.getOrderID() == 85) && worker.getOrderTargetID() != mineral.getKey().getID()) {
						game.rightClick(worker.getID(), mineral.getKey().getID());
					}
				}
			}
			
			// Harvest gas
			for (Map.Entry<Unit, ArrayList<Unit>> assimilator: assimilators.entrySet()) {
				for (Unit worker: assimilator.getValue()) {
					if (worker.isIdle() || worker.isGatheringMinerals()) {
						game.rightClick(worker.getID(), assimilator.getKey().getID());
					}
				}
			}
		}
		
		public int getWorkersNumToBuild() {
			return this.getMaxAllWorkers() - ((nexus.isTraining() ? 1 : 0) + mineralWorkers.size() + gasWorkers.size());
		}
		
		public int getCountMineralWorkers() {
			return mineralWorkers.size() + fakeMineralWorkers;
		}
		
		public int getMaxMineralWorkers() {
			return (int)(minerals.size() * maxWorkersPerMineralField);
		}
		
		public int getCountGasWorkers() {
			return gasWorkers.size() + fakeGasWorkers;
		}
		
		public int getMaxGasWorkers() {
			return (int)(assimilators.size() * maxWorkersPerGeyser);
		}
		
		public int getMaxAllWorkers() {
			return getMaxMineralWorkers() + getMaxGasWorkers();
		}
		
		public double getWorkerRatio() {
			double div = getMaxMineralWorkers() + getMaxGasWorkers();
			if (div < 1) return 1;
				
			return (double)((getCountMineralWorkers() + getCountGasWorkers()) / div);
		}
		
		public int getMineralIncome(int frames) {
			if (nexus == null || !nexus.isExists()) {
				return 0;
			}
			
			int tempFakeMineralWorkers = fakeMineralWorkers;
			int mineralIncome = 0;
			
			Position nexusPos = new Position(nexus.getX(), nexus.getY());
			
			for (Map.Entry<Unit, ArrayList<Unit>> mineral: minerals.entrySet()) {
				Position mineralPos = new Position(mineral.getKey().getX(), mineral.getKey().getY());
				int i = 0;
				
				for (Unit worker: mineral.getValue()) {
					if (i >= 2) {
						break;
					}
					
					Position pos = new Position(worker.getX(), worker.getY());
					int workerTime = frames;
					
					double time = frames+1;
					double distToNexus = pos.distance(nexusPos);
					double distToMineral = pos.distance(mineralPos);
					
					if (worker.isCarryingMinerals() && worker.getOrderID() == 90 /* order ReturnMinerals*/) {
						time = distToNexus / probeTopSpeed;
					}
					else if (worker.isGatheringMinerals()) {
						int workerStartMiningFrame = startMiningFrame.get(worker);
						int remainingFrames = 114;
						
						if (workerStartMiningFrame != -1) {
							remainingFrames -= game.getFrameCount() - workerStartMiningFrame;
						}
						
						time = (distToMineral / probeTopSpeed) + remainingFrames;						
						time += distToNexus / probeTopSpeed;
					}
					
					if (workerTime - time >= 0) {
						mineralIncome += 8;
						workerTime -= time;
					}
					
					int mineralsPerTime = (int)((distToNexus / probeTopSpeed) * 2.0 + 114);
					int coef = (int)((double)workerTime / (double)mineralsPerTime);
					
					mineralIncome += (int)((double)(coef * 8.0));
					
					i++;
				}
				
				if (mineral.getValue().size() < 2 && tempFakeMineralWorkers > 0) {
					double distMineralToNexus = mineralPos.distance(nexusPos);
					int mineralsPerTime = (int)((distMineralToNexus / probeTopSpeed) * 2.0 + 114);
					int coef = (int)((double)frames / (double)mineralsPerTime);
					
					for (int j = 0; j < 2 - mineral.getValue().size(); j++) { 
						mineralIncome += (int)((double)(coef * 8.0));
						tempFakeMineralWorkers--;
					}
				}
			}
			
			return mineralIncome;
		}
		
		public int getGasIncome(int frames) {
			if (nexus == null || !nexus.isExists()) {
				return 0;
			}
			
			int tempFakeGasWorkers = fakeGasWorkers;
			int gasIncome = 0;
			
			Position nexusPos = new Position(nexus.getX(), nexus.getY());
			
			for (Map.Entry<Unit, ArrayList<Unit>> assimilator: assimilators.entrySet()) {
				Position assimilatorPos = new Position(assimilator.getKey().getX(), assimilator.getKey().getY());
				int i = 0;
				int income = 8;
				
				if (assimilator.getKey().getResources() <= 0) {
					income = 2;
				}
				
				for (Unit worker: assimilator.getValue()) {
					if (i >= 3) {
						break;
					}
					
					Position pos = new Position(worker.getX(), worker.getY());
					int workerTime = frames;
					
					double time = frames+1;
					double distToNexus = pos.distance(nexusPos);
					double distToGeyser = pos.distance(assimilatorPos);
					
					if (worker.isCarryingGas() && worker.getOrderID() == 84 /* order ReturnGas*/) {
						time = distToNexus / probeTopSpeed;
					}
					else if (worker.isGatheringGas()) {
						int workerStartMiningFrame = startMiningFrame.get(worker);
						int remainingFrames = 60;
						
						if (workerStartMiningFrame != -1) {
							remainingFrames -= game.getFrameCount() - workerStartMiningFrame;
						}
						
						time = (distToGeyser / probeTopSpeed) + remainingFrames;						
						time += distToNexus / probeTopSpeed;
					}
					
					if (workerTime - time >= 0) {
						gasIncome += income;
						workerTime -= time;
					}
					
					int gasPerTime = (int)((distToNexus / probeTopSpeed) * 2.0 + 60);
					int coef = (int)((double)workerTime / (double)gasPerTime);
					
					gasIncome += (int)((double)(coef * income));
					
					i++;
				}
				
				if (assimilator.getValue().size() < 3 && tempFakeGasWorkers > 0) {
					double distAssimilatorToNexus = assimilatorPos.distance(nexusPos);
					int gasPerTime = (int)((distAssimilatorToNexus / probeTopSpeed) * 2.0 + 60);
					int coef = (int)((double)frames / (double)gasPerTime);
					
					for (int j = 0; j < 3 - assimilator.getValue().size(); j++) { 
						gasIncome += (int)((double)(coef * income));
						tempFakeGasWorkers--;
					}
				}
			}
			
			return gasIncome;
		}

		private void drawDebugInfo() {
			for (Map.Entry<Unit, ArrayList<Unit>> mineral: minerals.entrySet()) {
				game.drawText(mineral.getKey().getX()-10, mineral.getKey().getY(), "" + mineral.getValue().size(), false);
				
				for (Unit worker: mineral.getValue()) {
					game.drawLine(mineral.getKey().getX(), mineral.getKey().getY(), worker.getX(), worker.getY(), BWColor.WHITE, false);
				}
			}
			
			for (Map.Entry<Unit, ArrayList<Unit>> assimilator: assimilators.entrySet()) {
				for (Unit worker: assimilator.getValue()) {
					game.drawLine(assimilator.getKey().getX(), assimilator.getKey().getY(), worker.getX(), worker.getY(), BWColor.WHITE, false);
				}
			}
			
			for (Unit worker: mineralWorkers) {
				game.drawLine(nexus.getX(), nexus.getY(), worker.getX(), worker.getY(), BWColor.CYAN, false);
				game.drawBox(worker.getX()-16, worker.getY()-16, worker.getX()+16, worker.getY()+16, BWColor.CYAN, false, false);
			}
			
			for (Unit worker: gasWorkers) {
				game.drawLine(nexus.getX(), nexus.getY(), worker.getX(), worker.getY(), BWColor.GREEN, false);
				game.drawCircle(worker.getX(), worker.getY(), 16, BWColor.GREEN, false, false);
			}
			
		}
		
	}
	
	
	private class Pair<L, R> {
	    private L left;
	    private R right;
	    
	    public Pair(L left, R right) {
	        this.left = left;
	        this.right = right;
	    }
	}
}
