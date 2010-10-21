/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;

/**
 * Implementation of {@link IConsumptionRateCalculator} which wraps another
 * {@link IConsumptionRateCalculator} instance and caches the result in a
 * {@link HashMap} for future lookups.
 * 
 * @author Simon Goodall
 * 
 */
public final class CachingConsumptionRateCalculator implements
		IConsumptionRateCalculator {

	private final IConsumptionRateCalculator calc;

	/**
	 * Cache of speed to rate pairs.
	 */
	private final Map<Integer, Long> cache = new HashMap<Integer, Long>();

	public CachingConsumptionRateCalculator(IConsumptionRateCalculator calc) {
		this.calc = calc;
	}

	@Override
	public long getRate(int speed) {

		// Check cache first,
		if (cache.containsKey(speed)) {
			return cache.get(speed);
		}

		// Fallback to real calculator
		long rate = calc.getRate(speed);

		// Cache the value
		cache.put(speed, rate);

		return rate;
	}

	@Override
	public int getSpeed(long rate) {
		return calc.getSpeed(rate);
	}
}
