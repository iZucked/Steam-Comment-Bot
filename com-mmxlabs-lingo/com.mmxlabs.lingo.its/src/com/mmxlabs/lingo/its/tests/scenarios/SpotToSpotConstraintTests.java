/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.scenarios;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.transformer.its.tests.ScenarioRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;

/**
 * Test Spot to Spot constraint.
 * @author achurchill
 *
 */
public class SpotToSpotConstraintTests extends AbstractOptimisationResultTester {

	@Test
	public void test_SpotToSpot_No_Spots() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Spot To Spot/No Spots.lingo");

		final ScenarioRunner runner = evaluateScenario(url);
		Assert.assertNotNull(runner);

		// Update the scenario with the Schedule links
		runner.updateScenario();

		// Should be the same as the updateScenario as we have only called ScenarioRunner#init()
		Schedule schedule = runner.getIntialSchedule();

		CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation("load-Cargo 7", schedule);
		Assert.assertNotNull(cargoAllocation);

		// Check cargo purchase and sales prices
		for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
			if (slotAllocation.getSlot() instanceof LoadSlot) {
				Assert.assertEquals(5.00, slotAllocation.getPrice(), 0.005);
				Assert.assertEquals("load-Cargo 7", slotAllocation.getSlot().getName());
			} else if (slotAllocation.getSlot() instanceof DischargeSlot) {
				Assert.assertEquals(10.0, slotAllocation.getPrice(), 0.005);
				Assert.assertEquals("discharge-Cargo 7", slotAllocation.getSlot().getName());
			}
		}
		
		// Optimise!
		runner.run();

		schedule = runner.getFinalSchedule();
		
		cargoAllocation = ScheduleTools.findCargoAllocation("load-Cargo 7", schedule);
		Assert.assertNotNull(cargoAllocation);

		// Check cargo purchase and sales prices
		for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
			if (slotAllocation.getSlot() instanceof LoadSlot) {
				Assert.assertEquals(5.00, slotAllocation.getPrice(), 0.005);
				Assert.assertEquals("load-Cargo 7", slotAllocation.getSlot().getName());
			} else if (slotAllocation.getSlot() instanceof DischargeSlot) {
				Assert.assertEquals(10.0, slotAllocation.getPrice(), 0.005);
				Assert.assertEquals("discharge-Cargo 7", slotAllocation.getSlot().getName());
			}
		}

	}

	@Test
	public void test_SpotToSpot_With_Spots() throws Exception {
		
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/demo-cases/Spot To Spot/With Spots.lingo");
		
		final ScenarioRunner runner = evaluateScenario(url);
		Assert.assertNotNull(runner);
		
		// Update the scenario with the Schedule links
		runner.updateScenario();
		
		// Should be the same as the updateScenario as we have only called ScenarioRunner#init()
		Schedule schedule = runner.getIntialSchedule();
		
		CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation("load-Cargo 7", schedule);
		Assert.assertNotNull(cargoAllocation);
		
		// Check cargo purchase and sales prices
		for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
			if (slotAllocation.getSlot() instanceof LoadSlot) {
				Assert.assertEquals(5.00, slotAllocation.getPrice(), 0.005);
				Assert.assertEquals("load-Cargo 7", slotAllocation.getSlot().getName());
			} else if (slotAllocation.getSlot() instanceof DischargeSlot) {
				Assert.assertEquals(10.0, slotAllocation.getPrice(), 0.005);
				Assert.assertEquals("discharge-Cargo 7", slotAllocation.getSlot().getName());
			}
		}
		
		// Optimise!
		runner.run();

		schedule = runner.getFinalSchedule();
		
		cargoAllocation = ScheduleTools.findCargoAllocation("load-Cargo 7", schedule);
		Assert.assertNotNull(cargoAllocation);

		// Check cargo purchase and sales prices
		for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
			if (slotAllocation.getSlot() instanceof LoadSlot) {
				Assert.assertEquals(5.00, slotAllocation.getPrice(), 0.005);
				Assert.assertEquals("load-Cargo 7", slotAllocation.getSlot().getName());
			} else if (slotAllocation.getSlot() instanceof DischargeSlot) {
				Assert.assertEquals(100.0, slotAllocation.getPrice(), 0.005);
				Assert.assertTrue(slotAllocation.getSlot().getName().matches("DSale(.*)"));
			}
		}

	}
}
