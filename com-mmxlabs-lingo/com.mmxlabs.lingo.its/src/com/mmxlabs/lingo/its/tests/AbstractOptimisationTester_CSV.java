/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.its.scenario.CSVImporter;

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
		runScenarioWithGCO(new CSVTestDataProvider(url));
	}
}
