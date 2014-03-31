/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;

/**
 * Simple {@link IOptimiserProgressMonitor} implementation using {@link System#out} to report progress.
 * 
 * @author Simon Goodall
 * 
 */
public final class SystemOutProgressMonitor implements IOptimiserProgressMonitor {

	@Override
	public final void begin(final IOptimiser optimiser, final long initialFitness, final IAnnotatedSolution annotatedSolution) {
		System.out.println("Initial Fitness: " + initialFitness);
	}

	@Override
	public final void report(final IOptimiser optimiser, final int iteration, final long currentFitness, final long bestFitness, final IAnnotatedSolution currentSolution,
			final IAnnotatedSolution annotatedSolution) {
		System.out.println("Iter: " + iteration + " Fitness: " + bestFitness);
	}

	@Override
	public final void done(final IOptimiser optimiser, final long bestFitness, final IAnnotatedSolution annotatedSolution) {
		System.out.println("Final Fitness: " + bestFitness);
	}
}