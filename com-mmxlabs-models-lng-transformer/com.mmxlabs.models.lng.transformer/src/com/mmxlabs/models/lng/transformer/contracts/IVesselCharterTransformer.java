/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.contracts;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

@NonNullByDefault
public interface IVesselCharterTransformer extends ITransformerExtension {

	/**
	 * Called when a vessel charter has been transformed.
	 * 
	 * @param modelSlot
	 * @param optimiserSlot
	 */
	default void vesselCharterTransformed(VesselCharter modelEvent, IVesselCharter optimiserVesselCharter) {
	}

	/**
	 * Called when a vessel charter has been transformed.
	 * 
	 * @param modelSlot
	 * @param optimiserSlot
	 */
	default void charterInVesselCharterTransformed(CharterInMarket modelEvent, IVesselCharter optimiserVesselCharter) {
	}

}
