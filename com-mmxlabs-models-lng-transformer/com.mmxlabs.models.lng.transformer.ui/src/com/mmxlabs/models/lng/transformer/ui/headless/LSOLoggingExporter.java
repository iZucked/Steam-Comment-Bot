/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.caches.MemoryUsageInfo;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserJSON;
import com.mmxlabs.optimiser.common.logging.impl.EvaluationNumberKey;
import com.mmxlabs.optimiser.lso.logging.FitnessAnnotationLogger;
import com.mmxlabs.optimiser.lso.logging.GeneralAnnotationLogger;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;

public class LSOLoggingExporter {

	private final JSONObject root;
	private final String phase;

	private final LSOLogger lsoLogger;

	public LSOLoggingExporter(final JSONArray nodes, final String phase, final LSOLogger lsoLogger) {
		this.phase = phase;
		this.lsoLogger = lsoLogger;
		root = new JSONObject();
		nodes.add(root);
		root.put("name", phase);
	}

	private JSONObject getJSONObject(final String nodeName) {
		return getJSONObject(root, nodeName);
	}

	private JSONObject getJSONObject(final JSONObject parent, final String nodeName) {
		return (JSONObject) parent.computeIfAbsent(nodeName, k -> new JSONObject());
	}

	private JSONArray getJSONArray(final String nodeName) {
		return getJSONArray(root, nodeName);
	}

	private JSONArray getJSONArray(final JSONObject parent, final String nodeName) {
		return (JSONArray) parent.computeIfAbsent(nodeName, k -> new JSONArray());
	}

	public void close() {
		writeDate(root);
	}

	private void writeDate(final JSONObject node) {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// // get current date time with Date()
		final Date date = new Date();
		node.put("date", dateFormat.format(date));
	}

	private void writeEndData() {
		// final Path newPath = getPath("results");
		final JSONObject node = getJSONObject("results");
		writeEndData(node);
	}

	private void writeEndData(final JSONObject node) {
		final Map<String, Long> endResults = lsoLogger.getEndProgressResults();
		for (final String key : endResults.keySet()) {
			node.put(key, endResults.get(key));
		}
	}

	private void writeDataOverCourseOfRun(final String... keys) {
		final List<EvaluationNumberKey> evaluations = lsoLogger.getProgressEvaluations();
		for (final String key : keys) {
			if (lsoLogger.keyInProgressLog(key)) {
				writeDataTypeOverCourseOfRun(evaluations, key);
			}
		}
	}

	private void writeDataTypeOverCourseOfRun(final List<EvaluationNumberKey> evaluations, final String key) {
		final JSONObject node = getJSONObject(key);
		for (final EvaluationNumberKey evaluationNumberKey : evaluations) {
			node.put(evaluationNumberKey.getNumber(), lsoLogger.getProgressValue(evaluationNumberKey, key));
		}
	}

	private void exportFitnessAnnotations() {
		writeFitnessAnnotations(lsoLogger.getFitnessAnnotationLogger());
	}

	private void writeFitnessAnnotations(final FitnessAnnotationLogger fitnessAnnotationLogger) {
		fitnessAnnotationLogger.exportData(getJSONArray("fitnessAnnotations"));
	}

	private void exportGeneralAnnotations() {
		writeGeneralAnnotations(lsoLogger.getGeneralAnnotationLogger());
	}

	private void writeGeneralAnnotations(final GeneralAnnotationLogger generalAnnotationLogger) {
		generalAnnotationLogger.exportData(getJSONArray("generalAnnotations"));
	}
	
	private void writeHeapUsageData(final JSONObject node, final Map<Long, MemoryUsageInfo> info) {
		node.putAll(info);
	}

	public void exportData(final String... keys) {
		writeDataOverCourseOfRun(keys);
		exportEndData();
		exportMovesData();
		exportSequencesData();
		exportConstraintFailedSequences();
		exportAcceptedSequences();
		exportRejectedSequences();
		exportAcceptedFitnesses();
		exportRejectedFitnesses();
		exportFitnessAnnotations();
		exportGeneralAnnotations();
		exportHeapUsageData();
	}

	public void exportEndData() {
		writeEndData();
	}

	public void exportMovesData() {
		final JSONObject node = getJSONObject("moves");
		writeMoveData(node);
	}

	private void exportSequencesData() {
		final JSONObject node = getJSONObject("sequences");
		writeSequencesData(node, lsoLogger.getSequenceFrequencyCounts());
	}

	private void exportConstraintFailedSequences() {
		final JSONObject node = getJSONObject("constraintFailedSequences");
		writeSequencesData(node, lsoLogger.getSequenceCountFailedConstraint());
	}

	private void exportAcceptedSequences() {
		final JSONObject node = getJSONObject("acceptedSequences");
		writeSequencesData(node, lsoLogger.getSequenceCountAccepted());
	}

	private void exportRejectedSequences() {
		final JSONObject node = getJSONObject("rejectedSequences");
		writeSequencesData(node, lsoLogger.getSequenceCountRejected());
	}

	private void exportIterationsList(final List<Pair<Integer, Long>> iterations, final JSONArray array) {
		for (final Pair<Integer, Long> i : iterations) {
			final JSONObject obj = new JSONObject();
			obj.put("iteration", i.getFirst());
			obj.put("fitness", i.getSecond());
			array.add(obj);
		}
	}

	private void exportIterationsList(final List<Pair<Integer, Long>> iterations, final JSONObject parent) {
		JSONArray iterationsJson = getJSONArray(parent, "iteration");
		JSONArray fitnessJson = getJSONArray(parent, "fitness");
		for (final Pair<Integer, Long> i : iterations) {
			iterationsJson.add(i.getFirst());
			fitnessJson.add(i.getSecond());
		}
	}

	private void exportAcceptedFitnesses() {
		if (lsoLogger.getLoggingParameters().doLogAcceptedFitnesses) {
			final JSONObject object = getJSONObject("acceptedFitnesses");
			exportIterationsList(lsoLogger.getAcceptedFitnesses(), object);
		}
	}

	private void exportRejectedFitnesses() {
		if (lsoLogger.getLoggingParameters().doLogRejectedFitnesses) {
			final JSONObject object = getJSONObject("rejectedFitnesses");
			exportIterationsList(lsoLogger.getRejectedFitnesses(), object);
		}
	}
	
	private void exportHeapUsageData() {
		writeHeapUsageData(getJSONObject("heapUsage"), lsoLogger.getHeapUsageMap());
	}

	private void writeSequencesData(final JSONObject node, final List<Integer> frequencies) {
		final JSONArray array = new JSONArray();
		for (final int i : frequencies) {
			array.add(i);
		}
		node.put("frequencies", array);
	}

	private void writeSequencesDataFitnessStatistics(final JSONObject node, final Map<Integer, Double[]> frequencies) {

		for (int i : frequencies.keySet()) {
			final JSONArray row = new JSONArray();
			for (final int di = 0; i < frequencies.get(i).length; i++) {
				row.add(frequencies.get(i)[di]);
			}
			node.put("i", row);
		}

	}

	private void writeMoveData(final JSONObject node) {
		{
			final JSONObject m = new JSONObject();

			for (final String move : lsoLogger.getMovesList()) {
				m.put(move, lsoLogger.getMoveCount(move));
			}
			node.put("moves", m);
		}

		{
			final JSONObject m = new JSONObject();

			// NullMoves
			for (final String move : lsoLogger.getNullMovesList()) {
				final JSONObject m2 = new JSONObject();

				m.put(move, m2);

				for (final String failure : lsoLogger.getNullMoveSubKeys(move)) {
					m2.put(failure, lsoLogger.getSpecificNullMoveCount(move, failure));
				}
			}
			node.put("nullMoves", m);

		}
		{

			final JSONObject m = new JSONObject();

			// SuccessfulMoves
			for (final String move : lsoLogger.getSuccessfulMovesList()) {
				m.put(move, lsoLogger.getSuccessfulMoveCount(move));

			}
			node.put("successfulMoves", m);

		}
		// FailedToValidate
		{

			final JSONObject m = new JSONObject();
			for (final String move : lsoLogger.getFailedToValidateMovesList()) {
				m.put(move, lsoLogger.getFailedToValidateMoveCount(move));

			}
			node.put("failedToValidate", m);

		}

		{
			final JSONObject m = new JSONObject();
			// FailedConstraints
			for (final String move : lsoLogger.getFailedConstraints()) {

				final JSONObject m2 = new JSONObject();
				m.put(move, m2);

				for (final String failure : lsoLogger.getFailedConstraintsMoves(move)) {
					m2.put(failure, lsoLogger.getFailedConstraintsMovesIndividualCount(move, failure));
				}
			}

			node.put("failedConstraints", m);

		}
		{
			final JSONObject m = new JSONObject();
			// FailedEvaluatedConstraints
			for (final String move : lsoLogger.getFailedEvaluatedConstraints()) {

				final JSONObject m2 = new JSONObject();
				m.put(move, m2);

				for (final String failure : lsoLogger.getFailedEvaluatedConstraintsMoves(move)) {
					m2.put(failure, lsoLogger.getFailedEvaluatedConstraintsMovesIndividualCount(move, failure));
				}
			}

			node.put("failedEvaluatedConstraints", m);

		}
	}
}
