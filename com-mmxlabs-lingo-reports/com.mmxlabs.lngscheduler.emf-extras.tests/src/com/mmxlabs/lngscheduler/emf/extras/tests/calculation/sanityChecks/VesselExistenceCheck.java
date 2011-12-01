/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.calculation.sanityChecks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import scenario.Scenario;
import scenario.fleet.Vessel;
import scenario.port.Port;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;
import scenario.schedule.fleetallocation.AllocatedVessel;
import scenario.schedule.fleetallocation.FleetVessel;

import com.mmxlabs.demo.app.wizards.CustomScenarioCreator;
import com.mmxlabs.lngscheduler.emf.extras.tests.calculation.ScenarioTools;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?252">Case 252: Check vessels in input exist in output.</a>
 * 
 * @author Adam
 * 
 */
public class VesselExistenceCheck {

	private CustomScenarioCreator csc;

	/**
	 * TODO Add other vessels (time charter spot charter).
	 */
	@Test
	public void test() {

		final ArrayList<Vessel> inputVessels = new ArrayList<Vessel>();

		final int dischargePrice = 1;
		final int loadPrice = 1;
		final int cvValue = 10;

		csc = new CustomScenarioCreator(dischargePrice);

		final Port[] ports = new Port[] { ScenarioTools.createPort("portA"), ScenarioTools.createPort("portB"), ScenarioTools.createPort("portC"), ScenarioTools.createPort("portD"),
				ScenarioTools.createPort("portE"), ScenarioTools.createPort("portF") };

		// Add the ports, and set the distances.
		setPortDistances(ports);
		
		// create a few vessels and add them to the list of vessels created.
		final int numOfClassOne = 3;
		final int numOfClassTwo = 7;
		final int numOfClassThree = 4;
		final int numOfClassFour = 6;
		final int numOfInputVessels = numOfClassOne + numOfClassTwo + numOfClassThree + numOfClassFour;
		
		inputVessels.addAll(Arrays.asList(createVessels("classOne", numOfClassOne, 10, 10, 1000000, 10, 10, 0, 500)));
		inputVessels.addAll(Arrays.asList(createVessels("classTwo", numOfClassTwo, 9, 15, 700000, 11, 9, 7, 0)));
		inputVessels.addAll(Arrays.asList(createVessels("classThree", numOfClassThree, 20, 25, 10000, 17, 14, 10, 1000)));
		inputVessels.addAll(Arrays.asList(createVessels("classFour", numOfClassFour, 15, 20, 150000, 20, 10, 5, 2000)));
		

		// create some cargos.
		addCargos(ports, loadPrice, dischargePrice, cvValue);

		final Scenario scenario = csc.buildScenario();

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);

		// print the legs to console
		for (CargoAllocation ca : result.getCargoAllocations())
			ScenarioTools.printCargoAllocation(ca.getName(), ca);

		ScenarioTools.printSequences(result);

		int numOfVesselsInOutput = 0;
		for (AllocatedVessel av : result.getFleet()) {
			if (av instanceof FleetVessel) {
				FleetVessel fv = (FleetVessel) av;

				Assert.assertTrue("Input vessel exists in output", inputVessels.contains(fv.getVessel()));

				if (inputVessels.contains(fv.getVessel()))
					numOfVesselsInOutput++;
			}
		}
		
		Assert.assertEquals("Number of vessels in input same as number of vessels in output", numOfInputVessels, numOfVesselsInOutput);
	}

	private void setPortDistances(Port[] ports) {

		int distance = 10;

		for (Port portX : ports) {
			for (Port portY : ports) {
				if (!portX.equals(portY)) {

					csc.addPorts(portX, portY, distance);

					distance += 10;
				} else {

					distance -= distance / 2;
				}
			}
		}
	}

	private Vessel[] createVessels(final String vesselClassOne, final int numOfClassOne, final float baseFuelUnitPriceClassOne, final int speedClassOne, final int capacityClassOne,
			final int consumptionClassOne, final int NBORateClassOne, final int pilotLightRateClassOne, final int minHeelVolumeClassOne) {

		final Vessel[] vesselsOfClassOne = csc.addVesselSimple(vesselClassOne, numOfClassOne, baseFuelUnitPriceClassOne, speedClassOne, capacityClassOne, consumptionClassOne, NBORateClassOne,
				pilotLightRateClassOne, minHeelVolumeClassOne);

		return vesselsOfClassOne;
	}

	private void addCargos(Port[] ports, final int loadPrice, final float dischargePrice, final float cvValue) {

		Date cargoStart = new Date(System.currentTimeMillis());
		int duration = 50;

		for (Port portX : ports) {
			for (Port portY : ports) {
				if (!portX.equals(portY)) {

					csc.addCargo(portX.getName() + " to " + portY.getName() + " in " + duration + ".", portX, portY, loadPrice, dischargePrice, cvValue, cargoStart, duration);

					duration += 25;
					cargoStart.setTime(cargoStart.getTime() + TimeUnit.DAYS.toMillis(1));

				} else {

					duration -= duration / 2;
				}
			}
		}

	}
}
