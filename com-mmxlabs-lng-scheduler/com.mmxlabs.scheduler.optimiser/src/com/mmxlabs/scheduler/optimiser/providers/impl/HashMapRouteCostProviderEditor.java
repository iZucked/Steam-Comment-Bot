/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProviderEditor;

public class HashMapRouteCostProviderEditor implements IRouteCostProviderEditor {

	private final Map<ERouteOption, Map<IVessel, EnumMap<CostType, ILongCurve>>> pricesByRouteClassAndState = new HashMap<>();

	private final Map<ERouteOption, ILongCurve> defaultPrices = new HashMap<>();

	private final Map<ERouteOption, Map<IVessel, Integer>> travelTimesByRouteAndClass = new HashMap<>();
	private final Map<ERouteOption, Map<IVessel, EnumMap<VesselState, Long>>> baseFuelByRouteAndClass = new HashMap<>();
	private final Map<ERouteOption, Map<IVessel, EnumMap<VesselState, Long>>> nboRateByRouteAndClass = new HashMap<>();

	/**
	 */
	@Override
	public long getRouteCost(final @NonNull ERouteOption route, final @NonNull IVessel vessel, final int voyageStartTime, final @NonNull CostType vesselState) {

		// Special case DIRECT
		if (route == ERouteOption.DIRECT) {
			return 0L;
		}

		ILongCurve cost = get(pricesByRouteClassAndState, route, vessel, vesselState, null);

		if (cost == null) {
			cost = defaultPrices.get(route);

		}
		if (cost != null) {
			return cost.getValueAtPoint(voyageStartTime);
		}
		return 0L;
	}

	/**
	 */
	@Override
	public void setRouteCost(final @NonNull ERouteOption route, final @NonNull IVessel vessel, final @NonNull CostType costType, final ILongCurve price) {
		set(pricesByRouteClassAndState, route, vessel, costType, price, CostType.class);
	}

	/**
	 */
	@Override
	public void setDefaultRouteCost(final ERouteOption route, final ILongCurve price) {
		defaultPrices.put(route, price);
	}

	@Override
	public void setRouteFuel(final @NonNull ERouteOption routeName, final @NonNull IVessel vessel, final @NonNull VesselState vesselState, final long baseFuelInScaledMT,
			final long nboRateInScaledM3) {

		set(baseFuelByRouteAndClass, routeName, vessel, vesselState, baseFuelInScaledMT, VesselState.class);
		set(nboRateByRouteAndClass, routeName, vessel, vesselState, nboRateInScaledM3, VesselState.class);
	}

	@Override
	public void setRouteTransitTime(final @NonNull ERouteOption routeName, final @NonNull IVessel vessel, final int transitTimeInHours) {
		if (!travelTimesByRouteAndClass.containsKey(routeName)) {
			travelTimesByRouteAndClass.put(routeName, new HashMap<>());
		}
		travelTimesByRouteAndClass.get(routeName).put(vessel, transitTimeInHours);
	}

	@Override
	public long getRouteFuelUsage(final @NonNull ERouteOption route, final @NonNull IVessel vessel, final VesselState vesselState) {
		return get(baseFuelByRouteAndClass, route, vessel, vesselState, 0L);
	}

	@Override
	public long getRouteNBORate(final @NonNull ERouteOption route, final @NonNull IVessel vessel, final VesselState vesselState) {
		return get(nboRateByRouteAndClass, route, vessel, vesselState, 0L);
	}

	@Override
	public int getRouteTransitTime(final @NonNull ERouteOption route, final IVessel vessel) {
		if (route == ERouteOption.DIRECT) {
			return 0;
		}
		final Map<IVessel, Integer> byVessel = travelTimesByRouteAndClass.get(route);
		if (byVessel != null) {
			final Integer value = byVessel.get(vessel);
			if (value != null) {
				return value;
			}
		}
		return 0;
	}

	private <T, U extends Enum<U>> void set(final Map<ERouteOption, Map<IVessel, EnumMap<U, T>>> map, final @NonNull ERouteOption route, final @NonNull IVessel vessel, final @NonNull U vesselState,
			final T value, final Class<U> enumClass) {
		if (!map.containsKey(route)) {
			final EnumMap<U, T> single = new EnumMap<U, T>(enumClass);
			single.put(vesselState, value);
			final HashMap<IVessel, EnumMap<U, T>> byV = new HashMap<>();
			byV.put(vessel, single);
			map.put(route, byV);
		} else {
			final Map<IVessel, EnumMap<U, T>> byV = map.get(route);
			if (byV.containsKey(vessel)) {
				byV.get(vessel).put(vesselState, value);
			} else {
				final EnumMap<U, T> single = new EnumMap<U, T>(enumClass);
				single.put(vesselState, value);
				byV.put(vessel, single);
			}
		}
	}

	private <T, U extends Enum<U>> T get(final Map<ERouteOption, Map<IVessel, EnumMap<U, T>>> map, final @NonNull ERouteOption route, final @NonNull IVessel vessel, final @NonNull U vesselState,
			final T defaultValue) {
		{
			final Map<IVessel, EnumMap<U, T>> byVessel = map.get(route);
			if (byVessel != null) {
				final EnumMap<U, T> byState = byVessel.get(vessel);
				if (byState != null) {
					final T x = byState.get(vesselState);
					if (x != null) {
						return x;
					}
				} else {
					throw new IllegalStateException();
				}
			}
		}
		return defaultValue;
	}
}
