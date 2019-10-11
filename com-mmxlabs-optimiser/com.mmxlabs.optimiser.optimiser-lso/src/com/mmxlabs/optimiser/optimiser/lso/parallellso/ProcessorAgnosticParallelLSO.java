/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.optimiser.optimiser.lso.parallellso;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.common.components.impl.IncrementingRandomSeed;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IInitialSequencesConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IReducingConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.logging.ILoggingProvider;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;

public class ProcessorAgnosticParallelLSO extends LocalSearchOptimiser {

	public static final String PARALLEL_LSO_BATCH_SIZE = "MMX_PARALLEL_LSO_BATCH_SIZE";
	protected IPhaseOptimisationData data;
	private ModifiableSequences currentRawSequences;
	private ModifiableSequences potentialRawSequences;

	protected int numberOfMovesTried;

	@Inject
	private Injector injector;

	@Inject
	private CleanableExecutorService executorService;

	@Inject
	@Named(LocalSearchOptimiserModule.RANDOM_SEED)
	private long seed;

	@Inject
	@Named(PARALLEL_LSO_BATCH_SIZE)
	private int batchSize;

	@Inject
	@Named(LocalSearchOptimiserModule.OPTIMISER_DEBUG_MODE)
	protected boolean DEBUG;

	private IncrementingRandomSeed incrementingRandomSeed = new IncrementingRandomSeed(seed);
	private boolean failedInitialConstraintCheckers;

	protected LSOLogger loggingDataStore;

	@Inject(optional = true)
	protected ILoggingProvider loggingProvider;
	private int numberOfMovesAccepted = 0;
	private int numberOfMovesRejected = 0;

	@Override
	public IAnnotatedSolution start(@NonNull final IOptimisationContext optimiserContext, @NonNull final ISequences initialRawSequences, @NonNull final ISequences inputRawSequences) {
		setCurrentContext(optimiserContext);

		initLogger();

		final ModifiableSequences currentRawSequences = new ModifiableSequences(inputRawSequences);

		final ModifiableSequences potentialRawSequences = new ModifiableSequences(currentRawSequences.getResources());
		updateSequences(currentRawSequences, potentialRawSequences, currentRawSequences.getResources());

		// Evaluate initial sequences
		setInitialSequences(initialRawSequences);

		evaluateInputSequences(initialRawSequences);

		evaluateInputSequences(inputRawSequences);

		// Set initial sequences
		updateSequencesLookup(potentialRawSequences, null);

		// For constraint checker changed resources functions, if initial solution is invalid, we want to always perform a full constraint checker set of checks.
		this.failedInitialConstraintCheckers = false;
		// Apply sequence manipulators
		final IModifiableSequences potentialFullSequences = getSequenceManipulator().createManipulatedSequences(currentRawSequences);

		// Apply hard constraint checkers
		for (final IConstraintChecker checker : getConstraintCheckers()) {
			if (checker.checkConstraints(potentialFullSequences, null) == false) {
				failedInitialConstraintCheckers = true;
				break;
			}
		}

		final IAnnotatedSolution annotatedBestSolution = getFitnessEvaluator().getBestAnnotatedSolution();
		if (annotatedBestSolution == null) {
			return null;
		}

		annotatedBestSolution.setGeneralAnnotation(OptimiserConstants.G_AI_iterations, 0);
		annotatedBestSolution.setGeneralAnnotation(OptimiserConstants.G_AI_runtime, 0l);

		setStartTime(System.currentTimeMillis());

		setNumberOfIterationsCompleted(0);

		this.currentRawSequences = currentRawSequences;
		this.potentialRawSequences = potentialRawSequences;

		initProgressLog();

		return annotatedBestSolution;

	}

	@Override
	public int step(final int percentage) {
		return step(percentage, potentialRawSequences, currentRawSequences);
	}

	protected int step(final int percentage, @NonNull final ModifiableSequences pinnedPotentialRawSequences, @NonNull final ModifiableSequences pinnedCurrentRawSequences) {
		long start = System.currentTimeMillis();
		final int iterationsThisStep = Math.min(Math.max(1, (getNumberOfIterations() * percentage) / 100), getNumberOfIterations() - getNumberOfIterationsCompleted());
		int iterationsCompletedThisStep = 0;
		while (iterationsCompletedThisStep < iterationsThisStep) {
			List<Future<LSOJobState>> futures = new LinkedList<>();

			// create and submit jobs
			final ILookupManager lookupManagerBatch = injector.getInstance(ILookupManager.class);
			ISequences batchSequences = new ModifiableSequences(pinnedPotentialRawSequences);
			lookupManagerBatch.createLookup(batchSequences);

			// build jobs
			for (int b = 0; b < batchSize; b++) {
				LSOJob job = new LSOJob(injector, batchSequences, lookupManagerBatch, getMoveGenerator(), incrementingRandomSeed.getSeed(), failedInitialConstraintCheckers);
				futures.add(executorService.submit(job));
			}

			// collect and process jobs
			boolean sequenceWasAccepted = false;
			long acceptedSeed = 0;
			ISequences stateToUpdateTo = null;
			for (int futureIdx = 0; futureIdx < futures.size(); futureIdx++) {
				LSOJobState state;
				Future<LSOJobState> f = futures.get(futureIdx);
				
				try {
					state = f.get();
				} catch (final ExecutionException | InterruptedException e) {
					throw new RuntimeException(e);
				}
				
				getFitnessEvaluator().step();
				++numberOfMovesTried;
				++iterationsCompletedThisStep;
				
				logJobState(state);
				
				boolean accepted = getFitnessEvaluator().evaluateSequencesFromFitnessOnly(state.rawSequences, state.evaluationState, state.fullSequences, state.fitness);
				
				if (loggingDataStore != null && (numberOfMovesTried % loggingDataStore.getReportingInterval()) == 0) {
					loggingDataStore.logProgress(numberOfMovesTried, numberOfMovesAccepted, numberOfMovesRejected, 0, 0, getFitnessEvaluator().getBestFitness(), getFitnessEvaluator().getCurrentFitness(),
							new Date().getTime());
				}
				
				if (accepted) {
					numberOfMovesAccepted++;
					sequenceWasAccepted = true;
					// save state for new sequences
					stateToUpdateTo = state.rawSequences;
					
					loggingDataStore.logSuccessfulMove(state.getMove(), numberOfMovesTried, getFitnessEvaluator().getLastFitness());
					
					failedInitialConstraintCheckers = false;
					acceptedSeed = state.getSeed();
					for (int cancelIdx = futureIdx + 1; cancelIdx < futures.size(); cancelIdx++) {
						Future<LSOJobState> futureToCancel = futures.get(cancelIdx);
						futureToCancel.cancel(false);
					}
					break;
				} else {
					numberOfMovesRejected++;

					loggingDataStore.logRejectedMove(state.getMove(), numberOfMovesTried, getFitnessEvaluator().getLastFitness());
				}
				
				if (iterationsCompletedThisStep >= iterationsThisStep) {
					incrementingRandomSeed.setSeed(state.getSeed());
					break;
				}
			}
			if (sequenceWasAccepted) {
				updateSequences(stateToUpdateTo, pinnedPotentialRawSequences, stateToUpdateTo.getResources());
				updateSequencesLookup(pinnedPotentialRawSequences, stateToUpdateTo.getResources());

				incrementingRandomSeed.setSeed(acceptedSeed);
				continue;
			}
			this.executorService.removeCompleted();
		}
		setNumberOfIterationsCompleted(numberOfMovesTried);
		if (DEBUG) {
			System.out.println("moves tried:" + numberOfMovesTried);
			System.out.println("time:" + (System.currentTimeMillis() - start) / 1000.0);
		}

		return iterationsThisStep;
	}

	private void logJobState(LSOJobState state) {
		if (loggingDataStore != null) {
			switch (state.getStatus()) {
				case NullMoveFail: {
					loggingDataStore.logNullMove(state.getMove());
					break;
				}
				case CannotValidateFail: {
					loggingDataStore.logFailedToValidateMove(state.getMove());
					break;
				}
				case ConstraintFail: {
					loggingDataStore.logFailedConstraints((IConstraintChecker) state.getFailedChecker(), state.getMove());
					break;
				}
				case EvaluationProcessFail: {
					break;
				}
				case EvaluatedConstraintFail: {
					loggingDataStore.logFailedEvaluatedStateConstraints((IEvaluatedStateConstraintChecker) state.getFailedChecker(), state.getMove());
					break;					
				}
				case Pass: {
					loggingDataStore.logAppliedMove(state.getMove().getClass().getName());
					break;
				}
			}
		}
		
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

	protected void setInitialSequences(@NonNull final ISequences initialSequences) {
		// Apply sequence manipulators
		final IModifiableSequences fullSequences = new ModifiableSequences(initialSequences);
		getSequenceManipulator().manipulate(fullSequences);

		// Prime IInitialSequencesConstraintCheckers with initial state
		for (final IInitialSequencesConstraintChecker checker : getInitialSequencesConstraintCheckers()) {
			checker.sequencesAccepted(initialSequences, fullSequences);
		}
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

		// Prime fitness cores with initial sequences
		getFitnessEvaluator().setInitialSequences(currentRawSequences, fullSequences, evaluationState);
	}

	private void updateSequences(@NonNull ISequences pinnedPotentialRawSequences, @NonNull ISequences rawSequences) {
		pinnedPotentialRawSequences = rawSequences;
	}

}