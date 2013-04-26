/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import java.util.Collection;

import javax.inject.Inject;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.schedule.ScheduleCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * The {@link ScheduleFitnessEvaluator} evaluates the linear fitness (using {@link ICargoSchedulerFitnessComponent}s) of a {@link ScheduledSequences} after the {@link ScheduleCalculator} has processed
 * it. It is intended to be used within a {@link ISequenceScheduler} to compare one {@link ScheduledSequences} to another one.
 */
public class ScheduleFitnessEvaluator {

	@Inject
	private ScheduleCalculator scheduleCalculator;

	@Inject
	private VoyagePlanIterator vpIterator;

	private Collection<ICargoSchedulerFitnessComponent> fitnessComponents;

	public long evaluateSchedule(final ISequences sequences, final ScheduledSequences scheduledSequences, final IAnnotatedSolution solution) {

		// Process the schedule
		scheduleCalculator.calculateSchedule(sequences, scheduledSequences, solution);

		// Evaluate fitness components
		final long[] fitnesses = new long[fitnessComponents.size()];

		if (!iterateSchedulerComponents(vpIterator, fitnessComponents, scheduledSequences, fitnesses)) {
			return Long.MAX_VALUE;
		}

		long total = 0;
		for (final long l : fitnesses) {
			if (l == Long.MAX_VALUE) {
				// Abort!
				return Long.MAX_VALUE;
			}
			total += l;
		}

		return total;
	}

	public Collection<ICargoSchedulerFitnessComponent> getFitnessComponents() {
		return fitnessComponents;
	}

	public void setFitnessComponents(final Collection<ICargoSchedulerFitnessComponent> fitnessComponents) {
		this.fitnessComponents = fitnessComponents;
	}

	/**
	 * Iterate the given scheduler components, and copy the resulting fitness values into the fitnesses array provided (the last parameter)
	 * 
	 * @param components
	 * @param sequences
	 * @param fitnesses
	 *            output parameter containing fitnesses, in the order the iterator provides the components
	 * @return
	 */
	private static boolean iterateSchedulerComponents(final VoyagePlanIterator vpItr, final Iterable<ICargoSchedulerFitnessComponent> components, final ScheduledSequences sequences,
			final long[] fitnesses) {
		for (final ICargoSchedulerFitnessComponent component : components) {
			component.startEvaluation();
		}

		for (final ScheduledSequence sequence : sequences) {
			if (!iterateSchedulerComponents(vpItr, components, sequence)) {
				return false;
			}
		}

		int i = 0;
		for (final ICargoSchedulerFitnessComponent component : components) {
			fitnesses[i++] = component.endEvaluationAndGetCost();
		}

		return true;
	}

	/**
	 * Iterate through the given sequence, with the given components. Assumes start/end evaluation process has been done.
	 * 
	 * @param components
	 * @param sequence
	 * @return
	 */
	private static boolean iterateSchedulerComponents(final VoyagePlanIterator vpItr, final Iterable<ICargoSchedulerFitnessComponent> components, final ScheduledSequence sequence) {

		for (final ICargoSchedulerFitnessComponent component : components) {
			component.startSequence(sequence.getResource());
		}

		vpItr.setVoyagePlans(sequence.getResource(), sequence.getVoyagePlans(), sequence.getArrivalTimes());

		while (vpItr.hasNextObject()) {
			if (vpItr.nextObjectIsStartOfPlan()) {
				final Object obj = vpItr.nextObject();
				final int time = vpItr.getCurrentTime();
				final VoyagePlan plan = vpItr.getCurrentPlan();

				for (final ICargoSchedulerFitnessComponent component : components) {
					if (!component.nextVoyagePlan(plan, time)) {
						return false;
					}
					if (!component.nextObject(obj, time)) {
						return false;
					}
				}
			} else {
				final Object obj = vpItr.nextObject();
				final int time = vpItr.getCurrentTime();
				for (final ICargoSchedulerFitnessComponent component : components) {
					if (!component.nextObject(obj, time)) {
						return false;
					}
				}
			}

		}

		for (final ICargoSchedulerFitnessComponent component : components) {
			if (!component.endSequence()) {
				return false;
			}
		}

		return true;
	}
}
