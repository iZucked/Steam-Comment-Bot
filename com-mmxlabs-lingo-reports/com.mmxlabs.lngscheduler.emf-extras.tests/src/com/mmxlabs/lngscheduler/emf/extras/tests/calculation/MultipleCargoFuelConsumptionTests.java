/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.calculation;

import java.util.Date;

import javax.management.timer.Timer;

import org.junit.Test;

import scenario.Scenario;
import scenario.port.Port;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;

import com.mmxlabs.demo.app.wizards.CustomScenarioCreator;

/**
 * @author Adam Semenenko
 * 
 */
public class MultipleCargoFuelConsumptionTests {

	private static final int dischargePrice = 10;

	@Test
	public void twoCargoTest() {

		CustomScenarioCreator csc = new CustomScenarioCreator(dischargePrice);

		final Port portA = csc.createPort("A");
		final Port portB = csc.createPort("B");
		final int distanceBetweenPorts = 100;
		csc.addPorts(portA, portB, distanceBetweenPorts);

		final String vesselClassName = "vc";
		final int numOfVesselsToCreate = 1;
		final float baseFuelUnitPrice = 10;
		final int speed = 10;
		final int capacity = 1000000;
		final int consumption = 10;
		final int NBORate = 10;
		final int pilotLightRate = 0;
		final int minHeelVolume = 0;
		csc.addVesselSimple(vesselClassName, numOfVesselsToCreate, baseFuelUnitPrice, speed, capacity, consumption, NBORate, pilotLightRate, minHeelVolume);

		// Set up start dates and durations.
		final Date cargoAStart = new Date(System.currentTimeMillis());
		final int cargoADuration = 10;
		// cargo B starts as soon as cargo A ends.
		final Date cargoBStart = new Date(cargoAStart.getTime() + Timer.ONE_HOUR * cargoADuration);
		final int cargoBDuration = 10;

		csc.addCargo("alpha", portA, portB, dischargePrice, 10, cargoAStart, cargoADuration);
		csc.addCargo("beta", portA, portB, dischargePrice, 10, cargoBStart, cargoBDuration);

		final Scenario scenario = csc.buildScenario();

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);

		for (CargoAllocation ca : result.getCargoAllocations())
			ScenarioTools.printScenario(ca.getName(), ca);
	}
}
