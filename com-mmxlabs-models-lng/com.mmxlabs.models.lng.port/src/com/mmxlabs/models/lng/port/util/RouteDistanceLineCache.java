/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.util;

import java.lang.ref.SoftReference;
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

	private SoftReference<Map<Pair<Port, Port>, Integer>> distanceCache = new SoftReference<>(null);

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

	public synchronized Map<Pair<Port, Port>, Integer> buildCache() {
		final Map<Pair<Port, Port>, Integer> distanceCacheObj = new HashMap<>();
		for (final RouteLine rl : route.getLines()) {
			if (rl.getDistance() != Integer.MAX_VALUE && rl.getDistance() >= 0) {
				distanceCacheObj.put(new Pair<>(rl.getFrom(), rl.getTo()), rl.getFullDistance());
			}
		}
		distanceCache = new SoftReference<>(distanceCacheObj);
		return distanceCacheObj;
	}

	public synchronized void clearCache() {
		distanceCache.clear();
	}

	public int getDistance(final Port from, final Port to) {
		if (from == null || to == null) {
			return Integer.MAX_VALUE;
		}
		if (from.equals(to)) {
			return 0;
		}

		Map<Pair<Port, Port>, Integer> map = distanceCache.get();
		if (map == null) {
			map = buildCache();
		}

		final Pair<Port, Port> key = new Pair<>(from, to);
		if (map.containsKey(key)) {
			final Integer dist = map.get(key);
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
