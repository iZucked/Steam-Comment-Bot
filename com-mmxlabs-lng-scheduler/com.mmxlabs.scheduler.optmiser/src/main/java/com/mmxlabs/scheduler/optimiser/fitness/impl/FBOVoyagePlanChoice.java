package com.mmxlabs.scheduler.optimiser.fitness.impl;

import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;

public class FBOVoyagePlanChoice implements IVoyagePlanChoice {

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

		boolean useFBOForSupplement = choices[choice];
		options.setUseFBOForSupplement(useFBOForSupplement);
		// Only a valid choice if NBO is enabled.
		return options.useNBOForTravel();
	}
}
