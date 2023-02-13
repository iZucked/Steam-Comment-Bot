/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.common.components.impl.IncrementingRandomSeed;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;

public class ProcessorAgnosticParallelLSO extends DefaultLocalSearchOptimiser {
	protected static final Logger LOG = LoggerFactory.getLogger(ProcessorAgnosticParallelLSO.class);

	public static final String PARALLEL_LSO_BATCH_SIZE = "MMX_PARALLEL_LSO_BATCH_SIZE";

	@Inject
	private Injector injector;

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

	protected int step(final int percentage, @NonNull final ModifiableSequences pinnedPotentialRawSequences, @NonNull final ModifiableSequences pinnedCurrentRawSequences, JobExecutor jobExecutor) {
		long start = System.currentTimeMillis();
		final int iterationsThisStep = Math.min(Math.max(1, (getNumberOfIterations() * percentage) / 100), getNumberOfIterations() - getNumberOfIterationsCompleted());
		int iterationsCompletedThisStep = 0;
		while (iterationsCompletedThisStep < iterationsThisStep) {

			if (isRestartIteration(getNumberOfMovesTried())) {
				restart();
			}

			List<Future<LSOJobState>> futures = new LinkedList<>();

			// create and submit jobs
			final ILookupManager lookupManagerBatch = injector.getInstance(ILookupManager.class);
			ISequences batchSequences = new ModifiableSequences(pinnedPotentialRawSequences);
			lookupManagerBatch.createLookup(batchSequences);

			// build jobs
			for (int b = 0; b < batchSize; b++) {
				LSOJob job = new LSOJob(injector, batchSequences, lookupManagerBatch, getMoveGenerator(), incrementingRandomSeed.getSeed(), failedInitialConstraintCheckers);
				futures.add(jobExecutor.submit(job));
			}

			// collect and process jobs
			boolean sequenceWasAccepted = false;
			long acceptedSeed = 0;
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

				logNextIteration();

				logJobState(state);

				++iterationsCompletedThisStep;
				boolean accepted = acceptOrRejectMove(pinnedPotentialRawSequences, pinnedCurrentRawSequences, state);

				if (accepted) {
					sequenceWasAccepted = true;
					acceptedSeed = state.getSeed();

					for (int cancelIdx = futureIdx + 1; cancelIdx < futures.size(); cancelIdx++) {
						Future<LSOJobState> futureToCancel = futures.get(cancelIdx);
						futureToCancel.cancel(false);
					}
					break;
				}

				if (iterationsCompletedThisStep >= iterationsThisStep) {
					incrementingRandomSeed.setSeed(state.getSeed());
					break;
				}
			}
			if (sequenceWasAccepted) {
				incrementingRandomSeed.setSeed(acceptedSeed);
				continue;
			}
			jobExecutor.removeCompleted();
		}
		setNumberOfIterationsCompleted(numberOfMovesTried);

		updateProgressLogs();

		if (DEBUG) {
			System.out.println("moves tried:" + numberOfMovesTried);
			System.out.println("time:" + (System.currentTimeMillis() - start) / 1000.0);
		}

		return iterationsThisStep;
	}

}