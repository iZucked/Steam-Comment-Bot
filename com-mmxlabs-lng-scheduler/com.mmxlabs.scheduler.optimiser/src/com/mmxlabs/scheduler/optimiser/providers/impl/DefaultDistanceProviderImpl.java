/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IExtraIdleTimeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteExclusionProvider;
import com.mmxlabs.scheduler.optimiser.shared.port.DistanceMatrixEntry;
import com.mmxlabs.scheduler.optimiser.shared.port.IDistanceMatrixProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

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
	
	@Inject
	private IExtraIdleTimeProvider routeContingencyProvider;

	// Pair<North Entrance, South entrance>
	private final Map<ERouteOption, Pair<IPort, IPort>> routeOptionEntryPoints = new HashMap<>();
	private final Map<ERouteOption, Integer> canalDistances = new HashMap<>();
	
	// cache
	private final Map<Pair<IPort, ERouteOption>, IPort> nearestRouteOptionEntryPort = new ConcurrentHashMap<>();
	private final Map<Pair<IPort, ERouteOption>, ECanalEntry> nearestRouteOptionEntry = new ConcurrentHashMap<>();
	private final Map<Pair<IPort, ERouteOption>, RouteOptionDirection> nearestRouteOptionDirection = new ConcurrentHashMap<>();

	@Override
	public List<DistanceMatrixEntry> getDistanceValues(final IPort from, final IPort to, final IVessel vessel, AvailableRouteChoices availableRouteChoices) {

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
			} else if (!isRouteValid(e.getRoute(), availableRouteChoices)) {
				// Restricted route choice
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

		if (!isRouteAvailable(route, vessel)) {
			return Integer.MAX_VALUE;
		}
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

	public int getTravelTimeViaCanal(@NonNull final ERouteOption route, @NonNull final IVessel vessel, @NonNull final IPort entryPort, @NonNull final IPort to, final int speed) {
		if (route == ERouteOption.DIRECT) {
			throw new IllegalArgumentException("Only use this method for travel via canal.");
		}

		if (speed == 0) {
			return Integer.MAX_VALUE;
		}

		// Get the distance between entry and exit point of the canal.
		int canalDistance = getCanalDistance(route);
		if (canalDistance == Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		}

		// Need to get exit port as only direct distances available from exit to destination
		// (not via panama, since through already).
		IPort exitPort = this.getRouteOptionExitPort(entryPort, route);
		if (exitPort == null) {
			return Integer.MAX_VALUE;
		}

		// Get the direct distance, as through already.
		int legDistance = getDistance(ERouteOption.DIRECT, exitPort, to, vessel);
		if (legDistance == Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		}

		int distance = canalDistance + legDistance;

		final int baseTime = Calculator.getTimeFromSpeedDistance(speed, distance);
		if (baseTime == Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		}
		final int transitTime = routeCostProvider.getRouteTransitTime(route, vessel);
		return baseTime + transitTime;
	}
    
    private int getCanalDistance(final ERouteOption route) {
    	return canalDistances.get(route);
    }
   
	@Override
	public Pair<ERouteOption, Integer> getQuickestTravelTime(@NonNull final IVessel vessel, final IPort from, final IPort to, final int speed, AvailableRouteChoices availableRouteChoices) {

		ERouteOption bestRoute = null;
		int bestTime = Integer.MAX_VALUE;
		for (final ERouteOption route : getRoutes()) {
			
			if (!isRouteValid(route, availableRouteChoices)) {
				continue;
			}

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
	public Pair<ERouteOption, Integer> getQuickestTravelTimeWithContingency(@NonNull final IVessel vessel, final IPortSlot from, final IPortSlot to, final int speed, AvailableRouteChoices availableRouteChoices) {

		Pair<ERouteOption, Integer> result = getQuickestTravelTime(vessel, from.getPort(), to.getPort(), speed, availableRouteChoices);

		int bestTime = result.getSecond();
		if (bestTime != Integer.MAX_VALUE) {
			bestTime += routeContingencyProvider.getExtraIdleTimeInHours(from, to);
		}
		result.setSecond(bestTime);
		
		return result;
	}

	@Override
	public ERouteOption[] getRoutes() {
		return distanceProvider.getRoutes();
	}

	@Override
	public void setEntryPointsForRouteOption(ERouteOption route, IPort northEntrance, IPort southEntrance) {
		routeOptionEntryPoints.put(route, new Pair<>(northEntrance, southEntrance));
	}
	
	@Override
	public void setCanalDistance(ERouteOption route, int distance) {
		canalDistances.put(route, distance);
	}

	private IPort getRouteOptionExitPort(IPort entryPort, ERouteOption routeOption) {
		Pair<IPort,IPort> entryPoints = routeOptionEntryPoints.get(routeOption);
		if (entryPoints != null) {
			return entryPoints.getFirst().equals(entryPort) ? entryPoints.getSecond() : entryPoints.getFirst();
		}
		else {
			return null;
		}
	}
	
	@Override
	public IPort getRouteOptionEntryPort(IPort port, ERouteOption routeOption) {
		return nearestRouteOptionEntryPort.computeIfAbsent(new Pair<IPort, ERouteOption>(port, routeOption), pair -> {
			Pair<IPort, IPort> entryPoints = routeOptionEntryPoints.get(pair.getSecond());
			if (entryPoints != null) {

				int distanceToNorthEntrance = getDistance(ERouteOption.DIRECT, port, entryPoints.getFirst(), null);
				int distanceToSouthEntrance = getDistance(ERouteOption.DIRECT, port, entryPoints.getSecond(), null);
				if (distanceToNorthEntrance < distanceToSouthEntrance) {
					return entryPoints.getFirst();
				} else {
					return entryPoints.getSecond();
				}
			}
			return null;
		});
	}

	@Override
	public RouteOptionDirection getRouteOptionDirection(IPort port, ERouteOption routeOption) {
		return nearestRouteOptionDirection.computeIfAbsent(new Pair<IPort, ERouteOption>(port, routeOption), pair -> {
			Pair<IPort, IPort> entryPoints = routeOptionEntryPoints.get(pair.getSecond());
			if (entryPoints != null) {

				int distanceToNorthEntrance = getDistance(ERouteOption.DIRECT, port, entryPoints.getFirst(), null);
				int distanceToSouthEntrance = getDistance(ERouteOption.DIRECT, port, entryPoints.getSecond(), null);
				if (distanceToNorthEntrance < distanceToSouthEntrance) {
					return RouteOptionDirection.SOUTHBOUND;
				} else {
					return RouteOptionDirection.NORTHBOUND;
				}
			}
			return null;
		});
	}

	private boolean isRouteValid(ERouteOption routeOption, AvailableRouteChoices availableRouteChoice) {
		if (availableRouteChoice == AvailableRouteChoices.EXCLUDE_PANAMA && routeOption == ERouteOption.PANAMA) {
			return false;
		}

		if (availableRouteChoice == AvailableRouteChoices.DIRECT_ONLY && routeOption != ERouteOption.DIRECT) {
			return false;
		}

		if (availableRouteChoice == AvailableRouteChoices.PANAMA_ONLY && routeOption != ERouteOption.PANAMA) {
			return false;
		}

		if (availableRouteChoice == AvailableRouteChoices.SUEZ_ONLY && routeOption != ERouteOption.SUEZ) {
			return false;
		}
		return true;
	}

	@Override
	public ECanalEntry getRouteOptionCanalEntrance(@NonNull IPort port, ERouteOption routeOption) {

		return nearestRouteOptionEntry.computeIfAbsent(new Pair<IPort, ERouteOption>(port, routeOption), pair -> {
			Pair<IPort, IPort> entryPoints = routeOptionEntryPoints.get(pair.getSecond());
			if (entryPoints != null) {

				int distanceToNorthEntrance = getDistance(ERouteOption.DIRECT, port, entryPoints.getFirst(), null);
				int distanceToSouthEntrance = getDistance(ERouteOption.DIRECT, port, entryPoints.getSecond(), null);
				if (distanceToNorthEntrance < distanceToSouthEntrance) {
					return ECanalEntry.NorthSide;
				} else {
					return ECanalEntry.SouthSide;
				}
			}
			return null;
		});
	}

	@Override
	public IPort getRouteOptionEntryPort(ERouteOption routeOption, ECanalEntry canalEntry) {
		Pair<IPort, IPort> p = routeOptionEntryPoints.get(routeOption);
		if (p == null) {
			return null;
		}
		if (canalEntry == ECanalEntry.NorthSide) {
			return p.getFirst();
		}
		if (canalEntry == ECanalEntry.SouthSide) {
			return p.getSecond();
		}
		return null;
	}
}
