/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.period;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.LumpSumRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.TimePeriod;

/**
 * Misc period transformer tests
 *
 */
public class VesselCharterTests extends AbstractMicroTestCase {

	/**
	 * Check repositioning is cleared as the first cargo should be removed and the start conditions of the charter should be updated.
	 */
	@Test
	public void testClearRemoveRepositioning() {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withRepositioning("5000") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2021, 10, 26), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2021, 11, 8), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2022, 1, 14), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2022, 1, 24), portFinder.findPortById(InternalDataConstants.PORT_HIMEJI), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2021, 4, 7));
		lngScenarioModel.setPromptPeriodEnd(LocalDate.of(2021, 7, 6));

		// Check pre-conditions
		Assertions.assertNull(vesselAvailability.getGenericCharterContract());
		Assertions.assertNotNull(vesselAvailability.getContainedCharterContract());
		Assertions.assertNotNull(vesselAvailability.getCharterOrDelegateCharterContract().getRepositioningFeeTerms());

		evaluateWithLSOTest(true, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStartDate(LocalDate.of(2022, 2, 1));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2022, 12));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			final CargoModel cargoModel = optimiserScenario.getCargoModel();
			// Expect first cargo will have been removed
			Assertions.assertEquals(1, cargoModel.getCargoes().size());

			final VesselAvailability periodVA = cargoModel.getVesselAvailabilities().get(0);

			Assertions.assertNull(periodVA.getGenericCharterContract());
			Assertions.assertNotNull(periodVA.getContainedCharterContract());
			Assertions.assertNull(periodVA.getCharterOrDelegateCharterContract().getRepositioningFeeTerms());

		}, null);
	}

	/**
	 * Check repositioning is cleared as the first cargo should be removed and the start conditions of the charter should be updated. Shared contract should be unaffected
	 */
	@Test
	public void testClearRemoveRepositioningSharedContract() {

		final LumpSumRepositioningFeeTerm term = CommercialFactory.eINSTANCE.createLumpSumRepositioningFeeTerm();
		term.setPriceExpression("5000");

		final SimpleRepositioningFeeContainer repositioningFee = CommercialFactory.eINSTANCE.createSimpleRepositioningFeeContainer();
		repositioningFee.getTerms().add(term);

		final GenericCharterContract gcc = CommercialFactory.eINSTANCE.createGenericCharterContract();
		gcc.setRepositioningFeeTerms(repositioningFee);

		commercialModelFinder.getCommercialModel().getCharterContracts().add(gcc);

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();
		vesselAvailability.setGenericCharterContract(gcc);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2021, 10, 26), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2021, 11, 8), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2022, 1, 14), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2022, 1, 24), portFinder.findPortById(InternalDataConstants.PORT_HIMEJI), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2021, 4, 7));
		lngScenarioModel.setPromptPeriodEnd(LocalDate.of(2021, 7, 6));

		// Check pre-conditions
		Assertions.assertNotNull(vesselAvailability.getCharterOrDelegateCharterContract().getRepositioningFeeTerms());

		evaluateWithLSOTest(true, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStartDate(LocalDate.of(2022, 2, 1));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2022, 12));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			final CargoModel cargoModel = optimiserScenario.getCargoModel();
			// Expect first cargo will have been removed
			Assertions.assertEquals(1, cargoModel.getCargoes().size());

			final VesselAvailability periodVA = cargoModel.getVesselAvailabilities().get(0);
			// Vessel should have no repositioning terms
			Assertions.assertNull(periodVA.getCharterOrDelegateCharterContract().getRepositioningFeeTerms());
			
			Assertions.assertNull(periodVA.getGenericCharterContract());
			Assertions.assertNotNull(periodVA.getContainedCharterContract());
			// The shared contract should still be ok
			Assertions.assertNotNull(optimiserScenario.getReferenceModel().getCommercialModel().getCharterContracts().get(0).getRepositioningFeeTerms());

		}, null);
	}

	/**
	 * Check repositioning is cleared as the first cargo should be removed and the start conditions of the charter should be updated.
	 */
	@Test
	public void testClearRemoveBallastBonus() {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		// Ballast bonus
		final GenericCharterContract gcc = commercialModelBuilder.createSimpleNotionalJourneyBallastBonusContract(Collections.emptySet(), 16.0, "35000", "200", false, false,
				Collections.singleton(portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS)));
		vesselAvailability.setCharterContractOverride(true);
		vesselAvailability.setContainedCharterContract(gcc);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2021, 10, 26), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2021, 11, 8), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2022, 1, 14), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2022, 1, 24), portFinder.findPortById(InternalDataConstants.PORT_HIMEJI), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2021, 4, 7));
		lngScenarioModel.setPromptPeriodEnd(LocalDate.of(2021, 7, 6));

		// Check pre-conditions
		Assertions.assertNotNull(vesselAvailability.getCharterOrDelegateCharterContract().getBallastBonusTerms());

		evaluateWithLSOTest(true, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStartDate(LocalDate.of(2021, 9, 1));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2021, 11));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			final CargoModel cargoModel = optimiserScenario.getCargoModel();
			// Expect second cargo will have been removed
			Assertions.assertEquals(1, cargoModel.getCargoes().size());

			final VesselAvailability periodVA = cargoModel.getVesselAvailabilities().get(0);

			Assertions.assertNull(periodVA.getGenericCharterContract());
			Assertions.assertNotNull(periodVA.getContainedCharterContract());
			Assertions.assertNull(periodVA.getCharterOrDelegateCharterContract().getBallastBonusTerms());

		}, null);
	}

	/**
	 * Check repositioning is cleared as the first cargo should be removed and the start conditions of the charter should be updated.
	 */
	@Test
	public void testClearRemoveBallastBonusSharedContract() {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		// Ballast bonus
		final GenericCharterContract gcc = commercialModelBuilder.createSimpleNotionalJourneyBallastBonusContract(Collections.emptySet(), 16.0, "35000", "200", false, false,
				Collections.singleton(portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS)));
		commercialModelFinder.getCommercialModel().getCharterContracts().add(gcc);
		vesselAvailability.setGenericCharterContract(gcc);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2021, 10, 26), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2021, 11, 8), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2022, 1, 14), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2022, 1, 24), portFinder.findPortById(InternalDataConstants.PORT_HIMEJI), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2021, 4, 7));
		lngScenarioModel.setPromptPeriodEnd(LocalDate.of(2021, 7, 6));

		// Check pre-conditions
		Assertions.assertNotNull(vesselAvailability.getCharterOrDelegateCharterContract().getBallastBonusTerms());

		evaluateWithLSOTest(true, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStartDate(LocalDate.of(2021, 9, 1));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2021, 11));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			final CargoModel cargoModel = optimiserScenario.getCargoModel();
			// Expect second cargo will have been removed
			Assertions.assertEquals(1, cargoModel.getCargoes().size());

			final VesselAvailability periodVA = cargoModel.getVesselAvailabilities().get(0);

			Assertions.assertNull(periodVA.getCharterOrDelegateCharterContract().getBallastBonusTerms());
			
			Assertions.assertNull(periodVA.getGenericCharterContract());
			Assertions.assertNotNull(periodVA.getContainedCharterContract());
			// The shared contract should still be ok
			Assertions.assertNotNull(optimiserScenario.getReferenceModel().getCommercialModel().getCharterContracts().get(0).getBallastBonusTerms());
		}, null);
	}

	/**
	 * 
	 */
	@Test
	public void testMinMaxDurationUpdatedBeforePeriod() {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withMinDuration(100) //
				.withMaxDuration(300) //

				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2021, 10, 1), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2021, 11, 1), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2022, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2022, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_HIMEJI), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2021, 4, 7));
		lngScenarioModel.setPromptPeriodEnd(LocalDate.of(2021, 7, 6));

		evaluateWithLSOTest(true, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStartDate(LocalDate.of(2022, 2, 1));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2022, 12));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			final CargoModel cargoModel = optimiserScenario.getCargoModel();
			// Expect second cargo will have been removed
			Assertions.assertEquals(1, cargoModel.getCargoes().size());

			final VesselAvailability periodVA = cargoModel.getVesselAvailabilities().get(0);

			// Loose first cargo (so reduce min duration by 31 days Oct, 30 Nov & 31 Dec)
			Assertions.assertEquals(100 - 92, periodVA.getMinDuration());
			Assertions.assertEquals(100 - 92, periodVA.getCharterOrDelegateMinDuration());
			Assertions.assertEquals(300 - 92, periodVA.getMaxDuration());
			Assertions.assertEquals(300 - 92, periodVA.getCharterOrDelegateMaxDuration());
			
			Assertions.assertNull(periodVA.getGenericCharterContract());
			Assertions.assertNull(periodVA.getContainedCharterContract());
		}, null);
	}

	/**
	 * 
	 */
	@Test
	public void testMinMaxDurationUpdatedBeforePeriodSharedContract() {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //

				.build();

		// Ballast bonus
		final GenericCharterContract gcc = CommercialFactory.eINSTANCE.createGenericCharterContract();
		commercialModelFinder.getCommercialModel().getCharterContracts().add(gcc);
		gcc.setMinDuration(100);
		gcc.setMaxDuration(300);
		vesselAvailability.setGenericCharterContract(gcc);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2021, 10, 1), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2021, 11, 1), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2022, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2022, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_HIMEJI), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2021, 4, 7));
		lngScenarioModel.setPromptPeriodEnd(LocalDate.of(2021, 7, 6));

		evaluateWithLSOTest(true, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStartDate(LocalDate.of(2022, 2, 1));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2022, 12));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			final CargoModel cargoModel = optimiserScenario.getCargoModel();
			// Expect second cargo will have been removed
			Assertions.assertEquals(1, cargoModel.getCargoes().size());

			final VesselAvailability periodVA = cargoModel.getVesselAvailabilities().get(0);

			
			Assertions.assertNull(periodVA.getGenericCharterContract());
			Assertions.assertNotNull(periodVA.getContainedCharterContract());
			
			// Loose first cargo (so reduce min duration by 31 days Oct, 30 Nov & 31 Dec)
			Assertions.assertEquals(100 - 92, periodVA.getCharterOrDelegateMinDuration());
			Assertions.assertEquals(100 - 92, periodVA.getMinDuration());
			Assertions.assertEquals(300 - 92, periodVA.getCharterOrDelegateMaxDuration());
			Assertions.assertEquals(300 - 92, periodVA.getMaxDuration());
		}, null);
	}

	/**
	 * This test is for the min duration being updated for the lost orphan ballast (the time between vessel start and first cargo).
	 * 
	 * TODO: Current period transformer has a bug where this is not taken into account. This test should fail and needs to be updated when the bug is fixed.
	 */
	@Test
	public void testMinMaxDurationUpdatedBeforePeriodIncludeOrphanBallast() {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withMinDuration(100) //
				.withMaxDuration(300) //
				.withStartWindow(LocalDateTime.of(2021, 9, 28, 0, 0, 0)) //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2021, 10, 1), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2021, 11, 1), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2022, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2022, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_HIMEJI), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2021, 4, 7));
		lngScenarioModel.setPromptPeriodEnd(LocalDate.of(2021, 7, 6));

		evaluateWithLSOTest(true, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStartDate(LocalDate.of(2022, 2, 1));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2022, 12));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			final CargoModel cargoModel = optimiserScenario.getCargoModel();
			// Expect second cargo will have been removed
			Assertions.assertEquals(1, cargoModel.getCargoes().size());

			final VesselAvailability periodVA = cargoModel.getVesselAvailabilities().get(0);

			// Loose first cargo (so reduce min duration by 31 days Oct, 30 Nov & 31 Dec )
			int cargoTime = 92;
			// Reduce orphan leg (currently a bug in the period transformer ignores this. Expect it to be 2 or 3 days)
			int orphanLegTime = 0;
			Assertions.assertEquals(100 - cargoTime - orphanLegTime, periodVA.getMinDuration());
			Assertions.assertEquals(100 - cargoTime - orphanLegTime, periodVA.getCharterOrDelegateMinDuration());
			Assertions.assertEquals(300 - cargoTime - orphanLegTime, periodVA.getMaxDuration());
			Assertions.assertEquals(300 - cargoTime - orphanLegTime, periodVA.getCharterOrDelegateMaxDuration());

			Assertions.assertNull(periodVA.getGenericCharterContract());
			Assertions.assertNull(periodVA.getContainedCharterContract());
		}, null);
	}

	
	/**
	 * This test is for the min duration being updated for the lost orphan ballast (the time between vessel start and first cargo).
	 * 
	 * TODO: Current period transformer has a bug where this is not taken into account. This test should fail and needs to be updated when the bug is fixed.
	 */
	@Test
	public void testMinMaxDurationUpdatedBeforePeriodIncludeOrphanBallastSharedContract() {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2021, 9, 28, 0, 0, 0)) //
				.build();
		
		final GenericCharterContract gcc = CommercialFactory.eINSTANCE.createGenericCharterContract();
		commercialModelFinder.getCommercialModel().getCharterContracts().add(gcc);
		gcc.setMinDuration(100);
		gcc.setMaxDuration(300);
		vesselAvailability.setGenericCharterContract(gcc);
		

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2021, 10, 1), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2021, 11, 1), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2022, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2022, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_HIMEJI), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2021, 4, 7));
		lngScenarioModel.setPromptPeriodEnd(LocalDate.of(2021, 7, 6));

		evaluateWithLSOTest(true, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStartDate(LocalDate.of(2022, 2, 1));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2022, 12));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			final CargoModel cargoModel = optimiserScenario.getCargoModel();
			// Expect second cargo will have been removed
			Assertions.assertEquals(1, cargoModel.getCargoes().size());

			final VesselAvailability periodVA = cargoModel.getVesselAvailabilities().get(0);

			// Loose first cargo (so reduce min duration by 31 days Oct, 30 Nov & 31 Dec )
			int cargoTime = 92;
			// Reduce orphan leg (currently a bug in the period transformer ignores this. Expect it to be 2 or 3 days)
			int orphanLegTime = 0;
			Assertions.assertEquals(100 - cargoTime - orphanLegTime, periodVA.getMinDuration());
			Assertions.assertEquals(100 - cargoTime - orphanLegTime, periodVA.getCharterOrDelegateMinDuration());
			Assertions.assertEquals(300 - cargoTime - orphanLegTime, periodVA.getMaxDuration());
			Assertions.assertEquals(300 - cargoTime - orphanLegTime, periodVA.getCharterOrDelegateMaxDuration());

			Assertions.assertNull(periodVA.getGenericCharterContract());
			Assertions.assertNotNull(periodVA.getContainedCharterContract());
			
			
		}, null);
	}
	
	/**
	 * 
	 */
	@Test
	public void testMinMaxDurationUpdatedAfterPeriod() {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withMinDuration(100) //
				.withMaxDuration(300) //
				.withEndWindow(LocalDateTime.of(2022, 3, 1, 0, 0, 0)) // Set end window to make maths easier

				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2021, 10, 1), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2021, 11, 1), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2022, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2022, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_HIMEJI), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2021, 4, 7));
		lngScenarioModel.setPromptPeriodEnd(LocalDate.of(2021, 7, 6));

		evaluateWithLSOTest(true, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStartDate(LocalDate.of(2021, 9, 1));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2021, 11));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			final CargoModel cargoModel = optimiserScenario.getCargoModel();
			// Expect second cargo will have been removed
			Assertions.assertEquals(1, cargoModel.getCargoes().size());

			final VesselAvailability periodVA = cargoModel.getVesselAvailabilities().get(0);

			// Loose first cargo (so reduce min duration by 31 days Jan, 28 Feb)
			// -1 for round error due to timezones (whole days vs hours)
			Assertions.assertEquals(100 - 31 - 28 - 1, periodVA.getCharterOrDelegateMinDuration());
			Assertions.assertEquals(100 - 31 - 28 - 1, periodVA.getMinDuration());
			Assertions.assertEquals(300 - 31 - 28, periodVA.getCharterOrDelegateMaxDuration());
			Assertions.assertEquals(300 - 31 - 28, periodVA.getMaxDuration());
			
			
			Assertions.assertNull(periodVA.getGenericCharterContract());
			Assertions.assertNull(periodVA.getContainedCharterContract());
		}, null);
	}
	/**
	 * 
	 */
	@Test
	public void testMinMaxDurationUpdatedAfterPeriodSharedContract() {
		
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withEndWindow(LocalDateTime.of(2022, 3, 1, 0, 0, 0)) // Set end window to make maths easier
				
				.build();
		
		final GenericCharterContract gcc = CommercialFactory.eINSTANCE.createGenericCharterContract();
		commercialModelFinder.getCommercialModel().getCharterContracts().add(gcc);
		gcc.setMinDuration(100);
		gcc.setMaxDuration(300);
		vesselAvailability.setGenericCharterContract(gcc);
		
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2021, 10, 1), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2021, 11, 1), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();
		
		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2022, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2022, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_HIMEJI), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();
		
		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2021, 4, 7));
		lngScenarioModel.setPromptPeriodEnd(LocalDate.of(2021, 7, 6));
		
		evaluateWithLSOTest(true, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStartDate(LocalDate.of(2021, 9, 1));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2021, 11));
		}, null, scenarioRunner -> {
			
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			final CargoModel cargoModel = optimiserScenario.getCargoModel();
			// Expect second cargo will have been removed
			Assertions.assertEquals(1, cargoModel.getCargoes().size());
			
			final VesselAvailability periodVA = cargoModel.getVesselAvailabilities().get(0);
			
			// Loose first cargo (so reduce min duration by 31 days Jan, 28 Feb)
			// -1 for round error due to timezones (whole days vs hours)
			Assertions.assertEquals(100 - 31 - 28 - 1, periodVA.getCharterOrDelegateMinDuration());
			Assertions.assertEquals(100 - 31 - 28 - 1, periodVA.getMinDuration());
			Assertions.assertEquals(300 - 31 - 28, periodVA.getCharterOrDelegateMaxDuration());
			Assertions.assertEquals(300 - 31 - 28, periodVA.getMaxDuration());
			
			Assertions.assertNull(periodVA.getGenericCharterContract());
			Assertions.assertNotNull(periodVA.getContainedCharterContract());
			
		}, null);
	}

}
