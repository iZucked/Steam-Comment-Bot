/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.actionableset;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.common.components.impl.IncrementingRandomSeed;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IProgressReporter;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.scheduler.optimiser.actionableset.ActionableSetJobState.Status;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.FitnessCalculator;
import com.mmxlabs.scheduler.optimiser.moves.util.MetricType;

public class ActionableSetOptimiser {

	private final IncrementingRandomSeed incrementingRandomSeed = new IncrementingRandomSeed(0);

	@Inject
	private Injector injector;

	@Inject
	private FitnessCalculator fitnessCalculator;

	@Inject
	private ExecutorService executorService;

	@Inject
	private ISequencesManipulator manipulator;

	@Inject
	private EvaluationHelper evaluationHelper;

	public Collection<IMultiStateResult> optimise(@NonNull final ISequences inputRawSequences, final IProgressReporter progressReporter) throws Exception {

		long initialFitness;
		long[] initialMetrics;
		{
			final IModifiableSequences currentFullSequences = manipulator.createManipulatedSequences(inputRawSequences);

			@Nullable
			final IEvaluationState evaluationState = evaluationHelper.evaluateSequence(currentFullSequences);
			if (evaluationState == null) {
				throw new IllegalStateException();
			}

			// now evaluate
			initialFitness = fitnessCalculator.evaluateSequencesFitness(currentFullSequences, evaluationState, null);

			initialMetrics = evaluationHelper.evaluateState(inputRawSequences, currentFullSequences, null, null, null);
			if (initialMetrics == null) {
				throw new IllegalStateException();

			}
		}

		final int iterations = 10;
		final int numberOfJobs = 5_000;
		final int totalWork = iterations * numberOfJobs;

		progressReporter.begin(totalWork);

		List<ActionableSetJobState> jobStates = new LinkedList<ActionableSetJobState>();
		jobStates.add(new ActionableSetJobState(inputRawSequences, initialFitness, initialMetrics, Status.Pass, -1, "Initial Solution", null));
		for (int iteration = 0; iteration < iterations; ++iteration) {
			final List<ActionableSetJobState> currentIterationJobs = generateBatch(jobStates, new Random(iteration), numberOfJobs);
			final List<ActionableSetJobState> results = runJobs(currentIterationJobs, progressReporter);

			// TODO: Mix up generations!

			// Early termination!
			if (results.isEmpty()) {
				break;
			}
			// Clear result in prep for next iteration.
			// jobStates.clear();
			results.addAll(jobStates);
			// TODO: Implement equals on ActionSetJobState!
			jobStates = results.parallelStream().distinct().collect(Collectors.toList());
			Collections.sort(jobStates, (a, b) -> {
				final long fa = a.getFitness();
				final long fb = b.getFitness();
				if (fa > fb) {
					return 1;
				} else if (fa < fb) {
					return -1;
				} else {
					return 0;
				}
			});
			// Retain top 1_000 results;
			System.out.printf("Iteration %d: %d solutions\n", iteration, jobStates.size());
			jobStates = jobStates.stream().limit(3_000).collect(Collectors.toList());
		}
		jobStates = jobStates.stream().limit(10).collect(Collectors.toList());

		progressReporter.done();

		final List<ActionableSetJobState> jobStates2 = new LinkedList<ActionableSetJobState>();
		for (final ActionableSetJobState state : jobStates) {
			final List<ActionableSetJobState> states = new LinkedList<>();
			{
				ActionableSetJobState s = state;
				while (s != null) {
					states.add(0, s);
					s = s.getParent();
				}
			}
			int idx = 0;

			final int[] bestMetricsIdx = new int[MetricType.values().length];
			final long[] bestMetrics = new long[MetricType.values().length];

			while (idx < states.size()) {
				final ActionableSetJobState cs = states.get(idx);
				if (idx == 0) {
					for (final MetricType mt : MetricType.values()) {
						bestMetrics[mt.ordinal()] = cs.getMetrics()[mt.ordinal()];
					}
				} else {
					if (cs.getMetrics()[MetricType.PNL.ordinal()] > bestMetrics[MetricType.PNL.ordinal()]) {
						bestMetrics[MetricType.PNL.ordinal()] = cs.getMetrics()[MetricType.PNL.ordinal()];
						bestMetricsIdx[MetricType.PNL.ordinal()] = idx;
					}
					if (cs.getMetrics()[MetricType.LATENESS.ordinal()] < bestMetrics[MetricType.LATENESS.ordinal()]) {
						bestMetrics[MetricType.LATENESS.ordinal()] = cs.getMetrics()[MetricType.LATENESS.ordinal()];
						bestMetricsIdx[MetricType.LATENESS.ordinal()] = idx;
					}
					if (cs.getMetrics()[MetricType.COMPULSARY_SLOT.ordinal()] < bestMetrics[MetricType.COMPULSARY_SLOT.ordinal()]) {
						bestMetrics[MetricType.COMPULSARY_SLOT.ordinal()] = cs.getMetrics()[MetricType.COMPULSARY_SLOT.ordinal()];
						bestMetricsIdx[MetricType.COMPULSARY_SLOT.ordinal()] = idx;
					}
					if (cs.getMetrics()[MetricType.CAPACITY.ordinal()] < bestMetrics[MetricType.CAPACITY.ordinal()]) {
						bestMetrics[MetricType.CAPACITY.ordinal()] = cs.getMetrics()[MetricType.CAPACITY.ordinal()];
						bestMetricsIdx[MetricType.CAPACITY.ordinal()] = idx;
					}
				}
				idx++;
			}
			jobStates2.add(states.get(bestMetricsIdx[MetricType.PNL.ordinal()]));
		}

		Collections.sort(jobStates2, (a, b) -> metric(a, b));

		final List<IMultiStateResult> results = new LinkedList<IMultiStateResult>();
		for (final ActionableSetJobState state : jobStates2) {

			final List<NonNullPair<ISequences, Map<String, Object>>> solutions = new LinkedList<NonNullPair<ISequences, Map<String, Object>>>();
			ActionableSetJobState s = state;
			while (s != null) {
				solutions.add(0, new NonNullPair<ISequences, Map<String, Object>>(s.getRawSequences(), new HashMap<>()));
				s = s.getParent();
			}

			final MultiStateResult result = new MultiStateResult(solutions.get(0), solutions);
			results.add(result);
		}

		return results;

	}

	private List<ActionableSetJobState> generateBatch(final List<ActionableSetJobState> jobStates, final Random random, final int targetSize) {
		final List<ActionableSetJobState> nextOutput = new LinkedList<ActionableSetJobState>();
		for (int i = 0; i < targetSize; ++i) {
			nextOutput.add(RandomHelper.chooseElementFrom(random, jobStates));
		}
		return nextOutput;
	}

	@NonNull
	protected List<ActionableSetJobState> runJobs(final List<ActionableSetJobState> sortedJobStates, final IProgressReporter progressReporter) throws InterruptedException {
		// Create a batcher, which produces small batches of jobs that we can then spread among cores
		// but keep the progress log accurate and maintain repeatablility
		final ActionableSetJobBatcher jobBatcher = new ActionableSetJobBatcher(executorService, sortedJobStates, 100);

		final List<ActionableSetJobState> states = new LinkedList<>();
		List<Future<ActionableSetJobState>> futures;
		while (jobBatcher.hasNext()) {
			final long start = System.currentTimeMillis();
			futures = jobBatcher.getNextFutures(injector, incrementingRandomSeed, progressReporter);
			// Collect all results
			for (final Future<ActionableSetJobState> f : futures) {
				try {
					final ActionableSetJobState futureState = f.get();
					// Filter out bad states
					if (futureState != null && futureState.getStatus() == Status.Pass) {
						if (futureState.getFitness() != Long.MAX_VALUE) {
							// if (futureState.getFitness() < futureState.getParent().getFitness()) {
							states.add(futureState);
							// }
						}
					}

				} catch (final ExecutionException e) {
					throw new RuntimeException(e);
				}
			}
			final long end = System.currentTimeMillis();
			// System.out.println("batch:"+(end-start));
		}

		return states;
	}

	private int metric(final ActionableSetJobState o1, final ActionableSetJobState o2) {

		return Long.compare(getMetric(o1), getMetric(o2)) * -1;
	}

	static long[] getMetricsDelta(final long[] prev, final long[] current) {
		final long[] m = new long[current.length];
		if (prev == null) {
			return m;
		}
		for (final MetricType mt : MetricType.values()) {
			m[mt.ordinal()] = current[mt.ordinal()] - prev[mt.ordinal()];
		}
		return m;
	}

	public static long getMetric(final ActionableSetJobState state) {

		final List<ActionableSetJobState> states = new LinkedList<>();
		{
			ActionableSetJobState s = state;
			while (s != null) {
				states.add(0, s);
				s = s.getParent();
			}
		}
		long cumulativeChanges = 0;
		long value = 0;
		int idx = 0;

		final long[] lastMetrics = states.get(0).getMetrics();
		while (idx < states.size()) {
			final ActionableSetJobState cs = states.get(idx);
			cumulativeChanges += 1;// cs.changesList.size();

			final long[] delta = getMetricsDelta(lastMetrics, cs.getMetrics());

			if (delta[MetricType.PNL.ordinal()] > 0) {
				value += (delta[MetricType.PNL.ordinal()] / cumulativeChanges);
			} else {
				boolean foundPositive = false;
				long currentSum = delta[MetricType.PNL.ordinal()];
				long currentChanges = cumulativeChanges;
				// look for a net +ve

				final long[] cLastMetrics = cs.getMetrics();

				for (int i = idx + 1; i < states.size(); i++) {
					final long[] cDelta = getMetricsDelta(cLastMetrics, states.get(i).getMetrics());

					currentSum += cDelta[MetricType.PNL.ordinal()];
					currentChanges += 1;// states.get(i).changesList.size();
					if (currentSum > 0) {
						value += (currentSum / currentChanges);
						cumulativeChanges = currentChanges;
						idx = i;
						foundPositive = true;
						break;
					}
				}
				if (!foundPositive) {
					// no positive to roll into so just add value
					value += delta[MetricType.PNL.ordinal()];
				}
			}
			idx++;
		}
		return value;
	}

}