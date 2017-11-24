/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;

/**
 * 
 * @author achurchill
 *
 */
@RunWith(value = ShiroRunner.class)
public class MiscCostsTests extends AbstractMicroTestCase {

	@Test
	@Category({ MicroTest.class })
	public void testCargo_NoMiscCosts() throws Exception {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withOptionality(true).build();

		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");

		@NonNull
		final Port port2 = portFinder.findPort("Dominion Cove Point LNG");

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);
		vessel.setMaxSpeed(15.0);

		final LocalDateTime dischargeDate = LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100);

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", LocalDate.of(2015, 12, 1), port1, null, entity, "5") //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		evaluateTest(null, null, scenarioRunner -> {

			final SlotVisit visit1 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(0), lngScenarioModel);
			final PortVisitLateness lateness1 = visit1.getLateness();
			Assert.assertNull(lateness1);

			final SlotVisit visit2 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(1), lngScenarioModel);
			final PortVisitLateness lateness2 = visit2.getLateness();
			Assert.assertNull(lateness2);

			CargoAllocation cargoAllocation = lngScenarioModel.getScheduleModel().getSchedule().getCargoAllocations().stream().filter(c -> ScheduleModelUtils.matchingSlots(cargo, c)).findFirst()
					.get();
			assert cargoAllocation != null;
			Assert.assertEquals(6042139L, cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss());
			StartEvent start = getStartEvent(vesselAvailability);
			Assert.assertEquals(0, start.getGroupProfitAndLoss().getProfitAndLoss());
			Assert.assertEquals(6042139L, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testCargo_WithMiscCostsLoad() throws Exception {
	
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withOptionality(true).build();

		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");

		@NonNull
		final Port port2 = portFinder.findPort("Dominion Cove Point LNG");

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);
		vessel.setMaxSpeed(15.0);

		final LocalDateTime dischargeDate = LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100);

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", LocalDate.of(2015, 12, 1), port1, null, entity, "5") //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withMiscCosts(1_000_000).build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		evaluateTest(null, null, scenarioRunner -> {

			final SlotVisit visit1 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(0), lngScenarioModel);
			final PortVisitLateness lateness1 = visit1.getLateness();
			Assert.assertNull(lateness1);

			final SlotVisit visit2 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(1), lngScenarioModel);
			final PortVisitLateness lateness2 = visit2.getLateness();
			Assert.assertNull(lateness2);

			CargoAllocation cargoAllocation = lngScenarioModel.getScheduleModel().getSchedule().getCargoAllocations().stream().filter(c -> ScheduleModelUtils.matchingSlots(cargo, c)).findFirst()
					.get();
			assert cargoAllocation != null;
			Assert.assertEquals(String.format("Expected %s was %s", 6_042_139L - 1_000_000L, cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss()),6_042_139L - 1_000_000L, cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss());
			Assert.assertEquals( 6_042_139L - 1_000_000L, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testCargo_WithMiscCostsDischarge() throws Exception {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withOptionality(true).build();

		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");

		@NonNull
		final Port port2 = portFinder.findPort("Dominion Cove Point LNG");

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);
		vessel.setMaxSpeed(15.0);

		final LocalDateTime dischargeDate = LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100);

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", LocalDate.of(2015, 12, 1), port1, null, entity, "5") //
				.withVolumeLimits(0, 140000, VolumeUnits.M3)//
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withMiscCosts(1_000_000) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		evaluateTest(null, null, scenarioRunner -> {

			final SlotVisit visit1 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(0), lngScenarioModel);
			final PortVisitLateness lateness1 = visit1.getLateness();
			Assert.assertNull(lateness1);

			final SlotVisit visit2 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(1), lngScenarioModel);
			final PortVisitLateness lateness2 = visit2.getLateness();
			Assert.assertNull(lateness2);

			CargoAllocation cargoAllocation = lngScenarioModel.getScheduleModel().getSchedule().getCargoAllocations().stream().filter(c -> ScheduleModelUtils.matchingSlots(cargo, c)).findFirst()
					.get();
			assert cargoAllocation != null;
			Assert.assertEquals(String.format("Expected %s was %s",  6_042_139L - 1_000_000L, cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss()),  6_042_139L - 1_000_000L, cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss());
			Assert.assertEquals(6_042_139L - 1_000_000L, ScheduleModelKPIUtils.getScheduleProfitAndLoss(lngScenarioModel.getScheduleModel().getSchedule()));
		});
	}

	private StartEvent getStartEvent(final VesselAvailability vesselAvailability) {
		Sequence sequence = lngScenarioModel.getScheduleModel().getSchedule().getSequences().stream().filter(s -> s.getVesselAvailability().equals(vesselAvailability)).findFirst().get();
		Event event = sequence.getEvents().get(0);
		assert event instanceof StartEvent;
		return (StartEvent) event;
	}

}