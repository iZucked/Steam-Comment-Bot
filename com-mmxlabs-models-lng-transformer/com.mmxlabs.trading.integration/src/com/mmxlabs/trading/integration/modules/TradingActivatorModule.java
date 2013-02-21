/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.trading.integration.modules;

import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.mmxlabs.trading.integration.TradingOptimiserModuleService;
import com.mmxlabs.trading.integration.factories.EntityTransformerExtensionFactory;
import com.mmxlabs.trading.integration.factories.TradingExporterExtensionFactory;
import com.mmxlabs.trading.optimiser.components.ProfitAndLossAllocationComponentProvider;

/**
 * @since 2.0
 */
public class TradingActivatorModule extends PeaberryActivationModule {
	@Override
	protected void configure() {

	}
}
