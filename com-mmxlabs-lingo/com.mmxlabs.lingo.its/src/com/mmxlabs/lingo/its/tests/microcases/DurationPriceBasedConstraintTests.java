/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.inject.Injector;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
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
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.scheduling.ScheduledTimeWindows;
import com.mmxlabs.scheduler.optimiser.scheduling.TimeWindowScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

@SuppressWarnings({ "unused", "null" })
@ExtendWith(ShiroRunner.class)
public class DurationPriceBasedConstraintTests extends AbstractMicroTestCase {

	@Override
	public IScenarioDataProvider importReferenceData() throws Exception {

		@NonNull
		final IScenarioDataProvider scenarioDataProvider = super.importReferenceData();

		final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		@NonNull
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);
		cargoModel.setCanalBookings(canalBookings);

		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);

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
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setMaxSpeed(16.0);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();
		return vesselAvailability;
	}

	private VesselAvailability getDefaultVesselAvailabilityWithTW(LocalDateTime windowStart, LocalDateTime windowEnd) {
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setMaxSpeed(16.0);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(windowStart, windowEnd) //
				.build();
		return vesselAvailability;
	}

	private VesselAvailability getVesselAvailabilityWithTW(String vesselName, LocalDateTime windowStart, LocalDateTime windowEnd) {
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setMaxSpeed(16.0);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(windowStart, windowEnd) //
				.build();
		return vesselAvailability;
	}

	private VesselAvailability getVesselAvailabilityWithoutTW(String vesselName) {
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setMaxSpeed(16.0);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();
		return vesselAvailability;
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void maxDurationPriceBasedTrimmingEmptyVesselTest() {

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
				scheduler.setUsePriceBasedWindowTrimming(true);
				scheduler.setUsePNLBasedWindowTrimming(true);

				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(initialSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = initialSequences.getResources().get(0);
				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(0);

				Assertions.assertEquals(1, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getExclusiveEnd());
				Assertions.assertEquals(0, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getInclusiveStart());

				// Expect to return as soon as possible, (i.e. 25th)
				Assertions.assertEquals(24 * 24, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getReturnSlot()).getInclusiveStart());
				Assertions.assertEquals(24 * 24 + 1, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getReturnSlot()).getExclusiveEnd());
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void minDurationPriceBasedTrimmingEmptyVesselTest() {

		final VesselAvailability vesselAvailability = getDefaultVesselAvailabilityWithTW(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0), LocalDateTime.of(2017, Month.JUNE, 20, 0, 0, 0));

		// Set the end requirement's time window and max duration
		vesselAvailability.setMinDuration(4);
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
				scheduler.setUsePriceBasedWindowTrimming(true);

				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(initialSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = initialSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(0);

				// Assert expected result (Truncated start window)
				// Start as early as possible
				Assertions.assertEquals(24 * 19 + 1, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getExclusiveEnd());
				Assertions.assertEquals(24 * 19, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getInclusiveStart());

				Assertions.assertEquals(24 * 24 + 1, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getReturnSlot()).getExclusiveEnd());
				Assertions.assertEquals(24 * 24, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getReturnSlot()).getInclusiveStart());
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void minMaxDurationPriceBasedTrimmingEmptyVesselTest() {

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
				scheduler.setUsePriceBasedWindowTrimming(true);
				scheduler.setUsePNLBasedWindowTrimming(true);

				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(initialSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = initialSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(0);

				// Start as late as possible
				Assertions.assertEquals(24 * 19, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getInclusiveStart());
				Assertions.assertEquals(24 * 19 + 1, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getExclusiveEnd());

				// Assert Expect to return on 25th (early as possible - still > min duration)
				Assertions.assertEquals(24 * 24 + 1, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getReturnSlot()).getExclusiveEnd());
				Assertions.assertEquals(24 * 24, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getReturnSlot()).getInclusiveStart());

			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void minMaxDurationPriceBasedTrimmingCargoTest() {

		final VesselAvailability vesselAvailability = getDefaultVesselAvailabilityWithTW(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0), LocalDateTime.of(2017, Month.JUNE, 20, 0, 0, 0));

		// Set the end requirement's time window and max duration
		vesselAvailability.setMaxDuration(20);
		vesselAvailability.setMinDuration(3);
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JULY, 20, 0, 0, 0));
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JUNE, 25, 0, 0, 0));

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_MANZANILLO);

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
				scheduler.setUsePNLBasedWindowTrimming(true);

				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(initialSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = initialSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(0);
				// Pick up vessel as late as possible ( ie. the 20th)
				Assertions.assertEquals(24 * 19 + 1, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getExclusiveEnd());
				Assertions.assertEquals(24 * 19, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getInclusiveStart());

				final IPortTimeWindowsRecord ptr_r1_cargo = records.get(r0).get(1);
				// Assert expected result (Truncated start AND end windows)
				int startPlus20Days = 24 * 19 + 24 * 20;
				Assertions.assertTrue(ptr_r1_cargo.getSlotFeasibleTimeWindow(ptr_r1_cargo.getReturnSlot()).getInclusiveStart() <= startPlus20Days);
				// Assertions.assertEquals(24 * 37 + 6, ptr_r1_cargo.getSlotFeasibleTimeWindow(ptr_r1_cargo.getReturnSlot()).getExclusiveEnd());
				// Assertions.assertEquals(24 * 37 + 5, ptr_r1_cargo.getSlotFeasibleTimeWindow(ptr_r1_cargo.getReturnSlot()).getInclusiveStart());

			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void maxDurationOptimiseOutOfRangePriceBasedCargoTest() throws Exception {

		// Create first vessel
		// Set the end requirement's time window and max duration
		final VesselAvailability vesselAvailability = getDefaultVesselAvailabilityWithTW(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0), LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));
		vesselAvailability.setMaxDuration(55);
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JUNE, 30, 0, 0, 0));
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JUNE, 25, 0, 0, 0));

		// Create second vessel
		// Set the end requirement's time window and max duration
		final VesselAvailability vesselAvailability2 = getVesselAvailabilityWithTW("STEAM-145.8", LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0), LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));
		vesselAvailability2.setEndBy(LocalDateTime.of(2017, Month.JULY, 30, 0, 0, 0));
		vesselAvailability2.setEndAfter(LocalDateTime.of(2017, Month.JULY, 25, 0, 0, 0));

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_MANZANILLO);

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
			final IScenarioDataProvider optimiserDataProvider = scenarioToOptimiserBridge.getOptimiserScenario();

			// Check if the cargo is scheduled
			Assertions.assertEquals(1, ScenarioModelUtil.getCargoModel(optimiserDataProvider).getCargoes().size());

			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assertions.assertNotNull(schedule);

			// Check the identity of the vessel
			Assertions.assertEquals(vesselAvailability, schedule.getSequences().get(0).getVesselAvailability());
			Assertions.assertEquals(vesselAvailability2, schedule.getSequences().get(1).getVesselAvailability());

			// Check On which vessel the cargo is
			Assertions.assertTrue(schedule.getSequences().get(1).getEvents().size() > 3);
			Assertions.assertEquals(3, schedule.getSequences().get(0).getEvents().size());
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void minDurationOptimiseInPriceBasedCargoTest() throws Exception {

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
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_MANZANILLO);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, Month.JUNE, 27), port1, null, entity, "7") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, Month.JUNE, 29), port2, null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability2, 1) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			final IScenarioDataProvider optimiserDataProvider = scenarioToOptimiserBridge.getOptimiserScenario();

			// Check if the cargo is scheduled
			Assertions.assertEquals(1, ScenarioModelUtil.getCargoModel(optimiserDataProvider).getCargoes().size());

			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assertions.assertNotNull(schedule);

			// Check if the cargo moved from ship 1 to ship 2
			Assertions.assertEquals(vesselAvailability, schedule.getSequences().get(0).getVesselAvailability());
			Assertions.assertEquals(vesselAvailability2, schedule.getSequences().get(1).getVesselAvailability());

			Assertions.assertTrue(schedule.getSequences().get(0).getEvents().size() > 3);
			Assertions.assertEquals(3, schedule.getSequences().get(1).getEvents().size());
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void maxDurationPriceBasedTrimmingCargoVesselTest() {

		final VesselAvailability vesselAvailability = getDefaultVesselAvailabilityWithTW(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0), LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));

		// Set the end requirement's time window and max duration
		vesselAvailability.setMaxDuration(26);
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JUNE, 30, 0, 0, 0));
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JUNE, 25, 0, 0, 0));

		// Construct the cargo
		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_MANZANILLO);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, Month.JUNE, 15), port1, null, entity, "7") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, Month.JUNE, 20), port2, null, entity, "7") //
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

				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(initialSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = initialSequences.getResources().get(0);

				final IPortTimeWindowsRecord ptr_r0_cargo = records.get(r0).get(0);
				Assertions.assertEquals(1, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getExclusiveEnd());
				Assertions.assertEquals(0, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getInclusiveStart());

				// Assert expected result within max duration
				final IPortTimeWindowsRecord ptr_r1_cargo = records.get(r0).get(1);
				Assertions.assertTrue(ptr_r1_cargo.getSlotFeasibleTimeWindow(ptr_r1_cargo.getReturnSlot()).getInclusiveStart() < 24 * 26 - 1);

				// Assertions.assertEquals(24 * 25 + 6, ptr_r1_cargo.getSlotFeasibleTimeWindow(ptr_r1_cargo.getReturnSlot()).getExclusiveEnd());
				// Assertions.assertEquals(24 * 25 + 5, ptr_r1_cargo.getSlotFeasibleTimeWindow(ptr_r1_cargo.getReturnSlot()).getInclusiveStart());

			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void minDurationPriceBasedTrimmingCargoOptionalVesselTest() {

		final VesselAvailability vesselAvailability = getDefaultVesselAvailabilityWithTW(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0), LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));

		// Set the end requirement's time window and max duration
		vesselAvailability.setOptional(true);
		vesselAvailability.setMinDuration(100);
		vesselAvailability.setTimeCharterRate("10");
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.SEPTEMBER, 1, 0, 0, 0));

		final VesselAvailability vesselAvailability2 = getVesselAvailabilityWithoutTW("STEAM-145.8");
		// vesselAvailability.setOptional(true);
		vesselAvailability2.setStartAfter(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));
		vesselAvailability2.setStartBy(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));
		vesselAvailability2.setTimeCharterRate("50000");
		// vesselAvailability2.setEndBy(LocalDateTime.of(2017, Month.JULY, 30, 0, 0, 0));
		// vesselAvailability2.setEndAfter(LocalDateTime.of(2017, Month.JULY, 25, 0, 0, 0));

		// Construct the cargo
		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_MANZANILLO);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, Month.JUNE, 15), port1, null, entity, "7") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, Month.JUNE, 27), port2, null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability2, 1) //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			final IScenarioDataProvider optimiserDataProvider = scenarioToOptimiserBridge.getOptimiserScenario();

			// Check if the cargo is scheduled
			Assertions.assertEquals(1, ScenarioModelUtil.getCargoModel(optimiserDataProvider).getCargoes().size());

			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assertions.assertNotNull(schedule);

			// Check if the cargo moved from ship 1 to ship 2
			// assertEquals(vesselAvailability, schedule.getSequences().get(1).getVesselAvailability());
			Assertions.assertEquals(vesselAvailability2, schedule.getSequences().get(0).getVesselAvailability());

			Assertions.assertTrue(schedule.getSequences().get(0).getEvents().size() > 3);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void maxDurationPriceBasedWithLateness() throws Exception {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final VesselAvailability vesselAvailability = getDefaultVesselAvailability();
		vesselAvailability.setStartBy(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));
		vesselAvailability.setMaxDuration(35);
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JULY, 1, 0, 0, 0));
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JULY, 10, 0, 0, 0));

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);

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
				scheduler.setUsePriceBasedWindowTrimming(true);

				final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(initialSequences);
			}
			final EndEvent endEvent = MicroTestUtils.findVesselEndEvent(lngScenarioModel);
			final PortVisitLateness lateness = endEvent.getLateness();
			Assertions.assertNotNull(lateness);
			Assertions.assertEquals(48, lateness.getLatenessInHours());
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
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

		Assertions.assertTrue(start.getStart().toLocalDateTime().isEqual(LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0)));
		Assertions.assertTrue(end.getEnd().toLocalDateTime().isEqual(LocalDateTime.of(2017, Month.JUNE, 25, 0, 0, 0)));
	}
}
