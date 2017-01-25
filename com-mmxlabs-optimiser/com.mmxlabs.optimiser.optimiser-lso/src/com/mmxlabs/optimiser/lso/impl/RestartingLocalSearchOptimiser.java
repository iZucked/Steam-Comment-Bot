/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Date;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IReducingConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess.Phase;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.INullMove;
import com.mmxlabs.optimiser.lso.IRestartingOptimiser;

/**
 * A reheating local search optimiser.
 * 
 * @author Alex Churchill
 * 
 */
public class RestartingLocalSearchOptimiser extends DefaultLocalSearchOptimiser implements IRestartingOptimiser {

	public static final String RESTART_ITERATIONS_THRESHOLD = "restartIterationsThreshold";
	private int maxRestarts;
	private int currentRestart = 0;

	@Inject
	@Named(RESTART_ITERATIONS_THRESHOLD)
	private int restartInterval;

	@Override
	protected int step(final int percentage, @NonNull final ModifiableSequences pinnedPotentialRawSequences, @NonNull final ModifiableSequences pinnedCurrentRawSequences) {

		final int iterationsThisStep = Math.min(Math.max(1, (getNumberOfIterations() * percentage) / 100), getNumberOfIterations() - getNumberOfIterationsCompleted());
		MAIN_LOOP: for (int i = 0; i < iterationsThisStep; i++) {
			getFitnessEvaluator().step();
			setNumberOfMovesTried(getNumberOfMovesTried() + 1);
			if (numberOfMovesTried % 10000 == 0) {
				System.out.println("iteration:" + numberOfMovesTried);
			}
			if (isRestartIteration(getNumberOfMovesTried())) {
				restart();
			}
			if (loggingDataStore != null && (getNumberOfMovesTried() % loggingDataStore.getReportingInterval()) == 0) {
				loggingDataStore.logProgress(getNumberOfMovesTried(), getNumberOfMovesAccepted(), getNumberOfRejectedMoves(), getNumberOfFailedEvaluations(), getNumberOfFailedToValidate(),
						getFitnessEvaluator().getBestFitness(), getFitnessEvaluator().getCurrentFitness(), new Date().getTime());
			}

			// Generate a new move
			final IMove move = getMoveGenerator().generateMove(potentialRawSequences, getLookupManager(), getRandom());

			// Make sure the generator was able to generate a move
			if (move == null || move instanceof INullMove) {
				if (loggingDataStore != null) {
					loggingDataStore.logNullMove(move);
				}
				continue;
			}

			// Test move is valid against data.
			if (!move.validate(pinnedPotentialRawSequences)) {
				setNumberOfFailedToValidate(getNumberOfFailedToValidate() + 1);
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
				if (DO_SEQUENCE_LOGGING) {
					loggingDataStore.logSequence(pinnedPotentialRawSequences);
				}
			}

			// Apply sequence manipulators
			final IModifiableSequences potentialFullSequences = new ModifiableSequences(pinnedPotentialRawSequences);
			getSequenceManipulator().manipulate(potentialFullSequences);

			// Apply hard constraint checkers
			for (final IConstraintChecker checker : getConstraintCheckers()) {
				if (checker.checkConstraints(potentialFullSequences, move.getAffectedResources()) == false) {
					if (loggingDataStore != null) {
						loggingDataStore.logFailedConstraints(checker, move);
						if (DO_SEQUENCE_LOGGING) {
							loggingDataStore.logSequenceFailedConstraint(checker, pinnedPotentialRawSequences);
						}
					}

					// Reject Move
					updateSequences(pinnedCurrentRawSequences, pinnedPotentialRawSequences, move.getAffectedResources());
					// Break out
					continue MAIN_LOOP;
				}
			}

			if (loggingDataStore != null) {
				if (DO_SEQUENCE_LOGGING) {
					loggingDataStore.logSequence(pinnedPotentialRawSequences);
				}
			}

			final IEvaluationState evaluationState = new EvaluationState();
			for (final IEvaluationProcess evaluationProcess : getEvaluationProcesses()) {
				if (!evaluationProcess.evaluate(Phase.Checked_Evaluation, potentialFullSequences, evaluationState)) {
					// Problem evaluating, reject move
					++numberOfFailedEvaluations;

					updateSequences(pinnedCurrentRawSequences, pinnedPotentialRawSequences, move.getAffectedResources());
					continue MAIN_LOOP;
				}
			}
			// Apply hard constraint checkers
			for (final IEvaluatedStateConstraintChecker checker : getEvaluatedStateConstraintCheckers()) {
				if (checker.checkConstraints(potentialRawSequences, potentialFullSequences, evaluationState) == false) {
					// Problem evaluating, reject move
					++numberOfFailedEvaluations;

					updateSequences(pinnedCurrentRawSequences, pinnedPotentialRawSequences, move.getAffectedResources());
					// Break out
					continue MAIN_LOOP;
				}
			}

			for (final IEvaluationProcess evaluationProcess : getEvaluationProcesses()) {
				if (!evaluationProcess.evaluate(Phase.Final_Evaluation, potentialFullSequences, evaluationState)) {
					// Problem evaluating, reject move
					++numberOfFailedEvaluations;

					updateSequences(pinnedCurrentRawSequences, pinnedPotentialRawSequences, move.getAffectedResources());
					continue MAIN_LOOP;
				}
			}

			// Test move and update state if accepted
			if (getFitnessEvaluator().evaluateSequences(potentialRawSequences, potentialFullSequences, evaluationState, move.getAffectedResources())) {

				// Update IReducingConstraintCheckers with new state
				for (final IReducingConstraintChecker checker : getReducingConstraintCheckers()) {
					checker.sequencesAccepted(potentialFullSequences);
				}
				if (loggingDataStore != null) {
					if (DO_SEQUENCE_LOGGING) {
						loggingDataStore.logSequenceAccepted(pinnedPotentialRawSequences, getFitnessEvaluator().getCurrentFitness());
					}
					loggingDataStore.logSuccessfulMove(move, getNumberOfMovesTried(), getFitnessEvaluator().getLastFitness());
				}

				// Success update state for new sequences
				updateSequences(pinnedPotentialRawSequences, pinnedCurrentRawSequences, move.getAffectedResources());

				// Update move sequences.
				setSequences(pinnedPotentialRawSequences);

				setNumberOfMovesAccepted(getNumberOfMovesAccepted() + 1);
				if (getFitnessEvaluator().getBestFitness() < best.getSecond()) {
					best.setFirst(getNumberOfMovesTried());
					best.setSecond(getFitnessEvaluator().getBestFitness());
					if (false) {
						System.out.println(best.getFirst() + ":" + best.getSecond());
					}
				}
			} else {
				// Failed, reset state for old sequences
				setNumberOfRejectedMoves(getNumberOfRejectedMoves() + 1);
				if (loggingDataStore != null) {
					if (DO_SEQUENCE_LOGGING) {
						loggingDataStore.logSequenceRejected(pinnedPotentialRawSequences, getFitnessEvaluator().getCurrentFitness());
					}
					loggingDataStore.logRejectedMove(move, getNumberOfMovesTried(), getFitnessEvaluator().getLastFitness());
				}
				updateSequences(pinnedCurrentRawSequences, pinnedPotentialRawSequences, move.getAffectedResources());
			}
		}

		updateProgressLogs();
		setNumberOfIterationsCompleted(getNumberOfMovesTried());
		return iterationsThisStep;
	}

	private boolean isRestartIteration(int numberOfMovesTried) {
		if (numberOfMovesTried > 0 && numberOfMovesTried % restartInterval == 0) {
			return true;
		}
		return false;
	}

	@Override
	public void restart() {
		currentRestart++;
		currentRawSequences = new ModifiableSequences(initialRawSequences);
		potentialRawSequences = new ModifiableSequences(potentialRawSequences);
		System.out.println("Restarting:" + currentRestart);
		getFitnessEvaluator().restart();
	}

	public int getMaxRestarts() {
		return maxRestarts;
	}

	public void setMaxRestarts(int maxRestarts) {
		this.maxRestarts = maxRestarts;
	}

	public int getRestartInterval() {
		return restartInterval;
	}

	public void setRestartInterval(int restartInterval) {
		this.restartInterval = restartInterval;
	}

}
