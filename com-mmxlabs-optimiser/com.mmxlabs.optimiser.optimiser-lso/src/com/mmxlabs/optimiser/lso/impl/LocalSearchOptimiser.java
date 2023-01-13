/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.optimiser.common.components.ILookupManager;
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
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.impl.AbstractSequencesOptimiser;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.optimiser.lso.ILocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.logging.ILoggingProvider;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;

/**
 * Main class implementing a Local Search Optimiser. While the actual
 * optimisation loop is left to sub-classes, this class provides the bulk of the
 * implementation to simplify such loops.
 * 
 * @author Simon Goodall
 * 
 */
public abstract class LocalSearchOptimiser extends AbstractSequencesOptimiser implements ILocalSearchOptimiser {

	protected static final Logger LOG = LoggerFactory.getLogger(LocalSearchOptimiser.class);

	@Inject
	protected IPhaseOptimisationData data;

	@Inject(optional = true)
	protected ILoggingProvider loggingProvider;

	@Inject
	private ILookupManager lookupManager;

	protected int numberOfMovesTried = 0;

	protected int numberOfMovesAccepted = 0;

	protected int numberOfRejectedMoves = 0;

	protected int numberOfFailedEvaluations = 0;

	protected int numberOfFailedToValidate = 0;

	private IMoveGenerator moveGenerator;

	protected LSOLogger loggingDataStore;

	protected ModifiableSequences initialRawSequences;

	protected ModifiableSequences currentRawSequences;
	protected ModifiableSequences potentialRawSequences;
	protected boolean failedInitialConstraintCheckers;

	/**
	 * Initialise method checking the object has all the correct pieces of data to
	 * be able to perform the
	 * {@link #optimise(IOptimisationContext, Collection, Object)} method. Throws an
	 * {@link IllegalStateException} on error.
	 */
	@Override
	public void init() {

		super.init();
		checkState(moveGenerator != null, "Move Generator is not set");
	}

	public final void setMoveGenerator(final IMoveGenerator moveGenerator) {
		this.moveGenerator = moveGenerator;
	}

	@Override
	public final IMoveGenerator getMoveGenerator() {
		return moveGenerator;
	}

	public ILookupManager getLookupManager() {
		return lookupManager;
	}

	public void setLookupManager(ILookupManager stateManager) {
		this.lookupManager = stateManager;
	}

	protected void updateSequencesLookup(@NonNull ISequences rawSequences, @Nullable Collection<IResource> changedResources) {
		this.lookupManager.createLookup(rawSequences);
	}

	@Override
	public int step(final int percentage, JobExecutor jobExecutor) {
		return step(percentage, potentialRawSequences, currentRawSequences, jobExecutor);
	}

	protected abstract int step(final int percentage, @NonNull final ModifiableSequences pinnedPotentialRawSequences, @NonNull final ModifiableSequences pinnedCurrentRawSequences,
			JobExecutor jobExecutor);

	@Override
	public IAnnotatedSolution start(@NonNull final IOptimisationContext optimiserContext, @NonNull final ISequences initialRawSequences, @NonNull final ISequences inputRawSequences) {
		setCurrentContext(optimiserContext);

		initLogger();

		final ModifiableSequences lCurrentRawSequences = new ModifiableSequences(inputRawSequences);

		final ModifiableSequences lPotentialRawSequences = new ModifiableSequences(lCurrentRawSequences.getResources());
		updateSequences(lCurrentRawSequences, lPotentialRawSequences, lCurrentRawSequences.getResources());

		// Evaluate initial sequences
		setInitialSequences(initialRawSequences);

		evaluateInputSequences(initialRawSequences);

		evaluateInputSequences(inputRawSequences);

		// Set initial sequences
		updateSequencesLookup(lPotentialRawSequences, null);

		// For constraint checker changed resources functions, if initial solution is
		// invalid, we want to always perform a full constraint checker set of checks.
		this.failedInitialConstraintCheckers = false;
		// Apply sequence manipulators
		final IModifiableSequences potentialFullSequences = getSequenceManipulator().createManipulatedSequences(lCurrentRawSequences);

		final List<@NonNull String> messages;
		if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES) {
			messages = new ArrayList<>();
			messages.add(String.format("%s: start", this.getClass().getName()));
		} else {
			messages = null;
		}
		// Apply hard constraint checkers
		for (final IConstraintChecker checker : getConstraintCheckers()) {
			if (!checker.checkConstraints(potentialFullSequences, null, messages)) {
				failedInitialConstraintCheckers = true;
				break;
			}
		}
		if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES && !messages.isEmpty()) {
			messages.stream().forEach(LOG::debug);
		}

		final IAnnotatedSolution annotatedBestSolution = getFitnessEvaluator().getBestAnnotatedSolution();
		if (annotatedBestSolution == null) {
			return null;
		}

		annotatedBestSolution.setGeneralAnnotation(OptimiserConstants.G_AI_iterations, 0);
		annotatedBestSolution.setGeneralAnnotation(OptimiserConstants.G_AI_runtime, 0L);

		setStartTime(System.currentTimeMillis());

		setNumberOfIterationsCompleted(0);

		this.currentRawSequences = lCurrentRawSequences;
		this.potentialRawSequences = lPotentialRawSequences;

		initProgressLog();

		return annotatedBestSolution;
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

	protected void initProgressLog() {
		if (loggingDataStore != null) {
			loggingDataStore.intialiseProgressLog(getFitnessEvaluator().getBestFitness(), getFitnessEvaluator().getCurrentFitness());
		}
	}

	protected void initLogger() {
		if (loggingProvider != null) {
			loggingDataStore = loggingProvider.providerLSOLogger(getFitnessEvaluator(), getCurrentContext());
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

	protected void setInitialSequences(@NonNull final ISequences initialSequences) {
		// Store initialSequences
		this.initialRawSequences = new ModifiableSequences(initialSequences);
		// Apply sequence manipulators
		final IModifiableSequences fullSequences = new ModifiableSequences(initialSequences);
		getSequenceManipulator().manipulate(fullSequences);

		// Prime IInitialSequencesConstraintCheckers with initial state
		for (final IInitialSequencesConstraintChecker checker : getInitialSequencesConstraintCheckers()) {
			checker.sequencesAccepted(initialSequences, fullSequences, new ArrayList<>());
		}
	}

	protected void logNextIteration() {
		if (numberOfMovesTried % 10000 == 0) {
			System.out.println("iteration:" + numberOfMovesTried);
		}

		if (loggingDataStore != null && (numberOfMovesTried % loggingDataStore.getReportingInterval()) == 0) {
			loggingDataStore.logProgress(getNumberOfMovesTried(), getNumberOfMovesAccepted(), getNumberOfRejectedMoves(), getNumberOfFailedEvaluations(), getNumberOfFailedToValidate(),
					getFitnessEvaluator().getBestFitness(), getFitnessEvaluator().getCurrentFitness(), new Date().getTime());
		}
	}



}
