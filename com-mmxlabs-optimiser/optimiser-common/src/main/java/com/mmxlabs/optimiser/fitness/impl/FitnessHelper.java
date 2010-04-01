package com.mmxlabs.optimiser.fitness.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessCore;
import com.mmxlabs.optimiser.fitness.IFitnessHelper;

/**
 * Basic implementation of {@link IFitnessHelper}.
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
		final Set<IFitnessCore<T>> fitnessCores = getFitnessCores(fitnessComponents);

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
			final Collection<IResource> affectedResources) {
		final Set<IFitnessCore<T>> fitnessCores = getFitnessCores(fitnessComponents);

		evaluateSequencesFromCores(sequences, fitnessCores, affectedResources);

	}

	@Override
	public void evaluateSequencesFromCores(final ISequences<T> sequences,
			final Collection<IFitnessCore<T>> fitnessCores,
			final Collection<IResource> affectedResources) {
		for (final IFitnessCore<T> core : fitnessCores) {
			core.evaluate(sequences, affectedResources);
		}
	}

	@Override
	public void initFitnessComponents(
			final Collection<IFitnessComponent<T>> fitnessComponents) {
		final Set<IFitnessCore<T>> fitnessCores = getFitnessCores(fitnessComponents);
		initFitnessCores(fitnessCores);

	}

	@Override
	public Set<IFitnessCore<T>> getFitnessCores(
			final Collection<IFitnessComponent<T>> fitnessComponents) {
		final Set<IFitnessCore<T>> fitnessCores = new HashSet<IFitnessCore<T>>();
		for (final IFitnessComponent<T> component : fitnessComponents) {
			fitnessCores.add(component.getFitnessCore());
		}
		return fitnessCores;
	}

	@Override
	public void initFitnessCores(final Collection<IFitnessCore<T>> fitnessCores) {
		for (final IFitnessCore<T> core : fitnessCores) {
			core.init();
		}
	}

	@Override
	public void acceptFromComponents(
			final Collection<IFitnessComponent<T>> fitnessComponents,
			final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {
		final Set<IFitnessCore<T>> fitnessCores = getFitnessCores(fitnessComponents);
		acceptFromCores(fitnessCores, sequences, affectedResources);
	}

	@Override
	public void acceptFromCores(final Collection<IFitnessCore<T>> fitnessCores,
			final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {
		for (final IFitnessCore<T> core : fitnessCores) {
			core.accepted(sequences, affectedResources);
		}
	}
}
