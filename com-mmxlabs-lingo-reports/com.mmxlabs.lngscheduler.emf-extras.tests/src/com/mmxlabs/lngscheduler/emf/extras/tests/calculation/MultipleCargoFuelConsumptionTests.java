/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.calculation;

import java.util.Date;

import javax.management.timer.Timer;

import junit.framework.Assert;

import org.junit.Test;

import scenario.Scenario;
import scenario.port.Port;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.FuelType;

import com.mmxlabs.common.TimeUnitConvert;
import com.mmxlabs.demo.app.wizards.CustomScenarioCreator;

/**
 * @author Adam Semenenko
 * 
 */
public class MultipleCargoFuelConsumptionTests {

	private static final int dischargePrice = 1;
	private static final int loadPrice = 1;

	/**
	 * One vessel must take two cargos from port A to port B. NBO should be cheaper always.
	 */
	@Test
	public void twoCargoTest() {

		CustomScenarioCreator csc = new CustomScenarioCreator(dischargePrice);

		final Port portA = csc.createPort("A");
		final Port portB = csc.createPort("B");
		final int distanceBetweenPorts = 100;
		csc.addPorts(portA, portB, distanceBetweenPorts);

		final String vesselClassName = "vc";
		final int numOfVesselsToCreate = 1;
		final float baseFuelUnitPrice = 10.01f;
		final int speed = 10;
		final int capacity = 1000000;
		final int consumption = TimeUnitConvert.convertPerHourToPerDay(10);
		final int NBORate = TimeUnitConvert.convertPerHourToPerDay(10);
		final int pilotLightRate = 0;
		final int minHeelVolume = 0;
		csc.addVesselSimple(vesselClassName, numOfVesselsToCreate, baseFuelUnitPrice, speed, capacity, consumption, NBORate, pilotLightRate, minHeelVolume);

		// Set up start dates and durations.
		final Date cargoAStart = new Date(System.currentTimeMillis());
		final int cargoADuration = 10;
		// cargo B starts as soon as cargo A ends.
		final Date cargoBStart = new Date(cargoAStart.getTime() + Timer.ONE_HOUR * cargoADuration);
		final int cargoBDuration = 10;

		csc.addCargo("alpha", portA, portB, loadPrice, dischargePrice, 10, cargoAStart, cargoADuration);
		csc.addCargo("beta", portA, portB, loadPrice, dischargePrice, 10, cargoBStart, cargoBDuration);

		// add a dry dock so ballast idle doesn't take ages.
		final Date cargoBEnd = new Date(cargoBStart.getTime() + Timer.ONE_HOUR * cargoBDuration);
		csc.addDryDock(portA, cargoBEnd);
		
		final Scenario scenario = csc.buildScenario();

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);


		for (CargoAllocation ca : result.getCargoAllocations())
			ScenarioTools.printScenario(ca.getName(), ca);
		
		// add assertions on results
		for (CargoAllocation ca : result.getCargoAllocations()) {
			// expect only NBO to be used always

			// check the laden journey
			for (final FuelQuantity fq : ca.getLadenLeg().getFuelUsage()) {
				if (fq.getFuelType() == FuelType.FBO)
					Assert.assertTrue("Laden leg never uses FBO", fq.getQuantity() == 0);
				if (fq.getFuelType() == FuelType.BASE_FUEL)
					Assert.assertTrue("Laden leg never uses base fuel", fq.getQuantity() == 0);
				if (fq.getFuelType() == FuelType.NBO)
					Assert.assertTrue("Laden leg uses NBO", fq.getQuantity() > 0);
			}
			for (final FuelQuantity fq : ca.getLadenIdle().getFuelUsage()) {
				if (fq.getFuelType() == FuelType.FBO)
					Assert.assertTrue("Laden idle never uses FBO", fq.getQuantity() == 0);
				if (fq.getFuelType() == FuelType.BASE_FUEL)
					Assert.assertTrue("Laden idle never uses base fuel", fq.getQuantity() == 0);
				if (fq.getFuelType() == FuelType.NBO)
					Assert.assertTrue("Laden idle uses NBO", fq.getQuantity() > 0);
			}

			// check the ballast journey
			for (final FuelQuantity fq : ca.getBallastLeg().getFuelUsage()) {
				if (fq.getFuelType() == FuelType.FBO)
					Assert.assertTrue("Ballast leg never uses FBO", fq.getQuantity() == 0);
				if (fq.getFuelType() == FuelType.BASE_FUEL)
					Assert.assertTrue("Ballast leg never uses base fuel", fq.getQuantity() == 0);
				if (fq.getFuelType() == FuelType.NBO)
					Assert.assertTrue("Ballast leg uses NBO", fq.getQuantity() > 0);
			}
			for (final FuelQuantity fq : ca.getBallastIdle().getFuelUsage()) {
				if (fq.getFuelType() == FuelType.FBO)
					Assert.assertTrue("Ballast idle never uses FBO", fq.getQuantity() == 0);
				if (fq.getFuelType() == FuelType.BASE_FUEL)
					Assert.assertTrue("Ballast idle never uses base fuel", fq.getQuantity() == 0);
				if (fq.getFuelType() == FuelType.NBO)
					Assert.assertTrue("Ballast idle uses NBO", fq.getQuantity() > 0);
			}
		}
	}
}
