/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.timewindows;

import java.net.URL;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.tests.LiNGOTestDataProvider;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class TimeWindowsTests extends AbstractOptimisationResultTester {

	@Disabled
	@Test
	public void LDDD1() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/time-windows/L D D D 1.lingo");
		Assertions.assertNotNull(url);
		runScenarioWithGCO(new LiNGOTestDataProvider(url), runner -> {
			Assertions.assertNotNull(runner);
			final Schedule schedule = runner.getSchedule();
			Assertions.assertNotNull(schedule);

			checkLoadAndDischargeTime(schedule, 3, 4);
		});
	}

	@Disabled
	@Test
	public void LDDD2() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/time-windows/L D D D 2.lingo");
		Assertions.assertNotNull(url);
		runScenarioWithGCO(new LiNGOTestDataProvider(url), runner -> {
			Assertions.assertNotNull(runner);
			final Schedule schedule = runner.getSchedule();
			Assertions.assertNotNull(schedule);

			checkLoadAndDischargeTime(schedule, 3, 5);

		});

	}

	@Disabled
	@Test
	public void LDDL1() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/time-windows/L D D L 1.lingo");
		Assertions.assertNotNull(url);
		runScenarioWithGCO(new LiNGOTestDataProvider(url), runner -> {

			Assertions.assertNotNull(runner);
			final Schedule schedule = runner.getSchedule();
			Assertions.assertNotNull(schedule);

			checkLoadAndDischargeTime(schedule, 3, 5);
		});

	}

	@Disabled
	@Test
	public void LDDL2() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/time-windows/L D D L 2.lingo");
		Assertions.assertNotNull(url);
		runScenarioWithGCO(new LiNGOTestDataProvider(url), runner -> {
			Assertions.assertNotNull(runner);
			final Schedule schedule = runner.getSchedule();
			Assertions.assertNotNull(schedule);
			checkLoadAndDischargeTime(schedule, 4, 4);
		});

	}

	@Disabled
	@Test
	public void simpleLD() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/time-windows/simpleLD.lingo");
		Assertions.assertNotNull(url);
		runScenarioWithGCO(new LiNGOTestDataProvider(url), runner -> {
			Assertions.assertNotNull(runner);
			final Schedule schedule = runner.getSchedule();
			Assertions.assertNotNull(schedule);

			checkLoadAndDischargeTime(schedule, 4, 4);
		});
	}

	private void checkLoadAndDischargeTime(final Schedule schedule, final int loadMonth, final int dischargeMonth) {
		LoadSlot l = null;
		DischargeSlot d = null;
		CargoAllocation allocation = null;

		for (final CargoAllocation cargoA : schedule.getCargoAllocations()) {
			for (final SlotAllocation sa : cargoA.getSlotAllocations()) {
				final Slot s = sa.getSlot();
				if (s instanceof LoadSlot) {
					l = (LoadSlot) s;
					allocation = cargoA;
				} else if (s instanceof DischargeSlot) {
					d = (DischargeSlot) s;
				}
			}
		}

		Assertions.assertNotNull(allocation);
		for (final Event event : allocation.getEvents()) {
			if (event instanceof SlotVisit) {
				final SlotVisit sv = (SlotVisit) event;
				if (sv.getSlotAllocation().getSlot() == l) {
					Assertions.assertTrue(sv.getStart().getMonth().getValue() == loadMonth);
				} else if (sv.getSlotAllocation().getSlot() == d) {
					Assertions.assertTrue(sv.getStart().getMonth().getValue() == dischargeMonth);
				}
			}
		}
	}
}
