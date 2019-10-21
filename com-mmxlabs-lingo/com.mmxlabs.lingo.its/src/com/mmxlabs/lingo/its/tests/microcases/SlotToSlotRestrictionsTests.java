/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.common.collect.Lists;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.transformer.extensions.restrictedslots.RestrictedSlotsConstraintChecker;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;

@SuppressWarnings("unused")
@ExtendWith(value = ShiroRunner.class)
public class SlotToSlotRestrictionsTests extends AbstractMicroTestCase {

	private static List<String> requiredFeatures = Lists.newArrayList();
	private static List<String> addedFeatures = new LinkedList<>();

	@BeforeAll
	public static void hookIn() {
		for (final String feature : requiredFeatures) {
			if (!LicenseFeatures.isPermitted("features:" + feature)) {
				LicenseFeatures.addFeatureEnablements(feature);
				addedFeatures.add(feature);
			}
		}
	}

	@AfterAll
	public static void hookOut() {
		for (final String feature : addedFeatures) {
			LicenseFeatures.removeFeatureEnablements(feature);
		}
		addedFeatures.clear();
	}

	@BeforeEach
	@Override
	public void constructor() throws Exception {

		super.constructor();
		// Set a default prompt in the past
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 3, 1));
	}

	/**
	 * Test the initial case with no restrictions
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testUnrestrictedCase() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPort("Sakai"), null, entity, "7")//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assertions.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestrictedLoadSlot() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		// Construct the cargo scenario
		final List<Slot> slots = new ArrayList<Slot>();
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.with(s -> slots.add(s))//
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPort("Sakai"), null, entity, "7")//
				.with(s -> slots.add(s)).build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();
		slots.get(0).getRestrictedSlots().add(slots.get(1));

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedSlotsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestrictedDischargeSlot() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		// Construct the cargo scenario
		final List<Slot> slots = new ArrayList<Slot>();
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.with(s -> slots.add(s))//
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPort("Sakai"), null, entity, "7")//
				.with(s -> slots.add(s)).build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();
		slots.get(1).getRestrictedSlots().add(slots.get(0));

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedSlotsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestrictedLoadSlot_Unshipped() throws Exception {
		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		// Construct the cargo scenario
		final List<Slot> slots = new ArrayList<Slot>();
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeDESPurchase("L", DESPurchaseDealType.DIVERT_FROM_SOURCE, LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), null, entity, "5", vessel) //
				.with(s -> s.setShippingDaysRestriction(60)) //
				.with(s -> slots.add(s)).build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPort("Sakai"), null, entity, "7")//
				.with(s -> slots.add(s)).build() //
				// Cargo
				.build();
		slots.get(0).getRestrictedSlots().add(slots.get(1));

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedSlotsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestrictedDischargeSlot_Unshipped() throws Exception {
		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		// Construct the cargo scenario
		final List<Slot> slots = new ArrayList<Slot>();
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeDESPurchase("L", DESPurchaseDealType.DIVERT_FROM_SOURCE, LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), null, entity, "5", vessel) //
				.with(s -> s.setShippingDaysRestriction(60)) //
				.with(s -> slots.add(s)).build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPort("Sakai"), null, entity, "7")//
				.with(s -> slots.add(s)).build() //
				// Cargo
				.build();
		slots.get(1).getRestrictedSlots().add(slots.get(0));

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedSlotsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_LoadSpotFOBPurchase() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		final FOBPurchasesMarket spotMarket = spotMarketsModelBuilder.makeFOBPurchaseMarket("Spot", portFinder.findPort("Point Fortin"), entity, "5", 22.8) //
				.withVolumeLimits(0, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		// Construct the cargo scenario
		final List<Slot> slots = new ArrayList<Slot>();
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeMarketFOBPurchase("L", spotMarket, YearMonth.of(2017, 1), portFinder.findPort("Point Fortin")) //
				.with(s -> slots.add(s)).build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPort("Sakai"), null, entity, "7")//
				.with(s -> slots.add(s)).build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();
		slots.get(0).getRestrictedSlots().add(slots.get(1));

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedSlotsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_LoadSpotFOBPurchase_Unshipped() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		final FOBPurchasesMarket spotMarket = spotMarketsModelBuilder.makeFOBPurchaseMarket("Spot", portFinder.findPort("Point Fortin"), entity, "5", 22.8) //
				.withVolumeLimits(0, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		// Construct the cargo scenario
		final List<Slot> slots = new ArrayList<Slot>();
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeMarketFOBPurchase("L", spotMarket, YearMonth.of(2017, 1), portFinder.findPort("Point Fortin")) //
				.with(s -> slots.add(s)).build() //
				// Discharge
				.makeFOBSale("D", FOBSaleDealType.DIVERT_TO_DEST, LocalDate.of(2017, 1, 13), portFinder.findPort("Sakai"), null, entity, "7", null)//
				.with(s -> slots.add(s)).build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();
		slots.get(0).getRestrictedSlots().add(slots.get(1));

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedSlotsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_DischargeSpotDESSale() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		final DESSalesMarket spotMarket = spotMarketsModelBuilder.makeDESSaleMarket("Spot", portFinder.findPort("Sakai"), entity, "7") //
				.withVolumeLimits(0, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		// Construct the cargo scenario
		final List<Slot> slots = new ArrayList<Slot>();
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.with(s -> slots.add(s)).build() //
				// Discharge
				.makeMarketDESSale("D", spotMarket, YearMonth.of(2017, 2), portFinder.findPort("Sakai"))//
				.with(s -> slots.add(s)).build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();
		slots.get(0).getRestrictedSlots().add(slots.get(1));

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedSlotsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_DischargeSpotDESSale_Unshipped() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		final DESSalesMarket spotMarket = spotMarketsModelBuilder.makeDESSaleMarket("Spot", portFinder.findPort("Sakai"), entity, "7") //
				.withVolumeLimits(0, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		// Construct the cargo scenario
		final List<Slot> slots = new ArrayList<Slot>();
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeDESPurchase("L", DESPurchaseDealType.DIVERT_FROM_SOURCE, LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), null, entity, "5", vessel) //
				.with(s -> s.setShippingDaysRestriction(60)) //
				.with(s -> slots.add(s))//
				.build() //
				// Discharge
				.makeMarketDESSale("D", spotMarket, YearMonth.of(2017, 2), portFinder.findPort("Sakai"))//
				.with(s -> slots.add(s))//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();
		slots.get(0).getRestrictedSlots().add(slots.get(1));

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedSlotsConstraintChecker);
		});
	}

}