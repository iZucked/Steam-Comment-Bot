/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.EVesselTankState;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;

@ExtendWith(ShiroRunner.class)
public class EvaluationSequencingTests extends AbstractMicroTestCase {

	/**
	 * Based on FB6402
	 * 
	 * This test checks that the evaluator correctly sequences a maintenance event before a cargo. Sequencing based on windows is ambiguous and having the maintenance scheduled after the cargo leads
	 * to lateness.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testOverlappingCargoAndEventOrderingCustomCargoDuration() {
		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_BARROW_ISLAND);
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_TOKYO_BAY);
		final Port maintenancePort = portFinder.findPortById(InternalDataConstants.PORT_GWANGYANG);

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_160);

		final VesselAvailability va = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("10000") //
				.withStartWindow(LocalDateTime.of(2022, 4, 24, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2022, 7, 31, 15, 0)) //
				.withStartHeel(2_000, 7_000, 23, "") //
				.withEndHeel(500, 500, EVesselTankState.MUST_BE_COLD, false) //
				.build();

		final LocalDateTime mStartAfter = LocalDateTime.of(2022, 5, 1, 0, 0);
		final LocalDateTime mStartBy = LocalDateTime.of(2022, 6, 15, 0, 0);
		final MaintenanceEvent maintenanceEvent = cargoModelBuilder.makeMaintenanceEvent("MaintenanceEvent", mStartAfter, mStartBy, maintenancePort) //
				.withDurationInDays(5) //
				.withVesselAssignment(va, 0) //
				.build();

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("Load", LocalDate.of(2022, 5, 29), loadPort, null, entity, "9.5") //
				.withVolumeLimits(125_000, 174_000, VolumeUnits.M3) //
				.withWindowStartTime(18) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.withVisitDuration(30) //
				.build() //
				.makeDESSale("Discharge", LocalDate.of(2022, 6, 1), dischargePort, null, entity, "10.26") //
				.withVolumeLimits(130_000, 180_000, VolumeUnits.M3) //
				.withWindowStartTime(0).withWindowSize(1, TimePeriod.MONTHS) //
				.withVisitDuration(144) //
				.build() //
				.withVesselAssignment(va, 0) //
				.build();

		Assertions.assertEquals(0, maintenanceEvent.getSequenceHint());
		Assertions.assertEquals(0, cargo.getSequenceHint());

		evaluateTest();
		final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);

		Assertions.assertNotNull(schedule);

		// Maintenance should come before cargo.
		Assertions.assertEquals(1, maintenanceEvent.getSequenceHint());
		Assertions.assertEquals(2, cargo.getSequenceHint());

		// Double check event sequence agrees with sequence hint
		boolean seenMaintenance = false;
		boolean seenLoad = false;
		boolean seenDischarge = false;

		Assertions.assertEquals(1, schedule.getSequences().size());
		for (final Event event : schedule.getSequences().get(0).getEvents()) {
			if (event instanceof StartEvent || event instanceof Idle || event instanceof Journey || event instanceof EndEvent) {
				continue;
			}
			if (event instanceof final VesselEventVisit vev) {
				Assertions.assertEquals(maintenanceEvent, vev.getVesselEvent());
				Assertions.assertFalse(seenLoad);
				Assertions.assertFalse(seenDischarge);
				Assertions.assertFalse(seenMaintenance);
				seenMaintenance = true;
			} else if (event instanceof final SlotVisit sv) {
				final Slot<?> slot = sv.getSlotAllocation().getSlot();
				if (slot instanceof final LoadSlot loadSlot) {
					Assertions.assertEquals(cargo.getSlots().get(0), loadSlot);
					Assertions.assertTrue(seenMaintenance);
					Assertions.assertFalse(seenLoad);
					Assertions.assertFalse(seenDischarge);
					seenLoad = true;
				} else {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
					Assertions.assertEquals(cargo.getSlots().get(1), dischargeSlot);
					Assertions.assertTrue(seenMaintenance);
					Assertions.assertTrue(seenLoad);
					Assertions.assertFalse(seenDischarge);
					seenDischarge = true;
				}
			} else {
				// Unexpected event
				Assertions.fail();
			}
		}
		Assertions.assertTrue(seenMaintenance);
		Assertions.assertTrue(seenLoad);
		Assertions.assertTrue(seenDischarge);
	}

	/**
	 * Based on FB6402
	 * 
	 * This test checks that the evaluator respects provided sequence hints. testOverlappingCargoAndEventOrderingCustomCargoDuration() should prove (when passing) that the maintenance event should
	 * precede the cargo. This test forces the maintenance event to follow the cargo by setting sequence hints.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testOverlappingCargoAndEventOrderingCustomCargoDurationForceOppositeSequenceHint() {
		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_BARROW_ISLAND);
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_TOKYO_BAY);
		final Port maintenancePort = portFinder.findPortById(InternalDataConstants.PORT_GWANGYANG);

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_160);

		final VesselAvailability va = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withCharterRate("10000") //
				.withStartWindow(LocalDateTime.of(2022, 4, 24, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2022, 7, 31, 15, 0)) //
				.withStartHeel(2_000, 7_000, 23, "") //
				.withEndHeel(500, 500, EVesselTankState.MUST_BE_COLD, false) //
				.build();

		final LocalDateTime mStartAfter = LocalDateTime.of(2022, 5, 1, 0, 0);
		final LocalDateTime mStartBy = LocalDateTime.of(2022, 6, 15, 0, 0);
		final MaintenanceEvent maintenanceEvent = cargoModelBuilder.makeMaintenanceEvent("MaintenanceEvent", mStartAfter, mStartBy, maintenancePort) //
				.withDurationInDays(5) //
				.withVesselAssignment(va, 2) //
				.build();

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("Load", LocalDate.of(2022, 5, 29), loadPort, null, entity, "9.5") //
				.withVolumeLimits(125_000, 174_000, VolumeUnits.M3) //
				.withWindowStartTime(18) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.withVisitDuration(30) //
				.build() //
				.makeDESSale("Discharge", LocalDate.of(2022, 6, 1), dischargePort, null, entity, "10.26") //
				.withVolumeLimits(130_000, 180_000, VolumeUnits.M3) //
				.withWindowStartTime(0).withWindowSize(1, TimePeriod.MONTHS) //
				.withVisitDuration(144) //
				.build() //
				.withVesselAssignment(va, 1) //
				.build();

		Assertions.assertEquals(2, maintenanceEvent.getSequenceHint());
		Assertions.assertEquals(1, cargo.getSequenceHint());

		evaluateTest();
		final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);

		Assertions.assertNotNull(schedule);

		// Maintenance should come before cargo.
		Assertions.assertEquals(2, maintenanceEvent.getSequenceHint());
		Assertions.assertEquals(1, cargo.getSequenceHint());

		// Double check event sequence agrees with sequence hint
		boolean seenMaintenance = false;
		boolean seenLoad = false;
		boolean seenDischarge = false;

		Assertions.assertEquals(1, schedule.getSequences().size());
		for (final Event event : schedule.getSequences().get(0).getEvents()) {
			if (event instanceof StartEvent || event instanceof Idle || event instanceof Journey || event instanceof EndEvent) {
				continue;
			}
			if (event instanceof final VesselEventVisit vev) {
				Assertions.assertEquals(maintenanceEvent, vev.getVesselEvent());
				Assertions.assertTrue(seenLoad);
				Assertions.assertTrue(seenDischarge);
				Assertions.assertFalse(seenMaintenance);
				seenMaintenance = true;
			} else if (event instanceof final SlotVisit sv) {
				final Slot<?> slot = sv.getSlotAllocation().getSlot();
				if (slot instanceof final LoadSlot loadSlot) {
					Assertions.assertEquals(cargo.getSlots().get(0), loadSlot);
					Assertions.assertFalse(seenMaintenance);
					Assertions.assertFalse(seenLoad);
					Assertions.assertFalse(seenDischarge);
					seenLoad = true;
				} else {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
					Assertions.assertEquals(cargo.getSlots().get(1), dischargeSlot);
					Assertions.assertFalse(seenMaintenance);
					Assertions.assertTrue(seenLoad);
					Assertions.assertFalse(seenDischarge);
					seenDischarge = true;
				}
			} else {
				// Unexpected event
				Assertions.fail();
			}
		}
		Assertions.assertTrue(seenMaintenance);
		Assertions.assertTrue(seenLoad);
		Assertions.assertTrue(seenDischarge);
	}
}