/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

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
	 * @param isConstrainedPanamaVoyage
	 * @param additionalPanamaIdleHours
	 * @return
	 */
	@NonNull
	TravelRouteData @NonNull [] getMinimumTravelTimes(@NonNull IPort load, @NonNull IPort discharge, @NonNull IVessel vessel, int ladenStartTime, AvailableRouteChoices availableRouteChoice, boolean isConstrainedPanamaVoyage, int additionalPanamaIdleHours, boolean isLaden);

	/**
	 * Get feasible routes for min and max times
	 * 
	 * @param sortedCanalTimes
	 * @param minTime
	 * @param maxTime
	 * @return
	 */
	@NonNull
	List<@NonNull Integer> getFeasibleRoutes(@NonNull TravelRouteData @NonNull [] sortedCanalTimes, int minTime, int maxTime);

	/**
	 * Get an array of the min time and cost of the best route we can take
	 * 
	 * @param sortedCanalTimes
	 * @param maxTime
	 * @return
	 */
	@NonNull
	TravelRouteData getBestCanalDetails(@NonNull TravelRouteData @NonNull [] sortedCanalTimes, int maxTime);

	/**
	 * Return a list of potential end times based on different speeds a vessel can travel and routes it can take
	 * 
	 * @param load
	 * @param discharge
	 * @param cv
	 * @param vessel
	 * @param startTime
	 * @return
	 */
	@NonNull
	List<Integer> getTimeDataForDifferentSpeedsAndRoutes(@NonNull IPort load, @NonNull IPort discharge, @NonNull IVessel vessel, int cv, int startTime, boolean isLaden,
			AvailableRouteChoices availableRouteChoice, boolean isConstrainedPanamaVoyage, int additionalPanamaIdleHours);
}
