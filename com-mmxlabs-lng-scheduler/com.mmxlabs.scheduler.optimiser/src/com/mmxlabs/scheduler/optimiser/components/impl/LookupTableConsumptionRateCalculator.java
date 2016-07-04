/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;

/**
 * {@link IConsumptionRateCalculator} implementation which takes an existing {@link IConsumptionRateCalculator} and a min/max bounds to generate a lookup table of speed to rate pairs. Note if the
 * range between min and max bounds is large, then the lookup table will also be large.
 * 
 * @author Simon Goodall
 * 
 */
public final class LookupTableConsumptionRateCalculator implements IConsumptionRateCalculator {

	private final int minSpeed;

	private final int maxSpeed;

	private long[] table;

	/**
	 * Constructor.
	 * 
	 * @param minSpeed
	 *            Inclusive min speed
	 * @param maxSpeed
	 *            Inclusive max speed
	 * @param calc
	 */
	public LookupTableConsumptionRateCalculator(final int minSpeed, final int maxSpeed, final IConsumptionRateCalculator calc) {
		this.minSpeed = minSpeed;
		this.maxSpeed = maxSpeed;

		init(calc);
	}

	@Override
	public long getRate(final int speed) {
		return table[speed - minSpeed];
	}

	@Override
	public int getSpeed(final long rate) {
		for (int i = 0; i < table.length; i++) {
			if (table[i] == rate) {
				return i + minSpeed;
			} else if (table[i] > rate) {
				return i > 0 ? i - 1 + minSpeed : minSpeed;
			}
		}
		// Hit max speed
		return minSpeed + table.length;
		// throw new IllegalStateException();
	}

	private void init(final IConsumptionRateCalculator calc) {
		final int s = (maxSpeed - minSpeed) + 1;

		table = new long[s];

		for (int i = 0; i < table.length; ++i) {

			final int speed = minSpeed + i;
			final long rate = calc.getRate(speed);
			table[i] = rate;
		}
	}
}
