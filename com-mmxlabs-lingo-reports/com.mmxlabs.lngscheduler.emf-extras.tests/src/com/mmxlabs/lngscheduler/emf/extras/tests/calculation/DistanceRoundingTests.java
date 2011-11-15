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

import com.mmxlabs.common.TimeUnitConvert;

/**
 * 
 * Test that distances smaller than 10 but greater than 0 work. No exceptions should be thrown, the distance should not be rounded to 0, and the result should be sensible.
 * 
 * @author Adam Semenenko
 * 
 */
public class DistanceRoundingTests {

	/**
	 * Test that a distance of 9 and speed of 10 between two ports gives sensible results.
	 */
	@Test
	public void testDistanceRounding() {

		final int distanceBetweenPorts = 9;
		final String testName = "Test distance rounding for distance of " + distanceBetweenPorts;
		final int travelTime = 1;
		final int speed = 10;

		CargoAllocation a = test(testName, distanceBetweenPorts, travelTime, speed);

		// The expected duration, if an int, will be rounded to zero.
		final int expectedDuration = distanceBetweenPorts / speed;

		// check actual distances match the set distance
		Assert.assertSame("Laden leg same distance", distanceBetweenPorts, a.getLadenLeg().getDistance());
		Assert.assertSame("Ballast leg same distance", distanceBetweenPorts, a.getBallastLeg().getDistance());

		Assert.assertSame(expectedDuration, a.getLadenLeg().getEventDuration());
		Assert.assertSame(expectedDuration, a.getBallastLeg().getEventDuration());

		/* At the moment the expected result is that the duration is rounded to zero. These tests are for if this isn't the case.
		 *
		 * 
		 * // check the transit time is non-zero
		 * 
		 * final int ladenDuration = a.getLadenLeg().getEventDuration(); final int ballastDuration = a.getBallastLeg().getEventDuration();//
		 * 
		 * Assert.assertTrue("Expect non-zero duration for laden leg (expected " + expectedDuration + ", got " + ladenDuration + ").", ladenDuration > 0);//
		 * 
		 * Assert.assertTrue("Expect non-zero duration for ballast leg (expected " + expectedDuration + ", got " + ballastDuration + ").", ballastDuration > 0);//
		 * 
		 * // the cost of the route should be non-zero
		 * 
		 * Assert.assertTrue("Non-zero cost of laden leg", a.getLadenLeg().getTotalCost() > 0);
		 * 
		 * Assert.assertTrue("Non-zero cost of ballast leg", a.getBallastLeg().getTotalCost() > 0);
		 */
	}

	private CargoAllocation test(final String testName, final int distanceBetweenPorts, final int travelTime, final int speed) {

		final float baseFuelUnitPrice = 1;
		final float dischargePrice = 1;
		final float cvValue = 1;

		// make fuel and LNG equivalent for ease.
		final float equivalenceFactor = 1f;

		final int fuelConsumptionPerDay = TimeUnitConvert.convertPerHourToPerDay(10);
		final int NBORatePerDay = TimeUnitConvert.convertPerHourToPerDay(10);

		final int capacity = 100000;
		final int ballastMinSpeed = speed;
		final int ballastMinConsumption = fuelConsumptionPerDay;
		final int ballastMaxSpeed = speed;
		final int ballastMaxConsumption = fuelConsumptionPerDay;
		final int ballastIdleConsumptionRate = fuelConsumptionPerDay;
		final int ballastIdleNBORate = NBORatePerDay;
		final int ballastNBORate = NBORatePerDay;
		final int ladenMinSpeed = speed;
		final int ladenMinConsumption = fuelConsumptionPerDay;
		final int ladenMaxSpeed = speed;
		final int ladenMaxConsumption = fuelConsumptionPerDay;
		final int ladenIdleConsumptionRate = fuelConsumptionPerDay;
		final int ladenIdleNBORate = NBORatePerDay;
		final int ladenNBORate = NBORatePerDay;
		final int pilotLightRate = 0;
		final int minHeelVolume = 0;

		Scenario scenario = ScenarioTools.createScenario(distanceBetweenPorts, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, speed, speed, capacity, ballastMinSpeed,
				ballastMinConsumption, ballastMaxSpeed, ballastMaxConsumption, ballastIdleConsumptionRate, ballastIdleNBORate, ballastNBORate, ladenMinSpeed, ladenMinConsumption, ladenMaxSpeed,
				ladenMaxConsumption, ladenIdleConsumptionRate, ladenIdleNBORate, ladenNBORate, true, pilotLightRate, minHeelVolume);

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);
		// check result is how we expect it to be
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		ScenarioTools.printCargoAllocation(testName, a);

		return a;
	}

}
