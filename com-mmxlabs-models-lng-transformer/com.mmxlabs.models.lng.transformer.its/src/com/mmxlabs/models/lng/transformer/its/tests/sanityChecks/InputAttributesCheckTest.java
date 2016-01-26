/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import java.io.IOException;

import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?254">Case 254: Check input attributes have not unexpectedly changed</a>
 * 
 * 1. Create scenario<br>
 * 2. Hook up a {@link ChangeRecorder}<br>
 * 3. evaluate original scenario<br>
 * 4. Check the {@link ChangeDescription} is empty<br>
 * 
 * @author Adam
 */
@RunWith(value = ShiroRunner.class)
public class InputAttributesCheckTest {

	private static final int dischargePrice = 1;
	private final CustomScenarioCreator csc = new CustomScenarioCreator(dischargePrice);

	/**
	 * Check that the scenario doesn't change after being evaluated. The scenario being tested is a simple one.
	 * 
	 * @throws InterruptedException
	 *             Thrown by the method that checks that the original and evaluated scenarios are the same
	 * @throws IOException
	 */
	@Ignore("Result does change as we now have a ScheduleModel")
	@Test
	public void test() throws InterruptedException {

		final int loadPrice = 1;
		final int cvValue = 10;

		// a list of ports to use in the scenario
		final Port[] ports = new Port[] { ScenarioTools.createPort("portA"), ScenarioTools.createPort("portB"), ScenarioTools.createPort("portC"), ScenarioTools.createPort("portD"),
				ScenarioTools.createPort("portE"), ScenarioTools.createPort("portF") };

		// Add the ports, and set the distances.
		SanityCheckTools.setPortDistances(csc, ports);

		// create a few vessels and add them to the list of vessels created.
		final int numOfClassOne = 3;
		final int numOfClassTwo = 7;
		final int numOfClassThree = 4;
		final int numOfClassFour = 6;

		// createVessels creates and adds the vessels to the scenario.
		csc.addVesselSimple("classOne", numOfClassOne, 10, 10, 1000000, 10, 10, 0, 500, false);
		csc.addVesselSimple("classTwo", numOfClassTwo, 9, 15, 700000, 11, 9, 7, 0, false);
		csc.addVesselSimple("classThree", numOfClassThree, 20, 25, 10000, 17, 14, 10, 1000, false);
		csc.addVesselSimple("classFour", numOfClassFour, 15, 20, 150000, 20, 10, 5, 2000, true);

		// create some cargoes.
		SanityCheckTools.addCargoes(csc, ports, loadPrice, dischargePrice, cvValue);

		final LNGScenarioModel scenario = csc.buildScenario();
		final ChangeRecorder changeRecorder = new ChangeRecorder(scenario);

		ScenarioTools.evaluate(scenario);

		final ChangeDescription changeDescription = changeRecorder.endRecording();
		Assert.assertTrue(changeDescription.getObjectChanges().isEmpty());
	}
}
