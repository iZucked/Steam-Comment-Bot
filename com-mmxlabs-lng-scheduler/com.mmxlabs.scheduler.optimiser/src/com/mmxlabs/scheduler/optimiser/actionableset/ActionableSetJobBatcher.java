/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.actionableset;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import com.google.inject.Injector;
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.optimiser.common.components.impl.IncrementingRandomSeed;
import com.mmxlabs.optimiser.core.IProgressReporter;

public class ActionableSetJobBatcher {
	private List<ActionableSetJobState> states = new LinkedList<>();
	private int index = 0; // index of next int
	private final int batchSize;
	private final JobExecutor jobExecutor;

	public ActionableSetJobBatcher(final JobExecutor jobExecutor, final List<ActionableSetJobState> states, final int batchSize) {
		this.states = states;
		this.batchSize = batchSize;
		this.jobExecutor = jobExecutor;
	}

	public List<Future<ActionableSetJobState>> getNextFutures(final Injector injector, final IncrementingRandomSeed incrementingRandomSeed, final IProgressReporter progressReporter) {
		if (index < states.size()) {
			final List<Future<ActionableSetJobState>> futures = new LinkedList<>();
			final int incrementedBatchSize = index + this.batchSize;
			for (int i = index; i < Math.min(incrementedBatchSize, states.size()); i++) {
				futures.add(jobExecutor.submit(new ActionableSetJob(injector, states.get(i), incrementingRandomSeed.getSeed(), progressReporter)));
				index++;
			}
			return futures;
		} else {
			return Collections.<Future<ActionableSetJobState>> emptyList();
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
