/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.stochasticactionsets;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.ILNGStateTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.IMultiStateResult;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGOptimisationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_OptimiserSettingsModule;
import com.mmxlabs.models.lng.transformer.ui.BagOptimiser;
import com.mmxlabs.models.lng.transformer.ui.ContainerProvider;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.breakdown.ActionSetEvaluationHelper;
import com.mmxlabs.models.lng.transformer.ui.breakdown.BagMover;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class LNGActionSetTransformerUnit implements ILNGStateTransformerUnit {
	private final Map<Thread, BagMover> threadCache = new ConcurrentHashMap<>(100);

	@NonNull
	public static IChainLink chain(final ChainBuilder chainBuilder, @NonNull final OptimiserSettings settings, final int progressTicks) {
		return chain(chainBuilder, settings, null, progressTicks);
	}

	@NonNull
	public static IChainLink chain(final ChainBuilder chainBuilder, @NonNull final OptimiserSettings settings, @Nullable final ExecutorService executorService, final int progressTicks) {
		final IChainLink link = new IChainLink() {

			private LNGActionSetTransformerUnit t;

			@Override
			public IMultiStateResult run(final IProgressMonitor monitor) {
				if (t == null) {
					throw new IllegalStateException("#init has not been called");
				}
				return t.run(monitor);
			}

			@Override
			public void init(final IMultiStateResult inputState) {
				final LNGDataTransformer dt = chainBuilder.getDataTransformer();
				t = new LNGActionSetTransformerUnit(dt, settings, executorService, inputState, dt.getHints());
			}

			@Override
			public int getProgressTicks() {
				return progressTicks;
			}

			@Override
			public IMultiStateResult getInputState() {
				if (t == null) {
					throw new IllegalStateException("#init has not been called");
				}
				return t.getInputState();
			}
		};
		chainBuilder.addLink(link);
		return link;
	}

	@NonNull
	public static IChainLink chainFake(final ChainBuilder chainBuilder, @NonNull final OptimiserSettings settings, @Nullable final ExecutorService executorService, final int progressTicks) {
		final IChainLink link = new IChainLink() {

			private LNGActionSetTransformerUnit t;

			@Override
			public IMultiStateResult run(final IProgressMonitor monitor) {
				if (t == null) {
					throw new IllegalStateException("#init has not been called");
				}
				t.run(monitor);
				return t.getInputState();
			}

			@Override
			public void init(final IMultiStateResult inputState) {
				final LNGDataTransformer dt = chainBuilder.getDataTransformer();
				t = new LNGActionSetTransformerUnit(dt, settings, executorService, inputState, dt.getHints());
			}

			@Override
			public int getProgressTicks() {
				return progressTicks;
			}

			@Override
			public IMultiStateResult getInputState() {
				if (t == null) {
					throw new IllegalStateException("#init has not been called");
				}
				return t.getInputState();
			}
		};
		chainBuilder.addLink(link);
		return link;
	}

	public static IChainLink export(final ChainBuilder chainBuilder, final int progressTicks, @NonNull final LNGScenarioToOptimiserBridge runner, @NonNull final ContainerProvider containerProvider) {
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
				{
					final List<Container> elementsToRemove = new LinkedList<>();
					for (final Container c : parent.getElements()) {
						if (c.getName().startsWith("ActionSet-")) {
							elementsToRemove.add(c);
						}
					}
					for (final Container c : elementsToRemove) {
						parent.getScenarioService().delete(c);
					}
				}
				final List<NonNullPair<ISequences, Map<String, Object>>> solutions = pState.getSolutions();
				monitor.beginTask("Saving action sets", solutions.size());
				try {
					int changeSetIdx = 0;
					for (final NonNullPair<ISequences, Map<String, Object>> changeSet : solutions) {
						String newName;
						if (changeSetIdx == 0) {
							newName = "ActionSet-base";
							changeSetIdx++;
						} else {
							newName = String.format("ActionSet-%s", (changeSetIdx++));
						}

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
			public void init(@NonNull final IMultiStateResult inputState) {
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

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final Injector injector;

	@NonNull
	private final IMultiStateResult inputState;

	@SuppressWarnings("null")
	public LNGActionSetTransformerUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final OptimiserSettings settings, @Nullable final ExecutorService executorService,
			@NonNull final IMultiStateResult inputState, @NonNull final Collection<String> hints) {
		this.dataTransformer = dataTransformer;

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		final List<Module> modules = new LinkedList<>();
		modules.add(new InputSequencesModule(inputState.getBestSolution().getFirst()));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(settings), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_OptimiserSettingsModule(settings), services,
				IOptimiserInjectorService.ModuleType.Module_OptimisationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGOptimisationModule(), services, IOptimiserInjectorService.ModuleType.Module_Optimisation, hints));

		modules.add(new AbstractModule() {

			@Override
			protected void configure() {
				// bind(BagMover.class).in(PerChainUnitScope.class);
				bind(ExecutorService.class).toInstance(executorService);
			}

			@Provides
			private BagMover providePerThreadBagMover(@NonNull final Injector injector) {

				BagMover bagMover = threadCache.get(Thread.currentThread());
				if (bagMover == null) {
					final PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class);
					scope.enter();
					bagMover = new BagMover();
					injector.injectMembers(bagMover);
					threadCache.put(Thread.currentThread(), bagMover);
					// System.out.println("thread:" + Thread.currentThread().getId());
				}
				return bagMover;
			}

			@Provides
			@Named("MAIN_MOVER")
			private ActionSetEvaluationHelper provideMainBagMover(@NonNull final Injector injector) {
				final ActionSetEvaluationHelper bagMover = injector.getInstance(ActionSetEvaluationHelper.class);
				bagMover.setStrictChecking(true);
				return bagMover;
			}

		});

		injector = dataTransformer.getInjector().createChildInjector(modules);

		this.inputState = inputState;
	}

	@Override
	@NonNull
	public LNGDataTransformer getDataTransformer() {
		return dataTransformer;
	}

	@Override
	@NonNull
	public IMultiStateResult getInputState() {
		return inputState;
	}

	@Override
	public IMultiStateResult run(@NonNull final IProgressMonitor monitor) {
		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();
			monitor.beginTask("", 100);
			try {

				final BagOptimiser instance = injector.getInstance(BagOptimiser.class);
				final ISequences inputRawSequences = injector.getInstance(Key.get(ISequences.class, Names.named(OptimiserConstants.SEQUENCE_TYPE_INPUT)));
				final IMultiStateResult result = instance.optimise(inputRawSequences, new SubProgressMonitor(monitor, 95), 1000);
				if (result != null) {
					return result;
				}
				return inputState;
			} catch (final Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				monitor.done();
				// Clean up thread-locals created in the scope object
				for (final Thread thread : threadCache.keySet()) {
					scope.exit(thread);
				}
				threadCache.clear();
			}
		}
	}
}
