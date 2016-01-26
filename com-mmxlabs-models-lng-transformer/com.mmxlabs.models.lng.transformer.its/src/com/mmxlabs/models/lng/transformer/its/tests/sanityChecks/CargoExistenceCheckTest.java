/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;

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
@RunWith(value = ShiroRunner.class)
public class CargoExistenceCheckTest {

	private static final int dischargePrice = 1;
	private final CustomScenarioCreator csc = new CustomScenarioCreator(dischargePrice);

	/**
	 * Create a scenario with some cargoes and vessels. Check that all cargoes added to the scenario are in the output.
	 */
	@Test
	public void test() {

		// A list to hold all cargoes that are input.
		final ArrayList<Cargo> inputCargoes = new ArrayList<Cargo>();

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

		// create some cargoes.
		inputCargoes.addAll(Arrays.asList(SanityCheckTools.addCargoes(csc, ports, loadPrice, dischargePrice, cvValue)));

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
		this.checkCargoes(result, inputCargoes);
	}

	/**
	 * Check all the cargoes in output are in the input cargoes once.
	 * 
	 * @param result
	 *            The evaluated scenario (containing the cargoes in the output)
	 * @param inputCargoes
	 *            The cargoes that were input into the unevaluated scenario.
	 */
	private void checkCargoes(final Schedule result, final ArrayList<Cargo> inputCargoes) {

		final int numOfInputCargoes = inputCargoes.size();

		Assert.assertEquals("Same number of cargoes in the output as in the input", numOfInputCargoes, result.getCargoAllocations().size());

		for (final CargoAllocation ca : result.getCargoAllocations()) {

			final Cargo c = ca.getInputCargo();

			Assert.assertTrue("Input cargo is in output", inputCargoes.contains(c));

			inputCargoes.remove(c);
		}

		Assert.assertEquals("All cargoes in output", 0, inputCargoes.size());
	}
}