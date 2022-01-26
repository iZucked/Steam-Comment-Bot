/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.providers.GroupedSlotsConstraintInfo;
import com.mmxlabs.scheduler.optimiser.providers.IGroupedSlotsConstraintDataProviderEditor;

public class DefaultGroupedSlotsConstraintDataProviderEditor implements IGroupedSlotsConstraintDataProviderEditor {

	private List<GroupedSlotsConstraintInfo<IDischargeOption>> dischargeRestrictions = new LinkedList<>();

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