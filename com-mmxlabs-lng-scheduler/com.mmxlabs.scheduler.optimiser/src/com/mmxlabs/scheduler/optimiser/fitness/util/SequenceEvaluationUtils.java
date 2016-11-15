package com.mmxlabs.scheduler.optimiser.fitness.util;

import com.mmxlabs.optimiser.core.ISequence;

public class SequenceEvaluationUtils {
	/**
	 * Checks to see whether the sequence should be ignored in an evaluation.
	 * For example, if an optional vessel is empty.
	 * @param sequence
	 * @return
	 */
	public static final boolean shouldIgnoreSequence(ISequence sequence) {
		return sequence.size() < 2;
	}
}
