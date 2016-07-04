/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public interface IVesselProviderEditor extends IVesselProvider {

	void setVesselAvailabilityResource(@NonNull IResource resource, @NonNull IVesselAvailability vesselAvailability);

}
