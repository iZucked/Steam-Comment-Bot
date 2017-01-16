/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.RestrictedElementsConstraintChecker;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;

@SuppressWarnings("unused")
@RunWith(value = ShiroRunner.class)
public class PortAndContractRestrictionsTests extends AbstractMicroTestCase {

	private static List<String> requiredFeatures = Lists.newArrayList();
	private static List<String> addedFeatures = new LinkedList<>();

	@BeforeClass
	public static void hookIn() {
		for (final String feature : requiredFeatures) {
			if (!LicenseFeatures.isPermitted("features:" + feature)) {
				LicenseFeatures.addFeatureEnablements(feature);
				addedFeatures.add(feature);
			}
		}
	}

	@AfterClass
	public static void hookOut() {
		for (final String feature : addedFeatures) {
			LicenseFeatures.removeFeatureEnablements(feature);
		}
		addedFeatures.clear();
	}

	@Override
	public void constructor() throws MalformedURLException {

		super.constructor();
		// Set a default prompt in the past
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 3, 1));
	}

	/**
	 * Test the initial case with no restrictions
	 */
	@Test
	@Category({ MicroTest.class })
	public void testUnrestrictedCase() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

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
			Assert.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestricted_SlotDischarge_Port() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPort("Sakai"), null, entity, "7")//
				.with(s -> s.setOverrideRestrictions(true)) //
				.with(s -> s.setRestrictedListsArePermissive(false)) //
				.with(s -> s.getRestrictedPorts().add(portFinder.findPort("Point Fortin"))) //
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedCheckers);
			Assert.assertEquals(1, failedCheckers.size());
			Assert.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestricted_SlotDischarge_Port_Unshipped() throws Exception {
		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("Vessel", vesselClass);

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeDESPurchase("L", true, LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), null, entity, "5", vessel) //
				.with(s -> s.setShippingDaysRestriction(60)) //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPort("Sakai"), null, entity, "7")//
				.with(s -> s.setOverrideRestrictions(true)) //
				.with(s -> s.setRestrictedListsArePermissive(false)) //
				.with(s -> s.getRestrictedPorts().add(portFinder.findPort("Point Fortin"))) //
				.build() //
				// Cargo
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedCheckers);
			Assert.assertEquals(1, failedCheckers.size());
			Assert.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestricted_SlotLoad_Port() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.with(s -> s.setOverrideRestrictions(true)) //
				.with(s -> s.setRestrictedListsArePermissive(false)) //
				.with(s -> s.getRestrictedPorts().add(portFinder.findPort("Sakai"))) //
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
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedCheckers);
			Assert.assertEquals(1, failedCheckers.size());
			Assert.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestricted_SlotLoad_Port_Unshipped() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("Vessel", vesselClass);

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.with(s -> s.setOverrideRestrictions(true)) //
				.with(s -> s.setRestrictedListsArePermissive(false)) //
				.with(s -> s.getRestrictedPorts().add(portFinder.findPort("Sakai"))) //
				.build() //
				// Discharge
				.makeFOBSale("D", true, LocalDate.of(2017, 1, 13), portFinder.findPort("Sakai"), null, entity, "7", null)//
				.build() //
				// Cargo
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedCheckers);
			Assert.assertEquals(1, failedCheckers.size());
			Assert.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestricted_SlotPurchaseContract_Port() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		final PurchaseContract contract = commercialModelBuilder.makeExpressionPurchaseContract("P", entity, "5");
		contract.setRestrictedListsArePermissive(false);
		contract.getRestrictedPorts().add(portFinder.findPort("Sakai"));

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), contract, null, null) //
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
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedCheckers);
			Assert.assertEquals(1, failedCheckers.size());
			Assert.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestricted_SlotPurchaseContract_Port_Unshipped() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("Vessel", vesselClass);

		final PurchaseContract contract = commercialModelBuilder.makeExpressionPurchaseContract("P", entity, "5");
		contract.setRestrictedListsArePermissive(false);
		contract.getRestrictedPorts().add(portFinder.findPort("Sakai"));

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeDESPurchase("L", true, LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), contract, null, null, vessel) //
				.with(s -> s.setShippingDaysRestriction(60)) //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPort("Sakai"), null, entity, "7")//
				.build() //
				// Cargo
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedCheckers);
			Assert.assertEquals(1, failedCheckers.size());
			Assert.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestricted_SlotSalesContract_Port() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		final SalesContract contract = commercialModelBuilder.makeExpressionSalesContract("P", entity, "7");
		contract.setRestrictedListsArePermissive(false);
		contract.getRestrictedPorts().add(portFinder.findPort("Point Fortin"));

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPort("Sakai"), contract, null, null)//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedCheckers);
			Assert.assertEquals(1, failedCheckers.size());
			Assert.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestricted_SlotSalesContract_Port_Unshipped() throws Exception {

		final SalesContract contract = commercialModelBuilder.makeExpressionSalesContract("P", entity, "7");
		contract.setRestrictedListsArePermissive(false);
		contract.getRestrictedPorts().add(portFinder.findPort("Point Fortin"));

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				// Discharge
				.makeFOBSale("D", true, LocalDate.of(2017, 1, 13), portFinder.findPort("Sakai"), contract, null, null, null)//
				.build() //
				// Cargo
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedCheckers);
			Assert.assertEquals(1, failedCheckers.size());
			Assert.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestricted_SpotFOBPurchase_Port() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		final FOBPurchasesMarket spotMarket = spotMarketsModelBuilder.makeFOBPurchaseMarket("Spot", portFinder.findPort("Point Fortin"), entity, "5", 22.8) //
				.withVolumeLimits(0, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		spotMarket.getRestrictedPorts().add(portFinder.findPort("Sakai"));

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeMarketFOBPurchase("L", spotMarket, YearMonth.of(2017, 1), portFinder.findPort("Point Fortin")) //
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
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedCheckers);
			Assert.assertEquals(1, failedCheckers.size());
			Assert.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestricted_SpotFOBPurchase_Port_Unshipped() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		final FOBPurchasesMarket spotMarket = spotMarketsModelBuilder.makeFOBPurchaseMarket("Spot", portFinder.findPort("Point Fortin"), entity, "5", 22.8) //
				.withVolumeLimits(0, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		spotMarket.getRestrictedPorts().add(portFinder.findPort("Sakai"));

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeMarketFOBPurchase("L", spotMarket, YearMonth.of(2017, 1), portFinder.findPort("Point Fortin")) //
				.build() //
				// Discharge
				.makeFOBSale("D", true, LocalDate.of(2017, 1, 13), portFinder.findPort("Sakai"), null, entity, "7", null)//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedCheckers);
			Assert.assertEquals(1, failedCheckers.size());
			Assert.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestricted_SpotDESSale_Port() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		final DESSalesMarket spotMarket = spotMarketsModelBuilder.makeDESSaleMarket("Spot", portFinder.findPort("Sakai"), entity, "7") //
				.withVolumeLimits(0, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		spotMarket.getRestrictedPorts().add(portFinder.findPort("Point Fortin"));

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				// Discharge
				.makeMarketDESSale("D", spotMarket, YearMonth.of(2017, 2), portFinder.findPort("Sakai"))//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedCheckers);
			Assert.assertEquals(1, failedCheckers.size());
			Assert.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestricted_SpotDESSale_Port_Unshipped() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("Vessel", vesselClass);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		final DESSalesMarket spotMarket = spotMarketsModelBuilder.makeDESSaleMarket("Spot", portFinder.findPort("Sakai"), entity, "7") //
				.withVolumeLimits(0, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		spotMarket.getRestrictedPorts().add(portFinder.findPort("Point Fortin"));

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeDESPurchase("L", true, LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), null, entity, "5", vessel) //
				.with(s -> s.setShippingDaysRestriction(60)) //
				.build() //
				// Discharge
				.makeMarketDESSale("D", spotMarket, YearMonth.of(2017, 2), portFinder.findPort("Sakai"))//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedCheckers);
			Assert.assertEquals(1, failedCheckers.size());
			Assert.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestricted_SlotDischarge_Contract() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		PurchaseContract contract = commercialModelBuilder.makeExpressionPurchaseContract("Contract", entity, "5");

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), contract, null, null) //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPort("Sakai"), null, entity, "7")//
				.with(s -> s.setOverrideRestrictions(true)) //
				.with(s -> s.setRestrictedListsArePermissive(false)) //
				.with(s -> s.getRestrictedContracts().add(contract)) //
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedCheckers);
			Assert.assertEquals(1, failedCheckers.size());
			Assert.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestricted_SlotDischarge_Contract_Unshipped() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("Vessel", vesselClass);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		PurchaseContract contract = commercialModelBuilder.makeExpressionPurchaseContract("Contract", entity, "5");

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeDESPurchase("L", true, LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), contract, null, null, vessel) //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPort("Sakai"), null, entity, "7")//
				.with(s -> s.setOverrideRestrictions(true)) //
				.with(s -> s.setRestrictedListsArePermissive(false)) //
				.with(s -> s.getRestrictedContracts().add(contract)) //
				.build() //
				// Cargo
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedCheckers);
			Assert.assertEquals(1, failedCheckers.size());
			Assert.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestricted_SlotLoad_Contract() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		SalesContract contract = commercialModelBuilder.makeExpressionSalesContract("Contract", entity, "7");

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.with(s -> s.setOverrideRestrictions(true)) //
				.with(s -> s.setRestrictedListsArePermissive(false)) //
				.with(s -> s.getRestrictedContracts().add(contract)) //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPort("Sakai"), contract, null, null)//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedCheckers);
			Assert.assertEquals(1, failedCheckers.size());
			Assert.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestricted_SlotLoad_Contract_Unshipped() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		SalesContract contract = commercialModelBuilder.makeExpressionSalesContract("Contract", entity, "7");

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.with(s -> s.setOverrideRestrictions(true)) //
				.with(s -> s.setRestrictedListsArePermissive(false)) //
				.with(s -> s.getRestrictedContracts().add(contract)) //
				.build() //
				// Discharge
				.makeFOBSale("D", true, LocalDate.of(2017, 1, 13), portFinder.findPort("Sakai"), contract, null, null, null)//
				.build() //
				// Cargo
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedCheckers);
			Assert.assertEquals(1, failedCheckers.size());
			Assert.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestricted_SlotPurchaseContract_Contract() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		SalesContract salesContract = commercialModelBuilder.makeExpressionSalesContract("Contract", entity, "7");

		final PurchaseContract contract = commercialModelBuilder.makeExpressionPurchaseContract("P", entity, "5");
		contract.setRestrictedListsArePermissive(false);
		contract.getRestrictedContracts().add(salesContract);

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), contract, null, null) //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPort("Sakai"), salesContract, null, null)//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedCheckers);
			Assert.assertEquals(1, failedCheckers.size());
			Assert.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestricted_SlotSalesContract_Contract() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		PurchaseContract purchaseContract = commercialModelBuilder.makeExpressionPurchaseContract("Contract", entity, "5");

		final SalesContract contract = commercialModelBuilder.makeExpressionSalesContract("P", entity, "7");

		contract.setRestrictedListsArePermissive(false);
		contract.getRestrictedContracts().add(purchaseContract);

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), purchaseContract, null, null) //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPort("Sakai"), contract, null, null)//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedCheckers);
			Assert.assertEquals(1, failedCheckers.size());
			Assert.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestricted_SpotFOBPurchase_Contract() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		SalesContract contract = commercialModelBuilder.makeExpressionSalesContract("C", entity, "7");

		final FOBPurchasesMarket spotMarket = spotMarketsModelBuilder.makeFOBPurchaseMarket("Spot", portFinder.findPort("Point Fortin"), entity, "5", 22.8) //
				.withVolumeLimits(0, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		spotMarket.getRestrictedContracts().add(contract);

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeMarketFOBPurchase("L", spotMarket, YearMonth.of(2017, 1), portFinder.findPort("Point Fortin")) //
				.build() //
				// Discharge
				.makeDESSale("D", LocalDate.of(2017, 2, 13), portFinder.findPort("Sakai"), contract, null, null)//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedCheckers);
			Assert.assertEquals(1, failedCheckers.size());
			Assert.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testRestricted_SpotDESSale_Contract() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		PurchaseContract contract = commercialModelBuilder.makeExpressionPurchaseContract("C", entity, "5");

		final DESSalesMarket spotMarket = spotMarketsModelBuilder.makeDESSaleMarket("Spot", portFinder.findPort("Sakai"), entity, "7") //
				.withVolumeLimits(0, 3_000_000, VolumeUnits.MMBTU) //
				.build();

		spotMarket.getRestrictedContracts().add(contract);

		// Construct the cargo scenario
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				// Load
				.makeFOBPurchase("L", LocalDate.of(2017, 1, 13), portFinder.findPort("Point Fortin"), contract, null, null) //
				.build() //
				// Discharge
				.makeMarketDESSale("D", spotMarket, YearMonth.of(2017, 2), portFinder.findPort("Sakai"))//
				.build() //
				// Cargo
				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			final List<IConstraintChecker> failedCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedCheckers);
			Assert.assertEquals(1, failedCheckers.size());
			Assert.assertTrue(failedCheckers.get(0) instanceof RestrictedElementsConstraintChecker);
		});
	}

}