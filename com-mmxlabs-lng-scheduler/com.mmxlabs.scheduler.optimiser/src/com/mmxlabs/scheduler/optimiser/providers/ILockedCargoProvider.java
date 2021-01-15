/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

@NonNullByDefault
public interface ILockedCargoProvider extends IDataComponentProvider {

	/**
	 * Returns true if the slots is part of a locked vessel assignment cargo
	 * 
	 * @param slot
	 * @return
	 */
	boolean isLockedSlot(IPortSlot slot);
}
