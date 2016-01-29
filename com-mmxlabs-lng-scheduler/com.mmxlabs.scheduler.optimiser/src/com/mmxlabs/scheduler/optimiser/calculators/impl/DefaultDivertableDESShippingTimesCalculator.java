/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
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
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;

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

	private Pair<Integer, String> getDischargeTimeAndRoute(final ILoadOption buyOption, final IDischargeOption sellOption, final IVessel nominatedVessel, final IResource resource) {
		final ISequenceElement buyElement = portSlotProvider.getElement(buyOption);
		final int fobLoadTime = shippingHoursRestrictionProvider.getBaseTime(buyElement).getStart();
		final int loadDuration = durationProvider.getElementDuration(buyElement, resource);

		final Triple<Integer, String, Integer> distanceData = getShortestDistanceToPort(buyOption.getPort(), sellOption.getPort(), nominatedVessel,
				getReferenceSpeed(buyOption, nominatedVessel, VesselState.Laden), fobLoadTime + loadDuration);
		final int notionalLadenTime = distanceData.getThird();
		final String route = distanceData.getSecond();

		final int notionalDischargeTime = fobLoadTime + loadDuration + notionalLadenTime;
		final ITimeWindow sellWindow = sellOption.getTimeWindow();

		return new Pair<>(Math.max(notionalDischargeTime, sellWindow.getStart()), route);
	}

	@Override
	public Pair<Integer, Integer> getDivertableDESTimes(final ILoadOption buyOption, final IDischargeOption sellOption, final IVessel nominatedVessel, final IResource resource) {
		final Pair<Integer, String> dischargeJourney = getDischargeTimeAndRoute(buyOption, sellOption, nominatedVessel, resource);
		final String ballastRoute = dischargeJourney.getSecond();
		final ISequenceElement sellElement = portSlotProvider.getElement(sellOption);
		final int dischargeDuration = durationProvider.getElementDuration(sellElement, resource);

		final int startOfDischarge = dischargeJourney.getFirst();
		final int completionOfDischarge = startOfDischarge + dischargeDuration;

		final int ballastDistance = distanceProvider.getDistance(ballastRoute, sellOption.getPort(), buyOption.getPort(), completionOfDischarge);
		// Get notional speed
		final int referenceSpeed = getReferenceSpeed(buyOption, nominatedVessel, VesselState.Ballast);

		final int minBallastReturnTime = dischargeJourney.getFirst() + dischargeDuration + Calculator.getTimeFromSpeedDistance(referenceSpeed, ballastDistance)
				+ routeCostProvider.getRouteTransitTime(ballastRoute, nominatedVessel);

		return new Pair<>(dischargeJourney.getFirst(), minBallastReturnTime);
	}

	private Triple<Integer, String, Integer> getShortestDistanceToPort(final IPort to, final IPort from, final IVessel vessel, final int referenceSpeed, final int voyageStartTime) {
		int distance = Integer.MAX_VALUE;
		int shortestTime = Integer.MAX_VALUE;
		String route = "";
		final Collection<String> allowedRoutes = shippingHoursRestrictionProvider.getDivertableDESAllowedRoutes(vessel.getVesselClass());
		final List<MatrixEntry<IPort, Integer>> distances = new ArrayList<MatrixEntry<IPort, Integer>>(distanceProvider.getDistanceValues(to, from, voyageStartTime));
		for (final MatrixEntry<IPort, Integer> d : distances) {
			if (allowedRoutes == null || allowedRoutes.isEmpty() || allowedRoutes.contains(d.getKey())) {
				final int travelTime = Calculator.getTimeFromSpeedDistance(referenceSpeed, d.getValue()) + routeCostProvider.getRouteTransitTime(d.getKey(), vessel);
				if (travelTime < shortestTime) {
					distance = d.getValue();
					route = d.getKey();
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
