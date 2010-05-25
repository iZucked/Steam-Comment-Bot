package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.Collection;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.scenario.IOptimisationData;

public interface ICargoSchedulerFitnessComponent<T> extends IFitnessComponent<T> {

	void init(IOptimisationData<T> data);
	
	void evaluateSequence(final IResource resource,
			final ISequence<T> sequence, final ISequenceScheduler<T> scheduler,boolean newSequence);

	void accepted(ISequences<T> sequences,
			Collection<IResource> affectedResources);

	/**
	 * Result old fitness
	 */
	void prepare();

	void dispose();

	void complete();
}
