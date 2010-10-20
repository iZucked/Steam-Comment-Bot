/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.core.fitness.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

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
	public boolean evaluateSequencesFromComponents(final ISequences<T> sequences,
			final Collection<IFitnessComponent<T>> fitnessComponents) {
		final Set<IFitnessCore<T>> fitnessCores = getFitnessCores(fitnessComponents);

		return evaluateSequencesFromCores(sequences, fitnessCores);
	}

	@Override
	public boolean evaluateSequencesFromCores(final ISequences<T> sequences,
			final Collection<IFitnessCore<T>> fitnessCores) {

		for (final IFitnessCore<T> core : fitnessCores) {
			if (!core.evaluate(sequences)) return false;
		}
		return true;
	}

	@Override
	public boolean evaluateSequencesFromComponents(final ISequences<T> sequences,
			final Collection<IFitnessComponent<T>> fitnessComponents,
			final Collection<IResource> affectedResources) {
		final Set<IFitnessCore<T>> fitnessCores = getFitnessCores(fitnessComponents);

		return evaluateSequencesFromCores(sequences, fitnessCores, affectedResources);

	}

	@Override
	public boolean evaluateSequencesFromCores(final ISequences<T> sequences,
			final Collection<IFitnessCore<T>> fitnessCores,
			final Collection<IResource> affectedResources) {
		for (final IFitnessCore<T> core : fitnessCores) {
			if (!core.evaluate(sequences, affectedResources)) return false;
		}
		return true;
	}

	@Override
	public void initFitnessComponents(
			final Collection<IFitnessComponent<T>> fitnessComponents,
			final IOptimisationData<T> data) {
		final Set<IFitnessCore<T>> fitnessCores = getFitnessCores(fitnessComponents);
		initFitnessCores(fitnessCores, data);

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
	public void initFitnessCores(
			final Collection<IFitnessCore<T>> fitnessCores,
			final IOptimisationData<T> data) {
		for (final IFitnessCore<T> core : fitnessCores) {
			core.init(data);
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
