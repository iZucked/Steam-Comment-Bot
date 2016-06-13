/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Callable;

import com.google.inject.Injector;
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
	private final JobStore jobStore;
	private final long seed;
	private final Injector injector;
	private int depthStart;
	private int depthEnd;

	public ChangeSetFinderJob(final Injector injector, final JobState state, final SimilarityState similarityState, final JobStore jobStore, final long seed, int depthStart, int depthEnd) {
		this.injector = injector;
		this.state = state;
		this.similarityState = similarityState;
		this.jobStore = jobStore;
		this.seed = seed;
		this.depthStart = depthStart;
		this.depthEnd = depthEnd;
	}

	@Override
	public Collection<JobState> call() {
		try {
			final BagMover optimiser = injector.getInstance(BagMover.class);
			optimiser.setDepthRange(depthStart, depthEnd);
			try {
				final int localDepth = state.mode == JobStateMode.LIMITED ? 2 : BagMover.DEPTH_START;
				return optimiser.search(new Sequences(state.rawSequences), similarityState, new LinkedList<Change>(state.changesAsList), new LinkedList<ChangeSet>(state.changeSetsAsList), localDepth,
						BagMover.MOVE_TYPE_NONE, state.metric, jobStore, null, state.getDifferencesList(), new BreakdownSearchData(new BreakdownSearchStatistics(), new Random(seed)),
						null);
			} catch (final Throwable e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		} catch (final Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}