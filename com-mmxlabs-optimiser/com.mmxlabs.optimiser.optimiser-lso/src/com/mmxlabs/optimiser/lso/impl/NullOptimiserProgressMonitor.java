/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;

/**
 * Empty (null) implementation of {@link IOptimiserProgressMonitor} to
 * 
 * @author Simon Goodall
 * 
 */
public final class NullOptimiserProgressMonitor implements IOptimiserProgressMonitor {

	@Override
	public void begin(@NonNull final IOptimiser optimiser, final long initialFitness, @Nullable final IAnnotatedSolution annotatedSolution) {

	}

	@Override
	public void report(@NonNull final IOptimiser optimiser, final int iteration, final long currentFitness, final long bestFitness, @Nullable final IAnnotatedSolution currentSolution,
			@Nullable final IAnnotatedSolution bestSolution) {

	}

	@Override
	public void done(@NonNull final IOptimiser optimiser, final long bestFitness, @Nullable final IAnnotatedSolution annotatedSolution) {

	}
}
