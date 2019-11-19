package com.mmxlabs.scheduler.optimiser.contracts;

/**
 * For calculating the cost of chartering in a vessel.
 */
public interface ICharterCostCalculator extends ICalculator {

	/**
	 * Get the total charter cost for the duration. 
	 * @param vesselStartTime
	 * @param voyagePlanStartTime
	 * @param eventStartTime 
	 * @param duration in hours
	 * @return the total charter cost.
	 */
	long getCharterCost(int vesselStartTime, int voyagePlanStartTime, int eventStartTime, int duration);
}
