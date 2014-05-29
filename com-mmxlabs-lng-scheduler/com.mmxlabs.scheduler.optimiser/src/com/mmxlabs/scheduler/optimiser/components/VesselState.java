/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

/**
 * Enum defining the states a vessel can be in.
 * 
 * @author Simon Goodall
 * 
 */
public enum VesselState {
	/**
	 * Vessel has a cargo loaded
	 */
	Laden,

	/**
	 * Vessel has no cargo loaded. However, a small quantitymay remain for use a fuel.
	 */
	Ballast
}