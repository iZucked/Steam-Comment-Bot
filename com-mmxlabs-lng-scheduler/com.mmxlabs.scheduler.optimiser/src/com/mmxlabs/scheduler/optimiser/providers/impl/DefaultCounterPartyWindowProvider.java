/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ICounterPartyWindowProviderEditor;

@NonNullByDefault
public class DefaultCounterPartyWindowProvider implements ICounterPartyWindowProviderEditor {

	private final Set<IPortSlot> cpWindowPortSlots = new HashSet<>();

	@Override
	public boolean isCounterPartyWindow(final IPortSlot portSlot) {
		return cpWindowPortSlots.contains(portSlot);
	}

	@Override
	public void setCounterPartyWindow(final IPortSlot slot) {
		cpWindowPortSlots.add(slot);
	}

}
