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

	// @Ignore("scenario file not up to date and test is incomplete")
	@Test
	public void test() throws IOException, IncompleteScenarioException, InterruptedException {

		// final URL url = getClass().getResource("/hand-tweaked.scenario");
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

		final boolean printFitnessMap = false;

		if (printFitnessMap) {
			printFitnessesAsMap("originalFitnesses", originalScenarioRunner.getFinalSchedule().getFitness());
			printFitnessesAsMap("endFitnesses", endScenarioRunner.getFinalSchedule().getFitness());
		}

		else {
			final HashMap<String, Long> originalFitnesses = new HashMap<String, Long>();
			originalFitnesses.put("cargo-scheduler-canal-cost", 2065000L);
			originalFitnesses.put("cargo-scheduler-volume-allocation", 0L);
			originalFitnesses.put("cargo-scheduler-cost-cooldown", 30692220L);
			originalFitnesses.put("cargo-scheduler-charter-revenue", 0L);
			originalFitnesses.put("cargo-scheduler-lateness", 0L);
			originalFitnesses.put("cargo-scheduler-cost-base", 24769115L);
			originalFitnesses.put("cargo-scheduler-cost-lng", 224278136L);
			originalFitnesses.put("cargo-scheduler-charter-cost", 0L);


			final HashMap<String, Long> endFitnesses = new HashMap<String, Long>();
			endFitnesses.put("cargo-scheduler-canal-cost", 1375000L);
			endFitnesses.put("cargo-scheduler-volume-allocation", 0L);
			endFitnesses.put("cargo-scheduler-cost-cooldown", 29076840L);
			endFitnesses.put("cargo-scheduler-charter-revenue", 0L);
			endFitnesses.put("cargo-scheduler-lateness", 0L);
			endFitnesses.put("cargo-scheduler-cost-base", 29881125L);
			endFitnesses.put("cargo-scheduler-cost-lng", 222873171L);
			endFitnesses.put("cargo-scheduler-charter-cost", 0L);

			testOriginalAndCurrentFitnesses(originalFitnesses, originalScenarioRunner.getIntialSchedule().getFitness());
			testOriginalAndCurrentFitnesses(endFitnesses, endScenarioRunner.getFinalSchedule().getFitness());
		}
	}

	private void printFitnessesAsMap(String mapName, EList<ScheduleFitness> fitnesses) {

		System.out.println();
		System.out.println("final HashMap<String, Long> " + mapName + " = new HashMap<String, Long>();");

		for (ScheduleFitness f : fitnesses)
			System.out.println(mapName + ".put(\"" + f.getName() + "\", " + f.getValue() + "L);");

		System.out.println();
	}
	
	private void testOriginalAndCurrentFitnesses(HashMap<String, Long> originalFitnesses, EList<ScheduleFitness> currentFitnesses) {

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

}
