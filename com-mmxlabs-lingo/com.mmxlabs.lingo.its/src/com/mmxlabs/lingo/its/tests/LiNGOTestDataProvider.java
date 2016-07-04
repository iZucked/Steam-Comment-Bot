/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public class LiNGOTestDataProvider implements ITestDataProvider {

	private URL scenarioURL;
	private @NonNull LNGScenarioModel scenarioModel;

	public LiNGOTestDataProvider(URL scenarioURL) throws IOException {
		this.scenarioURL = scenarioURL;
		scenarioModel = LNGScenarioRunnerCreator.getScenarioModelFromURL(scenarioURL);
	}

	@Override
	public LNGScenarioModel getScenarioModel() {
		return scenarioModel;
	}

	@Override
	public File getFitnessDataAsFile() throws IOException, URISyntaxException {
		URL fileURL = FileLocator.toFileURL(scenarioURL);
		final URL propertiesFile = new URL(fileURL.toString().replaceAll(" ", "%20") + ".properties");
		return new File(propertiesFile.toURI());
	}

	@Override
	public URL getFitnessDataAsURL() throws IOException {
		final URL propertiesFile = new URL(scenarioURL.toString() + ".properties");
		return propertiesFile;
	}

}