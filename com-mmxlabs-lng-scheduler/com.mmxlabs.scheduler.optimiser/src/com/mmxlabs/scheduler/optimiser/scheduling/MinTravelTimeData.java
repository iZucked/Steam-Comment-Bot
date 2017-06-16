/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.fitness.util.SequenceEvaluationUtils;

/**
 * An object storing the travel times and route options (TODO) between element pairs.
 * 
 * @author Simon Goodall
 * 
 */
public final class MinTravelTimeData {

	/**
	 * The minimum time this vessel can take to get from the indexed element to its successor. i.e. min travel time + visit time at indexed element.
	 */
	private int[][] minTimeToNextElement;

	public MinTravelTimeData(@NonNull ISequences sequences) {

		final int size = sequences.size();

		minTimeToNextElement = new int[size][];

		for (int seqIndex = 0; seqIndex < size; seqIndex++) {
			final IResource resource = sequences.getResources().get(seqIndex);
			final ISequence sequence = sequences.getSequence(resource);
			final int seqSize = sequence.size();
			// filters out solutions with less than 2 elements (i.e. spot charters, etc.)
			if (SequenceEvaluationUtils.shouldIgnoreSequence(sequence)) {
				// return;
			}
			// Do we need plus one?
			minTimeToNextElement[seqIndex] = new int[seqSize + 1];
		}
	}

	public void setMinTravelTime(int seqIndex, int elementIndex, int travelTime) {
		if (travelTime == Integer.MIN_VALUE) {
			int ii = 0;
		}
		
		minTimeToNextElement[seqIndex][elementIndex] = travelTime;
	}

	public int getMinTravelTime(int seqIndex, int elementIndex) {
		return minTimeToNextElement[seqIndex][elementIndex];
	}
}
