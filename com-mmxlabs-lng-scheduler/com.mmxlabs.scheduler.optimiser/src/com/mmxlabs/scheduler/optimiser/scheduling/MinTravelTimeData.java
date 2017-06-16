/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.fitness.util.SequenceEvaluationUtils;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;

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
	private int[] minTimeToNextElement;
	private @NonNull IResource resource;

	private ISequence sequence;

	public MinTravelTimeData(@NonNull IResource resource, ISequence sequence) {

		this.resource = resource;
		this.sequence = sequence;

		final int seqSize = sequence.size();

		// Do we need plus one?
		minTimeToNextElement = new int[seqSize + 1];
	}

	public void setMinTravelTime(int elementIndex, int travelTime) {
		minTimeToNextElement[elementIndex] = travelTime;
	}

	public int getMinTravelTime(int elementIndex) {
		return minTimeToNextElement[elementIndex];
	}

	public IResource getResource() {
		return resource;
	}

	public ISequence getSequence() {
		return sequence;
	}
}
