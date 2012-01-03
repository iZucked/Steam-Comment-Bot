/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.fitness.impl;

import java.util.Collection;
import java.util.Collections;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.IntegerElement;

public final class SortingFitnessCore implements IFitnessCore {
	public static final String CORE_NAME = "Sorting Fitness Core";

	private long fitness;

	private final SortingFitnessComponent component;

	public SortingFitnessCore() {
		component = new SortingFitnessComponent(this);
	}

	@Override
	public void accepted(final ISequences sequences, final Collection<IResource> affectedResources) {

		// Nothing to do here, no state is recorded
	}

	@Override
	public boolean evaluate(final ISequences sequences) {

		fitness = evaluateSequences(sequences);
		return true;
	}

	@Override
	public boolean evaluate(final ISequences sequences, final Collection<IResource> affectedResources) {

		fitness = evaluateSequences(sequences);
		return true;
	}

	@Override
	public Collection<IFitnessComponent> getFitnessComponents() {

		return Collections.singleton((IFitnessComponent) component);
	}

	@Override
	public void init(final IOptimisationData data) {
		fitness = Long.MAX_VALUE;
	}

	public long getSortingFitness() {

		return fitness;
	}

	private long evaluateSequences(final ISequences sequences) {
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
	public void annotate(final ISequences sequences, final IAnnotatedSolution solution, final boolean forExport) {

	}
}
