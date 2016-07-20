/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.IMultiStateResult;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class LNGExporterUnit {

	public static IChainLink exportMultiple(final ChainBuilder chainBuilder, final int progressTicks, @NonNull final LNGScenarioToOptimiserBridge runner,
			@NonNull final ContainerProvider containerProvider, String taskName, @Nullable Consumer<Container> cleanUpParent, @NonNull Function<Integer, String> nameFactory) {

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
					{
						// Assume ITS run and just try to dump results.
						final List<NonNullPair<ISequences, Map<String, Object>>> solutions = pState.getSolutions();
						for (final NonNullPair<ISequences, Map<String, Object>> p : solutions) {
							runner.exportAsCopy(p.getFirst(), p.getSecond());
						}
					}

					return pState;
				}
				if (cleanUpParent != null) {
					cleanUpParent.accept(parent);
				}
				// {
				// final List<Container> elementsToRemove = new LinkedList<>();
				// for (final Container c : parent.getElements()) {
				// if (c.getName().startsWith("ActionSet-")) {
				// elementsToRemove.add(c);
				// }
				// }
				// for (final Container c : elementsToRemove) {
				// parent.getScenarioService().delete(c);
				// }
				// }
				final List<NonNullPair<ISequences, Map<String, Object>>> solutions = pState.getSolutions();
				monitor.beginTask(taskName, solutions.size());
				try {
					int changeSetIdx = 0;
					for (final NonNullPair<ISequences, Map<String, Object>> changeSet : solutions) {
						String newName = nameFactory.apply(changeSetIdx);
						// if (changeSetIdx == 0) {
						// newName = "ActionSet-base";
						// changeSetIdx++;
						// } else {
						// newName = String.format("ActionSet-%s", (changeSetIdx++));
						// }

						try {
							// Save the scenario as a fork.
							runner.storeAsCopy(changeSet.getFirst(), newName, parent, null);
						} catch (final Exception e) {
							throw new RuntimeException("Unable to store scenario: " + e.getMessage(), e);
						}

						monitor.worked(1);
					}
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

	public static IChainLink exportSingle(final ChainBuilder chainBuilder, final int progressTicks, @NonNull final LNGScenarioToOptimiserBridge runner, String name,
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
