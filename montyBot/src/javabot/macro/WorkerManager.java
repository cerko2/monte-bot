package javabot.macro;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.model.Unit;
import javabot.model.BaseLocation;
import javabot.types.UnitType.UnitTypes;
import javabot.util.BWColor;
import javabot.util.Position;


public class WorkerManager extends AbstractManager {
	
	private final boolean WORKER_MANAGER_DEBUG = true;
	
	private final double maxWorkersPerMineralField = 2.3;
	private final double maxWorkersPerGeyser = 3.0;
	private final int maxWorkersCount = 70;
	
	private Boss boss;
	private JNIBWAPI game;
	
	// Minerals and gas assigned to this manager by Boss
	private int minerals = 0;
	private int gas = 0;
	
	// All Nexus bases which bot have
	public ArrayList<NexusBase> nexusBases = new ArrayList<NexusBase>();
	
	// All workers assigned to this manager
	public ArrayList<Unit> allWorkers = new ArrayList<Unit>();
	
	// All workers that isn't assigned to any NexusBase
	public ArrayList<Unit> unassignedWorkers = new ArrayList<Unit>();
	
	// Queued number of workers to be built
	int numWorkersToBuild = 0;
	
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
		ArrayList<Unit> myUnits = boss.game.getMyUnits();
		ArrayList<Unit> myNexuses = new ArrayList<Unit>();
		
		// Create list of all my Nexuses
		for (Unit unit: myUnits) {
			if (unit.getTypeID() == UnitTypes.Protoss_Nexus.ordinal() && unit.isCompleted()) {
				myNexuses.add(unit);
			}
		}
		
		// Find out wether new Nexus is completed and is not added into nexusBases list
		for (Unit unit: myNexuses) {
			boolean newNexus = true;
				
			for (NexusBase nexusBase: nexusBases) {
				if (nexusBase.nexus == unit) {
					newNexus = false;
				}
			}
			
			if (newNexus) {
				nexusBases.add(new NexusBase(this, unit));
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
		
		// Finds out wether new worker was assigned to this manager and is not assigned 
		// to some NexusBase
		for (Unit worker: units) {
			if (!allWorkers.contains(worker)) {
				allWorkers.add(worker);
				
				NexusBase nexusBaseWithSmallestWorkerRatio = getNexusBaseWithSmallestWorkerRatio();
				NexusBase nearestNexusBase = getNearestNexusBase(new Position(worker.getX(), worker.getY()));
				
				if (nexusBaseWithSmallestWorkerRatio != null && nearestNexusBase != null) {
					if (Math.abs(nexusBaseWithSmallestWorkerRatio.getWorkerRatio() - nearestNexusBase.getWorkerRatio()) <= 0.15 && nearestNexusBase.getWorkerRatio() < 1.0) {
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
		}
		
		if (unassignedWorkers.size() > 0) {
			Unit worker = unassignedWorkers.get(0);
			
			NexusBase nexusBaseWithSmallestWorkerRatio = getNexusBaseWithSmallestWorkerRatio();
			NexusBase nearestNexusBase = getNearestNexusBase(new Position(worker.getX(), worker.getY()));
			
			if (nexusBaseWithSmallestWorkerRatio != null && nearestNexusBase != null) {
				if (Math.abs(nexusBaseWithSmallestWorkerRatio.getWorkerRatio() - nearestNexusBase.getWorkerRatio()) <= 0.15 && nearestNexusBase.getWorkerRatio() < 1.0) {
					nearestNexusBase.addWorker(worker);
				}
				else {
					nexusBaseWithSmallestWorkerRatio.addWorker(worker);
				}
				
				unassignedWorkers.remove(worker);
			}
		}
		
		// Finds out wether some worker was unassigned by boss from this manager and deletes
		// it from NexusBase
		for (Unit worker: allWorkers) {
			if (!units.contains(worker)) {
				for (NexusBase nexusBase: nexusBases) {
					nexusBase.deleteWorker(worker);
				}
				
				allWorkers.remove(worker);
				unassignedWorkers.remove(worker);
				break;
			}
		}

		// Handle all NexusBase instances
		for (NexusBase nexusBase: nexusBases) {
			nexusBase.checkMinerals();
			nexusBase.checkAssimilators();
			if (unassignedWorkers.size() == 0 && getWorkersNumIsTraining() + allWorkers.size() < maxWorkersCount && !boss.getOpeningManager().isActive()) {
				nexusBase.buildWorkers();
			}
			nexusBase.handleWorkers();
		}
		
		if (numWorkersToBuild > 0) {
			if (nexusBases.get(0).buildWorkers()) {
				numWorkersToBuild--;
			}
		}
		
		// Transfer worker from base with largest worker ratio to base with smallest
		// worker ratio
		transferWorker();
		
		// Show manager debug info on the map
		if (WORKER_MANAGER_DEBUG) {
			drawDebugInfo();
		}
	}
	
	/**
	 * Orders WorkerManager to build worker in first Nexus. If worker cannot be built
	 * in first Nexus, it will wait until he can build worker. Building workers can be
	 * queued. 
	 */
	public void buildWorker() {
		if (nexusBases.size() > 0) {
			if (!nexusBases.get(0).buildWorkers()) {
				numWorkersToBuild++;
			}
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
		
		for (Unit worker: allWorkers) {
			Position currentPos = new Position(worker.getX(), worker.getY());
			
			if (nearestWorker == null || target.distance(currentPos) < target.distance(nearestPos)) {
				nearestWorker = worker;
				nearestPos = currentPos;
			}
		}
		
		if (nearestWorker != null) {
			for (NexusBase nexusBase: nexusBases) {
				nexusBase.deleteWorker(nearestWorker);
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
		if (nexusBaseWithSmallestWorkerRatio != null) {
			nexusBaseWithSmallestWorkerRatio.addWorker(worker);
		}
		else {
			unassignedWorkers.add(worker);
		}
	}
	
	/**
	 * Assigns resources to WorkerManager.
	 *
	 * @param minerals - minerals that will be added to actual manager minerals
	 * @param gas - gas that will be added to actual manager gas
	 */
	public void setAddResources(int minerals, int gas) {
		this.minerals += minerals;
		this.gas += gas;
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
	 * Retrieves number of workers that can be build.

	 * @return number of workers.
	 */
	public int getWorkersNumToBuild() {
		return getMaxAllWorkers() - (getWorkersNumIsTraining() + allWorkers.size());
	}
	
	/**
	 * Transfers worker from base with smallest worker ratio to base with largest 
	 * worker ratio.
	 * 
	 * @return number of workers.
	 */
	private void transferWorker() {
		NexusBase smallest = getNexusBaseWithSmallestWorkerRatio();
		NexusBase largest = getNexusBaseWithLargestWorkerRatio();
		
		if (smallest != null && largest != null && smallest != largest && largest.getWorkerRatio() - smallest.getWorkerRatio() >= 0.15) {
			Unit worker = largest.getWorker();
			
			if (worker != null) {
				largest.deleteWorker(worker);
				smallest.addWorker(worker);
			}
		}
	}
	
	/**
	 * Transfers worker from base with smallest worker ratio to base with largest 
	 * worker ratio.
	 * 
	 * @return number of workers.
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
	 * Draws on map manager debug information.
	 */
	private void drawDebugInfo() {
		DecimalFormat df = new DecimalFormat("#.##");
		
		String debug = "";
		debug += "work: " + allWorkers.size();
		debug += "  un. work: " + unassignedWorkers.size();
		debug += "  ratio: ";
		
		int i = 1;
		for (NexusBase nexusBase: nexusBases) {
			nexusBase.drawDebugInfo();
			debug += (i > 1 ? ", " : "") + df.format(nexusBase.getWorkerRatio());
			i++;
		}
		
		game.drawText(6, 48, debug, true);
	}
	
	
	private class NexusBase {
		
		private WorkerManager manager;
		private JNIBWAPI game;
		
		public Unit nexus;
		
		private BaseLocation base = null;
		
		// Each mineral field of minerals belonging to this BaseLocation and it's workers, 
		// which will mine this mineral field
		private HashMap<Unit, ArrayList<Unit>> minerals = new HashMap<Unit, ArrayList<Unit>>();
		
		// Each Assimilator belonging to this Nexus and it's workers, which will harvest 
		// harvest gas from this Assimilator
		private HashMap<Unit, ArrayList<Unit>> assimilators = new HashMap<Unit, ArrayList<Unit>>();
		
		// All workers that will mine minerals assigned to this Nexus
		public ArrayList<Unit> mineralWorkers = new ArrayList<Unit>();
		
		// All workers that will harvest gas assigned to this Nexus
		public ArrayList<Unit> gasWorkers = new ArrayList<Unit>();
		
		
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
		 * Adds worker to this NexusBase
		 *
		 * @param worker - worker which will be added to this NexusBase
		 */
		public void addWorker(Unit worker) {
			// If we have 1 or more Assimilator assigned to this Nexus and count of gas workers
			// is smaller than maximum number of workers which can be assigned to this Nexus
			// add worker to gas workers and assign it to first Assimilator which have assigned 
			// workers number smaller to workersPerGeyser constant
			if (assimilators.size() > 0 && getCountGasWorkers() < getMaxGasWorkers()) {
				gasWorkers.add(worker);
				
				for (Map.Entry<Unit, ArrayList<Unit>> assimilator: assimilators.entrySet()) {
					ArrayList<Unit> workers = assimilator.getValue();
					
					if (workers.size() < manager.maxWorkersPerGeyser) {
						workers.add(worker);
						break;
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
				}
				else {
					manager.unassignedWorkers.add(worker);
				}
			}
			else {
				manager.unassignedWorkers.add(worker);
			}
		}
		
		/**
		 * Adds worker to this NexusBase
		 *
		 * @param worker - worker which will be added to this NexusBase
		 */
		public void deleteWorker(Unit worker) {
			if (mineralWorkers.contains(worker)) {
				mineralWorkers.remove(worker);
				
				for (Map.Entry<Unit, ArrayList<Unit>> mineral: minerals.entrySet()) {
					if (mineral.getValue().contains(worker)) {
						mineral.getValue().remove(worker);
						break;
					}
				}
			}
			else if (gasWorkers.contains(worker)) {
				gasWorkers.remove(worker);
				
				for (Map.Entry<Unit, ArrayList<Unit>> assimilator: assimilators.entrySet()) {
					if (assimilator.getValue().contains(worker)) {
						assimilator.getValue().remove(worker);
						break;
					}
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
		 * Checks all minerals in this BaseLocation and deletes it from list and assigns all
		 * mineral workers to unassignedWorkers list in WorkerManager
		 */
		public void checkMinerals() {
			Iterator<Entry<Unit, ArrayList<Unit>>> it = minerals.entrySet().iterator();

		    while (it.hasNext()) {
		    	Map.Entry<Unit, ArrayList<Unit>> pair = (Entry<Unit, ArrayList<Unit>>)it.next();
		        if (pair.getKey().getResources() < 8) {
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
			if (/*manager.minerals >= 50 &&*/ !nexus.isTraining() && game.getSelf().getMinerals() >= 50) {
				if (getWorkerRatio() < 1.0 && manager.getWorkersNumToBuild() > 0) {
					game.train(nexus.getID(), UnitTypes.Protoss_Probe.ordinal());
					return true;
				}
				else {
					for (NexusBase nexusBase: manager.nexusBases) {
						if (nexusBase.getWorkerRatio() < 1.0 && manager.getWorkersNumToBuild() > 0) {
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
					if ((worker.isIdle() || worker.getOrderID() == 85) && worker.getOrderTargetID() != mineral.getKey().getID()) {
						game.rightClick(worker.getID(), mineral.getKey().getID());
					}
				}
			}
			
			// Harvest gas
			for (Map.Entry<Unit, ArrayList<Unit>> assimilator: assimilators.entrySet()) {
				for (Unit worker: assimilator.getValue()) {
					if (worker.isIdle()) {
						game.rightClick(worker.getID(), assimilator.getKey().getID());
					}
				}
			}
		}
		
		public int getCountMineralWorkers() {
			return mineralWorkers.size();
		}
		
		public int getMaxMineralWorkers() {
			return (int)(minerals.size() * maxWorkersPerMineralField);
		}
		
		public int getCountGasWorkers() {
			return gasWorkers.size();
		}
		
		public int getMaxGasWorkers() {
			return (int)(assimilators.size() * maxWorkersPerGeyser);
		}
		
		public int getMaxAllWorkers() {
			return getMaxMineralWorkers() + getMaxGasWorkers();
		}
		public double getWorkerRatio() {
			return (double)(getCountMineralWorkers() + getCountGasWorkers()) / (double)(getMaxMineralWorkers() + getMaxGasWorkers());
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
				game.drawLine(nexus.getX(), nexus.getY(), worker.getX(), worker.getY(), BWColor.ORANGE, false);
			}
			
			for (Unit worker: gasWorkers) {
				game.drawLine(nexus.getX(), nexus.getY(), worker.getX(), worker.getY(), BWColor.ORANGE, false);
			}
			
		}
		
	}
}
