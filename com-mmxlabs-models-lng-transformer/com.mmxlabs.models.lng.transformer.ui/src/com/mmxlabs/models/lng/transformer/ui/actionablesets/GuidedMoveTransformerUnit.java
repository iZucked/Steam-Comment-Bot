/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.actionablesets;

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
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.actionplan.LNGParameters_ActionPlanSettingsModule;
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
import com.mmxlabs.models.lng.transformer.inject.modules.PhaseOptimisationDataModule;
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
import com.mmxlabs.scheduler.optimiser.actionableset.GuidedMoveMultipleSolutionOptimiser;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.FitnessCalculator;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.LookupManager;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class GuidedMoveTransformerUnit implements ILNGStateTransformerUnit {

	private static final Logger LOG = LoggerFactory.getLogger(GuidedMoveTransformerUnit.class);

	@NonNull
	public static IChainLink chainPool(@NonNull final ChainBuilder chainBuilder, @NonNull final String stage, @NonNull final UserSettings userSettings,
			@NonNull final LocalSearchOptimisationStage stageSettings, final int progressTicks, @NonNull final JobExecutorFactory jobExecutorFactory, final int... seeds) {
		final IChainLink link = new IChainLink() {

			@Override
			public IMultiStateResult run(final SequencesContainer initialSequences, final IMultiStateResult inputState, final IProgressMonitor monitor) {
				final LNGDataTransformer dataTransformer = chainBuilder.getDataTransformer();
				dataTransformer.getLifecyleManager().startPhase(stage);

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

				@NonNull
				final Collection<@NonNull String> hints = new HashSet<>(dataTransformer.getHints());
				LNGTransformerHelper.updateHintsFromUserSettings(userSettings, hints);

				monitor.beginTask("", 100 * seeds.length);
				try {
					final LocalSearchOptimisationStage copyStageSettings = EcoreUtil.copy(stageSettings);
					copyStageSettings.setSeed(seeds[0]);

					final int jobId = 0;
					final GuidedMoveTransformerUnit t = new GuidedMoveTransformerUnit(dataTransformer, stage, jobId, userSettings, copyStageSettings, initialSequences.getSequences(),
							inputState.getBestSolution().getFirst(), jobExecutorFactory, hints);
					IMultiStateResult result = t.run(new SubProgressMonitor(monitor, 100));

					// final List<NonNullPair<ISequences, Map<String, Object>>> output = new LinkedList<>();

					// Check monitor state
					if (monitor.isCanceled()) {
						throw new OperationCanceledException();
					}

					// // Sort results
					// Collections.sort(output, new Comparator<NonNullPair<ISequences, Map<String, Object>>>() {
					//
					// @Override
					// public int compare(final NonNullPair<ISequences, Map<String, Object>> o1, final NonNullPair<ISequences, Map<String, Object>> o2) {
					// final long a = getTotal(o1.getSecond());
					// final long b = getTotal(o2.getSecond());
					// return Long.compare(a, b);
					// }
					//
					// long getTotal(final Map<String, Object> m) {
					// if (m == null) {
					// return 0L;
					// }
					// final Map<String, Long> currentFitnesses = (Map<String, Long>) m.get(OptimiserConstants.G_AI_fitnessComponents);
					// if (currentFitnesses == null) {
					// return 0L;
					// }
					// long sum = 0L;
					// for (final Long l : currentFitnesses.values()) {
					// if (l != null) {
					// sum += l.longValue();
					// }
					// }
					// return sum;
					//
					// }
					// });

					if (result == null) {
						throw new IllegalStateException("No results generated");
					}

					if (runnerHook != null) {
						runnerHook.reportSequences(stage, result.getBestSolution().getFirst(), dataTransformer);
					}

					return result;
				} finally {
					if (runnerHook != null) {
						runnerHook.endStage(stage);
					}
					dataTransformer.getLifecyleManager().endPhase(stage);

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

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final Injector injector;

	@NonNull
	private final String stage;

	private JobExecutorFactory jobExecutorFactory;

	public GuidedMoveTransformerUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final String stage, final int jobID, @NonNull final UserSettings userSettings,
			@NonNull final LocalSearchOptimisationStage stageSettings, @NonNull final ISequences initialSequences, @NonNull final ISequences inputSequences, JobExecutorFactory jobExecutorFactory,
			@NonNull final Collection<@NonNull String> hints) {
		this.dataTransformer = dataTransformer;
		this.stage = stage;
		this.jobExecutorFactory = jobExecutorFactory;

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		final List<Module> modules = new LinkedList<>();
		modules.add(new AbstractModule() {

			@Override
			protected void configure() {
				// binder().requireExplicitBindings();

			}
		});

		ActionPlanOptimisationStage actionPlanOptimisationStage = ParametersFactory.eINSTANCE.createActionPlanOptimisationStage();
		{
			actionPlanOptimisationStage.setConstraintAndFitnessSettings(EcoreUtil.copy(stageSettings.getConstraintAndFitnessSettings()));
			actionPlanOptimisationStage.setInRunEvaluations(1000);
			actionPlanOptimisationStage.setSearchDepth(1_000);
			actionPlanOptimisationStage.setTotalEvaluations(1_000_000);
		}
		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputSequences));
		modules.add(new PhaseOptimisationDataModule());
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, stageSettings.getConstraintAndFitnessSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_ActionPlanSettingsModule(actionPlanOptimisationStage), services,
				IOptimiserInjectorService.ModuleType.Module_OptimisationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new CreateActionableSetPlanModule(), services, IOptimiserInjectorService.ModuleType.Module_Optimisation, hints));

		modules.add(new AbstractModule() {

			@Override
			protected void configure() {

				bind(EvaluationHelper.class).in(ThreadLocalScope.class);

				bind(FitnessCalculator.class).in(ThreadLocalScope.class);
				bind(GuidedMoveMultipleSolutionOptimiser.class).in(ThreadLocalScope.class);
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
		try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
			scope.enter();


			final JobExecutorFactory subExecutorFactory = jobExecutorFactory.withDefaultBegin(() -> {
				final ThreadLocalScopeImpl s = injector.getInstance(ThreadLocalScopeImpl.class);
				s.enter();
				return s;
			});
			try (JobExecutor jobExecutor = subExecutorFactory.begin()) {

				final GuidedMoveMultipleSolutionOptimiser instance = injector.getInstance(GuidedMoveMultipleSolutionOptimiser.class);
				final ISequences inputRawSequences = injector.getInstance(Key.get(ISequences.class, Names.named(OptimiserConstants.SEQUENCE_TYPE_INPUT)));
				final List<ISequences> result = instance.optimise(inputRawSequences, new IProgressReporter() {

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
				List<NonNullPair<ISequences, Map<String, Object>>> solutions = new LinkedList<>();
				NonNullPair<ISequences, Map<String, Object>> best = null;
				if (result != null) {
					solutions = result.stream() //
							.distinct() //
							.map(r -> new NonNullPair<ISequences, Map<String, Object>>(r, new HashMap<>())) //
							.collect(Collectors.toList());
					best = solutions.get(0);
				} else {
					NonNullPair<ISequences, Map<String, Object>> initial = new NonNullPair<>(inputRawSequences, new HashMap<>());
					solutions.add(initial);
				}
				assert best != null;
				return new MultiStateResult(best, solutions.subList(0, 100));
			} catch (final Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} finally {
				monitor.done();
			}
		}
	}
}
