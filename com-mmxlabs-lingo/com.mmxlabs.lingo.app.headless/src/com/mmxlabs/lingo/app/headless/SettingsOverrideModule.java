/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.transformer.inject.modules.OptimiserSettingsModule;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintChecker;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.LatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.EnumeratingSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.lso.SequencesConstrainedMoveGeneratorUnit;

/**
 * A {@link Module} providing the data from {@link SettingsOverride} to the {@link Injector} framework.
 * 
 * @author Simon Goodall
 * 
 */
public class SettingsOverrideModule extends AbstractModule {

	private final SettingsOverride settings;

	public SettingsOverrideModule(final SettingsOverride settings) {
		this.settings = settings;
	}

	@Override
	protected void configure() {

	}

	@Provides
	@Named(EnumeratingSequenceScheduler.OPTIMISER_REEVALUATE)
	private boolean isOptimiserReevaluating() {
		return false;
	}

	
	 @Provides
	 @Named(SequencesConstrainedMoveGeneratorUnit.OPTIMISER_ENABLE_FOUR_OPT_2)
	 private boolean enableFourOpt2() {
	 return false;
	 }
	
	 @Provides
	 @Named(TravelTimeConstraintChecker.OPTIMISER_START_ELEMENT_FIX)
	 private boolean enableStartOfSequenceFix() {
	 return false;
	 }

//	@Provides
//	@Singleton
//	private ILatenessComponentParameters provideLatenessComponentParamters() {
//		Map<String, Integer> latenessParameterMap = settings.getlatenessParameterMap();
//		final LatenessComponentParameters lcp = new LatenessComponentParameters();
//
//		lcp.setPromptLowThreshold(latenessParameterMap.get("lcp-set-prompt-low-threshold"));
//		lcp.setPromptLowThresholdWeight("lcp-set-prompt-low-threshold-weight");
//		lcp.setPromptHighThresholdWeight("lcp-set-prompt-high-threshold-weight");
//
//		lcp.setForwardLowThreshold("lcp-set-forward-low-threshold");
//		lcp.setForwardMidThreshold("lcp-set-forward-mid-threshold");
//		lcp.setForwardLowThresholdWeight("lcp-set-forward-low-threshold-weight");
//		lcp.setForwardMidThresholdWeight("lcp-set-forward-mid-threshold-weight");
//		lcp.setForwardHighThresholdWeight("lcp-set-forward-high-threshold-weight");
//
//		return lcp;
//	}
	
	@Provides
	@Singleton
	private ILatenessComponentParameters provideLatenessComponentParamters() {
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
