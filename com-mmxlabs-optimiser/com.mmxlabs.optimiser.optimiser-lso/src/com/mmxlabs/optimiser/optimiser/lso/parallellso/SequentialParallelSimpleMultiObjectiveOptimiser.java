/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.optimiser.optimiser.lso.parallellso;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.common.components.impl.IncrementingRandomSeed;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.optimiser.lso.multiobjective.impl.NonDominatedSolution;
import com.mmxlabs.optimiser.lso.multiobjective.impl.SimpleMultiObjectiveOptimiser;

public class SequentialParallelSimpleMultiObjectiveOptimiser extends SimpleMultiObjectiveOptimiser {

	@Inject
	@Named(LocalSearchOptimiserModule.RANDOM_SEED)
	private long seed;

	@Inject
	private CleanableExecutorService executorService;

	@Inject
	@Named(ParallelLSOConstants.PARALLEL_MOO_BATCH_SIZE)
	private int batchSize;

	@Inject
	private Injector injector;

	private final IncrementingRandomSeed incrementingRandomSeed = new IncrementingRandomSeed(seed);

	public SequentialParallelSimpleMultiObjectiveOptimiser(final List<IFitnessComponent> fitnessComponents, final Random random) {
		super(fitnessComponents, random);
	}

	@Override
	protected int step(final int percentage, @NonNull final ModifiableSequences pinnedPotentialRawSequences, @NonNull final ModifiableSequences pinnedCurrentRawSequences) {

		final int iterationsThisStep = Math.min(Math.max(1, (getNumberOfIterations() * percentage) / 100), getNumberOfIterations() - getNumberOfIterationsCompleted());
		int i = 0;
		while (i < iterationsThisStep) {
			initNextIteration();

			// choose a solution from the archive
			final List<Future<MultiObjectiveJobState>> futures = new LinkedList<>();
			// create and submit jobs
			for (int b = 0; b < batchSize; b++) {
				final long seedd = incrementingRandomSeed.getSeed();
				final NonDominatedSolution nonDominatedSolution = getNonDominatedSolution(seedd);
				final MultiObjectiveOptimiserJob job = new MultiObjectiveOptimiserJob(injector, nonDominatedSolution.getSequences(), nonDominatedSolution.getManager(), getMoveGenerator(), seedd,
						failedInitialConstraintCheckers);
				futures.add(executorService.submit(job));
			}
			int futureIdx = 0;
			for (final Future<MultiObjectiveJobState> f : futures) {
				MultiObjectiveJobState state;
				try {
					state = f.get();
				} catch (final ExecutionException | InterruptedException e) {
					throw new RuntimeException(e);
				}

				if (state.getStatus() == LSOJobStatus.Pass && state.getRawSequences() != null && state.getFitness() != null) {
					final boolean addedToArchive = addSolutionToNonDominatedArchive(state.getRawSequences(), state.getFitness());
					if (addedToArchive) {
						if (DEBUG) {
							System.out.println("new dom:" + state.getSeed());
						}
						incrementingRandomSeed.setSeed(state.getSeed());
						for (int cancelIdx = futureIdx + 1; cancelIdx < futures.size(); cancelIdx++) {
							final Future<MultiObjectiveJobState> futureToCancel = futures.get(cancelIdx);
							futureToCancel.cancel(false);
						}
						break;
					}
				}
				i++;
				futureIdx++;
				// clean up executor
				this.executorService.removeCompleted();
			}
		}
		if (DEBUG) {
			System.out.println("-------------");
			final List<NonDominatedSolution> sortedValues = getSortedArchiveWithEpsilonDominance(archive, 1);
			sortedValues.forEach(v -> System.out.println(String.format("[%s-start,%s],", v.getFitnesses()[0] * -1, v.getFitnesses()[1])));
		}
		setNumberOfIterationsCompleted(numberOfMovesTried);

		updateProgressLogs();
		return iterationsThisStep;
	}

	private NonDominatedSolution getNonDominatedSolution(final long seedd) {
		return archive.get(new Random(seedd).nextInt(archive.size()));
	}

	@Override
	protected boolean addSolutionToNonDominatedArchive(final ISequences pinnedPotentialRawSequences, final long[] fitnesses) {
		final boolean nonDominated = checkIsDominatedAndRemoveDominatedSolutionsFromArchive(archive, fitnesses);
		if (nonDominated) {
			final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
			lookupManager.createLookup(pinnedPotentialRawSequences);
			archive.add(new NonDominatedSolution(new Sequences(pinnedPotentialRawSequences), fitnesses, lookupManager));
		}
		return nonDominated;
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
