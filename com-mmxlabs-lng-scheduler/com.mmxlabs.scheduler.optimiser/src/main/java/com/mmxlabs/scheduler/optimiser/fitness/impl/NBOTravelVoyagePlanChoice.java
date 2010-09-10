package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Arrays;

import com.mmxlabs.common.Equality;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;

/**
 * Implementation of {@link IVoyagePlanChoice} to alter NBO choice during
 * journey events.
 * 
 * @author Simon Goodall
 * 
 */
public final class NBOTravelVoyagePlanChoice implements IVoyagePlanChoice {

	private final VoyageOptions previousOptions;
	
	private final VoyageOptions options;

	private final boolean[] choices;

	public NBOTravelVoyagePlanChoice(final VoyageOptions previousOptions, final VoyageOptions options) {
		this.previousOptions = previousOptions;
		this.options = options;
		this.choices = new boolean[] { true, false };
	}

	public NBOTravelVoyagePlanChoice(final VoyageOptions previousOptions, final VoyageOptions options,
			final boolean[] choices) {
		this.previousOptions = previousOptions;
		this.options = options;
		this.choices = choices;
	}

	@Override
	public int numChoices() {
		return choices.length;
	}

	@Override
	public boolean apply(final int choice) {

		boolean useNBO = choices[choice];
		
		options.setUseNBOForTravel(useNBO);
		
		if (useNBO && previousOptions != null) {
			return previousOptions.useNBOForTravel();
		}
		
		// Ensure NBO is always true when state is laden
		if (!useNBO && options.getVesselState() == VesselState.Laden) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof NBOTravelVoyagePlanChoice) {

			final NBOTravelVoyagePlanChoice other = (NBOTravelVoyagePlanChoice) obj;

			if (!Arrays.equals(choices, other.choices)) {
				return false;
			}

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
