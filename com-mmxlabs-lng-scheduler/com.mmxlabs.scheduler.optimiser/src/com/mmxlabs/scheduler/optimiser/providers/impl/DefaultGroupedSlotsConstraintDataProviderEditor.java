/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

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
	public void addMinDischargeSlots(final List<IDischargeOption> slots, final int limit) {
		final Set<IDischargeOption> set = new HashSet<>(slots);
		dischargeRestrictions.add(new GroupedSlotsConstraintInfo<>(set, limit));
	}
}