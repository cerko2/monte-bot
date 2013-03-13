package javabot.util;

import javabot.model.Unit;

public class Position {

	public double x, y;
	
	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Position(Unit unit){
		x = unit.getX();
		y = unit.getY();
	}
	
	public boolean equals(Position pos){
		return x == pos.x && y == pos.y;
	}
	
	public Position add(Position pos){
		return new Position(x + pos.x, y + pos.y);
	}
	
	public Position substract(Position pos){
		return new Position(x - pos.x, y - pos.y);
	}
	
	public Position multiply(double scalar){
		return new Position(x * scalar, y * scalar);
	}
	
	public Position divide(double scalar){
		return new Position(x / scalar, y / scalar);
	}
	
	public double lengthSq(){
		return x*x + y*y;
	}
	
	public double length(){
		return Math.sqrt(lengthSq());
	}
	
	public void normalize(){
		double tmpLength = length();
		x = x / tmpLength;
		y = y / tmpLength;
	}
	
	public void rotate(double _angle){
		double angle = _angle * Math.PI / 180.0;
		double tx = x * Math.cos(angle) - y * Math.sin(angle);
		double ty = y * Math.cos(angle) + x * Math.sin(angle);
		x = tx;
		y = ty;
	}
	
	public double distance(Position pos) {
		return Math.sqrt(Math.pow(pos.x - x, 2.0) + Math.pow(pos.y - y, 2.0)); 
	}
}
