/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga.impl;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.ISequencesOptimiser;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.impl.AbstractSequencesOptimiser;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.ga.IGeneticAlgorithm;
import com.mmxlabs.optimiser.ga.IGeneticAlgorithmOptimiser;

/**
 * Implementation of a {@link ISequencesOptimiser} using a
 * {@link IGeneticAlgorithm} to perform the optimisation.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public class GeneticAlgorithmOptimiser<T> extends AbstractSequencesOptimiser<T>
		implements IGeneticAlgorithmOptimiser<T> {

	/**
	 * Initialise method checking the object has all the correct pieces of data
	 * to be able to perform the
	 * {@link #optimise(IOptimisationContext, Collection, Object)} method.
	 * Throws an {@link IllegalStateException} on error.
	 */
	@Override
	public void init() {
		// TODO: Implement

		super.init();
	}

	@Override
	public void dispose() {
		// TODO: Implement

		super.dispose();
	}

	@Override
	public IAnnotatedSolution<T> start(
			final IOptimisationContext<T> optimiserContext) {
		
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public int step(final int percentage) {

		throw new UnsupportedOperationException("Not yet implemented");
	}
}
