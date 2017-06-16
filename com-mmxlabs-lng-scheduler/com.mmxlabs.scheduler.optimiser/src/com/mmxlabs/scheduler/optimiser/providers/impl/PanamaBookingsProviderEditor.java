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
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProviderEditor;

/**
 * Implementation of {@link IPanamaBookingsProviderEditor} using a {@link HashMap} as backing data store.
 * 
 */
public class PanamaBookingsProviderEditor implements IPanamaBookingsProviderEditor {
	
	// make client configurable
	private static final int MAX_SPEED_TO_CANAL = Integer.MAX_VALUE;

	private ImmutableMap<IPort, ImmutableSortedSet<IRouteOptionBooking>> panamaBookings;
	private int strictBoundary;
	private int relaxedBoundary;
	private int relaxedBookingsCount;
	private int arrivalMargin;
	
	@Override
	public void setBookings(Map<IPort, SortedSet<IRouteOptionBooking>> bookings) {
		ImmutableMap.Builder<IPort, ImmutableSortedSet<IRouteOptionBooking>> builder = new ImmutableMap.Builder<>();
		bookings.forEach((k,v) -> {
			builder.put(k, ImmutableSortedSet.copyOf(v));
		});
		panamaBookings = builder.build();
	}

	@Override
	public ImmutableMap<IPort, ImmutableSortedSet<IRouteOptionBooking>> getBookings() {
		if (panamaBookings == null){
			throw new IllegalStateException("Panama booking not set");
		}
		return panamaBookings;
	}

	@Override
	public int getStrictBoundary() {
		return strictBoundary;
	}

	@Override
	public int getRelaxedBookingCount() {
		return relaxedBookingsCount;
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
	public void setRelaxedBookingCount(int bookingCount) {
		relaxedBookingsCount = bookingCount;
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
		return arrivalMargin;
	}

	@Override
	public void setArrivalMargin(int margin) {
		arrivalMargin = margin;
	}
}
