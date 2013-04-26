/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.builder.impl;

import com.mmxlabs.scheduler.optimiser.builder.IXYPortDistanceCalculator;
import com.mmxlabs.scheduler.optimiser.components.IXYPort;

/**
 * {@link IXYPortDistanceCalculator} implementation calculating the Euclidean distance between the X/Y co-ordinates of two ports.
 * 
 * @author Simon Goodall
 * 
 */
public final class XYPortEuclideanDistanceCalculator implements IXYPortDistanceCalculator {

	@Override
	public double getDistance(final IXYPort from, final IXYPort to) {

		final float diffX = from.getX() - to.getX();
		final float diffY = from.getY() - to.getY();

		return Math.sqrt((diffX * diffX) + (diffY * diffY));
	}
}
