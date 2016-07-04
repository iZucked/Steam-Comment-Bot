/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.google.inject.Injector;

public class JobBatcher {
	List<JobState> states = new LinkedList<>();
	int index = 0; // index of next int
	private int batchSize;
	private ExecutorService executorService;
	private int depthStart;
	private int depthEnd;

	public JobBatcher(ExecutorService executorService, List<JobState> states, int batchSize, int depthStart, int depthEnd) {
		this.states = states;
		this.batchSize = batchSize;
		this.executorService = executorService;
		this.depthStart = depthStart;
		this.depthEnd = depthEnd;
	}
	
	public List<Future<Collection<JobState>>> getNextFutures(Injector injector, SimilarityState similarityState, JobStore jobStore, IncrementingRandomSeed incrementingRandomSeed) {
		if (index < states.size()) {
			List<Future<Collection<JobState>>> futures = new LinkedList<>();
			int incrementedBatchSize = index + this.batchSize;
			for (int i = index; i < Math.min(incrementedBatchSize, states.size()); i++) {
				futures.add(executorService.submit(new ChangeSetFinderJob(injector, states.get(i), similarityState, jobStore, incrementingRandomSeed.getSeed(), depthStart, depthEnd)));
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
