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
 * Tests for <a href="https://mmxlabs.fogbugz.com/default.asp?184">FogBugz: Case 184</a>
 * 
 * Testing whether a canal is/isn't used depending on the distances, the canals transit time & fuel consumption, and the canal fee.
 * 
 * @author Adam Semenenko
 * 
 */
public class CanalBoundaryTests {

	private static final String canalName = "Suez canal";

	/**
	 * Test that a canal is used if it's just shorter than the ocean route. No costs are associated with the canal
	 * @see #testSimpleCanalDistance(String, int, int) testSimpleCanalDistance
	 */
	@Test
	public void testCanalShorter() {

		final String testName = "Canal shorter than ocean route.";
		final int distanceBetweenPorts = 1000;
		final int canalDistance = 999;

		CargoAllocation a = testSimpleCanalDistance(testName, canalDistance, distanceBetweenPorts);

		Assert.assertTrue("Laden leg travels down canal", canalName.equals(a.getLadenLeg().getRoute()));
		Assert.assertTrue("Ballast leg travels down canal", canalName.equals(a.getBallastLeg().getRoute()));
	}

	/**
	 * Test that a canal isn't used if it's just longer than the ocean route. No costs are associated with the canal.
	 * @see #testSimpleCanalDistance(String, int, int) testSimpleCanalDistance
	 */
	@Test
	public void testOceanShorter() {

		final String testName = "Ocean shorter than canal.";
		final int distanceBetweenPorts = 999;
		final int canalDistance = 1000;

		CargoAllocation a = testSimpleCanalDistance(testName, canalDistance, distanceBetweenPorts);

		Assert.assertTrue("Laden leg travels on open sea", ScenarioTools.defaultRouteName.equals(a.getLadenLeg().getRoute()));
		Assert.assertTrue("Ballast leg travels on open sea", ScenarioTools.defaultRouteName.equals(a.getBallastLeg().getRoute()));
	}

	/**
	 * Test that if the canal is longer then it isn't chosen.
	 * <p>
	 * Note: the canal has to be 10miles longer than the port distance due to rounding (1001 miles at 10mph takes 10hours, the same as 1000miles).
	 */
	@Test
	public void testCanalLonger() {

		final String testName = "Canal longer than ocean route.";
		final int distanceBetweenPorts = 1000;
		final int canalDistance = 1010;

		CargoAllocation a = testSimpleCanalDistance(testName, canalDistance, distanceBetweenPorts);

		Assert.assertTrue("Laden leg travels on open sea", ScenarioTools.defaultRouteName.equals(a.getLadenLeg().getRoute()));
		Assert.assertTrue("Ballast leg travels on open sea", ScenarioTools.defaultRouteName.equals(a.getBallastLeg().getRoute()));
	}

	/**
	 * Test the results of having different canal and ocean distances between the ports.
	 * <p>
	 * The canals have no fees or transit time, and the fuel consumption rate is the same for all routes/ballasts, but is less for idles (idles being cheaper gives incentive towards choosing the
	 * shortest/quickest route).
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
		final int canalTransitTime = 0;
		final int fuelTravelConsumptionDays = 10;
		final int fuelIdleConsumptionDays = 5;
		final int NBORateDays = 10;

		return testCanalCost(testName, portDistance, canalDistance, canalLadenCost, canalUnladenCost, fuelTravelConsumptionDays, canalTransitTime, fuelTravelConsumptionDays, fuelIdleConsumptionDays,
				NBORateDays, fuelIdleConsumptionDays);
	}

	/**
	 * Test that if a canal is actually cheaper (despite the fee for the canal) it is used. The canal fuel consumption is less than other routes but the fee is non-zero.
	 * 
	 * TODO Resolve this. Bug? The canal should be 10 cheaper than the ocean, but the ocean route is chosen. Spoke to Simon (15/09/11), track it down through the code, see why. Test that the cost
	 * being calculated for both routes correctly and find out where it's not working.
	 */
	@Test
	public void testCanalCheaperDespiteFee() {

		final String testName = "Canal cheaper than ocean route despite fee.";
		final int canalCost = 90;
		final int canalTranistFuel = 10;
		final int canalTransitTimeHours = 0;

		CargoAllocation a = testCanalCost(testName, canalCost, canalTranistFuel, canalTransitTimeHours);

		Assert.assertTrue("Laden leg travels in canal", canalName.equals(a.getLadenLeg().getRoute()));
		Assert.assertTrue("Ballast leg travels in canal", canalName.equals(a.getBallastLeg().getRoute()));
	}

	/**
	 * Test that if a canal is slightly more expensive (because of the fee for the canal) it is not used. The canal fuel costs 0 but the fee is non-zero.
	 */
	@Test
	public void testCanalMoreExpensiveFee() {

		final String testName = "Ocean route cheaper than canal because of fee.";
		final int canalCost = 101;
		final int canalFuel = 0;
		final int canalTransitTimeHours = 0;

		CargoAllocation a = testCanalCost(testName, canalCost, canalFuel, canalTransitTimeHours);

		Assert.assertTrue("Laden leg travels on canal", ScenarioTools.defaultRouteName.equals(a.getLadenLeg().getRoute()));
		Assert.assertTrue("Ballast leg travels on canal", ScenarioTools.defaultRouteName.equals(a.getBallastLeg().getRoute()));
	}

	/**
	 * Test that if a canal is slightly more expensive (because of the fuel for the canal transit time) and so it is not used. The fee is zero.
	 */
	@Test
	public void testCanalMoreExpensiveFuel() {

		final String testName = "Ocean route cheaper than canal because of canal fuel.";
		final int canalCost = 0;
		final int canalFuel = 21;
		final int canalTransitTimeHours = 5;

		CargoAllocation a = testCanalCost(testName, canalCost, canalFuel, canalTransitTimeHours);

		Assert.assertTrue("Laden leg travels on ocean", ScenarioTools.defaultRouteName.equals(a.getLadenLeg().getRoute()));
		Assert.assertTrue("Ballast leg travels on ocean", ScenarioTools.defaultRouteName.equals(a.getBallastLeg().getRoute()));
	}

	/**
	 * Test that if a canal is slightly cheaper (because of the fuel for the canal transit time) and so the ocean route is not used. The fee is zero.
	 */
	@Test
	public void testCanalCheaperFuel() {

		final String testName = "Ocean route cheaper than canal because of canal fuel.";
		final int canalCost = 0;
		final int canalFuel = 19;
		final int canalTransitTimeHours = 5;

		CargoAllocation a = testCanalCost(testName, canalCost, canalFuel, canalTransitTimeHours);

		Assert.assertTrue("Laden leg travels on ocean", ScenarioTools.defaultRouteName.equals(a.getLadenLeg().getRoute()));
		Assert.assertTrue("Ballast leg travels on ocean", ScenarioTools.defaultRouteName.equals(a.getBallastLeg().getRoute()));
	}

	/**
	 * For testing whether the canal is/isn't used if it is cheaper/more expensive.
	 * <p>
	 * The distances are very small compared to the time available to ensure that the only reason for selection of a route is due to price, not avoiding lateness. The fuel required for idle and the
	 * idle NBO rate are both as low as possible to prevent the cost of idling preventing route choice.
	 */
	private CargoAllocation testCanalCost(final String testName, final int canalCost, final int canalFuelDays, final int canalTransitTimeHours) {

		final int canalDistance = 900;
		final int portDistance = 1000;

		// use the same fuel consumption for every travel/idle/canal
		final int fuelConsumptionHours = 10;
		final int NBORateHours = 5;

		return testCanalCost(testName, portDistance, canalDistance, canalCost, canalCost, canalFuelDays, canalTransitTimeHours, fuelConsumptionHours, fuelConsumptionHours, NBORateHours, NBORateHours);
	}

	/**
	 * Tests a simple scenario with a canal. The canal's cost, distance and fuel can be set. Travel time is 200 hours, speed is 10mph.
	 * <p>
	 * The travel time of the ballast and laden voyages leave a minimum of 10 hours idle time to ensure that the cost of idle is always non-zero. If it's zero then it will affect the route choice
	 * (e.g. taking a longer router to avoid the potentially expensive idle costs).
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
			final int canalTransitFuelDays, final int canalTransitTime, final int fuelTravelConsumptionDays, final int fuelIdleConsumptionDays, final int NBOTravelRateDays, final int NBOIdleRateDays) {

		final int travelTime = 200;
		final float baseFuelUnitPrice = 1;
		final float equivalenceFactor = 1;
		final float dischargePrice = 1;
		final float cvValue = 1;

		final int speed = 10;
		final int capacity = 1000000;

		final int fuelTravelConsumptionHours = (int) TimeUnit.DAYS.toHours(fuelTravelConsumptionDays);
		final int NBOTravelRateHours = (int) TimeUnit.DAYS.toHours(NBOTravelRateDays);
		final int canalTransitFuelHours = (int) TimeUnit.DAYS.toHours(canalTransitFuelDays);

		final int fuelIdleConsumptionHours = (int) TimeUnit.DAYS.toHours(fuelIdleConsumptionDays);
		final int NBOIdleRateHours = (int) TimeUnit.DAYS.toHours(NBOIdleRateDays);

		final boolean useDryDock = true;
		final int pilotLightRate = 0;

		VesselClassCost canalCost = ScenarioTools.createCanalAndCost(canalName, canalDistance, canalDistance, canalLadenCost, canalUnladenCost, canalTransitFuelHours, canalTransitTime);

		Scenario canalScenario = ScenarioTools.createScenarioWithCanal(distanceBetweenPorts, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, speed, speed, capacity, speed,
				fuelTravelConsumptionHours, speed, fuelTravelConsumptionHours, fuelIdleConsumptionHours, NBOIdleRateHours, NBOTravelRateHours, speed, fuelTravelConsumptionHours, speed,
				fuelTravelConsumptionHours, fuelIdleConsumptionHours, NBOIdleRateHours, NBOTravelRateHours, useDryDock, pilotLightRate, canalCost);
		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(canalScenario);
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		ScenarioTools.printScenario(testName, a);

		return a;
	}
}
