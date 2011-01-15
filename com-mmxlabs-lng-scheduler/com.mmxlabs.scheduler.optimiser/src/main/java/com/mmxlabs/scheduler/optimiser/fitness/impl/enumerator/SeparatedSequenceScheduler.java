/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import java.util.List;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * A variation on the {@link DirectRandomSequenceScheduler} which uses the
 * {@link EnumeratingSequenceScheduler#separationPoints} list to randomly sample
 * the independent sub-sequences separately and select an output which is the
 * best seen for every sub-sequence.
 * 
 * @author hinton
 * 
 */
public abstract class SeparatedSequenceScheduler<T> extends
		EnumeratingSequenceScheduler<T> {

	// public SeparatedSequenceScheduler() {
	// super();
	// }
	//
	// @Override
	// public Pair<Integer, List<VoyagePlan>> schedule(final IResource resource,
	// final ISequence<T> sequence) {
	// setResourceAndSequence(resource, sequence);
	// resetBest();
	// prepare(0);
	//
	// // initialize arrivaltime vector to random arrival times
	// // until we get something
	// if (!initializeArrivalTimes()) return null; //fail
	//
	// int chunkStart = 0;
	// int chunkEnd = -1;
	// for (int point : separationPoints) {
	// chunkStart = chunkEnd + 1;
	// chunkEnd = point;
	// optimiseSubSequence(chunkStart, chunkEnd);
	// }
	//
	// return getBestResult();
	// }
	//
	// protected abstract boolean initializeArrivalTimes();
	//
	// /**
	// * Apply a random search to this subsequence. Both indices are inclusive,
	// so
	// * our responsibility is to set good arrival times for [chunkStart,
	// * chunkStart+1, ..., chunkEnd]
	// *
	// * @param chunkStart
	// * @param chunkEnd
	// */
	// protected abstract void optimiseSubSequence(int chunkStart, int
	// chunkEnd);
}
