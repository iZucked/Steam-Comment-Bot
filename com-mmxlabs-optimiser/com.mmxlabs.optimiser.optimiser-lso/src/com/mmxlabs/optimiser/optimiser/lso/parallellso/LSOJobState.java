/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.optimiser.optimiser.lso.parallellso;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Objects;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;

/**
 * The current state of a particular track of search in the LSO.
 * 
 * @author Alex
 *
 */
public class LSOJobState extends AbstractLSOJobState {
	
	protected long fitness;
	
	public LSOJobState(final ISequences rawSequences, final ISequences fullSequences, long fitness, LSOJobStatus status, IEvaluationState evaluationState, long seed, String note) {
		this.rawSequences = rawSequences;
		this.fullSequences = fullSequences;
		this.fitness = fitness;
		this.setStatus(status);
		this.evaluationState = evaluationState;
		this.setNote(note);
		this.setSeed(seed);
	}
}