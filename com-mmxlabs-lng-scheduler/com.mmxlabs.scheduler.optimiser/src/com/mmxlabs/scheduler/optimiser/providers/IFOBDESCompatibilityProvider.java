/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

/**
 * A {@link IDataComponentProvider} to provide information about which {@link IPortSlot}s can be used on FOB/DES cargoes resources
 * 
 * @author Simon Goodall
 * 
 */
public interface IFOBDESCompatibilityProvider extends IDataComponentProvider {

	/**
	 * Returns true if slot can be used on the given availability (checked if FOB/DES or returns *false* for non FOB/DES resources)
	 * 
	 * @param portSlot
	 * @param vesselAvailability
	 * @return
	 */
	boolean isPermittedOnResource(@NonNull IPortSlot portSlot, @NonNull IVesselAvailability vesselAvailability);

	boolean isPermittedOnResource(@NonNull ISequenceElement element, @NonNull IResource resource);

}
