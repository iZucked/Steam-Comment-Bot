/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import com.mmxlabs.common.curves.ILongCurve;

/**
 * For calculating the cost of chartering in a vessel.
 */
public interface ICharterCostCalculator extends ICalculator {

	/**
	 * Get the total charter cost for the duration. 
	 * @param voyagePlanStartTimeUTC
	 * @param eventStartTime 
	 * @param duration in hours
	 * @return the total charter cost.
	 */
	long getCharterCost(int voyagePlanStartTimeUTC, int eventStartTime, int duration);
	
	/**
	 * Set the charter rate curve to be used for calculation purposes.
	 * @param charterRateCurve
	 */
	default void setCharterRateCurve(final ILongCurve charterRateCurve) { }
}
