/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.google.inject.Injector;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.extensions.panamaslots.PanamaSlotsConstraintChecker;
import com.mmxlabs.models.lng.transformer.extensions.panamaslots.PanamaSlotsConstraintCheckerFactory;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.scheduler.optimiser.scheduling.ScheduledTimeWindows;
import com.mmxlabs.scheduler.optimiser.scheduling.TimeWindowScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

@RunWith(value = ShiroRunner.class)
public class PanamaSlotBookingsTests extends AbstractMicroTestCase {

	@Override
	public LNGScenarioModel importReferenceData() throws MalformedURLException {

		@NonNull
		final LNGScenarioModel model = importReferenceData("/referencedata/reference-data-2/");

		final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		model.getCargoModel().setCanalBookings(canalBookings);

		PortModel portModel = ScenarioModelUtil.getPortModel(model);

		final Optional<Route> potentialPanama = portModel.getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		final Route panama = potentialPanama.get();

		portModel.getPorts().stream().filter(p -> {
			return "Colon".equals(p.getName()) || "Balboa".equals(p.getName());
		}).forEach(p -> {
			final EntryPoint ep = PortFactory.eINSTANCE.createEntryPoint();
			ep.setPort(p);
			if (p.getName().equals("Colon")) {
				ep.setName("EAST");
				panama.setEntryA(ep);
			} else {
				ep.setName("WEST");
				panama.setEntryB(ep);
			}
		});

		model.getCargoModel().getCanalBookings().setStrictBoundaryOffsetDays(30);
		model.getCargoModel().getCanalBookings().setRelaxedBoundaryOffsetDays(90);
		model.getCargoModel().getCanalBookings().setFlexibleBookingAmount(0);

		model.setPromptPeriodStart(LocalDate.of(2017, 6, 1));
		model.setPromptPeriodEnd(LocalDate.of(2017, 9, 1));
		
		return model;
	}

	private Cargo createFobDesCargo(final VesselAvailability vesselAvailability, final Port loadPort, final Port dischargePort, final LocalDateTime loadDate, final LocalDateTime dischargeDate) {
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", loadDate.toLocalDate(), loadPort, null, entity, "5") //
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), dischargePort, null, entity, "7") //
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
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		vesselClass.setMaxSpeed(16.0);
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();
		return vesselAvailability;
	}

	@Test
	@Category({ MicroTest.class })
	public void panamaSlotAvailableTest() {

		PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
		final Optional<Route> potentialPanama = portModel.getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();

		final Route panama = potentialPanama.get();

		final EntryPoint colon = panama.getEntryA();

		final CanalBookingSlot d1 = cargoModelBuilder.makeCanalBooking(panama, colon, LocalDate.of(2017, Month.JUNE, 7), null);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Quintero");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(13);

		final Cargo cargo = createFobDesCargo(vesselAvailability, port1, port2, loadDate, dischargeDate);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());

			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();

				final PanamaSlotsConstraintChecker checker = new PanamaSlotsConstraintChecker(PanamaSlotsConstraintCheckerFactory.NAME);//
				injector.injectMembers(checker);

				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargo));
				checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge), null);
				assertTrue(checker.checkConstraints(manipulatedSequences, null));
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void panamaSlotNoDubleAssignmentTest() {

		PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);

		final Optional<Route> potentialPanama = portModel.getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		final Route panama = potentialPanama.get();
		final EntryPoint colon = panama.getEntryA();

		cargoModelBuilder.makeCanalBooking(panama, colon, LocalDate.of(2017, Month.JUNE, 6), null);

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		final Vessel vessel2 = fleetModelBuilder.createVessel("vessel2", vesselClass);
		final VesselAvailability vesselAvailability2 = cargoModelBuilder.makeVesselAvailability(vessel2, entity) //
				.build();

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Quintero");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		vesselClass.setMaxSpeed(16.0);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(13);

		final Cargo cargo = createFobDesCargo(vesselAvailability, port1, port2, loadDate, dischargeDate);

		final Cargo cargo2 = createFobDesCargo(vesselAvailability2, port1, port2, loadDate, dischargeDate);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargo));
				SequenceHelper.addSequence(manipulatedSequences, scenarioToOptimiserBridge, vesselAvailability2, cargo2);

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				ScheduledTimeWindows schedule = scheduler.schedule(manipulatedSequences);
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
	@Category({ MicroTest.class })
	public void panamaSlotUnavailableTest() {

		PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);

		final Optional<Route> potentialPanama = portModel.getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		final Route panama = potentialPanama.get();

		final EntryPoint colon = panama.getEntryA();

		cargoModelBuilder.makeCanalBooking(panama, colon, LocalDate.of(2017, Month.JUNE, 7), null);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Quintero");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.AUGUST, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(10);

		final Cargo cargo = createFobDesCargo(vesselAvailability, port1, port2, loadDate, dischargeDate);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargo));

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				ScheduledTimeWindows schedule = scheduler.schedule(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);

				assertNull(ptr_r0_cargo.getRouteOptionBooking(ptr_r0_cargo.getFirstSlot()));
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void bookingButNoVoyageTest() {

		PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);

		final Optional<Route> potentialPanama = portModel.getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		final Route panama = potentialPanama.get();

		final EntryPoint colon = panama.getEntryA();

		cargoModelBuilder.makeCanalBooking(panama, colon, LocalDate.of(2017, Month.JUNE, 7), null);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Barcelona");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(13);

		final Cargo cargo = createFobDesCargo(vesselAvailability, port1, port2, loadDate, dischargeDate);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());

			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();

				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargo));

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				ScheduledTimeWindows schedule = scheduler.schedule(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);

				assertNull(ptr_r0_cargo.getRouteOptionBooking(ptr_r0_cargo.getFirstSlot()));
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void nonMatchingBookingTest() {
		PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
		final Optional<Route> potentialPanama = portModel.getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		final Route panama = potentialPanama.get();

		final EntryPoint colon = panama.getEntryA();

		cargoModelBuilder.makeCanalBooking(panama, colon, LocalDate.of(2017, Month.JUNE, 10), null);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Quintero");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(13);

		final Cargo cargo = createFobDesCargo(vesselAvailability, port1, port2, loadDate, dischargeDate);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargo));

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				ScheduledTimeWindows schedule = scheduler.schedule(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);

				assertNull(ptr_r0_cargo.getRouteOptionBooking(ptr_r0_cargo.getFirstSlot()));
			}
		});

	}

	@Test
	@Category({ MicroTest.class })
	public void bookingAssignedToSlotTest() {
		PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);

		final Optional<Route> potentialPanama = portModel.getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		final Route panama = potentialPanama.get();

		final EntryPoint colon = panama.getEntryA();

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Quintero");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(13);

		final Cargo cargo = createFobDesCargo(vesselAvailability, port1, port2, loadDate, dischargeDate);

		cargoModelBuilder.makeCanalBooking(panama, colon, LocalDate.of(2017, Month.JUNE, 7), cargo.getSortedSlots().get(0));

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargo));

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				ScheduledTimeWindows schedule = scheduler.schedule(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);

				assertNotNull(ptr_r0_cargo.getRouteOptionBooking(ptr_r0_cargo.getFirstSlot()));
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void asssignedBookingNotUsedTest() {
		PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);

		final Optional<Route> potentialPanama = portModel.getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		final Route panama = potentialPanama.get();

		final EntryPoint colon = panama.getEntryA();

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		final Vessel vessel2 = fleetModelBuilder.createVessel("vessel2", vesselClass);
		final VesselAvailability vesselAvailability2 = cargoModelBuilder.makeVesselAvailability(vessel2, entity) //
				.build();

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Quintero");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		vesselClass.setMaxSpeed(16.0);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(13);

		final Cargo cargo = createFobDesCargo(vesselAvailability, port1, port2, loadDate, dischargeDate);
		final Cargo cargo2 = createFobDesCargo(vesselAvailability2, port1, port2, loadDate, dischargeDate);

		cargoModelBuilder.makeCanalBooking(panama, colon, LocalDate.of(2017, Month.JUNE, 7), cargo2.getSortedSlots().get(0));

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargo));
				SequenceHelper.addSequence(manipulatedSequences, scenarioToOptimiserBridge, vesselAvailability2, cargo2);

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				ScheduledTimeWindows schedule = scheduler.schedule(manipulatedSequences);
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
	@Category({ MicroTest.class })
	public void journeyCanBeMadeDirectTest() {

		PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);

		final Optional<Route> potentialPanama = portModel.getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		final Route panama = potentialPanama.get();
		final EntryPoint colon = panama.getEntryA();

		cargoModelBuilder.makeCanalBooking(panama, colon, LocalDate.of(2017, Month.JUNE, 7), null);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Chita");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(25);

		final Cargo cargo = createFobDesCargo(vesselAvailability, port1, port2, loadDate, dischargeDate);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());

			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();

				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargo));

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				ScheduledTimeWindows schedule = scheduler.schedule(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);

				assertNull(ptr_r0_cargo.getRouteOptionBooking(ptr_r0_cargo.getFirstSlot()));
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void choosePanamaIfRelaxedTest() {

		PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
		lngScenarioModel.getCargoModel().getCanalBookings().setStrictBoundaryOffsetDays(0);
		lngScenarioModel.getCargoModel().getCanalBookings().setRelaxedBoundaryOffsetDays(0);
		final Optional<Route> potentialPanama = portModel.getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		final Route panama = potentialPanama.get();

		final EntryPoint colon = panama.getEntryA();

		cargoModelBuilder.makeCanalBooking(panama, colon, LocalDate.of(2017, Month.JUNE, 7), null);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");
		@NonNull
		final Port port2 = portFinder.findPort("Quintero");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.AUGUST, 1, 0, 0, 0);
		// journey could be made direct
		final LocalDateTime dischargeDate = loadDate.plusDays(30);

		final Cargo cargo = createFobDesCargo(vesselAvailability, port1, port2, loadDate, dischargeDate);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargo));

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				ScheduledTimeWindows schedule = scheduler.schedule(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);

				assertEquals(AvailableRouteChoices.OPTIMAL, ptr_r0_cargo.getSlotNextVoyageOptions(ptr_r0_cargo.getFirstSlot()));
				assertNull(ptr_r0_cargo.getRouteOptionBooking(ptr_r0_cargo.getFirstSlot()));
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void marginTest() {

		PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
		lngScenarioModel.getCargoModel().getCanalBookings().setArrivalMarginHours(4);

		final Optional<Route> potentialPanama = portModel.getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		final Route panama = potentialPanama.get();

		final EntryPoint colon = panama.getEntryA();

		cargoModelBuilder.makeCanalBooking(panama, colon, LocalDate.of(2017, Month.JUNE, 6), null);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Quintero");

		// map into same timezone to make expectations easier
		port1.setTimeZone("America/Panama");
		port2.setTimeZone("America/Panama");

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(10);

		final Cargo cargo = createFobDesCargo(vesselAvailability, port1, port2, loadDate, dischargeDate);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargo));

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				ScheduledTimeWindows schedule = scheduler.schedule(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);

				assertNull(ptr_r0_cargo.getRouteOptionBooking(ptr_r0_cargo.getFirstSlot()));
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void windowEndTrimmingTest() {

		PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
		final Optional<Route> potentialPanama = portModel.getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();

		final Route panama = potentialPanama.get();

		final EntryPoint colon = panama.getEntryA();

		cargoModelBuilder.makeCanalBooking(panama, colon, LocalDate.of(2017, Month.JUNE, 7), null);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Brownsville");

		@NonNull
		final Port port3 = portFinder.findPort("Himeji");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

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
			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargo, cargo2));
				// SequenceHelper.addSequence(manipulatedSequences, scenarioToOptimiserBridge, vesselAvailability, cargo2);

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				ScheduledTimeWindows schedule = scheduler.schedule(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);
				final IPortTimeWindowsRecord ptr_r0_cargo2 = records.get(r0).get(2);

				assertEquals(76, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getSlots().get(1)).getExclusiveEnd());
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void noSuezDistanceAvailableTest() {

		PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
		final Optional<Route> potentialPanama = portModel.getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		final Optional<Route> potentialSuez = portModel.getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.SUEZ).findFirst();

		final Route panama = potentialPanama.get();
		final Route suez = potentialSuez.get();

		final EntryPoint colon = panama.getEntryA();

		final CanalBookingSlot d1 = cargoModelBuilder.makeCanalBooking(panama, colon, LocalDate.of(2017, Month.JUNE, 7), null);

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Manzanillo");

		@NonNull
		final Port portUnrelevant = portFinder.findPort("Brownsville");

		boolean removed = suez.getLines().removeIf(e -> e.getFrom().equals(port1) && e.getTo().equals(port2));
		assert removed;

		boolean removed2 = suez.getLines().removeIf(e -> e.getFrom().equals(port2) && e.getTo().equals(port1));
		assert removed2;

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		final LocalDateTime startDate = LocalDateTime.of(2017, Month.MAY, 15, 0, 0, 0);
		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(30);

		final Cargo cargoUnrelevant = createFobDesCargo(vesselAvailability, port1, portUnrelevant, startDate, startDate.plusDays(6));
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
			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargoUnrelevant, cargo));
				// SequenceHelper.addSequence(manipulatedSequences, scenarioToOptimiserBridge, vesselAvailability, cargo2);

				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);
				ScheduledTimeWindows schedule = scheduler.schedule(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(2);
				assertNotNull(ptr_r0_cargo.getRouteOptionBooking(ptr_r0_cargo.getFirstSlot()));
			}
		});
	}
}
