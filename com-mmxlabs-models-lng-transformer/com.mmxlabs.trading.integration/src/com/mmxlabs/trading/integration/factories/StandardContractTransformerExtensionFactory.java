package com.mmxlabs.trading.integration.factories;

import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.inject.ITransformerExtensionFactory;
import com.mmxlabs.trading.integration.extensions.StandardContractTransformerExtension;

/**
 * @since 2.0
 */
public class StandardContractTransformerExtensionFactory implements ITransformerExtensionFactory {

	@Override
	public ITransformerExtension createInstance() {
		return new StandardContractTransformerExtension();
	}
}
