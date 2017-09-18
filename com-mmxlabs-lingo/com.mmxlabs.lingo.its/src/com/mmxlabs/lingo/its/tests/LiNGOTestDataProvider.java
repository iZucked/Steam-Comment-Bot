/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class LiNGOTestDataProvider implements ITestDataProvider {

	private URL scenarioURL;

	public LiNGOTestDataProvider(URL scenarioURL) throws IOException {
		this.scenarioURL = scenarioURL;
	}

	@Override
	public File getFitnessDataAsFile() throws URISyntaxException, IOException {
		URL fileURL = FileLocator.toFileURL(scenarioURL);
		final URL propertiesFile = new URL(fileURL.toString().replaceAll(" ", "%20") + ".properties");
		return new File(propertiesFile.toURI());
	}

	@Override
	public URL getFitnessDataAsURL() throws MalformedURLException, IOException {
		final URL propertiesFile = new URL(scenarioURL.toString() + ".properties");
		return propertiesFile;
	}

	@Override
	public <E extends Exception> void execute(CheckedConsumer<@NonNull IScenarioDataProvider, E> testRunner) throws E, Exception {
		URL fileURL = new URL(FileLocator.toFileURL(scenarioURL).toString().replaceAll(" ", "%20"));
		LNGScenarioRunnerCreator.withLiNGOFileEvaluationRunner(fileURL, runner -> testRunner.accept(runner.getScenarioDataProvider()));
	}
}