/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?261">Check that all vesselevents in input exist in output.</a>
 * 
 * @author Adam
 * 
 */
@ExtendWith(ShiroRunner.class)
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
		List<VesselAvailability> charters = new LinkedList<>();

		charters.addAll(Lists.newArrayList(csc.addVesselSimple("classOne", numOfClassOne, 10, 10, 1000000, 10, 10, 0, 500, false)));
		charters.addAll(Lists.newArrayList(csc.addVesselSimple("classTwo", numOfClassTwo, 9, 15, 700000, 11, 9, 7, 0, false)));
		// Time charter
		charters.addAll(Lists.newArrayList(csc.addVesselSimple("classThree", numOfClassThree, 11, 12, 500000, 13, 15, 15, 0, true)));

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

		int charterIndex = 0;
		for (VesselEvent ve : inputVesselEvents) {
			ve.setVesselAssignmentType(charters.get(charterIndex++ % charters.size()));
		}

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

					Assertions.assertTrue(inputVesselEvents.contains(ve), "Input VesselEvent is in output");

					inputVesselEvents.remove(ve);

					outputNumOfVesselEvents++;

				}
			}
		}
		Assertions.assertEquals(inputNumOfVesselEvents, outputNumOfVesselEvents, "Same number of VesselEvents in the output as in the input");

		Assertions.assertEquals(0, inputVesselEvents.size(), "All VesselEvents in output");
	}
}