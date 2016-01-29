/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGEvaluationTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGLSOOptimiserTransformerUnit;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;

public class MicroTestUtils {
	/**
	 * Returns null on success, or returns the failing constraint checkers.
	 * 
	 * @param rawSequences
	 * @return
	 */
	public static List<IConstraintChecker> validateConstraintCheckers(@NonNull final LNGDataTransformer dataTransformer, @NonNull final ISequences rawSequences) {

		final LNGEvaluationTransformerUnit evaluationTransformerUnit = new LNGEvaluationTransformerUnit(dataTransformer, dataTransformer.getInitialSequences(), dataTransformer.getHints());
		final List<IConstraintChecker> constraintCheckers = evaluationTransformerUnit.getInjector().getInstance(Key.get(new TypeLiteral<List<IConstraintChecker>>() {
		}));
		final ISequencesManipulator sequencesManipulator = evaluationTransformerUnit.getInjector().getInstance(ISequencesManipulator.class);

		final List<IConstraintChecker> failedCheckers = new LinkedList<>();
		// // Apply sequence manipulators
		final IModifiableSequences fullSequences = sequencesManipulator.createManipulatedSequences(rawSequences);

		// Apply hard constraint checkers
		for (final IConstraintChecker checker : constraintCheckers) {
			if (!checker.checkConstraints(fullSequences)) {
				failedCheckers.add(checker);
			}
		}
		return failedCheckers.isEmpty() ? null : failedCheckers;
	}

	/**
	 * Returns null on success, or returns the failing constraint checkers.
	 * 
	 * @param rawSequences
	 * @return
	 */
	public static boolean evaluateLSOSequences(@NonNull final LNGDataTransformer dataTransformer, @NonNull final ISequences rawSequences) {

		final LNGLSOOptimiserTransformerUnit unit = new LNGLSOOptimiserTransformerUnit(dataTransformer, dataTransformer.getOptimiserSettings(), dataTransformer.getInitialSequences(),
				dataTransformer.getHints());

		return true;
	}
}