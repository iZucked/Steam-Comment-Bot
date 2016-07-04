/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
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
			return null;
		}

		annotatedBestSolution.setGeneralAnnotation(OptimiserConstants.G_AI_iterations, 0);
		annotatedBestSolution.setGeneralAnnotation(OptimiserConstants.G_AI_runtime, 0l);

		// For constraint checker changed resources functions, if initial solution is invalid, we want to always perform a full constraint checker set of checks.
		this.failedInitialConstraintCheckers = false;
		// Apply sequence manipulators
		final IModifiableSequences potentialFullSequences = getSequenceManipulator().createManipulatedSequences(currentRawSequences);

		// Apply hard constraint checkers
		for (final IConstraintChecker checker : getConstraintCheckers()) {
			if (checker.checkConstraints(potentialFullSequences, null) == false) {
				// Set break point here!
				// checker.checkConstraints(potentialFullSequences, null);

				failedInitialConstraintCheckers = true;
				break;
			}
		}

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
}