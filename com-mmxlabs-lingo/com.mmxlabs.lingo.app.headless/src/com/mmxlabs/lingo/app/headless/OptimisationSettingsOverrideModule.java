/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
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
import com.mmxlabs.models.lng.transformer.inject.modules.ActionPlanModule;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintChecker;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.fitness.components.ISimilarityComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.LatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.SimilarityComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.EnumeratingSequenceScheduler;
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

	@Provides
	@Singleton
	private ISimilarityComponentParameters provideSimilarityComponentParameters() {
		Map<String, Integer> scpm = settings.getSimilarityParameterMap();
		final SimilarityComponentParameters scp = new SimilarityComponentParameters();

		scp.setThreshold(SimilarityComponentParameters.Interval.LOW, scpm.get("scp-set-low-thresh"));
		scp.setWeight(SimilarityComponentParameters.Interval.LOW, scpm.get("scp-set-low-weight"));

		scp.setThreshold(SimilarityComponentParameters.Interval.MEDIUM, scpm.get("scp-set-med-thresh"));
		scp.setWeight(SimilarityComponentParameters.Interval.MEDIUM, scpm.get("scp-set-med-weight"));

		scp.setThreshold(SimilarityComponentParameters.Interval.HIGH, scpm.get("scp-set-high-thresh"));
		scp.setWeight(SimilarityComponentParameters.Interval.HIGH, scpm.get("scp-set-high-weight"));

		scp.setOutOfBoundsWeight(scpm.get("scp-set-outOfBounds-weight"));

		return scp;
	}

	@Provides
	@Named(ActionPlanModule.ACTION_PLAN_TOTAL_EVALUATIONS)
	private int actionPlanTotalEvals() {
		return settings.getActionPlanTotalEvals();
	}

	@Provides
	@Named(ActionPlanModule.ACTION_PLAN_IN_RUN_EVALUATIONS)
	private int actionPlanInRunEvals() {
		return settings.getActionPlanInRunEvals();
	}

	@Provides
	@Named(ActionPlanModule.ACTION_PLAN_MAX_SEARCH_DEPTH)
	private int actionPlanInRunSearchDepth() {
		return settings.getActionPlanMaxSearchDepth();
	}

}
