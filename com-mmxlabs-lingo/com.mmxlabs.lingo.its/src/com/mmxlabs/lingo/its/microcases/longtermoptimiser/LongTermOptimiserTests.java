/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
///**
// * Copyright (C) Minimax Labs Ltd., 2010 - 2017
// * All rights reserved.
// */
//package com.mmxlabs.lingo.its.microcases.longtermoptimiser;
//
//import java.time.LocalDate;
//import java.util.Collections;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import org.eclipse.core.runtime.NullProgressMonitor;
//import org.eclipse.jdt.annotation.NonNull;
//import org.junit.jupiter.api.AfterClass;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Before;
//import org.junit.jupiter.api.BeforeClass;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.experimental.categories.Category;
//import org.junit.jupiter.api.runner.RunWith;
//
//import com.google.common.collect.Lists;
//import com.mmxlabs.license.features.LicenseFeatures;
//import com.mmxlabs.lingo.its.tests.category.MicroTest;
//import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
//import com.mmxlabs.models.lng.cargo.Cargo;
//import com.mmxlabs.models.lng.cargo.DischargeSlot;
//import com.mmxlabs.models.lng.cargo.LoadSlot;
//import com.mmxlabs.models.lng.fleet.Vessel;
//import com.mmxlabs.models.lng.parameters.Constraint;
//import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
//import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
//import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
//import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
//import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
//import com.mmxlabs.models.lng.transformer.longterm.LongTermOptimiserUnit;
//import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
//import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
//import com.mmxlabs.models.lng.types.TimePeriod;
//import com.mmxlabs.optimiser.core.IMultiStateResult;
//import com.mmxlabs.scheduler.optimiser.constraints.impl.LadenLegLimitConstraintCheckerFactory;
//import com.mmxlabs.scheduler.optimiser.constraints.impl.PromptRoundTripVesselPermissionConstraintCheckerFactory;
//import com.mmxlabs.scheduler.optimiser.constraints.impl.RoundTripVesselPermissionConstraintCheckerFactory;
//
//@SuppressWarnings("unused")
//@ExtendWith(ShiroRunner.class)
//public class LongTermOptimiserTests extends AbstractMicroTestCase {
//	@Override
//	protected @NonNull ExecutorService createExecutorService() {
//		return Executors.newFixedThreadPool(4);
//	}
//
//	private static List<String> requiredFeatures = Lists.newArrayList("no-nominal-in-prompt", "optimisation-actionset");
//	private static List<String> addedFeatures = new LinkedList<>();
//
//	@BeforeAll
//	public static void hookIn() {
//		for (final String feature : requiredFeatures) {
//			if (!LicenseFeatures.isPermitted("features:" + feature)) {
//				LicenseFeatures.addFeatureEnablements(feature);
//				addedFeatures.add(feature);
//			}
//		}
//	}
//
//	@AfterAll
//	public static void hookOut() {
//		for (final String feature : addedFeatures) {
//			LicenseFeatures.removeFeatureEnablements(feature);
//		}
//		addedFeatures.clear();
//	}
//
//	@BeforeEach
//	@Override
//	public void constructor() throws Exception {
//
//		super.constructor();
//		// Set a default prompt in the past
//		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2014, 1, 1), LocalDate.of(2018, 3, 1));
//	}
//	//
//	// @Test
//	// @Tag(TestCategories.MICRO_TEST)
//	// public void testLongTermOptimiser() throws Exception {
//	//
//	// lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
//	// lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();
//	// final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
//	//
//	// final LoadSlot load_DES1 = cargoModelBuilder.makeDESPurchase("DES_Purchase", false, LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "5", 22.8, null).build();
//	// final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();
//	// final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);
//	//
//	// evaluateWithLSOTest(true, (plan) -> {
//	// // Clear default stages so we can run our own stuff here.
//	// plan.getStages().clear();
//	// }, null, scenarioRunner -> {
//	// final LongTermOptimiserUnit longTermOptimiser = getOptimiser(scenarioRunner);
//	//
//	// final IMultiStateResult result1 = longTermOptimiser.run(new NullProgressMonitor());
//	// Assertions.assertNotNull(result1);
//	// Assertions.assertTrue(result1.getSolutions().size() == 2);
//	// assert result1.getSolutions().get(1).getFirst().getUnusedElements().isEmpty();
//	//
//	// final IMultiStateResult result2 = longTermOptimiser.run(new NullProgressMonitor());
//	// Assertions.assertNotNull(result2);
//	// Assertions.assertTrue(result2.getSolutions().size() == 2);
//	// assert result2.getSolutions().get(1).getFirst().getUnusedElements().isEmpty();
//	// }, null);
//	// }
//
//	@Test
//	@Tag(TestCategories.MICRO_TEST)
//	public void testInsertShippedPair_Open() throws Exception {
//
//		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
//		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();
//
//		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
//		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 0);
//
//		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("Bonny", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_DARWIN), null, entity, "5", 22.8)
//				.withWindowSize(1, TimePeriod.MONTHS).build();
//		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 2, 5), portFinder.findPortById(InternalDataConstants.PORT_BARCELONA), null, entity, "7")
//				.withWindowSize(1, TimePeriod.MONTHS).build();
//
//		// Create cargo 1, cargo 2
//		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
//				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_HAMMERFEST), null, entity, "5") //
//				.withWindowSize(1, TimePeriod.MONTHS).build() //
//				.makeDESSale("D1", LocalDate.of(2016, 2, 5), portFinder.findPortById(InternalDataConstants.PORT_INCHEON), null, entity, "7") //
//				.withWindowSize(1, TimePeriod.MONTHS).build() //
//				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
//				.withAssignmentFlags(true, false) //
//				.build();
//
//		evaluateWithLSOTest(true, (plan) -> {
//			// Clear default stages so we can run our own stuff here.
//			plan.getStages().clear();
//		}, null, scenarioRunner -> {
//			final LongTermOptimiserUnit longTermOptimiser = getOptimiser(scenarioRunner);
//
//			final IMultiStateResult result1 = longTermOptimiser.run(new NullProgressMonitor());
//			Assertions.assertNotNull(result1);
//			Assertions.assertTrue(result1.getSolutions().size() == 2);
//			assert result1.getSolutions().get(1).getFirst().getUnusedElements().isEmpty();
//
//			final IMultiStateResult result2 = longTermOptimiser.run(new NullProgressMonitor());
//			Assertions.assertNotNull(result2);
//			Assertions.assertTrue(result2.getSolutions().size() == 2);
//			assert result2.getSolutions().get(1).getFirst().getUnusedElements().isEmpty();
//		}, null);
//	}
//
//	// /**
//	// * Test: test a nominal vessel start and end heel is present
//	// *
//	// * @throws Exception
//	// */
//	// @Test
//	// @Tag(TestCategories.MICRO_TEST)
//	// public void testNominalHeel() throws Exception {
//	//
//	// // Create the required basic elements
//	// final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
//	// vesselClass.setMinHeel(500);
//	//
//	// final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);
//	//
//	// // Construct the cargo scenario
//	//
//	// // Create cargo 1, cargo 2
//	// final Cargo cargo1 = cargoModelBuilder.makeCargo() //
//	// .makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
//	// .build() //
//	// .makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
//	// .build() //
//	// .withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
//	// .withAssignmentFlags(false, false) //
//	// .build();
//	//
//	// evaluateWithLSOTest(scenarioRunner -> {
//	//
//	// final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
//	//
//	// // Check spot index has been updated
//	// final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
//	// // Check cargoes removed
//	// Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
//	//
//	// // Check correct cargoes remain and spot index has changed.
//	// final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);
//	//
//	// @Nullable
//	// final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
//	// Assertions.assertNotNull(schedule);
//	//
//	// @Nullable
//	// final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);
//	//
//	// Assertions.assertNotNull(cargoAllocation);
//	// final EList<Event> events = cargoAllocation.getEvents();
//	// final Event firstEvent = events.get(0);
//	// final Event lastEvent = events.get(events.size() - 1);
//	//
//	// // Check safety heel present.
//	// Assertions.assertEquals(vesselClass.getMinHeel(), firstEvent.getHeelAtStart());
//	// Assertions.assertEquals(vesselClass.getMinHeel(), lastEvent.getHeelAtEnd());
//	//
//	// });
//	// }
//
//	private LongTermOptimiserUnit getOptimiser(LNGScenarioRunner scenarioRunner) {
//		@NonNull
//		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
//		@NonNull
//		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
//
//		final ConstraintAndFitnessSettings constraintAndFitnessSettings = ScenarioUtils.createDefaultConstraintAndFitnessSettings();
//		// TODO: Filter
//		final Iterator<Constraint> iterator = constraintAndFitnessSettings.getConstraints().iterator();
//		while (iterator.hasNext()) {
//			final Constraint constraint = iterator.next();
//			if (constraint.getName().equals(PromptRoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
//				iterator.remove();
//			}
//			if (constraint.getName().equals(RoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
//				iterator.remove();
//			}
//		}
//
//		ScenarioUtils.createOrUpdateContraints(LadenLegLimitConstraintCheckerFactory.NAME, true, constraintAndFitnessSettings);
//
//		final LongTermOptimiserUnit slotInserter = new LongTermOptimiserUnit(dataTransformer, "pairing-stage", dataTransformer.getUserSettings(), constraintAndFitnessSettings,
//				scenarioRunner.getExecutorService(), dataTransformer.getInitialSequences(), scenarioRunner.getScenarioDataProvider(), dataTransformer.getInitialResult(), Collections.emptyList());
//		return slotInserter;
//	}
//
//}