package com.mmxlabs.scheduler.optimiser.fitness.impl;

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

	public FBOVoyagePlanChoice(final VoyageOptions options) {
		this.options = options;
//		this.choices = new boolean[] { true, false };
	}

	public FBOVoyagePlanChoice(final VoyageOptions options,
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
		final boolean useFBOForSupplement = choice == 0;//choices[choice];
		options.setUseFBOForSupplement(useFBOForSupplement);
		
		if (useFBOForSupplement) {
			// Only a valid choice if NBO is enabled.
			return options.useNBOForTravel();
		}
		
		return true;
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof FBOVoyagePlanChoice) {

			final FBOVoyagePlanChoice other = (FBOVoyagePlanChoice) obj;

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
