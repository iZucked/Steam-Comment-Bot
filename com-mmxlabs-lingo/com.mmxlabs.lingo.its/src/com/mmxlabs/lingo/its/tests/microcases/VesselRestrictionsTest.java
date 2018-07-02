/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AllowedVesselPermissionConstraintChecker;

@ExtendWith(ShiroRunner.class)
public class VesselRestrictionsTest extends AbstractMicroTestCase {

	private static final boolean RESTRICTION_APPLY_TO_NON_SHIPPED_CARGOES = false;

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testNoRestrictions() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 1);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability1, cargo1), null));
			// Nominal vessel
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, -1, cargo1), null));
			// Spot cargo
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, 0, cargo1), null));

		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLoadVesselClassRestrictions_NoInstance() throws Exception {

		// Create the required basic elements
		final Vessel vessel1 = fleetModelFinder.findVessel("STEAM-145");
		final Vessel vessel2 = fleetModelFinder.findVessel("STEAM-138");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel1, "50000", 1);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withAllowedVessels(vessel2) // <<<<<< Restrict load to alternative vessel
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				//
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability1, cargo1), null));
			// Nominal vessel
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, -1, cargo1), null));
			// Spot cargo
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, 0, cargo1), null));

		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLoadVesselClassRestrictions_Fleet() throws Exception {

		// Create the required basic elements
		final Vessel vessel1 = fleetModelFinder.findVessel("STEAM-145");
		final Vessel vessel2 = fleetModelFinder.findVessel("STEAM-138");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel1, "50000", 1);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel2, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withAllowedVessels(vessel2) // <<<<<< Restrict load to alternative vessel
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability1, cargo1), null));
			// Nominal vessel
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, -1, cargo1), null));
			// Spot cargo
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, 0, cargo1), null));
		});
	}

	/**
	 * The initial sequence builder will normally allocate a cargo to a suitable vessel. In this case there is one vessel which is not compatible, so no cargo allocation can occur
	 * 
	 * Update 2018/12/05 - New initial sequence builder will never allocate to a vessel which is not set in ecore model
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLoadVesselRestrictions_AutoAllocate_NoPossible() throws Exception {

		// Create the required basic elements
		final Vessel vessel1 = fleetModelFinder.findVessel("STEAM-145");
		final Vessel vessel2 = fleetModelFinder.findVessel("STEAM-138");

		// final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 1);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel2, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withAllowedVessels(vessel1) // <<<<<< Restrict load to alternative vessel
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				// .withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();
		try {
			evaluateWithLSOTest(scenarioRunner -> {
			});
			// Assertions.fail("Cargo should not be allocated");
		} catch (Exception e) {
			// Expect an exception for failed initial solution builder
		}
		// Cargo should not have been allocated a vessel
		Assertions.assertNull(cargo1.getVesselAssignmentType());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLoadVesselRestrictions_VesselExists() throws Exception {

		// Create the required basic elements
		final Vessel vessel1 = fleetModelFinder.findVessel("STEAM-145");
		final Vessel vessel2 = fleetModelFinder.findVessel("STEAM-138");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel1, "50000", 1);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel2, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withAllowedVessels(vessel2) // <<<<<< Restrict load to alternative vessel
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability1, cargo1), null));
			// Nominal vessel
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, -1, cargo1), null));
			// Spot cargo
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, 0, cargo1), null));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLoadVesselRestrictions_NoInstance() throws Exception {

		// Create the required basic elements
		final Vessel source = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", source, "50000", 1);

		final Vessel vessel1 = fleetModelBuilder.createVesselFrom("Vessel1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("Vessel2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withAllowedVessels(vessel2) // <<<<<< Restrict load to alternative vessel
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability1, cargo1), null));
			// Nominal vessel
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, -1, cargo1), null));
			// Spot cargo
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, 0, cargo1), null));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLoadVesselClassRestrictions_SpotOnly() throws Exception {

		// Create the required basic elements
		final Vessel vessel1 = fleetModelFinder.findVessel("STEAM-145");
		final Vessel vessel2 = fleetModelFinder.findVessel("STEAM-138");

		final CharterCurve charterIndex1 = pricingModelBuilder.createCharterCurve("CharterIndex1", "$", "day", 50_000);
		// final CharterIndex charterIndex2 = pricingModelBuilder.createCharterIndex("CharterIndex2", "$/day", 100_000);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel2, "50000", 1);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withAllowedVessels(vessel2) // <<<<<< Restrict load to alternative vessel
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability1, cargo1), null));
			// Nominal vessel
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, -1, cargo1), null));
			// Spot cargo
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, 0, cargo1), null));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDESPurchaseVesselRestrictions_VesselExists() throws Exception {
		Assumptions.assumeTrue(RESTRICTION_APPLY_TO_NON_SHIPPED_CARGOES);

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", true, LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", vessel) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withAllowedVessels(vessel) // <<<<<< Restrict load to alternative vessel
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assertions.assertTrue(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDESPurchaseVesselRestrictions_VesselExists2() throws Exception {
		Assumptions.assumeTrue(RESTRICTION_APPLY_TO_NON_SHIPPED_CARGOES);

		// Create the required basic elements
		final Vessel source = fleetModelFinder.findVessel("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVesselFrom("Vessel1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("Vessel2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", true, LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", vessel) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withAllowedVessels(vessel2) // <<<<<< Restrict load to alternative vessel
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assertions.assertFalse(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testFOBSaleVesselRestrictions_VesselExists() throws Exception {
		Assumptions.assumeTrue(RESTRICTION_APPLY_TO_NON_SHIPPED_CARGOES);

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withAllowedVessels(vessel) // <<<<<< Restrict load to alternative vessel
				.build() //
				.makeFOBSale("D1", true, LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7", vessel) //
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assertions.assertTrue(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testFOBSaleVesselRestrictions_VesselExists2() throws Exception {
		Assumptions.assumeTrue(RESTRICTION_APPLY_TO_NON_SHIPPED_CARGOES);

		// Create the required basic elements
		final Vessel source = fleetModelFinder.findVessel("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVesselFrom("Vessel1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("Vessel2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withAllowedVessels(vessel2) // <<<<<< Restrict load to alternative vessel
				.build() //
				.makeFOBSale("D1", true, LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7", vessel) //
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assertions.assertFalse(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDESPurchaseVesselRestrictions_WrongVessel() throws Exception {
		Assumptions.assumeTrue(RESTRICTION_APPLY_TO_NON_SHIPPED_CARGOES);

		// Create the required basic elements
		final Vessel source = fleetModelFinder.findVessel("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVesselFrom("Vessel1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("Vessel2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", true, LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", vessel) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withAllowedVessels(vessel2) // <<<<<< Restrict discharge to alternative vessel
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assertions.assertFalse(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDESPurchaseVesselRestrictions_WrongVesselClass() throws Exception {
		Assumptions.assumeTrue(RESTRICTION_APPLY_TO_NON_SHIPPED_CARGOES);

		// Create the required basic elements
		final Vessel vessel1 = fleetModelFinder.findVessel("STEAM-145");
		final Vessel vessel2 = fleetModelFinder.findVessel("STEAM-138");

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", true, LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", vessel1) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withAllowedVessels(vessel2) // <<<<<< Restrict discharge to alternative vessel
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assertions.assertFalse(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null));
		});
	}

	@Disabled("FOB/DESallowed vessel restrictions not applied in optimiser")
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testFOBSaleVesselRestrictions_WrongVessel() throws Exception {
		Assumptions.assumeTrue(RESTRICTION_APPLY_TO_NON_SHIPPED_CARGOES);

		// Create the required basic elements
		final Vessel source = fleetModelFinder.findVessel("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVesselFrom("Vessel1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("Vessel2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeFOBSale("D1", true, LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7", vessel) //
				.withAllowedVessels(vessel2) // <<<<<< Restrict discharge to alternative vessel
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assertions.assertFalse(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null));
		});
	}

	private AllowedVesselPermissionConstraintChecker getChecker(final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge) {
		return MicroTestUtils.getChecker(scenarioToOptimiserBridge, AllowedVesselPermissionConstraintChecker.class);
	}
}