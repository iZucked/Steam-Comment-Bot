/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Collection;
import java.util.Date;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IInitialSequencesConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IReducingConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess.Phase;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.INullMove;
import com.mmxlabs.optimiser.lso.logging.ILoggingProvider;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;

/**
 * A sub-class of {@link LocalSearchOptimiser} implementing a default main loop.
 * 
 * @author Simon Goodall
 * 
 */
public class DefaultLocalSearchOptimiser extends LocalSearchOptimiser {
	
	@Inject
	protected IOptimisationData data;

	protected int numberOfMovesTried;

	protected int numberOfMovesAccepted;

	protected int numberOfRejectedMoves;

	protected int numberOfFailedEvaluations;

	protected int numberOfFailedToValidate;

	protected ModifiableSequences currentRawSequences;

	protected ModifiableSequences potentialRawSequences;

	// @Inject(optional = true)
	// @Named(LSOLoggingConstants.LSO_LOGGER)
	protected LSOLogger loggingDataStore;

	@Inject(optional = true)
	protected ILoggingProvider loggingProvider;

	protected Pair<Integer, Long> best = new Pair<>(0, Long.MAX_VALUE);

	protected boolean DO_SEQUENCE_LOGGING = false;

	protected boolean failedInitialConstraintCheckers;

	@Override
	public IAnnotatedSolution start(@NonNull final IOptimisationContext optimiserContext, @NonNull final ISequences initialRawSequences, @NonNull final ISequences inputRawSequences) {
		setCurrentContext(optimiserContext);

		initLogger();
//		data = optimiserContext.getOptimisationData();
		numberOfMovesTried = 0;
		numberOfMovesAccepted = 0;

		final ModifiableSequences currentRawSequences = new ModifiableSequences(inputRawSequences);

		final ModifiableSequences potentialRawSequences = new ModifiableSequences(currentRawSequences.getResources());
		updateSequences(currentRawSequences, potentialRawSequences, currentRawSequences.getResources());

		// Evaluate initial sequences
		setInitialSequences(initialRawSequences);

		// FIXME: This is the only extra line in DefaultLocalSearchOptimiser compared to ArbitraryStateLSO #start methods
		evaluateInputSequences(initialRawSequences);

		evaluateInputSequences(inputRawSequences);

		// Set initial sequences
		getMoveGenerator().setSequences(potentialRawSequences);

		final IAnnotatedSolution annotatedBestSolution = getFitnessEvaluator().getBestAnnotatedSolution();
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

	protected void initLogger() {
		if (loggingProvider != null) {
			loggingDataStore = loggingProvider.providerLSOLogger(getFitnessEvaluator(), getCurrentContext());
		}
	}

	@Override
	public int step(final int percentage) {
		return step(percentage, potentialRawSequences, currentRawSequences);
	}

	protected int step(final int percentage, @NonNull final ModifiableSequences pinnedPotentialRawSequences, @NonNull final ModifiableSequences pinnedCurrentRawSequences) {

		final int iterationsThisStep = Math.min(Math.max(1, (getNumberOfIterations() * percentage) / 100), getNumberOfIterations() - getNumberOfIterationsCompleted());
		MAIN_LOOP: for (int i = 0; i < iterationsThisStep; i++) {
			++numberOfMovesTried;
			if (numberOfMovesTried % 10000 == 0) {
				System.out.println("iteration:" + numberOfMovesTried);
			}
			getFitnessEvaluator().step();
			if (loggingDataStore != null && (numberOfMovesTried % loggingDataStore.getReportingInterval()) == 0) {
				loggingDataStore.logProgress(getNumberOfMovesTried(), getNumberOfMovesAccepted(), getNumberOfRejectedMoves(), getNumberOfFailedEvaluations(), getNumberOfFailedToValidate(),
						getFitnessEvaluator().getBestFitness(), getFitnessEvaluator().getCurrentFitness(), new Date().getTime());
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
			final String moveName = move.getClass().getName();
			if (loggingDataStore != null) {
				loggingDataStore.logAppliedMove(moveName);
				if (DO_SEQUENCE_LOGGING) {
					loggingDataStore.logSequence(pinnedPotentialRawSequences);
				}
			}

			// Apply sequence manipulators
			final IModifiableSequences potentialFullSequences = getSequenceManipulator().createManipulatedSequences(pinnedPotentialRawSequences);

			// Apply hard constraint checkers
			for (final IConstraintChecker checker : getConstraintCheckers()) {
				// For constraint checker changed resources functions, if initial solution is invalid, we want to always perform a full constraint checker set of checks until we accept a valid
				// solution
				final Collection<@NonNull IResource> changedResources = failedInitialConstraintCheckers ? null : move.getAffectedResources();

				if (checker.checkConstraints(potentialFullSequences, changedResources) == false) {
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

					 if (loggingDataStore != null) {
					 loggingDataStore.logFailedEvaluatedStateConstraints(checker, move);
					 if (DO_SEQUENCE_LOGGING) {
					 loggingDataStore.logSequenceFailedEvaluatedStateConstraint(checker, pinnedPotentialRawSequences);
					 }
					 }
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
				getMoveGenerator().setSequences(pinnedPotentialRawSequences);

				++numberOfMovesAccepted;
				if (getFitnessEvaluator().getBestFitness() < best.getSecond()) {
					best.setFirst(getNumberOfMovesTried());
					best.setSecond(getFitnessEvaluator().getBestFitness());
					if (false) {
						System.out.println(best.getFirst() + ":" + best.getSecond());
					}
				}

				// Current state is now accepted, set this flag to false to permit the constraint checker delta functionality
				failedInitialConstraintCheckers = false;

			} else {
				// Failed, reset state for old sequences
				++numberOfRejectedMoves;
				if (loggingDataStore != null) {
					if (DO_SEQUENCE_LOGGING) {
						loggingDataStore.logSequenceRejected(pinnedPotentialRawSequences, getFitnessEvaluator().getCurrentFitness());
					}
					loggingDataStore.logRejectedMove(move, getNumberOfMovesTried(), getFitnessEvaluator().getLastFitness());
				}

				updateSequences(pinnedCurrentRawSequences, pinnedPotentialRawSequences, move.getAffectedResources());
			}
		}

		setNumberOfIterationsCompleted(numberOfMovesTried);

		updateProgressLogs();
		return iterationsThisStep;
	}

	protected void evaluateInputSequences(@NonNull final ISequences currentRawSequences) {
		// Apply sequence manipulators
		final IModifiableSequences fullSequences = new ModifiableSequences(currentRawSequences);
		getSequenceManipulator().manipulate(fullSequences);

		// Prime IReducingConstraintCheckers with initial state
		for (final IReducingConstraintChecker checker : getReducingConstraintCheckers()) {
			checker.sequencesAccepted(fullSequences);
		}

		// Prime IInitialSequencesConstraintCheckers with initial state
		for (final IInitialSequencesConstraintChecker checker : getInitialSequencesConstraintCheckers()) {
			checker.sequencesAccepted(currentRawSequences, fullSequences);
		}

		final IEvaluationState evaluationState = new EvaluationState();
		for (final IEvaluationProcess evaluationProcess : getEvaluationProcesses()) {
			evaluationProcess.evaluate(fullSequences, evaluationState);
		}

		for (final IEvaluatedStateConstraintChecker checker : getEvaluatedStateConstraintCheckers()) {
			checker.checkConstraints(currentRawSequences, fullSequences, evaluationState);
		}

		// Prime fitness cores with initial sequences
		getFitnessEvaluator().setInitialSequences(currentRawSequences, fullSequences, evaluationState);
	}

	protected void setInitialSequences(@NonNull final ISequences initialSequences) {
		// Apply sequence manipulators
		final IModifiableSequences fullSequences = new ModifiableSequences(initialSequences);
		getSequenceManipulator().manipulate(fullSequences);

		// Prime IInitialSequencesConstraintCheckers with initial state
		for (final IInitialSequencesConstraintChecker checker : getInitialSequencesConstraintCheckers()) {
			checker.sequencesAccepted(initialSequences, fullSequences);
		}
	}

	protected void initProgressLog() {
		if (loggingDataStore != null) {
			loggingDataStore.intialiseProgressLog(getFitnessEvaluator().getBestFitness(), getFitnessEvaluator().getCurrentFitness());
		}
	}

	protected void updateProgressLogs() {
		if (loggingDataStore != null) {
			loggingDataStore.setNumberOfMovesTried(getNumberOfMovesTried());
			loggingDataStore.setNumberOfMovesAccepted(getNumberOfMovesAccepted());
			loggingDataStore.setNumberOfRejectedMoves(getNumberOfRejectedMoves());
			loggingDataStore.setNumberOfFailedToValidate(getNumberOfFailedToValidate());
			loggingDataStore.setNumberOfFailedEvaluations(getNumberOfFailedEvaluations());
		}
	}

	public int getNumberOfMovesTried() {
		return numberOfMovesTried;
	}

	public void setNumberOfMovesTried(final int numberOfMovesTried) {
		this.numberOfMovesTried = numberOfMovesTried;
	}

	public int getNumberOfMovesAccepted() {
		return numberOfMovesAccepted;
	}

	public void setNumberOfMovesAccepted(final int numberOfMovesAccepted) {
		this.numberOfMovesAccepted = numberOfMovesAccepted;
	}

	public int getNumberOfRejectedMoves() {
		return numberOfRejectedMoves;
	}

	public void setNumberOfRejectedMoves(final int numberOfRejectedMoves) {
		this.numberOfRejectedMoves = numberOfRejectedMoves;
	}

	public int getNumberOfFailedEvaluations() {
		return numberOfFailedEvaluations;
	}

	public void setNumberOfFailedEvaluations(final int numberOfFailedEvaluations) {
		this.numberOfFailedEvaluations = numberOfFailedEvaluations;
	}

	public int getNumberOfFailedToValidate() {
		return numberOfFailedToValidate;
	}

	public void setNumberOfFailedToValidate(final int numberOfFailedToValidate) {
		this.numberOfFailedToValidate = numberOfFailedToValidate;
	}

}
