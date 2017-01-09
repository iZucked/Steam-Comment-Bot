/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

/**
 * The {@link VesselInstanceType} indicates what the {@link IVessel} instance is. For example, is it a core fleet vessel, or is it an instance created to handle spot charters?
 * 
 * @author Simon Goodall
 * 
 */
public enum VesselInstanceType {

	/**
	 * Unknown type
	 */
	UNKNOWN,

	/**
	 * Core fleet vessel. Implies vessel is always available for use.
	 */
	FLEET,

	/**
	 * Long term charter vessel. May have a specific start or end time and port, depending on the overlap of contracted period with the optimisation period.
	 */
	TIME_CHARTER,

	/**
	 * Short term charter to deal with one or two cargoes. Start and end time and port needs to be determined by optimiser.
	 */
	SPOT_CHARTER,

	/**
	 * Sequence represents a FOB Sale
	 * 
	 */
	FOB_SALE,

	/**
	 * Sequence represents a DES Purchase
	 * 
	 */
	DES_PURCHASE,

	/**
	 * Sequence cargoes evaluated on a round trip basis.
	 * 
	 */
	ROUND_TRIP
}
