/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;

/**
 * Simple unit class to reduce multiple solutions to a single one based on best
 * fitness
 * 
 * @author Simon Goodall
 *
 */
public class LNGReduceToBestSolutionUnit {

	@NonNull
	public static IChainLink chain(@NonNull final ChainBuilder chainBuilder, @NonNull final String stage, final int progressTicks) {
		final IChainLink link = new IChainLink() {

			@Override
			public IMultiStateResult run(final LNGDataTransformer dataTransformer, final SequencesContainer initialSequences, final IMultiStateResult inputState, final IProgressMonitor monitor) {

				dataTransformer.getLifecyleManager().startPhase(stage, dataTransformer.getHints());

				final IRunnerHook runnerHook = dataTransformer.getRunnerHook();
				if (runnerHook != null) {
					runnerHook.beginStage(stage);

					final ISequences preloadedResult = runnerHook.getPrestoredSequences(stage, dataTransformer);
					if (preloadedResult != null) {
						monitor.beginTask("", 1);
						try {
							monitor.worked(1);
							return new MultiStateResult(preloadedResult, new HashMap<>());
						} finally {
							runnerHook.endStage(stage);
							dataTransformer.getLifecyleManager().endPhase(stage);

							monitor.done();
						}
					}
				}

				try {
					if (inputState.getSolutions().size() == 1) {
						return inputState;
					}

					final List<NonNullPair<ISequences, Map<String, Object>>> output = new LinkedList<>(inputState.getSolutions());
					// Sort results by fitness
					Collections.sort(output, new Comparator<NonNullPair<ISequences, Map<String, Object>>>() {

						@Override
						public int compare(final NonNullPair<ISequences, Map<String, Object>> o1, final NonNullPair<ISequences, Map<String, Object>> o2) {
							final long a = getTotal(o1.getSecond());
							final long b = getTotal(o2.getSecond());
							return Long.compare(a, b);
						}

						long getTotal(final Map<String, Object> m) {
							if (m == null) {
								return 0L;
							}
							final Map<String, Long> currentFitnesses = (Map<String, Long>) m.get(OptimiserConstants.G_AI_fitnessComponents);
							if (currentFitnesses == null) {
								return 0L;
							}
							long sum = 0L;
							for (final Long l : currentFitnesses.values()) {
								if (l != null) {
									sum += l.longValue();
								}
							}
							return sum;

						}
					});

					// Trim results
					while (output.size() > 1) {
						output.remove(1);
					}
					
					if (runnerHook != null) {
						runnerHook.reportSequences(stage, output.get(0).getFirst(), dataTransformer);
					}

					return new MultiStateResult(output.get(0), output);
				} finally {
					if (runnerHook != null) {
						runnerHook.endStage(stage);
					}
					dataTransformer.getLifecyleManager().endPhase(stage);

					monitor.done();
				}
			}

			@Override
			public int getProgressTicks() {
				return progressTicks;
			}
		};
		chainBuilder.addLink(link);
		return link;
	}
}
