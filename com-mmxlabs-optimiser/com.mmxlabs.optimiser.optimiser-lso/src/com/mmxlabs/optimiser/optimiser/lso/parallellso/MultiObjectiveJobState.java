/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.optimiser.optimiser.lso.parallellso;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;

public class MultiObjectiveJobState extends AbstractLSOJobState {
	protected long[] fitness;

	public MultiObjectiveJobState(final ISequences rawSequences, final ISequences fullSequences, long[] fitness, LSOJobStatus status, IEvaluationState evaluationState, long seed, String note) {
		this.rawSequences = rawSequences;
		this.fullSequences = fullSequences;
		this.fitness = fitness;
		this.setStatus(status);
		this.evaluationState = evaluationState;
		this.setNote(note);
		this.setSeed(seed);
	}
	
	public long[] getFitness() {
		return fitness;
	}
}