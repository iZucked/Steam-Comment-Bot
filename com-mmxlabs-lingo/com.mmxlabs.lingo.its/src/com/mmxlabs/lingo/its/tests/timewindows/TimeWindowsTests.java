/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.timewindows;

import java.net.URL;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.tests.LiNGOTestDataProvider;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class TimeWindowsTests extends AbstractOptimisationResultTester {

	@Ignore
	@Test
	public void LDDD1() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/time-windows/L D D D 1.lingo");
		Assert.assertNotNull(url);
		runScenarioWithGCO(new LiNGOTestDataProvider(url), runner -> {
			Assert.assertNotNull(runner);
			Schedule schedule = runner.getSchedule();
			Assert.assertNotNull(schedule);

			checkLoadAndDischargeTime(schedule, 3, 4);
		});
	}

	@Ignore
	@Test
	public void LDDD2() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/time-windows/L D D D 2.lingo");
		Assert.assertNotNull(url);
		runScenarioWithGCO(new LiNGOTestDataProvider(url), runner -> {
			Assert.assertNotNull(runner);
			Schedule schedule = runner.getSchedule();
			Assert.assertNotNull(schedule);

			checkLoadAndDischargeTime(schedule, 3, 5);

		});

	}

	@Ignore
	@Test
	public void LDDL1() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/time-windows/L D D L 1.lingo");
		Assert.assertNotNull(url);
		runScenarioWithGCO(new LiNGOTestDataProvider(url), runner -> {

			Assert.assertNotNull(runner);
			Schedule schedule = runner.getSchedule();
			Assert.assertNotNull(schedule);

			checkLoadAndDischargeTime(schedule, 3, 5);
		});

	}

	@Ignore
	@Test
	public void LDDL2() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/time-windows/L D D L 2.lingo");
		Assert.assertNotNull(url);
		runScenarioWithGCO(new LiNGOTestDataProvider(url), runner -> {
			Assert.assertNotNull(runner);
			Schedule schedule = runner.getSchedule();
			Assert.assertNotNull(schedule);
			checkLoadAndDischargeTime(schedule, 4, 4);
		});

	}

	@Ignore
	@Test
	public void simpleLD() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/time-windows/simpleLD.lingo");
		Assert.assertNotNull(url);
		runScenarioWithGCO(new LiNGOTestDataProvider(url), runner -> {
			Assert.assertNotNull(runner);
			Schedule schedule = runner.getSchedule();
			Assert.assertNotNull(schedule);

			checkLoadAndDischargeTime(schedule, 4, 4);
		});
	}

	private void checkLoadAndDischargeTime(Schedule schedule, int loadMonth, int dischargeMonth) {
		LoadSlot l = null;
		DischargeSlot d = null;
		CargoAllocation allocation = null;

		for (CargoAllocation cargoA : schedule.getCargoAllocations()) {
			Cargo cargo = cargoA.getInputCargo();
			if (cargo != null) {
				for (Slot s : cargo.getSlots()) {
					if (s instanceof LoadSlot) {
						l = (LoadSlot) s;
						allocation = cargoA;
					} else if (s instanceof DischargeSlot) {
						d = (DischargeSlot) s;
					}
				}
			}
		}

		Assert.assertNotNull(allocation);
		for (Event event : allocation.getEvents()) {
			if (event instanceof SlotVisit) {
				SlotVisit sv = (SlotVisit) event;
				if (sv.getSlotAllocation().getSlot() == l) {
					Assert.assertTrue(sv.getStart().getMonth().getValue() == loadMonth);
				} else if (sv.getSlotAllocation().getSlot() == d) {
					Assert.assertTrue(sv.getStart().getMonth().getValue() == dischargeMonth);
				}
			}
		}
	}
}
