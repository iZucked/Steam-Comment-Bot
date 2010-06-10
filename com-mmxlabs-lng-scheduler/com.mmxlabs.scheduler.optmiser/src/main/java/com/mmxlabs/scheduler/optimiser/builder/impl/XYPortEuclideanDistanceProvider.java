package com.mmxlabs.scheduler.optimiser.builder.impl;

import com.mmxlabs.scheduler.optimiser.builder.IXYPortDistanceProvider;
import com.mmxlabs.scheduler.optimiser.components.IXYPort;

/**
 * {@link IXYPortDistanceProvider} implementation calculating the Euclidean
 * distance between the X/Y co-ordinates of two ports.
 * 
 * @author Simon Goodall
 * 
 */
public final class XYPortEuclideanDistanceProvider implements
		IXYPortDistanceProvider {

	public double getDistance(final IXYPort from, final IXYPort to) {

		final float diffX = from.getX() - to.getX();
		final float diffY = from.getY() - to.getY();

		return Math.sqrt((diffX * diffX) + (diffY * diffY));
	}
}
