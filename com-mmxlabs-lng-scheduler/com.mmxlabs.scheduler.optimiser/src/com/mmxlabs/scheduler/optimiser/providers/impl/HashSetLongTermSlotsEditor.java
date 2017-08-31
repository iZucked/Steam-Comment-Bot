/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProviderEditor;

public final class HashSetLongTermSlotsEditor implements ILongTermSlotsProviderEditor {

	@NonNull
	private final Set<IPortSlot> portSlotMap = new HashSet<>();

	@Override
	public @NonNull Collection<IPortSlot> getLongTermSlots() {
		return this.portSlotMap;
	}
	
	@Override
	public boolean isLongTermSlot(@NonNull IPortSlot portSlot) {
		return portSlotMap.contains(portSlot);
	}
	
	@Override
	public void addLongTermSlot(@NonNull IPortSlot element) {
		portSlotMap.add(element);
	}

}
