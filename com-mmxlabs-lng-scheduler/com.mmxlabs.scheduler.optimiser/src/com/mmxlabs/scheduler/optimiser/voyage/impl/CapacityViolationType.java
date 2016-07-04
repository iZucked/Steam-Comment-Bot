/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

/**
 * Enum specifying the different types of capacity constraint violation
 * 
 * @author Simon Goodall
 * 
 */
public enum CapacityViolationType {
	/**
	 * The capacity of the vessel has been exceeded
	 */
	VESSEL_CAPACITY("Vessel Capacity Breach"),
	/**
	 * The minimum load volume has not been attained
	 */
	MIN_LOAD("Minimum Load Breach"),
	/**
	 * The maximum load volume has been exceeded
	 */
	MAX_LOAD("Maximum Load Breach"),
	/**
	 * The minimum discharge volume has not been attained
	 */
	MIN_DISCHARGE("Minimum Discharge Breach"),
	/**
	 * The maximum discharge volume has been exceeded
	 */
	MAX_DISCHARGE("Maximum Discharge Breach"),
	/**
	 * A cooldown has been forced where a cooldown is not permitted
	 */
	FORCED_COOLDOWN("Cooldown forced"),
	/**
	 * The voyage consumption exceeds available heel volume
	 */
	MAX_HEEL("Heel Breach"),
	/**
	 * Heel lost at the end of the {@link VoyagePlan}. Typically the safety heel. This is the heel which cannot be rolled over to the next voyage plan (once heel tracking is merged in)
	 */
	LOST_HEEL("Lost Heel");

	private final String displayName;

	private CapacityViolationType(final String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Returns a human readable name for this enum.
	 * 
	 * @return
	 */
	public String getDisplayName() {
		return displayName;
	}

}
