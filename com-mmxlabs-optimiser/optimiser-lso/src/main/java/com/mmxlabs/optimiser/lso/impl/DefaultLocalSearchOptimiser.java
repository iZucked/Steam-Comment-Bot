/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.List;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.IMove;

/**
 * A sub-class of {@link LocalSearchOptimiser} implementing a default main loop.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public class DefaultLocalSearchOptimiser<T> extends LocalSearchOptimiser<T> {
	private IOptimisationData<T> data;

	IFitnessEvaluator<T> fitnessEvaluator;

	private List<IConstraintChecker<T>> constraintCheckers;

	private ISequencesManipulator<T> manipulator;

	private int numberOfMovesTried;

	private int numberOfMovesAccepted;

	private ModifiableSequences<T> currentRawSequences;

	private ModifiableSequences<T> potentialRawSequences;

	public IAnnotatedSolution<T> start(
			final IOptimisationContext<T> optimiserContext) {
		setCurrentContext(optimiserContext);
		data = optimiserContext.getOptimisationData();
		fitnessEvaluator = getFitnessEvaluator();
		constraintCheckers = getConstraintCheckers();
		manipulator = getSequenceManipulator();
		numberOfMovesTried = 0;
		numberOfMovesAccepted = 0;

		final ISequences<T> initialSequences = optimiserContext
				.getInitialSequences();
		currentRawSequences = new ModifiableSequences<T>(initialSequences);
		potentialRawSequences = new ModifiableSequences<T>(
				currentRawSequences.getResources());
		updateSequences(currentRawSequences, potentialRawSequences,
				currentRawSequences.getResources());

		// Evaluate initial sequences
		{
			// Apply sequence manipulators
			final IModifiableSequences<T> fullSequences = new ModifiableSequences<T>(
					currentRawSequences);
			manipulator.manipulate(fullSequences);

			// Prime fitness cores with initial sequences
			fitnessEvaluator.setOptimisationData(data);
			fitnessEvaluator.setInitialSequences(fullSequences);
		}

		// Set initial sequences
		getMoveGenerator().setSequences(potentialRawSequences);

		final IAnnotatedSolution<T> annotatedBestSolution = fitnessEvaluator
				.getBestAnnotatedSolution(optimiserContext, false);
		// fitnessEvaluator.getBestSequences()

		annotatedBestSolution.setGeneralAnnotation(
				OptimiserConstants.G_AI_iterations, 0);
		annotatedBestSolution.setGeneralAnnotation(
				OptimiserConstants.G_AI_runtime, 0l);

		setStartTime(System.currentTimeMillis());

		setNumberOfIterationsCompleted(0);

		return annotatedBestSolution;
	}

	public int step(final int percentage) {
		final int iterationsThisStep = Math.min(
				Math.max(1, getNumberOfIterations() * percentage / 100),
				getNumberOfIterations() - getNumberOfIterationsCompleted());
		MAIN_LOOP: for (int i = 0; i < iterationsThisStep; i++) {
			++numberOfMovesTried;

			// Generate a new move
			final IMove<T> move = getMoveGenerator().generateMove();

			// Make sure the generator was able to generate a move
			if (move == null) {
				continue;
			}

			// Test move is valid against data.
			if (!move.validate(potentialRawSequences)) {
				continue;
			}

			// Update potential sequences
			move.apply(potentialRawSequences);

			// Apply sequence manipulators
			final IModifiableSequences<T> potentialFullSequences = new ModifiableSequences<T>(
					potentialRawSequences);
			manipulator.manipulate(potentialFullSequences);

			// Apply hard constraint checkers
			for (final IConstraintChecker<T> checker : constraintCheckers) {
				if (checker.checkConstraints(potentialFullSequences) == false) {
					// Reject Move
					updateSequences(currentRawSequences, potentialRawSequences,
							move.getAffectedResources());
					// Break out
					continue MAIN_LOOP;
				}
			}

			// Test move and update state if accepted
			if (fitnessEvaluator.evaluateSequences(potentialFullSequences,
					move.getAffectedResources())) {

				// Success update state for new sequences
				updateSequences(potentialRawSequences, currentRawSequences,
						move.getAffectedResources());

				// Update move sequences.
				getMoveGenerator().setSequences(potentialRawSequences);

				++numberOfMovesAccepted;
			} else {
				// Failed, reset state for old sequences
				updateSequences(currentRawSequences, potentialRawSequences,
						move.getAffectedResources());
			}
		}

		setNumberOfIterationsCompleted(numberOfMovesTried);

		return iterationsThisStep;
	}
}
