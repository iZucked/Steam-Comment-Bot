/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.logging.impl.EvaluationNumberKey;

public class LSOLoggingExporter {

	private final JSONObject root;
	private final String phase;

	private final LSOLogger lsoLogger;

	public LSOLoggingExporter(JSONArray nodes, final String phase, final LSOLogger lsoLogger) {
		this.phase = phase;
		this.lsoLogger = lsoLogger;
		root = new JSONObject();
		nodes.add(root);
		root.put("name", phase);
	}

	private JSONObject getWriter(final String filename) {
		return (JSONObject) root.computeIfAbsent(filename, k -> new JSONObject());
		// PrintWriter out = null;
		//// try {
		//// // clear file
		//// final FileWriter fw = new FileWriter(filename, false);
		//// fw.close();
		//// out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
		//// } catch (final IOException e) {
		//// System.out.println(e);
		//// }
		// return out;
	}

	public void close() {
		writeDate(root);
		// close(writer);
	}

	// private void close(final PrintWriter writer) {
	//// writer.close();
	// }

	private void writeDate(final JSONObject node) {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// // get current date time with Date()
		final Date date = new Date();
		// writer.println();
		node.put("date", dateFormat.format(date));
	}

	private void writeEndData() {
		// final Path newPath = getPath("results");
		JSONObject node = getWriter("results");
		writeEndData(node);
	}

	private void writeEndData(final JSONObject node) {
		final Map<String, Long> endResults = lsoLogger.getEndProgressResults();
		for (final String key : endResults.keySet()) {
			node.put(key, endResults.get(key));
			// writeLineForFinalData(node, key, endResults.get(key));
		}
		// writer.close();
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
		JSONObject node = getWriter(key);
		for (final EvaluationNumberKey evaluationNumberKey : evaluations) {
			node.put(evaluationNumberKey.getNumber(), lsoLogger.getProgressValue(evaluationNumberKey, key));
		}
	}

	private void exportFitnessAnnotations() {
		writeFitnessAnnotations(lsoLogger.getFitnessAnnotationLogger());
	}

	private void writeFitnessAnnotations(FitnessAnnotationLogger fitnessAnnotationLogger) {
		JSONObject node = getWriter("fitnessAnnotations");
		fitnessAnnotationLogger.exportData(node);
	}

	private void exportGeneralAnnotations() {
		writeGeneralAnnotations(lsoLogger.getGeneralAnnotationLogger());
	}

	private void writeGeneralAnnotations(GeneralAnnotationLogger generalAnnotationLogger) {
		JSONObject node = getWriter("generalAnnotations");
		generalAnnotationLogger.exportData(node);
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
	}

	public void exportEndData() {
		writeEndData();
	}

	public void exportMovesData() {
		JSONObject node = getWriter("moves");
		writeMoveData(node);
	}

	private void exportSequencesData() {
		JSONObject node = getWriter("sequences");
		writeSequencesData(node, lsoLogger.getSequenceFrequencyCounts());
	}

	private void exportConstraintFailedSequences() {
		JSONObject node = getWriter("constraintFailedSequences");
		writeSequencesData(node, lsoLogger.getSequenceCountFailedConstraint());
	}

	private void exportAcceptedSequences() {
		JSONObject node = getWriter("acceptedSequences");
		writeSequencesData(node, lsoLogger.getSequenceCountAccepted());
	}

	private void exportRejectedSequences() {
		JSONObject node = getWriter("rejectedSequences");
		writeSequencesData(node, lsoLogger.getSequenceCountRejected());
	}

	private void exportIterationsList(final List<Pair<Integer, Long>> iterations, final JSONArray array) {
		for (final Pair<Integer, Long> i : iterations) {
			JSONObject obj = new JSONObject();
			obj.put("iteration", i.getFirst());
			obj.put("fitness", i.getSecond());
			array.add(obj);
		}
	}

	private void exportAcceptedFitnesses() {
		JSONObject node = getWriter("acceptedFitnesses");
		JSONArray array = new JSONArray();
		exportIterationsList(lsoLogger.getAcceptedFitnesses(), array);
		node.put("entries", array);
	}

	private void exportRejectedFitnesses() {
		JSONObject node = getWriter("rejectedFitnesses");
		JSONArray array = new JSONArray();

		exportIterationsList(lsoLogger.getRejectedFitnesses(), array);
		node.put("entries", array);
	}

	// private void exportAcceptedFitnesses() {
	// Path newPath = getPath("acceptedSequencesFitnesses");
	// PrintWriter writer = getWriter(newPath.toString());
	// writeSequencesDataFitnessStatistics(writer, lsoLogger.getSequenceFitnessesAccepted());
	// writer.close();
	// }
	//
	// private void exportRejectedFitnesses() {
	// Path newPath = getPath("rejectedSequencesFitnesses" );
	// PrintWriter writer = getWriter(newPath.toString());
	// writeSequencesDataFitnessStatistics(writer, lsoLogger.getSequenceFitnessesRejected());
	// writer.close();
	// }

	private void writeSequencesData(final JSONObject node, final List<Integer> frequencies) {
		JSONArray array = new JSONArray();
		for (final int i : frequencies) {
			array.add(i);
		}
		node.put("frequencies", array);
	}

	private void writeSequencesDataFitnessStatistics(final JSONObject node, final Map<Integer, Double[]> frequencies) {

		for (int i : frequencies.keySet()) {
			JSONArray row = new JSONArray();
			for (final int di = 0; i < frequencies.get(i).length; i++) {
				row.add(frequencies.get(i)[di]);
			}
			node.put("i", row);
		}

	}

	// private void writeLineForFinalData(final JSONObject node, final String key, final Object value) {
	// writer.println(String.format("[%s] %s", key, value.toString()));
	// }

	private void writeMoveData(final JSONObject node) {
		{
			JSONObject m = new JSONObject();

			for (final String move : lsoLogger.getMovesList()) {
				m.put(move, lsoLogger.getMoveCount(move));
			}
			node.put("moves", m);
		}

		{
			JSONObject m = new JSONObject();

			// NullMoves
			for (final String move : lsoLogger.getNullMovesList()) {
				JSONObject m2 = new JSONObject();

				m.put(move, m2);

				for (final String failure : lsoLogger.getNullMoveSubKeys(move)) {
					m2.put(failure, lsoLogger.getSpecificNullMoveCount(move, failure));
				}
			}
			node.put("nullMoves", m);

		}
		{

			JSONObject m = new JSONObject();

			// SuccessfulMoves
			for (final String move : lsoLogger.getSuccessfulMovesList()) {
				m.put(move, lsoLogger.getSuccessfulMoveCount(move));

			}
			node.put("successfulMoves", m);

		}
		// FailedToValidate
		{

			JSONObject m = new JSONObject();
			for (final String move : lsoLogger.getFailedToValidateMovesList()) {
				m.put(move, lsoLogger.getFailedToValidateMoveCount(move));

			}
			node.put("failedToValidate", m);

		}

		{
			JSONObject m = new JSONObject();
			// FailedConstraints
			for (final String move : lsoLogger.getFailedConstraints()) {

				JSONObject m2 = new JSONObject();
				m.put(move, m2);

				for (final String failure : lsoLogger.getFailedConstraintsMoves(move)) {
					m2.put(failure, lsoLogger.getFailedConstraintsMovesIndividualCount(move, failure));
				}
			}

			node.put("failedConstraints", m);

		}
		{
			JSONObject m = new JSONObject();
			// FailedEvaluatedConstraints
			for (final String move : lsoLogger.getFailedEvaluatedConstraints()) {

				JSONObject m2 = new JSONObject();
				m.put(move, m2);

				for (final String failure : lsoLogger.getFailedEvaluatedConstraintsMoves(move)) {
					m2.put(failure, lsoLogger.getFailedEvaluatedConstraintsMovesIndividualCount(move, failure));
				}
			}

			node.put("failedEvaluatedConstraints", m);

		}
	}
}
