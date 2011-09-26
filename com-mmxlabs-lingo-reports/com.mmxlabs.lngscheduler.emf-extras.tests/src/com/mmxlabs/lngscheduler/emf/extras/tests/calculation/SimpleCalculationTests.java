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
 * This class contains some whole-system tests which check the fuel choices on a single cargo.
 * 
 * TODO test canal selection; make fuel choice values more precise (find tipping points on paper and write test)
 * 
 * @author hinton
 * 
 */
public class SimpleCalculationTests {

	// Create a dummy scenario
	private static final float baseFuelPriceCheap = 0; // $/MT -- very cheap, normally around 700
	private static final float baseFuelPriceNormal = 700; // $/MT -- normal
	@SuppressWarnings("unused")
	private static final float baseFuelPriceExpensive = 70000; // $/MT
	private static final float dischargePriceCheap = 1.0f; // $/mmbtu - cheap
	@SuppressWarnings("unused")
	private static final float dischargePriceNormal = 7.0f; // $/mmbtu // normal
	private static final float dischargePriceExpensive = 7000.0f; // $/mmbtu // very expensive, normally around 7

	/**
	 * Test that the ballast leg and idle never use base fuel.
	 */
	@Test
	public void testLNGSelection() {
		// Create a dummy scenario
		final float baseFuelPrice = baseFuelPriceNormal; // $/MT -- normal
		final float dischargePrice = dischargePriceCheap; // $/mmbtu - cheap
		final float cvValue = 22.8f;
		final int travelTime = (int) TimeUnit.DAYS.toHours(3);

		final Scenario scenario = createSimpleScenario(baseFuelPrice, dischargePrice, cvValue, travelTime);
		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);
		// check result is how we expect it to be
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		ScenarioTools.printScenario("testLNGSelection", a);

		// on the laden leg we always use NBO; decision time is on the ballast leg
		for (final FuelQuantity fq : a.getBallastLeg().getFuelUsage()) {
			Assert.assertTrue("Ballast leg never uses base", fq.getQuantity() == 0 || fq.getFuelType() != FuelType.BASE_FUEL);
		}

		for (final FuelQuantity fq : a.getBallastIdle().getFuelUsage()) {
			Assert.assertTrue("Ballast idle never uses base", fq.getQuantity() == 0 || fq.getFuelType() != FuelType.BASE_FUEL);
		}
	}

	/**
	 * Test that the ballast leg and idle only use base.
	 */
	@Test
	public void testBaseSelection() {
		// Create a dummy scenario
		final float baseFuelPrice = baseFuelPriceCheap; // $/MT -- very cheap, normally around 700
		final float dischargePrice = dischargePriceExpensive; // $/mmbtu // very expensive, normally around 7
		final float cvValue = 22.8f;
		final int travelTime = (int) TimeUnit.DAYS.toHours(3);

		final Scenario scenario = createSimpleScenario(baseFuelPrice, dischargePrice, cvValue, travelTime);
		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);
		// check result is how we expect it to be
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		ScenarioTools.printScenario("testBaseSelection", a);

		// on the laden leg we always use NBO; decision time is on the ballast leg
		for (final FuelQuantity fq : a.getBallastLeg().getFuelUsage()) {
			Assert.assertTrue("Ballast leg only uses base", fq.getQuantity() == 0 || fq.getFuelType() == FuelType.BASE_FUEL);
		}

		for (final FuelQuantity fq : a.getBallastIdle().getFuelUsage()) {
			Assert.assertTrue("Ballast idle only uses base", fq.getQuantity() == 0 || fq.getFuelType() == FuelType.BASE_FUEL);
		}
	}
	
	/**
	 * Create a simple scenario which contains a vessel + vessel class a couple of ports and a canal, etc.
	 * 
	 * Most tests will call this, and then change some price parameters in the result
	 * 
	 * @param dischargePrice
	 * 
	 * @return
	 */
	private Scenario createSimpleScenario(final float baseFuelUnitPrice, final float dischargePrice, final float cvValue, final int travelTime) {

		// 'magic' numbers that could be set in the arguments.
		// vessel class
		final float equivalenceFactor = 0.5f;
		final int minSpeed = 12;
		final int maxSpeed = 20;
		final int capacity = 150000;
		final int pilotLightRate = 0;
		final int cooldownTime = 0;
		final int warmupTime = Integer.MAX_VALUE;
		final int cooldownVolume = 0;
		final int minHeelVolume = 0;
		final int spotCharterCount = 0;
		final double fillCapacity = 1.0;
		// ballast
		final int ballastMinSpeed = 12;
		final int ballastMinConsumption = 80;
		final int ballastMaxSpeed = 20;
		final int ballastMaxConsumption = 200;
		final int ballastIdleConsumptionRate = 15;
		final int ballastIdleNBORate = 100;
		final int ballastNBORate = 180;
		// laden
		final int ladenMinSpeed = 12;
		final int ladenMinConsumption = 100;
		final int ladenMaxSpeed = 20;
		final int ladenMaxConsumption = 230;
		final int ladenIdleConsumptionRate = 15;
		final int ladenIdleNBORate = 180;
		final int ladenNBORate = 200;
		// ports
		final int distanceBetweenPorts = 1000;
		// load and discharge prices and quantities
		final int loadPrice = 1000;
		final int loadMaxQuantity = 100000;
		final int dischargeMaxQuantity = 100000;

		final boolean useDryDock = true;

		return ScenarioTools.createScenario(distanceBetweenPorts, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, minSpeed, maxSpeed, capacity, ballastMinSpeed,
				ballastMinConsumption, ballastMaxSpeed, ballastMaxConsumption, ballastIdleConsumptionRate, ballastIdleNBORate, ballastNBORate, ladenMinSpeed, ladenMinConsumption, ladenMaxSpeed,
				ladenMaxConsumption, ladenIdleConsumptionRate, ladenIdleNBORate, ladenNBORate, useDryDock, minHeelVolume, pilotLightRate);
	}
}
