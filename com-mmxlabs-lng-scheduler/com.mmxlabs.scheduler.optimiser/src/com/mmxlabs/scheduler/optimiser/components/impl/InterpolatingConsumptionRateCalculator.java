/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Map;
import java.util.TreeMap;

import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;

/**
 * A {@link IConsumptionRateCalculator} which, given a list of speed/fuel consumption rate pairs, calculates the fuel consumption rate by interpolating straight line segments connecting those pairs.
 * 
 * The pairs are provided in a TreeMap<Integer, Long>, whose keys are speeds and values are consumption rates
 * 
 * @author hinton
 * 
 */
public class InterpolatingConsumptionRateCalculator implements IConsumptionRateCalculator {

	private final TreeMap<Integer, Long> keypoints;
	private final TreeMap<Long, Integer> transposedKeypoints;

	/**
	 * Create an interpolating rate calculator from the keypoints, which maps speeds to consumption rates
	 * 
	 * @param keypoints
	 *            a map from speeds to consumption rates
	 */
	public InterpolatingConsumptionRateCalculator(final TreeMap<Integer, Long> keypoints) {
		this.keypoints = keypoints;
		transposedKeypoints = new TreeMap<Long, Integer>();
		for (final Map.Entry<Integer, Long> e : keypoints.entrySet()) {
			transposedKeypoints.put(e.getValue(), e.getKey());
		}
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

		final double p = (double) (speed - lowerBound.getKey()) / (double) diffSpeed;
		final double ir = Math.round((p * diffRate)) + lowerBound.getValue();

		return (long) ir;
	}

	@Override
	public int getSpeed(final long rate) {
		// Check for exact match first
		if (transposedKeypoints.containsKey(rate)) {
			return transposedKeypoints.get(rate);
		}

		// Need to interpolate then..
		final Map.Entry<Long, Integer> upperBound = transposedKeypoints.ceilingEntry(rate);
		final Map.Entry<Long, Integer> lowerBound = transposedKeypoints.floorEntry(rate);

		// TODO: Better error handling.
		if (upperBound == null) {
			throw new RuntimeException("Upper bound breached");
		}
		if (lowerBound == null) {
			throw new RuntimeException("Lower bound breached");
		}

		final long diffRate = upperBound.getKey() - lowerBound.getKey();
		final int diffSpeed = upperBound.getValue() - lowerBound.getValue();

		final double p = (double) (rate - lowerBound.getKey()) / (double) diffRate;
		final double is = Math.round((p * diffSpeed)) + lowerBound.getValue();

		return (int) is;
	}
}
