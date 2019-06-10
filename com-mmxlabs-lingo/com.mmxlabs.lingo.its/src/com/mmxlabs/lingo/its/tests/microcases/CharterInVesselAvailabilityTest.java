/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@ExtendWith(ShiroRunner.class)
public class CharterInVesselAvailabilityTest extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargo_CargoOnNonOptionalVessel() throws Exception {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		// Create the required basic elements
		final CharterInMarket charterInMarket_1 = createChartInMarket();

		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");

		@NonNull
		final Port port2 = portFinder.findPort("Dominion Cove Point LNG");

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);

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
				.withVesselAssignment(charterInMarket_1, 0, 0) 
				.build();

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final IScenarioDataProvider optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final Sequence sequence = ScenarioModelUtil.getScheduleModel(optimiserScenario).getSchedule().getSequences().get(0);
			final StartEvent startEvent = (StartEvent) sequence.getEvents().get(0);
			final Idle idle = (Idle) sequence.getEvents().get(1);
			//Is SlotVisit actually LoadEvent?
			final SlotVisit loadEvent = (SlotVisit)sequence.getEvents().get(2);
			
			Assertions.assertEquals(0, startEvent.getDuration());
			Assertions.assertEquals(0, idle.getDuration());
			
			//System.out.println("Slot visit = "+loadEvent);
			Assertions.assertEquals(0, loadEvent.getDuration());
						
			final SlotVisit visit1 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(0), lngScenarioModel);
			final PortVisitLateness lateness1 = visit1.getLateness();
			Assertions.assertNull(lateness1);

			final SlotVisit visit2 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(1), lngScenarioModel);
			final PortVisitLateness lateness2 = visit2.getLateness();
			Assertions.assertNull(lateness2);
		});
	}

	private CharterInMarket createChartInMarket() {
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 1);
		return charterInMarket_1;
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargo_CargoOnOptionalVessel() throws Exception {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final CharterInMarket charterInMarket_1 = createChartInMarket();

		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");

		@NonNull
		final Port port2 = portFinder.findPort("Dominion Cove Point LNG");

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);

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
				.withVesselAssignment(charterInMarket_1, 0, 0) 
				.build();

		evaluateTest(null, null, scenarioRunner -> {

			final SlotVisit visit1 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(0), lngScenarioModel);
			final PortVisitLateness lateness1 = visit1.getLateness();
			Assertions.assertNull(lateness1);

			final SlotVisit visit2 = MicroTestUtils.findSlotVisit(cargo.getSlots().get(1), lngScenarioModel);
			final PortVisitLateness lateness2 = visit2.getLateness();
			Assertions.assertNull(lateness2);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargo_NoCargoOnNonOptionalVessel() throws Exception {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final VesselAvailability vesselAvailability = this.createVesselAvailabilty(false);
		
		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");

		@NonNull
		final Port port2 = portFinder.findPort("Dominion Cove Point LNG");

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);

		final LocalDateTime dischargeDate = LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100);

		evaluateTest(null, null, scenarioRunner -> {

			List<Sequence> sequences = ScenarioModelUtil.getScheduleModel(scenarioRunner.getScenarioDataProvider()).getSchedule().getSequences();
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
	@Tag(TestCategories.MICRO_TEST)
	public void testCargo_NoCargoOnOptionalVessel() throws Exception {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		final VesselAvailability vesselAvailability = createVesselAvailabilty(true);

		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");

		@NonNull
		final Port port2 = portFinder.findPort("Dominion Cove Point LNG");

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);

		final LocalDateTime dischargeDate = LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100);

		evaluateTest(null, null, scenarioRunner -> {

			EList<Sequence> sequences = ScenarioModelUtil.getScheduleModel(scenarioRunner.getScenarioDataProvider()).getSchedule().getSequences();
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

	private VesselAvailability createVesselAvailabilty(boolean optionality) {
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 1);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withOptionality(optionality).build();
		return vesselAvailability;
	}

}