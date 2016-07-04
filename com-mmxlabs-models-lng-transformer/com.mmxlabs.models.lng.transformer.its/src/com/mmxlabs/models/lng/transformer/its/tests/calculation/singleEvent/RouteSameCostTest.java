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
 * <a href="https://mmxlabs.fogbugz.com/default.asp?209">Case 209: Equally priced routes</a>
 * <p>
 * Test behaviour when there are two cheapest routes with identical prices. The order doesn't matter so long as the price is the same.
 * <p>
 * Should test that for at least two identically priced routes they are the same price and are chosen over more expensive routes.
 * 
 * @author Adam Semenenko
 * 
 */
@RunWith(value = ShiroRunner.class)
public class RouteSameCostTest {

	static class CanalParameters {

		public RouteOption canalOption;
		public int canalDistance;
		public int canalCost;
		public int canalTransitFuelPerDay;
		public int canalTransitTime;
		public int NBOTravelRatePerDay;
	}

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

		CanalParameters params = new CanalParameters();
		params.canalOption = RouteOption.SUEZ;
		params.canalDistance = 900;
		params.canalCost = 50;
		params.canalTransitFuelPerDay = TimeUnitConvert.convertPerHourToPerDay(0);
		params.canalTransitTime = 0;
		params.NBOTravelRatePerDay = 0;

		// the NBO rate is set as the idle consumption rate, so it costs lets to idle than travel. The canal route will allow idle time, but the fee will make the canal the same price as the longer
		// ocean route.
		final int fuelTravelConsumptionPerHour = 10;
		final int NBORatePerHour = 5;

		final SimpleCargoAllocation a = new SimpleCargoAllocation(testEquallyPricedRoutes(testName, portDistances, params, fuelTravelConsumptionPerHour, NBORatePerHour));

		// check same price
		final long canalPrice = getPriceOfCanal(testName, params, fuelTravelConsumptionPerHour, NBORatePerHour);
		final long oceanPrice = getPriceOfOcean(testName, cheapestDistance, fuelTravelConsumptionPerHour, NBORatePerHour);
		Assert.assertTrue("Cheapest routes are same price", canalPrice == oceanPrice);

		// check the vessel took either of the cheapest routes.
		final boolean ballastCheapestOceanRouteOrCanal = (a.getBallastLeg().getDistance() == cheapestDistance) || a.getBallastLeg().getRoute().getRouteOption().equals(params.canalOption);
		final boolean ladenCheapestOceanRouteOrCanal = (a.getLadenLeg().getDistance() == cheapestDistance) || a.getLadenLeg().getRoute().getRouteOption().equals(params.canalOption);

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

		CanalParameters params = new CanalParameters();
		params.canalOption = RouteOption.SUEZ;
		params.canalDistance = 2000;
		params.canalCost = 0;
		params.canalTransitFuelPerDay = TimeUnitConvert.convertPerHourToPerDay(0);
		params.canalTransitTime = 0;
		params.NBOTravelRatePerDay = 0;

		// use the same fuel consumption for every travel
		final int fuelConsumptionHours = 10;
		final int NBORateHours = 10;

		final SimpleCargoAllocation a = new SimpleCargoAllocation(testEquallyPricedRoutes(testName, portDistances, params, fuelConsumptionHours, NBORateHours));

		// check same price
		final long canalPrice = getPriceOfCanal(testName, params, fuelConsumptionHours, NBORateHours);
		final long oceanPrice = getPriceOfOcean(testName, cheapestDistance, fuelConsumptionHours, NBORateHours);
		Assert.assertTrue("Cheapest routes are same price", canalPrice == oceanPrice);

		// check the vessel took either of the cheapest routes.
		final boolean ballastCheapestOceanRouteOrCanal = (a.getBallastLeg().getDistance() == cheapestDistance) || a.getBallastLeg().getRoute().getRouteOption().equals(params.canalOption);
		final boolean ladenCheapestOceanRouteOrCanal = (a.getLadenLeg().getDistance() == cheapestDistance) || a.getLadenLeg().getRoute().getRouteOption().equals(params.canalOption);

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
	private CargoAllocation testEquallyPricedRoutes(final String testName, final int[] distancesBetweenPorts, final CanalParameters canalCosts, final int fuelTravelConsumptionPerHour,
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

		final LNGScenarioModel canalScenario = ScenarioTools.createScenarioWithCanals(distancesBetweenPorts, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, minSpeed,
				maxSpeed, capacity, ballastMinSpeed, ballastMinConsumption, ballastMaxSpeed, ballastMaxConsumption, ballastIdleConsumptionRate, ballastIdleNBORate, ballastNBORate, ladenMinSpeed,
				ladenMinConsumption, ladenMaxSpeed, ladenMaxConsumption, ladenIdleConsumptionRate, ladenIdleNBORate, ladenNBORate, useDryDock, pilotLightRate, minHeelVolume);

		addCanalParameters(canalCosts, canalScenario);
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
	private long getPriceOfCanal(final String testName, final CanalParameters canalCost, final int fuelTravelConsumptionPerHour, final int NBORatePerHour) {
		final SimpleCargoAllocation a = new SimpleCargoAllocation(
				testEquallyPricedRoutes(testName + ": get canal price", new int[] { 10000 }, canalCost, fuelTravelConsumptionPerHour, NBORatePerHour));

		return getCargoAllocationCost(a);
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
		final SimpleCargoAllocation a = new SimpleCargoAllocation(testEquallyPricedRoutes(testName + ": get ocean price", new int[] { distance }, null, fuelTravelConsumptionPerHour, NBORatePerHour));

		return getCargoAllocationCost(a);

	}

	private void addCanalParameters(CanalParameters canalParameters, LNGScenarioModel canalScenario) {
		if (canalParameters != null) {
			CustomScenarioCreator.createCanalAndCost(canalScenario, canalParameters.canalOption, ScenarioTools.A, ScenarioTools.B, canalParameters.canalDistance, canalParameters.canalDistance,
					canalParameters.canalCost, canalParameters.canalCost, canalParameters.canalTransitFuelPerDay, canalParameters.NBOTravelRatePerDay, canalParameters.canalTransitTime);
		}
	}

	long getCargoAllocationCost(SimpleCargoAllocation a) {

		long total = 0;

		total += a.getLadenLeg().getFuelCost();
		total += a.getLadenLeg().getCharterCost();
		total += a.getLadenLeg().getToll();
		total += a.getLadenIdle().getFuelCost();
		total += a.getLadenIdle().getCharterCost();

		total += a.getBallastLeg().getFuelCost();
		total += a.getBallastLeg().getCharterCost();
		total += a.getBallastLeg().getToll();
		total += a.getBallastIdle().getFuelCost();
		total += a.getBallastIdle().getCharterCost();

		return total;

	}
}
