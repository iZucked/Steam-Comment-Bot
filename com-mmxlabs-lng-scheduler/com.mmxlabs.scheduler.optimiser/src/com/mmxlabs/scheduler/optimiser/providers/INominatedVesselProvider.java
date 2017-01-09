/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * A DCP to store the nominated vessel for a DES Purchase or FOB Sale.
 * 
 * @author Simon Goodall
 * 
 */
public interface INominatedVesselProvider extends IDataComponentProvider {

	/**
	 * Returns the nominated vessel for the given sequence element, or null if there is no mapping defined.
	 * 
	 * @param element
	 * @return
	 */
	@Nullable
	IVessel getNominatedVessel(@NonNull ISequenceElement element);

	@Nullable
	IVessel getNominatedVessel(@NonNull IResource resource);
}
