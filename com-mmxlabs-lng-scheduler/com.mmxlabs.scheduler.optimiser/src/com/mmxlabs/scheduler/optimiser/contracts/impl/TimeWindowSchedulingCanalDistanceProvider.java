package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.MatrixEntry;
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
		long[][] times = new long[allDistanceValues.size()][2];
		int i = 0;
		for (final Pair<ERouteOption, Integer> d : allDistanceValues) {
			int travelTime = Calculator.getTimeFromSpeedDistance(vesselClass.getMaxSpeed(), d.getSecond());
			int transitTime = routeCostProvider.getRouteTransitTime(d.getFirst(), vessel);
			times[i][0] = travelTime + transitTime;
			times[i][1] = OptimiserUnitConvertor.convertToInternalDailyCost(routeCostProvider.getRouteCost(d.getFirst(), vessel, IRouteCostProvider.CostType.Laden));
			i++;
		}
		return times;
	}

	@Override
	public List<Integer> getFeasibleRoutes(long[][] sortedCanalTimes, int minTime, int maxTime) {
		List<Integer> canalsWeCanUse = new LinkedList<>();
		for (int i = 0; i < sortedCanalTimes.length; i++) {
			if (sortedCanalTimes[i][0] <= maxTime) {
				canalsWeCanUse.add(i);
			} else {
				break;
			}
		}
		return canalsWeCanUse;
	}

	@Override
	public long[] getBestCanalDetails(long[][] times, int maxTime) {
		for (long[] canal : times) {
			if (maxTime >= canal[0]) {
				return canal;
			}
		}
		return times[times.length - 1];
	}
}
