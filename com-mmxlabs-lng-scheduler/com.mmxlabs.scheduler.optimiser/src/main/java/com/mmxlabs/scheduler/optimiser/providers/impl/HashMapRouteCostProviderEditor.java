/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProviderEditor;

public class HashMapRouteCostProviderEditor implements IRouteCostProviderEditor {

	public HashMapRouteCostProviderEditor(final String name, final String defaultKey) {
		super();
		this.name = name;
		this.defaultKey = defaultKey;
	}

	final String name;
	private final Map<String, Map<IVesselClass, EnumMap<VesselState, Long>>> pricesByRouteClassAndState = new HashMap<String, Map<IVesselClass, EnumMap<VesselState, Long>>>();

	private final Map<String, Long> defaultPrices = new HashMap<String, Long>();
	private final String defaultKey;

	private final Map<String, Map<IVesselClass, Integer>> travelTimesByRouteAndClass = new HashMap<String, Map<IVesselClass, Integer>>();
	private final Map<String, Map<IVesselClass, EnumMap<VesselState, Long>>> baseFuelByRouteAndClass = new HashMap<String, Map<IVesselClass, EnumMap<VesselState, Long>>>();
	private final Map<String, Map<IVesselClass, EnumMap<VesselState, Long>>> nboRateByRouteAndClass = new HashMap<String, Map<IVesselClass, EnumMap<VesselState, Long>>>();

	/**
	 * @since 2.0
	 */
	@Override
	public long getRouteCost(final String route, final IVesselClass vesselClass, final VesselState vesselState) {

		if (route.equals(defaultKey)) {
			return 0;
		}
		Long cost = get(pricesByRouteClassAndState, route, vesselClass, vesselState, null);

		if (cost == null) {
			cost = defaultPrices.get(route);
		}
		if (cost != null) {
			return cost;
		}
		return 0;
	}

	/**
	 * @since 2.0
	 */
	@Override
	public void setRouteCost(final String route, final IVesselClass vesselClass, final VesselState vesselState, final long price) {
		set(pricesByRouteClassAndState, route, vesselClass, vesselState, price);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		pricesByRouteClassAndState.clear();
		baseFuelByRouteAndClass.clear();
		travelTimesByRouteAndClass.clear();
	}

	/**
	 * @since 2.0
	 */
	@Override
	public void setDefaultRouteCost(final String route, final long price) {
		defaultPrices.put(route, price);
	}

	@Override
	public void setRouteFuel(final String routeName, final IVesselClass vesselClass, final VesselState vesselState, final long baseFuelInScaledMT, final long nboRateInScaledM3) {

		set(baseFuelByRouteAndClass, routeName, vesselClass, vesselState, baseFuelInScaledMT);
		set(nboRateByRouteAndClass, routeName, vesselClass, vesselState, nboRateInScaledM3);
	}

	@Override
	public void setRouteTransitTime(final String routeName, final IVesselClass vc, final int transitTimeInHours) {
		if (!travelTimesByRouteAndClass.containsKey(routeName)) {
			travelTimesByRouteAndClass.put(routeName, new HashMap<IVesselClass, Integer>());
		}
		travelTimesByRouteAndClass.get(routeName).put(vc, transitTimeInHours);
	}

	@Override
	public long getRouteFuelUsage(final String route, final IVesselClass vesselClass, final VesselState vesselState) {
		return get(baseFuelByRouteAndClass, route, vesselClass, vesselState, 0l);
	}

	@Override
	public long getRouteNBORate(final String route, final IVesselClass vesselClass, final VesselState vesselState) {
		return get(nboRateByRouteAndClass, route, vesselClass, vesselState, 0l);
	}

	@Override
	public int getRouteTransitTime(final String route, final IVesselClass vesselClass) {
		if (defaultKey.equals(route)) {
			return 0;
		}
		final Map<IVesselClass, Integer> byClass = travelTimesByRouteAndClass.get(route);
		if (byClass != null) {
			final Integer value = byClass.get(vesselClass);
			if (value != null) {
				return value;
			}
		}
		return 0;
	}

	private <T> void set(final Map<String, Map<IVesselClass, EnumMap<VesselState, T>>> map, final String route, final IVesselClass vesselClass, final VesselState vesselState, final T value) {
		if (!map.containsKey(route)) {
			final EnumMap<VesselState, T> single = new EnumMap<VesselState, T>(VesselState.class);
			single.put(vesselState, value);
			final HashMap<IVesselClass, EnumMap<VesselState, T>> byV = new HashMap<IVesselClass, EnumMap<VesselState, T>>();
			byV.put(vesselClass, single);
			map.put(route, byV);
		} else {
			final Map<IVesselClass, EnumMap<VesselState, T>> byV = map.get(route);
			if (byV.containsKey(vesselClass)) {
				byV.get(vesselClass).put(vesselState, value);
			} else {
				final EnumMap<VesselState, T> single = new EnumMap<VesselState, T>(VesselState.class);
				single.put(vesselState, value);
				byV.put(vesselClass, single);
			}
		}
	}

	private <T> T get(final Map<String, Map<IVesselClass, EnumMap<VesselState, T>>> map, final String route, final IVesselClass vesselClass, final VesselState vesselState, final T defaultValue) {
		if (route.equals(defaultKey)) {
			return defaultValue;
		}
		{
			final Map<IVesselClass, EnumMap<VesselState, T>> byVessel = map.get(route);
			if (byVessel != null) {
				final EnumMap<VesselState, T> byState = byVessel.get(vesselClass);
				if (byState != null) {
					final T x = byState.get(vesselState);
					if (x != null) {
						return x;
					}
				}
			}
		}
		return defaultValue;
	}
}
