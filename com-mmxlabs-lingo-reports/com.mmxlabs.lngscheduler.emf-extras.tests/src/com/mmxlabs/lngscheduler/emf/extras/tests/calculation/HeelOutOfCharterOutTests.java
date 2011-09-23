/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.calculation;

import org.junit.Assert;
import org.junit.Test;

import scenario.Scenario;
import scenario.schedule.Schedule;
import scenario.schedule.Sequence;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.FuelType;
import scenario.schedule.events.Journey;
import scenario.schedule.events.ScheduledEvent;

/**
 * @author Adam Semenenko
 * 
 */
public class HeelOutOfCharterOutTests {

	/**
	 * Test that the heel out of charter out is used as NBO but not FBO because LNG is expensive.
	 */
	@Test
	public void heelFBONotUsed() {

		final float baseFuelUnitPrice = 1f;
		// set discharge price and cv value high to make NBO expensive to try and force BF use.
		final float dischargePrice = 100;
		final float cvValue = 100;
		// set charter out details.
		final int charterOutTimeDays = 10;
		final int heelLimit = 1000;

		Schedule result = evaluateCharterOutScenario(dischargePrice, cvValue, baseFuelUnitPrice, charterOutTimeDays, heelLimit);
		
		// get the journey after the charter out.
		ScheduledEvent e = result.getSequences().get(0).getEvents().get(2); // TODO fix this so it's not hacky.
		Assert.assertTrue(e instanceof Journey);
		
		Journey j = (Journey) e;
		
		// Because LNG is expensive and BF is cheap, expect BF to be used on the journey after the charter out.
		for (FuelQuantity fq : j.getFuelUsage())
		{
			if (fq.getFuelType() == FuelType.BASE_FUEL)
			Assert.assertTrue("Base fuel used", fq.getQuantity() > 0);
		}
	}

	/**
	 * Evaluate a scenario with a charter out and return the result.
	 * 
	 * @param dischargePrice
	 *            The discharge price of the LNG
	 * @param cvValue
	 *            The CV value.
	 * @param baseFuelUnitPrice
	 *            The price per unit of base fuel
	 * @param charterOutTimeDays
	 *            The length the charter out is for.
	 * @param heelLimit
	 *            The amount of LNG that the vessel has once it has been returned from the heel.
	 * @return The schedule, detailing the route of the vessel.
	 */
	private Schedule evaluateCharterOutScenario(final float dischargePrice, final float cvValue, final float baseFuelUnitPrice, final int charterOutTimeDays, final int heelLimit) {

		final int speed = 10;
		// Set NBO so it is not enough to cover travelling fuel, so either need base fuel or FBO.
		final int fuelConsumptionPerDay = ScenarioTools.convertPerHourToPerDay(10);
		final int NBORatePerHour = ScenarioTools.convertPerHourToPerDay(5);

		final int distanceBetweenPorts = 1000;

		final int travelTime = 100;
		final float equivalenceFactor = 1f;

		final int minSpeed = speed;
		final int maxSpeed = speed;
		final int capacity = 100000;
		final int ballastMinSpeed = speed;
		final int ballastMinConsumption = speed;
		final int ballastMaxSpeed = speed;
		final int ballastMaxConsumption = fuelConsumptionPerDay;
		final int ballastIdleConsumptionRate = fuelConsumptionPerDay;
		final int ballastIdleNBORate = NBORatePerHour;
		final int ballastNBORate = NBORatePerHour;
		final int ladenMinSpeed = speed;
		final int ladenMinConsumption = fuelConsumptionPerDay;
		final int ladenMaxSpeed = speed;
		final int ladenMaxConsumption = fuelConsumptionPerDay;
		final int ladenIdleConsumptionRate = fuelConsumptionPerDay;
		final int ladenIdleNBORate = NBORatePerHour;
		final int ladenNBORate = NBORatePerHour;
		final int pilotLightRate = 0;

		Scenario scenario = ScenarioTools.createCharterOutScenario(distanceBetweenPorts, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, minSpeed, maxSpeed, capacity,
				ballastMinSpeed, ballastMinConsumption, ballastMaxSpeed, ballastMaxConsumption, ballastIdleConsumptionRate, ballastIdleNBORate, ballastNBORate, ladenMinSpeed, ladenMinConsumption,
				ladenMaxSpeed, ladenMaxConsumption, ladenIdleConsumptionRate, ladenIdleNBORate, ladenNBORate, pilotLightRate, charterOutTimeDays, heelLimit);

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);

		// print the Sequence (there should only be one Sequence)
		for (Sequence seq : result.getSequences())
			ScenarioTools.printSequence(seq);

		return result;
	}
}