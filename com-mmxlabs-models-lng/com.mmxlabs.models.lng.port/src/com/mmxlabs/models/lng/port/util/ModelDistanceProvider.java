/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.util;

import java.lang.ref.SoftReference;
import java.util.Collections;
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
import com.mmxlabs.models.lng.port.ContingencyMatrix;
import com.mmxlabs.models.lng.port.ContingencyMatrixEntry;
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

	private SoftReference<@Nullable Map<RouteOption, Map<Pair<String, String>, Double>>> distanceCache = new SoftReference<>(null);
	private SoftReference<@Nullable Map<Pair<String, String>, Integer>> portToPortContingencyIdleTimeCache = new SoftReference<>(null);
	private Map<String, Port> mmxIDtoPort = new HashMap<>();

	public synchronized Map<RouteOption, Map<Pair<String, String>, Double>> buildDistanceCache() {

		for (final Port p : portModel.getPorts()) {
			Location l = p.getLocation();
			if (l != null) {
				mmxIDtoPort.put(l.getMmxId(), p);
			}
		}

		final Map<RouteOption, Map<Pair<String, String>, Double>> distanceCacheObj = new EnumMap<>(RouteOption.class);
		for (final Route route : portModel.getRoutes()) {
			final Map<Pair<String, String>, Double> distanceMatrixCacheObj = new HashMap<>();

			for (final RouteLine rl : route.getLines()) {
				if (rl.getDistance() != Double.MAX_VALUE && rl.getDistance() != Integer.MAX_VALUE && rl.getDistance() >= 0.0) {

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

	public synchronized Map<Pair<String, String>, Integer> buildContingnecyIdleTimeCache() {

		ContingencyMatrix contingencyMatrix = portModel.getContingencyMatrix();
		if (contingencyMatrix == null) {
			return Collections.emptyMap();
		}

		final Map<Pair<String, String>, Integer> cacheObj = new HashMap<>();

		for (final ContingencyMatrixEntry entry : contingencyMatrix.getEntries()) {

			String fromId = getId(entry.getFromPort());
			String toId = getId(entry.getToPort());

			cacheObj.put(new Pair<>(fromId, toId), entry.getDuration());
		}
		portToPortContingencyIdleTimeCache = new SoftReference<>(cacheObj);
		return cacheObj;
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
	
	public Port getPortByMMXID(final String mmxID) {
		if (mmxIDtoPort.isEmpty()) {
			buildDistanceCache();
		}
		return mmxIDtoPort.get(mmxID);
	}

	public synchronized void clearCache() {
		distanceCache.clear();
		portToPortContingencyIdleTimeCache.clear();
		mmxIDtoPort.clear();
	}

	public ModelDistanceProvider(final PortModel portModel) {
		this.portModel = portModel;
	}

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

		Map<RouteOption, Map<Pair<String, String>, Double>> map = distanceCache.get();
		if (map == null) {
			map = buildDistanceCache();
		}
		final Map<Pair<String, String>, Double> matrix = map.get(routeOption);
		if (matrix == null) {
			return Integer.MAX_VALUE;
		}

		final Pair<String, String> key = new Pair<>(from, to);
		if (matrix.containsKey(key)) {
			final Double dist = matrix.get(key);
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
		return portModel.getDistanceVersionRecord().getVersion();
	}

	@Override
	public void notifyChanged(final @Nullable Notification notification) {

		super.notifyChanged(notification);

		if (notification == null || notification.isTouch()) {
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
		// Override default impl and do nothing
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

	public static String getCanalEntranceName(PortModel portModel, RouteOption routeOption, CanalEntry canalEntry) {
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

	public int getPortToPortContingencyIdleTimeInHours(final Port from, final Port to) {
		if (from == null || from.getLocation() == null) {
			return 0;
		}
		if (to == null || to.getLocation() == null) {
			return 0;
		}
		return getPortToPortContingencyIdleTimeInHours(from.getLocation(), to.getLocation());
	}

	public int getPortToPortContingencyIdleTimeInHours(final Location from, final Location to) {
		if (from == null) {
			return 0;
		}
		if (to == null) {
			return 0;
		}
		return getPortToPortContingencyIdleTimeInHours(from.getTempMMXID(), to.getTempMMXID());
	}

	public int getPortToPortContingencyIdleTimeInHours(final String from, final String to) {

		if (from == null) {
			return 0;
		}
		if (to == null) {
			return 0;
		}

		if (from.equals(to)) {
			return 0;
		}

		assert portModel != null;

		Map<Pair<String, String>, Integer> map = portToPortContingencyIdleTimeCache.get();
		if (map == null) {
			map = buildContingnecyIdleTimeCache();
		}
		final Pair<String, String> key = new Pair<>(from, to);
		if (map.containsKey(key)) {
			final Integer days = map.get(key);
			if (days != null) {
				return 24 * days.intValue();
			}

		}
		return 0;
	}
}
