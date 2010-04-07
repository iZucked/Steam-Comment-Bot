package com.mmxlabs.optimiser.lso.fitness.impl;

import java.util.Collection;
import java.util.Collections;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessCore;

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
	public void evaluate(final ISequences<T> sequences) {

		fitness = evaluateSequences(sequences);
	}

	@Override
	public void evaluate(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {

		fitness = evaluateSequences(sequences);
	}

	@Override
	public Collection<IFitnessComponent<T>> getFitnessComponents() {

		return Collections.singleton((IFitnessComponent<T>) component);
	}

	@Override
	public void init() {
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
}
