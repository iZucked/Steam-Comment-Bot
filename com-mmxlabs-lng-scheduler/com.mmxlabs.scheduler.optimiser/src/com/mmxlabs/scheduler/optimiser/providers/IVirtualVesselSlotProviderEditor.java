/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

/**
 * Editor interface for {@link IVirtualVesselSlotProvider}
 * 
 * @author Simon Goodall
 * 
 * 
 */
public interface IVirtualVesselSlotProviderEditor extends IVirtualVesselSlotProvider {

	void setVesselAvailabilityForElement(IVesselAvailability vesselAvailability, ISequenceElement element);
}
