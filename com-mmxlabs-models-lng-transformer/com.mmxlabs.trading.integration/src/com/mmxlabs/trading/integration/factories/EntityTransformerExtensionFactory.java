/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.trading.integration.factories;

import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.inject.ITransformerExtensionFactory;
import com.mmxlabs.trading.integration.extensions.EntityTransformerExtension;

/**
 * @since 2.0
 */
public class EntityTransformerExtensionFactory implements ITransformerExtensionFactory {

	@Override
	public ITransformerExtension createInstance() {
		return new EntityTransformerExtension();
	}
}
