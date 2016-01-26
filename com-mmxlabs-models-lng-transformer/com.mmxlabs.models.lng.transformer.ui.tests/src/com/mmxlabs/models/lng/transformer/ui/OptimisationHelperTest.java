/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.time.YearMonth;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;

public class OptimisationHelperTest {

	@Test
	public void testTransformUserSettings_Similarity_Medium() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setSimilarityMode(SimilarityMode.MEDIUM);

		final OptimiserSettings settings = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(settings);
		Assert.assertNotNull(settings.getSimilaritySettings());
		// hard to test this now...
		// Assert.assertEquals(ScenarioUtils.createLowSimilaritySettings(), settings.getSimilaritySettings());
	}

	@Test
	public void testTransformUserSettings_Similarity_High() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setSimilarityMode(SimilarityMode.HIGH);

		final OptimiserSettings settings = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(settings);
		Assert.assertNotNull(settings.getSimilaritySettings());
		// hard to test this now...
		// Assert.assertEquals(ScenarioUtils.createLowSimilaritySettings(), settings.getSimilaritySettings());
	}

	@Test
	public void testTransformUserSettings_Similarity_Low() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setSimilarityMode(SimilarityMode.LOW);

		final OptimiserSettings settings = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(settings);
		Assert.assertNotNull(settings.getSimilaritySettings());
		// hard to test this now...
		// Assert.assertEquals(ScenarioUtils.createLowSimilaritySettings(), settings.getSimilaritySettings());
	}

	@Test
	public void testTransformUserSettings_Similarity_Off() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final OptimiserSettings settings = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(settings);
		Assert.assertNotNull(settings.getSimilaritySettings());
		Assert.assertEquals(SimilarityUIParameters.createOffSimilaritySettings(), settings.getSimilaritySettings());
	}

	@Test
	public void testTransformUserSettings_ShippingOnly_Off() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setShippingOnly(false);

		final OptimiserSettings settings = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(settings);
		Assert.assertFalse(settings.isShippingOnly());
	}

	@Test
	public void testTransformUserSettings_ShippingOnly_On() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setShippingOnly(true);

		final OptimiserSettings settings = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(settings);
		Assert.assertTrue(settings.isShippingOnly());
	}

	@Test
	public void testTransformUserSettings_GenerateCharterOuts_Off() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);

		final OptimiserSettings settings = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(settings);
		Assert.assertFalse(settings.isGenerateCharterOuts());
	}

	@Test
	public void testTransformUserSettings_GenerateCharterOuts_On() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(true);

		final OptimiserSettings settings = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(settings);
		Assert.assertTrue(settings.isGenerateCharterOuts());
	}

	@Test
	public void testTransformUserSettings_PeriodStart() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setPeriodStart(YearMonth.of(2015, 4));

		final OptimiserSettings settings = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(settings);
		Assert.assertNotNull(settings.getRange());
		Assert.assertEquals(YearMonth.of(2015, 4), settings.getRange().getOptimiseAfter());
		Assert.assertFalse(settings.getRange().isSetOptimiseBefore());
	}

	@Test
	public void testTransformUserSettings_PeriodEnd() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setPeriodEnd(YearMonth.of(2015, 4));

		final OptimiserSettings settings = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(settings);
		Assert.assertNotNull(settings.getRange());
		Assert.assertFalse(settings.getRange().isSetOptimiseAfter());
		Assert.assertEquals(YearMonth.of(2015, 4), settings.getRange().getOptimiseBefore());
	}

	@Test
	public void testTransformUserSettings_PeriodStartEnd() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setPeriodStart(YearMonth.of(2015, 2));
		userSettings.setPeriodEnd(YearMonth.of(2015, 4));

		final OptimiserSettings settings = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(settings);
		Assert.assertNotNull(settings.getRange());
		Assert.assertEquals(YearMonth.of(2015, 2), settings.getRange().getOptimiseAfter());
		Assert.assertEquals(YearMonth.of(2015, 4), settings.getRange().getOptimiseBefore());
	}

	@Test
	public void testTransformUserSettings_ActionSet_disabled_NoPeriod_SimOff() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(true);

		final OptimiserSettings settings = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(settings);
		Assert.assertFalse(settings.isBuildActionSets());
	}

	@Test
	public void testTransformUserSettings_ActionSet_disabled_3m_SimOff() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(true);
		userSettings.setPeriodStart(YearMonth.of(2015, 2));
		userSettings.setPeriodEnd(YearMonth.of(2015, 4));

		final OptimiserSettings settings = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(settings);
		Assert.assertFalse(settings.isBuildActionSets());
	}

	@Test
	public void testTransformUserSettings_ActionSet_disabled_4m_SimLow() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(true);
		userSettings.setSimilarityMode(SimilarityMode.LOW);
		userSettings.setPeriodStart(YearMonth.of(2015, 2));
		userSettings.setPeriodEnd(YearMonth.of(2015, 6));

		final OptimiserSettings settings = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(settings);
		Assert.assertFalse(settings.isBuildActionSets());
		Assert.assertEquals(YearMonth.of(2015, 2), settings.getRange().getOptimiseAfter());
		Assert.assertEquals(YearMonth.of(2015, 6), settings.getRange().getOptimiseBefore());
	}

	@Test
	public void testTransformUserSettings_ActionSet_enabled_6m_SimMedium() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(true);
		userSettings.setSimilarityMode(SimilarityMode.MEDIUM);
		userSettings.setPeriodStart(YearMonth.of(2015, 2));
		userSettings.setPeriodEnd(YearMonth.of(2015, 8));
		
		final OptimiserSettings settings = OptimisationHelper.transformUserSettings(userSettings, null, null);
		
		Assert.assertNotNull(settings);
		Assert.assertTrue(settings.isBuildActionSets());
		Assert.assertEquals(YearMonth.of(2015, 2), settings.getRange().getOptimiseAfter());
		Assert.assertEquals(YearMonth.of(2015, 8), settings.getRange().getOptimiseBefore());
	}
	
	@Test
	public void testTransformUserSettings_ActionSet_disabled_7m_SimMedium() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(true);
		userSettings.setSimilarityMode(SimilarityMode.MEDIUM);
		userSettings.setPeriodStart(YearMonth.of(2015, 2));
		userSettings.setPeriodEnd(YearMonth.of(2015, 9));

		final OptimiserSettings settings = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(settings);
		Assert.assertFalse(settings.isBuildActionSets());
		Assert.assertEquals(YearMonth.of(2015, 2), settings.getRange().getOptimiseAfter());
		Assert.assertEquals(YearMonth.of(2015, 9), settings.getRange().getOptimiseBefore());
	}

	@Test
	public void testTransformUserSettings_ActionSet_enabled_6m_SimHigh() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(true);
		userSettings.setSimilarityMode(SimilarityMode.HIGH);
		userSettings.setPeriodStart(YearMonth.of(2015, 2));
		userSettings.setPeriodEnd(YearMonth.of(2015, 8));
		
		final OptimiserSettings settings = OptimisationHelper.transformUserSettings(userSettings, null, null);
		
		Assert.assertNotNull(settings);
		Assert.assertTrue(settings.isBuildActionSets());
		Assert.assertEquals(YearMonth.of(2015, 2), settings.getRange().getOptimiseAfter());
		Assert.assertEquals(YearMonth.of(2015, 8), settings.getRange().getOptimiseBefore());
	}
	
	@Test
	public void testTransformUserSettings_ActionSet_disabled_7m_SimHigh() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(true);
		userSettings.setSimilarityMode(SimilarityMode.HIGH);
		userSettings.setPeriodStart(YearMonth.of(2015, 2));
		userSettings.setPeriodEnd(YearMonth.of(2015, 9));

		final OptimiserSettings settings = OptimisationHelper.transformUserSettings(userSettings, null, null);

		Assert.assertNotNull(settings);
		Assert.assertFalse(settings.isBuildActionSets());
		Assert.assertEquals(YearMonth.of(2015, 2), settings.getRange().getOptimiseAfter());
		Assert.assertEquals(YearMonth.of(2015, 9), settings.getRange().getOptimiseBefore());
	}

}
