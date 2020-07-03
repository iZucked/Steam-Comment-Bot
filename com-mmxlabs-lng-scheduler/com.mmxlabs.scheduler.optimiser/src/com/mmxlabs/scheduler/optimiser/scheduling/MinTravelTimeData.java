/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;

/**
 * An object storing the travel times and route options (TODO) between element pairs.
 * 
 * @author Simon Goodall
 * 
 */
@NonNullByDefault
public final class MinTravelTimeData {

	/**
	 * The minimum time this vessel can take to get from the indexed element to its successor. i.e. min travel time + visit time at indexed element.
	 */
	private final int[] minTimeToNextElement;
	private final int[][] timeToNextElement;
	private final IResource resource;

	private final ISequence sequence;

	public MinTravelTimeData(final IResource resource, final ISequence sequence) {

		this.resource = resource;
		this.sequence = sequence;

		final int seqSize = sequence.size();

		// Do we need plus one?
		minTimeToNextElement = new int[seqSize + 1];
		timeToNextElement = new int[ERouteOption.values().length][seqSize + 1];
	}

	public void setMinTravelTime(final int elementIndex, final int travelTime) {
		minTimeToNextElement[elementIndex] = travelTime;
	}

	public int getMinTravelTime(final int elementIndex) {
		return minTimeToNextElement[elementIndex];
	}

	public IResource getResource() {
		return resource;
	}

	public ISequence getSequence() {
		return sequence;
	}

	public void setTravelTime(final ERouteOption routeOption,final int elementIndex, final int travelTime) {
		timeToNextElement[routeOption.ordinal()][elementIndex] = travelTime;
	}

	public int getTravelTime(final ERouteOption routeOption, final int elementIndex) {
		return timeToNextElement[routeOption.ordinal()][elementIndex];
	}
}
