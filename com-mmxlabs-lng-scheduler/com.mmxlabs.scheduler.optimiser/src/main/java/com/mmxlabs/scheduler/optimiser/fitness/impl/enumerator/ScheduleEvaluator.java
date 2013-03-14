/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import javax.inject.Inject;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoAllocationFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IGeneratedCharterOutEvaluator;
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
	private ICargoAllocator cargoAllocator;

	@Inject
	private ICalculatorProvider calculatorProvider;

	@com.google.inject.Inject(optional = true)
	private IGeneratedCharterOutEvaluator generatedCharterOutEvaluator;

	@com.google.inject.Inject(optional = true)
	private IBreakEvenEvaluator breakEvenEvaluator;

	@Inject
	private VoyagePlanIterator vpIterator;

	private Collection<ILoadPriceCalculator> loadPriceCalculators;
	private Collection<ICargoSchedulerFitnessComponent> fitnessComponents;
	private Collection<ICargoAllocationFitnessComponent> allocationComponents;

	private long[] fitnesses;

	/**
	 * @since 2.0
	 */
	@Inject
	public void init() {
		setLoadPriceCalculators(calculatorProvider.getLoadPriceCalculators());
	}

	public long evaluateSchedule(final ScheduledSequences scheduledSequences, final int[] changedSequences) {
		// First, we evaluate all the scheduler components. These are fitness components dependent only on
		// values from the ScheduledSequences.

		final HashSet<IResource> affectedResources = new HashSet<IResource>();
		for (final int index : changedSequences) {
			affectedResources.add(scheduledSequences.get(index).getResource());
		}
		if (!iterateSchedulerComponents(vpIterator, fitnessComponents, scheduledSequences, affectedResources, fitnesses)) {
			return Long.MAX_VALUE;
		}

		long total = 0;
		for (final long l : fitnesses) {
			if (l == Long.MAX_VALUE) {
				return Long.MAX_VALUE;
			}
			total += l;
		}

		// Prime the load price calculators with the scheduled result
		for (final ILoadPriceCalculator calculator : loadPriceCalculators) {
			calculator.prepareEvaluation(scheduledSequences);
		}

		// Execute custom logic to manipulate the schedule and choices
		if (breakEvenEvaluator != null) {
			breakEvenEvaluator.processSchedule(scheduledSequences);
		}

		if (generatedCharterOutEvaluator != null) {
			generatedCharterOutEvaluator.processSchedule(scheduledSequences);
		}

		// Next we do P&L related business; first we have to assign the load volumes,
		// and then compute the resulting P&L fitness components.

		// Compute load volumes and prices
		final Map<VoyagePlan, IAllocationAnnotation> allocations = cargoAllocator.allocate(scheduledSequences);
		scheduledSequences.setAllocations(allocations);
		// Finally evaluate whole-solution components, like P&L.

		for (final ICargoAllocationFitnessComponent component : allocationComponents) {
			final long l = component.evaluate(scheduledSequences, allocations);
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

	public void setCargoAllocator(final ICargoAllocator cargoAllocator) {
		this.cargoAllocator = cargoAllocator;
	}

	public void setFitnessComponents(final Collection<ICargoSchedulerFitnessComponent> fitnessComponents, final Collection<ICargoAllocationFitnessComponent> allocationComponents) {
		this.fitnessComponents = fitnessComponents;
		this.fitnesses = new long[fitnessComponents.size()];
		this.allocationComponents = allocationComponents;
	}

	public void setLoadPriceCalculators(final Collection<ILoadPriceCalculator> loadPriceCalculators) {
		this.loadPriceCalculators = loadPriceCalculators;
	}

	public void evaluateSchedule(final ScheduledSequences bestResult) {
		final HashSet<IResource> allResources = new HashSet<IResource>();
		for (final ScheduledSequence schedule : bestResult) {
			allResources.add(schedule.getResource());
		}
		iterateSchedulerComponents(vpIterator, fitnessComponents, bestResult, allResources);
	}

	/**
	 * Iterate a bunch of scheduler components
	 * 
	 * @param components
	 * @param sequences
	 */
	public static boolean iterateSchedulerComponents(VoyagePlanIterator vpItr, final Iterable<ICargoSchedulerFitnessComponent> components, final ScheduledSequences sequences,
			final Collection<IResource> affectedResources) {

		if (sequences == null) {
			return false;
		}

		for (final ICargoSchedulerFitnessComponent component : components) {
			component.startEvaluation();
		}

		for (final ScheduledSequence sequence : sequences) {
			// TODO work out why this makes for problems.
			if (!iterateSchedulerComponents(vpItr, components, sequence, affectedResources.contains(sequence.getResource()))) {
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
	public static boolean iterateSchedulerComponents(VoyagePlanIterator vpItr, final Iterable<ICargoSchedulerFitnessComponent> components, final ScheduledSequences sequences,
			final Collection<IResource> affectedResources, final long[] fitnesses) {
		for (final ICargoSchedulerFitnessComponent component : components) {
			component.startEvaluation();
		}

		for (final ScheduledSequence sequence : sequences) {
			if (!iterateSchedulerComponents(vpItr, components, sequence, affectedResources.contains(sequence.getResource()))) {
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
	public static boolean iterateSchedulerComponents(VoyagePlanIterator vpItr, final Iterable<ICargoSchedulerFitnessComponent> components, final ScheduledSequence sequence,
			final boolean sequenceHasChanged) {

		for (final ICargoSchedulerFitnessComponent component : components) {
			/*
			 * Although the "true" here looks bad (always evaluate every sequence), we need to keep track somehow of which sequences have changed in the layer above. The random sequence scheduler may
			 * well change every sequence's schedule even if only one or two sequences' elements have been shifted by a move.
			 */
			component.startSequence(sequence.getResource(), sequenceHasChanged);
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

	/**
	 * Ask the components to annotate the given sequences.
	 * 
	 * @param components
	 * @param sequence
	 * @param annotatedSequence
	 */
	public static void annotateSchedulerComponents(VoyagePlanIterator vpItr, final Iterable<ICargoSchedulerFitnessComponent> components, final ScheduledSequences sequences,
			final IAnnotatedSolution annotatedSolution) {

		for (final ICargoSchedulerFitnessComponent component : components) {
			component.startEvaluation();
		}

		for (final ScheduledSequence sequence : sequences) {
			annotateSequence(vpItr, sequence, components, annotatedSolution);
		}

		for (final ICargoSchedulerFitnessComponent component : components) {
			component.endEvaluationAndAnnotate(annotatedSolution);
		}
	}

	/**
	 * @param sequence
	 * @param components
	 * @param annotatedSolution
	 */
	private static void annotateSequence(VoyagePlanIterator vpItr, final ScheduledSequence sequence, final Iterable<ICargoSchedulerFitnessComponent> components,
			final IAnnotatedSolution annotatedSolution) {
		for (final ICargoSchedulerFitnessComponent component : components) {
			component.startSequence(sequence.getResource(), true);
		}

		vpItr.setVoyagePlans(sequence.getResource(), sequence.getVoyagePlans(), sequence.getArrivalTimes());

		while (vpItr.hasNextObject()) {
			if (vpItr.nextObjectIsStartOfPlan()) {
				final Object obj = vpItr.nextObject();
				final int time = vpItr.getCurrentTime();
				final VoyagePlan plan = vpItr.getCurrentPlan();

				for (final ICargoSchedulerFitnessComponent component : components) {
					if (!component.nextVoyagePlan(plan, time)) {
						return;
					}
					if (!component.annotateNextObject(obj, time, annotatedSolution)) {
						return;
					}
				}
			} else {
				final Object obj = vpItr.nextObject();
				final int time = vpItr.getCurrentTime();
				for (final ICargoSchedulerFitnessComponent component : components) {
					if (!component.annotateNextObject(obj, time, annotatedSolution)) {
						return;
					}
				}
			}

		}

		for (final ICargoSchedulerFitnessComponent component : components) {
			if (!component.endSequence()) {
				return;
			}
		}
	}
}
