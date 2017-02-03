/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.TimePeriod;

/**
 * Note various +1 hours in tests for exclusive timewindows. I believe they are all correct.
 * 
 * 
 * Rules Notes:
 * 
 * If open vessel ends within period, end by window should be date period end. End after should be horizon date (otherwise current date..) -- what if we take off a cargo....
 * 
 * @author sg
 *
 */
public class ScheduleHorizonTests extends AbstractMicroTestCase {

	/**
	 * "Simple" horizon - one vessel, one cargo, horizon in the future, vessel should end at that date.
	 */
	@Test
	public void testSimpleHorizon() {

		@NonNull
		LocalDate horizon = LocalDate.of(2016, 3, 1);
		scenarioModelBuilder.setScheduleHorizon(horizon);

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);

			Assert.assertNotNull(cargoAllocation);

			final EList<Event> events = cargoAllocation.getSequence().getEvents();
			final Event lastEvent = events.get(events.size() - 1);

			Assert.assertEquals(horizon, lastEvent.getEnd().plusHours(1).withZoneSameInstant(ZoneId.of("UTC")).toLocalDate());

		});
	}

	/**
	 * Optional unused time charter, should not be evaluated
	 */
	@Test
	public void testUnusedOptionalTC() {

		@NonNull
		LocalDate horizon = LocalDate.of(2016, 3, 1);
		scenarioModelBuilder.setScheduleHorizon(horizon);

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withOptionality(true) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			Assert.assertEquals(0, schedule.getSequences().size());
		});
	}

	/**
	 * Cargo and vessel lower bound are before schedule horizon date, should have no impact
	 */
	@Test
	public void testVesselWithEndAfterDate() {

		@NonNull
		LocalDate horizon = LocalDate.of(2016, 3, 1);
		scenarioModelBuilder.setScheduleHorizon(horizon);

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0), null) //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);

			Assert.assertNotNull(cargoAllocation);

			final EList<Event> events = cargoAllocation.getSequence().getEvents();
			final Event lastEvent = events.get(events.size() - 1);

			// End near early window bound
			// As we can finish any time, there is no need to keep going until schedule end date
			Assert.assertTrue(lastEvent.getEnd().plusHours(1).withZoneSameInstant(ZoneId.of("UTC")).toLocalDate().isBefore(horizon));

			// TODO: Not sure if we need +1 here...
			Assert.assertFalse(LocalDateTime.of(2016, 1, 1, 0, 0, 0).isAfter(lastEvent.getEnd().plusHours(1).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()));
		});
	}

	/**
	 * Horizon is before vessel dates, so adjust end
	 */
	@Test
	public void testVesselWithDatesAfterHorizon() {

		@NonNull
		LocalDate horizon = LocalDate.of(2016, 3, 1);
		scenarioModelBuilder.setScheduleHorizon(horizon);

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withEndWindow(LocalDateTime.of(2016, 4, 1, 0, 0, 0), null) //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);

			Assert.assertNotNull(cargoAllocation);

			final EList<Event> events = cargoAllocation.getSequence().getEvents();
			final Event lastEvent = events.get(events.size() - 1);

			// End near early window bound
			// As we can finish any time, there is no need to keep going until schedule end date
			// Assert.assertTrue(lastEvent.getEnd().plusHours(1).withZoneSameInstant(ZoneId.of("UTC")).toLocalDate().isBefore(horizon));

			// TODO: Not sure if we need +1 here...
			Assert.assertEquals(LocalDateTime.of(2016, 3, 1, 0, 0, 0), lastEvent.getEnd().withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
		});
	}

	/**
	 * Even though the vessel availability is after horzion, no lower bound mean we can finish well before
	 */
	@Test
	public void testVesselWithEndByDate() {

		@NonNull
		LocalDate horizon = LocalDate.of(2016, 3, 1);
		scenarioModelBuilder.setScheduleHorizon(horizon);

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withEndWindow(null, LocalDateTime.of(2016, 4, 1, 0, 0, 0)) //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);

			Assert.assertNotNull(cargoAllocation);

			final EList<Event> events = cargoAllocation.getSequence().getEvents();
			final Event lastEvent = events.get(events.size() - 1);

			// As we can finish any time, there is no need to keep going until schedule end date
			Assert.assertTrue(lastEvent.getEnd().plusHours(1).withZoneSameInstant(ZoneId.of("UTC")).toLocalDate().isBefore(horizon));

		});
	}

	/**
	 * Period optimisation - the schedule horizon is within the period and the cargoes finish before the horizon. Expect the vessel to be set to finish at the schedule horizon date and flip to hire
	 * cost only rules as the horizon is explicitly removed in the period scenario.
	 * 
	 * The horizon is within the period - so define the lower bound to be scheduling horizon. To retain the flexibility of the soft limit, allow use up to the end of the optimisation period.
	 */
	@Test
	public void test_Period_WithoutVesselDates_HorizonInPeriod() {

		@NonNull
		LocalDate horizon = LocalDate.of(2016, 5, 1);
		scenarioModelBuilder.setScheduleHorizon(horizon);

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2016, 1, 5), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D2", LocalDate.of(2016, 2, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability, 2) //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(false, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStart(YearMonth.of(2015, 10));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2016, 7));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			Assert.assertEquals(2, optimiserScenario.getCargoModel().getCargoes().size());

			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(optimiserScenario);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);

			Assert.assertNotNull(cargoAllocation);

			final EList<Event> events = cargoAllocation.getSequence().getEvents();
			final Event lastEvent = events.get(events.size() - 1);

			Assert.assertEquals(horizon, lastEvent.getEnd().plusHours(1).withZoneSameInstant(ZoneId.of("UTC")).toLocalDate());

			// No end date flex, fully costed
			VesselAvailability period_vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			Assert.assertTrue(period_vesselAvailability.isForceHireCostOnlyEndRule());
			Assert.assertEquals(horizon, period_vesselAvailability.getEndAfter().plusHours(1).toLocalDate());
			Assert.assertEquals(LocalDate.of(2016, 7, 1), period_vesselAvailability.getEndBy().plusHours(1).toLocalDate());

		}, null);
	}

	/**
	 * The schedule horizon is outside of the period. We expect the end cargo to be trimmed and vessel state to match the expected load date of this cargo. Schedule horizon cannot influence result.
	 * This case is with no existing end dates set on the vessel availability
	 */
	@Test
	public void test_Period_WithNoVesselDates_PeriodTrimmed() {
		@NonNull
		LocalDate horizon = LocalDate.of(2016, 5, 1);
		scenarioModelBuilder.setScheduleHorizon(horizon);

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		// Get rid of timezone to make testing easier
		@NonNull
		Port loadPort = portFinder.findPort("Point Fortin");
		@NonNull
		Port dischargePort = portFinder.findPort("Dominion Cove Point LNG");

		loadPort.setTimeZone("UTC");
		loadPort.setDefaultStartTime(0);
		dischargePort.setTimeZone("UTC");
		dischargePort.setDefaultStartTime(0);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), loadPort, null, entity, "5") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), dischargePort, null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2016, 1, 5), loadPort, null, entity, "5") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2016, 2, 1), dischargePort, null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.withVesselAssignment(vesselAvailability, 2) //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(false, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStart(YearMonth.of(2015, 10));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2015, 12));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(optimiserScenario);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo1.getLoadName(), schedule);

			Assert.assertNotNull(cargoAllocation);

			Sequence sequence = cargoAllocation.getSequence();
			final List<Event> events = sequence.getEvents();
			final Event lastEvent = events.get(events.size() - 1);

			// Expect to end in time for cargo 2 loading which has been stripped out
			Assert.assertEquals(LocalDate.of(2016, 1, 5), lastEvent.getEnd().plusHours(1).withZoneSameInstant(ZoneId.of("UTC")).toLocalDate());

			// No end date flex, fully costed
			VesselAvailability period_vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			Assert.assertFalse(period_vesselAvailability.isForceHireCostOnlyEndRule());
			Assert.assertEquals(LocalDateTime.of(2016, 1, 5, 0, 0, 0), period_vesselAvailability.getEndAfter());
			Assert.assertEquals(LocalDateTime.of(2016, 1, 5, 0, 0, 0), period_vesselAvailability.getEndBy());

			Assert.assertNull(optimiserScenario.getSchedulingEndDate());
		}, null);
	}

	/**
	 * The schedule horizon is outside of the period. We expect the end cargo to be trimmed and vessel state to match the expected load date of this cargo. Schedule horizon cannot influence result.
	 * This case is with existing end dates set on the vessel availability
	 */
	@Test
	public void test_Period_WithVesselDates_PeriodTrimmed() {
		@NonNull
		LocalDate horizon = LocalDate.of(2016, 5, 1);
		scenarioModelBuilder.setScheduleHorizon(horizon);

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withEndWindow(LocalDateTime.of(2016, 4, 16, 0, 0, 0), LocalDateTime.of(2016, 5, 16, 0, 0, 0)) //
				.build();

		// Get rid of timezone to make testing easier
		@NonNull
		Port loadPort = portFinder.findPort("Point Fortin");
		@NonNull
		Port dischargePort = portFinder.findPort("Dominion Cove Point LNG");

		loadPort.setTimeZone("UTC");
		loadPort.setDefaultStartTime(0);
		dischargePort.setTimeZone("UTC");
		dischargePort.setDefaultStartTime(0);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), loadPort, null, entity, "5") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), dischargePort, null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2016, 1, 5), loadPort, null, entity, "5") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2016, 2, 1), dischargePort, null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.withVesselAssignment(vesselAvailability, 2) //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(false, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStart(YearMonth.of(2015, 10));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2015, 12));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(optimiserScenario);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo1.getLoadName(), schedule);

			Assert.assertNotNull(cargoAllocation);

			Sequence sequence = cargoAllocation.getSequence();
			final List<Event> events = sequence.getEvents();
			final Event lastEvent = events.get(events.size() - 1);

			// Expect to end in time for cargo 2 loading which has been stripped out
			Assert.assertEquals(LocalDate.of(2016, 1, 5), lastEvent.getEnd().plusHours(1).withZoneSameInstant(ZoneId.of("UTC")).toLocalDate());

			// No end date flex, fully costed
			VesselAvailability period_vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			Assert.assertFalse(period_vesselAvailability.isForceHireCostOnlyEndRule());
			Assert.assertEquals(LocalDateTime.of(2016, 1, 5, 0, 0, 0), period_vesselAvailability.getEndAfter());
			Assert.assertEquals(LocalDateTime.of(2016, 1, 5, 0, 0, 0), period_vesselAvailability.getEndBy());

			Assert.assertNull(optimiserScenario.getSchedulingEndDate());
		}, null);
	}

	/**
	 * With a single cargo, the schedule horizon would be included in the period scenario as we have no trailing cargo to trim. However as the end after is before the horizon, the existing end window
	 * is retained.
	 */
	@Test
	public void test_Period_WithVesselDates_Retain_vessel_dates() {
		@NonNull
		LocalDate horizon = LocalDate.of(2016, 5, 1);
		scenarioModelBuilder.setScheduleHorizon(horizon);

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withEndWindow(LocalDateTime.of(2016, 4, 16, 0, 0, 0), LocalDateTime.of(2016, 5, 16, 0, 0, 0)) //
				.build();

		// Get rid of timezone to make testing easier
		@NonNull
		Port loadPort = portFinder.findPort("Point Fortin");
		@NonNull
		Port dischargePort = portFinder.findPort("Dominion Cove Point LNG");

		loadPort.setTimeZone("UTC");
		loadPort.setDefaultStartTime(0);
		dischargePort.setTimeZone("UTC");
		dischargePort.setDefaultStartTime(0);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), loadPort, null, entity, "5") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), dischargePort, null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();
		//
		// final Cargo cargo2 = cargoModelBuilder.makeCargo() //
		// .makeFOBPurchase("L2", LocalDate.of(2016, 1, 5), loadPort, null, entity, "5") //
		// .withWindowSize(0, TimePeriod.HOURS) //
		// .build() //
		// .makeDESSale("D2", LocalDate.of(2016, 2, 1), dischargePort, null, entity, "7") //
		// .withWindowSize(0, TimePeriod.HOURS) //
		// .build() //
		// .withVesselAssignment(vesselAvailability, 2) //
		// .withAssignmentFlags(false, false) //
		// .build();

		evaluateWithLSOTest(false, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStart(YearMonth.of(2015, 10));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2015, 12));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(optimiserScenario);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo1.getLoadName(), schedule);

			Assert.assertNotNull(cargoAllocation);

			Sequence sequence = cargoAllocation.getSequence();
			final List<Event> events = sequence.getEvents();
			final Event lastEvent = events.get(events.size() - 1);

			// Expect to end in time for cargo 2 loading which has been stripped out
			Assert.assertEquals(LocalDate.of(2016, 4, 16), lastEvent.getEnd().plusHours(1).withZoneSameInstant(ZoneId.of("UTC")).toLocalDate());

			// No end date flex, fully costed
			VesselAvailability period_vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			Assert.assertFalse(period_vesselAvailability.isForceHireCostOnlyEndRule());
			Assert.assertEquals(LocalDateTime.of(2016, 4, 16, 0, 0, 0), period_vesselAvailability.getEndAfter());
			Assert.assertEquals(LocalDateTime.of(2016, 5, 16, 0, 0, 0), period_vesselAvailability.getEndBy());

			Assert.assertNull(optimiserScenario.getSchedulingEndDate());
		}, null);
	}

	/**
	 * The horizon is within the period - so define the lower bound to be scheduling horizon. To retain the flexibility of the soft limit, allow use up to the end of the optimisation period.
	 */
	@Test
	public void test_Period_WithVesselDates_HorizonInPeriod() {
		@NonNull
		LocalDate horizon = LocalDate.of(2016, 1, 1);
		scenarioModelBuilder.setScheduleHorizon(horizon);

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withEndWindow(LocalDateTime.of(2016, 4, 16, 0, 0, 0), LocalDateTime.of(2016, 5, 16, 0, 0, 0)) //
				.build();

		// Get rid of timezone to make testing easier
		@NonNull
		Port loadPort = portFinder.findPort("Point Fortin");
		@NonNull
		Port dischargePort = portFinder.findPort("Dominion Cove Point LNG");

		loadPort.setTimeZone("UTC");
		loadPort.setDefaultStartTime(0);
		dischargePort.setTimeZone("UTC");
		dischargePort.setDefaultStartTime(0);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), loadPort, null, entity, "5") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), dischargePort, null, entity, "7") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();
		//
		// final Cargo cargo2 = cargoModelBuilder.makeCargo() //
		// .makeFOBPurchase("L2", LocalDate.of(2016, 1, 5), loadPort, null, entity, "5") //
		// .withWindowSize(0, TimePeriod.HOURS) //
		// .build() //
		// .makeDESSale("D2", LocalDate.of(2016, 2, 1), dischargePort, null, entity, "7") //
		// .withWindowSize(0, TimePeriod.HOURS) //
		// .build() //
		// .withVesselAssignment(vesselAvailability, 2) //
		// .withAssignmentFlags(false, false) //
		// .build();

		evaluateWithLSOTest(false, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStart(YearMonth.of(2015, 10));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2016, 2));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(optimiserScenario);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo1.getLoadName(), schedule);

			Assert.assertNotNull(cargoAllocation);

			Sequence sequence = cargoAllocation.getSequence();
			final List<Event> events = sequence.getEvents();
			final Event lastEvent = events.get(events.size() - 1);

			// Expect to end in time for cargo 2 loading which has been stripped out
			// Assert.assertEquals(LocalDate.of(2016, 4, 16), lastEvent.getEnd().plusHours(1).withZoneSameInstant(ZoneId.of("UTC")).toLocalDate());

			// No end date flex, fully costed
			VesselAvailability period_vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			Assert.assertTrue(period_vesselAvailability.isForceHireCostOnlyEndRule());
			Assert.assertEquals(LocalDateTime.of(2016, 1, 1, 0, 0, 0), period_vesselAvailability.getEndAfter());
			Assert.assertEquals(LocalDateTime.of(2016, 5, 16, 0, 0, 0), period_vesselAvailability.getEndBy());

			Assert.assertNull(optimiserScenario.getSchedulingEndDate());
		}, null);
	}

	/**
	 * Real test case. V 4.1.3 - vessel with end date in 2021, horizon end 1 month after opt period end. Period transformer did not create correctly set end date or set the force end rules.
	 */
	@Test
	public void test_Period_FB_2372() {
		@NonNull
		LocalDate horizon = LocalDate.of(2017, 6, 1);
		scenarioModelBuilder.setScheduleHorizon(horizon);

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("80000") //
				.withStartWindow(LocalDateTime.of(2016, 12, 1, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2021, 9, 1, 0, 0, 0)) //
				.build();

		// Get rid of timezone to make testing easier
		@NonNull
		Port loadPort = portFinder.findPort("Point Fortin");
		@NonNull
		Port dischargePort = portFinder.findPort("Dominion Cove Point LNG");

		loadPort.setTimeZone("UTC");
		loadPort.setDefaultStartTime(0);
		dischargePort.setTimeZone("UTC");
		dischargePort.setDefaultStartTime(0);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2017, 2, 9), loadPort, null, entity, "5") //
				.withWindowSize(1, TimePeriod.DAYS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2017, 2, 27), dischargePort, null, entity, "7") //
				.withWindowSize(1, TimePeriod.DAYS) //
				.build() //
				.withVesselAssignment(vesselAvailability, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(false, optimisationPlan -> {
			optimisationPlan.getUserSettings().setPeriodStart(YearMonth.of(2017, 2));
			optimisationPlan.getUserSettings().setPeriodEnd(YearMonth.of(2017, 5));
		}, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());

			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			@Nullable
			final Schedule schedule = ScenarioModelUtil.findSchedule(optimiserScenario);
			Assert.assertNotNull(schedule);

			@Nullable
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargo1.getLoadName(), schedule);

			Assert.assertNotNull(cargoAllocation);

			Sequence sequence = cargoAllocation.getSequence();
			final List<Event> events = sequence.getEvents();
			final Event lastEvent = events.get(events.size() - 1);

			// Expect to end in time for cargo 2 loading which has been stripped out
			// Assert.assertEquals(LocalDate.of(2016, 4, 16), lastEvent.getEnd().plusHours(1).withZoneSameInstant(ZoneId.of("UTC")).toLocalDate());

			// No end date flex, fully costed
			VesselAvailability period_vesselAvailability = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0);
			Assert.assertTrue(period_vesselAvailability.isForceHireCostOnlyEndRule());
			Assert.assertEquals(LocalDateTime.of(2017, 6, 1, 0, 0, 0), period_vesselAvailability.getEndAfter());
			Assert.assertEquals(LocalDateTime.of(2021, 9, 1, 0, 0, 0), period_vesselAvailability.getEndBy());

			Assert.assertNull(optimiserScenario.getSchedulingEndDate());
		}, null);
	}
}
