/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

@NonNullByDefault
public interface IVesselProviderEditor extends IVesselProvider {

	void setVesselCharterResource(IResource resource, IVesselCharter vesselCharter);

	void setResourceOptional(IResource resource, boolean optional);

}
