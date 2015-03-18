/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

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
	public void begin(final IOptimiser optimiser, final long initialFitness, final IAnnotatedSolution annotatedSolution) {

	}

	@Override
	public void report(final IOptimiser optimiser, final int iteration, final long currentFitness, final long bestFitness, final IAnnotatedSolution currentSolution,
			final IAnnotatedSolution annotatedSolution) {

	}

	@Override
	public void done(final IOptimiser optimiser, final long bestFitness, final IAnnotatedSolution annotatedSolution) {

	}
}
