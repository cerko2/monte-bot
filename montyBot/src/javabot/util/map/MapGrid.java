package javabot.util.map;

import java.util.ArrayList;
import java.util.HashSet;

import javabot.JNIBWAPI;
import javabot.JavaBot;
import javabot.model.Map;
import javabot.model.Unit;
import javabot.types.UnitType;
import javabot.util.BWColor;
import javabot.util.Position;
import javabot.util.UnitUtils;

public class MapGrid {
	
	public static final boolean DEBUG = false;
	
	private JNIBWAPI game;
	private Map map;
	
	private int mapWidth;
	private int mapHeight;
	private int cols;
	private int rows;
	private ArrayList<MapGridCell> cells;
	private HashSet<MapGridCell> usedCells;
	
	public static MapGrid instance = new MapGrid();
	
	private MapGrid() {
        if (instance != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    public static MapGrid getInstance() {
        return instance;
    }
    
    public void gameStarted(){
    	map = game.getMap();
    	
    	mapWidth = map.getWidth();
    	mapHeight = map.getHeight();
    	
    	cols = map.getWidth();
    	rows = map.getHeight();
    	cells = new ArrayList<MapGridCell>();
    	usedCells = new HashSet<MapGridCell>();
    	
    	for (int i = 0; i < cols; i++){
    		for (int j = 0; j < rows; j++){
    			cells.add(new MapGridCell(new Position(j,i)));
    		}
    	}
    }
    
    public void gameUpdate(){
    	updateUnits();
    }
    
    private void updateUnits(){
    	
    	if (DEBUG){
    		for (int j = 0; j < cols; j++){
    			for (int i = 0; i < rows; i++){
    				MapGridCell cell = getCellByIndex(i, j);
    				if (cell.myUnits.size() > 0){
    					game.drawBox(j * 32, i * 32, j * 32 + 32, i * 32 + 32, BWColor.GREY, false, false);
    				}

    				if (cell.enemyUnits.size() > 0){
    					game.drawBox(j * 32, i * 32, j * 32 + 32, i * 32 + 32, BWColor.RED, false, false);
    				}

    				if (cell.neutralUnits.size() > 0){
    					game.drawBox(j * 32, i * 32, j * 32 + 32, i * 32 + 32, BWColor.BLUE, false, false);
    				}
    			}
    		}
    	}
    	
    	usedCells.clear();
    	clearUnitsOnGrid();
    	
    	MapGridCell usedCell;
    	for (Unit unit : game.getMyUnits()){
    		UnitType type = game.getUnitType(unit.getTypeID());
    		if (type.isBuilding()){
    			for (int i = 0; i < type.getTileHeight(); i++){
    				for (int j = 0; j < type.getTileWidth(); j++){
    					usedCell = getCellByIndex(unit.getTileY() + i, unit.getTileX() + j);
    					usedCell.myUnits.add(unit);
    					usedCell.lastVisited = game.getFrameCount();
    					
    					//getCellByIndex(unit.getTileY() + i, unit.getTileX() + j).myUnits.add(unit);
    					//getCellByIndex(unit.getTileY() + i, unit.getTileX() + j).lastVisited = game.getFrameCount();
    					/*
    					getCellByIndex(unit.getTileY() + i, unit.getTileX() + j).myUnits.add(unit);
    					getCellByIndex(unit.getTileY() + i, unit.getTileX() + j).lastVisited = game.getFrameCount();
    					*/
    				}
    			}
    		}
    		else {
    			int compTileX = (unit.getX() + type.getDimensionLeft()) / 32;
				int compTileY = (unit.getY() + type.getDimensionUp()) / 32;
				usedCell = getCell(compTileX, compTileY);
				usedCell.myUnits.add(unit);
				usedCell.lastVisited = game.getFrameCount();
				
				//getCell(compTileX, compTileY).myUnits.add(unit);
				//getCell(compTileX, compTileY).lastVisited = game.getFrameCount();
				
    			/*
    			getCell(unit).myUnits.add(unit);
    			getCell(unit).lastVisited = game.getFrameCount();
    			*/
    		}
    	}
    	
    	for (Unit unit : game.getEnemyUnits()){
    		UnitType type = game.getUnitType(unit.getTypeID());
    		if (type.isBuilding()){
    			for (int i = 0; i < type.getTileHeight(); i++){
    				for (int j = 0; j < type.getTileWidth(); j++){
    					usedCell = getCellByIndex(unit.getTileY() + i, unit.getTileX() + j);
    					usedCell.enemyUnits.add(unit);
    					usedCell.enemyTimeLastSeen = game.getFrameCount();
    					//getCellByIndex(unit.getTileY() + i, unit.getTileX() + j).enemyUnits.add(unit);
    					//getCellByIndex(unit.getTileY() + i, unit.getTileX() + j).enemyTimeLastSeen = game.getFrameCount();
    				}
    			}
    		}
    		else {
    			int compTileX = (unit.getX() + type.getDimensionLeft()) / 32;
				int compTileY = (unit.getY() + type.getDimensionUp()) / 32;
				usedCell = getCell(compTileX, compTileY);
				usedCell.enemyUnits.add(unit);
				usedCell.enemyTimeLastSeen = game.getFrameCount();
				//getCell(compTileX, compTileY).enemyUnits.add(unit);
				//getCell(compTileX, compTileY).enemyTimeLastSeen = game.getFrameCount();
    			
				/*
    			getCell(unit).enemyUnits.add(unit);
    			getCell(unit).enemyTimeLastSeen = game.getFrameCount();
    			*/
    		}
    	}
    	
    	for (Unit unit : game.getNeutralUnits()){
    		UnitType type = game.getUnitType(unit.getTypeID());
    		if (type.isBuilding()){
    			for (int i = 0; i < type.getTileHeight(); i++){
    				for (int j = 0; j < type.getTileWidth(); j++){
    					/*
    					getCellByIndex(unit.getTileX() + j, unit.getTileY() + i).neutralUnits.add(unit);
    					getCellByIndex(unit.getTileX() + j, unit.getTileY() + i).lastVisited = game.getFrameCount();
    					*/
    					usedCell = getCellByIndex(unit.getTileY() + i, unit.getTileX() + j);
    					usedCell.neutralUnits.add(unit);
    					usedCell.lastVisited = game.getFrameCount();
    					
    					//getCellByIndex(unit.getTileY() + i, unit.getTileX() + j).neutralUnits.add(unit);
    					//getCellByIndex(unit.getTileY() + i, unit.getTileX() + j).lastVisited = game.getFrameCount();
    				}
    			}
    		}
    		else {
    			usedCell = getCell(unit);
    			usedCell.neutralUnits.add(unit);
    			usedCell.lastVisited = game.getFrameCount();
    			
    			//getCell(unit).neutralUnits.add(unit);
    			//getCell(unit).lastVisited = game.getFrameCount();
    		}
    	}
    }
    
    public void clearUnitsOnGrid(){
    	//prejde len used
    	for (MapGridCell cell : usedCells){
    		cell.clearUnitsOnCell();
    	}
    	//setky tily prejde
    	/*
    	for (MapGridCell cell : cells){
    		cell.clearUnitsOnCell();
    	}
    	*/
    }
    
    public boolean cellHasUnits(int row, int col){
    	MapGridCell gridCell = getCellByIndex(row, col);
    	if (gridCell.myUnits.size() > 0 ||
    		gridCell.enemyUnits.size() > 0 ||
    		gridCell.neutralUnits.size() > 0)
    	{
    		return true;
    	}
    	return false;
    }
    
    public MapGridCell getCellByIndex(int row, int col){
    	return cells.get(row * cols + col);
    }
    
    public MapGridCell getCell(Unit unit){
    	return getCellByIndex(unit.getTileY(), unit.getTileX());
    }
    
    public MapGridCell getCell(int x, int y){
    	return getCellByIndex(y, x);
    }
    
    public void setGame(JNIBWAPI game){
    	this.game = game;
    }
}
