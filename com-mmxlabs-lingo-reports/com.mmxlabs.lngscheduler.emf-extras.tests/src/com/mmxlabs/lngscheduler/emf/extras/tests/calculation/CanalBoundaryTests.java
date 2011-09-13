/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.calculation;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import scenario.Scenario;
import scenario.fleet.VesselClassCost;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;

/**
 * Tests for case 184.
 * 
 * @author Adam Semenenko
 * 
 */
public class CanalBoundaryTests {

	private static final String canalName = "Suez canal";

	/**
	 * Test that a canal is used if it's just shorter than the ocean route.
	 */
	@Test
	public void testCanalShorter() {

		final String testName = "Canal shorter than ocean route.";
		final int distanceBetweenPorts = 1000;
		final int canalDistance = 999;

		CargoAllocation a = testCanalDistance(testName, canalDistance, distanceBetweenPorts);

		// TODO is there a better way to test a canal is used rather than testing the canal name?
		Assert.assertEquals("Laden leg travels down canal", canalName, a.getLadenLeg().getRoute());
		Assert.assertEquals("Ballast leg travels down canal", canalName, a.getBallastLeg().getRoute());
	}

	/**
	 * Test that a canal isn't used if it's just longer than the ocean route.
	 */
	@Test
	public void testCanalLonger() {

		final String testName = "Canal longer than ocean route.";
		final int distanceBetweenPorts = 1000;
		final int canalDistance = 1001;

		CargoAllocation a = testCanalDistance(testName, canalDistance, distanceBetweenPorts);

		Assert.assertNotSame("Laden leg travels on open sea", canalName, a.getLadenLeg().getRoute());
		Assert.assertNotSame("Ballast leg travels on open sea", canalName, a.getBallastLeg().getRoute());
	}

	/**
	 * Test the results of having different canal and ocean distances between the ports.
	 * <p>
	 * The canals cost nothing and incur no extra fuel costs.
	 * 
	 * @param testName
	 * @param canalDistance
	 * @param portDistance
	 * @return
	 */
	private CargoAllocation testCanalDistance(final String testName, final int canalDistance, final int portDistance) {

		final int canalLadenCost = 0;
		final int canalUnladenCost = 0;
		final int canalTransitFuel = 0;
		// final int canalTransitTime = 50;
		final float baseFuelUnitPrice = 1;
		final float dischargePrice = 1;
		final float cvValue = 1;
		final int fuelConsumptionHours = 10;
		final int NBORateHours = 10;

		return testCanalCost(testName, portDistance, canalDistance, canalLadenCost, canalUnladenCost, canalTransitFuel, baseFuelUnitPrice, dischargePrice, cvValue, fuelConsumptionHours, NBORateHours);
	}

	/**
	 * Tests a simple scenario with a canal. The canal's cost, distance and fuel can be set. The canal speed is set to be the same as the speed of the vessel in the ocean.
	 * 
	 * @param testName
	 *            The name of the test being run (to distinguish console output).
	 * @param distanceBetweenPorts
	 *            The distance between the ports on the ocean
	 * @param canalDistance
	 *            The distance between the ports using a canal.
	 * @param canalLadenCost
	 *            The cost of the canal if the vessel is laden in dollars
	 * @param canalUnladenCost
	 *            The cost of the canal if the vessel is unladen in dollars
	 * @param canalTransitFuel
	 *            MT of base fuel / day used when in transit
	 * @param baseFuelUnitPrice
	 * @param dischargePrice
	 * @param cvValue
	 * @param fuelConsumptionHours
	 * @param NBORateHours
	 * @return
	 */
	private CargoAllocation testCanalCost(final String testName, final int distanceBetweenPorts, final int canalDistance, final int canalLadenCost, final int canalUnladenCost,
			final int canalTransitFuel, final float baseFuelUnitPrice, final float dischargePrice, final float cvValue, final int fuelConsumptionHours, final int NBORateHours) {

		// Create a dummy scenario
		final int travelTime = 100;

		final float equivalenceFactor = 1;
		// for simplicity all speeds, fuel and NBO consumptions and rates are equal
		final int speed = 10;
		final int capacity = 1000000;

		final int fuelConsumptionDays = (int) TimeUnit.DAYS.toHours(fuelConsumptionHours);
		final int NBORateDays = (int) TimeUnit.DAYS.toHours(NBORateHours);

		final boolean useDryDock = true;
		final int pilotLightRate = 0;

		// calculate the transit time in the canal so the speeds in and out of the canal are the same.
		final int canalTransitTime = canalDistance / speed;

		VesselClassCost canalCost = ScenarioTools.createCanalAndCost(canalName, canalDistance, canalDistance, canalLadenCost, canalUnladenCost, canalTransitFuel, canalTransitTime);

		Scenario canalScenario = ScenarioTools.createScenarioWithCanal(distanceBetweenPorts, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, speed, speed, capacity, speed,
				fuelConsumptionDays, speed, fuelConsumptionDays, fuelConsumptionDays, NBORateDays, NBORateDays, speed, fuelConsumptionDays, speed, fuelConsumptionDays, fuelConsumptionDays,
				NBORateDays, NBORateDays, useDryDock, pilotLightRate, canalCost);
		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(canalScenario);
		// check result is how we expect it to be
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		ScenarioTools.printScenario(testName, a);

		return a;
	}
}
