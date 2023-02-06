/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.verifier.OptimiserResultVerifier;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.InsertionOptimisationStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.editor.util.UserSettingsHelper;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.analytics.OptioniserUnit;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.insertion.OptioniserLogger;

@SuppressWarnings("unused")
@ExtendWith(ShiroRunner.class)
public class OptioniserTests extends AbstractMicroTestCase {
	@Override
	protected int getThreadCount() {
		return 4;
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testInsertDESPurchase_OpenDESSale() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final LoadSlot load_DES1 = cargoModelBuilder
				.makeDESPurchase("DES_Purchase", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "5", 22.8, null)
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final OptioniserUnit slotInserter = getOptioniserUnit(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(load_DES1), Collections.emptyList(), 10, new OptioniserLogger(), new NullProgressMonitor());
			Assertions.assertNotNull(result1);
			Assertions.assertTrue(result1.getSolutions().size() == 2);
			assert result1.getSolutions().get(1).getFirst().getUnusedElements().isEmpty();

			final IMultiStateResult result2 = slotInserter.run(Collections.singletonList(discharge_DES1), Collections.emptyList(), 10, new OptioniserLogger(), new NullProgressMonitor());
			Assertions.assertNotNull(result2);
			Assertions.assertTrue(result2.getSolutions().size() == 2);
			assert result2.getSolutions().get(1).getFirst().getUnusedElements().isEmpty();
		}, null);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDESPurchase_SwapDESSale() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final LoadSlot load_DES1 = cargoModelBuilder
				.makeDESPurchase("DES_Purchase", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "5", 22.8, null)
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7")//
				.withOptional(true) //
				.build();

		@NonNull
		final Cargo cargo1 = cargoModelBuilder.createCargo(load_DES1, discharge_DES1);
		cargo1.setAllowRewiring(true);

		final DischargeSlot discharge_DES2 = cargoModelBuilder.makeDESSale("DES_Sale2", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7")//
				.withOptional(true)//
				.build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final OptioniserUnit slotInserter = getOptioniserUnit(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(discharge_DES2), Collections.emptyList(), 10, new OptioniserLogger(), new NullProgressMonitor());
			Assertions.assertNotNull(result1);
			Assertions.assertTrue(result1.getSolutions().size() == 2);
			assert result1.getSolutions().get(1).getFirst().getUnusedElements().size() == 1;

			Assertions.assertTrue(result1.getSolutions().get(1).getFirst().getUnusedElements().get(0).getName().contains("DES_Sale1"));

		}, null);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDESPurchase_SwapDESPurchase() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final LoadSlot load_DES1 = cargoModelBuilder
				.makeDESPurchase("DES_Purchase1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "5", 22.8, null)//
				.withOptional(true) //
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7")//
				.withOptional(true) //
				.build();

		@NonNull
		final Cargo cargo1 = cargoModelBuilder.createCargo(load_DES1, discharge_DES1);
		cargo1.setAllowRewiring(true);

		final LoadSlot load_DES2 = cargoModelBuilder
				.makeDESPurchase("DES_Purchase2", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "5", 22.8, null) //
				.withOptional(true)//
				.build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final OptioniserUnit slotInserter = getOptioniserUnit(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(load_DES2), Collections.emptyList(), 10, new OptioniserLogger(), new NullProgressMonitor());
			Assertions.assertNotNull(result1);

			final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(scenarioRunner) //
					.withAnySolutionResultChecker() //
					.withCargo("DES_Purchase2", "DES_Sale1")
					.isNonShipped() //
					.withUnusedSlot("DES_Purchase1") //
					.build();

			final ISequences solution = verifier.verifySolutionExistsInResults(result1, Assertions::fail);
			Assertions.assertNotNull(solution);

		}, null);
	}

	private OptioniserUnit getOptioniserUnit(final LNGScenarioRunner scenarioRunner) {
		@NonNull
		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
		@NonNull
		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

		final InsertionOptimisationStage stage = ScenarioUtils.createDefaultInsertionSettings();

		final OptioniserUnit slotInserter = new OptioniserUnit(dataTransformer, "pairing-stage", dataTransformer.getUserSettings(), stage,
				scenarioRunner.getJobExecutorFactory(), dataTransformer.getInitialSequences(), dataTransformer.getInitialResult(), Collections.emptyList());
		return slotInserter;
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testInsertFOBSale_OpenFOBPurchase() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)
				.build();
		final DischargeSlot discharge_FOB1 = cargoModelBuilder
				.makeFOBSale("FOB_Sale", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7", null)
				.build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final OptioniserUnit slotInserter = getOptioniserUnit(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(load_FOB1), Collections.emptyList(), 10, new OptioniserLogger(), new NullProgressMonitor());
			Assertions.assertNotNull(result1);
			Assertions.assertTrue(result1.getSolutions().size() == 2);
			assert result1.getSolutions().get(1).getFirst().getUnusedElements().isEmpty();

			final IMultiStateResult result2 = slotInserter.run(Collections.singletonList(discharge_FOB1), Collections.emptyList(), 10, new OptioniserLogger(), new NullProgressMonitor());
			Assertions.assertNotNull(result2);
			Assertions.assertTrue(result2.getSolutions().size() == 2);
			assert result2.getSolutions().get(1).getFirst().getUnusedElements().isEmpty();

		}, null);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSwaptFOBSale_FOBPurchase() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)//
				.withOptional(true)//
				.build();
		final DischargeSlot discharge_FOB1 = cargoModelBuilder
				.makeFOBSale("FOB_Sale", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7", null) //
				.build();

		@NonNull
		final Cargo cargo1 = cargoModelBuilder.createCargo(load_FOB1, discharge_FOB1);
		cargo1.setAllowRewiring(true);

		final LoadSlot load_FOB2 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase2", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)//
				.withOptional(true)//
				.build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final OptioniserUnit slotInserter = getOptioniserUnit(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(load_FOB2), Collections.emptyList(), 10, new OptioniserLogger(), new NullProgressMonitor());
			Assertions.assertNotNull(result1);

			Assertions.assertTrue(result1.getSolutions().size() == 2);
			assert result1.getSolutions().get(1).getFirst().getUnusedElements().size() == 1;

			Assertions.assertTrue(result1.getSolutions().get(1).getFirst().getUnusedElements().get(0).getName().contains("FOB_Purchase1"));

		}, null);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSwaptFOBSale_FOBSale() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)//
				.withOptional(true)//
				.build();
		final DischargeSlot discharge_FOB1 = cargoModelBuilder
				.makeFOBSale("FOB_Sale1", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7", null) //
				.withOptional(true)//
				.build();

		@NonNull
		final Cargo cargo1 = cargoModelBuilder.createCargo(load_FOB1, discharge_FOB1);
		cargo1.setAllowRewiring(true);

		final DischargeSlot discharge_FOB2 = cargoModelBuilder
				.makeFOBSale("FOB_Sale2", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7", null) //
				.withOptional(true)//
				.build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final OptioniserUnit slotInserter = getOptioniserUnit(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(discharge_FOB2), Collections.emptyList(), 10, new OptioniserLogger(), new NullProgressMonitor());
			Assertions.assertNotNull(result1);

			final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(scenarioRunner) //
					.withAnySolutionResultChecker() //
					.withCargo("FOB_Purchase1", "FOB_Sale2")
					.isNonShipped() //
					.withUnusedSlot("FOB_Sale1") //
					.build();

			final ISequences solution = verifier.verifySolutionExistsInResults(result1, Assertions::fail);
			Assertions.assertNotNull(solution);
		}, null);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testInsertShippedPair_Open() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final OptioniserUnit slotInserter = getOptioniserUnit(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(load_FOB1), Collections.emptyList(), 10, new OptioniserLogger(), new NullProgressMonitor());
			Assertions.assertNotNull(result1);
			Assertions.assertTrue(result1.getSolutions().size() == 2);
			assert result1.getSolutions().get(1).getFirst().getUnusedElements().isEmpty();

			final IMultiStateResult result2 = slotInserter.run(Collections.singletonList(discharge_DES1), Collections.emptyList(), 10, new OptioniserLogger(), new NullProgressMonitor());
			Assertions.assertNotNull(result2);
			Assertions.assertTrue(result2.getSolutions().size() == 2);
			assert result2.getSolutions().get(1).getFirst().getUnusedElements().isEmpty();

		}, null);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSwapShippedPair_FOBPurchase() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.withOptional(true) //
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.withOptional(true) //
				.build();

		@NonNull
		final Cargo cargo1 = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo1.setAllowRewiring(true);
		cargo1.setVesselAssignmentType(vesselCharter);

		final LoadSlot load_FOB2 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase2", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.withOptional(true) //
				.build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final OptioniserUnit slotInserter = getOptioniserUnit(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(load_FOB2), Collections.emptyList(), 10, new OptioniserLogger(), new NullProgressMonitor());
			Assertions.assertNotNull(result1);

			Assertions.assertEquals(2, result1.getSolutions().size());
			assert result1.getSolutions().get(1).getFirst().getUnusedElements().size() == 1;

			Assertions.assertTrue(result1.getSolutions().get(1).getFirst().getUnusedElements().get(0).getName().contains("FOB_Purchase1"));

		}, null);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSwapShippedPair_DESSale() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.withOptional(true) //
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale1", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.withOptional(true) //
				.build();

		@NonNull
		final Cargo cargo1 = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo1.setAllowRewiring(true);
		cargo1.setVesselAssignmentType(vesselCharter);

		final DischargeSlot discharge_DES2 = cargoModelBuilder.makeDESSale("DES_Sale2", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.withOptional(true) //
				.build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final OptioniserUnit slotInserter = getOptioniserUnit(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(discharge_DES2), Collections.emptyList(), 10, new OptioniserLogger(), new NullProgressMonitor());
			Assertions.assertNotNull(result1);

			Assertions.assertEquals(2, result1.getSolutions().size());
			assert result1.getSolutions().get(1).getFirst().getUnusedElements().size() == 1;

			Assertions.assertTrue(result1.getSolutions().get(1).getFirst().getUnusedElements().get(0).getName().contains("DES_Sale1"));

		}, null);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testInsertExtraShippedPair() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel source = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel1 = fleetModelBuilder.createVesselFrom("vessel1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("vessel2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel1, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final VesselCharter vesselCharter2 = cargoModelBuilder.makeVesselCharter(vessel2, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale1", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.build();

		@NonNull
		final Cargo cargo1 = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo1.setAllowRewiring(true);
		cargo1.setVesselAssignmentType(vesselCharter1);

		final LoadSlot load_FOB2 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase2", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.withOptional(true)//
				.build();

		// Forbid Chita LNG as destination to force a re-wire insertion
		load_FOB2.setRestrictedPortsOverride(true);
		load_FOB2.getRestrictedPorts().add(portFinder.findPortById(InternalDataConstants.PORT_CHITA));

		final DischargeSlot discharge_DES2 = cargoModelBuilder.makeDESSale("DES_Sale2", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "7") //
				.withOptional(true)//
				.build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final OptioniserUnit slotInserter = getOptioniserUnit(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(load_FOB2), Collections.emptyList(), 20, new OptioniserLogger(), new NullProgressMonitor());
			Assertions.assertNotNull(result1);

			final AtomicBoolean bool = new AtomicBoolean(false);
			Assertions.assertTrue(result1.getSolutions().size() > 1);
			result1.getSolutions().stream().map(p -> p.getFirst()).forEach(seq -> {
				if (result1.getSolutions().get(1).getFirst().getUnusedElements().isEmpty()) {
					bool.set(true);
				}
			});
			Assertions.assertTrue(bool.get());

		}, null);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDivertBackfill() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0, 0))//
				.withEndWindow(LocalDateTime.of(2016, 2, 6, 0, 0, 0, 0))//
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale1", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.build();

		@NonNull
		final Cargo cargo1 = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo1.setAllowRewiring(true);
		cargo1.setVesselAssignmentType(vesselCharter);
		cargo1.setSequenceHint(1);

		final LoadSlot load_DES = cargoModelBuilder
				.makeDESPurchase("DES_Purchase_1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "5", 22.8, null) //
				.build();

		// This can only be shipped
		final DischargeSlot discharge_DES2 = cargoModelBuilder.makeDESSale("DES_Sale2", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.with(s -> ((DischargeSlot) s).setPurchaseDeliveryType(CargoDeliveryType.SHIPPED)) //
				.build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final OptioniserUnit slotInserter = getOptioniserUnit(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(load_DES), Collections.emptyList(), 10, new OptioniserLogger(), new NullProgressMonitor());
			Assertions.assertNotNull(result1);

			final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(scenarioRunner) //
					.withAnySolutionResultChecker() //
					.withCargo("FOB_Purchase1", "DES_Sale2")
					.onFleetVessel(vessel.getName()) //
					.withCargo("DES_Purchase_1", "DES_Sale1")
					.isNonShipped() //
					.build();

			final ISequences solution = verifier.verifySolutionExistsInResults(result1, Assertions::fail);
			Assertions.assertNotNull(solution);
		}, null);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testInsertTrickyCargo() throws Exception {

		// Model the case Alex found.
		// Insert new DES Sale(?). Could swap with optional DES Sale, but dates clash with next load. Cannot do this.
		// Need to remove existing cargo and then re-insert on market vessel.
		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final Vessel source = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel1 = fleetModelBuilder.createVesselFrom("vessel1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("vessel2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel1, entity) //
				.build();

		final VesselCharter vesselCharter2 = cargoModelBuilder.makeVesselCharter(vessel2, entity) //
				.build();

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.build();
		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale1", LocalDate.of(2016, 1, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.build();

		@NonNull
		final Cargo cargo1 = cargoModelBuilder.createCargo(load_FOB1, discharge_DES1);
		cargo1.setAllowRewiring(false);
		cargo1.setVesselAssignmentType(vesselCharter1);
		cargo1.setSequenceHint(1);

		final LoadSlot load_FOB2 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase2", LocalDate.of(2016, 2, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.build();

		final DischargeSlot discharge_DES2 = cargoModelBuilder.makeDESSale("DES_Sale2", LocalDate.of(2016, 3, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.withOptional(true)//

				.build();

		@NonNull
		final Cargo cargo2 = cargoModelBuilder.createCargo(load_FOB2, discharge_DES2);
		cargo2.setAllowRewiring(true);
		cargo2.setVesselAssignmentType(vesselCharter1);
		cargo2.setSequenceHint(2);

		final LoadSlot load_FOB3 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase3", LocalDate.of(2016, 4, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8) //
				.build();
		final DischargeSlot discharge_DES3 = cargoModelBuilder.makeDESSale("DES_Sale3", LocalDate.of(2016, 5, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.build();

		@NonNull
		final Cargo cargo3 = cargoModelBuilder.createCargo(load_FOB3, discharge_DES3);
		cargo3.setAllowRewiring(false);
		cargo3.setVesselAssignmentType(vesselCharter1);
		cargo3.setSequenceHint(3);

		final DischargeSlot discharge_DES4 = cargoModelBuilder.makeDESSale("DES_Sale4", LocalDate.of(2016, 4, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.build();

		evaluateWithLSOTest(true, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final OptioniserUnit slotInserter = getOptioniserUnit(scenarioRunner);

			final IMultiStateResult results = slotInserter.run(Collections.singletonList(discharge_DES4), Collections.emptyList(), 10, new OptioniserLogger(), new NullProgressMonitor());

			Assertions.assertTrue(results.getSolutions().size() > 1);
			// Next validate solution types exist
			final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(scenarioRunner) //
					.withAnySolutionResultChecker() //
					.withCargo("FOB_Purchase2", "DES_Sale4")
					.any() //
					.withUnusedSlot("DES_Sale2") // Main sure we have a solution with this slot unused.
					.build();

			verifier.verifySolutionExistsInResults(results, msg -> Assertions.fail(msg));
		}, null);

	}

	/**
	 * Test that we can send multiple loads to the same market/month.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testMultipleSpotMarketInstances() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final LoadSlot load_FOB1 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase1", LocalDate.of(2022, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)
				.build();
		final LoadSlot load_FOB2 = cargoModelBuilder
				.makeFOBPurchase("FOB_Purchase2", LocalDate.of(2022, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8)
				.build();

		spotMarketsModelBuilder.makeDESSaleMarket("SaleMarket", portFinder.findPortById(InternalDataConstants.PORT_SAKAI), entity, "10") //
				.withEnabled(true)
				.withAvailabilityConstant(4) //
				.build();

		CharterInMarket createCharterInMarket = 
				spotMarketsModelBuilder.createCharterInMarket("SpotVessels", fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_160), entity, "100000", 2);
		
		createCharterInMarket.setEnabled(true);
		
		final UserSettings userSettings = UserSettingsHelper.createDefaultUserSettings();
		userSettings.setWithSpotCargoMarkets(true);
		
		evaluateWithLSOTest(true, userSettings, (plan) -> {
			// Clear default stages so we can run our own stuff here.
			plan.getStages().clear();
		}, null, scenarioRunner -> {
			final OptioniserUnit slotInserter = getOptioniserUnit(scenarioRunner);

			final IMultiStateResult result1 = slotInserter.run(Lists.newArrayList(load_FOB1, load_FOB2), Collections.emptyList(), 10, new OptioniserLogger(), new NullProgressMonitor());
			Assertions.assertNotNull(result1);
//			Assertions.assertTrue(result1.getSolutions().size() == 2);
//			assert result1.getSolutions().get(1).getFirst().getUnusedElements().isEmpty();
//			
//			final SlotInsertionOptimiserUnit slotInserter = getSlotInserter(scenarioRunner);
//
//			final IMultiStateResult result1 = slotInserter.run(Collections.singletonList(discharge_FOB2), Collections.emptyList(), 10, new SlotInsertionOptimiserLogger(), new NullProgressMonitor());
//			Assertions.assertNotNull(result1);

			final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(scenarioRunner) //
					.withAnySolutionResultChecker() //
					.withCargoToMarket("FOB_Purchase1", "SaleMarket")
					.anySpotCharterVessel() //
					.withCargoToMarket("FOB_Purchase2", "SaleMarket")
					.anySpotCharterVessel() //
					.build();

			final ISequences solution = verifier.verifySolutionExistsInResults(result1, Assertions::fail);
			Assertions.assertNotNull(solution);

		}, null);
	}
}