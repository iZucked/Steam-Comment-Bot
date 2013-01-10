package com.mmxlabs.models.lng.transformer.extensions.simplecontracts;

import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.inject.ITransformerExtensionFactory;

/**
 * @since 2.0
 */
public class SimpleContractTransformerFactory implements ITransformerExtensionFactory {

	@Override
	public ITransformerExtension createInstance() {
		return new SimpleContractTransformer();
	}

}
