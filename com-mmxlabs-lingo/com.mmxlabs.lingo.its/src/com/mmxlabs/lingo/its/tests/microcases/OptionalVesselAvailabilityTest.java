/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.types.TimePeriod;

/**
 * 
 * @author achurchill
 *
 */
@RunWith(value = ShiroRunner.class)
public class OptionalVesselAvailabilityTest extends AbstractMicroTestCase {

	@Test
	@Category({ MicroTest.class })
	public void testCargo_CargoOnNonOptionalVessel() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");

		@NonNull
		final Port port2 = portFinder.findPort("Dominion Cove Point LNG");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getPortModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);
		vesselClass.setMaxSpeed(15.0);

		final LocalDateTime dischargeDate = LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100);

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", LocalDate.of(2015, 12, 1), port1, null, entity, "5") //
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
		});
	}
	
	@Test
	@Category({ MicroTest.class })
	public void testCargo_CargoOnOptionalVessel() throws Exception {
		
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withOptionality(true)
				.build();
		
		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");
		
		@NonNull
		final Port port2 = portFinder.findPort("Dominion Cove Point LNG");
		
		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");
		
		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getPortModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);
		vesselClass.setMaxSpeed(15.0);
		
		final LocalDateTime dischargeDate = LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100);
		
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", LocalDate.of(2015, 12, 1), port1, null, entity, "5") //
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
		});
	}
	
	@Test
	@Category({ MicroTest.class })
	public void testCargo_NoCargoOnNonOptionalVessel() throws Exception {
		
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withOptionality(false)
				.build();
		
		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");
		
		@NonNull
		final Port port2 = portFinder.findPort("Dominion Cove Point LNG");
		
		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");
		
		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getPortModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);
		vesselClass.setMaxSpeed(15.0);
		
		final LocalDateTime dischargeDate = LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100);
		
		evaluateTest(null, null, scenarioRunner -> {
			
			EList<Sequence> sequences = scenarioRunner.getScenario().getScheduleModel().getSchedule().getSequences();
			boolean found = false;
			for (Sequence sequence : sequences) {
				if (vesselAvailability.equals(sequence.getVesselAvailability())) {
					found = true;
					break;
				}
			}
			assert found == true;
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testCargo_NoCargoOnOptionalVessel() throws Exception {
		
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withOptionality(true)
				.build();
		
		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");
		
		@NonNull
		final Port port2 = portFinder.findPort("Dominion Cove Point LNG");
		
		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");
		
		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getPortModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);
		vesselClass.setMaxSpeed(15.0);
		
		final LocalDateTime dischargeDate = LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100);
		
		evaluateTest(null, null, scenarioRunner -> {
			
			EList<Sequence> sequences = scenarioRunner.getScenario().getScheduleModel().getSchedule().getSequences();
			boolean found = false;
			for (Sequence sequence : sequences) {
				if (vesselAvailability.equals(sequence.getVesselAvailability())) {
					found = true;
					break;
				}
			}
			assert found == false;
		});
	}

}