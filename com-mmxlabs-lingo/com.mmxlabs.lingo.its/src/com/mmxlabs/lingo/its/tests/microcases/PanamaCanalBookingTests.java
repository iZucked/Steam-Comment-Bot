/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.Iterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters;
import com.mmxlabs.models.lng.commercial.EVesselTankState;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService.ModuleType;
import com.mmxlabs.scheduler.optimiser.peaberry.OptimiserInjectorServiceMaker;

/**
 * Single cargo case testing for Panama logic. As Panama logic typically extends
 * the available time between event without explicitly recording the change we
 * have to infer the delay. These tests are constructed so that the "basic" case
 * has a laden leg configured so that it travels at max speed without lateness.
 * Subsequent tests will increase voyage duration and will be recorded as
 * lateness - thus lateness is the extra panama time added. E.g. if we need 5
 * days idling on top of the basic voyage, then we should see 5 days lateness.
 * 
 * Note: Panama distance is hardcoded to try and be robust to reference data
 * changes
 * 
 * @author Simon Goodall
 *
 */
@ExtendWith(ShiroRunner.class)
public class PanamaCanalBookingTests extends AbstractMicroTestCase {

	class BasicCaseRecord {

		Cargo cargo;
		DESSalesMarket market;
		VesselCharter vesselCharter;

		int cameronToColonDistance;

		Port colon;

	}

	public IOptimiserInjectorService makeCanalBookingService() {

		return OptimiserInjectorServiceMaker.begin() //
				.withModuleOverrideBindNamedInstance(ModuleType.Module_LNGTransformerModule, SchedulerConstants.Key_UseCanalSlotBasedWindowTrimming, boolean.class, Boolean.TRUE).make();
	}

	/**
	 * Construct the basic case data - single cargo on a vessel, laden leg is
	 * exactly max-speed hours via panama
	 * 
	 * @return
	 */
	protected BasicCaseRecord createBasicCase() {

		final BasicCaseRecord record = new BasicCaseRecord();

		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_CAMERON);
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_BORYEONG);
		record.colon = portFinder.findPortById(InternalDataConstants.PORT_COLON);

		// Make Cameron to Boryeong direct/suez very large to make it silly to use.
		distanceModelBuilder.setPortToPortDistance(port1, port2, RouteOption.DIRECT, 19_000, false);
		distanceModelBuilder.setPortToPortDistance(port1, port2, RouteOption.SUEZ, 19_000, false);

		// Hard code this PANAMA distance in-case it the underlying distance data
		// changes which would cause this test to fail.
		distanceModelBuilder.setPortToPortDistance(port1, port2, RouteOption.PANAMA, 10093, false);
		record.cameronToColonDistance = 1555;
		distanceModelBuilder.setPortToPortDistance(port1, record.colon, RouteOption.DIRECT, record.cameronToColonDistance, false);

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_160);
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.build();

		final LocalDateTime loadDate = LocalDateTime.of(2021, Month.MARCH, 7, 5, 0, 0);
		final YearMonth dischargeDate = YearMonth.of(2021, Month.MARCH);

		final DESSalesMarket market = spotMarketsModelBuilder.makeDESSaleMarket("desmarket", port2, entity, "7") //
				.build();

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", loadDate.toLocalDate(), port1, null, entity, "5", 22.6) //
				.withWindowStartTime(loadDate.getHour()) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeMarketDESSale("D1", market, dischargeDate) //
				.build() //
				//
				.withVesselAssignment(vesselCharter, 1) //
				.build();

		record.vesselCharter = vesselCharter;
		record.cargo = cargo;
		record.market = market;
		return record;
	}

	/**
	 * This test checks the basic assumptions are valid - max-speed via Panama
	 * (without extra idle or bookings) introduces no lateness
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testBasicCase() {
		final BasicCaseRecord record = createBasicCase();

		final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		cargoModelFinder.getCargoModel().setCanalBookings(canalBookings);
		evaluateTestWith(makeCanalBookingService());

		final Vessel vessel = record.vesselCharter.getVessel();

		final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(schedule);
		Assertions.assertEquals(1, schedule.getCargoAllocations().size());

		final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);
		Assertions.assertNotNull(cargoAllocation);
		final SimpleCargoAllocation ca = new SimpleCargoAllocation(cargoAllocation);

		final Journey ladenLeg = ca.getLadenLeg();
		Assertions.assertSame(RouteOption.PANAMA, ladenLeg.getRouteOption());
		Assertions.assertNull(ladenLeg.getCanalBooking());
		Assertions.assertEquals(vessel.getMaxSpeed(), ladenLeg.getSpeed(), 0.001);

		// No idle time expected
		Assertions.assertEquals(0, ca.getLadenIdle().getDuration());

		final SlotAllocation dischargeAllocation = ca.getDischargeAllocation();
		Assertions.assertNull(dischargeAllocation.getSlotVisit().getLateness());
	}

	/**
	 * This test adds 5 days idle wait for a Panama voyage. Expect that we introduce
	 * 5 days of lateness over the basic case
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSouthboundIdleCase() {
		final BasicCaseRecord record = createBasicCase();

		final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		cargoModelFinder.getCargoModel().setCanalBookings(canalBookings);

		final VesselGroupCanalParameters p = CargoFactory.eINSTANCE.createVesselGroupCanalParameters();
		canalBookings.getVesselGroupCanalParameters().add(p);
		final PanamaSeasonalityRecord psr = CargoFactory.eINSTANCE.createPanamaSeasonalityRecord();
		psr.setNorthboundWaitingDays(10);
		psr.setSouthboundWaitingDays(5);
		psr.setVesselGroupCanalParameter(p);
		canalBookings.getPanamaSeasonalityRecords().add(psr);

		evaluateTestWith(makeCanalBookingService());

		final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(schedule);
		Assertions.assertEquals(1, schedule.getCargoAllocations().size());

		final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);
		Assertions.assertNotNull(cargoAllocation);
		final SimpleCargoAllocation ca = new SimpleCargoAllocation(cargoAllocation);

		final Journey ladenLeg = ca.getLadenLeg();
		Assertions.assertSame(RouteOption.PANAMA, ladenLeg.getRouteOption());
		Assertions.assertNull(ladenLeg.getCanalBooking());

		final SlotAllocation dischargeAllocation = ca.getDischargeAllocation();
		Assertions.assertNotNull(dischargeAllocation.getSlotVisit().getLateness());
		Assertions.assertEquals(5 * 24, dischargeAllocation.getSlotVisit().getLateness().getLatenessInHours());
	}

	/**
	 * In this case we have 5 days of waiting days but also we have 6 days of buffer
	 * for the spot market to schedule. We should see 11 days of lateness come in.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSouthboundIdleWithMarketBufferCase() {
		final BasicCaseRecord record = createBasicCase();
		record.market.setDaysBuffer(6);

		final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		cargoModelFinder.getCargoModel().setCanalBookings(canalBookings);

		final VesselGroupCanalParameters p = CargoFactory.eINSTANCE.createVesselGroupCanalParameters();
		canalBookings.getVesselGroupCanalParameters().add(p);
		final PanamaSeasonalityRecord psr = CargoFactory.eINSTANCE.createPanamaSeasonalityRecord();
		psr.setNorthboundWaitingDays(15);
		psr.setSouthboundWaitingDays(5);
		psr.setVesselGroupCanalParameter(p);
		canalBookings.getPanamaSeasonalityRecords().add(psr);

		evaluateTestWith(makeCanalBookingService());

		final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(schedule);
		Assertions.assertEquals(1, schedule.getCargoAllocations().size());

		final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);
		Assertions.assertNotNull(cargoAllocation);
		final SimpleCargoAllocation ca = new SimpleCargoAllocation(cargoAllocation);

		final Journey ladenLeg = ca.getLadenLeg();
		Assertions.assertSame(RouteOption.PANAMA, ladenLeg.getRouteOption());
		Assertions.assertNull(ladenLeg.getCanalBooking());

		final SlotAllocation dischargeAllocation = ca.getDischargeAllocation();
		Assertions.assertNotNull(dischargeAllocation.getSlotVisit().getLateness());
		Assertions.assertEquals((5 + 6) * 24, dischargeAllocation.getSlotVisit().getLateness().getLatenessInHours());
	}

	/**
	 * Add in a booking. We should see some lateness as we need to wait until 3AM to
	 * start the Panama voyage.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSouthboundFlexiBookingCase() {
		final BasicCaseRecord record = createBasicCase();

		final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		cargoModelFinder.getCargoModel().setCanalBookings(canalBookings);

		final VesselGroupCanalParameters p = CargoFactory.eINSTANCE.createVesselGroupCanalParameters();
		canalBookings.getVesselGroupCanalParameters().add(p);
		final PanamaSeasonalityRecord psr = CargoFactory.eINSTANCE.createPanamaSeasonalityRecord();
		psr.setNorthboundWaitingDays(10);
		psr.setSouthboundWaitingDays(5);
		psr.setVesselGroupCanalParameter(p);
		canalBookings.getPanamaSeasonalityRecords().add(psr);
		final CanalBookingSlot booking1 = cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2021, Month.MARCH, 12), null);

		evaluateTestWith(makeCanalBookingService());

		final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(schedule);
		Assertions.assertEquals(1, schedule.getCargoAllocations().size());

		final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);
		Assertions.assertNotNull(cargoAllocation);
		final SimpleCargoAllocation ca = new SimpleCargoAllocation(cargoAllocation);

		final Journey ladenLeg = ca.getLadenLeg();
		Assertions.assertSame(RouteOption.PANAMA, ladenLeg.getRouteOption());
		Assertions.assertNotNull(ladenLeg.getCanalBooking());
		Assertions.assertSame(booking1, ladenLeg.getCanalBooking());

		final SlotAllocation dischargeAllocation = ca.getDischargeAllocation();
		Assertions.assertNotNull(dischargeAllocation.getSlotVisit().getLateness());

		final Vessel vessel = record.vesselCharter.getVessel();
		final int timeToColon = (int) Math.round((double) record.cameronToColonDistance / vessel.getMaxSpeed());

		final int loadDuration = ca.getLoadAllocation().getSlot().getSchedulingTimeWindow().getDuration();
		final int timeUntilBookingStart = Hours.between(ca.getLoadAllocation().getSlotVisit().getStart(),
				booking1.getBookingDate().atTime(CanalBookingSlot.BOOKING_HOURS_OFFSET, 0).atZone(record.colon.getZoneId())) - loadDuration;

		// Basic delay is 11 hours. This is the wait to the next 3am after arrival
		final int expectedDelayInHoursForBookingStart = timeUntilBookingStart - timeToColon;
		Assertions.assertEquals(expectedDelayInHoursForBookingStart, dischargeAllocation.getSlotVisit().getLateness().getLatenessInHours());
	}

	/**
	 * Add in a booking. We should see some lateness as we need to wait until 3AM to
	 * start the Panama voyage.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSouthboundFlexiBookingCaseWithMarketBuffer() {
		final BasicCaseRecord record = createBasicCase();
		record.market.setDaysBuffer(6);

		final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		cargoModelFinder.getCargoModel().setCanalBookings(canalBookings);

		final VesselGroupCanalParameters p = CargoFactory.eINSTANCE.createVesselGroupCanalParameters();
		final PanamaSeasonalityRecord psr = CargoFactory.eINSTANCE.createPanamaSeasonalityRecord();
		psr.setNorthboundWaitingDays(10);
		psr.setSouthboundWaitingDays(5);
		psr.setVesselGroupCanalParameter(p);
		canalBookings.getPanamaSeasonalityRecords().add(psr);
		canalBookings.getVesselGroupCanalParameters().add(p);
		final CanalBookingSlot booking1 = cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2021, Month.MARCH, 12), null);

		evaluateTestWith(makeCanalBookingService());

		final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(schedule);
		Assertions.assertEquals(1, schedule.getCargoAllocations().size());

		final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);
		Assertions.assertNotNull(cargoAllocation);
		final SimpleCargoAllocation ca = new SimpleCargoAllocation(cargoAllocation);

		final Journey ladenLeg = ca.getLadenLeg();
		Assertions.assertSame(RouteOption.PANAMA, ladenLeg.getRouteOption());
		Assertions.assertNotNull(ladenLeg.getCanalBooking());
		Assertions.assertSame(booking1, ladenLeg.getCanalBooking());

		final SlotAllocation dischargeAllocation = ca.getDischargeAllocation();
		Assertions.assertNotNull(dischargeAllocation.getSlotVisit().getLateness());

		final Vessel vessel = record.vesselCharter.getVessel();
		final int timeToColon = (int) Math.round((double) record.cameronToColonDistance / vessel.getMaxSpeed());

		final int loadDuration = ca.getLoadAllocation().getSlot().getSchedulingTimeWindow().getDuration();
		final int timeUntilBookingStart = Hours.between(ca.getLoadAllocation().getSlotVisit().getStart(),
				booking1.getBookingDate().atTime(CanalBookingSlot.BOOKING_HOURS_OFFSET, 0).atZone(record.colon.getZoneId())) - loadDuration;

		// Basic delay is 11 hours. This is the wait to the next 3am after arrival
		final int expectedDelayInHoursForBookingStart = timeUntilBookingStart - timeToColon;
		Assertions.assertEquals(6 * 24 + expectedDelayInHoursForBookingStart, dischargeAllocation.getSlotVisit().getLateness().getLatenessInHours());
	}

	/**
	 * Add in a booking and arrival margin. We should see some lateness as we need
	 * to wait until 3AM to start the Panama voyage.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSouthboundFlexiBookingCaseWithMargin() {
		final BasicCaseRecord record = createBasicCase();

		final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		cargoModelFinder.getCargoModel().setCanalBookings(canalBookings);

		final VesselGroupCanalParameters p = CargoFactory.eINSTANCE.createVesselGroupCanalParameters();
		final PanamaSeasonalityRecord psr = CargoFactory.eINSTANCE.createPanamaSeasonalityRecord();
		psr.setNorthboundWaitingDays(10);
		psr.setSouthboundWaitingDays(5);
		psr.setVesselGroupCanalParameter(p);
		canalBookings.getPanamaSeasonalityRecords().add(psr);

		canalBookings.getVesselGroupCanalParameters().add(p);
		canalBookings.setArrivalMarginHours(24);

		final CanalBookingSlot booking1 = cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2021, Month.MARCH, 13), null);

		evaluateTestWith(makeCanalBookingService());

		final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(schedule);
		Assertions.assertEquals(1, schedule.getCargoAllocations().size());

		final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);
		Assertions.assertNotNull(cargoAllocation);
		final SimpleCargoAllocation ca = new SimpleCargoAllocation(cargoAllocation);

		final Journey ladenLeg = ca.getLadenLeg();
		Assertions.assertSame(RouteOption.PANAMA, ladenLeg.getRouteOption());
		Assertions.assertNotNull(ladenLeg.getCanalBooking());
		Assertions.assertSame(booking1, ladenLeg.getCanalBooking());

		final SlotAllocation dischargeAllocation = ca.getDischargeAllocation();
		Assertions.assertNotNull(dischargeAllocation.getSlotVisit().getLateness());

		final Vessel vessel = record.vesselCharter.getVessel();
		final int timeToColon = (int) Math.round((double) record.cameronToColonDistance / vessel.getMaxSpeed());

		final int loadDuration = ca.getLoadAllocation().getSlot().getSchedulingTimeWindow().getDuration();
		final int timeUntilBookingStart = Hours.between(ca.getLoadAllocation().getSlotVisit().getStart(),
				booking1.getBookingDate().atTime(CanalBookingSlot.BOOKING_HOURS_OFFSET, 0).atZone(record.colon.getZoneId())) - loadDuration;

		// Basic delay is 24 + 11 hours. This is the wait to the next 3am after arrival
		final int expectedDelayInHoursForBookingStart = timeUntilBookingStart - timeToColon;
		Assertions.assertEquals(expectedDelayInHoursForBookingStart, dischargeAllocation.getSlotVisit().getLateness().getLatenessInHours());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSouthboundAssignedBookingCase() {
		final BasicCaseRecord record = createBasicCase();

		final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		cargoModelFinder.getCargoModel().setCanalBookings(canalBookings);

		final VesselGroupCanalParameters p = CargoFactory.eINSTANCE.createVesselGroupCanalParameters();
		final PanamaSeasonalityRecord psr = CargoFactory.eINSTANCE.createPanamaSeasonalityRecord();
		psr.setNorthboundWaitingDays(10);
		psr.setSouthboundWaitingDays(5);
		psr.setVesselGroupCanalParameter(p);
		canalBookings.getPanamaSeasonalityRecords().add(psr);
		canalBookings.getVesselGroupCanalParameters().add(p);
		final Vessel vessel = record.vesselCharter.getVessel();
		final CanalBookingSlot booking1 = cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2021, Month.MARCH, 12), vessel);

		evaluateTestWith(makeCanalBookingService());

		final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(schedule);
		Assertions.assertEquals(1, schedule.getCargoAllocations().size());

		final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);
		Assertions.assertNotNull(cargoAllocation);
		final SimpleCargoAllocation ca = new SimpleCargoAllocation(cargoAllocation);

		final Journey ladenLeg = ca.getLadenLeg();
		Assertions.assertSame(RouteOption.PANAMA, ladenLeg.getRouteOption());
		Assertions.assertNotNull(ladenLeg.getCanalBooking());
		Assertions.assertSame(booking1, ladenLeg.getCanalBooking());

		final SlotAllocation dischargeAllocation = ca.getDischargeAllocation();
		Assertions.assertNotNull(dischargeAllocation.getSlotVisit().getLateness());

		final int timeToColon = (int) Math.round((double) record.cameronToColonDistance / vessel.getMaxSpeed());

		final int loadDuration = ca.getLoadAllocation().getSlot().getSchedulingTimeWindow().getDuration();
		final int timeUntilBookingStart = Hours.between(ca.getLoadAllocation().getSlotVisit().getStart(),
				booking1.getBookingDate().atTime(CanalBookingSlot.BOOKING_HOURS_OFFSET, 0).atZone(record.colon.getZoneId())) - loadDuration;

		// Basic delay is 11 hours. This is the wait to the next 3am after arrival
		final int expectedDelayInHoursForBookingStart = timeUntilBookingStart - timeToColon;
		Assertions.assertEquals(expectedDelayInHoursForBookingStart, dischargeAllocation.getSlotVisit().getLateness().getLatenessInHours());
	}

	/**
	 * Two bookings, assigned and flexible - we expect the assigned will be selected
	 * over the flexible.
	 */

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSouthboundPreferAssignedBookingCase() {
		final BasicCaseRecord record = createBasicCase();

		final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		cargoModelFinder.getCargoModel().setCanalBookings(canalBookings);

		final VesselGroupCanalParameters p = CargoFactory.eINSTANCE.createVesselGroupCanalParameters();
		final PanamaSeasonalityRecord psr = CargoFactory.eINSTANCE.createPanamaSeasonalityRecord();
		psr.setNorthboundWaitingDays(10);
		psr.setSouthboundWaitingDays(5);
		psr.setVesselGroupCanalParameter(p);
		canalBookings.getPanamaSeasonalityRecords().add(psr);
		canalBookings.getVesselGroupCanalParameters().add(p);
		final Vessel vessel = record.vesselCharter.getVessel();

		// Flexible booking first
		final CanalBookingSlot booking1 = cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2021, Month.MARCH, 12), null);
		// Assigned booking - the one we want to use.
		final CanalBookingSlot booking2 = cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2021, Month.MARCH, 12), vessel);

		evaluateTestWith(makeCanalBookingService());

		final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(schedule);
		Assertions.assertEquals(1, schedule.getCargoAllocations().size());

		final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);
		Assertions.assertNotNull(cargoAllocation);
		final SimpleCargoAllocation ca = new SimpleCargoAllocation(cargoAllocation);

		final Journey ladenLeg = ca.getLadenLeg();
		Assertions.assertSame(RouteOption.PANAMA, ladenLeg.getRouteOption());
		Assertions.assertNotNull(ladenLeg.getCanalBooking());
		Assertions.assertSame(booking2, ladenLeg.getCanalBooking());

		final SlotAllocation dischargeAllocation = ca.getDischargeAllocation();
		Assertions.assertNotNull(dischargeAllocation.getSlotVisit().getLateness());

		final int timeToColon = (int) Math.round((double) record.cameronToColonDistance / vessel.getMaxSpeed());

		final int loadDuration = ca.getLoadAllocation().getSlot().getSchedulingTimeWindow().getDuration();
		final int timeUntilBookingStart = Hours.between(ca.getLoadAllocation().getSlotVisit().getStart(),
				booking1.getBookingDate().atTime(CanalBookingSlot.BOOKING_HOURS_OFFSET, 0).atZone(record.colon.getZoneId())) - loadDuration;

		// Basic delay is 11 hours. This is the wait to the next 3am after arrival
		final int expectedDelayInHoursForBookingStart = timeUntilBookingStart - timeToColon;
		Assertions.assertEquals(expectedDelayInHoursForBookingStart, dischargeAllocation.getSlotVisit().getLateness().getLatenessInHours());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSouthboundAssignedBookingCaseWithMarketBuffer() {
		final BasicCaseRecord record = createBasicCase();
		record.market.setDaysBuffer(6);

		final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		cargoModelFinder.getCargoModel().setCanalBookings(canalBookings);

		final VesselGroupCanalParameters p = CargoFactory.eINSTANCE.createVesselGroupCanalParameters();
		final PanamaSeasonalityRecord psr = CargoFactory.eINSTANCE.createPanamaSeasonalityRecord();
		psr.setNorthboundWaitingDays(10);
		psr.setSouthboundWaitingDays(5);
		psr.setVesselGroupCanalParameter(p);
		canalBookings.getPanamaSeasonalityRecords().add(psr);
		canalBookings.getVesselGroupCanalParameters().add(p);
		final Vessel vessel = record.vesselCharter.getVessel();
		final CanalBookingSlot booking1 = cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2021, Month.MARCH, 12), vessel);

		evaluateTestWith(makeCanalBookingService());

		final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(schedule);
		Assertions.assertEquals(1, schedule.getCargoAllocations().size());

		final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);
		Assertions.assertNotNull(cargoAllocation);
		final SimpleCargoAllocation ca = new SimpleCargoAllocation(cargoAllocation);

		final Journey ladenLeg = ca.getLadenLeg();
		Assertions.assertSame(RouteOption.PANAMA, ladenLeg.getRouteOption());
		Assertions.assertNotNull(ladenLeg.getCanalBooking());
		Assertions.assertSame(booking1, ladenLeg.getCanalBooking());

		final SlotAllocation dischargeAllocation = ca.getDischargeAllocation();
		Assertions.assertNotNull(dischargeAllocation.getSlotVisit().getLateness());

		final int timeToColon = (int) Math.round((double) record.cameronToColonDistance / vessel.getMaxSpeed());

		final int loadDuration = ca.getLoadAllocation().getSlot().getSchedulingTimeWindow().getDuration();
		final int timeUntilBookingStart = Hours.between(ca.getLoadAllocation().getSlotVisit().getStart(),
				booking1.getBookingDate().atTime(CanalBookingSlot.BOOKING_HOURS_OFFSET, 0).atZone(record.colon.getZoneId())) - loadDuration;

		// Basic delay is 11 hours. This is the wait to the next 3am after arrival
		final int expectedDelayInHoursForBookingStart = timeUntilBookingStart - timeToColon;
		Assertions.assertEquals(6 * 24 + expectedDelayInHoursForBookingStart, dischargeAllocation.getSlotVisit().getLateness().getLatenessInHours());
	}

	/**
	 * Test assigned booking with arrival margin. Note 1 day later over no margin
	 * case. Note: The 1 day margin make get lost via 1 day later booking....
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSouthboundAssignedBookingCaseWithMargin() {
		final BasicCaseRecord record = createBasicCase();

		final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		cargoModelFinder.getCargoModel().setCanalBookings(canalBookings);

		final VesselGroupCanalParameters p = CargoFactory.eINSTANCE.createVesselGroupCanalParameters();
		final PanamaSeasonalityRecord psr = CargoFactory.eINSTANCE.createPanamaSeasonalityRecord();
		psr.setNorthboundWaitingDays(10);
		psr.setSouthboundWaitingDays(5);
		psr.setVesselGroupCanalParameter(p);
		canalBookings.getPanamaSeasonalityRecords().add(psr);
		canalBookings.getVesselGroupCanalParameters().add(p);

		canalBookings.setArrivalMarginHours(24);

		final Vessel vessel = record.vesselCharter.getVessel();
		final CanalBookingSlot booking1 = cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2021, Month.MARCH, 13), vessel);

		evaluateTestWith(makeCanalBookingService());

		final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(schedule);
		Assertions.assertEquals(1, schedule.getCargoAllocations().size());

		final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);
		Assertions.assertNotNull(cargoAllocation);
		final SimpleCargoAllocation ca = new SimpleCargoAllocation(cargoAllocation);

		final Journey ladenLeg = ca.getLadenLeg();
		Assertions.assertSame(RouteOption.PANAMA, ladenLeg.getRouteOption());
		Assertions.assertNotNull(ladenLeg.getCanalBooking());
		Assertions.assertSame(booking1, ladenLeg.getCanalBooking());

		final SlotAllocation dischargeAllocation = ca.getDischargeAllocation();
		Assertions.assertNotNull(dischargeAllocation.getSlotVisit().getLateness());

		final int timeToColon = (int) Math.round((double) record.cameronToColonDistance / vessel.getMaxSpeed());

		final int loadDuration = ca.getLoadAllocation().getSlot().getSchedulingTimeWindow().getDuration();
		final int timeUntilBookingStart = Hours.between(ca.getLoadAllocation().getSlotVisit().getStart(),
				booking1.getBookingDate().atTime(CanalBookingSlot.BOOKING_HOURS_OFFSET, 0).atZone(record.colon.getZoneId())) - loadDuration;

		// Basic delay is 24 + 11 hours. This is the wait to the next 3am after arrival
		final int expectedDelayInHoursForBookingStart = timeUntilBookingStart - timeToColon;
		Assertions.assertEquals(expectedDelayInHoursForBookingStart, dischargeAllocation.getSlotVisit().getLateness().getLatenessInHours());
	}

	/**
	 * This is a different type of case. Make sure we include idle days on the new
	 * legs created for the generated charter outs. In the case we create the new
	 * Panama voyage after the charter out event.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testIdleDaysPanamaAfterGeneratedCharterOut() {

		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_CAMERON);
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_BORYEONG);

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_160);
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.build();

		CharterOutMarket charterOutMarket = spotMarketsModelBuilder.createCharterOutMarket("COMarket", vessel, "80000", 10);
		charterOutMarket.setEnabled(true);
		charterOutMarket.getAvailablePorts().add(port2);

		final LocalDateTime loadDate1 = LocalDateTime.of(2021, Month.FEBRUARY, 1, 0, 0, 0);
		final LocalDateTime dischargeDate1 = LocalDateTime.of(2021, Month.MARCH, 10, 0, 0, 0);
		final LocalDateTime loadDate2 = LocalDateTime.of(2021, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate2 = LocalDateTime.of(2021, Month.JULY, 10, 0, 0, 0);

		final DESSalesMarket market = spotMarketsModelBuilder.makeDESSaleMarket("desmarket", port2, entity, "7") //
				.build();

		// Create two cargoes with a large idle time between them for chartering out
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", loadDate1.toLocalDate(), port1, null, entity, "5", 22.6) //
				.withWindowStartTime(loadDate1.getHour()) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D1", dischargeDate1.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate1.getHour()) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselCharter, 1) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", loadDate2.toLocalDate(), port1, null, entity, "5", 22.6) //
				.withWindowStartTime(loadDate2.getHour()) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D2", dischargeDate2.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate2.getHour()) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselCharter, 2) //
				.build();

		final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		cargoModelFinder.getCargoModel().setCanalBookings(canalBookings);

		final VesselGroupCanalParameters vgcp = cargoModelBuilder.createVesselGroupCanalParameters("default");
		canalBookings.getVesselGroupCanalParameters().add(vgcp);

		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		// Enable charter out system
		userSettings.setGenerateCharterOuts(true);

		// Evaluate with zero idle days...
		int[] charterDurationThenTravelDuration = new int[2];
		{
			canalBookings.getPanamaSeasonalityRecords().add(//
					cargoModelBuilder.createPanamaSeasonalityRecord(vgcp, 0, 0, 0, 0, 0));
			evaluateTestWith(userSettings, makeCanalBookingService());

			final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(2, schedule.getCargoAllocations().size());

			final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);
			Assertions.assertNotNull(cargoAllocation);
			final SimpleCargoAllocation ca = new SimpleCargoAllocation(cargoAllocation);

			final Idle ballastIdle = ca.getBallastIdle();
			Event gcoEvent = ballastIdle.getNextEvent(); // Expect to be GCO event
			Event postGCOJourney = gcoEvent.getNextEvent(); // post gco journey
			Event postGCOIdle = postGCOJourney.getNextEvent(); // post gco odl;e

			charterDurationThenTravelDuration[0] = gcoEvent.getDuration();
			charterDurationThenTravelDuration[1] = postGCOJourney.getDuration() + postGCOIdle.getDuration();

		}
		// ... then add in the waiting days and expect to see the time shift from the
		// event duration into the journey time
		{
			canalBookings.getPanamaSeasonalityRecords().clear();
			final PanamaSeasonalityRecord psr = cargoModelBuilder.createPanamaSeasonalityRecord(vgcp, 0, 0, 0, 5, 15);
			canalBookings.getPanamaSeasonalityRecords().add(psr);
			evaluateTestWith(userSettings, makeCanalBookingService());

			final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(2, schedule.getCargoAllocations().size());

			final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);
			Assertions.assertNotNull(cargoAllocation);
			final SimpleCargoAllocation ca = new SimpleCargoAllocation(cargoAllocation);

			final Idle ballastIdle = ca.getBallastIdle();
			Event gcoEvent = ballastIdle.getNextEvent(); // Expect to be GCO event
			Journey postGCOJourney = (Journey) gcoEvent.getNextEvent(); // post gco journey
			Event postGCOIdle = postGCOJourney.getNextEvent(); // post gco odl;e
			Assertions.assertEquals(RouteOption.PANAMA, postGCOJourney.getRouteOption());
			// Expect waiting days to have shifted out of the charter out event and into the
			// journey+idle time
			Assertions.assertEquals(charterDurationThenTravelDuration[0] - (psr.getNorthboundWaitingDays() * 24), gcoEvent.getDuration());
			Assertions.assertEquals(charterDurationThenTravelDuration[1] + (psr.getNorthboundWaitingDays() * 24), postGCOJourney.getDuration() + postGCOIdle.getDuration());
		}
	}

	/**
	 * This is a different type of case. Make sure we include idle days on the new
	 * legs created for the generated charter outs. In the case we create the new
	 * Panama voyage before the charter out event.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testIdleDaysPanamaBeforeGeneratedCharterOut() {

		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_CAMERON);
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_BORYEONG);

		final Port colon = portFinder.findPortById(InternalDataConstants.PORT_COLON);

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_160);
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.build();

		CharterOutMarket charterOutMarket = spotMarketsModelBuilder.createCharterOutMarket("COMarket", vessel, "80000", 10);
		charterOutMarket.setEnabled(true);
		charterOutMarket.getAvailablePorts().add(port1);

		final LocalDateTime loadDate1 = LocalDateTime.of(2021, Month.FEBRUARY, 1, 0, 0, 0);
		final LocalDateTime dischargeDate1 = LocalDateTime.of(2021, Month.MARCH, 10, 0, 0, 0);
		final LocalDateTime loadDate2 = LocalDateTime.of(2021, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate2 = LocalDateTime.of(2021, Month.JULY, 10, 0, 0, 0);

		final DESSalesMarket market = spotMarketsModelBuilder.makeDESSaleMarket("desmarket", port2, entity, "7") //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", loadDate1.toLocalDate(), port1, null, entity, "5", 22.6) //
				.withWindowStartTime(loadDate1.getHour()) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D1", dischargeDate1.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate1.getHour()) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselCharter, 1) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", loadDate2.toLocalDate(), port1, null, entity, "5", 22.6) //
				.withWindowStartTime(loadDate2.getHour()) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D2", dischargeDate2.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate2.getHour()) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselCharter, 2) //
				.build();

		final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		cargoModelFinder.getCargoModel().setCanalBookings(canalBookings);

		final VesselGroupCanalParameters vgcp = cargoModelBuilder.createVesselGroupCanalParameters("default");
		canalBookings.getVesselGroupCanalParameters().add(vgcp);

		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		// Enable charter out system
		userSettings.setGenerateCharterOuts(true);
		// Evaluate with zero idle days...

		int[] charterDurationThenTravelDuration = new int[2];
		{
			final PanamaSeasonalityRecord psr = cargoModelBuilder.createPanamaSeasonalityRecord(vgcp, 0, 0, 0, 0, 0);
			canalBookings.getPanamaSeasonalityRecords().add(psr);
			evaluateTestWith(userSettings, makeCanalBookingService());

			final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(2, schedule.getCargoAllocations().size());

			final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);
			Assertions.assertNotNull(cargoAllocation);
			final SimpleCargoAllocation ca = new SimpleCargoAllocation(cargoAllocation);

			Journey preGCOJourney = ca.getBallastLeg();
			Event preGCOIdle = preGCOJourney.getNextEvent(); // post gco odl;e
			Event gcoEvent = preGCOIdle.getNextEvent(); // Expect to be GCO event
			// Journey preGCOJourney = (Journey) gcoEvent.getNextEvent(); // post gco
			// journey
			Assertions.assertEquals(RouteOption.PANAMA, preGCOJourney.getRouteOption());

			charterDurationThenTravelDuration[0] = gcoEvent.getDuration();
			charterDurationThenTravelDuration[1] = preGCOJourney.getDuration() + preGCOIdle.getDuration();

		}
		// ... then add in the waiting days and expect to see the time shift from the
		// event duration into the journey time
		{
			canalBookings.getPanamaSeasonalityRecords().clear();
			final PanamaSeasonalityRecord psr = cargoModelBuilder.createPanamaSeasonalityRecord(vgcp, 0, 0, 0, 5, 15);
			evaluateTestWith(userSettings, makeCanalBookingService());

			final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(2, schedule.getCargoAllocations().size());

			final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);
			Assertions.assertNotNull(cargoAllocation);
			final SimpleCargoAllocation ca = new SimpleCargoAllocation(cargoAllocation);

			Journey preGCOJourney = ca.getBallastLeg();
			Event preGCOIdle = preGCOJourney.getNextEvent(); // post gco odl;e
			Event gcoEvent = preGCOIdle.getNextEvent(); // Expect to be GCO event
			// Journey preGCOJourney = (Journey) gcoEvent.getNextEvent(); // post gco
			// journey
			Assertions.assertEquals(RouteOption.PANAMA, preGCOJourney.getRouteOption());
			// Expect waiting days to have shifted out of the charter out event and into the
			// journey+idle time
			Assertions.assertEquals(charterDurationThenTravelDuration[0] - (psr.getNorthboundWaitingDays() * 24), gcoEvent.getDuration());
			Assertions.assertEquals(charterDurationThenTravelDuration[1] + (psr.getNorthboundWaitingDays() * 24), preGCOJourney.getDuration() + preGCOIdle.getDuration());

		}
	}

	/**
	 * Test with a vessel group that contains multiple vessels and we have a cargo
	 * that should pick up an associate (vessel group) booking.
	 */
	@Disabled("Although this test is valid, we only want to allocate bookings when they are really needed in order to make the discharge.")
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testVesselGroupBookingMatchedToCargo() {

		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_CAMERON);
		final Port port2 = portFinder.findPortById("L_JP_Osaka");

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_160);
		final Vessel dummyVessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final VesselGroup vesselGroup = FleetFactory.eINSTANCE.createVesselGroup();
		vesselGroup.setName("newVesselGroup");
		vesselGroup.getVessels().add(vessel);
		vesselGroup.getVessels().add(dummyVessel);
		fleetModelFinder.getFleetModel().getVesselGroups().add(vesselGroup);

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2020, 12, 14, 18, 00)) //
				.withEndWindow(LocalDateTime.of(2021, 03, 31, 19, 00)) //
				.withCharterRate("80000") //
				.withStartHeel(6000, 6000, 22.67, "0.01") //
				.withEndHeel(100, 100, EVesselTankState.MUST_BE_COLD, false) //
				.build();

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("load", LocalDate.of(2021, 01, 11), port1, null, entity, "3.70") //
				.withVisitDuration(24) //
				.withWindowSize(1, TimePeriod.DAYS) //
				.build() //
				//
				.makeDESSale("discharge", LocalDate.of(2021, 02, 13), port2, null, entity, "5.95") //
				.withVisitDuration(24) //
				.withWindowSize(5, TimePeriod.DAYS) //
				.build() //
				//
				.withVesselAssignment(vesselCharter, 0) //
				.build();

		final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		cargoModelFinder.getCargoModel().setCanalBookings(canalBookings);

		final VesselGroupCanalParameters p = CargoFactory.eINSTANCE.createVesselGroupCanalParameters();
		p.setName("canalParams");
		final PanamaSeasonalityRecord psr = CargoFactory.eINSTANCE.createPanamaSeasonalityRecord();
		psr.setNorthboundWaitingDays(2);
		psr.setSouthboundWaitingDays(2);
		psr.setVesselGroupCanalParameter(p);
		canalBookings.getPanamaSeasonalityRecords().add(psr);
		p.getVesselGroup().add(vesselGroup);
		canalBookings.getVesselGroupCanalParameters().add(p);

		// Sanity check force vessel to booking, if this is valid then assigning to the
		// vessel group should also be valid
		final CanalBookingSlot booking1 = cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2021, 01, 17), vessel);

		evaluateTestWith(makeCanalBookingService());
		{
			final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);

			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(schedule.getSequences().size(), 1);
			final Sequence sequence = schedule.getSequences().get(0);
			final Iterator<Event> eventsIter = sequence.getEvents().iterator();
			Assertions.assertTrue(eventsIter.hasNext());
			Event currentEvent = eventsIter.next();
			while (!(currentEvent instanceof SlotVisit) && eventsIter.hasNext()) {
				currentEvent = eventsIter.next();
			}
			Assertions.assertTrue(currentEvent instanceof SlotVisit);
			final SlotVisit loadSlotVisit = (SlotVisit) currentEvent;

			Assertions.assertEquals(loadSlotVisit.getSlotAllocation().getSlot(), cargo.getSlots().get(0));
			Assertions.assertTrue(eventsIter.hasNext());
			final Event panamaJourneyEvent = eventsIter.next();
			Assertions.assertTrue(panamaJourneyEvent instanceof Journey);
			final Journey panamaJourney = (Journey) panamaJourneyEvent;
			Assertions.assertEquals(panamaJourney.getRouteOption(), RouteOption.PANAMA);
			Assertions.assertEquals(panamaJourney.getCanalBooking(), booking1);
		}

		// If we get this far, the assertions prove that the booking will (should?) be
		// chosen
		// Switch booking to general vessel group
		booking1.setVessel(vesselGroup);
		{
			evaluateTestWith(makeCanalBookingService());
			final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);

			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(schedule.getSequences().size(), 1);
			final Sequence sequence = schedule.getSequences().get(0);
			final Iterator<Event> eventsIter = sequence.getEvents().iterator();
			Assertions.assertTrue(eventsIter.hasNext());
			Event currentEvent = eventsIter.next();
			while (!(currentEvent instanceof SlotVisit) && eventsIter.hasNext()) {
				currentEvent = eventsIter.next();
			}
			Assertions.assertTrue(currentEvent instanceof SlotVisit);
			final SlotVisit loadSlotVisit = (SlotVisit) currentEvent;

			Assertions.assertEquals(loadSlotVisit.getSlotAllocation().getSlot(), cargo.getSlots().get(0));
			Assertions.assertTrue(eventsIter.hasNext());
			final Event panamaJourneyEvent = eventsIter.next();
			Assertions.assertTrue(panamaJourneyEvent instanceof Journey);
			final Journey panamaJourney = (Journey) panamaJourneyEvent;
			Assertions.assertEquals(panamaJourney.getRouteOption(), RouteOption.PANAMA);
			Assertions.assertEquals(panamaJourney.getCanalBooking(), booking1);
		}
	}

	/**
	 * Test with a vessel group that contains multiple vessels and we have a cargo
	 * that should pick up an associate (vessel group) booking.
	 */
	@Disabled("Although this test is valid, we only want to allocate bookings when they are really needed in order to make the discharge.")
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testTwoCargoVesselSpecificAndVesselGroupBookingMatch() {

		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_CAMERON);
		final Port port2 = portFinder.findPortById("L_JP_Tokyo");
		final Port port3 = portFinder.findPortById(InternalDataConstants.PORT_OGISHIMA);

		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_160);
		final Vessel vessel2 = fleetModelFinder.findVessel("MEGI_176");

		final VesselGroup vesselGroup = FleetFactory.eINSTANCE.createVesselGroup();
		vesselGroup.setName("newVesselGroup");
		vesselGroup.getVessels().add(vessel1);
		vesselGroup.getVessels().add(vessel2);
		fleetModelFinder.getFleetModel().getVesselGroups().add(vesselGroup);

		final VesselCharter vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel1, entity) //
				.withStartWindow(LocalDateTime.of(2020, 12, 15, 00, 00)) //
				.withCharterRate("80000") //
				.withStartHeel(6000, 6000, 22.67, "0.01") //
				.withEndHeel(100, 100, EVesselTankState.MUST_BE_COLD, false) //
				.build();

		final VesselCharter vesselCharter2 = cargoModelBuilder.makeVesselCharter(vessel2, entity) //
				.withStartWindow(LocalDateTime.of(2020, 12, 26, 00, 00)) //
				.withCharterRate("80000") //
				.withStartHeel(6000, 6000, 22.67, "0.01") //
				.withEndHeel(100, 100, EVesselTankState.MUST_BE_COLD, false) //
				.build();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("load1", LocalDate.of(2021, 01, 11), port1, null, entity, "3.70") //
				.withVisitDuration(24) //
				.withWindowSize(1, TimePeriod.DAYS) //
				.build() //
				//
				.makeDESSale("discharge1", LocalDate.of(2021, 02, 15), port2, null, entity, "5.35") //
				.withVisitDuration(24) //
				.withWindowSize(1, TimePeriod.DAYS) //
				.build() //
				//
				.withVesselAssignment(vesselCharter1, 0) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("load2", LocalDate.of(2021, 01, 13), port1, null, entity, "3.70") //
				.withVisitDuration(24) //
				.withWindowSize(1, TimePeriod.DAYS) //
				.build() //
				//
				.makeDESSale("discharge2", LocalDate.of(2021, 02, 15), port3, null, entity, "10.33") //
				.withVisitDuration(24) //
				.withWindowSize(1, TimePeriod.DAYS) //
				.build() //
				//
				.withVesselAssignment(vesselCharter2, 0) //
				.build();

		final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		cargoModelFinder.getCargoModel().setCanalBookings(canalBookings);

		final VesselGroupCanalParameters p = CargoFactory.eINSTANCE.createVesselGroupCanalParameters();
		p.setName("canalParams");
		final PanamaSeasonalityRecord psr = CargoFactory.eINSTANCE.createPanamaSeasonalityRecord();
		psr.setNorthboundWaitingDays(2);
		psr.setSouthboundWaitingDays(2);
		psr.setVesselGroupCanalParameter(p);
		canalBookings.getPanamaSeasonalityRecords().add(psr);
		p.getVesselGroup().add(vesselGroup);
		canalBookings.getVesselGroupCanalParameters().add(p);

		// Sanity check force vessels to bookings, if this is valid then loosening one
		// of the bookings to the vessel group should also be valid
		final CanalBookingSlot booking1 = cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2021, 01, 18), vessel2);
		final CanalBookingSlot booking2 = cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2021, 01, 18), vessel1);

		evaluateTestWith(makeCanalBookingService());
		{
			final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);

			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(schedule.getSequences().size(), 2);
			final Sequence sequence1;
			final Sequence sequence2;
			final Sequence firstSequence = schedule.getSequences().get(0);
			if (firstSequence.getVesselCharter() == vesselCharter1) {
				sequence1 = firstSequence;
				sequence2 = schedule.getSequences().get(1);
			} else {
				sequence1 = schedule.getSequences().get(1);
				sequence2 = firstSequence;
			}

			Iterator<Event> eventsIter = sequence1.getEvents().iterator();
			Assertions.assertTrue(eventsIter.hasNext());
			Event currentEvent = eventsIter.next();
			while (!(currentEvent instanceof SlotVisit) && eventsIter.hasNext()) {
				currentEvent = eventsIter.next();
			}
			Assertions.assertTrue(currentEvent instanceof SlotVisit);
			SlotVisit loadSlotVisit = (SlotVisit) currentEvent;

			Assertions.assertEquals(loadSlotVisit.getSlotAllocation().getSlot(), cargo1.getSlots().get(0));
			Assertions.assertTrue(eventsIter.hasNext());
			Event panamaJourneyEvent = eventsIter.next();
			Assertions.assertTrue(panamaJourneyEvent instanceof Journey);
			Journey panamaJourney = (Journey) panamaJourneyEvent;
			Assertions.assertEquals(panamaJourney.getRouteOption(), RouteOption.PANAMA);
			Assertions.assertEquals(panamaJourney.getCanalBooking(), booking2);

			eventsIter = sequence2.getEvents().iterator();
			Assertions.assertTrue(eventsIter.hasNext());
			currentEvent = eventsIter.next();
			while (!(currentEvent instanceof SlotVisit) && eventsIter.hasNext()) {
				currentEvent = eventsIter.next();
			}
			Assertions.assertTrue(currentEvent instanceof SlotVisit);
			loadSlotVisit = (SlotVisit) currentEvent;

			Assertions.assertEquals(loadSlotVisit.getSlotAllocation().getSlot(), cargo2.getSlots().get(0));
			Assertions.assertTrue(eventsIter.hasNext());
			panamaJourneyEvent = eventsIter.next();
			Assertions.assertTrue(panamaJourneyEvent instanceof Journey);
			panamaJourney = (Journey) panamaJourneyEvent;
			Assertions.assertEquals(RouteOption.PANAMA, panamaJourney.getRouteOption());
			Assertions.assertEquals(booking1, panamaJourney.getCanalBooking());
		}

		// If we get this far, the assertions prove that the bookings can be selected to
		// match both cargo laden journeys
		// Switch booking to general vessel group
		booking2.setVessel(vesselGroup);
		{
			evaluateTestWith(makeCanalBookingService());

			final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);

			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(2, schedule.getSequences().size());
			final Sequence sequence1;
			final Sequence sequence2;
			final Sequence firstSequence = schedule.getSequences().get(0);
			if (firstSequence.getVesselCharter() == vesselCharter1) {
				sequence1 = firstSequence;
				sequence2 = schedule.getSequences().get(1);
			} else {
				sequence1 = schedule.getSequences().get(1);
				sequence2 = firstSequence;
			}

			Iterator<Event> eventsIter = sequence1.getEvents().iterator();
			Assertions.assertTrue(eventsIter.hasNext());
			Event currentEvent = eventsIter.next();
			while (!(currentEvent instanceof SlotVisit) && eventsIter.hasNext()) {
				currentEvent = eventsIter.next();
			}
			Assertions.assertTrue(currentEvent instanceof SlotVisit);
			SlotVisit loadSlotVisit = (SlotVisit) currentEvent;

			Assertions.assertEquals(loadSlotVisit.getSlotAllocation().getSlot(), cargo1.getSlots().get(0));
			Assertions.assertTrue(eventsIter.hasNext());
			Event panamaJourneyEvent = eventsIter.next();
			Assertions.assertTrue(panamaJourneyEvent instanceof Journey);
			Journey panamaJourney = (Journey) panamaJourneyEvent;
			Assertions.assertEquals(RouteOption.PANAMA, panamaJourney.getRouteOption());
			Assertions.assertEquals(booking2, panamaJourney.getCanalBooking());

			eventsIter = sequence2.getEvents().iterator();
			Assertions.assertTrue(eventsIter.hasNext());
			currentEvent = eventsIter.next();
			while (!(currentEvent instanceof SlotVisit) && eventsIter.hasNext()) {
				currentEvent = eventsIter.next();
			}
			Assertions.assertTrue(currentEvent instanceof SlotVisit);
			loadSlotVisit = (SlotVisit) currentEvent;

			Assertions.assertEquals(cargo2.getSlots().get(0), loadSlotVisit.getSlotAllocation().getSlot());
			Assertions.assertTrue(eventsIter.hasNext());
			panamaJourneyEvent = eventsIter.next();
			Assertions.assertTrue(panamaJourneyEvent instanceof Journey);
			panamaJourney = (Journey) panamaJourneyEvent;
			Assertions.assertEquals(RouteOption.PANAMA, panamaJourney.getRouteOption());
			Assertions.assertEquals(booking1, panamaJourney.getCanalBooking());
		}
	}
}
