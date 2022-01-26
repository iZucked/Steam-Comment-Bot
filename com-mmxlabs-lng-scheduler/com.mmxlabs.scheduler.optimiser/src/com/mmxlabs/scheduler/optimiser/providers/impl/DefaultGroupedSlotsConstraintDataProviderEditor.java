/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.providers.GroupedSlotsConstraintInfo;
import com.mmxlabs.scheduler.optimiser.providers.IGroupedSlotsConstraintDataProviderEditor;

@NonNullByDefault
public class DefaultGroupedSlotsConstraintDataProviderEditor implements IGroupedSlotsConstraintDataProviderEditor {

	private final List<GroupedSlotsConstraintInfo<IDischargeOption>> dischargeRestrictions = new LinkedList<>();

	@Override
	public List<GroupedSlotsConstraintInfo<IDischargeOption>> getAllMinDischargeGroupCounts() {
		return dischargeRestrictions;
	}

	@Override
	public void addMinDischargeSlots(final List<Pair<IDischargeOption, Integer>> slots, final int limit) {
		final Set<IDischargeOption> set = slots.stream().map(Pair::getFirst).collect(Collectors.toSet());
		dischargeRestrictions.add(new GroupedSlotsConstraintInfo<>(set, limit));
	}
}