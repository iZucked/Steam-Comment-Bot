/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IPort;

@NonNullByDefault
public interface IDistanceProviderEditor extends IDistanceProvider {

	void setEntryPointsForRouteOption(ERouteOption route, IPort northEntrance, IPort southEntrance);
}