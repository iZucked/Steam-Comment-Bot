/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.ILNGStateTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.IMultiStateResult;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGOptimisationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_AnnealingSettingsModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class LNGLSOOptimiserTransformerUnit implements ILNGStateTransformerUnit {

	private static final Logger LOG = LoggerFactory.getLogger(LNGLSOOptimiserTransformerUnit.class);

	@NonNull
	public static IChainLink chain(@NonNull final ChainBuilder chainBuilder, @NonNull final String phase, @NonNull final UserSettings userSettings, @NonNull LocalSearchOptimisationStage stageSettings,
			final int progressTicks) {
		final IChainLink link = new IChainLink() {

			private LNGLSOOptimiserTransformerUnit t;

			@Override
			public IMultiStateResult run(final IProgressMonitor monitor) {
				if (t == null) {
					throw new IllegalStateException("#init has not been called");
				}
				return t.run(monitor);
			}

			@Override
			public void init(SequencesContainer initialSequences, final IMultiStateResult inputState) {

				final LNGDataTransformer dt = chainBuilder.getDataTransformer();
				@NonNull
				Collection<@NonNull String> hints = new HashSet<>(dt.getHints());
				if (userSettings.isGenerateCharterOuts()) {
					hints.add(LNGTransformerHelper.HINT_GENERATE_CHARTER_OUTS);
				} else {
					hints.remove(LNGTransformerHelper.HINT_GENERATE_CHARTER_OUTS);
				}
				hints.remove(LNGTransformerHelper.HINT_CLEAN_STATE_EVALUATOR);

				t = new LNGLSOOptimiserTransformerUnit(dt, phase, userSettings, stageSettings, initialSequences.getSequences(), inputState.getBestSolution().getFirst(), hints);
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
	public static IChainLink chainPool(@NonNull final ChainBuilder chainBuilder, @NonNull final String phase, @NonNull final UserSettings userSettings,
			@NonNull LocalSearchOptimisationStage stageSettings, final int progressTicks, @NonNull final ExecutorService executorService, final int... seeds) {
		final IChainLink link = new IChainLink() {

			private LNGLSOOptimiserTransformerUnit[] t;

			class MyRunnable implements Callable<IMultiStateResult> {

				LNGLSOOptimiserTransformerUnit t;
				IProgressMonitor m;

				public MyRunnable(final LNGLSOOptimiserTransformerUnit t, final IProgressMonitor monitor, final int ticks) {
					this.t = t;
					this.m = new SubProgressMonitor(monitor, ticks);
				}

				@Override
				public IMultiStateResult call() throws OperationCanceledException {
					return t.run(m);
				}
			}

			@Override
			public IMultiStateResult run(final IProgressMonitor monitor) {
				try {
					if (t == null) {
						throw new IllegalStateException("#init has not been called");
					}

					monitor.beginTask("", 100 * seeds.length);

					final List<Future<IMultiStateResult>> results = new ArrayList<>(seeds.length);
					for (int i = 0; i < seeds.length; ++i) {
						results.add(executorService.submit(new MyRunnable(t[i], monitor, 100)));
					}

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

					return new MultiStateResult(output.get(0), output);
				} finally {
					monitor.done();
				}
			}

			@Override
			public void init(SequencesContainer initialSequences, final IMultiStateResult inputState) {
				final LNGDataTransformer dt = chainBuilder.getDataTransformer();
				t = new LNGLSOOptimiserTransformerUnit[seeds.length];

				@NonNull
				Collection<@NonNull String> hints = new HashSet<>(dt.getHints());
				if (userSettings.isGenerateCharterOuts()) {
					hints.add(LNGTransformerHelper.HINT_GENERATE_CHARTER_OUTS);
				} else {
					hints.remove(LNGTransformerHelper.HINT_GENERATE_CHARTER_OUTS);
				}
				hints.remove(LNGTransformerHelper.HINT_CLEAN_STATE_EVALUATOR);

				for (int i = 0; i < seeds.length; ++i) {
					final LocalSearchOptimisationStage copyStageSettings = EcoreUtil.copy(stageSettings);
					copyStageSettings.setSeed(seeds[i]);
					t[i] = new LNGLSOOptimiserTransformerUnit(dt, phase, userSettings, copyStageSettings, initialSequences.getSequences(), inputState.getBestSolution().getFirst(), hints);
				}
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
				return t[0].getInputState();
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
	private final LocalSearchOptimiser optimiser;

	@NonNull
	private final IMultiStateResult inputState;

	@NonNull
	private final String phase;

	public LNGLSOOptimiserTransformerUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull String phase, @NonNull final UserSettings userSettings,
			@NonNull LocalSearchOptimisationStage stageSettings, @NonNull ISequences initialSequences, @NonNull final ISequences inputSequences, @NonNull final Collection<@NonNull String> hints) {
		this.dataTransformer = dataTransformer;
		this.phase = phase;

		final Collection<@NonNull IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		final List<Module> modules = new LinkedList<>();

		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputSequences));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, stageSettings.getConstraintAndFitnessSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_AnnealingSettingsModule(stageSettings.getSeed(), stageSettings.getAnnealingSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_OptimisationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGOptimisationModule(), services, IOptimiserInjectorService.ModuleType.Module_Optimisation, hints));

		injector = dataTransformer.getInjector().createChildInjector(modules);
		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();

			optimiser = injector.getInstance(LocalSearchOptimiser.class);
			optimiser.setProgressMonitor(new NullOptimiserProgressMonitor());
			optimiser.init();
			final IRunnerHook runnerHook = dataTransformer.getRunnerHook();
			if (runnerHook != null) {
				runnerHook.beginPhase(phase, injector);
			}

			final IAnnotatedSolution startSolution = optimiser.start(injector.getInstance(IOptimisationContext.class),
					injector.getInstance(Key.get(ISequences.class, Names.named(OptimiserConstants.SEQUENCE_TYPE_INITIAL))), inputSequences);
			if (startSolution == null) {
				throw new IllegalStateException("Unable to get starting state");
			}
			inputState = new MultiStateResult(inputSequences, LNGSchedulerJobUtils.extractOptimisationAnnotations(startSolution));
		}
	}

	@NonNull
	public LNGDataTransformer getDataTransformer() {
		return dataTransformer;
	}

	@NonNull
	public Injector getInjector() {
		return injector;
	}

	@Override
	public IMultiStateResult run(@NonNull final IProgressMonitor monitor) {
		final IRunnerHook runnerHook = dataTransformer.getRunnerHook();
		if (runnerHook != null) {
			runnerHook.beginPhase(phase, injector);
			final ISequences preloadedResult = runnerHook.getPrestoredSequences(phase);
			if (preloadedResult != null) {
				monitor.beginTask("", 1);
				try {
					monitor.worked(1);
					return new MultiStateResult(preloadedResult, new HashMap<>());
				} finally {
					runnerHook.endPhase(phase);
					monitor.done();
				}
			}
		}

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

				if (runnerHook != null) {
					runnerHook.reportSequences(phase, bestRawSequences);
					runnerHook.endPhase(phase);
				}

				if (bestRawSequences != null && bestSolution != null) {
					return new MultiStateResult(bestRawSequences, LNGSchedulerJobUtils.extractOptimisationAnnotations(bestSolution));
				} else {
					throw new RuntimeException("Unable to optimise");
				}
			} finally {
				monitor.done();
			}
		}
	}

	public IMultiStateResult getInputState() {
		return inputState;
	}

}
