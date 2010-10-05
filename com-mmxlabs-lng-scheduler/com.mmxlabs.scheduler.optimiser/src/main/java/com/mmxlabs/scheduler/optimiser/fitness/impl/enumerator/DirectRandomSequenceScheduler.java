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
	
	
	@Override
	public Pair<Integer, List<VoyagePlan>> schedule(final IResource resource,
			final ISequence<T> sequence) {
		random = new Random(seed);
		
		setResourceAndSequence(resource, sequence);
		resetBest();
		prepare();

		final int maxCount = 10 * sequence.size();
		
		while (count < maxCount) {
			flatEnumerate();
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

}
