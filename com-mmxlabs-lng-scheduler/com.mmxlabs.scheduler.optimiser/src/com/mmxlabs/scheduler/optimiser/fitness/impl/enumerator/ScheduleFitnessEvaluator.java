/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.schedule.ScheduleCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * The {@link ScheduleFitnessEvaluator} evaluates the linear fitness (using {@link ICargoSchedulerFitnessComponent}s) of a {@link ScheduledSequences} after the {@link ScheduleCalculator} has processed
 * it. It is intended to be used within a {@link ISequenceScheduler} to compare one {@link ScheduledSequences} to another one.
 */
public class ScheduleFitnessEvaluator {

	private Collection<ICargoSchedulerFitnessComponent> fitnessComponents;

	public long evaluateSchedule(final ISequences sequences, final ProfitAndLossSequences scheduledSequences) {

		// Evaluate fitness components
		final long[] fitnesses = new long[fitnessComponents.size()];

		if (!iterateSchedulerComponents(fitnessComponents, scheduledSequences, sequences.getUnusedElements(), fitnesses)) {
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
	 * @param unusedElements
	 * @param fitnesses
	 *            output parameter containing fitnesses, in the order the iterator provides the components
	 * @return
	 */
	private static boolean iterateSchedulerComponents(@NonNull final Iterable<ICargoSchedulerFitnessComponent> components, @NonNull final ProfitAndLossSequences profitAndLossSequences,
			@NonNull List<ISequenceElement> unusedElements, final long[] fitnesses) {
		for (final ICargoSchedulerFitnessComponent component : components) {
			component.startEvaluation(profitAndLossSequences);
		}

		for (final VolumeAllocatedSequence sequence : profitAndLossSequences.getVolumeAllocatedSequences()) {
			if (!iterateSchedulerComponents(components, sequence)) {
				return false;
			}
		}

		for (final ICargoSchedulerFitnessComponent component : components) {
			if (!component.evaluateUnusedSlots(unusedElements, profitAndLossSequences)) {
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
	 * @param scheduledSequence
	 * @return
	 */
	private static boolean iterateSchedulerComponents(final Iterable<ICargoSchedulerFitnessComponent> components, final VolumeAllocatedSequence scheduledSequence) {

		final VoyagePlanIterator vpItr = new VoyagePlanIterator(scheduledSequence);

		for (final ICargoSchedulerFitnessComponent component : components) {
			component.startSequence(scheduledSequence.getResource());
		}

		if (vpItr.hasNextObject()) {
			final Object obj = vpItr.nextObject();
			assert obj != null;
			final int time = vpItr.getCurrentTime();
			final VoyagePlan plan = vpItr.getCurrentPlan();
			assert plan != null;
			for (final ICargoSchedulerFitnessComponent component : components) {
				if (!component.nextVoyagePlan(plan, time)) {
					return false;
				}
				if (!component.nextObject(obj, time)) {
					return false;
				}
			}
		}

		while (vpItr.hasNextObject()) {
			if (vpItr.nextObjectIsStartOfPlan()) {
				final Object obj = vpItr.nextObject();
				assert obj != null;

				final int time = vpItr.getCurrentTime();
				final VoyagePlan plan = vpItr.getCurrentPlan();
				assert plan != null;

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
				assert obj != null;
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
