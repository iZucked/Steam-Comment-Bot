/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.calculation.singleEvent;

import org.junit.Test;

import com.mmxlabs.common.TimeUnitConvert;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.FuelUsageAssertions;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.mmxcore.MMXRootObject;

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
public class MinTravelHeelTest {

	/**
	 * Min heel is zero. Test that ballast leg is NBO, idle is cheaper on BF. TODO test result of non-zero min heel
	 */
	@Test
	public void testZeroMinHeel() {
		final String testName = "Test zero min heel";
		final int minHeelVolume = 0;

		final CargoAllocation a = testMinHeel(testName, minHeelVolume);

		// ballast travel is cheaper on NBO
		FuelUsageAssertions.assertFuelNotUsed(a.getBallastLeg().getFuels(), Fuel.BASE_FUEL);
		FuelUsageAssertions.assertFuelNotUsed(a.getBallastLeg().getFuels(), Fuel.FBO);
		FuelUsageAssertions.assertFuelUsed(a.getBallastLeg().getFuels(), Fuel.NBO, FuelUnit.M3);

		// ballast idle is cheaper on base fuel
		FuelUsageAssertions.assertFuelNotUsed(a.getBallastIdle().getFuels(), Fuel.FBO);
		FuelUsageAssertions.assertFuelNotUsed(a.getBallastIdle().getFuels(), Fuel.NBO);
		FuelUsageAssertions.assertFuelUsed(a.getBallastIdle().getFuels(), Fuel.BASE_FUEL, FuelUnit.MT);

	}

	/**
	 * Min heel is non-zero. Test that ballast leg is NBO, idle is cheaper on BF but NBO is used because min heel is left over
	 */
	@Test
	public void testNonZeroMinHeel() {
		final String testName = "Test non-zero min heel";
		// travel uses 1000 MT of LNG, so give 90 MT extra for idle (9 MT/hour * 10 hours = 90 MT)
		final int minHeelVolume = 100;

		final CargoAllocation a = testMinHeel(testName, minHeelVolume);

		// ballast travel is cheaper on NBO
		FuelUsageAssertions.assertFuelNotUsed(a.getBallastLeg().getFuels(), Fuel.BASE_FUEL);
		FuelUsageAssertions.assertFuelNotUsed(a.getBallastLeg().getFuels(), Fuel.FBO);
		FuelUsageAssertions.assertFuelUsed(a.getBallastLeg().getFuels(), Fuel.NBO, FuelUnit.M3);
		
		// ballast idle is cheaper on base fuel but NBO used from left over min heel.
		FuelUsageAssertions.assertFuelUsed(a.getBallastIdle().getFuels(), Fuel.NBO, FuelUnit.M3);
		// TODO: What about FBO?
//		FuelUsageAssertions.assertFuelNotUsed(a.getBallastIdle().getFuels(), Fuel.FBO);
		FuelUsageAssertions.assertFuelNotUsed(a.getBallastIdle().getFuels(), Fuel.BASE_FUEL);
	}

	/**
	 * Test the result of different min heel volumes.
	 * <p>
	 * The ballast leg is cheaper on NBO, but the ballast idle is cheaper on base fuel. If the vessel has a min heel large enough to provide enough NBO for the idle then NBO should be used on the
	 * idle. If the vessel doesn't have enough min heel then base fuel should be used on the idle.
	 */
	public CargoAllocation testMinHeel(final String testName, final int minHeelVolume) {

		// These are set up so that the ballast journey is cheaper on NBO, whilst the ballast idle is cheaper on base fuel.
		final int travelFuelConsumptionPerHour = 10;
		final int idleFuelConsumptionPerHour = 9;
		final int travelNBORatePerHour = 10;
		final int idleNBORatePerHour = 10;

		return test(testName, minHeelVolume, travelFuelConsumptionPerHour, idleFuelConsumptionPerHour, travelNBORatePerHour, idleNBORatePerHour);
	}

	private CargoAllocation test(final String testName, final int minHeelVolume, final int fuelTravelConsumptionPerHour, final int fuelIdleConsumptionPerHour, final int NBOTravelRatePerHour,
			final int NBOIdleRatePerHour) {

		// NBO can cover fuel consumption
		// final int fuelConsumptionPerDay = ScenarioTools.convertPerHourToPerDay(10);
		// final int NBORatePerDay = ScenarioTools.convertPerHourToPerDay(10);

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
		final float baseFuelUnitPrice = 1.1f;

		final int minSpeed = speed;
		final int maxSpeed = speed;
		final int capacity = 100000;
		final int ballastMinSpeed = speed;
		final int ballastMinConsumption = speed;
		final int ballastMaxSpeed = speed;
		final int ballastMaxConsumption = TimeUnitConvert.convertPerHourToPerDay(fuelTravelConsumptionPerHour);
		final int ballastIdleConsumptionRate = TimeUnitConvert.convertPerHourToPerDay(fuelIdleConsumptionPerHour);
		final int ballastIdleNBORate = TimeUnitConvert.convertPerHourToPerDay(NBOIdleRatePerHour);
		final int ballastNBORate = TimeUnitConvert.convertPerHourToPerDay(NBOTravelRatePerHour);
		final int ladenMinSpeed = speed;
		final int ladenMinConsumption = TimeUnitConvert.convertPerHourToPerDay(fuelTravelConsumptionPerHour);
		final int ladenMaxSpeed = speed;
		final int ladenMaxConsumption = TimeUnitConvert.convertPerHourToPerDay(fuelTravelConsumptionPerHour);
		final int ladenIdleConsumptionRate = TimeUnitConvert.convertPerHourToPerDay(fuelIdleConsumptionPerHour);
		final int ladenIdleNBORate = TimeUnitConvert.convertPerHourToPerDay(NBOIdleRatePerHour);
		final int ladenNBORate = TimeUnitConvert.convertPerHourToPerDay(NBOTravelRatePerHour);
		final int pilotLightRate = 0;

		final MMXRootObject scenario = ScenarioTools.createScenario(distanceBetweenPorts, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, minSpeed, maxSpeed, capacity,
				ballastMinSpeed, ballastMinConsumption, ballastMaxSpeed, ballastMaxConsumption, ballastIdleConsumptionRate, ballastIdleNBORate, ballastNBORate, ladenMinSpeed, ladenMinConsumption,
				ladenMaxSpeed, ladenMaxConsumption, ladenIdleConsumptionRate, ladenIdleNBORate, ladenNBORate, true, pilotLightRate, minHeelVolume);

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		ScenarioTools.printCargoAllocation(testName, a);

		return a;
	}
}
