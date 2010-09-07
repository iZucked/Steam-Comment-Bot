package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Arrays;

import com.mmxlabs.common.Equality;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;

/**
 * Implementation of {@link IVoyagePlanChoice} to alter FBO choice during
 * journey events. If FBO is true, then this implies that the NBO choice is
 * true, otherwise {@link #apply(int)} will fail.
 * 
 * @author Simon Goodall
 * 
 */
public final class FBOVoyagePlanChoice implements IVoyagePlanChoice {

	private final VoyageOptions options;

	private final boolean[] choices;

	public FBOVoyagePlanChoice(final VoyageOptions options) {
		this.options = options;
		this.choices = new boolean[] { true, false };
	}

	public FBOVoyagePlanChoice(final VoyageOptions options,
			final boolean[] choices) {
		this.options = options;
		this.choices = choices;
	}

	@Override
	public int numChoices() {
		return choices.length;
	}

	@Override
	public boolean apply(final int choice) {

		final boolean useFBOForSupplement = choices[choice];
		options.setUseFBOForSupplement(useFBOForSupplement);
		// Only a valid choice if NBO is enabled.
		return options.useNBOForTravel();
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof FBOVoyagePlanChoice) {

			final FBOVoyagePlanChoice other = (FBOVoyagePlanChoice) obj;

			if (!Arrays.equals(choices, other.choices)) {
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
