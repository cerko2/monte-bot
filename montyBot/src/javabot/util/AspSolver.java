package javabot.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AspSolver {
	
	private String exePath;
	private String purpose;
	
	public AspSolver(String path, String purpose) {
		this.exePath = path;
		this.purpose = purpose;
	}
	
	public ArrayList<String> solve(String lp) {
		String out = getStringOutput(lp);
		ArrayList<String> ret = new ArrayList<String>();
		
		if (!out.contains("UNSATISFIABLE")) {
			for (String s : out.split(" ")) {
				if (!s.trim().isEmpty()) ret.add(s);
			}
		}
		
		return ret;
	}
	
	public String getStringOutput(String lp) {
		String output = "";
		
		try {
			PrintWriter out = new PrintWriter("bwapi-data/AI/montyBot/"+purpose+".lp");
			out.print(lp);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
		  String line;
		
	      Process p = Runtime.getRuntime().exec(this.exePath+" --asp09 bwapi-data/AI/montyBot/"+purpose+".lp");
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
			err.printStackTrace();
        }
	      
		return output;
	}
	
}
