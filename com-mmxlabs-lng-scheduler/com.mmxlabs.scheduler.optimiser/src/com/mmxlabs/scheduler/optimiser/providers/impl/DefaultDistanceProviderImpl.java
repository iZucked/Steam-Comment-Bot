/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.optimiser.core.scenario.common.IMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.MatrixEntry;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteExclusionProvider;

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
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	@Inject
	private IRouteCostProvider routeCostProvider;
	
	@Inject
	private IRouteExclusionProvider routeExclusionProvider;
	
	private final Map<ERouteOption, Integer> routeAvailableFrom = new HashMap<>();

	@Override
	public List<Pair<ERouteOption, Integer>> getDistanceValues(final IPort from, final IPort to, final int voyageStartTime, final IVessel vessel) {

		List<Pair<ERouteOption, Integer>> distances = getAllDistanceValues(from, to);

		// Filter out bad route choices
		final Iterator<Pair<ERouteOption, Integer>> itr = distances.iterator();
		while (itr.hasNext()) {
			final Pair<ERouteOption, Integer> e = itr.next();
			// No distance?
			if (e.getSecond() == Integer.MAX_VALUE) {
				itr.remove();
			} else if (!isRouteAvailable(e.getFirst(), vessel, voyageStartTime)) {
				// Distance available, but route is closed at this time
				itr.remove();
			}
		}
		return distances;

	}

	@Override
	public List<Pair<ERouteOption, Integer>> getAllDistanceValues(final IPort from, final IPort to) {

		final Collection<MatrixEntry<IPort, Integer>> distances = distanceProvider.getValues(from, to);
		return distances.stream().map(e -> new Pair<ERouteOption, Integer>(ERouteOption.valueOf(e.getKey()), e.getValue())).collect(Collectors.toList());
	}

	@Override
	public int getRouteAvailableFrom(@NonNull final ERouteOption route) {
		final Integer v = routeAvailableFrom.getOrDefault(route, Integer.MIN_VALUE);
		assert v != null;
		return v.intValue();
	}

	@Override
	public boolean isRouteAvailable(@NonNull final ERouteOption route, final IVessel vessel, final int voyageStartTime) {
		final int routeAvailableFrom = getRouteAvailableFrom(route);
		return (voyageStartTime >= routeAvailableFrom) && routeExclusionProvider.isRouteEnabled(vessel, route);
	}

	@Override
	public void setRouteAvailableFrom(final ERouteOption route, final int availableFrom) {
		routeAvailableFrom.put(route, availableFrom);
	}

	@Override
	public int getDistance(@NonNull final ERouteOption route, @NonNull final IPort from, @NonNull final IPort to, final int voyageStartTime, final IVessel vessel) {

		if (!isRouteAvailable(route, vessel, voyageStartTime)) {
			return Integer.MAX_VALUE;
		}
		return getOpenDistance(route, from, to);
	}

	@Override
	public int getOpenDistance(@NonNull final ERouteOption route, @NonNull final IPort from, @NonNull final IPort to) {
		final IMatrixProvider<IPort, Integer> matrix = distanceProvider.get(route.name());
		if (matrix == null) {
			return Integer.MAX_VALUE;
		}

		final Integer distance = matrix.get(from, to);
		if (distance == null) {
			return Integer.MAX_VALUE;
		}
		return distance;
	}

	@Override
	public int getTravelTime(@NonNull final ERouteOption route, @NonNull final IVessel vessel, @NonNull final IPort from, @NonNull final IPort to, final int voyageStartTime, final int speed) {
		if (speed == 0) {
			return Integer.MAX_VALUE;
		}
		final int distance = getDistance(route, from, to, voyageStartTime, vessel);
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
	public Pair<ERouteOption, Integer> getQuickestTravelTime(@NonNull final IVessel vessel, final IPort from, final IPort to, final int voyageStartTime, final int speed) {

		ERouteOption bestRoute = null;
		int bestTime = Integer.MAX_VALUE;
		for (final ERouteOption route : getRoutes()) {
			assert route != null;
			final int travelTime = getTravelTime(route, vessel, from, to, voyageStartTime, speed);
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
	public List<ERouteOption> getRoutes() {
		// TODO: Pre-calculate?
		return Lists.newArrayList(distanceProvider.getKeys()).stream().map(e -> ERouteOption.valueOf(e)).collect(Collectors.toList());
	}
}
