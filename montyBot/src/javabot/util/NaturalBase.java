package javabot.util;

import java.awt.Point;
import java.util.ArrayList;

import javabot.model.ChokePoint;
import javabot.model.Region;

public class NaturalBase {
	public ChokePoint chokepoint;
	public Region region;
	
	public NaturalBase(Region reg, ChokePoint choke) {
		this.chokepoint = choke;
		this.region = reg;
	}
}
