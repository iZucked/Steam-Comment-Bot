/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AllowedVesselPermissionConstraintChecker;

@RunWith(value = ShiroRunner.class)
public class VesselRestrictionsTest extends AbstractMicroTestCase {

	@Test
	@Category({ MicroTest.class })
	public void testNoRestrictions() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 1);

		final Vessel vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
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
			Assert.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability1, cargo1), null));
			// Nominal vessel
			Assert.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, charterInMarket_1, -1, cargo1), null));
			// Spot cargo
			Assert.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, charterInMarket_1, 0, cargo1), null));

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testLoadVesselClassRestrictions_NoInstance() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final VesselClass vesselClass2 = fleetModelFinder.findVesselClass("STEAM-138");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 1);

		final Vessel vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withVesselRestriction(vesselClass2) // <<<<<< Restrict load to alternative vessel class
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
			Assert.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability1, cargo1), null));
			// Nominal vessel
			Assert.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, charterInMarket_1, -1, cargo1), null));
			// Spot cargo
			Assert.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, charterInMarket_1, 0, cargo1), null));

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testLoadVesselClassRestrictions_Fleet() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final VesselClass vesselClass2 = fleetModelFinder.findVesselClass("STEAM-138");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 1);

		final Vessel vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass2);
		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withVesselRestriction(vesselClass2) // <<<<<< Restrict load to alternative vessel class
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
			Assert.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability1, cargo1), null));
			// Nominal vessel
			Assert.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, charterInMarket_1, -1, cargo1), null));
			// Spot cargo
			Assert.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, charterInMarket_1, 0, cargo1), null));
		});
	}

	/**
	 * The initial sequence builder will normally allocate a cargo to a suitable vessel. In this case there is one vessel which is not compatible, so no cargo allocation can occur
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testLoadVesselRestrictions_AutoAllocate_NoPossible() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final VesselClass vesselClass2 = fleetModelFinder.findVesselClass("STEAM-138");

		// final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 1);

		final Vessel vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass2);
		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withVesselRestriction(vesselClass) // <<<<<< Restrict load to alternative vessel class
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				// .withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();
		try {
			evaluateWithLSOTest(scenarioRunner -> {
			});
			Assert.fail("Cargo should not be allocated");
		} catch (Exception e) {
			// Expect an exception for failed initial solution builder
		}
		// Cargo should not have been allocated a vessel
		Assert.assertNull(cargo1.getVesselAssignmentType());
	}

	@Test
	@Category({ MicroTest.class })
	public void testLoadVesselRestrictions_VesselExists() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final VesselClass vesselClass2 = fleetModelFinder.findVesselClass("STEAM-138");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 1);

		final Vessel vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass2);
		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withVesselRestriction(vessel) // <<<<<< Restrict load to alternative vessel class
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
			Assert.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability1, cargo1), null));
			// Nominal vessel
			Assert.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, charterInMarket_1, -1, cargo1), null));
			// Spot cargo
			Assert.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, charterInMarket_1, 0, cargo1), null));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testLoadVesselRestrictions_NoInstance() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 1);

		final Vessel vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		final Vessel vessel2 = fleetModelBuilder.createVessel("Vessel2", vesselClass);
		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withVesselRestriction(vessel2) // <<<<<< Restrict load to alternative vessel class
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
			Assert.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability1, cargo1), null));
			// Nominal vessel
			Assert.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, charterInMarket_1, -1, cargo1), null));
			// Spot cargo
			Assert.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, charterInMarket_1, 0, cargo1), null));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testLoadVesselClassRestrictions_SpotOnly() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final VesselClass vesselClass2 = fleetModelFinder.findVesselClass("STEAM-138");

		final CharterIndex charterIndex1 = pricingModelBuilder.createCharterIndex("CharterIndex1", "$", "day", 50_000);
		// final CharterIndex charterIndex2 = pricingModelBuilder.createCharterIndex("CharterIndex2", "$/day", 100_000);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass2, "50000", 1);

		final Vessel vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withVesselRestriction(vesselClass2) // <<<<<< Restrict load to alternative vessel class
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
			Assert.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability1, cargo1), null));
			// Nominal vessel
			Assert.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, charterInMarket_1, -1, cargo1), null));
			// Spot cargo
			Assert.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge, charterInMarket_1, 0, cargo1), null));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testDESPurchaseVesselRestrictions_VesselExists() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", true, LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", vessel) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withVesselRestriction(vessel) // <<<<<< Restrict load to alternative vessel class
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assert.assertTrue(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testDESPurchaseVesselRestrictions_VesselExists2() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		final Vessel vessel2 = fleetModelBuilder.createVessel("Vessel2", vesselClass);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", true, LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", vessel) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withVesselRestriction(vessel2) // <<<<<< Restrict load to alternative vessel
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assert.assertFalse(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testFOBSaleVesselRestrictions_VesselExists() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withVesselRestriction(vessel) // <<<<<< Restrict load to alternative vessel class
				.build() //
				.makeFOBSale("D1", true, LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7", vessel) //
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assert.assertTrue(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testFOBSaleVesselRestrictions_VesselExists2() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		final Vessel vessel2 = fleetModelBuilder.createVessel("Vessel2", vesselClass);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withVesselRestriction(vessel2) // <<<<<< Restrict load to alternative vessel
				.build() //
				.makeFOBSale("D1", true, LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7", vessel) //
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assert.assertFalse(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testDESPurchaseVesselRestrictions_WrongVessel() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		final Vessel vessel2 = fleetModelBuilder.createVessel("Vessel2", vesselClass);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", true, LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", vessel) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withVesselRestriction(vessel2) // <<<<<< Restrict discharge to alternative vessel class
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assert.assertFalse(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testDESPurchaseVesselRestrictions_WrongVesselClass() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final VesselClass vesselClass2 = fleetModelFinder.findVesselClass("STEAM-138");

		final Vessel vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", true, LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", vessel) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withVesselRestriction(vesselClass2) // <<<<<< Restrict discharge to alternative vessel class
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assert.assertFalse(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testFOBSaleVesselRestrictions_WrongVessel() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		final Vessel vessel2 = fleetModelBuilder.createVessel("Vessel2", vesselClass);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeFOBSale("D1", true, LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7", vessel) //
				.withVesselRestriction(vessel2) // <<<<<< Restrict discharge to alternative vessel class
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assert.assertFalse(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testFOBSaleVesselRestrictions_WrongVesselClass() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final VesselClass vesselClass2 = fleetModelFinder.findVesselClass("STEAM-138");

		final Vessel vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		final Vessel vessel2 = fleetModelBuilder.createVessel("Vessel2", vesselClass);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeFOBSale("D1", true, LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7", vessel) //
				.withVesselRestriction(vesselClass2) // <<<<<< Restrict discharge to alternative vessel class
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assert.assertFalse(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null));
		});
	}

	private AllowedVesselPermissionConstraintChecker getChecker(final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge) {
		return MicroTestUtils.getChecker(scenarioToOptimiserBridge, AllowedVesselPermissionConstraintChecker.class);
	}
}