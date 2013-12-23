package javabot.combat;

import java.util.ArrayList;

import javabot.JNIBWAPI;
import javabot.macro.Boss;
import javabot.model.Unit;
import javabot.types.OrderType.OrderTypeTypes;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.util.Position;


public class WorkerDefenseManager {

	private Boss boss;
	private JNIBWAPI game;
	
	
	/**
	 * WorkerDefenseManager constructor.
	 *
	 * @param Boss - boss class
	 */
	public WorkerDefenseManager(Boss boss) {
		this.boss = boss;
		game = boss.game;
	}
	
	/**
	 * Main method called approximately 30 times per second.
	 *
	 * @param units - units assigned to worker defense manager
	 */
	public void update(ArrayList<Unit> units) {
		doWorkersSelfDefense(units);
	}
	
	private void doWorkersSelfDefense(ArrayList<Unit> units) {
		for (Unit unit: units) {
			if (unit.getTypeID() == UnitTypes.Protoss_Probe.ordinal()) {
				boolean stopAttack = true;

				Position unitPosition = new Position(unit.getX(), unit.getY());
				
				ArrayList<Unit> enemyUnits = game.getEnemyUnits();
				
				for (Unit enemyUnit: enemyUnits) {
					UnitType enemyUnitType = game.getUnitType(unit.getTypeID());

					if (enemyUnitType != null && unitPosition.distance(new Position(enemyUnit.getX(), enemyUnit.getY())) < 48) {
						if (enemyUnitType.isCanAttackGround() && !enemyUnitType.isFlyer()) {
							if (enemyUnit.getOrderID() != OrderTypeTypes.PlaceBuilding.ordinal()) {
								game.attack(unit.getID(), enemyUnit.getID());
							}
						}
						stopAttack = false;
						break;
					}
				}
		      
				stopAttack = true;

				if (unit.getOrderID() == OrderTypeTypes.AttackUnit.ordinal() && stopAttack) {
					game.stop(unit.getID());
				}
		    }
		}
	}
	
}
