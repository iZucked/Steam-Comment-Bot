package com.mmxlabs.models.lng.transformer.chain.impl;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transformer.chain.IMultiStateResult;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequences;

public class MultiStateResult implements IMultiStateResult {

	@NonNull
	private final Pair<ISequences, IAnnotatedSolution> bestResult;

	@NonNull
	private final List<Pair<ISequences, IAnnotatedSolution>> solutions;

	public MultiStateResult(@NonNull final ISequences sequences, @NonNull final IAnnotatedSolution solution) {
		this.bestResult = new Pair<>(sequences, solution);
		this.solutions = Lists.newArrayList(bestResult);
	}

	public MultiStateResult(@NonNull final Pair<ISequences, IAnnotatedSolution> bestResult, @NonNull final List<Pair<ISequences, IAnnotatedSolution>> solutions) {
		this.bestResult = bestResult;
		this.solutions = solutions;
	}

	@Override
	public Pair<ISequences, IAnnotatedSolution> getBestSolution() {
		return bestResult;
	}

	@Override
	public List<Pair<ISequences, IAnnotatedSolution>> getSolutions() {
		return solutions;
	}
}
