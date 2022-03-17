/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

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
@ExtendWith(ShiroRunner.class)
public class VesselEventConstraintCheckTest {

	private static final int dischargePrice = 1;
	private final static int cvValue = 10;

	private CustomScenarioCreator csc;

	private Port[] ports;
	private final int numOfClassOne = 3;
	private final int numOfClassTwo = 4;
	private final int numOfClassThree = 5;
	private final int numOfClassFour = 6;
	private List<VesselAvailability> vesselsOfClassOne;
	private List<VesselAvailability> vesselsOfClassTwo;
	private List<VesselAvailability> vesselsOfClassThree;
	private List<VesselAvailability> vesselsOfClassFour;

	/**
	 * This is called before each test is run.
	 */
	@BeforeEach
	public void setupTest() {
		csc = new CustomScenarioCreator(dischargePrice);

		// a list of ports to use in the scenario
		ports = new Port[] { ScenarioTools.createPort("portA"), ScenarioTools.createPort("portB"), ScenarioTools.createPort("portC"), ScenarioTools.createPort("portD"),
				ScenarioTools.createPort("portE"), ScenarioTools.createPort("portF") };

		// Add the ports, and set the distances.
		SanityCheckTools.setPortDistances(csc, ports);

		// createVessels creates and adds the vessels to the scenario.
		vesselsOfClassOne = new ArrayList<>(Arrays.asList(csc.addVesselSimple("classOne", numOfClassOne, 10, 25, 1000000, 10, 10, 0, 500, false)));
		vesselsOfClassTwo = new ArrayList<>(Arrays.asList(csc.addVesselSimple("classTwo", numOfClassTwo, 9, 30, 700000, 11, 9, 7, 0, false)));
		vesselsOfClassThree = new ArrayList<>(Arrays.asList(csc.addVesselSimple("classThree", numOfClassThree, 27, 25, 10000, 17, 14, 10, 1000, false)));
		vesselsOfClassFour = new ArrayList<>(Arrays.asList(csc.addVesselSimple("classFour", numOfClassFour, 15, 20, 150000, 20, 10, 5, 2000, true)));

	}

	/**
	 * Reset variables for the next test.
	 */
	@AfterEach
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
		final Vessel allowedDrydockVessel = vesselsOfClassThree.get(0).getVessel();
		final Vessel allowedCharterOutVessel = vesselsOfClassFour.get(0).getVessel();

		addVesselEventsAndRunTest(Collections.singleton(allowedDrydockVessel), Collections.singleton(allowedCharterOutVessel));
	}

	/**
	 * Only one vessel is allowed to take the dry dock. Test that the constraint works.
	 */
	@Test
	public void testDrydockVessel() {

		final VesselAvailability allowedDrydockVessel = vesselsOfClassOne.get(0);

		addVesselEventsAndRunTest(Collections.singleton(allowedDrydockVessel.getVessel()), Collections.emptySet());
	}

	/**
	 * Only one vessel is allowed to take charter outs. Test that the constraint works.
	 */
	@Test
	public void testCharterOutVessel() {

		final VesselAvailability allowedCharterOutVessel = vesselsOfClassOne.get(0);

		addVesselEventsAndRunTest(Collections.emptySet(), Collections.singleton(allowedCharterOutVessel.getVessel()));
	}

	/**
	 * Only one vessel class is allowed to take dry docks. Test that the constraint works.
	 */
	@Test
	public void testDryDockVesselClass() {

		final Vessel allowedDrydockVessel = vesselsOfClassOne.get(0).getVessel();
		VesselGroup group = FleetFactory.eINSTANCE.createVesselGroup();
		group.getVessels().add(allowedDrydockVessel);
		addVesselEventsAndRunTest(Collections.singleton(group), Collections.emptySet());
	}

	/**
	 * Only one vessel class is allowed to take charter outs. Test that the constraint works.
	 */
	@Test
	public void testCharterOutVesselClass() {

		final Vessel allowedCharterOutVesselClass = vesselsOfClassOne.get(0).getVessel();

		VesselGroup group = FleetFactory.eINSTANCE.createVesselGroup();
		group.getVessels().add(allowedCharterOutVesselClass);
		addVesselEventsAndRunTest(Collections.emptySet(), Collections.singleton(group));

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
	private void addVesselEventsAndRunTest(final Collection<AVesselSet<Vessel>> allowedDrydockVessel, final Collection<AVesselSet<Vessel>> allowedCharterOutVessel) {

		// add some VesselEvents, i.e. CharterOuts and DryDocks in a random-ish manner.
		SanityCheckTools.addDrydocks(csc, ports, allowedDrydockVessel);
		SanityCheckTools.addCharterOuts(csc, ports, allowedCharterOutVessel, cvValue, dischargePrice);

		final IScenarioDataProvider scenarioDataProvider = csc.getScenarioDataProvider();
		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenarioDataProvider);

		// print the legs to console
		for (final CargoAllocation ca : result.getCargoAllocations()) {
			ScenarioTools.printCargoAllocation(ca.getName(), ca);
		}

		// print each vessel's sequence
		ScenarioTools.printSequences(result);

		// check the output
		checkOutput(result, allowedDrydockVessel, allowedCharterOutVessel);
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
	private void checkOutput(final Schedule result, final Collection<AVesselSet<Vessel>> allowedDryDockVessel, final Collection<AVesselSet<Vessel>> allowedCharterOutVessel) {

		for (final Sequence seq : result.getSequences()) {
			for (final Event e : seq.getEvents()) {
				if (e instanceof VesselEventVisit) {

					final VesselEventVisit vev = (VesselEventVisit) e;
					final VesselEvent ve = vev.getVesselEvent();

					final Vessel usedVessel = seq.getVesselAvailability().getVessel();

					if (ve instanceof CharterOutEvent) {
						Assertions.assertTrue(isUsedVesselValid(usedVessel, allowedCharterOutVessel), "Charter out uses allowed vessel or vessel of allowed VesselClass");
					} else if (ve instanceof DryDockEvent) {
						Assertions.assertTrue(isUsedVesselValid(usedVessel, allowedDryDockVessel), "Drydock uses allowed vessel or vessel of allowed VesselClass");
					} else {
						Assertions.fail("Test should cover all VesselEvents.");
					}
				}
			}
		}
	}

	private boolean isUsedVesselValid(final Vessel usedVessel, final Collection<AVesselSet<Vessel>> allowedVessels) {
		// in the case that there are no restrictions on a vessel event all vessels are valid, so return true
		if ((allowedVessels == null || allowedVessels.isEmpty())) {
			return true;
		} else {
			return SetUtils.getObjects(allowedVessels).contains(usedVessel);
		}
	}
}