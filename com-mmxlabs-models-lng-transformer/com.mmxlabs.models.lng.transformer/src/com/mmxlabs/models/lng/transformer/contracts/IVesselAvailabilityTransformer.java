/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.contracts;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

@NonNullByDefault
public interface IVesselAvailabilityTransformer extends ITransformerExtension {

	/**
	 * Called when a vessel availability has been transformed.
	 * 
	 * @param modelSlot
	 * @param optimiserSlot
	 */
	default void vesselAvailabilityTransformed(VesselAvailability modelEvent, IVesselAvailability optimiserEventSlot) {
	}

	/**
	 * Called when a vessel availability has been transformed.
	 * 
	 * @param modelSlot
	 * @param optimiserSlot
	 */
	default void charterInVesselAvailabilityTransformed(CharterInMarket modelEvent, IVesselAvailability optimiserEventSlot) {
	}

}
