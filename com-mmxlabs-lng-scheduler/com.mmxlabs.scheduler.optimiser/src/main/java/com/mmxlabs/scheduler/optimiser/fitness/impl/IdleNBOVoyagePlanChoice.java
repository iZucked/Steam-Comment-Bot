package com.mmxlabs.scheduler.optimiser.fitness.impl;

import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;

public class IdleNBOVoyagePlanChoice implements IVoyagePlanChoice {

	private final VoyageOptions options;

	private final boolean[] choices;

	public IdleNBOVoyagePlanChoice(final VoyageOptions options) {
		this.options = options;
		this.choices = new boolean[] { true, false };
	}

	public IdleNBOVoyagePlanChoice(final VoyageOptions options,
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

		boolean useNBOForIdle = choices[choice];
		options.setUseNBOForIdle(useNBOForIdle);
		// Only a valid choice if NBO is enabled.
		return options.useNBOForTravel();
	}
}
