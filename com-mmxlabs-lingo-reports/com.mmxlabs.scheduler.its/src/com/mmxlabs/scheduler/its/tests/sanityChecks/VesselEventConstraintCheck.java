/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.its.tests.sanityChecks;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

	@Test
	public void test() {

		@SuppressWarnings("unused")
		final int loadPrice = 1;

		// a list of ports to use in the scenario
		final Port[] ports = new Port[] { ScenarioTools.createPort("portA"), ScenarioTools.createPort("portB"), ScenarioTools.createPort("portC"), ScenarioTools.createPort("portD"),
				ScenarioTools.createPort("portE"), ScenarioTools.createPort("portF") };

		// Add the ports, and set the distances.
		SanityCheckTools.setPortDistances(csc, ports);

		// create a few vessels and add them to the list of vessels created.
		final int numOfClassOne = 3;
		final int numOfClassTwo = 4;
		final int numOfClassThree = 5;
		final int numOfClassFour = 6;

		// createVessels creates and adds the vessels to the scenario.
		ArrayList<Vessel> vesselsOfClassOne = new ArrayList<Vessel>(Arrays.asList(csc.addVesselSimple("classOne", numOfClassOne, 10, 25, 1000000, 10, 10, 0, 500, false)));
		ArrayList<Vessel> vesselsOfClassTwo = new ArrayList<Vessel>(Arrays.asList(csc.addVesselSimple("classTwo", numOfClassTwo, 9, 30, 700000, 11, 9, 7, 0, false)));
		ArrayList<Vessel> vesselsOfClassThree = new ArrayList<Vessel>(Arrays.asList(csc.addVesselSimple("classThree", numOfClassThree, 27, 25, 10000, 17, 14, 10, 1000, false)));
		ArrayList<Vessel> vesselsOfClassFour = new ArrayList<Vessel>(Arrays.asList(csc.addVesselSimple("classFour", numOfClassFour, 15, 20, 150000, 20, 10, 5, 2000, true)));

		// get some vessels and vessel classes to restrict the dry docks and charter outs
		final Vessel allowedDrydockVessel = vesselsOfClassOne.get(0);
		final VesselClass allowedDrydockVesselClass = vesselsOfClassThree.get(0).getClass_();
		final Vessel allowedCharterOutVessel = vesselsOfClassTwo.get(0);
		final VesselClass allowedCharterOutVesselClass = vesselsOfClassFour.get(0).getClass_();

		// add some VesselEvents, i.e. CharterOuts and DryDocks in a random-ish manner.
		addDrydocks(ports, allowedDrydockVessel, allowedDrydockVesselClass);
		addCharterOuts(ports, allowedCharterOutVessel, allowedCharterOutVesselClass);

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
	 * Add a load of drydocks from every port to every port. Also add constraints to each dry dock.
	 * 
	 * @param ports
	 *            The ports to add dry docks to/from
	 * @param allowedDrydockVessel
	 *            A vessel to add to all dry dock's allowed-vessel list
	 * @param allowedDrydockVesselClass
	 *            A vessel class to add to all dry dock's allowed-vesselclass list.
	 */
	private void addDrydocks(final Port[] ports, final Vessel allowedDrydockVessel, final VesselClass allowedDrydockVesselClass) {

		Date start = new Date(System.currentTimeMillis());
		for (Port portA : ports) {
			for (Port portB : ports) {
				if (!portA.equals(portB)) {

					final Drydock dry = csc.addDryDock(portB, start, 1);

					dry.getVessels().add(allowedDrydockVessel);
					dry.getVesselClasses().add(allowedDrydockVesselClass);

					start.setTime(start.getTime() + TimeUnit.HOURS.toMillis(2));
				}
			}
		}
	}

	/**
	 * Adds a charter out from every port to every port. Also adds constraints to each charter out.
	 * 
	 * @param ports
	 *            The ports to add charter outs to/from
	 * @param allowedCharterOutVessel
	 *            A vessel to add to all charter outs allowed-vessel list.
	 * @param allowedCharterOutVesselClass
	 *            A class of vessel to add to every charter out's allowed vessel class list.
	 */
	private void addCharterOuts(final Port[] ports, final Vessel allowedCharterOutVessel, final VesselClass allowedCharterOutVesselClass) {

		Date start = new Date(System.currentTimeMillis());
		int charterOutDurationDays = 1;
		for (Port portA : ports) {
			for (Port portB : ports) {

				final String id = "CharterOut " + portA.getName() + " to " + portB.getName();

				CharterOut charterOut = csc.addCharterOut(id, portA, portB, start, 1000, charterOutDurationDays, cvValue, dischargePrice, 100, 0);

				charterOut.getVessels().add(allowedCharterOutVessel);
				charterOut.getVesselClasses().add(allowedCharterOutVesselClass);

				if (portA.equals(portB))
					charterOutDurationDays /= 2;

			}
		}
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