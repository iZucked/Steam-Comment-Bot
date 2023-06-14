/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.fobsalerotations;

import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.inject.ITransformerExtensionFactory;

public class FobSaleRotationTransformerFactory implements ITransformerExtensionFactory {

	@Override
	public ITransformerExtension createInstance() {
		return new FobSaleRotationTransformer();
	}

}
