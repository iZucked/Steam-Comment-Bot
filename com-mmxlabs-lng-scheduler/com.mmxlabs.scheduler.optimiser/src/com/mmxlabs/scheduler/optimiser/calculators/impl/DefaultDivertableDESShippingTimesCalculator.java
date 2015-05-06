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
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.MatrixEntry;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.calculators.IDivertableDESShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProvider;

public class DefaultDivertableDESShippingTimesCalculator implements IDivertableDESShippingTimesCalculator {

	@Inject
	private IElementDurationProvider durationProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IShippingHoursRestrictionProvider shippingHoursRestrictionProvider;

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Inject
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	private Pair<Integer, String> getDischargeTimeAndRoute(final ILoadOption buyOption, final IDischargeOption sellOption, final IVessel nominatedVessel, final IResource resource) {
		final ISequenceElement buyElement = portSlotProvider.getElement(buyOption);
		final int fobLoadTime = shippingHoursRestrictionProvider.getBaseTime(buyElement).getStart();
		final int loadDuration = durationProvider.getElementDuration(buyElement, resource);

		final Triple<Integer, String, Integer> distanceData = getShortestDistanceToPort(buyOption.getPort(), sellOption.getPort(), nominatedVessel.getVesselClass(), shippingHoursRestrictionProvider.getReferenceSpeed(nominatedVessel, VesselState.Laden));
		final int notionalLadenTime = distanceData.getThird();
		final String route = distanceData.getSecond();

		int notionalDischargeTime = fobLoadTime + loadDuration + notionalLadenTime;
		final ITimeWindow sellWindow = sellOption.getTimeWindow();

		return new Pair<>(Math.max(notionalDischargeTime, sellWindow.getStart()), route);
	}

	@Override
	public Pair<Integer, Integer> getDivertableDESTimes(ILoadOption buyOption, IDischargeOption sellOption, IVessel nominatedVessel, IResource resource) {
		Pair<Integer, String> dischargeJourney = getDischargeTimeAndRoute(buyOption, sellOption, nominatedVessel, resource);
		String route = dischargeJourney.getSecond();
		final int ballastDistance = distanceProvider.get(dischargeJourney.getSecond()).get(sellOption.getPort(), buyOption.getPort()) + routeCostProvider.getRouteTransitTime(route, nominatedVessel.getVesselClass());
		// Get notional speed
		final int referenceSpeed = shippingHoursRestrictionProvider.getReferenceSpeed(nominatedVessel, VesselState.Ballast);

		final ISequenceElement sellElement = portSlotProvider.getElement(sellOption);
		final int dischargeDuration = durationProvider.getElementDuration(sellElement, resource);

		final int minBallastReturnTime = dischargeJourney.getFirst() + Calculator.getTimeFromSpeedDistance(referenceSpeed, ballastDistance) + dischargeDuration;

		return new Pair<>(dischargeJourney.getFirst(), minBallastReturnTime);
	}

	private Triple<Integer, String, Integer> getShortestDistanceToPort(final IPort to, final IPort from, final IVesselClass vesselClass, final int referenceSpeed) {
		int distance = Integer.MAX_VALUE;
		int shortestTime = Integer.MAX_VALUE;
		String route = "";
		Collection<String> allowedRoutes = shippingHoursRestrictionProvider.getDivertableDESAllowedRoutes(vesselClass);
		final List<MatrixEntry<IPort, Integer>> distances = new ArrayList<MatrixEntry<IPort, Integer>>(distanceProvider.getValues(to, from));
		for (final MatrixEntry<IPort, Integer> d : distances) {
			if (allowedRoutes.isEmpty() || allowedRoutes.contains(d.getKey())) {
				final int travelTime = Calculator.getTimeFromSpeedDistance(referenceSpeed, d.getValue()) + routeCostProvider.getRouteTransitTime(d.getKey(), vesselClass);
				if (travelTime < shortestTime) {
					distance = d.getValue();
					route = d.getKey();
					shortestTime = travelTime;
				}
			}
		}
		return new Triple<>(distance, route, shortestTime);
	}

}
