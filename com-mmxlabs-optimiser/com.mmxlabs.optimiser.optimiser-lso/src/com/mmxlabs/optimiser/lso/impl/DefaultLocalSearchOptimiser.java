/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Date;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
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
import com.mmxlabs.optimiser.lso.INullMove;
import com.mmxlabs.optimiser.lso.LSOLoggingConstants;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;

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

	private int numberOfRejectedMoves;

	private int numberOfFailedEvaluations;

	private int numberOfFailedToValidate;

	private ModifiableSequences currentRawSequences;

	private ModifiableSequences potentialRawSequences;

	@Inject(optional = true)
	@Named(LSOLoggingConstants.LSO_LOGGER)
	private LSOLogger loggingDataStore;

	private Pair<Integer, Long> best = new Pair<>(0,0L);
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

		if (loggingDataStore != null) {
			loggingDataStore.intialiseProgressLog(getFitnessEvaluator().getBestFitness(), getFitnessEvaluator().getCurrentFitness());
		}
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
			if (loggingDataStore != null && (numberOfMovesTried % loggingDataStore.getReportingInterval()) == 0) {
				loggingDataStore.logProgress(numberOfMovesTried, numberOfMovesAccepted, numberOfRejectedMoves, numberOfFailedEvaluations, numberOfFailedToValidate, getFitnessEvaluator()
						.getBestFitness(), getFitnessEvaluator().getCurrentFitness(), new Date().getTime());
			}

			// Generate a new move
			final IMove move = getMoveGenerator().generateMove();

			// Make sure the generator was able to generate a move
			if (move == null || move instanceof INullMove) {
				if (loggingDataStore != null) {
					loggingDataStore.logNullMove(move);
				}
				continue;
			}

			// Test move is valid against data.
			if (!move.validate(pinnedPotentialRawSequences)) {
				++numberOfFailedToValidate;
				if (loggingDataStore != null) {
					loggingDataStore.logFailedToValidateMove(move);
				}
				continue;
			}

			// Update potential sequences
			move.apply(pinnedPotentialRawSequences);
			String moveName = move.getClass().getName();
			if (loggingDataStore != null) {
				loggingDataStore.logAppliedMove(moveName);
				loggingDataStore.logSequence(pinnedPotentialRawSequences);
			}

			// Apply sequence manipulators
			final IModifiableSequences potentialFullSequences = new ModifiableSequences(pinnedPotentialRawSequences);
			getSequenceManipulator().manipulate(potentialFullSequences);

			// Apply hard constraint checkers
			for (final IConstraintChecker checker : getConstraintCheckers()) {
				if (checker.checkConstraints(potentialFullSequences) == false) {
					if (loggingDataStore != null) {
						loggingDataStore.logFailedConstraints(checker, move);
						loggingDataStore.logSequenceFailedConstraint(checker, pinnedPotentialRawSequences);
					}

					// Reject Move
					updateSequences(pinnedCurrentRawSequences, pinnedPotentialRawSequences, move.getAffectedResources());
					// Break out
					continue MAIN_LOOP;
				}
			}

			if (loggingDataStore != null) {
				loggingDataStore.logSequence(pinnedPotentialRawSequences);
			}

			final IEvaluationState evaluationState = new EvaluationState();
			for (final IEvaluationProcess evaluationProcess : getEvaluationProcesses()) {
				if (!evaluationProcess.evaluate(potentialFullSequences, evaluationState)) {
					// Problem evaluating, reject move
					++numberOfFailedEvaluations;

					updateSequences(pinnedCurrentRawSequences, pinnedPotentialRawSequences, move.getAffectedResources());
					continue MAIN_LOOP;
				}
			}

			// Test move and update state if accepted
			if (getFitnessEvaluator().evaluateSequences(potentialRawSequences, potentialFullSequences, evaluationState, move.getAffectedResources())) {

				// Update IReducingConstraintCheckers with new state
				for (final IReducingContraintChecker checker : getReducingConstraintCheckers()) {
					checker.sequencesAccepted(potentialFullSequences);
				}
				if (loggingDataStore != null) {
					loggingDataStore.logSequenceAccepted(pinnedPotentialRawSequences, getFitnessEvaluator().getCurrentFitness());
					loggingDataStore.logSuccessfulMove(move);
				}

				// Success update state for new sequences
				updateSequences(pinnedPotentialRawSequences, pinnedCurrentRawSequences, move.getAffectedResources());

				// Update move sequences.
				getMoveGenerator().setSequences(pinnedPotentialRawSequences);

				++numberOfMovesAccepted;
				if (getFitnessEvaluator().getBestFitness() < best.getSecond()) {
					best.setFirst(numberOfMovesTried);
					best.setSecond(getFitnessEvaluator().getBestFitness());
					System.out.println(best.getFirst()+":"+best.getSecond());
				}
			} else {
				// Failed, reset state for old sequences
				++numberOfRejectedMoves;
				if (loggingDataStore != null) {
					loggingDataStore.logSequenceRejected(pinnedPotentialRawSequences, getFitnessEvaluator().getCurrentFitness());
				}
				updateSequences(pinnedCurrentRawSequences, pinnedPotentialRawSequences, move.getAffectedResources());
			}
		}

		setNumberOfIterationsCompleted(numberOfMovesTried);
		return iterationsThisStep;
	}

}
