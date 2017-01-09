/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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
	public final void begin(@NonNull final IOptimiser optimiser, final long initialFitness, @Nullable final IAnnotatedSolution annotatedSolution) {
		System.out.println("Initial Fitness: " + initialFitness);
	}

	@Override
	public final void report(@NonNull final IOptimiser optimiser, final int iteration, final long currentFitness, final long bestFitness, @Nullable final IAnnotatedSolution currentSolution,
			@Nullable final IAnnotatedSolution annotatedSolution) {
		System.out.println("Iter: " + iteration + " Fitness: " + bestFitness);
	}

	@Override
	public final void done(@NonNull final IOptimiser optimiser, final long bestFitness, @Nullable final IAnnotatedSolution annotatedSolution) {
		System.out.println("Final Fitness: " + bestFitness);
	}
}