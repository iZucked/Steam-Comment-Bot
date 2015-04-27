/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

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
	@NonNull
	UNKNOWN,

	/**
	 * Core fleet vessel. Implies vessel is always available for use.
	 */
	@NonNull
	FLEET,

	/**
	 * Long term charter vessel. May have a specific start or end time and port, depending on the overlap of contracted period with the optimisation period.
	 */
	@NonNull
	TIME_CHARTER,

	/**
	 * Short term charter to deal with one or two cargoes. Start and end time and port needs to be determined by optimiser.
	 */
	@NonNull
	SPOT_CHARTER,

	/**
	 * Sequence represents a FOB Sale
	 * 
	 */
	@NonNull
	FOB_SALE,

	/**
	 * Sequence represents a DES Purchase
	 * 
	 */
	@NonNull
	DES_PURCHASE,

	/**
	 * Sequence to store "short" cargoes.
	 * 
	 */
	@NonNull
	CARGO_SHORTS
}
