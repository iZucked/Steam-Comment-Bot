/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.calculators.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.common.MatrixEntry;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.calculators.IDivertableDESShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProvider;

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

	private Pair<Integer, ERouteOption> getDischargeTimeAndRoute(final ILoadOption buyOption, final IDischargeOption sellOption, final IVessel nominatedVessel, final IResource resource) {
		final ISequenceElement buyElement = portSlotProvider.getElement(buyOption);
		final int fobLoadTime = shippingHoursRestrictionProvider.getBaseTime(buyElement).getStart();
		final int loadDuration = durationProvider.getElementDuration(buyElement, resource);

		final Triple<Integer, ERouteOption, Integer> distanceData = getShortestDistanceToPort(buyOption.getPort(), sellOption.getPort(), nominatedVessel.getVesselClass(),
				getReferenceSpeed(buyOption, nominatedVessel, VesselState.Laden));
		final int notionalLadenTime = distanceData.getThird();
		final ERouteOption route = distanceData.getSecond();

		final int notionalDischargeTime = fobLoadTime + loadDuration + notionalLadenTime;
		final ITimeWindow sellWindow = sellOption.getTimeWindow();

		return new Pair<>(Math.max(notionalDischargeTime, sellWindow.getStart()), route);
	}

	@Override
	public Pair<Integer, Integer> getDivertableDESTimes(final ILoadOption buyOption, final IDischargeOption sellOption, final IVessel nominatedVessel, final IResource resource) {
		final Pair<Integer, ERouteOption> dischargeJourney = getDischargeTimeAndRoute(buyOption, sellOption, nominatedVessel, resource);
		final ERouteOption route = dischargeJourney.getSecond();
		final int ballastDistance = distanceProvider.getDistance(dischargeJourney.getSecond(), sellOption.getPort(), buyOption.getPort())
				+ routeCostProvider.getRouteTransitTime(route, nominatedVessel.getVesselClass());
		// Get notional speed
		final int referenceSpeed = getReferenceSpeed(buyOption, nominatedVessel, VesselState.Ballast);

		final ISequenceElement sellElement = portSlotProvider.getElement(sellOption);
		final int dischargeDuration = durationProvider.getElementDuration(sellElement, resource);

		final int minBallastReturnTime = dischargeJourney.getFirst() + Calculator.getTimeFromSpeedDistance(referenceSpeed, ballastDistance) + dischargeDuration;

		return new Pair<>(dischargeJourney.getFirst(), minBallastReturnTime);
	}

	private Triple<Integer, ERouteOption, Integer> getShortestDistanceToPort(final IPort to, final IPort from, final IVesselClass vesselClass, final int referenceSpeed) {
		int distance = Integer.MAX_VALUE;
		int shortestTime = Integer.MAX_VALUE;
		ERouteOption route = ERouteOption.DIRECT;
		final Collection<ERouteOption> allowedRoutes = shippingHoursRestrictionProvider.getDivertableDESAllowedRoutes(vesselClass);
		final List<Pair<ERouteOption, Integer>> distances = distanceProvider.getDistanceValues(to, from);
		for (final Pair<ERouteOption, Integer> d : distances) {
			final ERouteOption routeOption = d.getFirst();
			if (allowedRoutes == null || allowedRoutes.isEmpty() || allowedRoutes.contains(routeOption)) {
				final int travelTime = Calculator.getTimeFromSpeedDistance(referenceSpeed, d.getSecond()) + routeCostProvider.getRouteTransitTime(routeOption, vesselClass);
				if (travelTime < shortestTime) {
					distance = d.getSecond();
					route = routeOption;
					shortestTime = travelTime;
				}
			}
		}
		return new Triple<>(distance, route, shortestTime);
	}

	protected int getReferenceSpeed(final IPortSlot slot, final IVessel nominatedVessel, final VesselState vesselState) {
		return shippingHoursRestrictionProvider.getReferenceSpeed(nominatedVessel, vesselState);
	}

}
