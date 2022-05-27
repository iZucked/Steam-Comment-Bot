/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;

/**
 * Slots such as the DES Purchase and FOB Sales are bound to a single {@link VesselInstanceType#VIRTUAL} {@link IVesselCharter}. This {@link IDataComponentProvider} maintains the link between the
 * {@link ISequenceElement} for the slot and the {@link IVesselCharter} it is bounds.
 * 
 * @author Simon Goodall
 * 
 * 
 */
public interface IVirtualVesselSlotProvider extends IDataComponentProvider {

	IVesselCharter getVesselCharterForElement(ISequenceElement element);

	ISequenceElement getElementForVesselCharter(IVesselCharter vesselCharter);
}
