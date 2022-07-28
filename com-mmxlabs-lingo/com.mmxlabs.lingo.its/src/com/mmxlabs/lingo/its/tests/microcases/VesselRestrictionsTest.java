/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AllowedVesselPermissionConstraintChecker;

@ExtendWith(ShiroRunner.class)
public class VesselRestrictionsTest extends AbstractMicroTestCase {

	// private static final boolean RESTRICTION_APPLY_TO_NON_SHIPPED_CARGOES = false;

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testNoRestrictions() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 1);

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselCharter1, cargo1), null, new ArrayList<>()));
			// Nominal vessel
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, -1, cargo1), null, new ArrayList<>()));
			// Spot cargo
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, 0, cargo1), null, new ArrayList<>()));

		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLoadVesselRestrictions_NoInstance1() throws Exception {

		// Create the required basic elements
		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel2 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel1, entity, "50000", 1);

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel1, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withRestrictedVessels(vessel2, true).build() //
				//
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				//
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselCharter1, cargo1), null, new ArrayList<>()));
			// Nominal vessel
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, -1, cargo1), null, new ArrayList<>()));
			// Spot cargo
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, 0, cargo1), null, new ArrayList<>()));

		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLoadVesselRestrictions_Fleet() throws Exception {

		// Create the required basic elements
		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel2 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel1, entity, "50000", 1);

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel2, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withRestrictedVessels(vessel2, true) // <<<<<< Restrict load to alternative vessel
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselCharter1, cargo1), null, new ArrayList<>()));
			// Nominal vessel
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, -1, cargo1), null, new ArrayList<>()));
			// Spot cargo
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, 0, cargo1), null, new ArrayList<>()));
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
		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel2 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		// final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel1, "50000", 1);

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel2, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withRestrictedVessels(vessel1, true) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
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
		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel2 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel1, entity, "50000", 1);

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel2, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withRestrictedVessels(vessel2, true) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselCharter1, cargo1), null, new ArrayList<>()));
			// Nominal vessel
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, -1, cargo1), null, new ArrayList<>()));
			// Spot cargo
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, 0, cargo1), null, new ArrayList<>()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLoadVesselRestrictions_NoInstance2() throws Exception {

		// Create the required basic elements
		final Vessel source = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", source, entity, "50000", 1);

		final Vessel vessel1 = fleetModelBuilder.createVesselFrom("Vessel1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("Vessel2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel1, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withRestrictedVessels(vessel2, true) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselCharter1, cargo1), null, new ArrayList<>()));
			// Nominal vessel
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, -1, cargo1), null, new ArrayList<>()));
			// Spot cargo
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, 0, cargo1), null, new ArrayList<>()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLoadVesselRestrictions_SpotOnly() throws Exception {

		// Create the required basic elements
		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel2 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		final CharterCurve charterIndex1 = pricingModelBuilder.createCharterCurve("CharterIndex1", "$", "day", 50_000);
		// final CharterIndex charterIndex2 = pricingModelBuilder.createCharterIndex("CharterIndex2", "$/day", 100_000);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel2, entity, "50000", 1);

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel1, entity) //
				.withCharterRate("30000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withRestrictedVessels(vessel2, true) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselCharter1, cargo1), null, new ArrayList<>()));
			// Nominal vessel
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, -1, cargo1), null, new ArrayList<>()));
			// Spot cargo
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, 0, cargo1), null, new ArrayList<>()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	@Disabled("Restrictions do no apply to non-shipped cargoes")
	public void testDESPurchaseVesselRestrictions_VesselExists() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DIVERT_FROM_SOURCE, LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8,
						vessel) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withRestrictedVessels(vessel, true) //
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assertions.assertTrue(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null, new ArrayList<>()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testFOBPurchaseVesselRestrictions_VesselExists() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		// Construct the cargo scenario

		final FOBPurchasesMarket market = spotMarketsModelBuilder.makeFOBPurchaseMarket("FOBP1", portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), entity, "5", 22.3).build();

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeMarketFOBPurchase("L1", market, YearMonth.of(2015, 12), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withRestrictedVessels(vessel, true) //
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assertions.assertTrue(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null, new ArrayList<>()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testFOBPurchaseVesselRestrictions_VesselExists2() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		// Construct the cargo scenario

		final FOBPurchasesMarket market = spotMarketsModelBuilder.makeFOBPurchaseMarket("FOBP1", portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), entity, "5", 22.3) //
				.with(m -> m.getRestrictedVessels().add(vessel)) //
				.with(m -> m.setRestrictedVesselsArePermissive(true)) //
				.build();

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeMarketFOBPurchase("L1", market, YearMonth.of(2015, 12), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assertions.assertTrue(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null, new ArrayList<>()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	@Disabled("Restrictions do no apply to non-shipped cargoes")
	public void testDESPurchaseVesselRestrictions_VesselExists2() throws Exception {

		// Create the required basic elements
		final Vessel source = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final Vessel vessel = fleetModelBuilder.createVesselFrom("Vessel1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("Vessel2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DIVERT_FROM_SOURCE, LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8,
						vessel) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withRestrictedVessels(vessel2, true) //
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assertions.assertFalse(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null, new ArrayList<>()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	@Disabled("Restrictions do no apply to non-shipped cargoes")
	public void testFOBSaleVesselRestrictions_VesselExists() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withRestrictedVessels(vessel, true) //
				.build() //
				.makeFOBSale("D1", FOBSaleDealType.DIVERT_TO_DEST, LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7", vessel) //
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assertions.assertTrue(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null, new ArrayList<>()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	@Disabled("Restrictions do no apply to non-shipped cargoes")
	public void testFOBSaleVesselRestrictions_VesselExists2() throws Exception {

		// Create the required basic elements
		final Vessel source = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final Vessel vessel = fleetModelBuilder.createVesselFrom("Vessel1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("Vessel2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withRestrictedVessels(vessel2, true) //
				.build() //
				.makeFOBSale("D1", FOBSaleDealType.DIVERT_TO_DEST, LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7", vessel) //
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assertions.assertFalse(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null, new ArrayList<>()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	@Disabled("Restrictions do no apply to non-shipped cargoes")
	public void testDESPurchaseVesselRestrictions_WrongVessel() throws Exception {

		// Create the required basic elements
		final Vessel source = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel = fleetModelBuilder.createVesselFrom("Vessel1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("Vessel2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DIVERT_FROM_SOURCE, LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8,
						vessel) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withRestrictedVessels(vessel2, true) //
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assertions.assertFalse(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null, new ArrayList<>()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	@Disabled("Restrictions do no apply to non-shipped cargoes")
	public void testDESPurchaseVesselRestrictions_WrongVessel2() throws Exception {

		// Create the required basic elements
		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel2 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DIVERT_FROM_SOURCE, LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8,
						vessel1) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withRestrictedVessels(vessel2, true) //
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assertions.assertFalse(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null, new ArrayList<>()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	@Disabled("Restrictions do no apply to non-shipped cargoes")
	public void testFOBSaleVesselRestrictions_WrongVessel() throws Exception {

		// Create the required basic elements
		final Vessel source = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final Vessel vessel = fleetModelBuilder.createVesselFrom("Vessel1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("Vessel2", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeFOBSale("D1", FOBSaleDealType.DIVERT_TO_DEST, LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7", vessel) //
				.withRestrictedVessels(vessel2, true) //
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assertions.assertFalse(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null, new ArrayList<>()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLoad_RestrictedVessel() throws Exception {

		// Create the required basic elements
		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel2 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel1, entity, "50000", 1);

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel2, entity) //
				.withCharterRate("80000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withRestrictedVessels(vessel1, false) // <<<<<< Restrict load to alternative vessel
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselCharter1, cargo1), null, new ArrayList<>()));
			// Nominal vessel
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, -1, cargo1), null, new ArrayList<>()));
			// Spot cargo
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, 0, cargo1), null, new ArrayList<>()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLoad_RestrictedVessel_Contract() throws Exception {

		// Create the required basic elements
		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel2 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel1, entity, "50000", 1);

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel2, entity) //
				.withCharterRate("80000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		PurchaseContract contract = commercialModelBuilder.makeExpressionPurchaseContract("C1", entity, "5");
		contract.setRestrictedVesselsArePermissive(false);
		contract.getRestrictedVessels().add(vessel1);

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), contract, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselCharter1, cargo1), null, new ArrayList<>()));
			// Nominal vessel
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, -1, cargo1), null, new ArrayList<>()));
			// Spot cargo
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, 0, cargo1), null, new ArrayList<>()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLoad_PermissiveEmptyVessel() throws Exception {

		// Create the required basic elements
		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel2 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel1, entity, "50000", 1);

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel2, entity) //
				.withCharterRate("80000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withRestrictedVessels(true) // <<<<<< Restrict load to alternative vessel
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselCharter1, cargo1), null, new ArrayList<>()));
			// Nominal vessel
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, -1, cargo1), null, new ArrayList<>()));
			// Spot cargo
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, 0, cargo1), null, new ArrayList<>()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLoad_PermissiveEmptyVesselContract() throws Exception {

		// Create the required basic elements
		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel2 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel1, entity, "50000", 1);

		PurchaseContract contract = commercialModelBuilder.makeExpressionPurchaseContract("C1", entity, "5");
		contract.setRestrictedVesselsArePermissive(true);

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel2, entity) //
				.withCharterRate("80000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), contract, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselCharter1, cargo1), null, new ArrayList<>()));
			// Nominal vessel
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, -1, cargo1), null, new ArrayList<>()));
			// Spot cargo
			Assertions.assertFalse(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, 0, cargo1), null, new ArrayList<>()));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLoad_RestrictiveEmptyVessel() throws Exception {

		// Create the required basic elements
		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel2 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel1, entity, "50000", 1);

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel2, entity) //
				.withCharterRate("80000") //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withRestrictedVessels(false) // <<<<<< Restrict load to alternative vessel
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final AllowedVesselPermissionConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselCharter1, cargo1), null, new ArrayList<>()));
			// Nominal vessel
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, -1, cargo1), null, new ArrayList<>()));
			// Spot cargo
			Assertions.assertTrue(checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), charterInMarket_1, 0, cargo1), null, new ArrayList<>()));
		});
	}

	private AllowedVesselPermissionConstraintChecker getChecker(final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge) {
		return MicroTestUtils.getChecker(scenarioToOptimiserBridge, AllowedVesselPermissionConstraintChecker.class);
	}
}