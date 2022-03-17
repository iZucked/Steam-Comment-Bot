/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ICounterPartyVolumeProviderEditor;

@NonNullByDefault
public class DefaultCounterPartyVolumeProvider implements ICounterPartyVolumeProviderEditor {

	private final Set<IPortSlot> cpVolumePortSlots = new HashSet<>();

	@Override
	public boolean isCounterPartyVolume(IPortSlot portSlot) {
		return cpVolumePortSlots.contains(portSlot);
	}

	@Override
	public void setCounterPartyVolume(IPortSlot slot) {
		cpVolumePortSlots.add(slot);
	}
}
