package com.mmxlabs.scheduler.optimiser.voyage.impl;

public enum AvailableRouteChoices {
	UNDEFINED, // Undefined state! Assert false etc.
	OPTIMAL, // Optimal decision out of options
	DIRECT_ONLY, // Force use of direct use
	SUEZ_ONLY, // Force use of suez canal
	PANAMA_ONLY, // Force use of panama canal
	EXCLUDE_PANAMA, // Optimal, but exclude panama canala
}
