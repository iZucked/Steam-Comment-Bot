/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Date;

import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;

/**
 * A sub-class of {@link LocalSearchOptimiser} implementing a default main loop.
 * 
 * @author Simon Goodall
 * 
 */
public abstract class DefaultLocalSearchOptimiser extends LocalSearchOptimiser {

	protected static final Logger LOG = LoggerFactory.getLogger(DefaultLocalSearchOptimiser.class);

	protected Pair<Integer, Long> best = new Pair<>(0, Long.MAX_VALUE);

	protected static final boolean DO_SEQUENCE_LOGGING = false;

	public static final String RESTART_ITERATIONS_THRESHOLD = "restartIterationsThreshold";
	private int maxRestarts;
	private int currentRestart = 0;

	@Inject
	@Named(LocalSearchOptimiserModule.USE_RESTARTING_OPTIMISER)
	private boolean restartEnabled;

	@Inject
	@Named(RESTART_ITERATIONS_THRESHOLD)
	private int restartInterval;

	protected void logJobState(LSOJobState state) {
		if (loggingDataStore != null) {
			switch (state.getStatus()) {
			case NullMoveFail -> loggingDataStore.logNullMove(state.getMove());
			case CannotValidateFail -> loggingDataStore.logFailedToValidateMove(state.getMove());
			case ConstraintFail -> loggingDataStore.logFailedConstraints((IConstraintChecker) state.getFailedChecker(), state.getMove());
			case EvaluationProcessFail -> {
				/* Nothing to do */ }
			case EvaluatedConstraintFail -> loggingDataStore.logFailedEvaluatedStateConstraints((IEvaluatedStateConstraintChecker) state.getFailedChecker(), state.getMove());
			case Pass -> loggingDataStore.logAppliedMove(state.getMove().getClass().getName());
			}
		}

	}

	protected boolean acceptOrRejectMove(final @NonNull IModifiableSequences pinnedPotentialRawSequences, final @NonNull IModifiableSequences pinnedCurrentRawSequences, LSOJobState state) {
		boolean accepted = getFitnessEvaluator().evaluateSequencesFromFitnessOnly(state.getRawSequences(), state.getEvaluationState(), state.getFullSequences(), state.getFitness());

		if (loggingDataStore != null && (numberOfMovesTried % loggingDataStore.getReportingInterval()) == 0) {
			loggingDataStore.logProgress(numberOfMovesTried, numberOfMovesAccepted, numberOfRejectedMoves, 0, 0, getFitnessEvaluator().getBestFitness(), getFitnessEvaluator().getCurrentFitness(),
					new Date().getTime());
		}

		IMove move = state.getMove();
		if (accepted) {
			numberOfMovesAccepted++;

			if (loggingDataStore != null) {
				loggingDataStore.logSuccessfulMove(move, numberOfMovesTried, getFitnessEvaluator().getLastFitness());
			}

			failedInitialConstraintCheckers = false;

			updateSequences(state.getRawSequences(), pinnedPotentialRawSequences, move.getAffectedResources());
			updateSequencesLookup(pinnedPotentialRawSequences, move.getAffectedResources());
		} else {
			numberOfRejectedMoves++;
			if (loggingDataStore != null) {
				loggingDataStore.logRejectedMove(state.getMove(), numberOfMovesTried, getFitnessEvaluator().getLastFitness());
			}
		}
		return accepted;
	}

	protected boolean isRestartIteration(int numberOfMovesTried) {
		if (restartEnabled) {
			if (numberOfMovesTried > 0 && numberOfMovesTried % restartInterval == 0) {
				return true;
			}
		}
		return false;
	}

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
