/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import com.mmxlabs.common.Equality;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;

/**
 * Variation on {@link FBOVoyagePlanChoice} which links the FBO choice across multiple {@link VoyageOptions}
 * 
 * @author Simon Goodall
 * 
 */
public class LinkedFBOVoyagePlanChoice implements IVoyagePlanChoice {

	private int choice;

	private final VoyageOptions[] options;

	public LinkedFBOVoyagePlanChoice(final VoyageOptions... options) {
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
		final boolean useFBOForSupplement = choice == 0;
		for (final VoyageOptions option : options) {
			option.setUseFBOForSupplement(useFBOForSupplement);
			if (useFBOForSupplement) {
				// Only a valid choice if NBO is enabled.
				if (!option.useNBOForTravel()) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public final boolean equals(final Object obj) {

		if (obj instanceof LinkedFBOVoyagePlanChoice) {

			final LinkedFBOVoyagePlanChoice other = (LinkedFBOVoyagePlanChoice) obj;

			if (!Equality.isEqual(options, other.options)) {
				return false;
			}

			return true;
		}

		return false;
	}

}
