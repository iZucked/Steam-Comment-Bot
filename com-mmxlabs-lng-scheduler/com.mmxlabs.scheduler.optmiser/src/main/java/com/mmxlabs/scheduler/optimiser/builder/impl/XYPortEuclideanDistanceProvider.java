package com.mmxlabs.scheduler.optimiser.builder.impl;

import com.mmxlabs.scheduler.optimiser.components.IXYPort;

public class XYPortEuclideanDistanceProvider {

	double getDistance(IXYPort from, IXYPort to) {
		
		float diffX = from.getX() - to.getX();
		float diffY = from.getY() - to.getY();
		
		return Math.sqrt((diffX * diffX) + (diffY * diffY));
	}
}
