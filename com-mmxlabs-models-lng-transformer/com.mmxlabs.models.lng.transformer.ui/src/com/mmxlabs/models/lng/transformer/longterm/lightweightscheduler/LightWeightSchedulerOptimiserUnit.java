/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
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

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.longterm.CargoVesselRestrictionsMatrixProducer;
import com.mmxlabs.models.lng.transformer.longterm.ICargoToCargoCostCalculator;
import com.mmxlabs.models.lng.transformer.longterm.ICargoVesselRestrictionsMatrixProducer;
import com.mmxlabs.models.lng.transformer.longterm.ILongTermMatrixOptimiser;
import com.mmxlabs.models.lng.transformer.longterm.SequencesToPortSlotsUtils;
import com.mmxlabs.models.lng.transformer.longterm.SimpleCargoToCargoCostCalculator;
import com.mmxlabs.models.lng.transformer.longterm.metaheuristic.TabuLightWeightSequenceOptimiser;
import com.mmxlabs.models.lng.transformer.longterm.webservice.WebserviceLongTermMatrixOptimiser;
import com.mmxlabs.models.lng.transformer.multisimilarity.LNGMultiObjectiveOptimiserTransformerUnit;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.FollowersAndPrecedersProviderImpl;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashSetLongTermSlotsEditor;

public class LightWeightSchedulerOptimiserUnit {

	private static final Logger LOG = LoggerFactory.getLogger(LightWeightSchedulerOptimiserUnit.class);

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final Injector injector;

	private final Map<Thread, LightweightSchedulerOptimiser> threadCache = new ConcurrentHashMap<>(100);

	private LNGScenarioModel initialScenario;

	@SuppressWarnings("null")
	public LightWeightSchedulerOptimiserUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final UserSettings userSettings,
			@NonNull final ConstraintAndFitnessSettings constainAndFitnessSettings, @NonNull final ExecutorService executorService, @NonNull final ISequences initialSequences,
			LNGScenarioModel initialScenario, @NonNull final IMultiStateResult inputState, @NonNull final Collection<String> hints) {
		this.dataTransformer = dataTransformer;
		this.initialScenario = initialScenario;
		
		CharterInMarket charterInMarket = initialScenario.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().get(0);

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		final List<Module> modules = new LinkedList<>();
		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputState.getBestSolution().getFirst()));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, constainAndFitnessSettings), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));

		modules.add(new LightWeightSchedulerModule(threadCache, charterInMarket, dataTransformer));
//		modules.add(new AbstractModule() {
//			@Override
//			protected void configure() {
//				bind(IFollowersAndPreceders.class).to(FollowersAndPrecedersProviderImpl.class).in(Singleton.class);
//				HashSetLongTermSlotsEditor longTermSlotEditor = new HashSetLongTermSlotsEditor();
//				bind(ILongTermSlotsProvider.class).toInstance(longTermSlotEditor);
//				bind(ILongTermSlotsProviderEditor.class).toInstance(longTermSlotEditor);
//				WebserviceLongTermMatrixOptimiser matrixOptimiser = new WebserviceLongTermMatrixOptimiser();
//				bind(ILongTermMatrixOptimiser.class).toInstance(matrixOptimiser);
//				bind(ICargoToCargoCostCalculator.class).to(SimpleCargoToCargoCostCalculator.class);
//				bind(ICargoVesselRestrictionsMatrixProducer.class).to(CargoVesselRestrictionsMatrixProducer.class);
//				bind(ILightWeightSequenceOptimiser.class).to(TabuLightWeightSequenceOptimiser.class);
//			}
//
//			@Provides
//			private LightweightSchedulerOptimiser providePerThreadBagMover(@NonNull final Injector injector) {
//
//				LightweightSchedulerOptimiser lightweightSchedulerOptimiser = threadCache.get(Thread.currentThread());
//				if (lightweightSchedulerOptimiser == null) {
//					lightweightSchedulerOptimiser = new LightweightSchedulerOptimiser();
//					injector.injectMembers(lightweightSchedulerOptimiser);
//					threadCache.put(Thread.currentThread(), lightweightSchedulerOptimiser);
//				}
//				return lightweightSchedulerOptimiser;
//			}
//			
//		});

		injector = dataTransformer.getInjector().createChildInjector(modules);
	}

	@NonNull
	public static IChainLink chainPool(@NonNull final ChainBuilder chainBuilder, @NonNull final LNGScenarioToOptimiserBridge optimiserBridge, @NonNull final String stage, @NonNull final UserSettings userSettings,
			@NonNull final CleanStateOptimisationStage stageSettings, final int progressTicks, @NonNull final ExecutorService executorService, final int... seeds) {
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

				@NonNull
				final Collection<@NonNull String> hints = new HashSet<>(dataTransformer.getHints());
				if (userSettings.isGenerateCharterOuts()) {
					hints.add(LNGTransformerHelper.HINT_GENERATE_CHARTER_OUTS);
				} else {
					hints.remove(LNGTransformerHelper.HINT_GENERATE_CHARTER_OUTS);
				}
				hints.remove(LNGTransformerHelper.HINT_CLEAN_STATE_EVALUATOR);

				monitor.beginTask("", 100 * seeds.length);
				final List<Future<IMultiStateResult>> results = new ArrayList<>(seeds.length);
				try {
					for (int i = 0; i < seeds.length; ++i) {
						final CleanStateOptimisationStage copyStageSettings = EcoreUtil.copy(stageSettings);
						copyStageSettings.setSeed(seeds[i]);
						results.add(executorService.submit(() -> {
							final LightWeightSchedulerOptimiserUnit t  = new LightWeightSchedulerOptimiserUnit(dataTransformer, userSettings, copyStageSettings.getConstraintAndFitnessSettings(), executorService, initialSequences.getSequences(), (LNGScenarioModel) (optimiserBridge.getOptimiserScenario().getScenario()), inputState, hints);
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
						runnerHook.reportSequences(stage, output.get(0).getFirst(), dataTransformer);
					}

					return new MultiStateResult(output.get(0), output);
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

	public IMultiStateResult run(@NonNull final IProgressMonitor monitor) {
			try {

				@NonNull
				ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();
				ILongTermSlotsProviderEditor longTermSlotsProviderEditor = injector.getInstance(ILongTermSlotsProviderEditor.class);
				IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
				Collection<IPortSlot> allPortSlots = SequencesToPortSlotsUtils.getAllPortSlots(dataTransformer.getOptimisationData().getSequenceElements(), portSlotProvider);
				allPortSlots.forEach(e -> longTermSlotsProviderEditor.addLongTermSlot(e));
				monitor.beginTask("Generate solutions", 100);
				CharterInMarket charterInMarket = initialScenario.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().get(0);

				final LightweightSchedulerOptimiser calculator = injector.getInstance(LightweightSchedulerOptimiser.class);
				Pair<ISequences, Long> result = calculator.optimise(dataTransformer, charterInMarket);

					final List<NonNullPair<ISequences, Map<String, Object>>> solutions = new LinkedList<>();
					solutions.add(new NonNullPair<ISequences, Map<String, Object>>(result.getFirst(), new HashMap<>()));

					return new MultiStateResult(solutions.get(0), solutions);
				} finally {
					monitor.done();
				}
	}

}
