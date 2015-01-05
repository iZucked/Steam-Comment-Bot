/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 */
public interface IShippingHoursRestrictionProviderEditor extends IShippingHoursRestrictionProvider {

	/**
	 * Set shipping days restrictions. Implies {@link IShippingHoursRestrictionProvider#isDivertable(ISequenceElement)} will be true for this element
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
	void setReferenceSpeed(@NonNull IVessel vessel, int referenceSpeed);

}
