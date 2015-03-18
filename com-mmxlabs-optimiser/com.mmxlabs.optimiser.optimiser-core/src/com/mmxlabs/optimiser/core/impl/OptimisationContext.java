/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.List;

import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * Implementation of {@link IOptimisationContext}.
 * 
 * @author Simon Goodall
 */
public final class OptimisationContext implements IOptimisationContext {

	private final IOptimisationData optimisationData;
	private final ISequences initialSequences;

	private final List<String> fitnessComponents;

	private final IFitnessFunctionRegistry fitnessFunctionRegistry;

	private final List<String> constraintCheckers;

	private final IConstraintCheckerRegistry constraintCheckerRegistry;

	private final List<String> evaluationProcesses;

	private final IEvaluationProcessRegistry evaluationProcessRegistry;

	public OptimisationContext(final IOptimisationData optimisationData, final ISequences initialSequences, final List<String> fitnessComponents,
			final IFitnessFunctionRegistry fitnessFunctionRegistry, final List<String> constraintCheckers, final IConstraintCheckerRegistry constraintCheckerRegistry,
			final List<String> evaluationProcesses, final IEvaluationProcessRegistry evaluationProcessRegistry) {
		this.optimisationData = optimisationData;
		this.initialSequences = initialSequences;
		this.fitnessComponents = fitnessComponents;
		this.fitnessFunctionRegistry = fitnessFunctionRegistry;
		this.constraintCheckers = constraintCheckers;
		this.constraintCheckerRegistry = constraintCheckerRegistry;
		this.evaluationProcesses = evaluationProcesses;
		this.evaluationProcessRegistry = evaluationProcessRegistry;
	}

	@Override
	public List<String> getFitnessComponents() {
		return fitnessComponents;
	}

	@Override
	public ISequences getInitialSequences() {
		return initialSequences;
	}

	@Override
	public IOptimisationData getOptimisationData() {
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

	@Override
	public IEvaluationProcessRegistry getEvaluationProcessRegistry() {
		return evaluationProcessRegistry;
	}

	@Override
	public List<String> getEvaluationProcesses() {
		return evaluationProcesses;
	}
}
