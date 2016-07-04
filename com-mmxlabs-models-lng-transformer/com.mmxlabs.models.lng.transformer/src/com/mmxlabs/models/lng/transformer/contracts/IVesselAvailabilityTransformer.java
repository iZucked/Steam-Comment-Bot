/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.contracts;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public interface IVesselAvailabilityTransformer extends ITransformerExtension {

	/**
	 * Called when a vessel availability has been transformed.
	 *  
	 * @param modelSlot
	 * @param optimiserSlot
	 */
	public void vesselAvailabilityTransformed(@NonNull VesselAvailability modelEvent, @NonNull IVesselAvailability optimiserEventSlot);

}
