/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.calculation.singleEvent;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.TimeUnitConvert;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.FuelUsageAssertions;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?186">Case 186: Heel out of charter out</a>
 * <p>
 * Need to check that heel out of charter is used when it's available and cheaper, not used when it's not available and cheaper, not used when it's available and not cheaper, not used when not
 * available and not cheaper.
 * <p>
 * Whether the heel is used (y) or not (n):
 * 
 * <pre>
 * Quantity    \  price | Cheaper   | Expensive
 * ---------------------|-----------|----------
 * Enough for FBO + NBO | y (No BF) |   n
 *                      |           |
 * Enough for NBO       | y (NBO+BF)|   n
 *                      |           |
 * Not enough           | n         |   n
 * </pre>
 * <p>
 * Check heel out limits are respected correctly (i.e. no more heel used than limit allows).
 * 
 * @author Adam Semenenko
 * 
 */
@RunWith(value = ShiroRunner.class)
public class HeelOutOfCharterOutTest {

	/**
	 * LNG is available but BF is used as LNG is slightly more expensive
	 */
	@Ignore("Test cannot distinguish between base and base supplemental")
	@Test
	public void heelFBOAvailableLNGExpensiveUsed() {

		final int heelLimit = 1000;
		testHeelWithExpensiveLNG(heelLimit);
	}

	/**
	 * LNG is not available for FBO but not used for NBO as LNG is slightly more expensive
	 */
	@Ignore("Test cannot distinguish between base and base supplemental")
	@Test
	public void heelNBOAvailableLNGExpensiveUsed() {

		final int heelLimit = 500;
		testHeelWithExpensiveLNG(heelLimit);
	}

	/**
	 * LNG is not available so is not used
	 */
	@Test
	public void heelNotAvailableNotUsed() {

		final int heelLimit = 0;
		testHeelWithExpensiveLNG(heelLimit);
	}

	/**
	 * Test that the heel is always used even though LNG is slightly more expensive than base fuel
	 * 
	 * @param heelLimit
	 */
	private void testHeelWithExpensiveLNG(final int heelLimit) {

		final float baseFuelUnitPrice = 1f;

		// discharge price makes LNG slightly more expensive than BF.
		final float dischargePrice = 1.01f;
		final float cvValue = 1;
		// set charter out details.
		final int charterOutTimeDays = 10;

		final Schedule result = evaluateCharterOutScenario(dischargePrice, cvValue, baseFuelUnitPrice, charterOutTimeDays, heelLimit);

		final Journey j = getJourneyAfterCharterOut(result);

		if (heelLimit == 0) {
			// expect LNG to never be used as there is none
			FuelUsageAssertions.assertLNGNotUsed(j);
		} else {
			// expect LNG to used as any heel is automatically used.
			FuelUsageAssertions.assertBaseFuelNotUsed(j);
		}
	}

	/**
	 * For testing whether the heel limit affects fuel choice if LNG is cheaper than BF.
	 * 
	 * @param heelLimit
	 * @return The journey after the charter out to test.
	 */
	private Journey testHeelWithCheaperLNG(final int heelLimit) {

		final float baseFuelUnitPrice = 1f;

		// discharge price makes LNG slightly cheaper than BF.
		final float dischargePrice = 0.99f;
		final float cvValue = 1;
		// set charter out details.
		final int charterOutTimeDays = 10;

		final Schedule result = evaluateCharterOutScenario(dischargePrice, cvValue, baseFuelUnitPrice, charterOutTimeDays, heelLimit);

		return getJourneyAfterCharterOut(result);
	}

	/**
	 * If the heel is available and cheaper
	 */
	@Test
	public void heelAvailableCheaper() {

		final int heelLimit = 1000;
		final Journey j = testHeelWithCheaperLNG(heelLimit);
		FuelUsageAssertions.assertBaseFuelNotUsed(j);
	}

	/**
	 * Heel is only available for NBO, BF used to supplement.
	 */
	@Test
	public void heelNBOAvailableCheaper() {

		final int heelLimit = 500;
		final Journey j = testHeelWithCheaperLNG(heelLimit);
		FuelUsageAssertions.assertFBONotUsed(j);
	}

	/**
	 * LNG is not available so is not used even though it is cheaper.
	 */
	@Test
	public void heelCheaperButNotAvailable() {

		final int heelLimit = 0;
		final Journey j = testHeelWithCheaperLNG(heelLimit);
		FuelUsageAssertions.assertLNGNotUsed(j);
	}

	/**
	 * Check that for varying heel limits LNG usage never exceeds (but may equal) the amount of heel).
	 */
	@Ignore("Heel may not be exceeded with a capacity violation")
	@Test
	public void heelNotExceeded() {

		final int maxHeelLimit = 1250;

		for (int heelLimit = 0; heelLimit <= maxHeelLimit; heelLimit += 250) {

			System.err.println("Heel limit: " + heelLimit);

			final Journey j = testHeelWithCheaperLNG(heelLimit);

			// get the amount of heel used
			long LNGUsed = 0;
			for (final FuelQuantity fq : j.getFuels()) {
				if ((fq.getFuel() == Fuel.FBO) || (fq.getFuel() == Fuel.NBO)) {
					for (FuelAmount fa : fq.getAmounts()) {
						if (fa.getUnit() == FuelUnit.M3) {
							LNGUsed += fa.getQuantity();
						}
					}
				}
			}

			Assert.assertTrue("Heel not exceeded", LNGUsed <= heelLimit);
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
		final int fuelConsumptionPerDay = TimeUnitConvert.convertPerHourToPerDay(10);
		final int NBORatePerDay = TimeUnitConvert.convertPerHourToPerDay(5);

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

		final LNGScenarioModel scenario = ScenarioTools.createCharterOutScenario(distanceBetweenPorts, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, minSpeed, maxSpeed,
				capacity, ballastMinSpeed, ballastMinConsumption, ballastMaxSpeed, ballastMaxConsumption, ballastIdleConsumptionRate, ballastIdleNBORate, ballastNBORate, ladenMinSpeed,
				ladenMinConsumption, ladenMaxSpeed, ladenMaxConsumption, ladenIdleConsumptionRate, ladenIdleNBORate, ladenNBORate, pilotLightRate, charterOutTimeDays, heelLimit);

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);

		// print the Sequence (there should only be one Sequence)
		for (final Sequence seq : result.getSequences()) {
			ScenarioTools.printSequence(seq);
		}

		return result;
	}

	/**
	 * Assuming that a schedule has a charter out and a journey afterwards, this method will return the journey immediately after the charter out. If there is none an assert will fail.
	 * 
	 * @param charterOutSchedule
	 * @return
	 */
	private Journey getJourneyAfterCharterOut(final Schedule charterOutSchedule) {
		// only expect one sequence.
		Assert.assertTrue("Only one sequence", charterOutSchedule.getSequences().size() == 1);
		final Sequence seq = charterOutSchedule.getSequences().get(0);

		// get the event after the charter out.
		for (final Event e : seq.getEvents()) {

			if (e instanceof VesselEventVisit && ((VesselEventVisit) e).getVesselEvent() instanceof CharterOutEvent) {
				// got the charter out, now get the event after it and make sure it is a journey. If it is, return it.
				final int charterOutIndex = seq.getEvents().indexOf(e);
				final int nextEventIndex = charterOutIndex + 1;
				Assert.assertTrue("There is an event after the charter out", seq.getEvents().size() >= nextEventIndex);
				final Event eventAfterCharterOut = seq.getEvents().get(charterOutIndex + 1);
				Assert.assertTrue("Event after charter out is a journey", eventAfterCharterOut instanceof Journey);

				return (Journey) eventAfterCharterOut;
			}
		}

		Assert.fail("No charter out in sequence");
		return null;
	}
}