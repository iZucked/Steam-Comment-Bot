package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

public interface ITimedDistanceProviderEditor extends IDistanceProvider {

	/**
	 * Returns the time the route is available from. Returns {@link Integer#MIN_VALUE} if always open.
	 * 
	 * @param route
	 * @return
	 */
	void setRouteAvailableFrom(@NonNull String route, int availableFrom);
}
