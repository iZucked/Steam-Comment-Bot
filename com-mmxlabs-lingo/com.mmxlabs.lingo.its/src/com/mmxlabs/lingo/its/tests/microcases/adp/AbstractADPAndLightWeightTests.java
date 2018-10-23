/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 * 
 * Sets up tests for ADP using the training case data
 * 
 */
package com.mmxlabs.lingo.its.tests.microcases.adp;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.category.OptimisationTest;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lingo.its.verifier.OptimiserDataMapper;
import com.mmxlabs.lingo.its.verifier.OptimiserResultVerifier;
import com.mmxlabs.lingo.its.verifier.SolutionData;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.scenario.CSVImporter;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.impl.ParallelOptimisationPlanExtender;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.fitness.components.NonOptionalSlotFitnessCoreFactory;

@RunWith(value = ShiroRunner.class)
public abstract class AbstractADPAndLightWeightTests extends AbstractMicroTestCase {

	private static List<String> requiredFeatures = Lists.newArrayList(
			"adp",
			"optimisation-clean-state",
			"optimisation-similarity",
			"optimisation-hillclimb");
	private static List<String> addedFeatures = new LinkedList<>();

	@BeforeClass
	public static void hookIn() {
		for (final String feature : requiredFeatures) {
			if (!LicenseFeatures.isPermitted("features:" + feature)) {
				LicenseFeatures.addFeatureEnablements(feature);
				addedFeatures.add(feature);
			}
		}
	}

	@AfterClass
	public static void hookOut() {
		for (final String feature : addedFeatures) {
			LicenseFeatures.removeFeatureEnablements(feature);
		}
		addedFeatures.clear();
	}

	// Which scenario data to import
	@Override
	public @NonNull IScenarioDataProvider importReferenceData() throws MalformedURLException {
		final IScenarioDataProvider scenarioDataProvider = importReferenceData("/trainingcases/Shipping_I/");

		return scenarioDataProvider;
	}

	// Override default behaviour to also import portfolio data e.g. cargoes, vessel availabilities, events
	@NonNull
	public static IScenarioDataProvider importReferenceData(final String url) throws MalformedURLException {

		final @NonNull String urlRoot = AbstractMicroTestCase.class.getResource(url).toString();
		final CSVImporter importer = new CSVImporter();
		importer.importPortData(urlRoot);
		importer.importCostData(urlRoot);
		importer.importEntityData(urlRoot);
		importer.importFleetData(urlRoot);
		importer.importMarketData(urlRoot);
		importer.importPromptData(urlRoot);
		importer.importMarketData(urlRoot);

		// Import cargoes from CSV
		importer.importPorfolioData(urlRoot);

		return importer.doImport();
	}

	@Override
	protected BaseLegalEntity importDefaultEntity() {
		return commercialModelFinder.findEntity("Entity");
	}

	protected @NonNull UserSettings createUserSettings() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setAdpOptimisation(true);
		userSettings.setCleanStateOptimisation(true);

		userSettings.setShippingOnly(false);
		userSettings.setWithSpotCargoMarkets(true);
		userSettings.setSimilarityMode(SimilarityMode.OFF);
		return userSettings;
	}

	protected OptimisationPlan createOptimisationPlan(final UserSettings userSettings) {
		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		ScenarioUtils.setLSOStageIterations(optimisationPlan, 1_000_000);
		ScenarioUtils.setHillClimbStageIterations(optimisationPlan, 50_000);
		ScenarioUtils.setActionPlanStageParameters(optimisationPlan, 5_000_000, 1_500_000, 5_000);
		ScenarioUtils.createOrUpdateAllObjectives(optimisationPlan, NonOptionalSlotFitnessCoreFactory.NAME, true, 24_000_000);

		new ParallelOptimisationPlanExtender().extend(optimisationPlan);

		// and now delete lso and hill
		OptimisationEMFTestUtils.removeLSOAndHill(optimisationPlan);
		return optimisationPlan;
	}
}