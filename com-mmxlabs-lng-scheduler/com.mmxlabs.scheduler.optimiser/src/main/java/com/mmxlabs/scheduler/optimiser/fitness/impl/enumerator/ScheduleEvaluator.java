/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import java.util.Collection;
import java.util.HashSet;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoAllocationFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;

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

	private final VoyagePlanIterator vpIterator = new VoyagePlanIterator();

	private ICargoAllocator cargoAllocator;

	private Collection<ILoadPriceCalculator> loadPriceCalculators;
	private Collection<ICargoSchedulerFitnessComponent> fitnessComponents;

	private Collection<ICargoAllocationFitnessComponent> allocationComponents;

	private long[] fitnesses;

	public long evaluateSchedule(final ScheduledSequences scheduledSequences, final int[] changedSequences) {
		// First, we evaluate all the scheduler components. These are fitness components dependent only on
		// values from the ScheduledSequences.

		final HashSet<IResource> affectedResources = new HashSet<IResource>();
		for (final int index : changedSequences) {
			affectedResources.add(scheduledSequences.get(index).getResource());
		}
		if (!vpIterator.iterateSchedulerComponents(fitnessComponents, scheduledSequences, affectedResources, fitnesses)) {
			return Long.MAX_VALUE;
		}

		long total = 0;
		for (final long l : fitnesses) {
			if (l == Long.MAX_VALUE) {
				return Long.MAX_VALUE;
			}
			total += l;
		}

		// Next we do P&L related business; first we have to assign the load volumes,
		// and then compute the resulting P&L fitness components.

		// Prime the load price calculators with the scheduled result
		for (final ILoadPriceCalculator calculator : loadPriceCalculators) {
			calculator.prepareEvaluation(scheduledSequences);
		}

		// Compute load volumes and prices
		final Collection<IAllocationAnnotation> allocations = cargoAllocator.allocate(scheduledSequences);
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
		vpIterator.iterateSchedulerComponents(fitnessComponents, bestResult, allResources);
	}
}
