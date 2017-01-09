/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.fitness.impl;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.IntegerElement;

public final class SortingFitnessCore implements IFitnessCore {
	@NonNull
	public static final String CORE_NAME = "Sorting Fitness Core";

	private long fitness;

	@NonNull
	private final SortingFitnessComponent component;

	public SortingFitnessCore() {
		component = new SortingFitnessComponent(this);
	}

	@Override
	public void accepted(@NonNull final ISequences sequences, @Nullable final Collection<IResource> affectedResources) {

		// Nothing to do here, no state is recorded
	}

	@Override
	public boolean evaluate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState) {

		fitness = evaluateSequences(sequences, evaluationState);
		return true;
	}

	@Override
	public boolean evaluate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @Nullable final Collection<IResource> affectedResources) {

		fitness = evaluateSequences(sequences, evaluationState);
		return true;
	}

	@Override
	@NonNull
	public Collection<IFitnessComponent> getFitnessComponents() {

		return Collections.singleton((IFitnessComponent) component);
	}

	@Override
	public void init(@NonNull final IOptimisationData data) {
		fitness = Long.MAX_VALUE;
	}

	public long getSortingFitness() {

		return fitness;
	}

	private long evaluateSequences(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState) {
		long fitness = 0;

		final int numSequences = sequences.size();

		for (int i = 0; i < numSequences; ++i) {
			final ISequence sequence = sequences.getSequence(i);

			final int numElements = sequence.size();
			for (int j = 1; j < numElements; ++j) {
				final ISequenceElement a = sequence.get(j - 1);
				final ISequenceElement b = sequence.get(j);

				// TODO: Check element type.

				final IntegerElement ia = (IntegerElement) a;
				final IntegerElement ib = (IntegerElement) b;

				if (ia.getIndex() > ib.getIndex()) {
					++fitness;
				}
			}
		}
		return fitness;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void annotate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @NonNull final IAnnotatedSolution solution) {

	}
}
