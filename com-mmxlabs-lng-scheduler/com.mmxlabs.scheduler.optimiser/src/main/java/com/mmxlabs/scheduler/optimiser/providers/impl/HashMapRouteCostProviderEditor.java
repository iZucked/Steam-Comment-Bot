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
					final Integer x = byState.get(vesselClass);
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
	}

	@Override
	public void setRouteCost(String route, IVesselClass vesselClass,
			VesselState vesselState, int price) {
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
	public void setDefaultRouteCost(String route, int price) {
		defaultPrices.put(route, price);
	}
}
