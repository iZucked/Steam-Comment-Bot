/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.mmxlabs.lngdataserver.lng.importers.creator.ScenarioBuilder;
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
import com.mmxlabs.models.lng.port.util.DistanceModelBuilder;
import com.mmxlabs.models.lng.port.util.PortModelBuilder;
import com.mmxlabs.models.lng.port.util.PortModelFinder;
import com.mmxlabs.models.lng.pricing.util.CostModelBuilder;
import com.mmxlabs.models.lng.pricing.util.PricingModelBuilder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelBuilder;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelFinder;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

@SuppressWarnings("null")
public abstract class AbstractMicroTestCase {

	public LNGScenarioModel lngScenarioModel;
	public ScenarioModelFinder scenarioModelFinder;
	public ScenarioModelBuilder scenarioModelBuilder;
	public CommercialModelFinder commercialModelFinder;
	public FleetModelFinder fleetModelFinder;
	public PortModelBuilder portModelBuilder;
	public PortModelFinder portFinder;
	public CargoModelFinder cargoModelFinder;
	public CargoModelBuilder cargoModelBuilder;
	public CostModelBuilder costModelBuilder;
	public CommercialModelBuilder commercialModelBuilder;
	public FleetModelBuilder fleetModelBuilder;
	public SpotMarketsModelBuilder spotMarketsModelBuilder;
	public SpotMarketsModelFinder spotMarketsModelFinder;
	public PricingModelBuilder pricingModelBuilder;

	public DistanceModelBuilder distanceModelBuilder;

	public BaseLegalEntity entity;
	public IScenarioDataProvider scenarioDataProvider;

	public @NonNull IScenarioDataProvider importReferenceData() throws Exception {
		final ScenarioBuilder sb = ScenarioBuilder.initialiseBasicScenario();
		sb.loadDefaultData();
		return sb.getScenarioDataProvider();
	}

	protected BaseLegalEntity importDefaultEntity() {
		return commercialModelFinder.findEntity(ScenarioBuilder.DEFAULT_ENTITY_NAME);
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

	public void evaluateWithLSOTest(final @NonNull Consumer<LNGScenarioRunner> checker, final IOptimiserInjectorService overrides) {
		evaluateWithLSOTest(false, null, null, checker, overrides);
	}

	public void optimiseWithLSOTest(final @NonNull Consumer<LNGScenarioRunner> checker) {
		evaluateWithLSOTest(true, null, null, checker, null);
	}

	public void optimiseWithLSOTest(@Nullable final Consumer<OptimisationPlan> tweaker, final @NonNull Consumer<LNGScenarioRunner> checker) {
		evaluateWithLSOTest(true, tweaker, null, checker, null);
	}

	public void optimiseWithLSOTest(final @NonNull Consumer<LNGScenarioRunner> checker, final IOptimiserInjectorService overrides) {
		evaluateWithLSOTest(true, null, null, checker, overrides);
	}

	public void evaluateWithLSOTest(final boolean optimise, @Nullable final Consumer<OptimisationPlan> tweaker, @Nullable final Function<LNGScenarioRunner, IRunnerHook> runnerHookFactory,
			@NonNull final Consumer<LNGScenarioRunner> checker, final IOptimiserInjectorService overrides) {
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

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlanCustomiser(planCustomiser) //
				.withRunnerHookFactory(runnerHookFactory) //
				.withUserSettings(userSettings) //
				.withOptimiserInjectorService(overrides) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();
		runnerBuilder.run(optimise, checker);
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

		evaluateTestWith(userSettings, s);
	}

	public void evaluateTestWith(UserSettings userSettings, IOptimiserInjectorService s) {

		LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withUserSettings(userSettings) //
				.withOptimiserInjectorService(s) //
				.withThreadCount(getThreadCount()) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();
	}

	public void evaluateTestWith(UserSettings userSettings) {

		LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withUserSettings(userSettings) //
				.withThreadCount(getThreadCount()) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();
	}

	public void evaluateTest(@Nullable final Consumer<OptimisationPlan> tweaker, @Nullable final Function<LNGScenarioRunner, IRunnerHook> runnerHookFactory,
			@NonNull final Consumer<LNGScenarioRunner> checker) {

		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlanCustomiser(tweaker) //
				.withRunnerHookFactory(runnerHookFactory) //
				.withUserSettings(userSettings) //
				.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(getThreadCount()) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();
		runnerBuilder.run(false, checker);
	}

	public void evaluateWithOverrides(final IOptimiserInjectorService localOverrides, @Nullable final Consumer<OptimisationPlan> tweaker, @NonNull final Consumer<LNGScenarioRunner> checker) {

		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlanCustomiser(tweaker) //
				.withUserSettings(userSettings) //
				.withOptimiserInjectorService(localOverrides) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();
		runnerBuilder.run(false, checker);
	}

	protected int getThreadCount() {
		return 1;
	}
}