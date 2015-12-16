/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;

public class ArbitraryStateLocalSearchOptimiser extends DefaultLocalSearchOptimiser {

	@Override
	public IAnnotatedSolution start(@NonNull final IOptimisationContext optimiserContext, @NonNull ISequences initialRawSequences, @NonNull ISequences inputRawSequences) {
		setCurrentContext(optimiserContext);

		initLogger();

		data = optimiserContext.getOptimisationData();

		final ModifiableSequences currentRawSequences = new ModifiableSequences(inputRawSequences);

		final ModifiableSequences potentialRawSequences = new ModifiableSequences(currentRawSequences.getResources());
		updateSequences(currentRawSequences, potentialRawSequences, currentRawSequences.getResources());

		// Evaluate initial sequences
		setInitialSequences(initialRawSequences);
		evaluateInputSequences(inputRawSequences);

		// Set initial sequences
		getMoveGenerator().setSequences(potentialRawSequences);

		final IAnnotatedSolution annotatedBestSolution = getFitnessEvaluator().getBestAnnotatedSolution(optimiserContext);
		if (annotatedBestSolution == null) {
			// !!!!!
			// FIXME: Throw exception!
			return null;
		}

		annotatedBestSolution.setGeneralAnnotation(OptimiserConstants.G_AI_iterations, 0);
		annotatedBestSolution.setGeneralAnnotation(OptimiserConstants.G_AI_runtime, 0l);

		setStartTime(System.currentTimeMillis());

		setNumberOfIterationsCompleted(0);

		this.currentRawSequences = currentRawSequences;
		this.potentialRawSequences = potentialRawSequences;

		initProgressLog();

		return annotatedBestSolution;
	}

	@Override
	protected void initProgressLog() {
		// do nothing
	}


//	@Override
//	public int getNumberOfMovesTried() {
//		// adding on previous values for logs
//		return numberOfMovesTried + (loggingDataStore != null ? loggingDataStore.getNumberOfMovesTried() : 0);
//	}
//	
//	@Override
//	public int getNumberOfMovesAccepted() {
//		// adding on previous values for logs
//		return numberOfMovesAccepted + (loggingDataStore != null ? loggingDataStore.getNumberOfMovesAccepted() : 0);
//	}
//
//	@Override
//	public int getNumberOfRejectedMoves() {
//		// adding on previous values for logs
//		return numberOfRejectedMoves + (loggingDataStore != null ? loggingDataStore.getNumberOfRejectedMoves() : 0);
//	}
//
//	@Override
//	public int getNumberOfFailedEvaluations() {
//		// adding on previous values for logs
//		return numberOfFailedEvaluations + (loggingDataStore != null ? loggingDataStore.getNumberOfFailedEvaluations() : 0);
//	}
//
//	@Override
//	public int getNumberOfFailedToValidate() {
//		// adding on previous values for logs
//		return numberOfFailedToValidate + (loggingDataStore != null ? loggingDataStore.getNumberOfFailedToValidate() : 0);
//	}

}