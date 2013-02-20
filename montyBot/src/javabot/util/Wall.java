package javabot.util;

import java.awt.Point;
import java.util.ArrayList;

import javabot.model.ChokePoint;

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

}
