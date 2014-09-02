/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public interface IVesselProviderEditor extends IVesselProvider {

	void setVesselAvailabilityResource(IResource resource, IVesselAvailability vesselAvailability);

}
