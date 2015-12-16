/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

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

	@NonNull
	private final IOptimisationData optimisationData;

	@NonNull
	private final ISequences initialSequences;

	@NonNull
	private final List<String> fitnessComponents;

	@NonNull
	private final IFitnessFunctionRegistry fitnessFunctionRegistry;

	@NonNull
	private final List<String> constraintCheckers;

	@NonNull
	private final IConstraintCheckerRegistry constraintCheckerRegistry;

	@NonNull
	private final List<String> evaluationProcesses;

	@NonNull
	private final IEvaluationProcessRegistry evaluationProcessRegistry;

	public OptimisationContext(@NonNull final IOptimisationData optimisationData, @NonNull final ISequences initialSequences, @NonNull final List<String> fitnessComponents,
			@NonNull final IFitnessFunctionRegistry fitnessFunctionRegistry, @NonNull final List<String> constraintCheckers, @NonNull final IConstraintCheckerRegistry constraintCheckerRegistry,
			@NonNull final List<String> evaluationProcesses, @NonNull final IEvaluationProcessRegistry evaluationProcessRegistry) {
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
	@NonNull
	public List<String> getFitnessComponents() {
		return fitnessComponents;
	}

	@Override
	@NonNull
	public ISequences getInputSequences() {
		return initialSequences;
	}

	@Override
	@NonNull
	public IOptimisationData getOptimisationData() {
		return optimisationData;
	}

	@Override
	@NonNull
	public IFitnessFunctionRegistry getFitnessFunctionRegistry() {
		return fitnessFunctionRegistry;
	}

	@Override
	@NonNull
	public IConstraintCheckerRegistry getConstraintCheckerRegistry() {
		return constraintCheckerRegistry;
	}

	@Override
	@NonNull
	public List<String> getConstraintCheckers() {
		return constraintCheckers;
	}

	@Override
	@NonNull
	public IEvaluationProcessRegistry getEvaluationProcessRegistry() {
		return evaluationProcessRegistry;
	}

	@Override
	@NonNull
	public List<String> getEvaluationProcesses() {
		return evaluationProcesses;
	}
}
