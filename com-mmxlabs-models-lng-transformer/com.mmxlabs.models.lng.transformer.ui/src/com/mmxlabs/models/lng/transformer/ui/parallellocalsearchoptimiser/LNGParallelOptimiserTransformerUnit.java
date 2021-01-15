/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parallellocalsearchoptimiser;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGOptimisationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_AnnealingSettingsModule;
import com.mmxlabs.models.lng.transformer.ui.transformerunits.AbstractLNGOptimiserTransformerUnit;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.optimiser.optimiser.lso.parallellso.LSOMover;
import com.mmxlabs.optimiser.optimiser.lso.parallellso.ProcessorAgnosticParallelLSO;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class LNGParallelOptimiserTransformerUnit extends AbstractLNGOptimiserTransformerUnit<LocalSearchOptimisationStage> {
	private final Map<Thread, LSOMover> threadCache = new ConcurrentHashMap<>(100);

	@NonNull
	public static IChainLink chain(final ChainBuilder chainBuilder, @NonNull final String stage, @NonNull final UserSettings userSettings, @NonNull final LocalSearchOptimisationStage stageSettings,
			@Nullable final CleanableExecutorService executorService, final int progressTicks) {
		@NonNull
		final Collection<@NonNull String> hints = new HashSet<>(chainBuilder.getDataTransformer().getHints());
		LNGTransformerHelper.updatHintsFromUserSettings(userSettings, hints);
		hints.remove(LNGTransformerHelper.HINT_CLEAN_STATE_EVALUATOR);

		return AbstractLNGOptimiserTransformerUnit.chain(chainBuilder, stage, userSettings, executorService, progressTicks, hints, (initialSequences, inputState, monitor) -> {
			final LNGParallelOptimiserTransformerUnit unit = new LNGParallelOptimiserTransformerUnit(chainBuilder.getDataTransformer(), stage, userSettings, stageSettings,
					initialSequences.getSequences(), inputState.getBestSolution().getFirst(), hints, executorService);
			return unit.run(new SubProgressMonitor(monitor, 100));
		});
	}

	public LNGParallelOptimiserTransformerUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final String stage, @NonNull final UserSettings userSettings,
			@NonNull final LocalSearchOptimisationStage stageSettings, @NonNull final ISequences initialSequences, @NonNull final ISequences inputSequences,
			@NonNull final Collection<@NonNull String> hints, final CleanableExecutorService executorService) {
		super(dataTransformer, stage, userSettings, stageSettings, initialSequences, inputSequences, hints, executorService);
	}

	@Override
	protected LocalSearchOptimiser createOptimiser(final LNGDataTransformer dataTransformer, final String stage, final ISequences inputSequences) {
		final IRunnerHook runnerHook = dataTransformer.getRunnerHook();
		if (runnerHook != null) {
			runnerHook.beginStageJob(stage, 0, getInjector());
		}

		try (PerChainUnitScopeImpl scope = getInjector().getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();

			final LocalSearchOptimiser optimiser = getInjector().getInstance(ProcessorAgnosticParallelLSO.class);
			optimiser.setProgressMonitor(new NullOptimiserProgressMonitor());
			optimiser.init();

			final IAnnotatedSolution startSolution = optimiser.start(getInjector().getInstance(IOptimisationContext.class),
					getInjector().getInstance(Key.get(ISequences.class, Names.named(OptimiserConstants.SEQUENCE_TYPE_INITIAL))), inputSequences);
			if (startSolution == null) {
				throw new IllegalStateException("Unable to get starting state");
			}
			return optimiser;
		}
	}

	@Override
	protected List<Module> createModules(@NonNull final LNGDataTransformer dataTransformer, @NonNull final String stage, @NonNull final UserSettings userSettings,
			@NonNull final LocalSearchOptimisationStage stageSettings, @NonNull final ISequences initialSequences, @NonNull final ISequences inputSequences,
			@NonNull final Collection<@NonNull String> hints, final CleanableExecutorService executorService) {
		final List<Module> modules = new LinkedList<>();

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		addDefaultModules(modules, dataTransformer, stage, userSettings, stageSettings, initialSequences, inputSequences, hints, executorService);

		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_AnnealingSettingsModule(stageSettings.getSeed(), stageSettings.getAnnealingSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_OptimisationParametersModule, hints));

		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(CollectionsUtil.makeLinkedList(new LNGOptimisationModule(), new LocalSearchOptimiserModule()), services,
				IOptimiserInjectorService.ModuleType.Module_Optimisation, hints));

		modules.add(new AbstractModule() {

			@Override
			protected void configure() {
				bind(CleanableExecutorService.class).toInstance(executorService);
			}

			@Provides
			private LSOMover providePerThreadLSOMover(@NonNull final Injector injector) {

				LSOMover lsoMover = threadCache.get(Thread.currentThread());
				if (lsoMover == null) {
					final PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class);
					scope.enter();
					lsoMover = new LSOMover();
					injector.injectMembers(lsoMover);
					threadCache.put(Thread.currentThread(), lsoMover);
				}
				return lsoMover;
			}

		});

		return modules;
	}

	@Override
	protected void threadCleanup(final PerChainUnitScopeImpl scope) {
		this.executorService.clean();
		for (final Thread thread : threadCache.keySet()) {
			if (thread != null) {
				scope.exit(thread);
			}
		}
		threadCache.clear();
	}

}
