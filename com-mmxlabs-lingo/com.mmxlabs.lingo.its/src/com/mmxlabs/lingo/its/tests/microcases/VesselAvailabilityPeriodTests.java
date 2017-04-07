/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;

/**
 * Test cases for updating vessel availabilities in a period optimisation
 * 
 * @author Simon Goodall
 *
 */
public class VesselAvailabilityPeriodTests extends AbstractMicroTestCase {

	/**
	 * Vessel availability and cargoes are within the period. No change to start/end information.
	 */
	@Test
	public void testContainedAvailability() {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		vesselClass.setMinHeel(500);

		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 2, 1, 0, 0)) //
				.withStartHeel(1_000, 3_000, 22.6, "5") //
				.withRepositioning("1000000") //
				.withEndWindow(LocalDateTime.of(2017, 6, 1, 0, 0)) //
				.withEndHeel(4_000, 5_000, EVesselTankState.MUST_BE_COLD, "7") //
				.build();

		@NonNull
		RuleBasedBallastBonusContract ballastBonusContract = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPort("Sakai"), "1000000");
		vesselAvailability.setBallastBonusContract(ballastBonusContract);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 3, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2017, 4, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2017, 5, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 2) //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(false, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStart(YearMonth.of(2017, 1));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2017, 7));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			EList<VesselAvailability> vesselAvailabilities = optimiserScenario.getCargoModel().getVesselAvailabilities();

			Assert.assertEquals(1, vesselAvailabilities.size());
			VesselAvailability va = vesselAvailabilities.get(0);

			Assert.assertEquals(LocalDateTime.of(2017, 2, 1, 0, 0), va.getStartAfter());
			Assert.assertEquals(LocalDateTime.of(2017, 2, 1, 0, 0), va.getStartBy());
			Assert.assertEquals("1000000", va.getRepositioningFee());
			Assert.assertEquals("5", va.getStartHeel().getPriceExpression());
			Assert.assertEquals(1000.0, va.getStartHeel().getMinVolumeAvailable(), 0.0);
			Assert.assertEquals(3000.0, va.getStartHeel().getMaxVolumeAvailable(), 0.0);
			Assert.assertEquals(22.6, va.getStartHeel().getCvValue(), 0.0);

			Assert.assertEquals(LocalDateTime.of(2017, 6, 1, 0, 0), va.getEndAfter());
			Assert.assertEquals(LocalDateTime.of(2017, 6, 1, 0, 0), va.getEndBy());
			Assert.assertEquals("1000000", va.getRepositioningFee());
			Assert.assertEquals("7", va.getEndHeel().getPriceExpression());
			Assert.assertEquals(4000.0, va.getEndHeel().getMinimumEndHeel(), 0.0);
			Assert.assertEquals(5000.0, va.getEndHeel().getMaximumEndHeel(), 0.0);
			Assert.assertNotNull(va.getBallastBonusContract());
		}, null);
	}

	/**
	 * Vessel availability and cargoes are within the period. No change to start/end information.
	 */
	@Test
	public void testAvailabilityOverlapsEnd() {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		vesselClass.setMinHeel(500);

		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 2, 1, 0, 0)) //
				.withStartHeel(1_000, 3_000, 22.6, "5") //
				.withRepositioning("1000000") //
				.withEndWindow(LocalDateTime.of(2017, 6, 1, 0, 0)) //
				.withEndHeel(4_000, 5_000, EVesselTankState.MUST_BE_COLD, "7") //
				.build();

		@NonNull
		RuleBasedBallastBonusContract ballastBonusContract = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPort("Sakai"), "1000000");
		vesselAvailability.setBallastBonusContract(ballastBonusContract);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 3, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2017, 4, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2017, 5, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 2) //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(false, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStart(YearMonth.of(2017, 1));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2017, 5));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			EList<VesselAvailability> vesselAvailabilities = optimiserScenario.getCargoModel().getVesselAvailabilities();

			Assert.assertEquals(1, vesselAvailabilities.size());
			VesselAvailability va = vesselAvailabilities.get(0);

			Assert.assertEquals(LocalDateTime.of(2017, 2, 1, 0, 0), va.getStartAfter());
			Assert.assertEquals(LocalDateTime.of(2017, 2, 1, 0, 0), va.getStartBy());
			Assert.assertEquals("1000000", va.getRepositioningFee());
			Assert.assertEquals("5", va.getStartHeel().getPriceExpression());
			Assert.assertEquals(1000.0, va.getStartHeel().getMinVolumeAvailable(), 0.0);
			Assert.assertEquals(3000.0, va.getStartHeel().getMaxVolumeAvailable(), 0.0);
			Assert.assertEquals(22.6, va.getStartHeel().getCvValue(), 0.0);

			Assert.assertEquals(LocalDateTime.of(2017, 6, 1, 0, 0), va.getEndAfter());
			Assert.assertEquals(LocalDateTime.of(2017, 6, 1, 0, 0), va.getEndBy());
			Assert.assertEquals("1000000", va.getRepositioningFee());
			Assert.assertEquals("7", va.getEndHeel().getPriceExpression());
			Assert.assertEquals(4000.0, va.getEndHeel().getMinimumEndHeel(), 0.0);
			Assert.assertEquals(5000.0, va.getEndHeel().getMaximumEndHeel(), 0.0);
			Assert.assertNotNull(va.getBallastBonusContract());
		}, null);
	}

	/**
	 * Vessel availability and cargoes are within the period. No change to start/end information.
	 */
	@Test
	public void testAvailabilityLooseCargo1() {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		vesselClass.setMinHeel(500);

		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 2, 1, 0, 0)) //
				.withStartHeel(1_000, 3_000, 22.6, "5") //
				.withRepositioning("1000000") //
				.withEndWindow(LocalDateTime.of(2017, 6, 1, 0, 0)) //
				.withEndHeel(4_000, 5_000, EVesselTankState.MUST_BE_COLD, "7") //
				.build();

		@NonNull
		RuleBasedBallastBonusContract ballastBonusContract = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPort("Sakai"), "1000000");
		vesselAvailability.setBallastBonusContract(ballastBonusContract);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.with(slot -> ((LoadSlot) slot).setCargoCV(22.3)) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 3, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2017, 4, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2017, 5, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 2) //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(false, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStart(YearMonth.of(2017, 5));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2017, 7));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			EList<VesselAvailability> vesselAvailabilities = optimiserScenario.getCargoModel().getVesselAvailabilities();

			Assert.assertEquals(1, vesselAvailabilities.size());
			VesselAvailability va = vesselAvailabilities.get(0);

			Assert.assertEquals(LocalDateTime.of(2017, 4, 1, 12, 0), va.getStartAfter());
			Assert.assertEquals(LocalDateTime.of(2017, 4, 1, 12, 0), va.getStartBy());
			Assert.assertEquals("", va.getRepositioningFee());
			Assert.assertEquals("", va.getStartHeel().getPriceExpression());
			Assert.assertEquals(500.0, va.getStartHeel().getMinVolumeAvailable(), 0.0);
			Assert.assertEquals(500.0, va.getStartHeel().getMaxVolumeAvailable(), 0.0);
			// TODO: Decide what this should be! Is it previous heel or next heel?
			// TODO: (Add another trimmed cargo with another CV value to test the correct one has been picked up)
			// TODO: The period transformer hard codes 22.8!
			 Assert.assertEquals(22.3, va.getStartHeel().getCvValue(), 0.0);

			Assert.assertEquals(LocalDateTime.of(2017, 6, 1, 0, 0), va.getEndAfter());
			Assert.assertEquals(LocalDateTime.of(2017, 6, 1, 0, 0), va.getEndBy());
			Assert.assertEquals("1000000", va.getRepositioningFee());
			Assert.assertEquals("7", va.getEndHeel().getPriceExpression());
			Assert.assertEquals(4000.0, va.getEndHeel().getMinimumEndHeel(), 0.0);
			Assert.assertEquals(5000.0, va.getEndHeel().getMaximumEndHeel(), 0.0);
			Assert.assertNotNull(va.getBallastBonusContract());
		}, null);
	}

	/**
	 * Vessel availability and cargoes are within the period. No change to start/end information.
	 */
	@Test
	public void testAvailabilityLooseCargo2() {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		vesselClass.setMinHeel(500);

		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 2, 1, 0, 0)) //
				.withStartHeel(1_000, 3_000, 22.6, "5") //
				.withRepositioning("1000000") //
				.withEndWindow(LocalDateTime.of(2017, 6, 1, 0, 0)) //
				.withEndHeel(4_000, 5_000, EVesselTankState.MUST_BE_COLD, "7") //
				.build();

		@NonNull
		RuleBasedBallastBonusContract ballastBonusContract = commercialModelBuilder.createSimpleLumpSumBallastBonusContract(portFinder.findPort("Sakai"), "1000000");
		vesselAvailability.setBallastBonusContract(ballastBonusContract);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 2, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 3, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2017, 4, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2017, 5, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 2) //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(false, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStart(YearMonth.of(2017, 1));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2017, 2));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			EList<VesselAvailability> vesselAvailabilities = optimiserScenario.getCargoModel().getVesselAvailabilities();

			Assert.assertEquals(1, vesselAvailabilities.size());
			VesselAvailability va = vesselAvailabilities.get(0);

			Assert.assertEquals(LocalDateTime.of(2017, 2, 1, 0, 0), va.getStartAfter());
			Assert.assertEquals(LocalDateTime.of(2017, 2, 1, 0, 0), va.getStartBy());
			Assert.assertEquals("1000000", va.getRepositioningFee());
			Assert.assertEquals("5", va.getStartHeel().getPriceExpression());
			Assert.assertEquals(1000.0, va.getStartHeel().getMinVolumeAvailable(), 0.0);
			Assert.assertEquals(3000.0, va.getStartHeel().getMaxVolumeAvailable(), 0.0);
			Assert.assertEquals(22.6, va.getStartHeel().getCvValue(), 0.0);

			Assert.assertEquals(LocalDateTime.of(2017, 4, 1, 12, 0), va.getEndAfter());
			Assert.assertEquals(LocalDateTime.of(2017, 4, 1, 12, 0), va.getEndBy());
			Assert.assertEquals("1000000", va.getRepositioningFee());
			Assert.assertEquals("", va.getEndHeel().getPriceExpression());
			Assert.assertEquals(500.0, va.getEndHeel().getMinimumEndHeel(), 0.0);
			Assert.assertEquals(500.0, va.getEndHeel().getMaximumEndHeel(), 0.0);
			Assert.assertNull(va.getBallastBonusContract());
		}, null);
	}
}
