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
 * 
 * @author achurchill
 *
 */
public interface ITimeWindowSchedulingCanalDistanceProvider {
	public static final int MINIMUM_LADEN_TRAVEL_TIMES_FASTEST_LADEN_TRAVEL_TIME = 0;
	public static final int MINIMUM_LADEN_TRAVEL_TIMES_LADEN_ROUTE_COST = 1;
	public static final int MINIMUM_LADEN_TRAVEL_TIMES_NBO_LADEN_TRAVEL_TIME = 2;

	/**
	 * Provides the minimum travel time for a given load-discharge pair and vessel class
	 * 
	 * @param load
	 * @param discharge
	 * @param vessel
	 *            TODO
	 * @return
	 */
	@NonNull
	LadenRouteData @NonNull [] getMinimumLadenTravelTimes(@NonNull IPort load, @NonNull IPort discharge, @NonNull IVessel vessel, int ladenStartTime);

	/**
	 * Get feasible routes for min and max times
	 * 
	 * @param sortedCanalTimes
	 * @param minTime
	 * @param maxTime
	 * @return
	 */
	@NonNull
	List<@NonNull Integer> getFeasibleRoutes(@NonNull LadenRouteData @NonNull [] sortedCanalTimes, int minTime, int maxTime);

	/**
	 * Get an array of the min time and cost of the best route we can take
	 * 
	 * @param sortedCanalTimes
	 * @param maxTime
	 * @return
	 */
	@NonNull
	LadenRouteData getBestCanalDetails(@NonNull LadenRouteData @NonNull [] sortedCanalTimes, int maxTime);

	/**
	 * Return a list of potential end times based on different speeds a vessel can travel and routes it can take
	 * @param load
	 * @param discharge
	 * @param cv TODO
	 * @param vessel
	 * @param startTime
	 * @return
	 */
	@NonNull
	List<Integer> getTimeDataForDifferentSpeedsAndRoutes(@NonNull IPort load, @NonNull IPort discharge, @NonNull IVessel vessel, int cv, int startTime, boolean isLaden);

	@NonNull
	LadenRouteData @NonNull [] getMinimumBallastTravelTimes(@NonNull IPort load, @NonNull IPort discharge, @NonNull IVessel vessel, int ladenStartTime);

}
