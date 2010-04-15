package com.mmxlabs.optimiser.impl;

import java.util.List;

import com.mmxlabs.optimiser.IOptimisationContext;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.scenario.IOptimisationData;

/**
 * Implementation of {@link IOptimisationContext}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class OptimisationContext<T> implements IOptimisationContext<T> {

	private final ISequences<T> initialSequences;

	private final List<IFitnessComponent<T>> fitnessComponents;

	private final IOptimisationData<T> optimisationData;

	public OptimisationContext(IOptimisationData<T> optimisationData,
			final List<IFitnessComponent<T>> fitnessComponents,
			final ISequences<T> initialSequences) {
		this.optimisationData = optimisationData;
		this.initialSequences = initialSequences;
		this.fitnessComponents = fitnessComponents;
	}

	@Override
	public List<IFitnessComponent<T>> getFitnessComponents() {
		return fitnessComponents;
	}

	@Override
	public ISequences<T> getInitialSequences() {
		return initialSequences;
	}

	@Override
	public IOptimisationData<T> getOptimisationData() {
		return optimisationData;
	}
}
