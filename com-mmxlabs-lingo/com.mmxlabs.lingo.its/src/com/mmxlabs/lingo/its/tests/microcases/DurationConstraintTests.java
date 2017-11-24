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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.mmxlabs.common.time.Days;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.tests.category.RegressionTest;
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
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IEndEventScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService.ModuleType;
import com.mmxlabs.scheduler.optimiser.peaberry.OptimiserInjectorServiceMaker;
import com.mmxlabs.scheduler.optimiser.scheduling.EarliestSlotTimeScheduler;
import com.mmxlabs.scheduler.optimiser.scheduling.ISlotTimeScheduler;
import com.mmxlabs.scheduler.optimiser.scheduling.ScheduledTimeWindows;
import com.mmxlabs.scheduler.optimiser.scheduling.TimeWindowScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

@RunWith(value = ShiroRunner.class)
public class DurationConstraintTests extends AbstractMicroTestCase {

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
		vesselAvailability.setMaxDuration(30);
		vesselAvailability.setMinDuration(3);
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JULY, 20, 0, 0, 0));
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JUNE, 10, 0, 0, 0));

		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");

		@NonNull
		final Port port2 = portFinder.findPort("Colon");

		// Construct the cargoes
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, Month.JUNE, 15), port1, null, entity, "7") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, Month.JUNE, 30), port2, null, entity, "7") //
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
				assertEquals(24 * (vesselAvailability.getMaxDuration()) + 1, ptr_r1_cargo.getSlotFeasibleTimeWindow(ptr_r1_cargo.getReturnSlot()).getExclusiveEnd());
				assertEquals(24 * (vesselAvailability.getMaxDuration()), ptr_r1_cargo.getSlotFeasibleTimeWindow(ptr_r1_cargo.getReturnSlot()).getInclusiveStart());

				assertEquals(1, ptr_r0_cargo.getSlotFeasibleTimeWindow(ptr_r0_cargo.getFirstSlot()).getExclusiveEnd());
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

		if (minDeltaInHours < (vesselAvailability.getMinDuration() * 24)) {
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
		vesselAvailability.setMaxDuration(33);
		vesselAvailability.setEndBy(LocalDateTime.of(2017, Month.JULY, 30, 0, 0, 0));
		vesselAvailability.setEndAfter(LocalDateTime.of(2017, Month.JULY, 25, 0, 0, 0));

		// Construct the cargo
		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");

		@NonNull
		final Port port2 = portFinder.findPort("Colon");

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
		Event end = schedule.getSequences().get(0).getEvents().get(schedule.getSequences().get(0).getEvents().size() - 1);
		final long startTimestamp = start.getStart().toEpochSecond();
		final long endTimestamp = end.getStart().toEpochSecond();

		final long minDeltaInSeconds = endTimestamp - startTimestamp;
		final long minDeltaInHours = (minDeltaInSeconds / 3600);

		if (minDeltaInHours > (vesselAvailability.getMaxDuration() * 24)) {
			assertTrue(false);
		}
	}

	@Test
	@Category({ RegressionTest.class })
	public void maxDurationTrimCausedAssertion() {

		// The following case caused an assertion error as the feasible end window was badly trimmed so that the previous element window end was later than vessel end.

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setMaxSpeed(16.0);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 9, 11, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 10, 26, 23, 0, 0), LocalDateTime.of(2018, 1, 27, 23, 0, 0)) //
				.build();

		vesselAvailability.setMinDuration(46);
		vesselAvailability.setMaxDuration(139);
		Port port_SP = portFinder.findPort("Sabine Pass");
		Port port_HJ = portFinder.findPort("Himeji");

		vesselAvailability.getEndAt().add(port_HJ);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, Month.DECEMBER, 12), port_SP, null, entity, "7") //
				.withWindowSize(3, TimePeriod.DAYS)//
				.withWindowStartTime(0) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2018, Month.JANUARY, 1), port_HJ, null, entity, "7") //
				.withWindowSize(1, TimePeriod.MONTHS)//
				.withWindowStartTime(0) //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2017, Month.DECEMBER, 18), port_SP, null, entity, "7") //
				.withWindowSize(8, TimePeriod.DAYS)//
				.withWindowStartTime(7) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2017, Month.DECEMBER, 1), port_HJ, null, entity, "7") //
				.withWindowSize(1, TimePeriod.MONTHS)//
				.withWindowStartTime(0) //
				.build() //
				.withVesselAssignment(vesselAvailability, 2) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();
				final ISequences initialSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();

				// Set time scheduler settings
				final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
				scheduler.setUseCanalBasedWindowTrimming(true);
				scheduler.setUsePriceBasedWindowTrimming(false);

				final ScheduledTimeWindows schedule = scheduler.schedule(initialSequences);
				final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

				final IResource r0 = initialSequences.getResources().get(0);

				int[] windowStart = new int[6];
				int[] windowEnd = new int[6];

				final AtomicInteger idx = new AtomicInteger(0);
				Consumer<IPortTimeWindowsRecord> func = (ptr) -> {
					for (IPortSlot slot : ptr.getSlots()) {
						ITimeWindow tw = ptr.getSlotFeasibleTimeWindow(slot);
						windowStart[idx.get()] = tw.getInclusiveStart();
						windowEnd[idx.get()] = tw.getExclusiveEnd();
						idx.getAndIncrement();
					}
				};

				func.accept(records.get(r0).get(0));
				func.accept(records.get(r0).get(1));
				IPortTimeWindowsRecord ptr = records.get(r0).get(2);
				func.accept(ptr);
				{
					ITimeWindow tw = ptr.getSlotFeasibleTimeWindow(ptr.getReturnSlot());
					windowStart[idx.get()] = tw.getInclusiveStart();
					windowEnd[idx.get()] = tw.getExclusiveEnd();
					idx.getAndIncrement();
				}
				// Originally the windowEnd[5] > windowEnd[6] -- but was fixed by the time it was put into a IPortTimeWindowsRecord
				for (int i = 0; i < idx.get(); ++i) {
					Assert.assertTrue(windowStart[i] < windowEnd[i]);
					if (i > 0) {
						Assert.assertTrue(windowStart[i - 1] <= windowStart[i]);
						Assert.assertTrue(windowEnd[i - 1] <= windowEnd[i]);
					}
				}
			}
		});
	}

	/***
	 * We did see a case where the hire cost only end rule kicked in and ignored the max duration and canal booking requirements.
	 * 
	 * @throws Exception
	 */
	@Test
	@Category({ MicroTest.class })
	public void maxDurationWithOpenEndAndHireCostEndRulesTest() throws Exception {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		@NonNull
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(lngScenarioModel);
		cargoModel.getCanalBookings().getCanalBookingSlots().clear();

		cargoModel.getCanalBookings().setStrictBoundaryOffsetDays(10);
		cargoModel.getCanalBookings().setRelaxedBoundaryOffsetDays(100);

		cargoModel.getCanalBookings().setFlexibleBookingAmountSouthbound(0);
		cargoModel.getCanalBookings().setNorthboundMaxIdleDays(5);
		cargoModel.getCanalBookings().setArrivalMarginHours(12);

		final Vessel vessel1 = fleetModelFinder.findVessel("STEAM-145");

		final VesselAvailability vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withStartWindow(LocalDateTime.of(2017, 12, 15, 0, 0)) //
				.withMinDuration(60) //
				.withMaxDuration(100) //
				.withCharterRate("68000") //
				.withEndPorts(portFinder.getCapabilityPortsGroup(PortCapability.DISCHARGE)) //
				.build();

		// Construct the cargo
		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Himeji");

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2018, Month.JANUARY, 13), port1, null, entity, "7") //
				.withWindowSize(1, TimePeriod.DAYS) //

				.withVisitDuration(36) //

				.build() //
				.makeDESSale("D1", LocalDate.of(2018, Month.FEBRUARY, 1), port2, null, entity, "7") //
				.withWindowSize(1, TimePeriod.MONTHS) //
				.withVisitDuration(36) //

				.build() //
				.withVesselAssignment(vesselAvailability1, 1) //
				.build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2017, 11, 10), LocalDate.of(2018, 2, 8));

		IOptimiserInjectorService localOverrides = OptimiserInjectorServiceMaker.begin() //
				.makeModule() //
				.with(binder -> binder.bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_UsePriceBasedWindowTrimming)).toInstance(Boolean.TRUE)) //
				.with(binder -> binder.bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_UseCanalSlotBasedWindowTrimming)).toInstance(Boolean.TRUE)) //
				.with(binder -> binder.bind(ISlotTimeScheduler.class).to(EarliestSlotTimeScheduler.class)) //
				.with(binder -> binder.bind(boolean.class).annotatedWith(Names.named(VoyagePlanOptimiser.VPO_SPEED_STEPPING)).toInstance(Boolean.FALSE)) //
				.with(binder -> binder.bind(boolean.class).annotatedWith(Names.named(IEndEventScheduler.ENABLE_HIRE_COST_ONLY_END_RULE)).toInstance(Boolean.TRUE))//
				.with(binder -> binder.bind(boolean.class).annotatedWith(Names.named(VoyagePlanOptimiser.VPO_SPEED_STEPPING)).toInstance(Boolean.FALSE)) //
				.buildOverride(ModuleType.Module_LNGTransformerModule)//
				.make();

		evaluateWithOverrides(localOverrides, null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();

			boolean foundVessel1 = false;
			for (Sequence sequence : schedule.getSequences()) {
				if (sequence.getVesselAvailability() == vesselAvailability1) {
					foundVessel1 = true;
				} else {
					continue;
				}

				final Event start = sequence.getEvents().get(0);
				final Event end = sequence.getEvents().get(sequence.getEvents().size() - 1);

				Assert.assertTrue(Days.between(start.getStart(), end.getEnd()) >= 60);
				Assert.assertTrue(Days.between(start.getStart(), end.getEnd()) <= 100);

				// Check Panama is not used
				for (Event evt : sequence.getEvents()) {
					if (evt instanceof Journey) {
						Journey journey = (Journey) evt;
						Assert.assertTrue(journey.getRouteOption() != RouteOption.PANAMA);
					}
				}
			}
			Assert.assertTrue(foundVessel1);
		});
	}

}
