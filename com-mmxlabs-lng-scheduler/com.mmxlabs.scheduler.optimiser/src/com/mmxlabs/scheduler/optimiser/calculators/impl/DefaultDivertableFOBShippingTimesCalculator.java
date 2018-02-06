/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.calculators.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.calculators.IDivertableDESShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.calculators.IDivertableFOBShippingTimesCalculator;
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

public class DefaultDivertableFOBShippingTimesCalculator implements IDivertableFOBShippingTimesCalculator {

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
	public Pair<@NonNull Integer, @NonNull Integer> getDivertableFOBTimes(final @NonNull ILoadOption buyOption, final @NonNull IDischargeOption sellOption, final @NonNull IVessel nominatedVessel,
			final @NonNull IResource resource) {

		final Triple<Integer, ERouteOption, Integer> ladenDistanceData = getShortestTravelTimeToPort(sellOption, buyOption.getPort(), sellOption.getPort(), nominatedVessel,
				getReferenceSpeed(nominatedVessel, VesselState.Laden));
		if (ladenDistanceData == null) {
			throw new IllegalStateException(String.format("No distance between %s and %s", buyOption.getPort().getName(), sellOption.getPort().getName()));
		}
		final int notionalLadenTime = ladenDistanceData.getThird();

		final Triple<Integer, ERouteOption, Integer> ballastDistanceData = getShortestTravelTimeToPort(sellOption, buyOption.getPort(), sellOption.getPort(), nominatedVessel,
				getReferenceSpeed(nominatedVessel, VesselState.Ballast));
		if (ballastDistanceData == null) {
			throw new IllegalStateException(String.format("No distance between %s and %s", sellOption.getPort().getName(), buyOption.getPort().getName()));
		}
		final int notionalBallastTime = ballastDistanceData.getThird();

		final ISequenceElement buyElement = portSlotProvider.getElement(buyOption);
		final int loadDuration = durationProvider.getElementDuration(buyElement, resource);

		final ISequenceElement sellElement = portSlotProvider.getElement(sellOption);
		final int dischargeDuration = durationProvider.getElementDuration(sellElement, resource);

		ITimeWindow baseTime = shippingHoursRestrictionProvider.getBaseTime(sellElement);
		int earlyFOBLoadTime = buyOption.getTimeWindow().getInclusiveStart() + loadDuration + notionalLadenTime;
		if (earlyFOBLoadTime >= baseTime.getInclusiveStart()) {
			int returnTime = earlyFOBLoadTime + dischargeDuration + notionalBallastTime;
			return new Pair<>(buyOption.getTimeWindow().getInclusiveStart(), returnTime);
		} else {
			int fobTime = Math.min(baseTime.getInclusiveStart() - notionalLadenTime - loadDuration, buyOption.getTimeWindow().getExclusiveEnd() - 1);
			int returnTime = fobTime + loadDuration + notionalLadenTime + dischargeDuration + notionalBallastTime;
			return new Pair<>(fobTime, returnTime);
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
		final Collection<ERouteOption> allowedRoutes = shippingHoursRestrictionProvider.getDivertableFOBAllowedRoutes(fobSale);
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

	protected int getReferenceSpeed(final @NonNull IVessel nominatedVessel, final @NonNull VesselState vesselState) {
		return shippingHoursRestrictionProvider.getReferenceSpeed(nominatedVessel, vesselState);
	}

}
