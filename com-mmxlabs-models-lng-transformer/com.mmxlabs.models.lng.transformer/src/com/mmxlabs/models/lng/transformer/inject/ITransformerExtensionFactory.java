/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject;

import com.mmxlabs.models.lng.transformer.ITransformerExtension;

/**
 */
public interface ITransformerExtensionFactory {
	ITransformerExtension createInstance();
}
