/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.net.URL;
import java.util.function.Consumer;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Abstract class to run parameterised tests on report generation which require a selected element. Sub classes should create a method similar to the one below to run test cases. May need to also
 * include the @RunWith annotation.
 * 
 * <pre>
 * &#64;Parameters(name = "{0}")
 * 	public static Iterable<Object[]> generateTests() {
 * 		return Arrays.asList(new Object[][] {
 * 				{ "Test Prefix", "scenario path.lingo" , "Element ID" }, //
 * 				{ "Test Prefix", "scenario path.lingo", "Element ID" }  //
 * 		);
 * 	}
 * </pre>
 * 
 * 
 */
@RunWith(value = Parameterized.class)
public abstract class AbstractSelectedElementReportTester_LiNGO extends AbstractSelectedElementReportTester {

	private final Pair<String, String> key;

	private final String elementID;

	public AbstractSelectedElementReportTester_LiNGO(final String name, final String scenarioPath, String elementID) throws Exception {

		this.elementID = elementID;
		key = new Pair<>(name, scenarioPath);
	}

	@Override
	protected void testReports(final String reportID, final String shortName, final String extension, @Nullable Consumer<ScenarioInstance> preAction) throws Exception {
		final URL url = getClass().getResource(key.getSecond());
		final URI uri = URI.createURI(FileLocator.toFileURL(url).toString().replaceAll(" ", "%20"));

		ScenarioStorageUtil.withExternalScenarioModel(uri, (instance, scenarioModel) -> {
			ReportTester.testReportsWithElement(instance, (LNGScenarioModel) scenarioModel, url, reportID, shortName, extension, elementID, preAction);
		}, new NullProgressMonitor());
	}
}
