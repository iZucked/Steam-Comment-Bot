/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.calculation.singleEvent;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.TimeUnitConvert;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.SimpleCargoAllocation;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;

/**
 * Tests for <a href="https://mmxlabs.fogbugz.com/default.asp?184">FogBugz: Case 184</a>
 * 
 * Testing whether a canal is/isn't used depending on the distances, the canals transit time & fuel consumption, and the canal fee.
 * 
 * @author Adam Semenenko
 * 
 */
@RunWith(value = ShiroRunner.class)
public class CanalBoundaryTest {

	private static final RouteOption canalOption = RouteOption.SUEZ;

	/**
	 * Test that a canal is used if it's just shorter than the ocean route. No costs are associated with the canal
	 * 
	 * @see #testSimpleCanalDistance(String, int, int) testSimpleCanalDistance
	 */
	@Test
	public void testCanalShorter() {

		final String testName = "Canal shorter than ocean route.";
		final int distanceBetweenPorts = 1000;
		final int canalDistance = 999;

		final SimpleCargoAllocation a = new SimpleCargoAllocation(testSimpleCanalDistance(testName, canalDistance, distanceBetweenPorts));

		Assert.assertTrue("Laden leg travels down canal", canalOption.equals(a.getLadenLeg().getRoute().getRouteOption()));
		Assert.assertTrue("Ballast leg travels down canal", canalOption.equals(a.getBallastLeg().getRoute().getRouteOption()));
	}

	/**
	 * Test that a canal isn't used if it's just longer than the ocean route. No costs are associated with the canal.
	 * 
	 * @see #testSimpleCanalDistance(String, int, int) testSimpleCanalDistance
	 */
	@Test
	public void testOceanShorter() {

		final String testName = "Ocean shorter than canal.";
		final int distanceBetweenPorts = 999;
		final int canalDistance = 1000;

		final SimpleCargoAllocation a = new SimpleCargoAllocation(testSimpleCanalDistance(testName, canalDistance, distanceBetweenPorts));

		Assert.assertTrue("Laden leg travels on open sea", RouteOption.DIRECT == a.getLadenLeg().getRoute().getRouteOption());
		Assert.assertTrue("Ballast leg travels on open sea", RouteOption.DIRECT == a.getBallastLeg().getRoute().getRouteOption());
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

		final SimpleCargoAllocation a = new SimpleCargoAllocation(testSimpleCanalDistance(testName, canalDistance, distanceBetweenPorts));

		Assert.assertTrue("Laden leg travels on open sea", RouteOption.DIRECT == a.getLadenLeg().getRoute().getRouteOption());
		Assert.assertTrue("Ballast leg travels on open sea", RouteOption.DIRECT == a.getBallastLeg().getRoute().getRouteOption());
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
		final int fuelTravelConsumptionPerHour = 10;
		final int fuelIdleConsumptionPerHour = 5;
		final int NBORatePerHour = 10;

		return testCanalCost(testName, portDistance, canalDistance, canalLadenCost, canalUnladenCost, fuelTravelConsumptionPerHour, canalTransitTime, fuelTravelConsumptionPerHour,
				fuelIdleConsumptionPerHour, NBORatePerHour, fuelIdleConsumptionPerHour);
	}

	/**
	 * Test that if a canal is actually cheaper (despite the fee for the canal) it is used. The canal fuel consumption is less than other routes but the fee is non-zero.
	 * <p>
	 * Travelling costs $10 per hour, idling costs $5 per hour (regardless of fuel, e-factor is 1).
	 * <p>
	 * Ocean route cost = $1000 travel fuel cost + $500 idle fuel cost = $1500<br>
	 * Canal route cost = $900 travel fuel cost + $550 idle fuel cost + $49 canal fee = $1499<br>
	 */
	@Test
	public void testCanalCheaperDespiteFee() {

		final String testName = "Canal cheaper than ocean route despite fee.";
		final int canalCost = 49; // 50 is equal cost, don't use as behaviour for equal cost is undefined.
		final int canalTranistFuel = 0;
		final int canalTransitTimePerHour = 0;

		final SimpleCargoAllocation a = new SimpleCargoAllocation(testCanalCost(testName, canalCost, canalTranistFuel, canalTransitTimePerHour));

		Assert.assertTrue("Laden leg travels in canal", RouteOption.SUEZ == a.getLadenLeg().getRoute().getRouteOption());
		Assert.assertTrue("Ballast leg travels in canal", RouteOption.SUEZ == a.getBallastLeg().getRoute().getRouteOption());
	}

	/**
	 * Test that if a canal is slightly more expensive (because of the fee for the canal) it is not used. There is no canal transit time.
	 */
	@Test
	public void testCanalMoreExpensiveFee() {

		final String testName = "Ocean route cheaper than canal because of fee.";
		final int canalCost = 101;
		final int canalFuelPerHour = 0;
		final int canalTransitTimeHours = 0;

		final SimpleCargoAllocation a = new SimpleCargoAllocation(testCanalCost(testName, canalCost, canalFuelPerHour, canalTransitTimeHours));

		Assert.assertTrue("Laden leg travels on canal", RouteOption.DIRECT == a.getLadenLeg().getRoute().getRouteOption());
		Assert.assertTrue("Ballast leg travels on canal", RouteOption.DIRECT == a.getBallastLeg().getRoute().getRouteOption());
	}

	/**
	 * Test that if a canal is slightly more expensive (because of the fuel for the canal transit time) and so it is not used. The fee is zero.
	 */
	@Test
	public void testCanalMoreExpensiveFuel() {

		final String testName = "Ocean route cheaper than canal because of canal fuel.";
		final int canalCost = 0;
		final int canalFuelPerHour = 21;
		final int canalTransitTimeHours = 5;

		final SimpleCargoAllocation a = new SimpleCargoAllocation(testCanalCost(testName, canalCost, canalFuelPerHour, canalTransitTimeHours));

		Assert.assertTrue("Laden leg travels on ocean", RouteOption.DIRECT == a.getLadenLeg().getRoute().getRouteOption());
		Assert.assertTrue("Ballast leg travels on ocean", RouteOption.DIRECT == a.getBallastLeg().getRoute().getRouteOption());
	}

	/**
	 * Test that if a canal is slightly cheaper (because of the fuel for the canal transit time) and so the ocean route is not used. The fee is zero.
	 */
	@Test
	public void testCanalCheaperFuel() {

		final String testName = "Ocean route cheaper than canal because of canal fuel.";
		final int canalCost = 0;
		final int canalFuelPerHour = 19;
		final int canalTransitTimeHours = 5;

		final SimpleCargoAllocation a = new SimpleCargoAllocation(testCanalCost(testName, canalCost, canalFuelPerHour, canalTransitTimeHours));

		Assert.assertTrue("Laden leg travels on ocean", RouteOption.DIRECT == a.getLadenLeg().getRoute().getRouteOption());
		Assert.assertTrue("Ballast leg travels on ocean", RouteOption.DIRECT == a.getBallastLeg().getRoute().getRouteOption());
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

		// use the same fuel consumption for laden and ballast travel/idle
		final int fuelTravelConsumptionPerHour = 10;
		final int NBORatePerHour = 5;
		final int fuelIdleConsumptionPerHour = 5;

		return testCanalCost(testName, portDistance, canalDistance, canalCost, canalCost, canalFuelDays, canalTransitTimeHours, fuelTravelConsumptionPerHour, fuelIdleConsumptionPerHour,
				NBORatePerHour, NBORatePerHour);
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
			final int canalTransitFuelPerHour, final int canalTransitTime, final int fuelTravelConsumptionPerHour, final int fuelIdleConsumptionPerHour, final int NBOTravelRatePerHour,
			final int NBOIdleRatePerHour) {

		final int travelTime = 200;
		final float baseFuelUnitPrice = 1;
		final float equivalenceFactor = 1;
		final float dischargePrice = 1;
		final float cvValue = 1;

		final int speed = 10;
		final int capacity = 1000000;

		final int fuelTravelConsumptionPerDay = TimeUnitConvert.convertPerHourToPerDay(fuelTravelConsumptionPerHour);
		final int NBOTravelRatePerDay = TimeUnitConvert.convertPerHourToPerDay(NBOTravelRatePerHour);
		final int canalTransitFuelPerDay = TimeUnitConvert.convertPerHourToPerDay(canalTransitFuelPerHour);

		final int fuelIdleConsumptionPerDay = TimeUnitConvert.convertPerHourToPerDay(fuelIdleConsumptionPerHour);
		final int NBOIdleRatePerDay = TimeUnitConvert.convertPerHourToPerDay(NBOIdleRatePerHour);

		final boolean useDryDock = true;
		final int pilotLightRate = 0;
		final int minHeelVolume = 0;

		final LNGScenarioModel canalScenario = ScenarioTools.createScenarioWithCanal(distanceBetweenPorts, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, speed, speed,
				capacity, speed, fuelTravelConsumptionPerDay, speed, fuelTravelConsumptionPerDay, fuelIdleConsumptionPerDay, NBOIdleRatePerDay, NBOTravelRatePerDay, speed, fuelTravelConsumptionPerDay,
				speed, fuelTravelConsumptionPerDay, fuelIdleConsumptionPerDay, NBOIdleRatePerDay, NBOTravelRatePerDay, useDryDock, pilotLightRate, minHeelVolume);

		CustomScenarioCreator.createCanalAndCost(canalScenario, canalOption, ScenarioTools.A, ScenarioTools.B, canalDistance, canalDistance, canalLadenCost, canalUnladenCost, canalTransitFuelPerDay,
				NBOTravelRatePerDay, canalTransitTime);

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(canalScenario);
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		ScenarioTools.printCargoAllocation(testName, a);

		return a;
	}
}
