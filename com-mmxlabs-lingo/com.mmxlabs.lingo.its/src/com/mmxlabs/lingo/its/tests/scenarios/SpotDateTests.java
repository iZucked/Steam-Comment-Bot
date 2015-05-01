/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.scenarios;

import java.net.URL;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;

public class SpotDateTests extends AbstractOptimisationResultTester {

	@Test
	public void testSpotDates() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/spot-test-case/spot-date-test-case.lingo");
		final LNGScenarioRunner runner = evaluateScenario(url);

		Assert.assertNotNull(runner);
		runner.run();

		// Should be the same as the updateScenario as we have only called ScenarioRunner#init()
		final Schedule schedule = runner.getFinalSchedule();
		Assert.assertNotNull(schedule);
		{
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocationByDischargeID("D1", schedule);
			Assert.assertNotNull(cargoAllocation);

			for (final SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
				final Slot s = sa.getSlot();
				if (s instanceof SpotLoadSlot) {
					final SpotLoadSlot spotLoadSlot = (SpotLoadSlot) s;
					final Calendar cal = Calendar.getInstance();
					cal.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
					cal.clear();
					cal.set(Calendar.YEAR, 2014);
					cal.set(Calendar.MONTH, Calendar.MAY);
					cal.set(Calendar.DAY_OF_MONTH, 1);
					Assert.assertEquals(cal.getTime(), spotLoadSlot.getWindowStartWithSlotOrPortTime());
				}
			}
		}
		{
			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocationByDischargeID("D2", schedule);
			Assert.assertNotNull(cargoAllocation);

			for (final SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
				final Slot s = sa.getSlot();
				if (s instanceof SpotLoadSlot) {
					final SpotLoadSlot spotLoadSlot = (SpotLoadSlot) s;
					final Calendar cal = Calendar.getInstance();
					cal.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
					cal.clear();
					cal.set(Calendar.YEAR, 2014);
					cal.set(Calendar.MONTH, Calendar.JUNE);
					cal.set(Calendar.DAY_OF_MONTH, 1);
					Assert.assertEquals(cal.getTime(), spotLoadSlot.getWindowStartWithSlotOrPortTime());
				}
			}
		}
	}

}
