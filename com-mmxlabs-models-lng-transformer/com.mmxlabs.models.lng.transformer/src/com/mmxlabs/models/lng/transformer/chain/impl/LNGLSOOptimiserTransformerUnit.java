/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.models.lng.parameters.OptimisationMode;
import com.mmxlabs.models.lng.parameters.StrategicLocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGOptimisationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_AnnealingSettingsModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.inject.modules.PhaseOptimisationDataModule;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScopeImpl;
import com.mmxlabs.optimiser.lso.impl.DefaultLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.SingleThreadLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.scheduler.optimiser.lso.ConstrainedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.OptimiserInjectorServiceMaker;

@NonNullByDefault
public class LNGLSOOptimiserTransformerUnit extends AbstractLNGOptimiserTransformerUnit<SingleThreadLocalSearchOptimiser, StrategicLocalSearchOptimisationStage> {

	private static final Logger LOG = LoggerFactory.getLogger(LNGLSOOptimiserTransformerUnit.class);

	public LNGLSOOptimiserTransformerUnit(final String stage, final UserSettings userSettings, final StrategicLocalSearchOptimisationStage stageSettings, final JobExecutorFactory jobExecutorFactory,
			final int progressTicks) {
		super(stage, userSettings, stageSettings, jobExecutorFactory, progressTicks, SingleThreadLocalSearchOptimiser.class);
	}

	@Override
	public IMultiStateResult run(final LNGDataTransformer dataTransformer, final SequencesContainer initialSequences, final IMultiStateResult inputState, final IProgressMonitor monitor) {

		dataTransformer.getLifecyleManager().startPhase(stage, dataTransformer.getHints());

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
					dataTransformer.getLifecyleManager().endPhase(stage);

					monitor.done();
				}
			}
		}

		final int numJobs = stageSettings.getCount();
		monitor.beginTask("", 100 * numJobs);
		try {
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

					final List<Future<IMultiStateResult>> results = new ArrayList<>(numJobs);

					for (int i = 0; i < numJobs; ++i) {
						final StrategicLocalSearchOptimisationStage copyStageSettings = EcoreUtil.copy(stageSettings);
						copyStageSettings.setSeed(stageSettings.getSeed() + i);

						final int jobId = i;

						// Old sub-monitor rather than new style.
						// New style can only have one submonitor active at once. Here we need n active
						// at once.
						final SubProgressMonitor subMonitor = new SubProgressMonitor(monitor, 100);
						results.add(jobExecutor.submit(() -> {
							final SingleThreadLocalSearchOptimiser optimiser = createOptimiser(dataTransformer, injector.createChildInjector(), stage, jobId, initialSequences.getSequences(),
									inputState.getBestSolution().getFirst());

							// Main Optimisation Loop
							while (!optimiser.isFinished()) {
								if (monitor.isCanceled()) {
									throw new OperationCanceledException();
								}
								optimiser.step(1, jobExecutor);
								subMonitor.worked(1);
							}
							final IAnnotatedSolution bestSolution = optimiser.getBestSolution();
							final ISequences bestRawSequences = optimiser.getBestRawSequences();

							return returnBestSolution(optimiser, bestSolution, bestRawSequences);
						}));
					}

					final List<NonNullPair<ISequences, Map<String, Object>>> output = aggregateResults(monitor, results);

					if (runnerHook != null) {
						// TODO: Should really be whole multi state result
						runnerHook.reportSequences(stage, output.get(0).getFirst(), dataTransformer);
					}

					return new MultiStateResult(output.get(0), output);
				} finally {
					if (runnerHook != null) {
						runnerHook.endStageJob(stage, 1, injector);
					}
					monitor.done();
				}
			}
		} finally {
			if (runnerHook != null) {
				runnerHook.endStage(stage);
			}
			dataTransformer.getLifecyleManager().endPhase(stage);

			monitor.done();
		}
	}

	private List<NonNullPair<ISequences, Map<String, Object>>> aggregateResults(final IProgressMonitor monitor, final List<Future<IMultiStateResult>> results) {
		final List<NonNullPair<ISequences, Map<String, Object>>> output = new LinkedList<>();
		try {
			for (final Future<IMultiStateResult> f : results) {
				final IMultiStateResult r = f.get();
				output.add(r.getBestSolution());

				// Check monitor state
				if (monitor.isCanceled()) {
					throw new OperationCanceledException();
				}
			}
		} catch (Throwable e) {
			// An exception occurred, abort!

			// Unwrap exception
			if (e instanceof ExecutionException) {
				e = e.getCause();
			}

			// Abort any other running jobs
			for (final Future<IMultiStateResult> f : results) {
				try {
					f.cancel(true);
				} catch (final Exception e2) {
					LOG.error(e2.getMessage(), e2);
				}
			}

			if (e instanceof OperationCanceledException) {
				throw (OperationCanceledException) e;
			} else {
				throw new RuntimeException(e);
			}
		}

		// Check monitor state
		if (monitor.isCanceled()) {
			throw new OperationCanceledException();
		}

		// Sort results
		Collections.sort(output, new Comparator<NonNullPair<ISequences, Map<String, Object>>>() {

			@Override
			public int compare(final NonNullPair<ISequences, Map<String, Object>> o1, final NonNullPair<ISequences, Map<String, Object>> o2) {
				final long a = getTotal(o1.getSecond());
				final long b = getTotal(o2.getSecond());
				return Long.compare(a, b);
			}

			long getTotal(final Map<String, Object> m) {
				if (m == null) {
					return 0L;
				}
				final Map<String, Long> currentFitnesses = (Map<String, Long>) m.get(OptimiserConstants.G_AI_fitnessComponents);
				if (currentFitnesses == null) {
					return 0L;
				}
				long sum = 0L;
				for (final Long l : currentFitnesses.values()) {
					if (l != null) {
						sum += l.longValue();
					}
				}
				return sum;

			}
		});

		if (output.isEmpty()) {
			throw new IllegalStateException("No results generated");
		}
		return output;
	}

	@Override
	protected List<Module> createModules(final LNGDataTransformer dataTransformer, final String stage, final UserSettings userSettings, final StrategicLocalSearchOptimisationStage stageSettings,
			final ISequences initialSequences, final ISequences inputSequences, final Collection<String> hints) {

		final Collection<IOptimiserInjectorService> services = new LinkedList<>(dataTransformer.getModuleServices());

		final List<Module> modules = new LinkedList<>();

		if (userSettings.getMode() == OptimisationMode.STRATEGIC) {
			// Enable Guided moves for strategic mode
			final IOptimiserInjectorService strategicMoveModule = OptimiserInjectorServiceMaker.begin()
					.withModuleOverrideBindNamedInstance(IOptimiserInjectorService.ModuleType.Module_Optimisation, ConstrainedMoveGenerator.LSO_MOVES_GUIDED, boolean.class, Boolean.TRUE).make();
			services.add(strategicMoveModule);
		}
		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputSequences));
		modules.add(new PhaseOptimisationDataModule());
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, stageSettings.getConstraintAndFitnessSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_AnnealingSettingsModule(stageSettings.getSeed(), stageSettings.getAnnealingSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_OptimisationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(CollectionsUtil.makeLinkedList(new LNGOptimisationModule(), new LocalSearchOptimiserModule()), services,
				IOptimiserInjectorService.ModuleType.Module_Optimisation, hints));
		return modules;
	}
}
