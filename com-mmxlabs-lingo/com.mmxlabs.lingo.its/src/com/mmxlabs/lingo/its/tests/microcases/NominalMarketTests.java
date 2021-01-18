/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.common.collect.Lists;
import com.google.inject.Injector;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.modules.InitialPhaseOptimisationDataModule;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.util.CargoTypeUtil;
import com.mmxlabs.scheduler.optimiser.components.util.CargoTypeUtil.SimpleCargoType;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AllowedVesselPermissionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PromptRoundTripVesselPermissionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.RoundTripVesselPermissionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.fitness.components.NonOptionalSlotFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICustomVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService.ModuleType;
import com.mmxlabs.scheduler.optimiser.peaberry.OptimiserInjectorServiceMaker;

@SuppressWarnings("unused")
@ExtendWith(ShiroRunner.class)
public class NominalMarketTests extends AbstractMicroTestCase {

	private static List<String> requiredFeatures = Lists.newArrayList("no-nominal-in-prompt", "optimisation-actionset");
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
	 * Test: test a nominal vessel start and end heel is present
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testNominalHeel() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setSafetyHeel(500);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

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

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assertions.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);

			Assertions.assertNotNull(cargoAllocation);
			final EList<Event> events = cargoAllocation.getEvents();
			final Event firstEvent = events.get(0);
			final Event lastEvent = events.get(events.size() - 1);

			// Check safety heel present.
			Assertions.assertEquals(vessel.getVesselOrDelegateSafetyHeel(), firstEvent.getHeelAtStart());
			Assertions.assertEquals(vessel.getVesselOrDelegateSafetyHeel(), lastEvent.getHeelAtEnd());

		});
	}

	/**
	 * Test: Check heel rollover does not impact other nominal cargoes. Regression
	 * test.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testNominalHeel_MultipleCargoes() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setSafetyHeel(500);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		// Construct the cargo scenario

		// Create two identical cargoes, with limited heel.
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withVolumeLimits(150_000, 150_000, VolumeUnits.M3) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withVolumeLimits(50_000, 50_000, VolumeUnits.M3) //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withVolumeLimits(150_000, 150_000, VolumeUnits.M3) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withVolumeLimits(50_000, 50_000, VolumeUnits.M3) //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 2) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		ICustomVolumeAllocator customAllocator = record -> {
			if (record.slots.size() == 2) {
				final ILoadOption buy = (ILoadOption) record.slots.get(0);
				final IDischargeOption sell = (IDischargeOption) record.slots.get(1);

				if (CargoTypeUtil.getSimpleCargoType(buy, sell) == SimpleCargoType.SHIPPED) {
					record.preferShortLoadOverLeftoverHeel = false;
				}
			}
		};

		IOptimiserInjectorService customisationService = OptimiserInjectorServiceMaker.begin() //
				.withModuleOverrideBindInstance(ModuleType.Module_LNGTransformerModule, ICustomVolumeAllocator.class, customAllocator) //
				.make();
		
		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assertions.assertEquals(2, optimiserScenario.getCargoModel().getCargoes().size());

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assertions.assertNotNull(schedule);

			for (int i = 0; i < 2; ++i) {
				// Check correct heel values
				final Cargo optCargo = optimiserScenario.getCargoModel().getCargoes().get(i);

				@Nullable
				final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo.getLoadName(), schedule);

				Assertions.assertNotNull(cargoAllocation);
				final EList<Event> events = cargoAllocation.getEvents();
				final Event firstEvent = events.get(0);
				final Event lastEvent = events.get(events.size() - 1);

				// Check safety heel present.
				Assertions.assertEquals(vessel.getVesselOrDelegateSafetyHeel(), firstEvent.getHeelAtStart());
				// Expect a large quantity of gas on board, more than the heel.
				Assertions.assertTrue(lastEvent.getHeelAtEnd() > vessel.getVesselOrDelegateSafetyHeel());
				Assertions.assertTrue(lastEvent.getHeelAtEnd() > 80_000);
			}
		}, customisationService);
	}

	/**
	 * Test: Test a charter-in vessel start and end heel level. (Not strictly a
	 * nominal vessel issue, but implemented parallel test here also).
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCharterInHeel() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setSafetyHeel(500);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 1);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, 0, 1) // 0 is first market option
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assertions.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);

			Assertions.assertNotNull(cargoAllocation);
			final EList<Event> events = cargoAllocation.getEvents();
			final Event firstEvent = events.get(0);
			final Event lastEvent = events.get(events.size() - 1);

			// Check safety heel present.
			Assertions.assertEquals(vessel.getVesselOrDelegateSafetyHeel(), firstEvent.getHeelAtStart());
			Assertions.assertEquals(vessel.getVesselOrDelegateSafetyHeel(), lastEvent.getHeelAtEnd());

		});
	}

	/**
	 * Test: Make sure the slots are kept as a bound pair on the nominal. Test by
	 * swapping discharges round.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testNominalSlotBinding() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(true, false) //
				.build();
		// Create cargo 1, cargo 2
		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 2) // -1 is nominal
				.withAssignmentFlags(true, false) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assertions.assertEquals(2, optimiserScenario.getCargoModel().getCargoes().size());

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
			Assertions.assertNull(MicroTestUtils.validateConstraintCheckers(dataTransformer, initialRawSequences));

			final IModifiableSequences lsoSolution = SequenceHelper.createSequences(dataTransformer.getInjector());
			SequenceHelper.addSequence(lsoSolution, dataTransformer.getInjector(), charterInMarket_1, -1, l1, d2, l2, d1);

			// Validate the swapped discharges are invalid
			final List<IConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateConstraintCheckers(dataTransformer, lsoSolution);
			Assertions.assertNotNull(failedConstraintCheckers);

			// Expect just this one to fail
			Assertions.assertEquals(2, failedConstraintCheckers.size());
			// Order doesn't really matter. Should make test robust to order change
			Assertions.assertTrue(failedConstraintCheckers.get(0) instanceof RoundTripVesselPermissionConstraintChecker);
			Assertions.assertTrue(failedConstraintCheckers.get(1) instanceof PromptRoundTripVesselPermissionConstraintChecker);
		});
	}

	/**
	 * Test: Move a nominal cargo onto an empty fleet vessel (needs pre-defined
	 * vessel start and end dates)
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testMoveNominalToFleet() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
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

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			Assertions.assertEquals(vesselAvailability1, optCargo1.getVesselAssignmentType());

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			// Validate the initial sequences are valid
			Assertions.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		});
	}

	/**
	 * Test: Move a nominal cargo onto an empty fleet vessel (needs pre-defined
	 * vessel start and end dates). The non-optional fitness should kick in
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testMoveNominalToFleet_LossMakingCargo_ShipOnly() throws Exception {

		// Create the required basic elements
		final Vessel source = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", source, entity, "50000", 0);

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
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
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
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			final long beforePNL = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), optimiserScenario.getScheduleModel().getSchedule()).getGroupProfitAndLoss().getProfitAndLoss();
			scenarioRunner.runAndApplyBest();
			final long afterPNL = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), optimiserScenario.getScheduleModel().getSchedule()).getGroupProfitAndLoss().getProfitAndLoss();

			Assertions.assertTrue(beforePNL > afterPNL);

			// Check correct cargoes remain and spot index has changed.
			Assertions.assertEquals(vesselAvailability1, optCargo1.getVesselAssignmentType());

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			// Validate the initial sequences are valid
			Assertions.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));

			final NonOptionalSlotFitnessCore core = new NonOptionalSlotFitnessCore("");
			Injector cinjector = scenarioToOptimiserBridge.getInjector().createChildInjector(new InitialPhaseOptimisationDataModule());
			cinjector.injectMembers(core);
			core.init(cinjector.getInstance(IPhaseOptimisationData.class));
			core.evaluate(initialRawSequences, new EvaluationState(), null);
			Assertions.assertTrue(core.getFitness() > 0);

		}, null);
	}

	/**
	 * Test: Move a nominal cargo onto an empty fleet vessel (needs pre-defined
	 * vessel start and end dates). The non-optional fitness should kick in
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testMoveNominalToFleet_LossMakingCargo() throws Exception {

		// Create the required basic elements
		final Vessel source = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", source, entity, "50000", 0);

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
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			Assertions.assertEquals(vesselAvailability1, optCargo1.getVesselAssignmentType());

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			// Validate the initial sequences are valid
			Assertions.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));

			final NonOptionalSlotFitnessCore core = new NonOptionalSlotFitnessCore("");
			Injector cinjector = scenarioToOptimiserBridge.getInjector().createChildInjector(new InitialPhaseOptimisationDataModule());
			cinjector.injectMembers(core);
			core.init(cinjector.getInstance(IPhaseOptimisationData.class));
			core.evaluate(initialRawSequences, new EvaluationState(), null);
			Assertions.assertTrue(core.getFitness() > 0);
		});
	}

	/**
	 * Test: Break apart cargo into open positions (even with cancellation fee)
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testMoveNominalToOpen() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "200000", 0);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7") //
				.withOptional(true) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5") //
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
			Assertions.assertEquals(0, optimiserScenario.getCargoModel().getCargoes().size());
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testForcePromptNominalToOpen_InPrompt() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "200000", 0);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withOptional(false) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withOptional(false) //
				.withCancellationFee("300000") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2016, 12, 5));

		evaluateWithLSOTest(true, plan -> {
			// Set iterations to zero to avoid any optimisation changes and rely on the
			// unpairing opt step
			ScenarioUtils.setLSOStageIterations(plan, 0);
			ScenarioUtils.setHillClimbStageIterations(plan, 0);
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assertions.assertEquals(0, optimiserScenario.getCargoModel().getCargoes().size());
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());
		}, null);

	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testForcePromptNominalToOpen_InPrompt_MOO() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "200000", 0);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withOptional(false) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withOptional(false) //
				.withCancellationFee("300000") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2016, 12, 5));

		evaluateWithLSOTest(true, plan -> {
			plan.getUserSettings().setSimilarityMode(SimilarityMode.ALL);
			// Set iterations to zero to avoid any optimisation changes and rely on the
			// unpairing opt step
			// (Actually needs to be 1 to trigger the optimisation stage. This may cause the
			// test to fail if the first move set finds a good solution)
			ScenarioUtils.setLSOStageIterations(plan, 1);
			ScenarioUtils.setHillClimbStageIterations(plan, 0);
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assertions.assertEquals(0, optimiserScenario.getCargoModel().getCargoes().size());
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());
		}, null);

	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPromptLockedNominalInPromptNotOpened() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "200000", 0);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7") //
				.withOptional(true) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5") //
				.withOptional(true) //
				.withCancellationFee("300000") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, true) //
				.build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2016, 12, 5));

		evaluateWithLSOTest(true, plan -> {
			// Set iterations to zero to avoid any optimisation changes and rely on the
			// unpairing opt step
			ScenarioUtils.setLSOStageIterations(plan, 0);
			ScenarioUtils.setHillClimbStageIterations(plan, 0);
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargo still exists
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());

			Assertions.assertSame(charterInMarket_1, cargo1.getVesselAssignmentType());
			Assertions.assertEquals(-1, cargo1.getSpotIndex());
		}, null);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPromptLockedNominalInPromptNotOpenedWithSpotSale() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "200000", 0);

		DESSalesMarket market = spotMarketsModelBuilder.makeDESSaleMarket("Sale Market", portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), entity, "5").build();
		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7") //
				.build() //
				.makeMarketDESSale("D1", market, YearMonth.of(2015, 12), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT)) //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, true) //
				.build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2017, 1, 2));

		evaluateWithLSOTest(true, plan -> {
			// Set iterations to zero to avoid any optimisation changes and rely on the
			// unpairing opt step
			ScenarioUtils.setLSOStageIterations(plan, 0);
			ScenarioUtils.setHillClimbStageIterations(plan, 0);
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargo still exists
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());

			Assertions.assertSame(charterInMarket_1, cargo1.getVesselAssignmentType());
			Assertions.assertEquals(-1, cargo1.getSpotIndex());
		}, null);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPromptLockedNominalInPromptNotOpenedWithSpotSaleOpti() throws Exception {

		// Same as #testPromptLockedNominalInPromptNotOpenedWithSpotSale but we run an
		// optimisation and make sure loss making cargo is not unpaired.

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "200000", 0);

		DESSalesMarket market = spotMarketsModelBuilder.makeDESSaleMarket("Sale Market", portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), entity, "1").build();
		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7") //
				.build() //
				.makeMarketDESSale("D1", market, YearMonth.of(2015, 12), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT)) //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, true) //
				.build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2017, 1, 2));

		evaluateWithLSOTest(true, plan -> {
			ScenarioUtils.setLSOStageIterations(plan, 1_000);
			ScenarioUtils.setHillClimbStageIterations(plan, 0);
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargo still exists
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());

			Assertions.assertSame(charterInMarket_1, cargo1.getVesselAssignmentType());
			Assertions.assertEquals(-1, cargo1.getSpotIndex());
		}, null);
	}

	/**
	 * Test that BugzId: 2304 has been fixed.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPromptNominalOptimisedIn_ShipOnly() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "200000", 1);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withOptional(false) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
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
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());
		}, null);

	}

	/**
	 * Test that the correction to BugzId: 2304 has been fixed. (Initial fixes made
	 * all nominal cargoes optional, thus bad P&L cargoes were optimised out)
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testOutOfPromptButInPeriodNominalOptimisedStaysIn_ShipOnly() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "200000", 1);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7") //
				.withOptional(false) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5") //
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
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());
		}, null);

	}

	/**
	 * A cargo in the prompt, but outside the optimisation period should not be
	 * unpaired.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testOutOfPeriodButInPromptNominalOptimisedStaysNominal() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "200000", 0);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7") //
				.withOptional(false) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5") //
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
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());
		}, null);

	}

	/**
	 * Aim of test is to try and ensure a) the action set can work with these
	 * solutions correctly and b) support the filtering correctly. (I.e ensure
	 * highly profitable but nominal cargoes are not used).
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testForcePromptNominalToOpen_InPrompt_ActionSet() throws Exception {

		// Create the required basic elements
		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel2 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel1, entity, "200000", 0);
		final CharterInMarket charterInMarket_2a = spotMarketsModelBuilder.createCharterInMarket("CharterIn 2a", vessel2, entity, "200000", 1);
		final CharterInMarket charterInMarket_2b = spotMarketsModelBuilder.createCharterInMarket("CharterIn 2b", vessel2, entity, "1000", 1);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withOptional(true) //
				.withRestrictedVessels(vessel1, true) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "10.0") //
				.withOptional(true) //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withOptional(true) //
				.withRestrictedVessels(vessel1, true) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "10") //
				.withOptional(true) //
				.build() //
				.withVesselAssignment(charterInMarket_2a, 0, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 5));

		evaluateWithLSOTest(true, plan -> {

			// Set iterations to zero to avoid any optimisation changes and rely on the
			// unpairing opt step
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

						final IModifiableSequences lsoSolution = SequenceHelper.createSequences(dataTransformer.getInjector());
						// Cargo should be unpaired
						SequenceHelper.addToUnused(lsoSolution, dataTransformer.getInjector(), cargo1);
						// Cargo should move to cheaper vessel
						SequenceHelper.addSequence(lsoSolution, dataTransformer.getInjector(), charterInMarket_2b, 0, cargo2);

						// Sequences are now empty, but still need start/end events
						SequenceHelper.addSequence(lsoSolution, dataTransformer.getInjector(), charterInMarket_2a, 0);
						// Nothing left on nominals
						SequenceHelper.addSequence(lsoSolution, dataTransformer.getInjector(), charterInMarket_1, -1);
						SequenceHelper.addSequence(lsoSolution, dataTransformer.getInjector(), charterInMarket_2a, -1);
						SequenceHelper.addSequence(lsoSolution, dataTransformer.getInjector(), charterInMarket_2b, -1);

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
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			Assertions.assertEquals(2, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assertions.assertEquals(2, optimiserScenario.getCargoModel().getDischargeSlots().size());

			Assertions.assertSame(cargo2, optimiserScenario.getCargoModel().getCargoes().get(0));
			Assertions.assertSame(charterInMarket_2b, cargo2.getVesselAssignmentType());

		}, null);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testForcePromptNominalToOpen_OutPrompt() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "200000", 0);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7") //
				.withOptional(true) //
				.build() // ````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5") //
				.withOptional(true) //
				.withCancellationFee("300000") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2015, 10, 1), LocalDate.of(2015, 12, 4));

		evaluateWithLSOTest(true, plan -> {
			// Set iterations to zero to avoid any optimisation changes and rely on the
			// unpairing opt step
			ScenarioUtils.setLSOStageIterations(plan, 0);
			ScenarioUtils.setHillClimbStageIterations(plan, 0);

		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getDischargeSlots().size());
		}, null);
	}

	/**
	 * Test: Do not move onto second cheaper nominal market.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testMoveMultipleNominalVessels() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);

		// Second market much cheaper, but should not be used.
		final CharterInMarket charterInMarket_2 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 2", vessel, entity, "10000", 0);

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

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			final VesselAssignmentType vesselAssignmentType = optCargo1.getVesselAssignmentType();
			Assertions.assertEquals(charterInMarket_1, vesselAssignmentType);
			Assertions.assertEquals(-1, optCargo1.getSpotIndex());
		});
	}

	/**
	 * Test: Assign cargo to a nominal vessel of restricted class - initial solution
	 * should be invalid.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testMoveNominalVesselRestriction() throws Exception {

		// Create the required basic elements
		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel2 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel2, entity, "50000", 0);

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				// Forbid vessel
				.withRestrictedVessels(vessel1, true) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(false, null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			// Validate the initial sequences are invalid
			final List<IConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			Assertions.assertNotNull(failedConstraintCheckers);

			// Expect just this one to fail
			Assertions.assertEquals(1, failedConstraintCheckers.size());
			Assertions.assertTrue(failedConstraintCheckers.get(0) instanceof AllowedVesselPermissionConstraintChecker);

		}, null);
	}

	/**
	 * Test: Ensure we do not use a cheaper nominal vessel and avoid fleet vessel
	 * charter cost.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDoNotMoveFleetToNominal() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "10000", 0);

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("100000") // Much more costly than nominal
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check cargoes removed
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			Assertions.assertEquals(vesselAvailability1, optCargo1.getVesselAssignmentType());

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

			// Validate the initial sequences are valid
			Assertions.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
		});
	}

}