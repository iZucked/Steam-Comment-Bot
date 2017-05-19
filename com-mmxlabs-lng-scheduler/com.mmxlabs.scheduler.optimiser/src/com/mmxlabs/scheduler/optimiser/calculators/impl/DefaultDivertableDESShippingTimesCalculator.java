/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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

public class DefaultDivertableDESShippingTimesCalculator implements IDivertableDESShippingTimesCalculator {

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

	private Pair<@NonNull Integer, @NonNull ERouteOption> getDischargeTimeAndRoute(final @NonNull ILoadOption buyOption, final @NonNull IDischargeOption sellOption,
			final @NonNull IVessel nominatedVessel, final @NonNull IResource resource) {
		final ISequenceElement buyElement = portSlotProvider.getElement(buyOption);
		final int fobLoadTime = shippingHoursRestrictionProvider.getBaseTime(buyElement).getInclusiveStart();
		final int loadDuration = durationProvider.getElementDuration(buyElement, resource);

		final Triple<Integer, ERouteOption, Integer> distanceData = getShortestTravelTimeToPort(buyOption, buyOption.getPort(), sellOption.getPort(), nominatedVessel,
				getReferenceSpeed(buyOption, nominatedVessel, VesselState.Laden), fobLoadTime + loadDuration);
		if (distanceData == null) {
			throw new IllegalStateException(String.format("No distance between %s and %s", buyOption.getPort().getName(), sellOption.getPort().getName()));
		}
		final int notionalLadenTime = distanceData.getThird();
		final ERouteOption route = distanceData.getSecond();

		final int notionalDischargeTime = fobLoadTime + loadDuration + notionalLadenTime;
		final ITimeWindow sellWindow = sellOption.getTimeWindow();

		return new Pair<>(Math.max(notionalDischargeTime, sellWindow.getInclusiveStart()), route);
	}

	@Override
	public Pair<@NonNull Integer, @NonNull Integer> getDivertableDESTimes(final @NonNull ILoadOption buyOption, final @NonNull IDischargeOption sellOption, final @NonNull IVessel nominatedVessel,
			final @NonNull IResource resource) {
		final Pair<Integer, ERouteOption> dischargeJourney = getDischargeTimeAndRoute(buyOption, sellOption, nominatedVessel, resource);
		final ERouteOption ballastRoute = dischargeJourney.getSecond();

		final ISequenceElement sellElement = portSlotProvider.getElement(sellOption);
		final int dischargeDuration = durationProvider.getElementDuration(sellElement, resource);

		final int startOfDischarge = dischargeJourney.getFirst();
		final int completionOfDischarge = startOfDischarge + dischargeDuration;

		final int ballastDistance = distanceProvider.getDistance(ballastRoute, sellOption.getPort(), buyOption.getPort(), completionOfDischarge, nominatedVessel);
		// Get notional speed
		final int referenceSpeed = getReferenceSpeed(buyOption, nominatedVessel, VesselState.Ballast);

		final int minBallastReturnTime = dischargeJourney.getFirst() + dischargeDuration + Calculator.getTimeFromSpeedDistance(referenceSpeed, ballastDistance)
				+ routeCostProvider.getRouteTransitTime(ballastRoute, nominatedVessel);

		return new Pair<>(dischargeJourney.getFirst(), minBallastReturnTime);
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
	private Triple<Integer, ERouteOption, Integer> getShortestTravelTimeToPort(@NonNull final ILoadOption loadOption, final @NonNull IPort to, final @NonNull IPort from, final IVessel vessel,
			final int referenceSpeed, final int voyageStartTime) {
		int distance = Integer.MAX_VALUE;
		int shortestTime = Integer.MAX_VALUE;
		ERouteOption route = ERouteOption.DIRECT;
		final Collection<ERouteOption> allowedRoutes = shippingHoursRestrictionProvider.getDivertableDESAllowedRoutes(loadOption);
		final List<DistanceMatrixEntry> distances = distanceProvider.getDistanceValues(to, from, voyageStartTime, vessel);
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
		return shippingHoursRestrictionProvider.getReferenceSpeed(nominatedVessel, vesselState);
	}

}
