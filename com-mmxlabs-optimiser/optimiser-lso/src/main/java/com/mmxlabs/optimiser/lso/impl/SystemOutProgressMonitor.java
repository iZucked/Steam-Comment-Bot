/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;

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
	public final void begin(final IOptimiser<T> optimiser,
			final long initialFitness,
			final IAnnotatedSolution<T> annotatedSolution) {
		System.out.println("Initial Fitness: " + initialFitness);
	}

	@Override
	public final void report(final IOptimiser<T> optimiser,
			final int iteration, final long currentFitness,
			final long bestFitness,
			final IAnnotatedSolution<T> currentSolution,
			final IAnnotatedSolution<T> annotatedSolution) {
		System.out.println("Iter: " + iteration + " Fitness: " + bestFitness);
	}

	@Override
	public final void done(final IOptimiser<T> optimiser,
			final long bestFitness,
			final IAnnotatedSolution<T> annotatedSolution) {
		System.out.println("Final Fitness: " + bestFitness);
	}
}