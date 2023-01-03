/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Objects;

import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.voyage.IdleFuelChoice;
import com.mmxlabs.scheduler.optimiser.voyage.TravelFuelChoice;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;

/**
 * Implementation of {@link IVoyagePlanChoice} to alter NBO choice during idle events. If Idle NBO is true, then this implies that the NBO choice during the journey is true, otherwise
 * {@link #apply(int)} will fail.
 * 
 * @author Simon Goodall
 * 
 */
public final class IdleNBOVoyagePlanChoice implements IVoyagePlanChoice {

	private int choice;

	private final VoyageOptions options;

	public IdleNBOVoyagePlanChoice(final VoyageOptions options) {
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
		return IdleFuelChoice.values().length;
	}

	@Override
	public final boolean apply(final int choice) {
		
		this.choice = choice;
		final IdleFuelChoice idleFuelChoice = IdleFuelChoice.values()[choice];
		options.setIdleFuelChoice(idleFuelChoice);

		// We have to use idle NBO when laden
		if (idleFuelChoice == IdleFuelChoice.BUNKERS && (options.getVesselState() == VesselState.Laden)) {
			return false;
		}

		if (idleFuelChoice == IdleFuelChoice.NBO) {
			// Only a valid choice if NBO is enabled.
			return options.getTravelFuelChoice() != TravelFuelChoice.BUNKERS;
		}

		return true;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof IdleNBOVoyagePlanChoice) {

			final IdleNBOVoyagePlanChoice other = (IdleNBOVoyagePlanChoice) obj;

			if (!Objects.equals(options, other.options)) {
				return false;
			}

			return true;
		}

		return false;
	}
}
