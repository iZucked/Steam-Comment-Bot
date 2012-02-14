/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.its.tests.scenarios;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;

import junit.framework.Assert;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import scenario.Scenario;
import scenario.ScenarioPackage;
import scenario.schedule.ScheduleFitness;

import com.mmxlabs.lngscheduler.emf.extras.IncompleteScenarioException;
import com.mmxlabs.scheduler.its.tests.ScenarioRunner;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?220">Case 220: Optimisation Result Test</a>
 * 
 * @author Adam Semenenko
 * 
 */
public class AbstractOptimisationResultTester {

	static {
		// Trigger EMF initialisation outside of eclipse environment.
		@SuppressWarnings("unused")
		final ScenarioPackage einstance = ScenarioPackage.eINSTANCE;
	}

	/**
	 * Toggle between printing a map of fitness names to fitness values to testing the map against the fitnesses generated at runtime.
	 */
	private static final boolean printFitnessMap = false;
	private static final String originalFitnessesMapName = "originalFitnesses";
	private static final String endFitnessesMapName = "endFitnesses";

	public AbstractOptimisationResultTester() {
		super();
	}

	/**
	 * If run on two separate occasions the fitnesses generated need to be identical. This method tests this by being run twice. The first execution prints out a map that maps the name of the fitness
	 * to the value to the console. This is copied and pasted into the method. The second execution will test that map against a the fitnesses that have been generated again.
	 * 
	 * @throws IOException
	 * @throws IncompleteScenarioException
	 * @throws InterruptedException
	 */
	public void runScenario(final URL url, final HashMap<String, Long> originalFitnesses, final HashMap<String, Long> endFitnesses) throws IOException, IncompleteScenarioException {
		final Resource resource = new XMIResourceImpl(URI.createURI(url.toString()));
		resource.load(Collections.emptyMap());

		final ResourceSetImpl resourceSet = new ResourceSetImpl();
		resourceSet.getResources().add(resource);

		final Scenario originalScenario = (Scenario) (resource.getAllContents().next());

		final Scenario copy = EcoreUtil.copy(originalScenario);

		final Resource cpyResource = new XMIResourceImpl(resource.getURI());
		cpyResource.getContents().add(copy);
		final ResourceSetImpl cpyResourceSet = new ResourceSetImpl();
		cpyResourceSet.getResources().add(cpyResource);

		// Create two scenario runners.
		// TODO are two necessary?
		final ScenarioRunner originalScenarioRunner = new ScenarioRunner(originalScenario);
		originalScenarioRunner.init();
		originalScenarioRunner.run();
		final ScenarioRunner endScenarioRunner = new ScenarioRunner(copy);
		endScenarioRunner.init();
		endScenarioRunner.run();

		// get the fitnesses.
		final EList<ScheduleFitness> currentOriginalFitnesses = originalScenarioRunner.getIntialSchedule().getFitness();
		final EList<ScheduleFitness> currentEndFitnesses = endScenarioRunner.getFinalSchedule().getFitness();

		if (printFitnessMap) {
			printFitnessesAsMap(originalFitnessesMapName, currentOriginalFitnesses);
			printFitnessesAsMap(endFitnessesMapName, currentEndFitnesses);
		} else {

			// print them to console (for manual checking)
			printOldAndNew("original", originalFitnesses, currentOriginalFitnesses);
			printOldAndNew("end", endFitnesses, currentEndFitnesses);

			// Assert old and new are equal
			testOriginalAndCurrentFitnesses(originalFitnesses, currentOriginalFitnesses);
			testOriginalAndCurrentFitnesses(endFitnesses, currentEndFitnesses);
		}
	}

	/**
	 * Prints the given EList as a Map to the console.
	 * 
	 * @param mapName
	 *            The variable name of the map.
	 * @param fitnesses
	 *            A list of fitnesses to print.
	 */
	private void printFitnessesAsMap(final String mapName, final EList<ScheduleFitness> fitnesses) {

		System.out.println();
		System.out.println("final HashMap<String, Long> " + mapName + " = new HashMap<String, Long>();");

		for (final ScheduleFitness f : fitnesses) {
			System.out.println(mapName + ".put(\"" + f.getName() + "\", " + f.getValue() + "L);");
		}

		System.out.println();
	}

	/**
	 * Test the original (previously generated) fitnesses against the current. Also test that the total of the original and current are equal.
	 */
	private void testOriginalAndCurrentFitnesses(final HashMap<String, Long> originalFitnesses, final EList<ScheduleFitness> currentFitnesses) {

		long totalOriginalFitness = 0;
		long totalCurrentFitness = 0;

		for (final ScheduleFitness f : currentFitnesses) {

			// get the values
			final long originalFitnessValue = originalFitnesses.get(f.getName()).longValue();
			final long currentFitness = f.getValue();

			// test they are equal
			Assert.assertEquals(f.getName() + " - Previous fitness matches current fitness", originalFitnessValue, currentFitness);

			// add to total
			totalOriginalFitness += originalFitnessValue;
			totalCurrentFitness += currentFitness;
		}

		// test totals are equal
		Assert.assertEquals("Total original fitnesses equal current fitnesses", totalOriginalFitness, totalCurrentFitness);
	}

	/**
	 * Print the old and new fitnesses to the console.
	 * 
	 * @param name
	 *            The name of the fitnesses (for identification in the console).
	 * @param originalFitnesses
	 *            The fitnesses previously generated.
	 * @param currentFitnesses
	 *            The fitnesses generated in this execution.
	 */
	private void printOldAndNew(final String name, final HashMap<String, Long> originalFitnesses, final EList<ScheduleFitness> currentFitnesses) {

		System.out.println(name);
		for (final ScheduleFitness f : currentFitnesses) {
			System.out.println(f.getName() + ": " + originalFitnesses.get(f.getName()).longValue() + ", " + f.getValue());
		}
		System.out.println();
	}

}