/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public enum TravelFuelChoice {
	NBO_PLUS_FBO, // NBO is used and topped up by FBO if needed
	NBO_PLUS_BUNKERS, // NBO is used and topped up by bunkers if needed
	BUNKERS; // Bunkers only

	public static final TravelFuelChoice[] TravelChoices = { NBO_PLUS_FBO, NBO_PLUS_BUNKERS, BUNKERS };
	public static final TravelFuelChoice[] TravelSupplementChoices = { NBO_PLUS_FBO, NBO_PLUS_BUNKERS };
	public static final TravelFuelChoice[] TravelChoicesForReliq = { NBO_PLUS_FBO, BUNKERS };

}
