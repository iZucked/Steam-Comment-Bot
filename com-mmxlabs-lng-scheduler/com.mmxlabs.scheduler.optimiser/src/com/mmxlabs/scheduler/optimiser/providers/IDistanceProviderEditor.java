/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

public interface IDistanceProviderEditor extends IDistanceProvider {

	/**
	 * Returns the time the route is available from. Returns {@link Integer#MIN_VALUE} if always open.
	 * 
	 * @param route
	 * @return
	 */
	void setRouteAvailableFrom(@NonNull ERouteOption route, int availableFrom);
}
