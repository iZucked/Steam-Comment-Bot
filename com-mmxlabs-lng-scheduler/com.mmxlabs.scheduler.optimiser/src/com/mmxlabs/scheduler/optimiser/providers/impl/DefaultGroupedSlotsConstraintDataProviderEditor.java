/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.series.CalendarMonthMapper;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.providers.GroupedSlotsConstraintInfo;
import com.mmxlabs.scheduler.optimiser.providers.IGroupedSlotsConstraintDataProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;

public class DefaultGroupedSlotsConstraintDataProviderEditor implements IGroupedSlotsConstraintDataProviderEditor {

	@Inject
	private CalendarMonthMapper calendarMonthMapper;

	@Inject
	private @NonNull ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;
	
	List<GroupedSlotsConstraintInfo<IDischargeOption>> dischargeRestrictions = new LinkedList<>();

	@Override
	public List<GroupedSlotsConstraintInfo<IDischargeOption>> getAllMinDischargeGroupCounts() {
		return dischargeRestrictions;
	}

	@Override
	public void addMinDischargeSlots(List<Pair<IDischargeOption, Integer>> slots, int limit) {
		final Set<IDischargeOption> set = slots.stream().map(Pair::getFirst).collect(Collectors.toSet());
		dischargeRestrictions.add(new GroupedSlotsConstraintInfo<>(set, limit));
	}
}