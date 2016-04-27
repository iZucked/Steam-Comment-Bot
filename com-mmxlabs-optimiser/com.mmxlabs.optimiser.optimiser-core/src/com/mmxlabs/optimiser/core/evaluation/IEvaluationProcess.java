/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.evaluation;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequences;

public interface IEvaluationProcess {

	enum Phase {
		Checked_Evaluation, Final_Evaluation
	}

	/**
	 * Evaluate the current {@link ISequences} and store results in {@link IEvaluationState}.
	 * 
	 * @param sequences
	 * @param evaluationState
	 * @return Return false if {@link ISequences} is invalid in some way
	 */
	boolean evaluate(@NonNull Phase phase, @NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState);

	void annotate(@NonNull Phase phase, @NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState, @NonNull IAnnotatedSolution solution);

	default boolean evaluate(@NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState) {
		return evaluate(Phase.Checked_Evaluation, fullSequences, evaluationState) && evaluate(Phase.Final_Evaluation, fullSequences, evaluationState);
	}

	default void annotate(@NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState, @NonNull IAnnotatedSolution solution) {
		annotate(Phase.Checked_Evaluation, fullSequences, evaluationState, solution);
		annotate(Phase.Final_Evaluation, fullSequences, evaluationState, solution);
	}

}
