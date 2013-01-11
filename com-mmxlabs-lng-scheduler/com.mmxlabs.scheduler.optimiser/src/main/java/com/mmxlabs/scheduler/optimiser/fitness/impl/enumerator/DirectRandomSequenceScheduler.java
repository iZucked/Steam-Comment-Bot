/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import java.util.Collection;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;

/**
 * Another random sequence scheduler, which works by resting on top of the {@link EnumeratingSequenceScheduler} and replacing the exhaustive recursive loop with something randomised; for each element
 * it will randomly pick an arrival time from the feasible range
 * 
 * @author hinton
 * @param
 */
public class DirectRandomSequenceScheduler extends EnumeratingSequenceScheduler {
	private final int seed = 0;
	private Random random;
	private static final Logger log = LoggerFactory.getLogger(DirectRandomSequenceScheduler.class);
	/**
	 * Do this many times the usual number of samples for an exported solution. Slightly improves the quality of the exported schedule.
	 */
	private static final int EXPORT_INTENSITY = 1000;

	/**
	 * Never do more than this many samples
	 */
	private int samplingUpperBound = 1;

	@Override
	public ScheduledSequences schedule(final ISequences sequences, final boolean forExport) {
		return schedule(sequences, sequences.getResources(), forExport);
	}

	// private final HashSet<IResource> lastAffectedResources = new HashSet<IResource>();

	@Override
	public ScheduledSequences schedule(final ISequences sequences, final Collection<IResource> affectedResources, final boolean forExport) {
		random = new Random(seed);

		setSequences(sequences);
		resetBest();

		final int[] resourceIndices = getResourceIndices(sequences, forExport ? sequences.getResources() : affectedResources);

		prepare(resourceIndices);

		// if (forExport) {
		// final int sampleCount = EXPORT_INTENSITY * samplingUpperBound;
		// for (int i = 0; i < sampleCount; i++) {
		// for (int seq = 0; seq < arrivalTimes.length; seq++) {
		// randomise(seq);
		// }
		// evaluate(resourceIndices);
		// }
		// } else {
		final int sampleCount = samplingUpperBound;
		for (int i = 0; i < sampleCount; i++) {
			for (final int index : resourceIndices) {
				random.setSeed(seed);
				randomise(index);
			}
			evaluate(resourceIndices);
		}
		// }

		return reEvaluateAndGetBestResult();
	}

	private void randomise(final int seq) {
		if (arrivalTimes[seq] == null) {
			return;
		}

		if (sizes[seq] > 0) {
			final int lastIndex = sizes[seq] - 1;
			for (int pos = 0; pos < lastIndex; pos++) {
				final int min = getMinArrivalTime(seq, pos);
				final int max = getMaxArrivalTime(seq, pos);
				arrivalTimes[seq][pos] = RandomHelper.nextIntBetween(random, min, max);
			}

			// Set the arrival time at the last bit to be as early as possible; VPO will relax it if necessary.
			arrivalTimes[seq][lastIndex] = getMinArrivalTime(seq, lastIndex);

			arrivalTimes[seq][0] = getMaxArrivalTimeForNextArrival(seq, 0);
		}
	}

	public int getSamplingUpperBound() {
		return samplingUpperBound;
	}

	public void setSamplingUpperBound(final int samplingUpperBound) {
		this.samplingUpperBound = samplingUpperBound;
	}
}
