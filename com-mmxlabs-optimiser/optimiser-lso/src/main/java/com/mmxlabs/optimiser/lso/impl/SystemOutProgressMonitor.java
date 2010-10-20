/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.lso.impl;

import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IOptimiserProgressMonitor;

/**
 * Simple {@link IOptimiserProgressMonitor} implementation using
 * {@link System#out} to report progress.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class SystemOutProgressMonitor<T> implements
		IOptimiserProgressMonitor<T> {

	@Override
	public final void begin(final IOptimiser<T> optimiser, final long initialFitness,
			final ISequences<T> initialState) {
		System.out.println("Initial Fitness: " + initialFitness);
	}

	@Override
	public final void report(final IOptimiser<T> optimiser, final int iteration,
			final long currentFitness, final long bestFitness,
			final ISequences<T> currentState, final ISequences<T> bestState) {
		System.out.println("Iter: " + iteration + " Fitness: " + bestFitness);
	}

	@Override
	public final void done(final IOptimiser<T> optimiser, final long bestFitness,
			final ISequences<T> bestState) {
		System.out.println("Final Fitness: " + bestFitness);
	}
}