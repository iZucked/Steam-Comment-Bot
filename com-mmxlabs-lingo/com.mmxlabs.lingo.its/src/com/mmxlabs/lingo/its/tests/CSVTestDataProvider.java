/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.its.scenario.CSVImporter;

public class CSVTestDataProvider implements ITestDataProvider {

	private URL scenarioURL;
	private LNGScenarioModel scenarioModel;

	public CSVTestDataProvider(URL scenarioURL) throws MalformedURLException {
		this.scenarioURL = scenarioURL;
		this.scenarioModel = CSVImporter.importCSVScenario(scenarioURL.toString());
	}

	@Override
	public LNGScenarioModel getScenarioModel() {
		return scenarioModel;
	}

	@Override
	public File getFitnessDataAsFile() throws IOException, URISyntaxException {
		URL fileURL = FileLocator.toFileURL(scenarioURL);
		final URL propertiesFile = new URL(fileURL.toString().replaceAll(" ", "%20") + "fitness.properties");
		return new File(propertiesFile.toURI());
	}

	@Override
	public URL getFitnessDataAsURL() throws IOException {
		final URL propertiesFile = new URL(scenarioURL.toString() + "fitness.properties");
		return propertiesFile;
	}

}