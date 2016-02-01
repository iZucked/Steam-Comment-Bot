/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.simplecontracts;

import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.inject.ITransformerExtensionFactory;

/**
 */
public class SimpleContractTransformerFactory implements ITransformerExtensionFactory {

	@Override
	public ITransformerExtension createInstance() {
		return new SimpleContractTransformer();
	}

}
