/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
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
import com.mmxlabs.optimiser.core.constraints.IReducingContraintChecker;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.IMove;

/**
 * A sub-class of {@link LocalSearchOptimiser} implementing a default main loop.
 * 
 * @author Simon Goodall
 * 
 */
public class DefaultLocalSearchOptimiser extends LocalSearchOptimiser {
	private IOptimisationData data;

	IFitnessEvaluator fitnessEvaluator;

	private List<IConstraintChecker> constraintCheckers;

	private ISequencesManipulator manipulator;

	private int numberOfMovesTried;

	private int numberOfMovesAccepted;

	private ModifiableSequences currentRawSequences;

	private ModifiableSequences potentialRawSequences;

	@Override
	public IAnnotatedSolution start(final IOptimisationContext optimiserContext) {
		setCurrentContext(optimiserContext);
		data = optimiserContext.getOptimisationData();
		fitnessEvaluator = getFitnessEvaluator();
		constraintCheckers = getConstraintCheckers();
		manipulator = getSequenceManipulator();
		numberOfMovesTried = 0;
		numberOfMovesAccepted = 0;

		final ISequences initialSequences = optimiserContext.getInitialSequences();
		currentRawSequences = new ModifiableSequences(initialSequences);
		potentialRawSequences = new ModifiableSequences(currentRawSequences.getResources());
		updateSequences(currentRawSequences, potentialRawSequences, currentRawSequences.getResources());

		// Evaluate initial sequences
		{
			// Apply sequence manipulators
			final IModifiableSequences fullSequences = new ModifiableSequences(currentRawSequences);
			manipulator.manipulate(fullSequences);

			// Prime IReducingConstraintCheckers with initial state
			for (final IReducingContraintChecker checker : getReducingConstraintCheckers()) {
				checker.sequencesAccepted(fullSequences);
			}
			// Prime fitness cores with initial sequences
			fitnessEvaluator.setOptimisationData(data);
			fitnessEvaluator.setInitialSequences(fullSequences);
		}

		// Set initial sequences
		getMoveGenerator().setSequences(potentialRawSequences);

		final IAnnotatedSolution annotatedBestSolution = fitnessEvaluator.getBestAnnotatedSolution(optimiserContext, false);

		annotatedBestSolution.setGeneralAnnotation(OptimiserConstants.G_AI_iterations, 0);
		annotatedBestSolution.setGeneralAnnotation(OptimiserConstants.G_AI_runtime, 0l);

		setStartTime(System.currentTimeMillis());

		setNumberOfIterationsCompleted(0);

		return annotatedBestSolution;
	}

	@Override
	public int step(final int percentage) {
		final int iterationsThisStep = Math.min(Math.max(1, (getNumberOfIterations() * percentage) / 100), getNumberOfIterations() - getNumberOfIterationsCompleted());
		MAIN_LOOP: for (int i = 0; i < iterationsThisStep; i++) {
			++numberOfMovesTried;

			// Generate a new move
			final IMove move = getMoveGenerator().generateMove();

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
			final IModifiableSequences potentialFullSequences = new ModifiableSequences(potentialRawSequences);
			manipulator.manipulate(potentialFullSequences);

			// Apply hard constraint checkers
			for (final IConstraintChecker checker : constraintCheckers) {
				if (checker.checkConstraints(potentialFullSequences) == false) {
					// Reject Move
					updateSequences(currentRawSequences, potentialRawSequences, move.getAffectedResources());
					// Break out
					continue MAIN_LOOP;
				}
			}

			// Test move and update state if accepted
			if (fitnessEvaluator.evaluateSequences(potentialFullSequences, move.getAffectedResources())) {

				// Update IReducingConstraintCheckers with new state
				for (final IReducingContraintChecker checker : getReducingConstraintCheckers()) {
					checker.sequencesAccepted(potentialFullSequences);
				}

				// Success update state for new sequences
				updateSequences(potentialRawSequences, currentRawSequences, move.getAffectedResources());

				// Update move sequences.
				getMoveGenerator().setSequences(potentialRawSequences);

				++numberOfMovesAccepted;
			} else {
				// Failed, reset state for old sequences
				updateSequences(currentRawSequences, potentialRawSequences, move.getAffectedResources());
			}
		}

		setNumberOfIterationsCompleted(numberOfMovesTried);

		return iterationsThisStep;
	}
}
