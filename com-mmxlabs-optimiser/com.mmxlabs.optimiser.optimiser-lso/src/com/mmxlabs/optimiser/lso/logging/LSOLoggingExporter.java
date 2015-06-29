/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.common.logging.impl.EvaluationNumberKey;
import com.mmxlabs.optimiser.lso.LSOLoggingConstants;

public class LSOLoggingExporter {

	private PrintWriter writer;
	private String path;
	private String foldername;

	@Inject
	@Named(LSOLoggingConstants.LSO_LOGGER)
	private LSOLogger lsoLogger;

	public LSOLoggingExporter(String path, String foldername) {
		this.path = path;
		this.foldername = foldername;
		String pathToFolder = Paths.get(path, this.foldername).toString();
		boolean success = (new File(pathToFolder)).mkdirs();
	}

	private PrintWriter getWriter(String filename) {
		PrintWriter out = null;
		try {
			// clear file
			FileWriter fw = new FileWriter(filename, false);
			fw.close();
			out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
		} catch (IOException e) {
			System.out.println(e);
		}
		return out;
	}

	public void close() {
		writeDate(writer);
		close(writer);
	}

	private void close(PrintWriter writer) {
		writer.close();
	}

	private void writeDate(PrintWriter writer) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// get current date time with Date()
		Date date = new Date();
		writer.println(dateFormat.format(date));
	}

	private void writeEndData() {
		Path newPath = Paths.get(path, foldername, "results.txt");
		PrintWriter writer = getWriter(newPath.toString());
		writeEndData(writer);
	}

	private void writeEndData(PrintWriter writer) {
		Map<String, Long> endResults = lsoLogger.getEndProgressResults();
		for (String key : endResults.keySet()) {
			writeLineForFinalData(writer, key, endResults.get(key));
		}
		writer.close();
	}

	private void writeDataOverCourseOfRun(String... keys) {
		List<EvaluationNumberKey> evaluations = lsoLogger.getProgressEvaluations();
		for (String key : keys) {
			if (lsoLogger.keyInProgressLog(key)) {
				writeDataTypeOverCourseOfRun(evaluations, key);
			}
		}
	}

	private void writeDataTypeOverCourseOfRun(List<EvaluationNumberKey> evaluations, String key) {
		Path newPath = Paths.get(path, foldername, key + ".txt");
		PrintWriter writer = getWriter(newPath.toString());
		for (EvaluationNumberKey evaluationNumberKey : evaluations) {
			writer.println(String.format("%s,%s", evaluationNumberKey.getNumber(), lsoLogger.getProgressValue(evaluationNumberKey, key)));
		}
		writer.close();
	}

	public void exportData(String... keys) {
		writeDataOverCourseOfRun(keys);
		exportEndData();
		exportMovesData();
		exportSequencesData();
		exportConstraintFailedSequences();
		exportAcceptedSequences();
		exportRejectedSequences();
//		exportAcceptedFitnesses();
//		exportRejectedFitnesses();
	}

	public void exportEndData() {
		writeEndData();
	}
	
	public void exportMovesData() {
		Path newPath = Paths.get(path, foldername, "moves" + ".txt");
		PrintWriter writer = getWriter(newPath.toString());
		writeMoveData(writer);
		writer.close();
	}

	private void exportSequencesData() {
		Path newPath = Paths.get(path, foldername, "sequences" + ".txt");
		PrintWriter writer = getWriter(newPath.toString());
		writeSequencesData(writer, lsoLogger.getSequenceFrequencyCounts());
		writer.close();
	}
	
	private void exportConstraintFailedSequences() {
		Path newPath = Paths.get(path, foldername, "constraintFailedSequences" + ".txt");
		PrintWriter writer = getWriter(newPath.toString());
		writeSequencesData(writer, lsoLogger.getSequenceCountFailedConstraint());
		writer.close();
	}

	private void exportAcceptedSequences() {
		Path newPath = Paths.get(path, foldername, "acceptedSequences" + ".txt");
		PrintWriter writer = getWriter(newPath.toString());
		writeSequencesData(writer, lsoLogger.getSequenceCountAccepted());
		writer.close();
	}

	private void exportRejectedSequences() {
		Path newPath = Paths.get(path, foldername, "rejectedSequences" + ".txt");
		PrintWriter writer = getWriter(newPath.toString());
		writeSequencesData(writer, lsoLogger.getSequenceCountRejected());
		writer.close();
	}

//	private void exportAcceptedFitnesses() {
//		Path newPath = Paths.get(path, foldername, "acceptedSequencesFitnesses" + ".txt");
//		PrintWriter writer = getWriter(newPath.toString());
//		writeSequencesDataFitnessStatistics(writer, lsoLogger.getSequenceFitnessesAccepted());
//		writer.close();
//	}
//	
//	private void exportRejectedFitnesses() {
//		Path newPath = Paths.get(path, foldername, "rejectedSequencesFitnesses" + ".txt");
//		PrintWriter writer = getWriter(newPath.toString());
//		writeSequencesDataFitnessStatistics(writer, lsoLogger.getSequenceFitnessesRejected());
//		writer.close();
//	}
		
	private void writeSequencesData(PrintWriter writer, List<Integer> frequencies) {
		for (int i : frequencies) {
			writer.println(i);
		}
	}
	
	private void writeSequencesDataFitnessStatistics(PrintWriter writer, Map<Integer, Double[]> frequencies) {
		for (int i : frequencies.keySet()) {
			String row = ""+i+",";
			for (int di = 0; i < frequencies.get(i).length; i++) {
				row += frequencies.get(i)[di];
				if (di < frequencies.get(i).length - 1) {
					row += ",";
				}
			}
			writer.println(row);
		}
	}
	
	private void writeLineForFinalData(PrintWriter writer, String key, Object value) {
		writer.println(String.format("[%s] %s", key, value.toString()));
	}
	
	private void writeMoveData(PrintWriter writer) {
		writer.println("-----[moves]-----");
		for (String move : lsoLogger.getMovesList()) {
			writer.println(String.format("[%s] %s", move, lsoLogger.getMoveCount(move)));
		}
		writer.println("-----[nullMoves]-----");
		for (String move : lsoLogger.getNullMovesList()) {
			writer.println(String.format("[%s] %s", move, lsoLogger.getNullMoveCount(move)));
		}
		writer.println("-----[successfulMoves]-----");
		for (String move : lsoLogger.getSuccessfulMovesList()) {
			writer.println(String.format("[%s] %s", move, lsoLogger.getSuccessfulMoveCount(move)));
		}
		writer.println("-----[failedToValidate]-----");
		for (String move : lsoLogger.getFailedToValidateMovesList()) {
			writer.println(String.format("[%s] %s", move, lsoLogger.getFailedToValidateMoveCount(move)));
		}
		writer.println("-----[failedConstraints]-----");
		for (String failedConstraint : lsoLogger.getFailedConstraints()) {
			writer.println(String.format("[%s] %s", failedConstraint, lsoLogger.getFailedConstraintsMovesTotalCount(failedConstraint)));
			for (String failedMove : lsoLogger.getFailedConstraintsMoves(failedConstraint)) {
				writer.println(String.format("\t[%s] %s", failedMove, lsoLogger.getFailedConstraintsMovesIndividualCount(failedConstraint, failedMove)));
			}
		}
	}

}
