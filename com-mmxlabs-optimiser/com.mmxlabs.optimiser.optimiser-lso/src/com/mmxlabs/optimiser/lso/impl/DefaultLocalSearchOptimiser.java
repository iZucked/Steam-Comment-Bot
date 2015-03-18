/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
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
import com.mmxlabs.optimiser.core.constraints.IReducingContraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
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

	private int numberOfMovesTried;

	private int numberOfMovesAccepted;

	private ModifiableSequences currentRawSequences;

	private ModifiableSequences potentialRawSequences;

	@Override
	public IAnnotatedSolution start(@NonNull final IOptimisationContext optimiserContext) {
		setCurrentContext(optimiserContext);
		data = optimiserContext.getOptimisationData();
		numberOfMovesTried = 0;
		numberOfMovesAccepted = 0;

		final ISequences initialSequences = optimiserContext.getInitialSequences();
		final ModifiableSequences currentRawSequences = new ModifiableSequences(initialSequences);
		final ModifiableSequences potentialRawSequences = new ModifiableSequences(currentRawSequences.getResources());
		updateSequences(currentRawSequences, potentialRawSequences, currentRawSequences.getResources());

		// Evaluate initial sequences
		{
			// Apply sequence manipulators
			final IModifiableSequences fullSequences = new ModifiableSequences(currentRawSequences);
			getSequenceManipulator().manipulate(fullSequences);

			// Prime IReducingConstraintCheckers with initial state
			for (final IReducingContraintChecker checker : getReducingConstraintCheckers()) {
				checker.sequencesAccepted(fullSequences);
			}

			final IEvaluationState evaluationState = new EvaluationState();
			for (final IEvaluationProcess evaluationProcess : getEvaluationProcesses()) {
				evaluationProcess.evaluate(fullSequences, evaluationState);
			}

			// Prime fitness cores with initial sequences
			getFitnessEvaluator().setOptimisationData(data);
			getFitnessEvaluator().setInitialSequences(fullSequences, evaluationState);
		}

		// Set initial sequences
		getMoveGenerator().setSequences(potentialRawSequences);

		final IAnnotatedSolution annotatedBestSolution = getFitnessEvaluator().getBestAnnotatedSolution(optimiserContext);
		if (annotatedBestSolution == null) {
			return null;
		}

		annotatedBestSolution.setGeneralAnnotation(OptimiserConstants.G_AI_iterations, 0);
		annotatedBestSolution.setGeneralAnnotation(OptimiserConstants.G_AI_runtime, 0l);

		setStartTime(System.currentTimeMillis());

		setNumberOfIterationsCompleted(0);

		this.currentRawSequences = currentRawSequences;
		this.potentialRawSequences = potentialRawSequences;

		return annotatedBestSolution;
	}

	@Override
	public int step(final int percentage) {
		return step(percentage, potentialRawSequences, currentRawSequences);
	}

	protected int step(final int percentage, @NonNull final ModifiableSequences pinnedPotentialRawSequences, @NonNull final ModifiableSequences pinnedCurrentRawSequences) {

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
			if (!move.validate(pinnedPotentialRawSequences)) {
				continue;
			}

			// Update potential sequences
			move.apply(pinnedPotentialRawSequences);

			// Apply sequence manipulators
			final IModifiableSequences potentialFullSequences = new ModifiableSequences(pinnedPotentialRawSequences);
			getSequenceManipulator().manipulate(potentialFullSequences);

			// Apply hard constraint checkers
			for (final IConstraintChecker checker : getConstraintCheckers()) {
				if (checker.checkConstraints(potentialFullSequences) == false) {
					// Reject Move
					updateSequences(pinnedCurrentRawSequences, pinnedPotentialRawSequences, move.getAffectedResources());
					// Break out
					continue MAIN_LOOP;
				}
			}

			final IEvaluationState evaluationState = new EvaluationState();
			for (final IEvaluationProcess evaluationProcess : getEvaluationProcesses()) {
				if (!evaluationProcess.evaluate(potentialFullSequences, evaluationState)) {
					// Problem evaluating, reject move
					updateSequences(pinnedCurrentRawSequences, pinnedPotentialRawSequences, move.getAffectedResources());
					continue MAIN_LOOP;
				}
			}

			// Test move and update state if accepted
			if (getFitnessEvaluator().evaluateSequences(potentialFullSequences, evaluationState, move.getAffectedResources())) {

				// Update IReducingConstraintCheckers with new state
				for (final IReducingContraintChecker checker : getReducingConstraintCheckers()) {
					checker.sequencesAccepted(potentialFullSequences);
				}

				// Success update state for new sequences
				updateSequences(pinnedPotentialRawSequences, pinnedCurrentRawSequences, move.getAffectedResources());

				// Update move sequences.
				getMoveGenerator().setSequences(pinnedPotentialRawSequences);

				++numberOfMovesAccepted;
			} else {
				// Failed, reset state for old sequences
				updateSequences(pinnedCurrentRawSequences, pinnedPotentialRawSequences, move.getAffectedResources());
			}
		}

		setNumberOfIterationsCompleted(numberOfMovesTried);

		return iterationsThisStep;
	}
}
