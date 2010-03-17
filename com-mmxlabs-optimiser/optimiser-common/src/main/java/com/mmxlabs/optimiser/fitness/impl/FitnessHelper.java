package com.mmxlabs.optimiser.fitness.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessCore;
import com.mmxlabs.optimiser.fitness.IFitnessHelper;

/**
 * Basic implementation of {@link IFitnessHelper}
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class FitnessHelper<T> implements IFitnessHelper<T> {

	@Override
	public void evaluateSequencesFromComponents(final ISequences<T> sequences,
			final Collection<IFitnessComponent<T>> fitnessComponents) {
		final Set<IFitnessCore<T>> fitnessCores = new HashSet<IFitnessCore<T>>();
		for (final IFitnessComponent<T> component : fitnessComponents) {
			fitnessCores.add(component.getFitnessCore());
		}

		evaluateSequencesFromCores(sequences, fitnessCores);
	}

	@Override
	public void evaluateSequencesFromCores(final ISequences<T> sequences,
			final Collection<IFitnessCore<T>> fitnessCores) {

		for (final IFitnessCore<T> core : fitnessCores) {
			core.evaluate(sequences);
		}
	}

	@Override
	public void evaluateSequencesFromComponents(final ISequences<T> sequences,
			final Collection<IFitnessComponent<T>> fitnessComponents,
			final List<IResource> affectedResources) {
		final Set<IFitnessCore<T>> fitnessCores = new HashSet<IFitnessCore<T>>();
		for (final IFitnessComponent<T> component : fitnessComponents) {
			fitnessCores.add(component.getFitnessCore());
		}

		evaluateSequencesFromCores(sequences, fitnessCores, affectedResources);

	}

	@Override
	public void evaluateSequencesFromCores(final ISequences<T> sequences,
			final Collection<IFitnessCore<T>> fitnessCores,
			final List<IResource> affectedResources) {
		for (final IFitnessCore<T> core : fitnessCores) {
			core.evaluate(sequences, affectedResources);
		}
	}
}
