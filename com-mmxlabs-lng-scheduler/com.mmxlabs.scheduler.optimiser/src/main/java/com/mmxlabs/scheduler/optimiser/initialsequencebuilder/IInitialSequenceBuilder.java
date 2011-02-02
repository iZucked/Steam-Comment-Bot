/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.initialsequencebuilder;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * Implementations should be able to create some sensible initial sequences from a given {@link IOptimisationData}.
 * In future, the interface should be extended to include "advice" from the scenario, so that the schedule can be partially specified
 * already.
 * @author hinton
 *
 * @param <T>
 */
public interface IInitialSequenceBuilder<T> {
	/**
	 * Create some initial sequences
	 * 
	 * @param data the optimisation data
	 * @param suggestion a suggested (possibly incomplete) start state. May be null.
	 * @return
	 */
	public ISequences<T> createInitialSequences(IOptimisationData<T> data, ISequences<T> suggestion);
}
