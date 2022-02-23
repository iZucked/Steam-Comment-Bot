/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.actionablesets;

import java.util.Collection;
import java.util.HashSet;
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
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.ILNGStateTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InitialPhaseOptimisationDataModule;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IProgressReporter;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
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

public class ActionableSetsTransformerUnit implements ILNGStateTransformerUnit {

	@NonNull
	public static IChainLink chain(final ChainBuilder chainBuilder, @NonNull final String phase, @NonNull final UserSettings userSettings, @NonNull ActionPlanOptimisationStage stageSettings,
			final int progressTicks) {
		return chain(chainBuilder, phase, userSettings, stageSettings, null, progressTicks);
	}

	@NonNull
	public static IChainLink chain(final ChainBuilder chainBuilder, @NonNull final String phase, @NonNull final UserSettings userSettings, @NonNull ActionPlanOptimisationStage stageSettings,
			@Nullable final JobExecutorFactory jobExecutorFactory, final int progressTicks) {
		final IChainLink link = new IChainLink() {

			private ActionableSetsTransformerUnit t;

			@Override
			public IMultiStateResult run(SequencesContainer initialSequences, final IMultiStateResult inputState, final IProgressMonitor monitor) {
				final LNGDataTransformer dt = chainBuilder.getDataTransformer();

				@NonNull
				Collection<@NonNull String> hints = new HashSet<>(dt.getHints());
				LNGTransformerHelper.updateHintsFromUserSettings(userSettings, hints);

				t = new ActionableSetsTransformerUnit(dt, phase, userSettings, stageSettings, jobExecutorFactory, initialSequences.getSequences(), inputState, hints);
				return t.run(monitor);
			}

			@Override
			public int getProgressTicks() {
				return progressTicks;
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

	@NonNull
	private final String phase;

	private @Nullable JobExecutorFactory jobExecutorFactory;

	@SuppressWarnings("null")
	public ActionableSetsTransformerUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final String phase, @NonNull final UserSettings userSettings,
			@NonNull ActionPlanOptimisationStage stageSettings, @Nullable final JobExecutorFactory jobExecutorFactory, @NonNull ISequences initialSequences,
			@NonNull final IMultiStateResult inputState, @NonNull final Collection<String> hints) {
		this.dataTransformer = dataTransformer;
		this.phase = phase;
		this.jobExecutorFactory = jobExecutorFactory;

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		final List<Module> modules = new LinkedList<>();
		modules.add(new AbstractModule() {

			@Override
			protected void configure() {
				// binder().requireExplicitBindings();

			}
		});
		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputState.getBestSolution().getFirst()));
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
		
		final JobExecutorFactory subExecutorFactory = jobExecutorFactory.withDefaultBegin(() -> {
			final ThreadLocalScopeImpl s = injector.getInstance(ThreadLocalScopeImpl.class);
			s.enter();
			return s;
		});
		
		try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
			scope.enter();

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
			} catch (final Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} finally {
				monitor.done();
			}
		}
	}
}
