/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

@NonNullByDefault
public interface IRoundTripVesselPermissionProviderEditor extends IRoundTripVesselPermissionProvider {

	void permitElementOnResource(ISequenceElement element, IResource resource, IVesselCharter vesselCharter);

	void makeBoundPair(ISequenceElement first, ISequenceElement second);

}
