package com.mmxlabs.scheduler.optimiser.providers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.optimiser.core.scenario.common.IMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.MatrixEntry;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;

/**
 * A {@link IDataComponentProvider} implementation combining raw distance information with route availability information and offering basic travel time calculation APIs
 * 
 * TODO: Mixing logic and data here -> break up?
 * 
 * @author Simon Goodall
 *
 */
public class DefaultDistanceProviderImpl implements ITimedDistanceProviderEditor {

	@Inject
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	@Inject
	private IRouteCostProvider routeCostProvider;

	private final Map<String, Integer> routeAvailableFrom = new HashMap<>();

	@Inject
	@NonNull
	@Named(DataComponentProviderModule.DIRECT_ROUTE)
	private String directRoute;

	@Override
	public List<MatrixEntry<IPort, Integer>> getDistanceValues(final IPort from, final IPort to, final int voyageStartTime) {

		final List<MatrixEntry<IPort, Integer>> distances = new LinkedList<>(distanceProvider.getValues(from, to));

		// Filter out bad route choices
		final Iterator<MatrixEntry<IPort, Integer>> itr = distances.iterator();
		while (itr.hasNext()) {
			final MatrixEntry<IPort, Integer> e = itr.next();
			// No distance?
			if (e.getValue() == Integer.MAX_VALUE) {
				itr.remove();
			}
			// Distance available, but route is closed at this time
			if (!isRouteAvailable(e.getKey(), voyageStartTime)) {
				itr.remove();
			}
		}

		return distances;
	}

	@Override
	public List<MatrixEntry<IPort, Integer>> getAllDistanceValues(final IPort from, final IPort to) {

		final List<MatrixEntry<IPort, Integer>> distances = new LinkedList<>(distanceProvider.getValues(from, to));
		return distances;
	}

	@Override
	public int getRouteAvailableFrom(@NonNull final String route) {
		final Integer v = routeAvailableFrom.getOrDefault(route, Integer.MIN_VALUE);
		assert v != null;
		return v.intValue();
	}

	@Override
	public boolean isRouteAvailable(@NonNull final String route, final int voyageStartTime) {

		final int routeAvailableFrom = getRouteAvailableFrom(route);
		return (voyageStartTime >= routeAvailableFrom);
	}

	@Override
	public void setRouteAvailableFrom(final String route, final int availableFrom) {
		routeAvailableFrom.put(route, availableFrom);
	}

	@Override
	public int getDistance(@NonNull final String route, @NonNull final IPort from, @NonNull final IPort to, final int voyageStartTime) {

		if (!isRouteAvailable(route, voyageStartTime)) {
			return Integer.MAX_VALUE;
		}
		return getOpenDistance(route, from, to);
	}

	@Override
	public int getOpenDistance(@NonNull final String route, @NonNull final IPort from, @NonNull final IPort to) {
		final IMatrixProvider<IPort, Integer> matrix = distanceProvider.get(route);
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
	public int getTravelTime(@NonNull final String route, @NonNull final IVessel vessel, @NonNull final IPort from, @NonNull final IPort to, final int voyageStartTime, final int speed) {
		if (speed == 0) {
			return Integer.MAX_VALUE;
		}
		final int distance = getDistance(route, from, to, voyageStartTime);
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
	public Pair<String, Integer> getQuickestTravelTime(@NonNull final IVessel vessel, final IPort from, final IPort to, final int voyageStartTime, final int speed) {

		String bestRoute = null;
		int bestTime = Integer.MAX_VALUE;
		for (final String route : distanceProvider.getKeys()) {
			assert route != null;
			final int travelTime = getTravelTime(route, vessel, from, to, voyageStartTime, speed);
			if (travelTime < bestTime) {
				bestRoute = route;
				bestTime = travelTime;
			}
		}

		if (bestRoute == null) {
			bestRoute = directRoute;
		}

		return new Pair<>(bestRoute, bestTime);
	}

	@Override
	public List<String> getRoutes() {
		return Lists.newArrayList(distanceProvider.getKeys());
	}
}
