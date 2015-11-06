package com.mmxlabs.models.lng.transformer.chain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.chain.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
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

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final List<IChainRunner> chains;

	@NonNull
	private final IMultiStateResult initialState;

	private final int numThreads;

	public MultiChainRunner(@NonNull final LNGDataTransformer dataTransformer, @NonNull final List<IChainRunner> chains, final int numThreads) {
		this.dataTransformer = dataTransformer;
		this.chains = chains;
		this.numThreads = numThreads;
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
		final ExecutorService pool = Executors.newFixedThreadPool(numThreads);
		try {
			final List<Future<IMultiStateResult>> results = new ArrayList<>(numThreads);

			for (final IChainRunner chain : chains) {
				results.add(pool.submit(new MyRunnable(chain, monitor, 1000)));
			}

			// Wait for all results
			for (final Future<IMultiStateResult> f : results) {
				// TODO: If this throws an exception, we will skip blocking other results
				// TODO: Combine results?
				f.get();
			}
		} catch (final Throwable e) {
			// TODO: Log
			e.printStackTrace();
		} finally {
			monitor.done();
			// Ensure we keep blocking in case of exception above...
			pool.shutdown();
		}

		// TODO: Return a combined state?
		return initialState;
	}

	@NonNull
	private static IMultiStateResult createInitialResult(final @NonNull ISequences sequences) {

		final Pair<ISequences, IAnnotatedSolution> p = new Pair<>(sequences, null);
		final List<Pair<ISequences, IAnnotatedSolution>> l = Lists.newArrayList(p);
		return new MultiStateResult(p, l);
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