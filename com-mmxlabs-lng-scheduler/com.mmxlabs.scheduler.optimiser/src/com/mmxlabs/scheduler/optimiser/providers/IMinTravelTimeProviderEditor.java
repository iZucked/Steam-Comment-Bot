/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

@NonNullByDefault
public interface IMinTravelTimeProviderEditor extends IMinTravelTimeProvider {

	void setMinTravelTime(IPortSlot slot, int minTravelTime);
}
