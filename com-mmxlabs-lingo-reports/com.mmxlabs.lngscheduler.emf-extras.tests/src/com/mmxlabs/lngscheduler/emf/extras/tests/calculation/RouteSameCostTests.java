/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.calculation;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;

import scenario.Scenario;
import scenario.fleet.VesselClassCost;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;

/**
 * Test behaviour when there are two cheapest routes with identical prices. The order doesn't matter so long as the price is the same.
 * <p>
 * Should test that for at least two identically priced routes they are the same price and are chosen over more expensive routes.
 * 
 * @author Adam Semenenko
 *
 */
public class RouteSameCostTests {

	private static final String defaultRouteName = IMultiMatrixProvider.Default_Key;
	private static final String canalName = "Suez canal";



	/**
	 * Canal is twice the distance of a sea route. Fuel consumption and NBO rates are all identical. Canal has no fees.
	 * 
	 * TODO Create more tests along this line.
	 * 
	 * The routes are identically priced. Add some more expensive routes. Check at
	 * 1) the two cheapest routes are the same price.
	 * 2) one of the two cheapest are selected over the more expensive routes.
	 */
	@Test
	public void canalTwiceDistance() {

		final String testName = "Canal twice the distance of the ocean route";
		final int portDistance = 1000;
		final int canalDistance = 2000;

		final int canalLadenCost = 0;
		final int canalUnladenCost = 0;
		// the extra time that the vessel class takes the travel down the canal independent of speed & distance
		final int canalTransitTime = 0;
		// the consumption for idle and travel is the same.
		final int fuelTravelConsumptionDays = 10;
		final int fuelIdleConsumptionDays = 10;
		final int NBORateDays = 10;

		CargoAllocation a = testCanalCost(testName, portDistance, canalDistance, canalLadenCost, canalUnladenCost, fuelTravelConsumptionDays, canalTransitTime, fuelTravelConsumptionDays,
				fuelIdleConsumptionDays, NBORateDays, fuelIdleConsumptionDays);
		Assert.assertTrue("Laden leg travels on open sea", defaultRouteName.equals(a.getLadenLeg().getRoute()));
		Assert.assertTrue("Ballast leg travels on open sea", defaultRouteName.equals(a.getBallastLeg().getRoute()));
	}
	
	/**
	 * TODO What's the default behaviour if a canal and ocean are equal?
	 * 
	 * test costs ARE identical & one is chosen, add worse costs
	 */
	@Test
	public void testCanalOceanSameCost() {

		final String testName = "Ocean and canal same price because of fee.";
		final int canalCost = 100;
		final int canalFuel = 10;
		final int canalTransitTimeHours = 0;

		CargoAllocation a = testCanalCost(testName, canalCost, canalFuel, canalTransitTimeHours);

		Assert.assertTrue("Laden leg travels on ocean", defaultRouteName.equals(a.getLadenLeg().getRoute()));
		Assert.assertTrue("Ballast leg travels on ocean", defaultRouteName.equals(a.getBallastLeg().getRoute()));
	}

	/**
	 * TODO remove this.
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
	 * TODO remove this, add a method for creating a scenario with an arbitrary number of routes (canals and oceans)
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
		// check result is how we expect it to be
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		ScenarioTools.printScenario(testName, a);

		return a;
	}
	
}
