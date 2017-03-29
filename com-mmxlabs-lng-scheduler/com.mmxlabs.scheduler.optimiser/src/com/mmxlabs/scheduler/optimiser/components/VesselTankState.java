package com.mmxlabs.scheduler.optimiser.components;

public enum VesselTankState {
	MUST_BE_COLD, MUST_BE_WARM,
	/**
	 * Either means can end warm or cold. If the end heel is 0, we assume MUST_BE_WARM, otherwise we assume MUST_BE_COLD and require the heel to be within the specified range.
	 */
	EITHER
}