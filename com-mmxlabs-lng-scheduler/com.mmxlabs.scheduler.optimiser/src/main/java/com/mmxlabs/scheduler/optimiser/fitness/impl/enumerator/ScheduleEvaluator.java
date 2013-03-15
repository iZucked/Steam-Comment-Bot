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
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.schedule.ScheduleCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * This is similar to the IndividualEvaluator, but just uses a schedule The IE could probably be refactored to use this, but it's a bit tangled at the moment
 * 
 * (C) Minimax labs inc. 2010
 * 
 * @author hinton
 * 
 * @param
 */
public class ScheduleEvaluator {

	@Inject
	private ScheduleCalculator scheduleCalculator;

	@Inject
	private VoyagePlanIterator vpIterator;

	private Collection<ICargoSchedulerFitnessComponent> fitnessComponents;

	private long[] fitnesses;

	public long evaluateSchedule(final ISequences sequences, final ScheduledSequences scheduledSequences, final IAnnotatedSolution solution) {

		scheduleCalculator.calculateSchedule(sequences, scheduledSequences, solution);

		if (!iterateSchedulerComponents(vpIterator, fitnessComponents, scheduledSequences, fitnesses)) {
			return Long.MAX_VALUE;
		}

		long total = 0;
		for (final long l : fitnesses) {
			if (l == Long.MAX_VALUE) {
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
		this.fitnesses = new long[fitnessComponents.size()];
	}

	/**
	 * Iterate a bunch of scheduler components
	 * 
	 * @param components
	 * @param sequences
	 */
	public static boolean iterateSchedulerComponents(final VoyagePlanIterator vpItr, final Iterable<ICargoSchedulerFitnessComponent> components, final ScheduledSequences sequences) {

		if (sequences == null) {
			return false;
		}

		for (final ICargoSchedulerFitnessComponent component : components) {
			component.startEvaluation();
		}

		for (final ScheduledSequence sequence : sequences) {
			if (!iterateSchedulerComponents(vpItr, components, sequence)) {
				return false;
			}
		}

		for (final ICargoSchedulerFitnessComponent component : components) {
			component.endEvaluationAndGetCost();
		}

		return true;
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
	public static boolean iterateSchedulerComponents(final VoyagePlanIterator vpItr, final Iterable<ICargoSchedulerFitnessComponent> components, final ScheduledSequences sequences,
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
	public static boolean iterateSchedulerComponents(final VoyagePlanIterator vpItr, final Iterable<ICargoSchedulerFitnessComponent> components, final ScheduledSequence sequence) {

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
