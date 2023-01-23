/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.vesselcharters;

import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.inject.ITransformerExtensionFactory;

public class VesselCharterEntityTransformerExtensionFactory implements ITransformerExtensionFactory {
 
	@Override
	public ITransformerExtension createInstance() {
		return new VesselCharterEntityTransformer();
	}
}
