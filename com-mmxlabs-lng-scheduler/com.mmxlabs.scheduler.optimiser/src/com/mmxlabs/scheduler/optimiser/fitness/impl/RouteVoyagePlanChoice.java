/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;

/**
 * Implementation of {@link IVoyagePlanChoice} to change the route used between ports.
 * 
 * @author Simon Goodall
 * 
 */
public final class RouteVoyagePlanChoice implements IVoyagePlanChoice {

	private int choice;

	private final VoyageOptions options;

	private final List<Pair<ERouteOption, Integer>> distances;

	public RouteVoyagePlanChoice(final VoyageOptions options, final List<Pair<ERouteOption, Integer>> distances) {
		this.options = options;
		this.distances = distances;
	}

	@Override
	public final boolean reset() {
		for (int i = 0; i < numChoices(); i++) {
			if (apply(i)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public final boolean nextChoice() {
		while (true) {
			if ((choice + 1) == numChoices()) {
				return true;
			}
			if (apply(choice + 1)) {
				return false;
			}
		}
	}

	@Override
	public int numChoices() {
		return distances.size();
	}

	@Override
	public final boolean apply(final int choice) {
		this.choice = choice;

		final Pair<ERouteOption, Integer> entry = distances.get(choice);

		options.setRoute(entry.getFirst());

		final int distance = entry.getSecond();

		// Invalid distance
		if (distance == Integer.MAX_VALUE) {
			return false;
		}

		options.setDistance(distance);

		return true;
	}

	@Override
	public final boolean equals(final Object obj) {

		if (obj instanceof RouteVoyagePlanChoice) {

			final RouteVoyagePlanChoice other = (RouteVoyagePlanChoice) obj;

			if (!Equality.isEqual(distances, other.distances)) {
				return false;
			}

			if (!Equality.isEqual(options, other.options)) {
				return false;
			}

			return true;
		}

		return false;
	}
}
