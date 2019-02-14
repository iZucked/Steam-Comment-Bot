/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ILockedCargoProviderEditor;

public class DefaultLockedCargoProviderEditor implements ILockedCargoProviderEditor {

	private Set<IPortSlot> lockedCargoSlots = new HashSet<>();

	@Override
	public boolean isLockedSlot(@NonNull IPortSlot slot) {
		return lockedCargoSlots.contains(slot);
	}

	@Override
	public void addLockedCargo(@NonNull List<@NonNull IPortSlot> cargo) {
		cargo.forEach(lockedCargoSlots::add);
	}

}
