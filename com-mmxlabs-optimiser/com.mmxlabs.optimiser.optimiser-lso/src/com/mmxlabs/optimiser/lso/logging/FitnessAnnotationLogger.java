/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.logging;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;

public class FitnessAnnotationLogger {

	private LinearSimulatedAnnealingFitnessEvaluator lsafe;

	private final Map<Integer, Map<String, Long>> fitnessComponentTrace = new LinkedHashMap<Integer, Map<String, Long>>();

	private final Set<String> componentNames = new TreeSet<String>();

	public FitnessAnnotationLogger(IFitnessEvaluator lsafe) {
		this.lsafe = (LinearSimulatedAnnealingFitnessEvaluator) lsafe;
	}

	public void report(final int iteration) {

		final Map<String, Long> componentValues = lsafe.getBestFitnesses();

		componentNames.addAll(componentValues.keySet());

		fitnessComponentTrace.put(iteration, new LinkedHashMap<String, Long>(componentValues));
	}

	public void exportData(PrintWriter writer) {
		if (writer == null) {
			return;
		}

		// Write out the header
		writer.print("Iteration,");
		for (final String component : componentNames) {
			writer.print(",");
			writer.print(component);
		}
		writer.println();

		// Print Data rows.
		for (final Integer key : fitnessComponentTrace.keySet()) {
			final Map<String, Long> components = fitnessComponentTrace.get(key);

			writer.print(key);

			for (final String component : componentNames) {
				writer.print(",");
				writer.print(components.get(component));
			}
			writer.println();
		}

	}

}
