/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.shared.port.DistanceMatrixEntry;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

public interface IDistanceProvider extends IDataComponentProvider {

	/**
	 * Indicate whether a route is available for voyages starting at the given time
	 * 
	 * @param voyageStartTime
	 * @return
	 */
	boolean isRouteAvailable(@NonNull ERouteOption route, IVessel vessel);

	/**
	 * Get available distances. This filters out invalid distances and closed route distances.
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	@NonNull
	List<@NonNull DistanceMatrixEntry> getDistanceValues(@NonNull IPort from, @NonNull IPort to, IVessel vessel);

	@NonNull
	List<@NonNull DistanceMatrixEntry> getAllDistanceValues(@NonNull IPort from, @NonNull IPort to);

	/**
	 * Returns the distance if route is open for the given time. May return {@link Integer#MAX_VALUE} if the route is closed or there is no distance.
	 * 
	 * @param route
	 * @param from
	 * @param to
	 * @return
	 */
	int getDistance(@NonNull ERouteOption route, @NonNull IPort from, @NonNull IPort to, IVessel vessel);

	/**
	 * Returns the distance assuming the route is open. May return {@link Integer#MAX_VALUE} if there is no distance
	 * 
	 * @param route
	 * @param from
	 * @param to
	 * @return
	 */
	int getOpenDistance(@NonNull ERouteOption route, @NonNull IPort from, @NonNull IPort to);

	/**
	 * Returns the travel time between the given ports on the given route at the specified speed. This takes into account route additional transit times.
	 * 
	 * @param route
	 * @param from
	 * @param to
	 * @param voyageStartTime
	 * @param speed
	 * @return
	 */
	int getTravelTime(@NonNull ERouteOption route, @NonNull IVessel vessel, @NonNull IPort from, @NonNull IPort to, int speed);

	@NonNull
	Pair<@NonNull ERouteOption, @NonNull Integer> getQuickestTravelTime(@NonNull IVessel vessel, @NonNull IPort from, @NonNull IPort to, int speed, AvailableRouteChoices availableRouteChoices);

	ERouteOption[] getRoutes();

	/**
	 * Returns the closest entry point of a route option (canal) for a given port.
	 * 
	 * @param port
	 * @param routeOption
	 * @return
	 */
	@NonNull
	IPort getRouteOptionEntry(IPort port, ERouteOption routeOption);
}
