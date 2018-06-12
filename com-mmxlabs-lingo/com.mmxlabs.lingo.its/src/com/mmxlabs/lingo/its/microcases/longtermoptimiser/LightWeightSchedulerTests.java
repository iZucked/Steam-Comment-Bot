/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
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
//import org.junit.AfterClass;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.experimental.categories.Category;
//import org.junit.runner.RunWith;
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
//import com.mmxlabs.models.lng.transformer.longterm.LightWeightSchedulerOptimiserUnit;
//import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
//import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
//import com.mmxlabs.models.lng.types.TimePeriod;
//import com.mmxlabs.optimiser.core.IMultiStateResult;
//import com.mmxlabs.scheduler.optimiser.constraints.impl.LadenLegLimitConstraintCheckerFactory;
//import com.mmxlabs.scheduler.optimiser.constraints.impl.PromptRoundTripVesselPermissionConstraintCheckerFactory;
//import com.mmxlabs.scheduler.optimiser.constraints.impl.RoundTripVesselPermissionConstraintCheckerFactory;
//
//@SuppressWarnings("unused")
//@RunWith(value = ShiroRunner.class)
//public class LightWeightSchedulerTests extends AbstractMicroTestCase {
//	@Override
//	protected @NonNull ExecutorService createExecutorService() {
//		return Executors.newFixedThreadPool(4);
//	}
//
//	private static List<String> requiredFeatures = Lists.newArrayList("no-nominal-in-prompt", "optimisation-actionset");
//	private static List<String> addedFeatures = new LinkedList<>();
//
//	@BeforeClass
//	public static void hookIn() {
//		for (final String feature : requiredFeatures) {
//			if (!LicenseFeatures.isPermitted("features:" + feature)) {
//				LicenseFeatures.addFeatureEnablements(feature);
//				addedFeatures.add(feature);
//			}
//		}
//	}
//
//	@AfterClass
//	public static void hookOut() {
//		for (final String feature : addedFeatures) {
//			LicenseFeatures.removeFeatureEnablements(feature);
//		}
//		addedFeatures.clear();
//	}
//
//	@Before
//	@Override
//	public void constructor() throws Exception {
//
//		super.constructor();
//		// Set a default prompt in the past
//		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2014, 1, 1), LocalDate.of(2018, 3, 1));
//	}
//
//	@Test
//	@Category({ MicroTest.class })
//	public void testInsertShippedPair_Open() throws Exception {
//
//		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
//		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();
//
//		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
//		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 0);
//
//		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("Bonny", LocalDate.of(2016, 1, 1), portFinder.findPort("Darwin LNG"), null, entity, "5", 22.8).withWindowSize(1, TimePeriod.MONTHS).build();
//		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 2, 1), portFinder.findPort("Barcelona LNG"), null, entity, "7").withWindowSize(1, TimePeriod.MONTHS).build();
//
//		// Create cargo 1, cargo 2
//		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
//				.makeFOBPurchase("L1", LocalDate.of(2016, 3, 1), portFinder.findPort("Hammerfest LNG"), null, entity, "5") //
//				.withWindowSize(1, TimePeriod.MONTHS)
//				.build() //
//				.makeDESSale("D1", LocalDate.of(2016, 4, 1), portFinder.findPort("Incheon"), null, entity, "7") //
//				.withWindowSize(1, TimePeriod.MONTHS)
//				.build() //
//				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
//				.withAssignmentFlags(true, false) //
//				.build();
//
//		evaluateWithLSOTest(true, (plan) -> {
//			// Clear default stages so we can run our own stuff here.
//			plan.getStages().clear();
//		}, null, scenarioRunner -> {
//			final LightWeightSchedulerOptimiserUnit lightWeightSchedulerOptimiserUnit = getOptimiser(scenarioRunner);
//
//			final IMultiStateResult result1 = lightWeightSchedulerOptimiserUnit.run(new NullProgressMonitor());
//			Assert.assertNotNull(result1);
//			Assert.assertTrue(result1.getSolutions().size() == 2);
//			assert result1.getSolutions().get(1).getFirst().getUnusedElements().isEmpty();
//
//			final IMultiStateResult result2 = lightWeightSchedulerOptimiserUnit.run(new NullProgressMonitor());
//			Assert.assertNotNull(result2);
//			Assert.assertTrue(result2.getSolutions().size() == 2);
//			assert result2.getSolutions().get(1).getFirst().getUnusedElements().isEmpty();
//		}, null);
//	}
//
//	private LightWeightSchedulerOptimiserUnit getOptimiser(LNGScenarioRunner scenarioRunner) {
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
//		final LightWeightSchedulerOptimiserUnit slotInserter = new LightWeightSchedulerOptimiserUnit(dataTransformer, "pairing-stage", dataTransformer.getUserSettings(), constraintAndFitnessSettings,
//				scenarioRunner.getExecutorService(), dataTransformer.getInitialSequences(), scenarioRunner.getScenarioDataProvider(), dataTransformer.getInitialResult(), Collections.emptyList());
//		return slotInserter;
//	}
//
//}