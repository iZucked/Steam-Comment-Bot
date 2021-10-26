/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parallellocalsearchoptimiser;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.inject.Singleton;

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
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage;
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
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScope;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScopeImpl;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;
import com.mmxlabs.optimiser.lso.impl.thresholders.GreedyThresholder;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.optimiser.optimiser.lso.parallellso.LSOMover;
import com.mmxlabs.optimiser.optimiser.lso.parallellso.ProcessorAgnosticParallelLSO;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class LNGParallelHillClimbingOptimiserTransformerUnit extends AbstractLNGOptimiserTransformerUnit<HillClimbOptimisationStage> {

	@NonNull
	public static IChainLink chain(final ChainBuilder chainBuilder, @NonNull final String stage, @NonNull final UserSettings userSettings, @NonNull final HillClimbOptimisationStage stageSettings,
			@Nullable final JobExecutorFactory jobExecutorFactory, final int progressTicks) {
		@NonNull
		final Collection<@NonNull String> hints = new HashSet<>(chainBuilder.getDataTransformer().getHints());
		LNGTransformerHelper.updateHintsFromUserSettings(userSettings, hints);
		hints.remove(LNGTransformerHelper.HINT_CLEAN_STATE_EVALUATOR);

		return AbstractLNGOptimiserTransformerUnit.chain(chainBuilder, stage, userSettings, jobExecutorFactory, progressTicks, hints, (initialSequences, inputState, monitor) -> {
			final LNGParallelHillClimbingOptimiserTransformerUnit unit = new LNGParallelHillClimbingOptimiserTransformerUnit(chainBuilder.getDataTransformer(), stage, userSettings, stageSettings,
					initialSequences.getSequences(), inputState.getBestSolution().getFirst(), hints, jobExecutorFactory);
			return unit.run(monitor);
		});
	}

	public LNGParallelHillClimbingOptimiserTransformerUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final String stage, @NonNull final UserSettings userSettings,
			@NonNull final HillClimbOptimisationStage stageSettings, @NonNull final ISequences initialSequences, @NonNull final ISequences inputSequences,
			@NonNull final Collection<@NonNull String> hints, final JobExecutorFactory jobExecutorFactory) {
		super(dataTransformer, stage, userSettings, stageSettings, initialSequences, inputSequences, hints, jobExecutorFactory);
	}

	@Override
	protected LocalSearchOptimiser createOptimiser(final LNGDataTransformer dataTransformer, final String stage, final ISequences inputSequences) {
		final IRunnerHook runnerHook = dataTransformer.getRunnerHook();
		if (runnerHook != null) {
			runnerHook.beginStageJob(stage, 0, getInjector());
		}

		try (ThreadLocalScopeImpl scope = getInjector().getInstance(ThreadLocalScopeImpl.class)) {
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
			@NonNull final HillClimbOptimisationStage stageSettings, @NonNull final ISequences initialSequences, @NonNull final ISequences inputSequences,
			@NonNull final Collection<@NonNull String> hints) {
		final List<Module> modules = new LinkedList<>();

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		addDefaultModules(modules, dataTransformer, stage, userSettings, stageSettings, initialSequences, inputSequences, hints);

		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_AnnealingSettingsModule(stageSettings.getSeed(), stageSettings.getAnnealingSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_OptimisationParametersModule, hints));

		modules.addAll(
				LNGTransformerHelper.getModulesWithOverrides(CollectionsUtil.makeLinkedList(new LNGOptimisationModule()), services, IOptimiserInjectorService.ModuleType.Module_Optimisation, hints));

		modules.add(new AbstractModule() {

			@Override
			protected void configure() {
			}

			@Provides
			@Singleton
			ProcessorAgnosticParallelLSO buildParallelLSOOptimiser(@NonNull final Injector injector, @NonNull final IOptimisationContext context, @NonNull final ISequencesManipulator manipulator,
					@NonNull final List<@NonNull IFitnessComponent> fitnessComponents, @NonNull final IMoveGenerator moveGenerator,
					@Named(LocalSearchOptimiserModule.LSO_NUMBER_OF_ITERATIONS) final int numberOfIterations, @NonNull final List<IConstraintChecker> constraintCheckers,
					@NonNull final List<IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers, @NonNull final List<IEvaluationProcess> evaluationProcesses,
					@Named(LocalSearchOptimiserModule.RANDOM_SEED) final long seed) {

				final ProcessorAgnosticParallelLSO lso = new ProcessorAgnosticParallelLSO();
				final LinearSimulatedAnnealingFitnessEvaluator fitnessEvaluator = new LinearSimulatedAnnealingFitnessEvaluator(new GreedyThresholder(), fitnessComponents, evaluationProcesses);
				injector.injectMembers(fitnessEvaluator);

				setLSO(injector, context, manipulator, moveGenerator, fitnessEvaluator, numberOfIterations, constraintCheckers, evaluatedStateConstraintCheckers, evaluationProcesses, lso, seed);

				return lso;
			}

			private void setLSO(@NonNull final Injector injector, @NonNull final IOptimisationContext context, @NonNull final ISequencesManipulator manipulator,
					@NonNull final IMoveGenerator moveGenerator, @NonNull final IFitnessEvaluator fitnessEvaluator, final int numberOfIterations,
					@NonNull final List<@NonNull IConstraintChecker> constraintCheckers, @NonNull final List<@NonNull IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers,
					@NonNull final List<@NonNull IEvaluationProcess> evaluationProcesses, @NonNull final LocalSearchOptimiser lso, final long seed) {

				injector.injectMembers(lso);
				lso.setNumberOfIterations(numberOfIterations);

				lso.setSequenceManipulator(manipulator);

				lso.setMoveGenerator(moveGenerator);
				lso.setFitnessEvaluator(fitnessEvaluator);
				lso.setConstraintCheckers(constraintCheckers);
				lso.setEvaluatedStateConstraintCheckers(evaluatedStateConstraintCheckers);
				lso.setEvaluationProcesses(evaluationProcesses);

				lso.setReportInterval(Math.max(10, numberOfIterations / 100));

				lso.setRandom(new Random(seed));

			}
		});

		modules.add(new AbstractModule() {

			@Override
			protected void configure() {
				bind(JobExecutorFactory.class).toInstance(jobExecutorFactory);
			}

			@Provides
			@ThreadLocalScope
			private LSOMover providePerThreadLSOMover(@NonNull final Injector injector) {
				final LSOMover lsoMover = new LSOMover();
				injector.injectMembers(lsoMover);
				return lsoMover;
			}

		});

		return modules;
	}
}
