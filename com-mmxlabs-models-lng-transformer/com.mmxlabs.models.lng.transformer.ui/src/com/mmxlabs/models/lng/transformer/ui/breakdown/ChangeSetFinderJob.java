/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Callable;

import com.mmxlabs.optimiser.core.impl.Sequences;

/**
 * A class that could be passed into an ExecutorService to attempt to find change sets from the current state.
 * 
 * @author Simon Goodall
 *
 */
public final class ChangeSetFinderJob implements Callable<Collection<JobState>> {
	private final JobState state;
	private final SimilarityState similarityState;
	private final BreakdownOptimiserMover optimiser;
	private final JobStore jobStore;

	public ChangeSetFinderJob(final BreakdownOptimiserMover optimiser, final JobState state, final SimilarityState similarityState, final JobStore jobStore) {
		this.optimiser = optimiser;
		this.state = state;
		this.similarityState = similarityState;
		this.jobStore = jobStore;
	}

	@Override
	public Collection<JobState> call() {
		try {
			// Perform a recursive search to find the next change set.

			final int localDepth = state.mode == JobStateMode.LIMITED ? 2 : BreakdownOptimiserMover.DEPTH_START;
			return optimiser.search(new Sequences(state.rawSequences), similarityState, new LinkedList<Change>(state.changesAsList), new LinkedList<ChangeSet>(state.changeSetsAsList), localDepth,
					BreakdownOptimiserMover.MOVE_TYPE_NONE, state.metric, jobStore, null, state.getDifferencesList(), new BreakdownSearchStatistics());
		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}

	}
}