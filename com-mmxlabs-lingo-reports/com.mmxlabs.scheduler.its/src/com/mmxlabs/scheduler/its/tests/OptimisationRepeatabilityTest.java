/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.its.tests;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import scenario.Scenario;
import scenario.ScenarioPackage;

import com.mmxlabs.lngscheduler.emf.extras.IncompleteScenarioException;

/**
 * Run a test scenario several times to try and see if it varies.
 * 
 * @author Simon Goodall
 * 
 */
public class OptimisationRepeatabilityTest {

	@BeforeClass
	public static void registerEMF() {

		// Load up the Scenario Package 
		@SuppressWarnings("unused")
		final ScenarioPackage einstance = ScenarioPackage.eINSTANCE;
	}

	@Test
	public void testScenario() throws IOException, InterruptedException,
			IncompleteScenarioException {

		final URL url = getClass().getResource("/test.scenario");

		testScenario(url, 5);
	}

	private void testScenario(final URL url, final int numTries)
			throws IOException, InterruptedException,
			IncompleteScenarioException {

		final Resource resource = new XMIResourceImpl(URI.createURI(url
				.toString()));
		resource.load(Collections.emptyMap());

		final ResourceSetImpl resourceSet = new ResourceSetImpl();
		resourceSet.getResources().add(resource);

		final Scenario originalScenario = (Scenario) (resource.getAllContents()
				.next());

		final ScenarioRunner[] scenarioRunners = new ScenarioRunner[numTries];
		for (int i = 0; i < numTries; ++i) {

			final Scenario copy = EcoreUtil.copy(originalScenario);

			final Resource cpyResource = new XMIResourceImpl(resource.getURI());
			cpyResource.getContents().add(copy);
			final ResourceSetImpl cpyResourceSet = new ResourceSetImpl();
			cpyResourceSet.getResources().add(cpyResource);

			scenarioRunners[i] = new ScenarioRunner(copy);
			scenarioRunners[i].init();

			if (i > 0) {
				// Ensure same initial schedules
				final MatchModel match = MatchService.doMatch(
						scenarioRunners[0].getIntialSchedule(),
						scenarioRunners[i].getIntialSchedule(), null);
				final DiffModel diff = DiffService.doDiff(match);
				final EList<DiffElement> differences = diff.getDifferences();
				// Dump any differences to std.err before asserting
				for (final DiffElement d : differences) {
					System.err.println(d.toString());
				}
								
				Assert.assertTrue("initial solution 0 and " + i + " should be the same", differences.isEmpty());
			}
		}

		// Run all the optimisations
		for (int i = 0; i < numTries; ++i) {
			scenarioRunners[i].run();
			if (i > 0) {
				// Ensure same final schedules
				final MatchModel match = MatchService.doMatch(
						scenarioRunners[0].getFinalSchedule(),
						scenarioRunners[i].getFinalSchedule(), null);
				final DiffModel diff = DiffService.doDiff(match);
				final EList<DiffElement> differences = diff.getDifferences();

				// Dump any differences to std.err before asserting
				for (final DiffElement d : differences) {
					System.err.println(d.toString());
				}

				Assert.assertTrue(differences.isEmpty());
			}
		}

		// Ensure source model is unchanged
		for (int i = 0; i < numTries; ++i) {
			final MatchModel match = MatchService.doMatch(originalScenario,
					scenarioRunners[i].getScenario(), null);
			final DiffModel diff = DiffService.doDiff(match);
			final EList<DiffElement> differences = diff.getDifferences();
			// Dump any differences to std.err before asserting
			for (final DiffElement d : differences) {
				System.err.println(d.toString());
			}

			Assert.assertTrue("final solution 0 and " + i + " should be the same", differences.isEmpty());
		}
	}
}
