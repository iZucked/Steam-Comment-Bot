/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public interface ILongTermSlotsProviderEditor extends ILongTermSlotsProvider {

	public void addLongTermSlot(@NonNull IPortSlot element);
	public void addEvent(@NonNull IPortSlot event);
}
