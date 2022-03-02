/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.scenarios;

import java.net.URL;
import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.tests.LNGScenarioRunnerCreator;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;

@ExtendWith(ShiroRunner.class)
public class SpotDateTests extends AbstractOptimisationResultTester {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSpotDates() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/spot-test-case/spot-date-test-case.lingo");

		LNGScenarioRunnerCreator.withLiNGOFileLegacyOptimisationRunner(url, null, 10_000, runner -> {

			runner.runAndApplyBest();

			// Should be the same as the updateScenario as we have only called ScenarioRunner#init()
			final Schedule schedule = runner.getSchedule();
			Assertions.assertNotNull(schedule);
			assert schedule != null;
			{
				final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocationByDischargeID("D1", schedule);
				Assertions.assertNotNull(cargoAllocation);

				for (final SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
					final Slot<?> s = sa.getSlot();
					if (s instanceof SpotLoadSlot) {
						final SpotLoadSlot spotLoadSlot = (SpotLoadSlot) s;
						Assertions.assertEquals(LocalDate.of(2014, 5, 1), spotLoadSlot.getWindowStart());
					}
				}
			}
			{
				final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocationByDischargeID("D2", schedule);
				Assertions.assertNotNull(cargoAllocation);

				for (final SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
					final Slot<?> s = sa.getSlot();
					if (s instanceof SpotLoadSlot) {
						final SpotLoadSlot spotLoadSlot = (SpotLoadSlot) s;
						Assertions.assertEquals(LocalDate.of(2014, 6, 1), spotLoadSlot.getWindowStart());
					}
				}
			}
		});
	}
}
