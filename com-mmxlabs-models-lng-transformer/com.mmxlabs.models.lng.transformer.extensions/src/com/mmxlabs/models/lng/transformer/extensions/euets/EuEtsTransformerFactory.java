package com.mmxlabs.models.lng.transformer.extensions.euets;

import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.inject.ITransformerExtensionFactory;

public class EuEtsTransformerFactory implements ITransformerExtensionFactory  {

	@Override
	public ITransformerExtension createInstance() {
		return new EuEtsTransformer();
	}

}
