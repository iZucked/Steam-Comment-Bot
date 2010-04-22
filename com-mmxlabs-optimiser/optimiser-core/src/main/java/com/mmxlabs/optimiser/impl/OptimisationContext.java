package com.mmxlabs.optimiser.impl;

import java.util.List;

import com.mmxlabs.optimiser.IOptimisationContext;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.fitness.IFitnessFunctionRegistry;
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

	private final List<String> fitnessComponents;

	private final IOptimisationData<T> optimisationData;

	private final IFitnessFunctionRegistry fitnessFunctionRegistry;

	public OptimisationContext(final IOptimisationData<T> optimisationData,
			final List<String> fitnessComponents,
			final ISequences<T> initialSequences,
			final IFitnessFunctionRegistry fitnessFunctionRegistry) {
		this.optimisationData = optimisationData;
		this.initialSequences = initialSequences;
		this.fitnessComponents = fitnessComponents;
		this.fitnessFunctionRegistry = fitnessFunctionRegistry;
	}

	@Override
	public List<String> getFitnessComponents() {
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

	@Override
	public IFitnessFunctionRegistry getFitnessFunctionRegistry() {
		return fitnessFunctionRegistry;
	}
}
