package javabot.macro;

import java.awt.Point;
import java.util.Vector;

import javabot.AbstractManager;
import javabot.JNIBWAPI;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;

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
	private Vector<Integer> createStack = new Vector<Integer>();

	private static int actFrequency = 30; //frekvecia myAct
	
//-----------------------------------------------------------------------------------------	
	public BuildManager(Boss boss){
		this.boss = boss;
		this.game = boss.game;
		sendText("Start: Build Manager");
		if(testing){ /*TODO*/
			createStack.add(UnitTypes.Protoss_Pylon.ordinal());
		}
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
	public Vector<Integer> getConstructionPlans(){
		return createStack; 
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
		buildStack();
	}
//-----------------------------------------------------------------------------------------	
//-----------------------------------------------------------------------------------------	

	private void buildStack(){
		Boolean goStack =  true;
		for(int i = 0; i<  createStack.size() ; i++){
			if(goStack){
				int typeID = createStack.get(i);
				goStack = build(typeID);
				if(goStack)
					createStack.remove(i);
			}
		}
	}
	
	private boolean build(int typeID){
		int worker = getWorker();
		if(worker != -1){
			
			game.rightClick(worker, 100, 100);
			game.build(worker, 100, 100, typeID);
			return true;
		}
		return false;
	}
	private int getWorker(){
		/*TODO*/ //dat doprec ked bude WM.
		for(Unit u : game.getMyUnits()){
			if(u.getTypeID() == UnitTypes.Protoss_Probe.ordinal() ){	
				return u.getID();
			}
		}
		return -1;
	}
	

//------------------------------------ only testing ----------------------------------------
	private void sendText(String msg){
		if(testing) game.sendText("BM-" + msg);
	}
	private void drawDebugInfo() {
		if(testing){
			for(int i = 0; i < createStack.size();i++){
				int s = (int) Math.round(createStack.get(i));
				game.drawText(new Point(100,25+(i*10)), createStack.get(i) + ": "+ s, true);
			}
		}
	}	
}
