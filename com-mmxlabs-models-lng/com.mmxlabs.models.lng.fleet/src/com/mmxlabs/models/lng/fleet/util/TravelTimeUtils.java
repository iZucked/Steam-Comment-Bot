/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.util;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;

public final class TravelTimeUtils {

	private TravelTimeUtils() {
	}

	public static int getMinTimeFromAllowedRoutes(final Port fromPort, final Port toPort, final Vessel vessel, final double referenceSpeed, final Collection<Route> allowedRoutes,
			ModelDistanceProvider modelDistanceProvider) {
		int minDuration = Integer.MAX_VALUE;
		if (fromPort != null && toPort != null) {
			for (final Route route : allowedRoutes) {
				assert route != null;
				final int totalTime = TravelTimeUtils.getTimeForRoute(vessel, referenceSpeed, route, fromPort, toPort, modelDistanceProvider);
				if (totalTime < minDuration) {
					minDuration = totalTime;
				}
			}
		}
		return minDuration;
	}

	public static int getTimeForRoute(final @Nullable Vessel vessel, final double referenceSpeed, final @NonNull RouteOption routeOption, final @NonNull Port fromPort, final @NonNull Port toPort,
			@NonNull final PortModel portModel, ModelDistanceProvider modelDistanceProvider) {
		for (final Route route : portModel.getRoutes()) {
			if (route.getRouteOption() == routeOption) {
				final int distance = getDistance(route, fromPort, toPort, modelDistanceProvider);
				final int extraIdleTime = getContingencyIdleTimeInHours(fromPort, toPort, modelDistanceProvider);

				int extraTime = 0;
				if (vessel != null) {
					for (final VesselClassRouteParameters vcrp : vessel.getVesselOrDelegateRouteParameters()) {
						if (vcrp.getRouteOption() == routeOption) {
							extraTime = vcrp.getExtraTransitTime();
						}
					}
				}
				if (distance == Integer.MAX_VALUE) {
					return Integer.MAX_VALUE;
				}

				final double travelTime = distance / referenceSpeed;
				final int totalTime = (int) (Math.floor(travelTime) + extraTime + extraIdleTime);
				return totalTime;
			}
		}
		return Integer.MAX_VALUE;
	}

	public static int getTimeForRoute(final @Nullable Vessel vessel, final double referenceSpeed, final @NonNull Route route, final @NonNull Port fromPort, final @NonNull Port toPort,
			ModelDistanceProvider modelDistanceProvider) {

		final int distance = getDistance(route, fromPort, toPort, modelDistanceProvider);
		if (distance == Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		}
		final int extraIdleTime = getContingencyIdleTimeInHours(fromPort, toPort, modelDistanceProvider);

		int extraTime = 0;
		if (vessel != null) {
			for (final VesselClassRouteParameters vcrp : vessel.getVesselOrDelegateRouteParameters()) {
				if (vcrp.getRouteOption() == route.getRouteOption()) {
					extraTime = vcrp.getExtraTransitTime();
				}
			}
		}

		final double travelTime = distance / referenceSpeed;
		final int totalTime = (int) (Math.floor(travelTime) + extraTime) + extraIdleTime;

		return totalTime;
	}

	public static int getDistance(@NonNull final Route route, @NonNull final Port from, @NonNull final Port to, @NonNull ModelDistanceProvider modelDistanceProvider) {

		return modelDistanceProvider.getDistance(from, to, route.getRouteOption());
	}

	public static int getContingencyIdleTimeInHours(@NonNull final Port from, @NonNull final Port to, @NonNull ModelDistanceProvider modelDistanceProvider) {

		return modelDistanceProvider.getPortToPortContingencyIdleTimeInHours(from, to);
	}

	public static String formatHours(final long hours) {
		if (hours < 24) {
			if (hours == 1) {
				return hours + " hour";
			} else {
				return hours + " hours";
			}
		} else {
			final long remainderHours = hours % 24L;
			final long days = hours / 24L;
			return days + " day" + (days > 1 ? "s" : "") + (remainderHours > 0 ? (", " + remainderHours + " hour" + (remainderHours > 1 ? "s" : "")) : "");
		}
	}

	public static String formatShortHours(final long hours) {
		if (hours < 24) {
			if (hours == 1) {
				return hours + " hour";
			} else {
				return hours + " hours";
			}
		} else {
			final long remainderHours = hours % 24L;
			final long days = hours / 24L;
			return days + "d" + (remainderHours > 0 ? (" " + remainderHours + "h") : "");
		}
	}
}
