/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga.impl;

import java.util.Collection;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequencesOptimiser;
import com.mmxlabs.optimiser.core.impl.AbstractSequencesOptimiser;
import com.mmxlabs.optimiser.ga.IGeneticAlgorithm;
import com.mmxlabs.optimiser.ga.IGeneticAlgorithmOptimiser;

/**
 * Implementation of a {@link ISequencesOptimiser} using a {@link IGeneticAlgorithm} to perform the optimisation.
 * 
 * @author Simon Goodall
 * 
 */
public class GeneticAlgorithmOptimiser extends AbstractSequencesOptimiser implements IGeneticAlgorithmOptimiser {

	/**
	 * Initialise method checking the object has all the correct pieces of data to be able to perform the {@link #optimise(IOptimisationContext, Collection, Object)} method. Throws an
	 * {@link IllegalStateException} on error.
	 */
	@Override
	public void init() {
		// TODO: Implement

		super.init();

		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void dispose() {
		// TODO: Implement

		super.dispose();

		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public IAnnotatedSolution start(final IOptimisationContext optimiserContext) {

		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public int step(final int percentage) {

		throw new UnsupportedOperationException("Not yet implemented");
	}
}
