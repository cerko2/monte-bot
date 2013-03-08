package javabot.strategy;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.model.ChokePoint;
import javabot.model.Region;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;
import javabot.util.AspSolver;
import javabot.util.Wall;
import javabot.types.UnitType;



public class WallInModule extends AbstractManager {
	
	// constants
	public static final int CHOKEPOINT_RADIUS = 8;

	private JNIBWAPI bwapi;
	private AspSolver asp;
	
	private ArrayList<Wall> walls;
	
	public Wall getWallAt(ChokePoint choke) {
		for (Wall w : walls) {
			if (w.getChokePoint() == choke) {
				return w;
			}
		}
		return null;
	}

	public ArrayList<Wall> getAllWalls() {
		return this.walls;
	}
	
	public WallInModule(JNIBWAPI game) {
		this.bwapi = game;
		this.asp = new AspSolver("bwapi-data/AI/montyBot/clingo.exe","wall-in");
		this.walls = new ArrayList<Wall>();
	}
	
	public void gameStarted() {
		bwapi.printText("Wall-in Module loaded.");
	}
	
	private boolean isBuildable(int x, int y, int w, int h, Point forbidden1, Point forbidden2, int margin1, int margin2, int margin3, int margin4) {
		for (int i = x; i < x+w; i++) {
			for (int j = y; j < y+h; j++) {
				if ((!bwapi.isBuildable(i, j))
						|| (forbidden1.x == i && forbidden1.y == j)
						|| (forbidden2.x == i && forbidden2.y == j)
						){
					return false;
				}
				if (obstructedByNeutrals(i, j)) 
					return false;
				
				if ((i == margin1) || (i == margin2) || (j == margin3) || (j == margin4)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private double eucDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(((double)x1-(double)x2)*((double)x1-(double)x2)+((double)y1-(double)y2)*((double)y1-(double)y2)); 
	}
	
	public boolean obstructedByNeutrals(int tx, int ty) {
		UnitType nType;
		for (Unit u : bwapi.getAllStaticNeutralUnits()) {
			nType = bwapi.getUnitType(u.getTypeID());
			if ((tx >= u.getTileX()) && (tx <= u.getTileX()+nType.getTileWidth()) && (ty >= u.getTileY()) && (ty <= u.getTileY()+nType.getTileHeight())) return true;
		}
		return false;
	}
	
	public void computeWall(ChokePoint choke, Region insideRegion, Integer unitTypeId) {
		
		for (Wall w : walls) {
			if (w.getChokePoint() == choke) {
				bwapi.printText("Wall for chokepoint "+String.valueOf(choke.getCenterX())+","+String.valueOf(choke.getCenterY())+" has already been computed.");
				return;
			}
		}
		
		bwapi.printText("Computing wall for chokepoint "+String.valueOf(choke.getCenterX())+","+String.valueOf(choke.getCenterY())+".");
		
		Region outsideRegion;
		if (choke.getFirstRegion().getID() == insideRegion.getID()) {
			outsideRegion = choke.getSecondRegion();
		} else {
			outsideRegion = choke.getFirstRegion();
		}
		
		String dynamicLP = "";
	
		// enemy unit type
		if (unitTypeId == UnitTypes.Zerg_Zergling.ordinal()) dynamicLP += "enemyUnitX(16). enemyUnitY(16).\n";
		else if (unitTypeId == UnitTypes.Protoss_Zealot.ordinal()) dynamicLP += "enemyUnitX(23). enemyUnitY(19).\n";
		else if ((unitTypeId == UnitTypes.Terran_SCV.ordinal()) || (unitTypeId == UnitTypes.Zerg_Drone.ordinal()) || (unitTypeId == UnitTypes.Protoss_Probe.ordinal())) dynamicLP += "enemyUnitX(23). enemyUnitY(23).\n";  
		else {dynamicLP += "enemyUnitX(23). enemyUnitY(23).\n";}
		
		// wall composition (terran: 1xBarrack, 2xDepot)
		/*
		dynamicLP += 
		"% Specify what building instances to build.\n" +
		"building(barrack).\n" +
		"building(depot1).\n" +
		"building(depot2).\n" +
		"\n" +
		"% Specify building types and their sizes.\n" +
		"buildingType(barrackType).\n" +
		"buildingType(depotType).\n" +
		"width(barrackType,4). height(barrackType,3).	leftSpace(barrackType,16). rightSpace(barrackType,7). topSpace(barrackType,8). bottomSpace(barrackType,15).\n" +
		"width(depotType,3). height(depotType,2).		leftSpace(depotType,10). rightSpace(depotType,9). topSpace(depotType,10). bottomSpace(depotType,5).\n" +
		"% Assign building instances to their types.\n" +
		"type(barrack,barrackType).\n" +
		"type(depot1,depotType).\n" +
		"type(depot2,depotType).\n" +
		"% Generate all the models.\n" +
		"1[place(barrack,X,Y) : buildable(barrackType,X,Y)]1.\n" +
		"1[place(depot1,X,Y) : buildable(depotType,X,Y)]1.\n" +
		"1[place(depot2,X,Y) : buildable(depotType,X,Y)]1.\n" +
		"% Filter out those models, where the path is not blocked.\n" +
		":- insideBase(X1,Y1), outsideBase(X2,Y2), canreach(X2,Y2).\n";
		*/
		dynamicLP += 
		"% Specify what building instances to build.\n" +
		"building(forge).\n" +
		"building(gateway).\n" +
		"building(zealots).\n" +
		"building(pylon).\n" +
		"building(cannon).\n" +
		"\n" +
		"% Specify building types and their sizes.\n" +
		"buildingType(forgeType).\n" +
		"buildingType(gatewayType).\n" +
		"buildingType(zealotsType).\n" +
		"buildingType(pylonType).\n" +
		"buildingType(cannonType).\n" +
		"width(gatewayType,4). height(gatewayType,3).	leftSpace(gatewayType,16). rightSpace(gatewayType,15). topSpace(gatewayType,16). bottomSpace(gatewayType,7).\n" +
		"width(forgeType,3). height(forgeType,2).		leftSpace(forgeType,12). rightSpace(forgeType,11). topSpace(forgeType,8). bottomSpace(forgeType,11).\n" +
		"width(pylonType,2). height(pylonType,2).		leftSpace(pylonType,16). rightSpace(pylonType,15). topSpace(pylonType,20). bottomSpace(pylonType,11).\n" +
		"width(cannonType,2). height(cannonType,2).		leftSpace(cannonType,12). rightSpace(cannonType,11). topSpace(cannonType,16). bottomSpace(cannonType,15).\n" +
		"width(zealotsType,1). height(zealotsType,1).	leftSpace(zealotsType,-5). rightSpace(zealotsType,-5). topSpace(zealotsType,-5). bottomSpace(zealotsType,-5).\n" +
		"% Assign building instances to their types.\n" +
		"type(forge,forgeType).\n" +
		"type(gateway,gatewayType).\n" +
		"type(zealots,zealotsType).\n" +
		"type(pylon,pylonType).\n" +
		"type(cannon,cannonType).\n" +
		"% Generate all the models.\n" +
		"0[place(forge,X,Y) : buildable(forgeType,X,Y)]1.\n" +
		"0[place(cannon,X,Y) : buildable(cannonType,X,Y)]1.\n" +
		"0[place(gateway,X,Y) : buildable(gatewayType,X,Y)]1.\n" +
		"0[place(pylon,X,Y) : buildable(pylonType,X,Y)]1.\n" +
		"1[place(zealots,X,Y) : buildable(zealotsType,X,Y)]1.\n" +
		"% Filter out those models, where the path is not blocked.\n" +
		":- insideBase(X1,Y1), outsideBase(X2,Y2), canreach(X2,Y2).\n";
		
		String buildingName1 = "gateway";
		int buildingWidth1 = 4;
		int buildingHeight1 = 3;
		String buildingName2 = "forge";
		int buildingWidth2 = 3;
		int buildingHeight2 = 2;
		String buildingName3 = "pylon";
		int buildingWidth3 = 2;
		int buildingHeight3 = 2;
		String buildingName4 = "cannon";
		int buildingWidth4 = 2;
		int buildingHeight4 = 2;
		String buildingName5 = "zealots";
		int buildingWidth5 = 1;
		int buildingHeight5 = 1;
		
		HashSet<String> dynAtoms = new HashSet<String>();
		int insideX = -1;
		int insideY = -1;
		double distInside = 99999999;
		int outsideX = -1;
		int outsideY = -1;
		double distOutside = 99999999;
		
		// search the "outer ring" of tiles for inside and outside points 
		for (int x = choke.getCenterX()/32-CHOKEPOINT_RADIUS; x <=choke.getCenterX()/32+CHOKEPOINT_RADIUS; x++) {
			for (int y = choke.getCenterY()/32-CHOKEPOINT_RADIUS; y <=choke.getCenterY()/32+CHOKEPOINT_RADIUS; y++) {

				// skip innet tiles
				if ((x == choke.getCenterX()/32-CHOKEPOINT_RADIUS) && (y == choke.getCenterY()/32-CHOKEPOINT_RADIUS) && (x == choke.getCenterX()/32+CHOKEPOINT_RADIUS) && (y == choke.getCenterY()/32+CHOKEPOINT_RADIUS)) continue;
				
				// inside tile
				double di = this.eucDistance(x*32, y*32, insideRegion.getCenterX(), insideRegion.getCenterY());
				if ((insideX == -1) || (di < distInside)) {
					if (bwapi.isBuildable(x, y)) {
						insideX = x;
						insideY = y;
						distInside = di;
					}
				}
				// outside tile
				double dou = this.eucDistance(x*32, y*32, outsideRegion.getCenterX(), outsideRegion.getCenterY());
				if ((outsideX == -1) || (dou < distOutside)) {
					if (bwapi.isBuildable(x, y)) {
						outsideX = x;
						outsideY = y;
						distOutside = dou;
					}
				}
			}
		}
		
		// create ASP atoms about walkable/buildable tiles
		for (int x = choke.getCenterX()/32-CHOKEPOINT_RADIUS; x <=choke.getCenterX()/32+CHOKEPOINT_RADIUS; x++) {
			for (int y = choke.getCenterY()/32-CHOKEPOINT_RADIUS; y <=choke.getCenterY()/32+CHOKEPOINT_RADIUS; y++) {
				// walkable tiles
				if (bwapi.getMap().isWalkable(x*4+2, y*4+2)) {
					//if (!obstructedByNeutrals(x,y)) {
						dynAtoms.add("walkable("+String.valueOf(x)+","+String.valueOf(y)+").\n");
					//}
				}
				
				// buildable 
				if (this.isBuildable(x, y, buildingWidth1, buildingHeight1, new Point(insideX,insideY), new Point(outsideX,outsideY), choke.getCenterX()/32-CHOKEPOINT_RADIUS, choke.getCenterX()/32+CHOKEPOINT_RADIUS, choke.getCenterY()/32-CHOKEPOINT_RADIUS, choke.getCenterY()/32+CHOKEPOINT_RADIUS)) dynAtoms.add("buildable("+buildingName1+"Type,"+String.valueOf(x)+","+String.valueOf(y)+").\n");
				if (this.isBuildable(x, y, buildingWidth2, buildingHeight2, new Point(insideX,insideY), new Point(outsideX,outsideY), choke.getCenterX()/32-CHOKEPOINT_RADIUS, choke.getCenterX()/32+CHOKEPOINT_RADIUS, choke.getCenterY()/32-CHOKEPOINT_RADIUS, choke.getCenterY()/32+CHOKEPOINT_RADIUS)) dynAtoms.add("buildable("+buildingName2+"Type,"+String.valueOf(x)+","+String.valueOf(y)+").\n");
				if (this.isBuildable(x, y, buildingWidth3, buildingHeight3, new Point(insideX,insideY), new Point(outsideX,outsideY), choke.getCenterX()/32-CHOKEPOINT_RADIUS, choke.getCenterX()/32+CHOKEPOINT_RADIUS, choke.getCenterY()/32-CHOKEPOINT_RADIUS, choke.getCenterY()/32+CHOKEPOINT_RADIUS)) dynAtoms.add("buildable("+buildingName3+"Type,"+String.valueOf(x)+","+String.valueOf(y)+").\n");
				if (this.isBuildable(x, y, buildingWidth4, buildingHeight4, new Point(insideX,insideY), new Point(outsideX,outsideY), choke.getCenterX()/32-CHOKEPOINT_RADIUS, choke.getCenterX()/32+CHOKEPOINT_RADIUS, choke.getCenterY()/32-CHOKEPOINT_RADIUS, choke.getCenterY()/32+CHOKEPOINT_RADIUS)) dynAtoms.add("buildable("+buildingName4+"Type,"+String.valueOf(x)+","+String.valueOf(y)+").\n");
				if (this.isBuildable(x, y, buildingWidth5, buildingHeight5, new Point(insideX,insideY), new Point(outsideX,outsideY), choke.getCenterX()/32-CHOKEPOINT_RADIUS, choke.getCenterX()/32+CHOKEPOINT_RADIUS, choke.getCenterY()/32-CHOKEPOINT_RADIUS, choke.getCenterY()/32+CHOKEPOINT_RADIUS)) dynAtoms.add("buildable("+buildingName5+"Type,"+String.valueOf(x)+","+String.valueOf(y)+").\n");
				
				// blocked by neutrals
				//if (obstructedByNeutrals(x, y)) {
				//	dynAtoms.add("blocked("+String.valueOf(x)+","+String.valueOf(y)+").\n");
				//}
			}
		}
		
		

		// waklkable/buildable tiles
		for (String s : dynAtoms) { dynamicLP += s;	}

		// inside and outside tile
		dynamicLP += "insideBase("+String.valueOf(insideX)+","+String.valueOf(insideY)+").\n";
		dynamicLP += "outsideBase("+String.valueOf(outsideX)+","+String.valueOf(outsideY)+").\n";
		
		// call ASP solver
		ArrayList<String> model = asp.solve(staticLP+"\n"+dynamicLP);

		// add this new wall to our array
		if (!model.isEmpty()) {
			walls.add(new Wall());
			int newId = walls.size()-1;
			for (String s : model) {
				String[] pars = s.substring(s.indexOf("(")+1).substring(0,s.substring(s.indexOf("(")+1).indexOf(")")).split(",");
				if (pars[0].contains("gateway")) {
					walls.get(newId).getBuildingTypeIds().add(UnitTypes.Protoss_Gateway.ordinal());
				} else if (pars[0].contains("forge")) {
					walls.get(newId).getBuildingTypeIds().add(UnitTypes.Protoss_Forge.ordinal());
				} else if (pars[0].contains("pylon")) {
					walls.get(newId).getBuildingTypeIds().add(UnitTypes.Protoss_Pylon.ordinal());
				} else if (pars[0].contains("cannon")) {
					walls.get(newId).getBuildingTypeIds().add(UnitTypes.Protoss_Photon_Cannon.ordinal());
				} else if (pars[0].contains("zealots")) {
					walls.get(newId).getBuildingTypeIds().add(UnitTypes.Protoss_Zealot.ordinal());
				}
				walls.get(newId).getBuildTiles().add(new Point( Integer.valueOf(pars[1]), Integer.valueOf(pars[2])  ));
			}
		} else {
			bwapi.printText("Wall at "+String.valueOf(choke.getCenterX())+","+String.valueOf(choke.getCenterY())+" couldn't be found.");
			walls.add(new Wall());
			walls.get(walls.size()-1).setSuccessfullyFound(false);
		}
		
		
	}
	
	private String staticLP = 
"% =========== THIS IS THE STATIC LP ==========\n" +
"#hide.\n" +
"#show place/3.\n" +
"% Tiles occupied by the buildings\n" +
"occupiedBy(B,X2,Y2) :- place(B,X1,Y1), X2 >= X1, X2 < X1+Z, width(BT,Z), Y2 >= Y1, Y2 < Y1+Q, height(BT,Q), walkable(X2,Y2), type(B,BT).\n" +
"\n" +
"% Two buildings cannot occupy one tile\n" +
":- occupiedBy(B1,X,Y), occupiedBy(B2,X,Y), B1!=B2.\n" +
"\n" +
"% Gaps between every two adjacent tiles, that are occupied by buildings\n" +
"verticalGap(X1,Y1,X2,Y2,G) :-									 				% 1 above 2\n" +
"	blocked(X1,Y1), blocked(X2,Y2), occupiedBy(B1,X1,Y1), occupiedBy(B2,X2,Y2), B1 != B2,\n" +
"	X1=X2, Y1=Y2-1, G=S1+S2, 					\n" +
"	type(B1,T1), type(B2,T2), bottomSpace(T1,S1), topSpace(T2,S2).\n" +
"verticalGap(X1,Y1,X2,Y2,G) :-									 				% 2 above 1\n" +
"	blocked(X1,Y1), blocked(X2,Y2), occupiedBy(B1,X1,Y1), occupiedBy(B2,X2,Y2), B1 != B2,\n" +
"	X1=X2, Y1=Y2+1, G=S1+S2, 					\n" +
"	type(B1,T1), type(B2,T2), bottomSpace(T2,S2), topSpace(T1,S1).\n" +
"horizontalGap(X1,Y1,X2,Y2,G) :-									 				% 1 left from 2\n" +
"	blocked(X1,Y1), blocked(X2,Y2), occupiedBy(B1,X1,Y1), occupiedBy(B2,X2,Y2), B1 != B2,\n" +
"	X1=X2-1, Y1=Y2, G=S1+S2, 					\n" +
"	type(B1,T1), type(B2,T2), rightSpace(T1,S1), leftSpace(T2,S2).\n" +
"horizontalGap(X1,Y1,X2,Y2,G) :-									 				% 2 left from 1\n" +
"	blocked(X1,Y1), blocked(X2,Y2), occupiedBy(B1,X1,Y1), occupiedBy(B2,X2,Y2), B1 != B2,\n" +
"	X1=X2+1, Y1=Y2, G=S1+S2, 					\n" +
"	type(B1,T1), type(B2,T2), rightSpace(T2,S2), leftSpace(T1,S1).\n" +
"\n" +
"% Using gaps to reach (walk on) blocked locations\n" +
"canreach(X1,Y1) :- horizontalGap(X1,Y1,X2,Y1,G), G >= S, X2=X1+1, canreach(X1,Y3), Y3=Y1+1, enemyUnitX(S).\n" +
"canreach(X1,Y1) :- horizontalGap(X1,Y1,X2,Y1,G), G >= S, X2=X1-1, canreach(X1,Y3), Y3=Y1+1, enemyUnitX(S).\n" +
"canreach(X1,Y1) :- horizontalGap(X1,Y1,X2,Y1,G), G >= S, X2=X1+1, canreach(X1,Y3), Y3=Y1-1, enemyUnitX(S).\n" +
"canreach(X1,Y1) :- horizontalGap(X1,Y1,X2,Y1,G), G >= S, X2=X1-1, canreach(X1,Y3), Y3=Y1-1, enemyUnitX(S).\n" +
"canreach(X1,Y1) :- verticalGap(X1,Y1,X1,Y2,G), G >= S, Y2=Y1+1, canreach(X3,Y1), X3=X1-1, enemyUnitY(S).\n" +
"canreach(X1,Y1) :- verticalGap(X1,Y1,X1,Y2,G), G >= S, Y2=Y1-1, canreach(X3,Y1), X3=X1-1, enemyUnitY(S).\n" +
"canreach(X1,Y1) :- verticalGap(X1,Y1,X1,Y2,G), G >= S, Y2=Y1+1, canreach(X3,Y1), X3=X1+1, enemyUnitY(S).\n" +
"canreach(X1,Y1) :- verticalGap(X1,Y1,X1,Y2,G), G >= S, Y2=Y1-1, canreach(X3,Y1), X3=X1+1, enemyUnitY(S).\n" +
"\n" +
"% Reachability between tiles.\n" +
"blocked(X,Y) :- occupiedBy(B,X,Y), building(B), walkable(X,Y).\n" +
"\n" +
"canreach(X2,Y) :- insideBase(X1,Y), X1=X2+1, walkable(X1,Y), walkable(X2,Y), not blocked(X1,Y), not blocked(X2,Y).\n" +
"canreach(X2,Y) :- insideBase(X1,Y), X1=X2-1, walkable(X1,Y), walkable(X2,Y), not blocked(X1,Y), not blocked(X2,Y).\n" +
"canreach(X,Y2) :- insideBase(X,Y1), Y1=Y2+1, walkable(X,Y1), walkable(X,Y2), not blocked(X,Y1), not blocked(X,Y2).\n" +
"canreach(X,Y2) :- insideBase(X,Y1), Y1=Y2-1, walkable(X,Y1), walkable(X,Y2), not blocked(X,Y1), not blocked(X,Y2).\n" +
"canreach(X2,Y2) :- insideBase(X1,Y1), X1=X2+1, Y1=Y2+1, walkable(X1,Y1), walkable(X2,Y2), not blocked(X1,Y1), not blocked(X2,Y2).\n" +
"canreach(X2,Y2) :- insideBase(X1,Y1), X1=X2-1, Y1=Y2+1, walkable(X1,Y1), walkable(X2,Y2), not blocked(X1,Y1), not blocked(X2,Y2).\n" +
"canreach(X2,Y2) :- insideBase(X1,Y1), X1=X2+1, Y1=Y2-1, walkable(X1,Y1), walkable(X2,Y2), not blocked(X1,Y1), not blocked(X2,Y2).\n" +
"canreach(X2,Y2) :- insideBase(X1,Y1), X1=X2-1, Y1=Y2-1, walkable(X1,Y1), walkable(X2,Y2), not blocked(X1,Y1), not blocked(X2,Y2).\n" +
"\n" +
"canreach(X2,Y) :- canreach(X1,Y), X1=X2+1, walkable(X1,Y), walkable(X2,Y), not blocked(X1,Y), not blocked(X2,Y).\n" +
"canreach(X2,Y) :- canreach(X1,Y), X1=X2-1, walkable(X1,Y), walkable(X2,Y), not blocked(X1,Y), not blocked(X2,Y).\n" +
"canreach(X,Y2) :- canreach(X,Y1), Y1=Y2+1, walkable(X,Y1), walkable(X,Y2), not blocked(X,Y1), not blocked(X,Y2).\n" +
"canreach(X,Y2) :- canreach(X,Y1), Y1=Y2-1, walkable(X,Y1), walkable(X,Y2), not blocked(X,Y1), not blocked(X,Y2).\n" +
"canreach(X2,Y2) :- canreach(X1,Y1), X1=X2+1, Y1=Y2+1, walkable(X1,Y1), walkable(X2,Y2), not blocked(X1,Y1), not blocked(X2,Y2).\n" +
"canreach(X2,Y2) :- canreach(X1,Y1), X1=X2-1, Y1=Y2+1, walkable(X1,Y1), walkable(X2,Y2), not blocked(X1,Y1), not blocked(X2,Y2).\n" +
"canreach(X2,Y2) :- canreach(X1,Y1), X1=X2+1, Y1=Y2-1, walkable(X1,Y1), walkable(X2,Y2), not blocked(X1,Y1), not blocked(X2,Y2).\n" +
"canreach(X2,Y2) :- canreach(X1,Y1), X1=X2-1, Y1=Y2-1, walkable(X1,Y1), walkable(X2,Y2), not blocked(X1,Y1), not blocked(X2,Y2).\n";

}
