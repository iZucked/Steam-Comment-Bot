
package com.mmxlabs.scheduler.optimiser.schedule;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.inject.Inject;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PanamaPeriod;

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

		int timeToCanal = distanceProvider.getTravelTime(ERouteOption.DIRECT, vessel, sourcePort, routeOptionEntryPort,
				Math.min(panamaBookingsProvider.getSpeedToCanal(), vessel.getVesselClass().getMaxSpeed()));

		if (includeMargin) {
			timeToCanal += panamaBookingsProvider.getMarginInHours();
		}

		return timeToCanal;
	}

	public int getTravelTimeFromCanalEntry(IVessel vessel, IPort sourcePort, IPort destinationPort) {
		IPort routeOptionEntryPort = distanceProvider.getRouteOptionEntryPort(sourcePort, ERouteOption.PANAMA);
		if (routeOptionEntryPort == null) {
			return Integer.MAX_VALUE;
		}
		int vesselMaxSpeed = vessel.getVesselClass().getMaxSpeed();
		final int fromEntryPoint = distanceProvider.getTravelTime(ERouteOption.PANAMA, vessel, routeOptionEntryPort, destinationPort, vesselMaxSpeed);

		return fromEntryPoint;
	}

	public PanamaPeriod getPanamaPeriod(int estimatedCanalArrival) {
		if (estimatedCanalArrival > panamaBookingsProvider.getRelaxedBoundary()) {
			return PanamaPeriod.Beyond;
		} else if (estimatedCanalArrival > panamaBookingsProvider.getStrictBoundary()) {
			return PanamaPeriod.Relaxed;
		} else {
			return PanamaPeriod.Strict;
		}
	}
}
