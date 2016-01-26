/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless.exporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.base.Joiner;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.scheduler.optimiser.schedule.LatenessChecker;

public class LatenessExporter extends AbstractRunExporter {
	IAnnotatedSolution annotatedSolution;
	private File outputFile;
	private String[] latenessKeys = new String[] { LatenessChecker.GA_TOTAL_LATENESS_IN_HOURS, LatenessChecker.GA_TOTAL_PROMPT_LATENESS_LOW_IN_HOURS,
			LatenessChecker.GA_TOTAL_PROMPT_LATENESS_HIGH_IN_HOURS, LatenessChecker.GA_TOTAL_MIDTERM_LATENESS_LOW_IN_HOURS, LatenessChecker.GA_TOTAL_MIDTERM_LATENESS_HIGH_IN_HOURS,
			LatenessChecker.GA_TOTAL_BEYOND_LATENESS_LOW_IN_HOURS, LatenessChecker.GA_TOTAL_BEYOND_LATENESS_HIGH_IN_HOURS };

	private List<String> rows = new LinkedList<>();

	@Override
	protected void reset() {

		super.reset();
		rows.clear();
	};

	@Override
	public void begin(@NonNull IOptimiser optimiser, long initialFitness, @Nullable IAnnotatedSolution annotatedSolution) {
		this.annotatedSolution = annotatedSolution;
		rows.add(getLatenessColumns());
		rows.add(getLatenessRow(annotatedSolution));
	}

	@Override
	public void report(@NonNull IOptimiser optimiser, int iteration, long currentFitness, long bestFitness, @Nullable IAnnotatedSolution currentSolution, @Nullable IAnnotatedSolution bestSolution) {
		// TODO Auto-generated method stub

	}

	@Override
	public void done(@NonNull IOptimiser optimiser, long bestFitness, @Nullable IAnnotatedSolution annotatedSolution) {
		this.annotatedSolution = annotatedSolution;
	}

	@Override
	public void exportData() {
		rows.add(getLatenessRow(annotatedSolution));
		writeDataFromAnnotatedSolution(rows);
	}

	private void writeDataFromAnnotatedSolution(List<String> rows) {
		if (outputFile == null) {
			return;
		}
		PrintWriter writer = null;

		try {

			writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile, false)));
			for (String r : rows) {
				writer.println(r);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}

	}

	private String getLatenessColumns() {
		Joiner joiner = Joiner.on(",");
		return joiner.join(latenessKeys);
	}

	private String getLatenessRow(IAnnotatedSolution annotatedSolution) {
		Joiner joiner = Joiner.on(",");
		String[] latenesses = new String[latenessKeys.length];
		int idx = -1;
		for (String key : latenessKeys) {
			Integer lateness = annotatedSolution.getGeneralAnnotation(key, Integer.class);
			latenesses[++idx] = "" + (lateness == null ? 0 : lateness);
		}
		return joiner.join(latenesses);
	}

}
