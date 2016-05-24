/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGEvaluationTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGLSOOptimiserTransformerUnit;
import com.mmxlabs.optimiser.core.IEvaluationContext;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;
import com.mmxlabs.optimiser.core.impl.EvaluationContext;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;

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
			if (!checker.checkConstraints(fullSequences, null)) {
				failedCheckers.add(checker);
			}
		}
		return failedCheckers.isEmpty() ? null : failedCheckers;
	}

	/**
	 * Returns null on success, or returns the failing evaluated state constraint checkers.
	 * 
	 * @param rawSequences
	 * @return
	 */
	public static List<@NonNull IEvaluatedStateConstraintChecker> validateEvaluatedStateConstraintCheckers(@NonNull final LNGDataTransformer dataTransformer, @NonNull final ISequences rawSequences) {

		final LNGEvaluationTransformerUnit evaluationTransformerUnit = new LNGEvaluationTransformerUnit(dataTransformer, dataTransformer.getInitialSequences(), dataTransformer.getHints());
		final Injector injector = evaluationTransformerUnit.getInjector();

		final List<@NonNull IEvaluatedStateConstraintChecker> constraintCheckers = injector.getInstance(Key.get(new TypeLiteral<List<IEvaluatedStateConstraintChecker>>() {
		}));

		final List<IEvaluatedStateConstraintChecker> failedCheckers = new LinkedList<>();

		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();
			final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
			// Apply initial state (store initial lateness etc)
			{
				// // Apply sequence manipulators
				final IModifiableSequences fullSequences = sequencesManipulator.createManipulatedSequences(dataTransformer.getInitialSequences());
				final EvaluationState evaluationState = new EvaluationState();
				final IEvaluationProcess process = injector.getInstance(SchedulerEvaluationProcess.class);
				for (final IEvaluationProcess.Phase phase : IEvaluationProcess.Phase.values()) {
					Assert.assertTrue(process.evaluate(phase, fullSequences, evaluationState));
				}
				// Apply hard constraint checkers
				for (final IEvaluatedStateConstraintChecker checker : constraintCheckers) {
					checker.checkConstraints(rawSequences, fullSequences, evaluationState);
				}
			}
			// Apply to current
			{
				// // Apply sequence manipulators
				final IModifiableSequences fullSequences = sequencesManipulator.createManipulatedSequences(rawSequences);
				final EvaluationState evaluationState = new EvaluationState();
				final IEvaluationProcess process = injector.getInstance(SchedulerEvaluationProcess.class);
				for (final IEvaluationProcess.Phase phase : IEvaluationProcess.Phase.values()) {
					Assert.assertTrue(process.evaluate(phase, fullSequences, evaluationState));
				}
				// Apply hard constraint checkers
				for (final IEvaluatedStateConstraintChecker checker : constraintCheckers) {
					if (!checker.checkConstraints(rawSequences, fullSequences, evaluationState)) {
						failedCheckers.add(checker);
					}
				}
			}
		}

		return failedCheckers.isEmpty() ? null : failedCheckers;
	}

	public static Pair<LNGEvaluationTransformerUnit, List<IConstraintChecker>> getConstraintCheckers(@NonNull final LNGDataTransformer dataTransformer) {

		final LNGEvaluationTransformerUnit evaluationTransformerUnit = new LNGEvaluationTransformerUnit(dataTransformer, dataTransformer.getInitialSequences(), dataTransformer.getHints());
		final List<IConstraintChecker> constraintCheckers = evaluationTransformerUnit.getInjector().getInstance(Key.get(new TypeLiteral<List<IConstraintChecker>>() {
		}));

		return new Pair<>(evaluationTransformerUnit, constraintCheckers);
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