/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.After;
import org.junit.Before;

import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.util.CommercialModelFinder;
import com.mmxlabs.models.lng.fleet.util.FleetModelBuilder;
import com.mmxlabs.models.lng.fleet.util.FleetModelFinder;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.util.PortModelFinder;
import com.mmxlabs.models.lng.pricing.util.PricingModelBuilder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelBuilder;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.its.scenario.CSVImporter;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;

public abstract class AbstractMicroTestCase {

	protected LNGScenarioModel lngScenarioModel;
	protected ScenarioModelFinder scenarioModelFinder;
	protected ScenarioModelBuilder scenarioModelBuilder;
	protected CommercialModelFinder commercialModelFinder;
	protected FleetModelFinder fleetModelFinder;
	protected PortModelFinder portFinder;
	protected CargoModelBuilder cargoModelBuilder;
	protected FleetModelBuilder fleetModelBuilder;
	protected SpotMarketsModelBuilder spotMarketsModelBuilder;
	protected PricingModelBuilder pricingModelBuilder;
	protected BaseLegalEntity entity;

	@NonNull
	public LNGScenarioModel importReferenceData() throws MalformedURLException {
		return importReferenceData("/referencedata/reference-data-1/");
	}

	@NonNull
	public static LNGScenarioModel importReferenceData(final String url) throws MalformedURLException {

		final @NonNull String urlRoot = AbstractMicroTestCase.class.getResource(url).toString();
		final CSVImporter importer = new CSVImporter();
		importer.importPortData(urlRoot);
		importer.importCostData(urlRoot);
		importer.importEntityData(urlRoot);
		importer.importFleetData(urlRoot);
		importer.importMarketData(urlRoot);
		importer.importPromptData(urlRoot);
		importer.importMarketData(urlRoot);

		return importer.doImport();
	}

	@Before
	public void constructor() throws MalformedURLException {

		lngScenarioModel = importReferenceData();

		scenarioModelFinder = new ScenarioModelFinder(lngScenarioModel);
		scenarioModelBuilder = new ScenarioModelBuilder(lngScenarioModel);

		commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
		fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
		portFinder = scenarioModelFinder.getPortModelFinder();

		pricingModelBuilder = scenarioModelBuilder.getPricingModelBuilder();
		cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();
		spotMarketsModelBuilder = scenarioModelBuilder.getSpotMarketsModelBuilder();

		entity = commercialModelFinder.findEntity("Shipping");

	}

	@After
	public void destructor() {
		lngScenarioModel = null;
		scenarioModelFinder = null;
		scenarioModelBuilder = null;
		commercialModelFinder = null;
		fleetModelFinder = null;
		portFinder = null;
		cargoModelBuilder = null;
		fleetModelBuilder = null;
		spotMarketsModelBuilder = null;
		pricingModelBuilder = null;
		entity = null;
	}

	public void evaluateWithLSOTest(final @NonNull Consumer<LNGScenarioRunner> checker) {
		evaluateWithLSOTest(false, null, null, checker);
	}

	public void optimiseWithLSOTest(final @NonNull Consumer<LNGScenarioRunner> checker) {
		evaluateWithLSOTest(true, null, null, checker);
	}

	public void evaluateWithLSOTest(final boolean optimise, @Nullable final Consumer<OptimiserSettings> tweaker, @Nullable final Function<LNGScenarioRunner, IRunnerHook> runnerHookFactory,
			@NonNull final Consumer<LNGScenarioRunner> checker) {

		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final OptimiserSettings optimiserSettings = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);
		if (tweaker != null) {
			tweaker.accept(optimiserSettings);
		} else {
			optimiserSettings.getAnnealingSettings().setIterations(10_000);
		}

		// Generate internal data
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimiserSettings, new TransformerExtensionTestBootstrapModule(), null, false,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);

			if (runnerHookFactory != null) {
				final IRunnerHook runnerHook = runnerHookFactory.apply(scenarioRunner);
				if (runnerHook != null) {
					scenarioRunner.setRunnerHook(runnerHook);
				}
			}

			scenarioRunner.evaluateInitialState();

			if (optimise) {
				scenarioRunner.run();
			}

			checker.accept(scenarioRunner);
		} finally {
			executorService.shutdownNow();
		}
	}

	public void evaluateTest(@Nullable final Consumer<OptimiserSettings> tweaker, @Nullable final Function<LNGScenarioRunner, IRunnerHook> runnerHookFactory,
			@NonNull final Consumer<LNGScenarioRunner> checker) {

		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final OptimiserSettings optimiserSettings = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);
		if (tweaker != null) {
			tweaker.accept(optimiserSettings);
		}

		// Generate internal data
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimiserSettings, new TransformerExtensionTestBootstrapModule(), null, true);

			if (runnerHookFactory != null) {
				final IRunnerHook runnerHook = runnerHookFactory.apply(scenarioRunner);
				if (runnerHook != null) {
					scenarioRunner.setRunnerHook(runnerHook);
				}
			}

			scenarioRunner.evaluateInitialState();

			checker.accept(scenarioRunner);
		} finally {
			executorService.shutdownNow();
		}
	}
}