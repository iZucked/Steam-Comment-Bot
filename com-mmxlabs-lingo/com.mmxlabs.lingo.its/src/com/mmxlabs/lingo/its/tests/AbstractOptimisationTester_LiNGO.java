/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Abstract class to run parameterised tests on optimisation results. Sub classes should create a method similar to the one below to run test cases. May need to also include the @RunWith annotation.
 * 
 * <pre>
 * &#64;Parameters(name = "{0}")
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
public abstract class AbstractOptimisationTester_LiNGO extends AbstractOptimisationResultTester {

	// Used by annotation before getting to constructor
	@SuppressWarnings("unused")
	private final String name;
	private final String scenario;

	public AbstractOptimisationTester_LiNGO(final String name, final String scenario) {
		this.name = name;
		this.scenario = scenario;
	}

	@Test
	public void testOptimisation() throws Exception {

		// Load the scenario to test
		final URL scenarioURL = getClass().getResource(scenario);
		runScenarioWithGCO(new LiNGOTestDataProvider(scenarioURL));
	}
}
