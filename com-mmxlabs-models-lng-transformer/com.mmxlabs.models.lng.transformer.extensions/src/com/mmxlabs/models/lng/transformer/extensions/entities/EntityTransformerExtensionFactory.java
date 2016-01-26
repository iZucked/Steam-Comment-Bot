/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.entities;

import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.inject.ITransformerExtensionFactory;

/**
 */
public class EntityTransformerExtensionFactory implements ITransformerExtensionFactory {

	@Override
	public ITransformerExtension createInstance() {
		return new EntityTransformerExtension();
	}
}
