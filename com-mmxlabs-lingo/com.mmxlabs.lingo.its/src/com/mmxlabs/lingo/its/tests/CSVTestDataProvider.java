/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.util.CheckedConsumer;
import com.mmxlabs.models.lng.transformer.its.scenario.CSVImporter;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class CSVTestDataProvider implements ITestDataProvider {

	private final URL scenarioURL;

	public CSVTestDataProvider(final URL scenarioURL) throws MalformedURLException {
		this.scenarioURL = scenarioURL;
	}

	@Override
	public File getFitnessDataAsFile() throws IOException, URISyntaxException {
		final URL fileURL = FileLocator.toFileURL(scenarioURL);
		final URL propertiesFile = new URL(fileURL.toString().replaceAll(" ", "%20") + "fitness.properties");
		return new File(propertiesFile.toURI());
	}

	@Override
	public URL getFitnessDataAsURL() throws IOException {
		final URL propertiesFile = new URL(scenarioURL.toString() + "fitness.properties");
		return propertiesFile;
	}

	@Override
	public <E extends Exception> void execute(final CheckedConsumer<@NonNull IScenarioDataProvider, E> testRunner) throws E, MalformedURLException {
		final @NonNull IScenarioDataProvider scenarioDataProvider = CSVImporter.importCSVScenario(scenarioURL.toString());
		testRunner.accept(scenarioDataProvider);
	}
}