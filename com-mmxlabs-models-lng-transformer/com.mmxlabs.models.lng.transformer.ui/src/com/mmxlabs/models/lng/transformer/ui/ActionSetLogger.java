/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.mmxlabs.models.lng.transformer.ui.breakdown.Change;
import com.mmxlabs.models.lng.transformer.ui.breakdown.ChangeSet;
import com.mmxlabs.models.lng.transformer.ui.breakdown.Difference;
import com.mmxlabs.models.lng.transformer.ui.breakdown.JobState;
import com.mmxlabs.models.lng.transformer.ui.breakdown.MetricType;
import com.mmxlabs.models.lng.transformer.ui.breakdown.chain.StochasticActionSetUtils;
import com.mmxlabs.optimiser.common.logging.ILoggingDataStore;

public class ActionSetLogger implements ILoggingDataStore {

	public class ActionSetResult {
		JobState root;
		JobState bestResult;
		int populationIndex;
		int noLeafs;
		int pnlEvaluations;
		int constraintEvaluations;
		long runTime;
		List<Difference> changesRemaining;

		public ActionSetResult(JobState root, JobState bestResult, int noLeafs, int pnlEvaluations,
				int constraintEvaluations, List<Difference> changesRemaining, long runTime) {
			this.root = root;
			this.bestResult = bestResult;
			this.noLeafs = noLeafs;
			this.pnlEvaluations = pnlEvaluations;
			this.constraintEvaluations = constraintEvaluations;
			this.changesRemaining = changesRemaining;
			this.runTime = runTime;
		}

		public String jobStatePathResultRow() {
			String[] row = new String[] { "" + this.noLeafs, "" + this.pnlEvaluations, "" + this.constraintEvaluations,
					"" + StochasticActionSetUtils.getTotalPNLPerChange(this.bestResult.changeSetsAsList),
					"" + this.changesRemaining.size(), };
			return Arrays.stream(row).collect(Collectors.joining(","));
		}

		public String getChangesRemainingString() {
			return changesRemaining.stream() //
					.map(s -> s.toString()) //
					.collect(Collectors.joining("\n"));
		}
	}

	List<List<JobState>> initialPopulations = new LinkedList<>();
	List<ActionSetResult> rootResults = new LinkedList<>();
	List<JobState> leafs;
	List<JobState> limiteds;
	long startRunTime = 0;
	long endRunTime = 0;

	public void logRootActionSet(JobState root, JobState bestResult, int noLeafs, int pnlEvaluations,
			int constraintEvaluations, List<Difference> changesRemaining, long runTime) {
		rootResults.add(new ActionSetResult(root, bestResult, noLeafs, pnlEvaluations, constraintEvaluations,
				changesRemaining, runTime));
	}

	public void logInitialPopulation(List<JobState> jobStates) {
		initialPopulations.add(jobStates);
	}

	public void logLeafsFound(List<JobState> leafs) {
		this.leafs = leafs;
	}
	
	public void logLimiteds(List<JobState> limiteds) {
		this.limiteds = limiteds;
	}

	public void begin(long time) {
		startRunTime = time;
	}

	public void finish(long time) {
		endRunTime = time;
	}

	public void export(String filePath, String prefix) {
		exportActionSetResults(filePath, prefix);
		exportLeafs(filePath, prefix);
		exportInitialPopulation(filePath, prefix);
		exportAggregate(filePath, prefix);
		exportLimiteds(filePath, prefix);
	}

	private void exportAggregate(String filePath, String prefix) {
		Path path = Paths.get(filePath, String.format("%s.aggregate.txt", prefix));
		ActionSetResult acr = rootResults.size() > 0 ? rootResults.get(rootResults.size() - 1) : null;
		List<String> text = new LinkedList<>();
		text.add(String.format("%s,%s", "totalRunTime", endRunTime - startRunTime));
		text.add(String.format("%s,%s", "noLeafs", leafs.size()));
		if (acr != null)
			text.add(String.format("%s,%s", "constraintEvaluations", acr.constraintEvaluations));
		if (acr != null)
			text.add(String.format("%s,%s", "pnlEvaluations", acr.pnlEvaluations));
		if (leafs.size() > 0) {
			text.add(String.format("%s,%s", "remainingChanges", 0));
		} else {
			text.add(String.format("%s,%s", "remainingChanges",
					Collections.min(rootResults, new Comparator<ActionSetResult>() {

						@Override
						public int compare(ActionSetResult o1, ActionSetResult o2) {
							return Integer.compare(o1.changesRemaining.size(), o1.changesRemaining.size());
						}
					}).changesRemaining.size()));
		}
		PrintWriter writer = getWriter(path);
		writer.write(text.stream().collect(Collectors.joining("\n")));
		writer.close();

	}

	private void exportInitialPopulation(String filePath, String prefix) {
		int popDex = 0;
		for (List<JobState> initialPopulation : initialPopulations) {
			int idx = 0;
			for (JobState js : initialPopulation) {
				{
					Path path = Paths.get(filePath, String.format("%s.pop.%s.member.%s.verbose.txt", prefix, popDex, idx));
					PrintWriter writer = getWriter(path);
					writer.write(jobStateStringVerbose(js));
					writer.close();
				}
				{
					Path path = Paths.get(filePath, String.format("%s.pop.%s.member.%s.compact.txt", prefix, popDex, idx));
					PrintWriter writer = getWriter(path);
					writer.write(jobStateStringCompact(js));
					writer.close();
				}
				idx++;
			}
			popDex++;
		}
	}

	private void exportActionSetResults(String filePath, String prefix) {
		{
			Path path = Paths.get(filePath, String.format("%s.root.txt", prefix));
			PrintWriter writer = getWriter(path);
			for (ActionSetResult acr : rootResults) {
				writer.write(acr.jobStatePathResultRow());
				writer.write("\n");
			}
			writer.close();
		}
		{
			int idx = 0;
			for (ActionSetResult acr : rootResults) {
				Path path = Paths.get(filePath, String.format("%s.differencesLeft.%s.txt", prefix, idx));
				PrintWriter writer = getWriter(path);
				writer.write(acr.getChangesRemainingString());
				writer.close();
				idx++;
			}
		}
		{
			int idx = 0;
			for (ActionSetResult acr : rootResults) {
				{
					Path path = Paths.get(filePath, String.format("%s.acr.root.%s.verbose.txt", prefix, idx));
					PrintWriter writer = getWriter(path);
					writer.write(jobStateStringVerbose(acr.root));
					writer.close();
				}
				{
					Path path = Paths.get(filePath, String.format("%s.acr.root.%s.compact.txt", prefix, idx));
					PrintWriter writer = getWriter(path);
					writer.write(jobStateStringCompact(acr.root));
					writer.close();
				}
				{
					Path path = Paths.get(filePath, String.format("%s.acr.best.%s.verbose.txt", prefix, idx));
					PrintWriter writer = getWriter(path);
					writer.write(jobStateStringVerbose(acr.bestResult));
					writer.close();
				}
				{
					Path path = Paths.get(filePath, String.format("%s.acr.best.%s.compact.txt", prefix, idx));
					PrintWriter writer = getWriter(path);
					writer.write(jobStateStringCompact(acr.bestResult));
					writer.close();
				}

				idx++;
			}
		}
	}
	
	private void exportLeafs(String filePath, String prefix) {
		int idx = 0;
		for (JobState js : leafs) {
			{
				Path path = Paths.get(filePath, String.format("%s.leaf.%s.verbose.txt", prefix, idx));
				PrintWriter writer = getWriter(path);
				writer.write(jobStateStringVerbose(js));
				writer.close();
			}
			{
				Path path = Paths.get(filePath, String.format("%s.leaf.%s.compact.txt", prefix, idx));
				PrintWriter writer = getWriter(path);
				writer.write(jobStateStringCompact(js));
				writer.close();
			}
			idx++;
		}
	}

	private void exportLimiteds(String filePath, String prefix) {
		int idx = 0;
		List<String> diffs = new LinkedList<>();
		for (JobState js : limiteds) {
			{
				Path path = Paths.get(filePath, String.format("%s.limiteds.%s.verbose.txt", prefix, idx));
				PrintWriter writer = getWriter(path);
				writer.write(jobStateStringVerbose(js));
				writer.close();
			}
			{
				Path path = Paths.get(filePath, String.format("%s.limiteds.%s.compact.txt", prefix, idx));
				PrintWriter writer = getWriter(path);
				writer.write(jobStateStringCompact(js));
				writer.close();
			}
			diffs.add(String.format("%s,%s", idx, js.getDifferencesList().size()));
			idx++;
		}
		Path path = Paths.get(filePath, String.format("%s.limiteds.differences.txt", prefix, idx));
		PrintWriter writer = getWriter(path);
		writer.write(diffs.stream().collect(Collectors.joining("\n")));
		writer.close();
	}

	public String jobStateStringVerbose(JobState jobState) {
		List<String> file = new LinkedList<String>();
		int changeSetNo = 0;
		for (ChangeSet cs : jobState.changeSetsAsList) {
			file.add(String.format(
					"ChangeSet: %s DeltaPnLToLast: %s DeltaPnLToBase: %s DeltaLatenessToLast: %s DeltaLatenessToBase: %s",
					changeSetNo++, cs.metricDelta[MetricType.PNL.ordinal()],
					cs.metricDeltaToBase[MetricType.PNL.ordinal()], cs.metricDelta[MetricType.LATENESS.ordinal()],
					cs.metricDeltaToBase[MetricType.LATENESS.ordinal()]));
			for (Change c : cs.changesList) {
				file.add(c.description.replace("\n", ""));
			}
		}
		return file.stream().collect(Collectors.joining("\n"));
	}

	public String jobStateStringCompact(JobState jobState) {
		List<String> file = new LinkedList<String>();
		for (ChangeSet cs : jobState.changeSetsAsList) {
			String[] row = new String[] { "" + cs.changesList.size(), "" + cs.metricDelta[MetricType.PNL.ordinal()],
					"" + cs.metricDeltaToBase[MetricType.PNL.ordinal()],
					"" + cs.metricDelta[MetricType.LATENESS.ordinal()],
					"" + cs.metricDeltaToBase[MetricType.LATENESS.ordinal()] };
			file.add(Arrays.stream(row).collect(Collectors.joining(",")));
		}
		return file.stream().collect(Collectors.joining("\n"));
	}

	private PrintWriter getWriter(Path path) {
		return getWriter(path.toString());
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

}
