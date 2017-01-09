/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ScenarioOptimisationJobStorage {

	private PrintWriter writer;
	
	public ScenarioOptimisationJobStorage() {
		writer = getWriter();
	}
	
	private PrintWriter getWriter() {
		PrintWriter out = null;
		String fName = String.format("C:/Users/achurchill/Documents/work/F/period-work/10MillMay-End.txt");
		try {
			// clear file
			FileWriter fw = new FileWriter(fName, false);
			fw.close();
			out = new PrintWriter(new BufferedWriter(new FileWriter(fName, true)));
		} catch (IOException e) {
			System.out.println(e);
		}
		writeDate(out);
		return out;
	}
	
	public void write(int numberOfMovesTried, int numberOfMovesAccepted, int numberOfFailedMoves,
			int numberOfFailedEvaluations, int numberOfFailedNormalConstraints, int numberOfFailedToValidate,
			int numberOfNull, long bestFitness, long currentFitness,
			Map<String, Integer> moveMap,
			Map<String, Integer> nullMovesMap,
			Map<String, Integer> failedConstraintsMap,
			Map<String, Map<String, Integer>> failedConstraintsMovesMap) {
		writeData(writer, numberOfMovesTried, numberOfMovesAccepted, numberOfFailedMoves, numberOfFailedEvaluations, numberOfFailedNormalConstraints, numberOfFailedToValidate, numberOfNull, bestFitness, currentFitness, moveMap, nullMovesMap, failedConstraintsMap, failedConstraintsMovesMap);
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
		   //get current date time with Date()
		   Date date = new Date();
		   writer.println(dateFormat.format(date));
	}
	
	private void writeData(PrintWriter writer, int numberOfMovesTried, int numberOfMovesAccepted, int numberOfFailedMoves,
							int numberOfFailedEvaluations, int numberOfFailedNormalConstraints, int numberOfFailedToValidate,
							int numberOfNull, long bestFitness, long currentFitness,
							Map<String, Integer> moveMap,
							Map<String, Integer> nullMovesMap,
							Map<String, Integer> failedConstraintsMap,
							Map<String, Map<String, Integer>> failedConstraintsMovesMap
							) {
		writer.println("-----");
		writer.println("[" + numberOfMovesTried + "] numberOfMovesAccepted:" + numberOfMovesAccepted);
		writer.println("[" + numberOfMovesTried + "] numberOfFailedMoves:" + numberOfFailedMoves);
		writer.println("[" + numberOfMovesTried + "] numberOfFailedEvaluations:" + numberOfFailedEvaluations);
		writer.println("[" + numberOfMovesTried + "] numberOfFailedNormalConstraints:" + numberOfFailedNormalConstraints);
		writer.println("[" + numberOfMovesTried + "] numberOfFailedToValidate:" + numberOfFailedToValidate);
		writer.println("[" + numberOfMovesTried + "] numberOfNull:" + numberOfNull);
		writer.println("-----");
		writer.println("[" + numberOfMovesTried + "] bestFitness:" + bestFitness);
		writer.println("[" + numberOfMovesTried + "] currentFitness:" + currentFitness);
		writer.println("-----");
		for (String move : moveMap.keySet()) {
			writer.println("[" + numberOfMovesTried + "] " + move + ":" + moveMap.get(move));
		}
		writer.println("-----");
		for (String nullMove : nullMovesMap.keySet()) {
			writer.println("[" + numberOfMovesTried + "] " + nullMove + ":" + nullMovesMap.get(nullMove));
		}
		writer.println("-----");
		for (String failedConstraint : failedConstraintsMap.keySet()) {
			writer.println("[" + numberOfMovesTried + "] " + failedConstraint + ":" + failedConstraintsMap.get(failedConstraint));
			for (String failedMoves: failedConstraintsMovesMap.get(failedConstraint).keySet()) {
				writer.println(String.format("[%s] [%s] %s:%s", numberOfMovesTried, failedConstraint, failedMoves, failedConstraintsMovesMap.get(failedConstraint).get(failedMoves)));
			}
		}
	}
}
