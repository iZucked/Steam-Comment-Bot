/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

	public void exportData(JSONArray array) {
		if (array == null) {
			return;
		}

		// Print Data rows.
		for (final Integer key : fitnessComponentTrace.keySet()) {
			JSONObject m2 = new JSONObject();
			final Map<String, Long> components = fitnessComponentTrace.get(key);

			for (final String component : componentNames) {
				m2.put(component, components.get(component));
			}
			m2.put("iteration", key);
			array.add(m2);
		}
	}

}
