/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;

public interface IBaseFuelProviderEditor extends IBaseFuelProvider {

	void registerBaseFuel(@NonNull IBaseFuel baseFuel);
}
