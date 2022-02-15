/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.multiobjective.impl;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.impl.AbstractLSOJobState;
import com.mmxlabs.optimiser.lso.impl.LSOJobStatus;

public class MultiObjectiveJobState extends AbstractLSOJobState {
	protected long[] fitness;

	public MultiObjectiveJobState(final ISequences rawSequences, final ISequences fullSequences, long[] fitness, LSOJobStatus status, IEvaluationState evaluationState, long seed, IMove move,
			Object failedChecker) {
		this.rawSequences = rawSequences;
		this.fullSequences = fullSequences;
		this.fitness = fitness;
		this.status = status;
		this.evaluationState = evaluationState;
		this.move = move;
		this.failedChecker = failedChecker;
		this.seed = seed;
	}

	public long[] getFitness() {
		return fitness;
	}
}