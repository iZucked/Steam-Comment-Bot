package com.acme.optimiser.fitness.impl;

import java.util.List;

import com.acme.optimiser.ISequences;
import com.acme.optimiser.fitness.IFitnessFunction;
import com.acme.optimiser.fitness.IFitnessHelper;

/**
 * Basic implementation of {@link IFitnessHelper}
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public class FitnessHelper<T> implements IFitnessHelper<T> {

	@Override
	public void evaluateSequences(final ISequences<T> sequences,
			final List<IFitnessFunction<T>> fitnessFunctions) {

		throw new UnsupportedOperationException("Not yet implemeneted");
	}
}
