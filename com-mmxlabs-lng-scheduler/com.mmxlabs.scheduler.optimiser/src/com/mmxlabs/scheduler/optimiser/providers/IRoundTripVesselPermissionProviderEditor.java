/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

public interface IRoundTripVesselPermissionProviderEditor extends IRoundTripVesselPermissionProvider {

	void permitElementOnResource(@NonNull ISequenceElement element, @NonNull IPortSlot portSlot, @NonNull IResource resource, @NonNull IVesselCharter vesselCharter);
	
	void makeBoundPair(@NonNull ISequenceElement first, @NonNull ISequenceElement second);

}
