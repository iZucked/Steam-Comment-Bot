/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.Collection;

import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

public interface ISchedulerFactory<T> {
	public ISequenceScheduler<T> createScheduler(IOptimisationData<T> data, 
			Collection<ICargoSchedulerFitnessComponent<T>> components);
}
