/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.its.tests.scenarios;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

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
import com.mmxlabs.shiplingo.platform.its.tests.ScenarioRunner;

/**
 * 
 * Abstract class to test the optimisation reproducibility of a scenario. Super classes pass in a URL to a scenario resource. There are two modes of operation, determined by the boolean
 * {@link #storeFitnessMap}. When set to false (default) a properties file (located at the input URL with ".properties" appended) contains the expected initial and final fitness values of the
 * scenario. The second mode ({@link #storeFitnessMap} set to true) will generate the properties file. Note this mode will only work for file:// URLs. It is expected that a JUnit test run will provide
 * file based URLs. It is expected JUnit plugin tests will not.
 * 
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
	 * Toggle between storing fitness names and values in a properties file and testing the current fitnesses against the stored values. Note if this value is true, this should be run as part of a
	 * JUnit test rather than a plugin test as the URL to File conversion may not work as expected.
	 */
	private static final boolean storeFitnessMap = false;

	// Key prefixes used in the properties file.
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
	public void runScenario(final URL url) throws IOException, IncompleteScenarioException {
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

		if (storeFitnessMap) {

			/**
			 * Extend to save properties in a sorted order for ease of reading
			 */
			@SuppressWarnings("serial")
			Properties props = new Properties() {
				@Override
				public Set<Object> keySet() {
					return Collections.unmodifiableSet(new TreeSet<Object>(super.keySet()));
				}

				@Override
				public synchronized Enumeration<Object> keys() {
					return Collections.enumeration(new TreeSet<Object>(super.keySet()));
				}
			};

			storeFitnesses(props, originalFitnessesMapName, currentOriginalFitnesses);
			storeFitnesses(props, endFitnessesMapName, currentEndFitnesses);

			File f;
			try {
				f = new File(new File(url.toURI()).getAbsoluteFile() + ".properties");
				props.store(new FileOutputStream(f), "Created by " + AbstractOptimisationResultTester.class.getName());
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

			Properties props = new Properties();
			props.load(new URL(url.toString() + ".properties").openStream());

			// Assert old and new are equal
			testOriginalAndCurrentFitnesses(props, originalFitnessesMapName, currentOriginalFitnesses);
			testOriginalAndCurrentFitnesses(props, endFitnessesMapName, currentEndFitnesses);
		}
	}

	/**
	 * Stores the fitness values into the {@link Properties} object.
	 * 
	 * @param mapName
	 *            The variable name prefix.
	 * @param fitnesses
	 *            A list of fitnesses to store.
	 */

	private void storeFitnesses(Properties props, final String mapName, final EList<ScheduleFitness> fitnesses) {

		for (final ScheduleFitness f : fitnesses) {
			props.setProperty(mapName + "." + f.getName(), Long.toString(f.getValue()));
		}
	}

	/**
	 * Test the original (previously generated) fitnesses against the current. Also test that the total of the original and current are equal.
	 */
	private void testOriginalAndCurrentFitnesses(Properties props, String mapName, final EList<ScheduleFitness> currentFitnesses) {

		long totalOriginalFitness = 0;
		long totalCurrentFitness = 0;

		for (final ScheduleFitness f : currentFitnesses) {

			// get the values
			final long originalFitnessValue = Long.parseLong(props.getProperty(mapName + "." + f.getName(), "0"));
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
}