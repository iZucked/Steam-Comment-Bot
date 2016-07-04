/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless.exporter;

import java.io.File;
import java.nio.file.Path;
import com.google.inject.Injector;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimiser;

public abstract class AbstractRunExporter implements IRunExporter {

	protected File outputFile;

	protected Path folder;

	protected String fileNameSuffix;

	protected void reset() {
		outputFile = null;
	}

	@Override
	public void setPhase(String phase, Injector injector) {
		reset();

		this.outputFile = folder.resolve(phase + "." + fileNameSuffix).toFile();
	}

	@Override
	public final void setOutputFile(Path folder, String fileNameSuffix) {
		this.folder = folder;
		this.fileNameSuffix = fileNameSuffix;
	}

	@Override
	public abstract void begin(final IOptimiser optimiser, final long initialFitness, final IAnnotatedSolution annotatedSolution);

	@Override
	public abstract void report(final IOptimiser optimiser, final int iteration, final long currentFitness, final long bestFitness, final IAnnotatedSolution currentSolution,
			final IAnnotatedSolution bestSolution);

	@Override
	public abstract void done(final IOptimiser optimiser, final long bestFitness, final IAnnotatedSolution annotatedSolution);

	@Override
	public abstract void exportData();

}
