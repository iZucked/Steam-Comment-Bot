/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AllowedVesselPermissionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PromptRoundTripVesselPermissionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.RoundTripVesselPermissionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.fitness.components.NonOptionalSlotFitnessCore;

@SuppressWarnings("unused")
@RunWith(value = ShiroRunner.class)
public class NominalMarketTests extends AbstractMicroTestCase {

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
	@Override
	public void constructor() throws Exception {

		super.constructor();
		// Set a default prompt in the past
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 3, 1));
	}

	/**
	 * Test: test a nominal vessel start and end heel is present
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testNominalHeel() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 0);

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

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);

			Assert.assertNotNull(cargoAllocation);
			final EList<Event> events = cargoAllocation.getEvents();
			final Event firstEvent = events.get(0);
			final Event lastEvent = events.get(events.size() - 1);

			// Check safety heel present.
			Assert.assertEquals(vessel.getVesselOrDelegateSafetyHeel(), firstEvent.getHeelAtStart());
			Assert.assertEquals(vessel.getVesselOrDelegateSafetyHeel(), lastEvent.getHeelAtEnd());

		});
	}

	/**
	 * Test: Test a charter-in vessel start and end heel level. (Not strictly a nominal vessel issue, but implemented parallel test here also).
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testCharterInHeel() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 1);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, 0, 1) // 0 is first market option
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);

			Assert.assertNotNull(cargoAllocation);
			final EList<Event> events = cargoAllocation.getEvents();
			final Event firstEvent = events.get(0);
			final Event lastEvent = events.get(events.size() - 1);

			// Check safety heel present.
			Assert.assertEquals(vessel.getVesselOrDelegateSafetyHeel(), firstEvent.getHeelAtStart());
			Assert.assertEquals(vessel.getVesselOrDelegateSafetyHeel(), lastEvent.getHeelAtEnd());

		});
	}

	/**
	 * Test: Make sure the slots are kept as a bound pair on the nominal. Test by swapping discharges round.
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testNominalSlotBinding() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 0);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(true, false) //
				.build();
		// Create cargo 1, cargo 2
		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 2) // -1 is nominal
				.withAssignmentFlags(true, false) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assert.assertEquals(2, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);
			final Cargo optCargo2 = optimiserScenario.getCargoModel().getCargoes().get(1);

			final LoadSlot l1 = (LoadSlot) optCargo1.getSlots().get(0);
			final DischargeSlot d1 = (DischargeSlot) optCargo1.getSlots().get(1);
			final LoadSlot l2 = (LoadSlot) optCargo2.getSlots().get(0);
			final DischargeSlot d2 = (DischargeSlot) optCargo2.getSlots().get(1);

			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			// Validate the initial sequences are valid
			Assert.assertNull(MicroTestUtils.validateConstraintCheckers(dataTransformer, initialRawSequences));

			final IModifiableSequences lsoSolution = SequenceHelper.createSequences(dataTransformer);
			SequenceHelper.addSequence(lsoSolution, dataTransformer, charterInMarket_1, -1, l1, d2, l2, d1);

			// Validate the swapped discharges are invalid
			final List<IConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateConstraintCheckers(dataTransformer, lsoSolution);
			Assert.assertNotNull(failedConstraintCheckers);

			// Expect just this one to fail
			Assert.assertEquals(2, failedConstraintCheckers.size());
			// Order doesn't really matter. Should make test robust to order change
			Assert.assertTrue(failedConstraintCheckers.get(0) instanceof RoundTripVesselPermissionConstraintChecker);
			Assert.assertTrue(failedConstraintCheckers.get(1) instanceof PromptRoundTripVesselPermissionConstraintChecker);
		});
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
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 0);

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

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
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
	 * Test: Move a nominal cargo onto an empty fleet vessel (needs pre-defined vessel start and end dates). The non-optional fitness should kick in
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testMoveNominalToFleet_LossMakingCargo_ShipOnly() throws Exception {

		// Create the required basic elements
		final Vessel source = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", source, "50000", 0);

		final Vessel vessel = fleetModelBuilder.createVesselFrom("Vessel1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		// Half the capacity! So will loose P&L by doing this.
		vessel.setCapacity(70_000);

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

		evaluateWithLSOTest(false, plan -> {
			plan.getUserSettings().setShippingOnly(true);
			ScenarioUtils.setLSOStageIterations(plan, 10_000);
			// No hill climb
			ScenarioUtils.setHillClimbStageIterations(plan, 0);
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			final long beforePNL = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), optimiserScenario.getScheduleModel().getSchedule()).getGroupProfitAndLoss().getProfitAndLoss();
			scenarioRunner.runAndApplyBest();
			final long afterPNL = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), optimiserScenario.getScheduleModel().getSchedule()).getGroupProfitAndLoss().getProfitAndLoss();

			Assert.assertTrue(beforePNL > afterPNL);

			// Check correct cargoes remain and spot index has changed.
			Assert.assertEquals(vesselAvailability1, optCargo1.getVesselAssignmentType());

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			// Validate the initial sequences are valid
			Assert.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));

			final NonOptionalSlotFitnessCore core = new NonOptionalSlotFitnessCore("");
			scenarioToOptimiserBridge.getDataTransformer().getInjector().injectMembers(core);
			core.init(scenarioToOptimiserBridge.getDataTransformer().getInjector().getInstance(IOptimisationData.class));
			core.evaluate(initialRawSequences, new EvaluationState(), null);
			Assert.assertTrue(core.getFitness() > 0);

		}, null);
	}

	/**
	 * Test: Move a nominal cargo onto an empty fleet vessel (needs pre-defined vessel start and end dates). The non-optional fitness should kick in
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testMoveNominalToFleet_LossMakingCargo() throws Exception {

		// Create the required basic elements
		final Vessel source = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", source, "50000", 0);

		final Vessel vessel = fleetModelBuilder.createVesselFrom("Vessel1", source, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		// Half the capacity! So will loose P&L by doing this.
		vessel.setCapacity(70_000);

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

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			Assert.assertEquals(vesselAvailability1, optCargo1.getVesselAssignmentType());

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			// Validate the initial sequences are valid
			Assert.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));

			final NonOptionalSlotFitnessCore core = new NonOptionalSlotFitnessCore("");
			scenarioToOptimiserBridge.getDataTransformer().getInjector().injectMembers(core);
			core.init(scenarioToOptimiserBridge.getDataTransformer().getInjector().getInstance(IOptimisationData.class));
			core.evaluate(initialRawSequences, new EvaluationState(), null);
			Assert.assertTrue(core.getFitness() > 0);
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
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "200000", 0);

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

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
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
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "200000", 0);

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

		evaluateWithLSOTest(true, plan -> {
			// Set iterations to zero to avoid any optimisation changes and rely on the unpairing opt step
			ScenarioUtils.setLSOStageIterations(plan, 0);
			ScenarioUtils.setHillClimbStageIterations(plan, 0);
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assert.assertEquals(0, optimiserScenario.getCargoModel().getCargoes().size());
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());
		}, null);

	}

	/**
	 * Test that BugzId: 2304 has been fixed.
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testPromptNominalOptimisedIn_ShipOnly() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "200000", 1);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withOptional(false) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withOptional(false) //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2016, 12, 5));

		evaluateWithLSOTest(true, plan -> {

			plan.getUserSettings().setShippingOnly(true);
			ScenarioUtils.setLSOStageIterations(plan, 10_000);
			// No hill climb
			ScenarioUtils.setHillClimbStageIterations(plan, 0);
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargo still exists
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());
		}, null);

	}

	/**
	 * Test that the correction to BugzId: 2304 has been fixed. (Initial fixes made all nominal cargoes optional, thus bad P&L caroges were optimised out)
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testOutOfPromptButInPeriodNominalOptimisedStaysIn_ShipOnly() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "200000", 1);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "7") //
				.withOptional(false) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "5") //
				.withOptional(false) //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 11, 1));

		evaluateWithLSOTest(true, plan -> {
			plan.getUserSettings().setPeriodStartDate(LocalDate.of(2015, 10, 1));
			plan.getUserSettings().setPeriodEnd(YearMonth.of(2016, 1));

			plan.getUserSettings().setShippingOnly(true);
			ScenarioUtils.setLSOStageIterations(plan, 10_000);
			// No hill climb
			ScenarioUtils.setHillClimbStageIterations(plan, 0);
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargo still exists
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());
		}, null);

	}

	/**
	 * A cargo in the prompt, but outside the optimisation period should not be unpaired.
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testOutOfPeriodButInPromptNominalOptimisedStaysNominal() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "200000", 0);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "7") //
				.withOptional(false) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "5") //
				.withOptional(false) //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2016, 1, 1));

		evaluateWithLSOTest(true, plan -> {
			plan.getUserSettings().setPeriodStartDate(LocalDate.of(2015, 10, 1));
			plan.getUserSettings().setPeriodEnd(YearMonth.of(2015, 12));
			plan.getUserSettings().setShippingOnly(true);
			ScenarioUtils.setLSOStageIterations(plan, 10_000);
			// No hill climb
			ScenarioUtils.setHillClimbStageIterations(plan, 0);
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargo still exists
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());
		}, null);

	}

	/**
	 * Aim of test is to try and ensure a) the action set can work with these solutions correctly and b) support the filtering correctly. (I.e ensure highly profitable but nominal cargoes are not
	 * used).
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void testForcePromptNominalToOpen_InPrompt_ActionSet() throws Exception {

		// Create the required basic elements
		final Vessel vessel1 = fleetModelFinder.findVessel("STEAM-145");
		final Vessel vessel2 = fleetModelFinder.findVessel("STEAM-138");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel1, "200000", 0);
		final CharterInMarket charterInMarket_2a = spotMarketsModelBuilder.createCharterInMarket("CharterIn 2a", vessel2, "200000", 1);
		final CharterInMarket charterInMarket_2b = spotMarketsModelBuilder.createCharterInMarket("CharterIn 2b", vessel2, "1000", 1);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.withOptional(true) //
				.withAllowedVessels(vessel1) //
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
				.withAllowedVessels(vessel1) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "10") //
				.withOptional(true) //
				.build() //
				.withVesselAssignment(charterInMarket_2a, 0, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));

		evaluateWithLSOTest(true, plan -> {

			// Set iterations to zero to avoid any optimisation changes and rely on the unpairing opt step
			// s.getAnnealingSettings().setIterations(0);
			ScenarioUtils.setHillClimbStageIterations(plan, 0);

			plan.getUserSettings().setBuildActionSets(true);

			@NonNull
			final ActionPlanOptimisationStage stage = ScenarioUtils.createDefaultActionPlanParameters(ScenarioUtils.createDefaultConstraintAndFitnessSettings());
			// Limit action set
			stage.setTotalEvaluations(100);
			stage.setInRunEvaluations(100);

			plan.getStages().add(stage);
		}, (scenarioRunner) -> {
			return new AbstractRunnerHook() {

				@Override
				public @Nullable ISequences doGetPrestoredSequences(@NonNull final String stage, final LNGDataTransformer dataTransformer) {
					if (IRunnerHook.STAGE_LSO.equals(stage)) {
						final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

						// Ensure all sequences has been created!

						final IModifiableSequences lsoSolution = SequenceHelper.createSequences(dataTransformer);
						// Cargo should be unpaired
						SequenceHelper.addToUnused(lsoSolution, dataTransformer, cargo1);
						// Cargo should move to cheaper vessel
						SequenceHelper.addSequence(lsoSolution, dataTransformer, charterInMarket_2b, 0, cargo2);

						// Sequences are now empty, but still need start/end events
						SequenceHelper.addSequence(lsoSolution, dataTransformer, charterInMarket_2a, 0);
						// Nothing left on nominals
						SequenceHelper.addSequence(lsoSolution, dataTransformer, charterInMarket_1, -1);
						SequenceHelper.addSequence(lsoSolution, dataTransformer, charterInMarket_2a, -1);
						SequenceHelper.addSequence(lsoSolution, dataTransformer, charterInMarket_2b, -1);

						return lsoSolution;
					}
					return null;
				}

			};
		}, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			Assert.assertEquals(2, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assert.assertEquals(2, optimiserScenario.getCargoModel().getDischargeSlots().size());

			Assert.assertSame(cargo2, optimiserScenario.getCargoModel().getCargoes().get(0));
			Assert.assertSame(charterInMarket_2b, cargo2.getVesselAssignmentType());

		}, null);
	}

	@Test
	@Category({ MicroTest.class })
	public void testForcePromptNominalToOpen_OutPrompt() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "200000", 0);

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

		evaluateWithLSOTest(true, plan -> {
			// Set iterations to zero to avoid any optimisation changes and rely on the unpairing opt step
			ScenarioUtils.setLSOStageIterations(plan, 0);
			ScenarioUtils.setHillClimbStageIterations(plan, 0);

		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());
		}, null);
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
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 0);

		// Second market much cheaper, but should not be used.
		final CharterInMarket charterInMarket_2 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 2", vessel, "10000", 0);

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

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
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
		final Vessel vessel1 = fleetModelFinder.findVessel("STEAM-145");
		final Vessel vessel2 = fleetModelFinder.findVessel("STEAM-138");

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel2, "50000", 0);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				// Forbid vessel
				.withAllowedVessels(vessel1) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(false, null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			// Validate the initial sequences are invalid
			final List<IConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assert.assertNotNull(failedConstraintCheckers);

			// Expect just this one to fail
			Assert.assertEquals(1, failedConstraintCheckers.size());
			Assert.assertTrue(failedConstraintCheckers.get(0) instanceof AllowedVesselPermissionConstraintChecker);

		}, null);
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
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "10000", 0);

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

		optimiseWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			Assert.assertEquals(vesselAvailability1, optCargo1.getVesselAssignmentType());

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			// Validate the initial sequences are valid
			Assert.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		});
	}

}