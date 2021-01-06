/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.multisimilarity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.concurrent.CleanableExecutorService;
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
import com.mmxlabs.models.lng.transformer.inject.modules.PhaseOptimisationDataModule;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.fitness.IMultiObjectiveFitnessEvaluator;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.optimiser.lso.multiobjective.impl.NonDominatedSolution;
import com.mmxlabs.optimiser.lso.multiobjective.impl.SimpleMultiObjectiveOptimiser;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class LNGMultiObjectiveOptimiserTransformerUnit implements ILNGStateTransformerUnit {

	private static final Logger LOG = LoggerFactory.getLogger(LNGMultiObjectiveOptimiserTransformerUnit.class);

	@NonNull
	public static IChainLink chainPool(@NonNull final ChainBuilder chainBuilder, @NonNull final String stage, @NonNull final UserSettings userSettings,
			@NonNull final LocalSearchOptimisationStage stageSettings, final int progressTicks, @NonNull final CleanableExecutorService executorService, final int... seeds) {
		final IChainLink link = new IChainLink() {

			@Override
			public IMultiStateResult run(final SequencesContainer initialSequences, final IMultiStateResult inputState, final IProgressMonitor monitor) {
				final LNGDataTransformer lDataTransformer = chainBuilder.getDataTransformer();
				lDataTransformer.getLifecyleManager().startPhase(stage);

				final IRunnerHook runnerHook = lDataTransformer.getRunnerHook();
				if (runnerHook != null) {
					runnerHook.beginStage(stage);

					final ISequences preloadedResult = runnerHook.getPrestoredSequences(stage, lDataTransformer);
					if (preloadedResult != null) {
						monitor.beginTask("", 1);
						try {
							monitor.worked(1);
							return new MultiStateResult(preloadedResult, new HashMap<>());
						} finally {
							runnerHook.endStage(stage);
							lDataTransformer.getLifecyleManager().endPhase(stage);

							monitor.done();
						}
					}
				}

				@NonNull
				final Collection<@NonNull String> hints = new HashSet<>(lDataTransformer.getHints());
				LNGTransformerHelper.updatHintsFromUserSettings(userSettings, hints);
				hints.remove(LNGTransformerHelper.HINT_CLEAN_STATE_EVALUATOR);

				monitor.beginTask("", 100 * seeds.length);
				final List<Future<IMultiStateResult>> results = new ArrayList<>(seeds.length);
				try {
					for (int i = 0; i < seeds.length; ++i) {
						final LocalSearchOptimisationStage copyStageSettings = EcoreUtil.copy(stageSettings);
						copyStageSettings.setSeed(seeds[i]);

						final int jobId = i;
						results.add(executorService.submit(() -> {
							final LNGMultiObjectiveOptimiserTransformerUnit t = new LNGMultiObjectiveOptimiserTransformerUnit(lDataTransformer, stage, jobId, userSettings, copyStageSettings,
									initialSequences.getSequences(), inputState.getBestSolution().getFirst(), hints);
							return t.run(new SubProgressMonitor(monitor, 100));
						}));
					}

					final List<NonNullPair<ISequences, Map<String, Object>>> output = new LinkedList<>();
					try {
						for (final Future<IMultiStateResult> f : results) {
							final IMultiStateResult r = f.get();
							output.addAll(r.getSolutions());

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

					if (runnerHook != null) {
						runnerHook.reportSequences(stage, output.get(0).getFirst(), lDataTransformer);
					}

					return new MultiStateResult(output.get(0), output);
				} finally {
					if (runnerHook != null) {
						runnerHook.endStage(stage);
					}
					lDataTransformer.getLifecyleManager().endPhase(stage);

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
	public static IChainLink chainPoolFake(@NonNull final ChainBuilder chainBuilder, @NonNull final String stage, @NonNull final UserSettings userSettings,
			@NonNull final LocalSearchOptimisationStage stageSettings, final int progressTicks, @NonNull final CleanableExecutorService executorService, final int... seeds) {
		final IChainLink link = new IChainLink() {

			@Override
			public IMultiStateResult run(final SequencesContainer initialSequences, final IMultiStateResult inputState, final IProgressMonitor monitor) {
				final LNGDataTransformer lDataTransformer = chainBuilder.getDataTransformer();
				lDataTransformer.getLifecyleManager().startPhase(stage);

				final IRunnerHook runnerHook = lDataTransformer.getRunnerHook();
				if (runnerHook != null) {
					runnerHook.beginStage(stage);

					final ISequences preloadedResult = runnerHook.getPrestoredSequences(stage, lDataTransformer);
					if (preloadedResult != null) {
						monitor.beginTask("", 1);
						try {
							monitor.worked(1);
							return new MultiStateResult(preloadedResult, new HashMap<>());
						} finally {
							runnerHook.endStage(stage);
							lDataTransformer.getLifecyleManager().endPhase(stage);

							monitor.done();
						}
					}
				}

				@NonNull
				final Collection<@NonNull String> hints = new HashSet<>(lDataTransformer.getHints());
				LNGTransformerHelper.updatHintsFromUserSettings(userSettings, hints);
				hints.remove(LNGTransformerHelper.HINT_CLEAN_STATE_EVALUATOR);

				monitor.beginTask("", 100 * seeds.length);
				final List<Future<IMultiStateResult>> results = new ArrayList<>(seeds.length);
				try {
					for (int i = 0; i < seeds.length; ++i) {
						final LocalSearchOptimisationStage copyStageSettings = EcoreUtil.copy(stageSettings);
						copyStageSettings.setSeed(seeds[i]);
						copyStageSettings.getAnnealingSettings().setIterations(copyStageSettings.getAnnealingSettings().getIterations() / 2);

						final int jobId = i;
						results.add(executorService.submit(() -> {
							final LNGMultiObjectiveOptimiserTransformerUnit t = new LNGMultiObjectiveOptimiserTransformerUnit(lDataTransformer, stage, jobId, userSettings, copyStageSettings,
									initialSequences.getSequences(), inputState.getBestSolution().getFirst(), hints);
							t.run(new SubProgressMonitor(monitor, 100));
							return inputState;
						}));
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

					return inputState;

				} finally {
					if (runnerHook != null) {
						runnerHook.endStage(stage);
					}
					lDataTransformer.getLifecyleManager().endPhase(stage);

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
	private final SimpleMultiObjectiveOptimiser optimiser;

	@NonNull
	private final String stage;

	private final int jobID;

	public LNGMultiObjectiveOptimiserTransformerUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final String stage, final int jobID, @NonNull final UserSettings userSettings,
			@NonNull final LocalSearchOptimisationStage stageSettings, @NonNull final ISequences initialSequences, @NonNull final ISequences inputSequences,
			@NonNull final Collection<@NonNull String> hints) {
		this.dataTransformer = dataTransformer;
		this.stage = stage;
		this.jobID = jobID;

		final Collection<@NonNull IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		final List<Module> modules = new LinkedList<>();

		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputSequences));
		modules.add(new PhaseOptimisationDataModule());
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, stageSettings.getConstraintAndFitnessSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_AnnealingSettingsModule(stageSettings.getSeed(), stageSettings.getAnnealingSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_OptimisationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGOptimisationModule(), services, IOptimiserInjectorService.ModuleType.Module_Optimisation, hints));
		modules.add(new Module() {

			@Override
			public void configure(Binder binder) {

			}

			@Provides
			@Singleton
			SimpleMultiObjectiveOptimiser buildMultiObjectiveOptimiser(@NonNull final Injector injector, @NonNull final IOptimisationContext context, @NonNull final ISequencesManipulator manipulator,
					@NonNull final IMoveGenerator moveGenerator, @NonNull final IMultiObjectiveFitnessEvaluator fitnessEvaluator,
					@Named(LocalSearchOptimiserModule.LSO_NUMBER_OF_ITERATIONS) final int numberOfIterations,
					@Named(LocalSearchOptimiserModule.MULTIOBJECTIVE_OBJECTIVE_NAMES) final List<String> objectiveNames, @NonNull final List<IConstraintChecker> constraintCheckers,
					@NonNull final List<IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers, @NonNull final List<IEvaluationProcess> evaluationProcesses,
					@Named(LocalSearchOptimiserModule.RANDOM_SEED) final long randomSeed) {
				final List<IFitnessComponent> objectives = fitnessEvaluator.getFitnessComponents().stream().filter(f -> objectiveNames.contains(f.getName())).collect(Collectors.toList());
				assert (objectives.size() == objectiveNames.size());
				final SimpleMultiObjectiveOptimiser lso = new SimpleMultiObjectiveOptimiser(objectives, new Random(randomSeed));

				setLSO(injector, context, manipulator, moveGenerator, fitnessEvaluator, numberOfIterations, constraintCheckers, evaluatedStateConstraintCheckers, evaluationProcesses, lso, randomSeed);
				lso.setMultiObjectiveFitnessEvaluator(fitnessEvaluator);
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

			@Provides
			@Named(LocalSearchOptimiserModule.MULTIOBJECTIVE_OBJECTIVE_FITNESS_COMPONENTS)
			List<IFitnessComponent> buildSimpleMultiObjectiveLSOMover(@NonNull final Injector injector, @NonNull final IMultiObjectiveFitnessEvaluator fitnessEvaluator,
					@Named(LocalSearchOptimiserModule.MULTIOBJECTIVE_OBJECTIVE_NAMES) final List<String> objectiveNames) {
				List<IFitnessComponent> multiObjectiveFitnessComponents = getMultiObjectiveFitnessComponents(fitnessEvaluator, objectiveNames);
				return multiObjectiveFitnessComponents;
			}

			private List<IFitnessComponent> getMultiObjectiveFitnessComponents(final IMultiObjectiveFitnessEvaluator fitnessEvaluator, final List<String> objectiveNames) {
				final List<IFitnessComponent> objectives = fitnessEvaluator.getFitnessComponents().stream().filter(f -> objectiveNames.contains(f.getName())).collect(Collectors.toList());
				assert (objectives.size() == objectiveNames.size());
				return objectives;
			}

		});
		injector = dataTransformer.getInjector().createChildInjector(modules);
		final IRunnerHook runnerHook = dataTransformer.getRunnerHook();
		if (runnerHook != null) {
			runnerHook.beginStageJob(stage, jobID, injector);
		}

		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();
			TypeLiteral<List<IFitnessComponent>> list = new TypeLiteral<List<IFitnessComponent>>() {
			};
			List<IFitnessComponent> fitnessComponents = injector.getInstance(Key.get(list, Names.named(LocalSearchOptimiserModule.MULTIOBJECTIVE_OBJECTIVE_FITNESS_COMPONENTS)));
			Long randomSeed = injector.getInstance(Key.get(Long.class, Names.named(LocalSearchOptimiserModule.RANDOM_SEED)));

			optimiser = injector.getInstance(SimpleMultiObjectiveOptimiser.class);

			optimiser.setProgressMonitor(new NullOptimiserProgressMonitor());
			optimiser.init();

			injector.injectMembers(optimiser);

			final IAnnotatedSolution startSolution = optimiser.start(injector.getInstance(IOptimisationContext.class),
					injector.getInstance(Key.get(ISequences.class, Names.named(OptimiserConstants.SEQUENCE_TYPE_INITIAL))), inputSequences);
			if (startSolution == null) {
				throw new IllegalStateException("Unable to get starting state");
			}
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

				List<NonDominatedSolution> sortedArchive = optimiser.getSortedArchive(true);

				final List<NonNullPair<ISequences, Map<String, Object>>> solutions = sortedArchive.stream() //
						.distinct() //
						.map(r -> new NonNullPair<ISequences, Map<String, Object>>(r.getSequences(), new HashMap<>())) //
						.collect(Collectors.toList());

				if (bestRawSequences != null && bestSolution != null) {
					return new MultiStateResult(new NonNullPair<>(bestRawSequences, new HashMap<>()), solutions);
				} else {
					throw new RuntimeException("Unable to optimise");
				}
			} finally {
				if (runnerHook != null) {
					runnerHook.endStageJob(stage, jobID, injector);
				}
				monitor.done();
			}
		}
	}
}
