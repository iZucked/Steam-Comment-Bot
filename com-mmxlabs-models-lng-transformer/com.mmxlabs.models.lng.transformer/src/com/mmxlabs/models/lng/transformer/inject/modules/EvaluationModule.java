/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import java.util.Collection;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VoyagePlanStartDateCharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.entities.impl.DefaultEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IGeneratedCharterOutEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.impl.DefaultBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.impl.DefaultGeneratedCharterOutEvaluator;

/**
 * This {@link Module} configures the default schedule optimisation classes.
 * 
 */
public class EvaluationModule extends AbstractModule {

	private final Collection<String> hints;

	public EvaluationModule(final Collection<String> hints) {
		this.hints = hints;
	}

	@Override
	protected void configure() {

		// Register default implementations
		bind(IEntityValueCalculator.class).to(DefaultEntityValueCalculator.class);
		
		bind(VoyagePlanStartDateCharterRateCalculator.class).in(Singleton.class);
		bind(ICharterRateCalculator.class).to(VoyagePlanStartDateCharterRateCalculator.class);

		bind(IVesselBaseFuelCalculator.class).to(VesselBaseFuelCalculator.class);
		bind(VesselBaseFuelCalculator.class).in(Singleton.class);

		if (hints != null) {
			for (final String hint : hints) {
				if (LNGTransformer.HINT_GENERATE_CHARTER_OUTS.equals(hint)) {
					bind(IGeneratedCharterOutEvaluator.class).to(DefaultGeneratedCharterOutEvaluator.class);
				}
				break;
			}
		}
		bind(IBreakEvenEvaluator.class).to(DefaultBreakEvenEvaluator.class);
	}
}