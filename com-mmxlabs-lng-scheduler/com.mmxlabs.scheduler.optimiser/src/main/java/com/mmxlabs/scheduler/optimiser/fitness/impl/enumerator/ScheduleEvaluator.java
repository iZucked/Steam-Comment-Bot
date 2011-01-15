/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import java.util.Collection;

import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;

/**
 * This is similar to the IndividualEvaluator, but just uses a schedule The IE
 * could probably be refactored to use this, but it's a bit tangled at the
 * moment
 * 
 * (C) Minimax labs inc. 2010
 * 
 * @author hinton
 * 
 * @param <T>
 */
public class ScheduleEvaluator<T> {
	private VoyagePlanIterator<T> vpIterator = new VoyagePlanIterator<T>();

	private Collection<ICargoSchedulerFitnessComponent<T>> fitnessComponents;
	private long[] fitnesses;

	public long evaluateSchedule(final ScheduledSequences scheduledSequences) {
		if (!vpIterator.iterateSchedulerComponents(fitnessComponents,
				scheduledSequences, fitnesses)) {
			return Long.MAX_VALUE;
		}

		long total = 0;
		for (long l : fitnesses) {
			if (l == Long.MAX_VALUE) return Long.MAX_VALUE;
			total += l;
		}
		

		return total;
	}

	public Collection<ICargoSchedulerFitnessComponent<T>> getFitnessComponents() {
		return fitnessComponents;
	}

	public void setFitnessComponents(
			Collection<ICargoSchedulerFitnessComponent<T>> fitnessComponents) {
		this.fitnessComponents = fitnessComponents;
		this.fitnesses = new long[fitnessComponents.size()];
	}
}
