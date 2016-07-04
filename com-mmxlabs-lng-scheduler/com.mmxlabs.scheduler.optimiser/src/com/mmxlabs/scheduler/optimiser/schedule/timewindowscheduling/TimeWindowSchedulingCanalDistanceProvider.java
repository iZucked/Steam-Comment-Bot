/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling;

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

public class TimeWindowSchedulingCanalDistanceProvider implements ITimeWindowSchedulingCanalDistanceProvider {

	private static final int DEFAULT_CARGO_CV = 22670000;

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Inject
	private IDistanceProvider distanceProvider;

	@Override
	public @NonNull LadenRouteData @NonNull [] getMinimumLadenTravelTimes(@NonNull final IPort load, @NonNull final IPort discharge, @NonNull final IVessel vessel, final int ladenStartTime) {
		// get distances for this pairing (assumes that getAllDistanceValues() returns a copy of the data)
		List<@NonNull Pair<@NonNull ERouteOption, @NonNull Integer>> allDistanceValues = distanceProvider.getAllDistanceValues(load, discharge);
		final IVesselClass vesselClass = vessel.getVesselClass();
		assert vesselClass != null;
		// sort by cost then distance
		Collections.sort(allDistanceValues, new Comparator<Pair<@NonNull ERouteOption, @NonNull Integer>>() {
			@Override
			public int compare(final Pair<@NonNull ERouteOption, @NonNull Integer> o1, final Pair<@NonNull ERouteOption, @NonNull Integer> o2) {
				if (routeCostProvider.getRouteCost(o1.getFirst(), vessel, IRouteCostProvider.CostType.Laden) == routeCostProvider.getRouteCost(o2.getFirst(), vessel,
						IRouteCostProvider.CostType.Laden)) {
					return Integer.compare(Calculator.getTimeFromSpeedDistance(vesselClass.getMaxSpeed(), o1.getSecond()) + routeCostProvider.getRouteTransitTime(o1.getFirst(), vessel),
							Calculator.getTimeFromSpeedDistance(vesselClass.getMaxSpeed(), o2.getSecond()) + routeCostProvider.getRouteTransitTime(o2.getFirst(), vessel));
				} else {
					return Long.compare(routeCostProvider.getRouteCost(o1.getFirst(), vessel, IRouteCostProvider.CostType.Laden),
							routeCostProvider.getRouteCost(o2.getFirst(), vessel, IRouteCostProvider.CostType.Laden));
				}
			}
		});

		// filter out closed distances
		allDistanceValues = allDistanceValues.stream().filter(d -> distanceProvider.isRouteAvailable(d.getFirst(), ladenStartTime)).collect(Collectors.toList());

		// remove dominated distances
		for (int i = allDistanceValues.size() - 1; i > 0; i--) {
			if ((routeCostProvider.getRouteCost(allDistanceValues.get(i).getFirst(), vessel, IRouteCostProvider.CostType.Laden) >= routeCostProvider
					.getRouteCost(allDistanceValues.get(i - 1).getFirst(), vessel, IRouteCostProvider.CostType.Laden))
					&& allDistanceValues.get(i).getSecond() > allDistanceValues.get(i - 1).getSecond()) {
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
			final int nboSpeed = Math.min(Math.max(getNBOSpeed(vesselClass, VesselState.Laden), vesselClass.getMinSpeed()), vesselClass.getMaxSpeed());
			final int nbotravelTime = Calculator.getTimeFromSpeedDistance(nboSpeed, d.getSecond());
			final int transitTime = routeCostProvider.getRouteTransitTime(d.getFirst(), vessel);
			times[i] = new LadenRouteData(mintravelTime + transitTime, nbotravelTime + transitTime,
					OptimiserUnitConvertor.convertToInternalDailyCost(routeCostProvider.getRouteCost(d.getFirst(), vessel, IRouteCostProvider.CostType.Laden)), d.getSecond());
			i++;
		}
		return times;
	}

	private int getNBOSpeed(@NonNull final IVesselClass vesselClass, @NonNull final VesselState vesselState) {
		final long nboRateInM3PerHour = vesselClass.getNBORate(vesselState);
		final long nboProvidedInMT = Calculator.convertM3ToMT(nboRateInM3PerHour, DEFAULT_CARGO_CV, vesselClass.getBaseFuel().getEquivalenceFactor());
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
}
