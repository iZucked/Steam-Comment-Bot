/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * Editor interface for {@link IVirtualVesselSlotProvider}
 * 
 * @author Simon Goodall
 * 
 * 
 */
public interface IVirtualVesselSlotProviderEditor extends IVirtualVesselSlotProvider {

	void setVesselForElement(IVessel vessel, ISequenceElement element);
}
