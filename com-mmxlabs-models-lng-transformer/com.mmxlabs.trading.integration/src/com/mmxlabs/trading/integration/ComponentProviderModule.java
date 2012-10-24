/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.integration;

import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.mmxlabs.trading.optimiser.components.ProfitAndLossAllocationComponentProvider;

/**
 * @since 2.0
 */
public class ComponentProviderModule extends PeaberryActivationModule {
	@Override
	protected void configure() {
		bindService(ProfitAndLossAllocationComponentProvider.class).export();

		bindService(StandardContractBuilderFactory.class).export();
		bindService(TradingOptimiserModuleService.class).export();
	}
}
