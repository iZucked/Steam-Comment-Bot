package org.sgoodall.optimiser.lso.impl;

import java.util.Collection;

import org.sgoodall.optimiser.IOptimisationContext;
import org.sgoodall.optimiser.IOptimiser;
import org.sgoodall.optimiser.ISolution;
import org.sgoodall.optimiser.lso.IMove;
import org.sgoodall.optimiser.lso.IMoveGenerator;

public class LocalSearchOptimiser implements IOptimiser {

	@Override
	public void optimise(IOptimisationContext optimiserContext,
			Collection<ISolution> initialSolutions, Object archiverCallback) {
		
		// Setup the optimisation process
		
		
		// Perform the optimisation
		int numIterations;
		
		
		IMoveGenerator moveGenerator;
		
		for (int iter = 0; iter < numIterations; ++iter) {
			
			IMove move = moveGenerator.generateMove();

			if (testMove(move)) {
				
				move.apply(sequences);
			} else {
				
			}
			
			
			++iter;
		}
		

		// Finalise optimisation process
		
		
	}

}
