/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
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
	 * @param availableRouteChoice
	 * @return
	 */
	@NonNull
	default List<@NonNull DistanceMatrixEntry> getDistanceValues(@NonNull IPort from, @NonNull IPort to, IVessel vessel) {
		return getDistanceValues(from, to, vessel, AvailableRouteChoices.OPTIMAL);
	}

	@NonNull
	List<@NonNull DistanceMatrixEntry> getDistanceValues(@NonNull IPort from, @NonNull IPort to, IVessel vessel, AvailableRouteChoices availableRouteChoice);

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

	@NonNull
	Pair<@NonNull ERouteOption, @NonNull Integer> getQuickestTravelTimeWithContingency(@NonNull IVessel vessel, @NonNull IPortSlot from, @NonNull IPortSlot to, int speed, AvailableRouteChoices availableRouteChoices);
	
	ERouteOption[] getRoutes();

	/**
	 * Returns the closest entry point of a route option (canal) for a given port.
	 * 
	 * @param port
	 * @param routeOption
	 * @return
	 */
	@Nullable
	IPort getRouteOptionEntryPort(IPort port, ERouteOption routeOption);

	public enum RouteOptionDirection {
		NORTHBOUND, SOUTHBOUND
	};

	/**
	 * Returns the {@link RouteOptionDirection} for the route, given the <b>source</b> port.
	 * @param port The source port
	 * @param routeOption
	 * @return
	 */
	RouteOptionDirection getRouteOptionDirection(IPort port, ERouteOption routeOption);

	ECanalEntry getRouteOptionCanalEntrance(@NonNull IPort port, ERouteOption routeOption);

	IPort getRouteOptionEntryPort(ERouteOption routeOption, ECanalEntry canalEntry);
}
