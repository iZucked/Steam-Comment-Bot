/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.fitness.impl;

import java.util.Collection;
import java.util.Collections;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

public final class SortingFitnessCore<T> implements IFitnessCore<T> {
	public static final String CORE_NAME = "Sorting Fitness Core";

	private long fitness;

	private final SortingFitnessComponent<T> component;

	public SortingFitnessCore() {
		component = new SortingFitnessComponent<T>(this);
	}

	@Override
	public void accepted(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {

		// Nothing to do here, no state is recorded
	}

	@Override
	public boolean evaluate(final ISequences<T> sequences) {

		fitness = evaluateSequences(sequences);
		return true;
	}

	@Override
	public boolean evaluate(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {

		fitness = evaluateSequences(sequences);
		return true;
	}

	@Override
	public Collection<IFitnessComponent<T>> getFitnessComponents() {

		return Collections.singleton((IFitnessComponent<T>) component);
	}

	@Override
	public void init(IOptimisationData<T> data) {
		fitness = Long.MAX_VALUE;
	}

	public long getSortingFitness() {

		return fitness;
	}

	private long evaluateSequences(final ISequences<T> sequences) {
		long fitness = 0;

		final int numSequences = sequences.size();

		for (int i = 0; i < numSequences; ++i) {
			final ISequence<T> sequence = sequences.getSequence(i);

			final int numElements = sequence.size();
			for (int j = 1; j < numElements; ++j) {
				final T a = sequence.get(j - 1);
				final T b = sequence.get(j);

				// TODO: Check element type.

				final Integer ia = (Integer) a;
				final Integer ib = (Integer) b;

				if (ia.intValue() > ib.intValue()) {
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
	public void annotate(ISequences<T> sequences, IAnnotatedSolution<T> solution, final boolean forExport) {
		
	}
}
