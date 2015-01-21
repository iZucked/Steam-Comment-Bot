/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
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

	@Override
	public ScheduledSequences schedule(@NonNull final ISequences sequences, @Nullable final IAnnotatedSolution solution) {
		random = new Random(seed);

		setSequences(sequences);
		resetBest();

		prepare();

		for (int index = 0; index < sequences.size(); ++index) {
			random.setSeed(seed);
			randomise(index);
		}
		synchroniseShipToShipBindings();
		evaluate(solution);

		return getBestResult();
	}

	private void synchroniseShipToShipBindings() {
		for (int i = 0; i < bindings.size(); i += 4) {
			final int discharge_seq = bindings.get(i);
			final int discharge_index = bindings.get(i + 1);
			final int load_seq = bindings.get(i + 2);
			final int load_index = bindings.get(i + 3);

			// sequence elements bound by ship-to-ship transfers are effectively the same slot, so the arrival times must be synchronised
			arrivalTimes[load_seq][load_index] = arrivalTimes[discharge_seq][discharge_index];
		}

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
				// TODO force sync this with any ship-to-ship bindings
			}

			// Set the arrival time at the last bit to be as early as possible; VPO will relax it if necessary.
			arrivalTimes[seq][lastIndex] = getMinArrivalTime(seq, lastIndex);

			arrivalTimes[seq][0] = getMaxArrivalTimeForNextArrival(seq, 0);
		}
	}

}
