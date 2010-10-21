/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import java.util.List;
import java.util.Random;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * A subclass of {@link SeparatedSequenceScheduler} which randomly searches each
 * sub-sequence independently.
 * 
 * @author hinton
 * 
 */
public final class RandomSeparatedSequenceScheduler<T> extends
		SeparatedSequenceScheduler<T> {
	long randomSeed = 1;

	public long getRandomSeed() {
		return randomSeed;
	}

	public void setRandomSeed(long randomSeed) {
		this.randomSeed = randomSeed;
	}

	private final Random random = new Random();

	@Override
	public Pair<Integer, List<VoyagePlan>> schedule(IResource resource,
			ISequence<T> sequence) {
		// reset random seed
		random.setSeed(randomSeed);
		return super.schedule(resource, sequence);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.
	 * SeparatedSequenceScheduler#optimiseSubSequence(int, int)
	 */
	@Override
	protected void optimiseSubSequence(final int chunkStart, final int chunkEnd) {
		final int tests = 10;
		for (int counter = 0; counter < tests; counter++) {
			for (int i = chunkStart; i <= chunkEnd; i++) {
				final int minTime = getMinArrivalTime(i);
				final int maxTime = getMaxArrivalTime(i);

				arrivalTimes[i] = RandomHelper.nextIntBetween(random, minTime,
						maxTime);
			}
			evaluate();
		}
	}

}
