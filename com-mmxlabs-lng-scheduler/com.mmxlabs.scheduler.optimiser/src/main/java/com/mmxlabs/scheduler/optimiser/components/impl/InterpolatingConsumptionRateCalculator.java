package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Map;
import java.util.TreeMap;

import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;

public class InterpolatingConsumptionRateCalculator implements
		IConsumptionRateCalculator {

	private final TreeMap<Integer, Long> keypoints;

	public InterpolatingConsumptionRateCalculator(
			final TreeMap<Integer, Long> keypoints) {
		this.keypoints = keypoints;
	}

	@Override
	public long getRate(final int speed) {

		// Check for exact match first
		if (keypoints.containsKey(speed)) {
			return keypoints.get(speed);
		}

		// Need to interpolate then..
		final Map.Entry<Integer, Long> upperBound = keypoints.ceilingEntry(speed);
		final Map.Entry<Integer, Long> lowerBound = keypoints.floorEntry(speed);

		// TODO: Better error handling.
		if (upperBound == null) {
			throw new RuntimeException("Upper bound breached");
		}
		if (lowerBound == null) {
			throw new RuntimeException("Lower bound breached");
		}

		final int diffSpeed = upperBound.getKey() - lowerBound.getKey();
		final long diffRate = upperBound.getValue() - lowerBound.getValue();

		final double p = (double) (speed - lowerBound.getKey())
				/ (double) diffSpeed;
		final double ir = Math.round((p * (double) diffRate))
				+ lowerBound.getValue();

		return (long) ir;
	}
}
