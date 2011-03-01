/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProviderEditor;

public class HashMapRouteCostProviderEditor implements
		IRouteCostProviderEditor {

	public HashMapRouteCostProviderEditor(String name, String defaultKey) {
		super();
		this.name = name;
		this.defaultKey = defaultKey;
	}

	final String name;
	private Map<String, Map<IVesselClass, EnumMap<VesselState, Integer>>>
		pricesByRouteClassAndState = new HashMap<String, Map<IVesselClass, EnumMap<VesselState, Integer>>>();
	
	private Map<String, Integer> defaultPrices = new HashMap<String, Integer>();
	private final String defaultKey;
	
	private Map<String, Map<IVesselClass, Integer>> travelTimesByRouteAndClass =
		new HashMap<String, Map<IVesselClass, Integer>>();
	private Map<String, Map<IVesselClass, Long>> baseFuelByRouteAndClass = 
		new HashMap<String, Map<IVesselClass, Long>>();
	
	@Override
	public int getRouteCost(String route, IVesselClass vesselClass,
			VesselState vesselState) {
		if (route.equals(defaultKey)) {
			return 0;
		}
		{
			final Map<IVesselClass, EnumMap<VesselState, Integer>> byVessel = pricesByRouteClassAndState.get(route);
			if (byVessel != null) {
				final EnumMap<VesselState, Integer> byState = byVessel.get(vesselClass);
				if (byState != null) {
					final Integer x = byState.get(vesselState);
					if (x != null) {
						return x;
					}
				}
			}
		}
		final Integer x = defaultPrices.get(route);
		if (x != null) return x;
		return 0;
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

	@Override
	public void setRouteCost(final String route, final IVesselClass vesselClass,
			final VesselState vesselState, final int price) {
		if (!pricesByRouteClassAndState.containsKey(route)) {
			EnumMap<VesselState, Integer> single = new EnumMap<VesselState, Integer>(VesselState.class);
			single.put(vesselState, price);
			HashMap<IVesselClass, EnumMap<VesselState, Integer>> byV = new HashMap<IVesselClass, EnumMap<VesselState, Integer>>();
			byV.put(vesselClass, single);
			pricesByRouteClassAndState.put(route, byV);
		} else {
			Map<IVesselClass, EnumMap<VesselState, Integer>> byV = pricesByRouteClassAndState.get(route);
			if (byV.containsKey(vesselClass)) {
				byV.get(vesselClass).put(vesselState, price);
			} else {
				EnumMap<VesselState, Integer> single = new EnumMap<VesselState, Integer>(VesselState.class);
				single.put(vesselState, price);
				byV.put(vesselClass, single);	
			}
		}
	}

	@Override
	public void setDefaultRouteCost(final String route, final int price) {
		defaultPrices.put(route, price);
	}

	@Override
	public void setRouteTimeAndFuel(final String routeName, final IVesselClass vc,
			final int transitTimeInHours, final long baseFuelInScaledMT) {
		if (!travelTimesByRouteAndClass.containsKey(routeName)) {
			travelTimesByRouteAndClass.put(routeName, new HashMap<IVesselClass, Integer>());
		}
		travelTimesByRouteAndClass.get(routeName).put(vc, transitTimeInHours);
		
		if (!baseFuelByRouteAndClass.containsKey(routeName)) {
			baseFuelByRouteAndClass.put(routeName, new HashMap<IVesselClass, Long>());
		}
		baseFuelByRouteAndClass.get(routeName).put(vc, baseFuelInScaledMT);
	}

	@Override
	public long getRouteFuelUsage(final String route, final IVesselClass vesselClass) {
		if (route == defaultKey) return 0;
		final Map<IVesselClass, Long> byClass = baseFuelByRouteAndClass.get(route);
		if (byClass != null) {
			final Long value = byClass.get(vesselClass);
			if (value != null) {
				return value;
			}
		}
		return 0;
	}

	@Override
	public int getRouteTransitTime(final String route, final IVesselClass vesselClass) {
		if (route == defaultKey) return 0;
		final Map<IVesselClass, Integer> byClass = travelTimesByRouteAndClass.get(route);
		if (byClass != null) {
			final Integer value = byClass.get(vesselClass);
			if (value != null) {
				return value;
			}
		}
		return 0;
	}
}
