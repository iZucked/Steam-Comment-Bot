/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Objects;

import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.voyage.IdleFuelChoice;
import com.mmxlabs.scheduler.optimiser.voyage.TravelFuelChoice;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;

/**
 * Implementation of {@link IVoyagePlanChoice} to alter NBO choice during journey events.
 * 
 * @author Simon Goodall
 * 
 */
public final class TravelSupplementVoyagePlanChoice implements IVoyagePlanChoice {

	private int choice;

	private final VoyageOptions previousOptions;

	private final VoyageOptions options;

	public TravelSupplementVoyagePlanChoice(final VoyageOptions previousOptions, final VoyageOptions options) {
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

		return TravelFuelChoice.TravelSupplementChoices.length;
	}

	@Override
	public final boolean apply(final int choice) {

		this.choice = choice;
		final TravelFuelChoice fuelChoice = TravelFuelChoice.TravelChoices[choice];

		options.setTravelFuelChoice(fuelChoice);

		if (fuelChoice != TravelFuelChoice.BUNKERS && (previousOptions != null)) {
			return previousOptions.getIdleFuelChoice() != IdleFuelChoice.BUNKERS;
		}

		// Ensure NBO is always true when state is laden
		if (fuelChoice == TravelFuelChoice.BUNKERS && (options.getVesselState() == VesselState.Laden)) {
			return false;
		}

		return true;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof TravelSupplementVoyagePlanChoice) {

			final TravelSupplementVoyagePlanChoice other = (TravelSupplementVoyagePlanChoice) obj;

			if (!Objects.equals(options, other.options)) {
				return false;
			}

			if (!Objects.equals(previousOptions, other.previousOptions)) {
				return false;
			}

			return true;
		}

		return false;
	}
}
