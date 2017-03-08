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
import com.mmxlabs.scheduler.optimiser.fitness.components.ExcessIdleTimeComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ISimilarityComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.fitness.components.LatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.SimilarityComponentParameters;

/**
 * A {@link Module} providing the data from {@link SettingsOverride} to the {@link Injector} framework.
 * 
 * @author Simon Goodall
 * 
 */
public class EvaluationSettingsOverrideModule extends AbstractModule {

	private final SettingsOverride settings;

	public EvaluationSettingsOverrideModule(final SettingsOverride settings) {
		this.settings = settings;
	}

	@Override
	protected void configure() {

	}

	@Provides
	@Named(LNGParameters_EvaluationSettingsModule.OPTIMISER_REEVALUATE)
	private boolean isOptimiserReevaluating() {
		return false;
	}

	@Provides
	@Singleton
	private ILatenessComponentParameters provideLatenessComponentParameters() {
		Map<String, Integer> latenessParameterMap = settings.getlatenessMap();
		final LatenessComponentParameters lcp = new LatenessComponentParameters();

		lcp.setThreshold(Interval.PROMPT, latenessParameterMap.get("prompt-period"));
		lcp.setLowWeight(Interval.PROMPT, latenessParameterMap.get("prompt-low"));
		lcp.setHighWeight(Interval.PROMPT, latenessParameterMap.get("prompt-high"));

		lcp.setThreshold(Interval.MID_TERM, latenessParameterMap.get("mid-term-period"));
		lcp.setLowWeight(Interval.MID_TERM, latenessParameterMap.get("mid-term-low"));
		lcp.setHighWeight(Interval.MID_TERM, latenessParameterMap.get("mid-term-high"));

		lcp.setThreshold(Interval.BEYOND, latenessParameterMap.get("beyond-period"));
		lcp.setLowWeight(Interval.BEYOND, latenessParameterMap.get("beyond-low"));
		lcp.setHighWeight(Interval.BEYOND, latenessParameterMap.get("beyond-high"));


		return lcp;
	}

	@Provides
	@Singleton
	private ISimilarityComponentParameters provideSimilarityComponentParameters() {
		Map<String, Integer> scpm = settings.getSimilarityMap();
		final SimilarityComponentParameters scp = new SimilarityComponentParameters();

		scp.setThreshold(SimilarityComponentParameters.Interval.LOW, scpm.get("low-thresh"));
		scp.setWeight(SimilarityComponentParameters.Interval.LOW, scpm.get("low-weight"));

		scp.setThreshold(SimilarityComponentParameters.Interval.MEDIUM, scpm.get("med-thresh"));
		scp.setWeight(SimilarityComponentParameters.Interval.MEDIUM, scpm.get("med-weight"));

		scp.setThreshold(SimilarityComponentParameters.Interval.HIGH, scpm.get("high-thresh"));
		scp.setWeight(SimilarityComponentParameters.Interval.HIGH, scpm.get("high-weight"));

		scp.setOutOfBoundsWeight(scpm.get("out-of-bounds-weight"));

		return scp;
	}
	
	@Provides
	@Singleton
	private IExcessIdleTimeComponentParameters provideIdleComponentParameters() {
		final ExcessIdleTimeComponentParameters idleParams = new ExcessIdleTimeComponentParameters();
		idleParams.setThreshold(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.LOW, 13 * 24);
		idleParams.setThreshold(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.HIGH, 15 * 24);
		idleParams.setWeight(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.LOW, settings.getIdleTimeLow());
		idleParams.setWeight(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.HIGH, settings.getIdleTimeHigh());
		idleParams.setEndWeight(settings.getIdleTimeEnd());
		
		return idleParams;
	}

}
