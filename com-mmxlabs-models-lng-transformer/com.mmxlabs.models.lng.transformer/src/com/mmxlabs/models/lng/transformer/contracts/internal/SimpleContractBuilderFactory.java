package com.mmxlabs.models.lng.transformer.contracts.internal;

import com.mmxlabs.models.lng.transformer.inject.IBuilderExtensionFactory;
import com.mmxlabs.scheduler.optimiser.builder.IBuilderExtension;

public class SimpleContractBuilderFactory implements IBuilderExtensionFactory {

	@Override
	public IBuilderExtension createInstance() {
		return new SimpleContractBuilder();
	}

}
