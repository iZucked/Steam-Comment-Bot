/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public interface ITestDataProvider {

	LNGScenarioModel getScenarioModel();

	File getFitnessDataAsFile() throws IOException, URISyntaxException;

	URL getFitnessDataAsURL() throws IOException;

}