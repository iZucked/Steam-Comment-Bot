/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.tests.microcases.period.AbstractPeriodTestCase;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.util.CommercialModelFinder;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.util.FleetModelBuilder;
import com.mmxlabs.models.lng.fleet.util.FleetModelFinder;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.util.PortModelFinder;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.util.PricingModelBuilder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelBuilder;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGEvaluationTransformerUnit;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintChecker;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

@RunWith(value = ShiroRunner.class)
public class VesselRestrictionsTest extends AbstractPeriodTestCase {

	private LNGScenarioModel lngScenarioModel;
	private ScenarioModelFinder scenarioModelFinder;
	private ScenarioModelBuilder scenarioModelBuilder;
	private CommercialModelFinder commercialModelFinder;
	private FleetModelFinder fleetModelFinder;
	private PortModelFinder portFinder;
	private CargoModelBuilder cargoModelBuilder;
	private FleetModelBuilder fleetModelBuilder;
	private SpotMarketsModelBuilder spotMarketsModelBuilder;
	private PricingModelBuilder pricingModelBuilder;
	private BaseLegalEntity entity;

	@Before
	public void constructor() throws MalformedURLException {

		lngScenarioModel = importReferenceData();

		scenarioModelFinder = new ScenarioModelFinder(lngScenarioModel);
		scenarioModelBuilder = new ScenarioModelBuilder(lngScenarioModel);

		commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
		fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
		portFinder = scenarioModelFinder.getPortModelFinder();

		pricingModelBuilder = scenarioModelBuilder.getPricingModelBuilder();
		cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();
		spotMarketsModelBuilder = scenarioModelBuilder.getSpotMarketsModelBuilder();

		entity = commercialModelFinder.findEntity("Shipping");

	}

	@After
	public void destructor() {
		lngScenarioModel = null;
		scenarioModelFinder = null;
		scenarioModelBuilder = null;
		commercialModelFinder = null;
		fleetModelFinder = null;
		portFinder = null;
		cargoModelBuilder = null;
		fleetModelBuilder = null;
		spotMarketsModelBuilder = null;
		pricingModelBuilder = null;
		entity = null;
	}

	@Test
	@Category({ MicroTest.class })
	public void testNoRestrictions() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterIndex charterIndex1 = pricingModelBuilder.createCharterIndex("CharterIndex1", "$/day", 50_000);
		// final CharterIndex charterIndex2 = pricingModelBuilder.createCharterIndex("CharterIndex2", "$/day", 100_000);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, charterIndex1, 1);

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

		runTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ResourceAllocationConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assert.assertTrue(checker.checkConstraints(createSequences(scenarioToOptimiserBridge, cargo1, vesselAvailability1), null));
			// Nominal vessel
			Assert.assertTrue(checker.checkConstraints(createSequences(scenarioToOptimiserBridge, cargo1, charterInMarket_1, -1), null));
			// Spot cargo
			Assert.assertTrue(checker.checkConstraints(createSequences(scenarioToOptimiserBridge, cargo1, charterInMarket_1, 0), null));

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testLoadVesselClassRestrictions_NoInstance() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final VesselClass vesselClass2 = fleetModelFinder.findVesselClass("STEAM-138");

		final CharterIndex charterIndex1 = pricingModelBuilder.createCharterIndex("CharterIndex1", "$/day", 50_000);
		// final CharterIndex charterIndex2 = pricingModelBuilder.createCharterIndex("CharterIndex2", "$/day", 100_000);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, charterIndex1, 1);

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

		runTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ResourceAllocationConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assert.assertFalse(checker.checkConstraints(createSequences(scenarioToOptimiserBridge, cargo1, vesselAvailability1), null));
			// Nominal vessel
			Assert.assertFalse(checker.checkConstraints(createSequences(scenarioToOptimiserBridge, cargo1, charterInMarket_1, -1), null));
			// Spot cargo
			Assert.assertFalse(checker.checkConstraints(createSequences(scenarioToOptimiserBridge, cargo1, charterInMarket_1, 0), null));

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testLoadVesselClassRestrictions_Fleet() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final VesselClass vesselClass2 = fleetModelFinder.findVesselClass("STEAM-138");

		final CharterIndex charterIndex1 = pricingModelBuilder.createCharterIndex("CharterIndex1", "$/day", 50_000);
		// final CharterIndex charterIndex2 = pricingModelBuilder.createCharterIndex("CharterIndex2", "$/day", 100_000);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, charterIndex1, 1);

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

		runTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ResourceAllocationConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assert.assertTrue(checker.checkConstraints(createSequences(scenarioToOptimiserBridge, cargo1, vesselAvailability1), null));
			// Nominal vessel
			Assert.assertFalse(checker.checkConstraints(createSequences(scenarioToOptimiserBridge, cargo1, charterInMarket_1, -1), null));
			// Spot cargo
			Assert.assertFalse(checker.checkConstraints(createSequences(scenarioToOptimiserBridge, cargo1, charterInMarket_1, 0), null));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testLoadVesselRestrictions_VesselExists() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final VesselClass vesselClass2 = fleetModelFinder.findVesselClass("STEAM-138");

		final CharterIndex charterIndex1 = pricingModelBuilder.createCharterIndex("CharterIndex1", "$/day", 50_000);
		// final CharterIndex charterIndex2 = pricingModelBuilder.createCharterIndex("CharterIndex2", "$/day", 100_000);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, charterIndex1, 1);

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

		runTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ResourceAllocationConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assert.assertTrue(checker.checkConstraints(createSequences(scenarioToOptimiserBridge, cargo1, vesselAvailability1), null));
			// Nominal vessel
			Assert.assertFalse(checker.checkConstraints(createSequences(scenarioToOptimiserBridge, cargo1, charterInMarket_1, -1), null));
			// Spot cargo
			Assert.assertFalse(checker.checkConstraints(createSequences(scenarioToOptimiserBridge, cargo1, charterInMarket_1, 0), null));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testLoadVesselRestrictions_NoInstance() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterIndex charterIndex1 = pricingModelBuilder.createCharterIndex("CharterIndex1", "$/day", 50_000);
		// final CharterIndex charterIndex2 = pricingModelBuilder.createCharterIndex("CharterIndex2", "$/day", 100_000);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, charterIndex1, 1);

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

		runTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ResourceAllocationConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assert.assertFalse(checker.checkConstraints(createSequences(scenarioToOptimiserBridge, cargo1, vesselAvailability1), null));
			// Nominal vessel
			Assert.assertFalse(checker.checkConstraints(createSequences(scenarioToOptimiserBridge, cargo1, charterInMarket_1, -1), null));
			// Spot cargo
			Assert.assertFalse(checker.checkConstraints(createSequences(scenarioToOptimiserBridge, cargo1, charterInMarket_1, 0), null));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testLoadVesselClassRestrictions_SpotOnly() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final VesselClass vesselClass2 = fleetModelFinder.findVesselClass("STEAM-138");

		final CharterIndex charterIndex1 = pricingModelBuilder.createCharterIndex("CharterIndex1", "$/day", 50_000);
		// final CharterIndex charterIndex2 = pricingModelBuilder.createCharterIndex("CharterIndex2", "$/day", 100_000);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass2, charterIndex1, 1);

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

		runTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ResourceAllocationConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			// Real vessel
			Assert.assertFalse(checker.checkConstraints(createSequences(scenarioToOptimiserBridge, cargo1, vesselAvailability1), null));
			// Nominal vessel
			Assert.assertTrue(checker.checkConstraints(createSequences(scenarioToOptimiserBridge, cargo1, charterInMarket_1, -1), null));
			// Spot cargo
			Assert.assertTrue(checker.checkConstraints(createSequences(scenarioToOptimiserBridge, cargo1, charterInMarket_1, 0), null));
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

		runTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ResourceAllocationConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

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

		runTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ResourceAllocationConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

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

		runTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ResourceAllocationConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

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

		runTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ResourceAllocationConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

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

		runTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ResourceAllocationConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

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

		runTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ResourceAllocationConstraintChecker checker = getChecker(scenarioToOptimiserBridge);

			Assert.assertFalse(checker.checkConstraints(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences(), null));
		});
	}

	private @NonNull ISequences createSequences(final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, final Cargo cargo, final VesselAvailability vesselAvailability1) {

		@NonNull
		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

		@NonNull
		final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

		final IVesselProvider vesselProvider = dataTransformer.getInjector().getInstance(IVesselProvider.class);

		final IVesselAvailability o_vesselAvailability = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability1, IVesselAvailability.class);
		final IResource resource = vesselProvider.getResource(o_vesselAvailability);

		return createSequences(scenarioToOptimiserBridge, cargo, resource, o_vesselAvailability);
	}

	private @NonNull ISequences createSequences(final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, final Cargo cargo, final CharterInMarket charterInMarket, int spotIndex) {

		@NonNull
		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

		@NonNull
		final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

		ISpotCharterInMarket market = modelEntityMap.getOptimiserObjectNullChecked(charterInMarket, ISpotCharterInMarket.class);

		final IVesselProvider vesselProvider = dataTransformer.getInjector().getInstance(IVesselProvider.class);

		for (IResource o_resource : dataTransformer.getOptimisationData().getResources()) {
			IVesselAvailability o_vesselAvailability = vesselProvider.getVesselAvailability(o_resource);
			if (o_vesselAvailability.getSpotCharterInMarket() != market) {
				continue;
			}
			if (o_vesselAvailability.getSpotIndex() != spotIndex) {
				continue;
			}
			return createSequences(scenarioToOptimiserBridge, cargo, o_resource, o_vesselAvailability);
		}
		Assert.fail("Unable to find spot market vessel");
		throw new IllegalStateException();
	}

	private @NonNull ISequences createSequences(final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, final Cargo cargo, final IResource o_resource, IVesselAvailability o_vesselAvailability) {
		@NonNull
		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
		@NonNull
		final IOptimisationData optimisationData = dataTransformer.getOptimisationData();
		final ModifiableSequences sequences = new ModifiableSequences(optimisationData.getResources());

		@NonNull
		final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

		final IPortSlotProvider portSlotProvider = dataTransformer.getInjector().getInstance(IPortSlotProvider.class);
		final IStartEndRequirementProvider startEndRequirementProvider = dataTransformer.getInjector().getInstance(IStartEndRequirementProvider.class);

		@NonNull
		final IModifiableSequence modifiableSequence = sequences.getModifiableSequence(o_resource);
		modifiableSequence.add(startEndRequirementProvider.getStartElement(o_resource));

		for (final Slot slot : cargo.getSlots()) {
			final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(slot, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(o_slot);
			modifiableSequence.add(element);
		}

		modifiableSequence.add(startEndRequirementProvider.getEndElement(o_resource));

		return sequences;
	}

	private ResourceAllocationConstraintChecker getChecker(final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge) {
		final Pair<LNGEvaluationTransformerUnit, List<IConstraintChecker>> constraintCheckers = MicroTestUtils.getConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer());

		for (final IConstraintChecker checker : constraintCheckers.getSecond()) {
			if (checker instanceof ResourceAllocationConstraintChecker) {
				return (ResourceAllocationConstraintChecker) checker;
			}

		}
		Assert.fail("Unable to find ResourceAllocationConstraintChecker");
		throw new IllegalStateException();
	}

	private void runTest(Consumer<LNGScenarioRunner> checker) {
		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final OptimiserSettings optimiserSettings = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		// Generate internal data
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimiserSettings, new TransformerExtensionTestBootstrapModule(), null, false,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
			scenarioRunner.evaluateInitialState();
			checker.accept(scenarioRunner);
		} finally {
			executorService.shutdownNow();
		}
	}

}