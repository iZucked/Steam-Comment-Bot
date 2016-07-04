/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public interface IRoundTripVesselPermissionProviderEditor extends IRoundTripVesselPermissionProvider {

	void permitElementOnResource(@NonNull ISequenceElement element, @NonNull IPortSlot portSlot, @NonNull IResource resource, @NonNull IVesselAvailability vesselAvailability);
	
	void makeBoundPair(@NonNull ISequenceElement first, @NonNull ISequenceElement second);

}
