/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain;

import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;

/**
 * The default implementation of {@link IChainRunner}
 * 
 * @author Simon Goodall
 *
 */
public class ChainRunner implements IChainRunner {

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final List<IChainLink> chain;

	private final int totalTicks;

	@NonNull
	private IMultiStateResult initialState;

	@NonNull
	private SequencesContainer initialSequencesContainer;

	public ChainRunner(@NonNull final LNGDataTransformer dataTransformer, @NonNull List<IChainLink> chain) {
		this(dataTransformer, chain, dataTransformer.getInitialResult());
	}

	public ChainRunner(@NonNull final LNGDataTransformer dataTransformer, @NonNull List<IChainLink> chain, IMultiStateResult solutionBuilderSequences) {
		this.dataTransformer = dataTransformer;
		this.chain = chain;
		int ticks = 0;
		for (final IChainLink link : chain) {
			ticks += link.getProgressTicks();
		}
		this.totalTicks = ticks;

		initialSequencesContainer = new SequencesContainer(solutionBuilderSequences.getBestSolution());
		this.initialState = solutionBuilderSequences;
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
		IMultiStateResult r = initialState;
		monitor.beginTask("Execute chain", totalTicks);
		try {
			// boolean firstLink = true;
			for (final IChainLink link : chain) {
				// if (!firstLink) {
				// link.init();
				// }
				r = link.run(initialSequencesContainer, r, new SubProgressMonitor(monitor, link.getProgressTicks()));
				// firstLink = false;
				if (monitor.isCanceled()) {
					throw new OperationCanceledException();
				}
			}
		} finally {
			monitor.done();
		}
		return r;
	}

	@NonNull
	private static IMultiStateResult createInitialResult(final @NonNull ISequences sequences) {
		return new MultiStateResult(sequences, new HashMap<>());
	}
}