/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.training;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.HashMap;
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
import com.mmxlabs.lingo.its.verifier.OptimiserResultVerifier;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.scenario.CSVImporter;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.impl.ParallelOptimisationPlanExtender;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.fitness.components.NonOptionalSlotFitnessCoreFactory;

@RunWith(value = ShiroRunner.class)
public class TrainingShippingITests extends AbstractMicroTestCase {

	private static List<String> requiredFeatures = Lists.newArrayList("optimisation-similarity", "optimisation-hillclimb");
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

	@Override
	public @NonNull IScenarioDataProvider importReferenceData() throws MalformedURLException {
		final IScenarioDataProvider scenarioDataProvider = importReferenceData("/trainingcases/Shipping_I/");

		return scenarioDataProvider;
	}

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
		userSettings.setAdpOptimisation(false);
		userSettings.setCleanStateOptimisation(false);

		userSettings.setShippingOnly(true);
		userSettings.setWithSpotCargoMarkets(true);
		userSettings.setSimilarityMode(SimilarityMode.ALL);
		return userSettings;
	}

	protected OptimisationPlan createOptimisationPlan(final UserSettings userSettings) {
		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		ScenarioUtils.setLSOStageIterations(optimisationPlan, 1_000_000);
		ScenarioUtils.setHillClimbStageIterations(optimisationPlan, 50_000);
		ScenarioUtils.setActionPlanStageParameters(optimisationPlan, 5_000_000, 1_500_000, 5_000);
		ScenarioUtils.createOrUpdateAllObjectives(optimisationPlan, NonOptionalSlotFitnessCoreFactory.NAME, true, 24_000_000);

		new ParallelOptimisationPlanExtender().extend(optimisationPlan);

		return optimisationPlan;
	}

	protected @NonNull VesselAvailability createDefaultAvailability(@NonNull String vesselName) {
		return createDefaultAvailability(fleetModelFinder.findVessel(vesselName));
	}

	protected @NonNull VesselAvailability createDefaultAvailability(@NonNull Vessel vessel) {
		return cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("70000") //
				.withStartWindow(LocalDateTime.of(2017, 1, 1, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 8, 1, 0, 0)) //
				.withStartHeel(6_000, 6_000, 22.67, "0.01") //
				.withEndHeel(0, 500, EVesselTankState.EITHER, "0") //
				.build();
	}

	@Test
	@Category({ OptimisationTest.class })
	public void testShipping_I_Stage_1_Shipping() throws Exception {

		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		final UserSettings userSettings = createUserSettings();

		try (final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(createOptimisationPlan(userSettings)) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner()) {

			runnerBuilder.evaluateInitialState();

			final Schedule initialSchedule = ScenarioModelUtil.getScheduleModel(lngScenarioModel).getSchedule();
			Assert.assertNotNull(initialSchedule);
			final long initialPNL = ScheduleModelKPIUtils.getScheduleProfitAndLoss(initialSchedule);
			runnerBuilder.run(false, runner -> {

				// Run, get result and store to schedule model for inspection at EMF level if needed
				final IMultiStateResult result = runner.runWithProgress(new NullProgressMonitor());

				// Solution 1
				{
					final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner) //
							.withUsedLoad("A_3").onFleetVessel("Small Ship") //
							.withUsedLoad("S_1").onFleetVessel("Medium Ship") //
							.withUsedLoad("S_4").onFleetVessel("Large Ship") //
							.pnlDelta(initialPNL, 944_899, 1_000) //
					;

					final ISequences solution = verifier.verifySolutionExistsInResults(result, msg -> Assert.fail(msg));
					Assert.assertNotNull(solution);
				}
				// Solution 2
				{
					final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner) //
							.withUsedLoad("A_3").onFleetVessel("Small Ship") //
							.withUsedLoad("S_4").onFleetVessel("Medium Ship") //
							.pnlDelta(initialPNL, 610_378, 1_000) //
					;

					final ISequences solution = verifier.verifySolutionExistsInResults(result, msg -> Assert.fail(msg));
					Assert.assertNotNull(solution);
				}
			});
		}
	}

	@Test
	@Category({ OptimisationTest.class })
	public void testShipping_I_Stage_2_1_Lateness() throws Exception {

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
			Assert.assertNotNull(initialSchedule);

			final long initialPNL = ScheduleModelKPIUtils.getScheduleProfitAndLoss(initialSchedule);
			final long initialLateness = ScheduleModelKPIUtils.getScheduleLateness(initialSchedule)[ScheduleModelKPIUtils.LATENESS_WITHOUT_FLEX_IDX];
			final long initialViolations = ScheduleModelKPIUtils.getScheduleViolationCount(initialSchedule);

			runnerBuilder.run(false, runner -> {
				// Run, get result and store to schedule model for inspection at EMF level if needed
				final IMultiStateResult result = runner.runWithProgress(new NullProgressMonitor());

				// Solution 1
				{
					final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner) //
							.withUsedLoad("A_3").onFleetVessel("Small Ship") //
							.withUsedLoad("BO_1").onFleetVessel("Small Ship") //
							.withUsedLoad("S_1").onFleetVessel("Medium Ship") //
							.withUsedLoad("S_4").onFleetVessel("Large Ship") //
							.violationDelta(initialViolations, -1) //
							.latenessDelta(initialLateness, -((4 * 24) + 5)) //
							.pnlDelta(initialPNL, -992_994, 1_000) //
					;

					final ISequences solution = verifier.verifySolutionExistsInResults(result, msg -> Assert.fail(msg));
					Assert.assertNotNull(solution);

					final Schedule schedule = runner.getScenarioToOptimiserBridge().createSchedule(solution, new HashMap<>());
					final long pnl = ScheduleModelKPIUtils.getScheduleProfitAndLoss(schedule);
					final long lateness = ScheduleModelKPIUtils.getScheduleLateness(schedule)[ScheduleModelKPIUtils.LATENESS_WITHOUT_FLEX_IDX];
					final long violations = ScheduleModelKPIUtils.getScheduleViolationCount(schedule);
					Assert.assertEquals(-992_994, pnl - initialPNL, 1_000);
					Assert.assertEquals(-(24 * 4 + 5), lateness - initialLateness);
					Assert.assertEquals(-1, violations - initialViolations);
				}
				// Solution 2
				{
					final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner) //
							.withUsedLoad("A_3").onFleetVessel("Small Ship") //
							.withUsedLoad("BO_1").onFleetVessel("Small Ship") //
							.withUsedLoad("S_4").onFleetVessel("Medium Ship") //
							.violationDelta(initialViolations, -1) //
							.latenessDelta(initialLateness, -((4 * 24) + 5)) //
							.pnlDelta(initialPNL, -1_327_515, 1_000) //
					;

					final ISequences solution = verifier.verifySolutionExistsInResults(result, msg -> Assert.fail(msg));
					Assert.assertNotNull(solution);
					final Schedule schedule = runner.getScenarioToOptimiserBridge().createSchedule(solution, new HashMap<>());

					final long pnl = ScheduleModelKPIUtils.getScheduleProfitAndLoss(schedule);
					final long lateness = ScheduleModelKPIUtils.getScheduleLateness(schedule)[ScheduleModelKPIUtils.LATENESS_WITHOUT_FLEX_IDX];
					final long violations = ScheduleModelKPIUtils.getScheduleViolationCount(schedule);
					Assert.assertEquals(-1_327_515, pnl - initialPNL, 1_000);
					Assert.assertEquals(-(24 * 4 + 5), lateness - initialLateness);
					Assert.assertEquals(-1, violations - initialViolations);
				}
				// Solution 3
				{
					final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner) //
							.withUsedLoad("BO_1").onFleetVessel("Small Ship") //
							.violationDelta(initialViolations, -1) //
							.latenessDelta(initialLateness, -((4 * 24) + 5)) //
							.pnlDelta(initialPNL, -2_144_366, 1_000) //
					;

					final ISequences solution = verifier.verifySolutionExistsInResults(result, msg -> Assert.fail(msg));
					Assert.assertNotNull(solution);

				}
			});
		}
	}

	@Test
	@Category({ OptimisationTest.class })
	public void testShipping_I_Stage2_2_Lateness_with_charter_in() throws Exception {

		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		// Make changes to initial data
		cargoModelFinder.findLoadSlot("PE_1").setWindowSizeUnits(TimePeriod.DAYS);
		cargoModelFinder.findVesselAvailability("Large ship").setStartAt(portFinder.findPort("Gladstone"));
		cargoModelFinder.findVesselAvailability("Medium ship").setStartAt(portFinder.findPort("Barcelona"));
		cargoModelFinder.findVesselAvailability("Small ship").setStartAt(portFinder.findPort("Sabine Pass"));

		spotMarketsModelBuilder.getSpotMarketsModel().getCharterInMarkets().clear();
		CharterInMarket charterMarket = spotMarketsModelBuilder.createCharterInMarket("CI_10", fleetModelFinder.findVessel("Steam_2"), "21750", 1);
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
			Assert.assertNotNull(initialSchedule);

			final long initialPNL = ScheduleModelKPIUtils.getScheduleProfitAndLoss(initialSchedule);
			final long initialLateness = ScheduleModelKPIUtils.getScheduleLateness(initialSchedule)[ScheduleModelKPIUtils.LATENESS_WITHOUT_FLEX_IDX];
			final long initialViolations = ScheduleModelKPIUtils.getScheduleViolationCount(initialSchedule);

			runnerBuilder.run(false, runner -> {

				// Run, get result and store to schedule model for inspection at EMF level if needed
				final IMultiStateResult result = runner.runWithProgress(new NullProgressMonitor());

				// Solution 1
				{
					final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner) //
							.withUsedLoad("A_3").onFleetVessel("Small Ship") //
							.withUsedLoad("BO_2").onSpotCharter("CI_10") //
							.withUsedLoad("S_1").onFleetVessel("Medium Ship") //
							.withUsedLoad("S_4").onFleetVessel("Large Ship") //
							.violationDelta(initialViolations, 0) //
							.latenessDelta(initialLateness, -((4 * 24) + 5)) //
							.pnlDelta(initialPNL, -147_982, 1_000) //
					;

					final ISequences solution = verifier.verifySolutionExistsInResults(result, msg -> Assert.fail(msg));
					Assert.assertNotNull(solution);
				}
				// Solution 2
				{
					final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner) //
							.withUsedLoad("A_3").onFleetVessel("Small Ship") //
							.withUsedLoad("BO_2").onSpotCharter("CI_10") //
							.withUsedLoad("S_4").onFleetVessel("Medium Ship") //
							.violationDelta(initialViolations, 0) //
							.latenessDelta(initialLateness, -((4 * 24) + 5)) //
							.pnlDelta(initialPNL, -482_503, 1_000) //
					;

					final ISequences solution = verifier.verifySolutionExistsInResults(result, msg -> Assert.fail(msg));
					Assert.assertNotNull(solution);

				}
				// Solution 3
				{
					final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner) //
							.withUsedLoad("BO_2").onSpotCharter("CI_10") //
							.violationDelta(initialViolations, 0) //
							.latenessDelta(initialLateness, -((4 * 24) + 5)) //
							.pnlDelta(initialPNL, -1_351_441, 1_000) //
					;

					final ISequences solution = verifier.verifySolutionExistsInResults(result, msg -> Assert.fail(msg));
					Assert.assertNotNull(solution);
				}

			});
		}
	}

}