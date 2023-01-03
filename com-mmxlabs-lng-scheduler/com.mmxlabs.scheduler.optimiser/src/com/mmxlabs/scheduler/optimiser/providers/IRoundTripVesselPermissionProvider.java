/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

/**
 * A {@link IDataComponentProvider} to provide information about which {@link IPortSlot}s can be used on which nominal vessels (round trip vessel types).
 * 
 * @author Simon Goodall
 * 
 */
public interface IRoundTripVesselPermissionProvider extends IDataComponentProvider {

	boolean isPermittedOnResource(@NonNull IPortSlot portSlot, @NonNull IVesselCharter vesselCharter);

	boolean isPermittedOnResource(@NonNull ISequenceElement element, @NonNull IResource resource);

	/**
	 * If elements are permitted, is the order valid.
	 * @param first
	 * @param second
	 * @return
	 */
	boolean isBoundPair(@NonNull ISequenceElement first, @NonNull ISequenceElement second);

}
