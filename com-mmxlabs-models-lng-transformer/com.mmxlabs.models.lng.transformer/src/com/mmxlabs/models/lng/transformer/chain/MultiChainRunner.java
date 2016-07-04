/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.chain.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * A composite {@link IChainRunner} allowing multiple {@link IChainRunner} instances to be run in parallel from a shared {@link LNGDataTransformer}.
 * 
 * TODO: This current implementation assumes each chain in configured to store it's output in a separate child instance and the original scenario is untouched.
 * 
 * @author Simon Goodall
 *
 */
public class MultiChainRunner implements IChainRunner {

	private static final Logger LOG = LoggerFactory.getLogger(MultiChainRunner.class);

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final List<IChainRunner> chains;

	@NonNull
	private final IMultiStateResult initialState;

	@NonNull
	private final ExecutorService executorService;

	public MultiChainRunner(@NonNull final LNGDataTransformer dataTransformer, @NonNull final List<IChainRunner> chains, @NonNull final ExecutorService executorService) {
		this.dataTransformer = dataTransformer;
		this.chains = chains;
		this.executorService = executorService;
		// Return the data transformer state, - without an IAnnotatedSolution.
		initialState = createInitialResult(dataTransformer.getInitialSequences());
	}

	@NonNull
	public LNGDataTransformer getDataTransformer() {
		return dataTransformer;
	}

	@NonNull
	public IMultiStateResult getInitialState() {
		return initialState;

	}

	@NonNull
	public IMultiStateResult run(@NonNull final IProgressMonitor monitor) {
		monitor.beginTask("Execute chains", 1000 * chains.size());
		try {
			final List<Future<IMultiStateResult>> results = new ArrayList<>(chains.size());

			for (final IChainRunner chain : chains) {
				results.add(executorService.submit(new MyRunnable(chain, monitor, 1000)));
			}

			// Wait for all results
			for (final Future<IMultiStateResult> f : results) {
				// TODO: If this throws an exception, we will skip blocking other results
				// TODO: Combine results?

				// Put in try/catch to avoid breaking out of run and loosing track of other threads.
				try {
					f.get();
				} catch (final Exception e) {
					// TODO: throw exception at the end of execution.
					LOG.error(e.getMessage(), e);
				}
			}
		} catch (final Throwable e) {
			LOG.error(e.getMessage(), e);
		} finally {
			monitor.done();
		}

		// TODO: Return a combined state?
		return initialState;
	}

	@NonNull
	private static IMultiStateResult createInitialResult(final @NonNull ISequences sequences) {
		return new MultiStateResult(sequences, new HashMap<>());
	}

	static class MyRunnable implements Callable<IMultiStateResult> {
		@NonNull
		private final IChainRunner runner;

		@NonNull
		private final IProgressMonitor m;

		public MyRunnable(@NonNull final IChainRunner runner, @NonNull final IProgressMonitor monitor, final int ticks) {
			this.runner = runner;
			this.m = new SubProgressMonitor(monitor, ticks);
		}

		@Override
		public IMultiStateResult call() {
			return runner.run(m);
		}
	}
}