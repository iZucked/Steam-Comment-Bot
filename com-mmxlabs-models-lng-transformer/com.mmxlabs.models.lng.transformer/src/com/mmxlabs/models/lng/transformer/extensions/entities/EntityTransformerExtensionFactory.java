/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.entities;

import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.inject.ITransformerExtensionFactory;

/**
 * @since 3.0
 */
public class EntityTransformerExtensionFactory implements ITransformerExtensionFactory {

	@Override
	public ITransformerExtension createInstance() {
		return new EntityTransformerExtension();
	}
}
