package com.mmxlabs.scheduler.optimiser.fitness.impl.ga;

import java.util.List;
import java.util.Random;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.AbstractSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.CachingAbstractSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * {@link ISequenceScheduler} implementation using a Genetic Algorithm to
 * determine arrival times.
 * 
 * @author Simon Goodall
 * 
 */
public final class GASequenceScheduler<T> extends
		AbstractSequenceScheduler<T> {

	public GASequenceScheduler() {

	}

	private long randomSeed;

	private CachingIndividualEvaluator<T> cachingIndividualEvaluator;

	private float mutateThreshold;

	private int iterationsByteMultiplier;

	
	private int populationSize;
	
	private int topN;

	private boolean adjustArrivalTimes = false;

	private IndividualEvaluator<T> individualEvaluator;

	public void setIndividualEvaluator(
			final IndividualEvaluator<T> individualEvaluator) {
		this.individualEvaluator = individualEvaluator;
		this.cachingIndividualEvaluator = null;
	}

	@Override
	public List<VoyagePlan> schedule(final IResource resource,
			final ISequence<T> sequence) {

		final int numBytes = individualEvaluator.setup(resource, sequence);

		// Create a new random each time to ensure repeatability.
		Random random = new Random(randomSeed);
		
		if (cachingIndividualEvaluator == null) {
			cachingIndividualEvaluator = new CachingIndividualEvaluator<T>(
					individualEvaluator, populationSize * 10);
		} else {
			cachingIndividualEvaluator.clearCache();
		}
		// Run the GA
		final GAAlgorithm<T> algorithm = new GAAlgorithm<T>(random,

				cachingIndividualEvaluator,
//				individualEvaluator,

				mutateThreshold, populationSize, topN, numBytes);

		algorithm.init();

		final int numIterations = numBytes * iterationsByteMultiplier;
		
		for (int i = 0; i < numIterations; ++i) {
			algorithm.step();
		}

		final Individual bestIndividual = algorithm.getBestIndividual();
		if (bestIndividual == null) {
			return null;
		}

		final int[] arrivalTimes = new int[sequence.size()];
		individualEvaluator.decode(bestIndividual, arrivalTimes);

		return super.schedule(resource, sequence, arrivalTimes,
				adjustArrivalTimes);
	}

	@Override
	public void init() {

		if (individualEvaluator == null) {
			throw new IllegalStateException(
					"Individual Evaluator has not been set");
		}

		if (adjustArrivalTimes != individualEvaluator.isAdjustArrivalTimes()) {
			throw new IllegalStateException(
					"Individual evaluator and scheduler disagree about adjusting arrival times");
		}

		if (populationSize == 0) {
			throw new IllegalStateException(
					"Population size has not been set");
		}

		if (iterationsByteMultiplier == 0) {
			throw new IllegalStateException("Iterations byte multiplier has not been set");
		}

		if (topN == 0) {
			throw new IllegalStateException("Top N elements has not been set");
		}

		if (mutateThreshold == 0.0f) {
			throw new IllegalStateException("Mutate threshold has not been set");
		}

		super.init();
	}

	@Override
	public void dispose() {

		individualEvaluator = null;

		super.dispose();
	}

	public final float getMutateThreshold() {
		return mutateThreshold;
	}

	public final void setMutateThreshold(float mutateThreshold) {
		this.mutateThreshold = mutateThreshold;
	}

	public int getIterationsByteMultiplier() {
		return iterationsByteMultiplier;
	}

	public void setIterationsByteMultiplier(int populationByteMultiplier) {
		this.iterationsByteMultiplier = populationByteMultiplier;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public final int getTopN() {
		return topN;
	}

	public final void setTopN(int topN) {
		this.topN = topN;
	}

	public final IndividualEvaluator<T> getIndividualEvaluator() {
		return individualEvaluator;
	}

	public final long getRandomSeed() {
		return randomSeed;
	}

	public final void setRandomSeed(long randomSeed) {
		this.randomSeed = randomSeed;
	}

	public boolean isAdjustArrivalTimes() {
		return adjustArrivalTimes;
	}

	public void setAdjustArrivalTimes(boolean adjustArrivalTimes) {
		this.adjustArrivalTimes = adjustArrivalTimes;
	}
}
