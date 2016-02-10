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

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
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
	public long[][] getMinimumLadenTravelTimes(IPort load, IPort discharge, IVessel vessel, int ladenStartTime) {
		// get distances for this pairing (assumes that getAllDistanceValues() returns a copy of the data)
		List<Pair<ERouteOption,Integer>> allDistanceValues = distanceProvider.getAllDistanceValues(load, discharge);
		IVesselClass vesselClass = vessel.getVesselClass();
		assert vesselClass != null;
		// sort by cost then distance
		Collections.sort(allDistanceValues, new Comparator<Pair<ERouteOption,Integer>>() {
			@Override
			public int compare(Pair<ERouteOption,Integer> o1, Pair<ERouteOption,Integer> o2) {
				if (routeCostProvider.getRouteCost(o1.getFirst(), vessel, IRouteCostProvider.CostType.Laden)==routeCostProvider.getRouteCost(o2.getFirst(), vessel, IRouteCostProvider.CostType.Laden)) {
					return Integer.compare(Calculator.getTimeFromSpeedDistance(vesselClass.getMaxSpeed(),o1.getSecond())+routeCostProvider.getRouteTransitTime(o1.getFirst(), vessel), Calculator.getTimeFromSpeedDistance(vesselClass.getMaxSpeed(), o2.getSecond())+routeCostProvider.getRouteTransitTime(o2.getFirst(), vessel)); 
				} else {
					return Long.compare(routeCostProvider.getRouteCost(o1.getFirst(), vessel, IRouteCostProvider.CostType.Laden),routeCostProvider.getRouteCost(o2.getFirst(), vessel, IRouteCostProvider.CostType.Laden));
				}
			}
		});
		
		// filter out closed distances
		allDistanceValues = allDistanceValues.stream().filter(d -> distanceProvider.isRouteAvailable(d.getFirst(),  ladenStartTime)).collect(Collectors.toList());
		
		// remove dominated distances
		for (int i = allDistanceValues.size() - 1; i > 0; i--) {
			if ((routeCostProvider.getRouteCost(allDistanceValues.get(i).getFirst(), vessel, IRouteCostProvider.CostType.Laden) >= routeCostProvider.getRouteCost(allDistanceValues.get(i - 1).getFirst(), vessel, IRouteCostProvider.CostType.Laden))
					&& allDistanceValues.get(i).getSecond() > allDistanceValues.get(i-1).getSecond()) {
				allDistanceValues.remove(i);
			}
		}
		
		// create a new distances data structure
		long[][] times = new long[allDistanceValues.size()][3];
		int i = 0;
		for (final Pair<ERouteOption, Integer> d : allDistanceValues) {
			vesselClass.getBaseFuel().getEquivalenceFactor();
			int mintravelTime = Calculator.getTimeFromSpeedDistance(vesselClass.getMaxSpeed(), d.getSecond());
			int nboSpeed = Math.min(Math.max(getNBOSpeed(vesselClass, VesselState.Laden), vesselClass.getMinSpeed()), vesselClass.getMaxSpeed());
			int nbotravelTime = Calculator.getTimeFromSpeedDistance(nboSpeed, d.getSecond());
			int transitTime = routeCostProvider.getRouteTransitTime(d.getFirst(), vessel);
			times[i][0] = mintravelTime + transitTime;
			times[i][1] = OptimiserUnitConvertor.convertToInternalDailyCost(routeCostProvider.getRouteCost(d.getFirst(), vessel, IRouteCostProvider.CostType.Laden));
			times[i][2] = nbotravelTime + transitTime;
			i++;
		}
		return times;
	}

	private int getNBOSpeed(IVesselClass vesselClass, VesselState vesselState) {
		final long nboRateInM3PerHour = vesselClass.getNBORate(vesselState);
		final long nboProvidedInMT = Calculator.convertM3ToMT(nboRateInM3PerHour, DEFAULT_CARGO_CV, vesselClass.getBaseFuel().getEquivalenceFactor());
		return vesselClass.getConsumptionRate(vesselState).getSpeed(nboProvidedInMT); 
	}

	@Override
	public List<Integer> getFeasibleRoutes(long[][] sortedCanalTimes, int minTime, int maxTime) {
		List<Integer> canalsWeCanUse = new LinkedList<>();
		for (int i = 0; i < sortedCanalTimes.length; i++) {
			if (sortedCanalTimes[i][0] <= maxTime) {
				canalsWeCanUse.add(i);
			} 
		}
		return canalsWeCanUse;
	}

	@Override
	public long[] getBestCanalDetails(long[][] sortedCanalTimes, int maxTime) {
		for (long[] canal : sortedCanalTimes) {
			if (maxTime >= canal[0]) {
				return canal;
			}
		}
		return sortedCanalTimes[sortedCanalTimes.length - 1];
	}
}
