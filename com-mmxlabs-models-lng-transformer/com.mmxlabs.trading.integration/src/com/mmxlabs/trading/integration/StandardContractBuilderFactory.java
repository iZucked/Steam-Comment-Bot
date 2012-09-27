package com.mmxlabs.trading.integration;

import com.mmxlabs.models.lng.transformer.inject.IBuilderExtensionFactory;
import com.mmxlabs.scheduler.optimiser.builder.IBuilderExtension;
import com.mmxlabs.trading.optimiser.builder.TradingBuilderExtension;

public class StandardContractBuilderFactory implements IBuilderExtensionFactory {

	@Override
	public IBuilderExtension createInstance() {
		return new StandardContractBuilder();
	}
}
