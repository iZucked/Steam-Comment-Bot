/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteExclusionProvider;
import com.mmxlabs.scheduler.optimiser.shared.port.DistanceMatrixEntry;
import com.mmxlabs.scheduler.optimiser.shared.port.IDistanceMatrixProvider;

/**
 * A {@link IDataComponentProvider} implementation combining raw distance information with route availability information and offering basic travel time calculation APIs
 * 
 * TODO: Mixing logic and data here -> break up?
 * 
 * @author Simon Goodall
 *
 */
public class DefaultDistanceProviderImpl implements IDistanceProviderEditor {

	@Inject
	private IDistanceMatrixProvider distanceProvider;

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Inject
	private IRouteExclusionProvider routeExclusionProvider;

	@Override
	public List<DistanceMatrixEntry> getDistanceValues(final IPort from, final IPort to, final IVessel vessel) {

		List<DistanceMatrixEntry> distances = getAllDistanceValues(from, to);

		// Filter out bad route choices
		final Iterator<DistanceMatrixEntry> itr = distances.iterator();
		while (itr.hasNext()) {
			final DistanceMatrixEntry e = itr.next();
			// No distance?
			if (e.getDistance() == Integer.MAX_VALUE) {
				itr.remove();
			} else if (!isRouteAvailable(e.getRoute(), vessel)) {
				// Distance available, but route is closed at this time
				itr.remove();
			}
		}
		return distances;

	}

	@Override
	public List<DistanceMatrixEntry> getAllDistanceValues(final IPort from, final IPort to) {

		return distanceProvider.getValues(from, to);
	}

	@Override
	public boolean isRouteAvailable(@NonNull final ERouteOption route, final IVessel vessel) {
		return routeExclusionProvider.isRouteEnabled(vessel, route);
	}

	@Override
	public int getDistance(@NonNull final ERouteOption route, @NonNull final IPort from, @NonNull final IPort to, final IVessel vessel) {

		return getOpenDistance(route, from, to);
	}

	@Override
	public int getOpenDistance(@NonNull final ERouteOption route, @NonNull final IPort from, @NonNull final IPort to) {

		return distanceProvider.get(route, from, to);
	}

	@Override
	public int getTravelTime(@NonNull final ERouteOption route, @NonNull final IVessel vessel, @NonNull final IPort from, @NonNull final IPort to, final int speed) {
		if (speed == 0) {
			return Integer.MAX_VALUE;
		}
		final int distance = getDistance(route, from, to, vessel);
		if (distance == Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		}
		final int baseTime = Calculator.getTimeFromSpeedDistance(speed, distance);
		if (baseTime == Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		}
		final int transitTime = routeCostProvider.getRouteTransitTime(route, vessel);
		return baseTime + transitTime;
	}

	@Override
	public Pair<ERouteOption, Integer> getQuickestTravelTime(@NonNull final IVessel vessel, final IPort from, final IPort to, final int speed) {

		ERouteOption bestRoute = null;
		int bestTime = Integer.MAX_VALUE;
		for (final ERouteOption route : getRoutes()) {
			assert route != null;
			final int travelTime = getTravelTime(route, vessel, from, to, speed);
			if (travelTime < bestTime) {
				bestRoute = route;
				bestTime = travelTime;
			}
		}

		if (bestRoute == null) {
			bestRoute = ERouteOption.DIRECT;
		}

		return new Pair<>(bestRoute, bestTime);
	}

	@Override
	public ERouteOption[] getRoutes() {
		return distanceProvider.getRoutes();
	}
}
