package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.MatrixEntry;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;

public class TimeWindowSchedulingCanalDistanceProvider implements ITimeWindowSchedulingCanalDistanceProvider {

	@Inject
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Override
	public long[][] getMinimumTravelTimes(IPort load, IPort discharge, IVesselClass vesselClass) {
		final List<MatrixEntry<IPort, Integer>> distances = new ArrayList<MatrixEntry<IPort, Integer>>(distanceProvider.getValues(load, discharge));
		Collections.sort(distances, new Comparator<MatrixEntry<IPort, Integer>>() {
			@Override
			public int compare(MatrixEntry<IPort, Integer> o1, MatrixEntry<IPort, Integer> o2) {
				if (routeCostProvider.getRouteCost(o1.getKey(), vesselClass, VesselState.Laden)==routeCostProvider.getRouteCost(o2.getKey(), vesselClass, VesselState.Laden)) {
					return Integer.compare(Calculator.getTimeFromSpeedDistance(vesselClass.getMaxSpeed(),o1.getValue())+routeCostProvider.getRouteTransitTime(o1.getKey(), vesselClass), Calculator.getTimeFromSpeedDistance(vesselClass.getMaxSpeed(), o2.getValue())+routeCostProvider.getRouteTransitTime(o2.getKey(), vesselClass)); 
				} else {
					return Long.compare(routeCostProvider.getRouteCost(o1.getKey(), vesselClass, VesselState.Laden),routeCostProvider.getRouteCost(o2.getKey(), vesselClass, VesselState.Laden));
				}
			}
		});
		
		for (int i = distances.size() - 1; i > 0; i--) {
			if ((routeCostProvider.getRouteCost(distances.get(i).getKey(), vesselClass, VesselState.Laden) >= routeCostProvider.getRouteCost(distances.get(i - 1).getKey(), vesselClass, VesselState.Laden))
					&& distances.get(i).getValue() > distances.get(i-1).getValue()) {
				distances.remove(i);
			}
		}
		
		long[][] times = new long[distances.size()][2];
		int i = 0;
		for (final MatrixEntry<IPort, Integer> d : distances) {
			int distance = d.getValue();
			int travelTime = Calculator.getTimeFromSpeedDistance(vesselClass.getMaxSpeed(), distance);
			int transitTime = routeCostProvider.getRouteTransitTime(d.getKey(), vesselClass);
			times[i][0] = travelTime + transitTime;
			times[i][1] = OptimiserUnitConvertor.convertToInternalDailyCost(routeCostProvider.getRouteCost(d.getKey(), vesselClass, VesselState.Laden));
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
