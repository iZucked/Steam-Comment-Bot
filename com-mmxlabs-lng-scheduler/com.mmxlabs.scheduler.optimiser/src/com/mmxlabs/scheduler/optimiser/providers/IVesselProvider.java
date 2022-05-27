/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

@NonNullByDefault
public interface IVesselProvider extends IDataComponentProvider {

	IVesselCharter getVesselCharter(IResource resource);

	boolean isResourceOptional(IResource resource);

	IResource getResource(IVesselCharter vesselCharter);

	List<IResource> getSortedResources();
}
