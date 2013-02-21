package javabot.macro;

import javabot.AbstractManager;
import javabot.JNIBWAPI;

//Azder BM
/*TODO*/
/* 
* 
*/
public class BuildManager extends AbstractManager{
	private boolean testing = true; //testovacie vypisy.
	
	private JNIBWAPI game = null;
	private Boss boss  = null;
	private int minerals = 0;
	private int gas = 0;

	private static int actFrequency = 30; //frekvecia myAct
	
//-----------------------------------------------------------------------------------------	
	public BuildManager(Boss boss){
		this.boss = boss;
		this.game = boss.game;
		sendText("Start: Build Manager");	
	}
	public void setAddResources(int minerals,int gas){
		this.minerals += minerals;
		this.gas += gas;
	}
	public void gameUpdate(){
		if((game.getFrameCount() % actFrequency == 0 )){
			setSettings();
			if(minerals > 0) 
				myAct();
		}
		drawDebugInfo();		
	}
//-----------------------------------------------------------------------------------------	
	private void setSettings(){
		if(boss != null){
			this.minerals = boss.BuildManagerMinerals;  
			this.gas = boss.BuildManagerGas;
			if(testing){ /*TODO*/
				this.minerals = game.getSelf().getMinerals();
				this.gas = game.getSelf().getGas();
			}
		}else sendText("err: boss = null");
	}
	private void myAct(){
		
	}
//-----------------------------------------------------------------------------------------	

//------------------------------------ only testing ----------------------------------------
	private void sendText(String msg){
		if(testing) game.sendText("BM-" + msg);
	}
	private void drawDebugInfo() {

	}	
}
