package com.mmxlabs.scheduler.optimiser.fitness.impl;

import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;

/**
 * Implementation of {@link IVoyagePlanChoice} to alter NBO choice during idle
 * events. If Idle NBO is true, then this implies that the NBO choice during the
 * journey is true, otherwise {@link #apply(int)} will fail.
 * 
 * @author Simon Goodall
 * 
 */
public final class IdleNBOVoyagePlanChoice implements IVoyagePlanChoice {

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
