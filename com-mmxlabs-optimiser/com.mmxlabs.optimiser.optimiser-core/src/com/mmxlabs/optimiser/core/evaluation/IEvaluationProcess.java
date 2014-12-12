/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.evaluation;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequences;

public interface IEvaluationProcess {

	/**
	 * Evaluate the current {@link ISequences} and store results in {@link IEvaluationState}.
	 * 
	 * @param sequences
	 * @param evaluationState
	 * @return Return false is {@link ISequences} is invalid in some way
	 */
	boolean evaluate(@NonNull ISequences sequences, @NonNull IEvaluationState evaluationState);

	void annotate(@NonNull ISequences sequencces, @NonNull IEvaluationState evaluationState, @NonNull IAnnotatedSolution solution);

}
