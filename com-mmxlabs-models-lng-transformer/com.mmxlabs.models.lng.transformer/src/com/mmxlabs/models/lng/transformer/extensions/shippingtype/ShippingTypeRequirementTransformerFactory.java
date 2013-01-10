package com.mmxlabs.models.lng.transformer.extensions.shippingtype;

import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.inject.ITransformerExtensionFactory;

/**
 * @since 2.0
 */
public class ShippingTypeRequirementTransformerFactory implements ITransformerExtensionFactory {

	@Override
	public ITransformerExtension createInstance() {
		return new ShippingTypeRequirementTransformer();
	}

}
