/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * Basic implementation of {@link IFitnessHelper}.
 * 
 * @author Simon Goodall
 */
public final class FitnessHelper implements IFitnessHelper {

	@Override
	public boolean evaluateSequencesFromComponents(@NonNull final ISequences sequences, @NonNull final Collection<IFitnessComponent> fitnessComponents) {
		final Set<IFitnessCore> fitnessCores = getFitnessCores(fitnessComponents);

		return evaluateSequencesFromCores(sequences, fitnessCores);
	}

	@Override
	public boolean evaluateSequencesFromCores(@NonNull final ISequences sequences, @NonNull final Collection<IFitnessCore> fitnessCores) {

		for (final IFitnessCore core : fitnessCores) {
			if (!core.evaluate(sequences)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean evaluateSequencesFromComponents(@NonNull final ISequences sequences, @NonNull final Collection<IFitnessComponent> fitnessComponents,
			@Nullable final Collection<IResource> affectedResources) {
		final Set<IFitnessCore> fitnessCores = getFitnessCores(fitnessComponents);

		return evaluateSequencesFromCores(sequences, fitnessCores, affectedResources);

	}

	@Override
	public boolean evaluateSequencesFromCores(@NonNull final ISequences sequences, @NonNull final Collection<IFitnessCore> fitnessCores, @Nullable final Collection<IResource> affectedResources) {
		for (final IFitnessCore core : fitnessCores) {
			if (!core.evaluate(sequences, affectedResources)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void initFitnessComponents(@NonNull final Collection<IFitnessComponent> fitnessComponents, @NonNull final IOptimisationData data) {
		final Set<IFitnessCore> fitnessCores = getFitnessCores(fitnessComponents);
		initFitnessCores(fitnessCores, data);

	}

	@NonNull
	@Override
	public Set<IFitnessCore> getFitnessCores(@NonNull final Collection<IFitnessComponent> fitnessComponents) {
		final Set<IFitnessCore> fitnessCores = new HashSet<IFitnessCore>();
		for (final IFitnessComponent component : fitnessComponents) {
			fitnessCores.add(component.getFitnessCore());
		}
		return fitnessCores;
	}

	@Override
	public void initFitnessCores(@NonNull final Collection<IFitnessCore> fitnessCores, @NonNull final IOptimisationData data) {
		for (final IFitnessCore core : fitnessCores) {
			core.init(data);
		}
	}

	@Override
	public void acceptFromComponents(@NonNull final Collection<IFitnessComponent> fitnessComponents, @NonNull final ISequences sequences, @Nullable final Collection<IResource> affectedResources) {
		final Set<IFitnessCore> fitnessCores = getFitnessCores(fitnessComponents);
		acceptFromCores(fitnessCores, sequences, affectedResources);
	}

	@Override
	public void acceptFromCores(@NonNull final Collection<IFitnessCore> fitnessCores, @NonNull final ISequences sequences, @Nullable final Collection<IResource> affectedResources) {
		for (final IFitnessCore core : fitnessCores) {
			core.accepted(sequences, affectedResources);
		}
	}

	@NonNull
	@Override
	public IAnnotatedSolution buildAnnotatedSolution(@NonNull final IOptimisationContext context, @NonNull final ISequences state, @NonNull final Collection<IFitnessComponent> fitnessComponents) {

		final Set<IFitnessCore> cores = getFitnessCores(fitnessComponents);

		final AnnotatedSolution result = new AnnotatedSolution();
		result.setSequences(state);
		result.setContext(context);

		for (final IFitnessCore core : cores) {
			core.annotate(state, result);
		}

		return result;
	}
}
