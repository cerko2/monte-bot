package javabot.util;

import java.awt.Point;
import java.util.ArrayList;

import javabot.model.ChokePoint;

public class Wall {
	
	public boolean successfullyFound = false;
	public ChokePoint chokePoint;
	public ArrayList<Integer> buildingTypeIds;
	public ArrayList<Point> buildTiles;
	
	public Wall() {
		this.chokePoint = null;
		this.buildingTypeIds = new ArrayList<Integer>();
		this.buildTiles = new ArrayList<Point>();
	}

}
