/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaSlotsProviderEditor;

/**
 * Implementation of {@link IPanamaSlotsProviderEditor} using a {@link HashMap} as backing data store.
 * 
 */
public class PanamaBookingsProviderEditor implements IPanamaSlotsProviderEditor {
	
	// make client configurable
	private static final int MAX_SPEED_TO_CANAL = 16;
	private static final int CANAL_SLOT_MARGIN = 24;

	private ImmutableMap<IPort, ImmutableSortedSet<IRouteOptionBooking>> panamaSlots;
	private int strictBoundary;
	private int relaxedBoundary;
	private int relaxedSlotCount;
	
	@Override
	public void setBookings(Map<IPort, SortedSet<IRouteOptionBooking>> slots) {
		ImmutableMap.Builder<IPort, ImmutableSortedSet<IRouteOptionBooking>> builder = new ImmutableMap.Builder<>();
		slots.forEach((k,v) -> {
			builder.put(k, ImmutableSortedSet.copyOf(v));
		});
		panamaSlots = builder.build();
	}

	@Override
	public ImmutableMap<IPort, ImmutableSortedSet<IRouteOptionBooking>> getBookings() {
		if (panamaSlots == null){
			throw new IllegalStateException("Panama slots not set");
		}
		return panamaSlots;
	}

	@Override
	public int getStrictBoundary() {
		return strictBoundary;
	}

	@Override
	public int getRelaxedSlotCount() {
		return relaxedSlotCount;
	}

	@Override
	public int getRelaxedBoundary() {
		return relaxedBoundary;
	}

	@Override
	public void setStrictBoundary(int boundary) {
		strictBoundary = boundary;
	}

	@Override
	public void setRelaxedSlotCount(int slotCount) {
		relaxedSlotCount = slotCount;
	}

	@Override
	public void setRelaxedBoundary(int boundary) {
		relaxedBoundary = boundary;
	}

	@Override
	public int getSpeedToCanal() {
		return MAX_SPEED_TO_CANAL;
	}

	@Override
	public int getMargin() {
		return CANAL_SLOT_MARGIN;
	}
}
