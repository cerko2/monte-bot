package javabot.combat.micro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.model.BaseLocation;
import javabot.model.Map;
import javabot.model.Player;
import javabot.model.Region;
import javabot.model.Unit;
import javabot.types.UnitCommandType;
import javabot.types.UnitCommandType.UnitCommandTypes;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.types.WeaponType;
import javabot.types.WeaponType.WeaponTypes;
import javabot.util.BWColor;
import javabot.util.Position;
import javabot.util.RegionUtils;
import javabot.util.Threat;
import javabot.util.UnitUtils;
import javabot.util.map.MapGrid;

public class ScoutingManager extends AbstractManager{
	
	private JNIBWAPI game;
	private Map map;
	private Player player;
	private Player enemyPlayer;
	private MapGrid mapGrid;
	
	private Region homeRegion;
	private BaseLocation homeBase;
	private Region enemyRegion;
	private BaseLocation enemyBase;
	private Region scoutRegion;
	
	private HashSet<Unit> myUnits;
	private HashMap<Unit, Integer> scoutsLastAttacked;
	
	private ArrayList<BaseLocation> baseScoutOrder;
	
	private ArrayList<Threat> groundThreats;
	private ArrayList<Threat> airThreats;
	
	public ScoutingManager(JNIBWAPI game){
		this.game = game;
		this.player = game.getSelf();
		enemyPlayer = game.getEnemies().get(0);
		this.map = game.getMap();
		mapGrid = MapGrid.getInstance();
		
		myUnits = new HashSet<Unit>();
		baseScoutOrder = new ArrayList<BaseLocation>();
		groundThreats = new ArrayList<Threat>();
		airThreats = new ArrayList<Threat>();
		
		scoutsLastAttacked = new HashMap<Unit, Integer>();
	}
	
	public void gameStarted(){
		Unit nexus = game.getUnit(UnitUtils.getNearestUnit(game, UnitTypes.Protoss_Nexus.ordinal(), 0, 0));
		homeRegion = RegionUtils.getRegion(map, nexus);
		
		ArrayList<BaseLocation> baseLocations = map.getBaseLocations();
		for (BaseLocation bb : map.getBaseLocations()){
			if (bb.getTx() == nexus.getTileX() && bb.getTy() == nexus.getTileY()){
				homeBase = bb;
				break;
			}
			System.out.println("bb tx = " + bb.getTx() + " bb ty = " + bb.getTy());
		}
		
		if ( homeBase == null ) homeBase = baseLocations.get(0);
		
		for (Region region : map.getRegions()){
			if (homeBase.getRegionID() == region.getID()){
				homeRegion = region;
			}
		}
		
		for (BaseLocation bb : map.getBaseLocations()){
			if (bb.isStartLocation()){
				baseScoutOrder.add(bb);
			}
		}
	}
	
	public void gameUpdate(){
		
		if (!myUnits.isEmpty()){
			moveScouts();
		}
	}
	
	public void unitShow(int unitID){
		Unit unit = game.getUnit(unitID);
		if (unit.getPlayerID() == enemyPlayer.getID() 
				&& (unit.getTypeID() == UnitTypes.Protoss_Nexus.ordinal() 
				|| unit.getTypeID() == UnitTypes.Terran_Command_Center.ordinal()
				|| unit.getTypeID() == UnitTypes.Zerg_Hatchery.ordinal()) )
		{
			if (enemyRegion == null){
				enemyRegion = RegionUtils.getRegion(map, unit);
				
				//game.setGameSpeed(50);
				
				for (BaseLocation base : map.getBaseLocations()){
					if (base.getRegionID() == enemyRegion.getID()){
						if (base.isStartLocation()){
							enemyBase = base;
							//InformationManager.getInstance().setFoundEnemyHome(true);
							break;
						}
						else {
							enemyRegion = null;
							break;
						}
					}
				}
			}
		}
	}
	
	public void moveScouts(){
		
		Region scoutRegion = null;
		boolean scoutUnderAttack;
		int lastAttacked = 0;
		
		for (Unit unit : myUnits){
			
			scoutRegion = RegionUtils.getRegion(map, unit);
			
			if (isUnderAttack(unit) && (scoutRegion == enemyRegion)){
				scoutUnderAttack = true;
				scoutsLastAttacked.put(unit, game.getFrameCount());
			}
			
			if (scoutsLastAttacked.containsKey(unit)){
				lastAttacked = scoutsLastAttacked.get(unit);
			}
			
			scoutUnderAttack = (game.getFrameCount() - lastAttacked <= 24 * 5) ? true : false;
			
			if (isUnderAttack(unit) && 
					!enemyAttackCapableUnitInRadius(unit) &&
					(game.getFrameCount() - lastAttacked > 24 * 5))
			{
				if (scoutUnderAttack){
					System.out.println("scout no longer under attack");
				}
				scoutUnderAttack = false;
			}
			
			if (enemyRegion != null){
				if (scoutRegion == enemyRegion){
					fillGroundThreat(new Position(unit.getX(), unit.getY()));
					
					Unit closestWorker = getClosesEnemyWorker(unit);
					
					if (!scoutUnderAttack){
						if (closestWorker != null){
							smartAttack(unit, closestWorker);
						}
						else {
							smartMove(unit, new Position(enemyBase.getX(), enemyBase.getY()));
						}
					}
					else {
						Position fleePosition = getFleePosition(unit, groundThreats, new Position(enemyRegion.getChokePoints().get(0).getCenterX(),enemyRegion.getChokePoints().get(0).getCenterY()));
						smartMove(unit, fleePosition);
					}
				}
				else if (scoutUnderAttack){
					smartMove(unit, new Position(homeBase.getX(), homeBase.getY()));
				}
				else {
					smartMove(unit, new Position(enemyRegion.getCenterX(), enemyRegion.getCenterY()));
				}
			}
			else {
				if (!baseScoutOrder.isEmpty()){
					if (game.isExplored(baseScoutOrder.get(0).getTx(), baseScoutOrder.get(0).getTy())){
						game.drawText(5, 250, "baseLoc : " + baseScoutOrder.get(0).getTx() + ", " + baseScoutOrder.get(0).getTy(), true);
						baseScoutOrder.remove(0);
					}
					else {
						smartMove(unit, new Position(baseScoutOrder.get(0).getX(), baseScoutOrder.get(0).getY()));
					}
				}
			}
		}
		
	}
	
	private void fillGroundThreat(Position position){
		groundThreats.clear();
		
		int radius = 1000;
		int radiusSq = radius * radius;
		
		ArrayList<Unit> enemyUnits = game.getEnemyUnits();
		for (Unit enemy : enemyUnits){
			Position delta = position.substract(new Position(enemy.getX(), enemy.getY()));
			if (delta.x * delta.x + delta.y * delta.y > radiusSq){
				continue;
			}
			
			Threat threat = new Threat(enemy, 1);
			
			WeaponType groundWeapon = game.getWeaponType(game.getUnitType(enemy.getTypeID()).getGroundWeaponID());
			if (enemy.getTypeID() == UnitTypes.Terran_Bunker.ordinal()){
				groundWeapon = game.getWeaponType(WeaponTypes.Gauss_Rifle.ordinal());
				threat.weight = 4;
			}
			
			if (groundWeapon.getID() != WeaponTypes.None.ordinal()){
				threat.weight *= (double) groundWeapon.getDamageAmount() / (double) groundWeapon.getDamageCooldown();
				groundThreats.add(threat);
			}
		}
	}
	
	public Position getFleePosition(Unit unit, ArrayList<Threat> threats, Position target){
		Position fleeVector = getFleeVector(threats, unit);
		Position targetVector = new Position(0,0);
		
		if (target != null){
			targetVector.x = target.x - unit.getX();
			targetVector.y = target.y - unit.getY();
			targetVector.normalize();
		}
		
		Position testFleeVector;
		//fleeVector.rotate(30);
		
		int r = 0;
		int iterations = 0;
		
		Position fleePosition = new Position(homeBase.getX(),homeBase.getY());
		
		while (r <= 360){
			fleeVector.rotate(r);
			fleeVector.normalize();
			
			testFleeVector = fleeVector;
			//testFleeVector = fleeVector.multiply(2).add(targetVector);
			testFleeVector.normalize();
			
			Position fleePositionTest = new Position(unit.getX() + (testFleeVector.x * 64), unit.getY() + (testFleeVector.y * 64));
			
			//game.drawLine(workerScout.getUnit().getX(), workerScout.getUnit().getY(), (int) (fleePositionTest.x), (int) (fleePositionTest.y), BWColor.BLUE, false);
			
			if (map.isWalkable((int) fleePositionTest.x / 8, (int) fleePositionTest.y / 8) &&
				!MapGrid.getInstance().cellHasUnits((int) fleePositionTest.y / 32, (int) fleePositionTest.x / 32))
			{
				fleePosition = fleePositionTest;
				break;
			}
			
			r += 10;
			if (iterations > 36){
				break;
			}
			iterations++;
		}
		
		game.drawLine(unit.getX(), unit.getY(), (int) fleePosition.x, (int) fleePosition.y, BWColor.ORANGE, false);
		
		return fleePosition;
	}
	
	private Position getFleeVector(ArrayList<Threat> threats, Unit unit){
		Position result = new Position(0,0);
		
		for (Threat threat : threats){
			Position directionVector = (new Position(unit).substract(new Position(threat.unit)));
			
			double distanceSq = directionVector.lengthSq();
			if (distanceSq > 0){
				Position enemyInfluence = ( directionVector.divide(distanceSq).multiply(threat.weight));
				result = result.add(enemyInfluence);
			}
		}
		
		if (result.x != 0 && result.y != 0)
			result.normalize();
		
		return result;
	}
	
	private Unit getClosesEnemyWorker(Unit scout){
		
		ArrayList<Unit> enemyUnits = game.getEnemyUnits();
		int lowestHealth = 10000000;
		Unit lowestHealthWorker = null;
		
		for (Unit unit : enemyUnits){
			if (!game.getUnitType(unit.getTypeID()).isWorker()){
				continue;
			}
			
			if (game.getUnitType(unit.getTypeID()).isWorker() && unit.isConstructing()){
				return unit;
			}
			if (unit.getHitPoints() + unit.getShield() < lowestHealth){
				lowestHealth = unit.getHitPoints() + unit.getShield();
				lowestHealthWorker = unit;
			}
			if (unit.getHitPoints() + unit.getShield() == lowestHealth){
				if (game.getUnitType(unit.getTypeID()).isWorker() 
						&& UnitUtils.getDistance(scout, unit) < UnitUtils.getDistance(scout, lowestHealthWorker))
				{
					lowestHealthWorker = unit;
				}
			}
		}
		
		if (lowestHealthWorker != null){
			return lowestHealthWorker;
		}
		
		//TODO nemal by sa sem dostat ?deadcode?
		Unit enemyWorker = null;
		int minDist = 1000;
		
		for (Unit unit : enemyUnits){
			if (game.getUnitType(unit.getTypeID()).isWorker() 
					&& UnitUtils.getDistance(scout, unit) < minDist)
			{
				minDist = UnitUtils.getDistance(scout, unit);
				enemyWorker = unit;
			}
		}
		
		return enemyWorker;
		
	}
	
	private boolean enemyWorkerInRadius(Unit unit){
		for (Unit enemyUnit : game.getEnemyUnits()){
			if (game.getUnitType(enemyUnit.getTypeID()).isWorker() 
					&& UnitUtils.getDistance(enemyUnit, unit) < 300)
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean enemyAttackCapableUnitInRadius(Unit unit){
		
		UnitType enemyType = null;
		WeaponType enemyWeapon = null;
		
		for (Unit enemyUnit : game.getEnemyUnits()){
			
			enemyType = game.getUnitType(enemyUnit.getTypeID());
			enemyWeapon = game.getWeaponType(enemyType.getGroundWeaponID());
			
			if (enemyType.isAttackCapable()
					&& UnitUtils.getDistance(enemyUnit, unit) < enemyWeapon.getMaxRange() + 64)
			{
				return true;
			}
		}
		return false;
	}
	
	private void smartMove(Unit unit, Position position){
		UnitCommandType currentCommand = game.getUnitCommandType(unit.getLastCommandID());
		Position unitTargetPos = new Position(unit.getTargetX(), unit.getTargetY());
		
		if (currentCommand.getID() == UnitCommandTypes.Move.ordinal() 
				&& unitTargetPos.equals(position)){
			return;
		}
		
		game.move(unit.getID(), (int) position.x, (int) position.y);
	}
	
	private void smartAttack(Unit unit, Unit target){
		UnitCommandType currentCommand = game.getUnitCommandType(unit.getLastCommandID());
		
		if (currentCommand.getID() == UnitCommandTypes.Attack_Unit.ordinal() && unit.getTargetUnitID() == target.getID()){
			return;
		}
		
		game.attack(unit.getID(), target.getID());
	}
	
	private boolean isUnderAttack(Unit unit){
		ArrayList<Unit> enemyUnits = game.getEnemyUnits();
		
		UnitType enemyType = null;
		WeaponType weapon = null;
		for (Unit enemy : enemyUnits){
			enemyType = game.getUnitType(enemy.getTypeID());
			weapon = game.getWeaponType(enemyType.getGroundWeaponID());
			
			int dist = UnitUtils.getDistance(enemy, unit);
			Unit target = game.getUnit(enemy.getOrderTargetID());
			
			game.drawText(enemy.getX(), enemy.getY(), dist + " tID: " + enemy.getOrderTargetID(), false);
			
			if (target != null) {
				game.drawLine(enemy.getX(), enemy.getY(), target.getX(), target.getY(), BWColor.RED, false);
				
				if (target.getID() == unit.getID() )
					//					&& UnitUtils.getDistance(enemy, unit) <= weapon.getMaxRange() + 96)
				{
					return true;
				}
			}
			
		}
		return false;
	}
	
	//placeholder
	public boolean needNewScout(){
		return false;
	}
	
	public void setUnits(HashSet<Unit> units){
		myUnits = new HashSet<Unit>(units);
	}
	
	public HashSet<Unit> getUnits(){
		return myUnits;
	}
}
