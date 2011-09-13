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
	 * Test that a canal is used if it's just shorter than the ocean route. No costs are associated with the canal (see {@link #testSimpleCanalDistance(String, int, int)).
	 */
	@Test
	public void testCanalShorter() {

		final String testName = "Canal shorter than ocean route.";
		final int distanceBetweenPorts = 1000;
		final int canalDistance = 999;

		CargoAllocation a = testSimpleCanalDistance(testName, canalDistance, distanceBetweenPorts);

		// TODO is there a better way to test a canal is used rather than testing the route name is the same as the canal name?
		Assert.assertTrue("Laden leg travels down canal", canalName.equals(a.getLadenLeg().getRoute()));
		Assert.assertTrue("Ballast leg travels down canal", canalName.equals(a.getBallastLeg().getRoute()));
	}

	/**
	 * Test that a canal isn't used if it's just longer than the ocean route. No costs are associated with the canal (see {@link #testSimpleCanalDistance(String, int, int)).
	 */
	@Test
	public void testCanalLonger() {

		final String testName = "Canal longer than ocean route.";
		final int distanceBetweenPorts = 1000;
		final int canalDistance = 1001;

		CargoAllocation a = testSimpleCanalDistance(testName, canalDistance, distanceBetweenPorts);

		Assert.assertFalse("Laden leg travels on open sea", canalName.equals(a.getLadenLeg().getRoute()));
		Assert.assertFalse("Ballast leg travels on open sea", canalName.equals(a.getBallastLeg().getRoute()));
	}

	/**
	 * Test the results of having different canal and ocean distances between the ports.
	 * <p>
	 * The canals cost nothing and incur no extra fuel costs, and take the same time to travel as the ocean.
	 * 
	 * @param testName
	 *            An name used to identify console output
	 * @param canalDistance
	 *            The distance between two ports when travelling by canal
	 * @param portDistance
	 *            The distance between two ports when travelling by ocean
	 * @return
	 */
	private CargoAllocation testSimpleCanalDistance(final String testName, final int canalDistance, final int portDistance) {

		final int canalLadenCost = 0;
		final int canalUnladenCost = 0;
		final int canalTransitFuel = 0;
		final int fuelConsumptionHours = 10;
		final int NBORateHours = 10;

		return testCanalCost(testName, portDistance, canalDistance, canalLadenCost, canalUnladenCost, canalTransitFuel, fuelConsumptionHours, NBORateHours);
	}

	/**
	 * Test that if a canal is actually cheaper (despite the fee for the canal) it is used. The canal fuel costs 0 but the fee is non-zero.
	 */
	@Test
	public void testCanalCheaperFee() {

		final String testName = "Canal cheaper than ocean route despite fee.";
		final int canalCost = 9;
		final int canalFuel = 0;

		CargoAllocation a = testCanalCost(testName, canalCost, canalFuel);

		Assert.assertTrue("Laden leg travels in canal", canalName.equals(a.getLadenLeg().getRoute()));
		Assert.assertTrue("Ballast leg travels in canal", canalName.equals(a.getBallastLeg().getRoute()));
	}

	/**
	 * If the ocean and canal cost the same then the vessel will travel on the canal. The canal fuel costs 0 but the fee is non-zero. TODO is this the correct behaviour?
	 */
	@Test
	public void testCanalOceanSameCost() {

		final String testName = "Ocean and canal same price because of fee.";
		final int canalCost = 10;
		final int canalFuel = 0;

		CargoAllocation a = testCanalCost(testName, canalCost, canalFuel);

		Assert.assertTrue("Laden leg travels on canal", canalName.equals(a.getLadenLeg().getRoute()));
		Assert.assertTrue("Ballast leg travels on canal", canalName.equals(a.getBallastLeg().getRoute()));
	}

	/**
	 * Test that if a canal is slightly more expensive (because of the fee for the canal) it is not used. The canal fuel costs 0 but the fee is non-zero.
	 */
	@Test
	public void testCanalMoreExpensiveFee() {

		final String testName = "Ocean route cheaper than canal because of fee.";
		final int canalCost = 11;
		final int canalFuel = 0;

		CargoAllocation a = testCanalCost(testName, canalCost, canalFuel);

		Assert.assertFalse("Laden leg travels on canal", canalName.equals(a.getLadenLeg().getRoute()));
		Assert.assertFalse("Ballast leg travels on canal", canalName.equals(a.getBallastLeg().getRoute()));
	}

	/**
	 * Test that if a canal is slightly more expensive (because of the fuel for the canal) it is not used. The canal fuel is non-zero but the fee is zero.
	 */
	@Test
	public void testCanalMoreExpensiveFuel() {

		final String testName = "Ocean route cheaper than canal.";
		final int canalCost = 0;
		final int canalFuel = 10;

		CargoAllocation a = testCanalCost(testName, canalCost, canalFuel);

		Assert.assertFalse("Laden leg travels on canal", canalName.equals(a.getLadenLeg().getRoute()));
		Assert.assertFalse("Ballast leg travels on canal", canalName.equals(a.getBallastLeg().getRoute()));
	}

	/**
	 * For testing whether the canal is/isn't used if it is cheaper/more expensive.
	 * <p>
	 * The distances are very small compared to the time available to ensure that the only reason for selection of a route is due to price, not avoiding lateness. The fuel required for idle and the
	 * idle NBO rate are both as low as possible to prevent the cost of idling preventing route choice.
	 */
	private CargoAllocation testCanalCost(final String testName, final int canalCost, final int canalFuel) {

		final int canalDistance = 90;
		final int portDistance = 100;

		final int fuelConsumptionHours = 10;
		final int NBORateHours = 10;

		return testCanalCost(testName, portDistance, canalDistance, canalCost, canalCost, canalFuel, fuelConsumptionHours, NBORateHours);
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
	 * @param fuelTravelConsumptionHour
	 *            The base fuel required whilst travelling per hour
	 * @param NBOTravelRateHours
	 *            The rate at which NBO occurs per hour
	 * @return
	 */
	private CargoAllocation testCanalCost(final String testName, final int distanceBetweenPorts, final int canalDistance, final int canalLadenCost, final int canalUnladenCost,
			final int canalTransitFuel, final int fuelTravelConsumptionHours, final int NBOTravelRateHours) {

		// Create a dummy scenario
		final int travelTime = 100;

		// for equality between canals and oceans and for simplicity set these to 1
		final float baseFuelUnitPrice = 1;
		final float equivalenceFactor = 1;
		final float dischargePrice = 1;
		final float cvValue = 1;
		// for simplicity all speeds, fuel and NBO consumptions and rates are equal
		final int speed = 10;
		final int capacity = 1000000;
 
		final int fuelTravelConsumptionDays = (int) TimeUnit.DAYS.toHours(fuelTravelConsumptionHours);
		final int NBOTravelRateDays = (int) TimeUnit.DAYS.toHours(NBOTravelRateHours);
		// Set the idle consumption/rates to 1 to minimise the cost of idle time affecting route choice
		final int fuelIdleConsumptionDays = 1;
		final int NBOIdleRateDays = 1;

		final boolean useDryDock = true;
		final int pilotLightRate = 0;

		// calculate the transit time in the canal so the speeds in and out of the canal are the same.
		final int canalTransitTime = canalDistance / speed;

		VesselClassCost canalCost = ScenarioTools.createCanalAndCost(canalName, canalDistance, canalDistance, canalLadenCost, canalUnladenCost, canalTransitFuel, canalTransitTime);

		Scenario canalScenario = ScenarioTools.createScenarioWithCanal(distanceBetweenPorts, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, speed, speed, capacity, speed,
				fuelTravelConsumptionDays, speed, fuelTravelConsumptionDays, fuelIdleConsumptionDays, NBOIdleRateDays, NBOTravelRateDays, speed, fuelTravelConsumptionDays, speed,
				fuelTravelConsumptionDays, fuelIdleConsumptionDays, NBOIdleRateDays, NBOTravelRateDays, useDryDock, pilotLightRate, canalCost);
		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(canalScenario);
		// check result is how we expect it to be
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		ScenarioTools.printScenario(testName, a);

		return a;
	}
}
