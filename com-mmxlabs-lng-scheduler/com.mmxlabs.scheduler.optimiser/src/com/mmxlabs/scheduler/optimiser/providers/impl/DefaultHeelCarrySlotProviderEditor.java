/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IHeelCarrySlotProviderEditor;

@NonNullByDefault
public class DefaultHeelCarrySlotProviderEditor implements IHeelCarrySlotProviderEditor{

	private final Set<IPortSlot> heelCarrySlots = new HashSet<>(); 
	
	@Override
	public boolean isHeelCarryAllowed(IPortSlot portSlot) {
		return heelCarrySlots.contains(portSlot);
	}

	@Override
	public void setHeelCarrySlot(IPortSlot portSlot) {
		heelCarrySlots.add(portSlot);
	}

}
