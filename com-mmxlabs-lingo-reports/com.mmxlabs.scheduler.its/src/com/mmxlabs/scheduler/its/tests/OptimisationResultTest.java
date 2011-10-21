/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.its.tests;

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
import org.junit.Ignore;
import org.junit.Test;

import scenario.Scenario;
import scenario.schedule.ScheduleFitness;

import com.mmxlabs.lngscheduler.emf.extras.IncompleteScenarioException;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?220">Case 220: Optimisation Result Test</a>
 * 
 * @author Adam Semenenko
 * 
 */
public class OptimisationResultTest {

	/**
	 * Toggle between printing a map of fitness names to fitness values to testing the map against the fitnesses generated at runtime.
	 */
	private static final boolean printFitnessMap = true;
	
	@Ignore("Currently broken.")
	@Test
	public void test() throws IOException, IncompleteScenarioException, InterruptedException {

		final URL url = getClass().getResource("/optimisationResultTest.scenario");

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

		final ScenarioRunner originalScenarioRunner = new ScenarioRunner(originalScenario);
		originalScenarioRunner.init();
		originalScenarioRunner.run();
		final ScenarioRunner endScenarioRunner = new ScenarioRunner(copy);
		endScenarioRunner.init();
		endScenarioRunner.run();


		final EList<ScheduleFitness> currentOriginalFitnesses = originalScenarioRunner.getIntialSchedule().getFitness();
		final EList<ScheduleFitness> currentEndFitnesses = endScenarioRunner.getFinalSchedule().getFitness();
		
		if (printFitnessMap) {
			printFitnessesAsMap("originalFitnesses", currentOriginalFitnesses);
			printFitnessesAsMap("endFitnesses", currentEndFitnesses);
		}

		else {
			final HashMap<String, Long> originalFitnesses = new HashMap<String, Long>();
			originalFitnesses.put("cargo-scheduler-canal-cost", 9315000L);
			originalFitnesses.put("cargo-scheduler-volume-allocation", 0L);
			originalFitnesses.put("cargo-scheduler-cost-cooldown", 29076840L);
			originalFitnesses.put("cargo-scheduler-charter-revenue", 0L);
			originalFitnesses.put("cargo-scheduler-lateness", 538000000L);
			originalFitnesses.put("cargo-scheduler-cost-base", 51417391L);
			originalFitnesses.put("cargo-scheduler-cost-lng", 253328260L);
			originalFitnesses.put("cargo-scheduler-charter-cost", 0L);


			final HashMap<String, Long> endFitnesses = new HashMap<String, Long>();
			endFitnesses.put("cargo-scheduler-canal-cost", 690000L);
			endFitnesses.put("cargo-scheduler-volume-allocation", 0L);
			endFitnesses.put("cargo-scheduler-cost-cooldown", 30692220L);
			endFitnesses.put("cargo-scheduler-charter-revenue", 0L);
			endFitnesses.put("cargo-scheduler-lateness", 0L);
			endFitnesses.put("cargo-scheduler-cost-base", 25372620L);
			endFitnesses.put("cargo-scheduler-cost-lng", 213198070L);
			endFitnesses.put("cargo-scheduler-charter-cost", 0L);

			printOldAndNew("old", originalFitnesses, currentOriginalFitnesses);
			printOldAndNew("old", endFitnesses, currentEndFitnesses);
			
			testOriginalAndCurrentFitnesses(originalFitnesses, currentOriginalFitnesses);
			testOriginalAndCurrentFitnesses(endFitnesses, currentEndFitnesses);
		}
	}

	private void printFitnessesAsMap(final String mapName, final EList<ScheduleFitness> fitnesses) {

		System.out.println();
		System.out.println("final HashMap<String, Long> " + mapName + " = new HashMap<String, Long>();");

		for (ScheduleFitness f : fitnesses)
			System.out.println(mapName + ".put(\"" + f.getName() + "\", " + f.getValue() + "L);");

		System.out.println();
	}
	
	private void testOriginalAndCurrentFitnesses(final HashMap<String, Long> originalFitnesses, final EList<ScheduleFitness> currentFitnesses) {

		long totalOriginalFitness = 0;
		long totalCurrentFitness = 0;

		for (ScheduleFitness f : currentFitnesses) {

			final long originalFitnessValue = originalFitnesses.get(f.getName()).longValue();
			final long currentFitness = f.getValue();

			Assert.assertEquals(f.getName() + " - Previous fitness matches current fitness", originalFitnessValue, currentFitness);

			totalOriginalFitness += originalFitnessValue;
			totalCurrentFitness += currentFitness;
		}

		Assert.assertEquals("Total original fitnesses equal current fitnesses", totalOriginalFitness, totalCurrentFitness);
	}
	
	private void printOldAndNew(final String name, final HashMap<String, Long> originalFitnesses, final EList<ScheduleFitness> currentFitnesses) { 

		System.out.println(name);
		for (ScheduleFitness f : currentFitnesses) {
			System.out.println(f.getName() + ": " + originalFitnesses.get(f.getName()).longValue() + ", " + f.getValue());
		}
		System.out.println();
	}

}
