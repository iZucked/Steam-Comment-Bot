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

/**
 * <a href="">Case 203: Test for min/max speed conditions</a>
 * <p>
 * Create some tests which check that the min/max speeds are hit under the correct conditions: <br>
 * - max speed when available time is too small (can cause lateness)<br>
 * - min speed when available time is long enough and on base fuel mode<br>
 * - min NBO speed when available time is long enough and on NBO<br>
 * mode
 * 
 * @author Adam Semenenko
 * 
 */
public class MinMaxSpeedTests {

	// The min and max speed of the vessel class.
	private static final int minSpeed = 10;
	private static final int maxSpeed = 20;

	/** The distance between the ports. */
	private static final int distanceBetweenPorts = 1000;
	/** This value makes base fuel more expensive than NBO. */
	private static final float baseFuelExpensive = 1.1f;
	/** This value makes base fuel cheaper than NBO. */
	private static final float baseFuelCheap = 0.9f;
	
	/**
	 * Test that the max speed is hit on NBO when the available time is not enough.
	 */
	@Test
	public void maxSpeedWhenLate() {

		final String testName = "Max speed due to lateness";
		// set the travel time to be one hour too little
		final int travelTime = (distanceBetweenPorts / maxSpeed) - 1;

		CargoAllocation a = test(testName, travelTime, baseFuelExpensive);

		Assert.assertTrue("Laden leg travels at max speed", a.getLadenLeg().getSpeed() == maxSpeed);
	}

	/**
	 * Test that the min speed is used if the vessel won't be late
	 * */
	@Test
	public void minSpeedUsed() {

		final String testName = "Min speed used";
		// set the travel time to be sufficient for min speed travel
		final int travelTime = (distanceBetweenPorts / minSpeed) + 1;

		CargoAllocation a = test(testName, travelTime, baseFuelExpensive);

		Assert.assertTrue("Laden leg travels at min speed", a.getLadenLeg().getSpeed() == minSpeed);
	}

	private CargoAllocation test(final String testName, final int travelTime, final float baseFuelUnitPrice) {

		final float dischargePrice = 1;
		final float cvValue = 1;
		// make fuel and LNG equivalent for ease.
		final float equivalenceFactor = 1f;

		final int fuelConsumptionPerDay = ScenarioTools.convertPerHourToPerDay(10);
		final int NBORatePerDay = ScenarioTools.convertPerHourToPerDay(10);

		final int capacity = 100000;
		final int ballastMinSpeed = minSpeed;
		final int ballastMinConsumption = fuelConsumptionPerDay;
		final int ballastMaxSpeed = maxSpeed;
		final int ballastMaxConsumption = fuelConsumptionPerDay;
		final int ballastIdleConsumptionRate = fuelConsumptionPerDay;
		final int ballastIdleNBORate = NBORatePerDay;
		final int ballastNBORate = NBORatePerDay;
		final int ladenMinSpeed = minSpeed;
		final int ladenMinConsumption = fuelConsumptionPerDay;
		final int ladenMaxSpeed = maxSpeed;
		final int ladenMaxConsumption = fuelConsumptionPerDay;
		final int ladenIdleConsumptionRate = fuelConsumptionPerDay;
		final int ladenIdleNBORate = NBORatePerDay;
		final int ladenNBORate = NBORatePerDay;
		final int pilotLightRate = 0;
		final int minHeelVolume = 0;

		Scenario scenario = ScenarioTools.createScenario(distanceBetweenPorts, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, minSpeed, maxSpeed, capacity,
				ballastMinSpeed, ballastMinConsumption, ballastMaxSpeed, ballastMaxConsumption, ballastIdleConsumptionRate, ballastIdleNBORate, ballastNBORate, ladenMinSpeed, ladenMinConsumption,
				ladenMaxSpeed, ladenMaxConsumption, ladenIdleConsumptionRate, ladenIdleNBORate, ladenNBORate, false, pilotLightRate, minHeelVolume);

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);
		// check result is how we expect it to be
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		ScenarioTools.printScenario(testName, a);

		return a;
	}
}
