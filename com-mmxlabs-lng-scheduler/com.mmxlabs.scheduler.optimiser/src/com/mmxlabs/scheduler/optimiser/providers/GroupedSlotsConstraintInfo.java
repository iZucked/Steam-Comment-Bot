package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public class GroupedSlotsConstraintInfo<T extends IPortSlot> {
	Set<@NonNull T> slots;
	int bound;

	public GroupedSlotsConstraintInfo(final Set<T> slots, final int bound) {
		this.slots = slots;
		this.bound = bound;
	}

	public Set<@NonNull T> getSlots() {
		return slots;
	}

	public int getBound() {
		return bound;
	}
}
