package javabot.util;

import javabot.model.Unit;

public class Threat {
	
	public Unit unit;
	public double weight;
	
	public Threat(Unit unit, double weight) {
		this.unit = unit;
		this.weight = weight;
	}
}
