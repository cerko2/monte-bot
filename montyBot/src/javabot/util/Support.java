package javabot.util;

import java.awt.Point;
import javabot.model.Unit;

public class Support {
	
	public double getDistance (Point a, Point b){
		double distance, pomx, pomy;
		pomx = Math.abs(a.x-b.x);
		pomy = Math.abs(a.y-b.y);
		distance = Math.sqrt((pomx*pomx)+(pomy*pomy))   ;
		return distance;
	}
	public double getDistance (Unit a, Point b){
		double distance, pomx, pomy;
		pomx = Math.abs(a.getX()-b.x);
		pomy = Math.abs(a.getY()-b.y);
		distance = Math.sqrt((pomx*pomx)+(pomy*pomy))   ;
		return distance;
	}
}
