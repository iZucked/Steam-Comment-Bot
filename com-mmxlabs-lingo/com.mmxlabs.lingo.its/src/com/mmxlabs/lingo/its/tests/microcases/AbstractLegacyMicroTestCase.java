/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.io.IOException;
import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.lngdataserver.data.distances.DataLoader;
import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;
import com.mmxlabs.lngdataserver.integration.ports.model.PortsVersion;
import com.mmxlabs.lngdataserver.lng.io.distances.DistancesToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.port.PortsToScenarioCopier;
import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.cargo.util.CargoModelFinder;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.util.CommercialModelBuilder;
import com.mmxlabs.models.lng.commercial.util.CommercialModelFinder;
import com.mmxlabs.models.lng.fleet.util.FleetModelBuilder;
import com.mmxlabs.models.lng.fleet.util.FleetModelFinder;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.DistanceModelBuilder;
import com.mmxlabs.models.lng.port.util.PortModelBuilder;
import com.mmxlabs.models.lng.port.util.PortModelFinder;
import com.mmxlabs.models.lng.pricing.util.CostModelBuilder;
import com.mmxlabs.models.lng.pricing.util.PricingModelBuilder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelBuilder;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelFinder;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.its.scenario.CSVImporter;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

@SuppressWarnings("null")
public abstract class AbstractLegacyMicroTestCase {

	protected LNGScenarioModel lngScenarioModel;
	protected ScenarioModelFinder scenarioModelFinder;
	protected ScenarioModelBuilder scenarioModelBuilder;
	protected CommercialModelFinder commercialModelFinder;
	protected FleetModelFinder fleetModelFinder;
	protected PortModelBuilder portModelBuilder;
	protected PortModelFinder portFinder;
	protected CargoModelFinder cargoModelFinder;
	protected CargoModelBuilder cargoModelBuilder;
	protected CostModelBuilder costModelBuilder;
	protected CommercialModelBuilder commercialModelBuilder;
	protected FleetModelBuilder fleetModelBuilder;
	protected SpotMarketsModelBuilder spotMarketsModelBuilder;
	protected SpotMarketsModelFinder spotMarketsModelFinder;
	protected PricingModelBuilder pricingModelBuilder;

	protected DistanceModelBuilder distanceModelBuilder;

	protected BaseLegalEntity entity;
	protected IScenarioDataProvider scenarioDataProvider;

	@NonNull
	public IScenarioDataProvider importReferenceData() throws Exception {
		return importReferenceData("/referencedata/reference-data-2/");
	}

	public static void updateDistanceData(IScenarioDataProvider scenarioDataProvider, String key) throws Exception {
		final String input = DataLoader.importData(key);

		final ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.registerModule(new JavaTimeModule());

		final DistancesVersion distanceVersion = mapper.readerFor(DistancesVersion.class).readValue(input);

		final EditingDomain editingDomain = scenarioDataProvider.getEditingDomain();
		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
		final Command updateCommand = DistancesToScenarioCopier.getUpdateCommand(editingDomain, portModel, distanceVersion, true);

		Assertions.assertTrue(updateCommand.canExecute());

		RunnerHelper.syncExecDisplayOptional(() -> editingDomain.getCommandStack().execute(updateCommand));

		// Ensure updated.
		Assertions.assertEquals(distanceVersion.getIdentifier(), portModel.getDistanceVersionRecord().getVersion());
	}

	public static void updatePortsData(IScenarioDataProvider scenarioDataProvider, String key) throws Exception {
		final String input = DataLoader.importData(key);

		final ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.registerModule(new JavaTimeModule());

		final PortsVersion version = mapper.readerFor(PortsVersion.class).readValue(input);

		final EditingDomain editingDomain = scenarioDataProvider.getEditingDomain();
		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
		final Command updateCommand = PortsToScenarioCopier.getUpdateCommand(editingDomain, portModel, version);

		Assertions.assertTrue(updateCommand.canExecute());

		RunnerHelper.syncExecDisplayOptional(() -> editingDomain.getCommandStack().execute(updateCommand));

		// Ensure updated.
		Assertions.assertEquals(version.getIdentifier(), portModel.getPortVersionRecord().getVersion());
	}

	@NonNull
	public static IScenarioDataProvider importReferenceData(final String url) throws Exception {

		final @NonNull String urlRoot = AbstractLegacyMicroTestCase.class.getResource(url).toString();
		final CSVImporter importer = new CSVImporter();
		importer.importPortData(urlRoot);
		importer.importCostData(urlRoot);
		importer.importEntityData(urlRoot);
		importer.importFleetData(urlRoot);
		importer.importMarketData(urlRoot);
		importer.importPromptData(urlRoot);

		return importer.doImport();
	}

	@BeforeEach
	public void constructor() throws Exception {

		scenarioDataProvider = importReferenceData();
		lngScenarioModel = (LNGScenarioModel) scenarioDataProvider.getScenario();

		scenarioModelFinder = new ScenarioModelFinder(scenarioDataProvider);
		scenarioModelBuilder = new ScenarioModelBuilder(scenarioDataProvider);

		commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
		cargoModelFinder = scenarioModelFinder.getCargoModelFinder();
		fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
		portModelBuilder = scenarioModelBuilder.getPortModelBuilder();
		distanceModelBuilder = scenarioModelBuilder.getDistanceModelBuilder();
		portFinder = scenarioModelFinder.getPortModelFinder();
		spotMarketsModelFinder = scenarioModelFinder.getSpotMarketsModelFinder();

		costModelBuilder = scenarioModelBuilder.getCostModelBuilder();
		pricingModelBuilder = scenarioModelBuilder.getPricingModelBuilder();
		commercialModelBuilder = scenarioModelBuilder.getCommercialModelBuilder();
		cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();
		spotMarketsModelBuilder = scenarioModelBuilder.getSpotMarketsModelBuilder();

		entity = importDefaultEntity();

		setPromptDates();
	}

	/**
	 * Set a default prompt date
	 */
	protected void setPromptDates() {
		if (lngScenarioModel.getPromptPeriodStart() == null) {
			lngScenarioModel.setPromptPeriodStart(LocalDate.of(2016, 1, 1));
		}
	}

	protected BaseLegalEntity importDefaultEntity() {
		return commercialModelFinder.findEntity("Shipping");
	}

	@AfterEach
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
		cargoModelFinder = null;

		if (scenarioDataProvider != null) {
			scenarioDataProvider.close();
		}
	}

	public void evaluateWithLSOTest(final @NonNull Consumer<LNGScenarioRunner> checker) {
		evaluateWithLSOTest(false, null, null, checker, null);
	}

	public void evaluateWithLSOTest(@Nullable final Consumer<OptimisationPlan> tweaker, final @NonNull Consumer<LNGScenarioRunner> checker) {
		evaluateWithLSOTest(false, tweaker, null, checker, null);
	}

	public void evaluateWithLSOTest(final @NonNull Consumer<LNGScenarioRunner> checker, IOptimiserInjectorService overrides) {
		evaluateWithLSOTest(false, null, null, checker, overrides);
	}

	public void optimiseWithLSOTest(final @NonNull Consumer<LNGScenarioRunner> checker) {
		evaluateWithLSOTest(true, null, null, checker, null);
	}

	public void optimiseWithLSOTest(@Nullable final Consumer<OptimisationPlan> tweaker, final @NonNull Consumer<LNGScenarioRunner> checker) {
		evaluateWithLSOTest(true, tweaker, null, checker, null);
	}

	public void optimiseWithLSOTest(final @NonNull Consumer<LNGScenarioRunner> checker, IOptimiserInjectorService overrides) {
		evaluateWithLSOTest(true, null, null, checker, overrides);
	}

	public void evaluateWithLSOTest(final boolean optimise, @Nullable final Consumer<OptimisationPlan> tweaker, @Nullable final Function<LNGScenarioRunner, IRunnerHook> runnerHookFactory,
			@NonNull final Consumer<LNGScenarioRunner> checker, IOptimiserInjectorService overrides) {
		final Consumer<OptimisationPlan> planCustomiser;
		if (tweaker != null) {
			planCustomiser = tweaker;
		} else {
			planCustomiser = plan -> ScenarioUtils.setLSOStageIterations(plan, 10_000);
		}

		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setWithSpotCargoMarkets(true);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlanCustomiser(planCustomiser) //
				.withRunnerHookFactory(runnerHookFactory) //
				.withUserSettings(userSettings) //
				.withOptimiserInjectorService(overrides) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		try {
			runnerBuilder.evaluateInitialState();
			runnerBuilder.run(optimise, checker);
		} finally {
			runnerBuilder.dispose();
		}
	}

	public void evaluateTest() {
		evaluateTest(null, null, runner -> {
		});
	}

	public void evaluateTestWith(IOptimiserInjectorService s) {
		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withUserSettings(userSettings) //
				.withOptimiserInjectorService(s) //
				.withThreadCount(getThreadCount()) //
				.buildDefaultRunner();

		try {
			runnerBuilder.evaluateInitialState();
		} finally {
			runnerBuilder.dispose();
		}
	}

	public void evaluateTest(@Nullable final Consumer<OptimisationPlan> tweaker, @Nullable final Function<LNGScenarioRunner, IRunnerHook> runnerHookFactory,
			@NonNull final Consumer<LNGScenarioRunner> checker) {

		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlanCustomiser(tweaker) //
				.withRunnerHookFactory(runnerHookFactory) //
				.withUserSettings(userSettings) //
				.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(getThreadCount()) //
				.buildDefaultRunner();

		try {
			runnerBuilder.evaluateInitialState();
			runnerBuilder.run(false, checker);
		} finally {
			runnerBuilder.dispose();
		}
	}

	public void evaluateWithOverrides(IOptimiserInjectorService localOverrides, @Nullable final Consumer<OptimisationPlan> tweaker, @NonNull final Consumer<LNGScenarioRunner> checker) {

		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlanCustomiser(tweaker) //
				.withUserSettings(userSettings) //
				.withOptimiserInjectorService(localOverrides) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		try {
			runnerBuilder.evaluateInitialState();
			runnerBuilder.run(false, checker);
		} finally {
			runnerBuilder.dispose();
		}
	}

	protected int getThreadCount() {
		return 1;
	}
	
	protected void saveToLiNGOFile(LNGScenarioRunner scenarioRunner, String filenameStem) throws IOException {
		//Save to base case.
		scenarioRunner.getScenarioDataProvider().getManifest().setClientScenarioVersion(2);
		scenarioRunner.getScenarioDataProvider().getManifest().setClientVersionContext("com.mmxlabs.lingo.vanilla");
		MicroCaseUtils.storeToFile(scenarioRunner.getScenarioDataProvider(), filenameStem);
	}
}