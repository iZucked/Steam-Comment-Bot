/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.inject.Inject;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;

@NonNullByDefault
public class PanamaBookingHelper {

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private IPanamaBookingsProvider panamaBookingsProvider;
 
	public int getTravelTimeToCanal(IVessel vessel, IPort sourcePort, boolean includeMargin) {
		IPort routeOptionEntryPort = distanceProvider.getRouteOptionEntryPort(sourcePort, ERouteOption.PANAMA);
		if (routeOptionEntryPort == null) {
			return Integer.MAX_VALUE;
		}

		int timeToCanal = distanceProvider.getTravelTime(ERouteOption.DIRECT, vessel, sourcePort, routeOptionEntryPort, Math.min(panamaBookingsProvider.getSpeedToCanal(), vessel.getMaxSpeed()));

		if (includeMargin) {
			timeToCanal += panamaBookingsProvider.getMarginInHours();
		}

		return timeToCanal;
	}

	public int getTravelTimeFromCanalEntry(IVessel vessel, ECanalEntry canalEntrance, IPort destinationPort) {
		IPort routeOptionEntryPort = distanceProvider.getRouteOptionEntryPort(ERouteOption.PANAMA, canalEntrance);
		if (routeOptionEntryPort == null) {
			return Integer.MAX_VALUE;
		}

		int vesselMaxSpeed = vessel.getMaxSpeed();
		final int fromEntryPoint = distanceProvider.getTravelTimeViaCanal(ERouteOption.PANAMA, vessel, routeOptionEntryPort, destinationPort, vesselMaxSpeed);

		return fromEntryPoint;
	}
}
