/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.calculation;

import org.junit.Test;

import scenario.Scenario;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;
import scenario.schedule.Sequence;
import scenario.schedule.events.ScheduledEvent;

/**
 * @author Adam Semenenko
 * 
 */
public class HeelOutOfCharterOutTests {

	@Test
	public void test() {

		final int speed = 10;
		final int fuelConsumption = ScenarioTools.convertPerHourToPerDay(10);
		final int NBORate = ScenarioTools.convertPerHourToPerDay(5);

		final int distanceBetweenPorts = 1000;
		final float baseFuelUnitPrice = 1;
		final float dischargePrice = 2;
		final float cvValue = 1;
		final int travelTime = 100;
		final float equivalenceFactor = 1;

		final int minSpeed = speed;
		final int maxSpeed = speed;
		final int capacity = 100000;
		final int ballastMinSpeed = speed;
		final int ballastMinConsumption = speed;
		final int ballastMaxSpeed = speed;
		final int ballastMaxConsumption = fuelConsumption;
		final int ballastIdleConsumptionRate = fuelConsumption;
		final int ballastIdleNBORate = NBORate;
		final int ballastNBORate = NBORate;
		final int ladenMinSpeed = speed;
		final int ladenMinConsumption = fuelConsumption;
		final int ladenMaxSpeed = speed;
		final int ladenMaxConsumption = fuelConsumption;
		final int ladenIdleConsumptionRate = fuelConsumption;
		final int ladenIdleNBORate = NBORate;
		final int ladenNBORate = NBORate;
		final int pilotLightRate = 0;

		final int charterOutTime = 100;
		final int heelLimit = 100;

		Scenario scenario = ScenarioTools.createCharterOutScenario(distanceBetweenPorts, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, minSpeed, maxSpeed, capacity,
				ballastMinSpeed, ballastMinConsumption, ballastMaxSpeed, ballastMaxConsumption, ballastIdleConsumptionRate, ballastIdleNBORate, ballastNBORate, ladenMinSpeed, ladenMinConsumption,
				ladenMaxSpeed, ladenMaxConsumption, ladenIdleConsumptionRate, ladenIdleNBORate, ladenNBORate, pilotLightRate, charterOutTime, heelLimit);

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);

		System.err.println("Result: " + result);
		
		for (Sequence seq : result.getSequences()) {
			System.err.println("Seq: " + seq.toString());
			for (ScheduledEvent e : seq.getEvents())
				System.err.println("\tEvent: " + e.toString());
		}

		System.err.println("Num of cargo allocations: " + result.getCargoAllocations().size());
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		ScenarioTools.printScenario("CO test", a);

	}

}
