/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.its.tests.sanityChecks;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import scenario.Scenario;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.port.Port;
import scenario.schedule.Schedule;
import scenario.schedule.fleetallocation.AllocatedVessel;
import scenario.schedule.fleetallocation.FleetVessel;

import com.mmxlabs.demo.app.wizards.CustomScenarioCreator;
import com.mmxlabs.scheduler.its.tests.calculation.ScenarioTools;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?255">Case 255: Check constraints have not be violated</a><br>
 * 
 * Every VesselClass has a list of Ports called 'inaccessiblePorts'; check that no Vessel in the output Schedule goes to a port which is in its VesselClass' inaccessiblePorts list.
 * 
 * TODO Remove print statements once working.
 * 
 * @author Adam
 * 
 */
public class VesselClassPortConstraintCheck {

	private static final int dischargePrice = 1;
	private CustomScenarioCreator csc = new CustomScenarioCreator(dischargePrice);

	@Test
	public void test() {

		final int loadPrice = 1;
		final int cvValue = 10;

		final Port portA = ScenarioTools.createPort("portA");
		final Port portB = ScenarioTools.createPort("portB");
		final Port portC = ScenarioTools.createPort("portC");
		final Port portD = ScenarioTools.createPort("portD");
		// a list of ports to use in the scenario
		final Port[] ports = new Port[] { portA, portB, portC, portD, ScenarioTools.createPort("portE"), ScenarioTools.createPort("portF") };

		// Add the ports, and set the distances.
		SanityCheckTools.setPortDistances(csc, ports);

		// create a few vessels and add them to the list of vessels created.
		final int numOfClassOne = 10;
		final int numOfClassTwo = 11;
		final int numOfClassThree = 12;
		final int numOfClassFour = 1;

		// createVessels creates and adds the vessesl to the scenario.
		ArrayList<Vessel> vesselsOfClassOne = new ArrayList<Vessel>(Arrays.asList(csc.addVesselSimple("classOne", numOfClassOne, 10, 25, 1000000, 10, 10, 0, 500, false)));
		ArrayList<Vessel> vesselsOfClassTwo = new ArrayList<Vessel>(Arrays.asList(csc.addVesselSimple("classTwo", numOfClassTwo, 9, 30, 700000, 11, 9, 7, 0, false)));
		ArrayList<Vessel> vesselsOfClassThree = new ArrayList<Vessel>(Arrays.asList(csc.addVesselSimple("classThree", numOfClassThree, 27, 25, 10000, 17, 14, 10, 1000, false)));
		ArrayList<Vessel> vesselsOfClassFour = new ArrayList<Vessel>(Arrays.asList(csc.addVesselSimple("classFour", numOfClassFour, 15, 20, 150000, 20, 10, 5, 2000, true)));

		// get the vessel classes
		VesselClass vesselClassOne = vesselsOfClassOne.get(0).getClass_();
		VesselClass vesselClassTwo = vesselsOfClassTwo.get(0).getClass_();
		VesselClass vesselClassThree = vesselsOfClassThree.get(0).getClass_();
		VesselClass vesselClassFour = vesselsOfClassFour.get(0).getClass_();

		// create some cargos.
		SanityCheckTools.addCargos(csc, ports, loadPrice, dischargePrice, cvValue);

		// add some constraints.
		// vessel class One can't go to port A.
		vesselClassOne.getInaccessiblePorts().add(portA);
		// vessel class Two can't go to port B.
		vesselClassTwo.getInaccessiblePorts().add(portB);
		// vessel class Three can't go to port C.
		vesselClassThree.getInaccessiblePorts().add(portC);
		// vessel class Four can't go to port D.
		vesselClassFour.getInaccessiblePorts().add(portD);

		// build and run the scenario.
		final Scenario scenario = csc.buildScenario();
		final Schedule result = ScenarioTools.evaluate(scenario);

		// check contraints.

		for (AllocatedVessel av : result.getFleet()) {
			if (av instanceof FleetVessel) {
				FleetVessel fv = (FleetVessel) av;

				VesselClass vesselClass = fv.getVessel().getClass_();

				Port bannedPort = null;
				if (vesselClass.equals(vesselClassOne))
					bannedPort = portA;
				else if (vesselClass.equals(vesselClassTwo))
					bannedPort = portB;
				else if (vesselClass.equals(vesselClassThree))
					bannedPort = portC;
				else if (vesselClass.equals(vesselClassFour))
					bannedPort = portD;
				else
					Assert.fail("Expected an inaccessible port for all vessel classes, but vessel class " + vesselClass.getName() + " didn't have a banned port.");

				Assert.assertTrue("inaccessible port still exists", vesselClass.getInaccessiblePorts().contains(bannedPort));
				Assert.assertEquals("Only one inaccessible port expected", 1, vesselClass.getInaccessiblePorts().size());
				System.out.println("Vessel (" + vesselClass.getName() + ") is banned from " + vesselClass.getInaccessiblePorts().get(0).getName());

				ArrayList<Port> visitedPorts = SanityCheckTools.getVesselsVisitedPorts(result, fv);

				for (Port p : visitedPorts) {
					System.out.println("Vessel (" + vesselClass.getName() + ") visited " + p.getName() + " (banned from " + bannedPort.getName() + ")");

					Assert.assertFalse(vesselClass.getName() + " vessel did not visit inaccessible port (" + bannedPort.getName() + ")", bannedPort.equals(p));
				}
			}
		}
	}
}
