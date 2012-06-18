package com.mmxlabs.trading.integration;

import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.mmxlabs.trading.optimiser.components.ProfitAndLossAllocationComponent;
import com.mmxlabs.trading.optimiser.components.ProfitAndLossAllocationComponentProvider;

public class ComponentProviderModule extends PeaberryActivationModule {
	@Override
	protected void configure() {
		bindService(ProfitAndLossAllocationComponentProvider.class).export();
	}
}
