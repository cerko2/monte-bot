package javabot.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javabot.JNIBWAPI;
import javabot.model.Player;
import javabot.model.Race;

public class Octave {
	private JNIBWAPI bwapi;

	public Octave(JNIBWAPI game) {
		this.bwapi = game;
	}
	
	public ArrayList<Float> computeTheta(Player opponent, int typeID, int orderOfPolynomial) {
		
		ArrayList<Float> ret = new ArrayList<Float>();
		String trainingSetFile = "";

		// Select the right training set file:
		// 1. get the default file for an opponent's race
		if (opponent.getRaceID() == Race.PROTOSS.ordinal()) {
			 trainingSetFile = "_general\\PROTOSS\\"+bwapi.getUnitType(typeID).getName()+".trainingset";
		} else if (opponent.getRaceID() == Race.ZERG.ordinal()) {
			trainingSetFile = "_general\\ZERG\\"+bwapi.getUnitType(typeID).getName()+".trainingset";
		} else if (opponent.getRaceID() == Race.TERRAN.ordinal()) {
			trainingSetFile = "_general\\TERRAN\\"+bwapi.getUnitType(typeID).getName()+".trainingset";
		} else { /* random */ }
		// 2. get the personalized file for this opponent
		// TODO
		

		// call the octave & gradient_descent script and get the output 
		String argsStr = "production-prediction\\"+trainingSetFile.replace(" ", "")+" "+String.valueOf(orderOfPolynomial);
		String out = getStringOutput(argsStr);
		
		// parse the output
		if (!out.contains("ERROR")) {
			for (String s : out.split("\n")) {
				if (!s.trim().isEmpty() && (isNumeric(s))) {
					ret.add( Float.parseFloat(s) );
				}
			}
		} else {
			bwapi.printText("ERROR: Gradient descent script returned an error. Deal with it.");
		}
		
		return ret;
	}
	
	public String getStringOutput(String commandLineArgsString) {
		String output = "";
		
		try {
		  String line;
		  
	      Process p = Runtime.getRuntime().exec("cmd /C \"cd bwapi-data\\AI\\montyBot & octave\\bin\\octave.exe gradient_descent.m "+commandLineArgsString+"\"");
	      BufferedReader bri = new BufferedReader
	        (new InputStreamReader(p.getInputStream()));
	      BufferedReader bre = new BufferedReader
	        (new InputStreamReader(p.getErrorStream()));
	      while ((line = bri.readLine()) != null) {
	        output += line+"\n";
	      }
	      bri.close();
	      while ((line = bre.readLine()) != null) {
	        output += line+"\n";
	      }
	      bre.close();
	      p.waitFor();
		} catch (Exception err) {
			bwapi.printText(err.getMessage());
			err.printStackTrace();
        }
	      
		return output;
	}
	
	private static boolean isNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c) && (!Character.toString(c).contains(".")) && (!Character.toString(c).contains("-"))) {
	        	return false;
	        }
	    }
	    return true;
	}
	
}
