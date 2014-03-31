/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
	public boolean evaluateSequencesFromComponents(final ISequences sequences, final Collection<IFitnessComponent> fitnessComponents) {
		final Set<IFitnessCore> fitnessCores = getFitnessCores(fitnessComponents);

		return evaluateSequencesFromCores(sequences, fitnessCores);
	}

	@Override
	public boolean evaluateSequencesFromCores(final ISequences sequences, final Collection<IFitnessCore> fitnessCores) {

		for (final IFitnessCore core : fitnessCores) {
			if (!core.evaluate(sequences)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean evaluateSequencesFromComponents(final ISequences sequences, final Collection<IFitnessComponent> fitnessComponents, final Collection<IResource> affectedResources) {
		final Set<IFitnessCore> fitnessCores = getFitnessCores(fitnessComponents);

		return evaluateSequencesFromCores(sequences, fitnessCores, affectedResources);

	}

	@Override
	public boolean evaluateSequencesFromCores(final ISequences sequences, final Collection<IFitnessCore> fitnessCores, final Collection<IResource> affectedResources) {
		for (final IFitnessCore core : fitnessCores) {
			if (!core.evaluate(sequences, affectedResources)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void initFitnessComponents(final Collection<IFitnessComponent> fitnessComponents, final IOptimisationData data) {
		final Set<IFitnessCore> fitnessCores = getFitnessCores(fitnessComponents);
		initFitnessCores(fitnessCores, data);

	}

	@Override
	public Set<IFitnessCore> getFitnessCores(final Collection<IFitnessComponent> fitnessComponents) {
		final Set<IFitnessCore> fitnessCores = new HashSet<IFitnessCore>();
		for (final IFitnessComponent component : fitnessComponents) {
			fitnessCores.add(component.getFitnessCore());
		}
		return fitnessCores;
	}

	@Override
	public void initFitnessCores(final Collection<IFitnessCore> fitnessCores, final IOptimisationData data) {
		for (final IFitnessCore core : fitnessCores) {
			core.init(data);
		}
	}

	@Override
	public void acceptFromComponents(final Collection<IFitnessComponent> fitnessComponents, final ISequences sequences, final Collection<IResource> affectedResources) {
		final Set<IFitnessCore> fitnessCores = getFitnessCores(fitnessComponents);
		acceptFromCores(fitnessCores, sequences, affectedResources);
	}

	@Override
	public void acceptFromCores(final Collection<IFitnessCore> fitnessCores, final ISequences sequences, final Collection<IResource> affectedResources) {
		for (final IFitnessCore core : fitnessCores) {
			core.accepted(sequences, affectedResources);
		}
	}

	@Override
	public IAnnotatedSolution buildAnnotatedSolution(final IOptimisationContext context, final ISequences state, final Collection<IFitnessComponent> fitnessComponents, final boolean forExport) {

		final Set<IFitnessCore> cores = getFitnessCores(fitnessComponents);

		final AnnotatedSolution result = new AnnotatedSolution();
		result.setSequences(state);
		result.setContext(context);

		for (final IFitnessCore core : cores) {
			core.annotate(state, result, forExport);
		}

		return result;
	}
}
