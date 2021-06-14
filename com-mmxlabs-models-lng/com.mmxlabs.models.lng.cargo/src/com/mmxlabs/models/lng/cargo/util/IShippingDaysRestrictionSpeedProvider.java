/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;

/**
 * Interface for custom code to implement to provide an alternative speed for shipping days restrictions time checking.
 * 
 * @author Simon Goodall
 * 
 */
public interface IShippingDaysRestrictionSpeedProvider {

	/**
	 * Return the reference speed to use to check shipping days times for this vessel.
	 * 
	 * @param vessel
	 * @return
	 */

	double getSpeed(@NonNull LoadSlot desPurchase, @NonNull Vessel vessel, boolean isLaden);

	double getSpeed(@NonNull DischargeSlot fobSale, @NonNull Vessel vessel, boolean isLaden);

	@NonNull
	Collection<@NonNull Route> getValidRoutes(@NonNull PortModel portModel, @NonNull final LoadSlot desPurchase);

	@NonNull
	Collection<@NonNull Route> getValidRoutes(@NonNull PortModel portModel, @NonNull final DischargeSlot fobSale);
}
