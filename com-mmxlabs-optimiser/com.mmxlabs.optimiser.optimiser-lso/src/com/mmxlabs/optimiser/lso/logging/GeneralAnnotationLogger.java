/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.logging;

import org.eclipse.jdt.annotation.NonNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;

public class GeneralAnnotationLogger {

	private final @NonNull IFitnessEvaluator fitnessEvaluator;
	private final @NonNull IOptimisationContext context;
	private ISequences sequences;
	private IAnnotatedSolution solution;
	JSONArray rows = new JSONArray();

	public GeneralAnnotationLogger(@NonNull IFitnessEvaluator fitnessEvaluator, @NonNull IOptimisationContext context) {
		this.fitnessEvaluator = fitnessEvaluator;
		this.context = context;
		solution = null;
	}

	public void report(int iterations) {
		rows.add(getLatenessRow(iterations, getBestAnnotatedSolution()));
	}

	private IAnnotatedSolution getBestAnnotatedSolution() {
		Triple<ISequences, ISequences, IEvaluationState> bestSequences = fitnessEvaluator.getBestSequences();
		assert bestSequences != null;
		ISequences sequencesFromEvaluator = bestSequences.getSecond();
		assert sequencesFromEvaluator != null;

		if (sequencesFromEvaluator == sequences) {
			return solution;
		} else {
			solution = fitnessEvaluator.getBestAnnotatedSolution();
			sequences = sequencesFromEvaluator;
			return solution;
		}
	}

	public void exportData(JSONArray node) {
		if (node == null) {
			return;
		}
		node.addAll(rows);
	}

	private JSONObject getLatenessRow(int iterations, IAnnotatedSolution annotatedSolution) {

		JSONObject row = new JSONObject();
		row.put("iterations", iterations);
		for (String key : annotatedSolution.getGeneralAnnotationKeys()) {
			Object o = annotatedSolution.getGeneralAnnotation(key, Object.class);
			if (o instanceof Number) {
				row.put(key, o);
			} else {
				row.put(key, "0");
			}
		}
		return row;
	}

}
