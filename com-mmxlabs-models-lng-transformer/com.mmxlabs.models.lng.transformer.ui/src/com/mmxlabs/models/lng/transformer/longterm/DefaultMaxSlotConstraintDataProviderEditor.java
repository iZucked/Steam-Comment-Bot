package com.mmxlabs.models.lng.transformer.longterm;

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
import com.mmxlabs.scheduler.optimiser.providers.IMaxSlotCountConstraintDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;

public class DefaultMaxSlotConstraintDataProviderEditor implements IMaxSlotConstraintDataProviderEditor {
	@Inject
	CalendarMonthMapper calendarMonthMapper;
	@Inject
	private @NonNull ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	List<Pair<Set<ILoadOption>, Integer>> loadMinRestrictions = new LinkedList<>();
	List<Pair<Set<IDischargeOption>, Integer>> dischargeMinRestrictions = new LinkedList<>();
	List<Pair<Set<ILoadOption>, Integer>> loadMaxRestrictions = new LinkedList<>();
	List<Pair<Set<IDischargeOption>, Integer>> dischargeMaxRestrictions = new LinkedList<>();

	@Override
	public void addMinLoadSlotsPerMonth(List<ILoadOption> slots, int startMonth, int limit) {
		loadMinRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, limit, 1));
	}

	@Override
	public void addMaxLoadSlotsPerMonth(List<ILoadOption> slots, int startMonth, int limit) {
		loadMaxRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, limit, 1));
	}

	@Override
	public void addMinLoadSlotsPerYear(List<ILoadOption> slots, int startMonth, int limit) {
		loadMinRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, startMonth + 12, limit, 12));
	}

	@Override
	public void addMaxLoadSlotsPerYear(List<ILoadOption> slots, int startMonth, int limit) {
		loadMaxRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, startMonth + 12, limit, 12));
	}

	@Override
	public void addMinLoadSlotsPerQuarter(List<ILoadOption> slots, int startMonth, int limit) {
		loadMinRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, limit, 3));
	}

	@Override
	public void addMaxLoadSlotsPerQuarter(List<ILoadOption> slots, int startMonth, int limit) {
		loadMaxRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, limit, 3));
	}

	@Override
	public void addMinDischargeSlotsPerMonth(List<IDischargeOption> slots, int startMonth, int limit) {
		dischargeMinRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, limit, 1));
	}

	@Override
	public void addMaxDischargeSlotsPerMonth(List<IDischargeOption> slots, int startMonth, int limit) {
		dischargeMaxRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, limit, 1));
	}

	@Override
	public void addMinDischargeSlotsPerYear(List<IDischargeOption> slots, int startMonth, int limit) {
		dischargeMinRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, startMonth + 12, limit, 12));
	}

	@Override
	public void addMaxDischargeSlotsPerYear(List<IDischargeOption> slots, int startMonth, int limit) {
		dischargeMaxRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, startMonth + 12, limit, 12));
	}

	@Override
	public void addMinDischargeSlotsPerQuarter(List<IDischargeOption> slots, int startMonth, int limit) {
		dischargeMinRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, limit, 3));
	}

	@Override
	public void addMaxDischargeSlotsPerQuarter(List<IDischargeOption> slots, int startMonth, int limit) {
		dischargeMaxRestrictions.addAll(addSlotsPerPeriod(slots, startMonth, limit, 3));
	}

	private <T extends IPortSlot> List<Pair<Set<T>, Integer>> addSlotsPerMonth(List<T> slots,
			int startMonth, int limit) {
		List<Pair<Set<T>, Integer>> slotsSets = new LinkedList<>();
		Collections.sort(slots, (a,b) -> Integer.compare(a.getTimeWindow().getInclusiveStart(),
				a.getTimeWindow().getInclusiveStart()));
		T lastSlot = slots.get(slots.size() - 1);
		int lastMonth = getUTCMonth(lastSlot);
		for (int month = calendarMonthMapper.mapChangePointToMonth(startMonth); month < lastMonth + 1; month++) {
			Set<T> slotsSet = new LinkedHashSet<>();
			for (T slot : slots) {
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

	private <T extends IPortSlot> int getUTCMonth(T slot) {
		int lastMonth = calendarMonthMapper
				.mapChangePointToMonth(timeZoneToUtcOffsetProvider.UTC(slot.getTimeWindow().getInclusiveStart(),slot.getPort()));
		return lastMonth;
	}

	private <T extends IPortSlot> List<Pair<Set<T>, Integer>> addSlotsPerPeriod(List<T> slots,
			int startMonth, int limit, int interval) {
		List<Pair<Set<T>, Integer>> slotsSets = new LinkedList<>();
		Collections.sort(slots, (a,b) -> Integer.compare(a.getTimeWindow().getInclusiveStart(),
				a.getTimeWindow().getInclusiveStart()));
		int lastMonth = getUTCMonth(slots.get(slots.size()-1));
		return addSlotsPerPeriod(slots, startMonth, lastMonth, limit, interval);
	}

	private <T extends IPortSlot> List<Pair<Set<T>, Integer>> addSlotsPerPeriod(List<T> slots,
			int startMonth, int endMonthInclusive, int limit, int interval) {
		List<Pair<Set<T>, Integer>> slotsSets = new LinkedList<>();
		Collections.sort(slots, (a,b) -> Integer.compare(a.getTimeWindow().getInclusiveStart(),
				a.getTimeWindow().getInclusiveStart()));
		for (int month = startMonth; month < (endMonthInclusive + 2) - interval; month=month+interval) {
			Set<T> slotsSet = new LinkedHashSet<>();
			for (T slot : slots) {
				if (getUTCMonth(slot) >= month &&
						getUTCMonth(slot) < month + interval) {
					slotsSet.add(slot);
				}
			}
			if (slotsSet.size() > 1) {
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