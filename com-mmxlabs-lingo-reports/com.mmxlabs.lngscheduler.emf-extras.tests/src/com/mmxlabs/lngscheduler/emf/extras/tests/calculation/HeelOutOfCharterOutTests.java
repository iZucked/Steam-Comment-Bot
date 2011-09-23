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
import scenario.schedule.events.CharterOutVisit;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.FuelType;
import scenario.schedule.events.Journey;
import scenario.schedule.events.ScheduledEvent;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?186">Case 186: Heel out of charter out</a>
 * <p>
 * Need to check that heel out of charter is used when it's available and cheaper, not used when it's not available and cheaper, not used when it's available and not cheaper, not used when not
 * available and not cheaper.
 * <p>
 * Check heel out limits are respected correctly (i.e. no more heel used than limit allows).
 * 
 * @author Adam Semenenko
 * 
 */
public class HeelOutOfCharterOutTests {

	/**
	 * LNG is available and slightly more expensive than BF, so expect no NBO/FBO to be used and BF to be used instead.
	 */
	@Test
	public void heelNotUsed() {

		final float baseFuelUnitPrice = 1f;

		// discharge price makes LNG slightly more expensive than BF.
		final float dischargePrice = 1.01f;
		final float cvValue = 1;
		// set charter out details.
		final int charterOutTimeDays = 10;
		final int heelLimit = 1000;

		Schedule result = evaluateCharterOutScenario(dischargePrice, cvValue, baseFuelUnitPrice, charterOutTimeDays, heelLimit);

		Journey j = getJourneyAfterCharterOut(result);

		// Because LNG is expensive and BF is cheap, expect BF to be used on the journey after the charter out.
		for (FuelQuantity fq : j.getFuelUsage()) {
			if (fq.getFuelType() == FuelType.BASE_FUEL)
				Assert.assertTrue("Base fuel used", fq.getQuantity() > 0);
			else
				Assert.assertTrue("FBO and NBO not used", fq.getQuantity() == 0);
		}
	}

	/**
	 * LNG is available and slightly cheaper than BF, so expect no NBO/FBO to be used and BF to be used instead.
	 */
	@Test
	public void heelUsed() {

		final float baseFuelUnitPrice = 1f;

		// discharge price makes LNG slightly cheaper than BF.
		final float dischargePrice = 0.99f;
		final float cvValue = 1;
		// set charter out details.
		final int charterOutTimeDays = 10;
		final int heelLimit = 1000;

		Schedule result = evaluateCharterOutScenario(dischargePrice, cvValue, baseFuelUnitPrice, charterOutTimeDays, heelLimit);

		Journey j = getJourneyAfterCharterOut(result);

		// Because LNG is expensive and BF is cheap, expect BF to be used on the journey after the charter out.
		for (FuelQuantity fq : j.getFuelUsage()) {
			if (fq.getFuelType() == FuelType.BASE_FUEL)
				Assert.assertTrue("Base fuel not used", fq.getQuantity() == 0);
			else
				Assert.assertTrue("FBO and NBO used", fq.getQuantity() > 0);
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

		// Set NBO so it is not enough to cover travelling fuel, so either need base fuel or FBO.
		final int fuelConsumptionPerDay = ScenarioTools.convertPerHourToPerDay(10);
		final int NBORatePerHour = ScenarioTools.convertPerHourToPerDay(5);

		// same distance between both ports.
		final int distanceBetweenPorts = 1000;
		final int travelTime = 100;
		// Have the same speed for all travel.
		final int speed = 10;
		// make fuel and LNG equivalent for ease.
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

	/**
	 * Assuming that a schedule has a charter out and a journey afterwards, this method will return the journey immediately after the charter out. If there is none an assert will fail.
	 * 
	 * @param charterOutSchedule
	 * @return
	 */
	private Journey getJourneyAfterCharterOut(Schedule charterOutSchedule) {
		// only expect one sequence.
		Assert.assertTrue("Only one sequence", charterOutSchedule.getSequences().size() == 1);
		Sequence seq = charterOutSchedule.getSequences().get(0);

		// get the event after the charter out.
		for (ScheduledEvent e : seq.getEvents()) {

			if (e instanceof CharterOutVisit) {
				// got the charter out, now get the event after it and make sure it is a journey. If it is, return it.
				int charterOutIndex = seq.getEvents().indexOf(e);
				int nextEventIndex = charterOutIndex + 1;
				Assert.assertTrue("There is an event after the charter out", seq.getEvents().size() >= nextEventIndex);
				ScheduledEvent eventAfterCharterOut = seq.getEvents().get(charterOutIndex + 1);
				Assert.assertTrue("Event after charter out is a journey", eventAfterCharterOut instanceof Journey);

				return (Journey) eventAfterCharterOut;
			}
		}

		Assert.fail("No charter out in sequence");
		return null;
	}
}