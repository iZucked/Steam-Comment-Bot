/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.ISequences;

/**
 * 
 * @author robert
 * @param
 */
public class NonSchedulingScheduler extends EnumeratingSequenceScheduler {
	private final int seed = 0;

	@Override
	public int @Nullable [][] schedule(@NonNull final ISequences sequences) {

		setSequences(sequences);

		prepare();
		trimPanama();

		return arrivalTimes;
	}

	private void randomise(final int seq) {

	}
}
