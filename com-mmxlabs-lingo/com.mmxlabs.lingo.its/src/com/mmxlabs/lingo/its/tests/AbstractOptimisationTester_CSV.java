/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mmxlabs.lingo.its.utils.CSVImporter;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

/**
 * Abstract class to run parameterised tests on optimisation results. Sub classes should create a method similar to the one below to run test cases. May need to also include the @RunWith annotation.
 * 
 * <pre>
 * @Parameters(name = "{0}")
 * 	public static Iterable<Object[]> generateTests() {
 * 		return Arrays.asList(new Object[][] {
 * 				{ "Test Prefix", "scenario path/" }, //
 * 				{ "Test Prefix", "scenario path/" }  //
 * 		);
 * 	}
 * </pre>
 * 
 * 
 */
@RunWith(value = Parameterized.class)
public abstract class AbstractOptimisationTester_CSV extends AbstractOptimisationResultTester {

	// Used by annotation before getting to constructor
	@SuppressWarnings("unused")
	private final String name;
	private final String scenario;

	public AbstractOptimisationTester_CSV(final String name, final String scenario) {
		this.name = name;
		this.scenario = scenario;
	}

	@Test
	public void testOptimisation() throws Exception {
		final URL url = getClass().getResource(scenario);

		LNGScenarioModel scenario = CSVImporter.importCSVScenario(url.toString());

		runScenarioWithGCO(scenario, new URL(url.toString() + "fitness"));
	}
}
