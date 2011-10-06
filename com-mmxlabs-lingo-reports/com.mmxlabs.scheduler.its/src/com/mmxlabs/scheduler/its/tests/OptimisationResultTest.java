/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.its.tests;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;

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

	@Ignore("scenario file not up to date and test is incomplete")
	@Test
	public void test() throws IOException, IncompleteScenarioException, InterruptedException {

		final URL url = getClass().getResource("/hand-tweaked.scenario");

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

		System.out.println("Original");
		for (ScheduleFitness f : originalScenarioRunner.getIntialSchedule().getFitness()) {
			System.out.println(f);
		}
		System.out.println("End");
		for (ScheduleFitness f : endScenarioRunner.getFinalSchedule().getFitness()) {
			System.out.println(f);
		}
	}
}
