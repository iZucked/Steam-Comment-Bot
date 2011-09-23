/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.calculation;

import org.junit.Assert;
import org.junit.Test;

import scenario.Scenario;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.FuelType;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?187">Case 187: Min travel heel is used</a>
 * <p>
 * Vessel classes have a min heel for travel, which they must keep while moving on NBO. Because this heel is sitting around at arrival time and will boil off, it's "free" at that point and so should
 * be always be used for idling.
 * <p>
 * There should be a test in which the vessel class has some nonzero min heel, and arrives on a ballast leg for which NBO is the cheapest travel option but Base Fuel is the cheapest idle option - in
 * this situation there should be enough NBO used in the Idle to boil off all the travel heel.
 * <p>
 * Note that although this idle boil off is kind of free, it's not priced as free, since you still really pay for it because you are forced to keep it on board rather than selling it at the discharge.
 * 
 * @author Adam Semenenko
 * 
 */
public class MinTravelHeelTests {

	/**
	 * Test that ballast leg is NBO, idle is cheaper on BF. TODO test result of non-zero min heel
	 */
	@Test
	public void testZeroMinHeel() {
		final String testName = "Test min heel";
		final int minHeelVolume = 0;
		
		// These are set up so that the ballast journey is cheaper on NBO, whilst the ballast idle is cheaper on base fuel.
		final int travelFuelConsumptionPerHour = 10;
		final int idleFuelConsumptionPerHour = 9;
		final int travelNBORatePerHour = 10;
		final int idleNBORatePerHour = 10;

		CargoAllocation a = test(testName, minHeelVolume, travelFuelConsumptionPerHour, idleFuelConsumptionPerHour, travelNBORatePerHour, idleNBORatePerHour);

		// ballast travel is cheaper on NBO
		for (FuelQuantity fq : a.getBallastLeg().getFuelUsage()) {
			if (fq.getFuelType() == FuelType.NBO)
				Assert.assertTrue("Ballast leg uses NBO", fq.getQuantity() > 0);
			else
				Assert.assertTrue("Ballast leg doesn't use FBO or base fuel", fq.getQuantity() == 0);
		}
		// ballast idle is cheaper on base fuel
		for (FuelQuantity fq : a.getBallastIdle().getFuelUsage()) {
			if (fq.getFuelType() == FuelType.BASE_FUEL)
				Assert.assertTrue("Ballast idle uses base fuel", fq.getQuantity() > 0);
			else
				Assert.assertTrue("Ballast idle doesn't use NBO or FBO", fq.getQuantity() == 0);
		}
	}

	private CargoAllocation test(final String testName, final int minHeelVolume, final int fuelTravelConsumptionPerHour, final int fuelIdleConsumptionPerHour, final int NBOTravelRatePerHour,
			final int NBOIdleRatePerHour) {

		// NBO can cover fuel consumption
		//final int fuelConsumptionPerDay = ScenarioTools.convertPerHourToPerDay(10);
		//final int NBORatePerDay = ScenarioTools.convertPerHourToPerDay(10);

		// same distance between both ports.
		final int distanceBetweenPorts = 1000;
		// travel time leaves 10 hours idle time
		final int travelTime = 110;
		// Have the same speed for all travel.
		final int speed = 10;
		// make fuel and LNG equivalent for ease.
		final float equivalenceFactor = 1f;

		final float dischargePrice = 1;
		final float cvValue = 1;
		final float baseFuelUnitPrice = 1;

		final int minSpeed = speed;
		final int maxSpeed = speed;
		final int capacity = 100000;
		final int ballastMinSpeed = speed;
		final int ballastMinConsumption = speed;
		final int ballastMaxSpeed = speed;
		final int ballastMaxConsumption = ScenarioTools.convertPerHourToPerDay(fuelTravelConsumptionPerHour);
		final int ballastIdleConsumptionRate = ScenarioTools.convertPerHourToPerDay(fuelIdleConsumptionPerHour);
		final int ballastIdleNBORate = ScenarioTools.convertPerHourToPerDay(NBOIdleRatePerHour);
		final int ballastNBORate = ScenarioTools.convertPerHourToPerDay(NBOTravelRatePerHour);
		final int ladenMinSpeed = speed;
		final int ladenMinConsumption = ScenarioTools.convertPerHourToPerDay(fuelTravelConsumptionPerHour);
		final int ladenMaxSpeed = speed;
		final int ladenMaxConsumption = ScenarioTools.convertPerHourToPerDay(fuelTravelConsumptionPerHour);
		final int ladenIdleConsumptionRate = ScenarioTools.convertPerHourToPerDay(fuelIdleConsumptionPerHour);
		final int ladenIdleNBORate = ScenarioTools.convertPerHourToPerDay(NBOIdleRatePerHour);
		final int ladenNBORate = ScenarioTools.convertPerHourToPerDay(NBOTravelRatePerHour);
		final int pilotLightRate = 0;

		Scenario scenario = ScenarioTools.createScenario(distanceBetweenPorts, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, minSpeed, maxSpeed, capacity,
				ballastMinSpeed, ballastMinConsumption, ballastMaxSpeed, ballastMaxConsumption, ballastIdleConsumptionRate, ballastIdleNBORate, ballastNBORate, ladenMinSpeed, ladenMinConsumption,
				ladenMaxSpeed, ladenMaxConsumption, ladenIdleConsumptionRate, ladenIdleNBORate, ladenNBORate, true, pilotLightRate, minHeelVolume);

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		ScenarioTools.printScenario(testName, a);

		return a;
	}
}
