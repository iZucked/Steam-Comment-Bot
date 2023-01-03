/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.commercial.EVesselTankState;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.TimePeriod;

/**
 * Test cases for updating vessel availabilities in a period optimisation
 * 
 * @author Simon Goodall
 *
 */
public class VesselCharterPeriodTests extends AbstractLegacyMicroTestCase {

	/**
	 * Vessel availability and cargoes are within the period. No change to start/end information.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testContainedAvailability() {

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 2, 1, 0, 0)) //
				.withStartHeel(1_000, 3_000, 22.6, "5") //
				.withRepositioning("1000000") //
				.withEndWindow(LocalDateTime.of(2017, 6, 1, 0, 0)) //
				.withEndHeel(4_000, 5_000, EVesselTankState.MUST_BE_COLD, "7") //
				.build();

		@NonNull
		GenericCharterContract ballastBonusContract = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPortById(InternalDataConstants.PORT_SAKAI), "1000000");
		vesselCharter.setGenericCharterContract(ballastBonusContract);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 3, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselCharter, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2017, 4, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2017, 5, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselCharter, 2) //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(false, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStartDate(LocalDate.of(2017, 1, 1));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2017, 7));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			EList<VesselCharter> vesselAvailabilities = optimiserScenario.getCargoModel().getVesselCharters();

			Assertions.assertEquals(1, vesselAvailabilities.size());
			VesselCharter va = vesselAvailabilities.get(0);

			Assertions.assertEquals(LocalDateTime.of(2017, 2, 1, 0, 0), va.getStartAfter());
			Assertions.assertEquals(LocalDateTime.of(2017, 2, 1, 0, 0), va.getStartBy());
			Assertions.assertEquals("5", va.getStartHeel().getPriceExpression());
			Assertions.assertEquals(1000.0, va.getStartHeel().getMinVolumeAvailable(), 0.0001);
			Assertions.assertEquals(3000.0, va.getStartHeel().getMaxVolumeAvailable(), 0.0001);
			Assertions.assertEquals(22.6, va.getStartHeel().getCvValue(), 0.0001);

			Assertions.assertEquals(LocalDateTime.of(2017, 6, 1, 0, 0), va.getEndAfter());
			Assertions.assertEquals(LocalDateTime.of(2017, 6, 1, 0, 0), va.getEndBy());
			Assertions.assertEquals("7", va.getEndHeel().getPriceExpression());
			Assertions.assertEquals(4000.0, va.getEndHeel().getMinimumEndHeel(), 0.0001);
			Assertions.assertEquals(5000.0, va.getEndHeel().getMaximumEndHeel(), 0.0001);
			Assertions.assertNotNull(va.getGenericCharterContract());
		}, null);
	}

	/**
	 * Vessel availability and cargoes are within the period. No change to start/end information.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testAvailabilityOverlapsEnd() {

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 2, 1, 0, 0)) //
				.withStartHeel(1_000, 3_000, 22.6, "5") //
				.withRepositioning("1000000") //
				.withEndWindow(LocalDateTime.of(2017, 6, 1, 0, 0)) //
				.withEndHeel(4_000, 5_000, EVesselTankState.MUST_BE_COLD, "7") //
				.build();

		@NonNull
		GenericCharterContract ballastBonusContract = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPortById(InternalDataConstants.PORT_SAKAI), "1000000");
		vesselCharter.setGenericCharterContract(ballastBonusContract);

		/*final Cargo cargo1 =*/ 
		cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 3, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselCharter, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		/*final Cargo cargo2 =*/ 
		cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2017, 4, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2017, 5, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselCharter, 2) //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(false, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStartDate(LocalDate.of(2017, 1, 1));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2017, 5));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			EList<VesselCharter> vesselAvailabilities = optimiserScenario.getCargoModel().getVesselCharters();

			Assertions.assertEquals(1, vesselAvailabilities.size());
			VesselCharter va = vesselAvailabilities.get(0);

			Assertions.assertEquals(LocalDateTime.of(2017, 2, 1, 0, 0), va.getStartAfter());
			Assertions.assertEquals(LocalDateTime.of(2017, 2, 1, 0, 0), va.getStartBy());
			Assertions.assertEquals("5", va.getStartHeel().getPriceExpression());
			Assertions.assertEquals(1000.0, va.getStartHeel().getMinVolumeAvailable(), 0.0001);
			Assertions.assertEquals(3000.0, va.getStartHeel().getMaxVolumeAvailable(), 0.0001);
			Assertions.assertEquals(22.6, va.getStartHeel().getCvValue(), 0.0001);

			Assertions.assertEquals(LocalDateTime.of(2017, 6, 1, 0, 0), va.getEndAfter());
			Assertions.assertEquals(LocalDateTime.of(2017, 6, 1, 0, 0), va.getEndBy());
			Assertions.assertEquals("7", va.getEndHeel().getPriceExpression());
			Assertions.assertEquals(4000.0, va.getEndHeel().getMinimumEndHeel(), 0.0001);
			Assertions.assertEquals(5000.0, va.getEndHeel().getMaximumEndHeel(), 0.0001);
			Assertions.assertNotNull(va.getGenericCharterContract());
		}, null);
	}

	/**
	 * Vessel availability and cargoes are within the period. No change to start/end information.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testAvailabilityLooseCargo1() {

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 2, 1, 0, 0)) //
				.withStartHeel(1_000, 3_000, 22.6, "5") //
				.withRepositioning("1000000") //
				.withEndWindow(LocalDateTime.of(2017, 6, 1, 0, 0)) //
				.withEndHeel(4_000, 5_000, EVesselTankState.MUST_BE_COLD, "7") //
				.build();

		@NonNull
		GenericCharterContract ballastBonusContract = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPortById(InternalDataConstants.PORT_SAKAI), "1000000");
		vesselCharter.setGenericCharterContract(ballastBonusContract);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.with(slot -> ((LoadSlot) slot).setCargoCV(22.3)) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 3, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.withVesselAssignment(vesselCharter, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2017, 4, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2017, 5, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.withVesselAssignment(vesselCharter, 2) //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(false, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStartDate(LocalDate.of(2017, 5, 1));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2017, 7));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			EList<VesselCharter> vesselAvailabilities = optimiserScenario.getCargoModel().getVesselCharters();

			Assertions.assertEquals(1, vesselAvailabilities.size());
			VesselCharter va = vesselAvailabilities.get(0);

			Assertions.assertEquals(LocalDateTime.of(2017, 4, 1, 11, 0), va.getStartAfter());
			Assertions.assertEquals(LocalDateTime.of(2017, 4, 1, 11, 0), va.getStartBy());
			Assertions.assertEquals("", va.getStartHeel().getPriceExpression());
			Assertions.assertEquals(500.0, va.getStartHeel().getMinVolumeAvailable(), 0.0001);
			Assertions.assertEquals(500.0, va.getStartHeel().getMaxVolumeAvailable(), 0.0001);
			// TODO: Decide what this should be! Is it previous heel or next heel?
			// TODO: (Add another trimmed cargo with another CV value to test the correct one has been picked up)
			// TODO: The period transformer hard codes 22.8, it should really be 22.3 - the CV of cargo 1
			Assertions.assertEquals(22.8, va.getStartHeel().getCvValue(), 0.0001);

			Assertions.assertEquals(LocalDateTime.of(2017, 6, 1, 0, 0), va.getEndAfter());
			Assertions.assertEquals(LocalDateTime.of(2017, 6, 1, 0, 0), va.getEndBy());

			// Expect repositioning fee to be cleared
			Assertions.assertEquals("7", va.getEndHeel().getPriceExpression());
			Assertions.assertEquals(4000.0, va.getEndHeel().getMinimumEndHeel(), 0.0001);
			Assertions.assertEquals(5000.0, va.getEndHeel().getMaximumEndHeel(), 0.0001);
			Assertions.assertNotNull(va.getGenericCharterContract());
		}, null);
	}

	/**
	 * Vessel availability and cargoes are within the period. No change to start/end information.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testAvailabilityLooseCargo2() {

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 2, 1, 0, 0)) //
				.withStartHeel(1_000, 3_000, 22.6, "5") //
				.withRepositioning("1000000") //
				.withEndWindow(LocalDateTime.of(2017, 6, 1, 0, 0)) //
				.withEndHeel(4_000, 5_000, EVesselTankState.MUST_BE_COLD, "7") //
				.build();

		@NonNull
		GenericCharterContract ballastBonusContract = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPortById(InternalDataConstants.PORT_SAKAI), "1000000");
		vesselCharter.setContainedCharterContract(ballastBonusContract);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 3, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.withVesselAssignment(vesselCharter, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2017, 4, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2017, 5, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.withVesselAssignment(vesselCharter, 2) //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(false, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStartDate(LocalDate.of(2017, 1, 1));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2017, 2));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			EList<VesselCharter> vesselAvailabilities = optimiserScenario.getCargoModel().getVesselCharters();

			Assertions.assertEquals(1, vesselAvailabilities.size());
			VesselCharter va = vesselAvailabilities.get(0);

			Assertions.assertEquals(LocalDateTime.of(2017, 2, 1, 0, 0), va.getStartAfter());
			Assertions.assertEquals(LocalDateTime.of(2017, 2, 1, 0, 0), va.getStartBy());
			Assertions.assertEquals("5", va.getStartHeel().getPriceExpression());
			Assertions.assertEquals(1000.0, va.getStartHeel().getMinVolumeAvailable(), 0.0001);
			Assertions.assertEquals(3000.0, va.getStartHeel().getMaxVolumeAvailable(), 0.0001);
			Assertions.assertEquals(22.6, va.getStartHeel().getCvValue(), 0.0001);

			Assertions.assertEquals(LocalDateTime.of(2017, 4, 1, 11, 0), va.getEndAfter());
			Assertions.assertEquals(LocalDateTime.of(2017, 4, 1, 11, 0), va.getEndBy());
			Assertions.assertEquals("", va.getEndHeel().getPriceExpression());
			Assertions.assertEquals(500.0, va.getEndHeel().getMinimumEndHeel(), 0.0001);
			Assertions.assertEquals(500.0, va.getEndHeel().getMaximumEndHeel(), 0.0001);
			Assertions.assertNull(va.getGenericCharterContract());
		}, null);
	}
}
