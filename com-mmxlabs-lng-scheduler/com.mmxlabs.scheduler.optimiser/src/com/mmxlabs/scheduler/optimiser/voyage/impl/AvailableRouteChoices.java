/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

public enum AvailableRouteChoices {
	UNDEFINED, // Undefined state! Assert false etc.
	OPTIMAL, // Optimal decision out of options
	DIRECT_ONLY, // Force use of direct use
	SUEZ_ONLY, // Force use of suez canal
	PANAMA_ONLY, // Force use of panama canal
	EXCLUDE_PANAMA // Optimal, but exclude panama canal
	;

	public static boolean directPermitted(AvailableRouteChoices arc) {
		return arc == DIRECT_ONLY || arc == OPTIMAL || arc == EXCLUDE_PANAMA;
	}

	public static boolean suezPermitted(AvailableRouteChoices arc) {
		return arc == SUEZ_ONLY || arc == OPTIMAL || arc == EXCLUDE_PANAMA;
	}

	public static boolean panamaPermitted(AvailableRouteChoices arc) {
		return arc == PANAMA_ONLY || arc == OPTIMAL;
	}
}
