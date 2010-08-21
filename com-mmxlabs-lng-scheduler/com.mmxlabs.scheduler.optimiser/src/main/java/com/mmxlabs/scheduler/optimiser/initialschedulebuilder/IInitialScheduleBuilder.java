package com.mmxlabs.scheduler.optimiser.initialschedulebuilder;

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
public interface IInitialScheduleBuilder<T> {
	public ISequences<T> createInitialSequences(IOptimisationData<T> data);
}
