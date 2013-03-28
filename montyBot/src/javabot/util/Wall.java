package javabot.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javabot.JNIBWAPI;
import javabot.model.ChokePoint;
import javabot.model.Unit;

public class Wall {
	
	private boolean successfullyFound = false;
	private ChokePoint chokePoint;
	private ArrayList<Integer> buildingTypeIds;
	private ArrayList<Point> buildTiles;
	
	public Wall() {
		this.chokePoint = null;
		this.buildingTypeIds = new ArrayList<Integer>();
		this.buildTiles = new ArrayList<Point>();
	}
	
	public ArrayList<Point> getBuildTiles() {
		return this.buildTiles;
	}

	public ChokePoint getChokePoint() {
		return this.chokePoint;
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

}
