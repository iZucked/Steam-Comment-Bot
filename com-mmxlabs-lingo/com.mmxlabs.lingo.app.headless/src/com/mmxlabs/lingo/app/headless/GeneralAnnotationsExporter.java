package com.mmxlabs.lingo.app.headless;

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
import com.mmxlabs.lingo.app.headless.exporter.IRunExporter;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.schedule.LatenessChecker;

public class GeneralAnnotationsExporter implements IRunExporter {
	IAnnotatedSolution annotatedSolution;
	private File outputFile;
	private String[] latenessKeys = new String[] { LatenessChecker.GA_TOTAL_LATENESS_IN_HOURS, LatenessChecker.GA_TOTAL_PROMPT_LATENESS_LOW_IN_HOURS,
			LatenessChecker.GA_TOTAL_PROMPT_LATENESS_HIGH_IN_HOURS, LatenessChecker.GA_TOTAL_MIDTERM_LATENESS_LOW_IN_HOURS, LatenessChecker.GA_TOTAL_MIDTERM_LATENESS_HIGH_IN_HOURS,
			LatenessChecker.GA_TOTAL_BEYOND_LATENESS_LOW_IN_HOURS, LatenessChecker.GA_TOTAL_BEYOND_LATENESS_HIGH_IN_HOURS,
			SchedulerConstants.AI_similarityDifferences};

	private List<String> rows = new LinkedList<>();
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
	public void setOutputFile(File output) {
		this.outputFile = output;

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
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}

	}

	@Override
	public void setScenarioRunner(LNGScenarioRunner scenarioRunner) {
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
			latenesses[++idx] = ""+(lateness == null ? 0 : lateness);
		}
		return joiner.join(latenesses);
	}

}
