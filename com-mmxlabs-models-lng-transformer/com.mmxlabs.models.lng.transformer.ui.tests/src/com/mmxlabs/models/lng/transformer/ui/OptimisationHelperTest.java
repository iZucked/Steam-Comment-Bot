/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.time.YearMonth;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
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

		Assert.assertNotNull(optimisationPlan);
		Assert.assertNotNull(optimisationPlan.getUserSettings());
		Assert.assertSame(userSettings, optimisationPlan.getUserSettings());
		Assert.assertFalse(optimisationPlan.getStages().isEmpty());
		// hard to test this now...
		// Assert.assertEquals(ScenarioUtils.createLowSimilaritySettings(), settings.getSimilaritySettings());
	}

	@Test
	public void testTransformUserSettings_Similarity_High() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setSimilarityMode(SimilarityMode.HIGH);

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(optimisationPlan);
		Assert.assertNotNull(optimisationPlan.getUserSettings());
		Assert.assertSame(userSettings, optimisationPlan.getUserSettings());
		Assert.assertFalse(optimisationPlan.getStages().isEmpty()); // hard to test this now...
		// Assert.assertEquals(ScenarioUtils.createLowSimilaritySettings(), settings.getSimilaritySettings());
	}

	@Test
	public void testTransformUserSettings_Similarity_Low() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setSimilarityMode(SimilarityMode.LOW);

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(optimisationPlan);
		Assert.assertNotNull(optimisationPlan.getUserSettings());
		Assert.assertSame(userSettings, optimisationPlan.getUserSettings());
		Assert.assertFalse(optimisationPlan.getStages().isEmpty());
		// hard to test this now...
		// Assert.assertEquals(ScenarioUtils.createLowSimilaritySettings(), settings.getSimilaritySettings());
	}

	@Test
	public void testTransformUserSettings_Similarity_Off() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final OptimisationPlan plan = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(plan);
		for (OptimisationStage stage : plan.getStages()) {
			if (stage instanceof ConstraintsAndFitnessSettingsStage) {
				ConstraintsAndFitnessSettingsStage cfStage = (ConstraintsAndFitnessSettingsStage) stage;
				ConstraintAndFitnessSettings constraintAndFitnessSettings = cfStage.getConstraintAndFitnessSettings();
				Assert.assertNotNull(constraintAndFitnessSettings);
				Assert.assertNotNull(constraintAndFitnessSettings.getSimilaritySettings());
				Assert.assertEquals(ScenarioUtils.createOffSimilaritySettings(), constraintAndFitnessSettings.getSimilaritySettings());

			}
		}
	}

	@Test
	public void testTransformUserSettings_ActionSet_disabled_NoPeriod_SimOff() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(true);

		final OptimisationPlan plan = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(plan);
		Assert.assertFalse(userSettings.isBuildActionSets());
		for (OptimisationStage stage : plan.getStages()) {
			Assert.assertFalse(stage instanceof ActionPlanOptimisationStage);
		}
	}

	@Test
	public void testTransformUserSettings_ActionSet_disabled_3m_SimOff() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(true);
		userSettings.setPeriodStart(YearMonth.of(2015, 2));
		userSettings.setPeriodEnd(YearMonth.of(2015, 4));

		final OptimisationPlan plan = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(plan);
		Assert.assertFalse(plan.getUserSettings().isBuildActionSets());
	}

	@Test
	public void testTransformUserSettings_ActionSet_disabled_4m_SimLow() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(true);
		userSettings.setSimilarityMode(SimilarityMode.LOW);
		userSettings.setPeriodStart(YearMonth.of(2015, 2));
		userSettings.setPeriodEnd(YearMonth.of(2015, 6));

		final OptimisationPlan plan = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(plan);
		Assert.assertFalse(plan.getUserSettings().isBuildActionSets());
	}

	@Test
	public void testTransformUserSettings_ActionSet_enabled_6m_SimMedium() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(true);
		userSettings.setSimilarityMode(SimilarityMode.MEDIUM);
		userSettings.setPeriodStart(YearMonth.of(2015, 2));
		userSettings.setPeriodEnd(YearMonth.of(2015, 8));

		final OptimisationPlan plan = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(plan);
		Assert.assertTrue(plan.getUserSettings().isBuildActionSets());
	}

	@Test
	public void testTransformUserSettings_ActionSet_disabled_7m_SimMedium() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(true);
		userSettings.setSimilarityMode(SimilarityMode.MEDIUM);
		userSettings.setPeriodStart(YearMonth.of(2015, 2));
		userSettings.setPeriodEnd(YearMonth.of(2015, 9));

		final OptimisationPlan plan = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(plan);
		Assert.assertFalse(plan.getUserSettings().isBuildActionSets());
	}

	@Test
	public void testTransformUserSettings_ActionSet_enabled_6m_SimHigh() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(true);
		userSettings.setSimilarityMode(SimilarityMode.HIGH);
		userSettings.setPeriodStart(YearMonth.of(2015, 2));
		userSettings.setPeriodEnd(YearMonth.of(2015, 8));

		final OptimisationPlan plan = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(plan);
		Assert.assertTrue(plan.getUserSettings().isBuildActionSets());
	}

	@Test
	public void testTransformUserSettings_ActionSet_disabled_7m_SimHigh() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(true);
		userSettings.setSimilarityMode(SimilarityMode.HIGH);
		userSettings.setPeriodStart(YearMonth.of(2015, 2));
		userSettings.setPeriodEnd(YearMonth.of(2015, 9));

		final OptimisationPlan plan = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(plan);
		Assert.assertFalse(plan.getUserSettings().isBuildActionSets());
	}

	@Test
	public void testTransformUserSettings_SpotCargoMarkets_On() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setWithSpotCargoMarkets(false);

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(optimisationPlan);
		Assert.assertNotNull(optimisationPlan.getUserSettings());
		Assert.assertSame(userSettings, optimisationPlan.getUserSettings());
		Assert.assertFalse(optimisationPlan.getStages().isEmpty()); // hard to test this now...
		// Assert.assertEquals(ScenarioUtils.createLowSimilaritySettings(), settings.getSimilaritySettings());
	}

}
