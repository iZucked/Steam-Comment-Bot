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

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.After;
import org.junit.Before;

import com.google.inject.Module;
import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.util.CommercialModelBuilder;
import com.mmxlabs.models.lng.commercial.util.CommercialModelFinder;
import com.mmxlabs.models.lng.fleet.util.FleetModelBuilder;
import com.mmxlabs.models.lng.fleet.util.FleetModelFinder;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.util.PortModelFinder;
import com.mmxlabs.models.lng.pricing.util.PricingModelBuilder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelFinder;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelBuilder;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.its.scenario.CSVImporter;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public abstract class AbstractMicroTestCase {

	protected LNGScenarioModel lngScenarioModel;
	protected ScenarioModelFinder scenarioModelFinder;
	protected ScenarioModelBuilder scenarioModelBuilder;
	protected CommercialModelFinder commercialModelFinder;
	protected FleetModelFinder fleetModelFinder;
	protected PortModelFinder portFinder;
	protected CargoModelBuilder cargoModelBuilder;
	protected CommercialModelBuilder commercialModelBuilder;
	protected FleetModelBuilder fleetModelBuilder;
	protected SpotMarketsModelBuilder spotMarketsModelBuilder;
	protected SpotMarketsModelFinder spotMarketsModelFinder;
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
		spotMarketsModelFinder = scenarioModelFinder.getSpotMarketsModelFinder();
		
		pricingModelBuilder = scenarioModelBuilder.getPricingModelBuilder();
		commercialModelBuilder = scenarioModelBuilder.getCommercialModelBuilder();
		cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();
		spotMarketsModelBuilder = scenarioModelBuilder.getSpotMarketsModelBuilder();

		entity = importDefaultEntity();

	}

	protected BaseLegalEntity importDefaultEntity() {
		return commercialModelFinder.findEntity("Shipping");
	}

	@After
	public void destructor() {
		lngScenarioModel = null;
		scenarioModelFinder = null;
		scenarioModelBuilder = null;
		commercialModelFinder = null;
		fleetModelFinder = null;
		portFinder = null;
		commercialModelBuilder = null;
		cargoModelBuilder = null;
		fleetModelBuilder = null;
		spotMarketsModelBuilder = null;
		pricingModelBuilder = null;
		entity = null;
	}

	public void evaluateWithLSOTest(final @NonNull Consumer<LNGScenarioRunner> checker) {
		evaluateWithLSOTest(false, null, null, checker, null);
	}

	public void evaluateWithLSOTest(final @NonNull Consumer<LNGScenarioRunner> checker, IOptimiserInjectorService overrides) {
		evaluateWithLSOTest(false, null, null, checker, overrides);
	}

	public void optimiseWithLSOTest(final @NonNull Consumer<LNGScenarioRunner> checker) {
		evaluateWithLSOTest(true, null, null, checker, null);
	}

	public void optimiseWithLSOTest(final @NonNull Consumer<LNGScenarioRunner> checker, IOptimiserInjectorService overrides) {
		evaluateWithLSOTest(true, null, null, checker, overrides);
	}

	public void evaluateWithLSOTest(final boolean optimise, @Nullable final Consumer<OptimisationPlan> tweaker, @Nullable final Function<LNGScenarioRunner, IRunnerHook> runnerHookFactory,
			@NonNull final Consumer<LNGScenarioRunner> checker, IOptimiserInjectorService overrides) {

		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);
		optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationPlan);
		if (tweaker != null) {
			tweaker.accept(optimisationPlan);
		} else {
			ScenarioUtils.setLSOStageIterations(optimisationPlan, 10_000);
		}

		// Generate internal data
		final ExecutorService executorService = createExecutorService();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, null, optimisationPlan, LNGSchedulerJobUtils.createLocalEditingDomain(),
					new TransformerExtensionTestBootstrapModule(), overrides, null, false, LNGTransformerHelper.HINT_OPTIMISE_LSO);
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

	protected @NonNull ExecutorService createExecutorService() {
		return Executors.newSingleThreadExecutor();
	}

	public void evaluateTest(@Nullable final Consumer<OptimisationPlan> tweaker, @Nullable final Function<LNGScenarioRunner, IRunnerHook> runnerHookFactory,
			@NonNull final Consumer<LNGScenarioRunner> checker) {

		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);
		if (tweaker != null) {
			tweaker.accept(optimisationPlan);
		}

		// Generate internal data
		final ExecutorService executorService = createExecutorService();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimisationPlan, new TransformerExtensionTestBootstrapModule(), null, true);

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

	public void evaluateWithOverrides(IOptimiserInjectorService localOverrides, @Nullable final Consumer<OptimisationPlan> tweaker, @NonNull final Consumer<LNGScenarioRunner> checker) {

		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final OptimisationPlan optimiserPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);
		if (tweaker != null) {
			tweaker.accept(optimiserPlan);
		}

		// Generate internal data
		final ExecutorService executorService = createExecutorService();
		try {
			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, null, optimiserPlan, LNGSchedulerJobUtils.createLocalEditingDomain(), null,
					localOverrides, null, true);

			// final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimiserSettings, null, localOverrides, true);
			//
			// if (runnerHookFactory != null) {
			// final IRunnerHook runnerHook = runnerHookFactory.apply(scenarioRunner);
			// if (runnerHook != null) {
			// scenarioRunner.setRunnerHook(runnerHook);
			// }
			// }

			scenarioRunner.evaluateInitialState();
			checker.accept(scenarioRunner);
		} finally {
			executorService.shutdownNow();
		}
	}

}