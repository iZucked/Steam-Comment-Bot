/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.calculation.singleEvent;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.FuelUsageAssertions;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;

/**
 * This class contains some whole-system tests which check the fuel choices on a single cargo.
 * 
 * TODO test canal selection; make fuel choice values more precise (find tipping points on paper and write test)
 * 
 * @author hinton
 * 
 */
@RunWith(value = ShiroRunner.class)
public class SimpleCalculationTest {

	// Create a dummy scenario
	private static final float baseFuelPriceCheap = 0; // $/MT -- very cheap, normally around 700
	private static final float baseFuelPriceNormal = 700; // $/MT -- normal
	@SuppressWarnings("unused")
	private static final float baseFuelPriceExpensive = 1000; // $/MT
	private static final float dischargePriceCheap = 1.0f; // $/mmbtu - cheap
	@SuppressWarnings("unused")
	private static final float dischargePriceNormal = 7.0f; // $/mmbtu // normal
	private static final float dischargePriceExpensive = 70.0f; // $/mmbtu // very expensive, normally around 7

	/**
	 * Test that the ballast leg and idle never use base fuel.
	 */
	@Test
	public void testLNGSelection() {
		// Create a dummy scenario
		final float baseFuelPrice = baseFuelPriceNormal; // $/MT -- normal
		final float dischargePrice = dischargePriceCheap; // $/mmbtu - cheap
		final float cvValue = 22.8f;
		final int travelTime = (int) TimeUnit.DAYS.toHours(3);

		final LNGScenarioModel scenario = createSimpleScenario(baseFuelPrice, dischargePrice, cvValue, travelTime);
		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);
		// check result is how we expect it to be
		// there will be a single cargo allocation for this cargo
		CargoAllocation cargoAllocation = result.getCargoAllocations().get(0);
		final SimpleCargoAllocation a = new SimpleCargoAllocation(cargoAllocation);
		ScenarioTools.printCargoAllocation("testLNGSelection", cargoAllocation);

		// on the laden leg we always use NBO; decision time is on the ballast leg
		FuelUsageAssertions.assertFuelNotUsed(a.getBallastLeg().getFuels(), Fuel.BASE_FUEL);
		FuelUsageAssertions.assertFuelUsed(a.getBallastLeg().getFuels(), Fuel.NBO, FuelUnit.M3);

		FuelUsageAssertions.assertFuelNotUsed(a.getBallastIdle().getFuels(), Fuel.BASE_FUEL);
		FuelUsageAssertions.assertFuelUsed(a.getBallastIdle().getFuels(), Fuel.NBO, FuelUnit.M3);
	}

	/**
	 * Test that the ballast leg and idle only use base.
	 */
	@Test
	public void testBaseSelection() {
		// Create a dummy scenario
		final float baseFuelPrice = baseFuelPriceCheap; // $/MT -- very cheap, normally around 700
		final float dischargePrice = dischargePriceExpensive; // $/mmbtu // very expensive, normally around 7
		final float cvValue = 22.8f;
		final int travelTime = (int) TimeUnit.DAYS.toHours(3);

		final LNGScenarioModel scenario = createSimpleScenario(baseFuelPrice, dischargePrice, cvValue, travelTime);
		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);
		// check result is how we expect it to be
		// there will be a single cargo allocation for this cargo
		CargoAllocation cargoAllocation = result.getCargoAllocations().get(0);
		final SimpleCargoAllocation a = new SimpleCargoAllocation(cargoAllocation);
		ScenarioTools.printCargoAllocation("testBaseSelection", cargoAllocation);

		// on the laden leg we always use NBO; decision time is on the ballast leg
		FuelUsageAssertions.assertFuelNotUsed(a.getBallastLeg().getFuels(), Fuel.NBO);
		FuelUsageAssertions.assertFuelNotUsed(a.getBallastLeg().getFuels(), Fuel.FBO);
		FuelUsageAssertions.assertFuelUsed(a.getBallastLeg().getFuels(), Fuel.BASE_FUEL, FuelUnit.MT);

		FuelUsageAssertions.assertFuelNotUsed(a.getBallastIdle().getFuels(), Fuel.BASE_FUEL);
		FuelUsageAssertions.assertFuelUsed(a.getBallastIdle().getFuels(), Fuel.NBO, FuelUnit.M3);
	}

	/**
	 * Create a simple scenario which contains a vessel + vessel class a couple of ports and a canal, etc.
	 * 
	 * Most tests will call this, and then change some price parameters in the result
	 * 
	 * @param dischargePrice
	 * 
	 * @return
	 */
	private LNGScenarioModel createSimpleScenario(final float baseFuelUnitPrice, final float dischargePrice, final float cvValue, final int travelTime) {

		// 'magic' numbers that could be set in the arguments.
		// vessel class
		final float equivalenceFactor = 0.5f;
		final int minSpeed = 12;
		final int maxSpeed = 20;
		final int capacity = 150000;
		final int pilotLightRate = 0;
		final int minHeelVolume = 0;
		// ballast
		final int ballastMinSpeed = 12;
		final int ballastMinConsumption = 80;
		final int ballastMaxSpeed = 20;
		final int ballastMaxConsumption = 200;
		final int ballastIdleConsumptionRate = 15;
		final int ballastIdleNBORate = 100;
		final int ballastNBORate = 180;
		// laden
		final int ladenMinSpeed = 12;
		final int ladenMinConsumption = 100;
		final int ladenMaxSpeed = 20;
		final int ladenMaxConsumption = 230;
		final int ladenIdleConsumptionRate = 15;
		final int ladenIdleNBORate = 180;
		final int ladenNBORate = 200;
		// ports
		final int distanceBetweenPorts = 1000;

		final boolean useDryDock = true;

		return ScenarioTools.createScenario(distanceBetweenPorts, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, minSpeed, maxSpeed, capacity, ballastMinSpeed,
				ballastMinConsumption, ballastMaxSpeed, ballastMaxConsumption, ballastIdleConsumptionRate, ballastIdleNBORate, ballastNBORate, ladenMinSpeed, ladenMinConsumption, ladenMaxSpeed,
				ladenMaxConsumption, ladenIdleConsumptionRate, ladenIdleNBORate, ladenNBORate, useDryDock, minHeelVolume, pilotLightRate);
	}
}
