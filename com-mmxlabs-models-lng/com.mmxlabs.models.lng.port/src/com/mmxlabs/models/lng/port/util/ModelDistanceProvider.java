/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.util;

import java.lang.ref.SoftReference;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;

@NonNullByDefault
public class ModelDistanceProvider extends EContentAdapter {

	private final @Nullable PortModel portModel;
	// private final @Nullable IDistanceProvider distanceProvider;

	private SoftReference<@Nullable Map<RouteOption, Map<Pair<String, String>, Integer>>> distanceCache = new SoftReference<>(null);

	public synchronized Map<RouteOption, Map<Pair<String, String>, Integer>> buildCache() {

		final Map<String, Port> portMap = new HashMap<>();
		for (final Port p : portModel.getPorts()) {
			Location l = p.getLocation();
			portMap.put(l.getTempMMXID(), p);
		}

		final Map<RouteOption, Map<Pair<String, String>, Integer>> distanceCacheObj = new EnumMap<>(RouteOption.class);
		for (final Route route : portModel.getRoutes()) {
			final Map<Pair<String, String>, Integer> distanceMatrixCacheObj = new HashMap<>();

			for (final RouteLine rl : route.getLines()) {
				if (rl.getDistance() != Integer.MAX_VALUE && rl.getDistance() >= 0) {

					String fromId = getId(rl.getFrom());
					String toId = getId(rl.getTo());

					distanceMatrixCacheObj.put(new Pair<>(fromId, toId), rl.getDistance());
				}
			}
			distanceCacheObj.put(route.getRouteOption(), distanceMatrixCacheObj);
		}
		distanceCache = new SoftReference<>(distanceCacheObj);
		return distanceCacheObj;
	}

	private String getId(Port port) {
		Location l = port.getLocation();
		if (l != null) {
			if (l.getTempMMXID() != null) {
				return l.getTempMMXID();
			}
			return l.getName();
		}
		return port.getName();
	}

	public synchronized void clearCache() {
		distanceCache.clear();
	}

	public ModelDistanceProvider(final PortModel portModel) {
		this.portModel = portModel;
		// this.distanceProvider = null;
	}

	// public ModelDistanceProvider(final IDistanceProvider distanceProvider) {
	// this.portModel = null;
	// this.distanceModel = null;
	// this.distanceProvider = distanceProvider;
	// }

	public int getMinDistance(final Port from, final Port to) {

		int distance = Integer.MAX_VALUE;
		for (final RouteOption routeOption : RouteOption.values()) {
			distance = Math.min(distance, getDistance(from, to, routeOption));
		}
		return distance;

	}

	public boolean hasDistance(final Port from, final Port to, final RouteOption routeOption) {
		return getDistance(from, to, routeOption) != Integer.MAX_VALUE;
	}

	public int getDistance(final Port from, final Port to, final RouteOption routeOption) {
		if (from == null || from.getLocation() == null) {
			return Integer.MAX_VALUE;
		}
		if (to == null || to.getLocation() == null) {
			return Integer.MAX_VALUE;
		}
		return getDistance(from.getLocation(), to.getLocation(), routeOption);
	}

	public int getDistance(final Location from, final Location to, final RouteOption routeOption) {
		if (from == null) {
			return Integer.MAX_VALUE;
		}
		if (to == null) {
			return Integer.MAX_VALUE;
		}
		return getDistance(from.getTempMMXID(), to.getTempMMXID(), routeOption);
	}

	public int getDistance(final String from, final String to, final RouteOption routeOption) {

		if (from == null) {
			return Integer.MAX_VALUE;
		}
		if (to == null) {
			return Integer.MAX_VALUE;
		}
		if (routeOption == null) {
			return Integer.MAX_VALUE;
		}

		if (from.equals(to)) {
			return 0;
		}

		// // Try and lookup the distance from the service first.
		// // TODO: There isn't really a need for the distance model if the provider is available.
		// // TODO: Store the version number separately
		// if (distanceProvider != null) {
		// return distanceProvider.getDistance(from, to, mapVia(routeOption));
		// }

		assert portModel != null;

		Map<RouteOption, Map<Pair<String, String>, Integer>> map = distanceCache.get();
		if (map == null) {
			map = buildCache();
		}
		final Map<Pair<String, String>, Integer> matrix = map.get(routeOption);
		if (matrix == null) {
			return Integer.MAX_VALUE;
		}

		final Pair<String, String> key = new Pair<>(from, to);
		if (matrix.containsKey(key)) {
			final Integer dist = matrix.get(key);
			if (dist != null) {
				return dist.intValue();
			}

		}
		return Integer.MAX_VALUE;
	}

	private Pair<String, String> makePortToPortKey(final Port from, final Port to) {
		return makePortToPortKey(from.getLocation(), to.getLocation());
	}

	private Pair<String, String> makePortToPortKey(final Location from, final Location to) {
		return new Pair<>(from.getTempMMXID(), to.getTempMMXID());
	}

	public String getVersion() {
		// if (distanceProvider != null) {
		// return distanceProvider.getVersion();
		// } else {
		return portModel.getDistanceDataVersion();
		// }
	}
	// public static Via mapVia(final RouteOption routeOption) {
	// switch (routeOption) {
	// case DIRECT:
	// return Via.Direct;
	// case PANAMA:
	// return Via.PanamaCanal;
	// case SUEZ:
	// return Via.SuezCanal;
	// }
	// throw new IllegalStateException();
	// }

	@Override
	public void notifyChanged(final @Nullable Notification notification) {

		super.notifyChanged(notification);

		if (notification.isTouch()) {
			return;
		}
		clearCache();
	}

	@Override
	public @Nullable Notifier getTarget() {
		return null;
	}

	@Override
	public void setTarget(final @Nullable Notifier newTarget) {

	}

	public boolean hasAnyDistance(final Port from, final Port to) {

		if (from.equals(to)) {
			return true;
		}

		for (final RouteOption routeOption : RouteOption.values()) {
			if (hasDistance(from, to, routeOption)) {
				return true;
			}
		}
		return false;
	}

	public String getCanalEntranceName(RouteOption routeOption, CanalEntry canalEntry) {
		for (Route r : portModel.getRoutes()) {
			if (r.getRouteOption() == routeOption) {
				if (canalEntry == CanalEntry.NORTHSIDE) {
					EntryPoint northEntrance = r.getNorthEntrance();
					if (northEntrance != null) {
						return northEntrance.getName();
					}
				} else if (canalEntry == CanalEntry.SOUTHSIDE) {
					EntryPoint southEntrance = r.getSouthEntrance();
					if (southEntrance != null) {
						return southEntrance.getName();
					}
				}
			}
		}
		return "";
	}

	public @Nullable Port getCanalPort(RouteOption routeOption, CanalEntry canalEntry) {

		for (Route r : portModel.getRoutes()) {
			if (r.getRouteOption() == routeOption) {
				if (canalEntry == CanalEntry.NORTHSIDE) {
					EntryPoint northEntrance = r.getNorthEntrance();
					if (northEntrance != null) {
						return northEntrance.getPort();
					}
				} else if (canalEntry == CanalEntry.SOUTHSIDE) {
					EntryPoint southEntrance = r.getSouthEntrance();
					if (southEntrance != null) {
						return southEntrance.getPort();
					}
				}
			}
		}
		return null;
	}
}
