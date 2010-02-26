package com.acme.optimiser.lso.impl;

import java.util.Collection;

import com.acme.optimiser.IOptimisationContext;
import com.acme.optimiser.IOptimiser;
import com.acme.optimiser.ISolution;
import com.acme.optimiser.lso.IMoveGenerator;

/**
 * Main class implementing a Local Search Optimiser.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence Element Type
 */
public class LocalSearchOptimiser<T> implements IOptimiser<T> {

	private IMoveGenerator<T> moveGenerator;

	private int numberOfIterations;

	@Override
	public void optimise(IOptimisationContext optimiserContext,
			Collection<ISolution> initialSolutions, Object archiverCallback) {

		// Setup the optimisation process

		// Perform the optimisation
		int numIterations;

		IMoveGenerator moveGenerator;
		//		
		// for (int iter = 0; iter < numIterations; ++iter) {
		//			
		// IMove move = moveGenerator.generateMove();

		// if (testMove(move)) {
		//				
		// move.apply(sequences);
		// } else {
		//				
		// }

		//			
		// ++iter;
		// }

		// Finalise optimisation process

	}

}
