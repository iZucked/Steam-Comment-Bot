/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;
import java.util.List;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public interface IVesselProvider extends IDataComponentProvider {

	@NonNull
	IVesselAvailability getVesselAvailability(@NonNull IResource resource);

	@NonNull
	IResource getResource(@NonNull IVesselAvailability vessel);
	
	@NonNull
	List<IResource> getSortedResources();
}
