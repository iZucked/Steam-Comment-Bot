/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.IMultiStateResult;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class LNGExporterUnit {

	public static IChainLink export(final ChainBuilder chainBuilder, final int progressTicks, @NonNull final LNGScenarioToOptimiserBridge runner, String name,
			@NonNull ContainerProvider containerProvider, @NonNull ContainerProvider resultProvider) {
		final IChainLink link = new IChainLink() {

			private IMultiStateResult state;

			@Override
			public IMultiStateResult run(final IProgressMonitor monitor) {
				final IMultiStateResult pState = state;
				if (pState == null) {
					throw new IllegalStateException("#init has not been called");
				}
				// Assuming the scenario data is at the initial state.

				// Remove existing solutions
				final Container parent = containerProvider.get();
				if (parent == null) {
					// Error?
					return pState;
				}
				monitor.beginTask("Saving", 1);
				try {
					try {
						// Save the scenario as a fork.
						ScenarioInstance child = runner.storeAsCopy(pState.getBestSolution().getFirst(), name, parent, null);
						resultProvider.set(child);
					} catch (final Exception e) {
						throw new RuntimeException("Unable to store scenario: " + e.getMessage(), e);
					}

					monitor.worked(1);
				} finally {
					monitor.done();
				}

				return pState;
			}

			@Override
			public void init(SequencesContainer initialSequences, @NonNull final IMultiStateResult inputState) {
				this.state = inputState;
			}

			@Override
			public int getProgressTicks() {
				return progressTicks;
			}

			@Override
			public IMultiStateResult getInputState() {
				final IMultiStateResult pState = state;
				if (pState == null) {
					throw new IllegalStateException("#init has not been called");
				}
				return pState;
			}
		};
		chainBuilder.addLink(link);
		return link;
	}
}
