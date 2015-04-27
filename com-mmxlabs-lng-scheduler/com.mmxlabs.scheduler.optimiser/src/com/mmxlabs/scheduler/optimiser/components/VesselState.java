/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

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
	@NonNull
	Laden,

	/**
	 * Vessel has no cargo loaded. However, a small quantitymay remain for use a fuel.
	 */
	@NonNull
	Ballast
}