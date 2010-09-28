package com.mmxlabs.scheduler.optimiser.fitness.impl.iga;

import java.util.Random;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;

public class RandomArrivalTimeSampler<T> {
	private final int[] startTimes;
	private final int[] durations;
	private final int[] travels;
	private final Random random;

	public RandomArrivalTimeSampler(final IResource resource,
			final ISequence<T> sequence, final Random random) {
		this.random = random;
		startTimes = new int[sequence.size()];
		durations = new int[sequence.size()];
		travels = new int[sequence.size()];
		// unpack times
	}

	public void sampleArrivalTimes(final int[] arrivalTimes) {
		for (int i = 0; i < arrivalTimes.length; i++) {
			arrivalTimes[i] = startTimes[i] + random.nextInt(durations[i]);
		}
	}
}
