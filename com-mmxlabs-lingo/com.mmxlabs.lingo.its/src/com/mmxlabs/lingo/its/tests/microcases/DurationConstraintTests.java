/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.google.inject.Injector;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
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
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.scheduling.ScheduledTimeWindows;
import com.mmxlabs.scheduler.optimiser.scheduling.TimeWindowScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

@RunWith(value = ShiroRunner.class)
public class DurationConstraintTests extends AbstractMicroTestCase {

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Override
	public IScenarioDataProvider importReferenceData() throws MalformedURLException {

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

		canalBookings.setStrictBoundaryOffsetDays(30);
		canalBookings.setRelaxedBoundaryOffsetDays(90);
		canalBookings.setFlexibleBookingAmountNorthbound(0);
		canalBookings.setFlexibleBookingAmountSouthbound(0);

		final LNGScenarioModel scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		scenarioModel.setPromptPeriodStart(LocalDate.of(2017, 6, 1));
		scenarioModel.setPromptPeriodEnd(LocalDate.of(2017, 9, 1));

		// portModelBuilder.setAllExistingPortsToUTC();

		return scenarioDataProvider;
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
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setMaxSpeed(16.0);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();
		return vesselAvailability;
	}

	private VesselAvailability getDefaultVesselAvailabilityWithTW(LocalDateTime windowStart, LocalDateTime windowEnd) {
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setMaxSpeed(16.0);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(windowStart, windowEnd) //
				.build();
		return vesselAvailability;
	}

	private VesselAvailability getVesselAvailabilityWithTW(String vesselName, LocalDateTime windowStart, LocalDateTime windowEnd) {
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setMaxSpeed(16.0);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(windowStart, windowEnd) //
				.build();
		return vesselAvailability;
	}

	private VesselAvailability getVesselAvailabilityWithoutTW(String vesselName) {
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setMaxSpeed(16.0);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();
		return vesselAvailability;
	}

	@Test
	@Category({ MicroTest.class })
	public void maxDurationTrimmingEmptyVesselTest() {

		final VesselAvailability vesselAvailability = getDefaultVesselAvailabilityWithTW(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0), LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));

		// Set the end requirement's time window and max duration
		vesselAvailability.setMaxDuration(26);
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JUNE, 30, 0, 0, 0));
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JUNE, 25, 0, 0, 0));

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();
				final ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

				// Set time scheduler settings
				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(false);
				scheduler.setUsePriceBasedWindowTrimming(false);

				final ScheduledTimeWindows schedule = scheduler.schedule(initialSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = initialSequences.getResources().get(0);
				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(0);

				// Assert expected result (Truncated end window)
				assertEquals(24 * 26 + 1, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getReturnSlot()).getExclusiveEnd());
				assertEquals(24 * 24, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getReturnSlot()).getInclusiveStart());

				assertEquals(1, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getExclusiveEnd());
				assertEquals(0, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getInclusiveStart());
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void maxDurationNoStartVesselTest() {

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();

		// Set the end requirement's time window and max duration
		vesselAvailability.setMaxDuration(26);
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JUNE, 30, 0, 0, 0));
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JUNE, 25, 0, 0, 0));

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Manzanillo");

		// Construct the cargoes
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, Month.JUNE, 1), port1, null, entity, "7") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, Month.JUNE, 12), port2, null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();
				final ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

				// Set time scheduler settings
				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(false);
				scheduler.setUsePriceBasedWindowTrimming(false);

				final ScheduledTimeWindows schedule = scheduler.schedule(initialSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = initialSequences.getResources().get(0);
				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(0);

				// Assert expected result (Truncated end window)
				// TODO: NO RETURN SLOT HERE
				assertEquals(8, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getExclusiveEnd());
				assertEquals(0, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getInclusiveStart());

				final IPortTimeWindowsRecord ptr_r1_cargo = records.get(r0).get(1);
				assertEquals(24 * 11 + 24 + 1, ptr_r1_cargo.getSlotFeasibleTimeWindow(ptr_r1_cargo.getReturnSlot()).getExclusiveEnd());
				assertEquals(24 * 11 + 24, ptr_r1_cargo.getSlotFeasibleTimeWindow(ptr_r1_cargo.getReturnSlot()).getInclusiveStart());
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void minDurationTrimmingEmptyVesselTest() {

		final VesselAvailability vesselAvailability = getDefaultVesselAvailabilityWithTW(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0), LocalDateTime.of(2017, Month.JUNE, 20, 0, 0, 0));

		// Set the end requirement's time window and max duration
		vesselAvailability.setMinDuration(3);
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JUNE, 25, 0, 0, 0));
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JUNE, 25, 0, 0, 0));

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();
				final ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

				// Set Time Scheduler Settings
				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(false);
				scheduler.setUsePriceBasedWindowTrimming(false);

				final ScheduledTimeWindows schedule = scheduler.schedule(initialSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = initialSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(0);

				// Assert expected result (Truncated start window)
				assertEquals(24 * 19 + 1, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getExclusiveEnd());
				assertEquals(0, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getInclusiveStart());

				assertEquals(24 * 24 + 1, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getReturnSlot()).getExclusiveEnd());
				assertEquals(24 * 24, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getReturnSlot()).getInclusiveStart());
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void minDurationNoEndVesselTest() {

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();
		vesselAvailability.setStartBy(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));
		vesselAvailability.setStartAfter(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));

		// Set the end requirement's time window and max duration
		vesselAvailability.setMinDuration(90);

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Manzanillo");

		// Construct the cargoes
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, Month.JUNE, 1), port1, null, entity, "7") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, Month.JUNE, 12), port2, null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();
				final ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

				// Set time scheduler settings
				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(false);
				scheduler.setUsePriceBasedWindowTrimming(false);

				final ScheduledTimeWindows schedule = scheduler.schedule(initialSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = initialSequences.getResources().get(0);
				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(0);

				// Assert expected result (Truncated end window)
				assertEquals(1, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getExclusiveEnd());
				assertEquals(0, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getInclusiveStart());

				// FIXME: Off by one (+1)
				final IPortTimeWindowsRecord ptr_r1_cargo = records.get(r0).get(1);
				assertEquals(24 * 90, ptr_r1_cargo.getSlotFeasibleTimeWindow(ptr_r1_cargo.getReturnSlot()).getExclusiveEnd());
				assertEquals(24 * 90 - 1, ptr_r1_cargo.getSlotFeasibleTimeWindow(ptr_r1_cargo.getReturnSlot()).getInclusiveStart());
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void minMaxDurationTrimmingEmptyVesselTest() {

		final VesselAvailability vesselAvailability = getDefaultVesselAvailabilityWithTW(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0), LocalDateTime.of(2017, Month.JUNE, 20, 0, 0, 0));

		// Set the end requirement's time window and max duration
		vesselAvailability.setMaxDuration(20);
		vesselAvailability.setMinDuration(3);
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JULY, 20, 0, 0, 0));
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JUNE, 25, 0, 0, 0));

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();
				final ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

				// Set time scheduler settings
				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(false);
				scheduler.setUsePriceBasedWindowTrimming(false);

				final ScheduledTimeWindows schedule = scheduler.schedule(initialSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = initialSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(0);

				// Assert expected result (Truncated start AND end windows)
				assertEquals(24 * 39 + 1, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getReturnSlot()).getExclusiveEnd());
				assertEquals(24 * 24, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getReturnSlot()).getInclusiveStart());

				assertEquals(24 * 19 + 1, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getExclusiveEnd());
				assertEquals(0, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getInclusiveStart());
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void minMaxDurationPriceBasedTrimmingCargoTest() {

		final VesselAvailability vesselAvailability = getDefaultVesselAvailabilityWithTW(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0), LocalDateTime.of(2017, Month.JUNE, 20, 0, 0, 0));

		// Set the end requirement's time window and max duration
		vesselAvailability.setMaxDuration(20);
		vesselAvailability.setMinDuration(3);
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JULY, 20, 0, 0, 0));
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JUNE, 25, 0, 0, 0));

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Manzanillo");

		// Construct the cargoes
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, Month.JUNE, 27), port1, null, entity, "7") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, Month.JUNE, 29), port2, null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();
				final ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

				// Set time scheduler settings
				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(false);
				scheduler.setUsePriceBasedWindowTrimming(true);

				final ScheduledTimeWindows schedule = scheduler.schedule(initialSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = initialSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(0);
				final IPortTimeWindowsRecord ptr_r1_cargo = records.get(r0).get(1);

				// Assert expected result (Truncated start AND end windows)
				assertEquals(24 * 39 + 1, ptr_r1_cargo.getSlotFeasibleTimeWindow(ptr_r1_cargo.getReturnSlot()).getExclusiveEnd());
				assertEquals(24 * 24, ptr_r1_cargo.getSlotFeasibleTimeWindow(ptr_r1_cargo.getReturnSlot()).getInclusiveStart());

				assertEquals(24 * 19 + 1, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getExclusiveEnd());
				assertEquals(0, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getInclusiveStart());
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void maxDurationWithPanamaBookingTest() {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		// Create vessel with a default availability
		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();
		vesselAvailability.setStartBy(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));
		vesselAvailability.setStartAfter(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));
		// Set vessel time constraints
		vesselAvailability.setMaxDuration(90);
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.SEPTEMBER, 30, 0, 0, 0));
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.SEPTEMBER, 25, 0, 0, 0));

		// Create cargo
		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Quintero");

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(13);

		final Cargo cargo = createFobDesCargo(vesselAvailability, port1, port2, loadDate, dischargeDate);

		cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2017, Month.JUNE, 7), cargo.getSortedSlots().get(0));

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();
				final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
				@NonNull
				final IModifiableSequences manipulatedSequences = sequencesManipulator
						.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer(), vesselAvailability, cargo));
				// Time scheduler settings
				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);

				final ScheduledTimeWindows schedule = scheduler.schedule(manipulatedSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = manipulatedSequences.getResources().get(0);
				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(1);

				// Check expectation
				assertNotNull(ptr_r0_cargo.getRouteOptionBooking(ptr_r0_cargo.getFirstSlot()));
				assertEquals(24 * 90 + 1, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getReturnSlot()).getExclusiveEnd());
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void maxDurationOptimiseOutOfRangeCargoTest() throws Exception {

		// Create first vessel
		// Set the end requirement's time window and max duration
		final VesselAvailability vesselAvailability = getDefaultVesselAvailabilityWithTW(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0), LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));
		vesselAvailability.setMaxDuration(25);
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JUNE, 30, 0, 0, 0));
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JUNE, 25, 0, 0, 0));

		// Create second vessel
		// Set the end requirement's time window and max duration
		final VesselAvailability vesselAvailability2 = getVesselAvailabilityWithTW("STEAM-145.8", LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0), LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));
		vesselAvailability2.setEndBy(LocalDateTime.of(2017, Month.JULY, 30, 0, 0, 0));
		vesselAvailability2.setEndAfter(LocalDateTime.of(2017, Month.JULY, 25, 0, 0, 0));

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Manzanillo");

		// Construct the cargoes
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, Month.JUNE, 27), port1, null, entity, "7") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, Month.JUNE, 29), port2, null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			final IScenarioDataProvider optimiserScenarioDataProvider = scenarioToOptimiserBridge.getOptimiserScenario();

			// Check if the cargo is scheduled
			assertEquals(1, ScenarioModelUtil.getCargoModel(optimiserScenarioDataProvider).getCargoes().size());

			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			assertNotNull(schedule);

			// Check the identity of the vessel
			assertEquals(vesselAvailability, schedule.getSequences().get(0).getVesselAvailability());
			assertEquals(vesselAvailability2, schedule.getSequences().get(1).getVesselAvailability());

			// Check On which vessel the cargo is
			assertTrue(schedule.getSequences().get(1).getEvents().size() > 3);
			assertEquals(3, schedule.getSequences().get(0).getEvents().size());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void minDurationOptimiseInCargoTest() throws Exception {

		// Create first vessel
		// Set the end requirement's time window and max duration
		final VesselAvailability vesselAvailability = getDefaultVesselAvailabilityWithTW(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0), LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));
		vesselAvailability.setMinDuration(76);
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.AUGUST, 26, 0, 0, 0));
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.AUGUST, 27, 0, 0, 0));
		vesselAvailability.setTimeCharterRate("500000");

		// Create second vessel
		// Set the end requirement's time window and max duration
		final VesselAvailability vesselAvailability2 = getVesselAvailabilityWithTW("STEAM-145.8", LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0), LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));
		vesselAvailability2.setEndBy(LocalDateTime.of(2017, Month.JULY, 30, 0, 0, 0));
		vesselAvailability2.setEndAfter(LocalDateTime.of(2017, Month.JULY, 25, 0, 0, 0));
		vesselAvailability2.setTimeCharterRate("5000");

		// Construct the cargo
		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Manzanillo");

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, Month.JUNE, 27), port1, null, entity, "7") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, Month.JUNE, 29), port2, null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability2, 1) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			final IScenarioDataProvider optimiserScenarioDataProvider = scenarioToOptimiserBridge.getOptimiserScenario();

			// Check if the cargo is scheduled
			assertEquals(1, ScenarioModelUtil.getCargoModel(optimiserScenarioDataProvider).getCargoes().size());

			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			assertNotNull(schedule);

			// Check if the cargo moved from ship 1 to ship 2
			assertEquals(vesselAvailability, schedule.getSequences().get(0).getVesselAvailability());
			assertEquals(vesselAvailability2, schedule.getSequences().get(1).getVesselAvailability());

			assertTrue(schedule.getSequences().get(0).getEvents().size() > 3);
			assertEquals(3, schedule.getSequences().get(1).getEvents().size());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void maxDurationWithLateness() throws Exception {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();
		vesselAvailability.setStartBy(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));
		vesselAvailability.setMaxDuration(35);
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JULY, 1, 0, 0, 0));
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JULY, 10, 0, 0, 0));

		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");

		final DryDockEvent event = cargoModelBuilder.makeDryDockEvent("drydock1", LocalDateTime.of(2017, Month.JULY, 7, 0, 0, 0), LocalDateTime.of(2017, Month.JULY, 7, 0, 0, 0), port1) //
				.withDurationInDays(1) //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();

				final ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

				// Set time scheduler settings
				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(false);
				scheduler.setUsePriceBasedWindowTrimming(false);

				final ScheduledTimeWindows schedule = scheduler.schedule(initialSequences);
			}
			final EndEvent endEvent = MicroTestUtils.findVesselEndEvent(lngScenarioModel);
			final PortVisitLateness lateness = endEvent.getLateness();
			assertNotNull(lateness);
			assertEquals(48, lateness.getLatenessInHours());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void maxDurationSchedulerModelEmptyVesselTest() throws Exception {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final VesselAvailability vesselAvailability = getDefaultVesselAvailabilityWithTW(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0), LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));

		// Set the end requirement's time window and max duration
		vesselAvailability.setMaxDuration(26);
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JUNE, 30, 0, 0, 0));
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JUNE, 25, 0, 0, 0));

		optimiseWithLSOTest(scenarioRunner -> {

		});

		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
		final Schedule schedule = scheduleModel.getSchedule();
		Event start = schedule.getSequences().get(0).getEvents().get(0);
		Event end = schedule.getSequences().get(0).getEvents().get(2);

		assertTrue(start.getStart().toLocalDateTime().isEqual(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0)));
		assertTrue(end.getEnd().toLocalDateTime().isEqual(LocalDateTime.of(2017, Month.JUNE, 25, 0, 0, 0)));
	}

	@Test
	@Category({ MicroTest.class })
	public void minDurationSchedulerModelEmptyVesselTest() throws Exception {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final VesselAvailability vesselAvailability = getDefaultVesselAvailabilityWithTW(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0), LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));

		// Set the end requirement's time window and max duration
		vesselAvailability.setMinDuration(27);
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JUNE, 30, 0, 0, 0));
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JUNE, 25, 0, 0, 0));

		optimiseWithLSOTest(scenarioRunner -> {

		});

		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
		final Schedule schedule = scheduleModel.getSchedule();
		Event start = schedule.getSequences().get(0).getEvents().get(0);
		Event end = schedule.getSequences().get(0).getEvents().get(2);

		assertTrue(start.getStart().toLocalDateTime().isEqual(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0)));
		assertTrue(end.getEnd().toLocalDateTime().isEqual(LocalDateTime.of(2017, Month.JUNE, 28, 0, 0, 0)));
	}

	@Test
	@Category({ MicroTest.class })
	public void minDurationSatisfiedEmptyVesselTest() throws Exception {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final VesselAvailability vesselAvailability = getDefaultVesselAvailabilityWithTW(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0), LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));

		// Set the end requirement's time window and max duration
		vesselAvailability.setMinDuration(27);
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JUNE, 30, 0, 0, 0));
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JUNE, 25, 0, 0, 0));

		optimiseWithLSOTest(scenarioRunner -> {

		});

		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
		final Schedule schedule = scheduleModel.getSchedule();
		Event start = schedule.getSequences().get(0).getEvents().get(0);
		Event end = schedule.getSequences().get(0).getEvents().get(2);
		final long startTimestamp = start.getStart().toEpochSecond();
		final long endTimestamp = end.getStart().toEpochSecond();

		final long minDeltaInSeconds = endTimestamp - startTimestamp;
		final long minDeltaInHours = (minDeltaInSeconds / 3600);

		if (minDeltaInHours < (27 * 24)) {
			assertTrue(false);
		}
	}

	@Test
	@Category({ MicroTest.class })
	public void maxDurationSatisfiedEmptyVesselTest() throws Exception {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final VesselAvailability vesselAvailability = getDefaultVesselAvailabilityWithTW(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0), LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));

		// Set the end requirement's time window and max duration
		vesselAvailability.setMaxDuration(27);
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JUNE, 30, 0, 0, 0));
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JUNE, 25, 0, 0, 0));

		optimiseWithLSOTest(scenarioRunner -> {

		});

		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
		final Schedule schedule = scheduleModel.getSchedule();
		Event start = schedule.getSequences().get(0).getEvents().get(0);
		Event end = schedule.getSequences().get(0).getEvents().get(2);
		final long startTimestamp = start.getStart().toEpochSecond();
		final long endTimestamp = end.getStart().toEpochSecond();

		final long minDeltaInSeconds = endTimestamp - startTimestamp;
		final long minDeltaInHours = (minDeltaInSeconds / 3600);

		if (minDeltaInHours > (27 * 24)) {
			assertTrue(false);
		}
	}

	@Test
	@Category({ MicroTest.class })
	public void minDurationSatisfiedCargoVesselTest() throws Exception {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final VesselAvailability vesselAvailability = getDefaultVesselAvailabilityWithTW(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0), LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));

		// Set the end requirement's time window and max duration
		vesselAvailability.setMinDuration(27);
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JUNE, 30, 0, 0, 0));
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JUNE, 25, 0, 0, 0));

		// Construct the cargo
		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Manzanillo");

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, Month.JUNE, 27), port1, null, entity, "7") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, Month.JUNE, 29), port2, null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

		});

		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
		final Schedule schedule = scheduleModel.getSchedule();
		Event start = schedule.getSequences().get(0).getEvents().get(0);
		Event end = schedule.getSequences().get(0).getEvents().get(schedule.getSequences().get(0).getEvents().size() - 1);
		final long startTimestamp = start.getStart().toEpochSecond();
		final long endTimestamp = end.getStart().toEpochSecond();

		final long minDeltaInSeconds = endTimestamp - startTimestamp;
		final long minDeltaInHours = (minDeltaInSeconds / 3600);

		if (minDeltaInHours < (27 * 24)) {
			assertTrue(false);
		}
	}

	@Test
	@Category({ MicroTest.class })
	public void maxDurationSatisfiedCargoVesselTest() throws Exception {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final VesselAvailability vesselAvailability = getDefaultVesselAvailabilityWithTW(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0), LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));

		// Set the end requirement's time window and max duration
		vesselAvailability.setMaxDuration(27);
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JUNE, 30, 0, 0, 0));
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JUNE, 25, 0, 0, 0));

		// Construct the cargo
		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Manzanillo");

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, Month.JUNE, 25), port1, null, entity, "7") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, Month.JUNE, 26), port2, null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

		});

		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
		final Schedule schedule = scheduleModel.getSchedule();
		Event start = schedule.getSequences().get(0).getEvents().get(0);
		Event end = schedule.getSequences().get(0).getEvents().get(schedule.getSequences().get(0).getEvents().size() - 2);
		final long startTimestamp = start.getStart().toEpochSecond();
		final long endTimestamp = end.getStart().toEpochSecond();

		final long minDeltaInSeconds = endTimestamp - startTimestamp;
		final long minDeltaInHours = (minDeltaInSeconds / 3600);

		if (minDeltaInHours > (27 * 24)) {
			assertTrue(false);
		}
	}
}
