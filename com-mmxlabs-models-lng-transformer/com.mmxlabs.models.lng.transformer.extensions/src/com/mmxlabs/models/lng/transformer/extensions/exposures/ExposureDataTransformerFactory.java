/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.exposures;

import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.inject.ITransformerExtensionFactory;

/**
 */ 
public class ExposureDataTransformerFactory implements ITransformerExtensionFactory {

	@Override
	public ITransformerExtension createInstance() {
		return new ExposureDataTransformer();
	}

}
