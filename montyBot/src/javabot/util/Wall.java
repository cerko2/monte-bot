package javabot.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import javabot.JNIBWAPI;
import javabot.model.ChokePoint;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;

import java.awt.geom.Line2D;


public class Wall {
	
	private boolean successfullyFound = false;
	private ChokePoint chokePoint;
	private ArrayList<Integer> buildingTypeIds;
	private ArrayList<Point> buildTiles;
	private ArrayList<Point> weaknesses;
	private ArrayList<Line2D.Double> lines;
	
	public Wall() {
		this.chokePoint = null;
		this.buildingTypeIds = new ArrayList<Integer>();
		this.buildTiles = new ArrayList<Point>();
		this.weaknesses = new ArrayList<>();
		this.lines = new ArrayList<>();
	}
	
	public ArrayList<Point> getBuildTiles() {
		return this.buildTiles;
	}

	public ChokePoint getChokePoint() {
		return this.chokePoint;
	}
	
	public ArrayList<Point> getWeaknesses() {
		return this.weaknesses;
	}
	
	public ArrayList<Integer> getBuildingTypeIds() {
		return this.buildingTypeIds;
	}
	
	public boolean isSuccessfullyFound() {
		return this.successfullyFound;
	}
	
	public void setSuccessfullyFound(boolean b) {
		this.successfullyFound = b;
	}
	
	private boolean isIsolated(int buildingIndex, boolean[][] map, int w, int h) {
		for (int x = buildTiles.get(buildingIndex).x-1; x <= buildTiles.get(buildingIndex).x+w+1; x++) {
			if ((x < 0) || (x >= map.length) || (buildTiles.get(buildingIndex).y-1 < 0) || (buildTiles.get(buildingIndex).y+h+1 >= map[0].length)) return false; 
			if (map[x][buildTiles.get(buildingIndex).y-1] == true) return false;
			if (map[x][buildTiles.get(buildingIndex).y+h+1] == true) return false;
		}
		for (int y = buildTiles.get(buildingIndex).y-1; y <= buildTiles.get(buildingIndex).y+h+1; y++) {
			if ((y < 0) || (y >= map[0].length) || (buildTiles.get(buildingIndex).x-1 < 0) || (buildTiles.get(buildingIndex).x+w+1 >= map.length)) return false; 
			if (map[buildTiles.get(buildingIndex).x-1][y] == true) return false;
			if (map[buildTiles.get(buildingIndex).x+w+1][y] == true) return false;
		}
		return true;
	}
	
	public void correct(int radius, JNIBWAPI bwapi) {
		// create blockage map
		boolean[][] map = new boolean[bwapi.getMap().getWidth()+1][bwapi.getMap().getHeight()+1];
		
		// set true at tiles with buildings
		int posX; int posY;
		for (int i = 0; i < buildingTypeIds.size(); i++) {
			posX = buildTiles.get(i).x;
			posY = buildTiles.get(i).y;
			for (int x = posX; x <= posX+ bwapi.getUnitType(buildingTypeIds.get(i)).getTileWidth(); x++ ) {
				for (int y = posY; y <= posY+ bwapi.getUnitType(buildingTypeIds.get(i)).getTileHeight(); y++ ) {
					map[x][y] = true;
				}
			}
		}
		// set true at tiles with static neutrals
		for (Unit u : bwapi.getAllStaticNeutralUnits()) {
			posX = u.getTileX();
			posY = u.getTileY();
			for (int x = posX; x <= posX+ bwapi.getUnitType(u.getTypeID()).getTileWidth(); x++ ) {
				for (int y = posY; y <= posY+ bwapi.getUnitType(u.getTypeID()).getTileHeight(); y++ ) {
					map[x][y] = true;
				}
			}
		}
		
		ArrayList<Integer> redundant = new ArrayList<>();
		for (int i=0; i<buildingTypeIds.size(); i++) {
			if (isIsolated(i, map, bwapi.getUnitType(buildingTypeIds.get(i)).getTileWidth(), bwapi.getUnitType(buildingTypeIds.get(i)).getTileWidth())) {
				redundant.add(i);
			}
		}
		Collections.sort(redundant);
		Collections.reverse(redundant);
		
		for (int r : redundant) {
			this.buildingTypeIds.remove(r);
			this.buildTiles.remove(r);
		}
	}
	
	// detects the weak spots in the wall (gaps)
	public void findWeaknesses(JNIBWAPI bwapi, int criticalGapSize) {
		ArrayList<Integer> lineGaps = new ArrayList<>();
		this.weaknesses.clear();
		this.lines.clear();
		
		// compute all the lines that together make up building placement areas
		// and specify the right GAP value for each of them
		for (int i=0; i < this.getBuildTiles().size(); i++) {
			int tileWidth  = bwapi.getUnitType(this.getBuildingTypeIds().get(i)).getTileWidth();
			int tileHeight = bwapi.getUnitType(this.getBuildingTypeIds().get(i)).getTileHeight();
			Point leftTop  = new Point(this.getBuildTiles().get(i).x * 32, this.getBuildTiles().get(i).y * 32); 
			lines.add(new Line2D.Double(leftTop.x, leftTop.y, leftTop.x+tileWidth*32, leftTop.y));
			lineGaps.add(getLineGap(getBuildingTypeIds().get(i), "top"));
			lines.add(new Line2D.Double(leftTop.x, leftTop.y, leftTop.x, leftTop.y+tileHeight*32));
			lineGaps.add(getLineGap(getBuildingTypeIds().get(i), "left"));
			lines.add(new Line2D.Double(leftTop.x+tileWidth*32, leftTop.y, leftTop.x+tileWidth*32, leftTop.y+tileHeight*32));
			lineGaps.add(getLineGap(getBuildingTypeIds().get(i), "right"));
			lines.add(new Line2D.Double(leftTop.x+tileWidth*32, leftTop.y+tileHeight*32, leftTop.x, leftTop.y+tileHeight*32));
			lineGaps.add(getLineGap(getBuildingTypeIds().get(i), "bottom"));
		}

		// divide the lines by the points of other lines
		for (int i=0; i < lines.size(); i++) {
			for (int j=0; j < lines.size(); j++) {
				double i1x = lines.get(i).x1;
				double i1y = lines.get(i).y1;
				double i2x = lines.get(i).x2;
				double i2y = lines.get(i).y2;
				double j1x = lines.get(j).x1;
				double j1y = lines.get(j).y1;
				double j2x = lines.get(j).x2;
				double j2y = lines.get(j).y2;
				if (lines.get(i).ptSegDist(lines.get(j).x1, lines.get(j).y1) == 0) {
					// divide in J1
					if ((lines.get(j).x1 != lines.get(i).x1 || lines.get(j).y1 != lines.get(i).y1) && (lines.get(j).x1 != lines.get(i).x2 || lines.get(j).y1 != lines.get(i).y2)) {
						lines.get(i).setLine(i1x, i1y, j1x, j1y);
						lines.add(new Line2D.Double(j1x,j1y,i2x,i2y));
						lineGaps.add(lineGaps.get(i));
					}
				}
				if (lines.get(i).ptSegDist(lines.get(j).x2, lines.get(j).y2) == 0) {
					// divide in J2
					if ((lines.get(j).x2 != lines.get(i).x1 || lines.get(j).y2 != lines.get(i).y1) && (lines.get(j).x2 != lines.get(i).x2 || lines.get(j).y2 != lines.get(i).y2)) {
						lines.get(i).setLine(i1x, i1y, j2x, j2y);
						lines.add(new Line2D.Double(j2x,j2y,i2x,i2y));
						lineGaps.add(lineGaps.get(i));
					}
				}
			}
		}
		
		
		for (int i=0; i < lines.size(); i++) {
			for (int j=0; j < lines.size(); j++) {
				if (i == j) continue;
				// line i contains line j
				if (lines.get(i).ptSegDist(lines.get(j).x1, lines.get(j).y1) == 0 && lines.get(i).ptSegDist(lines.get(j).x2, lines.get(j).y2) == 0 ) {
					Point mid = new Point ( (int)(lines.get(j).x1 + (lines.get(j).x2-lines.get(j).x1)/2) , (int)(lines.get(j).y1 + (lines.get(j).y2-lines.get(j).y1)/2));
					if (!weaknesses.contains(mid)) {
						if (lineGaps.get(i)+lineGaps.get(j) >= criticalGapSize) {
							bwapi.javaPrint("gap size: "+String.valueOf(lineGaps.get(i))+"+"+String.valueOf(lineGaps.get(j))+" critical: "+String.valueOf(criticalGapSize));
							weaknesses.add(mid);
						}
					}
				}
				// line j contains line i
				if (lines.get(j).ptSegDist(lines.get(i).x1, lines.get(i).y1) == 0 && lines.get(j).ptSegDist(lines.get(i).x2, lines.get(i).y2) == 0 ) {
					Point mid = new Point ( (int)(lines.get(i).x1 + (lines.get(i).x2-lines.get(i).x1)/2) , (int)(lines.get(i).y1 + (lines.get(i).y2-lines.get(i).y1)/2));
					if (!weaknesses.contains(mid)) {
						if (lineGaps.get(i)+lineGaps.get(j) >= criticalGapSize) {
							bwapi.javaPrint("gap size: "+String.valueOf(lineGaps.get(i))+"+"+String.valueOf(lineGaps.get(j))+" critical: "+String.valueOf(criticalGapSize));
							weaknesses.add(mid);
						}
					}
				}
			}
		}

	}

	private int getLineGap(int typeID, String side) {
		if (side == "top") {
			if (typeID == UnitTypes.Protoss_Gateway.ordinal()) return 16;
			if (typeID == UnitTypes.Protoss_Forge.ordinal()) return 8;
			if (typeID == UnitTypes.Protoss_Pylon.ordinal()) return 20;
			if (typeID == UnitTypes.Protoss_Photon_Cannon.ordinal()) return 16;
			if (typeID == UnitTypes.Protoss_Cybernetics_Core.ordinal()) return 8;
		} else if (side == "left") {
			if (typeID == UnitTypes.Protoss_Gateway.ordinal()) return 16;
			if (typeID == UnitTypes.Protoss_Forge.ordinal()) return 12;
			if (typeID == UnitTypes.Protoss_Pylon.ordinal()) return 16;
			if (typeID == UnitTypes.Protoss_Photon_Cannon.ordinal()) return 12;
			if (typeID == UnitTypes.Protoss_Cybernetics_Core.ordinal()) return 8;
		} else if (side == "right") {
			if (typeID == UnitTypes.Protoss_Gateway.ordinal()) return 15;
			if (typeID == UnitTypes.Protoss_Forge.ordinal()) return 11;
			if (typeID == UnitTypes.Protoss_Pylon.ordinal()) return 15;
			if (typeID == UnitTypes.Protoss_Photon_Cannon.ordinal()) return 11;
			if (typeID == UnitTypes.Protoss_Cybernetics_Core.ordinal()) return 7;
		} else {
			if (typeID == UnitTypes.Protoss_Gateway.ordinal()) return 7;
			if (typeID == UnitTypes.Protoss_Forge.ordinal()) return 11;
			if (typeID == UnitTypes.Protoss_Pylon.ordinal()) return 11;
			if (typeID == UnitTypes.Protoss_Photon_Cannon.ordinal()) return 15;
			if (typeID == UnitTypes.Protoss_Cybernetics_Core.ordinal()) return 7;
		}
		return 6;
	}
	
	
	
	
}
