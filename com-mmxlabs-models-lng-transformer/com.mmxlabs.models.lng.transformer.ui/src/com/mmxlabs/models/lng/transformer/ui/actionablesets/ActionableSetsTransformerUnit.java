/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.actionablesets;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.actionplan.LNGParameters_ActionPlanSettingsModule;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InitialPhaseOptimisationDataModule;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IProgressReporter;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScope;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScopeImpl;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.optimiser.lso.impl.thresholders.GreedyThresholder;
import com.mmxlabs.scheduler.optimiser.actionableset.ActionableSetMover;
import com.mmxlabs.scheduler.optimiser.actionableset.ActionableSetOptimiser;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.FitnessCalculator;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.LookupManager;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class ActionableSetsTransformerUnit implements IChainLink {

	protected final String stage;

	protected JobExecutorFactory jobExecutorFactory;

	protected UserSettings userSettings;
	protected ActionPlanOptimisationStage stageSettings;

	private final int progressTicks;

	public ActionableSetsTransformerUnit(final String stage, final UserSettings userSettings, final ActionPlanOptimisationStage stageSettings, final JobExecutorFactory jobExecutorFactory,
			final int progressTicks) {
		this.stage = stage;
		this.userSettings = userSettings;
		this.stageSettings = stageSettings;
		this.jobExecutorFactory = jobExecutorFactory;
		this.progressTicks = progressTicks;
	}

	@Override
	public int getProgressTicks() {
		return progressTicks;
	}

	@Override
	public IMultiStateResult run(final LNGDataTransformer dataTransformer, final SequencesContainer initialSequences, final IMultiStateResult inputState, final IProgressMonitor monitor) {

		dataTransformer.getLifecyleManager().startPhase(stage, dataTransformer.getHints());

		final IRunnerHook runnerHook = dataTransformer.getRunnerHook();

		try {
			if (runnerHook != null) {
				runnerHook.beginStage(stage);

				// Is there an existing set of sequences to load ?
				final ISequences preloadedResult = runnerHook.getPrestoredSequences(stage, dataTransformer);
				if (preloadedResult != null) {
					monitor.beginTask("", 1);
					try {
						monitor.worked(1);
						return new MultiStateResult(preloadedResult, new HashMap<>());
					} finally {
						monitor.done();
					}
				}
			}

			return doRunOptimisation(dataTransformer, initialSequences, inputState, monitor, runnerHook);
		} finally {
			if (runnerHook != null) {
				runnerHook.endStage(stage);
			}
			dataTransformer.getLifecyleManager().endPhase(stage);
		}
	}

	protected IMultiStateResult doRunOptimisation(final LNGDataTransformer dataTransformer, final SequencesContainer initialSequences, final IMultiStateResult inputState,
			final IProgressMonitor monitor, final @Nullable IRunnerHook runnerHook) {

		final int jobId = 0;
		monitor.beginTask("", 100);
		try {
			// Construct the optimiser instance
			final List<Module> modules = createModules(dataTransformer, stage, userSettings, stageSettings, initialSequences.getSequences(), inputState.getBestSolution().getFirst(),
					dataTransformer.getHints());
			final Injector injector = dataTransformer.getInjector().createChildInjector(modules);

			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();

				final JobExecutorFactory subExecutorFactory = jobExecutorFactory.withDefaultBegin(() -> {
					final ThreadLocalScopeImpl s = injector.getInstance(ThreadLocalScopeImpl.class);
					s.enter();
					return s;
				});
				try (JobExecutor jobExecutor = subExecutorFactory.begin()) {

					final ActionableSetOptimiser instance = injector.getInstance(ActionableSetOptimiser.class);
					final ISequences inputRawSequences = injector.getInstance(Key.get(ISequences.class, Names.named(OptimiserConstants.SEQUENCE_TYPE_INPUT)));
					final Collection<IMultiStateResult> result = instance.optimise(inputRawSequences, new IProgressReporter() {

						@Override
						public void report(int workDone) {
							monitor.worked(workDone);

						}

						@Override
						public void done() {

						}

						@Override
						public void begin(int totalWork) {
							monitor.beginTask("Generate actionable sets", totalWork);

						}
					}, jobExecutor);
					if (result != null) {
						return result.iterator().next();
					}

					return inputState;
				} catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					if (runnerHook != null) {
						runnerHook.endStageJob(stage, jobId, injector);
					}
				}
			}
		} finally {
			monitor.done();
		}

	}

	protected List<Module> createModules(final LNGDataTransformer dataTransformer, final String stage, final UserSettings userSettings, final ActionPlanOptimisationStage stageSettings,
			final ISequences initialSequences, final ISequences inputSequences, final Collection<@NonNull String> hints) {

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		final List<Module> modules = new LinkedList<>();
		modules.add(new AbstractModule() {

			@Override
			protected void configure() {
				// binder().requireExplicitBindings();

			}
		});
		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputSequences));
		modules.add(new InitialPhaseOptimisationDataModule());
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, stageSettings.getConstraintAndFitnessSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));

		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_ActionPlanSettingsModule(stageSettings), services,
				IOptimiserInjectorService.ModuleType.Module_OptimisationParametersModule, hints));

		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new CreateActionableSetPlanModule(), services, IOptimiserInjectorService.ModuleType.Module_Optimisation, hints));

		modules.add(new AbstractModule() {

			@Override
			protected void configure() {

				bind(EvaluationHelper.class).in(ThreadLocalScope.class);
				bind(FitnessCalculator.class).in(ThreadLocalScope.class);
				bind(ActionableSetOptimiser.class).in(ThreadLocalScope.class);
				bind(ILookupManager.class).to(LookupManager.class);
			}

			@Provides
			@ThreadLocalScope
			private ActionableSetMover providePerThreadMover(@NonNull final Injector injector) {
				ActionableSetMover mover = new ActionableSetMover();
				injector.injectMembers(mover);
				return mover;
			}

			@Provides
			private IFitnessEvaluator createFitnessEvaluator(@NonNull final Injector injector, @NonNull final List<IFitnessComponent> fitnessComponents,
					@NonNull final List<IEvaluationProcess> evaluationProcesses) {
				// create a linear Fitness evaluator.
				final LinearSimulatedAnnealingFitnessEvaluator fitnessEvaluator = new LinearSimulatedAnnealingFitnessEvaluator(new GreedyThresholder(), fitnessComponents, evaluationProcesses);
				injector.injectMembers(fitnessEvaluator);
				return fitnessEvaluator;
			}
		});
		return modules;
	}
}
