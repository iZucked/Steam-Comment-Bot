/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

@NonNullByDefault
public class GroupedSlotsConstraintInfo<T extends IPortSlot> {
	private Set<T> slots;
	private int bound;

	public GroupedSlotsConstraintInfo(final Set<T> slots, final int bound) {
		this.slots = slots;
		this.bound = bound;
	}

	public Set<T> getSlots() {
		return slots;
	}

	public int getBound() {
		return bound;
	}
}
