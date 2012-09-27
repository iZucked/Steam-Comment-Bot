package com.mmxlabs.trading.integration;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.scheduler.optimiser.contracts.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.trading.optimiser.contracts.impl.DefaultEntityValueCalculator;

/**
 * Returns a Guice {@link Module} to provide additional components to add to the main optimisation {@link Injector}
 * 
 * @since 1.1
 */
public class TradingOptimiserModuleService implements IOptimiserInjectorService {

	@Override
	public Module requestModule() {

		return new AbstractModule() {

			@Override
			protected void configure() {
				// Register our entity based P&L group calculator
				bind(IEntityValueCalculator.class).to(DefaultEntityValueCalculator.class);
			}
		};
	}
}
