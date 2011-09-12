/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.calculation;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import scenario.Scenario;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.FuelType;

/**
 * From case 179:
 * 
 * The first thing to check is that the fuel choices being made are really the cheapest, by creating some situations which are right on the boundary of costs. The main options that I see are

 * Ballast leg just cheaper to do on base fuel - there should be no heel around
 * Ballast travel just cheaper to do on NBO, but ballast idle just cheaper on base fuel
 * Ballast travel would be just cheaper on NBO, but pilot light costs are just enough to make basefuel cheaper.
 * Everything just cheaper on NBO
 * 
 * @author Adam Semenenko
 *
 */
public class FuelChoiceBoundaryTests {
	
	/**
	 * Check that the base fuel is just cheaper, and so FBO is never used.
	 */
	@Test
	public void testBoundaryConditionBaseFuelCheaper() {
		final float baseFuelUnitPrice = 1;
		// this value makes the NBO 1 unit more expensive than base fuel.
		final float dischargePrice = 1.01f;
		final float cvValue = 1;
		final int fuelConsumptionHours = 11;
		final int NBORateHours = 10;

		CargoAllocation a = testPriceConsumption("Base fuel just cheaper than FBO", baseFuelUnitPrice, dischargePrice, cvValue, fuelConsumptionHours, NBORateHours);

		// need to check that FBO is never used on any leg or idle.
		for (final FuelQuantity fq : a.getLadenLeg().getFuelUsage()) {
			if (fq.getFuelType() == FuelType.FBO)
				Assert.assertTrue("Laden leg never uses FBO", fq.getQuantity() == 0);
		}
		for (final FuelQuantity fq : a.getLadenIdle().getFuelUsage()) {
			if (fq.getFuelType() == FuelType.FBO)
				Assert.assertTrue("Laden idle never uses FBO", fq.getQuantity() == 0);
		}

		// Check there is no heel by checking there is no NBO used after discharge
		for (final FuelQuantity fq : a.getBallastLeg().getFuelUsage()) {
			if (fq.getFuelType() == FuelType.NBO)
				Assert.assertTrue("Ballast leg never uses NBO", fq.getQuantity() == 0);
		}
		for (final FuelQuantity fq : a.getBallastIdle().getFuelUsage()) {
			if (fq.getFuelType() == FuelType.NBO)
				Assert.assertTrue("Ballast idle never uses NBO", fq.getQuantity() == 0);
		}
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
		final int fuelConsumptionHours = 11;
		final int NBORateHours = 10;

		CargoAllocation a = testPriceConsumption("LNG just cheaper than base fuel", baseFuelUnitPrice, dischargePrice, cvValue, fuelConsumptionHours, NBORateHours);

		// need to check that base fuel is never used on any leg or idle.
		for (final FuelQuantity fq : a.getLadenLeg().getFuelUsage()) {
			if (fq.getFuelType() == FuelType.BASE_FUEL)
				Assert.assertTrue("Laden leg never uses base fuel", fq.getQuantity() == 0);
		}
		for (final FuelQuantity fq : a.getLadenIdle().getFuelUsage()) {
			if (fq.getFuelType() == FuelType.BASE_FUEL)
				Assert.assertTrue("Laden idle never uses base fuel", fq.getQuantity() == 0);
		}
		for (final FuelQuantity fq : a.getBallastLeg().getFuelUsage()) {
			if (fq.getFuelType() == FuelType.BASE_FUEL)
				Assert.assertTrue("Ballast leg never uses base fuel", fq.getQuantity() == 0);
		}
		for (final FuelQuantity fq : a.getBallastIdle().getFuelUsage()) {
			if (fq.getFuelType() == FuelType.BASE_FUEL)
				Assert.assertTrue("Ballast idle never uses base fuel", fq.getQuantity() == 0);
		}
	}

	/**
	 * Test the difference that prices and fuel consumption/nbo rates can make. Attributes for the laden and ballast legs are identical. The vessel can only travel at 10, the distances between all
	 * ports is 1000, and the travel time is 100 (meaning there is no idle time).
	 * 
	 * @return
	 */
	private CargoAllocation testPriceConsumption(final String name, final float baseFuelUnitPrice, final float dischargePrice, final float cvValue, final int fuelConsumptionHours,
			final int NBORateHours) {
		// Create a dummy scenario
		final int travelTime = 100;

		final float equivalenceFactor = 1;
		// for simplicity all speeds, fuel and NBO consumptions and rates are equal
		final int speed = 10;
		final int capacity = 1000000;

		final int fuelConsumptionDays = (int) TimeUnit.DAYS.toHours(fuelConsumptionHours);
		final int NBORateDays = (int) TimeUnit.DAYS.toHours(NBORateHours);

		final int portDistance = 1000;
		final boolean useDryDock = true;
		final int pilotLightRate = 0;

		final Scenario scenario = ScenarioTools.createScenario(portDistance, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, speed, speed, capacity, speed, fuelConsumptionDays, speed,
				fuelConsumptionDays, fuelConsumptionDays, NBORateDays, NBORateDays, speed, fuelConsumptionDays, speed, fuelConsumptionDays, fuelConsumptionDays, NBORateDays, NBORateDays, useDryDock,
				pilotLightRate);
		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);
		// check result is how we expect it to be
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		ScenarioTools.printScenario(name, a);

		return a;
	}

	/**
	 * Test that ballast leg is cheaper on NBO, but ballast idle is cheaper on base fuel.
	 */
	@Test
	public void testTravelCheaperOnNBO_IdleCheaperOnBase() {

		final int pilotLightRate = 0;

		CargoAllocation a = testPilotLight("Test ballast cheaper on NBO, ballast idle cheaper on base", pilotLightRate);

		for (final FuelQuantity fq : a.getBallastLeg().getFuelUsage()) {
			if (fq.getFuelType() != FuelType.NBO)
				Assert.assertTrue("Ballast leg only uses NBO", fq.getQuantity() == 0);
		}
		for (final FuelQuantity fq : a.getBallastIdle().getFuelUsage()) {
			if (fq.getFuelType() == FuelType.BASE_FUEL)
				Assert.assertTrue("Ballast idle uses base fuel", fq.getQuantity() > 0);
		}
	}

	/**
	 * Test that ballast leg would be cheaper on NBO, but the pilot light costs are just enough to make base fuel cheaper.
	 */
	@Test
	public void testTravelCheaperOnBaseBecausePilotLight() {

		final int pilotLightRate = 3;

		CargoAllocation a = testPilotLight("Test ballast cheaper on base because of pilot light", pilotLightRate);

		for (final FuelQuantity fq : a.getBallastLeg().getFuelUsage()) {
			if (fq.getFuelType() != FuelType.BASE_FUEL)
				Assert.assertTrue("Ballast leg only uses base fuel", fq.getQuantity() == 0);
		}
	}

	/**
	 * Test that ballast leg would be cheaper on NBO, even though the pilot light is non-zero.
	 */
	@Test
	public void testTravelCheaperOnBaseDespitePilotLight() {

		final int pilotLightRate = 2;

		CargoAllocation a = testPilotLight("Test ballast cheaper on base because of pilot light", pilotLightRate);

		for (final FuelQuantity fq : a.getBallastLeg().getFuelUsage()) {
			if (fq.getFuelType() != FuelType.FBO)
				Assert.assertTrue("Ballast leg uses NBO and base fuel", fq.getQuantity() > 0);
		}
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
		final int travelFuelConsumption = 10;
		final int idleFuelConsumption = 9;
		final int travelNBORate = 10;
		final int idleNBORate = 10;
		final float baseFuelUnitPrice = 1.01f;
		final float dischargePrice = 1f;

		return test(nameOfTest, travelFuelConsumption, travelNBORate, idleFuelConsumption, idleNBORate, baseFuelUnitPrice, dischargePrice, pilotLightRate);
	}

	/**
	 * Test that ballast travel is cheaper on NBO, but ballast idle is cheaper on base fuel.
	 * <p>
	 * This test gives 10 hours of idle time after ballast and laden legs.
	 */
	private CargoAllocation test(final String name, final int travelFuelConsumptionHours, final int travelNBORateHours, final int idleFuelConsumptionHours, final int idleNBORateHours,
			final float baseFuelUnitPrice, final float dischargePrice, final int pilotLightRate) {
		// Create a dummy scenario

		// this gives 100 hours of travel and idle time for ballast and laden.
		final int travelTime = 200;

		final float cvValue = 1;

		final float equivalenceFactor = 1;
		// for simplicity all speeds, fuel and NBO consumptions and rates are equal
		final int speed = 10;
		final int capacity = 1000000;

		final int travelFuelConsumption = (int) TimeUnit.DAYS.toHours(travelFuelConsumptionHours);
		final int travelNBORate = (int) TimeUnit.DAYS.toHours(travelNBORateHours);
		final int idleFuelConsumption = (int) TimeUnit.DAYS.toHours(idleFuelConsumptionHours);
		final int idleNBORate = (int) TimeUnit.DAYS.toHours(idleNBORateHours);

		final int portDistance = 1000;
		final boolean useDryDock = true;

		final Scenario scenario = ScenarioTools.createScenario(portDistance, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, speed, speed, capacity, speed, travelFuelConsumption, speed,
				travelFuelConsumption, idleFuelConsumption, idleNBORate, travelNBORate, speed, travelFuelConsumption, speed, travelFuelConsumption, idleFuelConsumption, idleNBORate, travelNBORate,
				useDryDock, pilotLightRate);
		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);
		// check result is how we expect it to be
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		ScenarioTools.printScenario(name, a);

		return a;
	}

}
