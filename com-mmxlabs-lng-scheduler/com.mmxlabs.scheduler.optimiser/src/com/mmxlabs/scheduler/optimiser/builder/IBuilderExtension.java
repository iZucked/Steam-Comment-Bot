/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.builder;

import java.util.Collection;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * Extension to the scheduler builder interface, which {@link ISchedulerBuilder} instances should tie into the build process at appropriate times.
 * 
 * @author hinton
 * 
 */
public interface IBuilderExtension {
	/**
	 * This will be called after the SchedulerBuilder has finished, but before it outputs the optimisationData. It provides an opportunity to create and add any extra DCPs that may be required by
	 * plug-in parts of the optimization.
	 * 
	 * @param optimisationData
	 *            the data being created
	 * @return a collection of key / dcp pairs to be added.
	 */
	Collection<Pair<String, IDataComponentProvider>> createDataComponentProviders(IOptimisationData optimisationData);

	void dispose();

	/**
	 * This will be called at the very end of the process, after {@link #createDataComponentProviders(IOptimisationData)} has been called on every extension.
	 * 
	 * At this point any last-minute hooking up can be done.
	 * 
	 * @param optimisationData
	 */
	void finishBuilding(IOptimisationData optimisationData);
}
