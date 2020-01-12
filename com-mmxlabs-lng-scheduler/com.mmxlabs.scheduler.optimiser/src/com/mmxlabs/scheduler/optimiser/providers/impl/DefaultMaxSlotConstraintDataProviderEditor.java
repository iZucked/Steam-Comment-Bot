/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

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
import com.mmxlabs.scheduler.optimiser.providers.IMaxSlotConstraintDataProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;

public class DefaultMaxSlotConstraintDataProviderEditor implements IMaxSlotConstraintDataProviderEditor {
	@Inject
	private CalendarMonthMapper calendarMonthMapper;

	@Inject
	private @NonNull ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	List<Pair<Set<ILoadOption>, Integer>> loadMinRestrictions = new LinkedList<>();
	List<Pair<Set<IDischargeOption>, Integer>> dischargeMinRestrictions = new LinkedList<>();
	List<Pair<Set<ILoadOption>, Integer>> loadMaxRestrictions = new LinkedList<>();
	List<Pair<Set<IDischargeOption>, Integer>> dischargeMaxRestrictions = new LinkedList<>();

	@Override
	public void addMinLoadSlotsPerMonth(final List<ILoadOption> slots, final int startMonth, final int limit) {
		loadMinRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, limit, 1, 1));
	}

	@Override
	public void addMaxLoadSlotsPerMonth(final List<ILoadOption> slots, final int startMonth, final int limit) {
		loadMaxRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, limit, 1, 1));
	}

	@Override
	public void addMinLoadSlotsPerYear(final List<ILoadOption> slots, final int startMonth, final int limit) {
		loadMinRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, startMonth + 11, limit, 12, 12));
	}

	@Override
	public void addMaxLoadSlotsPerYear(final List<ILoadOption> slots, final int startMonth, final int limit) {
		loadMaxRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, startMonth + 11, limit, 12, 12));
	}

	@Override
	public void addMinLoadSlotsPerQuarter(final List<ILoadOption> slots, final int startMonth, final int limit) {
		loadMinRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, limit, 3, 3));
	}

	@Override
	public void addMaxLoadSlotsPerQuarter(final List<ILoadOption> slots, final int startMonth, final int limit) {
		loadMaxRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, limit, 3, 3));
	}

	@Override
	public void addMinLoadSlotsPerMonthlyPeriod(List<ILoadOption> slots, int startMonth, int period, int limit) {
		loadMinRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, limit, period, 1));	}

	@Override
	public void addMaxLoadSlotsPerMonthlyPeriod(List<ILoadOption> slots, int startMonth, int period, int limit) {
		loadMaxRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, limit, period, 1));	}

	@Override
	public void addMinDischargeSlotsPerMonth(final List<IDischargeOption> slots, final int startMonth, final int limit) {
		dischargeMinRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, limit, 1, 1));
	}

	@Override
	public void addMaxDischargeSlotsPerMonth(final List<IDischargeOption> slots, final int startMonth, final int limit) {
		dischargeMaxRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, limit, 1, 1));
	}

	@Override
	public void addMinDischargeSlotsPerYear(final List<IDischargeOption> slots, final int startMonth, final int limit) {
		dischargeMinRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, startMonth + 11, limit, 12, 12));
	}

	@Override
	public void addMaxDischargeSlotsPerYear(final List<IDischargeOption> slots, final int startMonth, final int limit) {
		dischargeMaxRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, startMonth + 11, limit, 12, 12));
	}

	@Override
	public void addMinDischargeSlotsPerQuarter(final List<IDischargeOption> slots, final int startMonth, final int limit) {
		dischargeMinRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, limit, 3, 3));
	}

	@Override
	public void addMaxDischargeSlotsPerQuarter(final List<IDischargeOption> slots, final int startMonth, final int limit) {
		dischargeMaxRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, limit, 3, 3));
	}

	@Override
	public void addMinDischargeSlotsPerMonthlyPeriod(List<IDischargeOption> slots, int startMonth, int period, int limit) {
		dischargeMinRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, limit, period, 1));
	}

	@Override
	public void addMaxDischargeSlotsPerMonthlyPeriod(List<IDischargeOption> slots, int startMonth, int period, int limit) {
		dischargeMaxRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, limit, period, 1));
	}

	@Override
	public void addMinMaxLoadSlotsPerMultiMonthPeriod(List<ILoadOption> slots, MonthlyDistributionConstraint monthlyDistributionConstraint) {
		setMinMaxSlotsPerMultiMonthPeriod(loadMinRestrictions, loadMaxRestrictions, slots, monthlyDistributionConstraint);
	}
	
	@Override
	public void addMinMaxDischargeSlotsPerMultiMonthPeriod(List<IDischargeOption> slots, MonthlyDistributionConstraint monthlyDistributionConstraint) {
		setMinMaxSlotsPerMultiMonthPeriod(dischargeMinRestrictions, dischargeMaxRestrictions, slots, monthlyDistributionConstraint);
	}

	private <T extends IPortSlot> void setMinMaxSlotsPerMultiMonthPeriod(List<Pair<Set<T>, Integer>> minList, List<Pair<Set<T>, Integer>> maxList, List<T> slots, MonthlyDistributionConstraint monthlyDistributionConstraint) {
		for (Row row : monthlyDistributionConstraint.getRows()) {
			if (row.getMin() != null) {
				minList.addAll(addSlotsPerCollectionOfMonths(slots, row.getMonths(), row.getMin()));
			}
			if (row.getMax() != null) {
				maxList.addAll(addSlotsPerCollectionOfMonths(slots, row.getMonths(), row.getMax()));
			}
		}
	}

	private <T extends IPortSlot> List<Pair<Set<T>, Integer>> addSlotsPerCollectionOfMonths(final List<T> slots, final Collection<Integer> monthsToConsider, final int limit) {
		final List<Pair<Set<T>, Integer>> slotsSets = new LinkedList<>();
		final Set<T> slotsSet = new LinkedHashSet<>();
		for (int month : monthsToConsider) {
			for (final T slot : slots) {
				if (getUTCMonth(slot) == month) {
					slotsSet.add(slot);
				}
			}
		}
		if (slotsSet.size() > 0) {
			slotsSets.add(new Pair<>(slotsSet, limit));
		}
		return slotsSets;
	}

	private <T extends IPortSlot> List<Pair<Set<T>, Integer>> addSlotsPerMonth(final List<T> slots, final int startMonth, final int limit) {
		final List<Pair<Set<T>, Integer>> slotsSets = new LinkedList<>();
		Collections.sort(slots, (a, b) -> Integer.compare(a.getTimeWindow().getInclusiveStart(), a.getTimeWindow().getInclusiveStart()));
		final T lastSlot = slots.get(slots.size() - 1);
		final int lastMonth = getUTCMonth(lastSlot);
		for (int month = calendarMonthMapper.mapChangePointToMonth(startMonth); month < lastMonth + 1; month++) {
			final Set<T> slotsSet = new LinkedHashSet<>();
			for (final T slot : slots) {
				if (getUTCMonth(slot) == month) {
					slotsSet.add(slot);
				}
			}
			if (slotsSet.size() > 1) {
				slotsSets.add(new Pair<>(slotsSet, limit));
			}
		}
		return slotsSets;
	}

	private <T extends IPortSlot> int getUTCMonth(final T slot) {
		final int lastMonth = calendarMonthMapper.mapChangePointToMonth(timeZoneToUtcOffsetProvider.UTC(slot.getTimeWindow().getInclusiveStart(), slot.getPort()));
		return lastMonth;
	}

	private <T extends IPortSlot> List<Pair<Set<T>, Integer>> addSlotsPerPeriod(final List<T> slots, final int startMonth, final int limit, final int interval, final int step) {
		if (slots.isEmpty()) {
			return Collections.<Pair<Set<T>, Integer>> emptyList();
		}
		Collections.sort(slots, (a, b) -> Integer.compare(a.getTimeWindow().getInclusiveStart(), a.getTimeWindow().getInclusiveStart()));
		final int lastMonth = getUTCMonth(slots.get(slots.size() - 1));
		return addSlotsPerPeriod(slots, startMonth, lastMonth, limit, interval, step);
	}

	private <T extends IPortSlot> List<Pair<Set<T>, Integer>> addSlotsPerPeriod(final List<T> slots, final int startMonth, final int endMonthInclusive, final int limit, final int interval, final int step) {
		final List<Pair<Set<T>, Integer>> slotsSets = new LinkedList<>();
		Collections.sort(slots, (a, b) -> Integer.compare(a.getTimeWindow().getInclusiveStart(), a.getTimeWindow().getInclusiveStart()));
		for (int month = startMonth; month < (endMonthInclusive + 2) - interval; month = month + step) {
			final Set<T> slotsSet = new LinkedHashSet<>();
			for (final T slot : slots) {
				if (getUTCMonth(slot) >= month && getUTCMonth(slot) < month + interval) {
					slotsSet.add(slot);
				}
			}
			if (slotsSet.size() > 0) {
				slotsSets.add(new Pair<>(slotsSet, limit));
			}
		}
		return slotsSets;
	}

	@Override
	public List<Pair<Set<ILoadOption>, Integer>> getAllMinLoadGroupCounts() {
		return loadMinRestrictions;
	}

	@Override
	public List<Pair<Set<ILoadOption>, Integer>> getAllMaxLoadGroupCounts() {
		return loadMaxRestrictions;
	}

	@Override
	public List<Pair<Set<IDischargeOption>, Integer>> getAllMinDischargeGroupCounts() {
		return dischargeMinRestrictions;
	}

	@Override
	public List<Pair<Set<IDischargeOption>, Integer>> getAllMaxDischargeGroupCounts() {
		return dischargeMaxRestrictions;
	}
}