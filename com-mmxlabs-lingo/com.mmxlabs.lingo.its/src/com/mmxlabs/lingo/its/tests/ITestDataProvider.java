/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.util.CheckedConsumer;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public interface ITestDataProvider {

	File getFitnessDataAsFile() throws URISyntaxException, IOException;

	URL getFitnessDataAsURL() throws MalformedURLException, IOException;

	<E extends Exception> void execute(CheckedConsumer<@NonNull IScenarioDataProvider, E> testRunner) throws Exception, E;

}