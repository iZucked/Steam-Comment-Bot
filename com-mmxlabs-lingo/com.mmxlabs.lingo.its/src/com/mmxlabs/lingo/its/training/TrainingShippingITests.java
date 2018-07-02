/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.training;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.TestMode;
import com.mmxlabs.lingo.its.tests.TestingModes;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lingo.its.verifier.OptimiserDataMapper;
import com.mmxlabs.lingo.its.verifier.OptimiserResultVerifier;
import com.mmxlabs.lingo.its.verifier.SolutionData;
import com.mmxlabs.lngdataserver.data.distances.DataConstants;
import com.mmxlabs.lngdataserver.data.distances.DataLoader;
import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;
import com.mmxlabs.lngdataserver.integration.ports.model.PortsVersion;
import com.mmxlabs.lngdataserver.lng.io.distances.DistancesToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.io.port.PortsToScenarioCopier;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
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

@ExtendWith(value = ShiroRunner.class)
public class TrainingShippingITests extends AbstractMicroTestCase {

	private static List<String> requiredFeatures = Lists.newArrayList("optimisation-similarity", "optimisation-hillclimb");
	private static List<String> addedFeatures = new LinkedList<>();

	@BeforeAll
	public static void hookIn() {
		for (final String feature : requiredFeatures) {
			if (!LicenseFeatures.isPermitted("features:" + feature)) {
				LicenseFeatures.addFeatureEnablements(feature);
				addedFeatures.add(feature);
			}
		}
	}

	@AfterAll
	public static void hookOut() {
		for (final String feature : addedFeatures) {
			LicenseFeatures.removeFeatureEnablements(feature);
		}
		addedFeatures.clear();
	}

	// Which scenario data to import
	@Override
	public @NonNull IScenarioDataProvider importReferenceData() throws Exception {
		final IScenarioDataProvider scenarioDataProvider = importReferenceData("/trainingcases/Shipping_I/");

		updateDistanceData(scenarioDataProvider, DataConstants.DISTANCES_LATEST_JSON);
		updatePortsData(scenarioDataProvider, DataConstants.PORTS_LATEST_JSON);

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

		editingDomain.getCommandStack().execute(updateCommand);

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

		editingDomain.getCommandStack().execute(updateCommand);

		// Ensure updated.
		Assertions.assertEquals(version.getIdentifier(), portModel.getPortVersionRecord().getVersion());
	}

	@Override
	protected BaseLegalEntity importDefaultEntity() {
		return commercialModelFinder.findEntity("Entity");
	}

	protected @NonNull UserSettings createUserSettings() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setAdpOptimisation(false);
		userSettings.setCleanStateOptimisation(false);

		userSettings.setShippingOnly(true);
		userSettings.setWithSpotCargoMarkets(true);
		userSettings.setSimilarityMode(SimilarityMode.ALL);
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

		return optimisationPlan;
	}

	@Test
	@Tag(TestCategories.OPTIMISATION_TEST)
	public void testShipping_I_Stage_1_Shipping() throws Exception {
		Assumptions.assumeTrue(TestingModes.OptimisationTestMode == TestMode.Run);

		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		final UserSettings userSettings = createUserSettings();

		try (final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(createOptimisationPlan(userSettings)) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner()) {

			runnerBuilder.evaluateInitialState();

			final Schedule initialSchedule = ScenarioModelUtil.getScheduleModel(lngScenarioModel).getSchedule();
			Assertions.assertNotNull(initialSchedule);
			final long initialPNL = ScheduleModelKPIUtils.getScheduleProfitAndLoss(initialSchedule);
			runnerBuilder.run(false, runner -> {

				// Run, get result and store to schedule model for inspection at EMF level if needed
				final IMultiStateResult result = runner.runWithProgress(new NullProgressMonitor());
				final LNGScenarioToOptimiserBridge bridge = runnerBuilder.getScenarioRunner().getScenarioToOptimiserBridge();
				final OptimiserDataMapper mapper = new OptimiserDataMapper(bridge);
				final List<SolutionData> solutionDataList = OptimiserResultVerifier.createSolutionData(true, result, mapper);
				// Solution 1
				{
					final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(mapper) //
							.withAnySolutionResultChecker().withUsedLoad("A_3").onFleetVessel("Small Ship") //
							.withUsedLoad("S_1").onFleetVessel("Medium Ship") //
							.withUsedLoad("S_4").onFleetVessel("Large Ship") //
							.pnlDelta(initialPNL, 944_899, 1_000) //
							.build();

					final ISequences solution = verifier.verifySolutionExistsInResults(solutionDataList, Assertions::fail);
					Assertions.assertNotNull(solution);
				}
				// Solution 2
				{
					final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(mapper) //
							.withAnySolutionResultChecker().withUsedLoad("A_3").onFleetVessel("Small Ship") //
							.withUsedLoad("S_4").onFleetVessel("Medium Ship") //
							.pnlDelta(initialPNL, 610_378, 1_000) //
							.build();

					final ISequences solution = verifier.verifySolutionExistsInResults(solutionDataList, Assertions::fail);
					Assertions.assertNotNull(solution);
				}
			});
		}
	}

	@Test
	@Tag(TestCategories.OPTIMISATION_TEST)
	public void testShipping_I_Stage_2_1_Lateness() throws Exception {
		Assumptions.assumeTrue(TestingModes.OptimisationTestMode == TestMode.Run);

		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		// Make changes to initial data
		cargoModelFinder.findLoadSlot("PE_1").setWindowSizeUnits(TimePeriod.DAYS);
		cargoModelFinder.findVesselAvailability("Large ship").setStartAt(portFinder.findPort("Gladstone"));
		cargoModelFinder.findVesselAvailability("Medium ship").setStartAt(portFinder.findPort("Barcelona"));
		cargoModelFinder.findVesselAvailability("Small ship").setStartAt(portFinder.findPort("Sabine Pass"));

		final UserSettings userSettings = createUserSettings();

		try (final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(createOptimisationPlan(userSettings)) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner()) {

			runnerBuilder.evaluateInitialState();

			final Schedule initialSchedule = ScenarioModelUtil.getScheduleModel(lngScenarioModel).getSchedule();
			Assertions.assertNotNull(initialSchedule);

			final long initialPNL = ScheduleModelKPIUtils.getScheduleProfitAndLoss(initialSchedule);
			final long initialLateness = ScheduleModelKPIUtils.getScheduleLateness(initialSchedule)[ScheduleModelKPIUtils.LATENESS_WITHOUT_FLEX_IDX];
			final long initialViolations = ScheduleModelKPIUtils.getScheduleViolationCount(initialSchedule);

			runnerBuilder.run(false, runner -> {
				// Run, get result and store to schedule model for inspection at EMF level if needed
				final IMultiStateResult result = runner.runWithProgress(new NullProgressMonitor());
				final LNGScenarioToOptimiserBridge bridge = runnerBuilder.getScenarioRunner().getScenarioToOptimiserBridge();
				final OptimiserDataMapper mapper = new OptimiserDataMapper(bridge);
				final List<SolutionData> solutionDataList = OptimiserResultVerifier.createSolutionData(true, result, mapper);
				// Solution 1
				{
					final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(mapper) //
							.withAnySolutionResultChecker().withUsedLoad("A_3").onFleetVessel("Small Ship") //
							.withUsedLoad("BO_1").onFleetVessel("Small Ship") //
							.withUsedLoad("S_1").onFleetVessel("Medium Ship") //
							.withUsedLoad("S_4").onFleetVessel("Large Ship") //
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
							.withAnySolutionResultChecker().withUsedLoad("A_3").onFleetVessel("Small Ship") //
							.withUsedLoad("BO_1").onFleetVessel("Small Ship") //
							.withUsedLoad("S_4").onFleetVessel("Medium Ship") //
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
							.withAnySolutionResultChecker().withUsedLoad("BO_1").onFleetVessel("Small Ship") //
							.violationDelta(initialViolations, -1) //
							.latenessDelta(initialLateness, -((4 * 24) + 5)) //
							.pnlDelta(initialPNL, -2_144_366, 1_000) //
							.build();

					final ISequences solution = verifier.verifySolutionExistsInResults(solutionDataList, msg -> Assertions.fail(msg));
					Assertions.assertNotNull(solution);

				}
			});
		}
	}

	@Test
	@Tag(TestCategories.OPTIMISATION_TEST)
	public void testShipping_I_Stage_2_2_Lateness_with_charter_in() throws Exception {
		Assumptions.assumeTrue(TestingModes.OptimisationTestMode == TestMode.Run);

		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		// Make changes to initial data
		cargoModelFinder.findLoadSlot("PE_1").setWindowSizeUnits(TimePeriod.DAYS);
		cargoModelFinder.findVesselAvailability("Large ship").setStartAt(portFinder.findPort("Gladstone"));
		cargoModelFinder.findVesselAvailability("Medium ship").setStartAt(portFinder.findPort("Barcelona"));
		cargoModelFinder.findVesselAvailability("Small ship").setStartAt(portFinder.findPort("Sabine Pass"));

		spotMarketsModelBuilder.getSpotMarketsModel().getCharterInMarkets().clear();
		final CharterInMarket charterMarket = spotMarketsModelBuilder.createCharterInMarket("CI_10", fleetModelFinder.findVessel("Steam_2"), "21750", 1);
		charterMarket.setEnabled(true);
		charterMarket.setNominal(true);

		final UserSettings userSettings = createUserSettings();

		try (final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(createOptimisationPlan(userSettings)) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner()) {

			runnerBuilder.evaluateInitialState();

			final Schedule initialSchedule = ScenarioModelUtil.getScheduleModel(lngScenarioModel).getSchedule();
			Assertions.assertNotNull(initialSchedule);

			final long initialPNL = ScheduleModelKPIUtils.getScheduleProfitAndLoss(initialSchedule);
			final long initialLateness = ScheduleModelKPIUtils.getScheduleLateness(initialSchedule)[ScheduleModelKPIUtils.LATENESS_WITHOUT_FLEX_IDX];
			final long initialViolations = ScheduleModelKPIUtils.getScheduleViolationCount(initialSchedule);

			runnerBuilder.run(false, runner -> {

				// Run, get result and store to schedule model for inspection at EMF level if needed
				final IMultiStateResult result = runner.runWithProgress(new NullProgressMonitor());
				final LNGScenarioToOptimiserBridge bridge = runnerBuilder.getScenarioRunner().getScenarioToOptimiserBridge();
				final OptimiserDataMapper mapper = new OptimiserDataMapper(bridge);
				final List<SolutionData> solutionDataList = OptimiserResultVerifier.createSolutionData(true, result, mapper);
				// Solution 1
				{
					final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(mapper) //
							.withAnySolutionResultChecker().withUsedLoad("A_3").onFleetVessel("Small Ship") //
							.withUsedLoad("BO_2").onSpotCharter("CI_10") //
							.withUsedLoad("S_1").onFleetVessel("Medium Ship") //
							.withUsedLoad("S_4").onFleetVessel("Large Ship") //
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
							.withAnySolutionResultChecker().withUsedLoad("A_3").onFleetVessel("Small Ship") //
							.withUsedLoad("BO_2").onSpotCharter("CI_10") //
							.withUsedLoad("S_4").onFleetVessel("Medium Ship") //
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
							.withAnySolutionResultChecker().withUsedLoad("BO_2").onSpotCharter("CI_10") //
							.violationDelta(initialViolations, 0) //
							.latenessDelta(initialLateness, -((4 * 24) + 5)) //
							.pnlDelta(initialPNL, -1_351_441, 1_000) //
							.build();

					final ISequences solution = verifier.verifySolutionExistsInResults(result, Assertions::fail);
					Assertions.assertNotNull(solution);
				}

			});
		}
	}

	@Test
	@Tag(TestCategories.OPTIMISATION_TEST)
	public void testShipping_I_Stage_3_1_allocation_and_keep_open() throws Exception {
		Assumptions.assumeTrue(TestingModes.OptimisationTestMode == TestMode.Run);

		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		// Make changes to initial data
		final Port p = portFinder.findPort("Corpus Christi");
		p.getCapabilities().add(PortCapability.LOAD);
		p.setLoadDuration(24);
		p.setCvValue(22.7);
		costModelBuilder.createPortCost(Collections.singleton(p), Collections.singleton(PortCapability.LOAD), 140000);

		cargoModelFinder.findVesselAvailability("Large ship").setStartAt(portFinder.findPort("Gladstone"));
		cargoModelFinder.findVesselAvailability("Medium ship").setStartAt(portFinder.findPort("Barcelona"));
		cargoModelFinder.findVesselAvailability("Small ship").setStartAt(portFinder.findPort("Sabine Pass"));

		cargoModelFinder.findLoadSlot("S_3").setArriveCold(false);

		cargoModelBuilder.makeFOBPurchase("New_Load", LocalDate.of(2017, 1, 1), p, null, entity, "10.5%BRENT_ICE", 22.7) //
				.withWindowStartTime(0) //
				.withWindowSize(1, TimePeriod.MONTHS) //
				.withVisitDuration(36) //
				.withVolumeLimits(2_950_000, 4_000_000, VolumeUnits.MMBTU) //
				.withCancellationFee("10500000") //
				.build();

		cargoModelBuilder.makeDESSale("New_Discharge", LocalDate.of(2017, 2, 1), portFinder.findPort("Dunkirk"), null, entity, "16%BRENT_ICE") //
				.withWindowStartTime(0) //
				.withWindowSize(1, TimePeriod.MONTHS) //
				.withVisitDuration(36) //
				.withVolumeLimits(2_910_000, 3_811_000, VolumeUnits.MMBTU) //
				.withCancellationFee("350000*FX_EUR_USD") //
				.build();

		final UserSettings userSettings = createUserSettings();
		userSettings.setShippingOnly(false);

		try (final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(createOptimisationPlan(userSettings)) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner()) {

			runnerBuilder.evaluateInitialState();

			final Schedule initialSchedule = ScenarioModelUtil.getScheduleModel(lngScenarioModel).getSchedule();
			Assertions.assertNotNull(initialSchedule);

			final long initialPNL = ScheduleModelKPIUtils.getScheduleProfitAndLoss(initialSchedule);
			final long initialLateness = ScheduleModelKPIUtils.getScheduleLateness(initialSchedule)[ScheduleModelKPIUtils.LATENESS_WITHOUT_FLEX_IDX];
			final long initialViolations = ScheduleModelKPIUtils.getScheduleViolationCount(initialSchedule);

			runnerBuilder.run(false, runner -> {
				// Run, get result and store to schedule model for inspection at EMF level if needed
				final IMultiStateResult result = runner.runWithProgress(new NullProgressMonitor());

				final LNGScenarioToOptimiserBridge bridge = runnerBuilder.getScenarioRunner().getScenarioToOptimiserBridge();
				final OptimiserDataMapper mapper = new OptimiserDataMapper(bridge);
				final List<SolutionData> solutionDataList = OptimiserResultVerifier.createSolutionData(true, result, mapper);
				// Solution 1
				{
					final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(mapper) //
							.withAnySolutionResultChecker().withCargo("New_Load", "New_Discharge").onFleetVessel("Small Ship") //
							.withUsedLoad("A_3").onFleetVessel("Small Ship") //
							.withUsedLoad("S_1").onFleetVessel("Medium Ship") //
							.withUsedLoad("S_4").onFleetVessel("Large Ship") //
							.violationDelta(initialViolations, -1) //
							.latenessDelta(initialLateness, 0) //
							.pnlDelta(initialPNL, 19_124_719, 1_000) //
							.build();

					final ISequences solution = verifier.verifySolutionExistsInResults(solutionDataList, Assertions::fail);

				}
				// Solution 2
				{
					final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(mapper) //
							.withAnySolutionResultChecker().withCargo("New_Load", "New_Discharge").onFleetVessel("Small Ship") //
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

}