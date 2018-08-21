/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
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
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider.RouteOptionDirection;
import com.mmxlabs.scheduler.optimiser.providers.IPortCVProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;
import com.mmxlabs.scheduler.optimiser.schedule.PanamaBookingHelper;
import com.mmxlabs.scheduler.optimiser.shared.port.DistanceMatrixEntry;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PanamaPeriod;

public class TimeWindowSchedulingCanalDistanceProvider implements ITimeWindowSchedulingCanalDistanceProvider {

	private static final int DEFAULT_CARGO_CV = 22670000;

	private static final int half_a_knot = OptimiserUnitConvertor.convertToInternalSpeed(.1);

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private PanamaBookingHelper panamaBookingHelper;
	
	@Inject
	private IPortCVProvider portCVProvider;

	@Override
	public @NonNull LadenRouteData @NonNull [] getMinimumLadenTravelTimes(@NonNull final IPort load, @NonNull final IPort discharge, @NonNull final IVessel vessel, final int ladenStartTime,
			AvailableRouteChoices availableRouteChoice, boolean isConstrainedPanamaVoyage, int additionalPanamaIdleHours) {
		return getMinimumTravelTimes(load, discharge, vessel, ladenStartTime, true, availableRouteChoice, isConstrainedPanamaVoyage, additionalPanamaIdleHours);
	}

	@Override
	public @NonNull LadenRouteData @NonNull [] getMinimumBallastTravelTimes(@NonNull final IPort load, @NonNull final IPort discharge, @NonNull final IVessel vessel, int ladenStartTime,
			final AvailableRouteChoices availableRouteChoice, boolean isConstrainedPanamaVoyage, int additionalPanamaIdleHours) {
		return getMinimumTravelTimes(load, discharge, vessel, ladenStartTime, false, availableRouteChoice, isConstrainedPanamaVoyage, additionalPanamaIdleHours);
	}

	private @NonNull LadenRouteData @NonNull [] getMinimumTravelTimes(@NonNull final IPort from, @NonNull final IPort to, @NonNull final IVessel vessel, int voyageStartTime, boolean isLaden,
			final AvailableRouteChoices availableRouteChoice, boolean isConstrainedPanamaVoyage, int additionalPanamaIdleHours) {
		
		if (from == to) {
			// shortcut for same port
			return new LadenRouteData[] { new LadenRouteData(0, 0, 0, 0, 0) };
		}

		if (isConstrainedPanamaVoyage && distanceProvider.getRouteOptionDirection(from, ERouteOption.PANAMA) == RouteOptionDirection.NORTHBOUND) {
			final int toCanal = panamaBookingHelper.getTravelTimeToCanal(vessel, from, true);
			if (toCanal != Integer.MAX_VALUE) {
				int estimatedCanalArrival = voyageStartTime + toCanal;
				if (panamaBookingHelper.getPanamaPeriod(estimatedCanalArrival) == PanamaPeriod.Beyond) {
					additionalPanamaIdleHours = 0;
				}
			}
		}

		final int finalAdditionalPanamaIdleHours = additionalPanamaIdleHours;
		// get distances for this pairing (assumes that getAllDistanceValues() returns a copy of the data)
		VesselState vesselState;
		IRouteCostProvider.CostType costType;
		int calculationCV;
		if (isLaden) {
			vesselState = VesselState.Laden;
			costType = CostType.Laden;
			calculationCV = portCVProvider.getPortCV(from);
		} else {
			vesselState = VesselState.Ballast;
			costType = CostType.Ballast;
			calculationCV = DEFAULT_CARGO_CV;
		}

		List<@NonNull DistanceMatrixEntry> allDistanceValues = distanceProvider.getDistanceValues(from, to, vessel, availableRouteChoice);
		// sort by cost then distance
		Collections.sort(allDistanceValues, new Comparator<DistanceMatrixEntry>() {
			@Override
			public int compare(final DistanceMatrixEntry o1, final DistanceMatrixEntry o2) {
				if (routeCostProvider.getRouteCost(o1.getRoute(), vessel, voyageStartTime, costType) == routeCostProvider.getRouteCost(o2.getRoute(), vessel, voyageStartTime, costType)) {
						return Integer.compare(
							Calculator.getTimeFromSpeedDistance(vessel.getMaxSpeed(), o1.getDistance())
									+ getProcessedRouteTransitTime(o1.getRoute(), vessel, isConstrainedPanamaVoyage, finalAdditionalPanamaIdleHours),
							Calculator.getTimeFromSpeedDistance(vessel.getMaxSpeed(), o2.getDistance())
									+ getProcessedRouteTransitTime(o2.getRoute(), vessel, isConstrainedPanamaVoyage, finalAdditionalPanamaIdleHours));
				} else {
					return Long.compare(routeCostProvider.getRouteCost(o1.getRoute(), vessel, voyageStartTime, costType),
							routeCostProvider.getRouteCost(o2.getRoute(), vessel, voyageStartTime, costType));
				}
			}
		});

		// remove dominated distances
		for (int i = allDistanceValues.size() - 1; i > 0; i--) {
			if ((routeCostProvider.getRouteCost(allDistanceValues.get(i).getRoute(), vessel, voyageStartTime, costType) >= routeCostProvider.getRouteCost(allDistanceValues.get(i - 1).getRoute(),
					vessel, voyageStartTime, costType)) && allDistanceValues.get(i).getDistance() > allDistanceValues.get(i - 1).getDistance()) {
				allDistanceValues.remove(i);
			}
		}

		// create a new distances data structure
		@NonNull
		final LadenRouteData @NonNull [] times = new @NonNull LadenRouteData[allDistanceValues.size()];
		int i = 0;
		for (final DistanceMatrixEntry d : allDistanceValues) {
			vessel.getTravelBaseFuel().getEquivalenceFactor();
			final int mintravelTime = Calculator.getTimeFromSpeedDistance(vessel.getMaxSpeed(), d.getDistance());
			final int nboSpeed = Math.min(Math.max(getNBOSpeed(vessel, vesselState, calculationCV), vessel.getMinSpeed()), vessel.getMaxSpeed());
			final int nbotravelTime = Calculator.getTimeFromSpeedDistance(nboSpeed, d.getDistance());
			final int transitTime = getProcessedRouteTransitTime(d.getRoute(), vessel, isConstrainedPanamaVoyage, finalAdditionalPanamaIdleHours);
			times[i] = new LadenRouteData(mintravelTime + transitTime, nbotravelTime + transitTime,
					OptimiserUnitConvertor.convertToInternalDailyCost(routeCostProvider.getRouteCost(d.getRoute(), vessel, voyageStartTime, costType)), d.getDistance(), transitTime);
			i++;
		}
		return times;
	}

	private int getProcessedRouteTransitTime(@NonNull ERouteOption route, @NonNull IVessel vessel, boolean isConstrainedPanamaVoyage, int additionalIdleTimeInHours) {
		if (isConstrainedPanamaVoyage && route == ERouteOption.PANAMA) {
			return routeCostProvider.getRouteTransitTime(route, vessel) + additionalIdleTimeInHours;
		} else {
			return routeCostProvider.getRouteTransitTime(route, vessel);
		}
	}
	
	private int getNBOSpeed(@NonNull final IVessel vessel, @NonNull final VesselState vesselState) {
		return getNBOSpeed(vessel, vesselState, DEFAULT_CARGO_CV);
	}

	private int getNBOSpeed(@NonNull final IVessel vessel, @NonNull final VesselState vesselState, final int cv) {
		final long nboRateInM3PerHour = vessel.getNBORate(vesselState);
		final long nboProvidedInMT = Calculator.convertM3ToMT(nboRateInM3PerHour, cv, vessel.getTravelBaseFuel().getEquivalenceFactor());
		return vessel.getConsumptionRate(vesselState).getSpeed(nboProvidedInMT);
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
			final boolean isLaden, AvailableRouteChoices availableRouteChoice, boolean isConstrainedPanamaVoyage, int additionalPanamaIdleHours) {
		int minSpeed;
		if (isLaden) {
			minSpeed = getNBOSpeed(vessel, VesselState.Laden, cv);
		} else {
			minSpeed = getNBOSpeed(vessel, VesselState.Ballast, cv);
		}
		// round min and max speeds to allow better speed stepping
		minSpeed = roundUpToNearest(minSpeed, 100);
		int maxSpeed = vessel.getMaxSpeed();

		// min speed needs to be bounded!
		minSpeed = Math.min(minSpeed, maxSpeed);

		// loop through speeds and canals
		int speed = minSpeed;
		@NonNull
		LadenRouteData @NonNull [] ladenRouteTimes = getMinimumTravelTimes(load, discharge, vessel, startTime, false, availableRouteChoice, isConstrainedPanamaVoyage, additionalPanamaIdleHours);

		List<Integer> times = new ArrayList<>();

		while (speed <= maxSpeed) {

			for (LadenRouteData ladenRouteData : ladenRouteTimes) {
				int time = startTime + Calculator.getTimeFromSpeedDistance(speed, ladenRouteData.ladenRouteDistance) + ladenRouteData.transitTime;
				times.add(time);
			}
			if (speed == maxSpeed) {
				break;
			}
			speed += half_a_knot;
			if (speed > maxSpeed) {
				speed = maxSpeed;
			}
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
