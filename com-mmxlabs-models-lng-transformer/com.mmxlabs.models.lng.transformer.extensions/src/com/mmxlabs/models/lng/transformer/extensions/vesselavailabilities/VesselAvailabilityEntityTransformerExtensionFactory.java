/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.vesselavailabilities;

import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.inject.ITransformerExtensionFactory;

/**
 */
public class VesselAvailabilityEntityTransformerExtensionFactory implements ITransformerExtensionFactory {

	@Override
	public ITransformerExtension createInstance() {
		return new VesselAvailabilityEntityTransformer();
	}
}
