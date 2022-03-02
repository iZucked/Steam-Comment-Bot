/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProviderEditor;

public final class HashSetLongTermSlotsEditor implements ILongTermSlotsProviderEditor {

	@NonNull
	private final Set<IPortSlot> portSlotMap = new LinkedHashSet<>();

	@NonNull
	private final Set<List<IPortSlot>> eventMap = new LinkedHashSet<>();

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

	@Override
	public @NonNull Collection<List<IPortSlot>> getLongTermEvents() {
		return this.eventMap;
	}

	@Override
	public void addEvent(@NonNull List<IPortSlot> event) {
		eventMap.add(event);
	}

}
