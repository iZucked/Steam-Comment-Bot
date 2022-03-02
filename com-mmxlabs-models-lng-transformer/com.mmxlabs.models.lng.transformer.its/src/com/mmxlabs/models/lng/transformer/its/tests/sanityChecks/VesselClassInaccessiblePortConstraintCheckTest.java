/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

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
@ExtendWith(ShiroRunner.class)
public class VesselClassInaccessiblePortConstraintCheckTest {

	private static final int dischargePrice = 1;
	private final CustomScenarioCreator csc = new CustomScenarioCreator(dischargePrice);

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

		// createVessels creates and adds the vessels to the scenario.
		final List<VesselAvailability> vesselsOfClassOne = new ArrayList<>(Arrays.asList(csc.addVesselSimple("classOne", numOfClassOne, 10, 25, 1000000, 10, 10, 0, 500, false)));
		final List<VesselAvailability> vesselsOfClassTwo = new ArrayList<>(Arrays.asList(csc.addVesselSimple("classTwo", numOfClassTwo, 9, 30, 700000, 11, 9, 7, 0, false)));
		final List<VesselAvailability> vesselsOfClassThree = new ArrayList<>(Arrays.asList(csc.addVesselSimple("classThree", numOfClassThree, 27, 25, 10000, 17, 14, 10, 1000, false)));
		final List<VesselAvailability> vesselsOfClassFour = new ArrayList<>(Arrays.asList(csc.addVesselSimple("classFour", numOfClassFour, 15, 20, 150000, 20, 10, 5, 2000, true)));

		// get the vessel classes
		final Vessel vesselClassOne = vesselsOfClassOne.get(0).getVessel();
		final Vessel vesselClassTwo = vesselsOfClassTwo.get(0).getVessel();
		final Vessel vesselClassThree = vesselsOfClassThree.get(0).getVessel();
		final Vessel vesselClassFour = vesselsOfClassFour.get(0).getVessel();

		// create some cargoes.
		SanityCheckTools.addCargoes(csc, ports, loadPrice, dischargePrice, cvValue);

		// add some constraints.
		// vessel class One can't go to port A.
		vesselClassOne.getVesselOrDelegateInaccessiblePorts().add(portA);
		// vessel class Two can't go to port B.
		vesselClassTwo.getVesselOrDelegateInaccessiblePorts().add(portB);
		// vessel class Three can't go to port C.
		vesselClassThree.getVesselOrDelegateInaccessiblePorts().add(portC);
		// vessel class Four can't go to port D.
		vesselClassFour.getVesselOrDelegateInaccessiblePorts().add(portD);

		// build and run the scenario.
		final IScenarioDataProvider scenarioDataProvider = csc.getScenarioDataProvider();
		final Schedule result = ScenarioTools.evaluate(scenarioDataProvider);

		// check constraints.

		for (final Sequence sequence : result.getSequences()) {

			// Vessel vessel = sequence.getVessel();
			Vessel vesselClass = sequence.isSetVesselAvailability() ? sequence.getVesselAvailability().getVessel() : sequence.getCharterInMarket().getVessel();

			Port bannedPort = null;
			if (vesselClass.equals(vesselClassOne)) {
				bannedPort = portA;
			} else if (vesselClass.equals(vesselClassTwo)) {
				bannedPort = portB;
			} else if (vesselClass.equals(vesselClassThree)) {
				bannedPort = portC;
			} else if (vesselClass.equals(vesselClassFour)) {
				bannedPort = portD;
			} else {
				Assertions.fail("Expected an inaccessible port for all vessel classes, but vessel class " + vesselClass.getName() + " didn't have a banned port.");
			}
			assert bannedPort != null;

			Assertions.assertTrue(vesselClass.getVesselOrDelegateInaccessiblePorts().contains(bannedPort), "inaccessible port still exists");
			Assertions.assertEquals(1, vesselClass.getVesselOrDelegateInaccessiblePorts().size(), "Only one inaccessible port expected");
			System.out.println("Vessel (" + vesselClass.getName() + ") is banned from " + vesselClass.getVesselOrDelegateInaccessiblePorts().get(0).getName());

			final List<Port> visitedPorts = SanityCheckTools.getVesselsVisitedPorts(sequence);

			for (final Port p : visitedPorts) {
				System.out.println("Vessel (" + vesselClass.getName() + ") visited " + p.getName() + " (banned from " + bannedPort.getName() + ")");

				Assertions.assertFalse(bannedPort.equals(p), vesselClass.getName() + " vessel did not visit inaccessible port (" + bannedPort.getName() + ")");
			}
		}
	}
}
