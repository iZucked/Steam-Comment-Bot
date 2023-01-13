/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;

@NonNullByDefault
public interface IScheduledPurgeProviderEditor extends IScheduledPurgeProvider {

	void setPurgeScheduled(ISequenceElement element, ILoadSlot slot);
}
