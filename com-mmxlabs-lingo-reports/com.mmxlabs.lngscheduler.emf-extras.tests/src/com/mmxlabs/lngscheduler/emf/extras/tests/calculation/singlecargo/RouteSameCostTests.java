/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.calculation.singlecargo;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.TimeUnitConvert;

import scenario.Scenario;
import scenario.fleet.VesselClassCost;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?209">Case 209: Equally priced routes</a>
 * <p>
 * Test behaviour when there are two cheapest routes with identical prices. The order doesn't matter so long as the price is the same.
 * <p>
 * Should test that for at least two identically priced routes they are the same price and are chosen over more expensive routes.
 * 
 * @author Adam Semenenko
 * 
 */
public class RouteSameCostTests {

	/**
	 * If a canal and ocean route have the same price then test to see that one is chosen (over more expensive routes), and they are definitely the same price.
	 */
	@Test
	public void testCanalOceanSameCost() {

		final String testName = "Ocean and canal same price because of fee.";

		final int cheapestDistance = 1000;
		final int expensiveDistance1 = 1100;
		final int expensiveDistance2 = 1200;

		final int[] portDistances = { cheapestDistance, expensiveDistance1, expensiveDistance2 };

		final String canalName = "Canal 1";
		final int canalDistance = 900;
		final int canalCost = 50;
		final int canalTransitFuelPerDay = TimeUnitConvert.convertPerHourToPerDay(0);
		final int canalTransitTime = 0;

		final VesselClassCost[] canalCosts = { ScenarioTools.createCanalAndCost(canalName, canalDistance, canalDistance, canalCost, canalCost, canalTransitFuelPerDay, canalTransitTime) };

		// the NBO rate is set as the idle consumption rate, so it costs lets to idle than travel. The canal route will allow idle time, but the fee will make the canal the same price as the longer
		// ocean route.
		final int fuelTravelConsumptionPerHour = 10;
		final int NBORatePerHour = 5;

		CargoAllocation a = testEquallyPricedRoutes(testName, portDistances, canalCosts, fuelTravelConsumptionPerHour, NBORatePerHour);

		// check same price
		final long canalPrice = getPriceOfCanal(testName, canalCosts[0], fuelTravelConsumptionPerHour, NBORatePerHour);
		final long oceanPrice = getPriceOfOcean(testName, cheapestDistance, fuelTravelConsumptionPerHour, NBORatePerHour);
		Assert.assertTrue("Cheapest routes are same price", canalPrice == oceanPrice);

		// check the vessel took either of the cheapest routes.
		final boolean ballastCheapestOceanRouteOrCanal = a.getBallastLeg().getDistance() == cheapestDistance || a.getBallastLeg().getRoute().equals(canalName);
		final boolean ladenCheapestOceanRouteOrCanal = a.getLadenLeg().getDistance() == cheapestDistance || a.getLadenLeg().getRoute().equals(canalName);

		Assert.assertTrue("Vessel took one of cheapest routes", ballastCheapestOceanRouteOrCanal && ladenCheapestOceanRouteOrCanal);
	}

	/**
	 * Canal is twice the distance of a sea route. Fuel consumption and NBO rates are all identical. Canal has no fees. This means the two routes cost the same amount.
	 * <p>
	 * Note: The ocean route is arguably 'better' though, since it plays it safe and arrives earlier for the same price. Because both are the same price either is fine, however.
	 */
	@Test
	public void canalTwiceDistance() {

		final String testName = "Canal twice the distance of the ocean route";

		final int cheapestDistance = 1000;
		final int expensiveDistance1 = 4000;
		final int expensiveDistance2 = 5000;

		final int[] portDistances = { cheapestDistance, expensiveDistance1, expensiveDistance2 };

		final String canalName = "Canal 1";
		final int canalDistance = 2000;
		final int canalCost = 0;
		final int canalTransitFuelPerDay = TimeUnitConvert.convertPerHourToPerDay(0);
		final int canalTransitTime = 0;

		final VesselClassCost[] canalCosts = { ScenarioTools.createCanalAndCost(canalName, canalDistance, canalDistance, canalCost, canalCost, canalTransitFuelPerDay, canalTransitTime) };

		// use the same fuel consumption for every travel
		final int fuelConsumptionHours = 10;
		final int NBORateHours = 10;

		CargoAllocation a = testEquallyPricedRoutes(testName, portDistances, canalCosts, fuelConsumptionHours, NBORateHours);

		// check same price
		final long canalPrice = getPriceOfCanal(testName, canalCosts[0], fuelConsumptionHours, NBORateHours);
		final long oceanPrice = getPriceOfOcean(testName, cheapestDistance, fuelConsumptionHours, NBORateHours);
		Assert.assertTrue("Cheapest routes are same price", canalPrice == oceanPrice);

		// check the vessel took either of the cheapest routes.
		final boolean ballastCheapestOceanRouteOrCanal = a.getBallastLeg().getDistance() == cheapestDistance || a.getBallastLeg().getRoute().equals(canalName);
		final boolean ladenCheapestOceanRouteOrCanal = a.getLadenLeg().getDistance() == cheapestDistance || a.getLadenLeg().getRoute().equals(canalName);

		Assert.assertTrue("Vessel took one of cheapest routes", ballastCheapestOceanRouteOrCanal && ladenCheapestOceanRouteOrCanal);
	}

	/**
	 * Run and evalutate a scenario with multiple ocean and canal routes.
	 * 
	 * @param testName
	 *            The name of the test to run (for console output).
	 * @param portDistances
	 *            An array of distances between the ports.
	 * @param canalCosts
	 *            An array of costs for each canal.
	 * @return The result of running the scenario.
	 */
	private CargoAllocation testEquallyPricedRoutes(final String testName, final int[] distancesBetweenPorts, VesselClassCost[] canalCosts, final int fuelTravelConsumptionPerHour,
			final int NBORatePerHour) {

		final int travelTime = 200;
		final float baseFuelUnitPrice = 1;
		final float equivalenceFactor = 1;
		final float dischargePrice = 1;
		final float cvValue = 1;
		final int minHeelVolume = 0;

		final int speed = 10;
		final int capacity = 1000000;

		final int fuelTravelConsumptionPerDay = TimeUnitConvert.convertPerHourToPerDay(fuelTravelConsumptionPerHour);
		final int NBOTravelRatePerDay = TimeUnitConvert.convertPerHourToPerDay(NBORatePerHour);

		// idle consumption and NBO rates are the same for every idle.
		final int NBOIdleRatePerDay = TimeUnitConvert.convertPerHourToPerDay(NBORatePerHour);
		final int fuelIdleConsumptionPerDay = NBOIdleRatePerDay;

		final boolean useDryDock = true;
		final int pilotLightRate = 0;

		final int minSpeed = speed;
		final int maxSpeed = speed;
		final int ballastMinSpeed = speed;
		final int ballastMinConsumption = NBOTravelRatePerDay;
		final int ballastMaxSpeed = speed;
		final int ballastMaxConsumption = fuelTravelConsumptionPerDay;
		final int ballastIdleConsumptionRate = fuelIdleConsumptionPerDay;
		final int ballastIdleNBORate = NBOIdleRatePerDay;
		final int ballastNBORate = NBOTravelRatePerDay;
		final int ladenMinSpeed = speed;
		final int ladenMinConsumption = fuelTravelConsumptionPerDay;
		final int ladenMaxSpeed = speed;
		final int ladenMaxConsumption = fuelTravelConsumptionPerDay;
		final int ladenIdleConsumptionRate = fuelIdleConsumptionPerDay;
		final int ladenIdleNBORate = NBOIdleRatePerDay;
		final int ladenNBORate = NBOTravelRatePerDay;

		Scenario canalScenario = ScenarioTools.createScenarioWithCanals(distancesBetweenPorts, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, minSpeed, maxSpeed, capacity,
				ballastMinSpeed, ballastMinConsumption, ballastMaxSpeed, ballastMaxConsumption, ballastIdleConsumptionRate, ballastIdleNBORate, ballastNBORate, ladenMinSpeed, ladenMinConsumption,
				ladenMaxSpeed, ladenMaxConsumption, ladenIdleConsumptionRate, ladenIdleNBORate, ladenNBORate, useDryDock, pilotLightRate, minHeelVolume, canalCosts);
		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(canalScenario);
		// check result is how we expect it to be
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		ScenarioTools.printCargoAllocation(testName, a);

		return a;
	}

	/**
	 * Get the price of a canal by testing it against the longest ocean route possible.
	 * 
	 * @param testName
	 *            The test the price is needed for (for console output).
	 * @param canalCost
	 *            The canal to get the price.
	 * @return The total cost for the route, including idles.
	 */
	private long getPriceOfCanal(final String testName, final VesselClassCost canalCost, final int fuelTravelConsumptionPerHour, final int NBORatePerHour) {
		CargoAllocation a = testEquallyPricedRoutes(testName + ": get canal price", new int[] { 10000 }, new VesselClassCost[] { canalCost }, fuelTravelConsumptionPerHour, NBORatePerHour);

		return a.getTotalCost();
	}

	/**
	 * Get the price of an ocean route by evaluating it.
	 * 
	 * @param testName
	 *            The test the price is needed for (for console output).
	 * @param distance
	 *            The distance of the ocean route
	 * @return The total cost for the route, including idles.
	 */
	private long getPriceOfOcean(final String testName, final int distance, final int fuelTravelConsumptionPerHour, final int NBORatePerHour) {
		CargoAllocation a = testEquallyPricedRoutes(testName + ": get ocean price", new int[] { distance }, null, fuelTravelConsumptionPerHour, NBORatePerHour);

		return a.getTotalCost();

	}

}
