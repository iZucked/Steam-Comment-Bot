/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProviderEditor;

/**
 * Implementation of {@link IPanamaBookingsProviderEditor} using a {@link HashMap} as backing data store.
 * 
 */
public class PanamaBookingsProviderEditor implements IPanamaBookingsProviderEditor {

	// make client configurable
	private static final int MAX_SPEED_TO_CANAL = Integer.MAX_VALUE;

	private ImmutableMap<ECanalEntry, ImmutableList<IRouteOptionBooking>> panamaBookings;
	private int strictBoundary;
	private int relaxedBoundary;
	private int northboundMaxIdleDays;
	private int southboundMaxIdleDays;
	private int relaxedBookingsCountAtEntryA;
	private int relaxedBookingsCountAtEntryB;
	private int arrivalMargin;

	private ImmutableMap<ECanalEntry, ImmutableList<IRouteOptionBooking>> assignedBookings;

	private ImmutableMap<ECanalEntry, ImmutableList<IRouteOptionBooking>> unassignedBookings;

	@Override
	public void setBookings(final Map<ECanalEntry, SortedSet<IRouteOptionBooking>> bookings) {
		final ImmutableMap.Builder<ECanalEntry, ImmutableList<IRouteOptionBooking>> builder = new ImmutableMap.Builder<>();
		bookings.forEach((k, v) -> {
			builder.put(k, ImmutableList.copyOf(v));
		});
		panamaBookings = builder.build();

		Map<ECanalEntry, ImmutableList<IRouteOptionBooking>> assignedBookings = new HashMap<>();
		Map<ECanalEntry, ImmutableList<IRouteOptionBooking>> unassignedBookings = new HashMap<>();

		panamaBookings.entrySet().forEach(e -> {
			final Set<@NonNull IRouteOptionBooking> assigned = e.getValue().stream().filter(j -> j.getPortSlot().isPresent()).collect(Collectors.toSet());

			final List<@NonNull IRouteOptionBooking> list_assigned = new ArrayList<>(assigned);
			Collections.sort(list_assigned);
			assignedBookings.put(e.getKey(), ImmutableList.copyOf(list_assigned));

			final List<@NonNull IRouteOptionBooking> list_unassigned = new ArrayList<>(Sets.difference(new HashSet<>(e.getValue()), new HashSet<>(assigned)));
			Collections.sort(list_unassigned);
			unassignedBookings.put(e.getKey(), ImmutableList.copyOf(list_unassigned));
		});

		this.assignedBookings = new ImmutableMap.Builder().putAll(assignedBookings).build();
		this.unassignedBookings = new ImmutableMap.Builder().putAll(unassignedBookings).build();

	}

	@Override
	public ImmutableMap<ECanalEntry, ImmutableList<IRouteOptionBooking>> getAllBookings() {
		if (panamaBookings == null) {
			throw new IllegalStateException("Panama booking not set");
		}
		return panamaBookings;
	}

	@Override
	public ImmutableMap<ECanalEntry, ImmutableList<IRouteOptionBooking>> getAssignedBookings() {
		if (assignedBookings == null) {
			throw new IllegalStateException("Panama booking not set");
		}
		return assignedBookings;
	}

	@Override
	public ImmutableMap<ECanalEntry, ImmutableList<IRouteOptionBooking>> getUnassignedBookings() {
		if (unassignedBookings == null) {
			throw new IllegalStateException("Panama booking not set");
		}
		return unassignedBookings;
	}

	@Override
	public int getStrictBoundary() {
		return strictBoundary;
	}

	public int getRelaxedBookingCountNorthbound() {
		return relaxedBookingsCountAtEntryA;
	}

	@Override
	public int getRelaxedBookingCountSouthbound() {
		return relaxedBookingsCountAtEntryB;
	}

	@Override
	public int getRelaxedBoundary() {
		return relaxedBoundary;
	}

	@Override
	public void setStrictBoundary(final int boundary) {
		strictBoundary = boundary;
	}

	@Override
	public void setRelaxedBookingCountNorthbound(final int bookingCount) {
		relaxedBookingsCountAtEntryA = bookingCount;
	}

	@Override
	public void setRelaxedBookingCountSouthbound(final int bookingCount) {
		relaxedBookingsCountAtEntryB = bookingCount;
	}

	@Override
	public void setRelaxedBoundary(final int boundary) {
		relaxedBoundary = boundary;
	}

	@Override
	public int getSpeedToCanal() {
		return MAX_SPEED_TO_CANAL;
	}

	@Override
	public int getMarginInHours() {
		return arrivalMargin;
	}

	@Override
	public void setArrivalMargin(final int margin) {
		arrivalMargin = margin;
	}

	@Override
	public int getNorthboundMaxIdleDays() {
		return northboundMaxIdleDays;
	}
	
	@Override
	public int getSouthboundMaxIdleDays() {
		return southboundMaxIdleDays;
	}
	
	@Override
	public void setSouthboundMaxIdleDays(int maxIdleDays) {
		southboundMaxIdleDays = maxIdleDays;
	}
	
	
	@Override
	public void setNorthboundMaxIdleDays(int maxIdleDays) {
		northboundMaxIdleDays = maxIdleDays;
	}
}
