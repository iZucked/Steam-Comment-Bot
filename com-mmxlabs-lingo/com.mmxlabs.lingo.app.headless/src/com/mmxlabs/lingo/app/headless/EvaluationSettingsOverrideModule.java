/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.fitness.components.LatenessComponentParameters;

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
		Map<String, Integer> latenessParameterMap = settings.getlatenessParameterMap();
		final LatenessComponentParameters lcp = new LatenessComponentParameters();

		lcp.setThreshold(Interval.PROMPT, latenessParameterMap.get("lcp-set-prompt-period"));
		lcp.setLowWeight(Interval.PROMPT, latenessParameterMap.get("lcp-set-prompt-lowWeight"));
		lcp.setHighWeight(Interval.PROMPT, latenessParameterMap.get("lcp-set-prompt-highWeight"));

		lcp.setThreshold(Interval.MID_TERM, latenessParameterMap.get("lcp-set-midTerm-period"));
		lcp.setLowWeight(Interval.MID_TERM, latenessParameterMap.get("lcp-set-midTerm-lowWeight"));
		lcp.setHighWeight(Interval.MID_TERM, latenessParameterMap.get("lcp-set-midTerm-highWeight"));

		lcp.setThreshold(Interval.BEYOND, latenessParameterMap.get("lcp-set-beyond-period"));
		lcp.setLowWeight(Interval.BEYOND, latenessParameterMap.get("lcp-set-beyond-lowWeight"));
		lcp.setHighWeight(Interval.BEYOND, latenessParameterMap.get("lcp-set-beyond-highWeight"));

		return lcp;
	}

}
