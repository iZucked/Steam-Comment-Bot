/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown.chain;

import java.util.Collection;
import java.util.HashSet;
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
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.ILNGStateTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.ui.ContainerProvider;
import com.mmxlabs.models.lng.transformer.ui.LNGExporterUnit;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.breakdown.ActionSetEvaluationHelper;
import com.mmxlabs.models.lng.transformer.ui.breakdown.BagMover;
import com.mmxlabs.models.lng.transformer.ui.breakdown.BagOptimiser;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class LNGActionSetTransformerUnit implements ILNGStateTransformerUnit {

	@NonNull
	public static IChainLink chain(final ChainBuilder chainBuilder, @NonNull final String phase, @NonNull final UserSettings userSettings, @NonNull ActionPlanOptimisationStage stageSettings,
			final int progressTicks) {
		return chain(chainBuilder, phase, userSettings, stageSettings, null, progressTicks);
	}

	@NonNull
	public static IChainLink chain(final ChainBuilder chainBuilder, @NonNull final String phase, @NonNull final UserSettings userSettings, @NonNull ActionPlanOptimisationStage stageSettings,
			@Nullable final ExecutorService executorService, final int progressTicks) {
		final IChainLink link = new IChainLink() {

			private LNGActionSetTransformerUnit t;

			// @Override
			// public IMultiStateResult run() {
			// if (t == null) {
			// throw new IllegalStateException("#init has not been called");
			// }
			// }

			@Override
			public IMultiStateResult run(SequencesContainer initialSequences, final IMultiStateResult inputState, final IProgressMonitor monitor) {
				final LNGDataTransformer dt = chainBuilder.getDataTransformer();

				@NonNull
				Collection<@NonNull String> hints = new HashSet<>(dt.getHints());
				if (userSettings.isGenerateCharterOuts()) {
					hints.add(LNGTransformerHelper.HINT_GENERATE_CHARTER_OUTS);
				} else {
					hints.remove(LNGTransformerHelper.HINT_GENERATE_CHARTER_OUTS);
				}
				hints.remove(LNGTransformerHelper.HINT_CLEAN_STATE_EVALUATOR);

				t = new LNGActionSetTransformerUnit(dt, phase, userSettings, stageSettings, executorService, initialSequences.getSequences(), inputState, hints);
				return t.run(monitor);
			}

			@Override
			public int getProgressTicks() {
				return progressTicks;
			}

			// @Override
			// public IMultiStateResult getInputState() {
			// if (t == null) {
			// throw new IllegalStateException("#init has not been called");
			// }
			// return t.getInputState();
			// }
		};
		chainBuilder.addLink(link);
		return link;
	}

	@NonNull
	public static IChainLink chainFake(final ChainBuilder chainBuilder, @NonNull final String phase, @NonNull final UserSettings userSettings, @NonNull ActionPlanOptimisationStage stageSettings,
			@Nullable final ExecutorService executorService, final int progressTicks) {
		final IChainLink link = new IChainLink() {

			private LNGActionSetTransformerUnit t;

			@Override
			public IMultiStateResult run(SequencesContainer initialSequences, final IMultiStateResult inputState, final IProgressMonitor monitor) {
				final LNGDataTransformer dt = chainBuilder.getDataTransformer();
				t = new LNGActionSetTransformerUnit(dt, phase, userSettings, stageSettings, executorService, initialSequences.getSequences(), inputState, dt.getHints());
				t.run(monitor);
				return inputState;
			}

			@Override
			public int getProgressTicks() {
				return progressTicks;
			}
		};
		chainBuilder.addLink(link);
		return link;
	}

	public static IChainLink export(final ChainBuilder chainBuilder, final int progressTicks, @NonNull final LNGScenarioToOptimiserBridge runner, @NonNull final ContainerProvider containerProvider) {
		return LNGExporterUnit.exportMultiple(chainBuilder, progressTicks, runner, containerProvider, "Saving action plan", parent -> {

			final List<Container> elementsToRemove = new LinkedList<>();
			for (final Container c : parent.getElements()) {
				if (c.getName().startsWith("ActionSet-")) {
					elementsToRemove.add(c);
				}
			}
			for (final Container c : elementsToRemove) {
				parent.getScenarioService().delete(c);
			}
		}, changeSetIdx -> {
			String newName;
			if (changeSetIdx == 0) {
				newName = "ActionSet-base";
				changeSetIdx++;
			} else {
				newName = String.format("ActionSet-%s", (changeSetIdx++));
			}
			return newName;
		});
	}

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final Injector injector;

	@NonNull
	private final IMultiStateResult inputState;

	@NonNull
	private final String phase;

	private final Map<Thread, BagMover> threadCache = new ConcurrentHashMap<>(100);

	@SuppressWarnings("null")
	public LNGActionSetTransformerUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final String phase, @NonNull final UserSettings userSettings,
			@NonNull ActionPlanOptimisationStage stageSettings, @Nullable final ExecutorService executorService, @NonNull ISequences initialSequences, @NonNull final IMultiStateResult inputState,
			@NonNull final Collection<String> hints) {
		this.dataTransformer = dataTransformer;
		this.phase = phase;

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		final List<Module> modules = new LinkedList<>();
		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputState.getBestSolution().getFirst()));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, stageSettings.getConstraintAndFitnessSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_ActionPlanSettingsModule(stageSettings), services,
				IOptimiserInjectorService.ModuleType.Module_OptimisationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGActionPlanModule(), services, IOptimiserInjectorService.ModuleType.Module_Optimisation, hints));

		modules.add(new AbstractModule() {

			@Override
			protected void configure() {
				// bind(BagMover.class).in(PerChainUnitScope.class);
				assert executorService != null;
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
