package com.mmxlabs.models.lng.transformer.inject;

import com.mmxlabs.models.lng.transformer.ITransformerExtension;

/**
 * @since 2.0
 */
public interface ITransformerExtensionFactory {
	ITransformerExtension createInstance();
}
