/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
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
	private int northboundMaxIdleDays;
	private int southboundMaxIdleDays;
	private int arrivalMargin;
	private Map<IVessel, Integer> vesselToNorthboundMaxIdleDays = new HashMap<>();
	private Map<IVessel, Integer> vesselToSouthboundMaxIdleDays = new HashMap<>();

	private ImmutableMap<ECanalEntry, ImmutableList<IRouteOptionBooking>> assignedBookings;

	private ImmutableMap<ECanalEntry, ImmutableList<IRouteOptionBooking>> unassignedBookings;

	@Override
	public void setBookings(final Map<ECanalEntry, List<IRouteOptionBooking>> bookings) {
		final ImmutableMap.Builder<ECanalEntry, ImmutableList<IRouteOptionBooking>> builder = new ImmutableMap.Builder<>();
		bookings.forEach((k, v) -> {
			builder.put(k, ImmutableList.copyOf(v));
		});
		
		panamaBookings = builder.build();

		Map<ECanalEntry, ImmutableList<IRouteOptionBooking>> assignedBookings = new HashMap<>();
		Map<ECanalEntry, ImmutableList<IRouteOptionBooking>> unassignedBookings = new HashMap<>();

		panamaBookings.entrySet().forEach(e -> {
			final Set<@NonNull IRouteOptionBooking> assigned = e.getValue().stream() //
					.filter(j -> j.getVessels().size() == 1) //
					.collect(Collectors.toSet());

			final List<@NonNull IRouteOptionBooking> list_assigned = new ArrayList<>(assigned);
			Collections.sort(list_assigned);
			assignedBookings.put(e.getKey(), ImmutableList.copyOf(list_assigned));

			final List<@NonNull IRouteOptionBooking> list_unassigned = new ArrayList<>(Sets.difference(new HashSet<>(e.getValue()), new HashSet<>(assigned)));
			Collections.sort(list_unassigned);
			unassignedBookings.put(e.getKey(), ImmutableList.copyOf(list_unassigned));
		});

		//Make sure a list even if empty for both south-side + north-side bookings, in case no bookings set.
		for (ECanalEntry entryPoint : ECanalEntry.values()) {
			assignedBookings.putIfAbsent(entryPoint, ImmutableList.of());
			unassignedBookings.putIfAbsent(entryPoint, ImmutableList.of());
		}
		
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
	
	@Override
	public int getNorthboundMaxIdleDays(IVessel vessel) {
		return this.vesselToNorthboundMaxIdleDays.getOrDefault(vessel, this.northboundMaxIdleDays);
	}

	@Override
	public int getSouthboundMaxIdleDays(IVessel vessel) {
		return this.vesselToSouthboundMaxIdleDays.getOrDefault(vessel, this.southboundMaxIdleDays);
	}

	@Override
	public void setNorthboundMaxIdleDays(IVessel vessel, int maxIdleDays) {
		this.vesselToNorthboundMaxIdleDays.put(vessel, maxIdleDays);
	}

	@Override
	public void setSouthboundMaxIdleDays(IVessel vessel, int maxIdleDays) {
		this.vesselToSouthboundMaxIdleDays.put(vessel, maxIdleDays);		
	}
}
