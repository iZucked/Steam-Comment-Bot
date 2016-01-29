/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

/**
 * Implementations of {@link IConsumptionRateCalculator} calculate vessel fuel consumption rates based on an input speed.
 * 
 * @author Simon Goodall
 * 
 */
public interface IConsumptionRateCalculator {

	/**
	 * Return the consumption rate for this speed.
	 * 
	 * @param speed
	 * @return
	 */
	long getRate(int speed);

	/**
	 * Return the speed for this consumption rate
	 * 
	 * @param rate
	 *            the consumption rate
	 * @return a speed so that getRate(speed) == rate
	 */
	int getSpeed(long rate);
}
