/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Triple;
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

	private final List<Triple<String, Integer, Long>> routeOptions;

	public RouteVoyagePlanChoice(final VoyageOptions options, final List<Triple<String, Integer, Long>> routeOptions) {
		this.options = options;
		this.routeOptions = routeOptions;
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
		return routeOptions.size();
	}

	@Override
	public final boolean apply(final int choice) {
		this.choice = choice;

		final Triple<String, Integer, Long> entry = routeOptions.get(choice);

		options.setRoute(entry.getFirst(), entry.getSecond(), entry.getThird());

		return true;
	}

	@Override
	public final boolean equals(final Object obj) {

		if (obj instanceof RouteVoyagePlanChoice) {

			final RouteVoyagePlanChoice other = (RouteVoyagePlanChoice) obj;

			if (!Equality.isEqual(routeOptions, other.routeOptions)) {
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
