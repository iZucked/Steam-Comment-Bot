/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

/**
 */
public interface IShippingHoursRestrictionProviderEditor extends IShippingHoursRestrictionProvider {

	/**
	 * Set shipping hours restrictions. Implies {@link IShippingHoursRestrictionProvider#isDivertible(ISequenceElement)} will be true for this element
	 * 
	 * @param element
	 * @param baseTimeWindow
	 * @param hours
	 */
	void setShippingHoursRestriction(@NonNull ISequenceElement element, @NonNull ITimeWindow baseTimeWindow, int hours);

	/**
	 * Set the reference speed for this vessel instance if used for DES diversions.
	 * 
	 * @param vessel
	 * @param referenceSpeed
	 */
	void setReferenceSpeed(@NonNull IPortSlot slot, @NonNull IVessel vessel, VesselState vesselState, int referenceSpeed);

	/**
	 * Add an allowed route that a DES cargo can follow
	 * 
	 * @param route
	 */
	void setDivertibleDESAllowedRoute(@NonNull ILoadOption desPurchase, @NonNull ERouteOption route);

	/**
	 * Add an allowed route that a FOB sale cargo can follow
	 * 
	 * @param route
	 */
	void setDivertibleFOBAllowedRoute(@NonNull IDischargeOption fobSale, @NonNull ERouteOption route);
}
