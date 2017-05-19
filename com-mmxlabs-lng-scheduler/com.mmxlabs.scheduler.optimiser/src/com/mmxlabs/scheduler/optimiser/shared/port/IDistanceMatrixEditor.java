/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.shared.port;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;

/**
 * Interface extending {@link IDistanceMatrixProvider} to allow setting of values.
 * 
 * @author Simon Goodall
 */
@NonNullByDefault
public interface IDistanceMatrixEditor extends IDistanceMatrixProvider {

	/**
	 * Set the route distance.
	 */
	void set(ERouteOption route, IPort from, IPort to, int distance);

	/**
	 * Optionally set a pre-defined ordered list of usable routes.
	 * 
	 * @param preSortedKeys
	 */
	void setPreSortedRoutes(@Nullable ERouteOption[] preSortedKeys);

	/**
	 * Allow pre-allocation of memory for this number of ports
	 * 
	 * @param minSize
	 */
	void ensureCapacity(int minSize);
}
