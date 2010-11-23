/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.lso.impl;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IOptimiserProgressMonitor;

/**
 * Empty (null) implementation of {@link IOptimiserProgressMonitor} to
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class NullOptimiserProgressMonitor<T> implements
		IOptimiserProgressMonitor<T> {

	@Override
	public void begin(IOptimiser<T> optimiser, long initialFitness,
			IAnnotatedSolution<T> annotatedSolution) {

	}

	@Override
	public void report(IOptimiser<T> optimiser, int iteration,
			long currentFitness, long bestFitness,
			IAnnotatedSolution<T> currentSolution,
			IAnnotatedSolution<T> annotatedSolution) {

	}

	@Override
	public void done(IOptimiser<T> optimiser, long bestFitness,
			IAnnotatedSolution<T> annotatedSolution) {

	}
}
