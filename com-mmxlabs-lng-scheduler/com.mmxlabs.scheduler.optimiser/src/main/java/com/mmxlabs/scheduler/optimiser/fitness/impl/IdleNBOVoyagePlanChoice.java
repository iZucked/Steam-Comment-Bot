package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Arrays;

import com.mmxlabs.common.Equality;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
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

	public boolean reset() {
		for (int i = 0; i<numChoices(); i++)
			if (apply(i)) return true;
		return false;
	}
	
	@Override
	public boolean nextChoice() {
		while (true) {
			if (choice + 1 == numChoices()) {
				return true;
			}
			if (apply(choice + 1))
				return false;
		}
	}
	private int choice;
	
	private final VoyageOptions options;

//	private final boolean[] choices;

	public IdleNBOVoyagePlanChoice(final VoyageOptions options) {
		this.options = options;
//		this.choices = new boolean[] { true, false };
	}

	public IdleNBOVoyagePlanChoice(final VoyageOptions options,
			final boolean[] choices) {
		this.options = options;
//		this.choices = choices;
	}

	@Override
	public int numChoices() {
//		return choices.length;
		return 2;
	}

	@Override
	public boolean apply(final int choice) {
		this.choice = choice;
		final boolean useNBOForIdle = choice == 0;
		options.setUseNBOForIdle(useNBOForIdle);

		// We have to use idle NBO when laden
		if (!useNBOForIdle && options.getVesselState() == VesselState.Laden) {
			return false;
		}

		if (useNBOForIdle) {
			// Only a valid choice if NBO is enabled.
			return options.useNBOForTravel();
		}
		
		return true;
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof IdleNBOVoyagePlanChoice) {

			final IdleNBOVoyagePlanChoice other = (IdleNBOVoyagePlanChoice) obj;

//			if (!Arrays.equals(choices, other.choices)) {
//				return false;
//			}

			if (!Equality.isEqual(options, other.options)) {
				return false;
			}

			return true;
		}

		return false;
	}
}
