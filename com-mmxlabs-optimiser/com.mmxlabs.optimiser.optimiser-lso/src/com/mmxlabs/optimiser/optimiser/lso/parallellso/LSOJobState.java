/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.optimiser.lso.parallellso;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.moves.IMove;

/**
 * The current state of a particular track of search in the LSO.
 * 
 * @author Alex
 *
 */
public class LSOJobState extends AbstractLSOJobState {
	
	protected long fitness;
	
	public LSOJobState(final ISequences rawSequences, final ISequences fullSequences, long fitness, LSOJobStatus status, IEvaluationState evaluationState, long seed, IMove move, Object failedChecker) {
		this.rawSequences = rawSequences;
		this.fullSequences = fullSequences;
		this.fitness = fitness;
		this.setStatus(status);
		this.evaluationState = evaluationState;
		this.setFailedChecker(failedChecker);
		this.setMove(move);
		this.setSeed(seed);
	}
}