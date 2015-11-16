package com.mmxlabs.models.lng.transformer.chain.impl;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.models.lng.transformer.chain.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;

public class MultiStateResult implements IMultiStateResult {

	@NonNull
	private final NonNullPair<ISequences, Map<String, Object>> bestResult;

	@NonNull
	private final List<NonNullPair<ISequences, Map<String, Object>>> solutions;

	public MultiStateResult(@NonNull final ISequences sequences, @NonNull final Map<String, Object> solution) {
		this.bestResult = new NonNullPair<>(sequences, solution);
		this.solutions = Lists.newArrayList(bestResult);
	}

	public MultiStateResult(@NonNull final NonNullPair<ISequences, Map<String, Object>> bestResult, @NonNull final List<NonNullPair<ISequences, Map<String, Object>>> solutions) {
		this.bestResult = bestResult;
		this.solutions = solutions;
	}

	@Override
	public NonNullPair<ISequences, Map<String, Object>> getBestSolution() {
		return bestResult;
	}

	@Override
	public List<NonNullPair<ISequences, Map<String, Object>>> getSolutions() {
		return solutions;
	}
}
