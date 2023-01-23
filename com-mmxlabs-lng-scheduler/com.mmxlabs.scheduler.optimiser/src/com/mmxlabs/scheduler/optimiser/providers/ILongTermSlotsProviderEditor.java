/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public interface ILongTermSlotsProviderEditor extends ILongTermSlotsProvider {

	void addLongTermSlot(@NonNull IPortSlot element);

	void addEvent(@NonNull List<IPortSlot> event);
}
