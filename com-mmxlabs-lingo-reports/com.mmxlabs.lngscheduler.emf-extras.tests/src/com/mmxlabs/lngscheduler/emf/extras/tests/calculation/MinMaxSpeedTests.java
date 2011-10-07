/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.calculation;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.TimeUnitConvert;

import scenario.Scenario;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.FuelType;

/**
 * <a href="">Case 203: Test for min/max speed conditions</a>
 * <p>
 * Create some tests which check that the min/max speeds are hit under the correct conditions: <br>
 * - max speed when available time is too small (can cause lateness)<br>
 * - min speed when available time is long enough and on base fuel mode<br>
 * - min NBO speed when available time is long enough and on NBO<br>
 * mode
 * 
 * TODO NBO speed not currently set. Wait for fix then properly implement tests for each min/max speed.
 * 
 * @author Adam Semenenko
 * 
 */
public class MinMaxSpeedTests {

	// The min and max speed of the vessel class.
	private static final int minSpeed = 10;
	private static final int maxSpeed = 20;

	private static final int fuelMinConsumptionPerHour = 10;
	private static final int fuelMaxConsumptionPerHour = 20;
	private static final int NBORatePerHour = 11;

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
		Assert.assertTrue("Ballast leg travels at max speed", a.getBallastLeg().getSpeed() == maxSpeed);
	}

	/**
	 * Test that the min speed is used if the vessel won't be late and vessel uses base fuel.
	 * */
	@Test
	public void minSpeedUsed() {

		final String testName = "Min speed used";
		// set the travel time to be sufficient for min speed travel
		final int travelTime = (distanceBetweenPorts / minSpeed) + 1;

		CargoAllocation a = test(testName, travelTime, baseFuelCheap);

		// assert that base fuel is used on the ballast leg as it is cheaper than LNG
		for (FuelQuantity fq : a.getBallastLeg().getFuelUsage()) {
			if (fq.getFuelType() == FuelType.NBO || fq.getFuelType() == FuelType.FBO)
				Assert.assertTrue("LNG not used as BF cheaper", fq.getQuantity() == 0);
			else if (fq.getFuelType() == FuelType.BASE_FUEL)
				Assert.assertTrue("Base fuel used, LNG more expensive", fq.getQuantity() > 0);
		}

		// assert that the vessel travels at min speed as there is sufficient time.
		Assert.assertTrue("Ballast leg travels at min speed", a.getBallastLeg().getSpeed() == minSpeed);
	}

	/**
	 * Test min NBO speed used when available time is long enough and on NBO mode
	 * <p>
	 * The NBO min speed is the speed at which the vessel should travel to use up all the NBO. If the vessel travels slower than this speed, boil-off is wasted so it is not desirable to do this.
	 */
	@Test
	public void minNBOSpeedUsed() {

		final String testName = "Min speed used";
		// set the travel time to be sufficient for min speed travel
		final int travelTime = (distanceBetweenPorts / minSpeed) + 10;

		CargoAllocation a = test(testName, travelTime, baseFuelExpensive);

		// assert that NBO is used on the ballast leg as it is cheaper than base fuel
		for (FuelQuantity fq : a.getBallastLeg().getFuelUsage()) {
			if (fq.getFuelType() == FuelType.NBO)
				Assert.assertTrue("Cheap NBO used", fq.getQuantity() > 0);
			else if (fq.getFuelType() == FuelType.BASE_FUEL)
				Assert.assertTrue("Base fuel not used", fq.getQuantity() == 0);
		}

		/*
		 * TODO work out NBO min speed
		 * 
		 * Get equivalent MT of base fuel, if more than fuel consumption, travel at higher speed?
		 * nbo rate per hour * equivalence ... ?
		 */
		
		// TODO reactivate test when min NBO speed is found and check ballast leg speed against that.
		// assert that the vessel travels at min speed as there is sufficient time.
		//Assert.assertTrue("Ballast leg travels at min speed", a.getBallastLeg().getSpeed() == minSpeed);
	}

	private CargoAllocation test(final String testName, final int travelTime, final float baseFuelUnitPrice) {

		final float dischargePrice = 1;
		final float cvValue = 1;
		// make fuel and LNG equivalent for ease.
		final float equivalenceFactor = 1f;

		final int fuelMinConsumptionPerDay = TimeUnitConvert.convertPerHourToPerDay(fuelMinConsumptionPerHour);
		final int fuelMaxConsumptionPerDay = TimeUnitConvert.convertPerHourToPerDay(fuelMaxConsumptionPerHour);
		final int NBORatePerDay = TimeUnitConvert.convertPerHourToPerDay(NBORatePerHour);

		final int capacity = 100000;
		final int ballastMinSpeed = minSpeed;
		final int ballastMinConsumption = fuelMinConsumptionPerDay;
		final int ballastMaxSpeed = maxSpeed;
		final int ballastMaxConsumption = fuelMaxConsumptionPerDay;
		final int ballastIdleConsumptionRate = fuelMinConsumptionPerDay;
		final int ballastIdleNBORate = NBORatePerDay;
		final int ballastNBORate = NBORatePerDay;
		final int ladenMinSpeed = minSpeed;
		final int ladenMinConsumption = fuelMinConsumptionPerDay;
		final int ladenMaxSpeed = maxSpeed;
		final int ladenMaxConsumption = fuelMaxConsumptionPerDay;
		final int ladenIdleConsumptionRate = fuelMinConsumptionPerDay;
		final int ladenIdleNBORate = NBORatePerDay;
		final int ladenNBORate = NBORatePerDay;
		final int pilotLightRate = 0;
		final int minHeelVolume = 0;

		Scenario scenario = ScenarioTools.createScenario(distanceBetweenPorts, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, minSpeed, maxSpeed, capacity,
				ballastMinSpeed, ballastMinConsumption, ballastMaxSpeed, ballastMaxConsumption, ballastIdleConsumptionRate, ballastIdleNBORate, ballastNBORate, ladenMinSpeed, ladenMinConsumption,
				ladenMaxSpeed, ladenMaxConsumption, ladenIdleConsumptionRate, ladenIdleNBORate, ladenNBORate, true, pilotLightRate, minHeelVolume);

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);
		// check result is how we expect it to be
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		ScenarioTools.printScenario(testName, a);

		return a;
	}
}
