/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.analytics.SlotInsertionOptimiserUnit;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LadenLegLimitConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PromptRoundTripVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.RoundTripVesselPermissionConstraintCheckerFactory;

@SuppressWarnings("unused")
@RunWith(value = ShiroRunner.class)
public class SlotInsertionTests extends AbstractMicroTestCase {
	@Override
	protected @NonNull ExecutorService createExecutorService() {
		return Executors.newFixedThreadPool(4);
	}

	@Test
	@Category({ MicroTest.class })
	public void testInsertDESPurchase_OpenDESSale() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final LoadSlot load_DES1 = cargoModelBuilder.makeDESPurchase("DES_Purchase", false, LocalDate.of(2015, 12, 5), portFinder.findPort("Sakai"), null, entity, "5", 22.8, null).build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2015, 12, 5), portFinder.findPort("Sakai"), null, entity, "7").build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final SlotInsertionOptimiserUnit slotInserter = getSlotInserter(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(load_DES1), 1, new NullProgressMonitor());
			Assert.assertNotNull(result1);
			Assert.assertTrue(result1.getSolutions().size() == 2);
			assert result1.getSolutions().get(1).getFirst().getUnusedElements().isEmpty();

			final IMultiStateResult result2 = slotInserter.run(Collections.singletonList(discharge_DES1), 1, new NullProgressMonitor());
			Assert.assertNotNull(result2);
			Assert.assertTrue(result2.getSolutions().size() == 2);
			assert result2.getSolutions().get(1).getFirst().getUnusedElements().isEmpty();
		}, null);
	}

	@Test
	@Category({ MicroTest.class })
	public void testDESPurchase_SwapDESSale() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final LoadSlot load_DES1 = cargoModelBuilder.makeDESPurchase("DES_Purchase", false, LocalDate.of(2015, 12, 5), portFinder.findPort("Sakai"), null, entity, "5", 22.8, null).build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale1", LocalDate.of(2015, 12, 5), portFinder.findPort("Sakai"), null, entity, "7")//
				.withOptional(true) //
				.build();

		@NonNull
		Cargo cargo1 = cargoModelBuilder.createCargo(load_DES1, discharge_DES1);
		cargo1.setAllowRewiring(true);

		final DischargeSlot discharge_DES2 = cargoModelBuilder.makeDESSale("DES_Sale2", LocalDate.of(2015, 12, 5), portFinder.findPort("Sakai"), null, entity, "7")//
				.withOptional(true)//
				.build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final SlotInsertionOptimiserUnit slotInserter = getSlotInserter(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(discharge_DES2), 1, new NullProgressMonitor());
			Assert.assertNotNull(result1);
			Assert.assertTrue(result1.getSolutions().size() == 2);
			assert result1.getSolutions().get(1).getFirst().getUnusedElements().size() == 1;

			Assert.assertTrue(result1.getSolutions().get(1).getFirst().getUnusedElements().get(0).getName().contains("DES_Sale1"));

		}, null);
	}

	@Test
	@Category({ MicroTest.class })
	public void testDESPurchase_SwapDESPurchase() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final LoadSlot load_DES1 = cargoModelBuilder.makeDESPurchase("DES_Purchase1", false, LocalDate.of(2015, 12, 5), portFinder.findPort("Sakai"), null, entity, "5", 22.8, null)//
				.withOptional(true) //
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale1", LocalDate.of(2015, 12, 5), portFinder.findPort("Sakai"), null, entity, "7")//
				.withOptional(true) //
				.build();

		@NonNull
		Cargo cargo1 = cargoModelBuilder.createCargo(load_DES1, discharge_DES1);
		cargo1.setAllowRewiring(true);

		final LoadSlot load_DES2 = cargoModelBuilder.makeDESPurchase("DES_Purchase2", false, LocalDate.of(2015, 12, 5), portFinder.findPort("Sakai"), null, entity, "5", 22.8, null) //
				.withOptional(true)//
				.build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final SlotInsertionOptimiserUnit slotInserter = getSlotInserter(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(load_DES2), 1, new NullProgressMonitor());
			Assert.assertNotNull(result1);
			Assert.assertTrue(result1.getSolutions().size() == 2);
			assert result1.getSolutions().get(1).getFirst().getUnusedElements().size() == 1;

			Assert.assertTrue(result1.getSolutions().get(1).getFirst().getUnusedElements().get(0).getName().contains("DES_Purchase1"));

		}, null);
	}

	private SlotInsertionOptimiserUnit getSlotInserter(LNGScenarioRunner scenarioRunner) {
		@NonNull
		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
		@NonNull
		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

		final ConstraintAndFitnessSettings constraintAndFitnessSettings = ScenarioUtils.createDefaultConstraintAndFitnessSettings();
		// TODO: Filter
		final Iterator<Constraint> iterator = constraintAndFitnessSettings.getConstraints().iterator();
		while (iterator.hasNext()) {
			final Constraint constraint = iterator.next();
			if (constraint.getName().equals(PromptRoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
				iterator.remove();
			}
			if (constraint.getName().equals(RoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
				iterator.remove();
			}
		}

		ScenarioUtils.createOrUpdateContraints(LadenLegLimitConstraintCheckerFactory.NAME, true, constraintAndFitnessSettings);

		final SlotInsertionOptimiserUnit slotInserter = new SlotInsertionOptimiserUnit(dataTransformer, "pairing-stage", dataTransformer.getUserSettings(), constraintAndFitnessSettings,
				scenarioRunner.getExecutorService(), dataTransformer.getInitialSequences(), dataTransformer.getInitialResult(), Collections.emptyList());
		return slotInserter;
	}

	@Test
	@Category({ MicroTest.class })
	public void testInsertFOBSale_OpenFOBPurchase() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8).build();
		final DischargeSlot discharge_FOB1 = cargoModelBuilder.makeFOBSale("FOB_Sale", false, LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "7", null).build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final SlotInsertionOptimiserUnit slotInserter = getSlotInserter(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(load_FOB1), 1, new NullProgressMonitor());
			Assert.assertNotNull(result1);
			Assert.assertTrue(result1.getSolutions().size() == 2);
			assert result1.getSolutions().get(1).getFirst().getUnusedElements().isEmpty();

			final IMultiStateResult result2 = slotInserter.run(Collections.singletonList(discharge_FOB1), 1, new NullProgressMonitor());
			Assert.assertNotNull(result2);
			Assert.assertTrue(result2.getSolutions().size() == 2);
			assert result2.getSolutions().get(1).getFirst().getUnusedElements().isEmpty();

		}, null);
	}

	@Test
	@Category({ MicroTest.class })
	public void testSwaptFOBSale_FOBPurchase() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8)//
				.withOptional(true)//
				.build();
		final DischargeSlot discharge_FOB1 = cargoModelBuilder.makeFOBSale("FOB_Sale", false, LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "7", null) //
				.build();

		@NonNull
		Cargo cargo1 = cargoModelBuilder.createCargo(load_FOB1, discharge_FOB1);
		cargo1.setAllowRewiring(true);

		final LoadSlot load_FOB2 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase2", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8)//
				.withOptional(true)//
				.build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final SlotInsertionOptimiserUnit slotInserter = getSlotInserter(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(load_FOB2), 1, new NullProgressMonitor());
			Assert.assertNotNull(result1);

			Assert.assertTrue(result1.getSolutions().size() == 2);
			assert result1.getSolutions().get(1).getFirst().getUnusedElements().size() == 1;

			Assert.assertTrue(result1.getSolutions().get(1).getFirst().getUnusedElements().get(0).getName().contains("FOB_Purchase1"));

		}, null);
	}

	@Test
	@Category({ MicroTest.class })
	public void testSwaptFOBSale_FOBSale() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8)//
				.withOptional(true)//
				.build();
		final DischargeSlot discharge_FOB1 = cargoModelBuilder.makeFOBSale("FOB_Sale1", false, LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "7", null) //
				.withOptional(true)//
				.build();

		@NonNull
		Cargo cargo1 = cargoModelBuilder.createCargo(load_FOB1, discharge_FOB1);
		cargo1.setAllowRewiring(true);

		final DischargeSlot discharge_FOB2 = cargoModelBuilder.makeFOBSale("FOB_Sale2", false, LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "7", null) //
				.withOptional(true)//
				.build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final SlotInsertionOptimiserUnit slotInserter = getSlotInserter(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(discharge_FOB2), 1, new NullProgressMonitor());
			Assert.assertNotNull(result1);

			Assert.assertTrue(result1.getSolutions().size() == 2);
			assert result1.getSolutions().get(1).getFirst().getUnusedElements().size() == 1;

			Assert.assertTrue(result1.getSolutions().get(1).getFirst().getUnusedElements().get(0).getName().contains("FOB_Sale1"));

		}, null);
	}

	@Test
	@Category({ MicroTest.class })
	public void testInsertShippedPair_Open() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8).build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7").build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final SlotInsertionOptimiserUnit slotInserter = getSlotInserter(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(load_FOB1), 1, new NullProgressMonitor());
			Assert.assertNotNull(result1);
			Assert.assertTrue(result1.getSolutions().size() == 2);
			assert result1.getSolutions().get(1).getFirst().getUnusedElements().isEmpty();

			final IMultiStateResult result2 = slotInserter.run(Collections.singletonList(discharge_DES1), 1, new NullProgressMonitor());
			Assert.assertNotNull(result2);
			Assert.assertTrue(result2.getSolutions().size() == 2);
			assert result2.getSolutions().get(1).getFirst().getUnusedElements().isEmpty();

		}, null);
	}

	@Test
	@Category({ MicroTest.class })
	public void testSwapShippedPair_FOBPurchase() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8) //
				.withOptional(true) //
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7") //
				.withOptional(true) //
				.build();

		@NonNull
		Cargo cargo1 = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo1.setAllowRewiring(true);

		final LoadSlot load_FOB2 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase2", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8) //
				.withOptional(true) //
				.build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final SlotInsertionOptimiserUnit slotInserter = getSlotInserter(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(load_FOB2), 1, new NullProgressMonitor());
			Assert.assertNotNull(result1);

			Assert.assertTrue(result1.getSolutions().size() == 2);
			assert result1.getSolutions().get(1).getFirst().getUnusedElements().size() == 1;

			Assert.assertTrue(result1.getSolutions().get(1).getFirst().getUnusedElements().get(0).getName().contains("FOB_Purchase1"));

		}, null);
	}

	@Test
	@Category({ MicroTest.class })
	public void testSwapShippedPair_DESSale() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8) //
				.withOptional(true) //
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale1", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7") //
				.withOptional(true) //
				.build();

		@NonNull
		Cargo cargo1 = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo1.setAllowRewiring(true);

		final DischargeSlot discharge_DES2 = cargoModelBuilder.makeDESSale("DES_Sale2", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7") //
				.withOptional(true) //
				.build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final SlotInsertionOptimiserUnit slotInserter = getSlotInserter(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(discharge_DES2), 1, new NullProgressMonitor());
			Assert.assertNotNull(result1);

			Assert.assertTrue(result1.getSolutions().size() == 2);
			assert result1.getSolutions().get(1).getFirst().getUnusedElements().size() == 1;

			Assert.assertTrue(result1.getSolutions().get(1).getFirst().getUnusedElements().get(0).getName().contains("DES_Sale1"));

		}, null);
	}

	@Test
	@Category({ MicroTest.class })
	public void testInsertExtraShippedPair() throws Exception {

		lngScenarioModel.getCargoModel().getVesselAvailabilities().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel1 = fleetModelBuilder.createVessel("vessel1", vesselClass);
		final Vessel vessel2 = fleetModelBuilder.createVessel("vessel2", vesselClass);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final VesselAvailability vesselAvailability2 = cargoModelBuilder.makeVesselAvailability(vessel2, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8) //
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale1", LocalDate.of(2016, 1, 5), portFinder.findPort("Sakai"), null, entity, "7") //
				.build();

		@NonNull
		Cargo cargo1 = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo1.setAllowRewiring(true);

		final LoadSlot load_FOB2 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase2", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", 22.8) //
				.withOptional(true)//
				.build();

		// Forbid Chita LNG as destination to force a re-wire insertion
		load_FOB2.setOverrideRestrictions(true);
		load_FOB2.getRestrictedPorts().add(portFinder.findPort("Chita LNG"));

		final DischargeSlot discharge_DES2 = cargoModelBuilder.makeDESSale("DES_Sale2", LocalDate.of(2016, 1, 5), portFinder.findPort("Chita LNG"), null, entity, "7") //
				.withOptional(true)//
				.build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final SlotInsertionOptimiserUnit slotInserter = getSlotInserter(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(load_FOB2), 5, new NullProgressMonitor());
			Assert.assertNotNull(result1);

			final AtomicBoolean bool = new AtomicBoolean(false);
			Assert.assertTrue(result1.getSolutions().size() > 1);
			result1.getSolutions().parallelStream().map(p -> p.getFirst()).forEach(seq -> {
				if (result1.getSolutions().get(1).getFirst().getUnusedElements().isEmpty()) {
					bool.set(true);
				}
			});
			Assert.assertTrue(bool.get());

		}, null);
	}

}