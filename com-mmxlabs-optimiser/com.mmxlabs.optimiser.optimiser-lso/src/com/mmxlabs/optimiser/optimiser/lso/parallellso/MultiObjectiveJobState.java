/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.optimiser.optimiser.lso.parallellso;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.moves.IMove;

public class MultiObjectiveJobState extends AbstractLSOJobState {
	protected long[] fitness;

	public MultiObjectiveJobState(final ISequences rawSequences, final ISequences fullSequences, long[] fitness, LSOJobStatus status, IEvaluationState evaluationState, long seed, IMove move, Object failedChecker) {
		this.rawSequences = rawSequences;
		this.fullSequences = fullSequences;
		this.fitness = fitness;
		this.setStatus(status);
		this.evaluationState = evaluationState;
		this.setMove(move);
		this.setFailedChecker(failedChecker);
		this.setSeed(seed);
	}
	
	public long[] getFitness() {
		return fitness;
	}
}