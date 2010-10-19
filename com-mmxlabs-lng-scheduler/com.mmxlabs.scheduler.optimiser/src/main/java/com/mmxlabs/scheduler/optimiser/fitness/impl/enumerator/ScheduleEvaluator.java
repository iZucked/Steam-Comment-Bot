/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * This is similar to the IndividualEvaluator, but just uses a schedule 
 * The IE could probably be refactored to use this, but it's a bit tangled at the moment
 *
 * (C) Minimax labs inc. 2010
 * 
 * @author hinton
 * 
 * @param <T>
 */
public class ScheduleEvaluator<T> {
	private IResource resource;
	private ISequence<T> sequence;
	
	private VoyagePlanIterator<T> vpIterator = new VoyagePlanIterator<T>();
	private ICargoSchedulerFitnessComponent<T>[] iteratingComponents;
	private Collection<ICargoSchedulerFitnessComponent<T>> fitnessComponents;
	
	public void setResourceAndSequence(final IResource resource, final ISequence<T> sequence) {
		this.resource = resource;
		this.sequence = sequence;
	}
	
	public long evaluateVoyagePlans(final Pair<Integer, List<VoyagePlan>> startAndPlans) {
		long totalFitness = 0;

		final int startTime = startAndPlans.getFirst().intValue();
		final List<VoyagePlan> plans = startAndPlans.getSecond();
		
		vpIterator.iterateComponents(plans, 
				startTime, resource, iteratingComponents);
		
		for (final ICargoSchedulerFitnessComponent<T> component : fitnessComponents) {
			final long rawFitness = component.rawEvaluateSequence(resource,
					sequence, plans, startTime);
			totalFitness += rawFitness;
		}
		
		return totalFitness;
	}

	public Collection<ICargoSchedulerFitnessComponent<T>> getFitnessComponents() {
		return fitnessComponents;
	}

	public void setFitnessComponents(
			Collection<ICargoSchedulerFitnessComponent<T>> fitnessComponents) {
		this.fitnessComponents = fitnessComponents;
		this.iteratingComponents = VoyagePlanIterator.filterIteratingComponents(fitnessComponents);
	}
}
