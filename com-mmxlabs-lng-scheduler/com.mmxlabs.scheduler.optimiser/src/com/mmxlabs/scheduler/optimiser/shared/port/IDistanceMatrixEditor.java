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
	 * 
	 * @param x
	 * @param y
	 * @param v
	 */
	void set(ERouteOption route, IPort from, IPort to, int distance);

	void setPreSortedRoutes(@Nullable ERouteOption[] preSortedKeys);

	void ensureCapacity(int minSize);
}
