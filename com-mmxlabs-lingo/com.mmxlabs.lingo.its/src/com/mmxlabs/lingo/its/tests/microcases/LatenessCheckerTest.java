/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;

/**
 * 
 * @author Simon Goodall
 *
 */
@RunWith(value = ShiroRunner.class)
public class LatenessCheckerTest extends AbstractMicroTestCase {

	@Test
	@Category({ MicroTest.class })
	public void testEvent_NoLateness() throws Exception {

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

		final DryDockEvent event1 = cargoModelBuilder.makeDryDockEvent("drydock1", LocalDateTime.of(2015, 12, 1, 0, 0, 0), LocalDateTime.of(2015, 12, 1, 0, 0, 0), port1) //
				.withDurationInDays(1) //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		final DryDockEvent event2 = cargoModelBuilder
				.makeDryDockEvent("drydock2", LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100), LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100), port2) //
				.withDurationInDays(1) //
				.withVesselAssignment(vesselAvailability, 2) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final VesselEventVisit visit1 = MicroTestUtils.findVesselEventVisit(event1, lngScenarioModel);
			final PortVisitLateness lateness1 = visit1.getLateness();
			Assert.assertNull(lateness1);

			final VesselEventVisit visit2 = MicroTestUtils.findVesselEventVisit(event2, lngScenarioModel);
			final PortVisitLateness lateness2 = visit2.getLateness();
			Assert.assertNull(lateness2);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testEvent_WithLateness() throws Exception {

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

		final DryDockEvent event1 = cargoModelBuilder.makeDryDockEvent("drydock1", LocalDateTime.of(2015, 12, 1, 0, 0, 0), LocalDateTime.of(2015, 12, 1, 0, 0, 0), port1) //
				.withDurationInDays(1) //
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		// Will be 1 hour late
		final DryDockEvent event2 = cargoModelBuilder
				.makeDryDockEvent("drydock2", LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 99), LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 99), port2) //
				.withDurationInDays(1) //
				.withVesselAssignment(vesselAvailability, 2) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final VesselEventVisit visit1 = MicroTestUtils.findVesselEventVisit(event1, lngScenarioModel);
			final PortVisitLateness lateness1 = visit1.getLateness();
			Assert.assertNull(lateness1);

			final VesselEventVisit visit2 = MicroTestUtils.findVesselEventVisit(event2, lngScenarioModel);
			final PortVisitLateness lateness2 = visit2.getLateness();
			Assert.assertNotNull(lateness2);
			Assert.assertEquals(1, lateness2.getLatenessInHours());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testCargo_NoLateness() throws Exception {

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
				.withWindowSize(0) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

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
	public void testCargo_WithLateness() throws Exception {

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

		final LocalDateTime dischargeDate = LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 99);

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", LocalDate.of(2015, 12, 1), port1, null, entity, "5") //
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final SlotVisit visit1 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(0), lngScenarioModel);
			final PortVisitLateness lateness1 = visit1.getLateness();
			Assert.assertNull(lateness1);

			final SlotVisit visit2 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(1), lngScenarioModel);
			final PortVisitLateness lateness2 = visit2.getLateness();
			Assert.assertNotNull(lateness2);
			Assert.assertEquals(1, lateness2.getLatenessInHours());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testCargo_WithLatenessInFlex() throws Exception {

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

		final LocalDateTime dischargeDate = LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 99);

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", LocalDate.of(2015, 12, 1), port1, null, entity, "5") //
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowFlex(1) //
				.withWindowSize(0) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final SlotVisit visit1 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(0), lngScenarioModel);
			final PortVisitLateness lateness1 = visit1.getLateness();
			Assert.assertNull(lateness1);

			// Still 1 hour late
			final SlotVisit visit2 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(1), lngScenarioModel);
			final PortVisitLateness lateness2 = visit2.getLateness();
			Assert.assertNotNull(lateness2);
			Assert.assertEquals(1, lateness2.getLatenessInHours());
		});
	}
}