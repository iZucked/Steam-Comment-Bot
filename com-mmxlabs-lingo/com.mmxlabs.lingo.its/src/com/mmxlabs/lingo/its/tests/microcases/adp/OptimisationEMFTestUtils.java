/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.adp;

import java.util.Iterator;

import com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.OptimisationMode;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.OptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;

public class OptimisationEMFTestUtils {
	
	public static OptimisationPlan setUpBasicADPSettingsWithNoLSOOrHill(LNGScenarioModel lngScenarioModel) {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);

		userSettings.setWithSpotCargoMarkets(true);
		userSettings.setMode(OptimisationMode.ADP);
		userSettings.setCleanSlateOptimisation(true);

		OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);
		optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationPlan);
		OptimisationEMFTestUtils.removeLSOAndHill(optimisationPlan);

		return optimisationPlan;
	}

	public static void removeLSOAndHill(OptimisationPlan optimisationPlan) {
		Iterator<OptimisationStage> iterator = optimisationPlan.getStages().iterator();
		while (iterator.hasNext()) {
			OptimisationStage stage = iterator.next();
			if (stage instanceof LocalSearchOptimisationStage
					|| stage instanceof HillClimbOptimisationStage) {
				iterator.remove();
			}
		}
	}

}
