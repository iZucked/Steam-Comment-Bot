package com.mmxlabs.models.lng.transformer.contracts;

import com.mmxlabs.models.lng.transformer.inject.IBuilderExtensionFactory;
import com.mmxlabs.scheduler.optimiser.builder.IBuilderExtension;

/**
 * @since 2.0
 */
public class SimpleContractBuilderFactory implements IBuilderExtensionFactory {

	@Override
	public IBuilderExtension createInstance() {
		return new SimpleContractBuilder();
	}

}
