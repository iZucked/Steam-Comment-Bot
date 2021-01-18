/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.YearMonth;
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
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.RestrictedElementsConstraintChecker;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;

/**
 * Similar to {@link PortAndContractRestrictionsTests}, but permissive mode.
 * 
 * @author Simon Goodall
 *
 */
@SuppressWarnings("unused")
@ExtendWith(ShiroRunner.class)
public class PortAndContractPermissiveRestrictionsTests extends AbstractMicroTestCase {

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
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7")//
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
	public void testRestricted_SlotDischarge_Port() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7")//
				.with(s -> s.setRestrictedPortsOverride(true)) //
				.with(s -> s.setRestrictedPortsArePermissive(true)) //
				.with(s -> s.getRestrictedPorts().add(portFinder.findPortById(InternalDataConstants.PORT_BONNY))) //
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_SlotDischarge_Port_Unshipped() throws Exception {
		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeDESPurchase("L", DESPurchaseDealType.DIVERT_FROM_SOURCE, LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8, vessel) //
				.with(s -> s.setShippingDaysRestriction(60)) //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7")//
				.with(s -> s.setRestrictedPortsOverride(true)) //
				.with(s -> s.setRestrictedPortsArePermissive(true)) //
				.with(s -> s.getRestrictedPorts().add(portFinder.findPortById(InternalDataConstants.PORT_BONNY))) //
				.build() //
				// Cargo
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_SlotLoad_Port() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.with(s -> s.setRestrictedPortsOverride(true)) //
				.with(s -> s.setRestrictedPortsArePermissive(true)) //
				.with(s -> s.getRestrictedPorts().add(portFinder.findPortById(InternalDataConstants.PORT_DRAGON))) //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7")//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_SlotLoad_Port_Unshipped() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.with(s -> s.setRestrictedPortsOverride(true)) //
				.with(s -> s.setRestrictedPortsArePermissive(true)) //
				.with(s -> s.getRestrictedPorts().add(portFinder.findPortById(InternalDataConstants.PORT_DRAGON))) //
				.build() //
				// Discharge
				.makeFOBSale("D", FOBSaleDealType.SOURCE_WITH_DEST, LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7", null)//
				.build() //
				// Cargo
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_SlotPurchaseContract_Port() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		final PurchaseContract contract = commercialModelBuilder.makeExpressionPurchaseContract("P", entity, "5");
		contract.setRestrictedPortsArePermissive(true);
		contract.getRestrictedPorts().add(portFinder.findPortById(InternalDataConstants.PORT_DRAGON));

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), contract, null, null) //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7")//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_SlotPurchaseContract_Port_Unshipped() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final PurchaseContract contract = commercialModelBuilder.makeExpressionPurchaseContract("P", entity, "5");
		contract.setRestrictedPortsArePermissive(true);
		contract.getRestrictedPorts().add(portFinder.findPortById(InternalDataConstants.PORT_DRAGON));

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeDESPurchase("L", DESPurchaseDealType.DIVERT_FROM_SOURCE, LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), contract, null, null, 22.8, vessel) //
				.with(s -> s.setShippingDaysRestriction(60)) //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7")//
				.build() //
				// Cargo
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_SlotSalesContract_Port() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		
		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		final SalesContract contract = commercialModelBuilder.makeExpressionSalesContract("P", entity, "7");
		contract.setRestrictedPortsArePermissive(true);
		contract.getRestrictedPorts().add(portFinder.findPortById(InternalDataConstants.PORT_BONNY));

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), contract, null, null)//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_SlotSalesContract_Port_Unshipped() throws Exception {

		final SalesContract contract = commercialModelBuilder.makeExpressionSalesContract("P", entity, "7");
		contract.setRestrictedPortsArePermissive(true);
		contract.getRestrictedPorts().add(portFinder.findPortById(InternalDataConstants.PORT_BONNY));

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				// Discharge
				.makeFOBSale("D", FOBSaleDealType.SOURCE_WITH_DEST, LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), contract, null, null, null)//
				.build() //
				// Cargo
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_SpotFOBPurchase_Port() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		
		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		final FOBPurchasesMarket spotMarket = spotMarketsModelBuilder.makeFOBPurchaseMarket("Spot", portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), entity, "5", 22.8) //
				.withVolumeLimits(0, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		spotMarket.setRestrictedPortsArePermissive(true);
		spotMarket.getRestrictedPorts().add(portFinder.findPortById(InternalDataConstants.PORT_DRAGON));

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeMarketFOBPurchase("L", spotMarket, YearMonth.of(2017, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7")//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_SpotFOBPurchase_Port_Unshipped() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		final FOBPurchasesMarket spotMarket = spotMarketsModelBuilder.makeFOBPurchaseMarket("Spot", portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), entity, "5", 22.8) //
				.withVolumeLimits(0, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		spotMarket.setRestrictedPortsArePermissive(true);
		spotMarket.getRestrictedPorts().add(portFinder.findPortById(InternalDataConstants.PORT_DRAGON));

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeMarketFOBPurchase("L", spotMarket, YearMonth.of(2017, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.build() //
				// Discharge
				.makeFOBSale("D", FOBSaleDealType.DIVERT_TO_DEST, LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7", null)//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_SpotDESSale_Port() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		final DESSalesMarket spotMarket = spotMarketsModelBuilder.makeDESSaleMarket("Spot", portFinder.findPortById(InternalDataConstants.PORT_SAKAI), entity, "7") //
				.withVolumeLimits(0, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		spotMarket.setRestrictedPortsArePermissive(true);
		spotMarket.getRestrictedPorts().add(portFinder.findPortById(InternalDataConstants.PORT_BONNY));

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				// Discharge
				.makeMarketDESSale("D", spotMarket, YearMonth.of(2017, 2), portFinder.findPortById(InternalDataConstants.PORT_SAKAI))//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_SpotDESSale_Port_Unshipped() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		final DESSalesMarket spotMarket = spotMarketsModelBuilder.makeDESSaleMarket("Spot", portFinder.findPortById(InternalDataConstants.PORT_SAKAI), entity, "7") //
				.withVolumeLimits(0, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		spotMarket.setRestrictedPortsArePermissive(true);
		spotMarket.getRestrictedPorts().add(portFinder.findPortById(InternalDataConstants.PORT_BONNY));

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeDESPurchase("L", DESPurchaseDealType.DIVERT_FROM_SOURCE, LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 22.8, vessel) //
				.with(s -> s.setShippingDaysRestriction(60)) //
				.build() //
				// Discharge
				.makeMarketDESSale("D", spotMarket, YearMonth.of(2017, 2), portFinder.findPortById(InternalDataConstants.PORT_SAKAI))//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_SlotDischarge_Contract() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		final PurchaseContract contract = commercialModelBuilder.makeExpressionPurchaseContract("Contract", entity, "5");
		final PurchaseContract contract2 = commercialModelBuilder.makeExpressionPurchaseContract("Contract2", entity, "5");

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), contract, null, null) //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7")//
				.with(s -> s.setRestrictedContractsOverride(true)) //
				.with(s -> s.setRestrictedContractsArePermissive(true)) //
				.with(s -> s.getRestrictedContracts().add(contract2)) //
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_SlotDischarge_Contract_Unshipped() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		final PurchaseContract contract = commercialModelBuilder.makeExpressionPurchaseContract("Contract", entity, "5");
		final PurchaseContract contract2 = commercialModelBuilder.makeExpressionPurchaseContract("Contract", entity, "5");

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeDESPurchase("L", DESPurchaseDealType.DIVERT_FROM_SOURCE, LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), contract, null, null, 22.8, vessel) //
				.with(s -> s.setShippingDaysRestriction(60)) //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7")//
				.with(s -> s.setRestrictedContractsOverride(true)) //
				.with(s -> s.setRestrictedContractsArePermissive(true)) //
				.with(s -> s.getRestrictedContracts().add(contract2)) //
				.build() //
				// Cargo
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_SlotLoad_Contract() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		
		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		final SalesContract contract = commercialModelBuilder.makeExpressionSalesContract("Contract", entity, "7");
		final SalesContract contract2 = commercialModelBuilder.makeExpressionSalesContract("Contract", entity, "7");

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.with(s -> s.setRestrictedContractsOverride(true)) //
				.with(s -> s.setRestrictedContractsArePermissive(true)) //
				.with(s -> s.getRestrictedContracts().add(contract2)) //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), contract, null, null)//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_SlotLoad_Contract_Unshipped() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		final SalesContract contract = commercialModelBuilder.makeExpressionSalesContract("Contract", entity, "7");
		final SalesContract contract2 = commercialModelBuilder.makeExpressionSalesContract("Contract", entity, "7");

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.with(s -> s.setRestrictedContractsOverride(true)) //
				.with(s -> s.setRestrictedContractsArePermissive(true)) //
				.with(s -> s.getRestrictedContracts().add(contract2)) //
				.build() //
				// Discharge
				.makeFOBSale("D", FOBSaleDealType.SOURCE_WITH_DEST, LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), contract, null, null, null)//
				.build() //
				// Cargo
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_SlotPurchaseContract_Contract() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		
		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		final SalesContract salesContract = commercialModelBuilder.makeExpressionSalesContract("C1", entity, "7");
		final SalesContract salesContract2 = commercialModelBuilder.makeExpressionSalesContract("C2", entity, "7");

		final PurchaseContract contract = commercialModelBuilder.makeExpressionPurchaseContract("P", entity, "5");
		contract.setRestrictedContractsArePermissive(true);
		contract.getRestrictedContracts().add(salesContract2);

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), contract, null, null) //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), salesContract, null, null)//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_SlotSalesContract_Contract() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		final PurchaseContract purchaseContract = commercialModelBuilder.makeExpressionPurchaseContract("Contract", entity, "5");
		final PurchaseContract purchaseContract2 = commercialModelBuilder.makeExpressionPurchaseContract("Contract", entity, "5");

		final SalesContract contract = commercialModelBuilder.makeExpressionSalesContract("P", entity, "7");

		contract.setRestrictedContractsArePermissive(true);
		contract.getRestrictedContracts().add(purchaseContract2);

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), purchaseContract, null, null) //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), contract, null, null)//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_SpotFOBPurchase_Contract() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		final SalesContract contract = commercialModelBuilder.makeExpressionSalesContract("C1", entity, "7");
		final SalesContract contract2 = commercialModelBuilder.makeExpressionSalesContract("C2", entity, "7");

		final FOBPurchasesMarket spotMarket = spotMarketsModelBuilder.makeFOBPurchaseMarket("Spot", portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), entity, "5", 22.8) //
				.withVolumeLimits(0, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		spotMarket.setRestrictedContractsArePermissive(true);
		spotMarket.getRestrictedContracts().add(contract2);

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeMarketFOBPurchase("L", spotMarket, YearMonth.of(2017, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), contract, null, null)//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testRestricted_SpotDESSale_Contract() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		final PurchaseContract contract = commercialModelBuilder.makeExpressionPurchaseContract("C1", entity, "5");
		final PurchaseContract contract2 = commercialModelBuilder.makeExpressionPurchaseContract("C2", entity, "5");

		final DESSalesMarket spotMarket = spotMarketsModelBuilder.makeDESSaleMarket("Spot", portFinder.findPortById(InternalDataConstants.PORT_SAKAI), entity, "7") //
				.withVolumeLimits(0, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		spotMarket.setRestrictedContractsArePermissive(true);
		spotMarket.getRestrictedContracts().add(contract2);

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), contract, null, null) //
				.build() //
				// Discharge
				.makeMarketDESSale("D", spotMarket, YearMonth.of(2017, 2), portFinder.findPortById(InternalDataConstants.PORT_SAKAI))//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedCheckers);
			Assertions.assertEquals(1, failedCheckers.size());
			Assertions.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

}