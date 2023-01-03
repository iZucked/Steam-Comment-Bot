/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.cache.AbstractWriteLockable;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

/**
 * An object storing the travel times and route options (TODO) between element pairs.
 * 
 * @author Simon Goodall
 * 
 */
@NonNullByDefault
public final class MinTravelTimeData extends AbstractWriteLockable {

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

	public void setMinTravelTime(final int fromElementIndex, final int travelTime) {
		checkWritable();
		minTimeToNextElement[fromElementIndex] = travelTime;
	}

	public int getMinTravelTime(final int fromElementIndex) {
		return minTimeToNextElement[fromElementIndex];
	}

	public IResource getResource() {
		return resource;
	}

	public ISequence getSequence() {
		return sequence;
	}

	public void setTravelTime(final ERouteOption routeOption, final int fromElementIndex, final int travelTime) {
		checkWritable();
		timeToNextElement[routeOption.ordinal()][fromElementIndex] = travelTime;
	}

	public int getTravelTime(final ERouteOption routeOption, final int fromElementIndex) {
		return timeToNextElement[routeOption.ordinal()][fromElementIndex];
	}

	/**
	 * Is there a valid travel time for the given route and is it permitted from the AvailableRouteChoices object.
	 * 
	 * @param routeOption
	 * @param fromElementIndex
	 * @param arc
	 * @return
	 */
	public boolean isRouteValid(final ERouteOption routeOption, final int fromElementIndex, final AvailableRouteChoices arc) {

		if (timeToNextElement[routeOption.ordinal()][fromElementIndex] == Integer.MAX_VALUE) {
			return false;
		}

		boolean arcMatch;
		switch (routeOption) {
		case DIRECT:
			arcMatch = AvailableRouteChoices.directPermitted(arc);
			break;
		case SUEZ:
			arcMatch = AvailableRouteChoices.suezPermitted(arc);
			break;
		case PANAMA:
			arcMatch = AvailableRouteChoices.panamaPermitted(arc);
			break;
		default:
			throw new IllegalArgumentException("Unknown route type");
		}
		return arcMatch;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {

		if (obj == this) {
			return true;
		}

		if (obj instanceof MinTravelTimeData) {
			final MinTravelTimeData other = (MinTravelTimeData) obj;

			if (!Arrays.equals(minTimeToNextElement, other.minTimeToNextElement)) {
				return false;
			}

			for (int i = 0; i < ERouteOption.values().length; ++i) {
				if (!Arrays.equals(timeToNextElement[i], other.timeToNextElement[i])) {
					return false;
				}
			}
			return true;
		}

		return false;
	}
}
