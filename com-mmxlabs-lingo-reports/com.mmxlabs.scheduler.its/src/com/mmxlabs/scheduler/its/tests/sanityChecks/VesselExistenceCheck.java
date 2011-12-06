/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.its.tests.sanityChecks;

import java.util.ArrayList;
import java.util.Arrays;

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

	private static final int dischargePrice = 1;
	private CustomScenarioCreator csc = new CustomScenarioCreator(dischargePrice);

	/**
	 * TODO Add other vessels (time charter spot charter).
	 * 
	 * Create a load of vessels of varying classes and store them in an ArrayList. Create a load of ports with varying distances. Create a load of cargos to different ports and with different
	 * durations. Check that the output contains all of the vessels in the ArrayList. <br>
	 * There are many more vessels than required (so a lot will be idle) to try and trip up the optimiser and ensure it doesn't remove vessels that aren't used.
	 */
	@Test
	public void test() {

		// this is the list of vessels to check against the output.
		final ArrayList<Vessel> inputVessels = new ArrayList<Vessel>();

		final int loadPrice = 1;
		final int cvValue = 10;

		// a list of ports to use in the scenario
		final Port[] ports = new Port[] { ScenarioTools.createPort("portA"), ScenarioTools.createPort("portB"), ScenarioTools.createPort("portC"), ScenarioTools.createPort("portD"),
				ScenarioTools.createPort("portE"), ScenarioTools.createPort("portF") };

		// Add the ports, and set the distances.
		SanityCheckTools.setPortDistances(csc, ports);

		// create a few vessels and add them to the list of vessels created.
		final int numOfClassOne = 3;
		final int numOfClassTwo = 7;
		final int numOfClassThree = 4;
		final int numOfClassFour = 6;
		final int numOfInputVessels = numOfClassOne + numOfClassTwo + numOfClassThree + numOfClassFour;

		// createVessels creates and adds the vessesl to the scenario.
		// Add the created vessels to the list of input vessels.
		inputVessels.addAll(Arrays.asList(csc.addVesselSimple("classOne", numOfClassOne, 10, 10, 1000000, 10, 10, 0, 500, false)));
		inputVessels.addAll(Arrays.asList(csc.addVesselSimple("classTwo", numOfClassTwo, 9, 15, 700000, 11, 9, 7, 0, false)));
		inputVessels.addAll(Arrays.asList(csc.addVesselSimple("classThree", numOfClassThree, 20, 25, 10000, 17, 14, 10, 1000, false)));
		inputVessels.addAll(Arrays.asList(csc.addVesselSimple("classFour", numOfClassFour, 15, 20, 150000, 20, 10, 5, 2000, true)));

		// create some cargos.
		SanityCheckTools.addCargos(csc, ports, loadPrice, dischargePrice, cvValue);

		final Scenario scenario = csc.buildScenario();

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);

		// print the legs to console
		for (CargoAllocation ca : result.getCargoAllocations())
			ScenarioTools.printCargoAllocation(ca.getName(), ca);

		// print each vessel's sequence
		ScenarioTools.printSequences(result);
		
		// check the output
		checkVesselExistence(result, inputVessels, numOfInputVessels);
	}
	
	/**
	 * Checks the output to make sure that the vessels from the input exist.
	 * @param result
	 * @param inputVessels
	 * @param expectedNumOfVessels
	 */
	private void checkVesselExistence(final Schedule result, final ArrayList<Vessel> inputVessels, final int expectedNumOfVessels) {

		// Check all vessels in the input exist in the output.
		int numOfVesselsInOutput = 0;
		for (AllocatedVessel av : result.getFleet()) {
			if (av instanceof FleetVessel) {
				FleetVessel fv = (FleetVessel) av;

				Assert.assertTrue("Input vessel exists in output", inputVessels.contains(fv.getVessel()));
				// remove the vessel - it should only exist once.
				inputVessels.remove(fv.getVessel());

				// count the number of vessels in the output
				numOfVesselsInOutput++;
			}
		}

		Assert.assertEquals("Number of vessels in input same as number of vessels in output", expectedNumOfVessels, numOfVesselsInOutput);
		Assert.assertEquals("All vessels were used in the output", inputVessels.size(), 0);
		
	}
}
