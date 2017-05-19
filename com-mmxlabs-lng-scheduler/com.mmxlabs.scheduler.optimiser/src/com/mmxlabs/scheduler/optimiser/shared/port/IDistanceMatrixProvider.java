/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.shared.port;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;

/**
 * Interface defining a set of distance matrices
 * 
 * @author Simon Goodall
 * 
 */
@NonNullByDefault
public interface IDistanceMatrixProvider extends IDataComponentProvider {

	/**
	 * Return the distance between the two ports. Returns {@link Integer#MAX_VALUE} is there is no distance.
	 * 
	 */
	int get(ERouteOption route, IPort from, IPort to);

	/**
	 * Returns a new {@link List} of {@link DistanceMatrixEntry}s , one from each {@link ERouteOption}, for the given port pair with a valid distance. This may return an empty list. It is safe to
	 * modify the returned list.
	 * 
	 * Note: A new list is created for each call.
	 * 
	 */
	List<DistanceMatrixEntry> getValues(IPort from, IPort to);

	/**
	 * Returns the {@link DistanceMatrixEntry} with minimum distance between the port pair.
	 * 
	 */
	@Nullable
	DistanceMatrixEntry getMinimum(IPort from, IPort to);

	/**
	 * Returns the {@link DistanceMatrixEntry} with maximum distance between the port pair.
	 * 
	 */
	@Nullable
	DistanceMatrixEntry getMaximum(IPort from, IPort to);

	/**
	 * Returns the shortest distance between these two ports or Integer.MAX_VALUE if there is no distance.
	 * 
	 */
	int getMinimumValue(IPort from, IPort to);

	/**
	 * Returns the longest distance between these two ports or Integer.MAX_VALUE if there is no distance.
	 * 
	 */
	int getMaximumValue(IPort from, IPort to);

	/**
	 * Returns an array of usable {@link ERouteOption}s.
	 * 
	 * @return
	 */
	ERouteOption[] getRoutes();
}
