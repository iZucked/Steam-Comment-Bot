/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

public interface IVesselProviderEditor extends IVesselProvider {

	void setVesselResource(IResource resource, IVessel vessel);
	
}
