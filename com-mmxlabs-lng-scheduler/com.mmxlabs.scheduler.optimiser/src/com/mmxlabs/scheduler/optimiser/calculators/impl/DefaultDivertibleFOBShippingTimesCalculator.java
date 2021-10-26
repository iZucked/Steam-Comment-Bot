/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.calculators.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.calculators.IDivertibleFOBShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProvider;
import com.mmxlabs.scheduler.optimiser.shared.port.DistanceMatrixEntry;

public class DefaultDivertibleFOBShippingTimesCalculator implements IDivertibleFOBShippingTimesCalculator {

	@Inject
	private IElementDurationProvider durationProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	protected IShippingHoursRestrictionProvider shippingHoursRestrictionProvider;

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Inject
	private IDistanceProvider distanceProvider;

	@Override
	public Triple<@NonNull Integer, @NonNull Integer, @NonNull Integer> getDivertibleFOBTimes(final @NonNull ILoadOption buyOption, final @NonNull IDischargeOption sellOption,
			final @NonNull IVessel nominatedVessel, final @NonNull IResource resource) {

		final Triple<Integer, ERouteOption, Integer> ladenDistanceData = getShortestTravelTimeToPort(sellOption, buyOption.getPort(), sellOption.getPort(), nominatedVessel,
				getReferenceSpeed(sellOption, nominatedVessel, VesselState.Laden));
		if (ladenDistanceData == null) {
			throw new IllegalStateException(String.format("No distance between %s and %s", buyOption.getPort().getName(), sellOption.getPort().getName()));
		}
		final int notionalLadenTime = ladenDistanceData.getThird();

		final Triple<Integer, ERouteOption, Integer> ballastDistanceData = getShortestTravelTimeToPort(sellOption, buyOption.getPort(), sellOption.getPort(), nominatedVessel,
				getReferenceSpeed(sellOption, nominatedVessel, VesselState.Ballast));
		if (ballastDistanceData == null) {
			throw new IllegalStateException(String.format("No distance between %s and %s", sellOption.getPort().getName(), buyOption.getPort().getName()));
		}
		final int notionalBallastTime = ballastDistanceData.getThird();

		final ISequenceElement buyElement = portSlotProvider.getElement(buyOption);
		final int loadDuration = durationProvider.getElementDuration(buyElement, resource);

		final ISequenceElement sellElement = portSlotProvider.getElement(sellOption);
		final int dischargeDuration = durationProvider.getElementDuration(sellElement, resource);

		ITimeWindow baseTime = shippingHoursRestrictionProvider.getBaseTime(sellElement);
		int earlyFOBDischargeTime = buyOption.getTimeWindow().getInclusiveStart() + loadDuration + notionalLadenTime;
		if (earlyFOBDischargeTime >= baseTime.getInclusiveStart()) {
			// Arrive after window start (or may even by late)
			int startOfDischarge = earlyFOBDischargeTime;
			int returnTime = startOfDischarge + dischargeDuration + notionalBallastTime;
			return new Triple<>(buyOption.getTimeWindow().getInclusiveStart(), startOfDischarge, returnTime);
		} else {
			// Arrive before window, so depart as late as possible and arrive at discharge window start
			int fobTime = Math.min(baseTime.getInclusiveStart() - notionalLadenTime - loadDuration, buyOption.getTimeWindow().getExclusiveEnd() - 1);
			int dischargeTime = baseTime.getInclusiveStart();
			int returnTime = dischargeTime + dischargeDuration + notionalBallastTime;
			return new Triple<>(fobTime, dischargeTime, returnTime);
		}
	}

	/**
	 * Returns a {@link Triple} of distance, route, travel time in hours
	 * 
	 * @param to
	 * @param from
	 * @param vessel
	 * @param referenceSpeed
	 * @return
	 */
	@Nullable
	private Triple<Integer, ERouteOption, Integer> getShortestTravelTimeToPort(@NonNull final IDischargeOption fobSale, final @NonNull IPort to, final @NonNull IPort from, final IVessel vessel,
			final int referenceSpeed) {
		int distance = Integer.MAX_VALUE;
		int shortestTime = Integer.MAX_VALUE;
		ERouteOption route = ERouteOption.DIRECT;
		final Collection<ERouteOption> allowedRoutes = shippingHoursRestrictionProvider.getDivertibleFOBAllowedRoutes(fobSale);
		final List<DistanceMatrixEntry> distances = distanceProvider.getDistanceValues(to, from, vessel);
		for (final DistanceMatrixEntry d : distances) {
			final ERouteOption routeOption = d.getRoute();
			if (allowedRoutes == null || allowedRoutes.isEmpty() || allowedRoutes.contains(routeOption)) {
				final int thisDistance = d.getDistance();
				if (thisDistance == Integer.MAX_VALUE) {
					continue;
				}
				final int travelTime = Calculator.getTimeFromSpeedDistance(referenceSpeed, thisDistance) + routeCostProvider.getRouteTransitTime(routeOption, vessel);
				if (travelTime < shortestTime) {
					distance = thisDistance;
					route = routeOption;
					shortestTime = travelTime;
				}
			}
		}
		if (distance == Integer.MAX_VALUE) {
			return null;
		}
		return new Triple<>(distance, route, shortestTime);
	}

	protected int getReferenceSpeed(final @NonNull IPortSlot slot, final @NonNull IVessel nominatedVessel, final @NonNull VesselState vesselState) {
		return shippingHoursRestrictionProvider.getReferenceSpeed(slot, nominatedVessel, vesselState);
	}

}
