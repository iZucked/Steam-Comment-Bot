/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.optimiser.lso.parallellso;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.moves.IMove;

public class AbstractLSOJobState implements ILSOJobState{
	
	protected transient ISequences rawSequences = null;
	protected transient ISequences fullSequences = null;
	private long seed;
	protected IEvaluationState evaluationState;
	private LSOJobStatus status;
	private String note;
	private IMove move;
	private Object failedChecker;
	
	public ISequences getRawSequences() {
		return rawSequences;
	}

	public void setRawSequences(ISequences rawSequences) {
		this.rawSequences = rawSequences;
	}

	public String getNote() {
		return note;
	}

	protected void setNote(String note) {
		this.note = note;
	}


	public long getSeed() {
		return seed;
	}


	public void setSeed(long seed) {
		this.seed = seed;
	}

	public LSOJobStatus getStatus() {
		return status;
	}

	public void setStatus(LSOJobStatus status) {
		this.status = status;
	}

	public IMove getMove() {
		return move;
	}

	public void setMove(IMove move) {
		this.move = move;
	}

	public Object getFailedChecker() {
		return failedChecker;
	}

	public void setFailedChecker(Object failedChecker) {
		this.failedChecker = failedChecker;
	}

}
