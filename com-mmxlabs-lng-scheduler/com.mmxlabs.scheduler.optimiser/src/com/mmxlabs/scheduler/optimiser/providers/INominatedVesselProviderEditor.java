/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 */
public interface INominatedVesselProviderEditor extends INominatedVesselProvider {

	/**
	 * For the given element (DES Purchase or FOB Sale) specify the {@link IResource} bound to the slot plus the nominated vessel.
	 * 
	 * @param element
	 * @param resource
	 * @param vessel
	 */
	void setNominatedVessel(@NonNull ISequenceElement element, @NonNull IResource resource, @NonNull IVessel vessel);
}
