/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.its.tests.sanityChecks;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import scenario.Scenario;
import scenario.cargo.Cargo;
import scenario.port.Port;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;

import com.mmxlabs.shiplingo.platform.its.tests.calculation.ScenarioTools;
import com.mmxlabs.shiplingo.platform.models.manifest.wizards.CustomScenarioCreator;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?253">BugzID: 253 Check all cargoes in input exist in output (unless spot/optional)</a><br>
 * 
 * Check that, when a scenario is evaluated:<br>
 * <br>
 * 1. The resulting schedule has a cargo allocation for every cargo in the input.<br>
 * 2. The schedule's sequences contain the load and discharge slots for each cargo somewhere.<br>
 * 3. Make sure that the SlotVisit events for each cargo happen in the right order.<br>
 * 
 * @author Adam
 * 
 */
public class CargoExistenceCheckTest {

	private static final int dischargePrice = 1;
	private final CustomScenarioCreator csc = new CustomScenarioCreator(dischargePrice);

	/**
	 * Create a scenario with some cargos and vessels. Check that all cargos added to the scenario are in the output.
	 */
	@Test
	public void test() {

		// A list to hold all cargos that are input.
		final ArrayList<Cargo> inputCargos = new ArrayList<Cargo>();

		final int loadPrice = 1;
		final int cvValue = 10;

		// a list of ports to use in the scenario
		final Port[] ports = new Port[] { ScenarioTools.createPort("portA"), ScenarioTools.createPort("portB"), ScenarioTools.createPort("portC"), ScenarioTools.createPort("portD"),
				ScenarioTools.createPort("portE"), ScenarioTools.createPort("portF") };

		// Add the ports, and set the distances.
		SanityCheckTools.setPortDistances(csc, ports);

		// create a few vessels and add them to the scenario
		final int numOfClassOne = 2;
		final int numOfClassTwo = 2;
		final int numOfClassThree = 1;

		csc.addVesselSimple("classOne", numOfClassOne, 10, 10, 1000000, 10, 10, 0, 500, false);
		csc.addVesselSimple("classTwo", numOfClassTwo, 9, 15, 700000, 11, 9, 7, 0, false);
		csc.addVesselSimple("classThree", numOfClassThree, 11, 12, 500000, 13, 15, 15, 0, true);

		// create some cargos.
		inputCargos.addAll(Arrays.asList(SanityCheckTools.addCargos(csc, ports, loadPrice, dischargePrice, cvValue)));

		final Scenario scenario = csc.buildScenario();

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);

		// print the legs to console
		for (final CargoAllocation ca : result.getCargoAllocations()) {
			ScenarioTools.printCargoAllocation(ca.getName(), ca);
		}

		// print each vessel's sequence
		ScenarioTools.printSequences(result);

		// check the output
		this.checkCargos(result, inputCargos);
	}

	/**
	 * Check all the cargos in output are in the input cargos once.
	 * 
	 * @param result
	 *            The evaluated scenario (containing the cargos in the output)
	 * @param inputCargos
	 *            The cargos that were input into the unevaluated scenario.
	 */
	private void checkCargos(final Schedule result, final ArrayList<Cargo> inputCargos) {

		final int numOfInputCargos = inputCargos.size();

		Assert.assertEquals("Same number of cargoes in the output as in the input", numOfInputCargos, result.getCargoAllocations().size());

		for (final CargoAllocation ca : result.getCargoAllocations()) {

			final Cargo c = (Cargo) (ca.getLoadSlot().eContainer());

			Assert.assertTrue("Input cargo is in output", inputCargos.contains(c));

			inputCargos.remove(c);
		}

		Assert.assertEquals("All cargos in output", 0, inputCargos.size());
	}
}