/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.optimiser.lso.parallellso;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.optimiser.common.components.impl.IncrementingRandomSeed;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.optimiser.lso.multiobjective.impl.NonDominatedSolution;
import com.mmxlabs.optimiser.lso.multiobjective.impl.SimpleMultiObjectiveOptimiser;

public class ParallelSimpleMultiObjectiveOptimiser extends SimpleMultiObjectiveOptimiser {

	@Inject
	@Named(LocalSearchOptimiserModule.RANDOM_SEED)
	private long seed;

	@Inject
	ExecutorService executorService;

	@Inject
	@Named(ParallelLSOConstants.PARALLEL_MOO_BATCH_SIZE)
	private int batchSize;
	
	@Inject
	Injector injector;

	private IncrementingRandomSeed incrementingRandomSeed = new IncrementingRandomSeed(seed);

	public ParallelSimpleMultiObjectiveOptimiser(List<IFitnessComponent> fitnessComponents, Random random) {
		super(fitnessComponents, random);
	}

	@Override
	protected int step(final int percentage, @NonNull final ModifiableSequences pinnedPotentialRawSequences, @NonNull final ModifiableSequences pinnedCurrentRawSequences) {

		final int iterationsThisStep = Math.min(Math.max(1, (getNumberOfIterations() * percentage) / 100), getNumberOfIterations() - getNumberOfIterationsCompleted());
		MAIN_LOOP: for (int i = 0; i < (iterationsThisStep / batchSize); i++) {
			initNextIteration();

			// choose a solution from the archive
			List<Future<MultiObjectiveJobState>> futures = new LinkedList<>();
			// create and submit jobs
			for (int b = 0; b < batchSize; b++) {
				NonDominatedSolution nonDominatedSolution = getNonDominatedSolution();
				MultiObjectiveOptimiserJob job = new MultiObjectiveOptimiserJob(injector, nonDominatedSolution.getSequences(), nonDominatedSolution.getManager(), getMoveGenerator(), incrementingRandomSeed.getSeed(), failedInitialConstraintCheckers);
				futures.add(executorService.submit(job));
			}
			List<MultiObjectiveJobState> states = new LinkedList<>();
			// collect jobs
			for (final Future<MultiObjectiveJobState> f : futures) {
				try {
					MultiObjectiveJobState state = f.get();
					states.add(state);
				} catch (final ExecutionException | InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			
			for (MultiObjectiveJobState state : states) {
				if (state.getStatus() == LSOJobStatus.Pass && state.getRawSequences() != null && state.getFitness() != null) {
					addSolutionToNonDominatedArchive(state.getRawSequences(), state.getFitness());
				}
			}

		}
		if (DEBUG) {
			System.out.println("-------------");
			List<NonDominatedSolution> sortedValues = getSortedArchiveWithEpsilonDominance(archive, 1);
			sortedValues.forEach(v -> System.out.println(String.format("[%s-start,%s],", v.getFitnesses()[0] * -1, v.getFitnesses()[1])));
		}
		setNumberOfIterationsCompleted(numberOfMovesTried);

		updateProgressLogs();
		return iterationsThisStep;
	}

	private NonDominatedSolution getNonDominatedSolution() {
		return archive.get(random.nextInt(archive.size()));
	}
	
	@Override
	protected void initNextIteration() {
		stepIteration();
		numberOfMovesTried += batchSize;
		if (numberOfMovesTried % 10000 == 0) {
			System.out.println("iteration:" + numberOfMovesTried);
		}
		if (loggingDataStore != null && (numberOfMovesTried % loggingDataStore.getReportingInterval()) == 0) {
			loggingDataStore.logProgress(getNumberOfMovesTried(), getNumberOfMovesAccepted(), getNumberOfRejectedMoves(), getNumberOfFailedEvaluations(), getNumberOfFailedToValidate(),
					getFitnessEvaluator().getBestFitness(), getFitnessEvaluator().getCurrentFitness(), new Date().getTime());
		}
		if (loggingDataStore != null && (numberOfMovesTried % loggingDataStore.getReportingInterval()) == 0) {
			// this sets best fitness for the best solution	
			getBestSolution();
		}
	}

}
