/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.util.Map;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.ui.breakdown.chain.LNGParameters_ActionPlanSettingsModule;
import com.mmxlabs.scheduler.optimiser.fitness.components.ISimilarityComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.SimilarityComponentParameters;
import com.mmxlabs.scheduler.optimiser.lso.ConstrainedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.SequencesConstrainedMoveGeneratorUnit;

/**
 * A {@link Module} providing the data from {@link SettingsOverride} to the {@link Injector} framework.
 * 
 * @author Simon Goodall
 * 
 */
public class OptimisationSettingsOverrideModule extends AbstractModule {

	private final SettingsOverride settings;

	public OptimisationSettingsOverrideModule(final SettingsOverride settings) {
		this.settings = settings;
	}

	@Override
	protected void configure() {

	}

	@Provides
	@Named(SequencesConstrainedMoveGeneratorUnit.OPTIMISER_ENABLE_FOUR_OPT_2)
	private boolean enableFourOpt2() {
		return true;
	}
//	@Provides
//	@Named(LNGParameters_ActionPlanSettingsModule.ACTION_PLAN_TOTAL_EVALUATIONS)
//	private int actionPlanTotalEvals() {
//		return settings.getActionPlanTotalEvals();
//	}
//
//	@Provides
//	@Named(LNGParameters_ActionPlanSettingsModule.ACTION_PLAN_IN_RUN_EVALUATIONS)
//	private int actionPlanInRunEvals() {
//		return settings.getActionPlanInRunEvals();
//	}
//
//	@Provides
//	@Named(LNGParameters_ActionPlanSettingsModule.ACTION_PLAN_MAX_SEARCH_DEPTH)
//	private int actionPlanInRunSearchDepth() {
//		return settings.getActionPlanMaxSearchDepth();
//	}

//	@Provides
//	@Singleton
//	private IExcessIdleTimeComponentParameters provideIdleComponentParameters() {
//		final ExcessIdleTimeComponentParameters idleParams = new ExcessIdleTimeComponentParameters();
//		int highPeriodInDays = 15;
//		int lowPeriodInDays = Math.max(0, highPeriodInDays - 2);
//		idleParams.setThreshold(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.LOW, lowPeriodInDays*24);
//		idleParams.setThreshold(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.HIGH, highPeriodInDays*24);
//		idleParams.setWeight(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.LOW, 2_500);
//		idleParams.setWeight(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.HIGH, 10_000);
//		idleParams.setEndWeight(10_000);
//
//		return idleParams;
//	}
	
}
