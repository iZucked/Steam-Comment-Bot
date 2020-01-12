/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;

public interface IBaseFuelProvider extends IDataComponentProvider {

	@NonNull
	Collection<@NonNull IBaseFuel> getBaseFuels();

	int getNumberOfBaseFuels();
}
