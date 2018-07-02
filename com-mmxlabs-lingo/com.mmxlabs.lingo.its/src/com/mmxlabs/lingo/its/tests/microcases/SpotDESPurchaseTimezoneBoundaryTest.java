/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.tests.CSVTestDataProvider;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;

public class SpotDESPurchaseTimezoneBoundaryTest extends AbstractOptimisationResultTester {

	/**
	 * This test case has two Jan discharges and a DES purchase spot market limited to two per month. Ensure this limit is respected and timezone is correctly take into account.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IncompleteScenarioException
	 * @throws URISyntaxException
	 */
	@Test
	@Tag(TestCategories.QUICK_TEST)
	public void spotDESPurchaseTimezoneBoundaryTest() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/micro-cases/spot-slot-timezone/");

		runScenarioWithGCO(new CSVTestDataProvider(url), runner -> {

			// runner.updateScenario();
			final Schedule schedule = runner.getSchedule();
			Assertions.assertNotNull(schedule);

			// Expect only two spot cargoes in Jan
			Assertions.assertNotNull(ScheduleTools.findCargoAllocation("DESPurchaseMarket-2014-01-0", schedule));
			Assertions.assertNotNull(ScheduleTools.findCargoAllocation("DESPurchaseMarket-2014-01-1", schedule));

			// Should not expect a third Jan cargo
			Assertions.assertNull(ScheduleTools.findCargoAllocation("DESPurchaseMarket-2014-01-2", schedule));
			// Should not expect a cargo in Dec or Feb
			Assertions.assertNull(ScheduleTools.findCargoAllocation("DESPurchaseMarket-2013-12-0", schedule));
			Assertions.assertNull(ScheduleTools.findCargoAllocation("DESPurchaseMarket-2014-02-0", schedule));

			// Check the two valid cargoes
			checkCargoAllocation(ScheduleTools.findCargoAllocation("DESPurchaseMarket-2014-01-0", schedule));
			checkCargoAllocation(ScheduleTools.findCargoAllocation("DESPurchaseMarket-2014-01-1", schedule));
		});
	}

	// Make sure the spot slot is on jan 1st, midnight in local timezone
	private void checkCargoAllocation(final CargoAllocation cargoAllocation) {
		for (final SlotAllocation slotAllcocation : cargoAllocation.getSlotAllocations()) {
			if (slotAllcocation.getSlot() instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slotAllcocation.getSlot();
				final ZonedDateTime cal = loadSlot.getWindowStartWithSlotOrPortTime();
				final LocalDateTime localDateTime = cal.toLocalDateTime();
				Assertions.assertEquals(1, localDateTime.getMonthValue());
				Assertions.assertEquals(1, localDateTime.getDayOfMonth());
				Assertions.assertEquals(0, localDateTime.getHour());
				Assertions.assertEquals(0, localDateTime.getMinute());
				Assertions.assertEquals(0, localDateTime.getSecond());
				Assertions.assertEquals(0, localDateTime.getNano());
				return;
			}
		}
		Assertions.fail("No spot slot found");
	}
}
