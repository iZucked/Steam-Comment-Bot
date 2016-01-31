/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProviderEditor;

public class HashMapRouteCostProviderEditor implements IRouteCostProviderEditor {

	private final Map<ERouteOption, Map<IVesselClass, EnumMap<VesselState, Long>>> pricesByRouteClassAndState = new HashMap<>();

	private final Map<ERouteOption, Long> defaultPrices = new HashMap<>();

	private final Map<ERouteOption, Map<IVesselClass, Integer>> travelTimesByRouteAndClass = new HashMap<>();
	private final Map<ERouteOption, Map<IVesselClass, EnumMap<VesselState, Long>>> baseFuelByRouteAndClass = new HashMap<>();
	private final Map<ERouteOption, Map<IVesselClass, EnumMap<VesselState, Long>>> nboRateByRouteAndClass = new HashMap<>();

	/**
	 */
	@Override
	public long getRouteCost(final ERouteOption route, final IVesselClass vesselClass, final VesselState vesselState) {

		if (route == ERouteOption.DIRECT) {
			return 0L;
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
	 */
	@Override
	public void setRouteCost(final ERouteOption route, final IVesselClass vesselClass, final VesselState vesselState, final long price) {
		set(pricesByRouteClassAndState, route, vesselClass, vesselState, price);
	}

	/**
	 */
	@Override
	public void setDefaultRouteCost(final ERouteOption route, final long price) {
		defaultPrices.put(route, price);
	}

	@Override
	public void setRouteFuel(final ERouteOption routeName, final IVesselClass vesselClass, final VesselState vesselState, final long baseFuelInScaledMT, final long nboRateInScaledM3) {

		set(baseFuelByRouteAndClass, routeName, vesselClass, vesselState, baseFuelInScaledMT);
		set(nboRateByRouteAndClass, routeName, vesselClass, vesselState, nboRateInScaledM3);
	}

	@Override
	public void setRouteTransitTime(final ERouteOption routeName, final IVesselClass vc, final int transitTimeInHours) {
		if (!travelTimesByRouteAndClass.containsKey(routeName)) {
			travelTimesByRouteAndClass.put(routeName, new HashMap<IVesselClass, Integer>());
		}
		travelTimesByRouteAndClass.get(routeName).put(vc, transitTimeInHours);
	}

	@Override
	public long getRouteFuelUsage(final ERouteOption route, final IVesselClass vesselClass, final VesselState vesselState) {
		return get(baseFuelByRouteAndClass, route, vesselClass, vesselState, 0L);
	}

	@Override
	public long getRouteNBORate(final ERouteOption route, final IVesselClass vesselClass, final VesselState vesselState) {
		return get(nboRateByRouteAndClass, route, vesselClass, vesselState, 0L);
	}

	@Override
	public int getRouteTransitTime(final ERouteOption route, final IVesselClass vesselClass) {
		if (route == ERouteOption.DIRECT) {
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

	private <T> void set(final Map<ERouteOption, Map<IVesselClass, EnumMap<VesselState, T>>> map, final ERouteOption route, final IVesselClass vesselClass, final VesselState vesselState,
			final T value) {
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

	private <T> T get(final Map<ERouteOption, Map<IVesselClass, EnumMap<VesselState, T>>> map, final ERouteOption route, final IVesselClass vesselClass, final VesselState vesselState,
			final T defaultValue) {
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
