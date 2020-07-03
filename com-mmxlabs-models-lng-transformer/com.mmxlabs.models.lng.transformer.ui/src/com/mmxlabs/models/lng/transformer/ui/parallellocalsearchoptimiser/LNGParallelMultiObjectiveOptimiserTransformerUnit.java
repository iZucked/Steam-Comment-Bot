/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
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
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.models.lng.parameters.MultipleSolutionSimilarityOptimisationStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGOptimisationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_AnnealingSettingsModule;
import com.mmxlabs.models.lng.transformer.ui.transformerunits.AbstractLNGOptimiserTransformerUnit;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.optimiser.lso.multiobjective.impl.NonDominatedSolution;
import com.mmxlabs.optimiser.lso.multiobjective.modules.MultiObjectiveOptimiserModule;
import com.mmxlabs.optimiser.optimiser.lso.parallellso.SequentialParallelSimpleMultiObjectiveOptimiser;
import com.mmxlabs.optimiser.optimiser.lso.parallellso.SimpleMultiObjectiveLSOMover;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class LNGParallelMultiObjectiveOptimiserTransformerUnit extends AbstractLNGOptimiserTransformerUnit<MultipleSolutionSimilarityOptimisationStage> {
	private final Map<Thread, SimpleMultiObjectiveLSOMover> threadCache = new ConcurrentHashMap<>(100);
	private boolean singleSolution;
	
	@NonNull
	public static IChainLink chain(final ChainBuilder chainBuilder, @NonNull final String stage, @NonNull final UserSettings userSettings, @NonNull final MultipleSolutionSimilarityOptimisationStage stageSettings,
			@Nullable final CleanableExecutorService executorService, final int progressTicks, final boolean singleSolution) {
		@NonNull
		final Collection<@NonNull String> hints = new HashSet<>(chainBuilder.getDataTransformer().getHints());
		LNGTransformerHelper.updatHintsFromUserSettings(userSettings, hints);
		hints.remove(LNGTransformerHelper.HINT_CLEAN_STATE_EVALUATOR);

		return AbstractLNGOptimiserTransformerUnit.chain(chainBuilder, stage, userSettings, executorService, progressTicks, hints, (initialSequences, inputState, monitor) -> {
			LNGParallelMultiObjectiveOptimiserTransformerUnit unit = new LNGParallelMultiObjectiveOptimiserTransformerUnit(chainBuilder.getDataTransformer(), stage, userSettings, stageSettings, initialSequences.getSequences(), inputState.getBestSolution().getFirst(), hints, executorService, singleSolution);
			return unit.run(new SubProgressMonitor(monitor, 100));
		});
	}

	public LNGParallelMultiObjectiveOptimiserTransformerUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final String stage, @NonNull final UserSettings userSettings,
			@NonNull final MultipleSolutionSimilarityOptimisationStage stageSettings, @NonNull final ISequences initialSequences, @NonNull final ISequences inputSequences,
			@NonNull final Collection<@NonNull String> hints, CleanableExecutorService executorService, final boolean singleSolution) {
		super(dataTransformer, stage, userSettings, stageSettings, initialSequences, inputSequences, hints, executorService);
		this.singleSolution = singleSolution;
	}

	@Override
	protected LocalSearchOptimiser createOptimiser(final LNGDataTransformer dataTransformer, final String stage, final ISequences inputSequences) {
		final IRunnerHook runnerHook = dataTransformer.getRunnerHook();
		if (runnerHook != null) {
			runnerHook.beginStageJob(stage, 0, getInjector());
		}

		try (PerChainUnitScopeImpl scope = getInjector().getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();

			LocalSearchOptimiser optimiser = getInjector().getInstance(SequentialParallelSimpleMultiObjectiveOptimiser.class);
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
			@NonNull final MultipleSolutionSimilarityOptimisationStage stageSettings, @NonNull final ISequences initialSequences, @NonNull final ISequences inputSequences,
			@NonNull final Collection<@NonNull String> hints, CleanableExecutorService executorService) {
		final List<Module> modules = new LinkedList<>();

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		addDefaultModules(modules, dataTransformer, stage, userSettings, stageSettings, initialSequences, inputSequences, hints, executorService);

		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_AnnealingSettingsModule(stageSettings.getSeed(), stageSettings.getAnnealingSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_OptimisationParametersModule, hints));

		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(CollectionsUtil.makeLinkedList(new LNGOptimisationModule()), services, IOptimiserInjectorService.ModuleType.Module_Optimisation, hints));

		modules.add(new MultiObjectiveOptimiserModule(executorService));
		
		modules.add(new AbstractModule() {

			@Provides
			private SimpleMultiObjectiveLSOMover providePerThreadLSOMover(@NonNull final Injector injector, @Named(LocalSearchOptimiserModule.MULTIOBJECTIVE_OBJECTIVE_NAMES) final List<String> objectiveNames){
				SimpleMultiObjectiveLSOMover simpleMultiObjectiveLSOMover = threadCache.get(Thread.currentThread());
				if (simpleMultiObjectiveLSOMover == null) {
					final PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class);
					scope.enter();
					simpleMultiObjectiveLSOMover = new SimpleMultiObjectiveLSOMover();
					injector.injectMembers(simpleMultiObjectiveLSOMover);
					
					// Note: this is critical because we need to use the components that are created per thread!
					List<IFitnessComponent> multiObjectiveFitnessComponents = MultiObjectiveOptimiserModule
							.getMultiObjectiveFitnessComponents(simpleMultiObjectiveLSOMover.getMultiObjectiveFitnessEvaluator(), objectiveNames);

					simpleMultiObjectiveLSOMover.setFitnessComponents(multiObjectiveFitnessComponents);

					threadCache.put(Thread.currentThread(), simpleMultiObjectiveLSOMover);
				}
				return simpleMultiObjectiveLSOMover;
			}

			@Override
			protected void configure() {
			}
		});
		
		return modules;
	}
	
	@Override
	public IMultiStateResult run(final @NonNull IProgressMonitor monitor) {
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

				if (singleSolution) {
					if (bestRawSequences != null && bestSolution != null) {
						return new MultiStateResult(bestRawSequences, LNGSchedulerJobUtils.extractOptimisationAnnotations(bestSolution));
					} else {
						throw new RuntimeException("Unable to optimise");
					}
				} else {
					List<NonDominatedSolution> sortedArchive = ((SequentialParallelSimpleMultiObjectiveOptimiser) optimiser).getSortedArchive(true); //TODO: make generic

					final List<NonNullPair<ISequences, Map<String, Object>>> solutions = sortedArchive.stream() //
							.distinct() //
							.map(r -> new NonNullPair<ISequences, Map<String, Object>>(r.getSequences(), new HashMap<>())) //
							.collect(Collectors.toList());

					if (bestRawSequences != null && bestSolution != null) {
						return new MultiStateResult(new NonNullPair<ISequences, Map<String, Object>>(bestRawSequences, new HashMap<>()), solutions);
					} else {
						throw new RuntimeException("Unable to optimise");
					}
				}
			} finally {
				if (runnerHook != null) {
					runnerHook.endStageJob(stage, 1, injector);
				}
				// Clean up thread-locals created in the scope object
				threadCleanup(scope);
				monitor.done();
			}
		}
	}

	protected void threadCleanup(PerChainUnitScopeImpl scope) {
		for (final Thread thread : threadCache.keySet()) {
			scope.exit(thread);
		}
		threadCache.clear();
	}

}
