/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.VesselClass;

/**
 * Interface for custom code to implement to provide an alternative speed for shipping days restrictions time checking.
 * 
 * @author Simon Goodall
 * 
 */
public interface IShippingDaysRestrictionSpeedProvider {

	/**
	 * Return the reference speed to use to check shipping days times for this vessel class.
	 * 
	 * @param vesselClass
	 * @return
	 */
	double getSpeed(@NonNull VesselClass vesselClass);
}
