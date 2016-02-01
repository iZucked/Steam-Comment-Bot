/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;

public class RouteDistanceLineCache extends MMXAdapterImpl {

	private Map<Pair<Port, Port>, Integer> distanceCache = null;

	private final Route route;

	public RouteDistanceLineCache(final Route route) {
		this.route = route;
	}

	@Override
	public void reallyNotifyChanged(final Notification notification) {
		if (notification.isTouch()) {
			return;
		}
		if (notification.getEventType() == Notification.REMOVING_ADAPTER) {
			return;
		}
		clearCache();
	}

	@Override
	protected void missedNotifications(final List<Notification> missed) {
		clearCache();

	}

	public synchronized void buildCache() {
		if (distanceCache == null) {
			distanceCache = new HashMap<>();
			for (final RouteLine rl : route.getLines()) {
				if (rl.getDistance() != Integer.MAX_VALUE && rl.getDistance() >= 0) {
					distanceCache.put(new Pair<>(rl.getFrom(), rl.getTo()), rl.getFullDistance());
				}
			}
		}
	}

	public synchronized void clearCache() {
		distanceCache = null;
	}

	public int getDistance(final Port from, final Port to) {
		if (from == null || to == null) {
			return Integer.MAX_VALUE;
		}
		if (from.equals(to)) {
			return 0;
		}

		if (distanceCache == null) {
			buildCache();
		}

		final Pair<Port, Port> key = new Pair<>(from, to);
		if (distanceCache.containsKey(key)) {
			final Integer dist = distanceCache.get(key);
			if (dist != null) {
				return dist.intValue();
			}

		}
		return Integer.MAX_VALUE;
	}

	public boolean hasDistance(final Port from, final Port to) {
		return getDistance(from, to) != Integer.MAX_VALUE;
	}
}
