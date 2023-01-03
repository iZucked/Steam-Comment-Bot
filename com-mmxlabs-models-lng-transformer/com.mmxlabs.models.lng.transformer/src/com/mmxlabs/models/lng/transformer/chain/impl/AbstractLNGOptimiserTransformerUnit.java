/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.models.lng.parameters.ConstraintsAndFitnessSettingsStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.inject.modules.PhaseOptimisationDataModule;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScopeImpl;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

@NonNullByDefault
public abstract class AbstractLNGOptimiserTransformerUnit<T extends LocalSearchOptimiser, U extends ConstraintsAndFitnessSettingsStage> implements IChainLink {

	protected final String stage;

	protected JobExecutorFactory jobExecutorFactory;

	protected UserSettings userSettings;
	protected U stageSettings;

	private final int progressTicks;
	private final Class<T> optimiserType;

	protected AbstractLNGOptimiserTransformerUnit(final String stage, final UserSettings userSettings, final U stageSettings, final JobExecutorFactory jobExecutorFactory, final int progressTicks,
			final Class<T> optimiserType) {
		this.stage = stage;
		this.userSettings = userSettings;
		this.stageSettings = stageSettings;
		this.jobExecutorFactory = jobExecutorFactory;
		this.progressTicks = progressTicks;
		this.optimiserType = optimiserType;
	}

	@Override
	public int getProgressTicks() {
		return progressTicks;
	}

	protected Key<T> getOptimiserTypeKey() {
		return Key.get(optimiserType);
	}

	protected T createOptimiser(final LNGDataTransformer dataTransformer, final Injector injector, final String stage, final int jobID, final ISequences initialSequences,
			final ISequences inputSequences) {
		final IRunnerHook runnerHook = dataTransformer.getRunnerHook();
		if (runnerHook != null) {
			runnerHook.beginStageJob(stage, jobID, injector);
		}

		try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
			scope.enter();

			final T optimiser = injector.getInstance(getOptimiserTypeKey());
			optimiser.setProgressMonitor(new NullOptimiserProgressMonitor());
			optimiser.init();

			final IAnnotatedSolution startSolution = optimiser.start(injector.getInstance(IOptimisationContext.class), initialSequences, inputSequences);
			if (startSolution == null) {
				throw new IllegalStateException("Unable to get starting state");
			}
			return optimiser;
		}
	}

	protected abstract List<Module> createModules(final LNGDataTransformer dataTransformer, final String stage, final UserSettings userSettings, final U stageSettings,
			final ISequences initialSequences, final ISequences inputSequences, final Collection<String> hints);

	@Override
	public IMultiStateResult run(final LNGDataTransformer dataTransformer, final SequencesContainer initialSequences, final IMultiStateResult inputState, final IProgressMonitor monitor) {

		dataTransformer.getLifecyleManager().startPhase(stage);

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
			final T optimiser = createOptimiser(dataTransformer, injector, stage, jobId, initialSequences.getSequences(), inputState.getBestSolution().getFirst());

			try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
				scope.enter();

				final JobExecutorFactory subExecutorFactory = jobExecutorFactory.withDefaultBegin(() -> {
					final ThreadLocalScopeImpl s = injector.getInstance(ThreadLocalScopeImpl.class);
					s.enter();
					return s;
				});
				try (JobExecutor jobExecutor = subExecutorFactory.begin()) {

					// Main Optimisation Loop
					while (!optimiser.isFinished()) {
						if (monitor.isCanceled()) {
							throw new OperationCanceledException();
						}
						optimiser.step(1, jobExecutor);
						monitor.worked(1);
					}
					assert optimiser.isFinished();

					final IAnnotatedSolution bestSolution = optimiser.getBestSolution();
					final ISequences bestRawSequences = optimiser.getBestRawSequences();

					final IMultiStateResult result = returnBestSolution(optimiser, bestSolution, bestRawSequences);

					if (runnerHook != null) {
						// TODO: Should really be whole multi state result
						runnerHook.reportSequences(stage, result.getBestSolution().getFirst(), dataTransformer);
					}
					return result;
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

	protected IMultiStateResult returnBestSolution(final T optimiser, final @Nullable IAnnotatedSolution bestSolution, final @Nullable ISequences bestRawSequences) {
		if (bestRawSequences != null && bestSolution != null) {
			return new MultiStateResult(bestRawSequences, LNGSchedulerJobUtils.extractOptimisationAnnotations(bestSolution));
		} else {
			throw new RuntimeException("Unable to optimise");
		}
	}

	protected void addDefaultModules(final List<Module> modules, final LNGDataTransformer dataTransformer, final String stage, final UserSettings userSettings, @NonNull final U stageSettings,
			final ISequences initialSequences, final ISequences inputSequences, final Collection<String> hints) {

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();
		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputSequences));
		modules.add(new PhaseOptimisationDataModule());

		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, stageSettings.getConstraintAndFitnessSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));

		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
	}

}
