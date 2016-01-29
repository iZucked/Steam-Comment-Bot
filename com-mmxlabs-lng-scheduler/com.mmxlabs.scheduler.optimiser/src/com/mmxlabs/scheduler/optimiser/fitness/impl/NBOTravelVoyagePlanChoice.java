/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import com.mmxlabs.common.Equality;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;

/**
 * Implementation of {@link IVoyagePlanChoice} to alter NBO choice during journey events.
 * 
 * @author Simon Goodall
 * 
 */
public final class NBOTravelVoyagePlanChoice implements IVoyagePlanChoice {

	private int choice;

	private final VoyageOptions previousOptions;

	private final VoyageOptions options;

	public NBOTravelVoyagePlanChoice(final VoyageOptions previousOptions, final VoyageOptions options) {
		this.previousOptions = previousOptions;
		this.options = options;
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
	public final int numChoices() {

		return 2;
	}

	@Override
	public final boolean apply(final int choice) {
		this.choice = choice;
		final boolean useNBO = choice == 0;

		options.setUseNBOForTravel(useNBO);

		if (useNBO && (previousOptions != null)) {
			return previousOptions.useNBOForTravel();
		}

		// Ensure NBO is always true when state is laden
		if (!useNBO && (options.getVesselState() == VesselState.Laden)) {
			return false;
		}

		return true;
	}

	@Override
	public final boolean equals(final Object obj) {

		if (obj instanceof NBOTravelVoyagePlanChoice) {

			final NBOTravelVoyagePlanChoice other = (NBOTravelVoyagePlanChoice) obj;

			if (!Equality.isEqual(options, other.options)) {
				return false;
			}

			if (!Equality.isEqual(previousOptions, other.previousOptions)) {
				return false;
			}

			return true;
		}

		return false;
	}
}
