/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IEvaluationContext;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;

/**
 * Implementation of {@link IOptimisationContext}.
 * 
 * @author Simon Goodall
 */
public final class EvaluationContext implements IEvaluationContext {

	@NonNull
	private final IPhaseOptimisationData optimisationData;

	@NonNull
	private final ISequences initialSequences;

	@NonNull
	private final List<@NonNull String> evaluationProcesses;

	@NonNull
	private final IEvaluationProcessRegistry evaluationProcessRegistry;

	public EvaluationContext(@NonNull final IPhaseOptimisationData optimisationData, @NonNull final ISequences initialSequences, @NonNull final List<@NonNull String> evaluationProcesses,
			@NonNull final IEvaluationProcessRegistry evaluationProcessRegistry) {
		this.optimisationData = optimisationData;
		this.initialSequences = initialSequences;
		this.evaluationProcesses = evaluationProcesses;
		this.evaluationProcessRegistry = evaluationProcessRegistry;
	}

	@Override
	@NonNull
	public ISequences getInputSequences() {
		return initialSequences;
	}

//	@Override
//	@NonNull
//	public IPhaseOptimisationData getOptimisationData() {
//		return optimisationData;
//	}

	@Override
	@NonNull
	public IEvaluationProcessRegistry getEvaluationProcessRegistry() {
		return evaluationProcessRegistry;
	}

	@Override
	@NonNull
	public List<@NonNull String> getEvaluationProcesses() {
		return evaluationProcesses;
	}
}
