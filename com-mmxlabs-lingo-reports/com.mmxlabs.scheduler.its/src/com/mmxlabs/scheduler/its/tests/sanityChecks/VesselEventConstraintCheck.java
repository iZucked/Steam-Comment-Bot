/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.its.tests.sanityChecks;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import scenario.Scenario;
import scenario.fleet.CharterOut;
import scenario.fleet.Drydock;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselEvent;
import scenario.port.Port;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;
import scenario.schedule.Sequence;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.VesselEventVisit;
import scenario.schedule.fleetallocation.FleetVessel;

import com.mmxlabs.demo.app.wizards.CustomScenarioCreator;
import com.mmxlabs.scheduler.its.tests.calculation.ScenarioTools;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?256">Case 256: Check charter-in rules respected</a>
 * 
 * On a VesselEvent there are two lists, vessels and vesselClasses. These restrict the vessel event to the given vessels or vessels of the given classes, so if you have vessels = (Vessel A), vessel
 * classes = (Vessel Class X, Vessel Class Y), the event must be on any of vessel A, or any of the vessels with class X or Y. If there's nothing in the lists, there's no constraint.
 * 
 * You may want to set up a scenario with RandomScenarioUtils, and then modify one of the vessel events to have some constraints like this.
 * 
 
 
 * TODO add vessel & it's vessel class to allowed lists, test it still works even though added twice. 
 * @author Adam
 * 
 */
public class VesselEventConstraintCheck {

	private static final int dischargePrice = 1;
	private final static int cvValue = 10;
	
	private CustomScenarioCreator csc = new CustomScenarioCreator(dischargePrice);
	
	private Port[] ports; 
	final int numOfClassOne = 3;
	final int numOfClassTwo = 4;
	final int numOfClassThree = 5;
	final int numOfClassFour = 6;
	private ArrayList<Vessel> vesselsOfClassOne;
	private ArrayList<Vessel> vesselsOfClassTwo ;
	private ArrayList<Vessel> vesselsOfClassThree;
	private ArrayList<Vessel> vesselsOfClassFour; 
	
	/**
	 * This is called before each test is run.
	 */
	@Before
	public void setupTest() {
		// a list of ports to use in the scenario
		ports = new Port[] { ScenarioTools.createPort("portA"), ScenarioTools.createPort("portB"), ScenarioTools.createPort("portC"), ScenarioTools.createPort("portD"),
				ScenarioTools.createPort("portE"), ScenarioTools.createPort("portF") };

		// Add the ports, and set the distances.
		SanityCheckTools.setPortDistances(csc, ports);

		// createVessels creates and adds the vessels to the scenario.
		 vesselsOfClassOne = new ArrayList<Vessel>(Arrays.asList(csc.addVesselSimple("classOne", numOfClassOne, 10, 25, 1000000, 10, 10, 0, 500, false)));
		 vesselsOfClassTwo = new ArrayList<Vessel>(Arrays.asList(csc.addVesselSimple("classTwo", numOfClassTwo, 9, 30, 700000, 11, 9, 7, 0, false)));
		 vesselsOfClassThree = new ArrayList<Vessel>(Arrays.asList(csc.addVesselSimple("classThree", numOfClassThree, 27, 25, 10000, 17, 14, 10, 1000, false)));
		 vesselsOfClassFour = new ArrayList<Vessel>(Arrays.asList(csc.addVesselSimple("classFour", numOfClassFour, 15, 20, 150000, 20, 10, 5, 2000, true)));

	}
	
	/**
	 * Test that if all charter outs and all dry docks have restrictions on vessels and vessel classes then the constraints work.
	 */
	@Test
	public void testAllConstraints() {

		// get some vessels and vessel classes to restrict the dry docks and charter outs
		final Vessel allowedDrydockVessel = vesselsOfClassOne.get(0);
		final VesselClass allowedDrydockVesselClass = vesselsOfClassThree.get(0).getClass_();
		final Vessel allowedCharterOutVessel = vesselsOfClassTwo.get(0);
		final VesselClass allowedCharterOutVesselClass = vesselsOfClassFour.get(0).getClass_();

		// add some VesselEvents, i.e. CharterOuts and DryDocks in a random-ish manner.
		SanityCheckTools.addDrydocks(csc, ports, allowedDrydockVessel, allowedDrydockVesselClass);
		SanityCheckTools.addCharterOuts(csc, ports, allowedCharterOutVessel, allowedCharterOutVesselClass, cvValue, dischargePrice);

		final Scenario scenario = csc.buildScenario();
		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);

		// print the legs to console
		for (CargoAllocation ca : result.getCargoAllocations())
			ScenarioTools.printCargoAllocation(ca.getName(), ca);

		// print each vessel's sequence
		ScenarioTools.printSequences(result);

		// check the output
		checkOutput(result, allowedDrydockVessel, allowedDrydockVesselClass, allowedCharterOutVessel, allowedCharterOutVesselClass);
	}


	/**
	 * Check that dry docks and charter outs only use the vessels and vessel classes specified in the input.
	 * 
	 * @param result The evaluated scenario
	 * @param allowedDryDockVessel The vessel allowed to take dry docks
	 * @param allowedDryDockVesselClass The vessel classes allowed to take dry docks
	 * @param allowedCharterOutVessel The charter out vessel allowed to take dry docks
	 * @param allowedCharterOutVesselClass The vessel class allowed to take charter outs.
	 */
	private void checkOutput(final Schedule result, final Vessel allowedDryDockVessel, final VesselClass allowedDryDockVesselClass, final Vessel allowedCharterOutVessel,
			final VesselClass allowedCharterOutVesselClass) {

		for (final Sequence seq : result.getSequences()) {
			for (final ScheduledEvent e : seq.getEvents()) {
				if (e instanceof VesselEventVisit) {

					VesselEventVisit vev = (VesselEventVisit) e;
					VesselEvent ve = vev.getVesselEvent();

					Vessel usedVessel = ((FleetVessel) seq.getVessel()).getVessel();

					if (ve instanceof CharterOut) {

						final boolean isAllowed = usedVessel.equals(allowedCharterOutVessel) || allowedCharterOutVesselClass.equals(usedVessel.getClass_());
						assertTrue(isAllowed);

					} else if (ve instanceof Drydock) {

						final boolean isAllowed = usedVessel.equals(allowedDryDockVessel) || allowedDryDockVesselClass.equals(usedVessel.getClass_());
						assertTrue(isAllowed);

					} else
						fail("Test should cover all VesselEvents.");
				}
			}
		}
	}
}