/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.csv.microcases;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.utils.CSVImporter;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.transformer.its.tests.ScenarioRunner;
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
	public void spotDESPurchaseTimezoneBoundaryTest() throws IOException, InterruptedException, IncompleteScenarioException, URISyntaxException {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/micro-cases/spot-slot-timezone/");

		final LNGScenarioModel scenario = CSVImporter.importCSVScenario(url.toString());

		final ScenarioRunner runner = runScenario(scenario, new URL(url.toString() + "fitness"));
		runner.updateScenario();
		final Schedule schedule = runner.getFinalSchedule();

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

	}

	// Make sure the spot slot is on jan 1st, midnight in local timezone
	private void checkCargoAllocation(final CargoAllocation cargoAllocation) {
		for (final SlotAllocation slotAllcocation : cargoAllocation.getSlotAllocations()) {
			if (slotAllcocation.getSlot() instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slotAllcocation.getSlot();
				final Calendar cal = Calendar.getInstance();
				cal.setTime(loadSlot.getWindowStartWithSlotOrPortTime());
				final Port port = loadSlot.getPort();
				cal.setTimeZone(TimeZone.getTimeZone(port.getTimeZone()));
				Assert.assertEquals(Calendar.JANUARY, cal.get(Calendar.MONTH));
				Assert.assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
				Assert.assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
				Assert.assertEquals(0, cal.get(Calendar.MINUTE));
				Assert.assertEquals(0, cal.get(Calendar.SECOND));
				Assert.assertEquals(0, cal.get(Calendar.MILLISECOND));
				return;
			}
		}
		Assert.fail("No spot slot found");
	}
}
