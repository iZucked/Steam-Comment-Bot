/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.List;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public interface IVesselProvider extends IDataComponentProvider {

	IVesselAvailability getVesselAvailability(IResource resource);

	IResource getResource(IVesselAvailability vessel);
	
	List<IResource> getSortedResources();
}
