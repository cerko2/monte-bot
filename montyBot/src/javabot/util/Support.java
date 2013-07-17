package javabot.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Vector;

import com.sun.corba.se.impl.interceptors.PINoOpHandlerImpl;

import javabot.JNIBWAPI;
import javabot.model.Region;
import javabot.model.Unit;

public class Support {
	private JNIBWAPI game = null;
	public Support(JNIBWAPI game){
		this.game = game;
	}
	public boolean isExist(int id) {
		for(Unit u : game.getMyUnits())
			if(u.getID() == id)
				return true;
		return false;
	}
	public Unit getUnit(int Id){
		for (Unit unit : game.getMyUnits()) {	
			if(unit.getID() == Id )
				return unit;
		}
		return null;
	}
	public boolean isInArray(Unit u,ArrayList<Point> array) {
		for(Point p: array){
			if(p.x == u.getTileX() && p.y == u.getTileY())
				return true;
		}
		return false;
	}
	public boolean isInArray(Point p, ArrayList<Point> array){
		for(Point a : array){
			if( p.x == a.x && p.y == a.y)
				return true;
		}
		return false;
	}
	public ArrayList<Point> sortAir(Point p, ArrayList<Point> array) {
		ArrayList<Point> ret  =  new ArrayList<Point>();
		int size =  array.size();
		for(int i = 0; i < size;i++){
			Point ner = getNearestPoint(p, array);
			if(ner != null){
				ret.add(ner);
				array.remove(ner);
			}
		}
		return ret;
	}
	private ArrayList<Region> sortAir(Region r, ArrayList<Region> array) {
		ArrayList<Region> ret  =  new ArrayList<Region>();
		int size =  array.size();
		for(int i = 0; i < size;i++){
			Region ner = getNearestRegion(r, array);
			if(ner != null){
				ret.add(ner);
				array.remove(ner);
			}
		}
		return ret;
	}
	public ArrayList<Point> sortGround(Point center, ArrayList<Point> array) {
		ArrayList<Point> ret  =  new ArrayList<Point>();
		Region centerRegion = getNearestRegion(new Point(center.x*32,center.y*32));
		Vector<Region> regions = new Vector<Region>();
		Vector<Region> regionsUse = new Vector<Region>();
		regions.add(centerRegion);
		while(!regions.isEmpty()){
			Region reg= regions.firstElement();
			regions.remove(reg);
			regionsUse.add(reg);
			ArrayList<Point> array2 = new ArrayList<Point>(array);
			for(Point p:array2){
				Region nerReg = getNearestRegion(new Point(p.x*32,p.y*32));
				if(nerReg.getCenterX() == reg.getCenterX() && nerReg.getCenterY() == reg.getCenterY()){
					ret.add(p);
					array.remove(p);
				}
			}
			ArrayList<Region> regionsNeighbor = reg.getConnectedRegions();
			regionsNeighbor = sortAir(centerRegion, regionsNeighbor);
			for(Region r :regionsNeighbor ){
				if(!isInGroup(r,regionsUse))
					regions.add(r);
			}
		}
		return ret;
	}
	private boolean isInGroup(Region r, Vector<Region> regionsUse) {
		for(Region rr: regionsUse){
			if(rr.getCenterX() == r.getCenterX() && rr.getCenterY() == r.getCenterY())
				return true;
		}
		return false;
	}
	public Region getNearestRegion(Point  center){
		ArrayList<Region> array = game.getMap().getRegions();
		Region near = array.get(0);
		double dis = getDistance(center, new Point(near.getCenterX(),near.getCenterY()));
		for(Region r : array){
			double disPom = getDistance(center, new Point(r.getCenterX(),r.getCenterY()));
			if(dis > disPom){
				near = r;
				dis = disPom;
			}	
		}		
		return near;
	}
	public Point getNearestPoint(Point center,ArrayList<Point> array){
		if(array.isEmpty())
			return null;
		Point near = array.get(0);
		double dis = getDistance(center, near);
		for(Point p : array){
			double disPom = getDistance(center, p);
			if(dis > disPom){
				near = p;
				dis = disPom;
			}	
		}
		return near;
	}
	public Point getNearestPoint(int x, int y,ArrayList<Point> array){
		return getNearestPoint(new Point(x, y), array);
	}
	private Region getNearestRegion(Region r, ArrayList<Region> array) {
		Region near = array.get(0);
		double dis = getDistance(r, near);
		for(Region p : array){
			double disPom = getDistance(r, p);
			if(dis > disPom){
				near = p;
				dis = disPom;
			}	
		}
		return near;
	}
	public double getDistance (Point a, Point b){
		double distance, pomx, pomy;
		pomx = Math.abs(a.x-b.x);
		pomy = Math.abs(a.y-b.y);
		distance = Math.sqrt((pomx*pomx)+(pomy*pomy))  ;
		return distance;
	}
	public double getDistance (Unit a, Point b){
		return getDistance(new Point(a.getX(),a.getY()),b);
	}
	public double getDistance (Unit a, Unit b){
		return getDistance(new Point(a.getX(),a.getY()),new Point(b.getX(),b.getY()));
	}
	public double getDistance (Region a, Region b){
		return getDistance(new Point(a.getCenterX(),a.getCenterY()), new Point(b.getCenterX(),b.getCenterY()));
	}
}
