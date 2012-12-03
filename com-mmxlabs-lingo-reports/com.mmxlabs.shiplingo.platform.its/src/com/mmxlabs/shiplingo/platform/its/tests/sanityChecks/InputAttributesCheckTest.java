/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.its.tests.sanityChecks;

import java.io.IOException;

import junit.framework.Assert;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.shiplingo.platform.its.tests.CustomScenarioCreator;
import com.mmxlabs.shiplingo.platform.its.tests.calculation.ScenarioTools;

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

		final MMXRootObject scenario = csc.buildScenario();
		final MMXRootObject copiedScenario = EcoreUtil.copy(scenario);

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
	private void assertScenariosEqual(final String assertionMessage, final MMXRootObject originalScenario, final MMXRootObject otherScenario) throws InterruptedException {

		final IComparisonScope scope = EMFCompare.createDefaultScope(originalScenario, otherScenario);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();

		final EList<Diff> differences = comparison.getDifferences();
		// Dump any differences to std.err before asserting
		for (final Diff d : differences) {
			System.err.println(d.toString());
		}

		Assert.assertTrue(assertionMessage, differences.isEmpty());
	}
}
