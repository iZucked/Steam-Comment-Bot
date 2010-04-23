package com.mmxlabs.optimiser.impl;

import java.util.List;

import com.mmxlabs.optimiser.IOptimisationContext;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.constraints.IConstraintCheckerRegistry;
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

	private final IOptimisationData<T> optimisationData;
	private final ISequences<T> initialSequences;

	private final List<String> fitnessComponents;

	private final IFitnessFunctionRegistry fitnessFunctionRegistry;

	private final List<String> constraintCheckers;

	private final IConstraintCheckerRegistry constraintCheckerRegistry;

	public OptimisationContext(final IOptimisationData<T> optimisationData,
			final ISequences<T> initialSequences,
			final List<String> fitnessComponents,
			final IFitnessFunctionRegistry fitnessFunctionRegistry,
			final List<String> constraintCheckers,
			final IConstraintCheckerRegistry constraintCheckerRegistry) {
		this.optimisationData = optimisationData;
		this.initialSequences = initialSequences;
		this.fitnessComponents = fitnessComponents;
		this.fitnessFunctionRegistry = fitnessFunctionRegistry;
		this.constraintCheckers = constraintCheckers;
		this.constraintCheckerRegistry = constraintCheckerRegistry;
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

	@Override
	public IConstraintCheckerRegistry getConstraintCheckerRegistry() {
		return constraintCheckerRegistry;
	}

	@Override
	public List<String> getConstraintCheckers() {
		return constraintCheckers;
	}
}
