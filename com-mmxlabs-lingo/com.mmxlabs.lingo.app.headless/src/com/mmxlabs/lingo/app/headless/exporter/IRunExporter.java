/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless.exporter;

import java.io.File;

import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;

public interface IRunExporter extends IOptimiserProgressMonitor {

	void setOutputFile(File output);

	void exportData();

	void setScenarioRunner(LNGScenarioRunner scenarioRunner);
}
