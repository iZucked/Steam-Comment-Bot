/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless.exporter;

import java.nio.file.Path;

import com.google.inject.Injector;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;

public interface IRunExporter extends IOptimiserProgressMonitor {

	void setOutputFile(Path folder, String fileNameSuffix);

	void exportData();

	void setPhase(String phase, Injector injector);
}
