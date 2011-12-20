/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.its.tests.sanityChecks;

import java.util.Arrays;

import junit.framework.Assert;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

import scenario.Scenario;
import scenario.port.Port;

import com.mmxlabs.demo.app.wizards.CustomScenarioCreator;
import com.mmxlabs.lngscheduler.emf.extras.tests.calculation.ScenarioTools;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?254">Case 254: Check input attributes have not unexpectedly changed</a>
 * 
 * 1. Create scenario<br>
 * 2. Copy scenario (easiest way is with copy = ECoreUtil.copy(foo))<br>
 * 3. evaluate original scenario<br>
 * 4. Check that copy = original.<br>
 * 
 * @author Adam
 */
public class InputAttributesCheck {

	private static final int dischargePrice = 1;
	private CustomScenarioCreator csc = new CustomScenarioCreator(dischargePrice);

	/**
	 * Check that the scenario doesn't change after being evaluated. The scenario being tested is a simple one.
	 * 
	 * @throws InterruptedException
	 *             Thrown by the method that checks the original and evaluated scenarios are the same
	 */
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

		// createVessels creates and adds the vessesl to the scenario.
		// Add the created vessels to the list of input vessels.
		Arrays.asList(csc.addVesselSimple("classOne", numOfClassOne, 10, 10, 1000000, 10, 10, 0, 500, false));
		Arrays.asList(csc.addVesselSimple("classTwo", numOfClassTwo, 9, 15, 700000, 11, 9, 7, 0, false));
		Arrays.asList(csc.addVesselSimple("classThree", numOfClassThree, 20, 25, 10000, 17, 14, 10, 1000, false));
		Arrays.asList(csc.addVesselSimple("classFour", numOfClassFour, 15, 20, 150000, 20, 10, 5, 2000, true));

		// create some cargos.
		SanityCheckTools.addCargos(csc, ports, loadPrice, dischargePrice, cvValue);

		final Scenario scenario = csc.buildScenario();
		final Scenario copiedScenario = EcoreUtil.copy(scenario);

		assertScenariosEqual("Copy has been made correctly", copiedScenario, scenario);
		ScenarioTools.evaluate(scenario);
		assertScenariosEqual("Evaluated scenario is the same", copiedScenario, scenario);
	}

	/**
	 * Check that there are no differences between the two given scenarios.
	 * 
	 * @param assertionMessage
	 *            A message for the assertion ({@link Assert#assertTrue(String, boolean)}).
	 * @param originalScenario
	 *            The original scenario
	 * @param otherScenario
	 *            Another scenario
	 * @throws InterruptedException
	 */
	private void assertScenariosEqual(final String assertionMessage, final Scenario originalScenario, final Scenario otherScenario) throws InterruptedException {

		final MatchModel match = MatchService.doMatch(originalScenario, otherScenario, null);
		final DiffModel diff = DiffService.doDiff(match);
		final EList<DiffElement> differences = diff.getDifferences();
		// Dump any differences to std.err before asserting
		for (final DiffElement d : differences) {
			System.err.println(d.toString());
		}

		Assert.assertTrue(assertionMessage, differences.isEmpty());
	}
}
