package javabot;

import java.util.ArrayList;

public abstract class AbstractManager implements BWAPIEventListener{

	protected ArrayList<BWAPIEventListener> managers = new ArrayList<BWAPIEventListener>();
	
	public void addManager(BWAPIEventListener newManager){
		managers.add(newManager);
	}
	
	/** connected to bridge */
	public void connected(){
		for (BWAPIEventListener bb : managers){
			try{
				bb.connected();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/** game has just started, game settings can be turned on here */
	public void gameStarted(){
		for (BWAPIEventListener bb : managers){
			try{
				bb.gameStarted();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/** perform AI logic here */
	public void gameUpdate(){
		for (BWAPIEventListener bb : managers){
			try{
				bb.gameUpdate();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/** game has just terminated */
	public void gameEnded(){
		for (BWAPIEventListener bb : managers){
			try{
				bb.gameEnded();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/** keyPressed from within StarCraft */
	public void keyPressed(int keyCode){
		for (BWAPIEventListener bb : managers){
			try{
				bb.keyPressed(keyCode);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	// BWAPI callbacks
	public void matchEnded(boolean winner){
		for (BWAPIEventListener bb : managers){
			try{
				bb.matchEnded(winner);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public void playerLeft(int id){
		for (BWAPIEventListener bb : managers){
			try{
				bb.playerLeft(id);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public void nukeDetect(int x, int y){
		for (BWAPIEventListener bb : managers){
			try{
				bb.nukeDetect(x, y);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public void nukeDetect(){
		for (BWAPIEventListener bb : managers){
			try{
				bb.nukeDetect();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public void unitDiscover(int unitID){
		for (BWAPIEventListener bb : managers){
			try{
				bb.unitDiscover(unitID);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public void unitEvade(int unitID){
		for (BWAPIEventListener bb : managers){
			try{
				bb.unitEvade(unitID);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public void unitShow(int unitID){
		for (BWAPIEventListener bb : managers){
			try{
				bb.unitShow(unitID);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public void unitHide(int unitID){
		for (BWAPIEventListener bb : managers){
			try{
				bb.unitHide(unitID);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public void unitCreate(int unitID){
		for (BWAPIEventListener bb : managers){
			try{
				bb.unitCreate(unitID);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public void unitDestroy(int unitID){
		for (BWAPIEventListener bb : managers){
			try{
				bb.unitDestroy(unitID);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public void unitMorph(int unitID){
		for (BWAPIEventListener bb : managers){
			try{
				bb.unitMorph(unitID);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
