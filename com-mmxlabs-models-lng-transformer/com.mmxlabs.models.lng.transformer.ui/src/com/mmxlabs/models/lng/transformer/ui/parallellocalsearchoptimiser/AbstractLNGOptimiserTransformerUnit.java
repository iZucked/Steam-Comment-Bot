/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parallellocalsearchoptimiser;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.mmxlabs.common.util.TriFunction;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.ConstraintsAndFitnessSettingsStage;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
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
import com.mmxlabs.models.lng.transformer.inject.modules.LNGOptimisationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_AnnealingSettingsModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.optimiser.optimiser.lso.parallellso.LSOMover;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public abstract class AbstractLNGOptimiserTransformerUnit<T extends ConstraintsAndFitnessSettingsStage> implements ILNGStateTransformerUnit {
	@NonNull
	protected
	final LNGDataTransformer dataTransformer;

	@NonNull
	protected
	final Injector injector;

	@NonNull
	protected
	final String stage;
	
	protected final LocalSearchOptimiser optimiser;

	public AbstractLNGOptimiserTransformerUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final String stage, @NonNull final UserSettings userSettings,
			@NonNull final T stageSettings, @NonNull final ISequences initialSequences, @NonNull final ISequences inputSequences,
			@NonNull final Collection<@NonNull String> hints, ExecutorService executorService) {
		this.dataTransformer = dataTransformer;
		this.stage = stage;

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		final List<Module> modules = createModules(dataTransformer, stage, userSettings, stageSettings, initialSequences, inputSequences, hints, executorService);

		injector = dataTransformer.getInjector().createChildInjector(modules);
		optimiser = createOptimiser(dataTransformer, stage, inputSequences);
	}

	protected abstract LocalSearchOptimiser createOptimiser(final LNGDataTransformer dataTransformer, final String stage, final ISequences inputSequences);
	
	protected abstract List<Module> createModules(@NonNull final LNGDataTransformer dataTransformer, @NonNull final String stage, @NonNull final UserSettings userSettings,
			@NonNull final T stageSettings, @NonNull final ISequences initialSequences, @NonNull final ISequences inputSequences,
			@NonNull final Collection<@NonNull String> hints, ExecutorService executorService);

	@Override
	@NonNull
	public LNGDataTransformer getDataTransformer() {
		return dataTransformer;
	}

	@Override
	public IMultiStateResult run(@NonNull final IProgressMonitor monitor) {
		final IRunnerHook runnerHook = dataTransformer.getRunnerHook();

		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();

			monitor.beginTask("", 100);
			try {

				// Main Optimisation Loop
				while (!optimiser.isFinished()) {
					if (monitor.isCanceled()) {
						throw new OperationCanceledException();
					}
					optimiser.step(1);
					monitor.worked(1);
				}
				assert optimiser.isFinished();

				final IAnnotatedSolution bestSolution = optimiser.getBestSolution();
				final ISequences bestRawSequences = optimiser.getBestRawSequences();

				if (bestRawSequences != null && bestSolution != null) {
					return new MultiStateResult(bestRawSequences, LNGSchedulerJobUtils.extractOptimisationAnnotations(bestSolution));
				} else {
					throw new RuntimeException("Unable to optimise");
				}
			} finally {
				if (runnerHook != null) {
					runnerHook.endStageJob(stage, 1, injector);
				}
				monitor.done();
				// Clean up thread-locals created in the scope object
				threadCleanup(scope);
			}
		}
	}

	protected abstract void threadCleanup(PerChainUnitScopeImpl scope);

	@NonNull
		public static IChainLink chain(final ChainBuilder chainBuilder, @NonNull final String stage, @NonNull final UserSettings userSettings,
				@Nullable final ExecutorService executorService, final int progressTicks, final Collection<@NonNull String> hints, final TriFunction<SequencesContainer, IMultiStateResult, IProgressMonitor, @NonNull IMultiStateResult> transformerUnitActor) {
			final IChainLink link = new IChainLink() {
	
				@Override
				public IMultiStateResult run(final SequencesContainer initialSequences, final IMultiStateResult inputState, final IProgressMonitor monitor) {
					final LNGDataTransformer dataTransformer = chainBuilder.getDataTransformer();
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
								monitor.done();
							}
						}
					}
	
					monitor.beginTask("", 100);
					try {
						return transformerUnitActor.apply(initialSequences, inputState, monitor);
					} finally {
						if (runnerHook != null) {
							runnerHook.endStage(stage);
						}
	
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

	protected void addDefaultModules(@NonNull List<Module> modules, @NonNull final LNGDataTransformer dataTransformer, @NonNull final String stage, @NonNull final UserSettings userSettings,
			@NonNull final T stageSettings, @NonNull final ISequences initialSequences, @NonNull final ISequences inputSequences,
			@NonNull final Collection<@NonNull String> hints, ExecutorService executorService) {

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();
		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputSequences));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, stageSettings.getConstraintAndFitnessSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
//		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGOptimisationModule(), services, IOptimiserInjectorService.ModuleType.Module_Optimisation, hints));
	}

	public Injector getInjector() {
		return injector;
	}

}
