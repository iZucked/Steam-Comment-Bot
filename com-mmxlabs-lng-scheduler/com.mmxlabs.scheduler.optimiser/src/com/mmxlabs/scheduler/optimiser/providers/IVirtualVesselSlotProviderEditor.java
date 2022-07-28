/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

/**
 * Editor interface for {@link IVirtualVesselSlotProvider}
 * 
 * @author Simon Goodall
 * 
 * 
 */
@NonNullByDefault
public interface IVirtualVesselSlotProviderEditor extends IVirtualVesselSlotProvider {

	void setVesselCharterForElement(IVesselCharter vesselCharter, ISequenceElement element);
}
