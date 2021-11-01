/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.inject.Injector;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.panamaslots.PanamaSlotsConstraintChecker;
import com.mmxlabs.models.lng.transformer.extensions.panamaslots.PanamaSlotsConstraintCheckerFactory;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScopeImpl;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.scheduling.ScheduledTimeWindows;
import com.mmxlabs.scheduler.optimiser.scheduling.TimeWindowScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

@ExtendWith(ShiroRunner.class)
public class PanamaSlotBookingsTests extends AbstractLegacyMicroTestCase {

	@Override
	public IScenarioDataProvider importReferenceData() throws Exception {

		@NonNull
		final IScenarioDataProvider scenarioDataProvider = importReferenceData("/referencedata/reference-data-2/");

		final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		@NonNull
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);
		cargoModel.setCanalBookings(canalBookings);

		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);

		final Optional<Route> potentialPanama = portModel.getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		final Route panama = potentialPanama.get();

		portModel.getPorts().stream().filter(p -> {
			return "Colon".equals(p.getName()) || "Balboa".equals(p.getName());
		}).forEach(p -> {
			final EntryPoint ep = PortFactory.eINSTANCE.createEntryPoint();
			ep.setPort(p);
			if (p.getName().equals("Colon")) {
				ep.setName("North");
				panama.setNorthEntrance(ep);
			} else {
				ep.setName("South");
				panama.setSouthEntrance(ep);
			}
		});

		final LNGScenarioModel scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		scenarioModel.setPromptPeriodStart(LocalDate.of(2017, 6, 1));
		scenarioModel.setPromptPeriodEnd(LocalDate.of(2017, 9, 1));

		return scenarioDataProvider;
	}

	private Cargo createFobDesCargo(int num, final VesselAvailability vesselAvailability, final Port loadPort, final Port dischargePort, final LocalDateTime loadDate,
			final LocalDateTime dischargeDate) {
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(String.format("L-%d", num), loadDate.toLocalDate(), loadPort, null, entity, "5") //
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale(String.format("D-%d", num), dischargeDate.toLocalDate(), dischargePort, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1) //
				.build();
		return cargo;
	}

	private VesselAvailability getDefaultVesselAvailability() {
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setMaxSpeed(16.0);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();
		return vesselAvailability;
	}

	private VesselAvailability getStartAtOtherSideOfPanamaVesselAvailability(LocalDateTime vesselStartDate) {
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setMaxSpeed(16.0);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();
		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_QUINTERO);
		vesselAvailability.setStartAt(port2);
		vesselAvailability.setStartBy(vesselStartDate);
		return vesselAvailability;
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void panamaSlotAvailableTest_Constraint() {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final CanalBookingSlot d1 = cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2017, Month.JUNE, 7), null);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_QUINTERO);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(13);

		final Cargo cargo = createFobDesCargo(1, vesselAvailability, port1, port2, loadDate, dischargeDate);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final Injector injector = MicroTestUtils.createEvaluationInjector(dataTransformer);

			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();

				final PanamaSlotsConstraintChecker checker = new PanamaSlotsConstraintChecker(PanamaSlotsConstraintCheckerFactory.NAME);//
				injector.injectMembers(checker);

				// Set blank sequences as initial state
				checker.sequencesAccepted(SequenceHelper.createSequences(dataTransformer.getInjector()), SequenceHelper.createSequences(dataTransformer.getInjector()), new ArrayList<>());

				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(dataTransformer.getInjector(), vesselAvailability, cargo));
				checker.checkConstraints(SequenceHelper.createSequences(dataTransformer.getInjector()), null, new ArrayList<>());
				assertTrue(checker.checkConstraints(manipulatedSequences, null, new ArrayList<>()));
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void panamaSlotNoDoubleAssignmentTest() {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2017, Month.JUNE, 6), null);

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();
		vessel.setMaxSpeed(16.0);

		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("vessel2", vessel, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final VesselAvailability vesselAvailability2 = cargoModelBuilder.makeVesselAvailability(vessel2, entity) //
				.build();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_QUINTERO);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(13);

		final Cargo cargo = createFobDesCargo(1, vesselAvailability, port1, port2, loadDate, dischargeDate);

		final Cargo cargo2 = createFobDesCargo(2, vesselAvailability2, port1, port2, loadDate, dischargeDate);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final Injector injector = MicroTestUtils.createEvaluationInjector(dataTransformer);
			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(dataTransformer.getInjector(), vesselAvailability, cargo));
				SequenceHelper.addSequence(manipulatedSequences, dataTransformer.getInjector(), vesselAvailability2, cargo2);

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);
				final IResource r1 = manipulatedSequences.getResources().get(1);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);
				final IPortTimeWindowsRecord ptr_r1_cargo = records.get(r1).get(1);

				assertNotNull(ptr_r0_cargo.getRouteOptionBooking(ptr_r0_cargo.getFirstSlot()));
				assertNull(ptr_r1_cargo.getRouteOptionBooking(ptr_r1_cargo.getFirstSlot()));
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void panamaSlotUnavailableTest() {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2017, Month.JUNE, 7), null);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_QUINTERO);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.AUGUST, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(10);

		final Cargo cargo = createFobDesCargo(1, vesselAvailability, port1, port2, loadDate, dischargeDate);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, cargo));

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);

				assertNull(ptr_r0_cargo.getRouteOptionBooking(ptr_r0_cargo.getFirstSlot()));
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void bookingButNoVoyageTest() {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2017, Month.JUNE, 7), null);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_BARCELONA);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(13);

		final Cargo cargo = createFobDesCargo(1, vesselAvailability, port1, port2, loadDate, dischargeDate);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());

			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();

				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, cargo));

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);

				assertNull(ptr_r0_cargo.getRouteOptionBooking(ptr_r0_cargo.getFirstSlot()));
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void nonMatchingBookingTest() {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2017, Month.JUNE, 10), null);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_QUINTERO);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);

		final LocalDateTime dischargeDate = loadDate.plusDays(13);

		final Cargo cargo = createFobDesCargo(1, vesselAvailability, port1, port2, loadDate, dischargeDate);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, cargo));

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);

				assertNull(ptr_r0_cargo.getRouteOptionBooking(ptr_r0_cargo.getFirstSlot()));
			}
		});

	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void bookingAssignedToSlotTest() {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_QUINTERO);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(13);

		final Cargo cargo = createFobDesCargo(1, vesselAvailability, port1, port2, loadDate, dischargeDate);

		cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2017, Month.JUNE, 7), vesselAvailability.getVessel());

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, cargo));

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);

				assertNotNull(ptr_r0_cargo.getRouteOptionBooking(ptr_r0_cargo.getFirstSlot()));
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void vesselStartBookingAssignedToSlotTest() {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);

		final LocalDateTime vesselStartDate = loadDate.minusDays(13);

		final LocalDateTime dischargeDate = loadDate.plusDays(13);

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_QUINTERO);

		final VesselAvailability vesselAvailability = this.getStartAtOtherSideOfPanamaVesselAvailability(vesselStartDate);

		final Cargo cargo = createFobDesCargo(1, vesselAvailability, port1, port2, loadDate, dischargeDate);

		// ballast from start to LOAD.
		cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.SOUTHSIDE, LocalDate.of(2017, Month.MAY, 26), vesselAvailability.getVessel());

		// laden to discharge booking.
		cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2017, Month.JUNE, 8), vesselAvailability.getVessel());

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, cargo));

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				// final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(manipulatedSequences);
				// //final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				var ptrMap = scheduler.schedule(manipulatedSequences);
				final IPortTimesRecord portTimesRecord = ptrMap.get(r0).get(0);

				IPortSlot startEvent = portTimesRecord.getFirstSlot();

				assertTrue(startEvent instanceof StartPortSlot);
				assertNotNull(portTimesRecord.getRouteOptionBooking(startEvent));
			}
		});
	}

	/**
	 * This test checks that an assigned booking is used by the correct vessel and not by an earlier sequence in the array that has a suitable space.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void asssignedBookingNotUsedTest() {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();
		vessel.setMaxSpeed(16.0);

		final Vessel vessel2 = fleetModelBuilder.createVesselFrom("vessel2", vessel, scenarioModelBuilder.getCostModelBuilder().copyRouteCosts());
		final VesselAvailability vesselAvailability2 = cargoModelBuilder.makeVesselAvailability(vessel2, entity) //
				.build();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_QUINTERO);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(13);

		final Cargo cargo = createFobDesCargo(1, vesselAvailability, port1, port2, loadDate, dischargeDate);
		final Cargo cargo2 = createFobDesCargo(2, vesselAvailability2, port1, port2, loadDate, dischargeDate);

		// Assign to vessel 2
		cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2017, Month.JUNE, 7), vesselAvailability2.getVessel());

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final Injector injector = MicroTestUtils.createEvaluationInjector(dataTransformer);
			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(dataTransformer.getInjector(), vesselAvailability, cargo));
				SequenceHelper.addSequence(manipulatedSequences, dataTransformer.getInjector(), vesselAvailability2, cargo2);

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);
				final IResource r1 = manipulatedSequences.getResources().get(1);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);
				final IPortTimeWindowsRecord ptr_r1_cargo = records.get(r1).get(1);

				assertNull(ptr_r0_cargo.getRouteOptionBooking(ptr_r0_cargo.getFirstSlot()));
				assertNotNull(ptr_r1_cargo.getRouteOptionBooking(ptr_r1_cargo.getFirstSlot()));
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void journeyCanBeMadeDirectTest() {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2017, Month.JUNE, 7), null);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_CHITA);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(25);

		final Cargo cargo = createFobDesCargo(1, vesselAvailability, port1, port2, loadDate, dischargeDate);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());

			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();

				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, cargo));

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);

				assertNull(ptr_r0_cargo.getRouteOptionBooking(ptr_r0_cargo.getFirstSlot()));
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void choosePanamaIfRelaxedTest() {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		lngScenarioModel.getCargoModel().getCanalBookings().setStrictBoundaryOffsetDays(0);
		lngScenarioModel.getCargoModel().getCanalBookings().setRelaxedBoundaryOffsetDays(0);

		cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2017, Month.JUNE, 7), null);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);
		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_QUINTERO);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.AUGUST, 1, 0, 0, 0);
		// journey could be made direct
		final LocalDateTime dischargeDate = loadDate.plusDays(30);

		final Cargo cargo = createFobDesCargo(1, vesselAvailability, port1, port2, loadDate, dischargeDate);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, cargo));

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);

				assertEquals(AvailableRouteChoices.OPTIMAL, ptr_r0_cargo.getSlotNextVoyageOptions(ptr_r0_cargo.getFirstSlot()));
				assertNull(ptr_r0_cargo.getRouteOptionBooking(ptr_r0_cargo.getFirstSlot()));
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void marginTest() {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		lngScenarioModel.getCargoModel().getCanalBookings().setArrivalMarginHours(4);

		cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2017, Month.JUNE, 6), null);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_QUINTERO);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(10);

		final Cargo cargo = createFobDesCargo(1, vesselAvailability, port1, port2, loadDate, dischargeDate);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, cargo));

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);

				assertNull(ptr_r0_cargo.getRouteOptionBooking(ptr_r0_cargo.getFirstSlot()));
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void windowEndTrimmingTest() {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2017, Month.JUNE, 7), null);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_BROWNSVILLE);

		@NonNull
		final Port port3 = portFinder.findPortById(InternalDataConstants.PORT_HIMEJI);

		final LocalDateTime loadDate1 = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate1 = loadDate1.plusDays(2);

		final LocalDateTime loadDate2 = LocalDateTime.of(2017, Month.JUNE, 6, 0, 0, 0);
		final LocalDateTime dischargeDate2 = loadDate2.plusDays(30);

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", loadDate1.toLocalDate(), port1, null, entity, "5") //
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D1", dischargeDate1.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate1.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(5, TimePeriod.DAYS)//
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", loadDate2.toLocalDate(), port1, null, entity, "5") //
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D2", dischargeDate2.toLocalDate(), port3, null, entity, "7") //
				.withWindowStartTime(dischargeDate2.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS)//
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 2) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, cargo, cargo2));
				// SequenceHelper.addSequence(manipulatedSequences, scenarioToOptimiserBridge, vesselAvailability, cargo2);

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				scheduler.setUsePNLBasedWindowTrimming(false);

				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);
				final IPortTimeWindowsRecord ptr_r0_cargo2 = records.get(r0).get(2);

				assertEquals(76, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getSlots().get(1)).getExclusiveEnd());
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void noSuezDistanceAvailableTest() {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);

		final CanalBookingSlot d1 = cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2017, Month.JUNE, 7), null);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_MANZANILLO);

		@NonNull
		final Port portUnrelevant = portFinder.findPortById(InternalDataConstants.PORT_BROWNSVILLE);

		final boolean removed = distanceModelBuilder.setPortToPortDistance(port1, port2, RouteOption.SUEZ, Integer.MAX_VALUE, true);
		assert removed;

		final LocalDateTime startDate = LocalDateTime.of(2017, Month.MAY, 15, 0, 0, 0);
		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(30);

		final Cargo cargoUnrelevant = createFobDesCargo(1, vesselAvailability, port1, portUnrelevant, startDate, startDate.plusDays(6));
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", loadDate.toLocalDate(), port1, null, entity, "5") //
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D2", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, cargoUnrelevant, cargo));
				// SequenceHelper.addSequence(manipulatedSequences, scenarioToOptimiserBridge, vesselAvailability, cargo2);

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(2);
				assertNotNull(ptr_r0_cargo.getRouteOptionBooking(ptr_r0_cargo.getFirstSlot()));
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void relaxedLimits_SouthBound_NotEnoughIdleTime() {

		// Now the default: NonLicenseFeatures.setSouthboundIdleTimeRuleEnabled(true);

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2017, 7, 1));

		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(lngScenarioModel);
		cargoModelBuilder.initCanalBookings();
		final CanalBookings canalBookings = cargoModel.getCanalBookings();

		canalBookings.setStrictBoundaryOffsetDays(0);
		canalBookings.setRelaxedBoundaryOffsetDays(60);
		canalBookings.setFlexibleBookingAmountNorthbound(1);
		canalBookings.setFlexibleBookingAmountSouthbound(0);

		// Need at least 5 days of idle time to allow it to go through.
		//canalBookings.setSouthboundMaxIdleDays(5);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();
		vesselAvailability.getVessel().setMaxSpeed(16.0);

		@NonNull
		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);
		@NonNull
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_QUINTERO);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JULY, 1, 0, 0, 0);
		// journey could be made direct
		final LocalDateTime dischargeDate = loadDate.plusDays(11);

		// Direct is twice as far as panama
		distanceModelBuilder.setPortToPortDistance(loadPort, dischargePort, 10 * 16 * 24 * 2, Integer.MAX_VALUE, 10 * 16 * 24, true);
		
		@NonNull
		final Port panama_entrance = portFinder.findPortById(InternalDataConstants.PORT_COLON);
		@NonNull
		final Port panama_exit = portFinder.findPortById(InternalDataConstants.PORT_BALBOA);

		// Load port to Panama canal entrance distance
		distanceModelBuilder.setPortToPortDistance(loadPort, panama_entrance, RouteOption.DIRECT, 10 * 16 * 12,true);
		// Distance from Panama canal exit to the discharge port 
		// we set it to zero, so it's not double accounted when compute travel time from Panama Canal Entrance port to the discharge port
		distanceModelBuilder.setPortToPortDistance(panama_exit, dischargePort, RouteOption.DIRECT, 0, true);
		// Distance from Panama canal entrance to the discharge via Panama canal
		distanceModelBuilder.setPortToPortDistance(panama_entrance, dischargePort, RouteOption.PANAMA, 10 * 16 * 12,true);

		final LoadSlot loadSlot = cargoModelBuilder.makeFOBPurchase("L", loadDate.toLocalDate(), loadPort, null, entity, "5", 22.6) //
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build();

		final DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D", dischargeDate.toLocalDate(), dischargePort, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, loadSlot, dischargeSlot));

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);

				assertEquals(AvailableRouteChoices.PANAMA_ONLY, ptr_r0_cargo.getSlotNextVoyageOptions(ptr_r0_cargo.getFirstSlot()));

				// Panama Slots Constraint Checker effectively disabled, and not sure why it would have failed in this case anyway.
//				final PanamaSlotsConstraintChecker checker = new PanamaSlotsConstraintChecker(PanamaSlotsConstraintCheckerFactory.NAME);//
//				injector.injectMembers(checker);
//
//				checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector()), null, new ArrayList<>());
//				assertFalse(checker.checkConstraints(manipulatedSequences, null, new ArrayList<>()));
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void relaxedLimits_SouthBound_EnoughIdleTime() {

		// Now the default: NonLicenseFeatures.setSouthboundIdleTimeRuleEnabled(true);

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2017, 7, 1));

		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(lngScenarioModel);
		final CanalBookings canalBookings = cargoModel.getCanalBookings();

		canalBookings.setStrictBoundaryOffsetDays(0);
		canalBookings.setRelaxedBoundaryOffsetDays(60);
		canalBookings.setFlexibleBookingAmountNorthbound(1);
		canalBookings.setFlexibleBookingAmountSouthbound(0);

		// Need at least 5 days of idle time to allow it to go through.
		//canalBookings.setSouthboundMaxIdleDays(5);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();
		vesselAvailability.getVessel().setMaxSpeed(16.0);

		@NonNull
		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);
		@NonNull
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_QUINTERO);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JULY, 1, 0, 0, 0);
		// journey could be made direct
		final LocalDateTime dischargeDate = loadDate.plusDays(16);

		// Direct is twice as far as panama
		distanceModelBuilder.setPortToPortDistance(loadPort, dischargePort, 10 * 16 * 24 * 2, Integer.MAX_VALUE, 10 * 16 * 24, true);

		final LoadSlot loadSlot = cargoModelBuilder.makeFOBPurchase("L", loadDate.toLocalDate(), loadPort, null, entity, "5", 22.6) //
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build();

		final DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D", dischargeDate.toLocalDate(), dischargePort, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, loadSlot, dischargeSlot));

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);

				assertEquals(AvailableRouteChoices.PANAMA_ONLY, ptr_r0_cargo.getSlotNextVoyageOptions(ptr_r0_cargo.getFirstSlot()));

				final PanamaSlotsConstraintChecker checker = new PanamaSlotsConstraintChecker(PanamaSlotsConstraintCheckerFactory.NAME);//
				injector.injectMembers(checker);

				checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector()), null, new ArrayList<>());
				assertTrue(checker.checkConstraints(manipulatedSequences, null, new ArrayList<>()));
			}
		});
	}

	/*
	 * @Test
	 * 
	 * @Tag(TestCategories.MICRO_TEST) public void relaxedLimits_SouthBound_NotAvailable_OldSouthboundLogic() {
	 * 
	 * NonLicenseFeatures.setSouthboundIdleTimeRuleEnabled(false);
	 * 
	 * // map into same timezone to make expectations easier portModelBuilder.setAllExistingPortsToUTC();
	 * 
	 * lngScenarioModel.setPromptPeriodStart(LocalDate.of(2017, 7, 1));
	 * 
	 * final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(lngScenarioModel); final CanalBookings canalBookings = cargoModel.getCanalBookings();
	 * 
	 * canalBookings.setStrictBoundaryOffsetDays(0); canalBookings.setRelaxedBoundaryOffsetDays(60); canalBookings.setFlexibleBookingAmountNorthbound(1);
	 * canalBookings.setFlexibleBookingAmountSouthbound(0);
	 * 
	 * final VesselAvailability vesselAvailability = getDefaultVesselAvailability(); vesselAvailability.getVessel().setMaxSpeed(16.0);
	 * 
	 * @NonNull final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);
	 * 
	 * @NonNull final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_QUINTERO);
	 * 
	 * final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JULY, 1, 0, 0, 0); // journey could be made direct final LocalDateTime dischargeDate = loadDate.plusDays(11);
	 * 
	 * // Direct is twice as far as panama distanceModelBuilder.setPortToPortDistance(loadPort, dischargePort, 10 * 16 * 24 * 2, Integer.MAX_VALUE, 10 * 16 * 24, true);
	 * 
	 * final LoadSlot loadSlot = cargoModelBuilder.makeFOBPurchase("L", loadDate.toLocalDate(), loadPort, null, entity, "5", 22.6) // .withWindowStartTime(0) // .withVisitDuration(24) //
	 * .withWindowSize(0, TimePeriod.HOURS) // .build();
	 * 
	 * final DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D", dischargeDate.toLocalDate(), dischargePort, null, entity, "7") //
	 * .withWindowStartTime(dischargeDate.toLocalTime().getHour()) // .withVisitDuration(24) // .withWindowSize(0, TimePeriod.HOURS) // .build();
	 * 
	 * evaluateWithLSOTest(scenarioRunner -> {
	 * 
	 * final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
	 * 
	 * final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer()); try (PerChainUnitScopeImpl scope =
	 * injector.getInstance(PerChainUnitScopeImpl.class)) { scope.enter(); final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
	 * 
	 * @NonNull final IModifiableSequences manipulatedSequences = sequencesManipulator
	 * .createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, loadSlot, dischargeSlot));
	 * 
	 * final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class); scheduler.setUseCanalBasedWindowTrimming(true); scheduler.setUsePriceBasedWindowTrimming(false); final
	 * ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(manipulatedSequences); final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();
	 * 
	 * final IResource r0 = manipulatedSequences.getResources().get(0);
	 * 
	 * final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);
	 * 
	 * assertEquals(AvailableRouteChoices.PANAMA_ONLY, ptr_r0_cargo.getSlotNextVoyageOptions(ptr_r0_cargo.getFirstSlot()));
	 * 
	 * final PanamaSlotsConstraintChecker checker = new PanamaSlotsConstraintChecker(PanamaSlotsConstraintCheckerFactory.NAME);// injector.injectMembers(checker);
	 * 
	 * checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector()), null, new ArrayList<>());
	 * assertFalse(checker.checkConstraints(manipulatedSequences, null, new ArrayList<>())); } }); }
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void relaxedLimits_NouthBound_Available() {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2017, 7, 1));

		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(lngScenarioModel);
		cargoModelBuilder.initCanalBookings();
		final CanalBookings canalBookings = cargoModel.getCanalBookings();

		canalBookings.setStrictBoundaryOffsetDays(0);
		canalBookings.setRelaxedBoundaryOffsetDays(60);
		canalBookings.setFlexibleBookingAmountNorthbound(1);
		canalBookings.setFlexibleBookingAmountSouthbound(1);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();
		vesselAvailability.getVessel().setMaxSpeed(16.0);
		vesselAvailability.setEndBy(LocalDateTime.of(2017, 7, 23, 0, 0));

		@NonNull
		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);
		@NonNull
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_QUINTERO);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JULY, 1, 0, 0, 0);
		// journey could be made direct
		final LocalDateTime dischargeDate = loadDate.plusDays(11);

		// Direct is twice as far as panama
		distanceModelBuilder.setPortToPortDistance(loadPort, dischargePort, 10 * 16 * 24 * 2, Integer.MAX_VALUE, 10 * 16 * 24, true);
		@NonNull
		final Port panama_entrance = portFinder.findPortById(InternalDataConstants.PORT_COLON);
		@NonNull
		final Port panama_exit = portFinder.findPortById(InternalDataConstants.PORT_BALBOA);

		// Load port to Panama canal entrance distance
		distanceModelBuilder.setPortToPortDistance(loadPort, panama_entrance, RouteOption.DIRECT, 10 * 16 * 12,true);
		// Distance from Panama canal exit to the discharge port 
		// we set it to zero, so it's not double accounted when compute travel time from Panama Canal Entrance port to the discharge port
		distanceModelBuilder.setPortToPortDistance(panama_exit, dischargePort, RouteOption.DIRECT, 0, true);
		// Distance from Panama canal entrance to the discharge via Panama canal
		distanceModelBuilder.setPortToPortDistance(panama_entrance, dischargePort, RouteOption.PANAMA, 10 * 16 * 12,true);

		final LoadSlot loadSlot = cargoModelBuilder.makeFOBPurchase("L", loadDate.toLocalDate(), loadPort, null, entity, "5", 22.6) //
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build();

		final DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D", dischargeDate.toLocalDate(), dischargePort, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, loadSlot, dischargeSlot));

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);

				assertEquals(AvailableRouteChoices.PANAMA_ONLY, ptr_r0_cargo.getSlotNextVoyageOptions(ptr_r0_cargo.getFirstSlot()));

				final PanamaSlotsConstraintChecker checker = new PanamaSlotsConstraintChecker(PanamaSlotsConstraintCheckerFactory.NAME);//
				injector.injectMembers(checker);

				checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector()), null, new ArrayList<>());
				assertTrue(checker.checkConstraints(manipulatedSequences, null, new ArrayList<>()));
			}
		});
	}

	/*
	 * @Test
	 * 
	 * @Tag(TestCategories.MICRO_TEST) public void northbound_cant_direct_can_panama() {
	 * 
	 * //This test expects the Panama optimisation constraint to fail due to the old southbound panama logic, therefore we use the old logic for it.
	 * NonLicenseFeatures.setSouthboundIdleTimeRuleEnabled(false);
	 * 
	 * // map into same timezone to make expectations easier portModelBuilder.setAllExistingPortsToUTC();
	 * 
	 * lngScenarioModel.setPromptPeriodStart(LocalDate.of(2017, 7, 1));
	 * 
	 * final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(lngScenarioModel); final CanalBookings canalBookings = cargoModel.getCanalBookings();
	 * 
	 * canalBookings.setStrictBoundaryOffsetDays(0); canalBookings.setRelaxedBoundaryOffsetDays(60); canalBookings.setNorthboundMaxIdleDays(5);
	 * 
	 * final VesselAvailability vesselAvailability = getDefaultVesselAvailability(); vesselAvailability.getVessel().setMaxSpeed(16.0); vesselAvailability.setEndBy(LocalDateTime.of(2017, 7, 23, 0, 0));
	 * 
	 * @NonNull final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_QUINTERO); loadPort.getCapabilities().add(PortCapability.LOAD);
	 * 
	 * @NonNull final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS); dischargePort.getCapabilities().add(PortCapability.DISCHARGE);
	 * 
	 * final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JULY, 1, 0, 0, 0); // journey could be made direct final LocalDateTime dischargeDate = loadDate.plusDays(15);
	 * 
	 * // Direct is twice as far as panama distanceModelBuilder.setPortToPortDistance(loadPort, dischargePort, 20 * 16 * 24, Integer.MAX_VALUE, 10 * 16 * 24, true);
	 * 
	 * final LoadSlot loadSlot = cargoModelBuilder.makeFOBPurchase("L", loadDate.toLocalDate(), loadPort, null, entity, "5", 22.6) // .withWindowStartTime(0) // .withVisitDuration(0) //
	 * .withWindowSize(0, TimePeriod.HOURS) // .build();
	 * 
	 * final DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D", dischargeDate.toLocalDate(), dischargePort, null, entity, "7") //
	 * .withWindowStartTime(dischargeDate.toLocalTime().getHour()) // .withVisitDuration(0) // .withWindowSize(0, TimePeriod.HOURS) // .build();
	 * 
	 * evaluateWithLSOTest(scenarioRunner -> {
	 * 
	 * final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
	 * 
	 * final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer()); try (PerChainUnitScopeImpl scope =
	 * injector.getInstance(PerChainUnitScopeImpl.class)) { scope.enter(); final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
	 * 
	 * @NonNull final IModifiableSequences manipulatedSequences = sequencesManipulator
	 * .createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, loadSlot, dischargeSlot));
	 * 
	 * final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class); scheduler.setUseCanalBasedWindowTrimming(true); scheduler.setUsePriceBasedWindowTrimming(false); final
	 * ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(manipulatedSequences); final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();
	 * 
	 * final IResource r0 = manipulatedSequences.getResources().get(0);
	 * 
	 * final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);
	 * 
	 * // make sure the 5 days are added assertEquals(24 * 15, ptr_r0_cargo.getSlots().get(1).getTimeWindow().getInclusiveStart());
	 * 
	 * assertEquals(AvailableRouteChoices.PANAMA_ONLY, ptr_r0_cargo.getSlotNextVoyageOptions(ptr_r0_cargo.getFirstSlot()));
	 * 
	 * final PanamaSlotsConstraintChecker checker = new PanamaSlotsConstraintChecker(PanamaSlotsConstraintCheckerFactory.NAME);// injector.injectMembers(checker);
	 * 
	 * checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector()), null, new ArrayList<>());
	 * assertFalse(checker.checkConstraints(manipulatedSequences, null, new ArrayList<>())); } }); }
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void northbound_cant_direct_cant_panama() {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2017, 7, 1));

		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(lngScenarioModel);
		cargoModelBuilder.initCanalBookings();
		final CanalBookings canalBookings = cargoModel.getCanalBookings();

		canalBookings.setStrictBoundaryOffsetDays(0);
		canalBookings.setRelaxedBoundaryOffsetDays(60);
		//canalBookings.setNorthboundMaxIdleDays(5);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();
		vesselAvailability.getVessel().setMaxSpeed(16.0);
		vesselAvailability.setEndBy(LocalDateTime.of(2017, 7, 23, 0, 0));

		@NonNull
		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_QUINTERO);
		loadPort.getCapabilities().add(PortCapability.LOAD);
		@NonNull
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);
		dischargePort.getCapabilities().add(PortCapability.DISCHARGE);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JULY, 1, 0, 0, 0);
		// journey could be made direct
		final LocalDateTime dischargeDate = loadDate.plusDays(10);

		// Direct is twice as far as panama
		distanceModelBuilder.setPortToPortDistance(loadPort, dischargePort, 20 * 16 * 24, Integer.MAX_VALUE, 10 * 16 * 24, true);

		@NonNull
		final Port panama_entrance = portFinder.findPortById(InternalDataConstants.PORT_BALBOA);
		@NonNull
		final Port panama_exit = portFinder.findPortById(InternalDataConstants.PORT_COLON);

		// Load port to Panama canal entrance distance
		distanceModelBuilder.setPortToPortDistance(loadPort, panama_entrance, RouteOption.DIRECT, 10 * 16 * 12,true);
		// Distance from Panama canal exit to the discharge port 
		// we set it to zero, so it's not double accounted when compute travel time from Panama Canal Entrance port to the discharge port
		distanceModelBuilder.setPortToPortDistance(panama_exit, dischargePort, RouteOption.DIRECT, 0, true);
		// Distance from Panama canal entrance to the discharge via Panama canal
		distanceModelBuilder.setPortToPortDistance(panama_entrance, dischargePort, RouteOption.PANAMA, 10 * 16 * 12,true);
		
		final LoadSlot loadSlot = cargoModelBuilder.makeFOBPurchase("L", loadDate.toLocalDate(), loadPort, null, entity, "5", 22.6) //
				.withWindowStartTime(0) //
				.withVisitDuration(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build();

		final DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D", dischargeDate.toLocalDate(), dischargePort, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, loadSlot, dischargeSlot));

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);

				// make sure the 5 days are NOT added
				assertEquals(24 * 10, ptr_r0_cargo.getSlots().get(1).getTimeWindow().getInclusiveStart());

				assertEquals(AvailableRouteChoices.PANAMA_ONLY, ptr_r0_cargo.getSlotNextVoyageOptions(ptr_r0_cargo.getFirstSlot()));
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void relaxedLimits_SouthBound_Available() {

		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2017, 7, 1));

		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(lngScenarioModel);
		cargoModelBuilder.initCanalBookings();
		final CanalBookings canalBookings = cargoModel.getCanalBookings();

		canalBookings.setStrictBoundaryOffsetDays(0);
		canalBookings.setRelaxedBoundaryOffsetDays(60);
		canalBookings.setFlexibleBookingAmountNorthbound(1);
		canalBookings.setFlexibleBookingAmountSouthbound(1);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();
		vesselAvailability.getVessel().setMaxSpeed(16.0);

		@NonNull
		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);
		@NonNull
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_QUINTERO);
		@NonNull
		final Port panama_entrance = portFinder.findPortById(InternalDataConstants.PORT_COLON);
		@NonNull
		final Port panama_exit = portFinder.findPortById(InternalDataConstants.PORT_BALBOA);		

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JULY, 1, 0, 0, 0);
		// journey could be made direct
		final LocalDateTime dischargeDate = loadDate.plusDays(11);

		// Direct is twice as far as panama
		distanceModelBuilder.setPortToPortDistance(loadPort, dischargePort, 10 * 16 * 24 * 2, Integer.MAX_VALUE, 10 * 16 * 24, true);
		// Load port to Panama canal entrance distance
		distanceModelBuilder.setPortToPortDistance(loadPort, panama_entrance, RouteOption.DIRECT, 10 * 16 * 12,true);
		// Distance from Panama canal exit to the discharge port 
		// we set it to zero, so it's not double accounted when compute travel time from Panama Canal Entrance port to the discharge port
		distanceModelBuilder.setPortToPortDistance(panama_exit, dischargePort, RouteOption.DIRECT, 0, true);
		// Distance from Panama canal entrance to the discharge via Panama canal
		distanceModelBuilder.setPortToPortDistance(panama_entrance, dischargePort, RouteOption.PANAMA, 10 * 16 * 12,true);

		final LoadSlot loadSlot = cargoModelBuilder.makeFOBPurchase("L", loadDate.toLocalDate(), loadPort, null, entity, "5", 22.6) //
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build();

		final DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D", dischargeDate.toLocalDate(), dischargePort, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselAvailability, loadSlot, dischargeSlot));

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);

				assertEquals(AvailableRouteChoices.PANAMA_ONLY, ptr_r0_cargo.getSlotNextVoyageOptions(ptr_r0_cargo.getFirstSlot()));

				final PanamaSlotsConstraintChecker checker = new PanamaSlotsConstraintChecker(PanamaSlotsConstraintCheckerFactory.NAME);//
				injector.injectMembers(checker);

				checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector()), null, new ArrayList<>());
				assertTrue(checker.checkConstraints(manipulatedSequences, null, new ArrayList<>()));
			}
		});
	}
}
