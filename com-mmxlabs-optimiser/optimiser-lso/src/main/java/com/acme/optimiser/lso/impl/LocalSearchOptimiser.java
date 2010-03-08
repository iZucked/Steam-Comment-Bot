package com.acme.optimiser.lso.impl;

import java.util.Collection;

import com.acme.optimiser.IOptimisationContext;
import com.acme.optimiser.IOptimiser;
import com.acme.optimiser.ISequenceManipulator;
import com.acme.optimiser.ISequences;
import com.acme.optimiser.ISolution;
import com.acme.optimiser.lso.IMove;
import com.acme.optimiser.lso.IMoveGenerator;

public class LocalSearchOptimiser implements IOptimiser {

	@Override
	public void optimise(IOptimisationContext optimiserContext,
			Collection<ISolution> initialSolutions, Object archiverCallback) {

		/* Setup the optimisation process */
		// construct initial sequences
		ISequences initialSequences;

		// Perform the optimisation
		int numIterations;

		IMoveGenerator moveGenerator;
		//		
		ISequences currentRawSequences = initialSequences.clone();
		ISequences nextRawSequences = initialSequences.clone();
		ISequences modifiedSequences = initialSequences.clone();
		ISolution currentSolution;
		ISolution solution;
		// for (int iter = 0; iter < numIterations; ++iter) {
		//			
		// IMove move = moveGenerator.generateMove();

		// if (testMove(move)) {
		//				
		// move.apply(sequences);`
		// } else {
		//				
		// }

		//			
		
		// Build a new Solution from the existing one, first by a move to give a new Sequence, and then expanding 
		// through custom decorations and eventually route builders and fitness functions to give the full Solution 
	
		// Generate move and 
		IMove move = moveGenerator.generateMove();					
		// loop over move components:
			// create new ISequences for move
		
		
		move.apply(nextRawSequences);
					
		
		modifiedSequences = nextRawSequences.clone();
		// apply ISequenceManipulator(s) e.g. return-to sites, etc.
		ISequenceManipulator manipulator;
		manipulator.manipulate(modifiedSequences);
		
		
		// do Sequence constraint checking on modifiedSequences
		
		// customisable module, default as below:
		{
		ISolutionBuilder solbuilder;
		// Convert Sequence to a Solution through the following steps (not necessarily in this order):
			// create Solution object
			// insert extra points
			// check hard constraints for early exit
			// loop over fitness functions to calculate fitness values/deltas, check hard constraints
			// populate Solution object as appropriate
		// evaluate fitnesses
		}
		
		// apply meta-heuristic acceptance function
		boolean accepted;
		// update solution and archive etc.
		if(accepted){
			currentRawSequences.copy(rawSequences, modifiedSequences);
		}
		else{
			rawSequences.copy(currentRawSequences, modifiedSequences);
		}

		// ++iter;
		
		
		// }

		// Finalise optimisation process

		
		
		
		
		
		
		// optimisation search loop:
		
			
			
		// export solution
	}

}
