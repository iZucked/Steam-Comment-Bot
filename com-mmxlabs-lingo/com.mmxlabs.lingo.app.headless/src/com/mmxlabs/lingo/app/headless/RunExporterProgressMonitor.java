/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.util.List;

import com.mmxlabs.lingo.app.headless.exporter.IRunExporter;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;

public final class RunExporterProgressMonitor implements IOptimiserProgressMonitor {
	private final List<IRunExporter> exporters;

	RunExporterProgressMonitor(final List<IRunExporter> exporters) {
		this.exporters = exporters;
	}

	@Override
	public void begin(final IOptimiser optimiser, final long initialFitness, final IAnnotatedSolution annotatedSolution) {

		for (final IRunExporter exporter : exporters) {
			exporter.begin(optimiser, initialFitness, annotatedSolution);
		}
	}

	@Override
	public void report(final IOptimiser optimiser, final int iteration, final long currentFitness, final long bestFitness, final IAnnotatedSolution currentSolution,
			final IAnnotatedSolution bestSolution) {

		for (final IOptimiserProgressMonitor exporter : exporters) {
			exporter.report(optimiser, iteration, currentFitness, bestFitness, currentSolution, bestSolution);
		}
	}

	@Override
	public void done(final IOptimiser optimiser, final long bestFitness, final IAnnotatedSolution annotatedSolution) {

		for (final IOptimiserProgressMonitor exporter : exporters) {
			exporter.done(optimiser, bestFitness, annotatedSolution);
		}
	}
}