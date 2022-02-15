/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.moves.IMove;

public class AbstractLSOJobState implements ILSOJobState {

	protected ISequences rawSequences = null;
	protected ISequences fullSequences = null;
	protected long seed;
	protected IEvaluationState evaluationState;
	protected LSOJobStatus status;
	protected String note;
	protected IMove move;
	protected Object failedChecker;

	public ISequences getRawSequences() {
		return rawSequences;
	}

	public String getNote() {
		return note;
	}

	public long getSeed() {
		return seed;
	}

	public LSOJobStatus getStatus() {
		return status;
	}

	public IMove getMove() {
		return move;
	}

	public Object getFailedChecker() {
		return failedChecker;
	}

	public ISequences getFullSequences() {
		return fullSequences;
	}

	public IEvaluationState getEvaluationState() {
		return evaluationState;
	}

}
