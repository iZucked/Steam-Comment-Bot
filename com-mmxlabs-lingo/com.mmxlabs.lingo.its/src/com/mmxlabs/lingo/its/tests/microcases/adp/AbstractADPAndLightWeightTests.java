/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.adp;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

import com.google.common.collect.Lists;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.models.lng.adp.ext.ISlotTemplateFactory;
import com.mmxlabs.models.lng.adp.ext.impl.AbstractSlotTemplateFactory;
import com.mmxlabs.models.lng.parameters.OptimisationMode;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.scheduler.optimiser.fitness.components.NonOptionalSlotFitnessCoreFactory;

@ExtendWith(value = ShiroRunner.class)
public abstract class AbstractADPAndLightWeightTests extends AbstractMicroTestCase {

	private static List<String> requiredFeatures = Lists.newArrayList("adp", "optimisation-clean-state", "optimisation-similarity", "optimisation-hillclimb");
	private static List<String> addedFeatures = new LinkedList<>();
	private static ServiceRegistration<?> registerService;

	@BeforeAll
	public static void hookIn() {
		for (final String feature : requiredFeatures) {
			if (!LicenseFeatures.isPermitted("features:" + feature)) {
				LicenseFeatures.addFeatureEnablements(feature);
				addedFeatures.add(feature);
			}
		}
		registerService = FrameworkUtil.getBundle(AbstractADPAndLightWeightTests.class).getBundleContext().registerService(ISlotTemplateFactory.class.getCanonicalName(),
				new AbstractSlotTemplateFactory(), null);
	}

	@AfterAll
	public static void hookOut() {
		for (final String feature : addedFeatures) {
			LicenseFeatures.removeFeatureEnablements(feature);
		}
		addedFeatures.clear();
		registerService.unregister();
	}

	 
	protected @NonNull UserSettings createUserSettings() {
		return createUserSettings(false);
	}

	protected @NonNull UserSettings createUserSettings(boolean nominalADP) {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);
		userSettings.setMode(OptimisationMode.ADP);
		userSettings.setCleanSlateOptimisation(true);
		userSettings.setNominalOnly(nominalADP);

		userSettings.setShippingOnly(false);
		userSettings.setWithSpotCargoMarkets(true);
		userSettings.setSimilarityMode(SimilarityMode.OFF);
		return userSettings;
	}

	protected OptimisationPlan createOptimisationPlan(final UserSettings userSettings) {
		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, lngScenarioModel);

		ScenarioUtils.setLSOStageIterations(optimisationPlan, 1_000_000);
		ScenarioUtils.setHillClimbStageIterations(optimisationPlan, 50_000);
		ScenarioUtils.createOrUpdateAllObjectives(optimisationPlan, NonOptionalSlotFitnessCoreFactory.NAME, true, 24_000_000);

		// and now delete lso and hill
		OptimisationEMFTestUtils.removeLSOAndHill(optimisationPlan);
		return optimisationPlan;
	}
}