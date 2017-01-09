/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.tests.CSVTestDataProvider;
import com.mmxlabs.lingo.its.tests.category.QuickTest;
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
	@Category(QuickTest.class)
	public void spotDESPurchaseTimezoneBoundaryTest() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/micro-cases/spot-slot-timezone/");

		runScenarioWithGCO(new CSVTestDataProvider(url), runner -> {

			// runner.updateScenario();
			final Schedule schedule = runner.getSchedule();
			Assert.assertNotNull(schedule);

			// Expect only two spot cargoes in Jan
			Assert.assertNotNull(ScheduleTools.findCargoAllocation("DESPurchaseMarket-2014-01-0", schedule));
			Assert.assertNotNull(ScheduleTools.findCargoAllocation("DESPurchaseMarket-2014-01-1", schedule));

			// Should not expect a third Jan cargo
			Assert.assertNull(ScheduleTools.findCargoAllocation("DESPurchaseMarket-2014-01-2", schedule));
			// Should not expect a cargo in Dec or Feb
			Assert.assertNull(ScheduleTools.findCargoAllocation("DESPurchaseMarket-2013-12-0", schedule));
			Assert.assertNull(ScheduleTools.findCargoAllocation("DESPurchaseMarket-2014-02-0", schedule));

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
				Assert.assertEquals(1, localDateTime.getMonthValue());
				Assert.assertEquals(1, localDateTime.getDayOfMonth());
				Assert.assertEquals(0, localDateTime.getHour());
				Assert.assertEquals(0, localDateTime.getMinute());
				Assert.assertEquals(0, localDateTime.getSecond());
				Assert.assertEquals(0, localDateTime.getNano());
				return;
			}
		}
		Assert.fail("No spot slot found");
	}
}
