package javabot.combat;

import java.awt.Point;
import java.util.ArrayList;
import java.util.TreeSet;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.macro.Boss;
import javabot.model.ChokePoint;
import javabot.model.Map;
import javabot.model.Region;
import javabot.model.Unit;

public class MonteCarloPlanner extends AbstractManager 
{

	SquadManager squadManager;
	JNIBWAPI     bwapi;
	int          endFrame;
	static Map   map;
	private Boss boss;
	private ArrayList<Base>  enemyBases;
	private ArrayList<Base>  myBases;
	private ArrayList<Squad> enemySquads;
	private ArrayList<Squad> mySquads;
	
	private ArrayList<Plan> ourPlans;
	private ArrayList<Plan> enemyPlans;
	
		
	public MonteCarloPlanner( JNIBWAPI bwapi, Boss boss )
	{
		this.bwapi   = bwapi;
		squadManager = new SquadManager(bwapi, this);
		this.boss    = boss;
	}
	
	/**
	 * Call this function to initialize monteCarloPlanner;
	 */
	public void gameStarted()
	{
		squadManager.setSquads( ( ArrayList<Unit> ) boss.getOpponentPositioning().getEnemyUnits(), bwapi.getMyUnits() );
		setOurBases();
		setEnemyBases();
		endFrame = bwapi.getFrameCount() + 120 * 24;
		map 	 = bwapi.getMap();
	} // gameStarted
	
	private void setOurBases() 
	{
		TreeSet<Region> baseRegions = new TreeSet<Region>();
		for ( Unit u : boss.getNexuses() )
		{
			baseRegions.add( MonteCarloPlanner.getPointRegion( new Point( u.getX(), u.getY() ) ) );
		}
		
		for ( Region r : baseRegions )
		{
			myBases.add( new Base( bwapi, r, this, true ) );
		}
		
	} // setOurBases
	
	private void setEnemyBases()
	{
		TreeSet<Region> baseRegions = new TreeSet<Region>();
		for ( Unit u : ( TreeSet<Unit> ) boss.getOpponentPositioning().getEnemyUnits() )
		{
			if ( bwapi.getUnitType( u.getID() ).isBuilding() )
			{
				baseRegions.add( MonteCarloPlanner.getPointRegion( new Point( u.getX(), u.getY() ) ) );
			}
		}
		
		for ( Region r : baseRegions )
		{
			myBases.add( new Base( bwapi, r, this, false ) );
		}
		
	} // setEnemyBases
	
	@Override
	public void unitDestroy( int unitID ) 
	{
		super.unitDestroy( unitID );
		update( unitID );
	}
	
	/**
	 * call this function every time an event unit killed fires
	 * @param unitID
	 */
	public void update( int unitID )
	{
		squadManager.updateSquadManager( unitID );
	}
	
	public void update( ArrayList<Unit> enemyUnits, ArrayList<Unit> myUnits )
	{
		
		if ( enemyUnits == null )
		{
			System.out.println( "enemy units je null" );
			return;
		}
		
		if ( myUnits == null )
		{
			System.out.println( "my units je null" );
			return;
		}
		
		if ( bwapi == null )
		{
			System.out.println( "bwapi je null" );
			return;
		}
		
//		if ( ( enemyUnits == null ) || ( myUnits == null ) || ( bwapi == null ) )
//		{	
//			return;
//		}
		
		// next two minutes
		endFrame = bwapi.getFrameCount() + 120 * 24; 
		
		if ( squadManager.getOurSquadsTree().size() <= squadManager.getEnemySquadsTree().size() )
		{
			squadManager.setSquads( enemyUnits, myUnits );
		}
		
		squadManager.updateSquadManager( enemyUnits, myUnits );
		System.out.println( "Our squads: " + squadManager.getOurSquadsTree().size() );
		System.out.println( "Enemy squads: " + squadManager.getEnemySquadsTree().size() );
	}
	
	
	/**** GENERATE PLANS ****/
	
	/**
	 * generates one plan for all my, or enemy units
	 * @param is_my_plan
	 * @return Plan
	 */
	private Plan generateOnePlan( boolean is_my_plan )
	{
		return new Plan( this.squadManager, this.myBases, this.enemyBases, is_my_plan );
	}
	
	private void generatePlans() 
	{
		for ( int i = 0; i < 100; i++ )
			ourPlans.add( generateOnePlan( true ) );
		
		for ( int i = 0; i < 1000; i++ )
			enemyPlans.add( generateOnePlan( false ) );
	}
	
	private int compute( Plan plan, ArrayList<Plan> EnemyPlans )
	{
		// vsetkym enemy planom s mojim planom prejdi casovu os
		// ked su dva squady v jednom case na rovnakom regione, simuluj boj
		// nejak to vyhodnot
		// zapamataj do priemeru
		// cykluj o zivot
		return -1;
	}
	
	public Plan getOurBestPlan()
	{
		generatePlans();
		for ( Plan p : ourPlans )
			p.evaluate( this.compute( p , this.enemyPlans ) );
		
		int worst 	   = 1000000;
		Plan bestPlan  = null;
		for ( Plan p : ourPlans )
		{
			if ( worst > p.getPlanGrade() )
			{
				worst    = p.getPlanGrade();
				bestPlan = p;
			}
		}
		
		if ( bestPlan == null ) throw new NullPointerException( "Best plan could not be found" );
		
		return bestPlan;
	}
	
	
	/*** STATIC METHODS ***/
	
	public static Region getPointRegion(Point point) {
        ArrayList<Region> regions = map.getRegions();
        for (Region region : regions){
            int[] coordinates = region.getCoordinates();
            double[] xCoords = new double[coordinates.length / 2];
            double[] yCoords = new double[coordinates.length / 2];
            for (int i = 0; i < xCoords.length; i++){
                xCoords[i] = coordinates[i * 2];
                yCoords[i] = coordinates[(i * 2) + 1];
            }
            if (pnpoly(xCoords.length, xCoords, yCoords, point.getX(), point.getY())){
                return region;
            }
        }
        return null;
    } // getPointRegion
	
	public static double getDistance( Unit u1, Unit u2 ) 
	{
		return Math.sqrt( Math.pow( u1.getX() - u2.getX(), 2 ) + Math.pow( u1.getY() - u2.getY(), 2 ) );
	}
	
	public static double getDistance( Region u1, Region u2 ) 
	{
		return Math.sqrt( Math.pow( u1.getCenterX() - u2.getCenterX(), 2 ) + Math.pow( u1.getCenterY() - u2.getCenterY(), 2 ) );
	}
	
	public static double getDistance( ChokePoint u1, ChokePoint u2 ) 
	{
		return Math.sqrt( Math.pow( u1.getCenterX() - u2.getCenterX(), 2 ) + Math.pow( u1.getCenterY() - u2.getCenterY(), 2 ) );
	}
	
	public static double getDistance( Region u1, ChokePoint u2 ) 
	{
		return Math.sqrt( Math.pow( u1.getCenterX() - u2.getCenterX(), 2 ) + Math.pow( u1.getCenterY() - u2.getCenterY(), 2 ) );
	}
	
	public static double getDistance( Unit u1, ChokePoint u2 ) 
	{
		return Math.sqrt( Math.pow( u1.getX() - u2.getCenterX(), 2 ) + Math.pow( u1.getY() - u2.getCenterY(), 2 ) );
	}
	
    public static boolean pnpoly(int nvert, double[] vertx, double[] verty, double testx, double testy){
        int i, j = 0;
        boolean c = false;
        for (i = 0, j = nvert-1; i < nvert; j = i++) {
            if ( ((verty[i]>testy) != (verty[j]>testy)) &&
                    (testx < (vertx[j]-vertx[i]) * (testy-verty[i]) / (verty[j]-verty[i]) + vertx[i]) )
                c = !c;
        }
        return c;
    } // pnpoly
    
    public Unit getNearestWorker( int x, int y )
    {
    	return bwapi.getUnit( boss.getWorkerManager().getWorker( x, y ) );
    }
	
}

