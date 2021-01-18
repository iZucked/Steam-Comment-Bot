/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
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
	
	protected static final Logger LOG = LoggerFactory.getLogger(DefaultLocalSearchOptimiser.class);

	@Inject
	protected IPhaseOptimisationData data;

	protected int numberOfMovesTried;

	protected int numberOfMovesAccepted;

	protected int numberOfRejectedMoves;

	protected int numberOfFailedEvaluations;

	protected int numberOfFailedToValidate;

	protected ModifiableSequences initialRawSequences;

	protected ModifiableSequences currentRawSequences;

	protected ModifiableSequences potentialRawSequences;

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
		// data = optimiserContext.getOptimisationData();
		numberOfMovesTried = 0;
		numberOfMovesAccepted = 0;

		final ModifiableSequences lCurrentRawSequences = new ModifiableSequences(inputRawSequences);

		final ModifiableSequences lPotentialRawSequences = new ModifiableSequences(lCurrentRawSequences.getResources());
		updateSequences(lCurrentRawSequences, lPotentialRawSequences, lCurrentRawSequences.getResources());

		// Evaluate initial sequences
		setInitialSequences(initialRawSequences);

		// FIXME: This is the only extra line in DefaultLocalSearchOptimiser compared to ArbitraryStateLSO #start methods
		evaluateInputSequences(initialRawSequences);

		evaluateInputSequences(inputRawSequences);

		// Set initial sequences
		updateSequencesLookup(lPotentialRawSequences, null);

		final IAnnotatedSolution annotatedBestSolution = getFitnessEvaluator().getBestAnnotatedSolution();
		if (annotatedBestSolution == null) {
			return null;
		}

		annotatedBestSolution.setGeneralAnnotation(OptimiserConstants.G_AI_iterations, 0);
		annotatedBestSolution.setGeneralAnnotation(OptimiserConstants.G_AI_runtime, 0L);

		// For constraint checker changed resources functions, if initial solution is invalid, we want to always perform a full constraint checker set of checks.
		this.failedInitialConstraintCheckers = false;
		// Apply sequence manipulators
		final IModifiableSequences potentialFullSequences = getSequenceManipulator().createManipulatedSequences(lCurrentRawSequences);

		final List<String> messages = new ArrayList<>();
		messages.add(String.format("%s: start", this.getClass().getName()));
		// Apply hard constraint checkers
		for (final IConstraintChecker checker : getConstraintCheckers()) {
			if (!checker.checkConstraints(potentialFullSequences, null, messages)) {
				// Set break point here!
				// checker.checkConstraints(potentialFullSequences, null);

				failedInitialConstraintCheckers = true;
				break;
			}
		}
		if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES && !messages.isEmpty())
			messages.stream().forEach(LOG::debug);
		setStartTime(System.currentTimeMillis());

		setNumberOfIterationsCompleted(0);

		this.currentRawSequences = lCurrentRawSequences;
		this.potentialRawSequences = lPotentialRawSequences;

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
			initNextIteration();

			// Generate a new move
			final IMove move = generateNewMove();

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
			applyNewMove(pinnedPotentialRawSequences, move);

			// Apply sequence manipulators
			final IModifiableSequences potentialFullSequences = getSequenceManipulator().createManipulatedSequences(pinnedPotentialRawSequences);

			// Apply hard constraint checkers
			if (!applyHardConstraints(pinnedPotentialRawSequences, pinnedCurrentRawSequences, move, potentialFullSequences)) {
				continue MAIN_LOOP;
			}

			if (loggingDataStore != null) {
				if (DO_SEQUENCE_LOGGING) {
					loggingDataStore.logSequence(pinnedPotentialRawSequences);
				}
			}

			final IEvaluationState evaluationState = getEvaluationState(pinnedPotentialRawSequences, pinnedCurrentRawSequences, move, potentialFullSequences);
			if (evaluationState == null) {
				continue MAIN_LOOP;
			}

			// Apply hard constraint checkers
			if (!applyHardEvaluatedConstraintCheckers(pinnedPotentialRawSequences, pinnedCurrentRawSequences, move, potentialFullSequences, evaluationState)) {
				continue MAIN_LOOP;
			}

			if (!evaluateOnEvaluationProcesses(pinnedPotentialRawSequences, pinnedCurrentRawSequences, move, potentialFullSequences, evaluationState)) {
				continue MAIN_LOOP;
			}

			// Test move and update state if accepted
			acceptOrRejectMove(pinnedPotentialRawSequences, pinnedCurrentRawSequences, move, potentialFullSequences, evaluationState);
		}

		setNumberOfIterationsCompleted(numberOfMovesTried);

		updateProgressLogs();
		return iterationsThisStep;
	}

	protected void acceptOrRejectMove(final @NonNull IModifiableSequences pinnedPotentialRawSequences, final @NonNull IModifiableSequences pinnedCurrentRawSequences, final IMove move,
			final @NonNull ISequences potentialFullSequences, final @NonNull IEvaluationState evaluationState) {
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
			updateSequencesLookup(pinnedPotentialRawSequences, move.getAffectedResources());

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

	protected boolean evaluateOnEvaluationProcesses(final @NonNull IModifiableSequences pinnedPotentialRawSequences, final @NonNull ISequences pinnedCurrentRawSequences, final IMove move,
			final @NonNull ISequences potentialFullSequences, final @NonNull IEvaluationState evaluationState) {
		for (final IEvaluationProcess evaluationProcess : getEvaluationProcesses()) {
			if (!evaluationProcess.evaluate(Phase.Final_Evaluation, potentialFullSequences, evaluationState)) {
				// Problem evaluating, reject move
				++numberOfFailedEvaluations;

				updateSequences(pinnedCurrentRawSequences, pinnedPotentialRawSequences, move.getAffectedResources());
				return false;
			}
		}
		return true;
	}

	protected boolean applyHardEvaluatedConstraintCheckers(final @NonNull IModifiableSequences pinnedPotentialRawSequences, final @NonNull ISequences pinnedCurrentRawSequences, final IMove move,
			final @NonNull ISequences potentialFullSequences, final @NonNull IEvaluationState evaluationState) {
		for (final IEvaluatedStateConstraintChecker checker : getEvaluatedStateConstraintCheckers()) {
			if (!checker.checkConstraints(potentialRawSequences, potentialFullSequences, evaluationState)) {
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
				return false;
			}
		}
		return true;
	}

	protected IEvaluationState getEvaluationState(final @NonNull IModifiableSequences pinnedPotentialRawSequences, final @NonNull ISequences pinnedCurrentRawSequences, final IMove move,
			final @NonNull ISequences potentialFullSequences) {
		final IEvaluationState evaluationState = new EvaluationState();
		for (final IEvaluationProcess evaluationProcess : getEvaluationProcesses()) {
			if (!evaluationProcess.evaluate(Phase.Checked_Evaluation, potentialFullSequences, evaluationState)) {
				// Problem evaluating, reject move
				++numberOfFailedEvaluations;

				updateSequences(pinnedCurrentRawSequences, pinnedPotentialRawSequences, move.getAffectedResources());
				return null;
			}
		}
		return evaluationState;
	}

	protected boolean applyHardConstraints(final @NonNull IModifiableSequences pinnedPotentialRawSequences, final @NonNull ISequences pinnedCurrentRawSequences, final IMove move,
			final @NonNull ISequences potentialFullSequences) {
		final List<String> messages = new ArrayList<>();
		messages.add(String.format("%s: applyHardConstraints", this.getClass().getName()));
		for (final IConstraintChecker checker : getConstraintCheckers()) {
			// For constraint checker changed resources functions, if initial solution is invalid, we want to always perform a full constraint checker set of checks until we accept a valid
			// solution
			final Collection<@NonNull IResource> changedResources = failedInitialConstraintCheckers ? null : move.getAffectedResources();

			if (!checker.checkConstraints(potentialFullSequences, changedResources, messages)) {
				if (loggingDataStore != null) {
					loggingDataStore.logFailedConstraints(checker, move);
					if (DO_SEQUENCE_LOGGING) {
						loggingDataStore.logSequenceFailedConstraint(checker, pinnedPotentialRawSequences);
					}
				}
				// Reject Move
				updateSequences(pinnedCurrentRawSequences, pinnedPotentialRawSequences, move.getAffectedResources());
				// Break out
				
				if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES && !messages.isEmpty())
					messages.stream().forEach(LOG::debug);
				return false;
				// continue MAIN_LOOP;
			}
		}
		return true;
	}

	protected void applyNewMove(final @NonNull IModifiableSequences pinnedPotentialRawSequences, final IMove move) {
		move.apply(pinnedPotentialRawSequences);
		final String moveName = move.getClass().getName();
		if (loggingDataStore != null) {
			loggingDataStore.logAppliedMove(moveName);
			if (DO_SEQUENCE_LOGGING) {
				loggingDataStore.logSequence(pinnedPotentialRawSequences);
			}
		}
	}

	protected IMove generateNewMove() {
		final IMove move = getMoveGenerator().generateMove(potentialRawSequences, getLookupManager(), getRandom());
		return move;
	}

	protected void initNextIteration() {
		stepIteration();
		++numberOfMovesTried;
		if (numberOfMovesTried % 10000 == 0) {
			System.out.println("iteration:" + numberOfMovesTried);
		}
		if (loggingDataStore != null && (numberOfMovesTried % loggingDataStore.getReportingInterval()) == 0) {
			loggingDataStore.logProgress(getNumberOfMovesTried(), getNumberOfMovesAccepted(), getNumberOfRejectedMoves(), getNumberOfFailedEvaluations(), getNumberOfFailedToValidate(),
					getFitnessEvaluator().getBestFitness(), getFitnessEvaluator().getCurrentFitness(), new Date().getTime());
		}
	}

	protected void stepIteration() {
		getFitnessEvaluator().step();
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
			checker.sequencesAccepted(currentRawSequences, fullSequences, new ArrayList<>());
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
		// Store initialSequences
		initialRawSequences = new ModifiableSequences(initialSequences);
		// Apply sequence manipulators
		final IModifiableSequences fullSequences = new ModifiableSequences(initialSequences);
		getSequenceManipulator().manipulate(fullSequences);

		// Prime IInitialSequencesConstraintCheckers with initial state
		for (final IInitialSequencesConstraintChecker checker : getInitialSequencesConstraintCheckers()) {
			checker.sequencesAccepted(initialSequences, fullSequences, new ArrayList<>());
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
