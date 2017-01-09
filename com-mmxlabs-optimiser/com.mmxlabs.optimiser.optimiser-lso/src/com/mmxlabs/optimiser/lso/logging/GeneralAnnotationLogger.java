/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.logging;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.base.Joiner;
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
	private List<String> rows = new LinkedList<>();

	public GeneralAnnotationLogger(@NonNull IFitnessEvaluator fitnessEvaluator, @NonNull IOptimisationContext context) {
		this.fitnessEvaluator = fitnessEvaluator;
		this.context = context;
		solution = null;
	}

	public void report(int iterations) {
		if (rows.size() == 0) {
			rows.add(getLatenessColumns(getBestAnnotatedSolution()));
		}
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

	public void exportData(PrintWriter writer) {
		if (writer == null) {
			return;
		}
		for (String r : rows) {
			writer.println(r);
		}
	}

	private String getLatenessColumns(IAnnotatedSolution annotatedSolution) {
		String[] keys = new String[annotatedSolution.getGeneralAnnotationKeys().size() + 1];
		keys[0] = "iterations";
		int i = 0;
		for (String key : annotatedSolution.getGeneralAnnotationKeys()) {
			keys[++i] = key;
		}
		Joiner joiner = Joiner.on(",");
		return joiner.join(keys);
	}

	private String getLatenessRow(int iterations, IAnnotatedSolution annotatedSolution) {
		Joiner joiner = Joiner.on(",");
		String[] latenesses = new String[annotatedSolution.getGeneralAnnotationKeys().size() + 1];
		int idx = 0;
		latenesses[0] = "" + iterations;
		for (String key : annotatedSolution.getGeneralAnnotationKeys()) {
			Object o = annotatedSolution.getGeneralAnnotation(key, Object.class);
			// Integer lateness = annotatedSolution.getGeneralAnnotation(key, Integer.class);
			// latenesses[++idx] = "" + (lateness == null ? 0 : lateness);
			if (o instanceof Number) {
				latenesses[++idx] = "" + (o == null ? 0 : o);
			} else {
				latenesses[++idx] = "0";
			}
		}
		return joiner.join(latenesses);
	}

}
