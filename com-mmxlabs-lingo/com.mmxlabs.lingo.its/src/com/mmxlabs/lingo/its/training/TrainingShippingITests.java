/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.training;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.lingo.its.tests.TestMode;
import com.mmxlabs.lingo.its.tests.TestingModes;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lingo.its.verifier.OptimiserDataMapper;
import com.mmxlabs.lingo.its.verifier.OptimiserResultVerifier;
import com.mmxlabs.lingo.its.verifier.SolutionData;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.lngdataserver.lng.importers.creator.ScenarioBuilder;
import com.mmxlabs.models.lng.parameters.OptimisationMode;
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
import com.mmxlabs.models.lng.transformer.its.RequireFeature;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.fitness.components.NonOptionalSlotFitnessCoreFactory;

@ExtendWith(value = ShiroRunner.class)
@RequireFeature(value = { KnownFeatures.FEATURE_OPTIMISATION_SIMILARITY, KnownFeatures.FEATURE_OPTIMISATION_HILLCLIMB })
public class TrainingShippingITests extends AbstractMicroTestCase {

	private static final String LARGE_SHIP = InternalDataConstants.REF_VESSEL_TFDE_160;
	private static final String MEDIUM_SHIP = InternalDataConstants.REF_VESSEL_STEAM_145;
	private static final String SMALL_SHIP = InternalDataConstants.REF_VESSEL_STEAM_138;

	// Which scenario data to import
	@Override
	public @NonNull IScenarioDataProvider importReferenceData() throws Exception {

		ScenarioBuilder sb = ScenarioBuilder.initialiseBasicScenario();
		sb.loadDefaultData();

		try (InputStream is = TrainingShippingITests.class.getResourceAsStream("/trainingcases/Shipping_I_mmx_maintained/Commodity Curves.csv")) {
			sb.importCommodityCurves(is);
		}
		try (InputStream is = TrainingShippingITests.class.getResourceAsStream("/trainingcases/Shipping_I_mmx_maintained/Vessel Charters.csv")) {
			sb.importVesselCharters(is);
		}
		// try (InputStream is = TrainingShippingITests.class.getResourceAsStream("/trainingcases/Shipping_I_mmx_maintained/Spot Cargo Markets.csv")) {
		// sb.importCargoes(is);
		// }
		try (InputStream is = TrainingShippingITests.class.getResourceAsStream("/trainingcases/Shipping_I_mmx_maintained/Cargoes.csv")) {
			sb.importCargoes(is);
		}
		try (InputStream is = TrainingShippingITests.class.getResourceAsStream("/trainingcases/Shipping_I_mmx_maintained/Assignments.csv")) {
			sb.importAssignments(is);
		}
		return sb.getScenarioDataProvider();
	}

	protected @NonNull UserSettings createUserSettings() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);
		userSettings.setMode(OptimisationMode.SHORT_TERM);
		userSettings.setCleanSlateOptimisation(false);

		userSettings.setShippingOnly(true);
		userSettings.setWithSpotCargoMarkets(true);
		userSettings.setSimilarityMode(SimilarityMode.ALL);
		return userSettings;
	}

	protected OptimisationPlan createOptimisationPlan(final UserSettings userSettings) {
		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, lngScenarioModel);

		ScenarioUtils.setLSOStageIterations(optimisationPlan, 1_000_000);
		ScenarioUtils.setHillClimbStageIterations(optimisationPlan, 50_000);
		ScenarioUtils.createOrUpdateAllObjectives(optimisationPlan, NonOptionalSlotFitnessCoreFactory.NAME, true, 24_000_000);

		return optimisationPlan;
	}

	@Disabled("Tests are broken")
	@Test
	@Tag(TestCategories.OPTIMISATION_TEST)
	public void testShipping_I_Stage_1_Shipping() throws Exception {
		Assumptions.assumeTrue(TestingModes.OptimisationTestMode == TestMode.Run);

		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		final UserSettings userSettings = createUserSettings();

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(createOptimisationPlan(userSettings)) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final Schedule initialSchedule = ScenarioModelUtil.getScheduleModel(lngScenarioModel).getSchedule();
		Assertions.assertNotNull(initialSchedule);

		final long initialPNL = ScheduleModelKPIUtils.getScheduleProfitAndLoss(initialSchedule);
		runnerBuilder.run(false, runner -> {

			// Run, get result and store to schedule model for inspection at EMF level if
			// needed
			final IMultiStateResult result = runner.runWithProgress(new NullProgressMonitor());
			final LNGScenarioToOptimiserBridge bridge = runnerBuilder.getScenarioRunner().getScenarioToOptimiserBridge();
			final OptimiserDataMapper mapper = new OptimiserDataMapper(bridge);
			final List<SolutionData> solutionDataList = OptimiserResultVerifier.createSolutionData(true, result, mapper);
			// Solution 1
			{
				final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(mapper) //
						.withAnySolutionResultChecker()
						.withUsedLoad("A_3")
						.onFleetVessel(SMALL_SHIP) //
						.withUsedLoad("S_1")
						.onFleetVessel(MEDIUM_SHIP) //
						.withUsedLoad("S_4")
						.onFleetVessel(LARGE_SHIP) //
						.pnlDelta(initialPNL, 944_899, 1_000) //
						.build();

				final ISequences solution = verifier.verifySolutionExistsInResults(solutionDataList, Assertions::fail);
				Assertions.assertNotNull(solution);
			}
			// Solution 2
			{
				final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(mapper) //
						.withAnySolutionResultChecker()
						.withUsedLoad("A_3")
						.onFleetVessel(SMALL_SHIP) //
						.withUsedLoad("S_4")
						.onFleetVessel(MEDIUM_SHIP) //
						.pnlDelta(initialPNL, 610_378, 1_000) //
						.build();

				final ISequences solution = verifier.verifySolutionExistsInResults(solutionDataList, Assertions::fail);
				Assertions.assertNotNull(solution);
			}
		});

	}

	@Disabled("Tests are broken")
	@Test
	@Tag(TestCategories.OPTIMISATION_TEST)
	public void testShipping_I_Stage_2_1_Lateness() throws Exception {
		Assumptions.assumeTrue(TestingModes.OptimisationTestMode == TestMode.Run);

		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		// Make changes to initial data
		cargoModelFinder.findLoadSlot("PE_1").setWindowSizeUnits(TimePeriod.DAYS);
		cargoModelFinder.findVesselCharter(LARGE_SHIP).setStartAt(portFinder.findPortById(InternalDataConstants.PORT_GLADSTONE));
		cargoModelFinder.findVesselCharter(MEDIUM_SHIP).setStartAt(portFinder.findPortById(InternalDataConstants.PORT_BARCELONA));
		cargoModelFinder.findVesselCharter(SMALL_SHIP).setStartAt(portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS));

		final UserSettings userSettings = createUserSettings();

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(createOptimisationPlan(userSettings)) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final Schedule initialSchedule = ScenarioModelUtil.getScheduleModel(lngScenarioModel).getSchedule();
		Assertions.assertNotNull(initialSchedule);

		final long initialPNL = ScheduleModelKPIUtils.getScheduleProfitAndLoss(initialSchedule);
		final long initialLateness = ScheduleModelKPIUtils.getScheduleLateness(initialSchedule)[ScheduleModelKPIUtils.LATENESS_WITHOUT_FLEX_IDX];
		final long initialViolations = ScheduleModelKPIUtils.getScheduleViolationCount(initialSchedule);

		runnerBuilder.run(false, runner -> {
			// Run, get result and store to schedule model for inspection at EMF level if
			// needed
			final IMultiStateResult result = runner.runWithProgress(new NullProgressMonitor());
			final LNGScenarioToOptimiserBridge bridge = runnerBuilder.getScenarioRunner().getScenarioToOptimiserBridge();
			final OptimiserDataMapper mapper = new OptimiserDataMapper(bridge);
			final List<SolutionData> solutionDataList = OptimiserResultVerifier.createSolutionData(true, result, mapper);
			// Solution 1
			{
				final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(mapper) //
						.withAnySolutionResultChecker()
						.withUsedLoad("A_3")
						.onFleetVessel(SMALL_SHIP) //
						.withUsedLoad("BO_1")
						.onFleetVessel(SMALL_SHIP) //
						.withUsedLoad("S_1")
						.onFleetVessel(MEDIUM_SHIP) //
						.withUsedLoad("S_4")
						.onFleetVessel(LARGE_SHIP) //
						.violationDelta(initialViolations, -1) //
						.latenessDelta(initialLateness, -((4 * 24) + 5)) //
						.pnlDelta(initialPNL, -992_994, 1_000) //
						.build();

				final ISequences solution = verifier.verifySolutionExistsInResults(solutionDataList, msg -> Assertions.fail(msg));
				Assertions.assertNotNull(solution);
			}
			// Solution 2
			{
				final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(mapper) //
						.withAnySolutionResultChecker()
						.withUsedLoad("A_3")
						.onFleetVessel(SMALL_SHIP) //
						.withUsedLoad("BO_1")
						.onFleetVessel(SMALL_SHIP) //
						.withUsedLoad("S_4")
						.onFleetVessel(MEDIUM_SHIP) //
						.violationDelta(initialViolations, -1) //
						.latenessDelta(initialLateness, -((4 * 24) + 5)) //
						.pnlDelta(initialPNL, -1_327_515, 1_000) //
						.build();

				final ISequences solution = verifier.verifySolutionExistsInResults(solutionDataList, msg -> Assertions.fail(msg));
				Assertions.assertNotNull(solution);
			}
			// Solution 3
			{
				final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner) //
						.withAnySolutionResultChecker()
						.withUsedLoad("BO_1")
						.onFleetVessel(SMALL_SHIP) //
						.violationDelta(initialViolations, -1) //
						.latenessDelta(initialLateness, -((4 * 24) + 5)) //
						.pnlDelta(initialPNL, -2_144_366, 1_000) //
						.build();

				final ISequences solution = verifier.verifySolutionExistsInResults(solutionDataList, msg -> Assertions.fail(msg));
				Assertions.assertNotNull(solution);

			}
		});
	}

	@Disabled("Tests are broken")
	@Test
	@Tag(TestCategories.OPTIMISATION_TEST)
	public void testShipping_I_Stage_2_2_Lateness_with_charter_in() throws Exception {
		Assumptions.assumeTrue(TestingModes.OptimisationTestMode == TestMode.Run);

		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		// Make changes to initial data
		cargoModelFinder.findLoadSlot("PE_1").setWindowSizeUnits(TimePeriod.DAYS);
		cargoModelFinder.findVesselCharter(LARGE_SHIP).setStartAt(portFinder.findPortById(InternalDataConstants.PORT_GLADSTONE));
		cargoModelFinder.findVesselCharter(MEDIUM_SHIP).setStartAt(portFinder.findPortById(InternalDataConstants.PORT_BARCELONA));
		cargoModelFinder.findVesselCharter(SMALL_SHIP).setStartAt(portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS));

		spotMarketsModelBuilder.getSpotMarketsModel().getCharterInMarkets().clear();
		final CharterInMarket charterMarket = spotMarketsModelBuilder.createCharterInMarket("CI_10", fleetModelFinder.findVessel(SMALL_SHIP), entity, "21750", 1);
		charterMarket.setEnabled(true);
		charterMarket.setNominal(true);

		final UserSettings userSettings = createUserSettings();

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(createOptimisationPlan(userSettings)) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final Schedule initialSchedule = ScenarioModelUtil.getScheduleModel(lngScenarioModel).getSchedule();
		Assertions.assertNotNull(initialSchedule);

		final long initialPNL = ScheduleModelKPIUtils.getScheduleProfitAndLoss(initialSchedule);
		final long initialLateness = ScheduleModelKPIUtils.getScheduleLateness(initialSchedule)[ScheduleModelKPIUtils.LATENESS_WITHOUT_FLEX_IDX];
		final long initialViolations = ScheduleModelKPIUtils.getScheduleViolationCount(initialSchedule);

		runnerBuilder.run(false, runner -> {

			// Run, get result and store to schedule model for inspection at EMF level if
			// needed
			final IMultiStateResult result = runner.runWithProgress(new NullProgressMonitor());
			final LNGScenarioToOptimiserBridge bridge = runnerBuilder.getScenarioRunner().getScenarioToOptimiserBridge();
			final OptimiserDataMapper mapper = new OptimiserDataMapper(bridge);
			final List<SolutionData> solutionDataList = OptimiserResultVerifier.createSolutionData(true, result, mapper);
			// Solution 1
			{
				final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(mapper) //
						.withAnySolutionResultChecker()
						.withUsedLoad("A_3")
						.onFleetVessel(SMALL_SHIP) //
						.withUsedLoad("BO_2")
						.onSpotCharter("CI_10") //
						.withUsedLoad("S_1")
						.onFleetVessel(MEDIUM_SHIP) //
						.withUsedLoad("S_4")
						.onFleetVessel(LARGE_SHIP) //
						.violationDelta(initialViolations, 0) //
						.latenessDelta(initialLateness, -((4 * 24) + 5)) //
						.pnlDelta(initialPNL, -147_982, 1_000) //
						.build();

				final ISequences solution = verifier.verifySolutionExistsInResults(solutionDataList, Assertions::fail);
				Assertions.assertNotNull(solution);
			}
			// Solution 2
			{
				final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(mapper) //
						.withAnySolutionResultChecker()
						.withUsedLoad("A_3")
						.onFleetVessel(SMALL_SHIP) //
						.withUsedLoad("BO_2")
						.onSpotCharter("CI_10") //
						.withUsedLoad("S_4")
						.onFleetVessel(MEDIUM_SHIP) //
						.violationDelta(initialViolations, 0) //
						.latenessDelta(initialLateness, -((4 * 24) + 5)) //
						.pnlDelta(initialPNL, -482_503, 1_000) //
						.build();

				final ISequences solution = verifier.verifySolutionExistsInResults(solutionDataList, Assertions::fail);
				Assertions.assertNotNull(solution);

			}
			// Solution 3
			{
				final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(mapper) //
						.withAnySolutionResultChecker()
						.withUsedLoad("BO_2")
						.onSpotCharter("CI_10") //
						.violationDelta(initialViolations, 0) //
						.latenessDelta(initialLateness, -((4 * 24) + 5)) //
						.pnlDelta(initialPNL, -1_351_441, 1_000) //
						.build();

				final ISequences solution = verifier.verifySolutionExistsInResults(result, Assertions::fail);
				Assertions.assertNotNull(solution);
			}

		});

	}

	@Disabled("Tests are broken")
	@Test
	@Tag(TestCategories.OPTIMISATION_TEST)
	public void testShipping_I_Stage_3_1_allocation_and_keep_open() throws Exception {
		Assumptions.assumeTrue(TestingModes.OptimisationTestMode == TestMode.Run);

		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		// Make changes to initial data
		final Port p = portFinder.findPortById(InternalDataConstants.PORT_CORPUS_CHRISTI);
		p.getCapabilities().add(PortCapability.LOAD);
		p.setLoadDuration(24);
		p.setCvValue(22.7);
		costModelBuilder.createPortCost(Collections.singleton(p), Collections.singleton(PortCapability.LOAD), 140000);

		cargoModelFinder.findVesselCharter(LARGE_SHIP).setStartAt(portFinder.findPortById(InternalDataConstants.PORT_GLADSTONE));
		cargoModelFinder.findVesselCharter(MEDIUM_SHIP).setStartAt(portFinder.findPortById(InternalDataConstants.PORT_BARCELONA));
		cargoModelFinder.findVesselCharter(SMALL_SHIP).setStartAt(portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS));

		cargoModelFinder.findLoadSlot("S_3").setArriveCold(false);

		cargoModelBuilder.makeFOBPurchase("New_Load", LocalDate.of(2017, 1, 1), p, null, entity, "10.5%BRENT_ICE", 22.7) //
				.withWindowStartTime(0) //
				.withWindowSize(1, TimePeriod.MONTHS) //
				.withVisitDuration(36) //
				.withVolumeLimits(2_950_000, 4_000_000, VolumeUnits.MMBTU) //
				.withCancellationFee("10500000") //
				.build();

		cargoModelBuilder.makeDESSale("New_Discharge", LocalDate.of(2017, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_DUNKIRK), null, entity, "16%BRENT_ICE") //
				.withWindowStartTime(0) //
				.withWindowSize(1, TimePeriod.MONTHS) //
				.withVisitDuration(36) //
				.withVolumeLimits(2_910_000, 3_811_000, VolumeUnits.MMBTU) //
				.withCancellationFee("350000*FX_EUR_USD") //
				.build();

		final UserSettings userSettings = createUserSettings();
		userSettings.setShippingOnly(false);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(createOptimisationPlan(userSettings)) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final Schedule initialSchedule = ScenarioModelUtil.getScheduleModel(lngScenarioModel).getSchedule();
		Assertions.assertNotNull(initialSchedule);

		final long initialPNL = ScheduleModelKPIUtils.getScheduleProfitAndLoss(initialSchedule);
		final long initialLateness = ScheduleModelKPIUtils.getScheduleLateness(initialSchedule)[ScheduleModelKPIUtils.LATENESS_WITHOUT_FLEX_IDX];
		final long initialViolations = ScheduleModelKPIUtils.getScheduleViolationCount(initialSchedule);

		runnerBuilder.run(false, runner -> {
			// Run, get result and store to schedule model for inspection at EMF level if
			// needed
			final IMultiStateResult result = runner.runWithProgress(new NullProgressMonitor());

			final LNGScenarioToOptimiserBridge bridge = runnerBuilder.getScenarioRunner().getScenarioToOptimiserBridge();
			final OptimiserDataMapper mapper = new OptimiserDataMapper(bridge);
			final List<SolutionData> solutionDataList = OptimiserResultVerifier.createSolutionData(true, result, mapper);
			// Solution 1
			{
				final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(mapper) //
						.withAnySolutionResultChecker()
						.withCargo("New_Load", "New_Discharge")
						.onFleetVessel(SMALL_SHIP) //
						.withUsedLoad("A_3")
						.onFleetVessel(SMALL_SHIP) //
						.withUsedLoad("S_1")
						.onFleetVessel(MEDIUM_SHIP) //
						.withUsedLoad("S_4")
						.onFleetVessel(LARGE_SHIP) //
						.violationDelta(initialViolations, -1) //
						.latenessDelta(initialLateness, 0) //
						.pnlDelta(initialPNL, 19_124_719, 1_000) //
						.build();

				final ISequences solution = verifier.verifySolutionExistsInResults(solutionDataList, Assertions::fail);

			}
			// Solution 2
			{
				final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(mapper) //
						.withAnySolutionResultChecker()
						.withCargo("New_Load", "New_Discharge")
						.onFleetVessel(SMALL_SHIP) //
						.violationDelta(initialViolations, -1) //
						.latenessDelta(initialLateness, 0) //
						.pnlDelta(initialPNL, 18_179_820, 1_000) //
						.build();

				final ISequences solution = verifier.verifySolutionExistsInResults(result, Assertions::fail);
				Assertions.assertNotNull(solution);

			}
		});

	}

}