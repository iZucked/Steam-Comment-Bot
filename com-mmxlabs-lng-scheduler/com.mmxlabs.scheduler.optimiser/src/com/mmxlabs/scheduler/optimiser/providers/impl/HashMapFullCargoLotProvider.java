/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IFullCargoLotProviderEditor;

public final class HashMapFullCargoLotProvider implements IFullCargoLotProviderEditor {

	@NonNull
	private final Set<IPortSlot> elements = new HashSet<IPortSlot>();

	@Override
	public boolean hasFCLRequirment(@NonNull IPortSlot portSlot) {
		return elements.contains(portSlot);
	}
	
	@Override
	public void addFCLSlot(@NonNull IPortSlot portSlot) {
		elements.add(portSlot);
	}
}
