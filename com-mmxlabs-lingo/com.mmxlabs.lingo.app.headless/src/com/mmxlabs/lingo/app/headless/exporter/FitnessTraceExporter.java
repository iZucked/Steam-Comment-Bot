/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless.exporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.mmxlabs.lingo.app.headless.ScenarioRunner;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;

public class FitnessTraceExporter implements IRunExporter {

	private LinearSimulatedAnnealingFitnessEvaluator lsafe;

	private int numberOfIterations;

	private final Map<Integer, Map<String, Long>> fitnessComponentTrace = new LinkedHashMap<Integer, Map<String, Long>>();
	private final Map<Integer, Long> fitnessTrace = new LinkedHashMap<Integer, Long>();

	private final Set<String> componentNames = new TreeSet<String>();

	private File outputFile;

	public FitnessTraceExporter() {

	}

	@Override
	public void setScenarioRunner(final ScenarioRunner runner) {

		lsafe = (LinearSimulatedAnnealingFitnessEvaluator) runner.getInjector()
				.getInstance(IFitnessEvaluator.class);
		numberOfIterations = runner
				.getInjector()
				.getInstance(
						Key.get(Integer.class,
								Names.named(LocalSearchOptimiserModule.LSO_NUMBER_OF_ITERATIONS)));
	}

	public void exportData(final OutputStream os) {

	}

	@Override
	public void begin(final IOptimiser optimiser, final long initialFitness,
			final IAnnotatedSolution annotatedSolution) {
		final Map<String, Long> componentValues = lsafe.getBestFitnesses();

		componentNames.addAll(componentValues.keySet());

		fitnessComponentTrace.put(0, new LinkedHashMap<String, Long>(
				componentValues));
		fitnessTrace.put(0, initialFitness);
	}

	@Override
	public void report(final IOptimiser optimiser, final int iteration,
			final long currentFitness, final long bestFitness,
			final IAnnotatedSolution currentSolution,
			final IAnnotatedSolution bestSolution) {

		final Map<String, Long> componentValues = lsafe.getBestFitnesses();

		componentNames.addAll(componentValues.keySet());

		fitnessComponentTrace.put(iteration, new LinkedHashMap<String, Long>(
				componentValues));

		fitnessTrace.put(iteration, bestFitness);
	}

	@Override
	public void done(final IOptimiser optimiser, final long bestFitness,
			final IAnnotatedSolution annotatedSolution) {

		final Map<String, Long> componentValues = lsafe.getBestFitnesses();

		componentNames.addAll(componentValues.keySet());

		fitnessComponentTrace.put(numberOfIterations,
				new LinkedHashMap<String, Long>(componentValues));

		fitnessTrace.put(numberOfIterations, bestFitness);
	}

	@Override
	public void setOutputFile(File output) {
		this.outputFile = output;
	}

	@Override
	public void exportData() {
		if (outputFile == null) {
			return;
		}
		PrintWriter writer = null;

		try {

			writer = new PrintWriter(new FileOutputStream(outputFile));

			// Write out the header
			writer.print("Iteration,");
			writer.print("Total");
			for (final String component : componentNames) {
				writer.print(",");
				writer.print(component);
			}
			writer.println();

			// Print Data rows.
			for (final Integer key : fitnessTrace.keySet()) {
				final Map<String, Long> components = fitnessComponentTrace
						.get(key);
				final Long fitness = fitnessTrace.get(key);

				writer.print(key);
				writer.print(",");
				writer.print(fitness);

				for (final String component : componentNames) {
					writer.print(",");
					writer.print(components.get(component));
				}
				writer.println();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

}
