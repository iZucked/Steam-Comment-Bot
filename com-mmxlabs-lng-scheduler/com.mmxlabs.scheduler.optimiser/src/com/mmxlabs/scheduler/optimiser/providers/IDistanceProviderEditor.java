/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IPort;

@NonNullByDefault
public interface IDistanceProviderEditor extends IDistanceProvider {

	void setEntryPointsForRouteOption(ERouteOption route, IPort northEntrance, IPort southEntrance);

	void setCanalDistance(ERouteOption route, int distance);
}