/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import com.mmxlabs.common.Equality;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;

/**
 * Sets whether cooldown is used for a given voyage.
 * 
 * @author hinton
 * 
 */
public class CooldownVoyagePlanChoice implements IVoyagePlanChoice {
	private final int choice = 0;
	private final VoyageOptions options;

	public CooldownVoyagePlanChoice(final VoyageOptions options) {
		this.options = options;
	}

	@Override
	public int numChoices() {
		return 2;
	}

	@Override
	public boolean apply(final int choice) {
		final boolean cooldown = choice == 0;
		options.setAllowCooldown(cooldown);

		if (!cooldown) {
			// if we are not allowing a cooldown, we have to be using NBO for
			// travel.
			return options.useNBOForTravel();
		}

		return true;
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
	public boolean nextChoice() {
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
	public final boolean equals(final Object obj) {

		if (obj instanceof CooldownVoyagePlanChoice) {

			final CooldownVoyagePlanChoice other = (CooldownVoyagePlanChoice) obj;

			if (!Equality.isEqual(options, other.options)) {
				return false;
			}

			return true;
		}

		return false;
	}
}
