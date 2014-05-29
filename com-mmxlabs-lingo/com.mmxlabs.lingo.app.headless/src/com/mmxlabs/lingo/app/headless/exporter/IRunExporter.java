/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless.exporter;

import java.io.File;

import com.mmxlabs.lingo.app.headless.ScenarioRunner;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;

public interface IRunExporter extends IOptimiserProgressMonitor {

	void setOutputFile(File output);

	void exportData();

	void setScenarioRunner(ScenarioRunner scenarioRunner);
}
