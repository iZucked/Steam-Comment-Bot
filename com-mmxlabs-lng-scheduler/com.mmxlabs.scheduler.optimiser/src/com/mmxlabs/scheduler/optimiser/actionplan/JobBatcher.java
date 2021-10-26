/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.actionplan;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import com.google.inject.Injector;
import com.mmxlabs.common.concurrent.JobExecutor;

public class JobBatcher {
	List<JobState> states = new LinkedList<>();
	int index = 0; // index of next int
	private final int batchSize;
	private final JobExecutor jobExecutor;
	private final int depthStart;
	private final int depthEnd;

	public JobBatcher(final JobExecutor jobExecutor, final List<JobState> states, final int batchSize, final int depthStart, final int depthEnd) {
		this.states = states;
		this.batchSize = batchSize;
		this.jobExecutor = jobExecutor;
		this.depthStart = depthStart;
		this.depthEnd = depthEnd;
	}
	
	public List<Future<Collection<JobState>>> getNextFutures(final Injector injector, final SimilarityState similarityState, final JobStore jobStore, final IncrementingRandomSeed incrementingRandomSeed) {
		if (index < states.size()) {
			final List<Future<Collection<JobState>>> futures = new LinkedList<>();
			final int incrementedBatchSize = index + this.batchSize;
			for (int i = index; i < Math.min(incrementedBatchSize, states.size()); i++) {
				futures.add(jobExecutor.submit(new ChangeSetFinderJob(injector, states.get(i), similarityState, jobStore, incrementingRandomSeed.getSeed(), depthStart, depthEnd)));
				index++;
			}
			return futures;
		} else {
			return Collections.<Future<Collection<JobState>>>emptyList();
		}
	}
	
	public boolean hasNext() {
		if (index < states.size()) {
			return true;
		} else {
			return false;
		}
	}
}
