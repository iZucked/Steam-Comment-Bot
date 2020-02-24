/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

@NonNullByDefault
public interface ICounterPartyVolumeProviderEditor extends ICounterPartyVolumeProvider {
	void setCounterPartyVolume(IPortSlot slot);
}
