/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.scenarios;

import java.net.URL;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.tests.LNGScenarioRunnerCreator;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;

@RunWith(value = ShiroRunner.class)
public class SpotDateTests extends AbstractOptimisationResultTester {

	@Test
	@Category(MicroTest.class)
	public void testSpotDates() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/spot-test-case/spot-date-test-case.lingo");

		LNGScenarioRunnerCreator.withLiNGOFileLegacyOptimisationRunner(url, null, 10_000, runner -> {

			runner.run();

			// Should be the same as the updateScenario as we have only called ScenarioRunner#init()
			final Schedule schedule = runner.getSchedule();
			Assert.assertNotNull(schedule);
			{
				final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocationByDischargeID("D1", schedule);
				Assert.assertNotNull(cargoAllocation);

				for (final SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
					final Slot s = sa.getSlot();
					if (s instanceof SpotLoadSlot) {
						final SpotLoadSlot spotLoadSlot = (SpotLoadSlot) s;
						Assert.assertEquals(LocalDate.of(2014, 5, 1), spotLoadSlot.getWindowStart());
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
						Assert.assertEquals(LocalDate.of(2014, 6, 1), spotLoadSlot.getWindowStart());
					}
				}
			}
		});
	}
}
