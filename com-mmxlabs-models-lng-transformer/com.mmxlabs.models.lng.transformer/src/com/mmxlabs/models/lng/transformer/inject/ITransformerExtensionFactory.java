/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject;

import com.mmxlabs.models.lng.transformer.ITransformerExtension;

/**
 * @since 2.0
 */
public interface ITransformerExtensionFactory {
	ITransformerExtension createInstance();
}
