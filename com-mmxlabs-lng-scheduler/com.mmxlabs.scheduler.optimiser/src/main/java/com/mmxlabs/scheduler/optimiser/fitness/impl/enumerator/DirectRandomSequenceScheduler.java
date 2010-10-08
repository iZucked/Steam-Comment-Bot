package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import java.util.List;
import java.util.Random;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Another random sequence scheduler, which works by resting on top of the {@link EnumeratingSequenceScheduler}
 * and replacing the exhaustive recursive loop with something randomised; for each element it will randomly pick
 * an arrival time from the feasible range
 * @author hinton
 *
 * @param <T>
 */
public class DirectRandomSequenceScheduler<T> extends
		EnumeratingSequenceScheduler<T> {
	private int seed = 0;
	private Random random;
	
	/**
	 * Below this number of possibilities, enumerate all options
	 */
	private int samplingLowerBound = 128; 
	/**
	 * Never do more than this many samples
	 */
	private int samplingUpperBound = 1000;
	/**
	 * Sample this proportion of the search space, up to {@code samplingUpperBound}
	 */
	private double sampleProportion = 0.005;
	
	@Override
	public Pair<Integer, List<VoyagePlan>> schedule(final IResource resource,
			final ISequence<T> sequence) {
		random = new Random(seed);
		
		setResourceAndSequence(resource, sequence);
		resetBest();
		final long approximateSpaceSize = prepare();

		if (approximateSpaceSize < samplingLowerBound) {
//			System.err.println("Exhaustive search " + approximateSpaceSize);
			enumerate(0);
//			System.err.println("Actually did " + count);
		} else {
			final int maxCount = Math.min(samplingUpperBound,
					(int)(sampleProportion * approximateSpaceSize));
//			System.err.println("Sample " + maxCount);
			for (int i = 0; i<maxCount; i++) {
				flatEnumerate();
			}
		}
		
		return getBestResult();
	}
	
	private final void flatEnumerate() {
		for (int index = 0; index<arrivalTimes.length; index++) {
			final int min = getMinArrivalTime(index);
			final int max = getMaxArrivalTime(index);
			arrivalTimes[index] = RandomHelper.nextIntBetween(random, min, max);
		}
		
		evaluate();
	}

	public int getSamplingLowerBound() {
		return samplingLowerBound;
	}

	public void setSamplingLowerBound(final int samplingLowerBound) {
		this.samplingLowerBound = samplingLowerBound;
	}

	public int getSamplingUpperBound() {
		return samplingUpperBound;
	}

	public void setSamplingUpperBound(final int samplingUpperBound) {
		this.samplingUpperBound = samplingUpperBound;
	}

	public double getSampleProportion() {
		return sampleProportion;
	}

	public void setSampleProportion(final double sampleProportion) {
		this.sampleProportion = sampleProportion;
	}
}
