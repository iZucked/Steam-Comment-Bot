/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.google.inject.Injector;
import com.mmxlabs.license.features.LicenseFeatures;
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
import com.mmxlabs.models.lng.pricing.util.PricingModelBuilder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelBuilder;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AllowedVesselPermissionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

@RunWith(value = ShiroRunner.class)
public class NominalMarketTests extends AbstractPeriodTestCase {

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

	private static List<String> requiredFeatures = Lists.newArrayList("no-nominal-in-prompt", "optimisation-actionset");
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

		// Set a default prompt in the past
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 3, 1));
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

	/**
	 * Test: Move a nominal cargo onto an empty fleet vessel (needs pre-defined vessel start and end dates)
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testMoveNominalToFleet() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

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

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check cargoes removed
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			Assert.assertEquals(vesselAvailability1, optCargo1.getVesselAssignmentType());

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			// Validate the initial sequences are valid
			Assert.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		});
	}

	/**
	 * Test: Break apart cargo into open positions (even with cancellation fee)
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testMoveNominalToOpen() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "200000", 0);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "7") //
				.withOptional(true) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "5") //
				.withOptional(true) //
				.withCancellationFee("300000") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		runTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check cargoes removed
			Assert.assertEquals(0, optimiserScenario.getCargoModel().getCargoes().size());
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testForcePromptNominalToOpen_InPrompt() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "200000", 0);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "7") //
				.withOptional(true) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "5") //
				.withOptional(true) //
				.withCancellationFee("300000") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2016, 12, 5));

		runTest(true, s -> {
			// Set iterations to zero to avoid any optimisation changes and rely on the unpairing opt step
			s.getAnnealingSettings().setIterations(0);
			s.getSolutionImprovementSettings().setIterations(0);
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check cargoes removed
			Assert.assertEquals(0, optimiserScenario.getCargoModel().getCargoes().size());
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());
		});

	}

	/**
	 * Aim of test is to try and ensure a) the action set can work with these solutions correctly and b) support the filtering correctly. (I.e ensure highly profitable but nominal cargoes are not
	 * used).ZDFD HSJH U8XD
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testForcePromptNominalToOpen_InPrompt_ActionSet() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass1 = fleetModelFinder.findVesselClass("STEAM-145");
		final VesselClass vesselClass2 = fleetModelFinder.findVesselClass("STEAM-138");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass1, "200000", 0);
		final CharterInMarket charterInMarket_2a = spotMarketsModelBuilder.createCharterInMarket("CharterIn 2a", vesselClass2, "200000", 1);
		final CharterInMarket charterInMarket_2b = spotMarketsModelBuilder.createCharterInMarket("CharterIn 2b", vesselClass2, "1000", 1);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withOptional(true) //
				.withVesselRestriction(vesselClass1) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "10.0") //
				.withOptional(true) //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withOptional(true) //
				.withVesselRestriction(vesselClass1) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "10") //
				.withOptional(true) //
				.build() //
				.withVesselAssignment(charterInMarket_2a, 0, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));

		runTest(true, s -> {
			// Set iterations to zero to avoid any optimisation changes and rely on the unpairing opt step
			// s.getAnnealingSettings().setIterations(0);
			s.getSolutionImprovementSettings().setIterations(0);

			s.setBuildActionSets(true);

			// Limit action set
			s.getActionPlanSettings().setTotalEvaluations(100);
			s.getActionPlanSettings().setInRunEvaluations(100);
		}, (scenarioRunner) -> {
			return new AbstractRunnerHook() {

				@Override
				public @Nullable ISequences getPrestoredSequences(@NonNull final String phase) {
					if (IRunnerHook.PHASE_LSO.equals(phase)) {
						final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

						// Ensure all sequences has been created!

						final IModifiableSequences lsoSolution = SequenceHelper.createSequences(scenarioToOptimiserBridge);
						// Cargo should be unpaired
						SequenceHelper.addToUnused(lsoSolution, scenarioToOptimiserBridge, cargo1);
						// Cargo should move to cheaper vessel
						SequenceHelper.addSequence(lsoSolution, scenarioToOptimiserBridge, cargo2, charterInMarket_2b, 0);

						// Sequences are now empty, but still need start/end events
						SequenceHelper.addSequence(lsoSolution, scenarioToOptimiserBridge, null, charterInMarket_2a, 0);
						// Nothing left on nominals
						SequenceHelper.addSequence(lsoSolution, scenarioToOptimiserBridge, null, charterInMarket_1, -1);
						SequenceHelper.addSequence(lsoSolution, scenarioToOptimiserBridge, null, charterInMarket_2a, -1);
						SequenceHelper.addSequence(lsoSolution, scenarioToOptimiserBridge, null, charterInMarket_2b, -1);

						return lsoSolution;
					}
					return null;
				}

			};
		}, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check cargoes removed
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			Assert.assertEquals(2, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assert.assertEquals(2, optimiserScenario.getCargoModel().getDischargeSlots().size());

			Assert.assertSame(cargo2, optimiserScenario.getCargoModel().getCargoes().get(0));
			Assert.assertSame(charterInMarket_2b, cargo2.getVesselAssignmentType());

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testForcePromptNominalToOpen_OutPrompt() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "200000", 0);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "7") //
				.withOptional(true) //
				.build() // ````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "5") //
				.withOptional(true) //
				.withCancellationFee("300000") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 4));

		runTest(true, s -> {
			// Set iterations to zero to avoid any optimisation changes and rely on the unpairing opt step
			s.getAnnealingSettings().setIterations(0);
			s.getSolutionImprovementSettings().setIterations(0);

		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());
		});
	}

	/**
	 * Test: Do not move onto second cheaper nominal market.
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testMoveMultipleNominalVessels() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "50000", 0);

		// Second market much cheaper, but should not be used.
		final CharterInMarket charterInMarket_2 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 2", vesselClass, "10000", 0);

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

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check cargoes removed
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			final VesselAssignmentType vesselAssignmentType = optCargo1.getVesselAssignmentType();
			Assert.assertEquals(charterInMarket_1, vesselAssignmentType);
			Assert.assertEquals(-1, optCargo1.getSpotIndex());
		});
	}

	/**
	 * Test: Assign cargo to a nominal vessel of restricted class - initial solution should be invalid.
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testMoveNominalVesselRestriction() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final VesselClass vesselClass2 = fleetModelFinder.findVesselClass("STEAM-138");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass2, "50000", 0);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				// Forbid vessel class
				.withVesselRestriction(vesselClass) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		runTest(false, null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			// Validate the initial sequences are invalid
			final List<IConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedConstraintCheckers);

			// Expect just this one to fail
			Assert.assertEquals(1, failedConstraintCheckers.size());
			Assert.assertTrue(failedConstraintCheckers.get(0) instanceof AllowedVesselPermissionConstraintChecker);

		});
	}

	/**
	 * Test: Ensure we do not use a cheaper nominal vessel and avoid fleet vessel charter cost.
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testDoNotMoveFleetToNominal() throws Exception {

		// Create the required basic elements
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "10000", 0);

		final Vessel vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("100000") // Much more costly than nominal
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		runTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			scenarioRunner.run();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check cargoes removed
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			Assert.assertEquals(vesselAvailability1, optCargo1.getVesselAssignmentType());

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			// Validate the initial sequences are valid
			Assert.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		});
	}

	// /**
	// * Test: Ensure we do not use a cheaper nominal vessel and avoid fleet vessel charter cost.
	// *
	// * @throws Exception
	// */
	// @Test
	// @Category({ MicroTest.class })
	// public void testAssignToDefaultNominal() throws Exception {
	//
	// // Create the required basic elements
	// final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
	//
	// final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vesselClass, "10000", 0);
	// spotMarketsModelBuilder.getSpotMarketsModel().setDefaultNominalMarket(charterInMarket_1);
	//
	// // Construct the cargo scenario
	//
	// // Create cargo 1, cargo 2
	// final Cargo cargo1 = cargoModelBuilder.makeCargo() //
	// .makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
	// .build() //
	// .makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
	// .build() //
	// .build();
	//
	// runTest(scenarioRunner -> {
	// final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
	//
	// // Check spot index has been updated
	// final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
	// // Check cargoes removed
	// Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
	//
	// final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);
	//
	// Assert.assertEquals(charterInMarket_1, optCargo1.getVesselAssignmentType());
	// Assert.assertEquals(-1, optCargo1.getSpotIndex());
	// });
	// }

	private void runTest(@NonNull final Consumer<LNGScenarioRunner> checker) {
		runTest(true, null, null, checker);
	}

	private void runTest(final boolean optimise, @Nullable final Consumer<OptimiserSettings> tweaker, @Nullable final Function<LNGScenarioRunner, IRunnerHook> runnerHookFactory,
			@NonNull final Consumer<LNGScenarioRunner> checker) {
		// Create UserSettings, place cargo 2 load in boundary, cargo 2 discharge in period.
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final OptimiserSettings optimiserSettings = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);
		if (tweaker != null) {
			tweaker.accept(optimiserSettings);
		} else {
			optimiserSettings.getAnnealingSettings().setIterations(10_000);
		}

		// Generate internal data
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimiserSettings, new TransformerExtensionTestBootstrapModule(), null, false,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);

			if (runnerHookFactory != null) {
				final IRunnerHook runnerHook = runnerHookFactory.apply(scenarioRunner);
				if (runnerHook != null) {
					scenarioRunner.setRunnerHook(runnerHook);
				}
			}

			scenarioRunner.evaluateInitialState();
			if (optimise) {
				scenarioRunner.run();
			}

			checker.accept(scenarioRunner);
		} finally {
			executorService.shutdownNow();
		}
	}
}