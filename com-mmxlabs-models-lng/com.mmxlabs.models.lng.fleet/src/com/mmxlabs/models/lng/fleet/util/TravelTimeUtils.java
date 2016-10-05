/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.util;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.util.RouteDistanceLineCache;

public class TravelTimeUtils {

	public static int getTimeForRoute(final @Nullable VesselClass vesselClass, final double referenceSpeed, final @NonNull Route route, final @NonNull Port fromPort, final @NonNull Port toPort) {
		final int distance = getDistance(route, fromPort, toPort);

		int extraTime = 0;
		if (vesselClass != null) {
			for (final VesselClassRouteParameters vcrp : vesselClass.getRouteParameters()) {
				if (vcrp.getRoute().equals(route)) {
					extraTime = vcrp.getExtraTransitTime();
				}
			}
		}
		if (distance == Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		}

		final double travelTime = distance / referenceSpeed;
		final int totalTime = (int) (Math.floor(travelTime) + extraTime);
		return totalTime;
	}

	public static int getDistance(@NonNull final Route route, @NonNull final Port from, @NonNull final Port to) {
		final RouteDistanceLineCache cache = (RouteDistanceLineCache) Platform.getAdapterManager().loadAdapter(route, RouteDistanceLineCache.class.getName());
		if (cache != null) {
			return cache.getDistance(from, to);
		}
		return Integer.MAX_VALUE;
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
}
