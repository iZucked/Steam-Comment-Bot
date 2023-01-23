/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

@NonNullByDefault
public interface IHeelCarrySlotProvider extends IDataComponentProvider {
	boolean isHeelCarryAllowed(IPortSlot portSlot);
}
