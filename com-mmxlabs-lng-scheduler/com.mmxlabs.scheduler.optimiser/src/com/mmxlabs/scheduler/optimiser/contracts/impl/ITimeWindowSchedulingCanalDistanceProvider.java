package com.mmxlabs.scheduler.optimiser.contracts.impl;


import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * An interface to provide details on routes that can be used for pairs of time windows.
 * @author achurchill
 *
 */
public interface ITimeWindowSchedulingCanalDistanceProvider {
	/**
	 * Provides the minimum travel time for a given load-discharge pair and vessel class
	 * @param load
	 * @param discharge
	 * @param vessel TODO
	 * @return
	 */
	@NonNull
	public long[][] getMinimumLadenTravelTimes(IPort load, IPort discharge, IVessel vessel, int ladenStartTime);

	/**
	 * Get feasible routes for min and max times
	 * @param sortedCanalTimes
	 * @param minTime
	 * @param maxTime
	 * @return
	 */
	@NonNull
	public List<Integer> getFeasibleRoutes(long[][] sortedCanalTimes, int minTime, int maxTime);

	/**
	 * Get an array of the min time and cost of the best route we can take
	 * @param times
	 * @param maxTime
	 * @return
	 */
	@NonNull
	long[] getBestCanalDetails(long[][] times, int maxTime);
	
}
