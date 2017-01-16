/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;

public class TimeWindowSchedulingCanalDistanceProvider implements ITimeWindowSchedulingCanalDistanceProvider {

	private static final int DEFAULT_CARGO_CV = 22670000;

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Inject
	private IDistanceProvider distanceProvider;

	@Override
	public @NonNull LadenRouteData @NonNull [] getMinimumLadenTravelTimes(@NonNull final IPort load, @NonNull final IPort discharge, @NonNull final IVessel vessel, final int ladenStartTime) {
		return getMinimumTravelTimes(load, discharge, vessel, ladenStartTime, true);
	}

	@Override
	public @NonNull LadenRouteData @NonNull [] getMinimumBallastTravelTimes(@NonNull final IPort load, @NonNull final IPort discharge, @NonNull final IVessel vessel, final int ladenStartTime) {
		return getMinimumTravelTimes(load, discharge, vessel, ladenStartTime, false);
	}

	public @NonNull LadenRouteData @NonNull [] getMinimumTravelTimes(@NonNull final IPort load, @NonNull final IPort discharge, @NonNull final IVessel vessel, final int ladenStartTime,
			boolean isLaden) {
		if (load == discharge) {
			// shortcut for same port
			return new LadenRouteData[] { new LadenRouteData(0, 0,
					0, 0, 0)};
		}
		
		// get distances for this pairing (assumes that getAllDistanceValues() returns a copy of the data)
		VesselState vesselState;
		IRouteCostProvider.CostType costType;
		if (isLaden) {
			vesselState = VesselState.Laden;
			costType = CostType.Laden;
		} else {
			vesselState = VesselState.Ballast;
			costType = CostType.Ballast;
		}

		List<@NonNull Pair<@NonNull ERouteOption, @NonNull Integer>> allDistanceValues = distanceProvider.getAllDistanceValues(load, discharge);
		final IVesselClass vesselClass = vessel.getVesselClass();
		assert vesselClass != null;
		// sort by cost then distance
		Collections.sort(allDistanceValues, new Comparator<Pair<@NonNull ERouteOption, @NonNull Integer>>() {
			@Override
			public int compare(final Pair<@NonNull ERouteOption, @NonNull Integer> o1, final Pair<@NonNull ERouteOption, @NonNull Integer> o2) {
				if (routeCostProvider.getRouteCost(o1.getFirst(), vessel, ladenStartTime, costType) == routeCostProvider.getRouteCost(o2.getFirst(), vessel, ladenStartTime, costType)) {
					return Integer.compare(Calculator.getTimeFromSpeedDistance(vesselClass.getMaxSpeed(), o1.getSecond()) + routeCostProvider.getRouteTransitTime(o1.getFirst(), vessel),
							Calculator.getTimeFromSpeedDistance(vesselClass.getMaxSpeed(), o2.getSecond()) + routeCostProvider.getRouteTransitTime(o2.getFirst(), vessel));
				} else {
					return Long.compare(routeCostProvider.getRouteCost(o1.getFirst(), vessel, ladenStartTime, costType),
							routeCostProvider.getRouteCost(o2.getFirst(), vessel, ladenStartTime, costType));
				}
			}
		});

		// filter out closed distances
		allDistanceValues = allDistanceValues.stream().filter(d -> distanceProvider.isRouteAvailable(d.getFirst(), vessel, ladenStartTime)).collect(Collectors.toList());

		// remove dominated distances
		for (int i = allDistanceValues.size() - 1; i > 0; i--) {
			if ((routeCostProvider.getRouteCost(allDistanceValues.get(i).getFirst(), vessel, ladenStartTime, costType) >= routeCostProvider.getRouteCost(allDistanceValues.get(i - 1).getFirst(),
					vessel, ladenStartTime, costType)) && allDistanceValues.get(i).getSecond() > allDistanceValues.get(i - 1).getSecond()) {
				allDistanceValues.remove(i);
			}
		}

		// create a new distances data structure
		@NonNull
		final LadenRouteData @NonNull [] times = new @NonNull LadenRouteData[allDistanceValues.size()];
		int i = 0;
		for (final Pair<@NonNull ERouteOption, @NonNull Integer> d : allDistanceValues) {
			vesselClass.getBaseFuel().getEquivalenceFactor();
			final int mintravelTime = Calculator.getTimeFromSpeedDistance(vesselClass.getMaxSpeed(), d.getSecond());
			final int nboSpeed = Math.min(Math.max(getNBOSpeed(vesselClass, vesselState), vesselClass.getMinSpeed()), vesselClass.getMaxSpeed());
			final int nbotravelTime = Calculator.getTimeFromSpeedDistance(nboSpeed, d.getSecond());
			final int transitTime = routeCostProvider.getRouteTransitTime(d.getFirst(), vessel);
			times[i] = new LadenRouteData(mintravelTime + transitTime, nbotravelTime + transitTime,
					OptimiserUnitConvertor.convertToInternalDailyCost(routeCostProvider.getRouteCost(d.getFirst(), vessel, ladenStartTime, costType)), d.getSecond(), transitTime);
			i++;
		}
		return times;
	}

	private int getNBOSpeed(@NonNull final IVesselClass vesselClass, @NonNull final VesselState vesselState) {
		return getNBOSpeed(vesselClass, vesselState, DEFAULT_CARGO_CV);
	}

	private int getNBOSpeed(@NonNull final IVesselClass vesselClass, @NonNull final VesselState vesselState, final int cv) {
		final long nboRateInM3PerHour = vesselClass.getNBORate(vesselState);
		final long nboProvidedInMT = Calculator.convertM3ToMT(nboRateInM3PerHour, cv, vesselClass.getBaseFuel().getEquivalenceFactor());
		return vesselClass.getConsumptionRate(vesselState).getSpeed(nboProvidedInMT);
	}

	@Override
	public @NonNull List<@NonNull Integer> getFeasibleRoutes(@NonNull final LadenRouteData @NonNull [] sortedCanalTimes, final int minTime, final int maxTime) {
		final List<@NonNull Integer> canalsWeCanUse = new LinkedList<>();
		for (int i = 0; i < sortedCanalTimes.length; i++) {
			if (sortedCanalTimes[i].ladenTimeAtMaxSpeed <= maxTime) {
				canalsWeCanUse.add(i);
			}
		}
		return canalsWeCanUse;
	}

	@Override
	public @NonNull LadenRouteData getBestCanalDetails(@NonNull final LadenRouteData @NonNull [] sortedCanalTimes, final int maxTime) {
		for (final LadenRouteData canal : sortedCanalTimes) {
			if (maxTime >= canal.ladenTimeAtMaxSpeed) {
				return canal;
			}
		}
		return sortedCanalTimes[sortedCanalTimes.length - 1];
	}

	/**
	 * Return a list of potential end times based on different speeds a vessel can travel and routes it can take
	 * 
	 * @param load
	 * @param discharge
	 * @param vessel
	 * @param startTime
	 * @return
	 */
	@Override
	@NonNull
	public List<Integer> getTimeDataForDifferentSpeedsAndRoutes(@NonNull final IPort load, @NonNull final IPort discharge, @NonNull final IVessel vessel, final int cv, final int startTime,
			final boolean isLaden) {
		int minSpeed;
		if (isLaden) {
			minSpeed = getNBOSpeed(vessel.getVesselClass(), VesselState.Laden, cv);
		} else {
			minSpeed = getNBOSpeed(vessel.getVesselClass(), VesselState.Ballast, cv);
		}
		// round min and max speeds to allow better speed stepping
		minSpeed = roundUpToNearest(minSpeed, 100);
		int maxSpeed = roundDownToNearest(vessel.getVesselClass().getMaxSpeed(), 100);

		// min speed needs to be bounded!
		minSpeed = Math.min(minSpeed, maxSpeed);

		// loop through speeds and canals
		int speed = minSpeed;
		@NonNull
		LadenRouteData @NonNull [] ladenRouteTimes = getMinimumTravelTimes(load, discharge, vessel, startTime, false);
		int half_a_knot = OptimiserUnitConvertor.convertToInternalSpeed(.1);
		List<Integer> times = new ArrayList<Integer>();
		while (speed <= maxSpeed) {
			for (LadenRouteData ladenRouteData : ladenRouteTimes) {
				int time = startTime + Calculator.getTimeFromSpeedDistance(speed, ladenRouteData.ladenRouteDistance) + ladenRouteData.transitTime;
				times.add(time);
			}
			speed += half_a_knot;
		}
		// return sorted unique times
		return times.stream().distinct().sorted().collect(Collectors.toList());
	}

	private static int roundUpToNearest(int input, int rounding) {
		return ((input + (rounding - 1)) / rounding) * rounding;
	}

	private static int roundDownToNearest(int input, int rounding) {
		return ((input) / rounding) * rounding;
	}
}
