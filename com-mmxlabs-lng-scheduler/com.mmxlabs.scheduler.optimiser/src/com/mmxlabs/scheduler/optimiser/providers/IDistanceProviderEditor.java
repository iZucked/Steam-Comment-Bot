/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IPort;

public interface IDistanceProviderEditor extends IDistanceProvider {

	void setEntryPointsForRouteOption(ERouteOption route, Set<IPort> entryPoints);	
}