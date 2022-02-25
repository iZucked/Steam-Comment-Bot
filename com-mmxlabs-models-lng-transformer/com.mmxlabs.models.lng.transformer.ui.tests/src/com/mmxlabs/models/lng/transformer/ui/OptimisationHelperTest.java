/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.ConstraintsAndFitnessSettingsStage;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.OptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;

public class OptimisationHelperTest {

	@Test
	public void testTransformUserSettings_Similarity_Medium() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setSimilarityMode(SimilarityMode.MEDIUM);

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assertions.assertNotNull(optimisationPlan);
		Assertions.assertNotNull(optimisationPlan.getUserSettings());
		Assertions.assertSame(userSettings, optimisationPlan.getUserSettings());
		Assertions.assertFalse(optimisationPlan.getStages().isEmpty());
		// hard to test this now...
		// Assertions.assertEquals(ScenarioUtils.createLowSimilaritySettings(), settings.getSimilaritySettings());
	}

	@Test
	public void testTransformUserSettings_Similarity_High() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setSimilarityMode(SimilarityMode.HIGH);

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assertions.assertNotNull(optimisationPlan);
		Assertions.assertNotNull(optimisationPlan.getUserSettings());
		Assertions.assertSame(userSettings, optimisationPlan.getUserSettings());
		Assertions.assertFalse(optimisationPlan.getStages().isEmpty()); // hard to test this now...
		// Assertions.assertEquals(ScenarioUtils.createLowSimilaritySettings(), settings.getSimilaritySettings());
	}

	@Test
	public void testTransformUserSettings_Similarity_Low() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setSimilarityMode(SimilarityMode.LOW);

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assertions.assertNotNull(optimisationPlan);
		Assertions.assertNotNull(optimisationPlan.getUserSettings());
		Assertions.assertSame(userSettings, optimisationPlan.getUserSettings());
		Assertions.assertFalse(optimisationPlan.getStages().isEmpty());
		// hard to test this now...
		// Assertions.assertEquals(ScenarioUtils.createLowSimilaritySettings(), settings.getSimilaritySettings());
	}

	@Test
	public void testTransformUserSettings_Similarity_Off() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final OptimisationPlan plan = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assertions.assertNotNull(plan);
		for (OptimisationStage stage : plan.getStages()) {
			if (stage instanceof ConstraintsAndFitnessSettingsStage) {
				ConstraintsAndFitnessSettingsStage cfStage = (ConstraintsAndFitnessSettingsStage) stage;
				ConstraintAndFitnessSettings constraintAndFitnessSettings = cfStage.getConstraintAndFitnessSettings();
				Assertions.assertNotNull(constraintAndFitnessSettings);
				Assertions.assertNotNull(constraintAndFitnessSettings.getSimilaritySettings());
				Assertions.assertEquals(ScenarioUtils.createOffSimilaritySettings(), constraintAndFitnessSettings.getSimilaritySettings());

			}
		}
	}

	@Test
	public void testTransformUserSettings_SpotCargoMarkets_On() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setWithSpotCargoMarkets(false);

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assertions.assertNotNull(optimisationPlan);
		Assertions.assertNotNull(optimisationPlan.getUserSettings());
		Assertions.assertSame(userSettings, optimisationPlan.getUserSettings());
		Assertions.assertFalse(optimisationPlan.getStages().isEmpty()); // hard to test this now...
		// Assertions.assertEquals(ScenarioUtils.createLowSimilaritySettings(), settings.getSimilaritySettings());
	}

}
