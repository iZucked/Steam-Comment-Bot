package com.mmxlabs.scheduler.optimiser.providers;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

public interface IDistanceProvider extends IDataComponentProvider {

	/**
	 * Returns the time the route is available from. Returns {@link Integer#MIN_VALUE} if always open.
	 * 
	 * @param route
	 * @return
	 */
	int getRouteAvailableFrom(@NonNull ERouteOption route);

	/**
	 * Indicate whether a route is available for voyages starting at the given time
	 * 
	 * @param voyageStartTime
	 * @return
	 */
	boolean isRouteAvailable(@NonNull ERouteOption route, int voyageStartTime);

	/**
	 * Get available distances. This filters out invalid distances and closed route distances.
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	@NonNull
	List<Pair<ERouteOption, Integer>> getDistanceValues(@NonNull IPort from, @NonNull IPort to, int voyageStartTime);

	@Deprecated
	default List<Pair<ERouteOption, Integer>> getDistanceValues(@NonNull IPort from, @NonNull IPort to) {
		return getDistanceValues(from, to, Integer.MIN_VALUE);
	}

	@NonNull
	List<Pair<ERouteOption, Integer>> getAllDistanceValues(@NonNull IPort from, @NonNull IPort to);

	/**
	 * Returns the distance if route is open for the given time. May return {@link Integer#MAX_VALUE} if the route is closed or there is no distance.
	 * 
	 * @param route
	 * @param from
	 * @param to
	 * @param voyageStartTime
	 * @return
	 */
	int getDistance(@NonNull ERouteOption route, @NonNull IPort from, @NonNull IPort to, int voyageStartTime);
	
	@Deprecated
	default int getDistance(@NonNull ERouteOption route, @NonNull IPort from, @NonNull IPort to) {
		return getDistance(route, from, to, Integer.MIN_VALUE);
	}

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
	int getTravelTime(@NonNull ERouteOption route, @NonNull IVessel vessel, @NonNull IPort from, @NonNull IPort to, int voyageStartTime, int speed);

	@NonNull
	Pair<ERouteOption, Integer> getQuickestTravelTime(@NonNull IVessel vessel, @NonNull IPort from, @NonNull IPort to, int voyageStartTime, int speed);

	@NonNull
	List<ERouteOption> getRoutes();
}
