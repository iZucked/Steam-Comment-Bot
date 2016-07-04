/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.logging.impl.EvaluationNumberKey;

public class LSOLoggingExporter {

	private PrintWriter writer;
	private final String path;
	private final String foldername;
	private final String phase;

	private final LSOLogger lsoLogger;

	public LSOLoggingExporter(final String path, final String foldername, final String phase, final LSOLogger lsoLogger) {
		this.path = path;
		this.foldername = foldername;
		this.phase = phase;
		this.lsoLogger = lsoLogger;
		final String pathToFolder = Paths.get(path, phase).toString();
		final boolean success = (new File(pathToFolder)).mkdirs();
	}

	private PrintWriter getWriter(final String filename) {
		PrintWriter out = null;
		try {
			// clear file
			final FileWriter fw = new FileWriter(filename, false);
			fw.close();
			out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
		} catch (final IOException e) {
			System.out.println(e);
		}
		return out;
	}

	public void close() {
		writeDate(writer);
		close(writer);
	}

	private void close(final PrintWriter writer) {
		writer.close();
	}

	private void writeDate(final PrintWriter writer) {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// get current date time with Date()
		final Date date = new Date();
		writer.println(dateFormat.format(date));
	}

	private void writeEndData() {
		final Path newPath = getPath("results");
		final PrintWriter writer = getWriter(newPath.toString());
		writeEndData(writer);
	}

	private void writeEndData(final PrintWriter writer) {
		final Map<String, Long> endResults = lsoLogger.getEndProgressResults();
		for (final String key : endResults.keySet()) {
			writeLineForFinalData(writer, key, endResults.get(key));
		}
		writer.close();
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
		final Path newPath = getPath(key);
		final PrintWriter writer = getWriter(newPath.toString());
		for (final EvaluationNumberKey evaluationNumberKey : evaluations) {
			writer.println(String.format("%s,%s", evaluationNumberKey.getNumber(), lsoLogger.getProgressValue(evaluationNumberKey, key)));
		}
		writer.close();
	}
	
	private void exportFitnessAnnotations() {
		writeFitnessAnnotations(lsoLogger.getFitnessAnnotationLogger());
	}
	
	private void writeFitnessAnnotations(FitnessAnnotationLogger fitnessAnnotationLogger) {
		final Path newPath = getPath("fitnessAnnotations");
		final PrintWriter writer = getWriter(newPath.toString());
		fitnessAnnotationLogger.exportData(writer);
		writer.close();
	}

	private void exportGeneralAnnotations() {
		writeGeneralAnnotations(lsoLogger.getGeneralAnnotationLogger());
	}
	
	private void writeGeneralAnnotations(GeneralAnnotationLogger generalAnnotationLogger) {
		final Path newPath = getPath("generalAnnotations");
		final PrintWriter writer = getWriter(newPath.toString());
		generalAnnotationLogger.exportData(writer);
		writer.close();
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
		final Path newPath = getPath("moves");
		final PrintWriter writer = getWriter(newPath.toString());
		writeMoveData(writer);
		writer.close();
	}

	private void exportSequencesData() {
		final Path newPath = getPath("sequences");
		final PrintWriter writer = getWriter(newPath.toString());
		writeSequencesData(writer, lsoLogger.getSequenceFrequencyCounts());
		writer.close();
	}

	private void exportConstraintFailedSequences() {
		final Path newPath = getPath("constraintFailedSequences");
		final PrintWriter writer = getWriter(newPath.toString());
		writeSequencesData(writer, lsoLogger.getSequenceCountFailedConstraint());
		writer.close();
	}

	private void exportAcceptedSequences() {
		final Path newPath = getPath("acceptedSequences");
		final PrintWriter writer = getWriter(newPath.toString());
		writeSequencesData(writer, lsoLogger.getSequenceCountAccepted());
		writer.close();
	}

	private void exportRejectedSequences() {
		final Path newPath = getPath("rejectedSequences");
		final PrintWriter writer = getWriter(newPath.toString());
		writeSequencesData(writer, lsoLogger.getSequenceCountRejected());
		writer.close();
	}

	private void exportIterationsList(final List<Pair<Integer, Long>> iterations, final PrintWriter writer) {
		for (final Pair<Integer, Long> i : iterations) {
			writer.println(String.format("%s,%s", i.getFirst(), i.getSecond()));
		}
	}

	private void exportAcceptedFitnesses() {
		final Path newPath = getPath("acceptedFitnesses");
		final PrintWriter writer = getWriter(newPath.toString());
		exportIterationsList(lsoLogger.getAcceptedFitnesses(), writer);
		writer.close();
	}

	private void exportRejectedFitnesses() {
		final Path newPath = getPath("rejectedFitnesses");
		final PrintWriter writer = getWriter(newPath.toString());
		exportIterationsList(lsoLogger.getRejectedFitnesses(), writer);
		writer.close();
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

	private void writeSequencesData(final PrintWriter writer, final List<Integer> frequencies) {
		for (final int i : frequencies) {
			writer.println(i);
		}
	}

	private void writeSequencesDataFitnessStatistics(final PrintWriter writer, final Map<Integer, Double[]> frequencies) {
		for (int i : frequencies.keySet()) {
			String row = "" + i + ",";
			for (final int di = 0; i < frequencies.get(i).length; i++) {
				row += frequencies.get(i)[di];
				if (di < frequencies.get(i).length - 1) {
					row += ",";
				}
			}
			writer.println(row);
		}
	}

	private void writeLineForFinalData(final PrintWriter writer, final String key, final Object value) {
		writer.println(String.format("[%s] %s", key, value.toString()));
	}

	private void writeMoveData(final PrintWriter writer) {
		writer.println("-----[moves]-----");
		for (final String move : lsoLogger.getMovesList()) {
			writer.println(String.format("[%s] %s", move, lsoLogger.getMoveCount(move)));
		}
		writer.println("-----[nullMoves]-----");
		for (final String move : lsoLogger.getNullMovesList()) {
			writer.println(String.format("[%s] %s", move, lsoLogger.getNullMoveCount(move)));
		}
		writer.println("-----[successfulMoves]-----");
		for (final String move : lsoLogger.getSuccessfulMovesList()) {
			writer.println(String.format("[%s] %s", move, lsoLogger.getSuccessfulMoveCount(move)));
		}
		writer.println("-----[failedToValidate]-----");
		for (final String move : lsoLogger.getFailedToValidateMovesList()) {
			writer.println(String.format("[%s] %s", move, lsoLogger.getFailedToValidateMoveCount(move)));
		}
		writer.println("-----[failedConstraints]-----");
		for (final String failedConstraint : lsoLogger.getFailedConstraints()) {
			writer.println(String.format("[%s] %s", failedConstraint, lsoLogger.getFailedConstraintsMovesTotalCount(failedConstraint)));
			for (final String failedMove : lsoLogger.getFailedConstraintsMoves(failedConstraint)) {
				writer.println(String.format("\t[%s] %s", failedMove, lsoLogger.getFailedConstraintsMovesIndividualCount(failedConstraint, failedMove)));
			}
		}
	}

	private Path getPath(final String fileType) {
		return Paths.get(path, phase, String.format("%s.%s.txt", foldername, fileType));
	}

}
