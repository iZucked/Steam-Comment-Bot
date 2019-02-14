/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProviderEditor;

public final class HashSetLongTermSlotsEditor implements ILongTermSlotsProviderEditor {

	@NonNull
	private final Set<IPortSlot> portSlotMap = new LinkedHashSet<>();

	@NonNull
	private final Set<IPortSlot> eventMap = new LinkedHashSet<>();

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
	public @NonNull Collection<IPortSlot> getLongTermEvents() {
		// TODO Auto-generated method stub
		return this.eventMap;
	}

	@Override
	public void addEvent(@NonNull IPortSlot event) {
		eventMap.add(event);
	}

}
