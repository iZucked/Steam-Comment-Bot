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
 * @author Adam Semenenko
 * 
 */
public class CanalLatenessBoundaryTests {

	private static final String canalName = "Suez canal";

	@Test
	public void canalUsedToAvoidLateness() {

		final String testName = "Expensive canal used to avoid lateness";

		final int canalCost = 500;
		final int travelTime = 100;
		// The ocean route is 100 miles longer so it will be 10 hours (10mph vessel) late. The canal route will be 100 hours early.
		final int canalDistance = 900;
		final int portDistance = 1100;

		CargoAllocation a = testRouteWhenLate(testName, canalCost, canalDistance, portDistance, travelTime);

		Assert.assertTrue("Laden leg travels on canal", canalName.equals(a.getLadenLeg().getRoute()));
		Assert.assertTrue("Ballast leg travels on canal", canalName.equals(a.getBallastLeg().getRoute()));
	}
	
	@Test
	public void expensiveCanalUsedWhenLate() {

		final String testName = "Expensive canal used when late";

		final int canalCost = 1000;
		final int travelTime = 100;
		// The ocean route is 100 miles longer so it will be 10 hours (10mph vessel) late. The canal route will be 100 hours early.
		final int canalDistance = 1050;
		final int portDistance = 1060;

		CargoAllocation a = testRouteWhenLate(testName, canalCost, canalDistance, portDistance, travelTime);

		Assert.assertTrue("Laden leg travels on canal", canalName.equals(a.getLadenLeg().getRoute()));
		Assert.assertTrue("Ballast leg travels on canal", canalName.equals(a.getBallastLeg().getRoute()));
	}

	private CargoAllocation testRouteWhenLate(final String testName, final int canalFee, final int canalDistance, final int distanceBetweenPorts, final int travelTime) {

		// have no transit time for the canal TODO change this to argument, should test it.
		final int canalTransitFuelDays = 0;
		final int canalTransitTimeHours = 0;

		// use the same fuel consumption for every travel/idle/canal
		final int fuelConsumptionDays = 10;
		final int NBORateDays = 10;
		
		// same fee for laden and ballast
		final int canalLadenCost = canalFee;
		final int canalUnladenCost = canalFee;
		
		// have same fuel consumption/nbo rate for travel and idle
		final int fuelTravelConsumptionDays = fuelConsumptionDays;
		final int fuelIdleConsumptionDays = fuelConsumptionDays;
		final int NBOTravelRateDays = NBORateDays;
		final int NBOIdleRateDays = NBORateDays;

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

		VesselClassCost canalCost = ScenarioTools.createCanalAndCost(canalName, canalDistance, canalDistance, canalLadenCost, canalUnladenCost, canalTransitFuelHours, canalTransitTimeHours);

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
