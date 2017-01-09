/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 */
public interface IExcessIdleTimeConstrainedSlotProvider {

	/**
	 * Check whether the idle time for this cargo should be constrained
	 * 
	 * @param slot
	 * @return
	 */
	@NonNull
	eIdleDetails getSlotIdleConstraintDetails(@NonNull final ILoadSlot load, @NonNull final IDischargeOption discharge, @NonNull IPortSlot returnSlot);

	public enum eIdleDetails {
		NONE, LADEN, BALLAST, BOTH
	}

}
