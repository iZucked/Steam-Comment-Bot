/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
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
import scenario.schedule.fleetallocation.SpotVessel;

import com.mmxlabs.demo.app.wizards.CustomScenarioCreator;
import com.mmxlabs.scheduler.its.tests.calculation.ScenarioTools;

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

		final int numOfSpotCharterClassOne = 10;
		final int numOfSpotCharterClassTwo = 0;
		final int numOfSpotCharterClassThree = 5;
		final int numOfSpotCharterClassFour = 0;
		
		final int baseFuelUnitPrice = 10;
		final int eqivFactor = 1;
		final int speed = 10;
		final int capacity = 1000000;
		final int consumption = 10;
		final int NBORate = 10;
		final int pilotLight = 0;
		final int minHeelVol = 500;
		final boolean isTimeChartered = false;

		inputVessels.addAll(Arrays.asList(csc.addVessel("classOne", numOfClassOne, numOfSpotCharterClassOne, baseFuelUnitPrice, eqivFactor, speed, speed, capacity, speed, consumption, speed,
				consumption, consumption, NBORate, NBORate, speed, consumption, speed, consumption, consumption, NBORate, NBORate, pilotLight, minHeelVol, isTimeChartered)));
		inputVessels.addAll(Arrays.asList(csc.addVessel("classTwo", numOfClassTwo, numOfSpotCharterClassTwo, baseFuelUnitPrice, eqivFactor, speed, speed, capacity, speed, consumption, speed,
				consumption, consumption, NBORate, NBORate, speed, consumption, speed, consumption, consumption, NBORate, NBORate, pilotLight, minHeelVol, isTimeChartered)));
		inputVessels.addAll(Arrays.asList(csc.addVessel("classThree", numOfClassThree, numOfSpotCharterClassThree, baseFuelUnitPrice, eqivFactor, speed, speed, capacity, speed, consumption, speed,
				consumption, consumption, NBORate, NBORate, speed, consumption, speed, consumption, consumption, NBORate, NBORate, pilotLight, minHeelVol, isTimeChartered)));
		inputVessels.addAll(Arrays.asList(csc.addVessel("classFour", numOfClassFour, numOfSpotCharterClassFour, baseFuelUnitPrice, eqivFactor, speed, speed, capacity, speed, consumption, speed,
				consumption, consumption, NBORate, NBORate, speed, consumption, speed, consumption, consumption, NBORate, NBORate, pilotLight, minHeelVol, isTimeChartered)));

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
		checkVesselExistence(result, inputVessels);
		
		final int maxNumOfSpotVessels = numOfSpotCharterClassOne + numOfSpotCharterClassTwo + numOfSpotCharterClassThree + numOfSpotCharterClassFour;
		
		checkNumOfSpotVesselsNotExceeded(result, maxNumOfSpotVessels);
	}

	/**
	 * Checks the output to make sure that the vessels from the input exist.
	 * 
	 * @param result
	 * @param inputVessels
	 * @param expectedNumOfVessels
	 */
	private void checkVesselExistence(final Schedule result, final ArrayList<Vessel> inputVessels) {

		// the list of input vessels will be changed, and the number of expected vessels it not yet know, so save the number of expected vessels.
		final int expectedNumOfVessels = inputVessels.size();

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
	
	private void checkNumOfSpotVesselsNotExceeded(final Schedule result, final int maxNumOfSpotVessels) {
		
		
		int numOfSpotVessels = 0;
		for (AllocatedVessel av : result.getFleet()) {
			
			if (av instanceof SpotVessel) 
				numOfSpotVessels++;
			
		
		}
		
		Assert.assertTrue("Actual number of spot charter vessels does not exceed allowed number", numOfSpotVessels <= maxNumOfSpotVessels);
		
	}
}
