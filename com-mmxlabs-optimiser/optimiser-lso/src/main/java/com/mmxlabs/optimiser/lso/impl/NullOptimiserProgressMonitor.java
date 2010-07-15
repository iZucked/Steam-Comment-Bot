package com.mmxlabs.optimiser.lso.impl;

import com.mmxlabs.optimiser.IOptimiser;
import com.mmxlabs.optimiser.ISequences;
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
			ISequences<T> initialState) {

	}

	@Override
	public void report(IOptimiser<T> optimiser, int iteration,
			long currentFitness, long bestFitness, ISequences<T> currentState,
			ISequences<T> bestState) {

	}

	@Override
	public void done(IOptimiser<T> optimiser, long bestFitness,
			ISequences<T> bestState) {

	}
}
