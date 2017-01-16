/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?256">Case 256: Check charter-in rules respected</a>
 * 
 * On a VesselEvent there are two lists, vessels and vesselClasses. These restrict the vessel event to the given vessels or vessels of the given classes, so if you have vessels = (Vessel A), vessel
 * classes = (Vessel Class X, Vessel Class Y), the event must be on any of vessel A, or any of the vessels with class X or Y. If there's nothing in the lists, there's no constraint.
 * 
 * You may want to set up a scenario with RandomScenarioUtils, and then modify one of the vessel events to have some constraints like this.
 * 
 * 
 * 
 * TODO add vessel & it's vessel class to allowed lists, test it still works even though added twice.
 * 
 * @author Adam
 * 
 */
@RunWith(value = ShiroRunner.class)
public class VesselEventConstraintCheckTest {

	private static final int dischargePrice = 1;
	private final static int cvValue = 10;

	private CustomScenarioCreator csc;

	private Port[] ports;
	private final int numOfClassOne = 3;
	private final int numOfClassTwo = 4;
	private final int numOfClassThree = 5;
	private final int numOfClassFour = 6;
	private ArrayList<Vessel> vesselsOfClassOne;
	private ArrayList<Vessel> vesselsOfClassTwo;
	private ArrayList<Vessel> vesselsOfClassThree;
	private ArrayList<Vessel> vesselsOfClassFour;

	/**
	 * This is called before each test is run.
	 */
	@Before
	public void setupTest() {
		csc = new CustomScenarioCreator(dischargePrice);

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
	 * Reset variables for the next test.
	 */
	@After
	public void afterTests() {
		csc = null;
		ports = null;
		vesselsOfClassOne = null;
		vesselsOfClassTwo = null;
		vesselsOfClassThree = null;
		vesselsOfClassFour = null;
	}

	/**
	 * Test that if all charter outs and all dry docks have restrictions on vessels and vessel classes then the constraints work.
	 */
	@Test
	public void testAllConstraints() {

		// get some vessels and vessel classes to restrict the dry docks and charter outs
		final Vessel allowedDrydockVessel = vesselsOfClassOne.get(0);
		final VesselClass allowedDrydockVesselClass = vesselsOfClassThree.get(0).getVesselClass();
		final Vessel allowedCharterOutVessel = vesselsOfClassTwo.get(0);
		final VesselClass allowedCharterOutVesselClass = vesselsOfClassFour.get(0).getVesselClass();

		addVesselEventsAndRunTest(allowedDrydockVessel, allowedDrydockVesselClass, allowedCharterOutVessel, allowedCharterOutVesselClass);
	}

	/**
	 * Only one vessel is allowed to take the dry dock. Test that the constraint works.
	 */
	@Test
	public void testDrydockVessel() {

		final Vessel allowedDrydockVessel = vesselsOfClassOne.get(0);
		final VesselClass allowedDrydockVesselClass = null;
		final Vessel allowedCharterOutVessel = null;
		final VesselClass allowedCharterOutVesselClass = null;

		addVesselEventsAndRunTest(allowedDrydockVessel, allowedDrydockVesselClass, allowedCharterOutVessel, allowedCharterOutVesselClass);
	}

	/**
	 * Only one vessel is allowed to take charter outs. Test that the constraint works.
	 */
	@Test
	public void testCharterOutVessel() {

		final Vessel allowedDrydockVessel = null;
		final VesselClass allowedDrydockVesselClass = null;
		final Vessel allowedCharterOutVessel = vesselsOfClassOne.get(0);
		final VesselClass allowedCharterOutVesselClass = null;

		addVesselEventsAndRunTest(allowedDrydockVessel, allowedDrydockVesselClass, allowedCharterOutVessel, allowedCharterOutVesselClass);
	}

	/**
	 * Only one vessel class is allowed to take dry docks. Test that the constraint works.
	 */
	@Test
	public void testDryDockVesselClass() {

		final Vessel allowedDrydockVessel = null;
		final VesselClass allowedDrydockVesselClass = vesselsOfClassOne.get(0).getVesselClass();
		final Vessel allowedCharterOutVessel = null;
		final VesselClass allowedCharterOutVesselClass = null;

		addVesselEventsAndRunTest(allowedDrydockVessel, allowedDrydockVesselClass, allowedCharterOutVessel, allowedCharterOutVesselClass);
	}

	/**
	 * Only one vessel class is allowed to take charter outs. Test that the constraint works.
	 */
	@Test
	public void testCharterOutVesselClass() {

		final Vessel allowedDrydockVessel = null;
		final VesselClass allowedDrydockVesselClass = null;
		final Vessel allowedCharterOutVessel = null;
		final VesselClass allowedCharterOutVesselClass = vesselsOfClassOne.get(0).getVesselClass();

		addVesselEventsAndRunTest(allowedDrydockVessel, allowedDrydockVesselClass, allowedCharterOutVessel, allowedCharterOutVesselClass);
	}

	/**
	 * Add some vessel events to the CSC and run the event. The given Vessels and VesselClasses are added to the allowed lists on every VesselEvent.
	 * 
	 * @param allowedDrydockVessel
	 *            A vessel that is allowed to dry dock
	 * @param allowedDrydockVesselClass
	 *            A class of vessel that is allowed to dry dock
	 * @param allowedCharterOutVessel
	 *            A vessel that is allowed to charter out
	 * @param allowedCharterOutVesselClass
	 *            A class of vessel that is allowed charter out
	 */
	private void addVesselEventsAndRunTest(final Vessel allowedDrydockVessel, final VesselClass allowedDrydockVesselClass, final Vessel allowedCharterOutVessel,
			final VesselClass allowedCharterOutVesselClass) {

		// add some VesselEvents, i.e. CharterOuts and DryDocks in a random-ish manner.
		SanityCheckTools.addDrydocks(csc, ports, allowedDrydockVessel, allowedDrydockVesselClass);
		SanityCheckTools.addCharterOuts(csc, ports, allowedCharterOutVessel, allowedCharterOutVesselClass, cvValue, dischargePrice);

		final LNGScenarioModel scenario = csc.buildScenario();
		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);

		// print the legs to console
		for (final CargoAllocation ca : result.getCargoAllocations()) {
			ScenarioTools.printCargoAllocation(ca.getName(), ca);
		}

		// print each vessel's sequence
		ScenarioTools.printSequences(result);

		// check the output
		checkOutput(result, allowedDrydockVessel, allowedDrydockVesselClass, allowedCharterOutVessel, allowedCharterOutVesselClass);
	}

	/**
	 * Check that dry docks and charter outs only use the vessels and vessel classes specified in the input.
	 * 
	 * @param result
	 *            The evaluated scenario
	 * @param allowedDryDockVessel
	 *            The vessel allowed to take dry docks
	 * @param allowedDryDockVesselClass
	 *            The vessel classes allowed to take dry docks
	 * @param allowedCharterOutVessel
	 *            The charter out vessel allowed to take dry docks
	 * @param allowedCharterOutVesselClass
	 *            The vessel class allowed to take charter outs.
	 */
	private void checkOutput(final Schedule result, final Vessel allowedDryDockVessel, final VesselClass allowedDryDockVesselClass, final Vessel allowedCharterOutVessel,
			final VesselClass allowedCharterOutVesselClass) {

		for (final Sequence seq : result.getSequences()) {
			for (final Event e : seq.getEvents()) {
				if (e instanceof VesselEventVisit) {

					final VesselEventVisit vev = (VesselEventVisit) e;
					final VesselEvent ve = vev.getVesselEvent();

					final Vessel usedVessel = seq.getVesselAvailability().getVessel();

					if (ve instanceof CharterOutEvent) {
						assertTrue("Charter out uses allowed vessel or vessel of allowed VesselClass", isUsedVesselValid(usedVessel, allowedCharterOutVessel, allowedCharterOutVesselClass));
					} else if (ve instanceof DryDockEvent) {
						assertTrue("Drydock uses allowed vessel or vessel of allowed VesselClass", isUsedVesselValid(usedVessel, allowedDryDockVessel, allowedDryDockVesselClass));
					} else {
						fail("Test should cover all VesselEvents.");
					}
				}
			}
		}
	}

	private boolean isUsedVesselValid(final Vessel usedVessel, final Vessel allowedVessel, final VesselClass allowedVesselClass) {

		// in the case that there are no restrictions on a vessel event all vessels are valid, so return true
		if ((allowedVessel == null) && (allowedVesselClass == null)) {
			return true;
		} else {
			boolean isValid = false;
			if (allowedVessel != null) {
				isValid = isValid || usedVessel.equals(allowedVessel);
			}
			if (allowedVesselClass != null) {
				isValid = isValid || allowedVesselClass.equals(usedVessel.getVesselClass());
			}

			return isValid;
		}
	}
}