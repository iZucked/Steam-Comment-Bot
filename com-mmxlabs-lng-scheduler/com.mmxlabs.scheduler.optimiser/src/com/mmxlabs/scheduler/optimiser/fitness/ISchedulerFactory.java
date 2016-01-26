/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.Collection;

import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

public interface ISchedulerFactory {
	public ISequenceScheduler createScheduler(IOptimisationData data, Collection<ICargoSchedulerFitnessComponent> schedulerComponents);
}
