/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling;


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
	public static final int MINIMUM_LADEN_TRAVEL_TIMES_FASTEST_LADEN_TRAVEL_TIME = 0;
	public static final int MINIMUM_LADEN_TRAVEL_TIMES_LADEN_ROUTE_COST = 1;
	public static final int MINIMUM_LADEN_TRAVEL_TIMES_NBO_LADEN_TRAVEL_TIME = 2;
	
	/**
	 * Provides the minimum travel time for a given load-discharge pair and vessel class
	 * @param load
	 * @param discharge
	 * @param vessel TODO
	 * @return
	 */
	@NonNull
	public LadenRouteData[] getMinimumLadenTravelTimes(IPort load, IPort discharge, IVessel vessel, int ladenStartTime);

	/**
	 * Get feasible routes for min and max times
	 * @param sortedCanalTimes
	 * @param minTime
	 * @param maxTime
	 * @return
	 */
	@NonNull
	public List<Integer> getFeasibleRoutes(LadenRouteData[] sortedCanalTimes, int minTime, int maxTime);

	/**
	 * Get an array of the min time and cost of the best route we can take
	 * @param sortedCanalTimes
	 * @param maxTime
	 * @return
	 */
	@NonNull
	LadenRouteData getBestCanalDetails(LadenRouteData[] sortedCanalTimes, int maxTime);
	
}
