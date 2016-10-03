/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.calculation.singleEvent;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.TimeUnitConvert;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.FuelUsageAssertions;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;

/**
 * From <a href="https://mmxlabs.fogbugz.com/default.asp?179">case 179</a>:
 * 
 * The first thing to check is that the fuel choices being made are really the cheapest, by creating some situations which are right on the boundary of costs. The main options that I see are
 * 
 * Ballast leg just cheaper to do on base fuel - there should be no heel around Ballast travel just cheaper to do on NBO, but ballast idle just cheaper on base fuel Ballast travel would be just
 * cheaper on NBO, but pilot light costs are just enough to make basefuel cheaper. Everything just cheaper on NBO
 * 
 * @author Adam Semenenko
 * 
 */
@RunWith(value = ShiroRunner.class)
public class FuelChoiceBoundaryTest {

	/**
	 * Check that the base fuel is just cheaper, and so FBO is never used.
	 */
	@Test
	public void testBoundaryConditionBaseFuelCheaper() {
		final float baseFuelUnitPrice = 1;
		// this value makes the NBO 1 unit more expensive than base fuel.
		final float dischargePrice = 1.01f;
		final float cvValue = 1;
		final int fuelConsumptionPerHour = 11;
		final int NBORatePerHour = 10;

		final SimpleCargoAllocation a = new SimpleCargoAllocation(testPriceConsumption("Base fuel just cheaper than FBO", baseFuelUnitPrice, dischargePrice, cvValue, fuelConsumptionPerHour,
				NBORatePerHour));

		// need to check that FBO is never used on any leg or idle.
		FuelUsageAssertions.assertFuelNotUsed(a.getLadenLeg().getFuels(), Fuel.FBO);

		// Check there is no heel by checking there is no NBO used after discharge
		FuelUsageAssertions.assertFuelNotUsed(a.getBallastLeg().getFuels(), Fuel.NBO);
		FuelUsageAssertions.assertFuelNotUsed(a.getBallastIdle().getFuels(), Fuel.NBO);
	}

	/**
	 * Check that the LNG is just cheaper, and so base fuel is never used.
	 */
	@Test
	public void testBoundaryConditionNBOCheaper() {
		// this value makes the base fuel 1 unit more expensive than NBO.
		final float baseFuelUnitPrice = 1.01f;
		final float dischargePrice = 1;
		final float cvValue = 1;
		final int fuelConsumptionPerHour = 11;
		final int NBORatePerHour = 10;

		final SimpleCargoAllocation a = new SimpleCargoAllocation(testPriceConsumption("LNG just cheaper than base fuel", baseFuelUnitPrice, dischargePrice, cvValue, fuelConsumptionPerHour,
				NBORatePerHour));

		// need to check that base fuel is never used on any leg or idle.
		FuelUsageAssertions.assertFuelNotUsed(a.getLadenLeg().getFuels(), Fuel.BASE_FUEL);
		FuelUsageAssertions.assertFuelNotUsed(a.getLadenIdle().getFuels(), Fuel.BASE_FUEL);

		FuelUsageAssertions.assertFuelNotUsed(a.getBallastLeg().getFuels(), Fuel.BASE_FUEL);
		FuelUsageAssertions.assertFuelNotUsed(a.getBallastIdle().getFuels(), Fuel.BASE_FUEL);
	}

	/**
	 * Test the difference that prices and fuel consumption/nbo rates can make. Attributes for the laden and ballast legs are identical. The vessel can only travel at 10, the distances between all
	 * ports is 1000, and the travel time is 100 (meaning there is no idle time).
	 * 
	 * @return
	 */
	private CargoAllocation testPriceConsumption(final String name, final float baseFuelUnitPrice, final float dischargePrice, final float cvValue, final int fuelConsumptionPerHour,
			final int NBORatePerHour) {
		// Create a dummy scenario
		final int travelTime = 100;

		final float equivalenceFactor = 1;
		// for simplicity all speeds, fuel and NBO consumptions and rates are equal
		final int speed = 10;
		final int capacity = 1000000;

		final int fuelTravelConsumptionPerDay = TimeUnitConvert.convertPerHourToPerDay(fuelConsumptionPerHour);
		final int NBORatePerDay = TimeUnitConvert.convertPerHourToPerDay(NBORatePerHour);

		final int fuelIdleConsumptionPerDay = NBORatePerDay;

		final int portDistance = 1000;
		final boolean useDryDock = true;
		final int pilotLightRate = 0;
		final int minHeelVolume = 0;

		final LNGScenarioModel scenario = ScenarioTools.createScenario(portDistance, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, speed, speed, capacity, speed,
				fuelTravelConsumptionPerDay, speed, fuelTravelConsumptionPerDay, fuelIdleConsumptionPerDay, NBORatePerDay, NBORatePerDay, speed, fuelTravelConsumptionPerDay, speed,
				fuelTravelConsumptionPerDay, fuelIdleConsumptionPerDay, NBORatePerDay, NBORatePerDay, useDryDock, pilotLightRate, minHeelVolume);
		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);
		// check result is how we expect it to be
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		ScenarioTools.printCargoAllocation(name, a);

		return a;
	}

	/**
	 * Test that ballast leg is cheaper on NBO, but ballast idle is cheaper on base fuel.
	 */
	@Test
	public void testTravelCheaperOnNBO_IdleCheaperOnBase() {

		final int pilotLightRate = 0;

		final SimpleCargoAllocation a = new SimpleCargoAllocation(testPilotLight("Test ballast cheaper on NBO, ballast idle cheaper on base", pilotLightRate));

		FuelUsageAssertions.assertFuelNotUsed(a.getBallastLeg().getFuels(), Fuel.BASE_FUEL);
		FuelUsageAssertions.assertFuelNotUsed(a.getBallastLeg().getFuels(), Fuel.FBO);
		FuelUsageAssertions.assertFuelUsed(a.getBallastLeg().getFuels(), Fuel.NBO, FuelUnit.M3);

		FuelUsageAssertions.assertFuelUsed(a.getBallastIdle().getFuels(), Fuel.BASE_FUEL, FuelUnit.MT);
	}

	/**
	 * Test that ballast leg would be cheaper on NBO, but the pilot light costs are just enough to make base fuel cheaper.
	 */
	@Test
	public void testTravelCheaperOnBaseBecausePilotLight() {

		final int pilotLightRate = 3;

		final SimpleCargoAllocation a = new SimpleCargoAllocation(testPilotLight("Test ballast cheaper on base because of pilot light", pilotLightRate));

		FuelUsageAssertions.assertFuelNotUsed(a.getBallastLeg().getFuels(), Fuel.NBO);
		FuelUsageAssertions.assertFuelNotUsed(a.getBallastLeg().getFuels(), Fuel.FBO);
		FuelUsageAssertions.assertFuelUsed(a.getBallastLeg().getFuels(), Fuel.BASE_FUEL, FuelUnit.MT);
	}

	/**
	 * Test that ballast leg would be cheaper on NBO, even though the pilot light is non-zero.
	 */
	@Test
	public void testTravelCheaperOnBaseDespitePilotLight() {

		final int pilotLightRate = 2;

		final SimpleCargoAllocation a = new SimpleCargoAllocation(testPilotLight("Test ballast cheaper on base because of pilot light", pilotLightRate));

		FuelUsageAssertions.assertFuelNotUsed(a.getBallastLeg().getFuels(), Fuel.FBO);
		FuelUsageAssertions.assertFuelUsed(a.getBallastLeg().getFuels(), Fuel.NBO, FuelUnit.M3);
		FuelUsageAssertions.assertFuelUsed(a.getBallastLeg().getFuels(), Fuel.BASE_FUEL, FuelUnit.MT);
	}

	/**
	 * Test the difference a pilot light can make. With a zero pilot light rate the ballast journey will be cheaper on NBO, whilst ballast idle will be cheaper on base fuel.
	 * 
	 * @param nameOfTest
	 * @param pilotLightRate
	 * @return
	 */
	private CargoAllocation testPilotLight(final String nameOfTest, final int pilotLightRate) {

		// These are set up so that the ballast journey is cheaper on NBO, whilst the ballast idle is cheaper on base fuel.
		final int travelFuelConsumptionPerHour = 10;
		final int idleFuelConsumptionPerHour = 9;
		final int travelNBORatePerHour = 10;
		final int idleNBORatePerHour = 10;
		final float baseFuelUnitPrice = 1.01f;
		final float dischargePrice = 1f;

		return test(nameOfTest, travelFuelConsumptionPerHour, travelNBORatePerHour, idleFuelConsumptionPerHour, idleNBORatePerHour, baseFuelUnitPrice, dischargePrice, pilotLightRate);
	}

	/**
	 * This test gives 100 hours of idle time after ballast and laden legs.
	 */
	private CargoAllocation test(final String testName, final int travelFuelConsumptionPerHour, final int travelNBORatePerHour, final int idleFuelConsumptionPerHour, final int idleNBORatePerHour,
			final float baseFuelUnitPrice, final float dischargePrice, final int pilotLightRate) {
		// Create a dummy scenario

		// this gives 100 hours of travel and idle time for ballast and laden.
		final int travelTime = 200;

		final float cvValue = 1;

		final float equivalenceFactor = 1;
		// for simplicity all speeds, fuel and NBO consumptions and rates are equal
		final int speed = 10;
		final int capacity = 1000000;

		final int travelFuelConsumptionPerDay = TimeUnitConvert.convertPerHourToPerDay(travelFuelConsumptionPerHour);
		final int travelNBORatePerDay = TimeUnitConvert.convertPerHourToPerDay(travelNBORatePerHour);
		final int idleFuelConsumptionPerDay = TimeUnitConvert.convertPerHourToPerDay(idleFuelConsumptionPerHour);
		final int idleNBORatePerDay = TimeUnitConvert.convertPerHourToPerDay(idleNBORatePerHour);

		final int portDistance = 1000;
		final int minHeelVolume = 0;
		final boolean useDryDock = true;

		final LNGScenarioModel scenario = ScenarioTools.createScenario(portDistance, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, speed, speed, capacity, speed,
				travelFuelConsumptionPerDay, speed, travelFuelConsumptionPerDay, idleFuelConsumptionPerDay, idleNBORatePerDay, travelNBORatePerDay, speed, travelFuelConsumptionPerDay, speed,
				travelFuelConsumptionPerDay, idleFuelConsumptionPerDay, idleNBORatePerDay, travelNBORatePerDay, useDryDock, pilotLightRate, minHeelVolume);
		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);
		// check result is how we expect it to be
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		ScenarioTools.printCargoAllocation(testName, a);

		return a;
	}

}
