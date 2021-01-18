/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.series.CalendarMonthMapper;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.util.MonthlyDistributionConstraint;
import com.mmxlabs.scheduler.optimiser.components.util.MonthlyDistributionConstraint.Row;
import com.mmxlabs.scheduler.optimiser.providers.ConstraintInfo;
import com.mmxlabs.scheduler.optimiser.providers.IMaxSlotConstraintDataProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;

public class DefaultMaxSlotConstraintDataProviderEditor<P,C> implements IMaxSlotConstraintDataProviderEditor<P,C> {

	@Inject
	private CalendarMonthMapper calendarMonthMapper;

	@Inject
	private @NonNull ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;
	
	List<ConstraintInfo<P,C,ILoadOption>> loadMinRestrictions = new LinkedList<>();
	List<ConstraintInfo<P,C,IDischargeOption>> dischargeMinRestrictions = new LinkedList<>();
	List<ConstraintInfo<P,C,ILoadOption>> loadMaxRestrictions = new LinkedList<>();
	List<ConstraintInfo<P,C,IDischargeOption>> dischargeMaxRestrictions = new LinkedList<>();

	@Override
	public void addMinLoadSlotsPerMonth(final P contractProfile, final C profileConstraint, final List<Pair<ILoadOption,Integer>> slots, final int startMonth, final int limit) {
		loadMinRestrictions.addAll(addSlotsPerPeriod(contractProfile, profileConstraint, slots, startMonth, limit, 1, 1));
	}

	@Override
	public void addMaxLoadSlotsPerMonth(final P contractProfile, final C profileConstraint, final List<Pair<ILoadOption,Integer>> slots, final int startMonth, final int limit) {
		loadMaxRestrictions.addAll(addSlotsPerPeriod(contractProfile, profileConstraint, slots, startMonth, limit, 1, 1));
	}

	@Override
	public void addMinLoadSlotsPerYear(final P contractProfile, final C profileConstraint, final List<Pair<ILoadOption,Integer>> slots, final int startMonth, final int limit) {
		loadMinRestrictions.addAll(addSlotsPerPeriod(contractProfile, profileConstraint, slots, startMonth, startMonth + 11, limit, 12, 12));
	}

	@Override
	public void addMaxLoadSlotsPerYear(final P contractProfile, final C profileConstraint, final List<Pair<ILoadOption,Integer>> slots, final int startMonth, final int limit) {
		loadMaxRestrictions.addAll(addSlotsPerPeriod(contractProfile, profileConstraint, slots, startMonth, startMonth + 11, limit, 12, 12));
	}

	@Override
	public void addMinLoadSlotsPerQuarter(final P contractProfile, final C profileConstraint, final List<Pair<ILoadOption,Integer>> slots, final int startMonth, final int limit) {
		loadMinRestrictions.addAll(addSlotsPerPeriod(contractProfile, profileConstraint, slots, startMonth, limit, 3, 3));
	}

	@Override
	public void addMaxLoadSlotsPerQuarter(final P contractProfile, final C profileConstraint, final List<Pair<ILoadOption,Integer>> slots, final int startMonth, final int limit) {
		loadMaxRestrictions.addAll(addSlotsPerPeriod(contractProfile, profileConstraint, slots, startMonth, limit, 3, 3));
	}

	@Override
	public void addMinLoadSlotsPerMonthlyPeriod(final P contractProfile, final C profileConstraint, List<Pair<ILoadOption,Integer>> slots, int startMonth, int period, int limit) {
		loadMinRestrictions.addAll(addSlotsPerPeriod(contractProfile, profileConstraint, slots, startMonth, limit, period, 1));	}

	@Override
	public void addMaxLoadSlotsPerMonthlyPeriod(final P contractProfile, final C profileConstraint, List<Pair<ILoadOption,Integer>> slots, int startMonth, int period, int limit) {
		loadMaxRestrictions.addAll(addSlotsPerPeriod(contractProfile, profileConstraint, slots, startMonth, limit, period, 1));	}

	@Override
	public void addMinDischargeSlotsPerMonth(final P contractProfile, final C profileConstraint, final List<Pair<IDischargeOption,Integer>> slots, final int startMonth, final int limit) {
		dischargeMinRestrictions.addAll(addSlotsPerPeriod(contractProfile, profileConstraint, slots, startMonth, limit, 1, 1));
	}

	@Override
	public void addMaxDischargeSlotsPerMonth(final P contractProfile, final C profileConstraint, final List<Pair<IDischargeOption,Integer>> slots, final int startMonth, final int limit) {
		dischargeMaxRestrictions.addAll(addSlotsPerPeriod(contractProfile, profileConstraint, slots, startMonth, limit, 1, 1));
	}

	@Override
	public void addMinDischargeSlotsPerYear(final P contractProfile, final C profileConstraint, final List<Pair<IDischargeOption,Integer>> slots, final int startMonth, final int limit) {
		dischargeMinRestrictions.addAll(addSlotsPerPeriod(contractProfile, profileConstraint, slots, startMonth, startMonth + 11, limit, 12, 12));
	}

	@Override
	public void addMaxDischargeSlotsPerYear(final P contractProfile, final C profileConstraint, final List<Pair<IDischargeOption,Integer>> slots, final int startMonth, final int limit) {
		dischargeMaxRestrictions.addAll(addSlotsPerPeriod(contractProfile, profileConstraint, slots, startMonth, startMonth + 11, limit, 12, 12));
	}

	@Override
	public void addMinDischargeSlotsPerQuarter(final P contractProfile, final C profileConstraint, final List<Pair<IDischargeOption,Integer>> slots, final int startMonth, final int limit) {
		dischargeMinRestrictions.addAll(addSlotsPerPeriod(contractProfile, profileConstraint, slots, startMonth, limit, 3, 3));
	}

	@Override
	public void addMaxDischargeSlotsPerQuarter(final P contractProfile, final C profileConstraint, final List<Pair<IDischargeOption,Integer>> slots, final int startMonth, final int limit) {
		dischargeMaxRestrictions.addAll(addSlotsPerPeriod(contractProfile, profileConstraint, slots, startMonth, limit, 3, 3));
	}

	@Override
	public void addMinDischargeSlotsPerMonthlyPeriod(final P contractProfile, final C profileConstraint, List<Pair<IDischargeOption,Integer>> slots, int startMonth, int period, int limit) {
		dischargeMinRestrictions.addAll(addSlotsPerPeriod(contractProfile, profileConstraint, slots, startMonth, limit, period, 1));
	}

	@Override
	public void addMaxDischargeSlotsPerMonthlyPeriod(final P contractProfile, final C profileConstraint, List<Pair<IDischargeOption,Integer>> slots, int startMonth, int period, int limit) {
		dischargeMaxRestrictions.addAll(addSlotsPerPeriod(contractProfile, profileConstraint, slots, startMonth, limit, period, 1));
	}

	@Override
	public void addMinMaxLoadSlotsPerMultiMonthPeriod(final P contractProfile, final C profileConstraint, List<Pair<ILoadOption,Integer>> slots, MonthlyDistributionConstraint monthlyDistributionConstraint) {
		setMinMaxSlotsPerMultiMonthPeriod(contractProfile, profileConstraint, loadMinRestrictions, loadMaxRestrictions, slots, monthlyDistributionConstraint);
	}
	
	@Override
	public void addMinMaxDischargeSlotsPerMultiMonthPeriod(final P contractProfile, final C profileConstraint, List<Pair<IDischargeOption,Integer>> slots, MonthlyDistributionConstraint monthlyDistributionConstraint) {
		setMinMaxSlotsPerMultiMonthPeriod(contractProfile, profileConstraint, dischargeMinRestrictions, dischargeMaxRestrictions, slots, monthlyDistributionConstraint);
	}

	private <T extends IPortSlot> void setMinMaxSlotsPerMultiMonthPeriod(final P contractProfile, final C profileConstraint, List<ConstraintInfo<P,C,T>> minList, List<ConstraintInfo<P,C,T>> maxList, List<Pair<T,Integer>> slots, MonthlyDistributionConstraint monthlyDistributionConstraint) {
		for (Row row : monthlyDistributionConstraint.getRows()) {
			if (row.getMin() != null) {
				minList.addAll(addSlotsPerCollectionOfMonths(contractProfile, profileConstraint, slots, row, row.getMin()));
			}
			if (row.getMax() != null) {
				maxList.addAll(addSlotsPerCollectionOfMonths(contractProfile, profileConstraint, slots, row, row.getMax()));
			}
		}
	}

	private <T extends IPortSlot> List<ConstraintInfo<P,C,T>> addSlotsPerCollectionOfMonths(final P contractProfile, final C profileConstraint, final List<Pair<T,Integer>> slots, final Row row, final int limit) {
		final Collection<Integer> monthsToConsider = row.getMonths();
		final List<ConstraintInfo<P,C,T>> constraints = new LinkedList<>();
		final Set<T> slotsSet = new LinkedHashSet<>();
		for (int month : monthsToConsider) {
			for (final Pair<T,Integer> slot : slots) {
				//Change to get month number from earliest.
				if (slot.getSecond() == month) { //Bug here: First slot getUTCMonth(slot) = 2, first month = 3.
					slotsSet.add(slot.getFirst());
				}
			}
		}
		if (!slotsSet.isEmpty()) {
			constraints.add(new ConstraintInfo<>(contractProfile,profileConstraint, slotsSet, limit, row));
		}
		return constraints;
	}

	private <T extends IPortSlot> int getUTCMonth(final T slot) {
		//Check for non-hourly timezone.
		boolean nonHourlyTimezone = false;
		if (slot.getPort() != null) {
			String timezone = slot.getPort().getTimeZoneId();
			LocalDateTime ldt = LocalDateTime.of(2020,1,1,0,0,0);
			Instant instant = ldt.toInstant(ZoneOffset.UTC);
			ZoneId zid = ZoneId.of(timezone); 
			ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zid);
			if (zdt.getMinute() != 0) {
				nonHourlyTimezone = true;
			}
		}
		if (nonHourlyTimezone) {
			//FIXME: should really be using smaller increments (e.g. every 15 minutes) than 1 hour.
			//Add an hour to compensate for rounding down to nearest hour when non hourly timezone, before mapping to a month.
			final int lastMonth = calendarMonthMapper.mapChangePointToMonth(timeZoneToUtcOffsetProvider.UTC(slot.getTimeWindow().getInclusiveStart(), slot.getPort())+1);
			return lastMonth;			
		}
		else {
			final int lastMonth = calendarMonthMapper.mapChangePointToMonth(timeZoneToUtcOffsetProvider.UTC(slot.getTimeWindow().getInclusiveStart(), slot.getPort()));
			return lastMonth;
		}
	}

	private <T extends IPortSlot> List<ConstraintInfo<P,C,T>> addSlotsPerPeriod(final P contractProfile, final C profileConstraint, final List<Pair<T,Integer>> slots, final int startMonth, final int limit, final int interval, final int step) {
		if (slots.isEmpty()) {
			return Collections.emptyList();
		}
		Collections.sort(slots, (a, b) -> Integer.compare(a.getFirst().getTimeWindow().getInclusiveStart(), b.getFirst().getTimeWindow().getInclusiveStart()));
		final int lastMonth = slots.get(slots.size() - 1).getSecond();
		return addSlotsPerPeriod(contractProfile, profileConstraint, slots, startMonth, lastMonth, limit, interval, step);
	}

	private <T extends IPortSlot> List<ConstraintInfo<P,C,T>> addSlotsPerPeriod(final P contractProfile, final C profileConstraint, final List<Pair<T,Integer>> slots, final int startMonth, final int endMonthInclusive, final int limit, final int interval, final int step) {
		final List<ConstraintInfo<P,C,T>> slotsSets = new LinkedList<>();
		Collections.sort(slots, (a, b) -> Integer.compare(a.getFirst().getTimeWindow().getInclusiveStart(), b.getFirst().getTimeWindow().getInclusiveStart()));
		for (int month = startMonth; month < (endMonthInclusive + 2) - interval; month = month + step) {
			final Set<T> slotsSet = new LinkedHashSet<>();
			for (final Pair<T,Integer> slot : slots) {
				if (slot.getSecond() >= month && slot.getSecond() < month + interval) {
					slotsSet.add(slot.getFirst());
				}
			}
			if (!slotsSet.isEmpty()) {
				slotsSets.add(new ConstraintInfo<P,C,T>(contractProfile, profileConstraint, slotsSet, limit));
			}
		}
		return slotsSets;
	}

	@Override
	public List<ConstraintInfo<P,C,ILoadOption>> getAllMinLoadGroupCounts() {
		return loadMinRestrictions;
	}

	@Override
	public List<ConstraintInfo<P,C,ILoadOption>> getAllMaxLoadGroupCounts() {
		return loadMaxRestrictions;
	}

	@Override
	public List<ConstraintInfo<P,C,IDischargeOption>> getAllMinDischargeGroupCounts() {
		return dischargeMinRestrictions;
	}

	@Override
	public List<ConstraintInfo<P,C,IDischargeOption>> getAllMaxDischargeGroupCounts() {
		return dischargeMaxRestrictions;
	}
}