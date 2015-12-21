package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

public class JobBatcher {
	List<JobState> states = new LinkedList<>();
	int index = 0; // index of next int
	private int batchSize;
	
	public JobBatcher(List<JobState> states, int batchSize) {
		this.states = states;
		this.batchSize = batchSize;
	}
	
	public List<Future<Collection<JobState>>> getNextFutures(BagMover bagMover, SimilarityState similarityState, JobStore jobStore, IncrementingRandomSeed incrementingRandomSeed) {
		if (index < states.size()) {
			List<Future<Collection<JobState>>> futures = new LinkedList<>();
			for (int i = index; i < Math.min(i + this.batchSize, states.size()); i++) {
				futures.add(new MyFuture(new ChangeSetFinderJob(bagMover, states.get(i), similarityState, jobStore, incrementingRandomSeed.getSeed())));
				index++;
			}
			return futures;
		} else {
			return Collections.<Future<Collection<JobState>>>emptyList();
		}
	}
}
