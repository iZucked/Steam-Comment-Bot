/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.marketability;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.models.lng.analytics.MarketabilityModel;
import com.mmxlabs.models.lng.analytics.ui.views.marketability.MarketabilitySandboxEvaluator;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.ui.analytics.marketability.MarketabilitySandboxRunner;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.peaberry.OptimiserInjectorServiceMaker;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService.ModuleType;

public class AbstractMarketablilityTest extends AbstractMicroTestCase {

	protected void evaluateMarketabilityModel(@NonNull MarketabilityModel marketModel) {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setWithSpotCargoMarkets(true);
		userSettings.setSimilarityMode(SimilarityMode.OFF);
		MarketabilitySandboxRunner.run(scenarioDataProvider, null, userSettings, marketModel, new NullProgressMonitor(), false, //
				OptimiserInjectorServiceMaker.begin() //
						.withModuleOverrideBindNamedInstance(ModuleType.Module_LNGTransformerModule, SchedulerConstants.Key_UseCanalSlotBasedWindowTrimming, boolean.class, Boolean.TRUE).make());

	}

}
