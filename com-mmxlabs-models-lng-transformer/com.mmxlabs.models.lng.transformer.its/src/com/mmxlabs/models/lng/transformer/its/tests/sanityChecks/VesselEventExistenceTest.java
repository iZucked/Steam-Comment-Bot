/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.models.lng.cargo.VesselEvent;
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
 * <a href="https://mmxlabs.fogbugz.com/default.asp?261">Check that all vesselevents in input exist in output.</a>
 * 
 * @author Adam
 * 
 */
@RunWith(value = ShiroRunner.class)
public class VesselEventExistenceTest {

	private static final int dischargePrice = 1;
	private final CustomScenarioCreator csc = new CustomScenarioCreator(dischargePrice);

	@Test
	public void test() {

		// A list for all VesselEvents from the input.
		final List<VesselEvent> inputVesselEvents = new ArrayList<>();

		@SuppressWarnings("unused")
		final int loadPrice = 1;
		final int cvValue = 10;

		// a list of ports to use in the scenario
		final Port[] ports = new Port[] { //
				ScenarioTools.createPort("portA"), //
				ScenarioTools.createPort("portB"), //
				ScenarioTools.createPort("portC"), //
				ScenarioTools.createPort("portD"), //
				ScenarioTools.createPort("portE"), //
				ScenarioTools.createPort("portF") };

		// Add the ports, and set the distances.
		SanityCheckTools.setPortDistances(csc, ports);

		// create a few vessels and add them to the scenario
		final int numOfClassOne = 2;
		final int numOfClassTwo = 2;
		final int numOfClassThree = 1;

		// Fleet
		csc.addVesselSimple("classOne", numOfClassOne, 10, 10, 1000000, 10, 10, 0, 500, false);
		csc.addVesselSimple("classTwo", numOfClassTwo, 9, 15, 700000, 11, 9, 7, 0, false);
		// Time charter
		csc.addVesselSimple("classThree", numOfClassThree, 11, 12, 500000, 13, 15, 15, 0, true);

		// add some VesselEvents, i.e. CharterOuts and DryDocks in a random-ish manner.
		LocalDateTime start = LocalDateTime.now();
		for (final Port portA : ports) {
			for (final Port portB : ports) {
				if (!portA.equals(portB)) {
					inputVesselEvents.add(csc.addDryDock(portB, start, 1));
					start = start.plusHours(2);
				}
			}
		}

		start = LocalDateTime.now();
		int charterOutDurationDays = 1;
		for (final Port portA : ports) {
			for (final Port portB : ports) {

				inputVesselEvents.add(csc.addCharterOut("CharterOut " + portA.getName() + " to " + portB.getName(), portA, portB, start, 1000, charterOutDurationDays, cvValue, numOfClassOne, 100, 0));

				// FIXME: What is the point of this? This will be set to zero on first invocation?
				if (portA.equals(portB)) {
					charterOutDurationDays /= 2;
				}
			}
		}

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
		checkVesselEvents(result, inputVesselEvents);
	}

	/**
	 * Check all the VesselEvents in output are in the input VesselEvents once.
	 * 
	 * @param result
	 *            The evaluated scenario (containing the VesselEvents in the output)
	 * @param inputVesselEvents
	 *            The VesselEvents that were input into the unevaluated scenario.
	 */
	private void checkVesselEvents(final Schedule result, final List<VesselEvent> inputVesselEvents) {

		final int inputNumOfVesselEvents = inputVesselEvents.size();
		int outputNumOfVesselEvents = 0;

		for (final Sequence seq : result.getSequences()) {
			for (final Event e : seq.getEvents()) {
				if (e instanceof VesselEventVisit) {

					final VesselEventVisit vev = (VesselEventVisit) e;
					final VesselEvent ve = vev.getVesselEvent();

					Assert.assertTrue("Input VesselEvent is in output", inputVesselEvents.contains(ve));

					inputVesselEvents.remove(ve);

					outputNumOfVesselEvents++;

				}
			}
		}
		Assert.assertEquals("Same number of VesselEvents in the output as in the input", inputNumOfVesselEvents, outputNumOfVesselEvents);

		Assert.assertEquals("All VesselEvents in output", 0, inputVesselEvents.size());
	}
}