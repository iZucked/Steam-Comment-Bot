/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.impl.PanamaSeasonalityCurve;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProviderEditor;

/**
 * Implementation of {@link IPanamaBookingsProviderEditor} using a
 * {@link HashMap} as backing data store.
 * 
 */
public class PanamaBookingsProviderEditor implements IPanamaBookingsProviderEditor {

	// make client configurable
	private static final int MAX_SPEED_TO_CANAL = Integer.MAX_VALUE;

	private ImmutableMap<ECanalEntry, ImmutableList<IRouteOptionBooking>> panamaBookings;
	private int arrivalMargin;

	private ImmutableMap<ECanalEntry, ImmutableList<IRouteOptionBooking>> assignedBookings;

	private ImmutableMap<ECanalEntry, ImmutableList<IRouteOptionBooking>> unassignedBookings;

	private ImmutableList<PanamaSeasonalityCurve> seasonalityCurves;

	@Override
	public void setBookings(final Map<ECanalEntry, List<IRouteOptionBooking>> bookings) {
		final ImmutableMap.Builder<ECanalEntry, ImmutableList<IRouteOptionBooking>> builder = new ImmutableMap.Builder<>();
		bookings.forEach((k, v) -> builder.put(k, ImmutableList.copyOf(v)));

		panamaBookings = builder.build();

		final Map<ECanalEntry, ImmutableList<@NonNull IRouteOptionBooking>> assignedBookings = new EnumMap<>(ECanalEntry.class);
		final Map<ECanalEntry, ImmutableList<@NonNull IRouteOptionBooking>> unassignedBookings = new EnumMap<>(ECanalEntry.class);

		panamaBookings.entrySet().forEach(e -> {
			final Set<@NonNull IRouteOptionBooking> assigned = e.getValue().stream() //
					.filter(j -> j.getVessels().size() == 1) //
					.collect(Collectors.toSet());

			final List<@NonNull IRouteOptionBooking> listAssigned = new ArrayList<>(assigned);
			Collections.sort(listAssigned);
			assignedBookings.put(e.getKey(), ImmutableList.copyOf(listAssigned));

			final List<@NonNull IRouteOptionBooking> listUnassigned = new ArrayList<>(Sets.difference(new HashSet<>(e.getValue()), new HashSet<>(assigned)));
			Collections.sort(listUnassigned);
			unassignedBookings.put(e.getKey(), ImmutableList.copyOf(listUnassigned));
		});

		// Make sure a list even if empty for both south-side + north-side bookings, in
		// case no bookings set.
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
	public int getNorthboundMaxIdleDays(IVessel vessel, int startAtPanamaTZ) {
		return getMaxIdleDays(vessel, startAtPanamaTZ, psr -> psr::getNorthboundMaxIdleDays);
	}

	@Override
	public int getSouthboundMaxIdleDays(IVessel vessel, int startAtPanamaTZ) {
		return getMaxIdleDays(vessel, startAtPanamaTZ, psr -> psr::getSouthboundMaxIdleDays);
	}

	private int getMaxIdleDays(final IVessel vessel, int startAtPanamaTZ, final Function<PanamaSeasonalityCurve, IntUnaryOperator> idleDaysProvider) {
		int defaultResult = Integer.MAX_VALUE;
		for (final PanamaSeasonalityCurve psc : seasonalityCurves) {
			if (psc.getVessel() == vessel) {
				return idleDaysProvider.apply(psc).applyAsInt(startAtPanamaTZ);
			} else if (psc.getVessel() == null) {
				defaultResult = idleDaysProvider.apply(psc).applyAsInt(startAtPanamaTZ);
			}
		}
		return defaultResult;
	}

	@Override
	public void setPanamaMaxIdleDays(List<PanamaSeasonalityCurve> seasonalityCurves) {
		this.seasonalityCurves = ImmutableList.copyOf(seasonalityCurves);
	}

	private int getWorstMaxIdleDays(int startDateInclusive, int endDateExclusive, IntUnaryOperator operator) {
		int result = Integer.MIN_VALUE;
		// We want to bind the end date value by three month after the start date
		endDateExclusive = Math.min(endDateExclusive, startDateInclusive + 1 + (90 * 24));
		for (int i = startDateInclusive; i < endDateExclusive; i += 24) {
			int temp = operator.applyAsInt(i);
			if (result < temp) {
				result = temp;
			}
		}
		return result;
	}

	private int getWorstNBMaxIdleDays(IVessel vessel, int startDateInclusive, int endDateExclusive) {
		return getWorstMaxIdleDays(startDateInclusive, endDateExclusive, time -> getNorthboundMaxIdleDays(vessel, time));
	}

	private int getWorstSBMaxIdleDays(IVessel vessel, int startDateInclusive, int endDateExclusive) {
		return getWorstMaxIdleDays(startDateInclusive, endDateExclusive, time -> getSouthboundMaxIdleDays(vessel, time));
	}

	public int getWorstIdleHours(IVessel vessel, int startDateInclusive, int endDateExclusive, boolean northbound) {
		assert startDateInclusive < endDateExclusive;
		int days;
		if (northbound) {
			days = getWorstNBMaxIdleDays(vessel, startDateInclusive, endDateExclusive);
		} else {
			days = getWorstSBMaxIdleDays(vessel, startDateInclusive, endDateExclusive);
		}
		assert days >= 0;
		assert days != Integer.MAX_VALUE;
		return days * 24;
	}

	private int getBestMaxIdleDays(IVessel vessel, int startDateInclusive, int endDateExclusive, IntUnaryOperator operator) {
		int result = Integer.MAX_VALUE;
		// We want to bind the end date value by three month after the start date
		endDateExclusive = Math.min(endDateExclusive, startDateInclusive + 1 + (90 * 24));
		for (int i = startDateInclusive; i < endDateExclusive; i += 24) {
			int temp = operator.applyAsInt(i);
			if (temp < result) {
				result = temp;
			}
		}
		return result;
	}

	private int getBestNBMaxIdleDays(IVessel vessel, int startDateInclusive, int endDateExclusive) {
		return getBestMaxIdleDays(vessel, startDateInclusive, endDateExclusive, time -> getNorthboundMaxIdleDays(vessel, time));
	}

	private int getBestSBMaxIdleDays(IVessel vessel, int startDateInclusive, int endDateExclusive) {
		return getBestMaxIdleDays(vessel, startDateInclusive, endDateExclusive, time -> getSouthboundMaxIdleDays(vessel, time));
	}

	public int getBestIdleHours(IVessel vessel, int startDateInclusive, int endDateExclusive, boolean northbound) {
		assert startDateInclusive < endDateExclusive;
		int days;
		if (northbound) {
			days = getBestNBMaxIdleDays(vessel, startDateInclusive, endDateExclusive);
		} else {
			days = getBestSBMaxIdleDays(vessel, startDateInclusive, endDateExclusive);
		}
		assert days >= 0;
		assert days != Integer.MAX_VALUE;
		return days;
	}

}
